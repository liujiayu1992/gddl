package com.zhiren.dc.caiygl;

import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;


/*   wzb
 * 2009-7-13 17:09:10
 * �޸�ҳ���ѯ���������֮ǰ��ҳ��ģ����ѯ��Ϊ������ȷ��ѯ
 * 
 * 
 */
public class Caiyzzybm extends BasePage implements PageValidateListener {
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
	
	private boolean _ShowChick = false;
	public void ShowButton(IRequestCycle cycle) {
		_ShowChick = true;
	}
	private void Show(IRequestCycle cycle) {
		if(getChange()==null || "".equals(getChange())) {
			setMsg("��ѡ��һ�����ݽ��в鿴��");
			return;
		}
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		if(rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "ShujshQ.Save �� getExtGrid().getModifyResultSet(getChange()) ����û����ȷ���ؼ�¼����");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		/*if(rsl.getRows()!=1) {
			setMsg("��ѡ��һ�����д�ӡ��");
			return;
		}*/
		String caiydh="";
		while(rsl.next()) {
			caiydh+="'"+rsl.getString("caiybm")+"',";
			
		}
		String aa=caiydh.substring(0,caiydh.length()-1);
		setChepid(aa);
		cycle.activate("Caiyzzybm_mx");
	}
	
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			getSelectData();
		}
		if (_ShowChick) {
			_ShowChick = false;
			Show(cycle);
		}
	}
	

	private String filterDcid(Visit v){
		
		String sqltmp = " ("+ v.getDiancxxb_id()+")";
		if(v.isFencb()){
			JDBCcon con = new JDBCcon();
			ResultSetList rsl = con.getResultSetList("select id from diancxxb where fuid="+v.getDiancxxb_id());
			sqltmp = "";
			while(rsl.next()) {
				sqltmp += ","+rsl.getString("id");
			}
			sqltmp ="("+ sqltmp.substring(1) + ") ";
			rsl.close();
			con.Close();
		}
		return sqltmp;
	}
	
	private void setDiancxxb_id(String id){
		
		((Visit)this.getPage().getVisit()).setString13(id);
		
	}
	
	

	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		StringBuffer sb = new StringBuffer();
		
	
		sb.append( "select distinct c.zhillsb_id as id,c.caiybm, z.zhiybm\n")
				.append("  from (select bianm as caiybm, zhillsb_id\n")
				.append("          from zhuanmb\n")
				.append("         where zhillsb_id in\n")
				.append("               (select zm.zhillsb_id as id\n")
				.append("                  from zhuanmb zm, zhuanmlb lb, yangpdhb y, zhillsb z, fahb f\n")
				.append("                 where zm.zhuanmlb_id = lb.id\n")
				.append("                   and lb.jib = 1\n")
				.append("                   and y.zhilblsb_id = zm.zhillsb_id\n")
				.append("                   and f.zhilb_id = z.zhilb_id\n")
				.append("                   and z.id = zm.zhillsb_id)\n")
				.append("           and zhuanmlb_id =\n")
				.append("               (select id from zhuanmlb where mingc = '��������') and bianm like '%%') c,\n")
				.append("       (select bianm as zhiybm, zhillsb_id\n")
				.append("          from zhuanmb\n")
				.append("         where zhillsb_id in\n")
				.append("               (select zm.zhillsb_id as id\n")
				.append("                  from zhuanmb zm, zhuanmlb lb, yangpdhb y, zhillsb z, fahb f\n")
				.append("                 where zm.zhuanmlb_id = lb.id\n")
				.append("                   and lb.jib = 2\n")
				.append("                   and y.zhilblsb_id = zm.zhillsb_id\n")
				.append("                   and f.zhilb_id = z.zhilb_id\n")
				.append("                   and z.id = zm.zhillsb_id)\n")
				.append("           and zhuanmlb_id =\n")
				.append("               (select id from zhuanmlb where mingc = '��������')) z,\n")
				.append("       (select distinct f.id, f.diancxxb_id, z.id as zid\n")
				.append("          from zhillsb z, fahb f, chepb c\n")
				.append("         where f.zhilb_id = z.zhilb_id\n")
				.append("           and c.fahb_id = f.id\n")
				.append("           AND C.zhongcsj BETWEEN \n")
				.append(DateUtil.FormatOracleDate(getBeginRiq()))
				.append(" AND ")
				.append(DateUtil.FormatOracleDate(getEndRiq()))
				.append("+1) s\n")
				.append(" where c.zhillsb_id = z.zhillsb_id\n")
				.append("   and z.zhillsb_id = s.zid\n")
				.append("   and c.zhillsb_id = s.zid\n")
				.append(" and  s.diancxxb_id = "+visit.getDiancxxb_id()+" order by caiybm,zhiybm");

		
		
		
		

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
		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
