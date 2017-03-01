package jdkTest.collection.map;

import jdkSrc.collection.map.HashMap;
import jdkSrc.collection.map.Map;

public class TestHashMap {

    public static void main(String[] args){
        HashMap<String,String> map = new HashMap<String,String>();
        
        map.put("1", "A");
        map.put("2", "B");
        map.put("1", "C");
        
        for(Map.Entry<String,String> entry : map.entrySet()){
            entry.getKey();
        }
        
    }
}
