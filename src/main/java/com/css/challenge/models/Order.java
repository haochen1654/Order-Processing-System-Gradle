package com.css.challenge.models;

import com.css.challenge.constants.Constants.Temperature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class Order {
  private String id;
  private String name;
  private Temperature temp;
  private int price;
  @Setter private volatile int freshness;

  // Default constructor needed by Jackson
  public Order() {}

  // Optional convenience constructor
  public Order(String id, String name, Temperature temp, int price,
               int freshness) {
    this.id = id;
    this.name = name;
    this.temp = temp;
    this.price = price;
    this.freshness = freshness;
  }

  public static List<Order> parse(String json) throws JsonProcessingException {
    return new ObjectMapper().readValue(json,
                                        new TypeReference<List<Order>>() {});
  }
}
