package tomcatSrc;

/**
 *   JIoEndpoint是Http11Protocol中负责接收处理socket的主要模块
 *   
 *   <一> JIoEndpoint的组成部分
 *   
 *   
 *   
 *   <二> JIoEndpoint的初始化(init()和start()方法)
 *      
 *      <1> serverSocket的创建
 *          ServerSocket serverSocket = new ServerSocket (port, backlog);
 *   
 *      <2> Acceptor线程的创建, WorkerStack(Worker线程栈)的创建
 *      
 *          //maxThreads默认为200
 *          WorkerStack workers = new WorkerStack(maxThreads); 
 *       -->
 *          WorkerStack是JIoEndpoint的内部类,作为Worker线程的栈:
 *          {
 *              Worker[] workers ;
 *              int end = 0;
 *          }
 *          
 *          Thread acceptorThread = new Thread(new Acceptor(), 
 *                                      getName() + "-Acceptor-" + i);
 *          acceptorThread.setDaemon(true);
 *          acceptorThread.start();
 *          
 *          
 *          
 *   <三> JIoEndpoint组件之间的关系
 *   
 *   【Acceptor与Worker的交互】 
 *      (1) 前提:
 *          请求到来,Acceptor接受请求并得到Socket
 *          
 *      (2) Acceptor将得到的Socket交给Worker线程。
 *          <1> 先要成功获取到可用的Worker线程
 *              
 *              getWorkerThread().assign(socket);
 *              
 *              getWorkerThread()取出一个工作线程:
 *              
 *              ``````````````````````````````
 *              // 如果不能成功的从WorkerStack中获取到可用的Worker线程,就需要阻塞
 *              getWorkerThread() {
 *                  synchronized (workers) {
 *                      Worker workerThread;
 *                      while ((workerThread = createWorkerThread()) == null) {
 *                          try {
 *                              workers.wait();
 *                          }
 *                          catch (InterruptedException e) {
 *                          }
 *                      }
 *                  }
 *                  return workerThread;s
 *              }
 *              
 *              // 而从阻塞中唤醒的条件当然是有可用的Worker线程(线程处理完归还给Stack)
 *              recycleWorkerThread(Worker workerThread) {
 *                  synchronized (workers) {
 *                      workers.push(workerThread);
 *                      workers.notify();
 *                  }
 *              }
 *              
 *              ``````````````````````````````
 *              
 *              
 *           <2> assign负责把Socket交给Worker线程
 *              通过调用Worker的assign()方法:
 *              
 *                  // 对当前Worker线程对象加锁
 *                  // 检查available的值,如果available为true就调用wait()方法
 *                  synchronized void assign(Socket socket) {
 *                      while (available) {
 *                          try {
 *                              wait();
 *                          }
 *                          catch (InterruptedException e) {}
 *                      }
 *                      
 *                      // (Acceptor线程)把得到的socket保存在这个Worker线程并调用
 *                      // notifyAll()唤醒阻塞于wait（）方法的线程(指的就是Worker线程)
 *                      this.socket = socket;
 *                      available = true;
 *                      notifyAll();
 *                  }
 *                  
 *                  
 *             Worker线程的run()方法,首先做的是等待有socket需要处理:
 *             
 *                  Socket socket = await();
 *                  
 *                  // 对当前Worker线程对象加锁
 *                  // 检查available的值,如果available为false就调用wait()方法
 *                  synchronized Socket await() {
 *                      while (!available) {
 *                          try {
 *                              wait();
 *                          }
 *                          catch (InterruptedException e) {}
 *                      }
 *                      
 *                      // 将保存在Worker的成员变量socket返回
 *                          (随后作为参数传入Http11Processor的process()方法)
 *                      // 调用notifyAll()唤醒阻塞于wait（）方法的线程
 *                          (指在assign()方法阻塞的Acceptor线程)
 *                      Socket socket = this.socket;
 *                      available = false;
 *                      notifyAll();
 *                  }
 *          
 *          
 */

public class JIoEndpoint {

}
