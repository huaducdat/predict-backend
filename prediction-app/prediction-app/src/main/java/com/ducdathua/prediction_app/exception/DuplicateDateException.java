package com.ducdathua.prediction_app.exception;

public class DuplicateDateException extends RuntimeException {
    public DuplicateDateException() {
        super("DATE_ALREADY_EXISTS");
    }
}