package jdkTest.collection.list;

import java.util.Iterator;

import jdkSrc.collection.list.LinkedList;

public class TestLinkedList {

    public static void main(String[] args){
        LinkedList<String> list = new LinkedList<String>();
        for(int i=0;i<=5;i++){
            list.add(String.valueOf(i));
        }
        
        Iterator itor = list.descendingIterator();
        while(itor.hasNext()){
            System.out.print(itor.next()+" ");
        }
        
    }
}
