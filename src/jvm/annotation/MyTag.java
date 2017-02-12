package jvm.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MyTag {
	
	public enum Light {ON , OFF}

	String name() default "AAA";

	int age() default 18;
	
	Light getLight() default Light.OFF;
}
