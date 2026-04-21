package com.ducdathua.prediction_app.service;

import com.ducdathua.prediction_app.dto.PredictionRequest;
import com.ducdathua.prediction_app.model.Prediction;
import com.ducdathua.prediction_app.model.PredictionSet;
import com.ducdathua.prediction_app.repository.PredictionRepository;
import com.ducdathua.prediction_app.repository.PredictionSetRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
public class PredictionService {
    private final PredictionRepository predictionRepo;
    private final PredictionSetRepository setRepo;

    public PredictionService(PredictionRepository predictionRepo, PredictionSetRepository setRepo) {
        this.predictionRepo = predictionRepo;
        this.setRepo = setRepo;
    }

    public void create(PredictionRequest req) {
        validate(req);
        Prediction p = new Prediction();
        p.setDate(req.getDate());
        p.setSingleNumber(req.getSingleNumber());

        PredictionSet set = new PredictionSet();
        set.setDate(req.getDate());
        set.setNumbers(req.getNumbers());
        set.setSingleNumber(req.getSingleNumber());
        predictionRepo.save(p);
        setRepo.save(set);
    }
    private void validate(PredictionRequest req) {
        if(req.getSingleNumber() < 0 || req.getSingleNumber() > 99) {
            throw new RuntimeException("singleNumber musts in range: 0-99!");
        }
        if (req.getNumbers().size() != 27) {
            throw new RuntimeException("Must be 27 numbers!");
        }
        if (!req.getNumbers().contains(req.getSingleNumber())) {
            throw new RuntimeException("Must be main number!");
        }

    }
}


