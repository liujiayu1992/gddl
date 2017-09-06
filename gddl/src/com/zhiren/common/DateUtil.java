package com.zhiren.common;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author 王磊
 * 
 */
public class DateUtil {
	public static final DateFormat DATE_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd");

	public static final DateFormat DATE_FORMAT_Time = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	public static final DateFormat TIME_FORMAT = new SimpleDateFormat(
			"HH:mm:ss");

	public static final int AddType_intYear = 1;

	public static final int AddType_intMonth = 10;

	public static final int AddType_intDay = 86400000;

	public static final int AddType_intHours = 3600000;

	public static final int AddType_intMinutes = 60000;

	public static final int AddType_intSeconds = 1000;

	public static final DateFormat DATE_FORMAT_Time_Minute = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm");

	public static String Formatdate(String format, Date date) {
		SimpleDateFormat formatter;
		String StrDate;
		try {
			formatter = new SimpleDateFormat(format);
			StrDate = formatter.format(date);
		} catch (IllegalArgumentException IAE) {
			StrDate = "1900-01-01";
			System.out.println("输入格式化字符串格式不正确！");
		} catch (NullPointerException NPE) {
			StrDate = "1900-01-01";
			System.out.println("格式化字符串或日期为空!");
		} catch (Exception E) {
			StrDate = "1900-01-01";
			System.out.println("未知异常！");
			E.printStackTrace();
		}
		return StrDate;
	}

	public static String FormatDate(Date date) {
		String StrDate;
		try {
			StrDate = DATE_FORMAT.format(date);
		} catch (NullPointerException NPE) {
			StrDate = "1900-01-01";
			System.out.println("格式化日期为空！");
		} catch (Exception E) {
			StrDate = "1900-01-01";
			System.out.println("未知异常！");
		}
		return StrDate;
	}

	public static String FormatOracleDate(String date) {
		return "to_date('" + date + "','yyyy-mm-dd')";
	}

	public static String FormatOracleDate(Date date) {
		String strdate = FormatDate(date);
		return FormatOracleDate(strdate);
	}

	public static String FormatTime(Date date) {
		String StrDate;
		try {
			StrDate = TIME_FORMAT.format(date);
		} catch (NullPointerException NPE) {
			StrDate = "00:00:00";
			System.out.println("格式化时间为空！");
		} catch (Exception E) {
			StrDate = "00:00:00";
			System.out.println("未知异常！");
		}
		return StrDate;
	}

	public static String FormatOracleTime(String date) {
		return "to_date('" + date + "','hh24:mi:ss')";
	}

	public static String FormatOracleTime(Date date) {
		String strdate = FormatTime(date);
		return FormatOracleTime(strdate);
	}

	public static String FormatDateTime(Date date) {
		String StrTime;
		try {
			StrTime = DATE_FORMAT_Time.format(date);
		} catch (NullPointerException NPE) {
			StrTime = "1900-01-01 00:00:00";
			System.out.println("格式化日期为空！");
		} catch (Exception E) {
			StrTime = "1900-01-01 00:00:00";
			System.out.println("未知异常！");
			E.printStackTrace();
		}
		return StrTime;
	}

	public static String FormatOracleDateTime(String date) {
		return "to_date('" + date + "','yyyy-mm-dd hh24:mi:ss')";
	}

	public static String FormatOracleDateTime(Date date) {
		String strdate = FormatDateTime(date);
		return FormatOracleDateTime(strdate);
	}

	public static int getYear(Date _date) {
		SimpleDateFormat formatter;
		String _year;
		int _intyear;
		try {
			formatter = new SimpleDateFormat("yyyy");
			_year = formatter.format(_date);
			_intyear = Integer.parseInt(_year);
		} catch (NullPointerException NPE) {
			_intyear = 1900;
			System.out.println("格式化日期为空！");
		} catch (NumberFormatException NFE) {
			_intyear = 1900;
			System.out.println("错误的数字格式！");
		} catch (Exception E) {
			_intyear = 1900;
			System.out.println("未知异常！");
			E.printStackTrace();
		}
		return _intyear;
	}

	public static int getMonth(Date _date) {
		SimpleDateFormat formatter;
		String _month;
		int _intmount;
		try {
			formatter = new SimpleDateFormat("MM");
			_month = formatter.format(_date);
			_intmount = Integer.parseInt(_month);
		} catch (NullPointerException NPE) {
			_intmount = 1;
			System.out.println("格式化日期为空！");
		} catch (NumberFormatException NFE) {
			_intmount = 1;
			System.out.println("错误的数字格式！");
		} catch (Exception E) {
			_intmount = 1;
			System.out.println("未知异常！");
			E.printStackTrace();
		}
		return _intmount;
	}

