package mg.nexora.biodiversity.dto;

public record ChangerMotDePasseRequest(
        String ancienMotDePasse,
        String nouveauMotDePasse,
        String confirmation
) {}