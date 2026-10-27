package mg.nexora.biodiversity.model.keys;

import java.io.Serializable;
import java.util.Objects;

public class AgentUserKey implements Serializable {
    private Integer agentID;
    private Integer utilisateurID;

    public AgentUserKey() {}

    public Integer getAgentID() { return agentID; }
    public void setAgentID(Integer agentID) { this.agentID = agentID; }
    public Integer getUtilisateurID() { return utilisateurID; }
    public void setUtilisateurID(Integer utilisateurID) { this.utilisateurID = utilisateurID; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AgentUserKey that = (AgentUserKey) o;
        return Objects.equals(agentID, that.agentID) && Objects.equals(utilisateurID, that.utilisateurID);
    }
    @Override public int hashCode() { return Objects.hash(agentID, utilisateurID); }
}
