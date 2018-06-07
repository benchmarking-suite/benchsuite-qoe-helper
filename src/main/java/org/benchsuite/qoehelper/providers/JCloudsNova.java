package org.benchsuite.qoehelper.providers;

import ch.qos.logback.core.Context;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multiset;
import com.google.common.io.Closeables;
import com.google.inject.Module;

import org.benchsuite.qoehelper.model.HardwareProfile;
import org.jclouds.Constants;
import org.jclouds.ContextBuilder;
import org.jclouds.aws.ec2.compute.AWSEC2TemplateOptions;
import org.jclouds.aws.ec2.reference.AWSEC2Constants;
import org.jclouds.compute.ComputeService;
import org.jclouds.compute.ComputeServiceContext;
import org.jclouds.compute.domain.ComputeMetadata;
import org.jclouds.compute.domain.Image;
import org.jclouds.compute.domain.NodeMetadata;
import org.jclouds.compute.domain.OsFamily;
import org.jclouds.compute.domain.Template;
import org.jclouds.domain.Location;
import org.jclouds.logging.slf4j.config.SLF4JLoggingModule;
import org.jclouds.openstack.keystone.config.KeystoneProperties;
import org.jclouds.openstack.nova.v2_0.NovaApi;
import org.jclouds.openstack.nova.v2_0.domain.Address;
import org.jclouds.openstack.nova.v2_0.domain.Flavor;
import org.jclouds.openstack.nova.v2_0.domain.SecurityGroup;
import org.jclouds.openstack.nova.v2_0.domain.Server;
import org.jclouds.openstack.nova.v2_0.extensions.FloatingIPApi;
import org.jclouds.openstack.nova.v2_0.extensions.KeyPairApi;
import org.jclouds.openstack.nova.v2_0.extensions.SecurityGroupApi;
import org.jclouds.openstack.nova.v2_0.features.FlavorApi;
import org.jclouds.openstack.nova.v2_0.features.ImageApi;
import org.jclouds.openstack.nova.v2_0.features.ServerApi;
import org.jclouds.sshj.config.SshjSshClientModule;
import org.jclouds.openstack.neutron.v2.NeutronApi;
import org.jclouds.openstack.neutron.v2.domain.Network;
import org.jclouds.openstack.neutron.v2.domain.Router;
import org.jclouds.openstack.neutron.v2.domain.Subnet;
import org.jclouds.openstack.neutron.v2.extensions.RouterApi;
import org.jclouds.openstack.neutron.v2.features.NetworkApi;
import org.jclouds.openstack.neutron.v2.features.PortApi;
import org.jclouds.openstack.neutron.v2.features.SubnetApi;

import static org.jclouds.aws.ec2.reference.AWSEC2Constants.PROPERTY_EC2_AMI_QUERY;
import static org.jclouds.aws.ec2.reference.AWSEC2Constants.PROPERTY_EC2_CC_AMI_QUERY;
import static org.jclouds.compute.config.ComputeServiceProperties.TIMEOUT_SCRIPT_COMPLETE;

