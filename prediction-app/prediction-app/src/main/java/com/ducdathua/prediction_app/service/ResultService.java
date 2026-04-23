package com.ducdathua.prediction_app.service;

import com.ducdathua.prediction_app.dto.ResultRequest;
import com.ducdathua.prediction_app.exception.DuplicateDateException;
import com.ducdathua.prediction_app.model.Result;
import com.ducdathua.prediction_app.repository.ResultRepository;

import org.springframework.stereotype.Service;
import java.util.Optional;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@Service
public class ResultService {
    private final ResultRepository repo;
    private final ResultAnalysisService analysisService;
    private final StreakService streakService;

    public ResultService(ResultRepository repo,
                         ResultAnalysisService analysisService,
                         StreakService streakService) {
        this.repo = repo;
        this.analysisService = analysisService;
        this.streakService = streakService;
    }
    public List<Result> getAll() {
        return repo.findAllByOrderByDateDesc();
    }

    public Optional<Result> getByDate(LocalDate date) {
        return repo.findByDate(date);
    }

    public Result create(ResultRequest req) {

        boolean exists = repo.existsByDate(req.getDate());
        if (exists && !req.isForce()) {
            throw new DuplicateDateException();
        }

        Result result = repo.findByDate(req.getDate()).orElse(new Result());

        result.setDate(req.getDate());
        result.setNumbers(req.getNumbers());
        result.setSingleNumber(req.getSingleNumber());

        // ✔ lưu trước
        Result saved = repo.save(result);

        // 🔥 trigger rebuild (THÊM ĐOẠN NÀY)

        streakService.rebuildLatestDayStreaks();

        return saved;
    }

    public Page<Result> getPaged(int page, int size) {
        Pageable pageable = PageRequest.of(
                page - 1, // frontend bắt đầu từ 1
                size,
                Sort.by("date").descending()
        );

        return repo.findAll(pageable);
    }

}


