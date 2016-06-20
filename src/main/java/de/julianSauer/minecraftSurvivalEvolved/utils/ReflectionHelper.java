package de.julianSauer.minecraftSurvivalEvolved.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectionHelper {

    /**
     * Makes a private variable public.
     *
     * @param clazz Class that contains the variable
     * @param name  Variable name
     * @return Field containing the variable or null
     */
    public static Field getPrivateVariable(Class clazz, String name) {
        try {
            Field field = clazz.getDeclaredField(name);
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Accesses the value of a private variable.
     *
     * @param clazz  Class that contains the declaration of the variable
     * @param object Object that contains the value of the variable
     * @param name   Variable name
     * @return Value or null
     */
    public static Object getPrivateVariableValue(Class clazz, Object object, String name) {
        Field field = getPrivateVariable(clazz, name);
        try {
            return field.get(object);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Accesses a private static variable a reference to it.
     *
     * @param clazz Class that contains the variable
     * @param name  Variable name
     * @return Reference to the variable
     */
    public static Object getPrivateStaticVariable(Class clazz, String name) {
        Field field = getPrivateVariable(clazz, name);
        try {
            return field.get(null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Makes a private method public.
     *
     * @param clazz Class that contains the method
     * @param name  Method name
     * @param args  Method parameters
     * @return Invokable method or null
     */
    public static Method getPrivateMethod(Class clazz, String name, Class... args) {
        try {
            Method method = clazz.getDeclaredMethod(name, args);
            method.setAccessible(true);
            return method;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Constructor getConstructor(Class clazz, String innerClassName, Class... parameterTypes) {
        try {
            Class innerClass = Class.forName(clazz.getName() + "$" + innerClassName);
            Constructor constructor = innerClass.getDeclaredConstructor(parameterTypes);
            constructor.setAccessible(true);
            return constructor;
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }
}

