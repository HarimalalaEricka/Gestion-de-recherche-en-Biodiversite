package mg.nexora.biodiversity.model;

import jakarta.persistence.*;
import java.time.*;
import java.math.BigDecimal;
import mg.nexora.biodiversity.model.enums.*;

@Entity
@Table(name = "attribut")
public class Attribut {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "attributid")

    private Integer attributID;

    @Column(name = "nom", nullable = false)

    private String nom;

    @Column(name = "cle", nullable = false)

    private String cle;

    @Column(name = "type_donnee", nullable = false)

    private String typeDonnee;

    public Integer getAttributID() { return attributID; }

    public void setAttributID(Integer attributID) { this.attributID = attributID; }

    public String getNom() { return nom; }

    public void setNom(String nom) { this.nom = nom; }

    public String getCle() { return cle; }

    public void setCle(String cle) { this.cle = cle; }

    public String getTypeDonnee() { return typeDonnee; }

    public void setTypeDonnee(String typeDonnee) { this.typeDonnee = typeDonnee; }

}
