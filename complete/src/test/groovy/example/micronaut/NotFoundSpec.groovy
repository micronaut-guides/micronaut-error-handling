package example.micronaut

import geb.spock.GebSpec
import io.micronaut.runtime.server.EmbeddedServer
import io.micronaut.test.annotation.MicronautTest
import spock.lang.IgnoreIf

import javax.inject.Inject

@MicronautTest
class NotFoundSpec extends GebSpec {

    @Inject
    EmbeddedServer embeddedServer

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
