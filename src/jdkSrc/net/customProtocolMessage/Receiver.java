package jdkSrc.net.customProtocolMessage;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Receiver {

    public static void main(String[] args) throws IOException{
        InetAddress address=InetAddress.getLocalHost();
        ServerSocket ss = new ServerSocket(1234);
        
        Socket s = ss.accept();
        
        InputStream in = s.getInputStream();
        byte[] bs = new byte[1000];
        int hasRead = 0; 
        hasRead = in.read(bs);
        VoteMsgTextCoder coder = new VoteMsgTextCoder();
        VoteMsg voteMsg = coder.fromWire(bs);
        
        System.out.println(voteMsg);
    }
}
