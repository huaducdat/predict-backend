package com.ducdathua.prediction_app.controller;

import com.ducdathua.prediction_app.dto.ResultRequest;
import com.ducdathua.prediction_app.model.Result;
import com.ducdathua.prediction_app.service.ResultService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/results")
@CrossOrigin(origins = "http://localhost:5173")
public class ResultController {
    private final ResultService service;

    public ResultController(ResultService service) {
        this.service = service;
    }

//    @GetMapping
//    public List<Result> getAll() {
//        return service.getAll();
//    }

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
            if ("DATE_ALREADY_EXISTS".equals(e.getMessage())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("DATE_ALREADY_EXISTS");
            }
            throw e;
        }
    }

    @GetMapping
    public Map<String, Object> getPaged(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<Result> resultPage = service.getPaged(page, size);

        Map<String, Object> res = new HashMap<>();
        res.put("content", resultPage.getContent());
        res.put("page", page);
        res.put("size", size);
        res.put("totalPages", resultPage.getTotalPages());
        res.put("totalElements", resultPage.getTotalElements());

        return res;
    }
}
