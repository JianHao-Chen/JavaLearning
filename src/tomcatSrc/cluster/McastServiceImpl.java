package tomcatSrc.cluster;

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
 *          
 *          
 *        }
 *      
 *      
 */

public class McastServiceImpl {

}
