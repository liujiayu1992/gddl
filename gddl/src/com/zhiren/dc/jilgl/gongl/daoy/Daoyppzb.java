package com.zhiren.dc.jilgl.gongl.daoy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
import com.zhiren.common.Locale;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Daoyppzb extends BasePage implements PageValidateListener {
//	����ҳ����ʾ��Ϣ������
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
//  ҳ��仯��¼
    private String Change;
    public String getChange() {
    	return Change;
    }
    public void setChange(String change) {
    	Change = change;
    }
//	ҳ���ʼ��
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if(!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			visit.setActivePageName(this.getPageName().toString());
			setBeginRiq(DateUtil.FormatDate(new Date()));
			setEndRiq(DateUtil.FormatDate(new Date()));
			getSelectData();
		}
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
	
//	��ť�¼�����

	private boolean _shuaxin = false;
	
	public void ShuaxinButton(IRequestCycle cycle) {
		_shuaxin = true;
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
			Save();
			getSelectData();
		}

		if (_shuaxin){
			_shuaxin=false;
			getSelectData();
		}
		
		if (_ReturnChick){
			_ReturnChick=false;
			gotoDaoypp(cycle);
			
		}
		
	}
	private void gotoDaoypp(IRequestCycle cycle) {

//		System.out.println(((Visit) this.getPage().getVisit()).getString1());
		cycle.activate("Daoypp");
	}
    
