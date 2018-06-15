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
