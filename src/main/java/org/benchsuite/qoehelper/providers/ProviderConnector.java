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
