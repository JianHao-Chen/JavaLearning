package jdkSrc.net.customProtocolMessage;

import java.io.IOException;

public interface VoteMsgCoder {

    /**
     * toWrie（）方法用于根据一个特定的协议，将投票消息转换成一个字节序列
     */
    byte[] toWire(VoteMsg msg) throws IOException;
 
    /**
     *  fromWire（）方法则根据相同的协议，对给定的字节序列进行解析，
     *  并根据信息的内容返回一个该消息类的实例。 
     */
    VoteMsg fromWire(byte[] input) throws IOException;
}
