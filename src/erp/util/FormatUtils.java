/* Copyright 2004 for General Secretariat of the Council of the European Union (GSC). */
 /* This code belongs to the GSC.                                                      */
package erp.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * This class is generally used for date formating. There are 4 predefined types
 * of date formats (defined as final static constants).
 */
public class FormatUtils {

    public static final String DATEPATTERN = "dd/MM/yyyy";
    public static final String TIMEPATTERN = "HH:mm";
    public static final String FULLDATEPATTERN = "dd/MM/yyyy HH:mm";
    public static final String TIMESTAMPDATEPATTERN = "yyyy-MM-dd";
    /**
     * European date format without time
     */
    public static final int DD_MM_YYYY_NO_TIME = 1;
    /**
     * European date format with time
     */
    public static final int DD_MM_YYYY_TIME = 2;
    /**
     * American date format without time
     */
    public static final int MM_DD_YYYY_NO_TIME = 3;
    /**
     * American date format with time
     */
    public static final int MM_DD_YYYY_TIME = 4;
    /**
     * Constant ID for SUNDAY
     */
    public static final int SUNDAY = 1;
    /**
     * Constant ID for MONDAY
     */
    public static final int MONDAY = 2;
    /**
     * Constant ID for TUESDAY
     */
    public static final int TUESDAY = 3;
    /**
     * Constant ID for WEDNESDAY
     */
    public static final int WEDNESDAY = 4;
    /**
     * Constant ID for THURSDAY
     */
    public static final int THURSDAY = 5;
    /**
     * Constant ID for FRIDAY
     */
    public static final int FRIDAY = 6;
    /**
     * Constant ID for SATURDAY
     */
    public static final int SATURDAY = 7;

    /**
     * Defauls private constructor, this is a common pattern for every utility
     * class.
     */
    private FormatUtils() {
        super();
    }

