package com.zhiren.jt.jiesgl.changfhs;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Liuc;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.Window;
import com.zhiren.common.ext.form.Checkbox;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.tree.TreeButton;
import com.zhiren.common.ext.tree.TreeNode;
import com.zhiren.dc.hesgl.jiesd.Jiesdcz;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*
 * ���ߣ�ly
 * ʱ��:2009-09-17
 * ����:���ڳ��������мӼۼ���
 */
public class Jiajjs extends BasePage implements PageValidateListener {
//	�����û���ʾ
	
	private final static String xiaosdd_fgs="�ֹ�˾����Ĭ�Ϻ�ͬID";//�ֹ�˾���� ϵͳ��mingc�ֶ�
	private final static String caigdd_dc="�糧�ɹ�Ĭ�Ϻ�ͬID";	//�糧�ɹ�   ϵͳ��mingc�ֶ�
	
	private final static String isXiaos_cz="�Ƿ����ֹ�˾����";//�ֹ�˾���� ϵͳ��mingc�ֶ�
	private final static String isCaig_cz="�Ƿ����糧�ɹ�";	 //�糧�ɹ�   ϵͳ��mingc�ֶ�
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
	}
	
//	��������mingc
	private String _liucmc;
	
	public void setLiucmc(String _value) {
		_liucmc = _value;
	}
	
	public String getLiucmc() {
		if (_liucmc == null) {
			_liucmc = "";
		}
		return _liucmc;
	}
	
	
	//�ɹ�����mingc
	private String _liucmc1;
	
	public void setLiucmc1(String _value) {
		_liucmc1 = _value;
	}
	
	public String getLiucmc1() {
		if (_liucmc1 == null) {
			_liucmc1 = "";
		}
		return _liucmc1;
	}
	protected void initialize() {
		super.initialize();
		setMsg("");
		this.setLiucmc("");
		this.setLiucmc1("");
		this.setChange("");
	}
	
	private String Zhi1;

	public String getZhi1() {
		return Zhi1;
	}

	public void setZhi1(String zhi) {
		Zhi1 = zhi;
	}
	
	private String Zhi2;

	public String getZhi2() {
		return Zhi2;
	}

	public void setZhi2(String zhi) {
		Zhi2 = zhi;
	}
//***************************************************************************//
	
	private String riq;
	public void setRiq(String value) {
		riq = value;
	}
	public String getRiq() {
		if ("".equals(riq) || riq == null) {
			riq = DateUtil.FormatDate(new Date());
		}
		return riq;
	}
	public void setOriRiq(String value) {
		((Visit) getPage().getVisit()).setString1(value);
	}
	public String getOriRiq() {
		return ((Visit) getPage().getVisit()).getString1();
	}
	
	private String riq1;
	public void setRiq1(String value) {
		riq1 = value;
	}
	public String getRiq1() {
		if ("".equals(riq1) || riq1 == null) {
			riq1 = DateUtil.FormatDate(new Date());
		}
		return riq1;
	}
	public void setOriRiq1(String value) {
		((Visit) getPage().getVisit()).setString2(value);
	}
	public String getOriRiq1() {
		return ((Visit) getPage().getVisit()).getString2();
	}
	
//	 ҳ��仯��¼
	private String Change;
	public String getChange() {
		return Change;
	}
	public void setChange(String change) {
		Change = change;
	}
	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}
	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}
	public String getGridScript() {
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getGridScript();
	}
	public String getGridHtml() {
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getHtml();
	}
	public ExtTreeUtil getTree() {
		return ((Visit) this.getPage().getVisit()).getExtTree1();
	}
	public void setTree(ExtTreeUtil etu) {
		((Visit) this.getPage().getVisit()).setExtTree1(etu);
	}

	public String getTreeHtml() {
		return getTree().getWindowTreeHtml(this);
	}

	public String getTreeScript() {
		return getTree().getWindowTreeScript();
	}
	
	private String treeid = "";
	public String getTreeid() {
		if (treeid.equals("")) {

			treeid = "0";
		}
		return treeid;
	}
	public void setTreeid(String treeid) {
		if (!this.treeid.equals(treeid)) {

			((Visit) getPage().getVisit()).setboolean3(true);
			this.treeid = treeid;
		}else{
			((Visit) getPage().getVisit()).setboolean3(false);
		}
	}
	
	private String gongysmc="";
	public String getGongysmc(){
		if(this.gongysmc==null){
			return "";
		}
		return this.gongysmc;
	}
	public void setGongysmc(String gongysmc){
		this.gongysmc=gongysmc;
	}
	
	private String checklc="";
	public void setChecklc(String checklc){
		this.checklc=checklc;
	}
	public String getChecklc(){
		
		if(this.checklc==null ||  this.checklc.equals("")){
			return "false";
		}
		return this.checklc;
	}
	// �õ��糧����ֵ�ļ���,(jib:1,2,3,) 1�Ǽ���,2�Ƿֹ�˾,3�ǵ糧
	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		String DiancTreeJib = this.getTreeid();
		// System.out.println("jib:" + DiancTreeJib);
		if (DiancTreeJib == null || DiancTreeJib.equals("")) {
			DiancTreeJib = "0";
		}
		String sqlJib = "select d.jib from diancxxb d where d.id="
				+ DiancTreeJib;
		ResultSet rs = con.getResultSet(sqlJib.toString());

		try {
			while (rs.next()) {
				jib = rs.getInt("jib");
			}
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			con.Close();
		}
		return jib;
	}
	
	private boolean _SaveChick = false;
	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
//	�趨Ĭ������
	private boolean _InsertChick=false;
	public void InsertButton(IRequestCycle cycle){
		_InsertChick=true;
	}
	
//	ѡ�����۵�
	private boolean _XiaoSChick=false;
	public void XiaoSButton(IRequestCycle cycle){
		_XiaoSChick=true;
	}
	
//	ѡ�����ɹ���
	private boolean _CaiGChick=false;
	public void CaiGButton(IRequestCycle cycle){
		_CaiGChick=true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			save(cycle);
			getSelectData();
		} else if(_InsertChick){
			_InsertChick=false;
//			�趨Ĭ�Ϻ�ͬ
			shedlc();
			getSelectData();
		} else if(_XiaoSChick){
			_XiaoSChick=false;
//			ѡ����㵥
			xiaosjs();
			getSelectData();
		} else if(_CaiGChick){
			_CaiGChick=false;
//			ѡ����㵥
			caigjs();
			getSelectData();
		}
	}
	
