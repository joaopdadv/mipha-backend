package br.com.mipha.repository;

import br.com.mipha.entity.user.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface UserRepository extends MongoRepository<User, String> {
}
