package tomcatSrc.cluster;

/**
 *  DeltaManager会话管理器是tomcat默认的集群会话管理器。
 *  
 *  DeltaManager其实就是一个会话同步通信解决方案，除了全节点复制外，它还有具有
 *  只复制会话增量的特性，增量是以一个完整请求为周期，即会将一个请求过程中所有会话
 *  修改量在响应前进行集群同步。
 *  
 *  为区分不同的动作必须要先定义好各种事件,有:
 *      EVT_SESSION_CREATED     --  会话创建事件
 *      EVT_SESSION_ACCESSED    --  会话访问事件
 *      EVT_SESSION_EXPIRED     --  会话失效事件
 *      EVT_GET_ALL_SESSIONS    --  获取所有会话事件
 *      EVT_SESSION_DELTA       --  会话增量事件
 *      EVT_CHANGE_SESSION_ID   --  会话ID改变事件
 *      
 *  
 *  <一> EVT_SESSION_CREATED : 会话创建事件
 *      
 *      【发送方】
 *          sendCreateSession(String sessionId, DeltaSession session) {
 *              SessionMessage msg = new SessionMessageImpl(XXX,
 *                              SessionMessage.EVT_SESSION_CREATED,
 *                              XXX);
 *              send(msg);
 *          }
 *  
 *      【接收方】
 *          handleSESSION_CREATED(SessionMessage msg,Member sender) {
 *              DeltaSession session = (DeltaSession) createEmptySession();
 *              session.setManager(this);
 *              session.setValid(true);
 *              session.setPrimarySession(false);
 *              session.setId(msg.getSessionID());
 *          }
 *      
 *  <二> EVT_SESSION_ACCESSED : 会话访问事件
 *  
 *     
 *          
 *      
 *  <三> EVT_SESSION_EXPIRED : 会话失效事件
 *  
 *      【发送方】
 *          sessionExpired(String id) {
 *              SessionMessage msg = 
 *                  new SessionMessageImpl(XX,SessionMessage.EVT_SESSION_EXPIRED,XX);
 *              send(msg);
 *          }
 *      【接收方】
 *          handleSESSION_EXPIRED(SessionMessage msg,Member sender){
 *              DeltaSession session = (DeltaSession) findSession(msg.getSessionID());
 *              session.expire();
 *          }
 *          
 *          
 *  <四> EVT_SESSION_DELTA : 会话增量事件
 *  
 *       【DeltaSession】
 *          在DeltaSession对象里面,有DeltaRequest:
 *              private transient DeltaRequest deltaRequest = null;
 *          DeltaRequest用于保存对某个session的操作。例如:
 *          调用DeltaSession的setAttribute("PhoneNum", "12345")方法,则会
 *          产生一个AttributeInfo对象（TYPE_ATTRIBUTE,ACTION_SET,"PhoneNum","12345"）,
 *          然后把这个对象放入deltaRequest的 actions(LinkedList类型)里面。
 *  
 *       【发送方】
 *          
 *      <1> ReplicationValve的invoke()方法,当request处理完之后,返回到这个方法
 *      <2> 如果当前使用ClusterManager并且有其他Member,就调用sendReplicationMessage()方法
 *      <3> 最后会调用DeltaManager的requestCompleted()方法,检查actions如果不为空,
 *          就生成SessionMessageImpl对象:
 *          
 *          // 序列化deltaRequest
 *          byte[] data = serializeDeltaRequest(session,deltaRequest);
 *          // 创建SessionMessageImpl对象
 *          msg = new SessionMessageImpl(XX,
 *              SessionMessage.EVT_SESSION_DELTA,
 *              data, XXX);
 *          
 *          
 *          DeltaRequest的serialize()方法:
 *          {
 *              ByteArrayOutputStream bos = new ByteArrayOutputStream();
 *              ObjectOutputStream oos = new ObjectOutputStream(bos);
 *              writeExternal(oos);
 *              oos.flush();
 *              oos.close();
 *          }
 *          
 *          writeExternal(java.io.ObjectOutput out )方法:
 *          {
 *              out.writeUTF(getSessionId());
 *              out.writeBoolean(recordAllActions);
 *              out.writeInt(getSize());    // actions的size
 *              for ( int i=0; i<getSize(); i++ ) {
 *                  AttributeInfo info = (AttributeInfo)actions.get(i);
 *                  info.writeExternal(out);
 *              }
 *          }
 *          
 *          AttributeInfo的writeExternal(java.io.ObjectOutput out)方法:
 *          {
 *              out.writeInt(getType());       // AttributeInfo的type
 *              out.writeInt(getAction());     // AttributeInfo的 action
 *              out.writeUTF(getName());       // session的Attribute的name
 *              out.writeBoolean(getValue()!=null); // 是否有value
 *              if (getValue()!=null) 
 *              out.writeObject(getValue());   //Attribute的value
 *          }
 *  
 *      <4> 把得到的SessionMessageImpl对象通过SimpleTcpCluster发送出去,
 *          SimpleTcpCluster会把消息通过tribes组件(GroupChannel)发送出去。
 *  
 *  
 *     【接收方】
 *      <1> DeltaManager的messageReceived()方法被调用,然后调用
 *              handleSESSION_DELTA(SessionMessage msg, Member sender)方法
 *      <2> 反序列化DeltaRequest:
 *              deserializeDeltaRequest(DeltaSession session, byte[] data){
 *                  ReplicationStream ois = getReplicationStream(data);
 *                  session.getDeltaRequest().readExternal(ois);
 *              }
 *              
 *              readExternal(java.io.ObjectInput in){
 *                  sessionId = in.readUTF();
 *                  recordAllActions = in.readBoolean();
 *                  int cnt = in.readInt();
 *                  for (int i = 0; i < cnt; i++) {
 *                      AttributeInfo info = new AttributeInfo();
 *                      info.readExternal(in);
 *                      actions.addLast(info);
 *                  }
 *              }
 *              
 *      <3> 把DeltaRequest包含的对某个session的所有操作同步到本地该session:
 *      
 *              AttributeInfo info = (AttributeInfo)actions.get(i);
 *              
 *              switch ( info.getType() ) {
 *              
 *                  case TYPE_ATTRIBUTE: {
 *                      if ( info.getAction() == ACTION_SET ) {
 *                          session.setAttribute(info.getName(), 
 *                              info.getValue(),notifyListeners,false);
 *                      }
 *                      else{
 *                          session.removeAttribute(info.getName(),notifyListeners,false);
 *                      }
 *                      break;
 *                  }
 *                  
 *                  case XXX
 *                      ......
 *              }
 *              
 *              
 */

public class DeltaManager {

}
