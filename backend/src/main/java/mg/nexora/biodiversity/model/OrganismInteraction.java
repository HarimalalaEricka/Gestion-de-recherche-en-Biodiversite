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
    private Occurrence occurrenceID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "occurrenceid_1", nullable = false)
    private Occurrence occurrenceID1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "eventid", nullable = false)
    private Event eventID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organisminteractiontypeid", nullable = false)
    private OrganismInteractionType organismInteractionTypeID;

    public Integer getOrganismInteractionID() { return organismInteractionID; }

    public void setOrganismInteractionID(Integer organismInteractionID) { this.organismInteractionID = organismInteractionID; }

    public String getOrganismInteractionDescription() { return organismInteractionDescription; }

    public void setOrganismInteractionDescription(String organismInteractionDescription) { this.organismInteractionDescription = organismInteractionDescription; }

    public Occurrence getOccurrenceID() { return occurrenceID; }

    public void setOccurrenceID(Occurrence occurrenceID) { this.occurrenceID = occurrenceID; }

    public Occurrence getOccurrenceID1() { return occurrenceID1; }

    public void setOccurrenceID1(Occurrence occurrenceID1) { this.occurrenceID1 = occurrenceID1; }

    public Event getEventID() { return eventID; }

    public void setEventID(Event eventID) { this.eventID = eventID; }

    public OrganismInteractionType getOrganismInteractionTypeID() { return organismInteractionTypeID; }

    public void setOrganismInteractionTypeID(OrganismInteractionType organismInteractionTypeID) { this.organismInteractionTypeID = organismInteractionTypeID; }

}
