package mg.nexora.biodiversity.model;

import jakarta.persistence.*;
import java.time.*;
import java.math.BigDecimal;
import mg.nexora.biodiversity.model.enums.*;

@Entity
@Table(name = "identification")
public class Identification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "identificationid")

    private Integer identificationID;

    @Column(name = "dateidentified", nullable = false)

    private LocalDateTime dateIdentified;

    @Column(name = "identificationqualifier")

    private String identificationQualifier;

    @Column(name = "isacceptedidentification", nullable = false)

    private Boolean isAcceptedIdentification;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "taxonid", nullable = false)
    private Taxon taxonID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agentid", nullable = false)
    private Agent agentID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "occurrenceid")
    private Occurrence occurrenceID;

    public Integer getIdentificationID() { return identificationID; }

    public void setIdentificationID(Integer identificationID) { this.identificationID = identificationID; }

    public LocalDateTime getDateIdentified() { return dateIdentified; }

    public void setDateIdentified(LocalDateTime dateIdentified) { this.dateIdentified = dateIdentified; }

    public String getIdentificationQualifier() { return identificationQualifier; }

    public void setIdentificationQualifier(String identificationQualifier) { this.identificationQualifier = identificationQualifier; }

    public Boolean getIsAcceptedIdentification() { return isAcceptedIdentification; }

    public void setIsAcceptedIdentification(Boolean isAcceptedIdentification) { this.isAcceptedIdentification = isAcceptedIdentification; }

    public Taxon getTaxonID() { return taxonID; }

    public void setTaxonID(Taxon taxonID) { this.taxonID = taxonID; }

    public Agent getAgentID() { return agentID; }

    public void setAgentID(Agent agentID) { this.agentID = agentID; }

    public Occurrence getOccurrenceID() { return occurrenceID; }

    public void setOccurrenceID(Occurrence occurrenceID) { this.occurrenceID = occurrenceID; }

}
