package com.css.challenge;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.css.challenge.constants.Constants.ActionType;
import com.css.challenge.constants.Constants.StorageType;
import com.css.challenge.constants.Constants.Temperature;
import com.css.challenge.ledger.Action;
import com.css.challenge.ledger.ActionLedger;
import com.css.challenge.models.Order;
import com.css.challenge.models.StoredOrder;
import java.util.List;
import org.junit.jupiter.api.Test;

class StorageManagerTest {
  @Test
  void placeUsesIdealStorageWhenAvailable() throws Exception {
    StorageManager manager = new StorageManager();
    ActionLedger ledger = new ActionLedger();
    StoredOrder hotOrder = StoredOrder.builder()
                              .order(new Order("hot1", "tea", Temperature.HOT, 10, 20))
                              .storedAtMicros(0)
                              .build();

    manager.place(hotOrder, ledger);

    List<Action> actions = ledger.snapshot();
    assertEquals(1, actions.size());
    assertEquals(ActionType.PLACE, actions.get(0).getAction());
    assertEquals(StorageType.HEATER, actions.get(0).getTarget());
    assertEquals(StorageType.HEATER, hotOrder.getStorageType());
  }

  @Test
  void placeDiscardsFromShelfWhenEverythingIsFull() throws Exception {
    StorageManager manager = new StorageManager();
    ActionLedger ledger = new ActionLedger();

    for (int i = 0; i < 6; i++) {
      manager.place(StoredOrder.builder()
                       .order(new Order("hot" + i, "tea", Temperature.HOT, 10, 20))
                       .storedAtMicros(0)
                       .build(),
                   ledger);
    }

    for (int i = 0; i < 12; i++) {
      manager.place(StoredOrder.builder()
                       .order(new Order("room" + i, "bread", Temperature.ROOM, 10, 20))
                       .storedAtMicros(0)
                       .build(),
                   ledger);
    }

    StoredOrder extra = StoredOrder.builder()
                            .order(new Order("extra", "pie", Temperature.ROOM, 10, 20))
                            .storedAtMicros(0)
                            .build();
    manager.place(extra, ledger);

    List<Action> actions = ledger.snapshot();
    boolean hasDiscard = actions.stream().anyMatch(a -> a.getAction() == ActionType.DISCARD);
    Action last = actions.get(actions.size() - 1);

    assertTrue(hasDiscard);
    assertEquals(ActionType.PLACE, last.getAction());
    assertEquals(StorageType.SHELF, last.getTarget());
  }

  @Test
  void removeHandlesNullOrder() {
    StorageManager manager = new StorageManager();
    assertDoesNotThrow(() -> manager.remove(null));
  }
}
