package org.benchsuite.qoehelper;

import org.benchsuite.qoehelper.model.CloudInfo;
import org.benchsuite.qoehelper.model.HardwareProfile;
import org.benchsuite.qoehelper.model.Image;
import org.benchsuite.qoehelper.model.Network;
import org.benchsuite.qoehelper.model.SecurityGroup;
import org.benchsuite.qoehelper.providers.JCloudsAmazon;
import org.benchsuite.qoehelper.providers.JCloudsNova;

import java.io.IOException;
import java.util.Collection;
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
 * @throws IOException 
   */
  
  
  
  public CloudInfo getCloudInfo(
      String provider,
      String identity,
      String credentials,
      Map<String, String> optionalParams) throws IOException{
	  
	  String host = optionalParams.get("host");
	  String region = optionalParams.get("region");

      CloudInfo result = new CloudInfo();

	  switch(provider) {
	 
	  case "aws-ec2":
	 	   	
		 	JCloudsAmazon jcloudsAmazon = new JCloudsAmazon(identity,credentials);
		 	try {
		 			if(region==null) 
		 				region ="us-east-1";

		 			Collection<Image> listImages = jcloudsAmazon.listImages(region);
		 			Collection<HardwareProfile> listHardwareProfiles = jcloudsAmazon.listHardwareProfiles(region);
		 			Collection<SecurityGroup> listSecurityGroups = jcloudsAmazon.listSecurityGroup(region);
		 			Collection<Network> listNetworks = jcloudsAmazon.listNetwork(region);
		 			
		 			result.setImages(listImages);
		 			result.setHardwareProfiles(listHardwareProfiles);
		 			result.setSecurityGroups(listSecurityGroups);
		 			result.setNetworks(listNetworks);
		
		         jcloudsAmazon.close();
		     } catch (Exception e) {
		         e.printStackTrace();
		     } finally {
		     	jcloudsAmazon.close();
		     }
		 	break;
	 	
	  case "openstack-nova":  
		  
		 	JCloudsNova jcloudsNova = new JCloudsNova(identity,credentials,host);
		 	try {
		 			if(region==null)
		 				region="Vicenza";

		 		    Collection<Image> listImages = jcloudsNova.listImageAPI(region);
		 		    Collection<HardwareProfile> listHardwareProfiles = jcloudsNova.listFlavorAPI(region);
		 		    Collection<SecurityGroup> listSecurityGroups = jcloudsNova.listSecurityGroup(region);
		 		    Collection<Network> listNetworks = jcloudsNova.listNetwork(region);
		 		   
		 		    result.setImages(listImages);
		 		    result.setHardwareProfiles(listHardwareProfiles);
		 		    result.setSecurityGroups(listSecurityGroups);
		 		    result.setNetworks(listNetworks);
		 		
		         jcloudsNova.close();
		     } catch (Exception e) {
		         e.printStackTrace();
		     } finally {
		         jcloudsNova.close();
		     }
		 	break;
    }	

    return result;
  }

}
