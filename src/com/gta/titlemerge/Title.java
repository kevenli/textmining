package com.gta.titlemerge;

import java.util.List;

public class Title {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
/*		
		String str1 = "习近平考察杭州高新企业海康威视 谈爱护高端尖子人才";
		String str2 = "习近平考察杭州 谈爱护高端尖子人才";
*/
		String str1 = "习近平接见驻浙部队干部：贯彻强军目标用实劲";
		String str2 = "习近平在接见驻浙部队领导干部时强调 贯彻强军目标出实招用实劲 努力开创部队建设新局面";
		String str3 = "习近平：贯彻强军目标出实招用实劲 努力开创部队建设新局面";
		
		String str4 = "16省份出台户籍制度改革意见";
		String str5 = "16省份出台户籍制度改革意见 将落实居住证制度";
		String str6 = "M5W9开头假币再现长春街头 流通全国多地逾半年";
		String str7 = "M5W9开头百元假币现长春街头 流通全国多地逾半年";
		
		String str8 = "一律师求娶奥巴马大女儿 愿送50头牛作聘礼(图)";
		String str9 = "肯尼亚律师求娶奥巴马大女儿 愿送50头牛作聘礼";
		
		String str10 = "银监会：继续推动扩大民间资本进入银行业";
		String str11 = "银监会：将进一步推动民营资本进入银行业";
		
		String str12 = "北京检察机关决定对吴振立案侦查";
		String str13 = "北京检察机关决定对宗新华立案侦查";
		String str14 = "北京检察机关决定对李大川立案侦查"; 
		
		String str15 = "台湾居民明日起持台胞证可通关 无需办理签注";
		String str16 = "台湾居民明日起持台胞证可通关";
		String str17 = "明起台胞来京无需办理签注 电子台胞证年内实施";

		TitleMerge tm = new TitleMerge();
		List<TitleDict> td1 = tm.tokenizer(str16);
		List<TitleDict> td2 = tm.tokenizer(str17);
		if (tm.anslysisTerms(td1, td2) > 0)
		{
			System.out.println("match");
		}
		else 
		{
			System.out.println("false");
		}
			
	}
}
