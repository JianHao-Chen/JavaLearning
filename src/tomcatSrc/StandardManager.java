package tomcatSrc;

/**
 *  对Session的管理,Tomcat是通过 Manager 接口及其实现类来完成的。
 *  
 *  StandardManager是 Tomcat默认的Session管理类,它继承于ManagerBase。
 *  ManagerBase 里面实现的关于session 的增删查改方法就省略了。
 *  
 *  对session是否有效的检查:
 *      在 ManagerBase有方法backgroundProcess(),它调用了processExpires()方法,
 *      processExpires()将保存在当前manager的关于session的HashMap遍历一遍,
 *      对每一个session都调用isValid()方法。
 *      
 *      isValid()方法通过计算当前时间和这个session的上一次访问时间的时间差(未被访问的时间),
 *      与 maxInactiveInterval比较,如果超过maxInactiveInterval,就调用session的
 *      expire()方法将这个session置为失效.
 *  
 *  
 *  在Tomcat中有一个后台线程:
 *      ContainerBackgroundProcessor,它会从StandardEngine开始依次执行所有容器的子容器。
 *      当执行到StandardContext的backgroundProcess()方法时,StandardManager的
 *      backgroundProcess()方法被调用。
 *      
 *  
 *  
 *
 *  关于session timeout 时间的设置,有以下几种方式:
 *  
 *  <1> Web容器级别
 *      在conf/web.xml中,使用如下标签设置
 *          <session-config>  
                <session-timeout>31</session-timeout>  
            </session-config>
 *      
 *      值"31"会被设置到StandardContext的 sessionTimeout 属性。
 *      而sessionTimeout的默认值是30:
 *          private int sessionTimeout = 30;
 *  
 *  <2> webapp级别
 *      在webapp中的 WEB-INF/web.xml,使用如下标签设置
 *          <session-config>  
                <session-timeout>32</session-timeout>  
            </session-config>
 *      值"32"会被设置到StandardContext的 sessionTimeout 属性。     
 *  
 *      【webapp的优先级比Web容器的高】
 *      
 *   
 *  当 Manager与 StandardContext 关联时, StandardContext的 sessionTimeout的值
 *  会赋给 Manager的 maxInactiveInterval 属性.
 *  maxInactiveInterval的默认值是 60:
 *      protected int maxInactiveInterval = 60;
 *
 */


public class StandardManager {

}