//	�趨Ĭ������
	public void shedlc(){
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con=new JDBCcon();
		String sql="";
		String het="";
		
//		ȡ����ǰҳ�����۵�Ĭ�Ϻ�ͬ���趨ֵ
		String het_id_xs=MainGlobal.getProperId_String(this.getILiucmcModel(), this.getLiucmc());
		
//		�趨���ۺ�ͬ
		if(!het_id_xs.equals("-1")){
			
//			�û���ҳ������������Ĭ�Ϻ�ͬ
			het=MainGlobal.getXitxx_item("����", xiaosdd_fgs
					, String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id()), "");
			
			if(het.equals("")){
				
				sql+=	"insert into xitxxb\n" +
					"  (id, xuh, diancxxb_id, mingc, zhi, danw, leib, zhuangt, beiz)\n" + 
					"values\n" + 
					"  (getnewid("+((Visit) getPage().getVisit()).getDiancxxb_id()+"), \n" +
					"  	(select max(xuh)+1 from xitxxb), "+((Visit) getPage().getVisit()).getDiancxxb_id()+", '"+
					xiaosdd_fgs+"', "+het_id_xs+", '', '����', 1, 'ʹ��');	\n";
			}else{
				sql+= "update xitxxb set zhi='"+het_id_xs+"' where mingc='"+xiaosdd_fgs+"';	\n";
			}
		} else {
			sql += "delete xitxxb where mingc = '"+xiaosdd_fgs+"';\n";
		}
		
//		ȡ�õ�ǰҳ��ɹ���Ĭ�Ϻ�ͬ���趨ֵ
		String het_id_cg=MainGlobal.getProperId_String(this.getILiucmcModel1(), this.getLiucmc1());
		
//		�趨�ɹ���ͬ
		if(!het_id_cg.equals("-1")){
			
//			�û���ҳ�������˲ɹ�Ĭ�Ϻ�ͬ
			het=MainGlobal.getXitxx_item("����", caigdd_dc
					, String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id()), "");
			
			if(het.equals("")){
				
				sql+=	"insert into xitxxb\n" +
					"  (id, xuh, diancxxb_id, mingc, zhi, danw, leib, zhuangt, beiz)\n" + 
					"values\n" + 
					"  (getnewid("+((Visit) getPage().getVisit()).getDiancxxb_id()+"), \n" +
					"  	(select max(xuh)+1 from xitxxb), "+((Visit) getPage().getVisit()).getDiancxxb_id()+", '"+
						caigdd_dc+"', "+het_id_cg+", '', '����', 1, 'ʹ��');	\n";
			}else{
				
				sql+= "update xitxxb set zhi='"+het_id_cg+"' where mingc='"+caigdd_dc+"';	\n";
			}
		} else {
			sql += "delete xitxxb where mingc = '"+caigdd_dc+"';\n";
		}
		
		if(sql.length()>0){
			sql=" begin \n"+sql+" end ;";
			int a=con.getUpdate(sql);
			
			if(a>=0){
				this.setMsg("��ͬĬ��״̬<br>���³ɹ�!");
			}
			con.Close();
			
			setLiucmcValue(null);
			setILiucmcModel(null);
			getILiucmcModels();

			setLiucmcValue1(null);
			setILiucmcModel1(null);
			getILiucmcModels1();
		}
	}
	
//	ѡ�����۵�
	public void xiaosjs(){
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con=new JDBCcon();
		String sql="";
		String isCZ="";
		
//		ȡ����ǰҳ��ֹ�˾���۵��Ƿ�ѡ��
		String xiaosd=getZhi1();
		
		if(!xiaosd.equals("")){			
//			�û���ҳ��ѡ���˷ֹ�˾���۵�
			isCZ=MainGlobal.getXitxx_item("����", isXiaos_cz
					, String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id()), "");
			
			if(isCZ.equals("")){
				sql+=	"insert into xitxxb\n" +
					"  (id, xuh, diancxxb_id, mingc, zhi, danw, leib, zhuangt, beiz)\n" + 
					"values\n" + 
					"  (getnewid("+((Visit) getPage().getVisit()).getDiancxxb_id()+"), \n" +
					"  	(select max(xuh)+1 from xitxxb), "+((Visit) getPage().getVisit()).getDiancxxb_id()+", '"+
					isXiaos_cz+"', '��', '', '����', 1, 'ʹ��')\n";
			}else{
				sql+= "update xitxxb set zhi='��' where mingc='"+isXiaos_cz+"'\n";
			}
		} 
		else {
			sql+= "update xitxxb set zhi = '��' where mingc='"+isXiaos_cz+"'	\n";
		}
		
		if(sql.length()>0){
//			sql=" begin \n"+sql+" end ;";
			int a=con.getUpdate(sql);
			
			con.Close();
			
			setZhi1("");
//			setZhi2("");
		}
	}
	
