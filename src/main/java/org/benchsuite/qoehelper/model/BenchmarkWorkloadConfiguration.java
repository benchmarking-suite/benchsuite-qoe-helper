package org.benchsuite.qoehelper.model;

import java.util.Collection;
import java.util.Map;

public class BenchmarkWorkloadConfiguration {

  private String id;
  private String name;
  private String description;
  private Collection<String> categories;

  public BenchmarkWorkloadConfiguration(){

  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Collection<String> getCategories() {
    return categories;
  }

  public void setCategories(Collection<String> categories) {
    this.categories = categories;
  }



}
