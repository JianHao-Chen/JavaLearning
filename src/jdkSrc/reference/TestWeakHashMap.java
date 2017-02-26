package jdkSrc.reference;

import java.util.WeakHashMap;

class Key {
	String id;   
	  
    public Key(String id){   
        this.id = id;   
    }
    public String toString(){   
        return id;   
    }
    public int hashCode(){   
        return id.hashCode();   
    }
    public boolean equals(Object r){   
        return (r instanceof Key) && id.equals(((Key) r).id);   
    }
    public void finalize(){   
        System.out.println("Finalizing Key " + id);   
    }
}

class Value{
    String id;   
    public Value(String id){   
        this.id = id;   
    }
    public String toString(){   
        return id;   
    }
    public void finalize(){   
        System.out.println("Finalizing Value " + id);   
    }
}

/**
 *	在WeakHashMap中存放了键对象的弱引用，当一个键对象被垃圾回收，
 *	那么相应的值对象的引用会从Map中删除。
 *
 *	WeakHashMap能够节约存储空间，可用来缓存那些非必须存在的数据。 
 */
public class TestWeakHashMap {

	public static void main(String[] args) throws Exception {
		int size = 1000;
		Key[] keys = new Key[size];
		WeakHashMap whm = new WeakHashMap();
		
		for (int i = 0; i < size; i++) {
			Key k = new Key(Integer.toString(i));
			Value v = new Value(Integer.toString(i));
			if (i % 3 == 0)   
                keys[i] = k; // 使Key对象持有强引用
			whm.put(k, v); // 使Key对象持有弱引用
		}
		
		// 催促垃圾回收器工作   
        System.gc();// 把CPU让给垃圾回收器线程   
        Thread.sleep(8000);
	}
}
