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
    private Agent agentID;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utilisateurid")
    private Utilisateur utilisateurID;

    @Column(name = "agentroleorder")

    private Integer agentRoleOrder;

    public Agent getAgentID() { return agentID; }

    public void setAgentID(Agent agentID) { this.agentID = agentID; }

    public Utilisateur getUtilisateurID() { return utilisateurID; }

    public void setUtilisateurID(Utilisateur utilisateurID) { this.utilisateurID = utilisateurID; }

    public Integer getAgentRoleOrder() { return agentRoleOrder; }

    public void setAgentRoleOrder(Integer agentRoleOrder) { this.agentRoleOrder = agentRoleOrder; }

}
