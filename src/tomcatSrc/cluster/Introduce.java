package tomcatSrc.cluster;

/**
 *  <一> 启用Tomcat的群集:
 *  
 *    <1> 所有的session attributes必须是实现java.io.Serializable
 *    <2> 在server.xml文件里面,uncomment 以下标签:
 *      <Cluster className="org.apache.catalina.ha.tcp.SimpleTcpCluster"/>
 *    <3> 在 web.xml 文件里面添加  <distributable/> 元素。
 *    ... 还有一些,先省略
 *    
 *   <二> Tomcat群集的结构:
 *      Server
           |
         Service
           |
         Engine
           |  \ 
           |  --- Cluster --*
           |
         Host
           |
         ------
        /      \
     Cluster    Context(1-N)                 
        |             \
        |             -- Manager
        |                   \
        |                   -- DeltaManager
        |                   -- BackupManager
        |
     ---------------------------
        |                       \
      Channel                    \
    ----------------------------- \
        |                          \
     Interceptor_1 ..               \
        |                            \
     Interceptor_N                    \
    -----------------------------      \
     |          |         |             \
   Receiver    Sender   Membership       \
                                         -- Valve
                                         |      \
                                         |       -- ReplicationValve
                                         |       -- JvmRouteBinderValve 
                                         |
                                         -- LifecycleListener 
                                         |
                                         -- ClusterListener 
                                         |      \
                                         |       -- ClusterSessionListener
                                         |       -- JvmRouteSessionIDBinderListener
                                         |
                                         -- Deployer 
                                                \
                                                 -- FarmWarDeployer
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

public class Introduce {

}
