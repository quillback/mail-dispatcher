package com.coris.example.maildispatcher.network.parser;

import com.coris.example.maildispatcher.network.converter.GsonConverter;
import com.coris.example.maildispatcher.network.registry.ConverterRegistry;
import com.coris.example.maildispatcher.network.util.JsonNode;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.logging.log4j.util.Strings;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Component
public class GsonParser {
    private final ConverterRegistry registry;

    private final Gson gson = new GsonBuilder().create();
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public GsonParser(final ConverterRegistry registry){
        this.registry = registry;
    }

    @SuppressWarnings("unchecked")
    public final <T> Optional<T> populateKlazz(@NotNull final Class<T> klazz, final String json) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        if (Strings.isEmpty(json)) return Optional.empty();
        final var instance = klazz.getConstructor().newInstance();

        final var nodes = getNodes(json);
        final var f = instance.getClass().getDeclaredFields();
        for (Field x : f) {
            x.setAccessible(true);

            final var jsonNode = x.getAnnotation(JsonNode.class);
            if (Objects.isNull(jsonNode))
                continue;

            if (jsonNode.autoPopulate() && x.getType() == String.class){
                var uuid = UUID.randomUUID().toString();
                uuid = uuid.replaceAll("-","");
                try {
                    x.set(instance, uuid);
                } catch (IllegalAccessException e) {
                    logger.error("Illegal Access for {}", x.getName());
                }
            }

            final var value = nodes.get(jsonNode.nodeName());
            if (Objects.nonNull(value)){
                if (jsonNode.converter() != GsonConverter.EmptyGsonConverter.class){
                    final GsonConverter<?> converter = this.registry.getConverter((Class<GsonConverter<?>>) jsonNode.converter());
                    if (ClassUtils.isAssignable(converter.supports(), x.getType())){
                        final Optional<?> response = converter.fromJson(String.valueOf(value));
                        if (response.isPresent()) x.set(instance, response.get());
                    }
                }else{

                    if (x.getType() == String.class){
                        x.set(instance, value);
                    }
                }

            }

        }
        return Optional.of(instance);
    }

    private Map<String, Object> getNodes(String json){
        final var type = new TypeToken<Map<String, Object>>() {}.getType();
        return gson.fromJson(json, type);
    }

}
