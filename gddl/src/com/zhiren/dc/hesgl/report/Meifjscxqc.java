package com.zhiren.dc.hesgl.report;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.LovComboBox;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
/*
 *�޸��ˣ����� 
 *�޸����ڣ�2010-09-21 
 *�޸����ݣ����������ʽ�����޸ĺϲ�
 */
public class Meifjscxqc extends BasePage {
	// �����û���ʾ
	private String msg = "";
	
	public String getMsg() {
		return msg;
	}
	
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}
	
	protected void initialize() {
		super.initialize();
		setMsg("");
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
	
	private boolean blnIsBegin = false;

	public String getPrintTable() {
		if (!blnIsBegin) {
			return "";
		}
		blnIsBegin = false;
		return getSelectData();
	}
	
	// ҳ��仯��¼
	private String Change="1";

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
	public boolean getRaw() {
		return true;
	}
	private String riq;
	
	public void setRiq(String riq){
		this.riq = riq;
	}
	public String getRiq(){
		return riq;
	}
	
	// �����Ʊ���Ĭ�ϵ�ǰ�û�
	private String getZhibr(){
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String zhibr = "";
		String zhi = "��";
		String sql = "select zhi from xitxxb where mingc = '�±������Ʊ����Ƿ�Ĭ�ϵ�ǰ�û�' " +
								"and diancxxb_id = " + visit.getDiancxxb_id();
		ResultSet rs = con.getResultSet(sql);
		try {
		  while(rs.next()) {
			  zhi = rs.getString("zhi");
		  }
		} catch (Exception e) {
			System.out.println(e);
		}
		if (zhi.equals("��")) {
			zhibr = visit.getRenymc();
		}	
		return zhibr;
	}
	
	// ���ѡ������ڵ�Ķ�Ӧ�ĵ糧����   
    private String getDcMingc(String id) { 
    	if(id == null || "".equals(id)) {
    		return "";
    	}
		JDBCcon con = new JDBCcon();
		String mingc = "";
		String sql = "select mingc from diancxxb where id = " + id;
		ResultSetList rsl = con.getResultSetList(sql);
		if (rsl.next()) {
			mingc = rsl.getString("mingc");
		}
		rsl.close();
		con.Close();
		return mingc;
	}
    
    // ˢ�ºⵥ�б�
	public void initToolbar() {
		Visit visit = (Visit) this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");
		//ʱ���
		tb1.addText(new ToolbarText("����ʱ��"));
		DateField df = new DateField();
		df.setValue(this.getRiq());
		df.setId("riq");
		df.Binding("riq","Form0");
		tb1.addField(df);
		tb1.addText(new ToolbarText("-"));
		// �糧��
//		DefaultTree dt1 = new DefaultTree(DefaultTree.tree_dianc_win,
//				"diancTree", "" + visit.getDiancxxb_id(), "Form0", null, getTreeid_dc());
//		setTree_dc(dt1);
//		TextField tf1 = new TextField();
//		tf1.setId("diancTree_text");
//		tf1.setWidth(100);
//		tf1.setValue(getDcMingc(getTreeid_dc()));
//		
//		ToolbarButton tb3 = new ToolbarButton(null, null,
//		"function(){diancTree_window.show();}");
//		tb3.setIcon("ext/resources/images/list-items.gif");
//		tb3.setCls("x-btn-icon");
//		tb3.setMinWidth(20);
//		
//		tb1.addText(new ToolbarText("��λ:"));
//		tb1.addField(tf1);
//		tb1.addItem(tb3);
//		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("���㷽��:"));
		LovComboBox jiesfa = new LovComboBox();
		jiesfa.setId("jiesfa");
//		ComboBox jiesfa = new ComboBox();
		jiesfa.setTransform("JiesfaDropDown");
		jiesfa.setWidth(160);
		jiesfa.setForceSelection(false);
		jiesfa.setListeners("select:function(e){document.getElementById('CHANGE').value = e.getValue();}");
		tb1.addField(jiesfa);
//		tb1.addText(new ToolbarText("���㷽��:"));
//		ComboBox jiesfa = new ComboBox();
//		jiesfa.setTransform("JiesfaDropDown");
//		jiesfa.setWidth(100);
//		// nianf.setListeners("select:function(){document.Form0.submit();}");
//		tb1.addField(jiesfa);
		tb1.addText(new ToolbarText("-"));
		
		// ���䷽ʽ
		tb1.addText(new ToolbarText("���䷽ʽ:"));
		ComboBox leix = new ComboBox();
		leix.setTransform("YunsfsDropDown");
		leix.setId("YunsfsDropDown");
		leix.setLazyRender(true);// ��̬��
		leix.setWidth(70);
		tb1.addField(leix);;
		tb1.addText(new ToolbarText("-"));
		
		ToolbarButton rbtn = new ToolbarButton(null,"��ѯ","function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
		tb1.setWidth("bodyWidth");
		tb1.addItem(rbtn);
		setToolbar(tb1);
	}
	
	private String getSelectData(){
		
		JDBCcon con = new JDBCcon();
		
		String Ttitle = "";
		if (getYunsfsValue().getId() == 1) {
			Ttitle = "��ú�ѽ�����ܱ�";
		} else if (getYunsfsValue().getId() == 2) {
			Ttitle = "����ú�ѽ�����ܱ�";
		}
		
		String time= "0";
		StringBuffer leijfaids = new StringBuffer("");
		
		String sql = 
				"SELECT TO_CHAR(FA.DAOHJZSJ, 'yyyy') || '��' || TO_CHAR(FA.DAOHJZSJ, 'MM') || '��' AS TITLE\n" +
				"\tFROM JIESFAB FA\n" + 
				" WHERE FA.ID ="+this.getJiesfaValue().getId();
	
		ResultSetList rstmp = con.getResultSetList(sql);
		if(rstmp.next()){
			
			time = rstmp.getString("TITLE");
		}
		
		
		sql= 
			"SELECT FA.ID\n" +
			"\tFROM JIESFAB FA,\n" + 
			"\t\t\t (SELECT JIESDW_ID, DAOHJZSJ FROM JIESFAB WHERE ID = "+getJiesfaValue().getId()+") A\n" + 
			" WHERE FA.JIESDW_ID = A.JIESDW_ID\n" + 
			"\t AND FA.DAOHJZSJ <= A.DAOHJZSJ\n" + 
			"\t AND FA.JIESLX = "+Locale.meikjs_feiylbb_id;
		
		rstmp = con.getResultSetList(sql);
		leijfaids.setLength(0);
		while(rstmp.next()){
			
			leijfaids.append(rstmp.getString("id")).append(",");
		}
		
		if(leijfaids.length()>0){
			
			leijfaids.deleteCharAt(leijfaids.length()-1);
		}else{
			
			leijfaids.append("0");
		}
		
		if(this.getYunsfsValue().getId()==1){
//			��·
			
			sql = 
				"select decode(grouping(biaot.leib), 1, '�ܼ�', biaot.leib) leib,\n" +
				"       decode(grouping(biaot.leib) + grouping(biaot.shoukdw), 2, '�ܼ�', 1, 'С��', biaot.shoukdw) shoukdw,\n" + 
				"       decode(grouping(biaot.leib) + grouping(biaot.mkmc), 2, '�ܼ�', 1, 'С��', biaot.mkmc) mkmc,\n" + 
				"       decode(grouping(biaot.leib) + grouping(biaot.mkmc), 2, '�ܼ�', 1, 'С��', sum(nvl(meil,0))) meil,\n" + 
				"       decode(grouping(biaot.leib) + grouping(biaot.mkmc), 2, '�ܼ�', 1, 'С��', decode(sum(nvl(jiessl,0)), 0, 0, round_new(sum(hetj * jiessl) / sum(jiessl),2))) hetj,\n" + 
				"       decode(grouping(biaot.leib) + grouping(biaot.mkmc), 2, '�ܼ�', 1, 'С��', decode(biaoz, 1, kcalkg_mjkg(hetrz,'-',2), hetrz)) hetrz,\n" + 
				"       biaot.leix,\n" + 
				"       sum(jiessl),\n" + 
				"       round_new(decode(sum(jiessl), 0, 0, sum(jsrz * jiessl) / sum(jiessl)), 2),\n" + 
				"       round_new(decode(sum(jiessl), 0, 0, sum(liuf * jiessl) / sum(jiessl)), 2),\n" + 
				"       round_new(decode(sum(jiessl), 0, 0, sum(rljc * jiessl) / sum(jiessl)), 2),\n" + 
				"       round_new(decode(sum(jiessl), 0, 0, sum(lufkk * jiessl) / sum(jiessl)), 2),\n" + 
				"       round_new(decode(biaoz, 2, jsmj, decode(sum(jiessl), 0, 0, sum(jsmj * jiessl) / sum(jiessl))), 2),\n" + 
				"       sum(buhsmk),\n" + 
				"       sum(shuik),\n" + 
				"       sum(hansmk),\n" + 
				"       ''\n" + 
				"  from (select mk.leib, js.shoukdw, mk.mingc as mkmc, '����' as fenx,\n" + 
				"               sum((select getjiesdzb('jiesb', js.id, '"+Locale.jiessl_zhibb+"', 'hetbz') from dual)) as meil,\n" + 
				"               decode(sum(nvl(js.jiessl,0)), 0, 0, round_new(sum(js.hetj * js.jiessl) / sum(js.jiessl),2)) as hetj,\n" + 
				"               getjiesdzb('jiesb', sum(js.id), 'Qnetar', 'hetbz')as hetrz,\n" + 
				"				1 as biaoz,\n" + 
				"               sum(nvl(js.jiessl,0)) as jiessl,\n" + 
				"               decode(sum(nvl(js.jiessl,0)), 0, 0, round_new(kcalkg_to_mjkg(sum(js.jiesrl * js.jiessl) / sum(js.jiessl),2), 2)) as jsrz,\n" + 
				"               decode(sum(js.jiessl), 0, 0, round_new(sum(js.jieslf * js.jiessl) / sum(js.jiessl),2)) as liuf,\n" + 
				"               decode(sum(js.jiessl), 0, 0, round_new(sum(getjiesdzb('jiesb', js.id, 'Qnetar', 'zhejbz') * js.jiessl) / sum(js.jiessl),2)) as rljc,\n" + 
				"               decode(sum(js.jiessl), 0, 0, round_new(sum(-getjiesdzb('jiesb', js.id, 'Std', 'zhejbz') * js.jiessl) / sum(js.jiessl),2)) as lufkk,\n" + 
				"               round_new(decode(sum(js.jiessl), 0, 0, sum(js.hansdj * js.jiessl) / sum(js.jiessl)), 2) as jsmj,\n" + 
				"               sum(js.buhsmk) as buhsmk,\n" + 
				"               sum(js.shuik) as shuik,\n" + 
				"               sum(js.hansmk) as hansmk,\n" + 
				"               ''\n" + 
				"          from jiesb js, meikxxb mk\n" + 
				"         where js.meikxxb_id = mk.id\n" + 
//				"           and js.diancxxb_id = "+getTreeid_dc()+"\n" + 
				"           and js.yunsfsb_id = "+getYunsfsValue().getId()+"\n" + 
				"           and js.jiesfab_id  in ("+this.getChange()+")\n" + 
				"         group by (mk.leib, js.shoukdw, mk.mingc)\n" + 
				"		 union\n" +
				"        select mk.leib, bj.jiesdw as shoukdw, bj.meikxxb_id as mkmc, '����' as fenx,\n" + 
				"               0 as meil, 0 as hetj, bj.miaos as hetrz, 2 as biaoz, 0 as jiessl, 0 as jsrz, 0 as liuf,\n" + 
				"               0 as rljc, 0 as lufkk, bj.danj as jsmj, bj.meik as buhsmk, bj.shuij as shuik, bj.meik + bj.shuij as hansmk, ''\n" + 
				"          from jiesb js, meikxxb mk, bujb bj\n" + 
				"         where bj.jiesdw = js.shoukdw\n" + 
				"           and js.meikxxb_id = mk.id\n" + 
//				"           and js.diancxxb_id = " + getTreeid_dc() + "\n" + 
				"           and js.yunsfsb_id = " + getYunsfsValue().getId() + "\n" + 
				"           and bj.jiesfab_id = js.jiesfab_id\n" + 
				"           and js.jiesfab_id in( " +this.getChange()+ ")\n" + 
				"        union\n" + 
				"        select mk.leib, js.shoukdw, mk.mingc as mkmc, '�ۼ�' as fenx,\n" + 
				"               sum((select getjiesdzb('jiesb', js.id, '��������', 'hetbz') from dual)) as meil,\n" + 
				"               decode(sum(nvl(js.jiessl,0)), 0, 0, round_new(sum(js.hetj * js.jiessl) / sum(js.jiessl),2)) as hetj,\n" + 
				"               getjiesdzb('jiesb', sum(js.id), 'Qnetar', 'hetbz')as hetrz,\n" + 
				"				1 as biaoz,\n" + 
				"               sum(nvl(js.jiessl,0)) as jiessl,\n" + 
				"               decode(sum(nvl(js.jiessl,0)), 0, 0, round_new(kcalkg_to_mjkg(sum(js.jiesrl * js.jiessl) / sum(js.jiessl),2), 2)) as jsrz,\n" + 
				"               decode(sum(js.jiessl), 0, 0, round_new(sum(js.jieslf * js.jiessl) / sum(js.jiessl),2)) as liuf,\n" + 
				"               decode(sum(js.jiessl), 0, 0, round_new(sum(getjiesdzb('jiesb', js.id, 'Qnetar', 'zhejbz') * js.jiessl) / sum(js.jiessl),2)) as rljc,\n" + 
				"               decode(sum(js.jiessl), 0, 0, round_new(sum(-getjiesdzb('jiesb', js.id, 'Std', 'zhejbz') * js.jiessl) / sum(js.jiessl),2)) as lufkk,\n" + 
				"               round_new(decode(sum(js.jiessl), 0, 0, sum(js.hansdj * js.jiessl) / sum(js.jiessl)), 2) as jsmj,\n" + 
				"               sum(js.buhsmk) as buhsmk,\n" + 
				"               sum(js.shuik) as shuik,\n" + 
				"               sum(js.hansmk) as hansmk,\n" + 
				"               ''\n" + 
				"          from jiesb js, meikxxb mk\n" + 
				"         where js.meikxxb_id = mk.id\n" + 
//				"           and js.diancxxb_id = "+getTreeid_dc()+"\n" + 
				"           and js.yunsfsb_id = "+getYunsfsValue().getId()+"\n" + 
				"           and js.jiesfab_id IN ("+leijfaids.toString()+")\n" + 
				"         group by (mk.leib, js.shoukdw, mk.mingc)" + 
				"		 union\n" +
				"        select mk.leib, bj.jiesdw as shoukdw, bj.meikxxb_id as mkmc, '�ۼ�' as fenx,\n" + 
				"               0 as meil, 0 as hetj, bj.miaos as hetrz, 2 as biaoz, 0 as jiessl, 0 as jsrz, 0 as liuf,\n" + 
				"               0 as rljc, 0 as lufkk, bj.danj as jsmj, bj.meik as buhsmk, bj.shuij as shuik, bj.meik + bj.shuij as hansmk, ''\n" + 
				"          from jiesb js, meikxxb mk, bujb bj\n" + 
				"         where bj.jiesdw = js.shoukdw\n" + 
				"           and js.meikxxb_id = mk.id\n" + 
//				"           and js.diancxxb_id = " + getTreeid_dc() + "\n" + 
				"           and js.yunsfsb_id = " + getYunsfsValue().getId() + "\n" + 
				"           and bj.jiesfab_id in (" + leijfaids.toString() + ")) shuj,\n" + 
				"       (select *\n" + 
				"          from (select distinct leib, shoukdw, mkmc\n" + 
				"                  from (select mk.leib, js.shoukdw, mk.mingc as mkmc\n" + 
				"                          from jiesb js, meikxxb mk\n" + 
				"                         where js.meikxxb_id = mk.id\n" + 
//				"                           and js.diancxxb_id = "+getTreeid_dc()+"\n" + 
				"                           and js.yunsfsb_id = 1\n" + 
				"                   and js.jiesfab_id in ("+leijfaids.toString()+")\n" + 
				"						 union\n" +
				"                        select mk.leib, bj.jiesdw as shoukdw, bj.meikxxb_id as mkmc\n" + 
				"                          from jiesb js, meikxxb mk, bujb bj\n" + 
				"                         where bj.jiesdw = js.shoukdw\n" + 
				"                           and js.meikxxb_id = mk.id\n" + 
//				"                           and js.diancxxb_id = " + getTreeid_dc() + "\n" + 
				"                           and js.yunsfsb_id = " + getYunsfsValue().getId() + "\n" + 
				"                           and bj.jiesfab_id in (" + leijfaids.toString() + "))) s,\n" + 
				"               (select '����' as leix from dual\n" + 
				"                union\n" + 
				"                select '�ۼ�' as leix from dual) l) biaot\n" + 
				" where biaot.leib = shuj.leib\n" + 
				"   and biaot.shoukdw = shuj.shoukdw\n" + 
				"   and biaot.mkmc = shuj.mkmc\n" + 
				"   and biaot.leix = shuj.fenx\n" + 
				" group by rollup(biaot.leix, biaot.leib, biaot.shoukdw, biaot.mkmc,shuj.hetrz, shuj.biaoz, shuj.jsmj)\n" + 
				" having not ((grouping(shuj.jsmj)=1 and grouping(biaot.shoukdw)=0) or grouping(biaot.leix)=1)\n" + 
				" order by grouping(biaot.leib), biaot.leib, grouping(biaot.shoukdw), biaot.shoukdw, grouping(biaot.mkmc), biaot.mkmc, grouping(biaot.leix), biaot.leix";
			
		}else if(this.getYunsfsValue().getId()==2){
//			��·
			
			sql = 
				"select decode(grouping(biaot.leib), 1, 'ú�Ѻϼ�', biaot.leib) leib,\n" +
				"       decode(grouping(biaot.leib) + grouping(biaot.gysmc), 2, 'ú�Ѻϼ�', 1, biaot.leib, biaot.gysmc) gysmc,\n" + 
				"       decode(grouping(biaot.leib) + grouping(biaot.gysmc) + grouping(biaot.shoukdw), 3, 'ú�Ѻϼ�', 2, '�ϼ�', 1, to_char(COUNT(biaot.mkmc)), biaot.shoukdw) shoukdw,\n" + 
				"       decode(grouping(biaot.leib) + grouping(biaot.gysmc) + grouping(biaot.mkmc), 3, 'ú�Ѻϼ�', 2, '�ϼ�', 1, to_char(COUNT(biaot.mkmc)), biaot.mkmc) mkmc,\n" + 
				"       decode(grouping(biaot.leib) + grouping(biaot.gysmc) + grouping(biaot.hetbh), 3, 'ú�Ѻϼ�', 2, '�ϼ�', 1, 'С��', biaot.hetbh) hetbh,\n" + 
				"       decode(grouping(biaot.leib) + grouping(biaot.gysmc) + grouping(biaot.hetbh), 3, 'ú�Ѻϼ�', 2, '�ϼ�', 1, 'С��',sum(meil)) meil,\n" + 
				"       decode(grouping(biaot.leib) + grouping(biaot.gysmc) + grouping(biaot.hetbh), 3, 'ú�Ѻϼ�', 2, '�ϼ�', 1, 'С��', decode(sum(nvl(jiessl,0)), 0, 0, round_new(sum(hetj * jiessl) / sum(jiessl),2))) hetj,\n" + 
				"       decode(grouping(biaot.leib) + grouping(biaot.gysmc) + grouping(biaot.hetbh), 3, 'ú�Ѻϼ�', 2, '�ϼ�', 1, 'С��', kcalkg_mjkg(hetrz,'-',2)) hetrz,\n" + 
				"       biaot.leix,\n" + 
				"       sum(jiessl),\n" + 
				"       sum(ches),\n" + 
				"       round_new(decode(sum(jiessl), 0, 0, sum(jsrz * jiessl) / sum(jiessl)), 2),\n" + 
				"       round_new(decode(sum(jiessl), 0, 0, sum(rzsj * jiessl) / sum(jiessl)), 2),\n" + 
				"       round_new(decode(sum(jiessl), 0, 0, sum(rljc * jiessl) / sum(jiessl)), 2),\n" + 
				"       round_new(decode(sum(jiessl), 0, 0, sum(liuf * jiessl) / sum(jiessl)), 2),\n" + 
				"       round_new(decode(sum(jiessl), 0, 0, sum(lufkk * jiessl) / sum(jiessl)), 2),\n" + 
				"       round_new(decode(biaoz, 2, jsmj, decode(sum(jiessl), 0, 0, sum(jsmj * jiessl) / sum(jiessl))), 2),\n" + 
				"       sum(buhsmk),\n" + 
				"       sum(shuik),\n" + 
				"       sum(hansmk),\n" + 
				"       ''\n" + 
				"  from (select mk.leib, gys.mingc as gysmc, js.shoukdw, mk.mingc as mkmc, ht.hetbh, '����' as fenx,\n" + 
				"               sum((select getjiesdzb('jiesb', js.id, '��������', 'hetbz') from dual)) as meil,\n" + 
				"               decode(sum(nvl(js.jiessl,0)), 0, 0, round_new(sum(js.hetj * js.jiessl) / sum(js.jiessl),2)) as hetj,\n" + 
				"               getjiesdzb('jiesb', sum(js.id), 'Qnetar', 'hetbz')as hetrz,\n" + 
				"				1 as biaoz,\n" + 
				"               sum(js.jiessl) as jiessl,\n" + 
				"               sum(js.ches) as ches,\n" + 
				"               decode(sum(nvl(js.jiessl,0)), 0, 0, round_new(kcalkg_to_mjkg(sum(js.jiesrl * js.jiessl) / sum(js.jiessl),2), 2)) as jsrz,\n" + 
				"               round_new(kcalkg_to_mjkg(decode(sum(js.jiessl), 0, 0, sum(getjiesdzb('jiesb', js.id, 'Qnetar', 'yingk') * js.jiessl) / sum(js.jiessl)), 2), 2) rzsj,\n" + 
				"               decode(sum(js.jiessl), 0, 0, round_new(sum(getjiesdzb('jiesb', js.id, 'Qnetar', 'zhejbz') * js.jiessl) / sum(js.jiessl),2)) as rljc,\n" + 
				"               decode(sum(js.jiessl), 0, 0, round_new(sum(js.jieslf * js.jiessl) / sum(js.jiessl),2)) as liuf,\n" + 
				"               decode(sum(js.jiessl), 0, 0, round_new(sum(-getjiesdzb('jiesb', js.id, 'Std', 'zhejbz') * js.jiessl) / sum(js.jiessl),2)) as lufkk,\n" + 
				"               round_new(decode(sum(js.jiessl), 0, 0, sum(js.hansdj * js.jiessl) / sum(js.jiessl)), 2) as jsmj,\n" + 
				"               sum(js.buhsmk) as buhsmk,\n" + 
				"               sum(js.shuik) as shuik,\n" + 
				"               sum(js.hansmk) as hansmk,\n" + 
				"               ''\n" + 
				"          from jiesb js, meikxxb mk, gongysb gys, hetb ht\n" + 
				"         where js.meikxxb_id = mk.id\n" + 
				"           and mk.meikdq_id = gys.id\n" + 
				"           and js.hetb_id = ht.id\n" + 
//				"           and js.diancxxb_id = " + getTreeid_dc() + "\n" + 
				"           and js.yunsfsb_id = "+getYunsfsValue().getId()+"\n" + 
				"           and js.jiesfab_id in( " +this.getChange() + ")\n"+
				"         group by (mk.leib, gys.mingc, js.shoukdw, mk.mingc, ht.hetbh)\n" + 
				"		 union\n" +
				"        select mk.leib, gys.mingc as gysmc, bj.jiesdw as shoukdw, bj.meikxxb_id as mkmc, bj.miaos as hetbh, '����' as fenx,\n" + 
				"               0 as meil, 0 as hetj, '' as hetrz, 2 as biaoz, 0 as jiessl, 0 as ches, 0 as jsrz, 0 as rzsj,\n" + 
				"               0 as rljc, 0 as liuf, 0 as lufkk, bj.danj as jsmj, bj.meik as buhsmk, bj.shuij as shuik, bj.meik + bj.shuij as hansmk, ''\n" + 
				"          from jiesb js, meikxxb mk, gongysb gys, bujb bj\n" + 
				"         where bj.jiesdw = js.shoukdw\n" + 
				"           and js.meikxxb_id = mk.id\n" + 
				"           and mk.meikdq_id = gys.id\n" + 
//				"           and js.diancxxb_id = " + getTreeid_dc() + "\n" + 
				"           and js.yunsfsb_id = " + getYunsfsValue().getId() + "\n" + 
				"           and bj.jiesfab_id = js.jiesfab_id\n" + 
				"           and js.jiesfab_id in (" +this.getChange()+ ")\n" + 
				"        union\n" + 
				"        select mk.leib, gys.mingc as gysmc, js.shoukdw, mk.mingc as mkmc, ht.hetbh, '�ۼ�' as fenx,\n" + 
				"               sum((select getjiesdzb('jiesb', js.id, '��������', 'hetbz') from dual)) as meil,\n" + 
				"               decode(sum(nvl(js.jiessl,0)), 0, 0, round_new(sum(js.hetj * js.jiessl) / sum(js.jiessl),2)) as hetj,\n" + 
				"               getjiesdzb('jiesb', sum(js.id), 'Qnetar', 'hetbz')as hetrz,\n" + 
				"				1 as biaoz,\n" + 
				"               sum(js.jiessl) as jiessl,\n" + 
				"               sum(js.ches) as ches,\n" + 
				"               decode(sum(nvl(js.jiessl,0)), 0, 0, round_new(kcalkg_to_mjkg(sum(js.jiesrl * js.jiessl) / sum(js.jiessl),2), 2)) as jsrz,\n" + 
				"               round_new(kcalkg_to_mjkg(decode(sum(js.jiessl), 0, 0, sum(getjiesdzb('jiesb', js.id, 'Qnetar', 'yingk') * js.jiessl) / sum(js.jiessl)), 2), 2) rzsj,\n" + 
				"               decode(sum(js.jiessl), 0, 0, round_new(sum(getjiesdzb('jiesb', js.id, 'Qnetar', 'zhejbz') * js.jiessl) / sum(js.jiessl),2)) as rljc,\n" + 
				"               decode(sum(js.jiessl), 0, 0, round_new(sum(js.jieslf * js.jiessl) / sum(js.jiessl),2)) as liuf,\n" + 
				"               decode(sum(js.jiessl), 0, 0, round_new(sum(-getjiesdzb('jiesb', js.id, 'Std', 'zhejbz') * js.jiessl) / sum(js.jiessl),2)) as lufkk,\n" + 
				"               round_new(decode(sum(js.jiessl), 0, 0, sum(js.hansdj * js.jiessl) / sum(js.jiessl)), 2) as jsmj,\n" + 
				"               sum(js.buhsmk) as buhsmk,\n" + 
				"               sum(js.shuik) as shuik,\n" + 
				"               sum(js.hansmk) as hansmk,\n" + 
				"               ''\n" + 
				"          from jiesb js, meikxxb mk, gongysb gys, hetb ht\n" + 
				"         where js.meikxxb_id = mk.id\n" + 
				"           and mk.meikdq_id = gys.id(+)\n" + 
				"           and js.hetb_id = ht.id\n" + 
//				"           and js.diancxxb_id = " + getTreeid_dc() + "\n" + 
				"           and js.yunsfsb_id = " + getYunsfsValue().getId() + "\n" + 
				"           and js.jiesfab_id in ("+leijfaids+")\n"+ 
				"         group by (mk.leib, gys.mingc, js.shoukdw, mk.mingc, ht.hetbh)" +
				"		 union\n" +
				"        select mk.leib, gys.mingc as gysmc, bj.jiesdw as shoukdw, bj.meikxxb_id as mkmc, bj.miaos as hetbh, '�ۼ�' as fenx,\n" + 
				"               0 as meil, 0 as hetj, '' as hetrz, 2 as biaoz, 0 as jiessl, 0 as ches, 0 as jsrz, 0 as rzsj,\n" + 
				"               0 as rljc, 0 as liuf, 0 as lufkk, bj.danj as jsmj, bj.meik as buhsmk, bj.shuij as shuik, bj.meik + bj.shuij as hansmk, ''\n" + 
				"          from jiesb js, meikxxb mk, gongysb gys, bujb bj\n" + 
				"         where bj.jiesdw = js.shoukdw\n" + 
				"           and js.meikxxb_id = mk.id\n" + 
				"           and mk.meikdq_id = gys.id(+)\n" + 
//				"           and js.diancxxb_id = " + getTreeid_dc() + "\n" + 
				"           and js.yunsfsb_id = " + getYunsfsValue().getId() + "\n" + 
				"           and bj.jiesfab_id in (" + leijfaids + ")) shuj,\n" + 
				"       (select *\n" + 
				"          from (select distinct leib, gysmc, shoukdw, mkmc, hetbh\n" + 
				"                  from (select mk.leib, gys.mingc as gysmc, js.shoukdw, mk.mingc as mkmc, ht.hetbh\n" + 
				"                          from jiesb js, meikxxb mk, gongysb gys, hetb ht\n" + 
				"                         where js.meikxxb_id = mk.id\n" + 
				"                           and mk.meikdq_id = gys.id\n" + 
				"                           and js.hetb_id = ht.id\n" + 
//				"                           and js.diancxxb_id = " + getTreeid_dc() + "\n" + 
				"           \t\t\t\t        and js.yunsfsb_id = " + getYunsfsValue().getId() + "\n" + 
				"           				and js.jiesfab_id in ("+leijfaids+")\n" + 
				"						 union\n" +
				"                        select mk.leib, gys.mingc as gysmc, bj.jiesdw as shoukdw, bj.meikxxb_id as mkmc, bj.miaos as hetbh\n" + 
				"                          from jiesb js, meikxxb mk, gongysb gys, bujb bj\n" + 
				"                         where bj.jiesdw = js.shoukdw\n" + 
				"                           and js.meikxxb_id = mk.id\n" + 
				"                           and mk.meikdq_id = gys.id\n" + 
//				"                           and js.diancxxb_id = " + getTreeid_dc() + "\n" + 
				"                           and js.yunsfsb_id = " + getYunsfsValue().getId() + "\n" + 
				"                           and bj.jiesfab_id in (" + leijfaids + "))) s,\n" + 
				"               (select '����' as leix from dual\n" + 
				"                union\n" + 
				"                select '�ۼ�' as leix from dual) l) biaot\n" + 
				" where biaot.leib = shuj.leib\n" + 
				"   and biaot.gysmc = shuj.gysmc\n" + 
				"   and biaot.shoukdw = shuj.shoukdw\n" + 
				"   and biaot.mkmc = shuj.mkmc\n" + 
				"   and biaot.leix = shuj.fenx\n" + 
				"   and biaot.hetbh = shuj.hetbh\n" + 
				" group by rollup(biaot.leix, biaot.leib, biaot.gysmc, biaot.shoukdw, biaot.mkmc, biaot.hetbh,shuj.hetrz, shuj.biaoz, shuj.jsmj)\n" + 
				"  having not ((grouping(shuj.jsmj) = 1 and grouping(biaot.shoukdw) = 0) or grouping(biaot.leix) = 1 or grouping(shuj.hetrz)+grouping(biaot.hetbh)=1)\n" + 
				" order by grouping(biaot.leib), biaot.leib, grouping(biaot.gysmc), biaot.gysmc, grouping(biaot.shoukdw), biaot.shoukdw, grouping(biaot.mkmc), biaot.mkmc, grouping(biaot.hetbh), biaot.hetbh, grouping(biaot.leix), biaot.leix";
			
		}
		
		Report rt = new Report();
		rstmp = con.getResultSetList(sql);
		ResultSetList rs = null;
		String[][] ArrHeader = null;
		String[] strFormat = null;
		int[] ArrWidth = null;
		String [] Zidm = null;

    	rs = rstmp;
    	
    	
    	
    	if(this.getYunsfsValue().getId()==1){
//    		��·
    		
    		 ArrHeader =new String[3][17];
    		 ArrHeader[0]=new String[] {"ú������","���㵥λ","ú������","��ֵͬ","��ֵͬ","��ֵͬ","���¼��ۼ�","����ú��","ú�����ս��","ú�����ս��","��ֵ<br>��+��-","��ݿۿ�","����ú��","����ú��","����ú��","����ú��","��������"};
    		 ArrHeader[1]=new String[] {"ú������","���㵥λ","ú������","ú����","ú��<br>Ԫ/��","��ֵMJ/kg","���¼��ۼ�","����ú��","��ֵ","���","��ֵ<br>��+��-","��ݿۿ�","����ú��","ú��","˰��","С��","��������"};
    		 ArrHeader[2]=new String[] {"ú������","���㵥λ","ú������","ú����","ú��<br>Ԫ/��","��ֵMJ/kg","���¼��ۼ�","��","MJ/kg","%","Ԫ/��","Ԫ/��","Ԫ/��","Ԫ","Ԫ","Ԫ","Ԫ"};

    		ArrWidth = new int[] {80, 80, 150, 100, 100, 70, 70, 70, 50, 70, 70, 70, 70,70,70,70,70};
    		
    	}else if(this.getYunsfsValue().getId()==2){
//    		��·
    		
    		ArrHeader = new String[2][21];
        	ArrHeader[0] = new String[] {"ú������","��������", "���㵥λ", "ú������", "��ֵͬ","��ֵͬ", 
        						"��ֵͬ","��ֵͬ", "���¼�<br>�ۼ�", "����ú��", "����", "ʵ��<br>��ֵ", 
        						"��ֵ����<br>��(+)��(-)", "��ֵ����<br>��(+)��(-)", "ʵ��<br>���", "���<br>�ۿ�", 
        						"����<br>ú��", "ú��", "˰��", "С��", "��������"};
        	ArrHeader[1] = new String[] {"ú������","��������","���㵥λ", "ú������","��ͬ���", "ú����", 
    							"ú��<br>Ԫ/��", "��ֵ<br>MJ/kg", "���¼�<br>�ۼ�", "����ú��", "��", "MJ/kg", 
    							"MJ/kg", "Ԫ/��", "%", "Ԫ/��", "Ԫ/��", "Ԫ", "Ԫ", "Ԫ", "Ԫ"};
        	
    		ArrWidth = new int[] {80, 80, 150, 100, 100, 70, 70, 70, 50, 70, 70, 70, 70, 70, 70, 70, 
    							   70, 70, 70, 70, 70};
    		
    	}
    	
    	
		ArrWidth = rt.getArrWidth(ArrWidth, 790);
		rt.setTitle(Ttitle, ArrWidth);
        // ����ҳ��
//		rt.setMarginBottom(rt.getMarginBottom() + 25);
		
		if(this.getYunsfsValue().getId()==1){
//			��·
			rt.setBody(new Table(rs, 3, 0, 6));
			
		}else if(this.getYunsfsValue().getId()==2){
//			��·
			rt.setBody(new Table(rs, 2, 0, 8));
		}
        
    	rt.body.setWidth(ArrWidth);
    	rt.body.setPageRows(24);
    	rt.body.setHeaderData(ArrHeader);
    	if(this.getYunsfsValue().getId()==2){
    	rt.body.merge(1, 1, 2, 4);
    	rt.body.merge(1, 5, 1, 8);
    	rt.body.merge(1, 9, 2, 9);
    	}
    	if(this.getYunsfsValue().getId()==1){
    		rt.body.merge(1, 1,3, 1);
    		rt.body.merge(1, 2, 3, 2);
    		rt.body.merge(1, 3, 3, 3);
    		rt.body.merge(1, 4, 1, 6);
    		rt.body.merge(2, 4, 3, 4);
    		rt.body.merge(2, 5, 3, 5);
    		rt.body.merge(2, 6, 3, 6);
    		rt.body.merge(1, 7, 3, 7);
    		rt.body.merge(1, 8, 2, 8);
    		rt.body.merge(1, 9, 1, 10);
    		rt.body.merge(1, 11, 2,11);
    		rt.body.merge(1, 12, 2, 12);
    		rt.body.merge(1, 13, 2, 13);
    		rt.body.merge(1, 14, 1, 16);
    		rt.body.merge(1, 17, 2, 17);
    		
    	}
		rt.title.fontSize=10;
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		//��·��ʽ
		if(this.getYunsfsValue().getId()==2){
			for(int m=1;m<=8;m++){
				rt.body.merge(3, m, rt.body.getRows(), m);
			}
			for(int i=1;i<=rt.body.getRows();i++){
				if(rt.body.getCellValue(i, 3).equals("�ϼ�")){
					rt.body.merge(i, 3, i, 8);
					rt.body.setCellAlign(i, 3, Table.ALIGN_CENTER);
				}
				if(rt.body.getCellValue(i, 1).equals("ú�Ѻϼ�")){
					rt.body.merge(i, 2, i, 8);
					rt.body.setCellAlign(i, 2, Table.ALIGN_CENTER);
				}
				if(rt.body.getCellValue(i, 5).equals("С��")){
					rt.body.merge(i, 5, i, 8);
					rt.body.setCellAlign(i, 5, Table.ALIGN_CENTER);
				}
			}
			for(int j=1;j<=rt.body.getRows();j++){
				if(rt.body.getCellValue(j, 1).equals("ú�Ѻϼ�")){
					rt.body.mergeRow(j);
					rt.body.setCellAlign(j, 1, Table.ALIGN_CENTER);
				}
			}
			for(int k=1;k<rt.body.getRows();k++){
				if(rt.body.getCellValue(k, 5).equals("С��")){
					rt.body.merge(k, 3, k, 4);
					rt.body.setCellAlign(k, 3, Table.ALIGN_CENTER);
				}
			}
		}
		//��·��ʽ
		if(this.getYunsfsValue().getId()==1){
			for(int j=1;j<=6;j++){
				rt.body.merge(4, j, rt.body.getRows(), j);
			}
			for(int i=3;i<=rt.body.getRows();i++){
				if(rt.body.getCellValue(i, 2).equals("С��")){
					rt.body.merge(i, 2, i, 6);
					rt.body.setCellAlign(i, 2, Table.ALIGN_CENTER);
				}
			}
			for(int m=3;m<=rt.body.getRows();m++){
				if(rt.body.getCellValue(m, 1).equals("�ܼ�")){
					rt.body.merge(m, 1, m,6);
				    rt.body.setCellAlign(m, 1, Table.ALIGN_CENTER);
				}
			}
		}		
		ResultSetList r = con.getResultSetList("select quanc from diancxxb where id = " + getTreeid_dc());
		String diancmc = "";
		if (r.next()) {
			diancmc = r.getString("quanc");
		}
		r.close();
		
		rt.setDefaultTitle(1, 3, "���λ��" + diancmc, Table.ALIGN_LEFT);
		
		if(this.getYunsfsValue().getId()==1){
//			��·
			rt.setDefaultTitle(15, 2, "����ڣ�"+ time , Table.ALIGN_RIGHT);
		}else if(this.getYunsfsValue().getId()==2){
//			��·
			rt.setDefaultTitle(20, 2, "����ڣ�"+ time , Table.ALIGN_RIGHT);
		}		
		rt.createFooter(1, ArrWidth);		
		if(this.getYunsfsValue().getId()==1){
//			��·
			rt.setDefautlFooter(1, 2, "��˾�쵼��", Table.ALIGN_LEFT);
			rt.setDefautlFooter(4, 2, "����", Table.ALIGN_LEFT);
			rt.setDefautlFooter(7, 2, "ȼ�ϲ���", Table.ALIGN_LEFT);
			rt.setDefautlFooter(11, 2, "��ˣ�", Table.ALIGN_LEFT);
			rt.setDefautlFooter(14, 2, "�Ʊ�" + getZhibr(), Table.ALIGN_LEFT);
			
		}else if(this.getYunsfsValue().getId()==2){
//			��·
			rt.setDefautlFooter(1, 2, "��˾�쵼��", Table.ALIGN_LEFT);
			rt.setDefautlFooter(6, 2, "����", Table.ALIGN_LEFT);
			rt.setDefautlFooter(10, 2, "ȼ�ϲ���", Table.ALIGN_LEFT);
			rt.setDefautlFooter(14, 2, "��ˣ�", Table.ALIGN_LEFT);
			rt.setDefautlFooter(18, 2, "�Ʊ�" + getZhibr(), Table.ALIGN_LEFT);
		}
		
		
		rt.footer.setRowCells(1, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.footer.fontSize=10;
		
		con.Close();
		if(rt.body.getPages()>0) {
			setCurrentPage(1);
			setAllPages(rt.body.getPages());
		}
     	return rt.getAllPagesHtml();
	}
	
//	-------------------------�糧Tree-----------------------------------------------------------------
	public String getTreeid_dc() {
		String treeid = ((Visit) getPage().getVisit()).getString3();
		if (treeid == null || treeid.equals("")) {
			((Visit) getPage().getVisit()).setString3(String
					.valueOf(((Visit) this.getPage().getVisit())
							.getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString3();
	}

	public void setTreeid_dc(String treeid) {
		
		if(!((Visit) getPage().getVisit()).equals(treeid)){
			
			((Visit) getPage().getVisit()).setboolean2(true);
		}
		((Visit) getPage().getVisit()).setString3(treeid);
	}
	
	DefaultTree dc ;

	public DefaultTree getTree_dc() {
		return dc;
	}

	public void setTree_dc(DefaultTree etu) {
		dc = etu;
	}

	public String getTreeScript1() {
		return getTree_dc().getScript();
	}
	
//	-------------------------�糧Tree END-------------------------------------------------------------
	
	public Toolbar getToolbar() {
		return ((Visit)this.getPage().getVisit()).getToolbar();
	}
	
	public void setToolbar(Toolbar tb1) {
		((Visit)this.getPage().getVisit()).setToolbar(tb1);
	}
	
	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}
	
	// ҳ���ʼ��
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		String reportType = cycle.getRequestContext().getParameter("lx");
		if(reportType != null) {
			visit.setInt1(Integer.parseInt(reportType));
		}
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			visit.setboolean1(false);	//���䷽ʽchange
			visit.setboolean2(false);	//�糧Tree_change
			setYunsfsValue(null);		//DropDownBean1
			setIYunsfsModel(null);		//ProSelectionModel1
			setJiesfaValue(null);		//DropDownBean2
			setIJiesfaModel(null);		//ProSelectionModel2
			getIJiesfaModels();	//���㷽��
			getIYunsfsModels();			//���䷽ʽ
			setRiq(DateUtil.FormatDate(new Date()));
			
			setTreeid_dc(visit.getDiancxxb_id() + "");
		}
		initToolbar();
		blnIsBegin = true;
		if(visit.getboolean2()){
			visit.setboolean2(false);
			getIJiesfaModels();
		}
//		this.getSelectData();
	}
	
	// ��ť�ļ����¼�
	private boolean _RefurbishChick = false;
	
	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	// ����ť�ύ����
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			
		}
		initToolbar();
	}
	
	// ҳ���½��֤
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
	
	// ���䷽ʽ
	public IDropDownBean getYunsfsValue() {
		
		if (((Visit) getPage().getVisit()).getDropDownBean1() == null) {
			if (getIYunsfsModels().getOptionCount() > 1) {
				
				((Visit) getPage().getVisit()).setDropDownBean1((IDropDownBean) getIYunsfsModels().getOption(0));
			} else {
				((Visit) getPage().getVisit()).setDropDownBean1(new IDropDownBean(-1, ""));
			}
			
		}
		return ((Visit) getPage().getVisit()).getDropDownBean1();
	}

	public void setYunsfsValue(IDropDownBean Value) {
		long id = -2;
		if (((Visit) getPage().getVisit()).getDropDownBean1()!= null) {
			
			id = ((Visit) getPage().getVisit()).getDropDownBean1().getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				
				((Visit) getPage().getVisit()).setboolean1(true);
			} else {
				
				((Visit) getPage().getVisit()).setboolean1(false);
			}
		}
		((Visit) getPage().getVisit()).setDropDownBean1(Value);
	}

	public void setIYunsfsModel(IPropertySelectionModel value) {
		
		((Visit) getPage().getVisit()).setProSelectionModel1(value);
	}

	public IPropertySelectionModel getIYunsfsModel() {
		
		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
			
			getIYunsfsModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public IPropertySelectionModel getIYunsfsModels() {

			String sql = "select id, mingc from yunsfsb WHERE id<=2 order by mingc";
			
			((Visit) getPage().getVisit()).setProSelectionModel1(new IDropDownModel(sql));

		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}
//	���䷽ʽ_end
	
//	���㷽������
	public IDropDownBean getJiesfaValue() {
		
		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			if (getIJiesfaModels().getOptionCount() > 1) {
				
				((Visit) getPage().getVisit()).setDropDownBean2((IDropDownBean) getIJiesfaModels().getOption(0));
			} else {
				((Visit) getPage().getVisit()).setDropDownBean2(new IDropDownBean(-1, ""));
			}
		}
		return ((Visit) getPage().getVisit()).getDropDownBean2();
	}

	public void setJiesfaValue(IDropDownBean Value) {
		
		long id = -2;
		if (((Visit) getPage().getVisit()).getDropDownBean2()!= null) {
			
			id = ((Visit) getPage().getVisit()).getDropDownBean2().getId();
		}
		((Visit) getPage().getVisit()).setDropDownBean2(Value);
	}

	public void setIJiesfaModel(IPropertySelectionModel value) {
		
		((Visit) getPage().getVisit()).setProSelectionModel2(value);
	}

	public IPropertySelectionModel getIJiesfaModel() {
		
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			
			getIJiesfaModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public IPropertySelectionModel getIJiesfaModels() {

			String sql = 
				"SELECT ID, BIANM\n" +
				"\tFROM JIESFAB\n" + 
				" WHERE SHIFJS = 1\n" + 
				"\t AND JIESLX = "+Locale.meikjs_feiylbb_id+"\n" + 
				"\t and DAOHJZSJ = to_date('"+this.getRiq()+"','yyyy-MM-dd')\n" +
				" ORDER BY BIANM desc";
			
			((Visit) getPage().getVisit()).setProSelectionModel2(new IDropDownModel(sql));

		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}
//	���㷽������_end
}