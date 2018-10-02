package example.micronaut

import geb.spock.GebSpec
import io.micronaut.context.ApplicationContext
import io.micronaut.runtime.server.EmbeddedServer
import spock.lang.AutoCleanup
import spock.lang.IgnoreIf
import spock.lang.Shared

class BookCreateSpec extends GebSpec {
    @Shared
    @AutoCleanup
    EmbeddedServer embeddedServer = ApplicationContext.run(EmbeddedServer)

    @IgnoreIf({ !(sys['geb.env'] in ['chrome', 'firefox']) })
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
