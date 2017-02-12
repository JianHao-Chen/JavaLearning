package jvm.annotation;

public class Demo {

	public static void m1() {

	}

	@MyTag
	public static void m2() {

	}
	
	@MyTag(name = "TTT")
	public static void m3() {

	}

	@MyTag(name = "TTT", age = 30)
	public static void m4() {

	}
}
