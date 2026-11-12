package mg.nexora.biodiversity.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import mg.nexora.biodiversity.model.enums.RoleCollaboration;

public record CollaboratorRequest(
        @NotBlank @Email String email,
        @NotNull RoleCollaboration role) {}