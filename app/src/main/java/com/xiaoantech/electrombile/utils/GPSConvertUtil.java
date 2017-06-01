package com.xiaoantech.electrombile.utils;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;

/**
 * Created by yangxu on 2017/3/20.
 */

public class GPSConvertUtil {
    public static LatLng convertFromCommToBdll09(LatLng latLng){
        CoordinateConverter converter = new CoordinateConverter();
        converter.from(CoordinateConverter.CoordType.GPS);
        converter.coord(latLng);
        return converter.convert();
    }

}
