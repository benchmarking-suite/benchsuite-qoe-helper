package org.benchsuite.qoehelper;

import org.benchsuite.qoehelper.model.CloudInfo;

import java.util.Map;

public class QoEHelper {

  private static QoEHelper instance;

  public static QoEHelper getInstance(){
    if(instance == null){
      instance = new QoEHelper();
    }
    return instance;
  }

  /**
   *
   * @param provider is the type of the provider (e.g. "aws-ec2", "openstack", ...)
   * @param identity is the username
   * @param credentials is the password
   * @param optionalParams is a map of parameters that are needed only for some
   *                       providers (e.g. Openstack projectName)
   * @return
   */
  public CloudInfo getCloudInfo(
      String provider,
      String identity,
      String credentials,
      Map<String, String> optionalParams){

    CloudInfo result = new CloudInfo();


    return result;
  }

}
