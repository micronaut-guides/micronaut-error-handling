package example.micronaut

import io.micronaut.http.HttpRequest
import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.annotation.MicronautTest
import spock.lang.Specification

import javax.inject.Inject

@MicronautTest
class ExceptionHandlerSpec extends Specification {

    @Inject
    @Client("/")
    RxHttpClient client

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
