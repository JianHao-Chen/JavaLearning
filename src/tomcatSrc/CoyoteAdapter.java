package tomcatSrc;

/**
 *  CoyoteAdapter的作用:
 *   
 *   Adapter连接了Tomcat连接器Connector和容器Container.它的实现类是CoyoteAdapter
 *   主要负责的是对请求进行封装,构造Request和Response对象.并将请求转发给Container也就
 *   是Servlet容器.


 *   service(req,res)方法:
 *   
 *   <1> 入参(req,res):
 *      req,类型是Request,在coyote包下.
 *      
 *      这个Request对象用于在 InternalNioInputBuffer解析 
 *      请求行(RequestLine)，头部（Headers）时,保存相关的信息。例如:
 *      
 *      buf[] 数组作为字节数组,保存从socket读取到的数据.
 *      
 *      Request下有:
 *          methodMB、uriMB、protoMB、headers等等的属性.
 *          而methodMB保存有buf的引用,和start、end等信息.
 *          
 *  
 *    <2> 创建 Request 、 Response 对象
 *      通过connector创建 Request 、 Response 对象,这里的Request类,
 *      是在 catalina.connector包下的。
 *      
 *    <3> 调用postParseRequest()方法,里面做了:
 *    
 *      <a> 解析path parameters
 *          这里默认的参数格式是  /path;name=value;name2=value2/
 *          
 *      <b> 调用Mapper的map()方法,对请求的URI进行解析,即查找对应的context和wrapper,
 *          将信息保存在MappingData对象里面,然后设置Request对象的context为对应的
 *          StandardContext对象,设置wrapper为对应的StandardWrapper对象。
 *          
 *      
 *      <c> 解析session Id
 *      
 *      
 *    <4> 调用容器的方法
 *      并不是直接调用,而是通过:
 *      connector.getContainer().getPipeline().getFirst().invoke(request, response);
 *      
 *      getContainer()得到容器 StandardEngine[Catalina],然后按如下顺序,调用invoke()方法:
 *      
 *      StandardEngineValve.invoke() -> StandardHostValve.invoke() ->
 *      StandardContextValve.invoke() -> StandardWrapperValve.invoke().
 *      
 *      在StandardWrapperValve.invoke（）里面的filterChain.doFilter()方法,
 *      最后调用相应servlet的service()方法。   
 *  
 */

public class CoyoteAdapter {

}
