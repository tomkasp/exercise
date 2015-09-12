package com.tomkasp

import com.tomkasp.registration.RegistrationController
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.web.WebAppConfiguration
import spock.lang.Specification

@WebAppConfiguration
@ContextConfiguration(classes = RegistrationExerciseApplication.class)
class RegistrationControllerTest extends Specification {

    @Autowired
    RegistrationController registrationController;

    def "test"() {
        expect:
        true
    }

}
