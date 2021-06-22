package org.perscholas.services;
import lombok.extern.slf4j.Slf4j;
import org.perscholas.dao.IBracketRepo;
import org.perscholas.dao.IUserRepo;
import org.perscholas.models.Bracket;
import org.springframework.stereotype.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
@Slf4j
@Service
public class BracketService  {

    private final IBracketRepo bracketRepo;
    private final IUserRepo userRepo;

    @Autowired
    public BracketService(IBracketRepo bracketRepoInput, IUserRepo userRepo) {
        this.bracketRepo = bracketRepoInput;
        this.userRepo = userRepo;
    }

     /*
            - add class annotations
            - add @Transactional on class or on each method
            - add crud methods
     */

    public Bracket saveBracket(Bracket savedBracket) {
        return bracketRepo.save(savedBracket);
    }
    public boolean AddNewUserToBracket(String bracketID, String userEmail)
    {
        Bracket updatedBracket = bracketRepo.getById(bracketID);
        if(userRepo.findByEmail(userEmail).isEmpty())
        {
            log.warn("User Does not exist");
            return false;
        }
        if (updatedBracket.getSeededList().contains(userRepo.getById(userEmail)))
        {
            log.info("Already in Bracket");
            return false;
        }
        updatedBracket.getSeededList().add(userRepo.getById(userEmail));
        bracketRepo.save(updatedBracket);
        log.info("Added User " + userRepo.getById(userEmail).getName());
        return true;

    }
    public boolean RemoveUserFromBracket(String bracketID, String userEmail)
    {
        Bracket updatedBracket = bracketRepo.getById(bracketID);
        if(userRepo.findByEmail(userEmail).isEmpty())
        {
            log.warn("User Does not exist");
            return false;
        }
        if (updatedBracket.getSeededList().contains(userRepo.getById(userEmail)))
        {

            updatedBracket.getSeededList().remove(userRepo.getById(userEmail));
            bracketRepo.save(updatedBracket);
            log.info("Removed User " + userRepo.getById(userEmail).getName());
            return true;
        }
        log.warn("User not in Bracket");
        return false;

    }


    public List<Bracket> getAllBrackets() {
        return bracketRepo.findAll();
    }

    public Optional<Bracket> getBracketById(String id_code) {
        Optional<Bracket> bracketOptional = bracketRepo.findById(id_code);
        return bracketOptional;
    }




}
