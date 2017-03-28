package jdkSrc.net.customProtocolMessage;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class Sender {

    public static void main(String[] args) throws IOException{
        VoteMsg voteMsg = new VoteMsg(false,true,0,0);
        VoteMsgTextCoder coder = new VoteMsgTextCoder();
        byte[] msgSrc = coder.toWire(voteMsg);
        
        Socket socket = new Socket();
        SocketAddress remoteAddr = new InetSocketAddress("localhost",1234);
        socket.connect(remoteAddr);
        
        OutputStream out = socket.getOutputStream();
        int hasWrite = 0;
        out.write(msgSrc);
        
        System.out.println(voteMsg);
    }
}
