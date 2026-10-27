package mg.nexora.biodiversity.model;

import jakarta.persistence.*;
import java.time.*;
import java.math.BigDecimal;
import mg.nexora.biodiversity.model.enums.*;

@Entity
@Table(name = "geological_context")
public class GeologicalContext {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "geological_contextid")

    private Integer geologicalContextID;

    @Column(name = "group_")

    private String group;

    @Column(name = "formation")

    private String formation;

    @Column(name = "member_")

    private String member;

    @Column(name = "bed")

    private String bed;

    @Column(name = "earliestperiodorlowestsystem")

    private String earliestPeriodOrLowestSystem;

    @Column(name = "latestperiodorhighestsystem")

    private String latestPeriodOrHighestSystem;

    @Column(name = "earliestageorloweststage")

    private String earliestAgeOrLowestStage;

    @Column(name = "latestageorhigheststage")

    private String latestAgeOrHighestStage;

    public Integer getGeologicalContextID() { return geologicalContextID; }

    public void setGeologicalContextID(Integer geologicalContextID) { this.geologicalContextID = geologicalContextID; }

    public String getGroup() { return group; }

    public void setGroup(String group) { this.group = group; }

    public String getFormation() { return formation; }

    public void setFormation(String formation) { this.formation = formation; }

    public String getMember() { return member; }

    public void setMember(String member) { this.member = member; }

    public String getBed() { return bed; }

    public void setBed(String bed) { this.bed = bed; }

    public String getEarliestPeriodOrLowestSystem() { return earliestPeriodOrLowestSystem; }

    public void setEarliestPeriodOrLowestSystem(String earliestPeriodOrLowestSystem) { this.earliestPeriodOrLowestSystem = earliestPeriodOrLowestSystem; }

    public String getLatestPeriodOrHighestSystem() { return latestPeriodOrHighestSystem; }

    public void setLatestPeriodOrHighestSystem(String latestPeriodOrHighestSystem) { this.latestPeriodOrHighestSystem = latestPeriodOrHighestSystem; }

    public String getEarliestAgeOrLowestStage() { return earliestAgeOrLowestStage; }

    public void setEarliestAgeOrLowestStage(String earliestAgeOrLowestStage) { this.earliestAgeOrLowestStage = earliestAgeOrLowestStage; }

    public String getLatestAgeOrHighestStage() { return latestAgeOrHighestStage; }

    public void setLatestAgeOrHighestStage(String latestAgeOrHighestStage) { this.latestAgeOrHighestStage = latestAgeOrHighestStage; }

}
