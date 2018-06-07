package org.benchsuite.qoehelper;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.benchsuite.qoehelper.model.IncomingData;

public class Client {
	
	public static void main(String[] args){
		
		javax.ws.rs.client.Client client = ClientBuilder.newClient();
		
		WebTarget target = client.target("http://localhost:8080/benchsuite-qoe-helper/rest").path("/CloudInfo");
		
		IncomingData iData= new IncomingData();
		iData.setProvider("");
		iData.setIdentity("");
		iData.setCredentials("");
		Map<String, String> optionalParameters = new HashMap<String,String>();
		optionalParameters.put("host", "");
		optionalParameters.put("region", "");
		iData.setOptionalParameters(optionalParameters);
		
		Response response = target.request().post(Entity.entity(iData, MediaType.APPLICATION_JSON)); 
	    System.out.println("Response code: " + response.getStatus());		
	}
}
