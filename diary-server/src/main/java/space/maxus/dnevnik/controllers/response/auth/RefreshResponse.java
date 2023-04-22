package space.maxus.dnevnik.controllers.response.auth;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class RefreshResponse {
    public final Date validUntil;
    private final String accessToken;
    private final UUID uid;
}
