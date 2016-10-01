package com.codepath.simpletodo; /**
 * Created by aditikakadebansal on 9/26/16.
 */
import android.content.Context;

import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;

public class CommonDateFunctions {
    /*
    Converts Date object to String in MM-dd-yyyy format
     */
    public static String convertDateToMMDDYYYY(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat(CommonConstants.MMddyyyy);
        return sdf.format(date);
    }

    /*
    Converts DateTime(String) to String in yyyy-MM-dd HH:mm:ss format
     */
    public static String getDateTimeInYYMMDDHHHMMSS(String datetime) throws ParseException{
        SimpleDateFormat sdf = new SimpleDateFormat(CommonConstants.yyyyMMddHHmmss);
        Date date = sdf.parse(datetime);
        return sdf.format(date);
    }

    /*
    Converts Date(String) in MM-dd-yyyy to Date(String) in yyyy-MM-dd format
     */
    public static String convertDateToYYYYMMDD(String date)throws ParseException{
        SimpleDateFormat sdf = new SimpleDateFormat(CommonConstants.MMddyyyy);
        Date convertedDate = sdf.parse(date);
        return new SimpleDateFormat(CommonConstants.yyyyMMdd).format(convertedDate);

    }
}
