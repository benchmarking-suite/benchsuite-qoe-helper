/* Benchmarking Suite
   Copyright 2018 Engineering Ingegneria Informatica S.p.A.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

   Developed in the ARTIST EU project (www.artist-project.eu) and in the
   CloudPerfect EU project (https://cloudperfect.eu/)
*/
package org.benchsuite.qoehelper.rest;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import org.benchsuite.qoehelper.BenchmarkConfigurationRepository;
import org.benchsuite.qoehelper.model.BenchmarkConfiguration;
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
		if(incomingData.getOptionalParameters()==null)
			incomingData.setOptionalParameters(new HashMap<>());
			
		return qoe.getCloudInfo(incomingData.getProvider(), incomingData.getIdentity(), incomingData.getCredentials(), incomingData.getOptionalParameters());
	}

	@GET
	@Path("/benchmarks/{version}")
	@Consumes({MediaType.APPLICATION_JSON})
  @Produces({MediaType.APPLICATION_JSON})
	public Collection<BenchmarkConfiguration> getBenchmarkConfiguration(
			@PathParam("version") String benchsuiteVersion) throws IOException {

		return BenchmarkConfigurationRepository.getInstance(benchsuiteVersion).getAllBenchmarkConfigurations();
	}

}
