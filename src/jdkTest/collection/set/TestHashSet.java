package jdkTest.collection.set;

import jdkSrc.collection.set.HashSet;

public class TestHashSet {

    public static void main(String[] args){
        HashSet<String> set = new HashSet<String>();
        set.add("1234");
        set.add("3456");
        
        for(String s : set){
            s.intern();
        }
    }
}
