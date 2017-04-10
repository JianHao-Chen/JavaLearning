package tomcatSrc.cluster;

import java.nio.channels.SelectionKey;

/**
 * <一>  NioSender
 *  
 *  【背景】
 *   当ClusterManager需要把信息(SessionMessage)发送给其他节点时,中间会经过
 *   Interceptor链和ChannelCoordinator,最后调用的是ParallelNioSender的
 *      sendMessage(Member[] destination, ChannelMessage msg)方法,
 *   其中,对应每个destination,都会有一个 NioSender来负责。
 *   
 *  【使用】
 *   NioSender的使用有以下几步:
 *   
 *   <1> connect
 *          readbuf = getReadBuffer();  
 *          writebuf = getWriteBuffer();
 *          InetSocketAddress addr =    
                new InetSocketAddress(getAddress(),getPort());
 *          socketChannel = SocketChannel.open();
 *          socketChannel.configureBlocking(false);
 *          
 *          socketChannel.connect(addr);
 *          如果connect成功,则向通道注册写事件,否则注册connect事件.
 *          socketChannel.register(getSelector(), SelectionKey.OP_CONNECT, this);
 *  
 *   <2> select 
 *   
 *          int selectedKeys = selector.select(selectTimeOut);
 *          Iterator it = selector.selectedKeys().iterator();
 *          while (it.hasNext()) {
 *              SelectionKey sk = (SelectionKey) it.next();
 *              it.remove();
 *              int readyOps = sk.readyOps();
 *              sk.interestOps(sk.interestOps() & ~readyOps);
 *              
 *              NioSender sender = (NioSender) sk.attachment();
 *              sender.process(sk,waitForAck)
 *          }
 *          
 *   <3> NioSender的process()方法:
 *          
 *          if ( key.isConnectable() ) {
 *              if ( socketChannel.finishConnect() ) {
 *                  completeConnect();
 *                  key.interestOps(key.interestOps() | SelectionKey.OP_WRITE);
 *              }
 *              else {
 *                  //wait for the connection to finish
 *                  key.interestOps(key.interestOps() | SelectionKey.OP_CONNECT);
 *              }
 *          }
 *          else if ( key.isWritable() ){
 *              write(key);
 *              ....
 *          }
 *
 *  <4> write()方法
 *          socketChannel.write(writebuf);
 *
 *
 *
 *
 *
 *  <二>  NioReceiver
 *  
 *   【背景】
 *      NioReceiver作为GroupChannel的接收器,用于接收信息。
 *      
 *   【使用】
 *      NioReceiver的使用有以下几步:
 *      
 *      <1> select
 *      
 *          int n = selector.select(getSelectorTimeout());
 *          Iterator it = selector.selectedKeys().iterator();
 *          while (it.hasNext()) {
 *              SelectionKey key = (SelectionKey) it.next();
 *              if (key.isAcceptable()) {
 *                  ServerSocketChannel server = (ServerSocketChannel) key.channel();
 *                  SocketChannel channel = server.accept();
 *                  channel.set(XXX).....
 *                  
 *                  registerChannel(selector,channel,SelectionKey.OP_READ,attach);
 *              }
 *              if (key.isReadable()) {
 *                  readDataFromSocket(key);
 *              }
 *          }   
 *      
 *      <2> readDataFromSocket
 *          生成 NioReplicationTask ,把它放入 ThreadPoolExecutor运行。
 *          NioReplicationTask的run()方法:
 *           
 *              buffer = ByteBuffer.allocate(getRxBufSize());
 *              drainChannel();
 *      
 *      <3> drainChannel()方法:
 *      
 *          SocketChannel channel = (SocketChannel) key.channel();
 *          buffer.clear();
 *          // loop while data available, channel is non-blocking
 *          while ((count = channel.read (buffer)) > 0) {
 *              buffer.flip();      // make buffer readable
 *              reader.append(buffer,count,false);
 *              buffer.clear();     // make buffer empty
 *              
 *              //do we have at least one package?
                if ( reader.hasPackage() )
                    break;
 *          }
 *          
 *          int pkgcnt = reader.count();
 *          //将byte数据转换为ChannelMessage
 *          ChannelMessage[] msgs = reader.execute()    
 *      
 *      
 *---------------------------------------------------------------------
 *
 *  【tribes组件使用 ACK】
 *  
 *  <1> 设置
 *  
 *      在SimpleTcpCluster中设置 channelSendOptions 为:
 *         channelSendOptions = Channel.SEND_OPTIONS_USE_ACK | Channel.SEND_OPTIONS_SYNCHRONIZED_ACK;
 *      然后,在MessageDispatch15Interceptor的sendMessage()方法时,会直接调用
 *      sendMessage()方法,而不是默认的放入线程池。这样会等到后面得到ACK才返回。
 *      
 *  <2> NioSender
 *      在NioSender处理SelectionKey的process()方法里面,在向SocketChannel写完
 *      数据以后,为读取对方返回的ACK向SocketChannel注册读事件:
 *      
 *      !!  注意 , SocketChannel 是被配置为 non blocking的    !!
 *      
 *      ...
 *      else if ( key.isWritable() ) {
 *          boolean writecomplete = write(key);
 *          if ( writecomplete ) {
 *              if ( waitForAck ) {
 *                  key.interestOps(key.interestOps() | SelectionKey.OP_READ);
 *              }
 *              else
 *                  ...
 *          }
 *      }
 *      
 *      而对于读事件的处理:
 *      
 *      else if ( key.isReadable() ) {
 *          boolean readcomplete = read(key);
 *          if ( readcomplete ) {
 *              ...
 *              return true;
 *          }
 *          else {
 *              key.interestOps(key.interestOps() | SelectionKey.OP_READ);
 *          }
 *      }
 *      
 *      
 *   <3> NioReceiver
 *       在 drainChannel()方法,把数据转换为ChannelMessage,并且已经通知Cluster
 *       及其上层组件后,会判断是否需要返回一个ACK。
 *       
 *          if (ChannelData.sendAckSync(msgs[i].getOptions()))
 *              sendAck(key,channel,Constants.ACK_COMMAND);
 *              
 *          sendAck(SelectionKey key, SocketChannel channel, byte[] command){
 *                //省略try catch
 *              ByteBuffer buf = ByteBuffer.wrap(command);
 *              int total = 0;
 *              while ( total < command.length ) {
 *                  total += channel.write(buf);
 *              }
 *          }
 *    
 *     
 *         
 *      
 */

public class NioSender_NioReceiver {

}
