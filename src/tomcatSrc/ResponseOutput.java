package tomcatSrc;

import java.nio.ByteBuffer;
import java.nio.channels.Selector;

/**
 *  目的:     分析在自定义的 Servlet中调用如下代码时,tomcat的数据是如何写到客户端的:
 *  
 *      PrintWriter out = response.getWriter();
 *      out.println("XXX");
 * 
 * <一>  门面设计
 *  在用户写的Servlet的doGet()、doPost()方法中,参数为 RequestFacade、ResponseFacade。
 *  它们有着Request、Response对象的引用(connector包下)。这里用到了门面设计模式。
 *  主要是为了让用户的Servlet访问到Request、Response的数据而对于他们的一些敏感数据就隔离开来。
 *  
 * <二>  CoyoteWriter(继续于PrintWriter)
 *  response.getWriter(),得到的是CoyoteWriter对象。
 *  
 *  <1> 创建CoyoteWriter
 *          CoyoteAdapter.service() -> Connector.createResponse() ->
 *          Response.setConnector()
 *          
 *      Response.setConnector(){    // Response是 (catalina.connector.Response)
 *          this.connector = connector;
 *          outputBuffer = new OutputBuffer();
 *          outputStream = new CoyoteOutputStream(outputBuffer);
 *          writer = new CoyoteWriter(outputBuffer);
 *      }
 *   
 *  <2> PrintWriter.println(String s)
 *          PrintWriter.println() -> PrintWriter.write() ->
 *          OutputBuffer.write()
 *      此时,内容s被写到OutputBuffer的bb属性(这是一个ByteChunk)。
 *      【注意】这里内容s是作为response的<Body>！！
 *
 *  <3> 调用 response.finishResponse()
 *      当回到CoyoteAdapter.service()方法时,调用 response.finishResponse()。
 *          response.finishResponse() -> OutputBuffer.close()
 *          
 *      OutputBuffer.close() : {
 *          coyoteResponse.setContentLength();
 *          doFlush();
 *          coyoteResponse.finish();
 *      }
 *      
 *   <4> OutputBuffer.doFlush() : {
 *          <a> coyoteResponse.sendHeaders()
 *          <b> 通过coyoteResponse.hook属性(这是一个指向Http11NioProcessor的引用),
 *              执行Http11NioProcessor.prepareResponse();
 *          <c> 调用InternalNioOutputBuffer的sendStatus()方法和sendHeader()方法。
 *          
 *              sendStatus()方法写入response的“status line”: 如  "HTTP/1.1 200 OK"
 *              sendHeader()方法写入response的“header” : 如 “Content-Type : text/html”
 *          
 *          <d> 调用InternalNioOutputBuffer的commit()方法。
 *              
 *              设置coyoteResponse的committed flag 为true;
 *              调用 addToBB()方法  (BB是 ByteBuffer) 
 *              
 *          <e> 调用bb.flushBuffer()方法,(bb是ByteChunk,作为OutputBuffer的属性)
 *              这是对OutputBuffer中保存的response的 <BODY> 写到  NioChannel的 writeBuffer
 *      }
 *
 *   <5> InternalNioOutputBuffer的write(byte[] b)方法  : {
 *          将b数组通过  System.arraycopy(b, 0, buf, pos, b.length)写入 自身的buf[]数组。
 *       }
 *       
 *   <6> InternalNioOutputBuffer的addToBB()方法 : {
 *          将 自己的buf[]数组的内容写入 NioChannel的 writeBuffer(是一个ByteBuffer)
 *       }
 *
 *   <7> ByteChunk的flushBuffer()方法 : {
 *          经过一系列的调用,最终调用   InternalNioOutputBuffer的addToBB()方法
 *       }
 *
 *   <8> coyoteResponse.finish() 方法 : {
 *          // 取出NioChannel的 writeBuffer
 *          ByteBuffer bytebuffer = socket.getBufHandler().getWriteBuffer();
 *          bytebuffer.flip();
 *          writeToSocket();
 *       }
 *       
 *   <9> writeToSocket() : {
 *          最终调用 NioChannel的write()方法  --> SocketChannel.write();
 *       }
 *
 */

public class ResponseOutput {

}
