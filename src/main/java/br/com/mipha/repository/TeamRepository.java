package br.com.mipha.repository;

import br.com.mipha.entity.team.Team;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TeamRepository extends MongoRepository<Team, String> {

    @Query("{ 'owner' : ?0 }")
    List<Team> findByOwner(String ownerId);
}
