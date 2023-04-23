package space.maxus.dnevnik.controllers.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TeacherRegisterRequest {
    @NotBlank(message = "Name is required")
    private final String name;
    @NotBlank(message = "Surname is required")
    private final String surname;
    @NotBlank(message = "Password is required")
    private final String password;
    @NotBlank(message = "Patronymic is required")
    private final String patronymic;
    @NotBlank(message = "Email is required")
    @Email
    private final String email;
    @NotBlank(message = "Register validation is required")
    private final String registerToken;
}
