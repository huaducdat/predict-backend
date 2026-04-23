package com.ducdathua.prediction_app.service.util;

import com.ducdathua.prediction_app.model.Result;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public final class ResultParser {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private ResultParser() {
    }


}