package com.example.demo;

public class BookNotFoundException extends Throwable {

    public BookNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
