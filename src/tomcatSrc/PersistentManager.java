package tomcatSrc;

/**
 * 目的:
 *  Session的主要数据被存储在服务器内存中，而服务器会为每个在线用户创建一个Session对象，
 *  当在线用户很多时，例如同时有几万或是几十万在线的情况下，Session内存的开销将会十分巨大，
 *  会影响Web服务器性能。而Session的钝化机制刚好可解决此问题。Session钝化机制的本质就在
 *  于把服务器中不经常使用的Session对象暂时序列化到系统文件系统或是数据库系统中，当被使用
 *  时反序列化到内存中，整个过程由服务器自动完成。 
 *  
 *  
 *  
 *  配置:
 *  
 *  在conf\context.xml加入如下标签:
 *  
 *      <Manager className="My.catalina.session.PersistentManager">
 *          <Store className="My.catalina.session.FileStore"/>
 *      </Manager>
 *  
 *  然后Tomcat启动时,会创建此Manager并把它与StandardContext关联起来。
 *  
 *  
 *  
 *  实现:
 *  PersistentManager会进行检查,如果当前活动的session对象超过了上限值，
 *  或者session对象闲置了过长时间,就把session换出.
 *  
 *  
 *  PersistentManager的检查:
 *      通过backgroundProcess()方法调用processExpires(),在processExpires()方法
 *      里面,检查session是否valid,将已经expired的session先排除掉。然后,调用
 *      processPersistenceChecks()方法。
 *      
 *      processPersistenceChecks()方法调用了3个方法: {
 *          processMaxIdleSwaps();  // idle时间太长的session被换出
 *          processMaxActiveSwaps();// active的session数目太多被换出
 *          processMaxIdleBackups();
 *      }
 *      
 *      processMaxIdleSwaps:
 *          PersistentManager有属性maxIdleSwap,用于设置当一个session的idle时间大于
 *          这个值,就把这个session换出。
 *      
 *      processMaxActiveSwaps:
 *          PersistentManager有属性maxActiveSessions,用于设置当session数目大于
 *          这个值,就换出n个session,直到session数目<maxActiveSessions。
 *      
 *      
 *  
 *  
 *  
 */

public class PersistentManager {

}
