package statystech.aqaframework.steps.DBsteps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.TableObjects.ParcelTable;
import statystech.aqaframework.common.Context.LwaTestContext;
import statystech.aqaframework.steps.Steps;
import statystech.aqaframework.utils.DBUtils;

public class ParcelSteps extends Steps {

    private static final Logger logger = LoggerFactory.getLogger(ParcelSteps.class);

    ParcelTable parcelTable = new ParcelTable();

    public String setParcelStatus(String status, LwaTestContext lwaTestContext) {
        if (DBUtils.update(String.format("UPDATE %s SET parcelStatus = '%s' WHERE parcelID = %d", parcelTable.getName(), status, lwaTestContext.getParcelID())))
            return "";
        logger.error(String.format("parcelStatus wasn't changed to '%s' WHERE parcelID = %d", status, lwaTestContext.getParcelID()));
        return String.format("parcelStatus wasn't changed to '%s' WHERE parcelID = %d", status, lwaTestContext.getParcelID());
    }
}
