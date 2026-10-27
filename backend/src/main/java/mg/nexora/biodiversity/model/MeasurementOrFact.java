package mg.nexora.biodiversity.model;

import jakarta.persistence.*;
import java.time.*;
import java.math.BigDecimal;
import mg.nexora.biodiversity.model.enums.*;

@Entity
@Table(name = "measurement_or_fact")
public class MeasurementOrFact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "measurement_or_factid")

    private Integer measurementOrFactID;

    @Column(name = "measurementvalue")

    private String measurementValue;

    @Column(name = "measurementaccuracy")

    private String measurementAccuracy;

    @Column(name = "measurementmethod")

    private String measurementMethod;

    @Column(name = "measurementdetermineddate")

    private LocalDateTime measurementDeterminedDate;

    @Column(name = "measurementremarks")

    private String measurementRemarks;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "occurrenceid", nullable = false)
    private Occurrence occurrenceID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "measurement_or_factid_1")
    private MeasurementOrFact measurementOrFactID1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "measurementunitid", nullable = false)
    private MeasurementUnit measurementUnitID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "measurementtypeid", nullable = false)
    private MeasurementType measurementTypeID;

    public Integer getMeasurementOrFactID() { return measurementOrFactID; }

    public void setMeasurementOrFactID(Integer measurementOrFactID) { this.measurementOrFactID = measurementOrFactID; }

    public String getMeasurementValue() { return measurementValue; }

    public void setMeasurementValue(String measurementValue) { this.measurementValue = measurementValue; }

    public String getMeasurementAccuracy() { return measurementAccuracy; }

    public void setMeasurementAccuracy(String measurementAccuracy) { this.measurementAccuracy = measurementAccuracy; }

    public String getMeasurementMethod() { return measurementMethod; }

    public void setMeasurementMethod(String measurementMethod) { this.measurementMethod = measurementMethod; }

    public LocalDateTime getMeasurementDeterminedDate() { return measurementDeterminedDate; }

    public void setMeasurementDeterminedDate(LocalDateTime measurementDeterminedDate) { this.measurementDeterminedDate = measurementDeterminedDate; }

    public String getMeasurementRemarks() { return measurementRemarks; }

    public void setMeasurementRemarks(String measurementRemarks) { this.measurementRemarks = measurementRemarks; }

    public Occurrence getOccurrenceID() { return occurrenceID; }

    public void setOccurrenceID(Occurrence occurrenceID) { this.occurrenceID = occurrenceID; }

    public MeasurementOrFact getMeasurementOrFactID1() { return measurementOrFactID1; }

    public void setMeasurementOrFactID1(MeasurementOrFact measurementOrFactID1) { this.measurementOrFactID1 = measurementOrFactID1; }

    public MeasurementUnit getMeasurementUnitID() { return measurementUnitID; }

    public void setMeasurementUnitID(MeasurementUnit measurementUnitID) { this.measurementUnitID = measurementUnitID; }

    public MeasurementType getMeasurementTypeID() { return measurementTypeID; }

    public void setMeasurementTypeID(MeasurementType measurementTypeID) { this.measurementTypeID = measurementTypeID; }

}
