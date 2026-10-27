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

    @Column(name = "licence")

    private String licence;

    @Column(name = "rightsholder")

    private String rightsHolder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rechercheid", nullable = false)
    private Recherche rechercheID;

    public Integer getDatasetID() { return datasetID; }

    public void setDatasetID(Integer datasetID) { this.datasetID = datasetID; }

    public String getDatasetName() { return datasetName; }

    public void setDatasetName(String datasetName) { this.datasetName = datasetName; }

    public String getLicence() { return licence; }

    public void setLicence(String licence) { this.licence = licence; }

    public String getRightsHolder() { return rightsHolder; }

    public void setRightsHolder(String rightsHolder) { this.rightsHolder = rightsHolder; }

    public Recherche getRechercheID() { return rechercheID; }

    public void setRechercheID(Recherche rechercheID) { this.rechercheID = rechercheID; }

}
