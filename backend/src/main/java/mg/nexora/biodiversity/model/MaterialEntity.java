package mg.nexora.biodiversity.model;

import jakarta.persistence.*;
import java.time.*;
import java.math.BigDecimal;
import mg.nexora.biodiversity.model.enums.*;

@Entity
@Table(name = "materialentity")
public class MaterialEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "materialentityid")

    private Integer materialEntityID;

    @Column(name = "institutioncode")

    private String institutionCode;

    @Column(name = "collectioncode")

    private String collectionCode;

    @Column(name = "catalognumber")

    private String catalogNumber;

    @Column(name = "preparations")

    private String preparations;

    @Column(name = "disposition")

    private String disposition;

    @Column(name = "materialentityremarks")

    private String materialEntityRemarks;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "materialentitycategoryid", nullable = false)
    private MaterialEntityCategory materialEntityCategoryID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "materialentitytypeid", nullable = false)
    private MaterialEntityType materialEntityTypeID;

    public Integer getMaterialEntityID() { return materialEntityID; }

    public void setMaterialEntityID(Integer materialEntityID) { this.materialEntityID = materialEntityID; }

    public String getInstitutionCode() { return institutionCode; }

    public void setInstitutionCode(String institutionCode) { this.institutionCode = institutionCode; }

    public String getCollectionCode() { return collectionCode; }

    public void setCollectionCode(String collectionCode) { this.collectionCode = collectionCode; }

    public String getCatalogNumber() { return catalogNumber; }

    public void setCatalogNumber(String catalogNumber) { this.catalogNumber = catalogNumber; }

    public String getPreparations() { return preparations; }

    public void setPreparations(String preparations) { this.preparations = preparations; }

    public String getDisposition() { return disposition; }

    public void setDisposition(String disposition) { this.disposition = disposition; }

    public String getMaterialEntityRemarks() { return materialEntityRemarks; }

    public void setMaterialEntityRemarks(String materialEntityRemarks) { this.materialEntityRemarks = materialEntityRemarks; }

    public MaterialEntityCategory getMaterialEntityCategoryID() { return materialEntityCategoryID; }

    public void setMaterialEntityCategoryID(MaterialEntityCategory materialEntityCategoryID) { this.materialEntityCategoryID = materialEntityCategoryID; }

    public MaterialEntityType getMaterialEntityTypeID() { return materialEntityTypeID; }

    public void setMaterialEntityTypeID(MaterialEntityType materialEntityTypeID) { this.materialEntityTypeID = materialEntityTypeID; }

}
