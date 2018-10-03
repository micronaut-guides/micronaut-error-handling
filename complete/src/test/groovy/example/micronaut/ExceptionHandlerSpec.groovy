package example.micronaut

import io.micronaut.context.ApplicationContext
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.RxHttpClient
import io.micronaut.runtime.server.EmbeddedServer
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

class ExceptionHandlerSpec extends Specification {
    @Shared
    @AutoCleanup
    EmbeddedServer embeddedServer = ApplicationContext.run(EmbeddedServer)

    @Shared
    @AutoCleanup
    RxHttpClient client = embeddedServer.applicationContext.createBean(RxHttpClient, embeddedServer.getURL())

    void "test OutOfStockException is handled by ExceptionHandler"() {
        when:
        HttpRequest request = HttpRequest.GET('/books/stock/1234')
        Integer stock = client.toBlocking().retrieve(request, Integer)

        then:
        noExceptionThrown()
        stock != null
        stock == 0
    }
}
