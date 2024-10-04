package com.coms309.nutrifit.users;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    //CREATE
    @PostMapping(path = "/users")
    String createUser(@RequestBody User user) {
        if (user == null){
            return failure;
        }
        userRepository.save(user);
        return success;
    }



    //READ
    @GetMapping(path = "/users/{id}")
    public User getUserById(@PathVariable int id) {


                return userRepository.findById(id);
    }
    //UPDATE
    @PutMapping(path = "/users/{id}")
    User updateUser(@PathVariable int id, @RequestBody User user) {
        User u = userRepository.findById(id);
        if (u == null){
            return null;
        }
        userRepository.save(user);
        return userRepository.findById(id);
    }

    //DELETE
    @DeleteMapping(path = "/users/{id}")
    String deleteUser(@PathVariable int id) {
        userRepository.deleteById(id);
        return success;
    }


    //LIST
    @GetMapping(path = "/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
