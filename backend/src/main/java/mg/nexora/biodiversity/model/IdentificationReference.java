package mg.nexora.biodiversity.model;

import jakarta.persistence.*;
import java.time.*;
import java.math.BigDecimal;
import mg.nexora.biodiversity.model.enums.*;

import mg.nexora.biodiversity.model.keys.IdentificationReferenceKey;
@Entity
@IdClass(IdentificationReferenceKey.class)
@Table(name = "identification_reference")
public class IdentificationReference {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "identificationid")
    private Identification identificationID;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "referenceid")
    private BibliographicResource referenceID;

    public Identification getIdentificationID() { return identificationID; }

    public void setIdentificationID(Identification identificationID) { this.identificationID = identificationID; }

    public BibliographicResource getReferenceID() { return referenceID; }

    public void setReferenceID(BibliographicResource referenceID) { this.referenceID = referenceID; }

}
