package com.example.demo;

public class BookIdMismatchException extends Throwable {
    public BookIdMismatchException(String message, Throwable cause) {
        super(message, cause);
    }

    public BookIdMismatchException() {
    }
}
