package example.micronaut

import geb.spock.GebSpec
import io.micronaut.runtime.server.EmbeddedServer
import io.micronaut.test.annotation.MicronautTest
import spock.lang.IgnoreIf

import javax.inject.Inject

@MicronautTest
class BookCreateSpec extends GebSpec {

    @Inject
    EmbeddedServer embeddedServer

    def "verify tenant can be selected works"() {
        given:
        browser.baseUrl = "http://localhost:${embeddedServer.port}"

        when:
        go "/books/create"

        then:
        at BookCreatePage

        when:
        BookCreatePage page = browser.page(BookCreatePage)

        then:
        !page.hasErrors()

        when:
        page.save("", 0)

        then:
        at BookCreatePage

        when:
        page = browser.page(BookCreatePage)

        then:
        page.title() == ""
        page.pages() == "0"
        page.hasErrors()
        page.errors().contains('title must not be blank')
        page.errors().contains('pages must be greater than 0')
    }
}
