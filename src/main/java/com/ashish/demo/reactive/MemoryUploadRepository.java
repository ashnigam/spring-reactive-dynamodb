package com.ashish.demo.reactive;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class MemoryUploadRepository implements UploadRepositoryInf {

    private Map<String, UploadMessage> messages = new ConcurrentHashMap<>();

    public Mono<UploadMessage> getUploadMessageForId(String assetId) {
        return Mono.justOrEmpty(messages.get(assetId));
    }

    public Flux<UploadMessage> getUploadMessage() {
        return Flux.fromIterable(messages.values());
    }

    public Mono<UploadMessage> saveMessage(UploadMessage message) {
        message.setAssetId(UUID.randomUUID().toString());
        messages.put(message.getAssetId(), message);
        return Mono.justOrEmpty(message);
    }

}