package com.zhiren.dc.hesgl.jiessz;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
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
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*
 * ����:tzf
 * ʱ��:2009-06-03
 * ����:��������ѡ��Σ�������ѯʱ����jiesszfahtglb�е����� ����ʱ�����������������ͬ��
 *      ��ͬú��λ��Ӧͬһ����ͬ��������д���ѡ������һ��ú��λҳ��չʾ��
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-12-21
 * �������޸ĺ�ͬ������IDʹ��Int���ʹ����������
 */
public class Hetglsz extends BasePage implements PageValidateListener {
	
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);;
	}
	
	protected void initialize() {
		msg = "";
	}
	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	 //��õ���ָ��������
	private Date getDateOfDay_Oracle(String date,int month,int day){
		SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
		Date d=null;
		try{
			d=sf.parse(date);
			
			String ds=DateUtil.Formatdate("yyyy-MM-dd", d);
			String new_s=ds.substring(0, 4)+"-"+month+"-"+day;
			
			d=sf.parse(new_s);
			
		}catch(Exception e){
			d=new Date();
			e.printStackTrace();
		}
		
	return d;
	
	
	}
//***************************************************************************//
	
	private String riq;
	public void setRiq(String value) {
		riq = value;
	}
	public String getRiq() {
		if ("".equals(riq) || riq == null) {
//			riq = DateUtil.FormatDate(new Date());
			riq=DateUtil.FormatDate(this.getDateOfDay_Oracle(DateUtil.FormatDate(new Date()),1,1));
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
	
	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	private boolean _ReturnChick = false;

	public void ReturnButton(IRequestCycle cycle) {
		_ReturnChick = true;
	}
	
	private boolean _RefreshChick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			Visit visit = (Visit) this.getPage().getVisit();
			_SaveChick = false;
			Save(getChange(), visit);
			getSelectData(((Visit) getPage().getVisit()).getString10());
		}
		if (_ReturnChick) {
			_ReturnChick = false;
			GotoJiesszfab(cycle);
		}
		if (_RefreshChick) {
			_RefreshChick = false;
			getSelectData(((Visit) getPage().getVisit()).getString10());
		}
		else{
			getSelectData(((Visit) getPage().getVisit()).getString10());
		}
	}

	private void GotoJiesszfab(IRequestCycle cycle) {
		// TODO �Զ����ɷ������
		
		cycle.activate("Jiesszfab");
	}
	private void Save(String strchange,Visit visit) {
		JDBCcon con = new JDBCcon();
		long mlngDiancxxb_id=visit.getDiancxxb_id();
		String fabid = visit.getString10().split(",")[0];
//		int fabid=Integer.parseInt(visit.getString10().substring(0,visit.getString10().lastIndexOf(',')));
		ResultSetList rsl = null;
		if(strchange.equals("")){
			String sql="delete from jiesszfahtglb where jiesszfab_id="+fabid;
			con.getUpdate(sql);
            if(con.getUpdate(sql.toString())>=0){
				this.setMsg("ȡ���ɹ���");
			}else{
				con.rollBack();
				this.setMsg("ȡ��ʧ�ܣ�");
			}
		}else{
			rsl = getExtGrid().getModifyResultSet(strchange);
//		rsl.beforefirst()
			
			String sql="delete from jiesszfahtglb where jiesszfab_id="+fabid;
			
			con.getUpdate(sql);
			StringBuffer Str_sql = new StringBuffer();
			Str_sql.append("begin\n");
			while(rsl.next()){
				String id =MainGlobal.getNewID(mlngDiancxxb_id);
				String hetid= rsl.getString("ID");
				Str_sql.append("insert into jiesszfahtglb (id,jiesszfab_id,hetb_id)  values(");
				Str_sql.append(id).append(",").append(fabid).append(",").append(hetid).append(");");
			}
			Str_sql.append("End;");
			if(con.getUpdate(Str_sql.toString())>=0){
				
				con.commit();
				this.setMsg("����ɹ���");
			}else{
				
				con.rollBack();
				this.setMsg("����ʧ�ܣ�");
			}
		}
		con.Close();
	}
	
	private String getSzfarq(long fangz_id){
		
		JDBCcon con=new JDBCcon();
		String strFarq="";
		try{
			
			String sql="select to_char(qiysj,'yyyy-MM-dd') as qiysj from jiesszfab where id="+fangz_id;
			ResultSet rs=con.getResultSet(sql);
			if(rs.next()){
				
				strFarq=rs.getString("qiysj");
			}
			rs.close();
		}catch(SQLException s){
			
			s.printStackTrace();
		}catch(Exception e){
			
			e.printStackTrace();
		}finally{
			
			con.Close();
		}
		
		return strFarq;
	}

	public void getSelectData(String Param) {
	    Visit visit = (Visit) getPage().getVisit();
	    JDBCcon con=new JDBCcon();
		String ID=Param.split(",")[0];
		String gongys_id = Param.split(",")[1];
//		String shezrq=DateUtil.FormatDate(new Date());
//		shezrq=getSzfarq(ID);
//		
		//��ʼʱ��ͽ���ʱ��
		String qissj=this.getRiq();
		String jiessj=this.getRiq1();;
		
//		int gongys_id=Integer.parseInt(Param.substring(Param.lastIndexOf(',')+1));
		try{
		String str="select 0 as gl_id,h.id,h.hetbh,\n" +
			"       b.hetl,\n" + 
			"       j.mingc as jihkj,\n" + 
			"       to_char(h.qisrq, 'yyyy-mm-dd') as qisrq,\n" + 
			"       to_char(h.guoqrq, 'yyyy-mm-dd') as guoqrq,\n" + 
			"       b.mingc as yunsfs,\n" + 
			"       c.mingc as faz,\n" + 
			"       h.meikmcs\n" + 
			"  from hetb h,\n" + 
			"       (select hetb_id, faz_id, hb.yunsfsb_id,y.mingc, sum(hetl) as hetl\n" + 
			"          from hetslb hb,yunsfsb y where hb.yunsfsb_id=y.id \n" + 
			"         group by hetb_id, faz_id, hb.yunsfsb_id,y.mingc) b,\n" + 
			"       jihkjb j,\n" + 
//			"       yunsfsb y,\n" + 
			"       chezxxb c\n" + 
			" where h.id = b.hetb_id(+)\n" + 
			" and b.faz_id=c.id(+)\n" + 
//			" and b.yunsfsb_id=y.id\n" + 
			" and h.qisrq>=to_date('"+qissj+"','yyyy-mm-dd')\n" + 
			" and h.qisrq<=to_date('"+jiessj+"','yyyy-mm-dd')\n" + 
			" and h.guoqrq>=to_date('"+jiessj+"','yyyy-mm-dd')\n" + 
			" and h.jihkjb_id=j.id\n" + 
			" and (h.gongysb_id="+gongys_id+" or h.gongysb_id in(select distinct h.hetfhr_id as gongysb_id from hetfhrgysglb h \n"+
			"  where h.gongysb_id="+gongys_id+"))\n" + 
			" and h.id not in (select h.id from hetb h,jiesszfahtglb g where h.id=g.hetb_id and g.jiesszfab_id="+ID+")\n" + 
			" union\n" + 
			" select g.id as gl_id,\n" + 
			" h.id as id,h.hetbh,b.hetl,j.mingc as jihkj,\n" + 
			"   to_char(h.qisrq, 'yyyy-mm-dd') as qisrq,\n" + 
			"       to_char(h.guoqrq, 'yyyy-mm-dd') as guoqrq,\n" + 
			"        b.mingc as yunsfs,\n" + 
			"       c.mingc as faz,\n" + 
			"       h.meikmcs\n" + 
			"  from jiesszfahtglb g,hetb h,chezxxb c,jihkjb j,(select hetb_id, faz_id, hb.yunsfsb_id, y.mingc,sum(hetl) as hetl\n" + 
			"          from hetslb hb,yunsfsb y where  hb.yunsfsb_id=y.id \n" + 
			"         group by hetb_id, faz_id, hb.yunsfsb_id,y.mingc) b\n" + 
			" where g.hetb_id=h.id\n" + 
			" and h.id = b.hetb_id(+)\n" + 
			" and b.faz_id=c.id(+)\n" + 
//			" and b.yunsfsb_id=y.id\n" + 
//			" and h.qisrq>=to_date('"+qissj+"','yyyy-mm-dd')\n" + 
//			" and h.qisrq<=to_date('"+jiessj+"','yyyy-mm-dd')\n" + 
//			" and h.guoqrq>=to_date('"+jiessj+"','yyyy-mm-dd')\n" + 
			" and h.jihkjb_id=j.id\n" + 
			" and (h.gongysb_id="+gongys_id+" or h.gongysb_id in(select distinct h.hetfhr_id as gongysb_id from hetfhrgysglb h \n"+
			"  where h.gongysb_id="+gongys_id+")) and g.jiesszfab_id="+ID+" \n" +
//			2009-3-10 zsj���˷Ѻ�ͬ����
			" union \n" +
			"select 0 as gl_id,h.id,h.hetbh,\n" +
			"       0 as hetl,\n" + 
			"       '' as jihkj,\n" + 
			"       to_char(h.qisrq, 'yyyy-mm-dd') as qisrq,\n" + 
			"       to_char(h.guoqrq, 'yyyy-mm-dd') as guoqrq,\n" + 
			"       ysdw.mingc as yunsfs,\n" + 
			"       '' as faz,\n" + 
			"       b.meikmcs\n" + 
			"  from hetys h,yunsdwb ysdw,\n" + 
			"       (select jg.hetys_id,max(mk.mingc) as meikmcs from hetysjgb jg,meikxxb mk,gongysmkglb glb\n" + 
			"              where jg.meikxxb_id=mk.id(+)\n" + 
			"                    and mk.id=glb.meikxxb_id\n" + 
			"                    and glb.gongysb_id="+gongys_id+"  group by jg.hetys_id ) b\n" + 
			"\n" + 
			" where h.id = b.hetys_id\n" + 
			"       and h.yunsdwb_id=ysdw.id\n" + 
			"       and h.qisrq>=to_date('"+qissj+"','yyyy-MM-dd')\n" + 
			"       and h.qisrq<=to_date('"+jiessj+"','yyyy-MM-dd')\n" + 
			"       and h.guoqrq>=to_date('"+jiessj+"','yyyy-MM-dd')\n" + 
			"       and h.id not in\n" + 
			"       (select h.id from hetys h,jiesszfahtglb g where h.id=g.hetb_id and g.jiesszfab_id="+ID+") \n" +
			" union \n "+
			"select g.id as gl_id,h.id,h.hetbh,\n" +
			"       0 as hetl,\n" + 
			"       '' as jihkj,\n" + 
			"       to_char(h.qisrq, 'yyyy-mm-dd') as qisrq,\n" + 
			"       to_char(h.guoqrq, 'yyyy-mm-dd') as guoqrq,\n" + 
			"       ysdw.mingc as yunsfs,\n" + 
			"       '' as faz,\n" + 
			"       b.meikmcs\n" + 
			"  from hetys h,yunsdwb ysdw,jiesszfahtglb g,\n" + 
			"       (select jg.hetys_id,max(mk.mingc) as meikmcs from hetysjgb jg,meikxxb mk,gongysmkglb glb\n" + 
			"              where jg.meikxxb_id=mk.id(+)\n" + 
			"                    and mk.id=glb.meikxxb_id\n" + 
			"                    and glb.gongysb_id="+gongys_id+"  group by jg.hetys_id ) b\n" + 
			"\n" + 
			" where h.id = b.hetys_id\n" + 
			"		and g.hetb_id=h.id\n" +
			"       and h.yunsdwb_id=ysdw.id\n" + 
//			"       and h.qisrq>=to_date('"+qissj+"','yyyy-MM-dd')\n" + 
//			"       and h.qisrq<=to_date('"+jiessj+"','yyyy-MM-dd')\n" + 
//			"       and h.guoqrq>=to_date('"+jiessj+"','yyyy-MM-dd')\n"+
			"		and g.jiesszfab_id="+ID;
 
		ResultSetList rsl = con.getResultSetList(str);
		
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.getColumn("id").setHeader("id");	
		egu.getColumn("id").setHidden(true);
		egu.getColumn("gl_id").setHeader("gl_id");
		egu.getColumn("gl_id").setHidden(true);
		egu.getColumn("hetbh").setHeader("��ͬ���");
		egu.getColumn("hetbh").setWidth(150);
		egu.getColumn("hetl").setHeader("��ͬ��");
		egu.getColumn("jihkj").setHeader("�ƻ��ھ�");
		egu.getColumn("qisrq").setHeader("��ʼ����");
		egu.getColumn("guoqrq").setHeader("��������");
		egu.getColumn("yunsfs").setHeader("���䷽ʽ");
		egu.getColumn("faz").setHeader("��վ");
		egu.getColumn("meikmcs").setHeader("ú��λ");
		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
		egu.getGridColumns().add(2, new GridColumn(GridColumn.ColType_Check));

//		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(0);
		
	
		
		
		egu.addTbarText("��ʼ����:");
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
		
		
		String tmp="var rec=gridDiv_grid.getSelectionModel().getSelections();	\n"
			+ " if(rec.length==0){	\n"
			+ " document.getElementById('CHANGE').value='';\n"
			+ " document.getElementById('SaveButton').click();	\n"
			+ " return;}";
		
		egu.addToolbarButton(GridButton.ButtonType_Refresh, "RefreshButton");
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
	    egu.addToolbarButton("����",GridButton.ButtonType_SubmitSel_condition, "SaveButton", tmp);
	    egu.addToolbarItem("{"+new GridButton("����","function(){ document.getElementById('ReturnButton').click();" +
		"}",SysConstant.Btn_Icon_Return).getScript()+"}");
		setExtGrid(egu);
		
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
	}


	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}

	public String getGridScript() {
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		return getExtGrid().getHtml();
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

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) { 
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			getSelectData(visit.getString10());
			this.setRiq("");
			this.setRiq1("");
		}
	}

	
	
}