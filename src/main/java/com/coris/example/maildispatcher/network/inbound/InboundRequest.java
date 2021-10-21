package com.coris.example.maildispatcher.network.inbound;

import com.coris.example.maildispatcher.model.ItemModel;
import com.linkedin.parseq.Engine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InboundRequest {
    private final Engine engine;
    private final Communication<ItemModel> communication;


    @Autowired
    public InboundRequest(Engine engine, Communication<ItemModel> communication) {
        this.engine = engine;
        this.communication = communication;

    }



}
