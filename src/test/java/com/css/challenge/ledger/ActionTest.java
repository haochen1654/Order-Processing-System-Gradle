package com.css.challenge.ledger;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.css.challenge.constants.Constants.ActionType;
import com.css.challenge.constants.Constants.StorageType;
import org.junit.jupiter.api.Test;

class ActionTest {
  @Test
  void builderSetsFields() {
    Action action = Action.builder()
                        .timestamp(1234L)
                        .id("o1")
                        .action(ActionType.PLACE)
                        .target(StorageType.SHELF)
                        .build();

    assertEquals(1234L, action.getTimestamp());
    assertEquals("o1", action.getId());
    assertEquals(ActionType.PLACE, action.getAction());
    assertEquals(StorageType.SHELF, action.getTarget());
  }
}
