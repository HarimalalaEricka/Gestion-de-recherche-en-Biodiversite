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
    private Event event;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "referenceid")
    private BibliographicResource reference;

    public Event getEvent() { return event; }

    public void setEvent(Event event) { this.event = event; }

    public BibliographicResource getReference() { return reference; }

    public void setReference(BibliographicResource reference) { this.reference = reference; }

}
