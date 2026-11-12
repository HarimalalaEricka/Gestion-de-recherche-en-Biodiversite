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

    @Column(name = "utilisateurid")
    private Integer utilisateurId;

    public Integer getAgentID() { return agentID; }

    public void setAgentID(Integer agentID) { this.agentID = agentID; }

    public Integer getUtilisateurId() { return utilisateurId; }

    public void setUtilisateurId(Integer utilisateurId) { this.utilisateurId = utilisateurId; }



}
