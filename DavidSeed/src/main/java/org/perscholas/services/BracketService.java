package org.perscholas.services;
import lombok.extern.slf4j.Slf4j;
import org.perscholas.dao.IBracketRepo;
import org.perscholas.dao.ISeededPlayerRepo;
import org.perscholas.dao.IUserRepo;
import org.perscholas.models.Bracket;
import org.perscholas.models.SeededPlayer;
import org.perscholas.models.User;
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
    private final ISeededPlayerRepo seededPlayerRepo;

    @Autowired
    public BracketService(IBracketRepo bracketRepoInput, IUserRepo userRepo, ISeededPlayerRepo seededPlayerRepo) {
        this.bracketRepo = bracketRepoInput;
        this.userRepo = userRepo;
        this.seededPlayerRepo = seededPlayerRepo;
    }

     /*
            - add class annotations
            - add @Transactional on class or on each method
            - add crud methods
     */

    public Bracket saveBracket(Bracket savedBracket) {
        return bracketRepo.save(savedBracket);
    }
    public boolean AddNewUserToBracket(String bracketID, String userID)
    {
        /*Bracket updatedBracket = bracketRepo.getById(bracketID);
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
        SeededOrder newSO = seededOrderRepo.save(new SeededOrder(userRepo.getById(userEmail).getEmail(),bracketID));
        updatedBracket.getSeededList().add(userRepo.getById(userEmail));
        bracketRepo.save(updatedBracket);
        log.info("Added User " + userRepo.getById(userEmail).getName());
        List<SeededOrder> myTestSOList = seededOrderRepo.findAll();
        log.warn(myTestSOList.toString());
        return true;*/

        Bracket updatedBracket = bracketRepo.getById(bracketID);
        if(userRepo.findByEmail(userID).isEmpty())
        {
            log.warn("User Does not exist");
            return false;
        }
        if (updatedBracket.getSeededList().contains(userRepo.getById(userID)))
        {
            log.info("Already in Bracket");
            return false;
        }
        int seed = updatedBracket.getSeededList().size()+1;
        SeededPlayer newPlayer = new SeededPlayer(userID, bracketID,seed );
        if (seededPlayerRepo.existsById(newPlayer.getId()))
        {
            log.info("Already in Bracket");
            return false;
        }
        // this loop moves everyone to make space for new guy
        /*for (String playerID : updatedBracket.getPlayerIDList())
        {
            SeededOrder myGuy = seededOrderRepo.getById(playerID+"+"+newPlayer.getBracketID());
            if(myGuy.getSeed() >= newPlayer.getSeed())
            {
                myGuy.setSeed(myGuy.getSeed()+1);
            }
        }*/
        updatedBracket.getSeededList().add(userRepo.getById(userID));
        bracketRepo.save(updatedBracket);
        if (newPlayer.getSeed() > updatedBracket.getSeededList().size())
            newPlayer.setSeed(updatedBracket.getSeededList().size());
        seededPlayerRepo.save(newPlayer);
        log.info("Added User " + newPlayer.getUserID() +
                " to Bracket " + newPlayer.getBracketID() +
                " at position " + newPlayer.getSeed());
        return true;


    }

    /*public boolean AddNewUserToBracketo(String bracketID, String userEmail, int seed)
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

        int location = (seed - 1);
        while (location > updatedBracket.getSeededList().size()) location--;
        updatedBracket.getSeededList().add(location,userRepo.getById(userEmail));
        bracketRepo.save(updatedBracket);
        log.info("Added User " + userRepo.getById(userEmail).getName());
        return true;

    }
    */
    public boolean AddNewUserToBracket(String bracketID, String userID, int seed)
    {
        Bracket updatedBracket = bracketRepo.getById(bracketID);
        SeededPlayer newPlayer = new SeededPlayer(userID, bracketID, seed);
        if(userRepo.findByEmail(userID).isEmpty())
        {
            log.warn("User Does not exist");
            return false;
        }
        if (updatedBracket.getSeededList().contains(userRepo.getById(userID)))
        {
            log.info("Already in Bracket");
            return false;
        }
        if (seededPlayerRepo.existsById(newPlayer.getId()))
        {
            log.info("Already in Bracket");
            return false;
        }
        // this loop moves everyone to make space for new guy
        for (User existingUser : updatedBracket.getSeededList())
        {
            SeededPlayer myGuy = seededPlayerRepo.getById(existingUser.getEmail()+"+"+newPlayer.getBracketID());
            if(myGuy.getSeed() >= newPlayer.getSeed())
            {
                 myGuy.setSeed(myGuy.getSeed()+1);
            }
        }
        updatedBracket.getSeededList().add(userRepo.getById(userID));
        bracketRepo.save(updatedBracket);
        if (newPlayer.getSeed() > updatedBracket.getSeededList().size())
            newPlayer.setSeed(updatedBracket.getSeededList().size());
        seededPlayerRepo.save(newPlayer);
        log.info("Added User " + newPlayer.getUserID() +
                " to Bracket " + newPlayer.getBracketID() +
                " at position " + newPlayer.getSeed());
        return true;

    }
    public boolean RemoveUserFromBracket(String bracketID, String userID)
    {
        Bracket updatedBracket = bracketRepo.getById(bracketID);
        String removedPlayerID = userID+"+"+bracketID;
        if(userRepo.findByEmail(userID).isEmpty())
        {
            log.warn("User Does not exist");
            return false;
        }
        if (updatedBracket.getSeededList().contains(userRepo.getById(userID)))
        {

            updatedBracket.getSeededList().remove(userRepo.getById(userID));
            bracketRepo.save(updatedBracket);
            log.info("Removed User " + userRepo.getById(userID).getName());
        }
        if(seededPlayerRepo.findById(removedPlayerID).isPresent())
        {
            SeededPlayer removedPlayer = seededPlayerRepo.getById(removedPlayerID);
            for (User existingUser : updatedBracket.getSeededList())
            {
                SeededPlayer myGuy = seededPlayerRepo.getById(existingUser.getEmail()+"+"+bracketID);
                if(myGuy.getSeed() > removedPlayer.getSeed())
                {
                    myGuy.setSeed(myGuy.getSeed()-1);
                }
            }
            seededPlayerRepo.deleteById(removedPlayerID);
            log.info("Removed User " + removedPlayer.getUserID() +
                    " from Bracket " + removedPlayer.getBracketID() +
                    " at position " + removedPlayer.getSeed());
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
