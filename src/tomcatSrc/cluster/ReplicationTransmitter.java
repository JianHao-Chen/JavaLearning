package tomcatSrc.cluster;


/**
 * <一>
 *   ReplicationTransmitter使用 PooledParallelSender作为它的Transport,
 *   这个Transport就是实际上发送消息的。
 * 
 *    PooledParallelSender有2个特性:
 *    <1> 它实现了 MultiPointSender 接口 , 需要实现这个方法:
 *           public void sendMessage(Member[] destination, ChannelMessage data) 
 *        就是为了实现向多个地址发送消息
 *      
 *    <2> 继承于PooledSender, 它拥有SenderQueue ,用于保存 DataSender,
 *        这里默认的是 ParallelNioSender。 
 *        所以PooledParallelSender就是一个DataSender池。
 *
 *
 *  <二> ParallelNioSender
 *  
 *  
 *
 *
 *
 */

public class ReplicationTransmitter {

}
