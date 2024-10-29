package me.udnek.itemscoreu.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sun.misc.Unsafe;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Reflex {
    public static Field getField(Class<?> source, String name) {
        try {
            return source.getDeclaredField(name);
        }
        catch (NoSuchFieldException exception) {
            Class<?> superClass = source.getSuperclass();
            return superClass == null ? null : getField(superClass, name);
        }
    }

    public static Object getFieldValue(Object source, String name) {
        try {
            Class<?> clazz = source instanceof Class<?> ? (Class<?>) source : source.getClass();
            Field field = getField(clazz, name);
            if (field == null) return null;

            field.setAccessible(true);
            return field.get(source);
        }
        catch (IllegalAccessException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public static <T> T getFieldValue(Object source, String name, Class<T> tClass) {
        return (T) getFieldValue(source, name);
    }


    public static void setFieldValue(Object source, @NotNull String name, @Nullable Object value) {
        try {
            boolean isStatic = source instanceof Class;
            Class<?> clazz = isStatic ? (Class<?>) source : source.getClass();

            Field field = getField(clazz, name);
            field.setAccessible(true);
            field.set(isStatic ? null : source, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setRecordFieldValue(Object instance, @NotNull String fieldName, @Nullable Object value) {
        try {
            Field f = instance.getClass().getDeclaredField(fieldName);

            Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
            unsafeField.setAccessible(true);
            Unsafe unsafe = (Unsafe) unsafeField.get(null);

            Field theInternalUnsafeField = Unsafe.class.getDeclaredField("theInternalUnsafe");
            theInternalUnsafeField.setAccessible(true);
            Object theInternalUnsafe = theInternalUnsafeField.get(null);

            Method offset = Class.forName("jdk.internal.misc.Unsafe").getMethod("objectFieldOffset", Field.class);
            unsafe.putBoolean(offset, 12, true);

            unsafe.putObject(instance, (long) offset.invoke(theInternalUnsafe, f), value);
        } catch (IllegalAccessException | NoSuchFieldException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }


    public static Method getMethod(Class<?> clazz, String name, Class<?>... parameterTypes){
        try {
            Method method = clazz.getDeclaredMethod(name, parameterTypes);
            method.setAccessible(true);
            return method;
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
    public static Method getMethod(Class<?> clazz, String name){
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (method.getName().equals(name)){
                method.setAccessible(true);
                return method;
            }
        }
        throw new RuntimeException(new NoSuchFieldException(name));
    }

    public static Object invokeMethod(Object object, Method method, Object ...args){
        try {
            return method.invoke(object, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> Constructor<T> getFirstConstructor(Class<T> clazz){
        Constructor<?>[] constructor = clazz.getDeclaredConstructors();
        constructor[0].setAccessible(true);
        return (Constructor<T>) constructor[0];
    }

    public static <T> Constructor<T> getConstructor(Class<T> clazz, Class<?> ...parameterTypes){
        try {
            Constructor<T> constructor = clazz.getDeclaredConstructor(parameterTypes);
            constructor.setAccessible(true);
            return constructor;
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T construct(Constructor<T> constructor, Object ...args){
        try {
            return constructor.newInstance(args);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}

















