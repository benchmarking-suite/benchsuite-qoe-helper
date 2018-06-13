package org.benchsuite.qoehelper.rest;

import java.io.IOException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import org.benchsuite.qoehelper.model.CloudInfo;
import org.benchsuite.qoehelper.model.GetInfoRequest;
import org.benchsuite.qoehelper.QoEHelper;
import org.benchsuite.qoehelper.providers.ProviderConfigurationException;


@Path("/")
public class QoEHelperResource {

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
	public CloudInfo getData(GetInfoRequest incomingData) throws IOException, ProviderConfigurationException {
			
		QoEHelper qoe =  new QoEHelper();
		return qoe.getCloudInfo(incomingData.getProvider(), incomingData.getIdentity(), incomingData.getCredentials(), incomingData.getOptionalParameters());
	}
}
