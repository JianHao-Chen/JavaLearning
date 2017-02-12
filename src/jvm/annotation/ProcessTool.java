package jvm.annotation;

import java.lang.reflect.Method;

public class ProcessTool {

	public static void process(String clazz) {
		Class targetClass = null;
		
		try {
			targetClass = Class.forName(clazz);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		for (Method m : targetClass.getMethods()) {
			if (m.isAnnotationPresent(MyTag.class)) {
				MyTag tag = m.getAnnotation(MyTag.class);
				System.out.println("方法" + m.getName() + "的MyTag注解内容为：" 
						+ tag.name() + "，" + tag.age());
			}
		}
	}
}
