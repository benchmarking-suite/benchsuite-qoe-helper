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
