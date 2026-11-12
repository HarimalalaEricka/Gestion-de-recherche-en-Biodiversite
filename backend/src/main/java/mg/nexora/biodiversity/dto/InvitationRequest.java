package mg.nexora.biodiversity.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import mg.nexora.biodiversity.model.enums.Role;

public record InvitationRequest(
        @NotBlank String nom,
        @NotBlank @Email String email,
        @NotNull Role role
) {}