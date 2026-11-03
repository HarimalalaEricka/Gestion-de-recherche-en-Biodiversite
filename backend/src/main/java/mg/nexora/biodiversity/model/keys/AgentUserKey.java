package mg.nexora.biodiversity.model.keys;

import java.io.Serializable;
import java.util.Objects;

public class AgentUserKey implements Serializable {
    private Integer agent;
    private Integer utilisateur;

    public AgentUserKey() {}

    public Integer getAgent() { return agent; }
    public void setAgent(Integer agent) { this.agent = agent; }
    public Integer getUtilisateur() { return utilisateur; }
    public void setUtilisateur(Integer utilisateur) { this.utilisateur = utilisateur; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AgentUserKey that = (AgentUserKey) o;
        return Objects.equals(agent, that.agent) && Objects.equals(utilisateur, that.utilisateur);
    }
    @Override public int hashCode() { return Objects.hash(agent, utilisateur); }
}