//		���ö�ѡ��
		egu.addColumn(1,new GridColumn(GridColumn.ColType_Check));
		//����ÿҳ��ʾ����
		egu.addPaging(18);
		
		
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").editor=null;
		egu.getColumn("caiybm").setHeader("��������");
		egu.getColumn("caiybm").setWidth(90);
		egu.getColumn("caiybm").setEditor(null);
		egu.getColumn("zhiybm").setHeader("��������");
		egu.getColumn("zhiybm").setWidth(90);
		egu.getColumn("zhiybm").setEditor(null);
		
		
		
		
		egu.addTbarText("��������:");
		TextField tf=new TextField();
		tf.setWidth(100);
		tf.setId("TIAOJ");
		egu.addToolbarItem(tf.getScript());
		egu.addTbarText("-");
		
		String Gongysstr="function (){"+
		//"if(!gridDiv_sm.hasSelection()){Ext.MessageBox.alert('��ʾ','���ȵ��Ҫ���ع�����λ����������У�');return; };"+
   		" var url = 'http://'+document.location.host+document.location.pathname;"+
        "var end = url.indexOf(';');"+
		"url = url.substring(0,end);"+
   	    "url = url + '?service=page/' + 'Caiyzzybm_son&tiaoj='+TIAOJ.getValue();\n"+
   	    "var rewin =  window.showModalDialog(url,'newWin','dialogWidth=350px;dialogHeight=200px;status:no;scroll:no;');\n" +
  
   	   "if   (rewin != null){gridDiv_sm.getSelected().set('GONGHDWBM',rewin.bianh);}" +
   	    "" +
   	    "}";
	
		egu.addToolbarItem("{"+new GridButton("��ѯ",""+Gongysstr+"").getScript()+"}");
		egu.addTbarText("-");
		
		
		
		
		
		DateField df = new DateField();
		df.setValue(getBeginRiq());
		df.Binding("BeginRq", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setId("BeginRq");
		egu.addTbarText("������ڣ�");
		egu.addToolbarItem(df.getScript());
		
		DateField dfe = new DateField();
		dfe.setValue(getEndRiq());
		dfe.Binding("EndRq", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		dfe.setId("EndRq");
		egu.addTbarText("��");
		egu.addToolbarItem(dfe.getScript());
		

	
		
		GridButton refurbish = new GridButton("ˢ��","function (){document.getElementById('RefurbishButton').click();}");
		refurbish.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(refurbish);
		egu.addToolbarButton("��ӡ",GridButton.ButtonType_Sel, "ShowButton", null, SysConstant.Btn_Icon_Print);
		
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
			if(!visit.getActivePageName().toString().equals("Qicjjd")){
				setBeginRiq(DateUtil.FormatDate(new Date()));
				setEndRiq(DateUtil.FormatDate(new Date()));
				init();
				String dianclb= cycle.getRequestContext().getParameter("lx");
				if(dianclb!=null){
					visit.setString15(dianclb);
				}else{
					visit.setString15("PRINT_MOR");
				}
			}
			getSelectData();
			visit.setActivePageName(getPageName().toString());
		}
	} 
	
	private void init() {
		setExtGrid(null);
		setChepid(null);
		getSelectData();
	}
}