package com.css.challenge.models;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.css.challenge.constants.Constants.StorageType;
import com.css.challenge.constants.Constants.Temperature;
import org.junit.jupiter.api.Test;

class StoredOrderTest {
  @Test
  void idealStorageExpiresOnlyAfterFreshness() {
    Order order = new Order("o1", "tea", Temperature.HOT, 100, 10);
    StoredOrder storedOrder = StoredOrder.builder()
                                 .order(order)
                                 .storedAtMicros(0)
                                 .storageType(StorageType.HEATER)
                                 .build();

    assertFalse(storedOrder.isExpired(10_000_000L));
    assertTrue(storedOrder.isExpired(11_000_000L));
  }

  @Test
  void nonIdealStorageAgesTwiceAsFast() {
    Order order = new Order("o2", "ice cream", Temperature.COLD, 100, 10);
    StoredOrder storedOrder = StoredOrder.builder()
                                 .order(order)
                                 .storedAtMicros(0)
                                 .storageType(StorageType.SHELF)
                                 .build();

    assertTrue(storedOrder.isExpired(6_000_000L));
  }
}
