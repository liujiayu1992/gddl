package com.zhiren.dc.hesgl.changnzf;

import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.Date;

import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.Money;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

/**
 * @author 尹佳明
 * 2009-06-24
 */

public class Zafjsd extends BasePage{
	
	private int _CurrentPage = -1;

	public int getCurrentPage() {
		return _CurrentPage;
	}

	public void setCurrentPage(int _value) {
		_CurrentPage = _value;
	}

	private int _AllPages = -1;

	public int getAllPages() {
		return _AllPages;
	}

	public void setAllPages(int _value) {
		_AllPages = _value;
	}
	
	public Zafjsd(){
		
	}

	public String getZafjsd(String where,int iPageIndex,String tables){
		
		JDBCcon cn = new JDBCcon();
		Report rt = new Report();
		
		try{
			 String type="";	// 结算单类型，"ZGDT"为中国大唐，"GD"为国电
			 long lgdiancxxb_id = 0;
			 String strhej_dx = ""; // 大写合计
			 String strhej_xx = ""; // 小写合计
			 String stryansrq = ""; // 验收日期
			 String stryinhzh = ""; // 银行账号
			 String strkaihyh = ""; // 开启银行
			 String strbianh = ""; // 编号
			 String strjiesrq = ""; // 结算日期
			 String strgongysmc = ""; // 供应商名称
			 String strfahrq = "";
			 String shuil = "";
			 String jiessl = "";
			 String danj = "";
			 String buhsdj = "";
			 String buhszf = "";
			 String zafsk = "";
			 String hej = "";
			 String shoukdw_id = "";
			 String quanc = "";
			 String qiszyfw = "";
			 String jiezzyfw = "";
			 String beiz = "";
			 
			 String sql = "select * from zafjsb where bianm = '" + where + "'";
			 ResultSet rs = cn.getResultSet(sql); 
			 if(rs.next()){
				 
				 lgdiancxxb_id = rs.getLong("diancxxb_id");
				 strbianh = rs.getString("bianm");
				 strjiesrq = FormatDate(rs.getDate("riq"));
				 if ((rs.getString("gongysmc") != null)) {
					 strgongysmc = rs.getString("gongysmc");
				 }
				 jiessl = rs.getString("jiessl");
				 danj = rs.getString("danj");
				 buhszf = rs.getString("buhszf");
				 zafsk = rs.getString("zafsk");
				 hej = rs.getString("hej");
				 shoukdw_id = rs.getString("shoukdw_id");
				 qiszyfw = rs.getString("qiszyfw");
				 jiezzyfw = rs.getString("jiezzyfw");
				 beiz = rs.getString("beiz");
				 
				 if (qiszyfw.equals(jiezzyfw)) {
					 strfahrq = FormatDate(rs.getDate("qiszyfw"));
				 } else {
					 strfahrq = FormatDate(rs.getDate("qiszyfw"))+" 至 "+FormatDate(rs.getDate("jiezzyfw"));
				 }

				 if(qiszyfw.equals(jiezzyfw)){
					 stryansrq = FormatDate(rs.getDate("qiszyfw"));
				 }else{
					 stryansrq = FormatDate(rs.getDate("qiszyfw"))+" 至 "+FormatDate(rs.getDate("jiezzyfw"));
				 }
				 
				 String shoukdwSql = "select * from shoukdw where id = " + shoukdw_id;
				 ResultSet rsl = cn.getResultSet(shoukdwSql);
				 rsl.next();
				 quanc = rsl.getString("quanc");
				 
				 strkaihyh = rsl.getString("kaihyh");
				 stryinhzh = rsl.getString("zhangh");

//				 不含税单价
				 buhsdj = Math.round(Double.parseDouble(buhszf) / Double.parseDouble(jiessl) * 10000000)/10000000.0+"";
//				 税率
				 shuil = Math.round((1 - Double.parseDouble(buhszf) / Double.parseDouble(hej))*100)/100.0+"";

			 }

			 Money money = new Money();
			 
			 strhej_xx = format(Double.parseDouble(hej),"0.00");
			 strhej_dx = money.NumToRMBStr(Double.parseDouble(hej));
			 
			 rs.close();
			 cn.Close();
			 
			 type = MainGlobal.getXitxx_item("结算", "结算单所属单位", String.valueOf(lgdiancxxb_id), "ZGDT");
			 if(type.equals("ZGDT")){
				 
				 int ArrWidth[]=new int[] {125,70,65,65,65,65,75,70,85,75,78,65};
				 
				 String ArrHeader[][]=new String[19][12];
				 ArrHeader[0] = new String[] {"供货单位:"+strgongysmc,"","","发站:","","代表车号:","","","收款单位:"+quanc,"","",""};
				 ArrHeader[1] = new String[] {"发货日期:"+strfahrq,"","","地区代码:","","原收货人:","","","开户银行:"+strkaihyh,"","",""};
				 ArrHeader[2] = new String[] {"验收日期:"+stryansrq,"","","货物名称:","","现收货人:","","","银行帐号:"+stryinhzh,"","",""};
				 ArrHeader[3] = new String[] {"发运数量(吨):"+jiessl,"","车数:","验收编号:","","发票编号:","","","兑付地点:","","付款方式:",""};
				 ArrHeader[4] = new String[] {"质量验收","质量验收","质量验收","质量验收","质量验收","质量验收","质量验收","质量验收","数量验收","","",""};
				 ArrHeader[5] = new String[] {"含税价:"+danj+"(元)","合同标准","供方标准","厂方验收","结算标准","相差数量","折价标准","折合金额","供方数量","验收数量","亏吨数量","折合金额"};
				 ArrHeader[6] = new String[] {""+Locale.Qnetar_zhibb+"("+Locale.qiankmqk_danw+")","","","","","","","","(吨)","(吨)","(吨)","(元)"};
				 ArrHeader[7] = new String[] {""+Locale.Std_zhibb+"("+Locale.baifb_danw+")","","","","","","","","","","",""};
				 ArrHeader[8] = new String[] {"结算数量","单价","金额","","补(扣)以前价款","补(扣)以前价款","价款合计","","税率","税款","价税合计","价税合计"};
				 ArrHeader[9] = new String[] {jiessl,buhsdj,formatq(buhszf),"","","",formatq(buhszf),"",shuil,zafsk,formatq(hej),""};
				 ArrHeader[10] = new String[] {"煤款合计(大写):","","","","","","","","","","",""};
				 ArrHeader[11] = new String[] {"铁路运费","铁路杂费","矿区运费","矿区杂费","补(扣)以前运杂费","补(扣)以前运杂费","不含税运费","","税率","税款","运杂费合计","运杂费合计"};
				 ArrHeader[12] = new String[] {"","","","","","","","","","","","151546.4"};
				 ArrHeader[13] = new String[] {"运杂费合计(大写):","","","","","","","","","","",""};
				 ArrHeader[14] = new String[] {"合计(大写):",strhej_dx,"","","","","","","合计(小写):",strhej_xx,"",""};
				 ArrHeader[15] = new String[] {"备注:",beiz,"","","","","","","过衡重量(吨):","",""+Locale.jiesslcy_title+"",""};
				 ArrHeader[16] = new String[] {"电厂燃料部门:(盖章)","","电厂财务部门:(盖章)","","","质量监督处:(签章)","","领导审批:(签章)","","综合财务处:(签章)","",""};
				 ArrHeader[17] = new String[] {"经办人:","","经办人:","","","经办人:","","","","经办人:","",""};
				 ArrHeader[18] = new String[] {"","","","","","","","","","","",""};
				 
//				 定义页Title
				 rt.setTitle(Locale.jiesd_title,ArrWidth);
				 String tianbdw = getTianzdw(lgdiancxxb_id); // 填制单位。（可根据条件来填入单位）
				 rt.setDefaultTitleLeft("填制单位："+tianbdw,3);
				 rt.setDefaultTitle(4,5,"结算日期："+strjiesrq,Table.ALIGN_CENTER);
				 rt.setDefaultTitle(9,4,"编号:"+strbianh,Table.ALIGN_RIGHT);
				 rt.setBody(new Table(ArrHeader,0,0,0));
				 rt.body.setWidth(ArrWidth);	
				 
				 rt.body.mergeCell(1,1,1,3);
				 rt.body.mergeCell(1,7,1,8);
				 rt.body.mergeCell(1,9,1,12);
				 rt.body.setCells(1,4,1,4,Table.PER_BORDER_RIGHT,0);
				 rt.body.setCells(1,6,1,6,Table.PER_BORDER_RIGHT,0);
				 
				 rt.body.mergeCell(2,1,2,3);
				 rt.body.mergeCell(2,7,2,8);
				 rt.body.mergeCell(2,9,2,12);
				 rt.body.setCells(2,4,2,4,Table.PER_BORDER_RIGHT,0);
				 rt.body.setCells(2,6,2,6,Table.PER_BORDER_RIGHT,0);
				 
				 rt.body.mergeCell(3,1,3,3);
				 rt.body.mergeCell(3,7,3,8);
				 rt.body.mergeCell(3,9,3,12);
				 rt.body.setCells(3,4,3,4,Table.PER_BORDER_RIGHT,0);
				 rt.body.setCells(3,6,3,6,Table.PER_BORDER_RIGHT,0);
				 
				 rt.body.mergeCell(4,7,4,8);
				 rt.body.setCells(4,4,4,4,Table.PER_BORDER_RIGHT,0);
				 rt.body.setCells(4,6,4,6,Table.PER_BORDER_RIGHT,0);
				 rt.body.setCells(4,9,4,9,Table.PER_BORDER_RIGHT,0);
				 rt.body.setCells(4,11,4,11,Table.PER_BORDER_RIGHT,0);
				 
				 rt.body.mergeCell(5,1,5,8);
				 rt.body.mergeCell(5,9,5,12);
				 
				 rt.body.mergeCell(9,3,9,4);
				 rt.body.mergeCell(9,5,9,6);
				 rt.body.mergeCell(9,7,9,8);
				 rt.body.mergeCell(9,11,9,12);
				 
				 rt.body.mergeCell(10,3,10,4);
				 rt.body.mergeCell(10,5,10,6);
				 rt.body.mergeCell(10,7,10,8);
				 rt.body.mergeCell(10,11,10,12);
				 
				 rt.body.mergeCell(12,5,12,6);
				 rt.body.mergeCell(12,7,12,8);
				 rt.body.mergeCell(12,11,12,12);
			 
				 rt.body.mergeCell(13,5,13,6);
				 rt.body.mergeCell(13,7,13,8);
				 rt.body.mergeCell(13,11,13,12);
				 
				 rt.body.mergeCell(14,2,14,12); // 运费合计大写
				 rt.body.mergeCell(15,2,15,8);
				 rt.body.mergeCell(15,10,15,12);
				 
				 rt.body.mergeCell(16,2,16,8);
				 rt.body.mergeCell(17,1,17,2);
				 rt.body.mergeCell(17,3,17,5);
				 rt.body.mergeCell(17,6,17,7);
				 rt.body.mergeCell(17,8,17,9);
				 rt.body.mergeCell(17,10,17,12);
				 
				 rt.body.mergeCell(18,1,18,2);
				 rt.body.mergeCell(18,3,18,5);
				 rt.body.mergeCell(18,6,18,7);
				 rt.body.mergeCell(18,8,18,9);
				 rt.body.mergeCell(18,10,18,12);
				 
				 rt.body.mergeCell(19,1,19,2);
				 rt.body.mergeCell(19,3,19,5);
				 rt.body.mergeCell(19,6,19,7);
				 rt.body.mergeCell(19,8,19,9);
				 rt.body.mergeCell(19,10,19,12);
				 
				 rt.body.setCells(4,1,4,1,Table.PER_BORDER_RIGHT,0);
				 rt.body.setCells(4,2,4,2,Table.PER_BORDER_RIGHT,0);
				 
				 rt.body.setCells(5, 1, 12, 12, Table.PER_ALIGN, Table.ALIGN_CENTER);
				 rt.body.setCells(6,1,11,1,Table.PER_ALIGN,Table.ALIGN_LEFT);
				 rt.body.setCells(9,1,9,12,Table.PER_ALIGN,Table.ALIGN_CENTER);
				 rt.body.setCells(12,1,12,12,Table.PER_ALIGN,Table.ALIGN_CENTER);
				 rt.body.setCells(13,1,13,12,Table.PER_ALIGN,Table.ALIGN_CENTER);
				 rt.body.setRowCells(8,Table.PER_ALIGN,Table.ALIGN_CENTER);
				 rt.body.setRowCells(17,Table.PER_BORDER_BOTTOM,0);
				 rt.body.setRowCells(18,Table.PER_BORDER_BOTTOM,0);
				 rt.body.setRowCells(18,Table.PER_ALIGN,Table.ALIGN_CENTER);
				 rt.body.setRowCells(19,Table.PER_ALIGN,Table.ALIGN_RIGHT);
				 
				 rt.body.mergeCell(11,2,11,12);	// 煤款合计大写
				 rt.body.setCells(11,12,11,12,Table.PER_BORDER_RIGHT,1);
				 rt.body.setCells(11,1,11,1,Table.PER_ALIGN,Table.ALIGN_LEFT);
				 rt.body.setCells(11,2,11,2,Table.PER_ALIGN,Table.ALIGN_LEFT);
				 rt.body.setCells(10,1,10,1,Table.PER_ALIGN,Table.ALIGN_CENTER);
				 
			 }else if(type.equals("GD")){ // 国电
				 
				 String tianbdw = getTianzdw(lgdiancxxb_id); // 填制单位。（可根据条件来填入单位）
				 
				 String ArrHeader[][]=new String[7][19];
				 ArrHeader[0]=new String[] {"","","","","","","","","","","","","","","","","","",""};
				 ArrHeader[1]=new String[] {"","","","","","","","","","","","","","","","","","",""};
				 ArrHeader[2]=new String[] {"国 电 电 力 发 展 股 份 有 限 公 司","国 电 电 力 发 展 股 份 有 限 公 司","国 电 电 力 发 展 股 份 有 限 公 司","国 电 电 力 发 展 股 份 有 限 公 司","国 电 电 力 发 展 股 份 有 限 公 司","国 电 电 力 发 展 股 份 有 限 公 司","国 电 电 力 发 展 股 份 有 限 公 司","国 电 电 力 发 展 股 份 有 限 公 司","国 电 电 力 发 展 股 份 有 限 公 司","国 电 电 力 发 展 股 份 有 限 公 司","国 电 电 力 发 展 股 份 有 限 公 司","国 电 电 力 发 展 股 份 有 限 公 司","国 电 电 力 发 展 股 份 有 限 公 司","国 电 电 力 发 展 股 份 有 限 公 司","国 电 电 力 发 展 股 份 有 限 公 司","国 电 电 力 发 展 股 份 有 限 公 司","国 电 电 力 发 展 股 份 有 限 公 司","国 电 电 力 发 展 股 份 有 限 公 司","国 电 电 力 发 展 股 份 有 限 公 司"};
				 ArrHeader[3]=new String[] {"SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD."};
				 ArrHeader[4]=new String[] {"燃  料  （ 煤 炭 ）  结  算  单","燃  料  （ 煤 炭 ）  结  算  单","燃  料  （ 煤 炭 ）  结  算  单","燃  料  （ 煤 炭 ）  结  算  单","燃  料  （ 煤 炭 ）  结  算  单","燃  料  （ 煤 炭 ）  结  算  单","燃  料  （ 煤 炭 ）  结  算  单","燃  料  （ 煤 炭 ）  结  算  单","燃  料  （ 煤 炭 ）  结  算  单","燃  料  （ 煤 炭 ）  结  算  单","燃  料  （ 煤 炭 ）  结  算  单","燃  料  （ 煤 炭 ）  结  算  单","燃  料  （ 煤 炭 ）  结  算  单","燃  料  （ 煤 炭 ）  结  算  单","燃  料  （ 煤 炭 ）  结  算  单","燃  料  （ 煤 炭 ）  结  算  单","燃  料  （ 煤 炭 ）  结  算  单","燃  料  （ 煤 炭 ）  结  算  单","燃  料  （ 煤 炭 ）  结  算  单"};
				 ArrHeader[5]=new String[] {"","","","","","","","","","","","","","","","","","",""};
				 ArrHeader[6]=new String[] {"单位："+tianbdw,"单位："+tianbdw,"单位："+tianbdw,"单位："+tianbdw,"单位："+tianbdw,"单位："+tianbdw,"单位："+tianbdw,"日期："+strjiesrq,"日期："+strjiesrq,"日期："+strjiesrq,"单位：吨,元/吨,MJ/kg,%,元","单位：吨,元/吨,MJ/kg,%,元","单位：吨,元/吨,MJ/kg,%,元","单位：吨,元/吨,MJ/kg,%,元","单位：吨,元/吨,MJ/kg,%,元","单位：吨,元/吨,MJ/kg,%,元","编号："+strbianh,"编号："+strbianh,"编号："+strbianh};

				 String ArrBody[][]=new String[19][19];
				 ArrBody[0]=new String[] {"结算部门：燃料部","","","","供货单位："+strgongysmc,"","","","供货地区：","","","","计划渠道：","","","","品种：","",""};
				 ArrBody[1]=new String[] {"数量","","","","","单价","","","","","","","","","","","煤款","","税金"};
				 ArrBody[2]=new String[] {"票重","","","","","","扣款","","","","","","","单价合计","不含税价","","","",""};
				 ArrBody[3]=new String[] {"车数","数量","盈吨","亏吨","实收","煤价","热值","灰分","挥发分","水分","硫量","其他","小计","","","","","",""};
				 ArrBody[4]=new String[] {"",jiessl,"","","","","","","","","","","",danj,buhsdj,"",""+formatq(buhszf)+"","",""+formatq(zafsk)+""};
				 ArrBody[5]=new String[] {"热值","灰分","挥发分","水分","硫量","应付价款","","应付税金","","其他扣款","","实付金额","","","","","","",""};
				 ArrBody[6]=new String[] {"","","","","","","",""+formatq(zafsk)+"","","","",""+formatq(hej)+"","","","","","","",""};
				 ArrBody[7]=new String[] {"运距(km)","","运费单价明细","","","","","","","","","","","","","","","","印花税"};
				 ArrBody[8]=new String[] {"国铁","地铁","国铁","地铁","矿运","专线","短运","汽运","其他运费","","","","","","运杂费(元/车)","","","",""};
				 ArrBody[9]=new String[] {"","","","","","","","","电附加","风沙","储装","道口","其它","小计","取送车","变更费","其它","小计",""};
				 ArrBody[10]=new String[] {"","","","","","","","","","","","","","","","","","",""};
				 ArrBody[11]=new String[] {"国铁运费","","地铁运费","","矿区运费","","专线运费","","短途运费","","汽车运费","","其他运费","","运杂费","","扣款","","实付运费金额"};
				 ArrBody[12]=new String[] {"","","","","","","","","","","","","","","","","复检费","其他",""};
				 ArrBody[13]=new String[] {"","","","","","","","","","","","","","","","","","",""};
				 ArrBody[14]=new String[] {"注：根据本企业资金管理制度权限履行会审及审批程序，会审部门及审批人写明意见、签名及日期。","","","","","","","","","","","","","","","","","",""};
				 ArrBody[15]=new String[] {"","","","","","","","","","","","","","","","","","",""};
				 ArrBody[16]=new String[] {"公司(厂)分管领导：","","","","","燃料部负责人：","","","","质价审核：","","","数量审核：","","","","制表：","",""};
				 ArrBody[17]=new String[] {"","","","","","","","","","","","","","","","","","",""};
				 ArrBody[18]=new String[] {"总会计师：","","","","","会计机构负责人：","","","","财务审核：","","","付款：","","","","结算人：","",""};

				 int ArrWidth[]=new int[] {54,54,54,54,54,54,54,54,54,54,54,54,54,54,54,54,54,54,54};
				 
//				 定义页Title
				 rt.setTitle(new Table(ArrHeader,0,0,0));
				 rt.setBody(new Table(ArrBody,0,0,0));
				 rt.body.setWidth(ArrWidth);
				 rt.title.setWidth(ArrWidth);
//				 合并单元格
//				 表头_Begin
				 rt.title.merge(2, 1, 2, 19);
				 rt.title.merge(3, 1, 3, 19);
				 rt.title.merge(4, 1, 4, 19);
				 rt.title.merge(5, 1, 5, 19);
				 rt.title.merge(6, 1, 6, 19);
				 
				 rt.title.merge(7, 1, 7, 7);
				 rt.title.merge(7, 8, 7, 10);
				 rt.title.merge(7, 11, 7, 16);
				 rt.title.merge(7, 17, 7, 19);
				 
				 rt.title.setBorder(0,0,0,0);
				 rt.title.setRowCells(1,Table.PER_BORDER_BOTTOM,0);
				 rt.title.setRowCells(2,Table.PER_BORDER_BOTTOM,0);
				 rt.title.setRowCells(3,Table.PER_BORDER_BOTTOM,0);
				 rt.title.setRowCells(4,Table.PER_BORDER_BOTTOM,0);
				 rt.title.setRowCells(5,Table.PER_BORDER_BOTTOM,0);
				 rt.title.setRowCells(6,Table.PER_BORDER_BOTTOM,0);
				 rt.title.setRowCells(7,Table.PER_BORDER_BOTTOM,0);
				 
				 rt.title.setRowCells(1,Table.PER_BORDER_RIGHT,0);
				 rt.title.setRowCells(2,Table.PER_BORDER_RIGHT,0);
				 rt.title.setRowCells(3,Table.PER_BORDER_RIGHT,0);
				 rt.title.setRowCells(4,Table.PER_BORDER_RIGHT,0);
				 rt.title.setRowCells(5,Table.PER_BORDER_RIGHT,0);
				 rt.title.setRowCells(6,Table.PER_BORDER_RIGHT,0);
				 rt.title.setRowCells(7,Table.PER_BORDER_RIGHT,0);
				 
				 rt.title.setRowCells(2, Table.PER_ALIGN, Table.ALIGN_CENTER);
				 rt.title.setRowCells(3, Table.PER_ALIGN, Table.ALIGN_CENTER);
				 rt.title.setRowCells(4, Table.PER_ALIGN, Table.ALIGN_CENTER);
				 rt.title.setRowCells(5, Table.PER_ALIGN, Table.ALIGN_CENTER);
				 rt.title.setRowCells(6, Table.PER_ALIGN, Table.ALIGN_CENTER);
				 rt.title.setRowCells(7, Table.PER_ALIGN, Table.ALIGN_LEFT);
				 
//				 字体
				 rt.title.setCells(3, 1, 3, 19, Table.PER_FONTNAME, "黑体");
				 rt.title.setCells(3, 1, 3, 19, Table.PER_FONTSIZE, 11);
				 rt.title.setCells(4, 1, 4, 19, Table.PER_FONTNAME, "Arial Unicode MS");
				 rt.title.setCells(4, 1, 4, 19, Table.PER_FONTSIZE, 12);
				 rt.title.setCells(5, 1, 5, 19, Table.PER_FONTNAME, "隶书");
				 rt.title.setCells(5, 1, 5, 19, Table.PER_FONTSIZE, 20);
//				 字体				 
				 
//				 图片
				 rt.title.setCellImage(2, 1, 120, 60, "imgs/report/GDBZ.gif");	//国电的标志（到现场要一个换上就行了）
				 rt.title.setRowCells(2, Table.PER_ALIGN, Table.ALIGN_LEFT);
				 rt.title.setCellImage(6, 1, rt.title.getWidth()/3+30, 10, "imgs/report/GDHX.gif");
//				 图片_End
				 
//				 表头_End
//				 表体_Begin
				 rt.body.mergeCell(1,1,1,4);
				 rt.body.mergeCell(1,5,1,8);
				 rt.body.mergeCell(1,9,1,12);
				 rt.body.mergeCell(1,13,1,16);
				 rt.body.mergeCell(1,17,1,19);
				 
				 rt.body.setRowCells(1, Table.PER_ALIGN, Table.ALIGN_LEFT);
				 rt.body.setCells(1,17,1,17,Table.PER_BORDER_RIGHT,2);
				 
				 rt.body.mergeCell(2,1,2,5);
				 rt.body.mergeCell(2,6,2,16);
				 rt.body.mergeCell(2,17,4,18);
				 rt.body.mergeCell(2,19,4,19);
				 rt.body.setRowCells(2, Table.PER_ALIGN, Table.ALIGN_CENTER);
				 
				 rt.body.mergeCell(3,1,3,2);
				 rt.body.mergeCell(3,7,3,13);
				 rt.body.mergeCell(3,14,4,14);
				 rt.body.mergeCell(3,15,4,16);
				 rt.body.setRowCells(3, Table.PER_ALIGN, Table.ALIGN_CENTER);
				 
				 rt.body.setRowCells(4, Table.PER_ALIGN, Table.ALIGN_CENTER);
				 
				 rt.body.mergeCell(5,15,5,16);
				 rt.body.mergeCell(5,17,5,18);
				 rt.body.setRowCells(5, Table.PER_ALIGN, Table.ALIGN_CENTER);
				 
				 rt.body.mergeCell(6,6,6,7);
				 rt.body.mergeCell(6,8,6,9);
				 rt.body.mergeCell(6,10,6,11);
				 rt.body.mergeCell(6,12,6,14);
				 rt.body.mergeCell(6,15,6,16);
				 rt.body.mergeCell(6,17,6,18);
				 rt.body.setRowCells(6, Table.PER_ALIGN, Table.ALIGN_CENTER);
				 
				 rt.body.mergeCell(7,6,7,7);
				 rt.body.mergeCell(7,8,7,9);
				 rt.body.mergeCell(7,10,7,11);
				 rt.body.mergeCell(7,12,7,14);
				 rt.body.mergeCell(7,15,7,16);
				 rt.body.mergeCell(7,17,7,18);
				 rt.body.setRowCells(7, Table.PER_ALIGN, Table.ALIGN_CENTER);
				 
				 rt.body.mergeCell(8,1,8,2);
				 rt.body.mergeCell(8,3,8,18);
				 rt.body.mergeCell(8,19,10,19);
				 rt.body.setRowCells(8, Table.PER_ALIGN, Table.ALIGN_CENTER);
				 
				 rt.body.mergeCell(9,1,10,1);
				 rt.body.mergeCell(9,2,10,2);
				 rt.body.mergeCell(9,3,10,3);
				 rt.body.mergeCell(9,4,10,4);
				 rt.body.mergeCell(9,5,10,5);
				 rt.body.mergeCell(9,6,10,6);
				 rt.body.mergeCell(9,7,10,7);
				 rt.body.mergeCell(9,8,10,8);
				 rt.body.mergeCell(9,9,9,14);
				 rt.body.mergeCell(9,15,9,18);
				 rt.body.setRowCells(9, Table.PER_ALIGN, Table.ALIGN_CENTER);
				 
				 rt.body.setRowCells(10, Table.PER_ALIGN, Table.ALIGN_CENTER);
				 
				 rt.body.setRowCells(11, Table.PER_ALIGN, Table.ALIGN_CENTER);
				 
				 rt.body.mergeCell(12,1,13,2);
				 rt.body.mergeCell(12,3,13,4);
				 rt.body.mergeCell(12,5,13,6);
				 rt.body.mergeCell(12,7,13,8);
				 rt.body.mergeCell(12,9,13,10);
				 rt.body.mergeCell(12,11,13,12);
				 rt.body.mergeCell(12,13,13,14);
				 rt.body.mergeCell(12,15,13,16);
				 rt.body.mergeCell(12,17,12,18);
				 rt.body.mergeCell(12,19,13,19);
				 rt.body.setRowCells(12, Table.PER_ALIGN, Table.ALIGN_CENTER);
				 
				 rt.body.setRowCells(13, Table.PER_ALIGN, Table.ALIGN_CENTER);
				 
				 rt.body.mergeCell(14,1,14,2);
				 rt.body.mergeCell(14,3,14,4);
				 rt.body.mergeCell(14,5,14,6);
				 rt.body.mergeCell(14,7,14,8);
				 rt.body.mergeCell(14,9,14,10);
				 rt.body.mergeCell(14,11,14,12);
				 rt.body.mergeCell(14,13,14,14);
				 rt.body.mergeCell(14,15,14,16);
				 rt.body.setRowCells(14, Table.PER_ALIGN, Table.ALIGN_CENTER);
				 
				 rt.body.mergeCell(15,1,15,19);
				 rt.body.setRowCells(15, Table.PER_BORDER_BOTTOM, 2);
				 rt.body.setCells(15,1,15,1,Table.PER_BORDER_RIGHT,2);
				 
				 rt.body.mergeCell(16,1,16,19);
				 rt.body.setRowHeight(16,8);
				 rt.body.setRowCells(16, Table.PER_BORDER_BOTTOM, 0);
				 rt.body.setRowCells(16, Table.PER_BORDER_RIGHT, 0);
				 
				 rt.body.setBorder(0, 0, 2, 0);
				 rt.body.setCells(1, 1, 15, 1, Table.PER_BORDER_LEFT, 2);
				 rt.body.setCells(1, 19, 15, 19, Table.PER_BORDER_RIGHT, 2);
				 
				 rt.body.mergeCell(17,1,17,5);
				 rt.body.mergeCell(17,6,17,9);
				 rt.body.mergeCell(17,10,17,12);
				 rt.body.mergeCell(17,13,17,16);
				 rt.body.mergeCell(17,17,17,19);
				 rt.body.setRowHeight(17,5);
				 rt.body.setRowCells(17, Table.PER_BORDER_BOTTOM, 0);
				 rt.body.setRowCells(17, Table.PER_BORDER_RIGHT, 0);
				 
				 rt.body.mergeCell(18,1,18,19);
				 rt.body.setRowHeight(18,0);
				 rt.body.setRowCells(18, Table.PER_BORDER_BOTTOM, 0);
				 rt.body.setRowCells(18, Table.PER_BORDER_RIGHT, 0);
				 
				 rt.body.mergeCell(19,1,19,5);
				 rt.body.mergeCell(19,6,19,9);
				 rt.body.mergeCell(19,10,19,12);
				 rt.body.mergeCell(19,13,19,16);
				 rt.body.mergeCell(19,17,19,19);
				 rt.body.setRowHeight(19,5);
				 rt.body.setRowCells(19, Table.PER_BORDER_BOTTOM, 0);
				 rt.body.setRowCells(19, Table.PER_BORDER_RIGHT, 0);
				 
			 }
			 
//			设置页数
			_CurrentPage = 1;
			_AllPages = 1;
			if (_AllPages == 0) {
				_CurrentPage = 0;
			}
			return rt.getAllPagesHtml(iPageIndex);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cn.Close();
		}
		return rt.getAllPagesHtml(iPageIndex);
	}
	
