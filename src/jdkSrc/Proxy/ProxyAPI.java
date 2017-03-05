package jdkSrc.Proxy;

import java.lang.reflect.InvocationHandler;

/**
 *  ★   java.lang.reflect.Proxy
 *  这是 Java 动态代理机制的主类，它提供了一组静态方法来为一组接口动态地生成代理类及其对象。
 *  
 *    ◇ 方法 1    该方法用于获取指定代理对象所关联的调用处理器
 *      public static InvocationHandler getInvocationHandler(Object proxy)
 *    
 *    ◇ 方法 2    该方法用于获取关联于指定类装载器和一组接口的动态代理类的类对象
 *      static Class getProxyClass(ClassLoader loader, Class[] interfaces)
 *      
 *    ◇ 方法 3    该方法用于判断指定类对象是否是一个动态代理类
 *      static boolean isProxyClass(Class cl)
 *     
 *    ◇ 方法 4    该方法用于为指定类装载器、一组接口及调用处理器生成动态代理类实例
 *      static Object newProxyInstance(ClassLoader loader, Class[] interfaces, 
                                        InvocationHandler h)
                                        
            loader:　　
                                                    一个ClassLoader对象，定义了由哪个ClassLoader对象来对生成的代理对象进行加载
 *
 *          interfaces: 
 *                 一个Interface对象的数组，表示的是我将要给我需要代理的对象提供一组什么接口，
 *                 如果我提供了一组接口给它，那么这个代理对象就宣称实现了该接口(多态)，这样我就能调用
 *                 这组接口中的方法了
 *
 *          h:　　一个InvocationHandler对象，表示的是当我这个动态代理对象在调用方法的时候，
 *              会关联到哪一个InvocationHandler对象上
 *----------------------------------------------------------------------------
 *
 *  ★   java.lang.reflect.InvocationHandler
 *  这是调用处理器接口,它自定义了一个 invoke 方法,用于集中处理在动态代理类对象上的方法调用,
 *  通常在该方法中实现对委托类的代理访问。
 *  Object invoke(Object proxy, Method method, Object[] args) throws Throwable
 *  
 *      proxy:  指动态生成的代理对象
 *  
 *      method: 指代的是我们所要调用真实对象的某个方法的Method对象
 *      
 *      args:   指代的是调用真实对象某个方法时接受的参数
 *  
 *-----------------------------------------------------------------------------
 *
 *  ★ Proxy的源码
 *  
 *    ※※※※※※     Proxy 的重要静态变量    ※※※※※※
 *    
 *      ◎       映射表 : 用于维护类装载器对象到其对应的代理类缓存
 *      private static Map loaderToCache = new WeakHashMap();
 *      
 *      ◎       标记  :  用于标记一个动态代理类正在被创建中
 *      private static Object pendingGenerationMarker = new Object();
 *      
 *      ◎       同步表 : 记录已经被创建的动态代理类类型，主要被方法 isProxyClass 进行相关的判断
 *      private static Map proxyClasses = Collections.synchronizedMap(new WeakHashMap()); 
 *      
 *      ◎       关联的调用处理器引用
 *      protected InvocationHandler h;
 *      
 *      
 *    ※※※※※※     Proxy 的构造方法     ※※※※※※
 *    
 *      // 由于 Proxy 内部从不直接调用构造函数，所以 private 类型意味着禁止任何调用
        private Proxy() {} 
 *      
 *      // 由于 Proxy 内部从不直接调用构造函数，所以 protected 意味着只有子类可以调用
        protected Proxy(InvocationHandler h) {this.h = h;}
        
 *      
 *      ※※※※※※  Proxy的newProxyInstance 方法       ※※※※※※
 *      
 *      public static Object newProxyInstance(ClassLoader loader,
                      Class<?>[] interfaces,
                      InvocationHandler h){
             
             // 检查 h 不为空，否则抛异常
            if (h == null) { 
                throw new NullPointerException(); 
            }
            
            // 获得与制定类装载器和一组接口相关的代理类类型对象
            Class cl = getProxyClass(loader, interfaces); 
                      
            // 通过反射获取构造函数对象并生成代理类实例
            try { 
                Constructor cons = cl.getConstructor(constructorParams); 
                return (Object) cons.newInstance(new Object[] { h }); 
            } 
            catch(Exception e){}
         }
 *      
 *      ※※※※※※  Proxy的  getProxyClass 方法       ※※※※※※
 *      该方法负责为一组接口动态地生成代理类类型对象。
 *      该方法总共可以分为四个步骤:
 *      
 *      ◆   步骤1
 *        对这组接口进行一定程度的安全检查,包括检查接口类对象是否对类装载器可见并且与类装载器所能识别
 *        的接口类对象是完全相同的,还会检查确保是 interface 类型而不是 class 类型。
 *        
 *        通过 Class.forName 方法判接口的可见性
 *          try { 
                // 指定接口名字、类装载器对象，同时制定 initializeBoolean 为 false 表示无须初始化类
                // 如果方法返回正常这表示可见，否则会抛出 ClassNotFoundException 异常表示不可见
                interfaceClass = Class.forName(interfaceName, false, loader); 
            } catch (ClassNotFoundException e) { }
 *  
 *  
 *      ◆   步骤2
 *      
 *        △ 获取缓存表
 *          从 loaderToCache 映射表中获取以类装载器对象为关键字所对应的缓存表，如果不存在就创建一个新的
 *          缓存表并更新到 loaderToCache。
 *          
 *          Map cache;
            synchronized (loaderToCache) {
                cache = (Map) loaderToCache.get(loader);
                if (cache == null) {
                    cache = new HashMap();
                    loaderToCache.put(loader, cache);
                }
            }
 *          
 *        △ 从缓存表中获取接口对应的代理类的类对象引用
 *          
 *          缓存表是一个 HashMap 实例，正常情况下它将存放键值对（接口名字列表，动态生成的代理类的类对象引用）。
 *          Object key = Arrays.asList(interfaceNames);
 *          
 *          当代理类正在被创建时它会临时保存（接口名字列表，pendingGenerationMarker）。
 *          标记 pendingGenerationMarke 的作用是通知后续的同类请求（接口数组相同且组内接口排列顺序也
 *          相同）代理类正在被创建，请保持等待直至创建完成。
 *          synchronized (cache) {
                do {
                    Object value = cache.get(key);
                    if (value instanceof Reference) {
                        proxyClass = (Class) ((Reference) value).get();
                    }
                    if (proxyClass != null) {
                        // proxy class already generated: return it
                        return proxyClass;
                    } else if (value == pendingGenerationMarker) {
                        // proxy class being generated: wait for it
                        try {cache.wait();} catch (InterruptedException e) {}
                        continue;
                    } else {
                        cache.put(key, pendingGenerationMarker);
                        break;
                    }
                } while (true);
            }
 *        
 *        
 *      ◆   步骤3
 *        动态创建代理类的类对象.
 *          
 *        // 动态地生成代理类的字节码数组
          byte[] proxyClassFile = ProxyGenerator.generateProxyClass( proxyName, interfaces); 
          try { 
            // 动态地定义新生成的代理类
            proxyClass = defineClass0(loader, proxyName, proxyClassFile, 0, 
            proxyClassFile.length); 
          } catch (ClassFormatError e) { 
            throw new IllegalArgumentException(e.toString()); 
          } 

          // 把生成的代理类的类对象记录进 proxyClasses 表
          proxyClasses.put(proxyClass, null);
 *      
 *      ◆   步骤4
 *        根据结果更新缓存表，如果成功则将代理类的类对象引用更新进缓存表，否则清楚缓存表中对应关键值，
 *        最后唤醒所有可能的正在等待的线程。
 *        
 *        synchronized (cache) {
              if (proxyClass != null) {
                  cache.put(key, new WeakReference(proxyClass));
              } else {
                  cache.remove(key);
              }
              cache.notifyAll();
          }
 *
 *
 *
 *
 */
public class ProxyAPI {

}
