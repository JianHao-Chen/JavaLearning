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
 */
public class Mapper {

}