//	ѡ�ɹ���
	public void caigjs(){
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con=new JDBCcon();
		String sql="";
		String isCZ="";
		
//		ȡ����ǰҳ��糧�ɹ����Ƿ�ѡ��
		String caigd=getZhi2();
		
		if(!caigd.equals("")){			
//			�û���ҳ��ѡ���˵糧�ɹ���
			isCZ=MainGlobal.getXitxx_item("����", isCaig_cz
					, String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id()), "");
			
			if(isCZ.equals("")){
				sql+=	"insert into xitxxb\n" +
					"  (id, xuh, diancxxb_id, mingc, zhi, danw, leib, zhuangt, beiz)\n" + 
					"values\n" + 
					"  (getnewid("+((Visit) getPage().getVisit()).getDiancxxb_id()+"), \n" +
					"  	(select max(xuh)+1 from xitxxb), "+((Visit) getPage().getVisit()).getDiancxxb_id()+", '"+
					isCaig_cz+"', '��', '', '����', 1, 'ʹ��')\n";
			}else{
				sql+= "update xitxxb set zhi='��' where mingc='"+isCaig_cz+"'\n";
			}
		} 
		else {
			sql+= "update xitxxb set zhi = '��' where mingc='"+isCaig_cz+"'\n";
		}
		
		if(sql.length()>0){
//			sql=" begin \n"+sql+" end ;";
			int a=con.getUpdate(sql);
			
			con.Close();
			
//			setZhi1("");
			setZhi2("");
		}
	}
	
	public void UpdateSetKuangfjsmkb(JDBCcon con,String TableName,String TableID){
		StringBuffer bf = new StringBuffer();
		bf.append("update "+TableName+" set zhuangt = 1 where id = "+TableID+"\n");
		con.getUpdate(bf.toString());
	}

	public String getHetid(String xx,int leix){
		String id = "";
		JDBCcon con = new JDBCcon();
		String sql_xs = 
			"select id from( \n" +
			"select distinct h.id, h.hetbh || ' ' || g.mingc || ' ' || min(jg.jij) as xx\n" +
			"  from kuangfjsmkb j,\n" + 
			"       hetb h,\n" + 
			"       gongysb g,\n" + 
			"       hetjgb jg,\n" + 
			"       (select d.quanc\n" + 
			"          from diancxxb d\n" + 
			"         where d.id in (108)) dc\n" + //108=select d.fuid from diancxxb d where id = "+((Visit) getPage().getVisit()).getDiancxxb_id()+"
			" where j.jihkjb_id = h.jihkjb_id\n" + 
			"   and h.gongysb_id = g.id\n" + 
			"   and jg.hetb_id = h.id\n" + 
			"   and j.yansksrq >= h.qisrq\n" + 
			"   and j.yansjzrq <= h.guoqrq\n" + 
			"   and h.leib = "+leix+"\n" + 
			"   and dc.quanc = g.quanc\n" + 
			" group by h.id, h.hetbh, g.mingc)\n" +
			" where xx = '"+xx+"'";
		ResultSet rs = con.getResultSet(sql_xs);
		try {	
			while(rs.next()){
				id = rs.getString("id");
			}
		} catch (SQLException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		}
		return id;
	}
	
	public void save(IRequestCycle cycle) {
//		setMsg(getZhi1()+getZhi2());
		Visit visit = (Visit) getPage().getVisit();
		String kuangfjsmkbid = "";
		String kuangfjsyfbid = "";
		String xiaosht = "";
		String caight = "";
		
		
		String[] raw1=this.getChange().split(",");//�ֱ��ȡ kuangfjsmkbid kuangfjsyfbid xiaosht caight
		kuangfjsmkbid=raw1[0];
		kuangfjsyfbid=raw1[1];
		xiaosht = raw1[2];		
		if(raw1.length>=4){
			caight = raw1[3];
		}
		
		if(!xiaosht.equals("")&&xiaosht!=null){
			xiaosht = getHetid(xiaosht,1);
		}else{
			xiaosht = "-1";
		}
		
		if(!caight.equals("")&&caight!=null){
			caight = getHetid(caight,1);
		}else{
			caight = "-1";
		}
		
		visit.setString3(kuangfjsmkbid+","+kuangfjsyfbid+","+xiaosht+","+caight);
		
		cycle.activate("Fengsxsd");
		
	}
	
	private StringBuffer getBaseSql(){
		Visit visit = (Visit) getPage().getVisit();
		StringBuffer bf=new StringBuffer();
		String diancxxb_id=this.getTreeid();
		
		String str_js="";	//where����
		String isOver = ""; //where���� �ж��Ƿ���������
		
		if(!diancxxb_id.equals("0")){
			
			str_js+=" and d.id="+diancxxb_id+"\n";
		
		}
		
		if(getWeizSelectValue().getId()!=0){
			isOver = "	where liuczt = '"+getWeizSelectValue().getValue()+"'\n";
		}
		
		String sql = 
			"select distinct *\n" +
			"  from (select js.id jiesbid,\n" + 
			"               jy.id jiesyfbid,\n" + 
			"               j.id jsid,\n" + 
			"               dj.id diancjsid,\n" + 
			"               '<a style=\"cursor:hand\" onclick=chak(' || '\"' || 'dianc,' || js.bianm || '\"' || ')>' || js.bianm || '</a>' kfbianh,\n" + 
			"               js.gongysmc gongysmc,\n" + 
			"               d.mingc dianc,\n" + 
			"               ht.hetbh hetbh,\n" + 
			"				decode(lc.leibztb_id,1,'�����','δ���') as liuczt,\n" +
			"               fl.mingc jieslx,\n" + 
			"               js.hansdj hansdj,\n" + 
			"               js.fengsjj fengsjj,\n" + 
			"               js.hansmk hansmc,\n" + 
			"               jy.hansyf hansyf,\n" + 
			"               GetFengsxsht(js.id,"+((Visit) getPage().getVisit()).getDiancxxb_id()+") as xiaosht,\n" + 
			"               GetDCcght(js.id) as caight,\n" + 
			"                '<a style=\"cursor:hand\" onclick=chak(' || '\"' || 'dianc,' || dj.bianm || '\"' || ')>' || dj.bianm || '</a>' dcbianh,\n" + 
			"				dj.bianm as xiaosdbm,\n" + 
			"                 '<a style=\"cursor:hand\" onclick=chak(' || '\"' || 'dianc,' || j.bianm || '\"' || ')>' || j.bianm || '</a>' jsbianh,\n" + 
			"				j.bianm as caigdbm\n" +
			"          from kuangfjsmkb js, kuangfjsyfb jy, jiesb j,diancjsmkb dj,diancxxb d, hetb ht, feiylbb fl,liucztb lc\n" + 
			"         where (js.diancxxb_id = d.id)\n" + 
			"           and fl.id(+) = js.jieslx\n" + 
			"           and js.hetb_id = ht.id(+)\n" + 
			"           and jy.diancjsmkb_id(+) = js.id\n" + 
			"           and js.fuid = 0\n" + 
			"           and js.id = j.kuangfjsmkb_id(+)\n" + 
			"           and js.diancjsmkb_id = dj.id(+)\n" + 
			"			and js.liucztb_id = lc.id(+)\n" +
			"           --and nvl(js.diancjsmkb_id, 0) = 0\n" + 
			str_js +
			"  			and js.jiesrq>="+DateUtil.FormatOracleDate(this.getRiq())+" and js.jiesrq<="+DateUtil.FormatOracleDate(this.getRiq1())+"\n" +
			"        union\n" + 
			"        select js.id jiesbid,\n" + 
			"               jy.id jiesyfbid,\n" + 
			"               j.id jsid,\n" + 
			"               dy.id diancjsid,\n" + 
			"               '<a style=\"cursor:hand\" onclick=chak(' || '\"' || 'dianc,' || jy.bianm || '\"' || ')>' || jy.bianm || '</a>' kfbianh,\n" + 
			"               jy.gongysmc gongysmc,\n" + 
			"               d.mingc dianc,\n" + 
			"               ht.hetbh hetbh,\n" + 
			"				decode(lc.leibztb_id,1,'�����','δ���') as liuczt,\n" +
			"               fl.mingc jieslx,\n" + 
			"               js.hansdj hansdj,\n" + 
			"               js.fengsjj fengsjj,\n" + 
			"               js.hansmk hansmc,\n" + 
			"               jy.hansyf hansyf,\n" + 
			"               GetFengsxsht(js.id,"+((Visit) getPage().getVisit()).getDiancxxb_id()+") as xiaosht,\n" + 
			"               GetDCcght(js.id) as caight,\n" + 
			"                '<a style=\"cursor:hand\" onclick=chak(' || '\"' || 'dianc,' || dy.bianm || '\"' || ')>' || dy.bianm || '</a>' dcbianh,\n" + 
			"				dy.bianm as xiaosdbm,\n" + 
			"                 '<a style=\"cursor:hand\" onclick=chak(' || '\"' || 'dianc,' || j.bianm || '\"' || ')>' || j.bianm || '</a>' jsbianh,\n" + 
			"				j.bianm as caigdbm\n" +
			"          from kuangfjsmkb js,\n" + 
			"               kuangfjsyfb jy,\n" + 
			"               diancjsyfb dy,\n" + 
			"               jiesyfb j,\n" + 
			"               diancxxb d,\n" + 
			"               (select id, hetbh\n" + 
			"                  from hetb\n" + 
			"                union\n" + 
			"                select id, hetbh from hetys) ht,\n" + 
			"               feiylbb fl,liucztb lc\n" + 
			"         where jy.diancxxb_id = d.id\n" + 
			"           and fl.id(+) = jy.jieslx\n" + 
			"           and jy.hetb_id = ht.id(+)\n" + 
			"           and jy.diancjsmkb_id = js.id(+)\n" + 
			"           and jy.fuid = 0\n" + 
			"           and jy.id = j.kuangfjsyfb_id(+)\n" + 
			"           and jy.diancjsyfb_id = dy.id(+)\n" +
			"			and js.liucztb_id = lc.id(+)\n" +
			"           --and nvl(jy.diancjsyfb_id, 0) = 0\n" + 
			str_js +
			"  			and jy.jiesrq>="+DateUtil.FormatOracleDate(this.getRiq())+" and jy.jiesrq<="+DateUtil.FormatOracleDate(this.getRiq1())+")\n" +
			isOver +
			" order by dcbianh\n";

		bf.append(sql);
		
		return bf;
		
	}
	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		
		String sSql=this.getBaseSql().toString();
		
		ResultSetList rsl = con.getResultSetList(sSql);
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "��������SQL:" + sSql);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv",rsl);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addColumn(1,new GridColumn(GridColumn.ColType_Check));
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.setHeight("bodyHeight");
		egu.getColumn("jiesbid").setHidden(true);
		egu.getColumn("jiesbid").setEditor(null);
		egu.getColumn("jiesyfbid").setHidden(true);
		egu.getColumn("jiesyfbid").setEditor(null);
		egu.getColumn("jsid").setHidden(true);
		egu.getColumn("jsid").setEditor(null);
		egu.getColumn("diancjsid").setHidden(true);
		egu.getColumn("diancjsid").setEditor(null);
		egu.getColumn("kfbianh").setHeader("������");
		egu.getColumn("kfbianh").setEditor(null);
		egu.getColumn("kfbianh").setWidth(120);
		egu.getColumn("gongysmc").setHeader("��Ӧ��");
		egu.getColumn("gongysmc").setEditor(null);
		egu.getColumn("dianc").setHeader("�糧");
		egu.getColumn("dianc").setEditor(null);
		egu.getColumn("hetbh").setHeader("��ͬ���");
		egu.getColumn("hetbh").setEditor(null);
		egu.getColumn("liuczt").setHeader("����״̬");
		egu.getColumn("liuczt").setEditor(null);
		egu.getColumn("jieslx").setHeader("��������");
		egu.getColumn("jieslx").setEditor(null);
		egu.getColumn("hansdj").setHeader("ú�˰����");
		egu.getColumn("hansdj").setEditor(null);
		egu.getColumn("fengsjj").setHeader("ú��ֹ�˾�Ӽ�");
		egu.getColumn("fengsjj").setEditor(null);
		egu.getColumn("hansmc").setHeader("��˰ú��");
		egu.getColumn("hansmc").setEditor(null);
		egu.getColumn("hansyf").setHeader("��˰�˷�");
		egu.getColumn("hansyf").setEditor(null);
		egu.getColumn("xiaosht").setHeader("ѡ��ֹ�˾���ۺ�ͬ");
		egu.getColumn("xiaosht").setWidth(250);
		egu.getColumn("caight").setHeader("ѡ��糧�ɹ���ͬ");
		egu.getColumn("caight").setWidth(250);
		egu.getColumn("dcbianh").setHeader("�ֹ�˾���۽�����");
		egu.getColumn("dcbianh").setEditor(null);
		egu.getColumn("dcbianh").setWidth(120);
		egu.getColumn("xiaosdbm").setHidden(true);
		egu.getColumn("xiaosdbm").setEditor(null);
		egu.getColumn("jsbianh").setHeader("�糧�ɹ�������");
		egu.getColumn("jsbianh").setEditor(null);
		egu.getColumn("jsbianh").setWidth(120);
		egu.getColumn("caigdbm").setHidden(true);
		egu.getColumn("caigdbm").setEditor(null);
