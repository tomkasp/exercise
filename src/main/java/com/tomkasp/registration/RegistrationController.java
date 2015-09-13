package com.tomkasp.registration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

@RestController
@RequestMapping(value = "/users")
public class RegistrationController {

    private static final Logger logger = LoggerFactory.getLogger(RegistrationController.class);
    private static final String USER_ALREADY_EXISTS = "User already exists. ";

    private final UserRepository userRepository;

    @Autowired
    public RegistrationController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @RequestMapping(method = RequestMethod.POST)
    public String registerUser(@Validated @RequestBody User user, BindingResult bindingResult) {
        logger.info("User value {}", user);
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new UserAlreadyExistsException();
        }
        userRepository.save(user);
        return "user created";
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleValidationException(ConstraintViolationException e) {
        StringBuilder violationMessage = new StringBuilder();
        for (ConstraintViolation violation : e.getConstraintViolations()){
            violationMessage.append(violation.getMessage());
        }
        return new ResponseEntity<>(violationMessage.toString(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler()
    public ResponseEntity<String> handeUserAlreadyExistsException(UserAlreadyExistsException e) {
        return new ResponseEntity<>(USER_ALREADY_EXISTS, HttpStatus.BAD_REQUEST);
    }

}
