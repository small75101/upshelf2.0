package com.avit.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 汉语转拼音
 * <b>不支持多音字的处理</b>
 * @author 双向业务
 */
public class Pinyin4jUtil {
	
	private static Log log = LogFactory.getLog(Pinyin4jUtil.class);
	
	/**
	 * 拼音简写(首字母)
	 * @param chinese 需要转换的字符串
	 */
	public static String cn2FirstSpell(String chinese) {
		StringBuffer pybf = new StringBuffer();
		char[] arr = chinese.toCharArray();
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] > 128) {
				try {
					String[] _t = PinyinHelper.toHanyuPinyinStringArray(arr[i], defaultFormat); 
					if (_t != null) {
						pybf.append(_t[0].charAt(0));
					}
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					log.error(" Pinying4j format error ... ", e);
				}
			} else {
				pybf.append(arr[i]);
			}
		}
		return pybf.toString().replaceAll("\\W", "").trim();
	} 
	
	/**
	 * 全拼(大写)
	 * @param chinese 需要转换的字符串
	 */
	public static String cn2Spell(String chinese) {
		StringBuffer pybf = new StringBuffer();
		char[] arr = chinese.toCharArray();
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);//返回不带音调数字
		//defaultFormat.setToneType(HanyuPinyinToneType.WITH_TONE_NUMBER);//返回带有音调 ZHONG1HUA2REN2MIN2GONG4HE2GUO2
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] > 128) {
				try {
					String[] strArr = PinyinHelper.toHanyuPinyinStringArray(arr[i], defaultFormat);
					if (strArr != null && strArr.length > 0) {
						pybf.append(strArr[0]);
					}
					//pybf.append(PinyinHelper.toHanyuPinyinStringArray(arr[i], defaultFormat)[0]);
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					log.error(" Pinying4j format error ... ", e);
				}
			} else {
				pybf.append(arr[i]);
			} 
		} 
		return pybf.toString();
	} 

	public static void main(String[] args) throws Exception {
//		String x = "中华人民共和国";
//		String x = "阿：胶";//多音字测试
//		String x = "蝙蝠侠前传2：黑暗骑士";
//		String x = null;
//		System.out.println(cn2FirstSpell(x));//首字母
//		System.out.println(cn2Spell(x));//全拼
	} 

}
