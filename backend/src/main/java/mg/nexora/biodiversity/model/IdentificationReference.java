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
    private Identification identification;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "referenceid")
    private BibliographicResource reference;

    public Identification getIdentification() { return identification; }

    public void setIdentification(Identification identification) { this.identification = identification; }

    public BibliographicResource getReference() { return reference; }

    public void setReference(BibliographicResource reference) { this.reference = reference; }

}
