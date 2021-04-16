package statystech.aqaframework.DataObjects;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class Warehouse {

    int id;
    String name;
    int assignedQuantity;
    List<Batch> batches;
}
