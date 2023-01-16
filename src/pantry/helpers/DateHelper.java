package pantry.helpers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

/**
 * DateHelper represents class providing helper methods for Date Time object
 */
public class DateHelper {
    /**
     * The maximum date possible.
     */
    public static Date MAX_DATE = new Date(Long.MAX_VALUE);

    /**
     * Checks if two dates are on the same day ignoring time.
     *
     * @param date1 the first date, not altered, not null
     * @param date2 the second date, not altered, not null
     * @return true if they represent the same day
     * @throws IllegalArgumentException if either date is <code>null</code>
     */
    public static boolean isSameDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return isSameDay(cal1, cal2);
    }

    /**
     * Checks if two calendars represent the same day ignoring time.
     *
     * @param cal1 the first calendar, not altered, not null
     * @param cal2 the second calendar, not altered, not null
     * @return true if they represent the same day
     * @throws IllegalArgumentException if either calendar is <code>null</code>
     */
    public static boolean isSameDay(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
    }

    /**
     * Checks if a date is today.
     *
     * @param date the date, not altered, not null.
     * @return true if the date is today.
     * @throws IllegalArgumentException if the date is <code>null</code>
     */
    public static boolean isToday(Date date) {
        return isSameDay(date, Calendar.getInstance().getTime());
    }

    /**
     * Checks if a calendar date is today.
     *
     * @param cal the calendar, not altered, not null
     * @return true if cal date is today
     * @throws IllegalArgumentException if the calendar is <code>null</code>
     */
    public static boolean isToday(Calendar cal) {
        return isSameDay(cal, Calendar.getInstance());
    }

    /**
     * Checks if the first date is before the second date ignoring time.
     *
     * @param date1 the first date, not altered, not null
     * @param date2 the second date, not altered, not null
     * @return true if the first date day is before the second date day.
     * @throws IllegalArgumentException if the date is <code>null</code>
     */
    public static boolean isBeforeDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return isBeforeDay(cal1, cal2);
    }

    /**
     * Checks if the first calendar date is before the second calendar date ignoring time.
     *
     * @param cal1 the first calendar, not altered, not null.
     * @param cal2 the second calendar, not altered, not null.
     * @return true if cal1 date is before cal2 date ignoring time.
     * @throws IllegalArgumentException if either of the calendars are <code>null</code>
     */
    public static boolean isBeforeDay(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        if (cal1.get(Calendar.ERA) < cal2.get(Calendar.ERA)) return true;
        if (cal1.get(Calendar.ERA) > cal2.get(Calendar.ERA)) return false;
        if (cal1.get(Calendar.YEAR) < cal2.get(Calendar.YEAR)) return true;
        if (cal1.get(Calendar.YEAR) > cal2.get(Calendar.YEAR)) return false;
        return cal1.get(Calendar.DAY_OF_YEAR) < cal2.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * Checks if the first date is after the second date ignoring time.
     *
     * @param date1 the first date, not altered, not null
     * @param date2 the second date, not altered, not null
     * @return true if the first date day is after the second date day.
     * @throws IllegalArgumentException if the date is <code>null</code>
     */
    public static boolean isAfterDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return isAfterDay(cal1, cal2);
    }

    /**
     * Checks if the first calendar date is after the second calendar date ignoring time.
     *
     * @param cal1 the first calendar, not altered, not null.
     * @param cal2 the second calendar, not altered, not null.
     * @return true if cal1 date is after cal2 date ignoring time.
     * @throws IllegalArgumentException if either of the calendars are <code>null</code>
     */
    public static boolean isAfterDay(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        if (cal1.get(Calendar.ERA) < cal2.get(Calendar.ERA)) return false;
        if (cal1.get(Calendar.ERA) > cal2.get(Calendar.ERA)) return true;
        if (cal1.get(Calendar.YEAR) < cal2.get(Calendar.YEAR)) return false;
        if (cal1.get(Calendar.YEAR) > cal2.get(Calendar.YEAR)) return true;
        return cal1.get(Calendar.DAY_OF_YEAR) > cal2.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * Checks if a date is after today and within a number of days in the future.
     *
     * @param date the date to check, not altered, not null.
     * @param days the number of days.
     * @return true if the date day is after today and within days in the future .
     * @throws IllegalArgumentException if the date is <code>null</code>
     */
    public static boolean isWithinDaysFuture(Date date, int days) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return isWithinDaysFuture(cal, days);
    }

    /**
     * Checks if a calendar date is after today and within a number of days in the future.
     *
     * @param cal  the calendar, not altered, not null
     * @param days the number of days.
     * @return true if the calendar date day is after today and within days in the future .
     * @throws IllegalArgumentException if the calendar is <code>null</code>
     */
    public static boolean isWithinDaysFuture(Calendar cal, int days) {
        if (cal == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        Calendar today = Calendar.getInstance();
        Calendar future = Calendar.getInstance();
        future.add(Calendar.DAY_OF_YEAR, days);
        return (isAfterDay(cal, today) && !isAfterDay(cal, future));
    }

    /**
     * Returns the given date with the time set to the start of the day.
     */
    public static Date getStart(Date date) {
        return clearTime(date);
    }

    /**
     * Returns the given date with the time values cleared.
     */
    public static Date clearTime(Date date) {
        if (date == null) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    /**
     * Determines whether or not a date has any time values.
     *
     * @param date The date.
     * @return true if the date is not null and any of the date's hour, minute,
     * seconds or millisecond values are greater than zero.
     */
    public static boolean hasTime(Date date) {
        if (date == null) {
            return false;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        if (c.get(Calendar.HOUR_OF_DAY) > 0) {
            return true;
        }
        if (c.get(Calendar.MINUTE) > 0) {
            return true;
        }
        if (c.get(Calendar.SECOND) > 0) {
            return true;
        }
        return c.get(Calendar.MILLISECOND) > 0;
    }

    /**
     * Returns the given date with time set to the end of the day
     *
     * @param date The date
     * @return The date with time set to the end of the day
     */
    public static Date getEnd(Date date) {
        if (date == null) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 999);
        return c.getTime();
    }

    /**
     * Returns the maximum of two dates. A null date is treated as being less
     * than any non-null date.
     *
     * @param d1 Input Date1
     * @param d2 Input Date 2
     * @return maximum date
     */
    public static Date max(Date d1, Date d2) {
        if (d1 == null && d2 == null) return null;
        if (d1 == null) return d2;
        if (d2 == null) return d1;
        return (d1.after(d2)) ? d1 : d2;
    }

    /**
     * Returns the minimum of two dates. A null date is treated as being less
     * than any non-null date.
     *
     * @param d1 Input Date1
     * @param d2 Input Date 2
     * @return minimum date
     */
    public static Date min(Date d1, Date d2) {
        if (d1 == null && d2 == null) return null;
        if (d1 == null) return d2;
        if (d2 == null) return d1;
        return (d1.before(d2)) ? d1 : d2;
    }

    /**
     * Converts Date object to LocalDateTime
     *
     * @param dt The Date
     * @return LocalDateTime object
     */
    public static LocalDateTime toLocalDateTime(Date dt) {
        //for more info - https://stackoverflow.com/questions/19431234/converting-between-java-time-localdatetime-and-java-util-date
        return LocalDateTime.ofInstant(dt.toInstant(), ZoneId.systemDefault());
    }

    /**
     * Converts LocalDateTime object to Date object
     *
     * @param local The LocalDateTime object
     * @return the Date
     */
    public static Date fromLocalDateTime(LocalDateTime local) {
        //for more info - https://stackoverflow.com/questions/19431234/converting-between-java-time-localdatetime-and-java-util-date
        Date dt = Date.from(local.atZone(ZoneId.systemDefault()).toInstant());
        return dt;
    }

    /**
     * Calculate no of years from the specified date
     *
     * @param date input date
     * @return number of years from the input date
     */
    public static int YearsFromDate(Date date) {
        LocalDate today = LocalDate.now();
        return Period.between(LocalDate.ofInstant(date.toInstant(), ZoneId.systemDefault()), today).getYears();
    }

    /**
     * Create java.util.Date object from string format date
     *
     * @param dateStr Date as String
     * @return Date object
     * @throws ParseException If the String argument passed is not in the format as the pattern "MM/dd/yyyy", then a ParseException will be thrown.
     */
    public static Date fromString(String dateStr) throws ParseException {
        var formatter = getDateFormatter();
        return formatter.parse(dateStr);
    }

    /**
     * Gets Date formatter used across Pantryware
     *
     * @return DateFormat object
     */
    public static DateFormat getDateFormatter() {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        return dateFormat;
    }

    /**
     * Check if input date as string is an empty string
     *
     * @param dateStr date as String
     * @return true if date is empty string else false
     */
    public static boolean isEmptyDateString(String dateStr) {
        String temp = dateStr.replace("/", "");
        temp = temp.replace(":", "");
        temp = temp.replace("T", "");
        temp = temp.trim();
        return StringHelper.isNullOrEmpty(temp);
    }
}