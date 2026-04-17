package com.css.challenge.storage;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class StorageTest {
  @Test
  void implementationsFollowStorageContractType() {
    assertTrue(new Heater() instanceof Storage);
    assertTrue(new Cooler() instanceof Storage);
    assertTrue(new Shelf() instanceof Storage);
  }
}
