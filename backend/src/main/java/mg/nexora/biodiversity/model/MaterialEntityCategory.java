package mg.nexora.biodiversity.model;

import jakarta.persistence.*;
import java.time.*;
import java.math.BigDecimal;
import mg.nexora.biodiversity.model.enums.*;

@Entity
@Table(name = "materialentitycategory")
public class MaterialEntityCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "materialentitycategoryid")

    private Integer materialEntityCategoryID;

    @Column(name = "category")

    private String category;

    public Integer getMaterialEntityCategoryID() { return materialEntityCategoryID; }

    public void setMaterialEntityCategoryID(Integer materialEntityCategoryID) { this.materialEntityCategoryID = materialEntityCategoryID; }

    public String getCategory() { return category; }

    public void setCategory(String category) { this.category = category; }

}
