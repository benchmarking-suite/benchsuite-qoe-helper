package org.benchsuite.qoehelper.model;

public class HardwareProfile {

  private String id;
  private String name;
  private int ram;
  private double vcpus;

  public HardwareProfile() {
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

  public int getRam() {
    return ram;
  }

  public void setRam(int ram) {
    this.ram = ram;
  }

  public double getVcpus() {
    return vcpus;
  }

  public void setVcpus(double d) {
    this.vcpus = d;
  }
}
