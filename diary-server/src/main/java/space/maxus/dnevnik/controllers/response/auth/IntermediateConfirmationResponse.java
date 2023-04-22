package space.maxus.dnevnik.controllers.response.auth;

import lombok.Data;

@Data
public class IntermediateConfirmationResponse {
    private String next = "/student/confirm/commit";
}
