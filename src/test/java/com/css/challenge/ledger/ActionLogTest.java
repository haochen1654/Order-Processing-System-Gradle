package com.css.challenge.ledger;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.css.challenge.models.Options;
import java.util.List;
import org.junit.jupiter.api.Test;

class ActionLogTest {
  @Test
  void builderAndSetterWork() {
    Options options = Options.builder().rate(1).min(2).max(3).build();
    Action action = Action.builder().timestamp(1).id("x").build();

    ActionLog log = ActionLog.builder().options(options).build();
    log.setActions(List.of(action));

    assertEquals(options, log.getOptions());
    assertEquals(1, log.getActions().size());
    assertEquals("x", log.getActions().get(0).getId());
  }
}