import java.io.Closeable;
import java.io.IOException;
import java.net.Authenticator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class JCloudsNova implements Closeable {
    private final NovaApi novaApi;
    private final NeutronApi neutronApi;
//    private final Set<String> regions;

    public JCloudsNova(String identity, String credential,String host) {
        Iterable<Module> modules = ImmutableSet.<Module>of(new SLF4JLoggingModule());
        
        final Properties overrides = new Properties();
        overrides.put(KeystoneProperties.KEYSTONE_VERSION, "2");

        novaApi = ContextBuilder.newBuilder("openstack-nova")
//                .endpoint("http://10.0.16.11:5000/v2.0/")    // Cosmote
        		.endpoint("https://"+host+":5000/v2.0")    // Fiware
                .credentials(identity, credential)
                //.modules(modules)
                .overrides(overrides)
                .buildApi(NovaApi.class);
        
        neutronApi = ContextBuilder.newBuilder("openstack-neutron")
//              .endpoint("http://10.0.16.11:5000/v2.0/")    // Cosmote
      		  .endpoint("https://"+host+":5000/v2.0")    // Fiware
              .credentials(identity, credential)
              //.modules(modules)
              .overrides(overrides)
              .buildApi(NeutronApi.class);
        
//        context = ContextBuilder.newBuilder("openstack-nova")
////              .endpoint("http://10.0.16.11:5000/v2.0/")    // Cosmote
//      		.endpoint("https://"+host+":5000/v2.0")    // Fiware
//              .credentials(identity, credential)
//              //.modules(modules)
//              .overrides(overrides)
//              .buildApi(ComputeServiceContext.class);
//        novaApi = context.unwrapApi(NovaApi.class);
        
//      regions = novaApi.getConfiguredRegions();
//      System.out.println("Regions: " + regions.toString());  
    }
    
    public Collection<org.benchsuite.qoehelper.model.Network> listNetwork(String region) {	
    	
    	    Collection<org.benchsuite.qoehelper.model.Network> listNetworks = new ArrayList<org.benchsuite.qoehelper.model.Network>();
    	    org.benchsuite.qoehelper.model.Network network = new org.benchsuite.qoehelper.model.Network();
    	
    		// Network with NeutronApi.class 
    	    NetworkApi networksNeutron = neutronApi.getNetworkApi(region);
    	    for (Network n: networksNeutron.list().concat()){
    	    	System.out.println(""+ n);
    	    	
    	    	network.setId(n.getId());
    	    	network.setName(n.getName());
    	    	listNetworks.add(network);
    	    }
    	    
    	    // Subnet with NeutronApi.class
//    	    SubnetApi subnetsNeutron = neutronApi.getSubnetApi(region);
//    	    for (Subnet s: subnetsNeutron.list().concat()){
//    	    	System.out.println("\n"+ s);
//    	    }
    	       	    
    	    return listNetworks;
    }
    	

    public Collection<org.benchsuite.qoehelper.model.SecurityGroup> listSecurityGroup(String region) {
    	
	    // Security Group with NeutronApi.class    	    
//	    org.jclouds.openstack.neutron.v2.features.SecurityGroupApi securityGroupsNeutron = neutronApi.getSecurityGroupApi(region);
//	    for (org.jclouds.openstack.neutron.v2.domain.SecurityGroup securityGroup: securityGroupsNeutron.listSecurityGroups().concat()){
//	    	System.out.println("\n"+ securityGroup);
//	    }
    	
    	 Collection<org.benchsuite.qoehelper.model.SecurityGroup> listSecurityGroups = new ArrayList<>();
    	 org.benchsuite.qoehelper.model.SecurityGroup securityGroup = new org.benchsuite.qoehelper.model.SecurityGroup();
    	 
    	 SecurityGroupApi securityGroups = novaApi.getSecurityGroupApi(region).get();
    	    	
    	 for (SecurityGroup sGroup : securityGroups.list()) {
    		 System.out.println(""+sGroup);
    		 
    		 securityGroup.setId(sGroup.getId());
    		 securityGroup.setName(sGroup.getName());
    		 listSecurityGroups.add(securityGroup);
    	 }
    	 
    	 return listSecurityGroups;
    }
         
    public Collection<org.benchsuite.qoehelper.model.Image> listImageAPI(String region) {
      
    	Collection<org.benchsuite.qoehelper.model.Image> listImages = new ArrayList<org.benchsuite.qoehelper.model.Image>();
    	org.benchsuite.qoehelper.model.Image image = new org.benchsuite.qoehelper.model.Image();
    	
    	ImageApi imageApi = novaApi.getImageApi(region);

        for (org.jclouds.openstack.nova.v2_0.domain.Image i : imageApi.listInDetail().concat()) {
        	System.out.println("" + i);
        	
        	image.setId(i.getId());
        	image.setName(i.getName());
        	image.setDescription(i.getMetadata().get("description"));
        	listImages.add(image);
        }
        
        return listImages;
    }
    
    public Collection<HardwareProfile> listFlavorAPI(String region) {
    	
    	Collection<HardwareProfile> listHardwareProfiles = new ArrayList<HardwareProfile>();
    	HardwareProfile hardwareProfile = new HardwareProfile();
    	
    	FlavorApi flavorApi = novaApi.getFlavorApi(region);
		for (Flavor flavor : flavorApi.listInDetail().concat()) {
			System.out.println("  " + flavor);
			
			hardwareProfile.setId(flavor.getId());
			hardwareProfile.setName(flavor.getName());
			hardwareProfile.setRam(flavor.getRam());
			hardwareProfile.setVcpus(flavor.getVcpus());
			listHardwareProfiles.add(hardwareProfile);
        }
		
		return listHardwareProfiles;
    }       

    public void close() throws IOException {
        Closeables.close(novaApi, true);
    }
}