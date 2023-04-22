package space.maxus.dnevnik.controllers.response.auth;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class LoginResponse {
    public final String accessToken;
    public final String refreshToken;
    public final UUID uid;
    public final Date validUntil;
}
