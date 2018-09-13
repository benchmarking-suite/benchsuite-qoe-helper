package org.benchsuite.qoehelper.rest;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.benchsuite.qoehelper.model.BenchmarkConfigurationParsingException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;


@Provider
public class BenchmarkConfigurationParsingExceptionMapper  implements ExceptionMapper<BenchmarkConfigurationParsingException> {
  @Override
  public Response toResponse(BenchmarkConfigurationParsingException exception) {
    return Response.status(500)
        .entity(ExceptionUtils.getStackTrace(exception))
        .type(MediaType.TEXT_PLAIN).
            build();
  }
}
