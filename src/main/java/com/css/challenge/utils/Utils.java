package com.css.challenge.utils;

import com.css.challenge.constants.Constants.ActionType;
import com.css.challenge.constants.Constants.StorageType;
import com.css.challenge.ledger.Action;
import com.css.challenge.models.StoredOrder;
import com.fasterxml.jackson.databind.ObjectMapper;


public class Utils {
  public static String toPrettyJson(Object obj) throws Exception {
    ObjectMapper mapper = new ObjectMapper();

    // Convert object to pretty JSON string
    return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
  }

  public static Long currentTimestampMicros() {
    return System.currentTimeMillis() * 1_000L;
  }

  public static Action buildAction(StoredOrder storedOrder, StorageType target,
                                   ActionType action) {
    return Action.builder()
        .timestamp(currentTimestampMicros())
        .action(action)
        .id(storedOrder.getOrder().getId())
        .target(target)
        .build();
  }
}