	public static int getDay(Date _date) {
		SimpleDateFormat formatter;
		String _day;
		int _intday;
		try {
			formatter = new SimpleDateFormat("dd");
			_day = formatter.format(_date);
			_intday = Integer.parseInt(_day);
		} catch (NullPointerException NPE) {
			_intday = 1;
			System.out.println("格式化日期为空！");
		} catch (NumberFormatException NFE) {
			_intday = 1;
			System.out.println("错误的数字格式！");
		} catch (Exception E) {
			_intday = 1;
			System.out.println("未知异常！");
			E.printStackTrace();
		}
		return _intday;
	}

	public static int getHour(Date _date) {
		SimpleDateFormat formatter;
		String _hours;
		int _inthours;
		try {
			formatter = new SimpleDateFormat("HH");
			_hours = formatter.format(_date);
			_inthours = Integer.parseInt(_hours);
		} catch (NullPointerException NPE) {
			_inthours = 0;
			System.out.println("格式化日期为空！");
			// NPE.printStackTrace();
		} catch (NumberFormatException NFE) {
			_inthours = 0;
			System.out.println("错误的数字格式！");
			// NFE.printStackTrace();
		} catch (Exception E) {
			_inthours = 0;
			System.out.println("未知异常！");
			E.printStackTrace();
		}
		return _inthours;
	}

	public static int getMinutes(Date _date) {
		SimpleDateFormat formatter;
		String _minutes;
		int _intminutes;
		try {
			formatter = new SimpleDateFormat("mm");
			_minutes = formatter.format(_date);
			_intminutes = Integer.parseInt(_minutes);
		} catch (NullPointerException NPE) {
			_intminutes = 0;
			System.out.println("格式化日期为空！");
			// NPE.printStackTrace();
		} catch (NumberFormatException NFE) {
			_intminutes = 0;
			System.out.println("错误的数字格式！");
			// NFE.printStackTrace();
		} catch (Exception E) {
			_intminutes = 0;
			System.out.println("未知异常！");
			E.printStackTrace();
		}
		return _intminutes;
	}

	public static int getSeconds(Date _date) {
		SimpleDateFormat formatter;
		String _seconds;
		int _intseconds;
		try {
			formatter = new SimpleDateFormat("ss");
			_seconds = formatter.format(_date);
			_intseconds = Integer.parseInt(_seconds);
		} catch (NullPointerException NPE) {
			_intseconds = 0;
			System.out.println("格式化日期为空！");
			// NPE.printStackTrace();
		} catch (NumberFormatException NFE) {
			_intseconds = 0;
			System.out.println("错误的数字格式！");
			// NFE.printStackTrace();
		} catch (Exception E) {
			_intseconds = 0;
			System.out.println("未知异常！");
			E.printStackTrace();
		}
		return _intseconds;
	}

	public static Date AddDate(Date date, int addTime, int addType) {
		return AddDate(FormatDateTime(date), addTime, addType);
		// Date _date = new Date(date.getTime() + addTime*addType);
		// return _date;
	}

	public static int GetDayOfWeek() {
		Calendar ca = Calendar.getInstance();
		ca.add(Calendar.DATE, -1);
		return ca.get(Calendar.DAY_OF_WEEK);
	}

	public static Date AddDate(String strDate, int addTime, int addType) {
		Calendar ca = Calendar.getInstance();
		try {
			String temp[] = strDate.split(" ");
			String date[] = temp[0].split("-");
			String time[] = temp[1].split(":");
			int year = Integer.parseInt(date[0]);
			int month = Integer.parseInt(date[1]) - 1;
			int day = Integer.parseInt(date[2]);
			int hour = Integer.parseInt(time[0]);
			int minute = Integer.parseInt(time[1]);
			int second = Integer.parseInt(time[2]);
			switch (addType) {
			case AddType_intYear:
				year = year + addTime;
				break;
			case AddType_intMonth:
				month = month + addTime;
				break;
			case AddType_intDay:
				day = day + addTime;
				break;
			case AddType_intHours:
				hour = hour + addTime;
				break;
			case AddType_intMinutes:
				minute = minute + addTime;
				break;
			case AddType_intSeconds:
				second = second + addTime;
				break;
			default:
				break;
			}
			ca.set(year, month, day, hour, minute, second);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return null;
		}
		return ca.getTime();
	}

	public static Date getDate(String strDate) {
		Calendar ca = Calendar.getInstance();
		try {
			String temp[] = strDate.split(" ");
			String date[] = temp[0].split("-");
			ca.set(Integer.parseInt(date[0]), Integer.parseInt(date[1]) - 1,
					Integer.parseInt(date[2]));
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return null;
		}
		return ca.getTime();
	}

