package com.ducdathua.prediction_app.scheduler;

import com.ducdathua.prediction_app.service.ResultAnalysisService;
import com.ducdathua.prediction_app.service.StreakService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class StreakScheduler {
    private final StreakService streakService;

    public StreakScheduler(StreakService streakService, ResultAnalysisService resultService) {
        this.streakService = streakService;

    }

    //Chay moi ngay luc 2 pm
    @Scheduled(cron = "0 0 1 * * ?")
    public void runDaily() {
        System.out.println("Auto rebuild streak...");
        streakService.rebuildLatestDayStreaks();

    }
}