//		egu.getColumn("chak").setHeader("�鿴");
//		egu.getColumn("chak").setEditor(null);
		
		//ѡ�����ۺ�ͬ
		egu.getColumn("xiaosht").setEditor(new ComboBox());
		String sql_xs = 
			"select distinct h.id, h.hetbh || ' ' || g.mingc || ' ' || min(jg.jij) as xx\n" +
			"  from kuangfjsmkb j,\n" + 
			"       hetb h,\n" + 
			"       gongysb g,\n" + 
			"       hetjgb jg,\n" + 
			"       (select d.quanc\n" + 
			"          from diancxxb d\n" + 
			"         where d.id in (108)) dc\n" + //108=select d.fuid from diancxxb d where id = "+((Visit) getPage().getVisit()).getDiancxxb_id()+"
			" where j.jihkjb_id = h.jihkjb_id\n" + 
			"   and h.gongysb_id = g.id\n" + 
			"   and jg.hetb_id = h.id\n" + 
			"   and j.yansksrq >= h.qisrq\n" + 
			"   and j.yansjzrq <= h.guoqrq\n" + 
			"   and h.leib = 1\n" + 
			"   and dc.quanc = g.quanc\n" + 
			" group by h.id, h.hetbh, g.mingc\n";
		egu.getColumn("xiaosht").setComboEditor(egu.gridId,new IDropDownModel(sql_xs));	
		egu.getColumn("xiaosht").setReturnId(true);
		
		//ѡ��ɹ���ͬ
		egu.getColumn("caight").setEditor(new ComboBox());
		String sql_cg = 
			"select distinct h.id, h.hetbh || ' ' || g.mingc || ' ' || min(jg.jij) as xx\n" +
			"  from kuangfjsmkb j, hetb h, gongysb g, hetjgb jg\n" + 
			" where j.jihkjb_id = h.jihkjb_id\n" + 
			"   and h.gongysb_id = g.id\n" + 
			"   and jg.hetb_id = h.id\n" + 
			"   and j.yansksrq >= h.qisrq\n" + 
			"   and j.yansjzrq <= h.guoqrq\n" + 
			"   and h.leib = 0\n" + 
			" group by h.id, h.hetbh, g.mingc\n";
		egu.getColumn("caight").setComboEditor(egu.gridId,new IDropDownModel(sql_cg));	
		egu.getColumn("caight").setReturnId(true);
		
