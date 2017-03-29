package tomcatSrc;

/**
 *  Session的定义:
 *      在服务器中保存客户端信息的对象。
 *  
 *  与Cookies的关系:
 *      Session是另一种记录客户状态的机制，不同的是Cookie保存在客户端浏览器中，
 *      而Session保存在服务器上。
 *      
 *      如果说Cookie机制是通过检查客户身上的“通行证”来确定客户身份的话，那么Session
 *      机制就是通过检查服务器上的“客户明细表”来确认客户身份。Session相当于程序在服务
 *      器上建立的一份客户档案，客户来访的时候只需要查询客户档案表就可以了。
 *---------------------------------------------------------------------------
 *  
 *  Session的数据结构:
 *  
 *    <1> Request对象持有:
 *          <a> Session对象的引用
 *                protected Session session = null;
 *          <b> RequestedSessionId(用于查找对应的session)
 *                protected String requestedSessionId = null;
 *    <2> 
 * 
 * 
 *---------------------------------------------------------------------------
 *
 *  Session的获取/创建:
 *  当调用request的getSession()方法而当前request没有session的话,默认是会创建一个的。
 *  
 *  其中的 doGetSession(boolean create)方法: //create==true表示session为空或失效时创建一个
 *  {
 *      <1> session不为null并且有效,返回
 *      
 *      (下面是创建session的逻辑)
 *      <2> 取出Manager,Manager是用于管理session的。Manager与StandardContext关联。
 *          这里默认的是StandardManager,
 *          还有:
 *              PersistentManager用于提供持久化session的功能,
 *              DeltaManager用于tomcat群集时session的传输。
 *              
 *      <3> 调用 Manager的createSession()方法,创建一个StandardSession对象,并设置
 *          session对象的相关属性(ID、isValid、CreationTime、MaxInactiveInterval)
 *          
 *      <4> 先在 StandardContext中取出 boolean flag "use cookies for session ids",
 *          用于指示是否用cookie来保存session ID.(默认是)
 *          
 *      <5> 创建 Cookie:
 *          Cookie cookie = new Cookie("JSESSIONID", session.getIdInternal());
 *      <6> 设置相关属性,如(maxAge=-1、path=StandardContext.getPath())
 *      
 *      <7> 将Cookie添加到Response的Header里面,添加了:
 *              " Set-Cookie = JSESSIONID=SessionID--1; Path=/examples "
 *  } 
 * 
 *      
 *      
 *      
 *      
 *      
 */

public class Session {

}
