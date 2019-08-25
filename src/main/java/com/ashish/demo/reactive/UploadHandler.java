package com.ashish.demo.reactive;

import reactor.core.publisher.Mono;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromPublisher;
import static org.springframework.web.reactive.function.server.ServerResponse.*;

public class UploadHandler {

    private UploadRepositoryInf uploadRepository = null;

    public UploadHandler(UploadRepositoryInf uploadRepository) {
        this.uploadRepository = uploadRepository;
    }

    public Mono<ServerResponse> get(ServerRequest request) {
        final String assetId = request.pathVariable("id");
        final Mono<UploadMessage> upload = uploadRepository.getUploadMessageForId(assetId);
        return upload.flatMap(p -> ok().contentType(APPLICATION_JSON).body(fromPublisher(upload, UploadMessage.class)))
                .switchIfEmpty(notFound().build());
    }

    public Mono<ServerResponse> all(ServerRequest req) {
        return ok().contentType(APPLICATION_JSON)
                .body(fromPublisher(uploadRepository.getUploadMessage(), UploadMessage.class));

    }

    public Mono<ServerResponse> create(ServerRequest req) {
        Mono<UploadMessage> upload = req.bodyToMono(UploadMessage.class);
        // return created(UriComponentsBuilder.fromPath("uploadevent/" +
        // id).build().toUri())
        return ok().contentType(APPLICATION_JSON)
                .body(fromPublisher(upload.flatMap(uploadRepository::saveMessage), UploadMessage.class));
    }
}
