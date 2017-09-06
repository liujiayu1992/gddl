package com.zhiren.jt.jiesgl.report.pub;

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
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/**
 * @author ly
 *
 */
public class Ranljsd extends BasePage implements PageValidateListener {
//	�����û���ʾ
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);;
	}
	protected void initialize() {
		super.initialize();
		setMsg("");
		setTbmsg(null);
	}
	private String tbmsg;
	public String getTbmsg() {
		return tbmsg;
	}
	public void setTbmsg(String tbmsg) {
		this.tbmsg = tbmsg;
	}
	public void setChepid(String fahids) {
		((Visit)this.getPage().getVisit()).setString1(fahids);
	}
//	������
	public String getBeginRiq() {
		return ((Visit) getPage().getVisit()).getString3();
	}

	public void setBeginRiq(String riq) {
		((Visit) getPage().getVisit()).setString3(riq);
	}
	

	public String getEndRiq() {
		return ((Visit) getPage().getVisit()).getString2();
	}

	public void setEndRiq(String riq) {
		((Visit) getPage().getVisit()).setString2(riq);
	}
	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	private boolean _RefurbishChick = false;
	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}
	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	private boolean _ShowChick = false;
	public void ShowButton(IRequestCycle cycle) {
		_ShowChick = true;
	}
	private void Show(IRequestCycle cycle) {
		if(getChange()==null || "".equals(getChange())) {
			setMsg("��ѡ�����ݽ��д�ӡ��");
			return;
		}
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		if(rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "ShujshQ.Save �� getExtGrid().getModifyResultSet(getChange()) ����û����ȷ���ؼ�¼����");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		String jiesdid="";
		String bianh="";
		while(rsl.next()) {
			
			jiesdid+=""+rsl.getString("ID")+",";
			bianh=rsl.getString("bianm");
			
		}
		String aa1=jiesdid.substring(0,jiesdid.length()-1);
		setChepid(null);
		setChepid(aa1);
		((Visit) getPage().getVisit()).setString4("");
		((Visit) getPage().getVisit()).setString4(bianh);
		cycle.activate("Jiesdcx_hs");
	}
	
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			getSelectData();
		}
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if (_ShowChick) {
			_ShowChick = false;
			Show(cycle);
		}
	}
	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		//visit.getExtGrid1().Save(getChange(), visit);
		ResultSetList rsl = visit.getExtGrid1().getModifyResultSet(getChange());
		StringBuffer sql = new StringBuffer();
		sql.delete(0, sql.length());
		sql.append("begin \n");
		while (rsl.next()) {
			
			if("0".equals(rsl.getString("ID"))){
				//��������ӵ����
			}else{
				//����fuid=0�Ľ��㵥��Ԥ������
				 sql.append("update jiesb set yufkje="+
						 + rsl.getDouble("yufkje")+""
						 + " where id=" + rsl.getLong("id")+";\n");
				 //�����ӽ��㵥��Ԥ����
				 UPDateZi(rsl.getLong("id"),rsl.getDouble("yufkje"));
				 
				 
			}
		}
		sql.append("end;");
	
		con.getUpdate(sql.toString());
		con.Close();
	}
	//�����ӽ����Ԥ����
	public void UPDateZi(long id,double jine){
		JDBCcon con = new JDBCcon();
		StringBuffer sql2 = new StringBuffer();
		double yufkje=jine;
		 //�ж��Ƿ����ӽ��㵥,�ҳ�id��С���ӽ��㵥,��������Ԥ����
		 String sql_id="select min(id) as id from jiesb where fuid="+id;
		 ResultSetList rs1 = con.getResultSetList(sql_id.toString());
		 while(rs1.next()){
			//System.out.println(rs1.getLong("id"));
			 sql2.append("update jiesb set yufkje="+ yufkje+"" + " where id=" + rs1.getLong("ID")+"");
		 }
		 con.getUpdate(sql2.toString());
		con.Close();
	}
	
	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		StringBuffer sb = new StringBuffer();
		String gys = "";
		if(getHetdwValue().getValue().equals("��ѡ��")){
			gys = " ";
		}else{
			gys = "	and j.gongysb_id = " + getHetdwValue().getId() + "\n";
		}
		
		//�������Ų�����"-"ʱ,ֻ���ս����Ų�ѯ
		boolean HaveJiesbh=false;
		String jsbh="";
		if(this.getJiesbhValue().getValue().equals("-")){
			HaveJiesbh=true;
		}else{
			HaveJiesbh=false;
			jsbh = "	and j.id = " + getJiesbhValue().getId() + "\n";
		}
		
		
		//ֻˢ�³����׽��㵥
		//String sql = 
			sb.append("select j.id,\n" );
			sb.append("       to_char(j.jiesrq,'yyyy-MM-dd') as jiesrq,\n" );
			sb.append("       j.bianm,\n");
			sb.append("       g.mingc as hetdw,\n" );
			sb.append("       j.ches,\n" );
			sb.append("       j.jiessl as jiesml,\n" );
			sb.append("      (j.hansmk+nvl(guotyf,0)+nvl(kuangqyf,0)-nvl(yf.kuidjfyf,0)-nvl(yf.kuidjfzf,0)) as jiesmk,\n" ); 
			sb.append("        j.yufkje as yufkje \n");
			sb.append("  from jiesb j,gongysb g,jiesyfb yf\n" ); 
			sb.append("  where j.gongysb_id = g.id(+)\n" );
			sb.append("  and  j.bianm=yf.bianm(+)\n" );
			sb.append("  and  j.fuid=0\n" );
			if(HaveJiesbh){
				sb.append("	and j.jiesrq BETWEEN " );
				sb.append( DateUtil.FormatOracleDate(getBeginRiq()) );
				sb.append( " AND  " );
				sb.append( DateUtil.FormatOracleDate(getEndRiq()));
				sb.append( "+1 \n"); 
				sb.append(gys );
			}else{
				sb.append(jsbh);
			}
			
			sb.append("order by j.jiesrq,j.yansbh,g.mingc");

		

		ResultSetList rsl = con.getResultSetList(sb.toString());
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "��������SQL:" + sb);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		//����ҳ����
		egu.setWidth(Locale.Grid_DefaultWidth);
