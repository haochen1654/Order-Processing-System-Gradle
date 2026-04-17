package com.css.challenge.constants;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.css.challenge.constants.Constants.ActionType;
import com.css.challenge.constants.Constants.StorageType;
import com.css.challenge.constants.Constants.Temperature;
import org.junit.jupiter.api.Test;

class ConstantsTest {
  @Test
  void temperatureFromStringIsCaseInsensitive() {
    assertEquals(Temperature.HOT, Temperature.fromString("HOT"));
    assertEquals(Temperature.COLD, Temperature.fromString("cold"));
    assertEquals(Temperature.ROOM, Temperature.fromString("Room"));
  }

  @Test
  void temperatureFromStringRejectsUnknownValues() {
    assertThrows(IllegalArgumentException.class,
                 () -> Temperature.fromString("warm"));
  }

  @Test
  void enumsExposeSerializedValues() {
    assertEquals("heater", StorageType.HEATER.getStorage());
    assertEquals("pickup", ActionType.PICKUP.getAction());
  }

  @Test
  void lockTimeoutConstantIsExpectedValue() {
    assertEquals(500, Constants.LOCK_TIMEOUT);
  }
}
