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

    @Column(name = "agentremarks")

    private String agentRemarks;

    @Enumerated(EnumType.STRING)
    @Column(name = "agenttype", nullable = false)

    private AgentType agentType;

    public Integer getAgentID() { return agentID; }

    public void setAgentID(Integer agentID) { this.agentID = agentID; }

    public String getAgentRemarks() { return agentRemarks; }

    public void setAgentRemarks(String agentRemarks) { this.agentRemarks = agentRemarks; }

    public AgentType getAgentType() { return agentType; }

    public void setAgentType(AgentType agentType) { this.agentType = agentType; }

}
