package mg.nexora.biodiversity.model;

import jakarta.persistence.*;
import java.time.*;
import java.math.BigDecimal;
import mg.nexora.biodiversity.model.enums.*;

@Entity
@Table(name = "organism")
public class Organism {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "organismid")

    private Integer organismID;

    @Column(name = "organismname")

    private String organismName;

    @Column(name = "causeofdeath")

    private String causeOfDeath;

    @Column(name = "organismremarks")

    private String organismRemarks;

    @Enumerated(EnumType.STRING)
    @Column(name = "organismscope", nullable = false)

    private OrganismScope organismScope;

    public Integer getOrganismID() { return organismID; }

    public void setOrganismID(Integer organismID) { this.organismID = organismID; }

    public String getOrganismName() { return organismName; }

    public void setOrganismName(String organismName) { this.organismName = organismName; }

    public String getCauseOfDeath() { return causeOfDeath; }

    public void setCauseOfDeath(String causeOfDeath) { this.causeOfDeath = causeOfDeath; }

    public String getOrganismRemarks() { return organismRemarks; }

    public void setOrganismRemarks(String organismRemarks) { this.organismRemarks = organismRemarks; }

    public OrganismScope getOrganismScope() { return organismScope; }

    public void setOrganismScope(OrganismScope organismScope) { this.organismScope = organismScope; }

}
