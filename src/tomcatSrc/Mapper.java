package tomcatSrc;

/**
 *  Mapper 提供请求路径的路由映射，根据某个请求路径通过计算得到相应的Servlet（Wrapper）.
 *  ----------------------------------------------------------------------
 *  
 *  【结构层次】
 *  
 *  <一> 定义在 Mapper里面的内部类:
 *      
 *  <1> MapElement
 *      MapElement {
            String name = null;     //名字
            Object object = null;   //具体的容器，例如是host对象，context对象或者wrapper对象
        }
 *     
 *  <2> Host
 *      Host extends MapElement{
 *          ContextList contextList = null;
 *      }
 *  
 *  <3> ContextList
 *      ContextList{
 *          Context[] contexts = new Context[0];
 *          int nesting = 0;
 *      }
 *  
 *  <4> Context         //某个context的某个版本的具体信息
 *      Context extends MapElement{
 *          public String path = null;   //path
 *          public String[] welcomeResources = new String[0];   //welcome的数据
 *          public Wrapper defaultWrapper = null;               //默认的wrapper
            public Wrapper[] exactWrappers = new Wrapper[0];    //对wrapper的精确的map
            public Wrapper[] wildcardWrappers = new Wrapper[0]; //基于通配符的map
            public Wrapper[] extensionWrappers = new Wrapper[0];//基于扩展名的map
 *      }
 *      
 *      
 *  <二> Mapper的定义
 *  
 *      protected Host[] hosts = new Host[0];       // 对host的map信息
 *      
 *      protected String defaultHostName = null;    // engine使用的默认的host名字
 *      
 *      protected Context context = new Context();  //context的map信息
 *      
 *  <三> Mapper数据的读入
 *  
 *      【原理】
 *          在Tomcat的各个容器(Engin、Host、Context)初始化的最后,会将自己注册到JMX,
 *          随后在Connector组件启动时,通过mapperListener.init(),会分别从JmxMBeanServer
 *          查询出各个容器,从而分别调用mapper的addXX()方法,来形成从Host到Wrapper的各级容器的快照
 *  
 *      <1> 作为Connector的成员,在Connector对象初始化时创建Mapper对象:
 *          protected Mapper mapper = new Mapper();
 *          // 还有创建了MapperListener对象。
 *          protected MapperListener mapperListener = new MapperListener(mapper, this);
 *          
 *      <2> 在Connector的start()方法
 *              调用 mapperListener.init();
 *          init方法里面,包含了 :
 *              registerEngine();
 *              registerHost();     -->     mapper.addHost()
 *              registerContext();  -->     mapper.addContext()
 *              registerWrapper();  -->     mapper.addWrapper()
 *          
 *      <3> addHost()方法
 *          创建新的Mapper.$Host对象,并保存到hosts数组中,hosts数组的元素是按照名字排序的。
 *      
 *      <4> addContext()
 *          先根据hostName来查找要添加到的mappedHost对象,然后这个mappedHost对象有一个
 *          ContextList,其实也就是一个MappedContext对象数组，然后接着就根据当年context的
 *          名字，创建一个新的MappedContext对象根据context的path的排序加入到contextList
 *          数组里面.
 *          
 *          
 *          
 *          
 *  <四> Mapper的使用--请求路径的路由映射
 *  
 *      入口 : map(MessageBytes host,MessageBytes uri,MappingData mappingData);
 *          示例输入:  (localhost , /examples/index.html , mappingData)
 *          mappingData用于保存这次 mapping的结果
 *          
 *          调用  internalMap(CharChunk host, CharChunk uri,MappingData mappingData)
 *      
 *      
 *      internalMap()的内部:
 *      
 *      Context[] contexts = null;
 *      Context context = null;
 *      
 *      <1> 找到相应的host,并取出这个host的contextList,保存在mappingData
 *              Host[] hosts = this.hosts;
 *              int pos = findIgnoreCase(hosts, host);
 *              if ((pos != -1) && (host.equalsIgnoreCase(hosts[pos].name))) {
 *                  mappingData.host = hosts[pos].object;
 *                  contexts = hosts[pos].contextList.contexts;
 *              }
 *              
 *      <2> 找到相应的context(即在contexts[]数组中找到name为"examples"的一项),
 *          保存在context变量且保存在mappingData
 *              ...
 *              context = contexts[pos];
 *              
 *              mappingData.context = context.object;
 *              mappingData.contextPath.setString(context.name);
 *              
 *      <3> 找出相应的wrapper
 *          (1) 按照精确路径来找(即context的exactWrappers)
 *              
 *          (2) 按照通配路径(/*)来找(即context的exactWrappers)
 *      
 *          (3) 按照扩展路径(*.)来找(即context的extensionWrappers)
 *          
 *          (4) 都找不到才交给defaultWrapper处理
 *          
 *                  // 默认是StandardWrapper[default]
 *                  mappingData.wrapper = context.defaultWrapper.object;
 *                  // 这里requestPath = "/index.html"
 *                  mappingData.requestPath.setChars(
 *                      path.getBuffer(), path.getStart(), path.getLength());        
 *                  
 *                  
 *                  
 */
public class Mapper {

}
