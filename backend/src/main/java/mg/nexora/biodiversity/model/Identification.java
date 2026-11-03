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
    private Taxon taxon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agentid", nullable = false)
    private Agent agent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "occurrenceid")
    private Occurrence occurrence;

    public Integer getIdentificationID() { return identificationID; }

    public void setIdentificationID(Integer identificationID) { this.identificationID = identificationID; }

    public LocalDateTime getDateIdentified() { return dateIdentified; }

    public void setDateIdentified(LocalDateTime dateIdentified) { this.dateIdentified = dateIdentified; }

    public String getIdentificationQualifier() { return identificationQualifier; }

    public void setIdentificationQualifier(String identificationQualifier) { this.identificationQualifier = identificationQualifier; }

    public Boolean getIsAcceptedIdentification() { return isAcceptedIdentification; }

    public void setIsAcceptedIdentification(Boolean isAcceptedIdentification) { this.isAcceptedIdentification = isAcceptedIdentification; }

    public Taxon getTaxon() { return taxon; }

    public void setTaxon(Taxon taxon) { this.taxon = taxon; }

    public Agent getAgent() { return agent; }

    public void setAgent(Agent agent) { this.agent = agent; }

    public Occurrence getOccurrence() { return occurrence; }

    public void setOccurrence(Occurrence occurrence) { this.occurrence = occurrence; }

}
