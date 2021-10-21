package com.coris.example.maildispatcher.network.registry;

import com.coris.example.maildispatcher.network.converter.BigDecimalConverter;
import com.coris.example.maildispatcher.network.converter.GsonConverter;
import com.coris.example.maildispatcher.network.converter.LocalDateConverter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
public class ConverterRegistry {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Map<Class<? extends GsonConverter<?>>, GsonConverter<?>> map = new HashMap<>();

    @PostConstruct
    public void postConstruct(){
        this.map.put(BigDecimalConverter.class, new BigDecimalConverter());
        this.map.put(LocalDateConverter.class, new LocalDateConverter());

        this.logger.info("Registered converter classes: [{}]", map.keySet());
    }

    @NotNull
    public GsonConverter<?> getConverter(@NotNull Class<GsonConverter<?>> klazz){
        return this.map.get(klazz);
    }
}
