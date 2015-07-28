package com.gta.cluster;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
        DBScan dbscan = new DBScan(0.0001, 3, 3);
        String s1 = "习近平9月将访美 并将首次在联合国大会演讲";
        String s2 = "证监会针对27日集中抛售股票等有关线索进场核查";
        String s3 = "长虹公司纪委书记实名举报董事长滥用职权";
        String s4 = "朝鲜大使回应金正恩是否参加抗战胜利70周年阅兵";
        dbscan.addDataNode(s1);
        dbscan.addDataNode(s2);
        dbscan.addDataNode(s3);
        dbscan.addDataNode(s4);
        dbscan.analysis();
	}

}
