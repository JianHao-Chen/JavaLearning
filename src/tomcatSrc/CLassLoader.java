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
 *                  // 将Class的字节数组转换为 Class对象
 *                  
 *                  clazz = defineClass(name, entry.binaryContent, 0,
 *                      entry.binaryContent.length,
 *                      new CodeSource(entry.codeBase, entry.certificates));
 *                  
 *                  entry.loadedClass = clazz;
 *                  entry.binaryContent = null;
 *                  ......
 *                  
 *              ``````````````````````````````````````````````````
 *              
 *              findResourceInternal(String name, String path)方法:
 *              作用是在本地的repositories里面找
 *              `````````````````````````
 *                  // resourceEntries是 HashMap,作为缓存
 *                  ResourceEntry entry = (ResourceEntry) resourceEntries.get(name);
 *                  if (entry != null)
 *                      return entry;
 *                  
 *                  // 缓存没有,就在自己的repositories里面找
 *                  for (i = 0; (entry == null) && (i < repositoriesLength); i++) {
 *                      
 *                      String fullPath = repositories[i] + path;
 *                        // 得到fullPath为 : " /WEB-INF/classes/CookieExample.class "
 *                      
 *                      // resources是DirContext对象(directory context的意思,目录上下文),
 *                      // 代表这个Webapp的resources
 *                      Object lookupResult = resources.lookup(fullPath);
 *                        // 得到lookupResult为FileResource对象
 *                        
 *                      ResourceAttributes attributes =
 *                          (ResourceAttributes) resources.getAttributes(fullPath);
 *                        // 得到attributes为FileResourceAttributes对象
 *                        
 *                      // 创建entry对象,并把资源的绝对路径保存在entry里面
 *                      entry = findResourceInternal(files[i], path);
 *                      
 *                      contentLength = (int) attributes.getContentLength();
 *                      entry.lastModified = attributes.getLastModified();
 *                      
 *                      // 做的仅仅是为resource所代表的文件打开FileInputStream,并把
 *                      // 引用赋给了resource里面的 inputStream
 *                      InputStream binaryStream = resource.streamContent();
 *                      
 *                      // 读取文件内容, 并把字节数组的引用保存在entry
 *                      byte[] binaryContent = new byte[contentLength];
 *                      int pos = 0;
 *                      while (true) {
 *                          int n = binaryStream.read(binaryContent, pos,
 *                                  binaryContent.length - pos);
 *                          if (n <= 0) break;
 *                          pos += n;        
 *                      }
 *                      
 *                      entry.binaryContent = binaryContent;
 *                      
 *                      
 *                      // 将这个entry保存在缓存里面
 *                      synchronized (resourceEntries) {
 *                          ResourceEntry entry2 = (ResourceEntry) resourceEntries.get(name);
 *                          if (entry2 == null)
 *                              resourceEntries.put(name, entry);
 *                          else
 *                              entry = entry2;
 *                          return entry;
 *                      }
 *                  }
 *              `````````````````````````
 *              
 *        <e> 委托给父类加载器
 *        
 *          if (!delegateLoad) {    // delegateLoad默认是false
 *              ClassLoader loader = parent;
 *              clazz = loader.loadClass(name);
 *              ...
 *          }
 *        
 *  
 *-------------------------------------------------------------------------
 *
 *    classLoader的 “reload”
 *    
 *        
 *    【配置】:
 *    
 *      在 server.xml 里面, Context标签 中的 “reloadable”变量设置为 true ,
 *      则这个classloader的仓库里面的资源发送改变, 资源会被reload.
 *      
 *          ``
 *          <Context docBase="examples" path="/examples" reloadable="true"> 
 *          ``
 *    
 *    【backgroundProcess】
 *    
 *      (1) WebappLoader的backgroundProcess()方法被调用
 *      (2) 调用WebappClassLoader的modified()方法,检查每一个resources的
 *          lastModifiedDate。
 *      (3) 如果仓库中有任何一个文件被改动了,就调用StandardContext的reload()方法。
 *      
 *      
 *    【modified()方法】
 *    
 *      (1) WebappClassLoader有以下2个属性:
 *      
 *          long[] lastModifiedDates;   // 资源的上次修改时间
 *          String[] paths;             // 记录资源的名称
 *      
 *      (2) 对资源是否被修改的判断:
 *      
 *        for(...){
 *          long lastModified = ((ResourceAttributes) 
 *                  resources.getAttributes(paths[i])).getLastModified();
 *          if (lastModified != lastModifiedDates[i]) {
 *              return (true);
 *          }
 *          ...
 *        }
 *        
 *        
 *        
 *     【reload()方法】
 *     
 *       (1) 暂时停止接受请求,通过2部分实现
 *           <1> 当前线程(backgroundProcess)将StandardContext的 paused字段
 *               置为true.
 *           <2> 在StandardContextValve的invoke()方法里面,会对当前的Context是否
 *               暂停进行判断,如果是(这个处理请求的线程)就进入睡眠。
 *           
 *               ```
 *                  boolean reloaded = false;
 *                  while (context.getPaused()) {
 *                      reloaded = true;
 *                      try { 
 *                          Thread.sleep(1000);
 *                      } 
 *                      catch (InterruptedException e) {;}
 *                  }
 *                  
 *               ```
 *               
 *       (2) 停止这个StandardContext (stop()方法)
 *           <1> 设置这个Context的available标志为 false
 *           <2> 设置这个Context下的所有StandardWrapper的available标志为 false
 *           <3> 执行这个Context所关联的manager的stop()
 *               --> 当然要Expire所有sessions,并且把有效的session持久化
 *           <4> 执行这个Context所关联的pipeline的stop(),解除pipeline上所有的Valve
 *              (Context的start()方法会导致相关配置文件的重新读取解析,Valve的配置可能
 *               会变动)。
 *           <5> 将StandardContext的resources引用置null,另相关对象被回收。
 *           
 *   【!重要!】   <6> WebappLoader的stop()
 *   
 *              调用WebappClassLoader的stop()方法:
 *                 {
 *                   clearReferences(); 
 *                   started = false;
 *                  
 *                   将以下的属性置为null:
 *                      File[] files;
 *                      HashMap resourceEntries; (先调用 clear())
 *                      DirContext resources; (实际上是ProxyDirContext类型)
 *                      String[] repositories;
 *                      ...
 *                 }
 *                
 *                  clearReferences()方法清除的内容:
 *                  {
 *                    -> 注销仍然存在的JDBC drivers
 *                    -> 清除ThreadLocal的引用
 *                    ...
 *                  }  
 *           
 *           
 *       (3) 启动这个StandardContext (start()方法) 
 *           当然是启动StandardContext下的各个组件。
 *           --> WebappLoader:
 *                  需要新建WebappClassLoader（新建！新建！新建！）
 *                  需要重新设置Repositories
 *           --> pipeline及其Valve
 *           
 */

public class CLassLoader {

}
