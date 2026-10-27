package mg.nexora.biodiversity.model;

import jakarta.persistence.*;
import java.time.*;
import java.math.BigDecimal;
import mg.nexora.biodiversity.model.enums.*;

@Entity
@Table(name = "protocoltype")
public class ProtocolType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "protocoltypeid")

    private Integer protocolTypeID;

    @Column(name = "type")

    private String type;

    public Integer getProtocolTypeID() { return protocolTypeID; }

    public void setProtocolTypeID(Integer protocolTypeID) { this.protocolTypeID = protocolTypeID; }

    public String getType() { return type; }

    public void setType(String type) { this.type = type; }

}
