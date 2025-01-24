package com.campusland.springbootdemo.infrastructure.error;

public class TopicNotFoundException extends RuntimeException {
    public TopicNotFoundException(String message) {
        super(message);
    }
}
