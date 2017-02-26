package jdkSrc.reference;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.HashMap;

class Employee{
	private String id;// 雇员的标识号码    
    private String name;// 雇员姓名    
    private String department;// 该雇员所在部门    
    private String Phone;// 该雇员联系电话    
    private int salary;// 该雇员薪资    
    private String origin;// 该雇员信息的来源    
      
    // 构造方法     
    public Employee(String id) {     
       this.id = id;     
       getDataFromlnfoCenter();     
    }
    
    public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	// 到数据库中取得雇员信息     
    private void getDataFromlnfoCenter() {     
       // 和数据库建立连接井查询该雇员的信息，将查询结果赋值     
       // 给name，department，plone，salary等变量     
       // 同时将origin赋值为"From DataBase"  
    }
}

/**
 * 	目的:
 * 		一个雇员信息查询系统的实例.
 * 
 * 	分析:
 * 		通过把过去查看过的雇员信息保存在内存中来提高性能。
 * 		只有当内存吃紧时,才GC掉缓存的信息。
 */
class EmployeeCache {
	
	private HashMap employeeRefMap;
	private ReferenceQueue queue;
	
	private class EmployeeRef extends SoftReference{
		private String key = "";
		
		public EmployeeRef(Employee e, ReferenceQueue q) {
			super(e,q);
			key = e.getId();
		}
	}
	
	// 构造函数
	public EmployeeCache() {
		this.queue = new ReferenceQueue();
		this.employeeRefMap = new HashMap();
	}
	
	// 创建软引用指向 Employee对象,并保存此软引用.
	public void cacheEmployee(Employee e){
		cleanCache();
		EmployeeRef sf = new EmployeeRef(e,queue);
		employeeRefMap.put(e.getId(), sf);
	}
	
	//根据给定的ID,返回 Employee对象。
	public Employee getEmployee(String id){
		Employee e = null;
		
		// 缓存中是否有该Employee实例的软引用，如果有，从软引用中取得。
		if(employeeRefMap.containsKey(id)){
			System.out.println("get from cache------------------");
			EmployeeRef employeeref = (EmployeeRef)employeeRefMap.get(id);
			e = (Employee)employeeref.get();
		}

		// 如果软引用不存在或者软引用所指向的对象不存在,则重新创建一个对象
		if(e==null){
			e = new Employee(id);
			System.out.println("Retrieve From EmployeeInfoCenter. ID=" + e.getId());
			// 保存到缓存
			cacheEmployee(e);
		}
		return e;
	}
	
	// 清除那些软引用指向的Employee对象已经被回收的SoftReference对象
	private void cleanCache() {
		EmployeeRef sf = null;
		while((sf = (EmployeeRef)queue.poll())!=null){
			employeeRefMap.remove(sf.key);
		}
	}
	
}


public class SoftReferenceExample {

	public static void main(String[] args) {
		Employee e = null;
		EmployeeCache eCache = new EmployeeCache();
		
		for(int i=0;i<=100000;i++){
			e = new Employee(String.valueOf(i));
			eCache.cacheEmployee(e);
		}
		
		for(int i=100000;i>=0;i--){
			e = eCache.getEmployee(
					String.valueOf(i));
			e = null;
		}
		
	}
}
