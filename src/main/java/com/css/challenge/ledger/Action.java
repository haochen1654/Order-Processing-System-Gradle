package com.css.challenge.ledger;

import com.css.challenge.constants.Constants.ActionType;
import com.css.challenge.constants.Constants.StorageType;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Action {
  private final long timestamp;
  private final String id;
  private final ActionType action;
  private final StorageType target;
}
