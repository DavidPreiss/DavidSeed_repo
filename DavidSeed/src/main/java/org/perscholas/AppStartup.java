package org.perscholas;

import lombok.extern.slf4j.Slf4j;
import org.perscholas.dao.IBracketRepo;
import org.perscholas.dao.ISeededPlayerRepo;
import org.perscholas.dao.IUserRepo;
import org.perscholas.models.Bracket;
import org.perscholas.models.User;
import org.perscholas.services.BracketService;
import org.perscholas.services.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.ArrayList;

@Component
@Slf4j
@Transactional
public class AppStartup implements CommandLineRunner {

    private BracketService bracketService;
    private UserService userService;
    private IUserRepo userRepo;
    private IBracketRepo bracketRepo;
    private ISeededPlayerRepo seededOrderRepo;
    public AppStartup(BracketService bracketService, UserService userService, IUserRepo userRepo, IBracketRepo bracketRepo, ISeededPlayerRepo seededOrderRepo) {
        this.bracketService = bracketService;
        this.userService = userService;
        this.userRepo = userRepo;
        this.bracketRepo = bracketRepo;
        this.seededOrderRepo = seededOrderRepo;
    }

    @Override
    public void run(String... args) throws Exception {

        ArrayList<User> myNewUsers = new ArrayList<>();
        myNewUsers.add( new User("first@test.com", "firstname","123"));
        myNewUsers.add( new User("second@test.com", "secondname","123"));
        myNewUsers.add( new User("third@test.com", "thirdname","123"));
        myNewUsers.add( new User("4@test.com", "4 name","123"));
        myNewUsers.add( new User("5@test.com", "5 name","123"));
        myNewUsers.add( new User("6@test.com", "6 name","123"));
        for (User user : myNewUsers)
            userRepo.save(user);
        log.warn(userRepo.findAll().toString());
        Bracket myFirstBracket = new Bracket("firstBracketID","firstBracketName");
        Bracket mySecondBracket = new Bracket("secondBracketID","secondBracketName");
        log.warn(bracketRepo.findAll().toString());
//        ArrayList<User> myList = new ArrayList<User>();
//        myList.add(userRepo.getById("first@test.com"));
//        myList.add(userRepo.getById("second@test.com"));
//        bracketService.saveBracketWithUsers(myFirstBracket,myList);
//        bracketService.saveBracketWithUsers(mySecondBracket,myList);

//        myFirstBracket.setSeededList(myList);
        bracketRepo.save(myFirstBracket);
        bracketRepo.save(mySecondBracket);
        System.out.println("test");
        //System.out.println(bracketRepo.findById("firstBracketID").get().getSeededList().get(0).getName());
    }
}
