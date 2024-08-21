package com.example.demo.controller;

import com.example.demo.model.Info;
import com.example.demo.model.User;
import com.example.demo.repository.InfoRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path="/api", produces = "application/json")
@CrossOrigin(origins = "*")
public class HelloWorld {

    private final List<User> users = new ArrayList<>();
    private UserRepository userRepository;

    @Autowired
    private InfoRepository infoRepo;

    /*
    public HelloWorld() {

        // Création du premier utilisateur et de ses informations
        Info info = new Info();
        User user = new User();
        user.setEmail("hello@gmail.com");
        user.setPassword("123456");
        info.setAddress("Paris");
        info.setPhone("0606060");
        info.setAge(28);
        info.setName("boloss");
        info.setLastName("Antoine");
        user.setInfo(info);

        // Création du deuxième utilisateur et de ses informations
        Info info1 = new Info();
        User user1 = new User();
        user1.setEmail("koala@gmail.com");
        user1.setPassword("98766");
        info1.setAddress("Lyon");
        info1.setAge(20);
        info1.setPhone("0707007");
        info1.setName("doe");
        info1.setLastName("John");
        user1.setInfo(info1);

        // Ajout des utilisateurs à la liste
        users.add(user);
        userRepository.save(user1);
        users.add(user1);
    }
    */

    @GetMapping("/info")
    public Info getInfo() {
        Info info = new Info();
        info.setAddress("chez moi");
        info.setAge(28);
        return info;
    }

    /*
    @GetMapping("/userid/{id}")
    public User getUser(@PathVariable("id") int id) {
        // Recherche de l'utilisateur par son ID avec une boucle for
        for (User user : users) {
            if (user.getId() == id) {
                return user;
            }
        }
        // Si aucun utilisateur avec cet ID n'est trouvé, retourne null
        return null;
    }
    */



}
