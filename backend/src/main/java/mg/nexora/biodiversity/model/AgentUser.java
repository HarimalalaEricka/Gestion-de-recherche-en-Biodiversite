package mg.nexora.biodiversity.model;

import jakarta.persistence.*;
import java.time.*;
import java.math.BigDecimal;
import mg.nexora.biodiversity.model.enums.*;

import mg.nexora.biodiversity.model.keys.AgentUserKey;
@Entity
@IdClass(AgentUserKey.class)
@Table(name = "agent_user")
public class AgentUser {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agentid")
    private Agent agent;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utilisateurid")
    private Utilisateur utilisateur;

    @Column(name = "agentroleorder")

    private Integer agentRoleOrder;

    public Agent getAgent() { return agent; }

    public void setAgent(Agent agent) { this.agent = agent; }

    public Utilisateur getUtilisateur() { return utilisateur; }

    public void setUtilisateur(Utilisateur utilisateur) { this.utilisateur = utilisateur; }

    public Integer getAgentRoleOrder() { return agentRoleOrder; }

    public void setAgentRoleOrder(Integer agentRoleOrder) { this.agentRoleOrder = agentRoleOrder; }

}
