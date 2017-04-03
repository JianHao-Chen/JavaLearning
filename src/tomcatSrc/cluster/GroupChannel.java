package tomcatSrc.cluster;

/**
 *    <一> GroupChannel的作用:
 *    
 *      GroupChannel是Channel接口的默认实现。
 *      
 *      <1> Channel是Tomcat节点之间进行通讯的工具。
 *          Channel包括5个组件：Membership、Receiver、Sender、Transport、Interceptor。
 *          
 *      <2> Membership维护集群的可用节点列表。它可以检查到新增的节点，也可以检查到没有心跳的节点
 *      
 *      <3> Receiver接收器，负责接收消息.具体实现分为两种：BioReceiver(阻塞式)、NioReceiver(非阻塞式)
 *
 *      <4> Sender发送器，负责发送消息.内嵌了Transport组件，Transport真正负责发送消息
 *          Transport分为两种：bio.PooledMultiSender(阻塞式)、nio.PooledParallelSender(非阻塞式)
 *          
 *      <5> Interceptor,Cluster的拦截器
 *          
 *          在Channel中的Interceptor是以链表的方式保存起来的:
 *              protected ChannelInterceptor interceptors = null;
 *          
 *          默认情况下,interceptors链表是:
 *              GroupChannel(第一个是GroupChannel自己) -> 
 *                  MessageDispatch15Interceptor -> TcpFailureDetector ->
 *                      ChannelCoordinator
 *                      
 *          ChannelCoordinator:
 *              协调Membership service,  sender 和  receiver
 *      
 *          TcpFailureDetector: 
 *              网络、系统比较繁忙时，Membership可能无法及时更新可用节点列表，
 *              此时TcpFailureDetector可以拦截到某个节点关闭的信息，并尝试
 *              通过TCP连接到此节点，以确保此节点真正关闭，从而更新集群可以用
 *              节点列表
 *      
 *          MessageDispatch15Interceptor:
 *              查看Cluster组件发送消息的方式是否设置为Channel.SEND_OPTIONS_ASYNCHRONOUS
 *              (Cluster标签下的channelSendOptions为8时)。设置为
 *              Channel.SEND_OPTIONS_ASYNCHRONOUS时，MessageDispatch15Interceptor
 *              先将等待发送的消息进行排队，然后将排好队的消息转给Sender
 *         
 *         
 *          
 *              
 *              
 *              
 *    <二> GroupChannel的运行:          
 *    
 *      start():
 *          <a> 设置好Interceptor链
 *          <b> 通过start调用链,最后执行ChannelCoordinator的start()方法,主要做的是启动以下组件:
 *              ◎ replication receiver,执行 NioReceiver 线程
 *              ◎ replication transmitter ,执行  ReplicationTransmitter
 *              ◎  membership receiver , 执行ReceiverThread 线程
 *              ◎  membership broadcaster , 执行 SenderThread 线程
 *              
 */

public class GroupChannel {

}
