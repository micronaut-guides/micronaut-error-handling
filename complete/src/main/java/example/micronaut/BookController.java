package example.micronaut;

import java.util.List;
import io.micronaut.core.io.Writable;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Consumes;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Produces;
import io.micronaut.validation.Validated;
import io.micronaut.views.View;
import io.micronaut.views.ViewsRenderer;
import io.micronaut.http.annotation.Error;

import javax.annotation.Nullable;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Validated
@Controller("/books")
public class BookController {

    private final ViewsRenderer viewsRenderer;

    public BookController(ViewsRenderer viewsRenderer) {
        this.viewsRenderer = viewsRenderer;
    }

    @View("bookscreate")
    @Get("/create")
    public Map<String, Object> create() {
        return createModel(null, null);
    }

    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Post("/save")
    public HttpResponse save(@Valid @Body CommandBookSave cmd) {
        return HttpResponse.ok();
    }

    @Produces(MediaType.TEXT_HTML)
    @Error(exception = ConstraintViolationException.class, global = false)
    public Writable onSavedFailed(HttpRequest request, ConstraintViolationException ex) {
        final Map<String, Object> model = createModel(null, null);
        model.put("errors", violationsMessages(ex.getConstraintViolations()));
        Optional<CommandBookSave> cmd = request.getBody(CommandBookSave.class);
        cmd.ifPresent(bookSave -> {
            model.put("title", bookSave.getTitle());
            model.put("pages", bookSave.getPages());
        });
        return viewsRenderer.render("bookscreate", model);
    }

    private Map<String, Object> createModel(@Nullable String title, @Nullable String pages) {
        final Map<String, Object> model = new HashMap<>();
        model.put("title", title != null ? title : "");
        model.put("pages", pages != null ? pages : "");
        return model;
    }

    private List<String> violationsMessages(Set<ConstraintViolation<?>> violations) {
        return violations.stream()
                .map(violation -> {
                    StringBuilder sb = new StringBuilder();
                    Path.Node lastNode = lastNode(violation.getPropertyPath());
                    if (lastNode != null) {
                        sb.append(lastNode.getName());
                        sb.append(" ");
                    }
                    sb.append(violation.getMessage());
                    return sb.toString();
                })
                .collect(Collectors.toList());
    }

    private Path.Node lastNode(Path path) {
        Path.Node lastNode = null;
        for (final Path.Node node : path) {
            lastNode = node;
        }
        return lastNode;
    }

}
