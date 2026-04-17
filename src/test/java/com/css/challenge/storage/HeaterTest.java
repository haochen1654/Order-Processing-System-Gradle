package com.css.challenge.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.css.challenge.constants.Constants.StorageType;
import com.css.challenge.constants.Constants.Temperature;
import com.css.challenge.models.Order;
import com.css.challenge.models.StoredOrder;
import org.junit.jupiter.api.Test;

class HeaterTest {
  @Test
  void addSetsStorageTypeAndRemoveReturnsStoredOrder() {
    Heater heater = new Heater();
    StoredOrder storedOrder = StoredOrder.builder()
                                 .order(new Order("h1", "tea", Temperature.HOT, 100, 20))
                                 .storedAtMicros(0)
                                 .build();

    heater.add(storedOrder);

    assertEquals(StorageType.HEATER, storedOrder.getStorageType());
    StoredOrder removed = heater.remove("h1");
    assertNotNull(removed);
    assertEquals("h1", removed.getOrder().getId());
  }

  @Test
  void hasRoomRespectsCapacity() {
    Heater heater = new Heater();

    for (int i = 0; i < 6; i++) {
      heater.add(StoredOrder.builder()
                     .order(new Order("h" + i, "tea", Temperature.HOT, 100, 20))
                     .storedAtMicros(0)
                     .build());
    }

    assertTrue(!heater.hasRoom());
  }
}
