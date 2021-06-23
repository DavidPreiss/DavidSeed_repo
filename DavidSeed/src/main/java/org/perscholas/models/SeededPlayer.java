package org.perscholas.models;

import lombok.*;
import org.springframework.context.annotation.Primary;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
@NoArgsConstructor
public class SeededPlayer {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    int id;
    @NonNull
    String userID;
    @NonNull
    String bracketID;
    @NonNull
    Integer seed;
    @Id
    String id;

    public SeededPlayer(@NonNull String userID, @NonNull String bracketID, @NonNull Integer seed) {
        this.userID = userID;
        this.bracketID = bracketID;
        this.seed = seed;
        this.id = userID +"+"+ bracketID;
    }

}
