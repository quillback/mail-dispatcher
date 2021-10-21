package com.coris.example.maildispatcher.network.inbound;

import com.coris.example.maildispatcher.model.ItemModel;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;

import java.util.function.Supplier;


@RequiredArgsConstructor
public class ScheduleInboundTaskItem implements Supplier<Publisher<ItemModel>> {
    private final Communication.Query query;
    private final Communication<ItemModel> communication;

    @Override
    public Publisher<ItemModel> get() {
        return query.single ? communication.getOne(query) :  communication.getMany(query);
    }
}