//	�������ĸĶ�
	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		Save1(getChange(),visit);
	}
	
	public void Save1(String strchange,Visit visit) {
		String tableName="daozcpb";
		boolean isinsert=false;//�ж��Ƿ������ݲ���
		boolean isdelete=false;//�ж��Ƿ�������ɾ��
		JDBCcon con = new JDBCcon();
		
		StringBuffer insertsql = new StringBuffer("begin \n");
		StringBuffer deletesql = new StringBuffer("begin \n");
		
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(strchange);
		mdrsl.beforefirst();
		
		if(mdrsl.getRows()==0||getselect(((Visit) this.getPage().getVisit()).getString1()).isEmpty()){
			mdrsl.beforefirst();
			isinsert=true;
			while(mdrsl.next()){
				insertsql.append("update ").append(tableName).append(" set fahb_id=")
				.append(((Visit) this.getPage().getVisit()).getString1())
				.append(" where id=").append(mdrsl.getString("ID")).append(";\n");
			}
		}else{
//���		
			while(mdrsl.next()) {
				for(int i=0;i<getselect(((Visit) this.getPage().getVisit()).getString1()).size();i++){
					if(getselect(((Visit) this.getPage().getVisit()).getString1()).get(i).equals(mdrsl.getString("ID"))){
							break;
					}else{
						
						if(i==(getselect(((Visit) this.getPage().getVisit()).getString1()).size()-1)){
							isinsert=true;
							insertsql.append("update ").append(tableName).append(" set fahb_id=")
							.append(((Visit) this.getPage().getVisit()).getString1())
							.append(" where id=").append(mdrsl.getString("ID")).append(";\n");
						}		
					}
				}
			}
//ɾ��
			mdrsl.beforefirst();
			for(int i=0;i<getselect(((Visit) this.getPage().getVisit()).getString1()).size();i++){
				mdrsl.beforefirst();
				while(mdrsl.next()) {
					if(getselect(((Visit) this.getPage().getVisit()).getString1()).get(i).equals(mdrsl.getString("ID"))){
						break;
					}else{
						
						if(mdrsl.getRow()==(mdrsl.getRows()-1)){
							isdelete=true;
							deletesql.append("update ").append(tableName)
							.append(" set fahb_id =0 where fahb_id=")
							.append(((Visit) this.getPage().getVisit()).getString1())
							.append(" and id=").append(getselect(((Visit) this.getPage().getVisit()).getString1()).get(i)).append(";\n");
						}
					}	
				}
			}
		}
		insertsql.append("end; \n");
		deletesql.append("end; \n");
		if(isinsert){
			con.getInsert(insertsql.toString());
			//System.out.println(insertsql.toString());
			isinsert=false;
		}		
		if(isdelete){
			con.getDelete(deletesql.toString());
			//System.out.println(deletesql.toString());
			isdelete=false;
		}	
		
	}
	
	private ArrayList getselect(String id){
		ArrayList list=new ArrayList();
		JDBCcon con=new JDBCcon();
		PreparedStatement ps=null;
		ResultSet rst=null;
		StringBuffer sql=new StringBuffer();
		sql.append("select id from daozcpb where fahb_id=");
		sql.append(id);

		try {
			ps=con.getPresultSet(sql.toString());
			rst=ps.executeQuery();
			while(rst.next()){
				list.add(rst.getString("id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
        con.Close();
		return list;
	}
	
	public void getSelectData() {
		// Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String sql="select id,fahb_id,diancxxb_id,gongysmc,meikdwmc,pinz,faz,jihkj,fahrq,daohrq,chec,cheph,maoz,piz,biaoz,ches from daozcpb ";
		sql += "where  daohrq>= " + DateUtil.FormatOracleDate(getBeginRiq()) + "\n";
		sql += "and daohrq<" + DateUtil.FormatOracleDate(getEndRiq()) + "+1\n";
		sql += " and (fahb_id=0 or fahb_id=";
		sql += ((Visit) this.getPage().getVisit()).getString1();
		sql += ") order by daohrq desc";
		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
//		����gridΪ�ɱ༭
		egu.setGridType(ExtGridUtil.Gridstyle_Read);
//		����Ϊgrid���ݲ���ҳ
		egu.addPaging(0);
//		����grid���
		egu.setWidth(1000);
		
		egu.setTableName("daozcpb");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").setHeader("ID");
		egu.getColumn("id").setWidth(60);
		egu.getColumn("fahb_id").setHidden(true);
		egu.getColumn("fahb_id").setHeader("fahb_id");
		egu.getColumn("fahb_id").setWidth(60);
		egu.getColumn("diancxxb_id").setHidden(true);
		egu.getColumn("diancxxb_id").setHeader("DIANCXXB_ID");
		egu.getColumn("diancxxb_id").setWidth(60);
		egu.getColumn("gongysmc").setHeader("��Ӧ������");
		egu.getColumn("gongysmc").setWidth(60);
		egu.getColumn("meikdwmc").setHeader("ú������");
		egu.getColumn("meikdwmc").setWidth(60);
		egu.getColumn("pinz").setHeader("Ʒ��");
		egu.getColumn("pinz").setWidth(60);
		egu.getColumn("faz").setHeader("��վ");
		egu.getColumn("faz").setWidth(60);
		egu.getColumn("jihkj").setHeader("�ƻ��ھ�");
		egu.getColumn("jihkj").setWidth(60);
		egu.getColumn("fahrq").setHeader("��������");
		egu.getColumn("fahrq").setWidth(60);
		egu.getColumn("daohrq").setHeader("��������");
		egu.getColumn("daohrq").setWidth(60);
		egu.getColumn("chec").setHeader("����");
		egu.getColumn("chec").setWidth(60);
		egu.getColumn("cheph").setHeader("����");
		egu.getColumn("cheph").setWidth(60);
		egu.getColumn("maoz").setHeader("ë��");
		egu.getColumn("maoz").setWidth(60);
		egu.getColumn("piz").setHeader("Ƥ��");
		egu.getColumn("piz").setWidth(60);
		egu.getColumn("biaoz").setHeader("Ʊ��");
		egu.getColumn("biaoz").setWidth(60);
		egu.getColumn("ches").setHeader("����");
		egu.getColumn("ches").setWidth(60);
		
//		����grid��Toolbar��ʾ���ڲ���
		egu.addTbarText("����ʱ��:");
		DateField dStart = new DateField();
		dStart.Binding("BeginRq","");
		dStart.setValue(getBeginRiq());
		egu.addToolbarItem(dStart.getScript());
		egu.addTbarText(" �� ");
		DateField dEnd = new DateField();
		dEnd.Binding("EndRq","");
		dEnd.setValue(getEndRiq());
		egu.addToolbarItem(dEnd.getScript());
		
//		����grid��ť		
	    egu.addToolbarButton(GridButton.ButtonType_SubmitSel, "SaveButton","");
	    egu.addTbarText("-");
		egu.addToolbarItem("{text:' ˢ��',icon:'imgs/btnicon/refurbish.gif',cls:'x-btn-text-icon',minWidth:75,handler:function(){document.getElementById('ShuaxinButton').click();}}");
		egu.addTbarText("-");
		egu.addToolbarItem("{"
				+ new GridButton("����",
						"function(){ document.getElementById('ReturnButton').click();"
								+ "}").getScript() + "}");
		
		setExtGrid(egu);
		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
		egu.getGridColumns().add(2, new GridColumn(GridColumn.ColType_Check));
		egu.addOtherScript("var i;var nums=[];");
		for(int i=0;i<getselect(((Visit) this.getPage().getVisit()).getString1()).size();i++){
				egu.addOtherScript(
						    "for(i=0;i<gridDiv_data.length;i++){"+
				            	
				            		"if(gridDiv_data[i][0]=='"+getselect(((Visit) this.getPage().getVisit()).getString1()).get(i)+"'){" +
				            			"nums[i]=i"+
				            		"}"+						  
			    "}");				
		}
		egu.addOtherScript("gridDiv_sm.selectRows(nums);");
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

