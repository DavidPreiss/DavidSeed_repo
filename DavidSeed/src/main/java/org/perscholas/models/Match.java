package org.perscholas.models;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
//Database
@Entity
//Spring Boot
@Component
@Table
public class Match implements Serializable {
    static final long serialVersionUID = 6381462249347345007L;
    //fields
    @Id
   // @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    Match match1;
    Match match2;
    @OneToOne(optional = true)
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

    public String asString()
    {
        if (endpoint)
        {
            return this.player.getName();
        }
        else
        {
            return "^^^\r\n"+
                    this.match1.asString()+
                    "\r\nxxx\r\n"+
                    this.match2.asString()+
                    "\r\nvvv\r\n";
        }
    }
}
