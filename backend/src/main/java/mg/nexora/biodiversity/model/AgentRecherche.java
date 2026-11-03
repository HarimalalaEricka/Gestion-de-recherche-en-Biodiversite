package mg.nexora.biodiversity.model;

import jakarta.persistence.*;
import java.time.*;
import java.math.BigDecimal;
import mg.nexora.biodiversity.model.enums.*;

import mg.nexora.biodiversity.model.keys.AgentRechercheKey;
@Entity
@IdClass(AgentRechercheKey.class)
@Table(name = "agent_recherche")
public class AgentRecherche {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agentid")
    private Agent agent;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rechercheid")
    private Recherche recherche;

    public Agent getAgent() { return agent; }

    public void setAgent(Agent agent) { this.agent = agent; }

    public Recherche getRecherche() { return recherche; }

    public void setRecherche(Recherche recherche) { this.recherche = recherche; }

}
