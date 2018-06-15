/* Benchmarking Suite
   Copyright 2018 Engineering Ingegneria Informatica S.p.A.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

   Developed in the ARTIST EU project (www.artist-project.eu) and in the
   CloudPerfect EU project (https://cloudperfect.eu/)
*/
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
