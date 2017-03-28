package jdkSrc.net.recvBufFull;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class Server {
    public static int PORT_NUMBER = 1234;
    
    /*public static void main(String[] args) throws IOException{
        InetAddress address=InetAddress.getLocalHost();
        ServerSocket ss = new ServerSocket(30000);
        byte[] bs = new byte[1000];
        for(int i=0;i<1000;i++)
            bs[i] = (byte)3;
        
        Socket s = ss.accept();
        // 这里设置了send Buffer size, 确实会导致当慢接收时,发送端会阻塞
        // 只是设置为1,但debug时看到buffer size确比1大,可能是与底层操作系统有关。
        s.setSendBufferSize(1);
        OutputStream out = s.getOutputStream();
        PrintStream ps = new PrintStream(out);
        int len = 1000;
        for(int i=0;i<1000;i++){
            out.write(bs[i]);
        }
        ps.close();
        s.close();
    }*/
    
    public static void main(String[] args) throws Exception {
        // Allocate an unbound server socket channel
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        
        // Get the associated ServerSocket to bind it with
        ServerSocket serverSocket = serverChannel.socket();
        
        // Set the port the server channel will listen to
        serverSocket.bind(new InetSocketAddress(PORT_NUMBER));
        
        // Set nonblocking mode for the listening socket
        serverChannel.configureBlocking(true);

        byte[] bs = new byte[1000];
        for(int i=0;i<1000;i++)
            bs[i] = (byte)3;
        
        SocketChannel socket = serverChannel.accept();
        
        //socket.configureBlocking(false);
        Socket s = socket.socket();
        
        s.setSendBufferSize(1);
        OutputStream out = s.getOutputStream();
        PrintStream ps = new PrintStream(out);
        int len = 1000;
        for(int i=0;i<1000;i++){
            out.write(bs[i]);
        }
        ps.close();
        s.close();
    }
}
