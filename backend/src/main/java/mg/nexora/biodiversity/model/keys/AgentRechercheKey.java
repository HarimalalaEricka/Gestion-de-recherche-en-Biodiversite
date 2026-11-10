package mg.nexora.biodiversity.model.keys;

import java.io.Serializable;
import java.util.Objects;

public class AgentRechercheKey implements Serializable {
    private Integer agent;
    private Integer recherche;

    public AgentRechercheKey() {}

    public Integer getAgent() { return agent; }
    public void setAgent(Integer agent) { this.agent = agent; }
    public Integer getRecherche() { return recherche; }
    public void setRecherche(Integer recherche) { this.recherche = recherche; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AgentRechercheKey that = (AgentRechercheKey) o;
        return Objects.equals(agent, that.agent) && Objects.equals(recherche, that.recherche);
    }
    @Override public int hashCode() { return Objects.hash(agent, recherche); }
}
