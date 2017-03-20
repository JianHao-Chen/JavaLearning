package tomcatSrc;

/**
 *  NioEndpoint是Http11NioProtocol中负责接收处理socket的主要模块,
 *  它是非阻塞的。
 *  需要注意的是，tomcat的NIO connector并非完全是非阻塞的，有的部分，
 *  例如接收socket，从socket中读写数据等，还是阻塞模式实现的
 *  
 *  <一> NioEndpoint的组成部分
 *  
 *    <1> Worker线程
 *    <2> Poller线程
 *        Poller包含:{selector、events、wakeupCounter}
 *        
 *        ** 在connector中并不止一个Selector，在socket的读写数据时，为了控制
 *        timeout也有一个Selector，在后面的BlockSelector中介绍。
 *        可以先把Poller线程中维护的这个Selector标为主Selector。
 *        
 *        ** wakeupCounter 是一个AtomicLong的变量,初始值是1.
 *           <a> addEvent（）时,进行判断  :
 *               if ( wakeupCounter.incrementAndGet() == 0 ) 
                   selector.wakeup(); 
             <b> 在 Poller线程run()方法里面,进行判断  :  
                 if (wakeupCounter.getAndSet(-1)>0) {
                     keyCount = selector.selectNow();
                 }
                 else {
                 // 如果这个blocking的select过程中,有event加入了,调用
                 // selector.wakeup(),这个 select()马上返回.
                     keyCount = selector.select(selectorTimeout);
                 }
                 wakeupCounter.set(0);
 *    
 *    <3> Acceptor线程
 *    <4> NioSelectorPool
 *  
 *
 *  <二> 组件之间的关系
 *---------------------------------------------------------------------- 
 *  【Acceptor】
 *    <1> Http请求到来,Acceptor线程通过:
 *          SocketChannel socket = serverSock.accept();
 *        获得SocketChannel对象(这个监听端口是使用了阻塞模式)。
 *    <2> 将得到的SocketChannel对象进行配置(NoBlocking、BufferSize、KeepAlive等等),
 *        然后将它封装到NioChannel对象里面。
 *    <3> 先获取一个Poller对象,再将NioChannel对象和一个KeyAttachment对象一起封装入
 *        一个PollerEvent对象里面,最后把这个PollerEvent对象放入Poller对象里面的
 *        队列。
 *    <4> Acceptor线程继续监听直到新的Http请求到来。
 *----------------------------------------------------------------------
 *   【Poller】
 *      <1> 首先从events队列拿出PollerEvent对象。执行它们的run()方法,其实质就是
 *          将PollerEvent对象的SocketChannel对象以OP_READ的方式注册到主Selector.
 *          
 *          socket.getIOChannel().register(
                        key.getPoller().getSelector(), SelectionKey.OP_READ, key);
 *    
 *      <2> 这个主Selector执行select操作，并取得selected-key set。
 *          这个selected-key set的意思是:
 *          
 *          is the set of keys such that each key's channel was detected 
 *          to be ready for at least one of the operations identified in 
 *          the key's interest set during a prior selection operation.
 *          
 *          This set is returned by the selectedKeys() method.
 *          The selected-key set is always a subset of the key set.
 *          
 *     <3>  对selected-key set的每个key,从集合中移除并执行processKey()方法。
 *          processKey()里面做了:
 *              <a> 从SelectionKey里取得NioChannel。
 *              <b> 将已经ready的事件从感兴趣的事件中移除
 *              
 *                  // clear the interest set
                    unreg(sk, attachment,sk.readyOps());
                    
                    protected void unreg(SelectionKey sk, KeyAttachment attachment, int readyOps) {
                        //must do this unreg, so that we don't have multiple threads messing with the socket
                        reg(sk,attachment,sk.interestOps()& (~readyOps));
                    }
 *                  protected void reg(SelectionKey sk, KeyAttachment attachment, int intops) {
                        sk.interestOps(intops); 
                        attachment.interestOps(intops);
                    }
 *            【注意】
 *              如果没有执行unreg()方法,这个Channel的这次有数据可读的事件依然保留着(即使
 *              这次我们已经知道了，交给SocketProcessor线程处理了).
 *              那么在selector的下一次select操作,这个Channel的这次有数据会被误以为又有
 *              新数据到达,又交给另一个SocketProcessor线程处理。
 *
 *              <c> 调用processSocket（）方法
 *                  里面将Channel放到SocketProcessor里面,然后把SocketProcessor
 *                  放入线程池运行。
 *-------------------------------------------------------------------------
 *
 *  【Worker】(也即SocketProcessor)
 *      通过Http11NioProcessor来读取Channel里面的数据，然后进行http协议的解析,生成相应的
 *      request,response对象,再把request,response对象放入CoyoteAdapter的service()方法
 *      执行。
 *          
 *  
 */

public class NioEndPoint {

}
