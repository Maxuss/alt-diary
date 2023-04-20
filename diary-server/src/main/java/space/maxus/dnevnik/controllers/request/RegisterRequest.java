package space.maxus.dnevnik.controllers.request;

import lombok.Data;

@Data
public class RegisterRequest {
    private final String name;
    private final String surname;
    private final String password;
    private final String email;
}
