package com.css.challenge.models;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class OptionsTest {
  @Test
  void builderSetsAllFields() {
    Options options = Options.builder().rate(500).min(4000).max(8000).build();

    assertEquals(500, options.getRate());
    assertEquals(4000, options.getMin());
    assertEquals(8000, options.getMax());
  }
}
