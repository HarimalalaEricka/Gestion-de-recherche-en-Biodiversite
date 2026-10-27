package mg.nexora.biodiversity.model;

import jakarta.persistence.*;
import java.time.*;
import java.math.BigDecimal;
import mg.nexora.biodiversity.model.enums.*;

@Entity
@Table(name = "organisminteractiontype")
public class OrganismInteractionType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "organisminteractiontypeid")

    private Integer organismInteractionTypeID;

    @Column(name = "type")

    private String type;

    public Integer getOrganismInteractionTypeID() { return organismInteractionTypeID; }

    public void setOrganismInteractionTypeID(Integer organismInteractionTypeID) { this.organismInteractionTypeID = organismInteractionTypeID; }

    public String getType() { return type; }

    public void setType(String type) { this.type = type; }

}
