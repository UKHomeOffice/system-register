package uk.gov.digital.ho.systemregister.io.api;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLConnection;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import org.apache.commons.io.IOUtils;
import org.jboss.logging.Logger;

@Path("/")
public class DefaultResource {
    private static final Logger LOG = Logger.getLogger(DefaultResource.class);
    private static final String FALLBACK_RESOURCE = " META-INF/resources/index.html";

    @GET
    @Path("/{fileName:.+}")
    public Response getFrontendStaticFile(@PathParam("fileName") String fileName) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("META-INF/resources/index.html");
        LOG.info("Here we go....");
        // LOG.info(inputStream.readAllBytes());
        final StreamingOutput streamingOutput = outputStream -> IOUtils.copy(inputStream, outputStream);
        // LOG.info(inputStream.readAllBytes());

    return Response
      .ok(streamingOutput)
      .cacheControl(CacheControl.valueOf("max-age=900"))
      .type(URLConnection.guessContentTypeFromStream(inputStream))
      .build();
    }
}
