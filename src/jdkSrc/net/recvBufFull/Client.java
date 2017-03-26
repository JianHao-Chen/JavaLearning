package jdkSrc.net.recvBufFull;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

public class Client {

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket();
        // 这个read的buff size设置为1是为了让接收端缓冲满
        // 导致发送端阻塞。
        socket.setReceiveBufferSize(1);
        
        SocketAddress remoteAddr = new InetSocketAddress("localhost",1234);
        socket.connect(remoteAddr);
        
        InputStream in = socket.getInputStream();
        int r = 0;
        while((r=in.read())!=-1){
            System.out.print((char)r);
        }
        
    }
}
