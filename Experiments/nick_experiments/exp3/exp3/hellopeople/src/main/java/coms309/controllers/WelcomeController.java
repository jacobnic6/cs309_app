package coms309.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Simple Hello World Controller to display the string returned
 *
 * @author Nicholas Jacobs
 */

@RestController
class WelcomeController
    {

        @GetMapping("/")
        public String welcome()
            {
                return "Hello and welcome to COMS 309";
            }
    }
