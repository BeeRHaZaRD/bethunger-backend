package com.hg.bethunger.mapper;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MappingUtils {
    public static <R, E> List<R> mapList(List<E> list, Function<E, R> converter) {
        return list.stream().map(converter).collect(Collectors.toList());
    }
}
