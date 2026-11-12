package mg.nexora.biodiversity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CollaboratorAcceptRequest(
        @NotBlank String token,
        @NotNull Integer rechercheId) {}