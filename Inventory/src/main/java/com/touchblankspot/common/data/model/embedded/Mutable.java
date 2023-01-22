package com.touchblankspot.common.data.model.embedded;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@MappedSuperclass
@Getter
@Setter
public class Mutable extends Versioned {

  @Id private UUID id = UUID.randomUUID();
}
