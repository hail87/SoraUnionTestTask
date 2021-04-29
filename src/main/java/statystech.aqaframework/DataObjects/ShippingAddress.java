
package statystech.aqaframework.DataObjects;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
@Getter
@Setter
public class ShippingAddress {

    @SerializedName("address_line_1")
    private String addressLine1;
    @SerializedName("address_line_2")
    private String addressLine2;
    @SerializedName("address_type")
    private String addressType;
    @Expose
    private String city;
    @Expose
    private String company;
    @Expose
    private String country;
    @SerializedName("first_name")
    private String firstName;
    @SerializedName("last_name")
    private String lastName;
    @SerializedName("middle_name")
    private String middleName;
    @SerializedName("phone_1")
    private String phone1;
    @SerializedName("phone_2")
    private Object phone2;
    @Expose
    private String state;
    @Expose
    private String title;
    @SerializedName("user_info_id")
    private String userInfoId;
    @Expose
    private String zip;
}