    public static String addOneDay(String date) {
        // Start date
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Calendar c = Calendar.getInstance();
            c.setTime(sdf.parse(date));
            c.add(Calendar.DATE, 1);  // number of days to add
            String retValue = sdf.format(c.getTime());
            return retValue;
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static Date addOneDay(Date date) {
        // Start date
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(Calendar.DATE, 1);  // number of days to add			
            return c.getTime();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Date minusOneDay(Date date) {
        // Start date
        try {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(Calendar.DATE, -1);  // number of days to add			
            return c.getTime();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Date shiftDatePerDay(Date date, int days) {
        // Start date
        try {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(Calendar.DATE, days);  // number of days to add			
            return c.getTime();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Date shiftDatePerMinute(Date date, int minutes) {
        // Start date
        try {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(Calendar.MINUTE, minutes);  // number of days to add			
            return c.getTime();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Date shiftDatePerHour(Date date, int hours) {
        // Start date
        try {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(Calendar.HOUR, hours);  // number of days to add			
            return c.getTime();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Date shiftDatePerYear(Date date, int years) {
        // Start date
        try {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(Calendar.YEAR, years);  // number of days to add			
            return c.getTime();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String minusOneDay(String date) {
        // Start date
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Calendar c = Calendar.getInstance();
            c.setTime(sdf.parse(date));
            c.add(Calendar.DATE, -1);  // number of days to add
            String retValue = sdf.format(c.getTime());
            return retValue;
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static int getDayWeek() {
        Calendar calendar = Calendar.getInstance();
        int weekday = calendar.get(Calendar.DAY_OF_WEEK);
        return weekday;
    }

    public static Date addTime(Date date, int days, int type) {
        Calendar cal = Calendar.getInstance();
        if (date != null) {
            cal.setTime(date);
        }
        cal.add(type, days);
        return cal.getTime();
    }

    public static int subtractDate(Date start, Date end) {
        GregorianCalendar gc1 = new GregorianCalendar();
        gc1.setTime(start);
        GregorianCalendar gc2 = new GregorianCalendar();
        gc2.setTime(end);

        int days1 = 0;
        int days2 = 0;
        int minYear = Math.min(gc1.get(Calendar.YEAR), gc2.get(Calendar.YEAR));

        GregorianCalendar gctmp = (GregorianCalendar) gc1.clone();
        for (int f = gctmp.get(Calendar.YEAR); f > minYear; f--) {
            days1 += gctmp.getActualMaximum(Calendar.DAY_OF_YEAR);
            gctmp.add(Calendar.YEAR, -1);
        }

        gctmp = (GregorianCalendar) gc2.clone();
        for (int f = gctmp.get(Calendar.YEAR); f > minYear; f--) {
            days2 += gctmp.getActualMaximum(Calendar.DAY_OF_YEAR);
            gctmp.add(Calendar.YEAR, -1);
        }

        days1 += gc1.get(Calendar.DAY_OF_YEAR) - 1;
        days2 += gc2.get(Calendar.DAY_OF_YEAR) - 1;

        return (days2 - days1);
    }

    public static String formatDate(java.util.Date date) {
        if (date != null) {
            DateFormat df = new SimpleDateFormat(DATEPATTERN);
            return df.format(date);
        } else {
            return "";
        }
    }

    public static String formatTimeStamp(Timestamp date) {
        if (date != null) {
            DateFormat df = new SimpleDateFormat(DATEPATTERN);
            return df.format(date);
        } else {
            return "";
        }
    }

    public static String formatDate(Date date, String patern) {
        if (date != null) {
            DateFormat df = new SimpleDateFormat(patern);
            return df.format(date);
        } else {
            return "";
        }
    }

    public static Date getDate(String date) {
        DateFormat df = new SimpleDateFormat(DATEPATTERN);
        try {
            return df.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();

        }
    }

    public static Timestamp getTimestamp(String date) {
        DateFormat df = new SimpleDateFormat(DATEPATTERN);
        try {
            Date temp = df.parse(date);
            return new Timestamp(temp.getTime());
        } catch (ParseException e) {
            //e.printStackTrace();
            return new Timestamp(new Date().getTime());

        }
    }

    public static Timestamp formatDateToTimestamp(java.util.Date date) throws Exception {
        Timestamp timestamp = null;
        try {
            DateFormat df = new SimpleDateFormat(DATEPATTERN);
            String dateString = df.format(date);
            timestamp = new Timestamp(df.parse(dateString).getTime());
            return timestamp;
        } catch (RuntimeException e) {
            throw e;
        }
    }

    public static Timestamp formatDateToTimestamp(java.util.Date date, String pattern) {
        Timestamp timestamp = null;
        try {
            DateFormat df = new SimpleDateFormat(pattern);
            String dateString = df.format(date);
            timestamp = new Timestamp(df.parse(dateString).getTime());
            return timestamp;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Returns a string from the input timestamp in the designated format.
     * Formats can be of the following types : <ul> <li> DD/MM/YYYY HH24:MI:SS
     * <li>
     * DD/MM/YYYY <li> MM/DD/YYYY HH24:MI:SS <li> MM/DD/YYYY </ul?
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     * @param timestamp is the epoc number to be used
     * @param format is the preferred format to be returned
     *
     * @return a String in the preferred format
     */
    public static String getFormat(long timestamp, int format) {
        java.util.Date modifiedDate = new java.util.Date(timestamp);
        return getFormatFromDate(modifiedDate, format);
    }

    /**
     * Return the hour in 24 basis (i.e. 0 - 23)
     *
     * @param date is the java.util.Date as input
     *
     * @return an integer value which represents the time in 24 basis
     */
    public static int getHourIn24Basis(java.util.Date date) {
        GregorianCalendar gcal = new GregorianCalendar();
        gcal.setTime(date);

        int offset = 0;

        if (gcal.get(Calendar.AM_PM) == 1) {
            offset = 12;
        }

        return gcal.get(Calendar.HOUR) + offset;
    }

    public static int getCurrentMinute(java.util.Date date) {
        GregorianCalendar gcal = new GregorianCalendar();
        gcal.setTime(date);
        int minute = gcal.get(Calendar.MINUTE);
        return minute;
    }

    /**
     * Returns the day of week for a specific date.
     *
     * @param date is the date of which the day of week is been seeked
     *
     * @return 1 sunday, 2 monday, ... 7 saturday
     */
    public static int getDayOfWeek(java.util.Date date) {
        GregorianCalendar gcal = new GregorianCalendar();
        gcal.setTime(date);
        return gcal.get(GregorianCalendar.DAY_OF_WEEK);
    }

    /**
     * Returns the day of month as an integer representation
     *
     * @param date the input date
     *
     * @return the integer representation of the day of month
     */
    public static int getDayOfMonth(java.util.Date date) {
        GregorianCalendar gcal = new GregorianCalendar();
        gcal.setTime(date);
        return gcal.get(GregorianCalendar.DAY_OF_MONTH);
    }

    public static int getMonth(java.util.Date date) {
        GregorianCalendar gcal = new GregorianCalendar();
        gcal.setTime(date);
        return (gcal.get(Calendar.MONTH) + 1);
    }

    public static int getYear(java.util.Date date) {
        GregorianCalendar gcal = new GregorianCalendar();
        gcal.setTime(date);
        return gcal.get(Calendar.YEAR);
    }

    /**
     * Returns a String from the in the designated
     *
     * @param modifiedDate
     * @param format
     * @return
     */
    private static String getFormatFromDate(java.util.Date modifiedDate, int format) {
        String result = new String();

        GregorianCalendar lastModified = new GregorianCalendar();
        lastModified.setTime(modifiedDate);

        if (format == DD_MM_YYYY_TIME) {
            result = gregorianCalendar2OracleDateString(lastModified, true, false);
        } else if (format == DD_MM_YYYY_NO_TIME) {
            result = gregorianCalendar2OracleDateString(lastModified, true, true);
        } else if (format == MM_DD_YYYY_TIME) {
            result = gregorianCalendar2OracleDateString(lastModified, false, false);
        } else if (format == MM_DD_YYYY_NO_TIME) {
            result = gregorianCalendar2OracleDateString(lastModified, false, true);
        }

        return result;

    }

    /**
     * Converts a Gregorian calendar object into an "oracle like" date string
     * DD/MM/YYYY HH24:MI:SS
     *
     * @param gcal the Gregorian calendar object
     * @param europeanFormat true for european format, false for US format
     * @param discardTime true for discarding the time or false for the oposite.
     *
     * @return the String representation of the Gregorian Calendar object (based
     * on the specific parameters passed).
     */
    private static String gregorianCalendar2OracleDateString(java.util.GregorianCalendar gcal,
            boolean europeanFormat, boolean discardTime) {
        String retVal;
        int offset = 0;

        if (gcal.get(Calendar.AM_PM) == 1) {
            offset = 12;
        }

        if (!europeanFormat) {
            if (!discardTime) {
                retVal = pad(gcal.get(Calendar.MONTH) + 1, 2) + "/" + pad(gcal.get(Calendar.DATE), 2) + "/" + gcal.get(Calendar.YEAR) + " "
                        + pad(String.valueOf(gcal.get(Calendar.HOUR) + offset), 2) + ":" + pad(gcal.get(Calendar.MINUTE), 2) + ":" + pad(gcal.get(Calendar.SECOND), 2);
            } else {
                retVal = pad(gcal.get(Calendar.MONTH) + 1, 2) + "/" + pad(gcal.get(Calendar.DATE), 2) + "/" + gcal.get(Calendar.YEAR);
            }
        } else {
            if (!discardTime) {
                retVal = pad(gcal.get(Calendar.DATE), 2) + "/" + pad(gcal.get(Calendar.MONTH) + 1, 2) + "/" + gcal.get(Calendar.YEAR) + " "
                        + pad(String.valueOf(gcal.get(Calendar.HOUR) + offset), 2) + ":" + pad(gcal.get(Calendar.MINUTE), 2) + ":" + pad(gcal.get(Calendar.SECOND), 2);
            } else {
                retVal = pad(gcal.get(Calendar.DATE), 2) + "/" + pad(gcal.get(Calendar.MONTH) + 1, 2) + "/" + gcal.get(Calendar.YEAR);
            }
        }

        return (retVal);
    }

    /**
     * This method finds the date after or before the current date. According to
     * the number (numOfWorkingDays) that we provide.
     *
     * The flag (isForward) finds the date after (true) or the date before
     * (false) today
     *
     * The counted days are working days only. DAY_OF_WEEK - 1 --> SUNDAY
     * DAY_OF_WEEK - 7 --> SATURDAY The previous two are ignored.
     *
     * @param numOfWorkingDays
     * @param isForward
     * @return
     */
    public static Date getWorking(int numOfWorkingDays, boolean isForward) {
        Calendar todayCal = new GregorianCalendar();
        Date today = new Date();
        todayCal.setTime(today);

        while (numOfWorkingDays > 0) {
            if (isForward) {
                todayCal.add(Calendar.DAY_OF_WEEK, 1);
            } else {
                todayCal.add(Calendar.DAY_OF_WEEK, -1);
            }

            int dayOfWeek = todayCal.get(Calendar.DAY_OF_WEEK);

            if ((dayOfWeek != 1) && (dayOfWeek != 7)) {
                numOfWorkingDays--;
            }
        }
        return todayCal.getTime();
    }

    /**
     * Pads a String with a specific padding (e.g. 3 -> 03)
     *
     * @param a the input value
     * @param thePad the padding to be used
     *
     * @return the padded value
     */
    public static String pad(String a, int thePad) {
        String help = new String();
        for (int i = 1; i <= thePad; i++) {
            help += "0";
        }
        return ((new DecimalFormat(help)).format(Long.parseLong(a)));
    }

    /**
     * Overloaded version of the above, (inputs are now integer values)
     *
     * @param a the input value
     * @param thePad the padding to be used
     *
     * @return the padded value
     */
    private static String pad(int a, int thePad) {
        String help = new String();
        for (int i = 1; i <= thePad; i++) {
            help += "0";
        }

        return ((new DecimalFormat(help)).format(a));
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static boolean isWindowsOS() {
        String osName = System.getProperty("os.name").toLowerCase();
        return osName.startsWith("windows");
    }

    public static boolean isDateCorrect(String dateStr) {
        boolean dateIsCorrect = true;
        int day = (new Integer(dateStr.substring(0, 2))).intValue();
        int month = (new Integer(dateStr.substring(3, 5))).intValue();
        int year = (new Integer(dateStr.substring(6))).intValue();
        if (year > 999) {
            GregorianCalendar gc = new GregorianCalendar();
            boolean leapYear = gc.isLeapYear(year);
            if (month < 1 || month > 12) {
                dateIsCorrect = false;
            } else if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
                if (day < 1 || day > 31) {
                    dateIsCorrect = false;
                }
            } else if (day < 1 || day > 30) {
                dateIsCorrect = false;
            } else if (month == 2) {
                if (leapYear && day > 29) {
                    dateIsCorrect = false;
                }
                if (!leapYear && day > 28) {
                    dateIsCorrect = false;
                }
            }
        } else {
            dateIsCorrect = false;
        }
        return dateIsCorrect;
    }

    public static String giveLeadingZeroes(String realVal, int fieldSize) {
        String leadingZeroes = "";
        for (int i = 0; i < fieldSize - realVal.length(); i++) {
            leadingZeroes = (new StringBuilder()).append(leadingZeroes).append("0").toString();
        }

        return leadingZeroes;
    }

    public static String constructDateFieldForHL7(String realVal) {
        String retField = "";
        if (!"".equals(realVal)) {
            String dateParts[] = realVal.split("/");
            retField = (new StringBuilder()).append(retField).append(dateParts[2]).append(dateParts[1]).append(dateParts[0]).toString();
        }
        return retField;
    }

    public static String constructFloatFieldForHL7(String realVal) {
        String retField = "";
        if (-1 != realVal.indexOf(".")) {
            String parts[] = realVal.split("\\.");
            if (parts[1].length() == 1) {
                retField = (new StringBuilder()).append(parts[0]).append(".").append(parts[1]).append("0").toString();
            } else {
                retField = (new StringBuilder()).append(parts[0]).append(".").append(parts[1]).toString();
            }
        } else if (-1 != realVal.indexOf(",")) {
            String parts[] = realVal.split(",");
            if (parts[1].length() == 1) {
                retField = (new StringBuilder()).append(parts[0]).append(".").append(parts[1]).append("0").toString();
            } else {
                retField = (new StringBuilder()).append(parts[0]).append(".").append(parts[1]).toString();
            }

        } else {
            retField = (new StringBuilder()).append(realVal).append(".00").toString();
        }
        return retField;
    }

    public static String makeDateString(boolean withSecs) {
        String dateStr = "";
        GregorianCalendar gc = new GregorianCalendar();
        String yearStr = (new Integer(gc.get(1))).toString();
        int month = (new Integer(gc.get(2))).intValue() + 1;
        String monthStr = (new Integer(month)).toString();
        if (monthStr.length() == 1) {
            monthStr = (new StringBuilder()).append("0").append(monthStr).toString();
        }
        int day = gc.get(5);
        String dayStr = (new Integer(day)).toString();
        if (dayStr.length() == 1) {
            dayStr = (new StringBuilder()).append("0").append(dayStr).toString();
        }
        int hour = gc.get(11);
        String hourStr = (new Integer(hour)).toString();
        if (hourStr.length() == 1) {
            hourStr = (new StringBuilder()).append("0").append(hourStr).toString();
        }
        int minute = gc.get(12);
        String minuteStr = (new Integer(minute)).toString();
        if (minuteStr.length() == 1) {
            minuteStr = (new StringBuilder()).append("0").append(minuteStr).toString();
        }
        if (withSecs) {
            int second = gc.get(13);
            String secondStr = (new Integer(second)).toString();
            if (secondStr.length() == 1) {
                secondStr = (new StringBuilder()).append("0").append(secondStr).toString();
            }
            dateStr = (new StringBuilder()).append(yearStr).append(monthStr).append(dayStr).append(hourStr).append(minuteStr).append(secondStr).toString();
        } else {
            dateStr = (new StringBuilder()).append(yearStr).append(monthStr).append(dayStr).append(hourStr).append(minuteStr).toString();
        }
        return dateStr;
    }

    public static String calculateDoubleGinToStr(double doubleVal, int intVal) {
        String resultStr = (new Double(doubleVal * (double) intVal)).toString();
        String modifiedStr = "";
        String akerPart = "";
        String decPart = "";
        if (-1 == resultStr.indexOf("E")) {
            akerPart = resultStr.substring(0, resultStr.indexOf("."));
            decPart = resultStr.substring(resultStr.indexOf(".") + 1);
        } else {
            int indOfE = resultStr.indexOf("E");
            String last = resultStr.substring(indOfE + 1);
            int lastInt = (new Integer(last)).intValue();
            String tempAker = resultStr.substring(0, resultStr.indexOf("."));
            String tempDecPart = resultStr.substring(resultStr.indexOf(".") + 1, indOfE);
            String tempDecPart2 = tempDecPart.substring(0, lastInt);
            akerPart = (new StringBuilder()).append(tempAker).append(tempDecPart2).toString();
            decPart = tempDecPart.substring(lastInt);
        }
        if (decPart.length() > 0 && !"0".equals(decPart)) {
            if (decPart.length() > 2) {
                Long aker = new Long(akerPart);
                Integer first = new Integer(decPart.substring(0, 1));
                Integer second = new Integer(decPart.substring(1, 2));
                Integer third = new Integer(decPart.substring(2, 3));
                if (third.intValue() > 5) {
                    second = Integer.valueOf(second.intValue() + 1);
                    if (second.intValue() == 10) {
                        second = Integer.valueOf(0);
                        first = Integer.valueOf(first.intValue() + 1);
                        if (first.intValue() == 10) {
                            first = Integer.valueOf(0);
                            aker = Long.valueOf(aker.longValue() + 1L);
                        }
                    }
                }
                modifiedStr = (new StringBuilder()).append(aker.toString()).append(",").append(first.toString()).append(second.toString()).toString();
            } else if (decPart.length() == 1) {
                modifiedStr = (new StringBuilder()).append(akerPart).append(",").append(decPart).append("0").toString();
            } else {
                modifiedStr = (new StringBuilder()).append(akerPart).append(",").append(decPart).toString();
            }
        } else {
            modifiedStr = (new StringBuilder()).append(akerPart).append(",00").toString();
        }
        return modifiedStr;
    }

    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }
}
