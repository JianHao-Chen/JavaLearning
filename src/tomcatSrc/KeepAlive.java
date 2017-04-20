package tomcatSrc;

/**
 *  【Keep-Alive】
 *      又称为persistent connection，或Http connection reuse。
 *      主要作用是用一个TCP连接来处理多个HTTP的请求和响应。
 *      
 *      好处是: 
 *          节省了创建和结束多个连接的时间,可以更专注于数据的传输,页面可以更快的渲染,
 *          同时也降低了服务器的负载。
 *      坏处是:
 *          影响了性能,因为在处理暂停期间,本来可以释放的资源仍旧被占用。
 * 
 * 
 *   浏览器默认是会在request的Header中加入 : connection = Keep-Alive
 *   来表示浏览器是支持使用Keep-Alive的。
 *   
 *   
 *   【Tomcat对Keep-Alive的支持】
 *   
 *     <1> 在处理请求阶段,解析解析请求行(RequestLine),头部（Headers）之后,会对
 *         头部的一些属性进行处理,例如:
 *         
 *           (取出的头部key为"connection"的field,根据它的值是"close"还是
 *            "Keep-Alive",对 变量keepAlive赋值)
 *           MessageBytes connectionValueMB = headers.getValue("connection");
 *           ByteChunk connectionValueBC = connectionValueMB.getByteChunk();
 *           if (findBytes(connectionValueBC, Constants.CLOSE_BYTES) != -1)
 *              keepAlive = false;
 *           else if (findBytes(connectionValueBC,Constants.KEEPALIVE_BYTES) != -1)
 *              keepAlive = true;
 *              
 *     <2> keep alive的最大连接数
 *         Http11NioProcessor中有成员变量:
 *              protected int maxKeepAliveRequests = -1;
 *              
 *         maxKeepAliveRequests会在Http11NioProcessor创建的时候,被赋默认值100
 *     
 *     
 *     <3> 已经开启的keep alive 连接也是会被关闭的
 *         在prepareResponse()方法里面:
 *         
 *              keepAlive = keepAlive && !statusDropsConnection(statusCode);
 *              if (!keepAlive)
 *                  headers.addValue(Constants.CONNECTION).setString(Constants.CLOSE);
 *              else if (!http11 && !error)
 *                  headers.addValue(Constants.CONNECTION).setString(Constants.KEEPALIVE);
 *             
 *              这里,statusDropsConnection()方法是对从response得到statusCode进行
 *              判断,如果是(像400,500,503...这些code)就可以在Response的header里面
 *              加入 ["connection" = "close"]
 *              
 *              
 */

public class KeepAlive {

}
