package org.benchsuite.qoehelper.model;

import java.util.*;

public class BenchmarkConfiguration {

  private String id;
  private String name;
  private String description;
  private Collection<BenchmarkWorkloadConfiguration> workloads;

  public BenchmarkConfiguration(){
  }

  public BenchmarkConfiguration(String id, Map<String, Map<String, String>> rawConfig){

    this.id = id;

    if(rawConfig.containsKey("DEFAULT")){
      this.name = rawConfig.get("DEFAULT").get("tool_name");
      this.description = rawConfig.get("DEFAULT").get("tool_description");
    }

    this.workloads = new HashSet<>();

    rawConfig.forEach((k, v) -> {
      if(k.equals("DEFAULT"))
        return;
      BenchmarkWorkloadConfiguration w = new BenchmarkWorkloadConfiguration();
      w.setId(k);
      w.setName(v.get("workload_name"));
      w.setDescription(v.get("workload_description"));
      if(w.getDescription() == null){
        w.setDescription(v.get("description"));
      }
      if(v.containsKey("categories")) {
        List<String> cats = Arrays.asList(v.get("categories").split(","));
        cats.replaceAll(String::trim);
        w.setCategories(cats);
      }
      this.workloads.add(w);
    });

  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Collection<BenchmarkWorkloadConfiguration> getWorkloads() {
    return workloads;
  }

  public void setWorkloads(Collection<BenchmarkWorkloadConfiguration> workloads) {
    this.workloads = workloads;
  }
}
