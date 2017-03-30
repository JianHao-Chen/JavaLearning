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
 *  Session的创建:
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
 *  Session的获取:
 *  当浏览器再次访问时,会在Request的请求头带上  "Cookie : JSESSIONID=SessionID--4"。
 *  
 *  <1> 获取RequestedSessionId
 *  CoyoteAdapter的parseSessionCookiesId()方法,是用于解析请求头的Cookie的,比如从header
 *  中解析出 Cookies对象,其中ServerCookie[0]是"Cookie JSESSIONID=SessionID--4",
 *  接着找出这个ServerCookie[0],依据是ServerCookie的name是"JSESSIONID",然后把这个
 *  ServerCookie的value即"SessionID--4"的值赋给request的RequestedSessionId。
 *  
 *  <2> 找出session
 *  doGetSession(boolean create)方法:
 *  {
 *      // XXXXX
 *      session = manager.findSession(requestedSessionId);
 *  }
 *  
 *  而manager中保存着session的Map:
 *      protected Map<String, Session> sessions = new ConcurrentHashMap<String, Session>();
 *      
 *  
 *      
 *      
 *      
 *  对于浏览器不支持Cookie的情况:
 *  
 *      可以通过 调用 response.encodeURL("SessionExample"),“SessionExample”是Context Name.
 *      得到 : SessionExample;jsessionid=SessionID--1
 *  
 *  然后,浏览器得到的URL会是: http://.../SessionExample;jsessionid=SessionID--1
 *  
 *  在CoyoteAdapter解析 PathParameters 时, 会把 " jsessionid=SessionID--1 "
 *  作为参数添加到request的 pathParameters(这是一个HashMap)
 *  
 *  随后会从PathParameter中取出key为jsessionid的值即"SessionID--1",并设置到
 *  RequestedSessionId中.
 *      
 */

public class Session {

}