//		egu.setHeight("bodyHeight");
		egu.addPaging(0);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);//�����Ƿ���Ա༭
		egu.setGridSelModel(ExtGridUtil.GridselModel_Row_single);
//		���ö�ѡ��
		egu.addColumn(1,new GridColumn(GridColumn.ColType_Check));
		
	
		
		//����ÿҳ��ʾ����
		egu.addPaging(100);
		
		egu.getColumn("id").setHeader("ID");
		egu.getColumn("id").hidden=true;
		
		egu.getColumn("jiesrq").setHeader("��������");
		egu.getColumn("jiesrq").setWidth(80);
		egu.getColumn("jiesrq").editor=null;
		
		egu.getColumn("bianm").setHeader("������");
		egu.getColumn("bianm").setWidth(130);
		egu.getColumn("bianm").editor=null;
		
		egu.getColumn("hetdw").setHeader("��ͬ��λ");
		egu.getColumn("hetdw").setWidth(150);
		egu.getColumn("hetdw").editor=null;
		
		egu.getColumn("ches").setHeader("����");
		egu.getColumn("ches").setWidth(50);
		egu.getColumn("ches").editor=null;
	
		egu.getColumn("jiesml").setHeader("����ú��");
		egu.getColumn("jiesml").setWidth(80);
		egu.getColumn("jiesml").editor=null;
		
		egu.getColumn("jiesmk").setHeader("����ú��");
		egu.getColumn("jiesmk").setWidth(80);
		egu.getColumn("jiesmk").editor=null;
	
		egu.getColumn("yufkje").setHeader("����Ӧ��Ԥ���");
		egu.getColumn("yufkje").setWidth(100);
		
		DateField df = new DateField();
		df.setValue(getBeginRiq());
		df.Binding("BeginRq", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setId("BeginRq");
		egu.addTbarText("�������ڣ�");
		egu.addToolbarItem(df.getScript());
		
		DateField dfe = new DateField();
		dfe.setValue(getEndRiq());
		dfe.Binding("EndRq", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		dfe.setId("EndRq");
		egu.addTbarText("��");
		egu.addToolbarItem(dfe.getScript());
		
		egu.addTbarText("-");
		
		egu.addTbarText("��ͬ��λ:");
		ComboBox comb5 = new ComboBox();
		comb5.setTransform("HetdwDropDown");
		comb5.setId("Heth");
		comb5.setEditable(false);
		comb5.setLazyRender(true);// ��̬��
		comb5.setWidth(135);
		comb5.setReadOnly(true);
		egu.addToolbarItem(comb5.getScript());
		
		egu.addTbarText("-");
		
		
		egu.addTbarText("������:");
		ComboBox comb6 = new ComboBox();
		comb6.setTransform("JiesbhDropDown");
		comb6.setId("jiesbh");
		comb6.setEditable(true);
		comb6.setLazyRender(true);// ��̬��
		comb6.setWidth(135);
		comb6.setReadOnly(false);
		egu.addToolbarItem(comb6.getScript());
		
		egu.addTbarText("-");
		
		
		
		
		GridButton refurbish = new GridButton("ˢ��","function (){document.getElementById('RefurbishButton').click();}");
		refurbish.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(refurbish);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		egu.addToolbarButton("��ӡ",GridButton.ButtonType_Sel, "ShowButton", null, SysConstant.Btn_Icon_Print);
		
//		egu.addOtherScript(" gridDiv_grid.addListener('cellclick',function(grid, rowIndex, columnIndex, e){});");
		
		setExtGrid(egu);
		con.Close();
	}

	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}

	public String getGridScript() {
		if (getExtGrid() == null) {
			return "";
		}
		if(getTbmsg()!=null) {
			getExtGrid().addToolbarItem("'->'");
			getExtGrid().addToolbarItem("'<marquee width=300 scrollamount=2>"+getTbmsg()+"</marquee>'");
		}
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		if (getExtGrid() == null) {
			return "";
		}
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
//			if(!visit.getActivePageName().toString().equals("Qicjjd")){
//				setBeginRiq(DateUtil.FormatDate(new Date()));
//				setEndRiq(DateUtil.FormatDate(new Date()));
//				init();
//				String dianclb= cycle.getRequestContext().getParameter("lx");
//				if(dianclb!=null){
//					visit.setString15(dianclb);
//				}else{
//					visit.setString15("PRINT_MOR");
//				}
//			}
			setBeginRiq(DateUtil.FormatDate(new Date()));
			setEndRiq(DateUtil.FormatDate(new Date()));
			setHetdwValue(null);	//2
			setHetdwModel(null);
			getHetdwModels();		//2
			this.setJiesbhValue(null);
			this.setJiesbhModel(null);
			this.getJiesbhModels();
			init();
			visit.setActivePageName(getPageName().toString());
		}
	} 
	
	private void init() {
		setExtGrid(null);
		setChepid(null);
		getSelectData();
	}
	
	
