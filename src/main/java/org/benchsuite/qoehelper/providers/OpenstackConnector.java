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

public class OpenstackConnector extends ProviderConnector {

  public static final String AUTH_URL_PARAM = "authUrl";
  public static final String REGION_PARAM   = "region";
  public static final String PROJECT_PARAM = "project";

  private String username;
  private String password;
  private String authUrl;
  private String region;
  private String project;

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

    if(optionalParams.containsKey(PROJECT_PARAM)){
      this.project = optionalParams.get(PROJECT_PARAM);
    }
    else {
      this.project = this.username;
    }

    this.connect();
  }

  private void connect(){

    Properties overrides = new Properties();

    // TODO: authVersion could also be "3". If "2" fails, we should try "3"
    overrides.put(KeystoneProperties.KEYSTONE_VERSION, "2");
    String v2AuthUrl = this.authUrl + "/v2.0/";
    String id = this.project + ":" + this.username;

    novaApi = ContextBuilder.newBuilder("openstack-nova")
        		.endpoint(v2AuthUrl)
            .credentials(id, this.password)
            .overrides(overrides)
            .buildApi(NovaApi.class);

    neutronApi = ContextBuilder.newBuilder("openstack-neutron")
            .endpoint(v2AuthUrl)
            .credentials(id, this.password)
            .overrides(overrides)
            .buildApi(NeutronApi.class);
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
