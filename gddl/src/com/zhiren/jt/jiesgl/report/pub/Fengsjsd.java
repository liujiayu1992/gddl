package com.zhiren.jt.jiesgl.report.pub;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.tapestry.html.BasePage;

import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

/**
 * @author 尹佳明
 * 2009-09-03
 * 类名：Fengsjsd(分公司结算单)
 */

public class Fengsjsd extends BasePage {
	
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
	
	public Fengsjsd(){
	}
	
	public String getFengsjsd(String type, String bianm) {
		
		JDBCcon con =new JDBCcon();
		Report rt = new Report();
		
		if (type.equals("xiaosd")) { // 销售单
			
			String sql = 
				"select dcjsmk.fuid, dcjsmk.diancxxb_id, to_char(dcjsmk.jiesrq,'yyyy-MM-dd') jiesrq, dcjsmk.meikdwmc, dcjsmk.jiessl,\n" +
				"    round_new((dcjsmk.jiesrl * 4.1816 / 1000), 2) qnet_mj,\n" + 
				"    dcjsmk.jiesrl, max(dpcjsmx.mt) mt, max(dpcjsmx.ad) ad,\n" + 
				"    max(dpcjsmx.vdaf) vdaf, max(dpcjsmx.std) std, dcjsmk.hetj,\n" + 
				"    getJiesdzb('diancjsmkb', dcjsmk.id, 'Qnetar', 'zhejbz') qnetark,\n" + 
				"    getJiesdzb('diancjsmkb', dcjsmk.id, 'Std', 'zhejbz') stdk,\n" + 
				"    getJiesdzb('diancjsmkb', dcjsmk.id, 'Ad', 'zhejbz') adk,\n" + 
				"    (dcjsmk.hetj + nvl(getJiesdzb('diancjsmkb', dcjsmk.id, 'Qnetar', 'zhejbz'), 0) +\n" + 
				"    nvl(getJiesdzb('diancjsmkb', dcjsmk.id, 'Std', 'zhejbz'), 0) + nvl(getJiesdzb('diancjsmkb', dcjsmk.id, 'Ad', 'zhejbz'), 0)) xiaoj,\n" + 
				"    dcjsmk.buhsdj, dcjsmk.hansyf,\n" + 
				"    round_new((dcjsmk.hansyf * (1 - 0.07)), 2) buhsyf, round_new((dcjsmk.hansyf * (1 - 0.07) / dcjsmk.jiessl), 2) buhsdmyf,\n" + 
				"    round_new(((dcjsmk.buhsdj + (dcjsmk.hansyf * (1 - 0.07) / dcjsmk.jiessl)) * 1.17), 2) hanszhdcj,\n" + 
				"    dcjsmk.hansdj, dcjsmk.hansmk, dcjsmk.jiajqdj,\n" + 
				"    round_new((dcjsmk.jiajqdj * dcjsmk.jiessl), 2) jine\n" + 
				"from diancjsmkb dcjsmk, danpcjsmxb dpcjsmx,\n" + 
				"(\n" + 
				"    select d.bianm from diancjsmkb d,(select id from diancjsmkb where bianm = '"+ bianm +"') d2\n" + 
				"    where d.fuid = d2.id union select d.bianm from diancjsmkb d where bianm = '"+ bianm +"'\n" + 
				") d1\n" + 
				"where dpcjsmx.jiesdid = dcjsmk.id and dcjsmk.bianm in (d1.bianm)\n" + 
				"group by dcjsmk.jiesrq, dcjsmk.meikdwmc, dcjsmk.jiessl, dcjsmk.buhsdj, dcjsmk.hansyf, dcjsmk.hetj,\n" + 
				"    dcjsmk.hansdj, dcjsmk.hansmk, dcjsmk.jiajqdj, dcjsmk.bukmk, dcjsmk.id, dcjsmk.fuid, dcjsmk.diancxxb_id, dcjsmk.jiesrl";
			
			ResultSetList rsl = con.getResultSetList(sql);
			
			String[] fuid = new String[rsl.getRows()];		// fuid
			String[] jiesrq = new String[rsl.getRows()];	// 日期
			String[] meikdwmc = new String[rsl.getRows()];	// 煤矿单位名称
			String[] jiessl = new String[rsl.getRows()];	// 结算数量
			String[] qnet_mj = new String[rsl.getRows()];	// Qnet_MJ/kg
			String[] jiesrl = new String[rsl.getRows()];	// Qnet_大卡/每千克, 结算热量
			String[] mt = new String[rsl.getRows()];		// 水分
			String[] ad = new String[rsl.getRows()];		// 灰分
			String[] vdaf = new String[rsl.getRows()];		// 挥发分
			String[] std = new String[rsl.getRows()];		// 硫分
			String[] hetj = new String[rsl.getRows()];		// 合同价
			String[] qnetark = new String[rsl.getRows()];	// 热值加（扣）
			String[] stdk = new String[rsl.getRows()];		// 硫加（扣）
			String[] adk = new String[rsl.getRows()];		// 灰分加（扣）
			String[] xiaoj = new String[rsl.getRows()];		// 小计
			String[] buhsdj = new String[rsl.getRows()];	// 不含税单价
			String[] hansyf = new String[rsl.getRows()];	// 含税运费
			String[] buhsyf = new String[rsl.getRows()];	// 不含税运费
			String[] buhsdmyf = new String[rsl.getRows()];	// 不含税吨煤运费
			String[] hanszhdcj = new String[rsl.getRows()];	// 含税综合到厂价
			String[] hansdj = new String[rsl.getRows()];	// 含税单价
			String[] hansmk = new String[rsl.getRows()];	// 含税煤款
			String[] jiajqdj = new String[rsl.getRows()];	// 加价前单价
			String[] jine = new String[rsl.getRows()];		// 补扣煤款
			String diancxxb_id = "";						// 电厂信息表id
			
			int index = 0;
			while(rsl.next()) {
				fuid[index] = rsl.getString("fuid");
				jiesrq[index] = rsl.getString("jiesrq");
				meikdwmc[index] = rsl.getString("meikdwmc");
				jiessl[index] = rsl.getString("jiessl");
				qnet_mj[index] = rsl.getString("qnet_mj");
				jiesrl[index] = rsl.getString("jiesrl");
				mt[index] = rsl.getString("mt");
				ad[index] = rsl.getString("ad");
				vdaf[index] = rsl.getString("vdaf");
				std[index] = rsl.getString("std");
				hetj[index] = rsl.getString("hetj");
				qnetark[index] = rsl.getString("qnetark");
				stdk[index] = rsl.getString("stdk");
				adk[index] = rsl.getString("adk");
				xiaoj[index] = rsl.getString("xiaoj");
				buhsdj[index] = rsl.getString("buhsdj");
				hansyf[index] = rsl.getString("hansyf");
				buhsyf[index] = rsl.getString("buhsyf");
				buhsdmyf[index] = rsl.getString("buhsdmyf");
				hanszhdcj[index] = rsl.getString("hanszhdcj");
				hansdj[index] = rsl.getString("hansdj");
				hansmk[index] = rsl.getString("hansmk");
				jiajqdj[index] = rsl.getString("jiajqdj");
				jine[index] = rsl.getString("jine");
				diancxxb_id = rsl.getString("diancxxb_id");
				index = index + 1;
			}
			
			String ArrHeader[][] = new String[5][23];
			ArrHeader[0] = new String[] {"","","","","","","","","","","","","","","","","","","","","","",""};
			ArrHeader[1] = new String[] {"燃料购销内部结算单","","","","","","","","","","","","","","","","","","","","","",""};
			ArrHeader[2] = new String[] {"","","","","","","","","","","","","","","","","","","","","","",""};
			ArrHeader[3] = new String[] {"","收货单位(加盖公章):"+getDanwmc(diancxxb_id),"","","","","","","",getHeadDate(jiesrq[0]),"","","","","","","供煤周期：","","","","","",""};
			ArrHeader[4] = new String[] {"","","","","","","","","","","","","","","","","","","","","","",""};
			
			String ArrBody[][] = new String[13 + rsl.getRows()][23];
			ArrBody[0] = new String[] {"时间","供煤单位","数量","Qnet","","水分","灰分","挥发分","硫分","煤价(元/吨、含税价)","","","","","不含税煤价(元/吨)","运杂费(元、元/吨)","","","含税综合到厂价(元/吨)","结算金额","","",""};
			ArrBody[1] = new String[] {"","","吨","MJ/kg","大卡/千克","%","%","%","%","基础价","热质加(扣)","硫加(扣)","灰分加(扣)","小计","","含税总金额","不含税总金额","不含税吨煤运费","","与燃料公司结算(含税)","","与多经企业结算(含税)",""};
			ArrBody[2] = new String[] {"","","","","","","","","","","","","","","","","","","","单位","金额","单位","金额"};
			ArrBody[3] = new String[] {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
			int j = 0;
			for (int i = 0; i < rsl.getRows(); i ++) {
				if (rsl.getRows() == 1) { // 如果结果集的记录数为一条，那么把此记录显示在表体第4行和"合计"行上
					ArrBody[4 + i] = new String[] {jiesrq[i], meikdwmc[i], jiessl[i], qnet_mj[i], jiesrl[i], mt[i], ad[i], vdaf[i], std[i], hetj[i], qnetark[i], stdk[i], adk[i], xiaoj[i], buhsdj[i], hansyf[i], buhsyf[i], buhsdmyf[i], hanszhdcj[i], hansdj[i], hansmk[i], jiajqdj[i], jine[i]};
					ArrBody[8 + rsl.getRows()] = new String[] {"合计","", jiessl[i], qnet_mj[i], jiesrl[i], mt[i], ad[i], vdaf[i], std[i], hetj[i], qnetark[i], stdk[i], adk[i], xiaoj[i], buhsdj[i], hansyf[i], buhsyf[i], buhsdmyf[i], hanszhdcj[i], hansdj[i], hansmk[i], jiajqdj[i], jine[i]};
				} else { // 如果结果集的记录数为多条，那么将fuid为0的那条记录显示在"合计"行上
					if (fuid[i].equals("0")) {
						ArrBody[8 + rsl.getRows()] = new String[] {"合计","", jiessl[i], qnet_mj[i], jiesrl[i], mt[i], ad[i], vdaf[i], std[i], hetj[i], qnetark[i], stdk[i], adk[i], xiaoj[i], buhsdj[i], hansyf[i], buhsyf[i], buhsdmyf[i], hanszhdcj[i], hansdj[i], hansmk[i], jiajqdj[i], jine[i]};
						j = i;
					} else {
						ArrBody[4 + j] = new String[] {jiesrq[i], meikdwmc[i], jiessl[i], qnet_mj[i], jiesrl[i], mt[i], ad[i], vdaf[i], std[i], hetj[i], qnetark[i], stdk[i], adk[i], xiaoj[i], buhsdj[i], hansyf[i], buhsyf[i], buhsdmyf[i], hanszhdcj[i], hansdj[i], hansmk[i], jiajqdj[i], jine[i]};
						j ++;
					}
				}
			}
			ArrBody[4 + rsl.getRows()] = new String[] {"","","","","","","","","","","","","","","","","","","","","","",""};
			ArrBody[5 + rsl.getRows()] = new String[] {"","","","","","","","","","","","","","","","","","","","","","",""};
			ArrBody[6 + rsl.getRows()] = new String[] {"","","","","","","","","","","","","","","","","","","","","","",""};
			ArrBody[7 + rsl.getRows()] = new String[] {"","","","","","","","","","","","","","","","","","","","","","",""};
			ArrBody[9 + rsl.getRows()] = new String[] {"其中：①代垫杂费","","","","","","","","","","","","","","","","","","","","","",""};
			ArrBody[10 + rsl.getRows()] = new String[] {"②煤款","","","","","","","","","","","","","","","","","","","","","",""};
			ArrBody[11 + rsl.getRows()] = new String[] {"","","收货单位负责人：","","","","燃料管理部门负责人：","","","","","制表：","","","编制时间：","","年","月","日","","","",""};
			ArrBody[12 + rsl.getRows()] = new String[] {"","燃料公司公章：","","","","","燃料公司负责人：","","","","","计划调运部主任：","","","","","计划调运部主任：","","","","多经公司审核：","",""};
			
			int ArrWidth[] = new int[] {70,210,54,54,54,54,54,54,54,54,54,54,54,54,75,54,54,54,54,75,75,75,75};
			
			rt.setTitle(new Table(ArrHeader, 0, 0, 0));
			rt.setBody(new Table(ArrBody, 0, 0, 0));
			rt.title.setWidth(ArrWidth);
			rt.body.setWidth(ArrWidth);
			
			rt.title.mergeCell(1, 1, 1, 23);
			rt.title.mergeCell(2, 1, 2, 23);
			rt.title.mergeCell(4, 2, 4, 9);
			rt.title.mergeCell(4, 10, 4, 12);
			rt.title.mergeCell(4, 17, 4, 22);
			rt.title.mergeCell(5, 1, 5, 23);
			
			rt.title.setRowCells(2, Table.PER_ALIGN, Table.ALIGN_CENTER);
			rt.title.setRowCells(4, Table.PER_ALIGN, Table.ALIGN_CENTER);
			rt.title.setCells(4, 2, 4, 9, Table.PER_ALIGN, Table.ALIGN_LEFT);
			rt.title.setRowCells(5, Table.PER_ALIGN, Table.ALIGN_RIGHT);
			
			rt.title.setRowCells(1,Table.PER_BORDER_BOTTOM,0);
			rt.title.setRowCells(2,Table.PER_BORDER_BOTTOM,0);
			rt.title.setRowCells(3,Table.PER_BORDER_BOTTOM,0);
			rt.title.setRowCells(4,Table.PER_BORDER_BOTTOM,0);
			rt.title.setRowCells(5,Table.PER_BORDER_BOTTOM,0);
			
			rt.title.setRowCells(1,Table.PER_BORDER_RIGHT,0);
			rt.title.setRowCells(2,Table.PER_BORDER_RIGHT,0);
			rt.title.setRowCells(3,Table.PER_BORDER_RIGHT,0);
			rt.title.setRowCells(4,Table.PER_BORDER_RIGHT,0);
			rt.title.setRowCells(5,Table.PER_BORDER_RIGHT,0);
			rt.title.setBorder(0,0,0,0);
			rt.title.setCellImage(1, 1, 290, 60, "imgs/report/datgsfd.gif");
			rt.title.setRowCells(1, Table.PER_ALIGN, Table.ALIGN_LEFT);
			
			rt.title.setRowCells(2, Table.PER_FONTNAME, "隶书");
			rt.title.setRowCells(2, Table.PER_FONTSIZE, 26);
			rt.title.setRowCells(4, Table.PER_FONTNAME, "黑体");
			rt.title.setRowCells(4, Table.PER_FONTSIZE, 14);
			rt.title.setRowCells(5, Table.PER_FONTNAME, "黑体");
			rt.title.setRowCells(5, Table.PER_FONTSIZE, 14);
			rt.title.setRowHeight(4, 50);
			
			rt.body.setRowCells(1, Table.PER_ALIGN, Table.ALIGN_CENTER);
			rt.body.setRowCells(2, Table.PER_ALIGN, Table.ALIGN_CENTER);
			rt.body.setRowCells(3, Table.PER_ALIGN, Table.ALIGN_CENTER);
			rt.body.setRowCells(9 + rsl.getRows(), Table.PER_ALIGN, Table.ALIGN_CENTER);
			rt.body.setRowCells(10 + rsl.getRows(), Table.PER_ALIGN, Table.ALIGN_CENTER);
			rt.body.setRowCells(11 + rsl.getRows(), Table.PER_ALIGN, Table.ALIGN_CENTER);
			
			rt.body.mergeCell(1, 4, 1, 5);
			rt.body.mergeCell(1, 10, 1, 14);
			rt.body.mergeCell(1, 16, 1, 18);
			rt.body.mergeCell(1, 20, 1, 23);
			rt.body.mergeCell(2, 20, 2, 21);
			rt.body.mergeCell(2, 22, 2, 23);
			
			rt.body.mergeCell(1, 1, 3, 1);
			rt.body.mergeCell(1, 2, 3, 2);
			rt.body.mergeCell(1, 15, 3, 15);
			rt.body.mergeCell(1, 19, 3, 19);
			
			rt.body.mergeCell(2, 3, 3, 3);
			rt.body.mergeCell(2, 4, 3, 4);
			rt.body.mergeCell(2, 5, 3, 5);
			rt.body.mergeCell(2, 6, 3, 6);
			rt.body.mergeCell(2, 7, 3, 7);
			rt.body.mergeCell(2, 8, 3, 8);
			rt.body.mergeCell(2, 9, 3, 9);
			rt.body.mergeCell(2, 10, 3, 10);
			rt.body.mergeCell(2, 11, 3, 11);
			rt.body.mergeCell(2, 12, 3, 12);
			rt.body.mergeCell(2, 13, 3, 13);
			rt.body.mergeCell(2, 14, 3, 14);
			rt.body.mergeCell(2, 16, 3, 16);
			rt.body.mergeCell(2, 17, 3, 17);
			rt.body.mergeCell(2, 18, 3, 18);
			
			rt.body.mergeCell(9 + rsl.getRows(), 1, 9 + rsl.getRows(), 2);
			rt.body.mergeCell(10 + rsl.getRows(), 1, 10 + rsl.getRows(), 2);
			rt.body.mergeCell(11 + rsl.getRows(), 1, 11 + rsl.getRows(), 2);
			
			rt.body.setBorder(0, 0, 2, 0);
			rt.body.setRowCells(11 + rsl.getRows(), Table.PER_BORDER_BOTTOM, 2);
			rt.body.setRowCells(12 + rsl.getRows(), Table.PER_BORDER_BOTTOM, 0);
			rt.body.setRowCells(12 + rsl.getRows(), Table.PER_BORDER_RIGHT, 0);
			rt.body.setRowCells(13 + rsl.getRows(), Table.PER_BORDER_BOTTOM, 0);
			rt.body.setRowCells(13 + rsl.getRows(), Table.PER_BORDER_RIGHT, 0);
			rt.body.setCells(1, 1, 11 + rsl.getRows(), 1, Table.PER_BORDER_LEFT, 2);
			rt.body.setCells(1, 23, 11 + rsl.getRows(), 23, Table.PER_BORDER_RIGHT, 2);
			rt.body.setCells(2, 22, 2, 22, Table.PER_BORDER_RIGHT, 2);
			rt.body.setCells(1, 20, 1, 20, Table.PER_BORDER_RIGHT, 2);
			
			rt.body.mergeCell(12 + rsl.getRows(), 3, 12 + rsl.getRows(), 6);
			rt.body.mergeCell(12 + rsl.getRows(), 7, 12 + rsl.getRows(), 11);
			rt.body.mergeCell(12 + rsl.getRows(), 12, 12 + rsl.getRows(), 14);
			rt.body.mergeCell(12 + rsl.getRows(), 15, 12 + rsl.getRows(), 16);
			rt.body.setRowCells(12 + rsl.getRows(), Table.PER_FONTNAME, "黑体");
			rt.body.setRowCells(12 + rsl.getRows(), Table.PER_FONTSIZE, 11);
			rt.body.setRowHeight(12 + rsl.getRows(), 70);
			
			rt.body.mergeCell(13 + rsl.getRows(), 2, 13 + rsl.getRows(), 6);
			rt.body.mergeCell(13 + rsl.getRows(), 7, 13 + rsl.getRows(), 11);
			rt.body.mergeCell(13 + rsl.getRows(), 12, 13 + rsl.getRows(), 16);
			rt.body.mergeCell(13 + rsl.getRows(), 17, 13 + rsl.getRows(), 20);
			rt.body.mergeCell(13 + rsl.getRows(), 21, 13 + rsl.getRows(), 23);
			rt.body.setRowCells(13 + rsl.getRows(), Table.PER_FONTNAME, "黑体");
			rt.body.setRowCells(13 + rsl.getRows(), Table.PER_FONTSIZE, 11);
			rt.body.setRowHeight(13 + rsl.getRows(), 80);
			
			rt.body.setCells(4, 1, 4, 23, Table.PER_ALIGN, Table.ALIGN_CENTER);
			rt.body.setCells(5, 1, 8 + rsl.getRows(), 1, Table.PER_ALIGN, Table.ALIGN_CENTER);
			rt.body.setCells(5, 3, 9 + rsl.getRows(), 23, Table.PER_ALIGN, Table.ALIGN_RIGHT);
			
			_CurrentPage = 1;
			_AllPages = 4;
			if (_AllPages == 0) {
				_CurrentPage = 0;
			}
			
			String str = "";
			rt.title.setCellValue(5, 1, "第一联：存根/共四联");
			str = rt.getAllPagesHtml(0);
			rt.title.setCellValue(5, 1, "第二联：记帐/共四联");
			str += rt.getAllPagesHtml(1);
			rt.title.setCellValue(5, 1, "第三联：拨款/共四联");
			str += rt.getAllPagesHtml(2);
			rt.title.setCellValue(5, 1, "第四联：结算/共四联");
			str += rt.getAllPagesHtml(3);
			return str.toString();
			
		} else if (type.equals("caigd")) { // 采购单
			
			String sql = 
				"select kfjsmk.fuid, kfjsmk.id, kfjsmk.diancxxb_id, to_char(kfjsmk.jiesrq, 'yyyy-MM-dd') jiesrq, kfjsmk.meikdwmc,\n" +
				"    getJiesdzb('kuangfjsmkb', kfjsmk.id, '结算数量', 'gongf') kuangfl,\n" + 
				"    kfjsmk.jiessl, kfjsmk.kuid,\n" + 
				"    round_new((kfjsmk.jiesrl * 4.1816 / 1000), 3) qnet_mj,\n" + 
				"    kfjsmk.jiesrl, max(dpcjsmx.mt) mt, max(dpcjsmx.ad) ad, max(dpcjsmx.vdaf) vdaf, max(dpcjsmx.std) std, kfjsmk.hetj,\n" + 
				"    getJiesdzb('kuangfjsmkb', kfjsmk.id, 'Qnetar', 'zhejbz') qnetark,\n" + 
				"    getJiesdzb('kuangfjsmkb', kfjsmk.id, 'Std', 'zhejbz') stdk,\n" + 
				"    getJiesdzb('kuangfjsmkb', kfjsmk.id, 'Ad', 'zhejbz') adk,\n" + 
				"    (kfjsmk.hetj + nvl(getJiesdzb('kuangfjsmkb', kfjsmk.id, 'Qnetar', 'zhejbz'), 0) +\n" + 
				"    nvl(getJiesdzb('kuangfjsmkb', kfjsmk.id, 'Std', 'zhejbz'), 0) + nvl(getJiesdzb('kuangfjsmkb', kfjsmk.id, 'Ad', 'zhejbz'), 0)) xiaoj,\n" + 
				"    kfjsmk.hansmk, kfjsmk.hansyf, (nvl(kfjsmk.hansmk, 0) + nvl(kfjsmk.hansyf, 0)) yingfje\n" + 
				"from kuangfjsmkb kfjsmk, danpcjsmxb dpcjsmx,\n" + 
				"(\n" + 
				"    select k.bianm from kuangfjsmkb k,(select id from kuangfjsmkb where bianm = '"+ bianm +"') d2\n" + 
				"    where k.fuid = d2.id union select k.bianm from kuangfjsmkb k where bianm = '"+ bianm +"'\n" + 
				") d1\n" + 
				"where dpcjsmx.jiesdid = kfjsmk.id and kfjsmk.bianm in (d1.bianm)\n" + 
				"group by kfjsmk.fuid, kfjsmk.id, kfjsmk.diancxxb_id, kfjsmk.jiesrq, kfjsmk.meikdwmc, kfjsmk.jiessl, kfjsmk.kuid, kfjsmk.jiesrl, kfjsmk.hetj,\n" + 
				"    kfjsmk.hansmk, kfjsmk.hansyf";
			
			ResultSetList rsl = con.getResultSetList(sql);
			
			String[] fuid = new String[rsl.getRows()];		// fuid
			String[] jiesrq = new String[rsl.getRows()];	// 日期
			String[] meikdwmc = new String[rsl.getRows()];	// 煤矿单位名称
			String[] kuangfl = new String[rsl.getRows()];	// 矿发量
			String[] jiessl = new String[rsl.getRows()];	// 结算数量
			String[] kuid = new String[rsl.getRows()];		// 亏吨
			String[] qnet_mj = new String[rsl.getRows()];	// Qnet_MJ/kg
			String[] jiesrl = new String[rsl.getRows()];	// Qnet_大卡/每千克, 结算热量
			String[] mt = new String[rsl.getRows()];		// 水分
			String[] ad = new String[rsl.getRows()];		// 灰分
			String[] vdaf = new String[rsl.getRows()];		// 挥发分
			String[] std = new String[rsl.getRows()];		// 硫分
			String[] hetj = new String[rsl.getRows()];		// 合同价
			String[] qnetark = new String[rsl.getRows()];	// 热值加（扣）
			String[] stdk = new String[rsl.getRows()];		// 硫加（扣）
			String[] adk = new String[rsl.getRows()];		// 灰分加（扣）
			String[] xiaoj = new String[rsl.getRows()];		// 小计
			String[] hansmk = new String[rsl.getRows()];	// 含税煤款
			String[] hansyf = new String[rsl.getRows()];	// 含税运费
			String[] yingfje = new String[rsl.getRows()];	// 应付金额
			String diancxxb_id = "";						// 电厂信息表id
			
			int index = 0;
			while(rsl.next()) {
				fuid[index] = rsl.getString("fuid");
				jiesrq[index] = rsl.getString("jiesrq");
				meikdwmc[index] = rsl.getString("meikdwmc");
				kuangfl[index] = rsl.getString("kuangfl");
				jiessl[index] = rsl.getString("jiessl");
				kuid[index] = rsl.getString("kuid");
				qnet_mj[index] = rsl.getString("qnet_mj");
				jiesrl[index] = rsl.getString("jiesrl");
				mt[index] = rsl.getString("mt");
				ad[index] = rsl.getString("ad");
				vdaf[index] = rsl.getString("vdaf");
				std[index] = rsl.getString("std");
				hetj[index] = rsl.getString("hetj");
				qnetark[index] = rsl.getString("qnetark");
				stdk[index] = rsl.getString("stdk");
				adk[index] = rsl.getString("adk");
				xiaoj[index] = rsl.getString("xiaoj");
				hansmk[index] = rsl.getString("hansmk");
				hansyf[index] = rsl.getString("hansyf");
				yingfje[index] = rsl.getString("yingfje");
				diancxxb_id = rsl.getString("diancxxb_id");
				index = index + 1;
			}

			String ArrHeader[][] = new String[4][19];
			ArrHeader[0] = new String[] {"","","","","","","","","","","","","","","","","","","" };
			ArrHeader[1] = new String[] {"燃料采购结算单","","","","","","","","","","","","","","","","","",""};
			ArrHeader[2] = new String[] {"","","","","","","","","","","","","","","","","","","" };
			ArrHeader[3] = new String[] {"","供煤单位："+getDanwmc(diancxxb_id),"","","","",getHeadDate(jiesrq[0]),"","","","","","供煤周期：","","","","","","" };
			
			String ArrBody[][] = new String[13 + rsl.getRows()][19];
			ArrBody[0] = new String[] {"时间","矿点","矿发量","厂收量","亏吨","Qnet","","水分","灰分","挥发分","硫分","煤价(元/吨、含税)","","","","","支付煤款","运杂费","应付金额" };
			ArrBody[1] = new String[] {"","","吨","吨","吨","MJ/kg","大卡/千克","%","%","%","%","基础价","热值加(扣)","硫加(扣)","灰分加(扣)","小计","元","元","元"};
			ArrBody[2] = new String[] {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19"};
			int j = 0;
			for (int i = 0; i < rsl.getRows(); i ++) {
				if (rsl.getRows() == 1) { // 如果结果集的记录数为一条，那么把此记录显示在表体第3行和"合计"行上
					ArrBody[3 + i] = new String[] {jiesrq[i], meikdwmc[i], kuangfl[i], jiessl[i], kuid[i], qnet_mj[i], jiesrl[i], mt[i], ad[i], vdaf[i], std[i], hetj[i], qnetark[i], stdk[i], adk[i], xiaoj[i], hansmk[i], hansyf[i], yingfje[i]};
					ArrBody[8 + rsl.getRows()] = new String[] {"合计","", kuangfl[i], jiessl[i], kuid[i], qnet_mj[i], jiesrl[i], mt[i], ad[i], vdaf[i], std[i], hetj[i], qnetark[i], stdk[i], adk[i], xiaoj[i], hansmk[i], hansyf[i], yingfje[i]};
				} else { // 如果结果集的记录数为多条，那么将fuid为0的那条记录显示在"合计"行上
					if (fuid[i].equals("0")) {
						ArrBody[8 + rsl.getRows()] = new String[] {"合计","", kuangfl[i], jiessl[i], kuid[i], qnet_mj[i], jiesrl[i], mt[i], ad[i], vdaf[i], std[i], hetj[i], qnetark[i], stdk[i], adk[i], xiaoj[i], hansmk[i], hansyf[i], yingfje[i]};
						j = i;
					} else {
						ArrBody[3 + j] = new String[] {jiesrq[i], meikdwmc[i], kuangfl[i], jiessl[i], kuid[i], qnet_mj[i], jiesrl[i], mt[i], ad[i], vdaf[i], std[i], hetj[i], qnetark[i], stdk[i], adk[i], xiaoj[i], hansmk[i], hansyf[i], yingfje[i]};
						j ++;
					}
				}
			}
			ArrBody[4 + rsl.getRows()] = new String[] {"","","","","","","","","","","","","","","","","","","" };
			ArrBody[5 + rsl.getRows()] = new String[] {"","","","","","","","","","","","","","","","","","","" };
			ArrBody[6 + rsl.getRows()] = new String[] {"","","","","","","","","","","","","","","","","","","" };
			ArrBody[7 + rsl.getRows()] = new String[] {"","","","","","","","","","","","","","","","","","","" };
			ArrBody[9 + rsl.getRows()] = new String[] {"其中：①代垫运杂费","","","","","","","","","","","","","","","","","","" };
			ArrBody[10 + rsl.getRows()] = new String[] {"②煤款","","","","","","","","","","","","","","","","","","" };
			ArrBody[11 + rsl.getRows()] = new String[] {"收货单位公章：","","收货单位负责人：","","","","燃料管理部门负责人：","","","","","制表：","","","编制时间：","","年","月","日" };
			ArrBody[12 + rsl.getRows()] = new String[] {"","燃料中心负责人：","","","","燃料管理中心审核：","","","","","燃料公司负责人：","","","","","燃料公司审核：","","","" };
			
			int ArrWidth[] = new int[] {70,210,54,54,54,54,54,54,54,54,54,54,54,54,54,54,65,65,65};
			
			rt.setTitle(new Table(ArrHeader, 0, 0, 0));
			rt.setBody(new Table(ArrBody, 0, 0, 0));
			rt.title.setWidth(ArrWidth);
			rt.body.setWidth(ArrWidth);
			
			rt.title.mergeCell(1, 1, 1, 19);
			rt.title.mergeCell(2, 1, 2, 19);
			rt.title.mergeCell(4, 2, 4, 6);
			rt.title.mergeCell(4, 7, 4, 10);
			rt.title.mergeCell(4, 13, 4, 19);
			
			rt.title.setRowCells(2, Table.PER_ALIGN, Table.ALIGN_CENTER);
			rt.title.setCells(4, 2, 4, 6, Table.PER_ALIGN, Table.ALIGN_LEFT);
			rt.title.setCells(4, 7, 4, 10, Table.PER_ALIGN, Table.ALIGN_CENTER);
			rt.title.setCells(4, 13, 4, 19, Table.PER_ALIGN, Table.ALIGN_CENTER);
			
			rt.title.setRowCells(2, Table.PER_FONTNAME, "隶书");
			rt.title.setRowCells(2, Table.PER_FONTSIZE, 26);
			rt.title.setRowCells(4, Table.PER_FONTNAME, "黑体");
			rt.title.setRowCells(4, Table.PER_FONTSIZE, 14);
			rt.title.setRowHeight(3, 5);
			rt.title.setRowHeight(4, 50);
			rt.body.setRowHeight(12 + rsl.getRows(), 100);
			
			rt.title.setCellImage(1, 1, 290, 60, "imgs/report/datgsfd.gif");
			rt.title.setRowCells(1, Table.PER_ALIGN, Table.ALIGN_LEFT);
			
			rt.title.setRowCells(1,Table.PER_BORDER_BOTTOM,0);
			rt.title.setRowCells(2,Table.PER_BORDER_BOTTOM,0);
			rt.title.setRowCells(3,Table.PER_BORDER_BOTTOM,0);
			rt.title.setRowCells(4,Table.PER_BORDER_BOTTOM,0);
			rt.title.setRowCells(1,Table.PER_BORDER_RIGHT,0);
			rt.title.setRowCells(2,Table.PER_BORDER_RIGHT,0);
			rt.title.setRowCells(3,Table.PER_BORDER_RIGHT,0);
			rt.title.setRowCells(4,Table.PER_BORDER_RIGHT,0);
			rt.title.setBorder(0,0,0,0);
			
			rt.body.setRowCells(1, Table.PER_ALIGN, Table.ALIGN_CENTER);
			rt.body.setRowCells(2, Table.PER_ALIGN, Table.ALIGN_CENTER);
			rt.body.setRowCells(9 + rsl.getRows(), Table.PER_ALIGN, Table.ALIGN_CENTER);
			rt.body.setRowCells(10 + rsl.getRows(), Table.PER_ALIGN, Table.ALIGN_CENTER);
			rt.body.setRowCells(11 + rsl.getRows(), Table.PER_ALIGN, Table.ALIGN_CENTER);
			
			rt.body.mergeCell(1, 1, 2, 1);
			rt.body.mergeCell(1, 2, 2, 2);
			rt.body.mergeCell(1, 6, 1, 7);
			rt.body.mergeCell(1, 12, 1, 16);
			rt.body.mergeCell(9 + rsl.getRows(), 1, 9 + rsl.getRows(), 2);
			rt.body.mergeCell(10 + rsl.getRows(), 1, 10 + rsl.getRows(), 2);
			rt.body.mergeCell(11 + rsl.getRows(), 1, 11 + rsl.getRows(), 2);
			rt.body.mergeCell(12 + rsl.getRows(), 1, 12 + rsl.getRows(), 2);
			rt.body.mergeCell(12 + rsl.getRows(), 3, 12 + rsl.getRows(), 6);
			rt.body.mergeCell(12 + rsl.getRows(), 7, 12 + rsl.getRows(), 11);
			rt.body.mergeCell(12 + rsl.getRows(), 12, 12 + rsl.getRows(), 14);
			rt.body.mergeCell(12 + rsl.getRows(), 15, 12 + rsl.getRows(), 16);
			rt.body.mergeCell(13 + rsl.getRows(), 6, 13 + rsl.getRows(), 10);
			rt.body.mergeCell(13 + rsl.getRows(), 11, 13 + rsl.getRows(), 15);
			rt.body.mergeCell(13 + rsl.getRows(), 16, 13 + rsl.getRows(), 19);
			
			rt.body.setBorder(0, 0, 2, 0);
			rt.body.setRowCells(11 + rsl.getRows(), Table.PER_BORDER_BOTTOM, 2);
			rt.body.setRowCells(12 + rsl.getRows(), Table.PER_BORDER_BOTTOM, 0);
			rt.body.setRowCells(12 + rsl.getRows(), Table.PER_BORDER_RIGHT, 0);
			rt.body.setRowCells(13 + rsl.getRows(), Table.PER_BORDER_BOTTOM, 0);
			rt.body.setRowCells(13 + rsl.getRows(), Table.PER_BORDER_RIGHT, 0);
			rt.body.setCells(1, 1, 11 + rsl.getRows(), 1, Table.PER_BORDER_LEFT, 2);
			rt.body.setCells(1, 19, 11 + rsl.getRows(), 19, Table.PER_BORDER_RIGHT, 2);
			
			rt.body.setCells(3, 1, 3, 19, Table.PER_ALIGN, Table.ALIGN_CENTER);
			rt.body.setCells(4, 1, 7 + rsl.getRows(), 1, Table.PER_ALIGN, Table.ALIGN_CENTER);
			rt.body.setCells(4, 3, 9 + rsl.getRows(), 19, Table.PER_ALIGN, Table.ALIGN_RIGHT);
			rt.body.setCells(13 + rsl.getRows(), 2, 13 + rsl.getRows(), 2, Table.PER_ALIGN, Table.ALIGN_RIGHT);
			
			rt.body.setRowCells(12 + rsl.getRows(), Table.PER_FONTNAME, "黑体");
			rt.body.setRowCells(12 + rsl.getRows(), Table.PER_FONTSIZE, 11);
			rt.body.setRowCells(13 + rsl.getRows(), Table.PER_FONTNAME, "黑体");
			rt.body.setRowCells(13 + rsl.getRows(), Table.PER_FONTSIZE, 11);
			
			_CurrentPage = 1;
			_AllPages = 1;
			if (_AllPages == 0) {
				_CurrentPage = 0;
			}
			return rt.getAllPagesHtml();
		}
		return "";
	}
	
	/**
	 * 返回表头上要显示的日期，日期格式为yyyy年MM月dd日 
	 */
	public String getHeadDate(String args) {
		
		String[] str = args.split("-");
		
		Calendar cal = Calendar.getInstance();
        cal.set(Integer.parseInt(str[0]), Integer.parseInt(str[1]) - 1, Integer.parseInt(str[2]));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        
        return sdf.format(cal.getTime());
	}
	
	/**
	 * 用diancxxb_id到数据库里取得电厂全称，返回电厂全称
	 */
	public String getDanwmc(String diancxxb_id) {
		
		String danwmc = "";
		
		JDBCcon con = new JDBCcon();
		String sql = "select quanc from diancxxb where id =" + diancxxb_id;
		ResultSetList rs = con.getResultSetList(sql);
		
		if (rs.next()) {
			danwmc = rs.getString("quanc");
		}
		rs.close();
		con.Close();
		return danwmc;
	}
	
}

