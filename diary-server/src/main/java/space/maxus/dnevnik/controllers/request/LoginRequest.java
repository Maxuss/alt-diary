package space.maxus.dnevnik.controllers.request;

import lombok.Data;

@Data
public class LoginRequest {
    private final String email;
    private final String password;
}
