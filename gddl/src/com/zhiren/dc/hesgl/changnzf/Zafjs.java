package com.zhiren.dc.hesgl.changnzf;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.main.Visit;

/**
 * @author ������
 * 2009-06-12
 * ������Zafjs(�ӷѽ���)
 */

public class Zafjs extends BasePage {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}
	
	protected void initialize() {
		msg = "";
	}
	
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
	private boolean _SaveChick = false;
	
	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	private boolean _ReturnChick = false;
	
	public void ReturnButton(IRequestCycle cycle) {
		_ReturnChick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			if(save()) {
				cycle.activate("Zafjsxz");
			}
		}
		if (_ReturnChick) {
			_ReturnChick = false;
			cycle.activate("Zafjsxz");
		}
	}
	
	public void getSelectData() {
		JDBCcon con = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		
		Visit visit = (Visit)this.getPage().getVisit();
		String gysmc = visit.getString1();
		double shul = visit.getDouble2();
		double danj = visit.getDouble3();
		double bhszf = visit.getDouble4();
		double zfsk = visit.getDouble5();
		double feiyhj = visit.getDouble6();
		String quanc = visit.getString7();
		String qiszyfw = visit.getString8();
		String jiezzyfw = visit.getString9();
		String beiz = visit.getString10();
		String bianm = visit.getString12();
		
		sbsql.append("select xiangm, zhi from (" +
				"select '������' as xiangm,'"+ bianm +"' as zhi , 1 as num from dual union " +
				"select '��Ӧ������' as xiangm, '"+ gysmc +"' as zhi , 2 as num from dual union " +
				"select '��������' as xiangm, '"+ shul + "' as zhi, 3 as num from dual union " +
				"select '����' as xiangm, '"+ danj + "' as zhi, 4 as num from dual union " +
				"select '����˰�ӷ�' as xiangm, '"+ bhszf +"' as zhi, 5 as num from dual union " +
				"select '�ӷ�˰��' as xiangm, '"+ zfsk +"' as zhi, 6 as num from dual union " +
				"select '���úϼ�' as xiangm, '"+ feiyhj +"' as zhi, 7 as num from dual union " +
				"select '�տλ' as xiangm, '"+ quanc +"' as zhi, 8 as num from dual union " +
				"select '��ʼ���÷�Χ' as xiangm, '"+ qiszyfw +"' as zhi, 9 as num from dual union " +
				"select '��ֹ���÷�Χ' as xiangm, '"+ jiezzyfw +"' as zhi, 10 as num from dual union " +
				"select '��ע' as xiangm, '"+ beiz +"' as zhi, 11 as num from dual ) order by num");
		
		ResultSetList rsl = con.getResultSetList(sbsql.toString());
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("dual");
		egu.getColumn("xiangm").setHeader("��Ŀ����");
		egu.getColumn("zhi").setHeader("ֵ");
		egu.getColumn("zhi").setWidth(300);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		
		egu.addOtherScript("\n gridDiv_grid.on('beforeedit',function(e){ 							\n" +
			"if(e.field=='ZHI') {																	\n" +
			"	if(e.row==4 || e.row==5) {															\n" +
			"		var temp4 = gridDiv_ds.getAt(4).get('ZHI');										\n" +
			"		var temp5 = gridDiv_ds.getAt(5).get('ZHI');										\n" +
			"		gridDiv_grid.on('afteredit',function(a){										\n" +
			"			if ((/^\\d+(\\.\\d+)?$/.test(gridDiv_ds.getAt(4).get('ZHI'))) " +
			"				&& (/^\\d+(\\.\\d+)?$/.test(gridDiv_ds.getAt(5).get('ZHI')))) {			\n" +
			"				temp4 = gridDiv_ds.getAt(4).get('ZHI');									\n" +
			"				temp5 = gridDiv_ds.getAt(5).get('ZHI');									\n" +
			"				var row4 = eval(gridDiv_ds.getAt(4).get('ZHI'));						\n" +
			"				var row5 = eval(gridDiv_ds.getAt(5).get('ZHI'));						\n" +
			"				var result = row4 + row5;												\n" +
			"				gridDiv_ds.getAt(gridDiv_ds.getCount()-5).set('ZHI',result.toString());	\n" +
			"			} else {																	\n" +
			"				Ext.MessageBox.alert('��ʾ��Ϣ','�������ݱ���Ϊ���֣�');					\n" +
			"				gridDiv_ds.getAt(gridDiv_ds.getCount()-6).set('ZHI',temp5.toString());	\n" +
			"				gridDiv_ds.getAt(gridDiv_ds.getCount()-7).set('ZHI',temp4.toString());	\n" +
			"			}																			\n" +
			"		}); 																			\n" +
			"	} else if(e.row==0) {																\n" +
			"		e.cancel=false;																	\n" +
			"	} else if(e.row==10) {																\n" +
			"		e.cancel=false;																	\n" +
			"	} else {																			\n" +
			"		e.cancel=true;																	\n" +
			"	}																					\n" +
			"} else {																				\n" +
			"	e.cancel=true;																		\n" +
			"}																						\n" +
			"});																					\n");
		
		egu.addTbarBtn(new GridButton("����", "function(){document.getElementById('ReturnButton').click();}", 
				SysConstant.Btn_Icon_Return));
		
		egu.addToolbarButton("����", GridButton.ButtonType_SaveAll, "SaveButton");
		egu.addPaging(0);
		setExtGrid(egu);
		
		rsl.close();
		con.Close();
	}
	
	public boolean save() {
		Visit visit = (Visit)this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		StringBuffer istsql = new StringBuffer();
		StringBuffer sbrsl = new StringBuffer();
		istsql.append("begin\n");
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(getChange());
		
		String bianm = "";
		while(mdrsl.next()) {
			if (mdrsl.getString("XIANGM").equals("��Ӧ������")) {
				sbrsl.append("'").append(mdrsl.getString("ZHI")).append("', ");
			} else if(mdrsl.getString("XIANGM").equals("����˰�ӷ�")) {
				visit.setDouble4(Double.parseDouble(mdrsl.getString("ZHI")));
				sbrsl.append(mdrsl.getString("ZHI")).append(", ");
			} else if(mdrsl.getString("XIANGM").equals("�ӷ�˰��")) {
				visit.setDouble5(Double.parseDouble(mdrsl.getString("ZHI")));
				sbrsl.append(mdrsl.getString("ZHI")).append(", ");
			} else if(mdrsl.getString("XIANGM").equals("���úϼ�")) {
				visit.setDouble6(Double.parseDouble(mdrsl.getString("ZHI")));
				sbrsl.append(mdrsl.getString("ZHI")).append(", ");
			} else if(mdrsl.getString("XIANGM").equals("��ע")) {
				visit.setString10(mdrsl.getString("ZHI"));
				sbrsl.append("'").append(mdrsl.getString("ZHI")).append("', ");
			} else if (mdrsl.getString("XIANGM").equals("��ʼ���÷�Χ") || mdrsl.getString("XIANGM").equals("��ֹ���÷�Χ")) {
				sbrsl.append("to_date('").append(mdrsl.getString("ZHI")).append("', 'yyyy-MM-dd'), ");
			} else if (mdrsl.getString("XIANGM").equals("�տλ")) {
				sbrsl.append(visit.getString13()).append(", ");
			} else if (mdrsl.getString("XIANGM").equals("������")){
				bianm = mdrsl.getString("ZHI");
				visit.setString12(bianm);
			} else {
				sbrsl.append(mdrsl.getString("ZHI")).append(", ");
			}
		}
		
		StringBuffer sbsql2 = new StringBuffer();
		sbsql2.append("select getnewid(").append(visit.getDiancxxb_id()).append(") id from dual");
		ResultSetList idrsl = con.getResultSetList(sbsql2.toString());
		String newId = "";
		while(idrsl.next()) {
			newId = idrsl.getString("ID");
		}
		
		String sltsql = "select bianm from zafjsb where bianm = '"+ bianm +"'";
		ResultSetList sltrsl = con.getResultSetList(sltsql);
		if (sltrsl.next()) {
			this.setMsg("�������Ѿ����ڣ�����������");
			return false;
		} else {
			String ids = visit.getString11();
			istsql.append("insert into zafjsb(id, bianm, riq, diancxxb_id, gongysmc, jiessl, danj, buhszf, zafsk, hej, shoukdw_id, qiszyfw, jiezzyfw, beiz)");
			istsql.append(" values(").append(newId).append(", '").append(bianm).append("', ").append("to_date(to_char(sysdate,'yyyy-MM-dd'),'yyyy-MM-dd')").append(", ").append(visit.getDiancxxb_id()).append(", ").append(sbrsl.substring(0, sbrsl.length() - 2)).append("); \n");
			istsql.append("update changnzfb set zafjsb_id = " + newId +" where id in (" + ids + "); \n");
			istsql.append("end;");
			
			con.getUpdate(istsql.toString());
		}
		mdrsl.close();
		idrsl.close();
		sltrsl.close();
		con.Close();
		return true;
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
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setProSelectionModel2(null);
			visit.setDropDownBean2(null);
		}
		getSelectData();
	}
}
