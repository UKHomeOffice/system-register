package uk.gov.digital.ho.systemregister.io.api;

import java.io.InputStream;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import static java.util.Objects.requireNonNull;
import static javax.ws.rs.core.MediaType.TEXT_HTML;
import static javax.ws.rs.core.MediaType.TEXT_HTML_TYPE;
import static org.apache.commons.io.IOUtils.copy;

@Path("/")
public class FrontendResource {
    private static final String INDEX_HTML_PATH = "META-INF/resources/index.html";

    @GET
    @Path("/{filename:.*}")
    @Produces(TEXT_HTML)
    public Response singlePageApplication() {
        InputStream inputStream = getWebPageAsStream();

        StreamingOutput streamingOutput = outputStream -> copy(inputStream, outputStream);

        return Response.ok(streamingOutput)
                .type(TEXT_HTML_TYPE)
                .build();
    }

    private InputStream getWebPageAsStream() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        return requireNonNull(classLoader.getResourceAsStream(INDEX_HTML_PATH));
    }
}
