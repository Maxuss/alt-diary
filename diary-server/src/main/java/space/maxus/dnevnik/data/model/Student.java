package space.maxus.dnevnik.data.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import space.maxus.dnevnik.controllers.response.ResponseStudent;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "students")
@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
@RequiredArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "$id")
public class Student {
    private final String name;
    private final String surname;
    private final String email;
    private final String passHash;
    @OneToMany(mappedBy = "student")
    private final List<Mark> marks;
    private boolean confirmed = false;
    @Id
    private UUID id = UUID.randomUUID();

    public ResponseStudent response() {
        return new ResponseStudent(id, name, surname, email, confirmed, marks != null ? marks.stream().map(Mark::response).toList() : null, null);
    }
}
