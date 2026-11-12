package mg.nexora.biodiversity.model.keys;

import java.io.Serializable;
import java.util.Objects;

public class OccurrenceAgentKey implements Serializable {
    private Integer agent;
    private Integer occurrence;

    public OccurrenceAgentKey() {}

    public Integer getAgent() { return agent; }
    public void setAgent(Integer agent) { this.agent = agent; }
    public Integer getOccurrence() { return occurrence; }
    public void setOccurrence(Integer occurrence) { this.occurrence = occurrence; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OccurrenceAgentKey that = (OccurrenceAgentKey) o;
        return Objects.equals(agent, that.agent) && Objects.equals(occurrence, that.occurrence);
    }
    @Override public int hashCode() { return Objects.hash(agent, occurrence); }
}
