package org.perscholas.dao;

import org.perscholas.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;
/*
        - add annotation
        - extend spring jpa
        - add custom methods if needed

 */
@Repository
public interface IUserRepo extends JpaRepository<User, String> {
    Optional<User> findUserByEmailAndPassword(String email, String password);
    Optional<User> findByEmail(String email);

}
