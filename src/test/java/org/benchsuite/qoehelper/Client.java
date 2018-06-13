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
		
		WebTarget target = client.target("http://localhost:8080/benchsuite-qoe_helper-0.0.1-SNAPSHOT/rest").path("CloudInfo");
		
		GetInfoRequest iData= new GetInfoRequest();
		
		// Fiware OpenStack
		iData.setProvider("openstack");
		iData.setIdentity(props.getProperty("fiware_user"));
		iData.setCredentials(props.getProperty("fiware_pass"));
		Map<String, String> optionalParameters = new HashMap<String,String>();
		optionalParameters.put("authUrl", props.getProperty("fiware_host"));
		optionalParameters.put("region", props.getProperty("fiware_region"));
		optionalParameters.put("project", props.getProperty("fiware_project"));
		iData.setOptionalParameters(optionalParameters);


		Response response = target.request().post(Entity.entity(iData, MediaType.APPLICATION_JSON));
		
	  System.out.println("Response code: " + response.getStatus());
		System.out.println("CloudInfo"+response.readEntity(String.class));
	}
}
