package org.perscholas.models;

import javax.persistence.Id;
import java.util.ArrayList;

public class Bracket {

    //fields
    @Id
    String bracketID_code;
    String name;
    ArrayList<User> seededList;
    ArrayList<User> placingList;
    Match bracketMatch;

    void CreateBracket()
    {
        this.bracketMatch = MatchMaker(seededList);
    }

    Match MatchMaker(ArrayList<User> inputList)
    {
        //input:    List of Users sorted by seed
        //output:   single Match object that contains all info on who plays who

        /*
         first, put all the User from the inputList
         in an ArrayList of Match objects
         keeping their order because it represents their seed
         */
        ArrayList<Match> MList = new ArrayList<Match>();
        for (int ii=0; ii<inputList.size(); ii++)
        {
            MList.add(ii, new Match(inputList.get(ii)));
        }

        /*
        however brackets need to have a number of contestants that
        is a power of 2. If they number of players is not a power of 2,
        fake players need to be added until it is. This represents byes.
        Fake players always lose and will always be the lowest seeds.
         */

        //create a User Object that represents a bye
        User byeGuy = new User(true);
        //figure out how many players will be in the final bracket
        int bracketSize = 1;
        while (bracketSize < MList.size())
            bracketSize = bracketSize * 2;

        /*
        fill the MList with dummy User Objects
        until the MList size matches the power of 2 we found
         */
        while(MList.size() < bracketSize)
            MList.add(new Match(byeGuy));
        /*
        Now that we have a List of Matches of a size that is a power of 2,
        we just need to pair them up until we have one big Match object.
        Since the MList is sorted by seed, we just need to do a sort of "rainbow join",
        where the first element is paired with the last, second with the second to last, etc.

        Each "rainbow join" should half the size of the MList.
        And since the size of MList is a power of 2,
        we should be able to repeat "rainbow join" until theres only one Match object at position 0.
        We then return that Match object at MList[0]
         */

        int totalMatches = bracketSize;
        while (totalMatches > 1)
        {
            for (int ii = 0; ii < (totalMatches/2); ii++)
            {
                Match newMatch = new Match(MList.get(ii),MList.get(totalMatches-(ii+1)));
                MList.set(ii, newMatch);
            }
            totalMatches = totalMatches/2;
        }

        return MList.get(0);
    }

}
