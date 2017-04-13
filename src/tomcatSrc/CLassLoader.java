package tomcatSrc;

/**
 *  <一> Tomcat的 WebappClassLoader
 *  
 *  这个classloader 加载流程:
 *      <1> 先到缓存中获取，如果缓存中有直接返回
 *      <2> 再在本地仓库加载,包括:
 *              WEB-INF/classes里的类;
 *              WEB-INF/lib里的jar.
 *      <3> 委托父装载器或者系统类装载器装载
 *                                  
 *  自定义classloader的原因:
 *      <1> 实现不同web app 的类隔离。
 *      <2> 通过自定义的缓存类提高速度。
 *          (每个由webappclassloader装载的类被视为资源，用ResourceEntry表示。)
 *  
 *  
 *  <二> WebappClassLoader的实现
 *  
 *      <1> 结构
 *      
 *          StandardContext中的 “loader” 为 WebappLoader。
 *          WebappLoader中的 “classLoader” 为 WebappClassLoader
 *      
 *      <2> start()方法
 *      
 *          StandardContext.start() --> WebappLoader.start()
 *          
 *          WebappLoader的start()方法:
 *            <a> 创建WebappClassLoader
 *              
 *            <b> 为WebappClassLoader设置 resources。
 *                这里resources就是WebappClassLoader的缓存类 
 *                  
 *            <c> 为WebappClassLoader设置 repositories。
 *            
 *                <c1> 设置 /WEB-INF/classes
 *                目的: 将folder(/WEB-INF/classes)读入WebappClassLoader的缓存
 *                
 *                ``
 *                  String classesPath = "/WEB-INF/classes";
 *                  Object object = resources.lookup(classesPath);
 *                ``
 *                其中,resources是WebappClassLoader的缓存,通过lookup()方法,
 *                返回缓存中的关于"/WEB-INF/classes"的一个FileDirContext对象,
 *                FileDirContext代表的是folder,因为"/WEB-INF/classes"是folder.
 *                
 *                <c2> 将“/WEB-INF/classes”加入到WebappClassLoader的list
 *                  
 *                    ``
 *                      String[] repositories ;
 *                      File[] files;
 *                    ``
 *   
 *      <3> loadClass(String name, boolean resolve)方法
 *      
 *        * 【加载的逻辑】 * 
 *           以StandardContext的start过程中,读取WEB-INF/web.xml,解析里面的
 *           “<servlet>”标签时,加载 "CookieExample" 为例子
 *        
 *        <a> 在自己的缓存中去找
 *        
 *              clazz = findLoadedClass0(name);
 *          
 *              findLoadedClass0(String name){
 *              ResourceEntry entry = (ResourceEntry) resourceEntries.get(name);
 *                  if (entry != null) {
 *                      return entry.loadedClass;
 *                  }
 *                  return (null);
 *              }
 *          
 *              resourceEntries只是一个HashMap而已。
 *                  HashMap resourceEntries = new HashMap();
 *          
 *              如果找不到,就继续到<b>.
 *              
 *        <b> 检查JVM中的缓存
 *        
 *              clazz = findLoadedClass(name);
 *                  (findLoadedClass是 java.lang.ClassLoader的方法)
 *              如果找不到,就继续到<c>.
 *        
 *        <c> 尝试用系统的类装载器进行装载,防止Web应用程序中的类覆盖J2EE的类
 *         
 *              clazz = system.loadClass(name);
 *              
 *              
 *        <d> 查找自己的 repositories
 *        
 *              findClassInternal()方法
 *              ``````````````````````````````````````````````````
 *                  String classPath ;  // 得到CookieExample.class
 *                  
 *                  ResourceEntry entry = findResourceInternal(classPath);
 *                  if (entry == null)
 *                      throw new ClassNotFoundException(name);
 *                      
 *                  Class clazz = entry.loadedClass;
 *                  
 *                      
 *                  
 *              ``````````````````````````````````````````````````
 *              
 *              
 *              findResourceInternal(String name, String path)方法:
 *              `````````````````````````
 *                  ResourceEntry entry = (ResourceEntry) resourceEntries.get(name);
 *                  if (entry != null)
 *                      return entry;
 *                  
 *                  for (i = 0; (entry == null) && (i < repositoriesLength); i++) {
 *                      
 *                      String fullPath = repositories[i] + path;
 *                        // 得到fullPath为 : " /WEB-INF/classes/CookieExample.class "
 *                      
 *                      Object lookupResult = resources.lookup(fullPath);
 *                        // 得到lookupResult为FileResource对象
 *                        
 *                      ResourceAttributes attributes =
 *                          (ResourceAttributes) resources.getAttributes(fullPath);
 *                        // 得到attributes为FileResourceAttributes对象
 *                      
 *                      
 *                      
 *                      
 *                  }
 *              
 *              
 *              
 *              `````````````````````````
 *              
 *              
 */

public class CLassLoader {

}
