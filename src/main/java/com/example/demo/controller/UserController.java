package com.example.demo.controller;

import com.example.demo.model.Info;
import com.example.demo.model.User;
import com.example.demo.model.UserInfoDTO;
import com.example.demo.repository.InfoRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InfoRepository infoRepository;

    @GetMapping("/allusers")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<User> getUserById(@PathVariable Long id) {
        return userRepository.findById(id);
    }

    @PostMapping(value="/creer",  consumes = "application/json;charset=UTF-8")
    public String createUser(@RequestBody User user) {
        userRepository.save(user);
        return "User created";
    }

    @GetMapping("/info/{id}")
    public Optional<Info> getInfos(@PathVariable Long id) {
        return infoRepository.findById(id);
    }

    @DeleteMapping(value="/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
       userRepository.deleteById(id);
       return "User deleted";
    }

    @PutMapping(value = "/update/{id}")
    public String updateUser(@PathVariable Long id, @RequestBody UserInfoDTO userInfoDTO ) {
        // Accéder à l'objet User à partir de UserInfoDTO
        User user = new UserInfoDTO().getUser();

        // Accéder à l'objet Info à partir de UserInfoDTO
        Info info = new User().getInfo();

        // Mettre à jour les champs de l'utilisateur et des informations
        user.setId(id);
        user.setEmail("toto11@gmail.com");
        user.setPassword("toto123");

        info.setName("poo");
        info.setPhone("06006");
        info.setAge(35);
        info.setLastName("dupont");
        info.setAddress("monaco");

        // Sauvegarder les mises à jour dans les repositories respectifs
        userRepository.save(user);
        infoRepository.save(info);

        return "User updated";
    }

    @PatchMapping(value ="/updateOne/{id}" )
    public String updateOneUser(@PathVariable Long id, @RequestBody User user)
    {
        user.setId(id);
        user.setEmail("FLORIAN@gmail.com");
        userRepository.save(user);
        return " User updated one attribute";
    }

}
