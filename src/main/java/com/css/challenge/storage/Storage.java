package com.css.challenge.storage;

import com.css.challenge.models.StoredOrder;

public interface Storage {

  public boolean hasRoom();

  public void add(StoredOrder storedOrder);

  public StoredOrder remove(String orderId);
}
