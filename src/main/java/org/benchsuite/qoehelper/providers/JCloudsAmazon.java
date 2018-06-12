package org.benchsuite.qoehelper.providers;

import com.google.common.base.Optional;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Module;

import org.jclouds.ContextBuilder;
import org.jclouds.aws.ec2.AWSEC2Api;
import org.jclouds.aws.ec2.compute.AWSEC2TemplateOptions;
import org.jclouds.aws.ec2.features.AWSAMIApi;
import org.jclouds.aws.ec2.features.AWSSecurityGroupApi;
import org.jclouds.compute.ComputeServiceContext;
import org.jclouds.compute.domain.ComputeMetadata;
import org.jclouds.compute.domain.Hardware;
import org.jclouds.compute.domain.Image;
import org.jclouds.compute.domain.OsFamily;
import org.jclouds.compute.domain.SecurityGroup;
import org.jclouds.compute.domain.Template;
import org.jclouds.compute.extensions.ImageExtension;
import org.jclouds.compute.extensions.SecurityGroupExtension;
import org.jclouds.domain.Location;
import org.jclouds.logging.slf4j.config.SLF4JLoggingModule;
import org.jclouds.sshj.config.SshjSshClientModule;
import org.jclouds.ec2.EC2Api;
import org.jclouds.ec2.compute.options.EC2TemplateOptions;
import org.jclouds.aws.ec2.options.AWSDescribeImagesOptions;

import org.jclouds.ec2.domain.Subnet;
import org.jclouds.ec2.features.SecurityGroupApi;
import org.jclouds.ec2.features.SubnetApi;

import static org.jclouds.aws.ec2.reference.AWSEC2Constants.PROPERTY_EC2_AMI_QUERY;
import static org.jclouds.aws.ec2.reference.AWSEC2Constants.PROPERTY_EC2_CC_AMI_QUERY;
import static org.jclouds.compute.config.ComputeServiceProperties.TIMEOUT_SCRIPT_COMPLETE;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.benchsuite.qoehelper.model.*;
import org.benchsuite.qoehelper.model.HardwareProfile;


public class JCloudsAmazon implements Closeable {
	private final ComputeServiceContext context;
	private final EC2Api ec2Context;
	private final AWSEC2Api awsec2Context;

    public JCloudsAmazon(String identity,String credential) {
        Iterable<Module> modules = ImmutableSet.<Module>of(new SshjSshClientModule(),
                new SLF4JLoggingModule()
                );
        
        Properties properties = new Properties();
        properties.setProperty(PROPERTY_EC2_AMI_QUERY, "owner-id=137112412989;state=available;image-type=machine");
        properties.setProperty(PROPERTY_EC2_CC_AMI_QUERY, "");
        long scriptTimeout = TimeUnit.MILLISECONDS.convert(20, TimeUnit.MINUTES);
        properties.setProperty(TIMEOUT_SCRIPT_COMPLETE, scriptTimeout + "");
        
        // get a context with ec2 that offers the portable ComputeService API
        context = ContextBuilder.newBuilder("aws-ec2")
                .credentials(identity, credential)
                .overrides(properties)
                //.modules(modules)
                .buildView(ComputeServiceContext.class);
        
        ec2Context = context.unwrapApi(EC2Api.class);
        awsec2Context = context.unwrapApi(AWSEC2Api.class);
    }
    
    public Collection<org.benchsuite.qoehelper.model.SecurityGroup> listSecurityGroup(String region){
    	
    	Collection<org.benchsuite.qoehelper.model.SecurityGroup> listSecurityGroups = new ArrayList<org.benchsuite.qoehelper.model.SecurityGroup>();

    	// Security Groups for Regions
    	AWSSecurityGroupApi secGroups = awsec2Context.getSecurityGroupApi().get();
    	for(org.jclouds.ec2.domain.SecurityGroup sGroups: secGroups.describeSecurityGroupsInRegion(region)){
    		
    		System.out.println("SecurityGroup for region:"+sGroups);
    		
    		org.benchsuite.qoehelper.model.SecurityGroup securityGroup = new org.benchsuite.qoehelper.model.SecurityGroup();
    		securityGroup.setId(sGroups.getId());
    		securityGroup.setName(sGroups.getName());
    		listSecurityGroups.add(securityGroup);
    	}
    	
    	// Security Groups All
//    	SecurityGroupExtension securityGroups = context.getComputeService().getSecurityGroupExtension().get();
//    	for(SecurityGroup group: securityGroups.listSecurityGroups()){
//    		System.out.println("SecurityGroup: "+group);
//    	}
    	
    	return listSecurityGroups;
    }
    
    public Collection<Network> listNetwork(String region){
    	
    	Collection<Network> listNetworks = new ArrayList<Network>();
 	
    	//Subnet all with AWSEC2Api.class
//    	SubnetApi subnetsAWSEC2Context = ec2Context.getSubnetApi().get();  
//    	for(Subnet subnet: subnetsAWSEC2Context.list()){
//    		System.out.println(""+subnet);
//    	}
    	
    	// Subnet for region with AWSEC2Api.class
    	SubnetApi subnetsRegion = awsec2Context.getSubnetApiForRegion(region).get();
		for(Subnet subnetRegion: subnetsRegion.list()){
			System.out.println(""+subnetRegion);
			
			Network network = new Network();
	    	network.setId(subnetRegion.getSubnetId());
	    	network.setName(null);
    		listNetworks.add(network);
	    }
	    
    	return listNetworks;
    }
 
    public Collection<org.benchsuite.qoehelper.model.Image> listImages(String region){
    	
    	Collection<org.benchsuite.qoehelper.model.Image> listImages = new ArrayList<org.benchsuite.qoehelper.model.Image>();

    	AWSAMIApi images = awsec2Context.getAMIApi().get();
    	for (org.jclouds.ec2.domain.Image i : images.describeImagesInRegion(region, AWSDescribeImagesOptions.Builder.ownedBy("137112412989"))){
    		System.out.println("Images:  " + i);
    		
    		org.benchsuite.qoehelper.model.Image image = new org.benchsuite.qoehelper.model.Image();
        	image.setId(i.getId());
        	image.setName(i.getName());
        	image.setDescription(i.getDescription());
            listImages.add(image);        	
        }
                
		return listImages;
    }

    public Collection<HardwareProfile> listHardwareProfiles(String region){
    	
    	Collection<HardwareProfile> listHardwareProfiles = new ArrayList<HardwareProfile>();
    	    	
    	Set<? extends Hardware> hardwareProfiles = context.getComputeService().listHardwareProfiles();
    	for(Hardware h : hardwareProfiles){
    		System.out.println("hardwareProfiles: "+ h.toString());
    		
    		HardwareProfile hardwareProfile = new HardwareProfile();
    		hardwareProfile.setId(h.getId());
    		hardwareProfile.setName(h.getName());
    		hardwareProfile.setRam(h.getRam());
    		hardwareProfile.setVcpus(h.getProcessors().get(0).getCores());
    		listHardwareProfiles.add(hardwareProfile);
    	}
    	
    	return listHardwareProfiles;
    }
        
	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub
		context.close();
	}

}