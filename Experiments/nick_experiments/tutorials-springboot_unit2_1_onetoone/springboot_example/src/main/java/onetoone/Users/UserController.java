package onetoone.Users;

import onetoone.Laptops.Laptop;
import onetoone.Laptops.LaptopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Vivek Bengre
 */

@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    LaptopRepository laptopRepository;

    private final String success = "{\"message\":\"success\"}";
    private final String failure = "{\"message\":\"failure\"}";

    @GetMapping(path = "/users")
    List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping(path = "/users/{id}")
    User getUserById(@PathVariable int id) {
        return userRepository.findById(id);
    }


    @PostMapping(path = "/users")
    String createUser(@RequestBody User user) {
        if (user == null)
            return failure;
        userRepository.save(user);
        return success;
    }

    /* not safe to update */
    //    @PutMapping("/users/{id}")
    //    User updateUser(@PathVariable int id, @RequestBody User request){
    //        User user = userRepository.findById(id);
    //        if(user == null)
    //            return null;
    //        userRepository.save(request);
    //        return userRepository.findById(id);
    //    }

    @PutMapping("/users/{id}")
    User updateUser(@PathVariable int id, @RequestBody User request) {
        User user = userRepository.findById(id);

        if (user == null) {
            throw new RuntimeException("user id does not exist");
        } else if (user.getId() != id) {
            throw new RuntimeException("path variable id does not match User request id");
        }

        userRepository.save(request);
        return userRepository.findById(id);
    }

    @PutMapping("/users/{userId}/laptops/{laptopId}")
    String assignLaptopToUser(@PathVariable int userId, @PathVariable int laptopId) {
        User user = userRepository.findById(userId);
        Laptop laptop = laptopRepository.findById(laptopId);
        if (user == null || laptop == null)
            return failure;
        laptop.setUser(user);
        user.setLaptop(laptop);
        userRepository.save(user);
        return success;
    }

    @DeleteMapping(path = "/users/{id}")
    String deleteUser(@PathVariable int id) {
        userRepository.deleteById(id);
        return success;
    }
}
