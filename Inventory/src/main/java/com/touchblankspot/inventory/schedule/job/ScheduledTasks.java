package com.touchblankspot.inventory.schedule.job;

import com.touchblankspot.inventory.schedule.job.worker.TokenPurgeWorkerService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class ScheduledTasks {

  @NonNull private final TokenPurgeWorkerService tokenPurgeWorkerService;

  @Scheduled(cron = "${purge.cron.expression:0 0 5 * * ?}")
  public void deleteExpiredToken() {
    tokenPurgeWorkerService.purgeExpired();
  }
}
