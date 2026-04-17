package com.css.challenge.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.css.challenge.constants.Constants.StorageType;
import com.css.challenge.constants.Constants.Temperature;
import com.css.challenge.models.Order;
import com.css.challenge.models.StoredOrder;
import org.junit.jupiter.api.Test;

class CoolerTest {
  @Test
  void addSetsStorageTypeAndRemoveReturnsStoredOrder() {
    Cooler cooler = new Cooler();
    StoredOrder storedOrder = StoredOrder.builder()
                                 .order(new Order("c1", "salad", Temperature.COLD, 100, 20))
                                 .storedAtMicros(0)
                                 .build();

    cooler.add(storedOrder);

    assertEquals(StorageType.COOLER, storedOrder.getStorageType());
    StoredOrder removed = cooler.remove("c1");
    assertNotNull(removed);
    assertEquals("c1", removed.getOrder().getId());
  }

  @Test
  void hasRoomRespectsCapacity() {
    Cooler cooler = new Cooler();

    for (int i = 0; i < 6; i++) {
      cooler.add(StoredOrder.builder()
                     .order(new Order("c" + i, "salad", Temperature.COLD, 100, 20))
                     .storedAtMicros(0)
                     .build());
    }

    assertTrue(!cooler.hasRoom());
  }
}
