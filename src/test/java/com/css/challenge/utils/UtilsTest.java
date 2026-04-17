package com.css.challenge.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.css.challenge.constants.Constants.ActionType;
import com.css.challenge.constants.Constants.StorageType;
import com.css.challenge.constants.Constants.Temperature;
import com.css.challenge.ledger.Action;
import com.css.challenge.models.Order;
import com.css.challenge.models.StoredOrder;
import org.junit.jupiter.api.Test;

class UtilsTest {
  @Test
  void toPrettyJsonProducesReadableJson() throws Exception {
    String json = Utils.toPrettyJson(new Order("id", "name", Temperature.ROOM, 1, 2));

    assertTrue(json.contains("\n"));
    assertTrue(json.contains("\"id\""));
  }

  @Test
  void currentTimestampMicrosIsPositive() {
    assertTrue(Utils.currentTimestampMicros() > 0);
  }

  @Test
  void buildActionCopiesOrderIdAndArguments() {
    StoredOrder storedOrder = StoredOrder.builder()
                                 .order(new Order("order-1", "soup", Temperature.HOT, 10, 20))
                                 .storedAtMicros(0)
                                 .build();

    Action action = Utils.buildAction(storedOrder, StorageType.HEATER, ActionType.PLACE);

    assertEquals("order-1", action.getId());
    assertEquals(StorageType.HEATER, action.getTarget());
    assertEquals(ActionType.PLACE, action.getAction());
    assertTrue(action.getTimestamp() > 0);
  }
}
