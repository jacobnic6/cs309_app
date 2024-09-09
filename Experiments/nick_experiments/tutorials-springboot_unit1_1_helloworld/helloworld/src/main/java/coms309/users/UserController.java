package coms309.users;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
public class UserController {
    HashMap<String, User> userList = new HashMap<>();

    //CRUDL (create/read/update/delete/list)
    // use POST, GET, PUT, DELETE, GET methods for CRUDL

    // THIS IS THE LIST OPERATION
    // gets all the people in the list and returns it in JSON format
    // This controller takes no input.
    // Springboot automatically converts the list to JSON format
    // in this case because of @ResponseBody
    // Note: To LIST, we use the GET method
@GetMapping("/users")
    public HashMap<String, User> getUserList() {
        return userList;
    }

    // THIS IS THE CREATE OPERATION
    // springboot automatically converts JSON input into a person object and
    // the method below enters it into the list.
    // It returns a string message in THIS example.
    // in this case because of @ResponseBody
    // Note: To CREATE we use POST method
    @PostMapping("/users")
    public String createUser(@RequestBody User user) {
        System.out.println(user.toString());
        userList.put(user.getFirstName(), user);
        return "New user " + user.getFirstName() + " created.";
    }

    // THIS IS THE READ OPERATION
    // Springboot gets the PATHVARIABLE from the URL
    // We extract the person from the HashMap.
    // springboot automatically converts Person to JSON format when we return it
    // in this case because of @ResponseBody
    // Note: To READ we use GET method
    @GetMapping("/users/{firstName}")
    public User getUser(@PathVariable String firstName) {

        User user = userList.get(firstName);
        System.out.println(user.toString());
        return user;
    }

    // THIS IS THE UPDATE OPERATION
    // We extract the person from the HashMap and modify it.
    // Springboot automatically converts the Person to JSON format
    // Springboot gets the PATHVARIABLE from the URL
    // Here we are returning what we sent to the method
    // in this case because of @ResponseBody
    // Note: To UPDATE we use PUT method
    @PutMapping("/users/{firstName}")
    public User updateUser(@PathVariable String firstName, @RequestBody User user) {
        userList.replace(firstName, user);
        return userList.get(firstName);
    }

    // THIS IS THE DELETE OPERATION
    // Springboot gets the PATHVARIABLE from the URL
    // We return the entire list -- converted to JSON
    // in this case because of @ResponseBody
    // Note: To DELETE we use delete method
    @DeleteMapping("/users/{firstName}")
    public HashMap<String, User> deleteUser(@PathVariable String firstName) {
        userList.remove(firstName);
        return userList;
    }


}
