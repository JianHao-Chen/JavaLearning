package tomcatSrc;

/**
 *              ***     Tomcat 总体结构         ***
 *  
 *  【Tomcat的组件】
 *  
 *  Tomcat 的心脏是两个组件：Connector(连接器) 和 Container(容器).
 *  当http请求过来了，连接器负责接收这个请求，然后转发给容器。
 *  
 *  容器即servlet容器，容器有很多层，分别是:
 *    <1> Engine
 *      最大的容器,代表一个servlet引擎
 *    <2> Host
 *      代表一个虚拟机
 *    <3> Context
 *      代表一个应用
 *    <4> Wrapper
 *      对应一个servlet
 *  
 *  而且，tomcat的实现为了统一管理连接器和容器等组件，额外添加了服务器组件（server）
 *  和服务组件（service）
 *  
 *  也就是:
 *  
 *  StandardServer -> StandardService -> StandardEngine ->
 *      StandardHost -> StandardContext -> StandardWrapper
 *  
 *-------------------------------------------------------------------------
 *
 *  【组件的生命周期】
 *  
 *  先看看Lifecycle 接口:
 *      public interface Lifecycle {
 *          public void start();
 *          public void stop();
 *      }
 *  组件只要继承这个接口并实现其中的方法就可以统一被拥有它的组件控制了.
 *  
 *  ----------------------------------------------------------------------
 *  
 *  【Valve和Pipeline】
 *  
 *  Pipeline 是一个管道， Valve是阀
 *  
 *  Engine 和 Host 都会执行这个 Pipeline，您可以在这个管道上增加任意的 Valve，
 *  Tomcat 会挨个执行这些 Valve，而且四个组件都会有自己的一套 Valve 集合。
 *  
 *  <1> 在ContainerBase里面直接定义了 Pipeline:
 *          protected Pipeline pipeline = new StandardPipeline(this);
 *  
 *  <2> 在ContainerBase的start()方法里面,会执行:
 *        ((Lifecycle) pipeline).start();
 *        
 *  <3> 在StandardEngine的构造函数里面加入了StandardEngineValve
 *      在StandardHost的构造函数里面加入了StandardHostValve
 *      
 *      
 *  StandardEngineValve 和 StandardHostValve 是 Engine 和 Host 的默认的 Valve，
 *  它们是最后一个 Valve 负责将请求传给它们的子容器，以继续往下执行。
 *  
 *      StandardEngineValve的invoke()方法:
 *          // Ask this Host to process this request
            host.getPipeline().getFirst().invoke(request, response);
 *  
 *  
 */

public class Framework {

}
