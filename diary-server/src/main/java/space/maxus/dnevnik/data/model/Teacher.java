package space.maxus.dnevnik.data.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "teachers")
@Data @AllArgsConstructor
public class Teacher {

    @Id
    private UUID id;

    private String name;
    private String surname;
    private String patronymic;
    private String email;
    private String passHash;

    public Teacher(String name, String surname, String patronymic, String email, String passHash) {
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.email = email;
        this.passHash = passHash;
    }

    public Teacher() {

    }
}
