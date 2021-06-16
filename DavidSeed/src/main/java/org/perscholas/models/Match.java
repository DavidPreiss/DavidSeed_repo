package org.perscholas.models;

public class Match {
    //fields
    Match match1;
    Match match2;
    User player;
    boolean endpoint;

    public Match(User inputPlayer)
    {
        this.player = inputPlayer;
        this.endpoint = true;
    }

    public Match(Match matchInput1, Match matchInput2)
    {
        this.match1 = matchInput1;
        this.match2 = matchInput2;
        this.endpoint = false;
    }

    public void print()
    {
        if (endpoint)
        {
            player.printName();
        }
        else
        {
            this.match1.print();
            this.match2.print();
        }
    }
}
