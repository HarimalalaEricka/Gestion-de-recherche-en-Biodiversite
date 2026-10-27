package mg.nexora.biodiversity.model;

import jakarta.persistence.*;
import java.time.*;
import java.math.BigDecimal;
import mg.nexora.biodiversity.model.enums.*;

@Entity
@Table(name = "ligne")
public class Ligne {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "ligneid")

    private Integer ligneID;

    @Column(name = "date_creation", nullable = false)

    private LocalDate dateCreation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tableau_personnaliseid", nullable = false)
    private TableauPersonnalise tableauPersonnaliseID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agentid", nullable = false)
    private Agent agentID;

    public Integer getLigneID() { return ligneID; }

    public void setLigneID(Integer ligneID) { this.ligneID = ligneID; }

    public LocalDate getDateCreation() { return dateCreation; }

    public void setDateCreation(LocalDate dateCreation) { this.dateCreation = dateCreation; }

    public TableauPersonnalise getTableauPersonnaliseID() { return tableauPersonnaliseID; }

    public void setTableauPersonnaliseID(TableauPersonnalise tableauPersonnaliseID) { this.tableauPersonnaliseID = tableauPersonnaliseID; }

    public Agent getAgentID() { return agentID; }

    public void setAgentID(Agent agentID) { this.agentID = agentID; }

}
