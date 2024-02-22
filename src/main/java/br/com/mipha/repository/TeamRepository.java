package br.com.mipha.repository;

import br.com.mipha.entity.team.Team;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TeamRepository extends MongoRepository<Team, String> {
}
