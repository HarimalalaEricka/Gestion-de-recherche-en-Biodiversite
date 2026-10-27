package mg.nexora.biodiversity.model;

import jakarta.persistence.*;
import java.time.*;
import java.math.BigDecimal;
import mg.nexora.biodiversity.model.enums.*;

@Entity
@Table(name = "tableau_personnalise")
public class TableauPersonnalise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "tableau_personnaliseid")

    private Integer tableauPersonnaliseID;

    @Column(name = "nom", nullable = false)

    private String nom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "datasetid", nullable = false)
    private Dataset datasetID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agentid", nullable = false)
    private Agent agentID;

    public Integer getTableauPersonnaliseID() { return tableauPersonnaliseID; }

    public void setTableauPersonnaliseID(Integer tableauPersonnaliseID) { this.tableauPersonnaliseID = tableauPersonnaliseID; }

    public String getNom() { return nom; }

    public void setNom(String nom) { this.nom = nom; }

    public Dataset getDatasetID() { return datasetID; }

    public void setDatasetID(Dataset datasetID) { this.datasetID = datasetID; }

    public Agent getAgentID() { return agentID; }

    public void setAgentID(Agent agentID) { this.agentID = agentID; }

}
