package tomcatSrc;

/**
 *   InternalNioInputBuffer类的作用(作为Http11NioProcessor对象里面的成员):
 *   
 *   <1> 
 *   提供读取数据的方法、保存数据的字节数组。
 *   <2>
 *   提供解析请求行(RequestLine)，头部（Headers）。
 *   
 *   
 *---------------------------------------------------------------------------   
 *   
 *  <一> InternalNioInputBuffer的属性:
 *   
 *      protected Request request;      //与当前buffer关联的tomcat的request
 *      protected MimeHeaders headers;  //用于保存所有的处理出来的header数据
 *      protected byte[] buf;           //用于存数据的字节数组
 *      protected int lastValid;        //表示最后一个可用数据的下标
 *      protected int pos;              //当前读取到的下标  
 *      protected int end;              //用于指向header在buffer的尾部的下标
 *      
 *      //这里内部有一个InputBuffer，用于代理当前对象的一些方法，主要是暴露给外部的读取数据的api
 *      protected InputBuffer inputStreamInputBuffer;
 *      其中,当InternalNioInputBuffer对象初始化时,
 *          inputStreamInputBuffer = new SocketInputBuffer();
 *      
 *      这里,SocketInputBuffer是InternalNioInputBuffer的内部类,它的意义是:
 *      
 *          为了代理当前对象的读取数据的方法，将方法暴露给外面的对象 。
 *          而不是直接将InternalNioInputBuffer的读取数据的方法暴露给外面的对象。
 *          
 *      
 *
 *
 *  <二> InternalNioInputBuffer的readSocket()方法(从底层的socket读取数据 ):
 *      {
 *          // 调用socket关联的nio的buf的clear方法，待会可以将socket里读取的数据写进去了
 *          socket.getBufHandler().getReadBuffer().clear();
 *          // 非阻塞的读数据,实际上调用的是SocketChannel的read()方法。
 *          nRead = socket.read(socket.getBufHandler().getReadBuffer());
 *          // 调用ByteBuffer的flip()方法
 *          socket.getBufHandler().getReadBuffer().flip();
 *          // 将ByteBuffer里面的数据转移到buf[]数组。
 *          socket.getBufHandler().getReadBuffer().get(buf,pos,nRead);
 *      }
 *      
 *   
 *   
 *
 *
 *
 */

public class InternalNioInputBuffer {

}
