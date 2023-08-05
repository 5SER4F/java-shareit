package ru.practicum.shareit.util;

import lombok.experimental.UtilityClass;

import java.util.function.Consumer;

@UtilityClass
public class Patcher {
    public static <T> void setIfNotNull(final Consumer<T> consumer, final T value) {
        if (value != null) {
            consumer.accept(value);
        }
    }
}
