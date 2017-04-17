package tomcatSrc;

/**
 *   在StandardContext里面有一个field :
 *      private transient DirContext webappResources = null;
 *   
 *   在StandardContext启动的时候,会创建一个ProxyDirContext对象,用于表示这个
 *   Webapp的资源.
 *   
 *   这个ProxyDirContext主要有以下属性:
 *   {
 *      ResourceCache cache = null; // 作为资源的缓存
 *      
 *      // 是一个FileDirContext对象,代表这个Webapp的根目录上下文(如...webapps\examples)
 *      // 还有是保存一些setting的值(是否使用缓存、缓存的总大小、缓存每个Object的大小)
 *      DirContext dirContext;      
 *                                  
 *   }
 *   
 *   ResourceCache有以下属性:
 *   {
 *      CacheEntry[] cache = new CacheEntry[0];
 *      ...
 *   }
 *   
 *   CacheEntry代表一个具体的资源
 *   
 *   
 *   
 *   <1> ProxyDirContext的lookup()方法:
 *   
 *   `````````````````````````````````
 *      public Object lookup(String name){
 *          // 先在缓存中查找
 *          CacheEntry entry = cacheLookup(name);
 *          if (entry != null) {
 *              if (entry.resource != null) 
 *                  return entry.resource;
 *              else
 *                  return entry.context;
 *          }
 *          
 *          //如果 资源是 nonCacheable的,直接是dirContext来找
 *          Object object = dirContext.lookup(name);
 *          ...
 *      
 *      }
 *   
 *   `````````````````````````````````
 *
 *   <2> ProxyDirContext的cacheLookup()方法:
 *   
 *   ```````````````````````````````````
 *      
 *      // 保存了nonCacheable数组,在cacheLookup()方法里面先判断,对于
 *      // 这2个folder下面的资源是不缓存的。cacheLookup()方法会直接退出。
 *      // String[] nonCacheable = { "/WEB-INF/lib/", "/WEB-INF/classes/" };
 *      
 *      for (int i = 0; i < nonCacheable.length; i++) {
 *          if (name.startsWith(nonCacheable[i])) return (null);
 *      }
 *      
 *      // 开始在cache里面查找, cache是ResourceCache对象。
 *      CacheEntry cacheEntry = cache.lookup(name);
 *      
 *      // 在cache里面找不到就生成一个cacheEntry并把新的Entry放入cache
 *      if (cacheEntry == null) {   
 *          cacheEntry = new CacheEntry();
 *          cacheEntry.name = name;
 *          cacheLoad(cacheEntry);
 *      }
 *      
 *   ```````````````````````````````````
 *  
 *  <3> ResourceCache的lookup()方法:
 *  
 *   ```````````````````````````````````
 *      public CacheEntry lookup(String name) {
 *          // 获取当前的CacheEntry数组
 *          CacheEntry[] currentCache = cache;
 *          // 在当前CacheEntry数组中查找给定名字的 Entry
 *          int pos = find(currentCache, name);
 *          
 *          if ((pos != -1) && (name.equals(currentCache[pos].name)))
 *              cacheEntry = currentCache[pos];
 *          ....
 *          return cacheEntry;
 *      }
 *  
 *   ```````````````````````````````````
 *  
 *  <4> ProxyDirContext的cacheLoad()方法:
 *  
 *   ```````````````````````````````````
 *      // 将给定的entry加载如cache
 *      void cacheLoad(CacheEntry entry) {
 *          (1) entry是新创建的,此时仅有name属性有值
 *          
 *          // 获取对应资源的属性
 *          // dirContext是FileDirContext的对象
 *          Attributes attributes = dirContext.getAttributes(entry.name);
 *          entry.attributes = (ResourceAttributes) attributes;
 *          
 *          // 获取代表对应资源的对象
 *          // 根据找到的对象是一个文件夹还是一个具体的文件,
 *          // 对应entry的resource或context
 *          Object object = dirContext.lookup(name);
 *          if (object instanceof DirContext) 
 *              entry.context = (DirContext) object;
 *          else if (object instanceof Resource) 
 *              entry.resource = (Resource) object;
 *          
 *          
 *          // 获取资源的content
 *          if( //先作以下判断,只有符合才会去读取content:
 *              // (1)对应的资源是文件而不是文件夹
 *              // (2)这个资源关联的cache entry中content为Null,即资源从为被读取content
 *              // (3)这个资源的大小 >0 并且 < 512KB(cacheObjectMaxSize默认是512)
 *              (entry.resource != null) &&
 *              (entry.resource.getContent() == null) &&
 *              (entry.attributes.getContentLength() >= 0) &&
 *              (entry.attributes.getContentLength() <cacheObjectMaxSize * 1024)
 *            ){
 *            
 *            int length = (int) entry.attributes.getContentLength();
 *            // entry的大小是 (resource.size()+1),以 KB为单位
 *            entry.size += (entry.attributes.getContentLength() / 1024);
 *            
 *            InputStream is = entry.resource.streamContent();
 *            int pos = 0;
 *            byte[] b = new byte[length];
 *            
 *            while (pos < length) {
 *              int n = is.read(b, pos, length - pos);
 *              if (n < 0) break;
 *              pos = pos + n;
 *            }
 *            entry.resource.setContent(b);
 *          }
 *          
 *          
 *          // 将这个entry加载入cache(ResourceCache对象) 
 *          synchronized (cache) {
 *              if (
 *                  (cache.lookup(name) == null) &&
 *                  (cache.allocate(entry.size)) ){ // 是否有足够的空间加入这个entry
 *                  
 *                  cache.load(entry);  // 就是把entry加入到CacheEntry[]数组
 *              }
 *          }
 *      }
 *   ```````````````````````````````````
 *   
 *   
 *   <5> FileDirContext的getAttributes()方法:
 *   
 *   ```````````````````````````````````
 *      Attributes getAttributes(String name){
 *          
 *          // name只是相对路径(如/WEB-INF/classes),file()函数做的只是
 *          // 以Webapp的绝对路径为基础,为"name"创建一个 File对象,并作一些检查
 *          // 例如文件是否存在、可读,文件路径是否超出Webapp根目录之外...
 *          File file = file(name);
 *          
 *          return new FileResourceAttributes(file);
 *      }
 *      
 *      这个 FileResourceAttributes是FileDirContext的一个内部类,它继承于ResourceAttributes
 *      主要的属性有:
 *      {
 *          Attributes attributes;
 *          long contentLength ;
 *          long creation ;
 *          ...
 *      }
 *      
 *   ```````````````````````````````````
 *   
 *   
 *   <6> FileDirContext的lookup()方法:
 *   
 *   ```````````````````````````````````
 *      Object lookup(String name){
 *          File file = file(name);
 *          
 *          if (file.isDirectory()) {
 *              FileDirContext tempContext = new FileDirContext(env);
 *              tempContext.setXXX();
 *              result = tempContext;
 *          }
 *          else
 *              result = new FileResource(file);
 *          return result;
 *      }
 *      
 *   ```````````````````````````````````
 *   
 *------------------------------------------------------------------------
 *   
 *   关于ResourceCache需要移除失效缓存的情况:
 *   
 *   (1)
 *      在ProxyDirContext的cacheLookup()方法:
 *      
 *      ....
 *      CacheEntry cacheEntry = cache.lookup(name);
 *      if (cacheEntry == null) {
 *          // 生成新的CacheEntry并放入cache
 *          ...
 *      }
 *      else{
 *          // 找到缓存,需要validate
 *          if (!revalidate(cacheEntry)){
 *              // revalidate通过检查缓存中 ResourceAttributes的LastModified Date
 *              // 和ContentLength与现在文件的比较,不符合就返回false(文件已被删除也返回false);
 *              cacheUnload(cacheEntry.name);
 *              return (null);
 *          }
 *          else
 *              cacheEntry.timestamp = System.currentTimeMillis() + cacheTTL;
 *      }
 *      
 *      
 *   (2)
 *      ProxyDirContext的cacheUnload(String name)方法:
 *      
 *          synchronized (cache) {
 *              boolean result = cache.unload(name);
 *              return result;
 *          }
 *      
 *      ResourceCache的unload(String name)方法:
 *          
 *          CacheEntry removedEntry = removeCache(name);
 *          if (removedEntry != null) {
 *              cacheSize -= removedEntry.size;
 *              return true;
 *          }
 *          return false;
 *      
 *      removeCache()方法就是使用System.arraycopy来移除数组中的一项.
 *      
 *      
 *      
 *   
 *   
 *   
 */

public class WebappResources {

}
