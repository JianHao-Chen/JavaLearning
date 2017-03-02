package jdkTest.collection.list;

import jdkSrc.collection.list.ArrayList;

public class TestArrayList {

    public static void main(String[] args){
        ArrayList<String> list = new ArrayList<String>();
        for(int i=0;i<=11;i++){
            list.add(String.valueOf(i));
        }
        
        for(String s : list){
            System.out.print(s+" ");
        }
    }
}
