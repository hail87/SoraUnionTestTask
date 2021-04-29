package statystech.aqaframework.DataObjects;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Batch {

    @SerializedName("qty")
    private int quantity;
    @SerializedName("number")
    private String number;
    @SerializedName("ff_center_id")
    private int warehouseID;
}
