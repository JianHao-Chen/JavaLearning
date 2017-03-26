package tomcatTest;

import java.io.*;
import java.net.*;

public class SendHttpRequest {

    public static void main(String[] args) throws Exception {
        String host = "127.0.0.1";
        int port = 8080;
        Socket socket = new Socket(host, port);
        socket.setReceiveBufferSize(1);
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        
//        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        
        StringBuffer sb = new StringBuffer();
        
        //请求的连接地址
        sb.append("GET /examples/servlets/servlet/CookieExample HTTP/1.1\r\n")
        .append("Host: localhost:8088\r\n")
        .append("Connection: Keep-Alive\r\n")
        .append("\r\n");
        
        out.write(sb.toString());
        out.flush();
        
        //打印响应
        InputStream in = socket.getInputStream();
        int bufferSize = socket.getReceiveBufferSize();
        int r;
        while((r=in.read())!=-1)
            System.out.println((char)r);
    }
}
