package tomcatSrc;


/**
 *  //加载MySQL 数据库驱动
 *  Class.forName("com.mysql.jdbc.Driver");
 *  
 *  Class.forName()将对应的驱动类加载到内存中，然后执行内存中的static静态代码段，
 *  代码段中，会创建一个驱动Driver的实例，放入DriverManager中，供DriverManager使用。
 *  
 *  
 *  DriverManager的作用:
 *      注册和删除加载的驱动程序
 *      根据给定的url获取符合url协议的驱动Driver
 *      建立Conenction连接，进行数据库交互
 *      
 *  
 *  在Tomcat里面,使用JdbcLeakPrevention来处理
 *      “由Webapp注册到DriverManager,但退出时却没有从DriverManager清理的JDBC驱动”。
 *  
 *  
 *  JdbcLeakPrevention的clearJdbcDriverRegistrations()方法:
 *  
 *          Enumeration<Driver> drivers = DriverManager.getDrivers();
 *          while (drivers.hasMoreElements()) {
 *              Driver driver = drivers.nextElement();
 *              if (driver.getClass().getClassLoader() !=
 *                  this.getClass().getClassLoader()) {
 *                  continue;
 *              }
 *              DriverManager.deregisterDriver(driver);
 *          }
 *   
 *   
 *   对JdbcLeakPrevention的使用:
 *   <1> 不能直接生成一个JdbcLeakPrevention的实例,因为这样会导致:
 *          JdbcLeakPrevention的加载会由   加载当前WebappClassLoader的类加载器加载,
 *          经debug , 是 sun.misc.Launcher$AppClassLoader
 *          
 *       这样得到的JdbcLeakPrevention,我们调用它的clearJdbcDriverRegistrations()
 *       是不会得到预期效果的。
 *      
 *   <2> 我们使用JdbcLeakPrevention的原理就是要当前Webapp的 class loader来
 *      加载这个JdbcLeakPrevention,然后在clearJdbcDriverRegistrations()方法
 *      里面会对DriverManager中的drivers进行判断,如果driver和这个JdbcLeakPrevention
 *      的类加载器(即当前这个正在stop的Webapp的 class loader)相同,就调用:
 *      DriverManager的deregisterDriver()方法。
 *      
 *   <3> 具体的代码:
 *   
 *       clearReferencesJdbc() {
 *       
 *          // getResourceAsStream()方法与loadClass方法相似,
 *          // (1) 在自己(WebappClassLoader)中的缓存(resourceEntries)找。
 *          // (2) 在自己的仓库(WEB-INF/class)中找
 *          // (3) 委托给父类加载器
 *          
 *          //      JdbcLeakPrevention的class文件最后是由
 *          //      sun.misc.Launcher$AppClassLoader找到
 *          InputStream is = getResourceAsStream(
 *              "org/apache/catalina/loader/JdbcLeakPrevention.class");
 *          
 *          
 *          // 读取class文件
 *          byte[] classBytes = new byte[2048];
 *          int offset = 0;
 *          int read = is.read(classBytes, offset, classBytes.length-offset);
 *       
 *       
 *          // Define Class
 *          // 使得到的obj是由WebappClassLoader加载的。
 *          Class<?> lpClass =
 *              defineClass("org.apache.catalina.loader.JdbcLeakPrevention",
 *              classBytes, 0, offset, this.getClass().getProtectionDomain());
 *          Object obj = lpClass.newInstance();
 *          
 *          
 *          // 通过反射调用clearJdbcDriverRegistrations()
 *          List<String> driverNames = (List<String>) obj.getClass().
 *              getMethod("clearJdbcDriverRegistrations").invoke(obj);
 *       }
 */

public class JdbcLeakPrevention {

}
