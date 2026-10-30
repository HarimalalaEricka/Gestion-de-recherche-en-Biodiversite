package mg.nexora.biodiversity.dto;

public record LoginResponse(String token, String email, String nom, String role) {}