package org.benchsuite.qoehelper.rest;

import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.benchsuite.qoehelper.model.CloudInfo;
import org.benchsuite.qoehelper.model.IncomingData;
import org.benchsuite.qoehelper.QoEHelper;

public class QoEHelperResource2 {
	
	@POST
	@Path("/CloudInfo")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.TEXT_PLAIN})
	public CloudInfo getData(IncomingData incomingData) throws IOException{
		
		System.out.println("Provider: "+incomingData.getProvider());
		System.out.println("Identity: "+incomingData.getIdentity());
		System.out.println("Credentials: "+incomingData.getCredentials());
		System.out.println("OptionalParameters host: "+incomingData.getOptionalParameters().get("host"));
		System.out.println("OptionalParameters region: "+incomingData.getOptionalParameters().get("region"));
		
		QoEHelper qoe =  new QoEHelper();
		return qoe.getCloudInfo(incomingData.getProvider(), incomingData.getIdentity(), incomingData.getCredentials(), incomingData.getOptionalParameters());
	}

}
