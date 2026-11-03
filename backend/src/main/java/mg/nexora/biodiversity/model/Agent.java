package mg.nexora.biodiversity.model;

import jakarta.persistence.*;
import java.time.*;
import java.math.BigDecimal;
import mg.nexora.biodiversity.model.enums.*;

@Entity
@Table(name = "agent")
public class Agent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "agentid")

    private Integer agentID;

    @Enumerated(EnumType.STRING)
    @Column(name = "agenttype", nullable = false)

    private AgentType agentType;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utilisateurid")
    private Utilisateur utilisateur;

    @Column(name = "agentroleorder")

    private Integer agentRoleOrder;

    public Integer getAgentID() { return agentID; }

    public void setAgentID(Integer agentID) { this.agentID = agentID; }

    public AgentType getAgentType() { return agentType; }

    public void setAgentType(AgentType agentType) { this.agentType = agentType; }

    public Utilisateur getUtilisateur() { return utilisateur; }

    public void setUtilisateur(Utilisateur utilisateur) { this.utilisateur = utilisateur; }

    public Integer getAgentRoleOrder() { return agentRoleOrder; }


    public void setAgentRoleOrder(Integer agentRoleOrder) { this.agentRoleOrder = agentRoleOrder; }

}
