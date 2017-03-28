package jdkSrc.net.customProtocolMessage;

/**
 *  一个简单的投票协议。
 *  
 *  一个客户端向服务器发送一个请求消息，消息中包含了一个候选人的ID，范围在0~1000。
 *  
 *  程序支持两种请求:
 *  一种是查询请求，即向服务器询问候选人当前获得的投票总数，服务器发回一个响应消息，
 *  包含了原来的候选人ID和该候选人当前获得的选票总数；
 *  
 *  另一种是投票请求，即向指定候选人投一票，服务器对这种请求也发回响应消息，
 *  包含了候选人ID和获得的选票数（包含了刚刚投的一票）。 
 *
 */


public class VoteMsg {
    private boolean isInquiry; // true if inquiry; false if vote  
    private boolean isResponse;// true if response from server  
    private int candidateID;   // in [0,1000]  
    private long voteCount;    // nonzero only in response
    
    public static final int MAX_CANDIDATE_ID = 1000;
    
    public VoteMsg(boolean isResponse, boolean isInquiry, int candidateID, long voteCount)  
    throws IllegalArgumentException {
        
        if (voteCount != 0 && !isResponse) {
            throw new IllegalArgumentException("Request vote count must be zero");
        }
        if (candidateID < 0 || candidateID > MAX_CANDIDATE_ID) { 
            throw new IllegalArgumentException("Bad Candidate ID: " + candidateID);
        }
        if (voteCount < 0) {
            throw new IllegalArgumentException("Total must be >= zero");
        }
        this.candidateID = candidateID;  
        this.isResponse = isResponse;  
        this.isInquiry = isInquiry;  
        this.voteCount = voteCount;
    }
    
    public void setInquiry(boolean isInquiry) {  
        this.isInquiry = isInquiry;  
    }  
    
    public void setResponse(boolean isResponse) {  
      this.isResponse = isResponse;  
    }  
    
    public boolean isInquiry() {  
      return isInquiry;  
    }  
    
    public boolean isResponse() {  
      return isResponse;  
    }  
    
    public void setCandidateID(int candidateID) throws IllegalArgumentException {  
      if (candidateID < 0 || candidateID > MAX_CANDIDATE_ID) {  
        throw new IllegalArgumentException("Bad Candidate ID: " + candidateID);  
      }  
      this.candidateID = candidateID;  
    }  
    
    public int getCandidateID() {  
      return candidateID;  
    }  
    
    public void setVoteCount(long count) {  
      if ((count != 0 && !isResponse) || count < 0) {  
        throw new IllegalArgumentException("Bad vote count");  
      }  
      voteCount = count;  
    }  
    
    public long getVoteCount() {  
      return voteCount;  
    }  
    
    public String toString() {  
      String res = (isInquiry ? "inquiry" : "vote") + " for candidate " + candidateID;  
      if (isResponse) {  
        res = "response to " + res + " who now has " + voteCount + " vote(s)";  
      }  
      return res;  
    }  
}  