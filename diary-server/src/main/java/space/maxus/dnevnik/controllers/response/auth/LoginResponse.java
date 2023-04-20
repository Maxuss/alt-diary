package space.maxus.dnevnik.controllers.response.auth;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class LoginResponse {
    public final String token;
    public final UUID uid;
    public final Date validUntil;
}
