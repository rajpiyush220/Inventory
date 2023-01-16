package com.touchblankspot.inventory.batch.schedule.job;

import com.touchblankspot.inventory.batch.schedule.job.worker.TokenPurgeWorkerService;
import com.touchblankspot.inventory.portal.service.ReportService;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

  @NonNull private final ReportService reportService;

  private static final DateTimeFormatter reportDateTimeFormatter =
      DateTimeFormatter.ofPattern("dd-MM-yyyy");

  @Scheduled(cron = "${purge.cron.expression:0 0 5 * * ?}")
  public void deleteExpiredToken() {
    tokenPurgeWorkerService.purgeExpired();
  }

  // this will execute at everyday 1 am
  @Scheduled(cron = "0 1 * * * ?}")
  public void executeDailySalesReportJob() {
    LocalDate reportExecutionDate = LocalDate.now().minusDays(1);
    reportService.generateDailySalesDetailsReport(
        reportDateTimeFormatter.format(reportExecutionDate));
    log.info("Sales daily report generated for {}", reportExecutionDate);
  }
}
