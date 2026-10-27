package mg.nexora.biodiversity.model;

import jakarta.persistence.*;
import java.time.*;
import java.math.BigDecimal;
import mg.nexora.biodiversity.model.enums.*;

@Entity
@Table(name = "occurrence")
public class Occurrence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "occurrenceid")

    private Integer occurrenceID;

    @Column(name = "individualcount")

    private Integer individualCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "geological_contextid")
    private GeologicalContext geologicalContextID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organismid", nullable = false)
    private Organism organismID;

    @Enumerated(EnumType.STRING)
    @Column(name = "occurrencestatus", nullable = false)

    private OccurenceStatus occurrenceStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "basisofrecord", nullable = false)

    private BasisOfRecord basisOfRecord;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "eventid", nullable = false)
    private Event eventID;

    public Integer getOccurrenceID() { return occurrenceID; }

    public void setOccurrenceID(Integer occurrenceID) { this.occurrenceID = occurrenceID; }

    public Integer getIndividualCount() { return individualCount; }

    public void setIndividualCount(Integer individualCount) { this.individualCount = individualCount; }

    public GeologicalContext getGeologicalContextID() { return geologicalContextID; }

    public void setGeologicalContextID(GeologicalContext geologicalContextID) { this.geologicalContextID = geologicalContextID; }

    public Organism getOrganismID() { return organismID; }

    public void setOrganismID(Organism organismID) { this.organismID = organismID; }

    public OccurenceStatus getOccurrenceStatus() { return occurrenceStatus; }

    public void setOccurrenceStatus(OccurenceStatus occurrenceStatus) { this.occurrenceStatus = occurrenceStatus; }

    public BasisOfRecord getBasisOfRecord() { return basisOfRecord; }

    public void setBasisOfRecord(BasisOfRecord basisOfRecord) { this.basisOfRecord = basisOfRecord; }

    public Event getEventID() { return eventID; }

    public void setEventID(Event eventID) { this.eventID = eventID; }

}
