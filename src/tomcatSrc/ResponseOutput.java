package tomcatSrc;

/**
 *  在 用户写的Servlet的doGet()、doPost()方法中,参数为 RequestFacade、ResponseFacade。
 *  它们有着Request、Response对象的引用(connector包下)。这里用到了门面设计模式。
 *  主要是为了让用户的Servlet访问到Request、Response的数据而对于他们的一些敏感数据就隔离
 * 
 *
 */

public class ResponseOutput {

}
