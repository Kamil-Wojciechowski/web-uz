package com.uz.shop.animal.world.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.List;

//Serwis odpowiadający za budowanie 400 oraz 404 jako response entity, a nie jako errory throwable
@Service
public class ErrorResponseCreator {

    private static ObjectNode objectNode = new ObjectMapper().createObjectNode();

    public static ResponseEntity<ObjectNode> buildResponse(HttpStatus status, String message, String error) {
        ArrayNode arrayNode = objectNode.putArray("errors");
        arrayNode.add(error);
        objectNode.put("Status", status.name());
        objectNode.put("message", message);
        return ResponseEntity.status(status).body(objectNode);
    }

    public static ResponseEntity<ObjectNode> buildResponse(HttpStatus status, String message, List<String> errors) {
        ArrayNode arrayNode = objectNode.putArray("errors");
        for(String error : errors ) {
            arrayNode.add(error);
        }
        objectNode.put("Status", status.name());
        objectNode.put("message", message);
        return ResponseEntity.status(status).body(objectNode);
    }

    public static ResponseEntity<ObjectNode> buildBadRequest(String message, String error) {
        ArrayNode arrayNode = objectNode.putArray("errors");
        arrayNode.add(error);
        objectNode.put("Status", HttpStatus.BAD_REQUEST.name());
        objectNode.put("message", message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(objectNode);
    }

    public static ResponseEntity<ObjectNode> buildNotFound(String message, String error) {
        ArrayNode arrayNode = objectNode.putArray("errors");
        arrayNode.add(error);
        objectNode.put("Status", HttpStatus.NOT_FOUND.name());
        objectNode.put("message", message);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(objectNode);
    }

}
