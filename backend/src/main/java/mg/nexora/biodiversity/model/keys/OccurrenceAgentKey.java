package mg.nexora.biodiversity.model.keys;

import java.io.Serializable;
import java.util.Objects;

public class OccurrenceAgentKey implements Serializable {
    private Integer agentID;
    private Integer occurrenceID;

    public OccurrenceAgentKey() {}

    public Integer getAgentID() { return agentID; }
    public void setAgentID(Integer agentID) { this.agentID = agentID; }
    public Integer getOccurrenceID() { return occurrenceID; }
    public void setOccurrenceID(Integer occurrenceID) { this.occurrenceID = occurrenceID; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OccurrenceAgentKey that = (OccurrenceAgentKey) o;
        return Objects.equals(agentID, that.agentID) && Objects.equals(occurrenceID, that.occurrenceID);
    }
    @Override public int hashCode() { return Objects.hash(agentID, occurrenceID); }
}
