package pl.mrstudios.deathrun.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;

import static java.lang.String.format;

public class ReflectionUtil {

    public static <TYPE> TYPE readField(
            @NotNull String field,
            @NotNull Object instance
    ) {
        return readField(field(field, instance.getClass()), instance);
    }

    @SuppressWarnings("unchecked")
    public static <TYPE> TYPE readField(
            @NotNull Field field,
            @NotNull Object instance
    ) {
        try {
            return (TYPE) field.get(instance);
        } catch (@NotNull Exception exception) {
            throw new RuntimeException(format("Unable to read '%s' field from provided instance.", field.getName()));
        }
    }

    public static void writeField(
            @NotNull String field,
            @NotNull Object instance,
            @Nullable Object value
    ) {
        writeField(field(field, instance.getClass()), instance, value);
    }

    public static void writeField(
            @NotNull Field field,
            @NotNull Object instance,
            @Nullable Object value
    ) {
        try {
            field.set(instance, value);
        } catch (@NotNull Exception exception) {
            throw new RuntimeException(format("Unable to write '%s' field to provided instance.", field.getName()));
        }
    }

    public static @NotNull Field field(
            @NotNull String name,
            @NotNull Class<?> clazz
    ) {
        try {
            return clazz.getDeclaredField(name);
        } catch (@NotNull Exception exception) {
            throw new RuntimeException(format("Unable to find '%s' field in provided class.", name));
        }
    }

}
