package org.perscholas.models;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
//database
@Entity
//springboot
@Component
public class User {

    public User(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.bye = false;
        //not used
    }

    public User(boolean isBye)
    {
        this.bye = isBye;
        this.name = "Bye";
    }

    @Id
    @Email(regexp = "\\b[\\w\\.-]+@[\\w\\.-]+\\.\\w{2,4}\\b",message = "Invalid email address")
    String email;

    @Length(min = 3, max = 25, message = "should be between {1} and {2}")
    @NotBlank(message = "Please Enter a Username")
    String name;
    /*
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$",
            message = "- at least 8 characters\n- must contain at least 1 uppercase letter," +
                    " 1 lowercase letter, and 1 number\n- Can contain special characters")
    */
    String password;
    Date creationDate = new Date();
    boolean bye;
    String displayName = name;

    public void printName()
    {
        System.out.println(this.getName());
    }
}
