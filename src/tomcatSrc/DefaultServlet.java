package tomcatSrc;

/**
 *  DefaultServlet是Tomcat提供的一个用于处理静态资源的Servlet。
 *  这里只分析对于静态资源的2种返回方式。
 *
 *  <1> 将文件内容copy到output stream
 *  
 *      if (!checkSendfile(request, response, cacheEntry, contentLength, null))
 *          copy(cacheEntry, renderResult, ostream);
 *
 *      通过checkSendfile()方法判断是否使用Sendfile功能,如果条件不满足,就把内容
 *      copy到output stream。
 *      
 *      copy()方法如下:
 *          
 *          copy(CacheEntry cacheEntry, InputStream is,
 *              ServletOutputStream ostream){
 *              
 *              if (cacheEntry.resource != null) {
 *                  byte buffer[] = cacheEntry.resource.getContent();
 *                  if (buffer != null) {
 *                      ostream.write(buffer, 0, buffer.length);
 *                      return;
 *                  }
 *              }
 *              ....
 *          }
 *  
 *        
 *  <2> 使用 Sendfile功能
 *  
 *      先看 checkSendfile中对于满足使用Sendfile功能的条件: 
 *      
 *      <a> sendfileSize>0 && length > sendfileSize
 *              sendfileSize默认是48KB , length是文件大小
 *          
 *      <b> entry.attributes.getCanonicalPath() != null
 *          这里的path是文件的绝对路径。
 *          
 *      如果条件满足,就在request里面添加以下属性,并且返回 true:
 *      
 *          request.setAttribute("org.apache.tomcat.sendfile.filename", 
 *                  entry.attributes.getCanonicalPath());
 *                  
 *          request.setAttribute("org.apache.tomcat.sendfile.start", new Long(0L));
 *          request.setAttribute("org.apache.tomcat.sendfile.end", new Long(length));
 *        (其实还有一个Range range参数,可以用于指定发送文件的开始、结束位置)
 *      否则返回 false;
 *     
 *     
 *      再看对这个新加的属性的处理:

 *          从 response.finishResponse()开始,最后调用到Http11NioProcessor
 *          的 prepareResponse()方法,里面会处理 sendfile属性:
 *          
 *          为 Http11NioProcessor中的属性sendfileData赋值(filename,start,end)
 *      
 *      随后, Response的状态行(HTTP协议版本号， 状态码， 状态消息)的生成和写入
 *      SocketChannel的byteBuffer.  这一步与平常的一样。
 *      
 *      接着,本来是应该将response中的content写入SocketChannel的byteBuffer。
 *      但是这是response中的content为空,因为会留到后面再写。
 *      
 *      当从adapter.service(request, response)返回到Http11NioProcessor,
 *      真正开始实现Sendfile()功能:
 *          
 *              //省略部分代码
 *          SendfileData sd = attachment.getSendfileData();
 *          File f = new File(sd.fileName);
 *          sd.fchannel = new FileInputStream(f).getChannel();
 *          
 *          NioChannel sc = attachment.getChannel();
 *          WritableByteChannel wc =(WritableByteChannel)sc.getIOChannel();
 *      
 *          long written = sd.fchannel.transferTo(sd.pos,sd.length,wc);
 *      
 *      
 *      
 *      
 *      FileChannel的transferTo方法会利用操作系统的sendfile系统调用来将
 *      磁盘文件输出到网络。
 *      
 *      【 ** 原理  **】 (用到了Linux中所谓的"零拷贝"特性)
 *      
 *      <A> 使用read和write方式的时候，将文件输出到网络所涉及的操作:
 *          
 *          read(file, tmp_buf, len);
 *          write(socket, tmp_buf, len);
 *      
 *        <A1> 系统调用read导致了从用户空间到内核空间的上下文切换。DMA模块从
 *             磁盘中读取文件内容，并将其存储在内核空间的缓冲区内，完成了第1次复制。
 *             
 *        <A2> 数据从内核空间缓冲区复制到用户空间缓冲区，之后系统调用read返回，这导致
 *             了从内核空间向用户空间的上下文切换。此时，需要的数据已存放在指定的用户
 *             空间缓冲区内(参数tmp_buf)，程序可以继续下面的操作。
 *             
 *        <A3> 系统调用write导致从用户空间到内核空间的上下文切换。数据从用户空间缓冲区
 *             被再次复制到内核空间缓冲区，完成了第3次复制。不过，这次数据存放在内核空间
 *             中与使用的socket相关的特定缓冲区中，而不是步骤一中的缓冲区。
 *            
 *        <A4> 系统调用返回，导致了第4次上下文切换。第4次复制在DMA模块将数据从内核空间
 *             缓冲区传递至协议引擎的时候发生，这与我们的代码的执行是独立且异步发生的。
 *            
 *            
 *      <B> 使用Sendfile系统调用，将文件输出到网络所涉及的操作:
 *      
 *              // sendfile系统调用的引入，不仅减少了数据复制，还减少了上下文切换的次数。
 *              sendfile(socket, file, len);
 *              
 *         
 *        <B1> sendfile系统调用导致文件内容通过DMA模块被复制到某个内核缓冲区，之后再被
 *             复制到与socket相关联的缓冲区内。
 *             
 *        <B2> 当DMA模块将位于socket相关联缓冲区中的数据传递给协议引擎时，执行第3次复制。
 *      
 *      
 *        ！！ 特别的,如果网络适配器支持聚合操作特性,那么Sendfile所涉及的操作:
 *        
 *        <B1*> sendfile系统调用导致文件内容通过DMA模块被复制到内核缓冲区中。 
 *        
 *        <B2*> 数据并未被复制到socket关联的缓冲区内。取而代之的是，只有记录数据
 *              位置和长度的描述符被加入到socket缓冲区中。DMA模块将数据直接从内核
 *              缓冲区传递给协议引擎，从而消除了遗留的最后一次复制。
 *          
 *      
 *      
 */

public class DefaultServlet {

}
