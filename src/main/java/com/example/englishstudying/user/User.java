package com.example.englishstudying.user;

import com.example.englishstudying.game.Game;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Document
public class User {
    @Id
    private String id;
    private String username;
    private String email;
    private String password;
    @DocumentReference(lazy = true)
    private List<Game> gamesHistory;
}
