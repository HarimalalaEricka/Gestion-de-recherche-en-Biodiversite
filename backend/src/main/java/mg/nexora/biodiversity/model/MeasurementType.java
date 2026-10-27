package mg.nexora.biodiversity.model;

import jakarta.persistence.*;
import java.time.*;
import java.math.BigDecimal;
import mg.nexora.biodiversity.model.enums.*;

@Entity
@Table(name = "measurementtype")
public class MeasurementType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "measurementtypeid")

    private Integer measurementTypeID;

    @Column(name = "measurementtype", nullable = false)

    private String measurementType;

    public Integer getMeasurementTypeID() { return measurementTypeID; }

    public void setMeasurementTypeID(Integer measurementTypeID) { this.measurementTypeID = measurementTypeID; }

    public String getMeasurementType() { return measurementType; }

    public void setMeasurementType(String measurementType) { this.measurementType = measurementType; }

}
