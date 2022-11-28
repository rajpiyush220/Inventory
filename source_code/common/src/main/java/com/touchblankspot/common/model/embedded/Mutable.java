package com.touchblankspot.common.model.embedded;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor // required for hibernate but shouldn't be used otherwise
@MappedSuperclass
@Getter
@Setter
public class Mutable extends Versioned {

  @Id private UUID id = UUID.randomUUID();
}
