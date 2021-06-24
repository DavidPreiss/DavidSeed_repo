package org.perscholas.dao;

import org.perscholas.models.SeededPlayer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISeededPlayerRepo extends JpaRepository<SeededPlayer,String> {
}
