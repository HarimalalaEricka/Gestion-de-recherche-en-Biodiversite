package mg.nexora.biodiversity.model;

import jakarta.persistence.*;
import java.time.*;
import java.math.BigDecimal;
import mg.nexora.biodiversity.model.enums.*;

import mg.nexora.biodiversity.model.keys.OccurrenceAgentKey;
@Entity
@IdClass(OccurrenceAgentKey.class)
@Table(name = "occurrence_agent")
public class OccurrenceAgent {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agentid")
    private Agent agentID;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "occurrenceid")
    private Occurrence occurrenceID;

    public Agent getAgentID() { return agentID; }

    public void setAgentID(Agent agentID) { this.agentID = agentID; }

    public Occurrence getOccurrenceID() { return occurrenceID; }

    public void setOccurrenceID(Occurrence occurrenceID) { this.occurrenceID = occurrenceID; }

}
