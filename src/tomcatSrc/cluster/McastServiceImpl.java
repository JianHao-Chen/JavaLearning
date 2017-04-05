package tomcatSrc.cluster;

import java.io.Serializable;

/**
 *  <一> 作用:
 *      Membership service的实现, 使用简单的多播。
 *      这个类维护了一个列表,用于记录当前cluster的 有效的(active)节点。
 *      当某个节点不能发送心跳包,那么这个节点就被mark为dismissed
 *      
 *  <二> 数据结构:
 *      
 *      <1> MulticastSocket socket; // 监听的套接字
 *      <2> MemberImpl member;      // 本地 member,会一直把它广播出去
 *      <3> InetAddress address;    // 多播地址
 *      <4> int port;               // 多播端口号
 *      <5> DatagramPacket sendPacket;      // 发送的数据包,reuse
 *      <6> DatagramPacket receivePacket;   // 接受的数据包,reuse
 *      <7> Membership membership;          // 这个service所维护的Membership信息
 *      <8> MembershipListener service;
 *      <9> ReceiverThread receiver;        // 用于监听 Membership
 *     <10> SenderThread sender;            // 用于广播 local Member
 *      
 *      
 *   <三> 初始化:
 *      <1> 为 socket 建立套接字  , 并进行设置 
 *          socket = new MulticastSocket(port);
 *          socket.setLoopbackMode(false);
 *          socket.setSoTimeout(mcastSoTimeout);
 *          
 *      <2> 为 sendPacket 、 receivePacket 创建  DatagramPacket , 并进行设置
 *      
 *      <3> 为 membership创建 Membership对象
 *      
 *      
 *   <四> 添加一个 Member的过程 :
 *   
 *      <1> 节点A执行McastServiceImpl的send()方法,具体如下:
 *        {
 *          <a> 获取节点A的local member 的byte[]数组
 *          
 *              -- local member的host、port由getData()方法 encode入 byte[] --
 *                  byte[] data = member.getData();  
 *                  
 *          <b> 创建 DatagramPacket 并设置 Address、Port
 *              DatagramPacket p = new DatagramPacket(data,data.length);
 *              p.setAddress(address);  // 填入的是多播的地址
 *              p.setPort(port);        // 填入的是多播的端口
 *          <c> 发送 DatagramPacket
 *              socket.send(p);
 *        }
 *   
 *      <2> 节点B执行McastServiceImpl的receive()方法,具体如下:
 *        {
 *          <a> 从 MulticastSocket中读取 datagram packet
 *                socket.receive(receivePacket);
 *                
 *          <b> 获取receivePacket中的 byte[]数组, 得到 byte[] data;
 *          
 *          <c> 将 data 转换为 MemberImpl对象
 *              final MemberImpl m = MemberImpl.getMember(data);
 *              
 *          <d> 如果这个MemberImpl不是 local Member,就把它加入到保存着的
 *              MemberImpl[]数组中.    
 *          
 *          <e> 新建一个线程,运行 McastService的 memberAdded()方法,
 *              为了运行listener(ChannelCoordinator)的memberAdded()方法.
 *              通过Interceptor链依次调用memberAdded()方法, 其中在
 *              TcpFailureDetector中保存的  membership信息中添加这个 member。
 *        }
 *      
 *      
 *      
 *   <五> 删除一个 Member的过程 :
 *      每个节点都会在执行McastServiceImpl的receive()方法的最后,进行检查。
 *      检查是通过执行 checkExpired()方法完成的。
 *      这个方法会遍历本节点保存的   membership 中的所有 member, 然后调用
 *      membership的removeMember()方法,删除在membership中保存的此member。
 *      同样,这个方法也会新建一个线程来执行 CallBack操作,通过运行 McastService的
 *      memberDisappeared()方法, 进而调用 ChannelCoordinator的
 *      memberDisappeared()方法。 通过Interceptor链,将会调用TcpFailureDetector
 *      来处理:
 *          它会先拦下这个member关闭的信息,然后按照这个member的 host、port来建立TCP
 *          连接。只有这个member真的down了,才会继续通知上层的Interceptor.
 *      
 *      
 *      
 *      
 *   <六> 发送一个session给其他member :  
 *      
 *      <1> DeltaManager在调用createSession()后,先构造出SessionMessage对象,
 *          SessionMessage的结构是:
 *          {
 *              contextName,        // "localhost#/examples"
 *              EvtType,            // SessionMessage.EVT_SESSION_CREATED
 *              mSession,           // 序列化的Session
 *              mSessionID,         // Session ID
 *              ....
 *          }
 *          然后调用SimpleTcpCluster的send()方法。
 *      
 *      <2> SimpleTcpCluster会调用GroupChannel的
 *              send(Member[] destination, Serializable msg, int options);
 *          
 *          <a> 创建ChannelData对象,ChannelData的结构是:
 *              {
 *                  address,    //LocalMember
 *                  message,    // 序列化的SessionMessage
 *                  options,
 *                  timestamp,
 *                  uniqueld
 *              }
 *          <b> 通过 Interceptor链调用sendMessage()方法
 *          <c> MessageDispatch15Interceptor的sendMessage()方法,会根据
 *              ChannelData里面的options被设置为 Channel.SEND_OPTIONS_ASYNCHRONOUS,
 *              会把data放入自己的队列里面进行异步发送,并且马上返回。(因为需要尽快返回到Servlet
 *              然后返回Response给用户)。
 *          <d> 所谓"异步"其实只是  使用 ThreadPoolExecutor执行发送方法,最后还是调用到
 *              ChannelCoordinator的sendMessage()方法。
 *          
 *      
 */

public class McastServiceImpl {

}
