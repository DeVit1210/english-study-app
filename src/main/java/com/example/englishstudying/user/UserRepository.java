package com.example.englishstudying.user;

import org.springframework.data.mongodb.repository.MongoRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends MongoRepository<User, String> {
    Mono<User> findByUsername(String username);
}
