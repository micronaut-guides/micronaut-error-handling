package example.micronaut;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Error;
import io.micronaut.http.hateos.JsonError;
import io.micronaut.http.hateos.Link;
import io.micronaut.views.ViewsRenderer;

import java.util.Collections;

@Controller("/notfound")
public class NotFoundController {

    private final ViewsRenderer viewsRenderer;

    public NotFoundController(ViewsRenderer viewsRenderer) {
        this.viewsRenderer = viewsRenderer;
    }

    @Error(status = HttpStatus.NOT_FOUND, global = true)
    public HttpResponse notFound(HttpRequest request) {
        if (request.getHeaders()
                .accept()
                .stream()
                .anyMatch(mediaType -> mediaType.getName().contains(MediaType.TEXT_HTML))) {
            return HttpResponse.ok(viewsRenderer.render("notFound", Collections.emptyMap()))
                    .contentType(MediaType.TEXT_HTML);
        }

        JsonError error = new JsonError("Page Not Found")
                .link(Link.SELF, Link.of(request.getUri()));

        return HttpResponse.<JsonError>notFound()
                .body(error);
    }
}
