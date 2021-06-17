package org.perscholas.models;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
//Database
@Entity
//Spring Boot
@Repository

@Component
@Table
public class Match {
    //fields
    Match match1;
    Match match2;
    User player;
    boolean endpoint;
    boolean bye;

    public Match(User inputPlayer)
    {
        this.player = inputPlayer;
        this.endpoint = true;
    }
    public Match(boolean bye)
    {
        this.bye = bye;
    }

    public Match(Match matchInput1, Match matchInput2)
    {
        this.match1 = matchInput1;
        this.match2 = matchInput2;
        if (matchInput1.bye)
        {
            this.player = matchInput2.player;
            this.endpoint = true;
        }
        else if (matchInput2.bye)
        {
            this.player = matchInput1.player;
            this.endpoint = true;
        }
        else this.endpoint = false;
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