//***********************************************������********************************************************************
		egu.addTbarText("��������:");
		DateField dStart = new DateField();
		dStart.Binding("RIQ","forms[0]");
		dStart.setValue(getRiq());
		egu.addToolbarItem(dStart.getScript());
		
		egu.addTbarText("��");
		
		DateField dEnd = new DateField();
		dEnd.Binding("RIQ1","forms[0]");
		dEnd.setValue(getRiq1());
		egu.addToolbarItem(dEnd.getScript());
		
		egu.addTbarText("-");
		egu.addTbarText("��λ����:");
//		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
//				ExtTreeUtil.treeWindowType_Dianc,
//				((Visit) getPage().getVisit()).getDiancxxb_id(),getTreeid());
		
		ExtTreeUtil etu=new ExtTreeUtil("diancTree");
		etu.defaultSelectid=this.getTreeid();
		//etu.getDefaultTree("diancTree", con.getResultSetList(this.getDTsql().toString()), false);
		this.getDefaultTree(etu, "diancTree", con.getResultSetList(this.getDTsql().toString()), false);
		
		etu.window = new Window("diancTree");
		etu.window.setItems("diancTree"+"_treePanel");
		TreeButton tb=new TreeButton("ȷ��","function(){" +
				" var cks = diancTree_treePanel.getSelectionModel().getSelectedNode();\n" +
				" if(cks==null){diancTree_window.hide();return;}\n" +
				" var obj0 = document.getElementById('diancTree_id');obj0.value = cks.id;diancTree_text.setValue(cks.text);\n" +
//				" if(cks.leaf){ document.all.diancmc.value=cks.parentNode.text;\n"+
//				" document.all.gongysmc.value=cks.text;}\n"+
//				" else{ document.all.diancmc.value=cks.text;\n"+
//				" document.all.gongysmc.value='';}\n"+
				" diancTree_window.hide();\n" +
				"document.forms[0].submit();}");
		//etu.addBbarButton(TreeButton.ButtonType_Window_Ok, "SaveButton");
		etu.addBbarButton(tb);
		etu.setWidth(200);
		etu.setTitle("��λѡ��");
		
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");
		
		egu.addTbarText("����״̬");
		ComboBox WeizSelect = new ComboBox();
		WeizSelect.setId("Weizx");
		WeizSelect.setWidth(80);
		WeizSelect.setLazyRender(true);
		WeizSelect.setTransform("WeizSelectx");
		WeizSelect.setListeners("select:function(own,newValue,oldValue){document.Form0.submit();}");
		egu.addToolbarItem(WeizSelect.getScript());

		egu.addTbarText("-");
		
//		ѡ�����������ѡ��
		String isXz_x = "false";//�ж�ҳ��ˢ�º�ֹ�˾���۵��Ƿ�ѡ��
		String isXz_c = "false";//�ж�ҳ��ˢ�º�糧�ɹ����Ƿ�ѡ��
		
		String xs_cz = MainGlobal.getXitxx_item("����", isXiaos_cz, String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id()), "");
		String cg_cz = MainGlobal.getXitxx_item("����", isCaig_cz, String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id()), "");
		if(xs_cz!=null && !xs_cz.equals("")){
			if(xs_cz.equals("��")){
				isXz_x = "true";
				setZhi1("�ֹ�˾���۵�");
			}
		}
		if(cg_cz!=null && !cg_cz.equals("")){
			if(cg_cz.equals("��")){
				isXz_c = "true";
				setZhi2("�糧�ɹ���");
			}
		}
		
		StringBuffer caozlx = new StringBuffer();
		caozlx.append(
				"{text:'��������',menu:new Ext.menu.Menu({\n" +
				"      items:[ {\n" + 
				"      id:'1',\n" + 
				"      text:'�ֹ�˾���۵�',\n" +
				"      checked: "+isXz_x+",\n" +
				"      checkHandler: onItemCheck,\n" + 
				"      minWidth:75\n" + 
				"    }\n" + 
				"    , {\n" + 
				"      id:'2',\n" + 
				"      text:'�糧�ɹ���',\n" + 
				"      checked: "+isXz_c+",\n" +
				"      checkHandler: onItemCheck,\n" + 
				"      minWidth:75\n" + 
				"    }\n" + 
				"    ]})\n" + 
				"}");
		egu.addToolbarItem(caozlx.toString());
		
		String caozfn=
			"function onItemCheck(item, checked){\n" +
			"	if (item.text==\"�ֹ�˾���۵�\"){\n" + 
			"		if (checked){\n" + 
			"			document.all.item('ZHI1').value=item.text;\n" + 
			"		}else{\n" + 
			"	    	document.all.item('ZHI1').value=\"\";\n" + 
			"		}\n" + 
			"		document.all.item('XiaoSButton').click();	\n" +
			"	} else if (item.text==\"�糧�ɹ���\"){\n" + 
			"		if (checked){\n" + 
			"			document.all.item('ZHI2').value=item.text;\n" + 
			"		}else{\n" + 
			"			document.all.item('ZHI2').value=\"\";\n" + 
			"		}\n" + 
			"		document.all.item('CaiGButton').click();	\n" +
			"	};\n" + 
