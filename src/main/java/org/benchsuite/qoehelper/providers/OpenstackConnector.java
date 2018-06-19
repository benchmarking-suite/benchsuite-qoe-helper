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

import org.benchsuite.qoehelper.model.HardwareProfile;
import org.benchsuite.qoehelper.model.Image;
import org.benchsuite.qoehelper.model.Network;
import org.benchsuite.qoehelper.model.SecurityGroup;

import java.util.*;

import org.jclouds.ContextBuilder;
import org.jclouds.openstack.keystone.config.KeystoneProperties;
import org.jclouds.openstack.neutron.v2.NeutronApi;
import org.jclouds.openstack.neutron.v2.features.NetworkApi;
import org.jclouds.openstack.nova.v2_0.NovaApi;
import org.jclouds.openstack.nova.v2_0.domain.Flavor;
import org.jclouds.openstack.nova.v2_0.extensions.SecurityGroupApi;
import org.jclouds.openstack.nova.v2_0.features.FlavorApi;
import org.jclouds.openstack.nova.v2_0.features.ImageApi;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class OpenstackConnector extends ProviderConnector {

  private static final Logger logger = LoggerFactory.getLogger(OpenstackConnector.class);

  public static final String AUTH_URL_PARAM = "authUrl";
  public static final String REGION_PARAM   = "region";
  public static final String PROJECT_PARAM = "project";

  // v3 authentication only parameters
  public static final String USER_DOMAIN_PARAM = "userDomain";
  public static final String PROJECT_DOMAIN_PARAM = "projectDomain";


  private String username;
  private String password;
  private String authUrl;
  private String region;
  private String project;
  private String userDomain;
  // FIXME: never used?
  private String projectDomain;


  private NovaApi novaApi;
  private NeutronApi neutronApi;

  public OpenstackConnector(String identity, String credentials, Map<String, String> optionalParams) throws ProviderConfigurationException {
    super(identity, credentials, optionalParams);
  }

  @Override
  protected void init(String identity, String credentials, Map<String, String> optionalParams) throws ProviderConfigurationException {

    // check mandatory parameters
    if(identity == null || credentials == null){
      throw new ProviderConfigurationException("Invalid username or password provided");
    }

    if(!optionalParams.containsKey(AUTH_URL_PARAM)){
      throw new ProviderConfigurationException("authUrl parameter is mandatory for Openstack");
    }

    // set mandatory parameters
    this.username = identity;
    this.password = credentials;
    this.authUrl = optionalParams.get(AUTH_URL_PARAM);


    // set optional parameters
    if(optionalParams.containsKey(REGION_PARAM)){
      this.region = optionalParams.get(REGION_PARAM);
    }
    else {
      this.region = "RegionOne";
    }

    if(optionalParams.containsKey(USER_DOMAIN_PARAM)){
      this.userDomain = optionalParams.get(USER_DOMAIN_PARAM);
    }
    else {
      this.userDomain = "Default";
    }

    if(optionalParams.containsKey(PROJECT_DOMAIN_PARAM)){
      this.projectDomain = optionalParams.get(PROJECT_DOMAIN_PARAM);
    }
    else {
      this.projectDomain = "default";
    }

    if(optionalParams.containsKey(PROJECT_PARAM)){
      this.project = optionalParams.get(PROJECT_PARAM);
    }
    else {
      this.project = this.username;
    }

    this.connect();
  }

  private void connect(){

    try {
      logger.debug("Trying v3 authentication");

      // first try with v3 authentication. If it fails, fallback to v2
      Properties overrides = new Properties();
      overrides.put(KeystoneProperties.KEYSTONE_VERSION, "3");
      overrides.put(KeystoneProperties.SCOPE, "project:" + this.project);
      String url = this.authUrl + "/v3";
      String id = this.userDomain + ":" + this.username;
      tryLogin(url, id, this.password, overrides);

    }
    catch (Exception ex){

      logger.warn("v3 authentication failed with \"" + ex.getMessage() + "\"");
      logger.debug("Trying v2 authentication");

      // perform v2 authentication
      Properties overrides = new Properties();
      overrides.put(KeystoneProperties.KEYSTONE_VERSION, "2");
      String url = this.authUrl + "/v2.0/";
      String id = this.project + ":" + this.username;

      tryLogin(url, id, this.password, overrides);

    }

  }


  private void tryLogin(String url, String id, String password, Properties overrides){
    novaApi = ContextBuilder.newBuilder("openstack-nova")
        		.endpoint(url)
            .credentials(id, password)
            .overrides(overrides)
            .buildApi(NovaApi.class);

    neutronApi = ContextBuilder.newBuilder("openstack-neutron")
            .endpoint(url)
            .credentials(id, password)
            .overrides(overrides)
            .buildApi(NeutronApi.class);

    // try an API call to test if the authentication is fine
    novaApi.getConfiguredRegions();
  }

  @Override
  public Collection<SecurityGroup> listSecurityGroups() {
	Collection<SecurityGroup> listSecurityGroups = new HashSet<>();
	  
 	SecurityGroupApi securityGroups = novaApi.getSecurityGroupApi(this.region).get();

	for (org.jclouds.openstack.nova.v2_0.domain.SecurityGroup sGroup : securityGroups.list()) {
 		SecurityGroup securityGroup = new SecurityGroup();
 		securityGroup.setId(sGroup.getId());
 		securityGroup.setName(sGroup.getName());
 		listSecurityGroups.add(securityGroup);
 	}
 	 
 	return listSecurityGroups;
  }

  @Override
  public Collection<Network> listNetworks() {
    Collection<Network> listNetworks = new HashSet<>();

    NetworkApi networksNeutron = neutronApi.getNetworkApi(this.region);

    for(org.jclouds.openstack.neutron.v2.domain.Network n: networksNeutron.list().concat()){
      Network network = new Network();
      network.setId(n.getId());
      network.setName(n.getName());
      listNetworks.add(network);
    }

    return listNetworks;
  }

  @Override
  public Collection<Image> listImages() {
	Collection<Image> listImages = new HashSet<>();

  	ImageApi imageApi = novaApi.getImageApi(this.region);

    for (org.jclouds.openstack.nova.v2_0.domain.Image i : imageApi.listInDetail().concat()) {
      Image image = new Image();
      image.setId(i.getId());
      image.setName(i.getName());
      image.setDescription(i.getMetadata().get("description"));
      listImages.add(image);
    }
      
    return listImages;
  }

  @Override
  public Collection<HardwareProfile> listHardwareProfiles() {
	Collection<HardwareProfile> listHardwareProfiles = new HashSet<>();

  	FlavorApi flavorApi = novaApi.getFlavorApi(this.region);
    for (Flavor flavor : flavorApi.listInDetail().concat()) {
  	  HardwareProfile hardwareProfile = new HardwareProfile();
	    hardwareProfile.setId(flavor.getId());
	    hardwareProfile.setName(flavor.getName());
	    hardwareProfile.setRam(flavor.getRam());
	    hardwareProfile.setVcpus(flavor.getVcpus());
	    listHardwareProfiles.add(hardwareProfile);
    }
		
	return listHardwareProfiles;
  }
}
