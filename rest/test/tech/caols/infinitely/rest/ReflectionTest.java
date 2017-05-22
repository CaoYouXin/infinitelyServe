package tech.caols.infinitely.rest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionTest {

    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method hello = VoidMethod.class.getDeclaredMethod("hello");
        Object invoke = hello.invoke(new VoidMethod());
        System.out.println(invoke);
    }

}
