package org.perscholas.services;

import org.perscholas.dao.ICourseRepo;
import org.perscholas.dao.IUserRepo;
import org.perscholas.models.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final IUserRepo userRepo;
    private final ICourseRepo courseRepo;

    public UserService(IUserRepo userRepo, ICourseRepo courseRepo) {
        this.userRepo = userRepo;
        this.courseRepo = courseRepo;
    }

/*
            - add class annotations
            - add @Transactional on class or on each method
            - add crud methods
     */

    public List<User> getAllUser() {
        return userRepo.findAll();
    }

    public User saveUser(User s) {
        return userRepo.save(s);
    }

    public void deleteUser(User s) {
        userRepo.delete(s);
    }

    public User getUserByEmail(String email) {
        return userRepo.getById(email);
    }


    public boolean validateUser(User user) {
        // If the optional is present, then the email and password match.
        return userRepo.findUserByEmailAndPassword(user.getEmail(), user.getPassword()).isPresent();
    }


}