package mg.nexora.biodiversity.model;

import jakarta.persistence.*;
import java.time.*;
import java.math.BigDecimal;
import mg.nexora.biodiversity.model.enums.*;

@Entity
@Table(name = "valeur")
public class Valeur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "valeurid")

    private Integer valeurID;

    @Column(name = "valeur_texte")

    private String valeurTexte;

    @Column(name = "valeur_numeric")

    private BigDecimal valeurNumeric;

    @Column(name = "valeur_boolean")

    private Boolean valeurBoolean;

    @Column(name = "valeur_date")

    private LocalDateTime valeurDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ligneid", nullable = false)
    private Ligne ligneID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attributid", nullable = false)
    private Attribut attributID;

    public Integer getValeurID() { return valeurID; }

    public void setValeurID(Integer valeurID) { this.valeurID = valeurID; }

    public String getValeurTexte() { return valeurTexte; }

    public void setValeurTexte(String valeurTexte) { this.valeurTexte = valeurTexte; }

    public BigDecimal getValeurNumeric() { return valeurNumeric; }

    public void setValeurNumeric(BigDecimal valeurNumeric) { this.valeurNumeric = valeurNumeric; }

    public Boolean getValeurBoolean() { return valeurBoolean; }

    public void setValeurBoolean(Boolean valeurBoolean) { this.valeurBoolean = valeurBoolean; }

    public LocalDateTime getValeurDate() { return valeurDate; }

    public void setValeurDate(LocalDateTime valeurDate) { this.valeurDate = valeurDate; }

    public Ligne getLigneID() { return ligneID; }

    public void setLigneID(Ligne ligneID) { this.ligneID = ligneID; }

    public Attribut getAttributID() { return attributID; }

    public void setAttributID(Attribut attributID) { this.attributID = attributID; }

}
