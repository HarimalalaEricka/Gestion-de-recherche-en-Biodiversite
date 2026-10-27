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
    private Agent agentID;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rechercheid")
    private Recherche rechercheID;

    public Agent getAgentID() { return agentID; }

    public void setAgentID(Agent agentID) { this.agentID = agentID; }

    public Recherche getRechercheID() { return rechercheID; }

    public void setRechercheID(Recherche rechercheID) { this.rechercheID = rechercheID; }

}
