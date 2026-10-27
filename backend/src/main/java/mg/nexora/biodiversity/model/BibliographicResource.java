package mg.nexora.biodiversity.model;

import jakarta.persistence.*;
import java.time.*;
import java.math.BigDecimal;
import mg.nexora.biodiversity.model.enums.*;

@Entity
@Table(name = "bibliographic_resource")
public class BibliographicResource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "referenceid")

    private Integer referenceID;

    @Column(name = "referenceremarks")

    private String referenceRemarks;

    @Column(name = "reference")

    private String reference;

    @Enumerated(EnumType.STRING)
    @Column(name = "referencetype", nullable = false)

    private ReferenceType referenceType;

    public Integer getReferenceID() { return referenceID; }

    public void setReferenceID(Integer referenceID) { this.referenceID = referenceID; }

    public String getReferenceRemarks() { return referenceRemarks; }

    public void setReferenceRemarks(String referenceRemarks) { this.referenceRemarks = referenceRemarks; }

    public String getReference() { return reference; }

    public void setReference(String reference) { this.reference = reference; }

    public ReferenceType getReferenceType() { return referenceType; }

    public void setReferenceType(ReferenceType referenceType) { this.referenceType = referenceType; }

}
