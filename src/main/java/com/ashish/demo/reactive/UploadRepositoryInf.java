package com.ashish.demo.reactive;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UploadRepositoryInf {

    public Mono<UploadMessage> getUploadMessageForId(String assetId);

    public Flux<UploadMessage> getUploadMessage();

    public Mono<UploadMessage> saveMessage(UploadMessage message);

}