//			"	document.all.item('XuanzButton').click();	\n" +
			"}";
		
		egu.addOtherScript(caozfn);
		
		egu.addTbarText("-");
		
//		�趨Ĭ�Ϻ�ͬ
		String xiaos_va="";
		String caig_va="";
		
		String sx = MainGlobal.getXitxx_item("����", xiaosdd_fgs, String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id()), "");
		String sc = MainGlobal.getXitxx_item("����", caigdd_dc, String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id()), "");
		
		if(sx!=null && !sx.equals("")){
			xiaos_va=" Ext.getDom('dayzt1').value='"+sx+"';\n";
		}
		if(sc!=null && !sc.equals("")){
			caig_va=" Ext.getDom('dayzt2').value='"+sc+"';\n";
		}
		egu.addOtherScript(xiaos_va+caig_va);
		
		String gb3_fs="function(){  \n"
//			+	"var Mrcd = gridDiv_grid.getSelectionModel().getSelections(); \n"
//			+   " if( Mrcd==null || Mrcd.length<=0){Ext.Msg.alert('��ʾ��Ϣ','����ѡ���¼�ٽ��в���!');return;}\n"
//			+"   document.all.CHANGE.value='';\n"
//			+   " for(i = 0; i< Mrcd.length; i++){\n"
//			+   " var rc=Mrcd[i]; document.all.CHANGE.value+=rc.get('JIESBID')+','+rc.get('JIESYFBID');\n"
//			+   " if(i!=Mrcd.length-1){ document.all.CHANGE.value+=';';}\n"
//			+   "}\n"
			
			+ " if(!win){	\n" 
			+ "	\tvar form = new Ext.form.FormPanel({	\n" 
			+ " \tbaseCls: 'x-plain',	\n" 		
			+ " \tlabelAlign:'right',	\n" 
			+ " \tdefaultType: 'textfield',	\n"
			+ " \titems: [{		\n"
			+ " \txtype:'fieldset',	\n"
			+ " \ttitle:'��ѡ��Ĭ�Ϻ�ͬ',	\n"
			+ " \tautoHeight:false,	\n"
			+ " \theight:220,	\n"
			+ " \titems:[	\n"
			
			+ " \tlcmccb=new Ext.form.ComboBox({	\n" 
			+ " \twidth:250,	\n"
			+ " \tid:'lcmccb',	\n"
			+ " \tselectOnFocus:true,	\n"
			+ "	\ttransform:'LiucmcDropDown',	\n"		
			+ " \tlazyRender:true,	\n"	
			+ " \tfieldLabel:'�ֹ�˾����<br>Ĭ�Ϻ�ͬ',		\n" 
			+ " \ttriggerAction:'all',	\n"
			+ " \ttypeAhead:true,	\n"	
			+ " \tforceSelection:true,	\n"
			+ " \teditable:false	\n"					
			+ " \t}),	\n"	
			
			+ " \tlcmccb1=new Ext.form.ComboBox({	\n" 
			+ " \twidth:250,	\n"
			+ " \tid:'lcmccb1',	\n"
			+ " \tselectOnFocus:true,	\n"
			+ "	\ttransform:'LiucmcDropDown1',	\n"		
			+ " \tlazyRender:true,	\n"	
			+ " \tfieldLabel:'�糧�ɹ�<br>Ĭ�Ϻ�ͬ',		\n" 
			+ " \ttriggerAction:'all',	\n"
			+ " \ttypeAhead:true,	\n"	
			+ " \tforceSelection:true,	\n"
			+ " \teditable:false	\n"					
			+ " \t})	\n"
			
			+ " \t]		\n"
			+ " \t}]	\n"		
			+ " \t});	\n"
			
			+ " \twin = new Ext.Window({	\n"
			+ " \tel:'hello-win',	\n"
			+ " \tlayout:'fit',	\n"
			+ " \twidth:500,	\n"	
			+ " \theight:300,	\n"
			+ " \tcloseAction:'hide',	\n"
			+ " \tplain: true,	\n"
			+ " \ttitle:'��ͬ',	\n"
			+ " \titems: [form],	\n"
			+ " \tbuttons: [{	\n"
			+ " \ttext:'ȷ��',	\n"
			+ " \thandler:function(){	\n"  
			+ " \twin.hide();	\n"
			
//			+ " \tif(lcmccb.getRawValue()=='��ѡ��' && lcmccb1.getRawValue()=='��ѡ��'){		\n" 
//			+ "	\t	alert('��ѡ���������ƣ�');		\n"
//			+ " \t}else{" 
			+ " \t\t document.getElementById('TEXT_LIUCMCSELECT_VALUE').value=lcmccb.getRawValue();	\n"
			+ " \t\t document.getElementById('TEXT_LIUCMCSELECT_VALUE1').value=lcmccb1.getRawValue();	\n"
			+ " \t\t document.all.item('InsertButton').click();	\n"
			+ " Ext.Msg.show({title: 'Please wait',msg: '���ڼ�������...',progressText: '���ݼ�����...',width:300,progress:true,closable:false});for(var i=0;i<=10;i++)setTimeout(function(){Ext.Msg.updateProgress(i/10,'���ݼ����С���','���ڼ�������');},i*100);"
//			+ " \t}	\n"	
			
			+ " \t}	\n"
			+ " \t},{	\n"
			+ " \ttext: 'ȡ��',	\n"
			+ " \thandler: function(){	\n"
			+ " \twin.hide();	\n"
//			+ " \tdocument.getElementById('TEXT_LIUCMCSELECT_VALUE').value='';	\n"
//			+ " \tdocument.getElementById('TEXT_LIUCMCSELECT_VALUE1').value='';	\n"
			+ " \t}		\n"
			+ " \t}]	\n"
			+ " \t});}	\n" 
			+ " \twin.show(this);	\n"	

