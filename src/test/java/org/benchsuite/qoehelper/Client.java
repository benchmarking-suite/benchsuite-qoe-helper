package org.benchsuite.qoehelper;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.io.InputStream;

import org.benchsuite.qoehelper.model.GetInfoRequest;
import org.benchsuite.qoehelper.providers.ProviderConfigurationException;

public class Client {
	
	public static void main(String[] args) throws IOException, ProviderConfigurationException {

		Properties props = new Properties();
		InputStream in = new FileInputStream("/home/ggiammat/test/qoe-helper/test.properties");
		props.load(in);
		in.close();

		javax.ws.rs.client.Client client = ClientBuilder.newClient();
		
		WebTarget target = client.target("http://localhost:8080/qoehelper/rest").path("CloudInfo");
		
		GetInfoRequest iData= new GetInfoRequest();
		
		// Fiware OpenStack
		iData.setProvider("openstack");
		iData.setIdentity(props.getProperty("fiware_user"));
		iData.setCredentials(props.getProperty("fiware_pass"));
		Map<String, String> optionalParameters = new HashMap<String,String>();
		optionalParameters.put("authUrl", props.getProperty("fiware_authUrl"));
		optionalParameters.put("region", props.getProperty("fiware_region"));
		optionalParameters.put("project", props.getProperty("fiware_project"));
		iData.setOptionalParameters(optionalParameters);
		
		// Amazon AWS-EC2
//		iData.setProvider("ec2");
//		iData.setIdentity(props.getProperty("amazon_user"));
//		iData.setCredentials(props.getProperty("amazon_pass"));
//		Map<String, String> optionalParameters = new HashMap<String,String>();
//		optionalParameters.put("region", props.getProperty("amazon_region"));
//		iData.setOptionalParameters(optionalParameters);
		
		// Cosmote OpenStack
//		iData.setProvider("openstack");
//		iData.setIdentity(props.getProperty("cosmote_user"));
//		iData.setCredentials(props.getProperty("cosmote_pass"));
//		Map<String, String> optionalParameters = new HashMap<String,String>();
//		optionalParameters.put("authUrl", props.getProperty("cosmote_authUrl"));
//		optionalParameters.put("region", props.getProperty("cosmote_region"));
//		optionalParameters.put("project", props.getProperty("cosmote_project"));
//		iData.setOptionalParameters(optionalParameters);

		Response response = target.request().post(Entity.entity(iData, MediaType.APPLICATION_JSON));
		
	    System.out.println("Response code: " + response.getStatus());
		System.out.println("CloudInfo"+response.readEntity(String.class));
	}
}
