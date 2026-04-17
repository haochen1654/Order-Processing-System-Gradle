package com.css.challenge.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.css.challenge.constants.Constants.StorageType;
import com.css.challenge.constants.Constants.Temperature;
import com.css.challenge.models.Order;
import com.css.challenge.models.StoredOrder;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class ShelfTest {
  @Test
  void addAndRemoveWorkAndSetStorageType() {
    Shelf shelf = new Shelf();
    StoredOrder order = StoredOrder.builder()
                            .order(new Order("s1", "bread", Temperature.ROOM, 100, 30))
                            .storedAtMicros(0)
                            .build();

    shelf.add(order);

    assertEquals(StorageType.SHELF, order.getStorageType());
    assertNotNull(shelf.remove("s1"));
    assertEquals(null, shelf.remove("s1"));
  }

  @Test
  void discardWorstReturnsLowestRemainingFreshness() {
    Shelf shelf = new Shelf();
    StoredOrder best = StoredOrder.builder()
                           .order(new Order("best", "fresh", Temperature.ROOM, 10, 100))
                           .storedAtMicros(0)
                           .build();
    StoredOrder worst = StoredOrder.builder()
                            .order(new Order("worst", "old", Temperature.ROOM, 10, 1))
                            .storedAtMicros(0)
                            .build();

    shelf.add(best);
    shelf.add(worst);

    StoredOrder discarded = shelf.discardWorst();
    assertNotNull(discarded);
    assertEquals("worst", discarded.getOrder().getId());
  }

  @Test
  void findMovableForIdealFiltersByTemperature() {
    Shelf shelf = new Shelf();
    shelf.add(StoredOrder.builder()
                  .order(new Order("hot-1", "tea", Temperature.HOT, 10, 20))
                  .storedAtMicros(0)
                  .build());
    shelf.add(StoredOrder.builder()
                  .order(new Order("room-1", "bread", Temperature.ROOM, 10, 20))
                  .storedAtMicros(0)
                  .build());

    Optional<StoredOrder> hot = shelf.findMovableForIdeal(Temperature.HOT);
    Optional<StoredOrder> cold = shelf.findMovableForIdeal(Temperature.COLD);

    assertTrue(hot.isPresent());
    assertEquals("hot-1", hot.get().getOrder().getId());
    assertFalse(cold.isPresent());
  }

  @Test
  void hasRoomRespectsCapacity() {
    Shelf shelf = new Shelf();
    for (int i = 0; i < 12; i++) {
      shelf.add(StoredOrder.builder()
                    .order(new Order("s" + i, "r", Temperature.ROOM, 10, 30))
                    .storedAtMicros(0)
                    .build());
    }

    assertFalse(shelf.hasRoom());
  }
}
