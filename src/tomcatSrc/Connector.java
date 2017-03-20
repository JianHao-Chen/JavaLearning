package tomcatSrc;


/**
 *          ***     Connector包含的组件和初始化        ***
 *      
 *    <一> Connector的初始化
 *    
 *      <1> 在server.xml里面定义了connector组件:
 *      
 *          <Connector port="8099" protocol="My.coyote.http11.Http11NioProtocol" 
               connectionTimeout="20000" 
               redirectPort="8449" />
               
         <2> Connector包含3部分(他们都会被初始化):
             {
                 Http11NioProtocol,
                 CoyoteAdapter(用于将protocolHandler生成好的request以及response，将其交给Container来处理),
                 Mapper
             }
 *       
 *        <3> Http11NioProtocol(用于具体的底层IO以及数据的处理，例如acceptor，http协议什么的)
 *          包含:
 *           {
 *              NioEndpoint,
 *              Http11ConnectionHandler
 *           }
 *           Http11ConnectionHandler对象维护了一个Http11Processor对象池，
 *           Http11Processor对象会调用CoyoteAdapter完成http request的解析和分派。 
 *
 *
 *        <4> NioEndpoint的初始化
 *        
 *            ServerSocketChannel serverSock = ServerSocketChannel.open();
 *            InetSocketAddress addr = new InetSocketAddress(8099);
 *            serverSock.socket().bind(addr,backlog);
 *            serverSock.configureBlocking(true);
 *            serverSock.socket().setSoTimeout(XXX);
 *---------------------------------------------------------------------------
 *
 *   <二> Connector的start
 *      
 *      <1> Connector.start() -> Http11NioProtocol.start() -> NioEndpoint.start()
 *      
 *      <2> NioEndpoint.start()
 *          创建工作者线程池、 启动Poller[]数组的所有Poller线程、启动Acceptor线程
 *          
 *      <3> 关于Mapper的另外写下.
 *   
 *               
 */

public class Connector {

}
