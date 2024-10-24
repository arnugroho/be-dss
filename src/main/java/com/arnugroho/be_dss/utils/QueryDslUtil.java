package com.arnugroho.be_dss.utils;

import com.querydsl.core.types.dsl.*;
import org.springframework.util.StringUtils;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Locale;

public class QueryDslUtil {
    public static <T extends Number & Comparable<T>> BooleanExpression eq(NumberPath<T> path, T value) {
        return value != null ? path.eq(value) : null;
    }

    public static BooleanExpression eq(StringPath path, String value) {
        return path.eq(value);
    }

    public static BooleanExpression like(StringPath path, String value) {
        return StringUtils.hasText(value) ? path.toLowerCase().like("%" + value.toLowerCase(Locale.ROOT) + "%") : null;
    }

    public static BooleanExpression eq(BooleanPath path, Boolean value) {
        return path.eq(value);
    }

    public static<T extends Number & Comparable<T>> BooleanExpression goe(NumberPath<T> numberPath, T value) {
        return numberPath.goe(value);
    }

    public static<T extends Number & Comparable<T>> BooleanExpression gt(NumberPath<T> numberPath, T value) {
        return numberPath.gt(value);
    }

    public static BooleanExpression goe(DateTimePath path, ZonedDateTime value) {
        return path.after(value);
    }

    public static BooleanExpression in(NumberPath path, List<Long> value) {
        return path.in(value);
    }

    public static BooleanExpression filterJson(String value, StringTemplate stringTemplate) {
        return stringTemplate.like("%" + value +"%");
    }
}
