package mg.nexora.biodiversity.model.keys;

import java.io.Serializable;
import java.util.Objects;

public class EventReferenceKey implements Serializable {
    private Integer event;
    private Integer reference;

    public EventReferenceKey() {}

    public Integer getEvent() { return event; }
    public void setEvent(Integer event) { this.event = event; }
    public Integer getReference() { return reference; }
    public void setReference(Integer reference) { this.reference = reference; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventReferenceKey that = (EventReferenceKey) o;
        return Objects.equals(event, that.event) && Objects.equals(reference, that.reference);
    }
    @Override public int hashCode() { return Objects.hash(event, reference); }
}
