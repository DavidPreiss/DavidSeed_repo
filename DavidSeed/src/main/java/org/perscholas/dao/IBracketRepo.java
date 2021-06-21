package org.perscholas.dao;

import org.perscholas.models.Bracket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IBracketRepo extends JpaRepository<Bracket,String>
{
//not needed
    //Optional<Bracket> findBracketById(String id_code);
}
