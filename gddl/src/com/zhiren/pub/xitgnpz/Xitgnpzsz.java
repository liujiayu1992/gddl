package com.zhiren.pub.xitgnpz;

import java.util.ArrayList;
import java.util.List;
	
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/*
 * ���ߣ�����
 * ʱ�䣺2009-08-12 14��34
 * �������޸�zidm�ֶβ��ɱ༭����ȥɾ����ť����ֹϵͳ�������ó���
 */
public class Xitgnpzsz extends BasePage implements PageValidateListener {
//		����ҳ����ʾ��Ϣ������
	private String _msg;
	public String getMsg() {
        if (_msg == null) {
            _msg = "";
        }
        return _msg;
    }
	public void setMsg(String _value) {
	    _msg = _value;
	}
    protected void initialize() {
        super.initialize();
        _msg = "";
    }
//	  ҳ��仯��¼
    private String Change;
    public String getChange() {
    	return Change;
    }
    public void setChange(String change) {
    	Change = change;
    }
//		ҳ���ʼ��
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if(!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			visit.setActivePageName(this.getPageName().toString());
			getSelectData();
		}
	}
//		��ť�¼�����

	private boolean _shuaxin = false;
	
	public void ShuaxinButton(IRequestCycle cycle) {
		_shuaxin = true;
	}
		
	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	private boolean _InsertChick = false;

	public void InsertButton(IRequestCycle cycle) {
		_InsertChick = true;
	}
	private boolean _DeleteChick = false;

	public void DeleteButton(IRequestCycle cycle) {
		_DeleteChick = true;
	}
	private boolean _ReturnChick = false;

	public void ReturnButton(IRequestCycle cycle) {
		_ReturnChick = true;
	}
	

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if (_InsertChick) {
			_InsertChick = false;
			
		}
		if (_DeleteChick) {
			_DeleteChick = false;
			
		}
		if (_shuaxin){
			_shuaxin=false;
			
		}
		
		if (_ReturnChick){
			_ReturnChick=false;
			gotobaobpz(cycle);
			
		}
		
	}
	private void gotobaobpz(IRequestCycle cycle) {

//			System.out.println(((Visit) this.getPage().getVisit()).getString1());
		cycle.activate("Xitgnpz");
	}
	public String getValueSql(GridColumn gc, String value) {
		if("string".equals(gc.datatype)) {
			if(gc.combo != null) {
				if(gc.returnId) {
					return "" + gc.combo.getBeanId(value);
				}else {
					return "'"+value+"'";
				}
			}else {
				return "'"+value+"'";
			}
			
		}else if("date".equals(gc.datatype)) {
			return "to_date('"+value+"','yyyy-mm-dd')";
		}else if("float".equals(gc.datatype)){
			if(gc.combo != null) {
				if(gc.returnId) {
					return "" + gc.combo.getBeanId(value);
				}else {
					return "'"+value+"'";
				}
			}
			else {
				return value==null||"".equals(value)?"null":value;
			}
//				return value==null||"".equals(value)?"null":value;
		}else{
			return value;
		}
	}
    
