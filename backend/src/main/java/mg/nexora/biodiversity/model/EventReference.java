package mg.nexora.biodiversity.model;

import jakarta.persistence.*;
import java.time.*;
import java.math.BigDecimal;
import mg.nexora.biodiversity.model.enums.*;

import mg.nexora.biodiversity.model.keys.EventReferenceKey;
@Entity
@IdClass(EventReferenceKey.class)
@Table(name = "event_reference")
public class EventReference {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "eventid")
    private Event eventID;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "referenceid")
    private BibliographicResource referenceID;

    public Event getEventID() { return eventID; }

    public void setEventID(Event eventID) { this.eventID = eventID; }

    public BibliographicResource getReferenceID() { return referenceID; }

    public void setReferenceID(BibliographicResource referenceID) { this.referenceID = referenceID; }

}
