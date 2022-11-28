package com.touchblankspot.inventory.config;

import com.touchblankspot.inventory.service.RoleCacheService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class PostStartupConfig {

  @NonNull private final RoleCacheService roleCacheService;

  @EventListener(ApplicationReadyEvent.class)
  public void onApplicationReadyEvent() {
    Executors.newSingleThreadScheduledExecutor()
        .schedule(() -> runAfterStartup(), 20, TimeUnit.SECONDS);
  }

  public void runAfterStartup() {
    roleCacheService.clearAllRolePermissionCache();
    log.info("Application role cache flushed");
  }
}