	public String getTianzdw (long diancxxb_id) {
		String Tianzdw = "";
		JDBCcon con = new JDBCcon();
		try{
			String sql = "select quanc from diancxxb where id = " + diancxxb_id;
			ResultSet rs = con.getResultSet(sql);
			if(rs.next()){
				Tianzdw = rs.getString("quanc");
			}
			rs.close();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.Close();
		}
		return Tianzdw;
	}
	
	private String FormatDate(Date _date) {
		if (_date == null) {
			return "";
		}
		return DateUtil.Formatdate("yyyy年MM月dd日", _date);
	}
	
	public String format(double dblValue,String strFormat){
		 DecimalFormat df = new DecimalFormat(strFormat);
		 return formatq(df.format(dblValue));
	}
	
	public String formatq(String strValue){ //加千位分隔符
		String strtmp="",xiaostmp="",tmp="";
		int i=3;
		if(strValue.lastIndexOf(".")==-1){
			strtmp=strValue;
			if(strValue.equals("")){
				xiaostmp="";
			}else{
				xiaostmp=".00";
			}
		}else {
			strtmp=strValue.substring(0,strValue.lastIndexOf("."));
			if(strValue.substring(strValue.lastIndexOf(".")).length()==2){
				xiaostmp=strValue.substring(strValue.lastIndexOf("."))+"0";
			}else{
				xiaostmp=strValue.substring(strValue.lastIndexOf("."));
			}
		}
		tmp=strtmp;
		
		while(i<tmp.length()){
			strtmp=strtmp.substring(0,strtmp.length()-(i+(i-3)/3))+","+strtmp.substring(strtmp.length()-(i+(i-3)/3),strtmp.length());
			i=i+3;
		}
		return strtmp+xiaostmp;
	}
}