//			+ " \tif(document.getElementById('TEXT_LIUCMCSELECT_VALUE').value!=''){	\n"	
//			//+ " \tChangb.setRawValue(document.getElementById('TEXT_LIUCMCSELECT_VALUE').value);	\n"	
//			+ " \t}	\n"	
			+ " \t}";
		GridButton gbt3 = new GridButton("�趨Ĭ�Ϻ�ͬ",gb3_fs);
		gbt3.setIcon(SysConstant.Btn_Icon_Create);
		egu.addTbarBtn(gbt3);
		
		egu.addTbarText("-");
		
		String gb_js="function(){\n" +
		" var Mrcd = gridDiv_grid.getSelectionModel().getSelections(); \n" +
		" if( Mrcd==null || Mrcd.length<=0){Ext.Msg.alert('��ʾ��Ϣ','����ѡ���¼�ٽ��в���!');return;}\n" +
		" document.all.CHANGE.value='';\n" +
		
		"var leix1=document.all.item('ZHI1').value;\n" +//���۵�
		"var leix2=document.all.item('ZHI2').value;\n" +//�ɹ���
		"var het1= Ext.getDom('dayzt1').value;\n" +//���ۺ�ͬ//document.getElementById('TEXT_LIUCMCSELECT_VALUE').value;
		"var het2= Ext.getDom('dayzt2').value;\n" +//�ɹ���ͬ//document.getElementById('TEXT_LIUCMCSELECT_VALUE1').value;
		"var xsht = '';\n" +
		"var cght = ''\n" +
		
		" for(i = 0; i< Mrcd.length; i++){\n" +
		" var rc=Mrcd[i]; \n" +
		//�ж��Ƿ�ѡ�����۵�,�ɹ���
		"if (leix1=='' && leix2==''){" +
		" Ext.Msg.alert('��ʾ��Ϣ','����ѡ������!');return; " +
		"}\n" +
		//�ж��Ƿ�ѡ�����۵�
		" if (!leix1==''){\n" +
			"if(rc.get('XIAOSDBM')==null || rc.get('XIAOSDBM')==''){\n" +
				"if(rc.get('XIAOSHT')==null || rc.get('XIAOSHT')==''){\n" +
					//�ж�Ĭ�Ϻ�ͬ
					" if (het1==-1){\n" +
						" Ext.Msg.alert('��ʾ��Ϣ','����ѡ�����ۺ�ͬ!');return;  \n" +
					"} else {\n" +	
						" xsht = het1;\n" +
					"}\n" +
				"} else {\n" +
					" xsht = rc.get('XIAOSHT');\n" +
				"}\n" +
			"} else {\n" +
				" Ext.Msg.alert('��ʾ��Ϣ','�ֹ�˾���۵������ظ�����!');return;  \n" +
			"}\n" +
		"}" +
		//�ж��Ƿ�ѡ�вɹ���		
		" if (!leix2==''){" +
			"if(rc.get('CAIGDBM')==null || rc.get('CAIGDBM')==''){\n" +
				"if(rc.get('CAIGHT')==null || rc.get('CAIGHT')==''){\n" +
					//�ж�Ĭ�Ϻ�ͬ
					" if (het2==-1){\n" +
						" Ext.Msg.alert('��ʾ��Ϣ','����ѡ��ɹ���ͬ!');return;  \n" +
					"} else {\n" +
						" cght = het2;\n" +
					"}\n" +
				"} else {\n" +
					" cght = rc.get('CAIGHT');\n" +
				"}\n" +
			"} else {\n" +
				" Ext.Msg.alert('��ʾ��Ϣ','�糧�ɹ����������ظ�����!');return;  \n" +
			"}\n" +
		"}" +
		" document.all.CHANGE.value+=rc.get('JIESBID')+','+rc.get('JIESYFBID')+','+xsht+','+cght;\n" +

		" if(i!=Mrcd.length-1){ document.all.CHANGE.value+=';';}\n" +
		" }\n" +
		" document.all.item('SaveButton').click();" +
//		" Ext.Msg.progress('��ʾ��Ϣ','��ȴ�','���ݼ����С���');\n"+
		" Ext.Msg.show({title: 'Please wait',msg: '���ڼ�������...',progressText: '���ݼ�����...',width:300,progress:true,closable:false});for(var i=0;i<=10;i++)setTimeout(function(){Ext.Msg.updateProgress(i/10,'���ݼ����С���','���ڼ�������');},i*100);"+
		"\n}";
			
		GridButton gbt = new GridButton("����",gb_js);
		gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbt);
		
		setExtGrid(egu);
		con.Close();
	}
	
	
//	���ۺ�ͬ
//	��ͬ��Ϣ DropDownBean8  
//  ��ͬ��Ϣ ProSelectionModel8
	public IDropDownBean getLiucmcValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean8() == null) {
			((Visit) getPage().getVisit()).setDropDownBean8((IDropDownBean) getILiucmcModel().getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean8();
	}

	public void setLiucmcValue(IDropDownBean value) {
		
		if(((Visit) getPage().getVisit()).getDropDownBean8()!=value){
			
			((Visit) getPage().getVisit()).setDropDownBean8(value);
		}
	}

	public void setILiucmcModel(IPropertySelectionModel value) {
		
		((Visit) getPage().getVisit()).setProSelectionModel8(value);
	}

	public IPropertySelectionModel getILiucmcModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel8() == null) {
			
			getILiucmcModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel8();
	}

	public IPropertySelectionModel getILiucmcModels() {
		
		String sql="";
		sql=
			"select distinct h.id, h.hetbh || ' ' || g.mingc || ' ' || min(jg.jij) as xx\n" +
			"  from kuangfjsmkb j,\n" + 
			"       hetb h,\n" + 
			"       gongysb g,\n" + 
			"       hetjgb jg,\n" + 
			"       (select d.quanc\n" + 
			"          from diancxxb d\n" + 
			"         where d.id in (select d.fuid from diancxxb d where id = "+((Visit) getPage().getVisit()).getDiancxxb_id()+")) dc\n" + 
			" where j.jihkjb_id = h.jihkjb_id\n" + 
			"   and h.gongysb_id = g.id\n" + 
			"   and jg.hetb_id = h.id\n" + 
			"   and j.yansksrq >= h.qisrq\n" + 
			"   and j.yansjzrq <= h.guoqrq\n" + 
			"   and h.leib = 1\n" + 
			"   and dc.quanc = g.quanc\n" + 
			" group by h.id, h.hetbh, g.mingc\n";

		((Visit) getPage().getVisit()).setProSelectionModel8(new IDropDownModel(sql,"��ѡ��"));
		return ((Visit) getPage().getVisit()).getProSelectionModel8();
	}
