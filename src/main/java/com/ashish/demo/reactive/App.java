package com.ashish.demo.reactive;

import java.time.Duration;
import java.util.Optional;

import com.ashish.demo.reactive.config.DynamoAsyncDBConfig;

import org.slf4j.LoggerFactory;
import reactor.netty.http.server.HttpServer;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

public class App {

    public static void main(String[] args) throws Exception {
        long begin = System.currentTimeMillis();
        int port = Optional.ofNullable(System.getenv("PORT")) //
                .map(Integer::parseInt) //
                .orElse(8080);

        HttpHandler httpHandler = RouterFunctions.toHttpHandler(App.routes(), HandlerStrategies.builder().build());
        HttpServer httpServer = HttpServer.create().host("0.0.0.0").port(port)
                .handle(new ReactorHttpHandlerAdapter(httpHandler));
        httpServer.bindUntilJavaShutdown(Duration.ofSeconds(3), disposableServer -> {
            long elapsed = System.currentTimeMillis() - begin;
            LoggerFactory.getLogger(App.class).info("Started in {} seconds", elapsed / 1000.0);
        });
    }

    static RouterFunction<ServerResponse> routes() {
        ApplicationContext context = new AnnotationConfigApplicationContext(DynamoAsyncDBConfig.class);
        UploadHandler handler = context.getBean("uploadHandler", UploadHandler.class);
        if (handler == null) {
            System.out.println("handler is null");
        }
        // ((AbstractApplicationContext) context).close();
        // return UploadRouter.routes(new UploadHandler(new MemoryUploadRepository()));
        return UploadRouter.routes(handler);
    }
}
