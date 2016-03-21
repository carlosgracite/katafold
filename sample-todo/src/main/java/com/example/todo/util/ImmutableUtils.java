package com.example.todo.util;

import com.google.common.collect.ImmutableList;

import java.util.List;

public class ImmutableUtils {

    public static <T> List<T> updateItem(List<T> list, T item, int index) {
        if (list.size() == 1) {
            return ImmutableList.of(item);
        }

        ImmutableList.Builder<T> builder = ImmutableList.builder();

        if (index > 0) {
            builder.addAll(list.subList(0, index));
        }

        builder.add(item);

        if (index+1 < list.size()) {
            builder.addAll(list.subList(index+1, list.size()));
        }

        return builder.build();
    }

}
