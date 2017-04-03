package tomcatSrc.cluster;

/**
 *  <一> 作用:
 *
 *
 *  <二> 接口
 *      实现了以下接口:CatalinaCluster,MembershipListener,ChannelListener
 *      几个重要的方法:
 *      
 *      <1> public Member[] getMembers();
 *          获取当前参与到这个群集的所有Member。
 *          
 *      <2> public void send(ClusterMessage msg);
 *          将msg发送给这个群集的所有Member
 *      
 *      <3> public void send(ClusterMessage msg, Member dest);
 *          将msg发送给这个群集的 dest 这个Member。
 *          
 *      <4> public void memberAdded(Member member);
 *          增加Member
 *          
 *      <5> public void memberDisappeared(Member member);
 *          remove Member
 *          
 *      <6> public boolean accept(Serializable msg, Member sender);
 *          收到msg以后,调用ChannelListener的这个方法来决定这个ChannelListener
 *          是否要对这个msg 进行处理。
 *          
 *      <7> public void messageReceived(Serializable msg, Member sender);
 *          收到msg以后,调用ChannelListener的这个方法对这个msg 进行处理.
 *      
 *
 *  <三> 运行流程:
 *  
 *    <1> start()
 *        做一些准备工作,例如:
 *        <a> addClusterListener
 *        <b> addValve
 *        <c> 建立channel :  channel = new GroupChannel();
 *        <d> 为channel添加Interceptor:
 *              channel.addInterceptor(new MessageDispatch15Interceptor());
 *              channel.addInterceptor(new TcpFailureDetector());
 *        <e> 为channel设置Listener (这个Listener就是SimpleTcpCluster自己):
 *              channel.addMembershipListener(this);
 *              channel.addChannelListener(this);
 *        <f> 启动channel:
 *              channel.start(channelStartOptions);
 *
 */

public class SimpleTcpCluster {

}
