package mg.nexora.biodiversity.model.keys;

import java.io.Serializable;
import java.util.Objects;

public class EventReferenceKey implements Serializable {
    private Integer eventID;
    private Integer referenceID;

    public EventReferenceKey() {}

    public Integer getEventID() { return eventID; }
    public void setEventID(Integer eventID) { this.eventID = eventID; }
    public Integer getReferenceID() { return referenceID; }
    public void setReferenceID(Integer referenceID) { this.referenceID = referenceID; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventReferenceKey that = (EventReferenceKey) o;
        return Objects.equals(eventID, that.eventID) && Objects.equals(referenceID, that.referenceID);
    }
    @Override public int hashCode() { return Objects.hash(eventID, referenceID); }
}
