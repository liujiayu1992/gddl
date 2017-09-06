package com.zhiren.jt.zdt.gonggxx.shicdt;

import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Shijgkjhjg extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
	}
	protected void initialize() {
		// TODO �Զ����ɷ������
		super.initialize();
		setMsg("");
	}

	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		if (getChange() == null || "".equals(getChange())) {
			setMsg("û����Ҫ����ļ�¼��");
			return;
		}
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		StringBuffer sql = new StringBuffer("begin \n");
		String tableName="shijgkjhjgb";
		ResultSetList mdrsl = getExtGrid().getModifyResultSet(getChange());
		while (mdrsl.next()) {
			String id=mdrsl.getString("id");
			String item_id=mdrsl.getString("item_id");
			String RIQ=DateUtil.FormatOracleDate(getEndRiq());
			String FARL=mdrsl.getString("farl");
			String JILJZ=mdrsl.getString("jiljz");
			String JIAOHFS=mdrsl.getString("jiaohfs");
			String LIUF=mdrsl.getString("liuf");
			String HUIF=mdrsl.getString("huif");
			String JIAG=mdrsl.getString("jiag");
			
			if ("0".equals(id)) {
				id="getnewid("+visit.getDiancxxb_id()+")";
				sql.append("insert into ").append(tableName).append("(id,item_id,riq,farl,jiljz,jiaohfs,liuf,huif,jiag");
				sql.append(") values("+id+","+item_id+","+RIQ+","+FARL+",'"+JILJZ+"','"+JIAOHFS+"',"+LIUF+","+HUIF+","+JIAG+");\n");
			} else {
				sql.append("update ").append(tableName).append(" set ");
				sql.append(" farl="+FARL+", jiljz='"+JILJZ+"', jiaohfs='"+JIAOHFS+"', liuf="+LIUF+",huif="+HUIF+",jiag="+JIAG);
				sql.append(" where id =").append(id).append(";\n");
			}
		}
		mdrsl.close();
		sql.append("end;");
		int flag = con.getUpdate(sql.toString());
		if (flag>-1){
			setMsg("����ɹ���");
			con.commit();
		}else{
			setMsg("����ʧ�ܣ�");
		}
		con.Close();
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	private boolean _RefreshChick = false;
	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
		}
		if (_RefreshChick) {
			_RefreshChick = false;
			System.out.print(getEndRiq());
	
		}
	}
	

//�󶨽�������
	private String endRiq;
	public String getEndRiq() {
		return endRiq;
	}
	public void setEndRiq(String riq) {
		this.endRiq = riq;
	}

	public void getSelectData() {
		// Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		
		String sql=	"select nvl(s.id,0) id ,nvl(s.item_id,i.id) item_id , i.mingc guoj,i.beiz gangk,\n" +
			"--s.riq,\n" + 
			"nvl(s.farl,6000) farl," +
			"nvl(s.jiljz,'NCV') jiljz, nvl(s.jiaohfs,'FOB') jiaohfs,s.liuf,s.huif,s.jiag\n" + 
			"from (select * from shijgkjhjgb where riq="+DateUtil.FormatOracleDate(getEndRiq()) +") s, item i, itemsort i2\n" + 
			"where i2.id=i.itemsortid and i2.mingc='������Ҫ�ۿڽ�����׼��' and i.id=s.item_id(+)\n"+
			 " order by i.xuh";
//		System.out.print(sql);
		ResultSetList rsl = con
				.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("shijgkjhjgb");
		egu.getColumn("id").setHeader("ID");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").editor = null;
		egu.getColumn("item_id").setHeader("item_id");
		egu.getColumn("item_id").setHidden(true);
		egu.getColumn("item_id").editor = null;
		
		egu.getColumn("guoj").setHeader("���Һ͵���");
		egu.getColumn("guoj").setWidth(100);
		egu.getColumn("guoj").editor = null;
		
		egu.getColumn("gangk").setHeader("�ۿ�");
		egu.getColumn("gangk").setWidth(100);
		egu.getColumn("gangk").editor = null;
		
		egu.getColumn("farl").setHeader("������(Kcal/Kg)");
		egu.getColumn("farl").setWidth(100);
		((NumberField)egu.getColumn("farl").editor).setDecimalPrecision(0);
		
		egu.getColumn("jiljz").setHeader("������׼");
		egu.getColumn("jiljz").setWidth(70);
		
		egu.getColumn("jiaohfs").setHeader("������ʽ");
		egu.getColumn("jiaohfs").setWidth(70);
		
		egu.getColumn("liuf").setHeader("�������%");
		egu.getColumn("liuf").setWidth(70);
		((NumberField)egu.getColumn("liuf").editor).setDecimalPrecision(3);
		egu.getColumn("huif").setHeader("���ҷ�%");
		egu.getColumn("huif").setWidth(70);
		((NumberField)egu.getColumn("huif").editor).setDecimalPrecision(3);
		egu.getColumn("jiag").setHeader("�۸�(��Ԫ/��)");
		egu.getColumn("jiag").setWidth(100);
		((NumberField)egu.getColumn("jiag").editor).setDecimalPrecision(5);
		egu.getColumn("jiag").editor.setMaxLength(9);

		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);

		egu.addTbarText("����:");
		
		DateField dEnd = new DateField();
		dEnd.Binding("ENDRIQ","forms[0]");
		dEnd.setValue(getEndRiq());
		dEnd.setId("Endriq");
		egu.addToolbarItem(dEnd.getScript());
		egu.addTbarText("-");
		
		GridButton gRefresh = new GridButton("ˢ��","function(){document.getElementById('RefreshButton').click();}");
		gRefresh.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gRefresh);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");

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
			setEndRiq(DateUtil.FormatDate(new Date()));
		}
		getSelectData();
	}
}