//   ��ͬ��Ϣ end
	
	//�ɹ���ͬ
//	��ͬ��Ϣ DropDownBean9  
//  ��ͬ��Ϣ ProSelectionModel9
	public IDropDownBean getLiucmcValue1() {
		if (((Visit) getPage().getVisit()).getDropDownBean9() == null) {
			((Visit) getPage().getVisit()).setDropDownBean9((IDropDownBean) getILiucmcModel1().getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean9();
	}

	public void setLiucmcValue1(IDropDownBean value) {
		
		if(((Visit) getPage().getVisit()).getDropDownBean9()!=value){
			
			((Visit) getPage().getVisit()).setDropDownBean9(value);
		}
	}

	public void setILiucmcModel1(IPropertySelectionModel value) {
		
		((Visit) getPage().getVisit()).setProSelectionModel9(value);
	}

	public IPropertySelectionModel getILiucmcModel1() {
		if (((Visit) getPage().getVisit()).getProSelectionModel9() == null) {
			
			getILiucmcModels1();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel9();
	}

	public IPropertySelectionModel getILiucmcModels1() {
		
		String sql="";
		sql=
			"select distinct h.id, h.hetbh || ' ' || g.mingc || ' ' || min(jg.jij) as xx\n" +
			"  from kuangfjsmkb j, hetb h, gongysb g, hetjgb jg\n" + 
			" where j.jihkjb_id = h.jihkjb_id\n" + 
			"   and h.gongysb_id = g.id\n" + 
			"   and jg.hetb_id = h.id\n" + 
			"   and j.yansksrq >= h.qisrq\n" + 
			"   and j.yansjzrq <= h.guoqrq\n" + 
			"   and h.leib = 0\n" + 
			" group by h.id, h.hetbh, g.mingc\n";
		((Visit) getPage().getVisit()).setProSelectionModel9(new IDropDownModel(sql,"��ѡ��"));
		return ((Visit) getPage().getVisit()).getProSelectionModel9();
	}
//   ��ͬ��Ϣ end
	
	
	public void getDefaultTree(ExtTreeUtil etu,String treeId,ResultSetList rsl,boolean checkbox) {
		
		etu.treeId=treeId;
		etu.init();
		TreeNode parentNode = null;
		TreeNode RootNode = null;
		int lastjib = 0;
		while(rsl.next()) {
			int curjib = rsl.getInt(2);
			TreeNode node = new TreeNode(rsl.getString(0),rsl.getString(1));
			node.setCheckbox(checkbox);
			if(parentNode==null) {
				RootNode = node;
				node.setCheckbox(false);
				parentNode = node;
				lastjib = curjib+1;
				continue;
			}
			if(lastjib < curjib) {
				parentNode = (TreeNode)parentNode.getLastChild();
			}else if(lastjib > curjib){
				for(int i=0;i<lastjib - curjib;i++)
					parentNode = (TreeNode)parentNode.getParentNode();
			}
			lastjib = curjib;
			parentNode.appendChild(node);
		}
		etu.setRootNode(RootNode);
	}
	
	
	//��õ糧 ��Ӧ�̵ĵ糧���νṹ��sql
	private StringBuffer getDTsql(){
		
		Visit visit=(Visit)this.getPage().getVisit();
		StringBuffer bf=new StringBuffer();
		
		bf.append(" select distinct * from ( \n");
		bf.append(" select 0 id,'ȫ��' mingc,0 jib from dual\n");
		bf.append(" union\n");
		bf.append(" select d.id,d.mingc,1 jib from jiesb js,diancxxb d where js.diancxxb_id=d.id\n");
		bf.append(" and nvl(js.diancjsmkb_id,0) not in (select nvl(dm.id,null) from diancjsmkb dm )\n");
		bf.append(" union \n");
		bf.append(" select d.id,d.mingc,1 jib from jiesyfb jf,diancxxb d  where jf.diancxxb_id=d.id \n");
		bf.append(" and nvl(jf.diancjsyfb_id,0) not in (select nvl(dy.id,null) from diancjsyfb dy)\n");
		bf.append(" ) \n");
		bf.append(" order by id,mingc\n");
	
		return bf;
	}
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			
			visit.setString14(visit.getActivePageName());
			visit.setActivePageName(getPageName().toString());
//			visit.setboolean2(false);//��������
			visit.setboolean3(true);
//			2
			setWeizSelectValue(null);
			setWeizSelectModel(null);
			getWeizSelectModel();
			this.setRiq("");
			this.setRiq1("");
			
			setLiucmcValue(null);
			setILiucmcModel(null);
			getILiucmcModels();
			
			setLiucmcValue1(null);
			setILiucmcModel1(null);
			getILiucmcModels1();
			
			setZhi1("");
			setZhi2("");
		}
		init();
	}
	private void init() {
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
	
//	����״̬
	public IDropDownBean getWeizSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean2((IDropDownBean) getWeizSelectModel()
							.getOption(0));
		}
		
		return ((Visit) getPage().getVisit()).getDropDownBean2();
	}

	public void setWeizSelectValue(IDropDownBean Value) {
		
		if(Value!=((Visit) getPage().getVisit()).getDropDownBean2()){
			
			((Visit) getPage().getVisit()).setboolean1(true);
			((Visit) getPage().getVisit()).setDropDownBean2(Value);
		}
	}

	public void setWeizSelectModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(value);
	}

	public IPropertySelectionModel getWeizSelectModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getWeizSelectModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public void getWeizSelectModels() {
		List list = new ArrayList();
		list.add(new IDropDownBean(0, "ȫ��"));
		list.add(new IDropDownBean(1, "�����"));
		list.add(new IDropDownBean(2, "δ���"));
		((Visit) getPage().getVisit()).setProSelectionModel2(new IDropDownModel(list));
	}

}