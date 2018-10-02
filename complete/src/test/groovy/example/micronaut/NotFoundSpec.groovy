package example.micronaut

import geb.spock.GebSpec
import io.micronaut.context.ApplicationContext
import io.micronaut.runtime.server.EmbeddedServer
import spock.lang.AutoCleanup
import spock.lang.IgnoreIf
import spock.lang.Shared

class NotFoundSpec extends GebSpec {
    @Shared
    @AutoCleanup
    EmbeddedServer embeddedServer = ApplicationContext.run(EmbeddedServer)

    @IgnoreIf({ !(sys['geb.env'] in ['chrome', 'firefox']) })
    def "verify tenant can be selected works"() {
        given:
        browser.baseUrl = "http://localhost:${embeddedServer.port}"

        when:
        go "/foo"

        then:
        at NotFoundPage
    }
}
