package com.ashish.demo.reactive.dynamodb;

import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import com.ashish.demo.reactive.UploadMessage;
import com.ashish.demo.reactive.UploadRepositoryInf;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemResponse;
import software.amazon.awssdk.services.dynamodb.model.ReturnValue;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanResponse;

public class DynamoDBUploadRepository implements UploadRepositoryInf {

    private static final String tableName = "UploadRequest";
    private DynamoDbAsyncClient client = null;

    public DynamoDBUploadRepository(DynamoDbAsyncClient client) {
        this.client = client;
    }

    @Override
    public Mono<UploadMessage> getUploadMessageForId(String assetId) {
        Map<String, AttributeValue> attributeValueHashMap = new HashMap<>();
        attributeValueHashMap.put("assetId", AttributeValue.builder().s(assetId).build());

        GetItemRequest getItemRequest = GetItemRequest.builder().tableName(tableName).key(attributeValueHashMap)
                .build();

        CompletableFuture<GetItemResponse> completableFuture = client.getItem(getItemRequest);

        CompletableFuture<UploadMessage> uploadCompletableFuture = completableFuture
                .thenApplyAsync(GetItemResponse::item).thenApplyAsync(map -> createUploadMessage(map));

        return Mono.fromFuture(uploadCompletableFuture);
    }

    @Override
    public Flux<UploadMessage> getUploadMessage() {
        ScanRequest scanRequest = ScanRequest.builder().tableName(tableName).build();
        CompletableFuture<ScanResponse> future = client.scan(scanRequest);

        CompletableFuture<List<UploadMessage>> response = future.thenApplyAsync(ScanResponse::items).thenApplyAsync(
                list -> list.parallelStream().map(map -> createUploadMessage(map)).collect(Collectors.toList()));
        Mono<List<UploadMessage>> mono = Mono.fromFuture(response);
        Flux<UploadMessage> flux = mono.flatMapMany(Flux::fromIterable);
        return flux;
    }

    @Override
    public Mono<UploadMessage> saveMessage(UploadMessage message) {
        Map<String, AttributeValue> attributeValueHashMap = new HashMap<>();
        attributeValueHashMap.put("affiliateId", AttributeValue.builder().s(message.getAffiliateId()).build());
        attributeValueHashMap.put("folderId", AttributeValue.builder().s(message.getFolderId()).build());
        attributeValueHashMap.put("fileName", AttributeValue.builder().s(message.getFileName()).build());
        String assetId = UUID.randomUUID().toString();
        attributeValueHashMap.put("assetId", AttributeValue.builder().s(assetId).build());
        message.setAssetId(assetId);
        PutItemRequest putItemRequest = PutItemRequest.builder().tableName(tableName).item(attributeValueHashMap)
                .returnValues(ReturnValue.ALL_OLD).build();

        CompletableFuture<PutItemResponse> completableFuture = client.putItem(putItemRequest);

        CompletableFuture<UploadMessage> uploadCompletableFuture = completableFuture
                .thenApplyAsync(PutItemResponse::attributes).thenApplyAsync(map -> message);

        return Mono.fromFuture(uploadCompletableFuture);
    }

    private UploadMessage createUploadMessage(Map<String, AttributeValue> map) {
        if (map != null) {
            return new UploadMessage(map.get("affiliateId").s(), map.get("assetId").s(), map.get("folderId").s(),
                    map.get("fileName").s());
        }
        return null;
    }
}