	public static Date getDateTime(String strDate) {
		Calendar ca = Calendar.getInstance();
		try {
			String temp[] = strDate.split(" ");
			String date[] = temp[0].split("-");
			String time[] = temp[1].split(":");
			ca.set(Integer.parseInt(date[0]), Integer.parseInt(date[1]) - 1,
					Integer.parseInt(date[2]), Integer.parseInt(time[0]),
					Integer.parseInt(time[1]), Integer.parseInt(time[2]));
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return null;
		}
		return ca.getTime();
	}

	public static Date getDate(DateFormat df, String strDate) {
		if (DATE_FORMAT.equals(df)) {
			return getDate(strDate);
		} else if (DATE_FORMAT_Time.equals(df)) {
			return getDateTime(strDate);
		}
		return null;
	}

	public static Date getFirstDayOfMonth(Date date) {
		return getFDOfMonth(FormatDate(date));
	}

	public static Date getFDOfMonth(String strDate) {
		Calendar ca = Calendar.getInstance();
		try {
			String date[] = strDate.split("-");
			ca.set(Integer.parseInt(date[0]), Integer.parseInt(date[1]) - 1, 1);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return null;
		}
		return ca.getTime();
	}

	public static Date getLastDayOfMonth(Date date) {
		return getLastDayOfMonth(FormatDate(date));
	}

	public static Date getLastDayOfMonth(String strDate) {
		Calendar ca = Calendar.getInstance();
		try {
			String date[] = strDate.split("-");
			ca.set(Integer.parseInt(date[0]), Integer.parseInt(date[1]), 0);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return null;
		}
		return ca.getTime();
	}

	public static Date getFirstDayOfNextMonth(Date date) {
		return getFirstDayOfNextMonth(FormatDate(date));
	}

	public static Date getFirstDayOfNextMonth(String strDate) {
		Calendar ca = Calendar.getInstance();
		try {
			String date[] = strDate.split("-");
			ca.set(Integer.parseInt(date[0]), Integer.parseInt(date[1]), 1);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return null;
		}
		return ca.getTime();
	}

	public static Date getFirstDayOfYear(Date date) {
		return getFirstDayOfYear(FormatDate(date));
	}

	public static Date getFirstDayOfYear(String strDate) {
		Calendar ca = Calendar.getInstance();
		try {
			String date[] = strDate.split("-");
			ca.set(Integer.parseInt(date[0]), 0, 1);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return null;
		}
		return ca.getTime();
	}

	public static Date getLastDayOfYear(Date date) {
		return getLastDayOfYear(FormatDate(date));
	}

	public static Date getLastDayOfYear(String strDate) {
		Calendar ca = Calendar.getInstance();
		try {
			String date[] = strDate.split("-");
			ca.set(Integer.parseInt(date[0]), 11, 31);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return null;
		}
		return ca.getTime();
	}

	public static Date getFirstDayOfNextYear(Date date) {
		return getFirstDayOfNextYear(FormatDate(date));
	}

	public static Date getFirstDayOfNextYear(String strDate) {
		Calendar ca = Calendar.getInstance();
		try {
			String date[] = strDate.split("-");
			ca.set(Integer.parseInt(date[0]) + 1, 0, 1);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return null;
		}
		return ca.getTime();
	}

	// 得到明年日期
	public static Date getFirstDayOfLastYear(Date date) {
		return getFirstDayOfLastYear(FormatDate(date));
	}

	public static Date getFirstDayOfLastYear(String strDate) {
		Calendar ca = Calendar.getInstance();
		try {
			String date[] = strDate.split("-");
			ca.set(Integer.parseInt(date[0]) - 1,
					Integer.parseInt(date[1]) - 1, Integer.parseInt(date[2]));
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return null;
		}
		return ca.getTime();
	}

	public static String FormatDateTimeMinute(Date date) {
		String StrDate;
		try {
			StrDate = DATE_FORMAT_Time_Minute.format(date);
		} catch (NullPointerException NPE) {
			StrDate = "1900-01-01";
			System.out.println("格式化日期为空！");
		} catch (Exception E) {
			StrDate = "1900-01-01";
			System.out.println("未知异常！");
		}
		return StrDate;
	}

	public static String GetDayOfWeektoSting() {
		String dayNames[] = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		return dayNames[dayOfWeek - 1];
	}

	public static long getQuot(String time1, String time2){
        long quot = 0;
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        try {
        Date date1 = ft.parse( time1 );
        Date date2 = ft.parse( time2 );
        quot = date1.getTime() - date2.getTime();
        quot = quot / 1000 / 60 / 60 / 24;
    } catch (ParseException e) {
        e.printStackTrace();
    }
        return quot;
    }
}
