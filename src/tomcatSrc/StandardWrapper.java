package tomcatSrc;

/**
 *  StandardWrapper,可以将它理解为对servlet的包装。
 *  
 *  ------------------------------------------------------------------------
 *  
 *  【StandardWrapper对象的创建】
 *  
 *    StandardContext的start()方法  -> lifecycle.fireLifecycleEvent(START_EVENT, null)  ->
 *    ContextConfig.lifecycleEvent(event)  ->  ContextConfig.start()  ->  defaultWebConfig()
 *    ->  processDefaultWebConfig(webDigester, stream, source);
 *    
 *    processDefaultWebConfig做的就是解析:
 *      \webapps\context_name\WEB-INF\web.xml文件,这个文件定义了在此context下的servlet.
 *      例如:
 *          <servlet>
                <servlet-name>HelloWorldExample</servlet-name>
                <servlet-class>HelloWorldExample</servlet-class>
            </servlet>
            <servlet-mapping>
                <servlet-name>HelloWorldExample</servlet-name>
                <url-pattern>/servlets/servlet/HelloWorldExample</url-pattern>
            </servlet-mapping>
 *    
 *    
 *    【StandardWrapper的allocate()方法】
 *      allocate()方法用于分配并初始化它关联的Servlet对象。
 *     allocate()
 *    {
 *      <1> instance = loadServlet();
 *      
 *      <2> Loader loader = getLoader();    //这里loader是WebappLoader。
 *      
 *      <3> ClassLoader classLoader = loader.getClassLoader();
 *          Class classClass = classLoader.loadClass(actualClass);
 *    
 *      <4> servlet = (Servlet) classClass.newInstance();
 *      
 *      <5> servlet.init(facade);       //facade是StandardWrapperFacade。
 *    
 *    }
 * 
 * 
 */

public class StandardWrapper {

}
