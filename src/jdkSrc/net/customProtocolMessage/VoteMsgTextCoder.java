package jdkSrc.net.customProtocolMessage;

import java.io.ByteArrayInputStream;  
import java.io.IOException;  
import java.io.InputStreamReader;  
import java.util.Scanner; 

/**
 *   VoteMsgTextCoder用文本方式对消息进行编码的Class.
 *   该协议指定使用ASCII字符集对文本进行编码。
 *   消息的开头是一个所谓的”魔术字符串“，即一个字符序列，用于快速将投票协议的消息和网络中
 *   随机到来的垃圾消息区分开.
 *   投票/查询布尔值被编码为字符形似，‘v’代表投票消息，‘i’代表查询消息。
 *   是否为服务器发送的响应消息，由字符‘R’指示，状态标记后面是候选人ID，其后跟的是选票总数，
 *   它们都编码成十进制字符串。
 */

public class VoteMsgTextCoder implements VoteMsgCoder{

    /* 
     * Wire Format "VOTEPROTO" <"v" | "i"> [<RESPFLAG>] <CANDIDATE> [<VOTECNT>] 
     * Charset is fixed by the wire format. 
     */ 
    
    
    // Manifest constants for encoding  
    public static final String MAGIC = "Voting";  
    public static final String VOTESTR = "v";  
    public static final String INQSTR = "i";  
    public static final String RESPONSESTR = "R";  
    
    public static final String CHARSETNAME = "US-ASCII";  
    public static final String DELIMSTR = " ";  
    public static final int MAX_WIRE_LENGTH = 2000;
    
    public byte[] toWire(VoteMsg msg) throws IOException {
        String msgString = MAGIC + DELIMSTR + (msg.isInquiry() ? INQSTR : VOTESTR)  
            + DELIMSTR + (msg.isResponse() ? RESPONSESTR + DELIMSTR : "")  
            + Integer.toString(msg.getCandidateID()) + DELIMSTR  
            + Long.toString(msg.getVoteCount());  
        byte data[] = msgString.getBytes(CHARSETNAME);  
        return data;  
    }
    
    public VoteMsg fromWire(byte[] message) throws IOException {
        ByteArrayInputStream msgStream = new ByteArrayInputStream(message);
        Scanner s = new Scanner(new InputStreamReader(msgStream, CHARSETNAME));
        boolean isInquiry;  
        boolean isResponse;  
        int candidateID;  
        long voteCount;  
        String token;  
        
        try { 
            token = s.next();
            if (!token.equals(MAGIC)) {
                throw new IOException("Bad magic string: " + token);
            }
            token = s.next();
            if (token.equals(VOTESTR)) {
                isInquiry = false;
            }
            else if (!token.equals(INQSTR)) { 
                throw new IOException("Bad vote/inq indicator: " + token);
            }
            else {  
                isInquiry = true;  
            }
            
            token = s.next();
            if (token.equals(RESPONSESTR)) {
                isResponse = true;
                token = s.next();
            }
            else {  
                isResponse = false;  
            }
            
            candidateID = Integer.parseInt(token);
            if (isResponse) {
                token = s.next();
                voteCount = Long.parseLong(token);
            }
            else {  
                voteCount = 0;  
            }
        }
        catch (IOException ioe) {  
            throw new IOException("Parse error...");  
        }
        return new VoteMsg(isResponse, isInquiry, candidateID, voteCount);
    }
    
}
