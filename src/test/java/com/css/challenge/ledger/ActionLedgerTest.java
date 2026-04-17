package com.css.challenge.ledger;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.css.challenge.constants.Constants.ActionType;
import com.css.challenge.constants.Constants.StorageType;
import java.util.List;
import org.junit.jupiter.api.Test;

class ActionLedgerTest {
  @Test
  void recordAndSnapshotStoreActionsInOrder() throws Exception {
    ActionLedger ledger = new ActionLedger();
    Action first = Action.builder()
                       .timestamp(1)
                       .id("a")
                       .action(ActionType.PLACE)
                       .target(StorageType.SHELF)
                       .build();
    Action second = Action.builder()
                        .timestamp(2)
                        .id("b")
                        .action(ActionType.PICKUP)
                        .target(StorageType.HEATER)
                        .build();

    ledger.record(first);
    ledger.record(second);

    List<Action> actions = ledger.snapshot();
    assertEquals(2, actions.size());
    assertEquals("a", actions.get(0).getId());
    assertEquals("b", actions.get(1).getId());
  }
}
