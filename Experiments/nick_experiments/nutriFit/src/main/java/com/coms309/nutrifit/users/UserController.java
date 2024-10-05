package com.coms309.nutrifit.users;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {



    private final UserServiceHandler userServiceHandler;

    public UserController(UserServiceHandler userServiceHandler) {
        this.userServiceHandler = userServiceHandler;
    }


    //CREATE
    @PostMapping(path = "/users")
    String createUser(@RequestBody User user) {

       return userServiceHandler.createUser(user);



    }



    //READ
    @GetMapping(path = "/users/{id}")
    public User getUserById(@PathVariable int id) {

        //return userRepository.findById(id);
                return userServiceHandler.readUser(id);
    }
    //UPDATE
    @PutMapping(path = "/users/{id}")
    User updateUser(@PathVariable int id, @RequestBody User user) {

        return userServiceHandler.updateUser(id, user);
    }

    //DELETE
    @DeleteMapping(path = "/users/{id}")
    String deleteUser(@PathVariable int id) {
       // userRepository.deleteById(id);


        return userServiceHandler.deleteUser(id);
    }


    //LIST
    @GetMapping(path = "/users")
    public List<User> getAllUsers() {
       // return userRepository.findAll();
        return  userServiceHandler.listAllUsers();
    }
}
