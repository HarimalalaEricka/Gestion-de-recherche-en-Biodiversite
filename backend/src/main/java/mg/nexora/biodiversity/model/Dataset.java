package mg.nexora.biodiversity.model;

import jakarta.persistence.*;
import java.time.*;
import java.math.BigDecimal;
import mg.nexora.biodiversity.model.enums.*;

@Entity
@Table(name = "dataset")
public class Dataset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "datasetid")

    private Integer datasetID;

    @Column(name = "datasetname", nullable = false)

    private String datasetName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "licenceid")
    private Licence licence;

    @Column(name = "rightsholder")

    private String rightsHolder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rechercheid", nullable = false)
    private Recherche recherche;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agentid", nullable = false)
    private Agent agent;

    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    public Integer getDatasetID() { return datasetID; }

    public void setDatasetID(Integer datasetID) { this.datasetID = datasetID; }

    public String getDatasetName() { return datasetName; }

    public void setDatasetName(String datasetName) { this.datasetName = datasetName; }

    public Licence getLicence() { return licence; }

    public void setLicence(Licence licence) { this.licence = licence; }

    public String getRightsHolder() { return rightsHolder; }

    public void setRightsHolder(String rightsHolder) { this.rightsHolder = rightsHolder; }

    public Recherche getRecherche() { return recherche; }

    public void setRecherche(Recherche recherche) { this.recherche = recherche; }

    public Agent getAgent() { return agent; }

    public void setAgent(Agent agent) { this.agent = agent; }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

}