//		�������ĸĶ�
	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		visit.getExtGrid1().Save(getChange(), visit);
	}
	
	/*public void Save1(String strchange,Visit visit) {
		String tableName="duanxdypzb";
		
		JDBCcon con = new JDBCcon();
		
		StringBuffer sql = new StringBuffer("begin \n");
		
		ResultSetList delrsl = visit.getExtGrid1().getDeleteResultSet(strchange);
		while(delrsl.next()) {
			sql.append("delete from ").append(tableName).append(" where id =").append(delrsl.getString(0)).append(";\n");
		}
		
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(strchange);

		while(mdrsl.next()) {
		
			StringBuffer sql2 = new StringBuffer();
	
			if("0".equals(mdrsl.getString("ID"))) {
            	sql.append("insert into ").append(tableName).append("(id");
            	sql2.append("getnewid("+visit.getDiancxxb_id()+")");
				
				for(int i=1;i<mdrsl.getColumnCount();i++) {
			
					sql.append(",").append(mdrsl.getColumnNames()[i]);					
					sql2.append(",").append(getValueSql(visit.getExtGrid1().getColumn(mdrsl.getColumnNames()[i]),mdrsl.getString(i)));}

				sql.append(") values(").append(sql2).append(");\n");
				
			}else {
				
				sql.append("update ").append(tableName).append(" set ");
				for(int i=1;i<mdrsl.getColumnCount();i++) {
					
					sql.append(mdrsl.getColumnNames()[i]).append(" = ");					
					sql.append(getValueSql(visit.getExtGrid1().getColumn(mdrsl.getColumnNames()[i]),mdrsl.getString(i))).append(",");

				}
				sql.deleteCharAt(sql.length()-1);
				sql.append(" where id =").append(mdrsl.getString("ID")).append(";\n");
			}
			
		}
		sql.append("end;");
		System.out.println(sql.toString());
		con.getUpdate(sql.toString());
	}*/
	
	public void getSelectData() {
		// Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con.getResultSetList("select id,xitpzb_id,zidm,biaot,xuh,kuand,decode(shifxs,0,'��',1,'��')as shifxs from Xitpzzb where xitpzb_id="+((Visit) this.getPage().getVisit()).getString1()+" order by xuh");

		
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("Xitpzzb ");
		egu.getColumn("id").setHeader("ID");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("xitpzb_id").setHeader("ϵͳ���ñ�ID");	
		egu.getColumn("xitpzb_id").setEditor(null);
		egu.getColumn("xitpzb_id").setHidden(true);
		egu.getColumn("zidm").setHeader("�ֶ�");	
		egu.getColumn("zidm").setEditor(null);
		egu.getColumn("biaot").setHeader("����");
		egu.getColumn("biaot").setWidth(150);
		egu.getColumn("xuh").setHeader("���");	
		egu.getColumn("xuh").editor.setMaxValue("99999");
		egu.getColumn("kuand").setHeader("���");
		egu.getColumn("shifxs").setHeader("�Ƿ���ʾ");	
		
		List l = new ArrayList();
		l.add(new IDropDownBean(0, "��"));
		l.add(new IDropDownBean(1, "��"));
		egu.getColumn("shifxs").setEditor(new ComboBox());
		egu.getColumn("shifxs").setComboEditor(egu.gridId,
				new IDropDownModel(l));
		egu.getColumn("shifxs").setDefaultValue("��");
		egu.getColumn("shifxs").setReturnId(true);
		egu.getColumn("shifxs").setWidth(70);
//			
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);		
		egu.setWidth(Locale.Grid_DefaultWidth);
		
		
		StringBuffer sb=new StringBuffer();
		
		sb.append("{text:' ���',icon:'imgs/btnicon/insert.gif',cls:'x-btn-text-icon',minWidth:75,handler:function() {	\n")
			
			.append("var plant = new gridDiv_plant({ID: '0',XITPZB_ID: '").append(((Visit) this.getPage().getVisit()).getString1()).append("',ZIDM: '',BIAOT: '',XUH: '',SHIFXS: '��'});	\n")
			.append("gridDiv_ds.insert(gridDiv_ds.getCount(),plant);	\n}}");
		
		egu.addToolbarItem(sb.toString());
		egu.addTbarText("-");	
		egu.addToolbarButton(GridButton.ButtonType_Refresh, "ShuaxinButton");
		egu.addTbarText("-");	
//		egu.addToolbarButton(GridButton.ButtonType_Delete,"");
//		egu.addTbarText("-");
	    egu.addToolbarButton(GridButton.ButtonType_Save_condition, "SaveButton","");
	    egu.addTbarText("-");
		egu.addToolbarItem("{"
				+ new GridButton("����",
						"function(){ document.getElementById('ReturnButton').click();"
								+ "}").getScript() + "}");
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
}

	