package space.maxus.dnevnik.controllers.response;

import lombok.Data;
import org.jetbrains.annotations.Nullable;
import space.maxus.dnevnik.data.model.Student;
import space.maxus.dnevnik.data.model.Teacher;

@Data
public class AccountInformation {
    private final String name;
    private final String surname;
    private final @Nullable String patronymic;
    private final boolean isVerified;
    private final AccountType accountType;
    private final @Nullable String username;

    public static AccountInformation student(Student student) {
        return new AccountInformation(student.getName(), student.getSurname(), null, student.isConfirmed(), AccountType.STUDENT, null);
    }

    public static AccountInformation teacher(Teacher teacher) {
        return new AccountInformation(teacher.getName(), teacher.getSurname(), teacher.getPatronymic(), true, AccountType.TEACHER, null);
    }

    public enum AccountType {
        STUDENT,
        TEACHER,
        ADMIN
    }
}
