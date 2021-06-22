package org.perscholas.services;
import org.perscholas.dao.IBracketRepo;
import org.perscholas.models.Bracket;
import org.perscholas.models.User;
import org.springframework.stereotype.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
@Service
public class BracketService  {

    private final IBracketRepo bracketRepo;

    @Autowired
    public BracketService(IBracketRepo bracketRepoInput) {
        this.bracketRepo = bracketRepoInput;
    }

     /*
            - add class annotations
            - add @Transactional on class or on each method
            - add crud methods
     */

    public Bracket saveBracket(Bracket savedBracket) {
        return bracketRepo.save(savedBracket);
    }
    public void saveBracketWithUsers(Bracket savedBracket, List<User> userList)
    {
        savedBracket.setSeededList(userList);
        bracketRepo.save(savedBracket);
    }

    public List<Bracket> getAllBrackets() {
        return bracketRepo.findAll();
    }

    public Optional<Bracket> getBracketById(String id_code) {
        Optional<Bracket> bracketOptional = bracketRepo.findById(id_code);
        return bracketOptional;
    }




}
