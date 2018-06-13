package org.benchsuite.qoehelper.providers;

import org.benchsuite.qoehelper.model.*;

import java.util.Collection;
import java.util.Map;

public abstract class ProviderConnector {

  public ProviderConnector(String identity, String credentials, Map<String,String> optionalParams) throws ProviderConfigurationException {
    this.init(identity, credentials, optionalParams);
  }

  public CloudInfo getAllInfo(){
    CloudInfo res = new CloudInfo();
    res.setNetworks(this.listNetworks());
    res.setHardwareProfiles(this.listHardwareProfiles());
    res.setImages(this.listImages());
    res.setSecurityGroups(this.listSecurityGroups());
    return res;
  }

  protected abstract void init(String identity, String credentials, Map<String, String> optionalParams) throws ProviderConfigurationException;

  public abstract Collection<SecurityGroup> listSecurityGroups();

  public abstract Collection<Network> listNetworks();

  public abstract Collection<Image> listImages();

  public abstract Collection<HardwareProfile> listHardwareProfiles();

}
