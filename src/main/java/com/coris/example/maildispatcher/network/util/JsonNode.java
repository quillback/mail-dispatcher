package com.coris.example.maildispatcher.network.util;

import com.coris.example.maildispatcher.network.converter.GsonConverter;
import org.apache.poi.ss.usermodel.CellType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonNode {

    String nodeName() default "";

    boolean autoPopulate() default false;

    Class<? extends GsonConverter<?>> converter() default GsonConverter.EmptyGsonConverter.class;

    boolean writerIgnore() default false;

    CellType celltype() default CellType.STRING;
}
