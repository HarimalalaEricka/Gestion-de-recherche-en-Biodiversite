package mg.nexora.biodiversity.model.keys;

import java.io.Serializable;
import java.util.Objects;

public class AgentRechercheKey implements Serializable {
    private Integer agentID;
    private Integer rechercheID;

    public AgentRechercheKey() {}

    public Integer getAgentID() { return agentID; }
    public void setAgentID(Integer agentID) { this.agentID = agentID; }
    public Integer getRechercheID() { return rechercheID; }
    public void setRechercheID(Integer rechercheID) { this.rechercheID = rechercheID; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AgentRechercheKey that = (AgentRechercheKey) o;
        return Objects.equals(agentID, that.agentID) && Objects.equals(rechercheID, that.rechercheID);
    }
    @Override public int hashCode() { return Objects.hash(agentID, rechercheID); }
}
