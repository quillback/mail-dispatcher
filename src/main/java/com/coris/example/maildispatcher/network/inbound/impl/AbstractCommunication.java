package com.coris.example.maildispatcher.network.inbound.impl;

import com.coris.example.maildispatcher.model.ItemModel;
import com.coris.example.maildispatcher.network.inbound.Communication;
import com.coris.example.maildispatcher.network.inbound.ScheduleInboundTaskItem;
import com.coris.example.maildispatcher.network.parser.GsonParser;
import com.linkedin.parseq.Engine;
import com.linkedin.parseq.Task;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.TimeUnit;

@Component
public class AbstractCommunication {
    private final Engine engine;
    private final GsonParser gsonParser;
    private final Communication<ItemModel> communication;
    private final Sinks.Many<ScheduleInboundTaskItem> sink;



    @Autowired
    public AbstractCommunication(Engine engine,
                                 final Communication<ItemModel> communication,
                                 final GsonParser gsonParser) {
        this.engine = engine;
        this.gsonParser = gsonParser;
        this.communication = communication;
        this.sink = Sinks.unsafe().many().unicast().onBackpressureBuffer();
        this.sink.asFlux().publishOn(Schedulers.boundedElastic()).flatMap(ScheduleInboundTaskItem::get)
                .flatMap(this::processAndStore).subscribe();
    }

    public void invokeWith(long duration, TimeUnit unit,
                           @NotNull Communication.Query query){
        final var task = Task.action(() -> {
            final var item = new ScheduleInboundTaskItem(query, this.communication);
            this.sink.emitNext(item, Sinks.EmitFailureHandler.FAIL_FAST);
        }).withDelay(duration, unit);
        this.engine.run(task);
    }

    //todo implement
    private Mono<Void> processAndStore(final ItemModel model){
        return Mono.empty();
    }
}
