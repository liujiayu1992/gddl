package com.zhiren.dc.rulgl.rulbb;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

/**
 * @author yinjm
 * 类名：入炉煤质、飞灰(分析)报告
 */

public class Rulmzfhbg extends BasePage implements PageValidateListener {
	
	private String _msg;

	public void setMsg(String _value) {
		this._msg = MainGlobal.getExtMessageBox(_value, false);
	}
	
	public String getMsg() {
		if (_msg == null) {
			_msg = "";
		}
		return _msg;
	}
	
	protected void initialize() {
		this._msg = "";
	}
	
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
	
	public boolean getRaw() {
		return true;
	}
	
	private String tbmsg;

	public String getTbmsg() {
		return tbmsg;
	}

	public void setTbmsg(String tbmsg) {
		this.tbmsg = tbmsg;
	}
	
	private String caiyrq; // 采样日期
	
	public String getCaiyrq() {
		if (caiyrq.equals("") || caiyrq == null) {
			caiyrq = DateUtil.FormatDate(new Date());
		}
		return caiyrq;
	}

	public void setCaiyrq(String caiyrq) {
		this.caiyrq = caiyrq;
	}

	private boolean _RefurbishChick = false;
	
	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
		}
	}
	
	public String getPrintTable(){
		
		JDBCcon con = new JDBCcon();
		Report rt = new Report();
		
		String sql = 
			"select 'PINGJZ' as xingmmc,\n" +
			"       to_char(round_new(sum(c.meil), 0)) meil,\n" + 
			"       to_char(round_new(sum(c.mad * c.meil) / decode(sum(c.meil), 0, 1, sum(c.meil)), 2)) mad,\n" + 
			"       to_char(round_new(sum(c.mt * c.meil) / decode(sum(c.meil), 0, 1, sum(c.meil)), 1),'FM90.0') mt,\n" + 
			"       to_char(round_new(sum(c.aad * c.meil) / decode(sum(c.meil), 0, 1, sum(c.meil)), 2),'FM90.00') aad,\n" + 
			"       to_char(round_new(sum(c.vad * c.meil) / decode(sum(c.meil), 0, 1, sum(c.meil)), 2),'FM90.00') vad,\n" + 
			"       to_char(round_new(sum(c.fcad * c.meil) / decode(sum(c.meil), 0, 1, sum(c.meil)), 2),'FM90.00') fcad,\n" + 
			"       to_char(round_new(sum(c.qbad * c.meil) / decode(sum(c.meil), 0, 1, sum(c.meil)), 3)*1000) qbad,\n" + 
			"       to_char(round_new(sum(c.qgrad * c.meil) / decode(sum(c.meil), 0, 1, sum(c.meil)), 3)*1000) qgrad,\n" + 
			"       to_char(round_new(sum(c.qnet_ar * c.meil) / decode(sum(c.meil), 0, 1, sum(c.meil)), 3)*1000) qnet_ar,\n" + 
			"       to_char(round_new(sum(Round_new(c.stad*(100-c.mt)/(100-mad),2) * c.meil) / decode(sum(c.meil), 0, 1, sum(c.meil)), 2),'FM9990.09') stad,\n" + 
			"       decode(\n" +
			"       to_char(nvl(round_new(sum(c.yihjz * c.meil) / decode(sum(c.meil), 0, 1, sum(c.meil)), 2),0),'FM990.00'),\n" + 
			"       '0.00','停炉',\n" + 
			"       to_char(nvl(round_new(sum(c.yihjz * c.meil) / decode(sum(c.meil), 0, 1, sum(c.meil)), 2),0),'FM990.00')) feihkrw_yhjz," +


			"decode(\n" +
			"       to_char(nvl(round_new(sum(c.erhjz * c.meil) / decode(sum(c.meil), 0, 1, sum(c.meil)), 2),0),'FM990.00'),\n" + 
			"       '0.00','停炉',\n" + 
			"       to_char(nvl(round_new(sum(c.erhjz * c.meil) / decode(sum(c.meil), 0, 1, sum(c.meil)), 2),0),'FM990.00')\n" + 
			"       )\n" + 
			"\n" + 
			"       feihkrw_ehjz," +
			"		'' as fenxrq\n" +
			"        from\n" + 
			"(select bz.mingc,\n" + 
			"       sum(zl.meil) meil,\n" + 
			"       max(zl.mad) mad,\n" + 
			"       max(zl.mt) mt,\n" + 
			"       max(zl.aad) aad,\n" + 
			"       max(zl.vad) vad,\n" + 
			"       max(zl.fcad) fcad,\n" + 
			"       max(zl.qbad) qbad,\n" + 
			"       max(zl.qgrad) qgrad,\n" + 
			"       max(zl.qnet_ar) qnet_ar,\n" + 
			"       max(zl.stad) stad,\n" + 
			"       max(a.zhi) yihjz,\n" + 
			"       max(b.zhi) erhjz,\n" +
			"		max(to_char(zl.fenxrq, 'yyyy-mm-dd')) fenxrq\n" +
			"  from rulmzlb zl,\n" + 
			"       rulbzb bz,\n" + 
			"       (select fh.rulbzb_id, fh.jizfzb_id, fh.zhi\n" + 
			"          from rulfhb fh, jizfzb jzfz\n" + 
			"         where fh.riq = to_date('"+ getCaiyrq() +"', 'yyyy-mm-dd')\n" + 
			"           and fh.jizfzb_id = jzfz.id\n" + 
			"           and jzfz.mingc = '1#'\n" + 
			"           and fh.xiangm = '灰分') a,\n" + 
			"       (select fh.rulbzb_id, fh.jizfzb_id, fh.zhi\n" + 
			"          from rulfhb fh, jizfzb jzfz\n" + 
			"         where fh.riq = to_date('"+ getCaiyrq() +"', 'yyyy-mm-dd')\n" + 
			"           and fh.jizfzb_id = jzfz.id\n" + 
			"           and jzfz.mingc = '2#'\n" + 
			"           and fh.xiangm = '灰分') b\n" + 
			" where zl.rulrq = to_date('"+ getCaiyrq() +"', 'yyyy-mm-dd')\n" + 
			"   and zl.rulbzb_id = a.rulbzb_id(+)\n" + 
			"   and zl.jizfzb_id = a.jizfzb_id(+)\n" + 
			"   and zl.rulbzb_id = b.rulbzb_id(+)\n" + 
//			"   and zl.jizfzb_id = b.jizfzb_id(+)\n" + 
			"   and zl.rulbzb_id = bz.id\n" + 
			"	and zl.diancxxb_id = "+ getTreeid() +"\n" +
			" group by bz.mingc) c\n" + 
			"union all\n" + 
			"select bz.mingc as xingmmc,\n" + 
			"       to_char(sum(zl.meil)) meil,\n" + 
			"       to_char(Round_new(max(zl.mad),2),'FM990.00') mad,\n" + 
			"       to_char(Round_new(max(zl.mt),1),'FM990.0') mt,\n" + 
			"       to_char(Round_new(max(zl.aad),2),'FM990.00') aad,\n" + 
			"       to_char(Round_new(max(zl.vad),2),'FM990.00') vad,\n" + 
			"       to_char(Round_new(max(zl.fcad),2),'FM990.00') fcad,\n" + 
			"       to_char(Round_new(max(zl.qbad),3)*1000) qbad,\n" + 
			"       to_char(Round_new(max(zl.qgrd),2)*1000) qgrad,\n" + 
			"       to_char(Round_new(max(zl.qnet_ar),2)*1000) qnet_ar,\n" + 
			"       to_char(Round_new(max(zl.stad)*(100-Round_new(max(zl.mt),1))/(100-Round_new(max(zl.mad),2)),2),'FM9990.09') stad,\n" + 
			"       decode(to_char(nvl(max(a.zhi),0),'FM9990.00'),'0.00','停炉',to_char(nvl(max(a.zhi),0),'FM9990.00')) feihkrw_yhjz,\n" +
			"       decode(to_char(nvl(max(b.zhi),0),'FM9990.00'),'0.00','停炉',to_char(nvl(max(b.zhi),0),'FM9990.00')) feihkrw_ehjz," +
			"	    '' fenxrq\n" +
			"       from rulmzlb zl,\n" + 
			"       rulbzb bz,\n" + 
			"       (select fh.rulbzb_id, fh.jizfzb_id, fh.zhi\n" + 
			"          from rulfhb fh, jizfzb jzfz\n" + 
			"         where fh.riq = to_date('"+ getCaiyrq() +"', 'yyyy-mm-dd')\n" + 
			"           and fh.jizfzb_id = jzfz.id\n" + 
			"           and jzfz.mingc = '1#'\n" + 
			"           and fh.xiangm = '灰分') a,\n" + 
			"       (select fh.rulbzb_id, fh.jizfzb_id, fh.zhi\n" + 
			"          from rulfhb fh, jizfzb jzfz\n" + 
			"         where fh.riq = to_date('"+ getCaiyrq() +"', 'yyyy-mm-dd')\n" + 
			"           and fh.jizfzb_id = jzfz.id\n" + 
			"           and jzfz.mingc = '2#'\n" + 
			"           and fh.xiangm = '灰分') b\n" + 
			" where zl.rulrq = to_date('"+ getCaiyrq() +"', 'yyyy-mm-dd')\n" + 
			"   and zl.rulbzb_id = a.rulbzb_id(+)\n" + 
			"   and zl.jizfzb_id = a.jizfzb_id(+)\n" +
			"   and zl.rulbzb_id = b.rulbzb_id(+)\n" +
//			"   and zl.jizfzb_id = b.rulbzb_id(+)\n" +
			"   and zl.rulbzb_id = bz.id\n" + 
			"	and zl.diancxxb_id = "+ getTreeid() +"\n" +
			" group by bz.mingc\n" +
			" order by xingmmc";
		
		ResultSetList rslData = con.getResultSetList(sql);
		String[][] strData = new String[4][14];
		String fenxrq = ""; // 分析日期
		
		if (rslData.getRows() != 0) {
			int temp = 0;
			while(rslData.next()) {
				strData[temp][0] = rslData.getString("xingmmc");
				strData[temp][1] = rslData.getString("meil");
				strData[temp][2] = rslData.getString("mad");
				strData[temp][3] = rslData.getString("mt");
				strData[temp][4] = rslData.getString("aad");
				strData[temp][5] = rslData.getString("vad");
				strData[temp][6] = rslData.getString("fcad");
				strData[temp][7] = rslData.getString("qbad");
				strData[temp][8] = rslData.getString("qgrad");
				strData[temp][9] = rslData.getString("qnet_ar");
				strData[temp][10] = rslData.getString("stad");
				strData[temp][11] = rslData.getString("feihkrw_yhjz");
				strData[temp][12] = rslData.getString("feihkrw_ehjz");
				strData[temp][13] = rslData.getString("fenxrq");
				temp ++;
			}
			if (strData[0][13] == "" || strData[0][13] == null) {
				fenxrq = "";
			} else {
				fenxrq = DateUtil.Formatdate("yyyy年MM月dd日", stringToDate(strData[0][13]));
			}
		}
		//获得报告日期
		Calendar ca = Calendar.getInstance();
		ca.setTime(stringToDate(getCaiyrq()));
		ca.add(Calendar.DATE, +1);
		Date baogrq=ca.getTime();
		String ArrHeader[][] = new String[4][6];
		ArrHeader[0] = new String[] {"","","","","",""};
		ArrHeader[1] = new String[] {"","","","","",""};
		ArrHeader[2] = new String[] {"入炉煤煤质、飞灰分析报告","","","","",""};
		ArrHeader[3] = new String[] {"采样日期:"+DateUtil.Formatdate("yyyy年MM月dd日", stringToDate(getCaiyrq())),"","","","报告日期:"+DateUtil.Formatdate("yyyy年MM月dd日",baogrq),""};
		
		String ArrBody[][] = new String[14][6];
		ArrBody[0] = new String[] {"项目","单位","班组","","","日平均值"};
		ArrBody[1] = new String[] {"","","0-8","8-16","16-24",""};
		ArrBody[2] = new String[] {"班组耗煤量","吨", strData[0][1],  strData[2][1],strData[1][1], strData[3][1]};
		ArrBody[3] = new String[] {"水分Mad","%", strData[0][2], strData[2][2],strData[1][2],  strData[3][2]};
		ArrBody[4] = new String[] {"全水分Mt","%", strData[0][3],  strData[2][3],strData[1][3], strData[3][3]};
		ArrBody[5] = new String[] {"灰分Aad","%", strData[0][4], strData[2][4],strData[1][4],  strData[3][4]};
		ArrBody[6] = new String[] {"挥发分Vad","%", strData[0][5],  strData[2][5],strData[1][5], strData[3][5]};
		ArrBody[7] = new String[] {"固定煤Fc","%", strData[0][6], strData[2][6],strData[1][6],  strData[3][6]};
		ArrBody[8] = new String[] {"弹筒发热量Qb.ad","J/g", strData[0][7],strData[2][7], strData[1][7],  strData[3][7]};
		ArrBody[9] = new String[] {"高位发热量Qgr.d","J/g", strData[0][8],strData[2][8], strData[1][8],  strData[3][8]};
		ArrBody[10] = new String[] {"低位发热量Qnet.ar","J/g", strData[0][9], strData[2][9],strData[1][9],  strData[3][9]};
		ArrBody[11] = new String[] {"全硫St.ar","%", strData[0][10],strData[2][10], strData[1][10],  strData[3][10]};
		ArrBody[12] = new String[] {"#1炉飞灰可燃物","%", strData[0][11], strData[2][11],strData[1][11],  strData[3][11]};
		ArrBody[13] = new String[] {"#2炉飞灰可燃物","%", strData[0][12],  strData[2][12],strData[1][12], strData[3][12]};
		
		int ArrWidth[] = new int[] {160, 45, 88, 88, 88, 88};
		
		rt.setTitle(new Table(ArrHeader, 0, 0, 0));
		rt.setBody(new Table(ArrBody, 0, 0, 0));
		rt.title.setWidth(ArrWidth);
		rt.body.setWidth(ArrWidth);
		
//		表头设置
		rt.title.mergeCell(2, 1, 2, 6);
		rt.title.mergeCell(3, 1, 3, 6);
		rt.title.mergeCell(4, 1, 4, 2);
		rt.title.mergeCell(4, 5, 4, 6);
		rt.title.setCellAlign(3, 1, Table.ALIGN_CENTER);
		rt.title.setRowCells(3, Table.PER_FONTNAME, "黑体");
		rt.title.setRowCells(3, Table.PER_FONTSIZE, 13);
		rt.title.setCellImage(2, 1, 408, 50, "imgs/report/zhangjkrd.jpg");
		rt.title.setRowCells(2, Table.PER_ALIGN, Table.ALIGN_CENTER);
		rt.title.setRowHeight(1, 30);
		rt.title.setBorder(0,0,0,0);
		
		for (int i = 1; i <= 4; i ++) {
			rt.title.setRowCells(i,Table.PER_BORDER_RIGHT,0);
			rt.title.setRowCells(i,Table.PER_BORDER_BOTTOM,0);
		}
		
//		表体设置
		rt.body.mergeCell(1, 1, 2, 1);
		rt.body.mergeCell(1, 2, 2, 2);
		rt.body.mergeCell(1, 3, 1, 5);
		rt.body.mergeCell(1, 6, 2, 6);
		
		rt.body.setCellAlign(1, 1, Table.ALIGN_CENTER);
		rt.body.setCellAlign(1, 2, Table.ALIGN_CENTER);
		rt.body.setCellAlign(1, 3, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		rt.body.setColAlign(3, Table.ALIGN_CENTER);
		rt.body.setColAlign(4, Table.ALIGN_CENTER);
		rt.body.setColAlign(5, Table.ALIGN_CENTER);
		rt.body.setColAlign(6, Table.ALIGN_CENTER);
		rt.body.setRowHeight(28);
		
		StringBuffer strsb = new StringBuffer(rt.getAllPagesHtml());
		
//		文字描述设置_开始
		String shenhr = ""; // 审核人
		String jiaodr = ""; // 校对人
		String huayy = "";	// 化验员
		String jianybz = ""; // 检验标准
		String jianyyqjbh_zhengc = "";  // 检验仪器及编号_正常
		String jianyyqjbh_feizc = ""; 	// 检验仪器及编号_非正常
		String yiqzt = "正常";	// 仪器使用状态
		
		// 取出分析人员信息(审核人、校对人)
		String sql_wenzmx = 
			"select mingc, beiz from item where itemsortid = (select id from itemsort where mingc = '入炉煤质、飞灰分析人员')";
		ResultSetList rsl_wenzmx =  con.getResultSetList(sql_wenzmx);
		while (rsl_wenzmx.next()) {
			if (rsl_wenzmx.getString("beiz").equals("审核")) {
				shenhr = rsl_wenzmx.getString("mingc");
			} else if(rsl_wenzmx.getString("beiz").equals("校对")) {
				jiaodr = rsl_wenzmx.getString("mingc");
			}
		}
		
		// 取出化验员信息
		sql_wenzmx = "select max(zl.huayy) huayy from rulmzlb zl where zl.rulrq = to_date('"+ getCaiyrq() +"', 'yyyy-mm-dd')";
		rsl_wenzmx = con.getResultSetList(sql_wenzmx);
		while (rsl_wenzmx.next()) {
			huayy = rsl_wenzmx.getString("huayy");
		}
		
		// 取出检验标准信息
		sql_wenzmx = "select xiangmmc||' '||mingc as mingc from guobb";
		rsl_wenzmx = con.getResultSetList(sql_wenzmx);
		while (rsl_wenzmx.next()) {
			jianybz += rsl_wenzmx.getString("mingc")+", ";
		}
		if (jianybz != "") {
			jianybz = jianybz.substring(0, jianybz.lastIndexOf(", "));
		}
		
		// 取出校验仪器名称及编号
		sql_wenzmx = "select mingc, beiz from item where itemsortid = (select id from itemsort where mingc = '检验仪器及编号')";
		rsl_wenzmx = con.getResultSetList(sql_wenzmx);
		while (rsl_wenzmx.next()) {
			if (!rsl_wenzmx.getString("beiz").equals("正常")) {
				jianyyqjbh_feizc += rsl_wenzmx.getString("mingc")+", ";
			}
			jianyyqjbh_zhengc += rsl_wenzmx.getString("mingc")+", ";
		}
		if (jianyyqjbh_zhengc != "") {
			jianyyqjbh_zhengc = jianyyqjbh_zhengc.substring(0, jianyyqjbh_zhengc.lastIndexOf(", "));
		}
		if (jianyyqjbh_feizc != "") {
			yiqzt = addEnter(jianyyqjbh_feizc.substring(0, jianyyqjbh_feizc.lastIndexOf(", ")))+"，不正常。";
		}
		String wenznr1 = "";
		String wenznr2 = "";
		String wenznr3 = "";
		String wenznr4 = "";
		String wenznr5 = "";
		String wenznr6 = "";
		if(addEnter(jianyyqjbh_zhengc)!=null){
		String [] a = addEnter(jianyyqjbh_zhengc).split("<br>");
	    wenznr1 = a[0].replaceAll(",", " ");
	    wenznr2 = a[1].replaceAll(",", " ");
		wenznr3 = a[2];
		}
		if(addEnter(jianybz)!=null){
		String [] b = addEnter(jianybz).split("<br>");
	    wenznr4 = b[0].replaceAll(",", " ");
		wenznr5 = b[1].replaceAll(",", " ");
		wenznr6 = b[2];
		}
		Report rt_wenzms = new Report();   // 文字描述Report
		String ArrBody_wenzms[][] = new String[9][2];
		ArrBody_wenzms[0] = new String[] {"审核："+shenhr,"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;校对："+jiaodr+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;化验："+huayy};
		ArrBody_wenzms[1] = new String[] {"检验依据标准：",wenznr4};
		ArrBody_wenzms[2] = new String[] {"",wenznr5};
		ArrBody_wenzms[3] = new String[] {"",wenznr6};
		ArrBody_wenzms[4] = new String[] {"检验用仪器及编号：",wenznr1};
		ArrBody_wenzms[5] = new String[] {"",wenznr2};
		ArrBody_wenzms[6] = new String[] {"",wenznr3};
		ArrBody_wenzms[7] = new String[] {"以上仪器使用状态：", yiqzt};
		ArrBody_wenzms[8] = new String[] {"采样方式及总样重量：",""};
		
		int[] ArrWidth_wenzms = new int[] {155, 365};
		rt_wenzms.setBody(new Table(ArrBody_wenzms, 0, 0, 0));
		rt_wenzms.body.setWidth(ArrWidth_wenzms);
		//rt_wenzms.body.mergeCell(2, 2, 4, 2);
		//rt_wenzms.body.mergeCell(5, 2, 6, 2);
		
		rt_wenzms.body.setCellAlign(1, 1, Table.ALIGN_CENTER);
		rt_wenzms.body.setCellAlign(2, 1, Table.ALIGN_LEFT);
		rt_wenzms.body.setCellAlign(5, 1, Table.ALIGN_LEFT);
//		rt_wenzms.body.setCellAlign(5, 2, Table.VALIGN_TOP);
		rt_wenzms.body.setCellAlign(5, 2, Table.ALIGN_LEFT);
		rt_wenzms.body.setCellAlign(6, 2, Table.ALIGN_LEFT);
		rt_wenzms.body.setCellAlign(7, 2, Table.ALIGN_LEFT);
		rt_wenzms.body.setCellAlign(8, 1, Table.ALIGN_RIGHT);
		rt_wenzms.body.setCellAlign(9, 1, Table.ALIGN_RIGHT);
//		rt_wenzms.body.setCellVAlign(2, 2, Table.VALIGN_TOP);
		
		for (int i = 1; i <= 9; i ++) {
			rt_wenzms.body.setRowCells(i,Table.PER_BORDER_BOTTOM,0);
			rt_wenzms.body.setRowCells(i,Table.PER_BORDER_RIGHT,0);
		}
     	rt_wenzms.body.setBorder(0,0,0,0);
       		
		strsb.append(rt_wenzms.getAllPagesHtml());
//		文字描述设置_结束
		
		
//		采样方式及总样重量_开始
		String sql_tongj = 
			"select bz.mingc rulbzb_id, max(cy.caiyfs) caiyfs, sum(cy.zongyzl) zongyzl\n" +
			"  from caiyfsjzl cy, rulbzb bz, jizfzb jzfz\n" + 
			" where cy.fenxrq = to_date('"+ getCaiyrq() +"', 'yyyy-mm-dd')\n" + 
			"   and cy.diancxxb_id = "+ getTreeid() +"\n" + 
			"   and cy.rulbzb_id = bz.id\n" + 
			"   and cy.jizfzb_id = jzfz.id\n" + 
			" group by bz.xuh ,bz.mingc\n" + 
			" order by bz.xuh";
		
		ResultSetList rsl_tongj = con.getResultSetList(sql_tongj);
		String[][] str_tongj = new String[3][3];
		
		if (rsl_tongj.getRows() != 0) {
			int temp = 0;
			while(rsl_tongj.next()) {
				str_tongj[temp][0] = rsl_tongj.getString("rulbzb_id");
				str_tongj[temp][1] = rsl_tongj.getString("caiyfs");
				str_tongj[temp][2] = rsl_tongj.getString("zongyzl");
				temp ++;
			}
		}
		
		Report rt_tongj = new Report();
		String ArrBody_tongj[][] = new String[3][4];
		ArrBody_tongj[0] = new String[] {"时间","0-8","8-16","16-24"};
		ArrBody_tongj[1] = new String[] {"机械或人工", str_tongj[0][1], str_tongj[1][1], str_tongj[2][1]};
		ArrBody_tongj[2] = new String[] {"总样重量", str_tongj[0][2]+"Kg", str_tongj[1][2]+"Kg", str_tongj[2][2]+"Kg"};
		
		int[] ArrWidth_tongj = new int[] {200, 110, 110, 110};
		rt_tongj.setBody(new Table(ArrBody_tongj, 0, 0, 0));
		rt_tongj.body.setWidth(ArrWidth_tongj);
		rt_tongj.body.setRowCells(1, Table.PER_ALIGN, Table.ALIGN_CENTER);
		rt_tongj.body.setRowCells(2, Table.PER_ALIGN, Table.ALIGN_CENTER);
		rt_tongj.body.setRowCells(3, Table.PER_ALIGN, Table.ALIGN_CENTER);
		rt_tongj.body.setRowHeight(28);
		
		strsb.append(rt_tongj.getAllPagesHtml());
//		采样方式及总样重量_结束
		
		
		_CurrentPage = 1;
		_AllPages = 1;
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		rslData.close();
		rsl_wenzmx.close();
		con.Close();
		return strsb.toString();
	}
	
	public void getSelectData() {
		
		Visit visit = (Visit)this.getPage().getVisit();
		
		Toolbar tbr = new Toolbar("tbdiv");
		DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win, "diancTree", "" + visit.getDiancxxb_id(), "", null, getTreeid());
		setTree(dt);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		tf.setValue(((IDropDownModel) getDiancmcModel())
			.getBeanValue(Long.parseLong(getTreeid() == null || "".equals(getTreeid()) ? "-1" : getTreeid())));
		
		ToolbarButton tb2 = new ToolbarButton(null, null,"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);
		tbr.addText(new ToolbarText("电厂："));
		tbr.addField(tf);
		tbr.addItem(tb2);
		tbr.addText(new ToolbarText("-"));
		
		tbr.addText(new ToolbarText("采样日期："));
		DateField df = new DateField();
		df.setValue(getCaiyrq());
		df.setId("caiyrq");
		df.Binding("Caiyrq", "forms[0]");
//		df.setListeners("change:function(){document.forms[0].submit();}");
		tbr.addField(df);
		tbr.addText(new ToolbarText("-"));
		
		ToolbarButton tbrtn = new ToolbarButton(null, "查询", "function(){document.getElementById('RefurbishButton').click();}");
		tbrtn.setIcon(SysConstant.Btn_Icon_Search);
		tbr.addItem(tbrtn);
		setToolbar(tbr);
	}
	
	/**
	 * 给字符串加上换行符
	 */
	public String addEnter(String str) {
		if (str != "") {
			String[] arr = str.split(",");
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < arr.length; i ++) {
				sb.append(arr[i] + ",");
				if (sb.toString().split(",").length % 2 == 0) {
					sb.append("<br>");
				}
			}
			if (sb.toString().endsWith("<br>")) {
				return sb.toString();
			} else {
				return sb.substring(0, sb.length() - 1);
			}
		} else {
			return "";
		}
	}
	
	/**
	 * String型日期转成Data型日期
	 * @param date
	 * @return
	 */
	public static Date stringToDate(String date){
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date temD = new Date();
		
		if(date == null){
			date = DateUtil.FormatDate(new Date());
		}
		try {
			temD = sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return temD;
	}
	
	public Toolbar getToolbar() {
		return ((Visit)this.getPage().getVisit()).getToolbar();
	}
	
	public void setToolbar(Toolbar tb1) {
		((Visit)this.getPage().getVisit()).setToolbar(tb1);
	}
	
	public String getToolbarScript() {
		if(getTbmsg()!=null) {
			getToolbar().deleteItem();
			getToolbar().addText(new ToolbarText("<marquee width=300 scrollamount=2>"+getTbmsg()+"</marquee>"));
		}
		return getToolbar().getRenderScript();
	}
	
//	电厂树_开始
	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
			getDiancmcModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public void setDiancmcModel(IPropertySelectionModel _value) {
		((Visit) getPage().getVisit()).setProSelectionModel1(_value);
	}

	public void getDiancmcModels() {
		String sql = "select id, mingc from diancxxb";
		setDiancmcModel(new IDropDownModel(sql));
	}

	public String getTreeid() {
		String treeid = ((Visit) getPage().getVisit()).getString3();
		if (treeid == null || treeid.equals("")) {
			((Visit) getPage().getVisit()).setString3(String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString3();
	}

	public void setTreeid(String treeid) {
		((Visit) getPage().getVisit()).setString3(treeid);
	}

	public DefaultTree getTree() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree();
	}

	public void setTree(DefaultTree etu) {
		((Visit) this.getPage().getVisit()).setDefaultTree(etu);
	}

	public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}
//	电厂树_结束

	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			this.setTreeid(null);
			setCaiyrq(DateUtil.FormatDate(new Date()));
		}
		getSelectData();
	}

	public void pageValidate(PageEvent arg0) {
		String PageName = arg0.getPage().getPageName();
		String ValPageName = Login.ValidateLogin(arg0.getPage());
		if (!PageName.equals(ValPageName)) {
			ValPageName = Login.ValidateAdmin(arg0.getPage());
			if (!PageName.equals(ValPageName)) {
				IPage ipage = arg0.getRequestCycle().getPage(ValPageName);
				throw new PageRedirectException(ipage);
			}
		}
	}
}