package com.ducdathua.prediction_app.controller;

import com.ducdathua.prediction_app.dto.ResultRequest;
import com.ducdathua.prediction_app.model.Result;
import com.ducdathua.prediction_app.service.resultService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/results")
@CrossOrigin(origins = "http://localhost:5173")
public class ResultController {
    private final resultService service;

    public ResultController(resultService service) {
        this.service = service;
    }

    @GetMapping
    public List<Result> getAll() {
        return service.getAll();
    }

    @GetMapping("/date/{date}")
    public Optional<Result> getByDate(@PathVariable LocalDate date) {
        return service.getByDate(date);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody ResultRequest req) {
        try {
            Result result = service.create(req);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            if("DATE_ALREADY_EXISTS".equals(e.getMessage())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("DATE_ALREADY_EXISTS");
            }
            throw e;
        }
    }
}
