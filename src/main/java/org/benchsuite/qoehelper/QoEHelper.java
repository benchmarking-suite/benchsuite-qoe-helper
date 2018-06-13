package org.benchsuite.qoehelper;

import org.benchsuite.qoehelper.model.CloudInfo;
import org.benchsuite.qoehelper.providers.*;

import java.util.Map;

public class QoEHelper {

  private static QoEHelper instance;

  public static QoEHelper getInstance(){
    if(instance == null){
      instance = new QoEHelper();
    }
    return instance;
  }

  public CloudInfo getCloudInfo(
      String provider,
      String identity,
      String credentials,
      Map<String, String> optionalParams) throws ProviderConfigurationException {

		return this.getConnector(provider, identity, credentials, optionalParams).getAllInfo();
	}


  private ProviderConnector getConnector(String provider, String identity, String credentials,
																				 Map<String, String> optionalParams) throws ProviderConfigurationException {
	switch (provider) {
		case "openstack":
			return new OpenstackConnector(identity, credentials, optionalParams);
				
		case "ec2":
			return new EC2Connector(identity, credentials, optionalParams);
	}

	throw new ProviderConfigurationException("Invalid provider type\""+provider+"\"");
  }
}
