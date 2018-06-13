package org.benchsuite.qoehelper.providers;

import org.benchsuite.qoehelper.model.HardwareProfile;
import org.benchsuite.qoehelper.model.Image;
import org.benchsuite.qoehelper.model.Network;
import org.benchsuite.qoehelper.model.SecurityGroup;

import java.util.Collection;
import java.util.Map;

public class EC2Connector extends ProviderConnector {

  public EC2Connector(String identity, String credentials, Map<String, String> optionalParams) throws ProviderConfigurationException {
    super(identity, credentials, optionalParams);
  }

  @Override
  protected void init(String identity, String credentials, Map<String, String> optionalParams) throws ProviderConfigurationException {

  }

  @Override
  public Collection<SecurityGroup> listSecurityGroups() {
    return null;
  }

  @Override
  public Collection<Network> listNetworks() {
    return null;
  }

  @Override
  public Collection<Image> listImages() {
    return null;
  }

  @Override
  public Collection<HardwareProfile> listHardwareProfiles() {
    return null;
  }
}
