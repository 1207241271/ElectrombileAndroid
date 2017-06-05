package com.xiaoantech.electrombile.constant;

/**
 * Created by yangxu on 2016/11/13.
 */

public class HttpConstant {
    public static final String ITINERARY = "itinerary";
    public static final String ROUTEMILE = "miles";
    public static final String CMD = "c";


    public enum HttpCmd{
        HTTP_CMD_GET_STATUS,
        HTTP_CMD_GET_GPS,
        HTTP_CMD_SET_AUTOLOCK,
        HTTP_CMD_GET_AUTOLOCK,
        HTTP_CMD_SET_FENCE,
        HTTP_CMD_GET_FENCE,
        HTTP_CMD_GET_BATTERY,
        HTTP_CMD_SET_BATTERY_TYPE,
        HTTP_CMD_START_RECORD,
        HTTP_CMD_STOP_RECORD,
        HTTP_CMD_SET_BLUETOOTH_IMEI,
        HTTP_CMD_DEFAULT,
        HTTP_CMD_SET_SERVER,
        HTTP_CMD_SET_FILENAME,
        HTTP_CMD_SET_BLUETOOTH,
        HTTP_CMD_SET_PHONEALARM,
        HTTP_CMD_SET_LOCKON,
        HTTP_CMD_GET_DEVICEMSG,
        HTTP_CMD_GET_GPS_SIGNAL,
        HTTP_CMD_GET_GSM_SIGNAL,
        HTTP_CMD_SET_ATTEST,
        HTTP_CMD_GET_LOG,
        HTTP_CMD_SET_RESET,
        HTTP_CMD_SET_LINK_SWITCH,
        HTTP_CMD_GET_LINK_SWITCH,
        HTTP_CMD_GET_LOCKON,
        HTTP_CMD_SET_LINK_ELECTRICLOCK,
        HTTP_CMD_GET_LINK_ELECTRICLOCK,
        HTTP_CMD_DEL_BLUETOOTH_IMEI,
        HTTP_CMD_SET_WHEEL,
        HTTP_CMD_OPEN_SEAT
    }
}
