package com.hg.bethunger.mapper;

import com.hg.bethunger.model.enums.UserRole;
import com.hg.bethunger.security.UserPrincipal;
import org.modelmapper.Condition;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MappingUtils {
    public static <R, E> List<R> mapList(List<E> list, Function<E, R> mapper) {
        return list.stream().map(mapper).collect(Collectors.toList());
    }

    public static Condition<?, ?> isSuperUser = ctx -> {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserRole userRole = userPrincipal.getUser().getRole();
        return userRole == UserRole.MANAGER || userRole == UserRole.ADMIN;
    };
}
