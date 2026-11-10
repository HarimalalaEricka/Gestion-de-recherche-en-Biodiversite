package mg.nexora.biodiversity.dto;

import java.time.LocalDate;
import mg.nexora.biodiversity.model.enums.Confidentiality;

public record CreerRechercheRequest(
    String sujet,
    String description,
    LocalDate dateDebut,
    LocalDate dateFin,
    Confidentiality confidentialite   
) {}