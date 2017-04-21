package tomcatSrc;

/**
 *  【Keep-Alive】
 *      又称为persistent connection，或Http connection reuse。
 *      主要作用是用一个TCP连接来处理多个HTTP的请求和响应。
 *      
 *      好处是: 
 *          节省了创建和结束多个连接的时间,可以更专注于数据的传输,页面可以更快的渲染,
 *          同时也降低了服务器的负载。
 *      坏处是:
 *          影响了性能,因为在处理暂停期间,本来可以释放的资源仍旧被占用。
 * 
 * 
 *   浏览器默认是会在request的Header中加入 : connection = Keep-Alive
 *   来表示浏览器是支持使用Keep-Alive的。
 *   
 *   
 *   【Tomcat对Keep-Alive的支持】 -- 这里只分析 NIO 的实现
 *   
 *     <1> 在处理请求阶段,解析解析请求行(RequestLine),头部（Headers）之后,会对
 *         头部的一些属性进行处理,例如:
 *         
 *           (取出的头部key为"connection"的field,根据它的值是"close"还是
 *            "Keep-Alive",对 变量keepAlive赋值)
 *           MessageBytes connectionValueMB = headers.getValue("connection");
 *           ByteChunk connectionValueBC = connectionValueMB.getByteChunk();
 *           if (findBytes(connectionValueBC, Constants.CLOSE_BYTES) != -1)
 *              keepAlive = false;
 *           else if (findBytes(connectionValueBC,Constants.KEEPALIVE_BYTES) != -1)
 *              keepAlive = true;
 *              
 *     <2> keep alive的最大连接数
 *         Http11NioProcessor中有成员变量:
 *              protected int maxKeepAliveRequests = -1;
 *              
 *         maxKeepAliveRequests会在Http11NioProcessor创建的时候,被赋默认值100
 *         如果设置maxKeepAliveRequests==0或者==1,都会认为是“不使用keep-Alive”
 *         
 *     
 *     <3> 已经开启的keep alive 连接也是会被关闭的
 *         在prepareResponse()方法里面:
 *         
 *              keepAlive = keepAlive && !statusDropsConnection(statusCode);
 *              if (!keepAlive)
 *                  headers.addValue(Constants.CONNECTION).setString(Constants.CLOSE);
 *              else if (!http11 && !error)
 *                  headers.addValue(Constants.CONNECTION).setString(Constants.KEEPALIVE);
 *             
 *              这里,statusDropsConnection()方法是对从response得到statusCode进行
 *              判断,如果是(像400,500,503...这些code)就可以在Response的header里面
 *              加入 ["connection" = "close"]
 *              
 *      
 *      <4> Tomcat中socket(或者说NioChannel)的关闭
 *          
 *        (1) 情况1(keepAliveLeft==0):
 *            keepAliveLeft初始值为maxKeepAliveRequests,随后每一次的request处理,
 *            都减1,当keepAliveLeft==0,Tomcat就会关闭这个链接。
 *            
 *            从代码的角度来看,SocketProcessor中的run()方法:
 *            {
 *              ...
 *              // 将会得到closed==true
 *              boolean closed = handler.process(socket)==Handler.SocketState.CLOSED
 *              
 *              // 随后调用cancelledKey()方法
 *              socket.getPoller().cancelledKey(key, SocketStatus.ERROR, false);
 *            }
 *            
 *            
 *        (2) 情况2(keepalive的timeout,而且请求数目还未达到maxKeepAliveRequests):   
 *            
 *            当Http11ConnectionHandler处理请求完成时:
 *              // 得到的SocketState应该是 LONG
 *              SocketState state = processor.process(socket);
 *            
 *            然后Http11ConnectionHandler会做2件事:
 *            <a> 把socket对应的processor保存起来
 *            <b> 把socket封装为PollerEvent,加到Poller的events队列。
 *                (Poller会负责处理PollerEvent--> 
 *                   把socket对应的SelectionKey的interestOps置为1,表示关注读事件)
 *            
 *            
 *            而Poller线程会在run()里面调用timeout()方法,用于检查每个连接是否timeout.
 *            检查的逻辑:
 *            
 *            Set<SelectionKey> keys = selector.keys();
 *            for (Iterator<SelectionKey> iter = keys.iterator(); iter.hasNext(); ) {
 *              SelectionKey key = iter.next();
 *              try {
 *                  ...
 *                  long delta = now - ka.getLastAccess();
 *                  long timeout = ka.getTimeout();
 *                  boolean isTimedout = delta > timeout;
 *                  ...
 *                  if(isTimedout){
 *                      key.interestOps(0);
 *                      cancelledKey(key, SocketStatus.TIMEOUT,true);
 *                  }
 *              }
 *            }
 *
 *            
 *        (3) 情况3(浏览器发送EOF过来)
 *            前半部分就和普通的读取数据一样,直到读到EOF,然后从process()方法返回
 *            
 *              // state == CLOSED
 *              SocketState state = processor.process(socket);
 *            
 *            <a> Http11ConnectionHandler对state == CLOSED的处理
 *              将此socket对应的processor从connections中删除,然后保存在
 *              recycledProcessors里面。
 *            
 *            <b> SocketProcessor对state == CLOSED的处理
 *              调用 cancelledKey()方法:
 *                socket.getPoller().cancelledKey(key, SocketStatus.ERROR, false);
 *            
 *            
 *  【补充】   Poller的cancelledKey(SelectionKey key, SocketStatus status)方法
 *        
 *        KeyAttachment ka = (KeyAttachment) key.attachment();
 *        key.attach(null);
 *        
 *    (1) 处理connections中的此 Processor, 并把它放入缓存 recycledProcessors
 *        handler.release(ka.getChannel());
 *        
 *    (2) 处理 SelectionKey
 *        // 先判断这个SelectionKey是否有效:
 *          一个SelectionKey自从创建以后,就保持有效,直到:
 *          (1) 它的cancel()方法被调用
 *          (2) 它的channel被关闭了
 *          (3) 它的selector被关闭了
 *        // 如果SelectionKey有效,就调用SelectionKey的cancel()
 *        // cancel()方法造成这个 SelectionKey被加入到selector的cancelled-key集合
 *        if (key.isValid()) key.cancel();
 *        
 *    (3) 调用 SocketChannel的close()方法
 *        if (key.channel().isOpen()) 
 *        try {
 *          key.channel().close();
 *        }catch (Exception ignore){}
 *              
 *    (4) 还要调用SocketChannel所关联的Socket的close()方法
 *            ka.channel.close(true)
 *        =>  NioChannel.close()
 *        =>  SocketChannel.socket().close();   
 *         
 *    (5) 处理(清理)Sendfile
 *       // 需要关闭还在打开的文件通道
 *       if (
 *          ka!=null && 
 *          ka.getSendfileData()!=null && 
 *          ka.getSendfileData().fchannel!=null && 
 *          ka.getSendfileData().fchannel.isOpen()
 *        ) 
 *        ka.getSendfileData().fchannel.close();
 *              
 */

public class KeepAlive {

}
