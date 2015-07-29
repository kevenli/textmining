package com.gta.cluster;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
        DBScan dbscan = new DBScan(0.70, 3, 3);
        String s1 = "内蒙古阿拉善左旗发生3.0级地震，震源深度0千米";
        String s2 = "云南景洪市发生3.2级地震，震源深度5千米";
        String s3 = "新疆皮山县发生3.3级地震，震源深度10千米";
        String s4 = "新疆皮山县发生4.2级地震，震源深度10千米";
        String s5 = "新疆皮山县发生3.3级地震，震源深度10千米（图）";
        String s6 = "新疆皮山县发生3.6级地震，震源深度10千米";
        String s7 = "菲律宾群岛发生4.9级地震，震源深度20千米";
        String s8 = "甘肃定西县发生4.9级地震，震源深度11千米";
        String s9 = "新疆皮山县发生3.3级地震，震源深度10千米（图）";
        String s10 = "西藏那曲地区尼玛县发生3.7级地震，震源深度6千米";
        String s11 = "西藏尼玛县发生3.7级地震，震源深度6千米";
        String s12 = "山东烟台附近海域发生4.0级地震，震源深度5千米";
        String s13 = "山东烟台附近海域发生4.0级地震，震源深度5千米";
        String s14 = "西藏丁青县发生3.1级地震，震源深度7千米";
        String s15 = "新疆皮山县发生4.6级地震，震源深度10千米";
        
        dbscan.addDataNode(s1);
        dbscan.addDataNode(s2);
        dbscan.addDataNode(s3);
        dbscan.addDataNode(s4);
        dbscan.addDataNode(s5);
        dbscan.addDataNode(s6);
        dbscan.addDataNode(s7);
        dbscan.addDataNode(s8);
        dbscan.addDataNode(s9);
        dbscan.addDataNode(s10);
        dbscan.addDataNode(s11);
        dbscan.addDataNode(s12);
        dbscan.addDataNode(s13);
        dbscan.addDataNode(s14);
        dbscan.addDataNode(s15);
        dbscan.addDataNode(s15);
        dbscan.analysis();
	}

}
