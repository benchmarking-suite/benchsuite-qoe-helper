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

public class Client {
	
	public static void main(String[] args) throws IOException {

		Properties props = new Properties();
		InputStream in = new FileInputStream("/home/ggiammat/test/qoe-helper/test.properties");
		props.load(in);
		in.close();

		javax.ws.rs.client.Client client = ClientBuilder.newClient();
		
		WebTarget target = client.target("http://localhost:8080/benchsuite-qoe_helper-0.0.1-SNAPSHOT/rest").path("CloudInfo");
		
		GetInfoRequest iData= new GetInfoRequest();
		iData.setProvider("openstack-nova");
		System.out.println(props.getProperty("fiware_user"));
		iData.setIdentity(props.getProperty("fiware_user"));
		iData.setCredentials(props.getProperty("fiware_pass"));
		Map<String, String> optionalParameters = new HashMap<String,String>();
		optionalParameters.put("host", props.getProperty("fiware_host"));
		optionalParameters.put("region", props.getProperty("fiware_region"));
		iData.setOptionalParameters(optionalParameters);
		
		Response response = target.request().post(Entity.entity(iData, MediaType.APPLICATION_JSON)); 
	    System.out.println("Response code: " + response.getStatus());		
	}
}
