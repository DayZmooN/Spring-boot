package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String email;

    private String password;


    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name = "info_id", referencedColumnName = "id")
    private Info info;

    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }

}
