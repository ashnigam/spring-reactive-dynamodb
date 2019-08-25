package com.ashish.demo.reactive;

import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

public class UploadRouter {

    public static RouterFunction<ServerResponse> routes(UploadHandler uploadHandler) {

        return RouterFunctions.route(GET("/uploadevent/{id}").and(accept(APPLICATION_JSON)), uploadHandler::get)
                .andRoute(GET("/uploadevent").and(accept(APPLICATION_JSON)), uploadHandler::all)
                .andRoute(POST("/uploadevent").and(accept(APPLICATION_JSON)).and(contentType(APPLICATION_JSON)),
                        uploadHandler::create);
        // .andRoute(PUT("/uploadevent/{id}").and(accept(APPLICATION_JSON)).and(contentType(APPLICATION_JSON)),
        // uploadHandler::update)
        // .andRoute(DELETE("/uploadevent/{id}"), uploadHandler::delete);
    }

}