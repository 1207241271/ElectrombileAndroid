package com.xiaoantech.electrombile.constant;

/**
 * Created by yangxu on 2016/10/31.
 */

public class LeanCloudConstant {
    public static final String UserTable = "_User";
    public static final String BindTable = "Bindings";
    public static final String DIDTable = "DID";

    public static final String IMEI = "IMEI";


    //User
    public static final String defaultPassword = "123456";
    public static final String UserName = "username";


    //Bindings
    public static final String User = "user";
    public static final String CreatedAt = "createdAt";

    //DID
    public static final String Itinerary = "itinerary";
    public static final String CarName = "name";
    public static final String Image = "image";
    public static final String Device = "device";
    public static final String Admin = "isAdmin";

    public enum leanCloudOptionType {
        LEAN_CLOUD_OPTION_TYPE_TOTAL_ITINERARY,
        LEAN_CLOUD_OPTION_TYPE_IMEI_LIST
    }


    public static final int BindFlagDID  = 0b0001;
    public static final int BindFlagBIND = 0b1000;

    public enum LeanCloudBindResult{
        LEAN_CLOUD_BIND_RESULT_DID_NONE,
        LEAN_CLOUD_BIND_RESULT_DID_MUCH,
        LEAN_CLOUD_BIND_RESULT_BIND_MUCH,
        LEAN_CLOUD_BIND_RESULT_BIND_SUCCESS,
        LEAN_CLOUD_BIND_RESULT_BIND_FAIL
    }

}