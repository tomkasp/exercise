package com.tomkasp

import com.google.gson.Gson
import com.tomkasp.registration.Constants
import com.tomkasp.registration.RegistrationController
import com.tomkasp.registration.User
import com.tomkasp.registration.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebAppConfiguration
@ContextConfiguration(classes = RegistrationExerciseApplication.class)
class RegistrationControllerTest extends Specification {

    MockMvc mockMvc
    Gson gson

    @Autowired
    WebApplicationContext webApplicationContext

    @Autowired
    UserRepository userRepository

    def setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build()
        gson = new Gson()
    }

    def "verify that username accepts alpha numeric values only"() {
        User user = new User().username("asd%@@").password("Password1")

        when: "user message contains non alphanumerics"
        def response = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(user)))
        def controllerResponse = response.andReturn().response.contentAsString;

        then:
        response.andExpect(status().isBadRequest())
        controllerResponse == Constants.USERNAME_ALPHANUMERIC_ERROR;

        when: "user message doesn't contain non alphanumerics"
        user.username("jodamaster")
        response = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(user)))

        then:
        response.andExpect(status().isOk())
    }

    def "verify that username length is no less than 5 characters"() {
        given:
        User user = new User().username("tom").password("Password1")

        when: "username is too short"
        def response = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(user)))
        def controllerResponse = response.andReturn().response.contentAsString;

        then:
        response.andExpect(status().isBadRequest())
        controllerResponse == Constants.USERNAME_LENGTH_ERROR
    }

    def "verify that username is not already registered"() {
        given:
        User user = new User().username("frodo").password("Password1")

        def response = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(user)))

        when: "we try to save user again"
        response = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(user)))
        def controllerResponse = response.andReturn().response.contentAsString

        then: "we receive an error"
        response.andExpect(status().isBadRequest())
        controllerResponse == RegistrationController.USER_ALREADY_EXISTS
    }

    def "Verify thatThe password has a minimum length of 8 characters and contains at least 1 number, uppercase, and 1 lowercase character"() {
        given:
        User user = new User().username("bilbo")

        when:
        user.password("password1")
        def response = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(user)))
        def controllerResponse = response.andReturn().response.contentAsString
        then:
        response.andExpect(status().isBadRequest())
        controllerResponse == Constants.PASSWORD_CHARACTERS_ERROR

        when:
        user.password("Password")
        response = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(user)))
        controllerResponse = response.andReturn().response.contentAsString
        then:
        response.andExpect(status().isBadRequest())
        controllerResponse == Constants.PASSWORD_CHARACTERS_ERROR


        when:
        user.password("Pass1")
        response = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(user)))
        controllerResponse = response.andReturn().response.contentAsString
        then:
        response.andExpect(status().isBadRequest())
        controllerResponse == Constants.PASSWORD_LENGTH_ERROR

        when:
        user.password("Pa")
        response = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(user)))
        controllerResponse = response.andReturn().response.contentAsString
        then:
        response.andExpect(status().isBadRequest())
        controllerResponse.contains(Constants.PASSWORD_LENGTH_ERROR)
        controllerResponse.contains(Constants.PASSWORD_CHARACTERS_ERROR)

        when:
        user.password("Password1")
        response = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(user)))
        then:
        response.andExpect(status().isOk())
    }

    def "Upon submission of a valid username and password, they are persisted to a database"() {
        User user = new User().username("tomasz").password("DifPasss23");
        when:
        def response = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(user)))

        then:
        userRepository.findByUsername("tomasz") != null
    }
}