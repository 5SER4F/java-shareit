package ru.practicum.shareit.util;

import java.util.function.Consumer;

public class Patcher {

    private Patcher() {
        throw new IllegalStateException("Утилити класс не может иметь экземпляр");
    }

    public static <T> void setIfNotNull(final Consumer<T> consumer, final T value) {
        if (value != null) {
            consumer.accept(value);
        }
    }
}
