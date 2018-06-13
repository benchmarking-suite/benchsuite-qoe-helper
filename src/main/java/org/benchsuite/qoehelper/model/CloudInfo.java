package org.benchsuite.qoehelper.model;

import java.util.Collection;

public class CloudInfo {

  private Collection<Image> images;
  private Collection<HardwareProfile> hardwareProfiles;
  private Collection<Network> networks;
  private Collection<SecurityGroup> securityGroups;

  public CloudInfo() {
  }

  public Collection<Image> getImages() {
    return images;
  }

  public void setImages(Collection<Image> images) {
    this.images = images;
  }

  public Collection<HardwareProfile> getHardwareProfiles() {
    return hardwareProfiles;
  }

  public void setHardwareProfiles(Collection<HardwareProfile> hardwareProfiles) {
    this.hardwareProfiles = hardwareProfiles;
  }

  public Collection<Network> getNetworks() {
    return networks;
  }

  public void setNetworks(Collection<Network> networks) {
    this.networks = networks;
  }

  public Collection<SecurityGroup> getSecurityGroups() {
    return securityGroups;
  }

  public void setSecurityGroups(Collection<SecurityGroup> securityGroups) {
    this.securityGroups = securityGroups;
  }


  @Override
  public String toString() {
    return "CloudInfo{" +
        "images=" + images +
        ", hardwareProfiles=" + hardwareProfiles +
        ", networks=" + networks +
        ", securityGroups=" + securityGroups +
        '}';
  }
}
