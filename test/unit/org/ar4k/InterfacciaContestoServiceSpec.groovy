package org.ar4k

import grails.test.mixin.TestFor
import spock.lang.Specification


/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(InterfacciaContestoService)
class InterfacciaContestoServiceSpec extends Specification {

	def setup() {
	}

	def cleanup() {
	}

	void "Piattaforma di testing"() {
		expect:
		1 == 1
	}
}
