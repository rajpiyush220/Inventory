package com.touchblankspot.common.model.embedded;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.ReadOnlyProperty;

@Data
@NoArgsConstructor // required for hibernate but shouldn't be used otherwise
@MappedSuperclass
public class Immutable {
  @Id private UUID id = UUID.randomUUID();

  @Column(nullable = false, updatable = false)
  @Version
  @ReadOnlyProperty
  private Long version;

  @Column(nullable = false, updatable = false)
  @CreatedDate
  @JsonIgnore
  private OffsetDateTime created =
      OffsetDateTime.now()
          .truncatedTo(ChronoUnit.MICROS); // To match with column definition datetime(6)
}
