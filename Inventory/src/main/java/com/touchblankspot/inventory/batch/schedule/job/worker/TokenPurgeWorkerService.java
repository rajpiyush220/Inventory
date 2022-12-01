package com.touchblankspot.inventory.batch.schedule.job.worker;

import com.touchblankspot.inventory.portal.data.repository.PasswordTokenRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class TokenPurgeWorkerService {

  @NonNull private final PasswordTokenRepository passwordTokenRepository;

  public void purgeExpired() {
    passwordTokenRepository.deleteAllExpired();
  }
}
