package org.benchsuite.qoehelper.rest;

import java.io.IOException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import org.benchsuite.qoehelper.model.CloudInfo;
import org.benchsuite.qoehelper.model.GetInfoRequest;
import org.benchsuite.qoehelper.QoEHelper;


@Path("/")
public class QoEHelperResource2 {

	public static final String REST_VERSION = "1.0.0";

	@GET
	@Path("/version")
	public String getVersion(){
		return REST_VERSION;
	}

	@POST
	@Path("/CloudInfo")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
	public CloudInfo getData(GetInfoRequest incomingData) throws IOException{
		
		System.out.println("Provider: "+incomingData.getProvider());
		System.out.println("Identity: "+incomingData.getIdentity());
		System.out.println("Credentials: "+incomingData.getCredentials());
		System.out.println("OptionalParameters host: "+incomingData.getOptionalParameters().get("host"));
		System.out.println("OptionalParameters region: "+incomingData.getOptionalParameters().get("region"));
		
		QoEHelper qoe =  new QoEHelper();
		return qoe.getCloudInfo(incomingData.getProvider(), incomingData.getIdentity(), incomingData.getCredentials(), incomingData.getOptionalParameters());
	}
}
