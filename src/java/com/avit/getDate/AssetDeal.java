package com.avit.getDate;

public class AssetDeal {
	
	/**
	 * 根据类型获取类型中文名
	 * @param genre
	 * @return
	 */
	public static String getGenreName(String genre){
		
		if("1".equals(genre)){
			return "电影 - 动作";
		}else if("2".equals(genre)){
			return "电影 - 成人";
		}else if("3".equals(genre)){
			return "电影 - 少儿";
		}else if("4".equals(genre)){
			return "电影 - 经典";
		}else if("5".equals(genre)){
			return "电影 - 喜剧";
		}else if("6".equals(genre)){
			return "电影 - 戏剧";
		}else if("7".equals(genre)){
			return "电影 - 家庭";
		}else if("8".equals(genre)){
			return "电影 - 外语";
		}else if("9".equals(genre)){
			return "电影 - 惊悚";
		}else if("10".equals(genre)){
			return "电影 - 科幻";
		}else if("11".equals(genre)){
			return "电影 - 西部";
		}else if("12".equals(genre)){
			return "电视剧";
		}else if("13".equals(genre)){
			return "电影";
		}else if("14".equals(genre)){
			return "科教";
		}else if("15".equals(genre)){
			return "少儿";
		}else if("16".equals(genre)){
			return "新闻综合";
		}else if("17".equals(genre)){
			return "纪实";
		}else if("18".equals(genre)){
			return "谈话";
		}else if("19".equals(genre)){
			return "文娱";
		}else if("20".equals(genre)){
			return "文娱 - 音乐";
		}else if("21".equals(genre)){
			return "文娱 - 戏曲";
		}else if("22".equals(genre)){
			return "文娱 - 综艺";
		}else if("23".equals(genre)){
			return "体育";
		}else if("24".equals(genre)){
			return "财经";
		}else if("25".equals(genre)){
			return "法制";
		}else if("26".equals(genre)){
			return "生活资讯";
		}else if("27".equals(genre)){
			return "其他";
		}else if("28".equals(genre)){
			return "文化";
		}else if("29".equals(genre)){
			return "卡通";
		}else if("30".equals(genre)){
			return "电影 - 情感";
		}else if("31".equals(genre)){
			return "电影 - 战争";
		}else if("32".equals(genre)){
			return "电影 - 纪实";
		}else if("33".equals(genre)){
			return "电影 - 古装";
		}else if("34".equals(genre)){
			return "电视剧 - 偶像";
		}else if("35".equals(genre)){
			return "电视剧 - 喜剧";
		}else if("36".equals(genre)){
			return "电视剧 - 生活";
		}else if("37".equals(genre)){
			return "电视剧 - 少儿";
		}else if("38".equals(genre)){
			return "电视剧 - 古装";
		}else if("39".equals(genre)){
			return "电视剧 - 历史";
		}else if("40".equals(genre)){
			return "电视剧 - 战争";
		}else if("41".equals(genre)){
			return "电视剧 - 警匪";
		}else if("42".equals(genre)){
			return "电视剧 - 主旋律";
		}else if("43".equals(genre)){
			return "电影 - 犯罪";
		}else if("44".equals(genre)){
			return "科学 - 自然";
		}else if("45".equals(genre)){
			return "科学 - 文化";
		}else if("46".equals(genre)){
			return "教育";
		}
		return genre;
	}

}
