package com.avit.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

public class StringUtil extends StringUtils {

	/**
	 * Encrypt the string with MD5 algorithm
	 *
	 * @param str input clear text string
	 * @return Return encrypted string.
	 */
	public static String encrypt(String str) {
		String encryptStr = null;
		if (str == null) {
			return null;
		}
		try {
			 //MD5 m = new MD5();
			 //encryptStr = m.getMD5ofStr(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return encryptStr;
	}
	
	/**
	 * Encrypt the string with MD5 algorithm
	 *
	 * @param str input clear text string
	 * @return Return encrypted string.
	 */
	public static String encrypt2(String str) {
		String encryptStr = null;
		if (str == null) {
			return null;
		}
		try {
			/*
			    
			    //��Ϊʹ��JDK�Դ��MD5�㷨�������ַ��Ա�ȥ���cryptix������
			         MessageDigest alga = MessageDigest.getInstance("MD5", "Cryptix");
			    byte[] digest = alga.digest(str.getBytes());
			    encryptStr = Hex.toString(digest);
			 */
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(str.getBytes());
			BigInteger hash = new BigInteger(1, md.digest());
			encryptStr = hash.toString(16).toUpperCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return encryptStr;
	}
	
    public static String toNotNullStr(Object canNullStr, String defaultStr) {
        if (canNullStr == null) {
            if (defaultStr != null) {
                return defaultStr;
            } else {
                return "";
            }
        } else {
            return canNullStr.toString();
        }
    }

    public static String toNotNullStr(Object canNullStr) {
        return toNotNullStr(canNullStr, "");
    }

    public static int toInt(Object objValue, int defaultValue) {
        if (objValue == null) {
            return defaultValue;
        } else {
            try {
                return Integer.parseInt(objValue.toString());
            } catch (Exception ex) {
                return defaultValue;
            }

        }
    }

    public static int toInt(Object objValue) {
        return toInt(objValue, 0);
    }

    public static Integer toInteger(Object objValue, Integer defaultValue) {
        if (objValue == null) {
            return defaultValue;
        } else {
            try {
                return new Integer(objValue.toString());
            } catch (Exception ex) {
                return defaultValue;
            }

        }
    }

    public static Integer toInteger(Object objValue) {
        return toInteger(objValue, new Integer(0));
    }

    public static Long toLong(Object objValue, Long defaultValue) {
        if (objValue == null) {
            return defaultValue;
        } else {
            try {
                return new Long(objValue.toString());
            } catch (Exception ex) {
                return defaultValue;
            }

        }
    }

    public static Long toLong(Object objValue) {
        return toLong(objValue, new Long(0));
    }

    public static Float toFloat(Object objValue, Float defaultValue) {
        if (objValue == null) {
            return defaultValue;
        } else {
            try {
                return new Float(objValue.toString());
            } catch (Exception ex) {
                return defaultValue;
            }

        }
    }

    public static Float toFloat(Object objValue) {
        return toFloat(objValue, new Float(0f));
    }

    public static Double toDouble(Object objValue, Double defaultValue) {
        if (objValue == null) {
            return defaultValue;
        } else {
            try {
                return new Double(objValue.toString());
            } catch (Exception ex) {
                return defaultValue;
            }

        }
    }

    public static Double toDouble(Object objValue) {
        return toDouble(objValue, new Double(0d));
    }

    public static BigDecimal toBigDecimal(Object objValue,
            BigDecimal defaultValue) {
        if (objValue == null) {
            return defaultValue;
        } else {
            try {
                String str = objValue.toString();
                str = str.replaceAll(",", "");
                return new BigDecimal(str);
            } catch (Exception ex) {
                return defaultValue;
            }
        }
    }

    public static BigDecimal toBigDecimal(Object objValue) {
        return toBigDecimal(objValue, new BigDecimal(0));
    }

    public static Timestamp toTimeStamp(Object objValue) {
        return toTimeStamp(objValue, false);
    }

    public static Timestamp toTimeStamp(Object objValue, boolean longFormat) {
        if (objValue == null || objValue.toString().equals("")) {
            return new Timestamp(System.currentTimeMillis());
        } else {
            SimpleDateFormat sdf = (SimpleDateFormat) SimpleDateFormat
                    .getDateInstance(DateFormat.LONG);
            sdf.setLenient(false);
            sdf.applyPattern(longFormat ? "yyyy-MM-dd HH:mm:ss" : "yyyy-MM-dd");
            try {
                return new Timestamp(sdf.parse(objValue.toString()).getTime());
            } catch (ParseException ex) {
                return new Timestamp(System.currentTimeMillis());
            }
        }
    }

    public static Timestamp toDateTime(Object objValue, Timestamp defaultValue) {
        if (objValue == null || objValue.toString().equals("")) {
            return defaultValue;
        } else {
            String str = objValue.toString();
            Timestamp tsRV = null;
            try {
                SimpleDateFormat sdf = (SimpleDateFormat) SimpleDateFormat
                        .getDateInstance(DateFormat.SHORT);
                sdf.setLenient(false);
                sdf.applyPattern("yyyy-MM-dd");
                tsRV = new Timestamp(sdf.parse(str).getTime());
            } catch (Exception ex) {
                return defaultValue;
            }
            return tsRV;
        }
    }

    public static Date toDate(Object objValue, String format) {
        if (objValue == null || objValue.toString().equals("")) {
            return null;
        } else {
            String str = objValue.toString();
            Date tsRV = null;
            try {
                SimpleDateFormat sdf = (SimpleDateFormat) SimpleDateFormat
                        .getDateInstance(DateFormat.SHORT);
                sdf.setLenient(false);
                sdf.applyPattern(format);
                tsRV = new Date(sdf.parse(str).getTime());
            } catch (Exception ex) {
                return null;
            }
            return tsRV;
        }
    }
    
    public static Timestamp toDateTime(Object objValue) {
        return toDateTime(objValue, new Timestamp(System.currentTimeMillis()));
    }

    public static Boolean toBoolean(Object objValue, Boolean defaultValue) {
        if (objValue == null) {
            return defaultValue;
        } else {
            try {
                return new Boolean(objValue.toString());
            } catch (Exception ex) {
                return defaultValue;
            }

        }
    }

    public static Boolean toBoolean(Object objValue) {
        return toBoolean(objValue, Boolean.FALSE);
    }

    public static boolean toBool(Object objValue, boolean defaultValue) {
        if (objValue == null) {
            return defaultValue;
        } else {
            return toBoolean(objValue, Boolean.valueOf(defaultValue))
                    .booleanValue();
        }
    }

    public static boolean toBool(Object objValue) {
        return toBool(objValue, false);
    }
    
    public static String lpad(String s, int n, String replace) {   
        while (s.length() < n) {   
            s = replace+s;   
        }   
        return s;   
    }   
    public static String rpad(String s, int n, String replace) {   
        while (s.length() < n) {   
            s = s+replace;   
        }   
        return s;   
    } 
}