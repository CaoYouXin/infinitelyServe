package tech.caols.infinitely.rest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BeanUtils {

    private static final Logger logger = LogManager.getLogger(BeanUtils.class);

    public static void copyBean(Object source, Object target) {
        for (Method method : target.getClass().getMethods()) {
            if (method.getName().startsWith("set")) {
                String prefix = "get";

                Class<?> parameterType = method.getParameterTypes()[0];
                if (parameterType.equals(boolean.class) || parameterType.equals(Boolean.class)) {
                    prefix = "is";
                }

                String getterMethodName = prefix + method.getName().substring("set".length());
                try {
                    Method getterMethod = source.getClass().getDeclaredMethod(getterMethodName);
                    if (getterMethod != null) {
                        method.invoke(target, getterMethod.invoke(source));
                    }
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    logger.catching(e);
                }
            }
        }
    }

}
