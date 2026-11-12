package mg.nexora.biodiversity.model;

import jakarta.persistence.*;
import java.time.*;
import java.math.BigDecimal;
import mg.nexora.biodiversity.model.enums.*;

@Entity
@Table(name = "organism_interaction")
public class OrganismInteraction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "organism_interactionid")

    private Integer organismInteractionID;

    @Column(name = "organisminteractiondescription")

    private String organismInteractionDescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "occurrenceid", nullable = false)
    private Occurrence occurrence;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "occurrenceid_1", nullable = false)
    private Occurrence occurrenceID1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "eventid", nullable = false)
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organisminteractiontypeid", nullable = false)
    private OrganismInteractionType organismInteractionType;

    public Integer getOrganismInteractionID() { return organismInteractionID; }

    public void setOrganismInteractionID(Integer organismInteractionID) { this.organismInteractionID = organismInteractionID; }

    public String getOrganismInteractionDescription() { return organismInteractionDescription; }

    public void setOrganismInteractionDescription(String organismInteractionDescription) { this.organismInteractionDescription = organismInteractionDescription; }

    public Occurrence getOccurrence() { return occurrence; }

    public void setOccurrence(Occurrence occurrence) { this.occurrence = occurrence; }

    public Occurrence getOccurrenceID1() { return occurrenceID1; }

    public void setOccurrenceID1(Occurrence occurrenceID1) { this.occurrenceID1 = occurrenceID1; }

    public Event getEvent() { return event; }

    public void setEvent(Event event) { this.event = event; }

    public OrganismInteractionType getOrganismInteractionType() { return organismInteractionType; }

    public void setOrganismInteractionType(OrganismInteractionType organismInteractionType) { this.organismInteractionType = organismInteractionType; }

}
