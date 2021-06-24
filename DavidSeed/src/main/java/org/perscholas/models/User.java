package org.perscholas.models;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
//database
@Entity
//springboot
@Component
public class User implements Serializable {
    static final long serialVersionUID = 6381462249347345007L;

    @Id @NonNull
    @Email(regexp = "\\b[\\w\\.-]+@[\\w\\.-]+\\.\\w{2,4}\\b",message = "Invalid email address")
    String email;

    @NonNull
    @Length(min = 3, max = 25, message = "should be between {1} and {2}")
    //@NotBlank(message = "Please Enter a Username")
    String name;
    /*
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$",
            message = "- at least 8 characters\n- must contain at least 1 uppercase letter," +
                    " 1 lowercase letter, and 1 number\n- Can contain special characters")
    */
    @NonNull
    String password;
    Date creationDate = new Date();
    boolean bye;
    String displayName = name;

    public User(@NonNull String email, @NonNull String name, @NonNull String password) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.creationDate = new Date();
    }
    //
//    public User(@NonNull String email, @NonNull String name, @NonNull String password) {
//        this.email = email;
//        this.name = name;
//        this.password = password;
//    }

    public User(boolean isBye)
    {
        this.bye = isBye;
        this.name = "Bye";
    }


    public void printName()
    {
        System.out.println(this.getName());
    }
}
