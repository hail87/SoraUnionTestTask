package statystech.aqaframework.DataObjects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class Warehouse {

    @SerializedName("ff_center_id")
    private int id;
    @SerializedName("ff_center_name")
    private String name;
    private int warehouseID;
    @SerializedName("assigned_qty")
    private int assignedQuantity;
    @Expose
    private List<Batch> batches;
}
