package com.coris.example.maildispatcher.network.inbound;

import com.coris.example.maildispatcher.model.ItemModel;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Function;

public class ScheduleTaskItem implements Function<Communication.Query , List<ItemModel>>, Callable<List<ItemModel>> {
    private final Communication<ItemModel> communication;

    public ScheduleTaskItem(Communication<ItemModel> communication) {
        this.communication = communication;
    }


    @Override
    public List<ItemModel> apply(Communication.Query query) {
        return null;
    }

    @Override
    public List<ItemModel> call() throws Exception {
        return null;
    }
}
