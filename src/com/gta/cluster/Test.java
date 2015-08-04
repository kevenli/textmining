package com.gta.cluster;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
        
        String []s = new String[16];
        
        s[0] = "内蒙古阿拉善左旗发生3.0级地震，震源深度1千米";
        s[1] = "内蒙古阿拉善左旗发生3.0级地震，震源深度0千米";
        s[2] = "云南景洪市发生3.2级地震，震源深度5千米";
        s[3] = "新疆皮山县发生3.3级地震，震源深度10千米";
        s[4] = "新疆皮山县发生4.2级地震，震源深度10千米";
        s[5] = "新疆皮山县发生3.3级地震，震源深度10千米（图）";
        s[6] = "新疆皮山县发生3.6级地震，震源深度10千米";
        s[7] = "菲律宾群岛发生4.9级地震，震源深度20千米";
        s[8] = "甘肃定西县发生4.9级地震，震源深度11千米";
        s[9] = "新疆皮山县发生3.3级地震，震源深度10千米（图）";
        s[10] = "西藏那曲地区尼玛县发生3.7级地震，震源深度6千米";
        s[11] = "西藏尼玛县发生3.7级地震，震源深度6千米";
        s[12] = "山东烟台附近海域发生4.0级地震，震源深度5千米";
        s[13] = "山东烟台附近海域发生4.0级地震，震源深度5千米";
        s[14] = "西藏丁青县发生3.1级地震，震源深度7千米";
        s[15] = "新疆皮山县发生4.6级地震，震源深度10千米";
        
/*
        DBScan dbscan = new DBScan(0.75, 3, 10);
        for (int i = 0; i < s.length; i++)
        {
        	dbscan.addDataNode(s[i]);
        }
        dbscan.analysis();
*/      

        
        OPTICS opt = new OPTICS(0.75, 3);
        for (int i = 0; i < s.length; i++)
        {
        	opt.addPoint(s[i]);
        }
        opt.analysis();
      
	}

}
