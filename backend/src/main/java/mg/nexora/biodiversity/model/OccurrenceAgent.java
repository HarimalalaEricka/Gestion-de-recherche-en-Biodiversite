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
    private Agent agent;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "occurrenceid")
    private Occurrence occurrence;

    public Agent getAgent() { return agent; }

    public void setAgent(Agent agent) { this.agent = agent; }

    public Occurrence getOccurrence() { return occurrence; }

    public void setOccurrence(Occurrence occurrence) { this.occurrence = occurrence; }

}
