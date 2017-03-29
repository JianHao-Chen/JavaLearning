package tomcatSrc;

/**
 *   Cookie的定义:
 *      Cookie实际上是一小段的文本信息。
 *      
 *     原因:   
 *      HTTP协议是无状态的协议,服务器无法从连接上跟踪会话.
 *      客户端请求服务器,如果服务器需要记录该用户状态,就使用response向客户端浏览器颁发
 *      一个Cookie。客户端浏览器会把Cookie保存起来。当浏览器再请求该网站时，浏览器把请求
 *      的网址连同该Cookie一同提交给服务器。服务器检查该Cookie，以此来辨认用户状态。
 *     
 *     注意:
 *      Cookie功能需要浏览器的支持。如果浏览器不支持Cookie（如大部分手机中的浏览器）或者把
 *      Cookie禁用了，Cookie功能就会失效。
 *
 *---------------------------------------------------------------------------
 *
 *
 *  Cookie的数据结构:
 *      <1> Request
 *          <A> 在Request对象中,持有Cookie[]数组的引用:
 *              protected Cookie[] cookies = null;
 *          
 *          <B> Cookie(在javax.servlet.http包下),它的结构是:
 *              {
 *                  private String name;
 *                  private String value;
 *                  private String comment;
 *                  private String domain;
 *                  private int maxAge = -1;
 *                  private String path;
 *                  private int version = 0;
 *              }
 *  
 *      <2> CoyoteRequest
 *          <A> 在CoyoteRequest对象中,持有Cookies的引用:
 *              private Cookies cookies = new Cookies(headers);
 *      
 *          <B> Cookies(在tomcat.util.http包下),它的结构是:
 *              {
 *                  ServerCookie scookies[] = new ServerCookie[];
 *                  int cookieCount=0;
 *                  MimeHeaders headers;    //持有CoyoteRequest的headers的引用
 *              }
 *          
 *              再看ServerCookie的结构:
 *              {
 *                  MessageBytes name,
 *                  MessageBytes value,
 *                  MessageBytes path,
 *                  MessageBytes domain,
 *                  MessageBytes comment,
 *                  int maxAge,
 *                  int version
 *              }
 *      
 *          ServerCookie可以说是Cookie的底层版本,它使用MessageBytes表示各个field,
 *          即引用着同一个byte[]数组。
 *          
 *---------------------------------------------------------------------------
 *  
 *  <一> Cookie的添加:
 *      <1> 新建一个Cookie对象
 *          Cookie cookie = new Cookie(cookieName, cookieValue);
 *      <2> 将 Cookie添加到 Response
 *          <a> 生成这个Cookie对应的字符串,例如 "phone=123456"
 *          <b> 将这个字符串设置到Response的header里面:
 *              addHeader("Set-Cookie", sb.toString());
 *              于是,在CoyoteResponse的Header多了一项: "Set-Cookie = phone=123456"
 * 
 *  
 *  <二>
 *      此时,浏览器得到的response,其header有这么一项:
 *          Set-Cookie: phone=123456
 *              
 *      如果浏览器再次访问服务端,Request的Header有:
 *          Cookie: phone=123456
 *      
 *  
 *  <三> 对Cookie的处理:
 *      <1> InternalNioInputBuffer这个类,已经解析HTTP请求的Headers部分并且把它们
 *          置于coyoteRequest的headers里面。
 *      <2> CoyoteAdapter在把request交给Container之前,先对请求做一些处理,其中就包括
 *          对cookie的处理
 *      <3> headers是MimeHeaders类型的,其中保存着MimeHeaderField类型的数组,每一个
 *          MimeHeaderField就是request header的一个键值对.
 *      <4> 首先在headers 中找出键值为"Cookie"的MimeHeaderField,做一些检查,然后就
 *          把Cookie添加到coyoteRequest。
 *      <5> 添加 Cookie 的具体细节:
 *            在Cookies下的ServerCookie[]数组中添加一项,然后设置这个新的ServerCookie
 *            的名字(如"phone") 和值(如"123456")
 *  
 *
 *  Cookie的获取(调用request.getCookies()):
 *      
 *      <1> 取得coyoteRequest的Cookies对象(serverCookies)
 *      <2> 调用serverCookies的getCookieCount()方法,取得serverCookies中cookie的数目n
 *      <3> 在Request中创建长度为n的 Cookie[]数组.并且将serverCookies中的cookie的值设置
 *          到每一个新建的Cookie对象中。
 *      
 *
 */

public class Cookie{

}
