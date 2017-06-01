package com.xiaoantech.electrombile.utils;

/**
 * Created by yangxu on 2017/4/19.
 */

public class ErrorCodeConvertUtil {
    public static String getHttpErrorStrWithCode(int code){
        String errStr = "";
        switch (code){
            case 100:
                errStr = "服务器内部错误!";
                break;
            case 101:
                errStr = "请求无设备信息";
                break;
            case 102:
                errStr = "请求无内容信息";
                break;
            case 103:
                errStr = "请求内容出错";
                break;
            case 104:
                errStr = "请求目标错误";
                break;
            case 106:
            case 107:
                errStr = "设备服务器无响应";
                break;
            case 108:
                errStr = "设备无响应";
                break;
            case 109:
                errStr = "设备不在线";
                break;
            case 111:
                errStr = "设备不支持该操作";
                break;
        }
        return errStr;
    }

}
