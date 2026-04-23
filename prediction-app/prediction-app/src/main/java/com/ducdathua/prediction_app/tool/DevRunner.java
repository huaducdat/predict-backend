package com.ducdathua.prediction_app.tool;

import com.ducdathua.prediction_app.service.StreakService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev") // 🔥 chỉ chạy khi dev
public class DevRunner implements CommandLineRunner {

    private final StreakService streakService;

    public DevRunner(StreakService streakService) {
        this.streakService = streakService;
    }

    @Override
    public void run(String... args) {

        System.out.println("🚀 Running DEV STREAK TOOL...");

        streakService.rebuildLatestDayStreaks();

        System.out.println("✅ DONE");
    }
}