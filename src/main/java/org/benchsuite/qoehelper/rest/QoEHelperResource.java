package org.benchsuite.qoehelper.rest;

import org.benchsuite.qoehelper.QoEHelper;
import org.benchsuite.qoehelper.model.CloudInfo;

import java.io.IOException;
import java.util.Map;

public class QoEHelperResource {

  private QoEHelper service;

  public QoEHelperResource(){
    this.service = new QoEHelper();
  }

  public CloudInfo getCloudInfo(
		   String provider,
		   String identity,
		   String credentials,
           Map<String, String> optionalParameters) throws IOException{

    return this.service.getCloudInfo(provider, identity, credentials, optionalParameters);
  }
}
