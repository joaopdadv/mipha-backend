package br.com.mipha.repository;

import br.com.mipha.entity.user.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

import java.util.UUID;

public interface UserRepository extends MongoRepository<User, String> {

    @Query("{ 'email' : ?0 }")
    Optional<User> findByEmail(String email);

}
