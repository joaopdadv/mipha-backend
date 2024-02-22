package br.com.mipha.repository;

import br.com.mipha.entity.Team;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface TeamRepository extends MongoRepository<Team, String> {
}
