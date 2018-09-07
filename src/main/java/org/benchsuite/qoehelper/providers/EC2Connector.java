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

import static org.jclouds.aws.ec2.reference.AWSEC2Constants.PROPERTY_EC2_AMI_QUERY;
import static org.jclouds.aws.ec2.reference.AWSEC2Constants.PROPERTY_EC2_CC_AMI_QUERY;
import static org.jclouds.compute.config.ComputeServiceProperties.TIMEOUT_SCRIPT_COMPLETE;

import org.benchsuite.qoehelper.model.HardwareProfile;
import org.benchsuite.qoehelper.model.Image;
import org.benchsuite.qoehelper.model.Network;
import org.benchsuite.qoehelper.model.SecurityGroup;
import org.jclouds.ContextBuilder;
import org.jclouds.aws.ec2.AWSEC2Api;
import org.jclouds.aws.ec2.features.AWSAMIApi;
import org.jclouds.aws.ec2.features.AWSSecurityGroupApi;
import org.jclouds.aws.ec2.options.AWSDescribeImagesOptions;
import org.jclouds.compute.ComputeServiceContext;
import org.jclouds.compute.domain.Hardware;
import org.jclouds.ec2.domain.Subnet;
import org.jclouds.ec2.features.SubnetApi;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class EC2Connector extends ProviderConnector {
	
  public static final String REGION_PARAM   = "region";
  
  private String username;
  private String password;
  private String region;
  
  private ComputeServiceContext context;
  private AWSEC2Api awsec2Context;

  public EC2Connector(String identity, String credentials, Map<String, String> optionalParams) throws ProviderConfigurationException {
    super(identity, credentials, optionalParams);
  }

  @Override
  protected void init(String identity, String credentials, Map<String, String> optionalParams) throws ProviderConfigurationException {

	// check mandatory parameters
	if(identity == null || credentials == null){
	   throw new ProviderConfigurationException("Invalid username or password provided");
	}
	
	// set mandatory parameters
    this.username = identity;
    this.password = credentials;
	    
    // set optional parameters
    if(optionalParams.containsKey(REGION_PARAM)){
      this.region = optionalParams.get(REGION_PARAM);
    }
    else {
      this.region = "us-east-1";
    }
    
    this.connect();	    
  }

  private void connect(){
	  
      Properties properties = new Properties();
      long scriptTimeout = TimeUnit.MILLISECONDS.convert(20, TimeUnit.MINUTES);
      properties.setProperty(TIMEOUT_SCRIPT_COMPLETE, scriptTimeout + "");
      
      context = ContextBuilder.newBuilder("aws-ec2")
              .credentials(this.username, this.password)
              .overrides(properties)
              .buildView(ComputeServiceContext.class);
      
      awsec2Context = context.unwrapApi(AWSEC2Api.class);
  }
  
  @Override
  public Collection<SecurityGroup> listSecurityGroups() {
  	Collection<SecurityGroup> listSecurityGroups = new HashSet<>();

  	AWSSecurityGroupApi secGroups = awsec2Context.getSecurityGroupApi().get();

  	for(org.jclouds.ec2.domain.SecurityGroup sGroups: secGroups.describeSecurityGroupsInRegion(this.region)){
  		SecurityGroup securityGroup = new SecurityGroup();
  		securityGroup.setId(sGroups.getId());
  		securityGroup.setName(sGroups.getName());
  		listSecurityGroups.add(securityGroup);
  	}
  	
  	return listSecurityGroups;
  }

  @Override
  public Collection<Network> listNetworks() {
	  Collection<Network> listNetworks = new HashSet<>();
	
	  SubnetApi subnetsRegion = awsec2Context.getSubnetApiForRegion(this.region).get();
	  for(Subnet subnetRegion: subnetsRegion.list()){
		  Network network = new Network();
      network.setId(subnetRegion.getSubnetId());
      network.setName(subnetRegion.getTags().get("Name")!= null ? subnetRegion.getTags().get("Name") : subnetRegion.getSubnetId() );
		  listNetworks.add(network);
	  }
    
	  return listNetworks;
  }

  @Override
  public Collection<Image> listImages() {
	Collection<Image> listImages = new HashSet<>();

  	AWSAMIApi imagesApi = awsec2Context.getAMIApi().get();


  	Map<String, String> amiFilters = new HashMap<>();

  	Set<? extends org.jclouds.ec2.domain.Image> images = imagesApi.describeImagesInRegion(
  	    this.region,
        AWSDescribeImagesOptions.Builder.filters(amiFilters)
    );

  	for (org.jclouds.ec2.domain.Image i : images){
  	  Image image = new Image();
      image.setId(i.getId());
      image.setName(i.getName());
      image.setDescription(i.getDescription());
      listImages.add(image);        	
    }
              
	return listImages;
  }

  @Override
  public Collection<HardwareProfile> listHardwareProfiles() {
	Collection<HardwareProfile> listHardwareProfiles = new HashSet<>();
  	
  	Set<? extends Hardware> hardwareProfiles = context.getComputeService().listHardwareProfiles();
  	for(Hardware h : hardwareProfiles){
  	  HardwareProfile hardwareProfile = new HardwareProfile();
  	  hardwareProfile.setId(h.getId());
  	  hardwareProfile.setName(h.getName()!=null ? h.getName() : h.getId());
  	  hardwareProfile.setRam(h.getRam());
  	  hardwareProfile.setVcpus(h.getProcessors().get(0).getCores());
  	  listHardwareProfiles.add(hardwareProfile);
  	}
  	
  	return listHardwareProfiles;
  }
}
