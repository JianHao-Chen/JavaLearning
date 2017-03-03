package jdkTest.collection.map;

import java.util.Iterator;

import jdkSrc.collection.map.LinkedHashMap;
import jdkSrc.collection.map.Map;

public class TestLinkedHashMap {

    public static void main(String[] args){
        LinkedHashMap<String,String> map = new LinkedHashMap<String,String>();
        for(int i=1;i<=15;i++){
            String s = String.valueOf(i);
            map.put(s, s);
        }
        Iterator it = map.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry entry = (Map.Entry)it.next();
            System.out.print(entry+" ");
            if(entry.getKey().equals("3")){
                it.remove();
            }
        }
    }
}
