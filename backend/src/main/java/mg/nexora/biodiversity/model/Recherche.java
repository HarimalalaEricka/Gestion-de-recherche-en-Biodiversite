package mg.nexora.biodiversity.model;

import jakarta.persistence.*;
import java.time.*;
import java.math.BigDecimal;
import mg.nexora.biodiversity.model.enums.*;

@Entity
@Table(name = "recherche")
public class Recherche {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "rechercheid")

    private Integer rechercheID;

    @Column(name = "sujet")

    private String sujet;

    @Column(name = "description")

    private String description;

    @Column(name = "date_debut", nullable = false)

    private LocalDate dateDebut;

    @Column(name = "date_creation", nullable = false)

    private LocalDateTime dateCreation;

    @Column(name = "date_fin")

    private LocalDate dateFin;

    @Column(name = "note_perso")

    private String notePerso;

    @Enumerated(EnumType.STRING)
    @Column(name = "confidentialite", nullable = false)

    private Confidentiality confidentialite;

    public Integer getRechercheID() { return rechercheID; }

    public void setRechercheID(Integer rechercheID) { this.rechercheID = rechercheID; }

    public String getSujet() { return sujet; }

    public void setSujet(String sujet) { this.sujet = sujet; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public LocalDate getDateDebut() { return dateDebut; }

    public void setDateDebut(LocalDate dateDebut) { this.dateDebut = dateDebut; }

    public LocalDateTime getDateCreation() { return dateCreation; }

    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }

    public LocalDate getDateFin() { return dateFin; }

    public void setDateFin(LocalDate dateFin) { this.dateFin = dateFin; }

    public String getNotePerso() { return notePerso; }

    public void setNotePerso(String notePerso) { this.notePerso = notePerso; }

    public Confidentiality getConfidentialite() { return confidentialite; }

    public void setConfidentialite(Confidentiality confidentialite) { this.confidentialite = confidentialite; }

}