//	 ��ͬ��λ
	public IDropDownBean getHetdwValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean2((IDropDownBean) getHetdwModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean2();
	}

	public void setHetdwValue(IDropDownBean Value) {

		if (Value != ((Visit) getPage().getVisit()).getDropDownBean2()) {

			((Visit) getPage().getVisit()).setboolean1(true);
		}
		((Visit) getPage().getVisit()).setDropDownBean2(Value);
	}

	public void setHetdwModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(value);
	}

	public IPropertySelectionModel getHetdwModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getHetdwModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public IPropertySelectionModel getHetdwModels() {
		
		String sql=
			" select distinct g.id,g.mingc\n" +
			" from jiesb j,gongysb g\n" + 
			" where j.gongysb_id = g.id\n" +
			" order by g.mingc\n";

			((Visit) getPage().getVisit())
			.setProSelectionModel2(new IDropDownModel(sql, "��ѡ��"));
	return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}
	
	

//	 ������
	public IDropDownBean getJiesbhValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean3() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean3((IDropDownBean) getJiesbhModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean3();
	}

	public void setJiesbhValue(IDropDownBean Value) {

		if (Value != ((Visit) getPage().getVisit()).getDropDownBean3()) {

			((Visit) getPage().getVisit()).setboolean1(true);
		}
		((Visit) getPage().getVisit()).setDropDownBean3(Value);
	}

	public void setJiesbhModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel3(value);
	}

	public IPropertySelectionModel getJiesbhModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {
			getJiesbhModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}

	public IPropertySelectionModel getJiesbhModels() {
		
		String sql="select j.id,j.bianm from jiesb j where j.fuid=0 order by j.bianm";
			

			((Visit) getPage().getVisit())
			.setProSelectionModel3(new IDropDownModel(sql,"-"));
	return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}
}