package mg.nexora.biodiversity.model;

import jakarta.persistence.*;
import java.time.*;
import java.math.BigDecimal;
import mg.nexora.biodiversity.model.enums.*;

@Entity
@Table(name = "measurementunit")
public class MeasurementUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "measurementunitid")

    private Integer measurementUnitID;

    @Column(name = "measurementunit", nullable = false)

    private String measurementUnit;

    public Integer getMeasurementUnitID() { return measurementUnitID; }

    public void setMeasurementUnitID(Integer measurementUnitID) { this.measurementUnitID = measurementUnitID; }

    public String getMeasurementUnit() { return measurementUnit; }

    public void setMeasurementUnit(String measurementUnit) { this.measurementUnit = measurementUnit; }

}
