package jdkSrc.Proxy;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;

public class Test {

    public static void main(String[] args) throws Exception {
        BusinessProcessorImpl bpimpl = new BusinessProcessorImpl();
        
        BusinessProcessorHandler handler = new BusinessProcessorHandler(bpimpl);   
        
        /*
         *  Proxy.newProxyInstance方法会做如下几件事:
         *  
         *  1.  根据传入的第二个参数interfaces动态生成一个类,实现interfaces中的接口,
         *      该例中即BusinessProcessor接口的processBusiness方法。
         *      并且继承了Proxy类,重写了hashcode,toString,equals等三个方法。
         *      具体实现可参看 ProxyGenerator.generateProxyClass(...); 
         *  
         *  2.  通过传入的第一个参数classloder将刚生成的类加载到jvm中。即load $Proxy0类.
         *  
         *  3.  利用第三个参数,调用$Proxy0的$Proxy0(InvocationHandler)构造函数 创建
         *      $Proxy0的对象,并且用interfaces参数遍历其所有接口的方法,并生成Method对象
         *      初始化对象的几个Method成员变量
         *    
         *  4.  将$Proxy0的实例返回给客户端。
         */
        BusinessProcessor bp = (BusinessProcessor)Proxy.newProxyInstance(
                bpimpl.getClass().getClassLoader(), 
                bpimpl.getClass().getInterfaces(), 
                handler);
        
        bp.processBusiness();
        
        Runnable r = (Runnable)bp;
        r.run();
        
        System.out.println(bp.getClass().getName());
        
        Class clz = bp.getClass();
        printClassDefinition(clz);
        
    }
    
    public static String getModifier(int modifier){
        String result = "";
        switch(modifier){
         case Modifier.PRIVATE:
          result = "private";
         case Modifier.PUBLIC:
          result = "public";
         case Modifier.PROTECTED:
          result = "protected";
         case Modifier.ABSTRACT :
          result = "abstract";
         case Modifier.FINAL :
          result = "final";
         case Modifier.NATIVE :
          result = "native";
         case Modifier.STATIC :
          result = "static";
         case Modifier.SYNCHRONIZED :
          result = "synchronized";
         case Modifier.STRICT  :
          result = "strict";
         case Modifier.TRANSIENT :
          result = "transient";
         case Modifier.VOLATILE :
          result = "volatile";
         case Modifier.INTERFACE :
          result = "interface";
        }
        return result;
    }
    
    
    public static void printClassDefinition(Class clz){
        
        String clzModifier = getModifier(clz.getModifiers());
        if(clzModifier!=null && !clzModifier.equals("")){
         clzModifier = clzModifier + " ";
        }
        String superClz = clz.getSuperclass().getName();
        if(superClz!=null && !superClz.equals("")){
         superClz = "extends " + superClz;
        }
        
        Class[] interfaces = clz.getInterfaces();
        
        String inters = "";
        for(int i=0; i<interfaces.length; i++){
         if(i==0){
          inters += "implements ";
         }
         inters += interfaces[i].getName();
        }
        
        System.out.println(clzModifier +clz.getName()+" " + superClz +" " + inters );
        System.out.println("{");
        
        Field[] fields = clz.getDeclaredFields();
        for(int i=0; i<fields.length; i++){
         String modifier = getModifier(fields[i].getModifiers());
         if(modifier!=null && !modifier.equals("")){
          modifier = modifier + " ";
         }
         String fieldName = fields[i].getName();
         String fieldType = fields[i].getType().getName();
         System.out.println("    "+modifier + fieldType + " "+ fieldName + ";");
        }
        
        System.out.println();
        
        Method[] methods = clz.getDeclaredMethods();
        for(int i=0; i<methods.length; i++){
         Method method = methods[i];

         String modifier = getModifier(method.getModifiers());
         if(modifier!=null && !modifier.equals("")){
          modifier = modifier + " ";
         }
         
         String methodName = method.getName();
         
         Class returnClz = method.getReturnType();
         String retrunType = returnClz.getName();
         
         Class[] clzs = method.getParameterTypes();
         String paraList = "(";
         for(int j=0; j<clzs.length; j++){
          paraList += clzs[j].getName();
          if(j != clzs.length -1 ){
           paraList += ", ";
          }
         }
         paraList += ")";
         
         clzs = method.getExceptionTypes();
         String exceptions = "";
         for(int j=0; j<clzs.length; j++){
          if(j==0){
           exceptions += "throws ";
          }

          exceptions += clzs[j].getName();
          
          if(j != clzs.length -1 ){
           exceptions += ", ";
          }
         }
         
         exceptions += ";";
         
         String methodPrototype = modifier +retrunType+" "+methodName+paraList+exceptions;
         
         System.out.println("    "+methodPrototype );
         
        }
        System.out.println("}");
    }
    
}
