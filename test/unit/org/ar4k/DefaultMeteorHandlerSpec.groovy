package org.ar4k

import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestMixin(GrailsUnitTestMixin)
class DefaultMeteorHandlerSpec extends Specification {

	def setup() {
	}

	def cleanup() {
	}

	void "Piattaforma di testing"() {
		expect:
		1 == 1
	}
}
