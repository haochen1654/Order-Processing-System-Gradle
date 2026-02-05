package com.css.challenge.ledger;

import com.css.challenge.models.Options;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
public class ActionLog {
  private final Options options;
  @Setter private List<Action> actions;
}
