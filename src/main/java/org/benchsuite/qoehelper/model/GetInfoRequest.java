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

import java.util.Map;

public class GetInfoRequest {

  private String provider;
  private String identity;
  private String credentials;
  private Map<String, String> optionalParameters;

  public GetInfoRequest(){

  }

  public String getProvider() {
    return provider;
  }

  public void setProvider(String provider) {
    this.provider = provider;
  }

  public String getIdentity() {
    return identity;
  }

  public void setIdentity(String identity) {
    this.identity = identity;
  }

  public String getCredentials() {
    return credentials;
  }

  public void setCredentials(String credentials) {
    this.credentials = credentials;
  }

  public Map<String, String> getOptionalParameters() {
    return optionalParameters;
  }

  public void setOptionalParameters(Map<String, String> optionalParameters) {
    this.optionalParameters = optionalParameters;
  }

}
