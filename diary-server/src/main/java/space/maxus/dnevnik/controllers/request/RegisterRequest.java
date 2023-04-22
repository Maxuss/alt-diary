package space.maxus.dnevnik.controllers.request;

import jakarta.validation.constraints.Email;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;

@Data
public class RegisterRequest {
    @NotBlank(message = "Name is required")
    private final String name;
    @NotBlank(message = "Surname is required")
    private final String surname;
    @NotBlank(message = "Password is required")
    private final String password;
    @NotBlank(message = "Email is required") @Email
    private final String email;
}
