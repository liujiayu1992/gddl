package com.zhiren.jt.zdt.xitgl.daimgl.chax_meik;

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
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.Field;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Chax_meik extends BasePage implements PageValidateListener {
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
		setTbmsg(null);
	}

	private String tbmsg;

	public String getTbmsg() {
		return tbmsg;
	}

	public void setTbmsg(String tbmsg) {
		this.tbmsg = tbmsg;
	}
	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}


	



	
	private boolean _Refreshclick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_Refreshclick = true;
	}
	public void submit(IRequestCycle cycle) {
		
		if (_Refreshclick) {
			_Refreshclick = false;
			
			getSelectData();
		}
	}

	public void getSelectData() {
		Visit visit2 = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		
		//visit.setString10("212299000000");
		
	
		
		String chaxun = "select  m.id,m.bianm, m.mingc, m.quanc, sf.quanc as shengfb_id,\n"
				+ "         (select max(cz.quanc)\n"
				+ "          from kuangzglb kgl, chezxxb cz\n"
				+ "         where kgl.meikxxb_id = m.id\n"
				+ "           and cz.id = kgl.chezxxb_id\n"
				+ "           and cz.leib = '��վ') as faz\n"
				+ "  from meikxxb m, shengfb sf\n"
				+ " where m.shengfb_id = sf.id(+)\n"
				+ " and m.bianm like '"+visit2.getString10()+"%'\n" 
				+ " order by m.bianm";

		
		


		
		
	 //System.out.println(chaxun);
	ResultSetList rsl = con.getResultSetList(chaxun);
	
    ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
   	egu.setTableName("meikxxb");
   	
	egu.getColumn("id").setHidden(true);
	egu.getColumn("id").setEditor(null);
	
	
	egu.getColumn("bianm").setCenterHeader("����");
	egu.getColumn("mingc").setCenterHeader("���");
	egu.getColumn("quanc").setCenterHeader("ȫ��");

	egu.getColumn("shengfb_id").setCenterHeader("ʡ��");
	
	egu.getColumn("faz").setCenterHeader("��վ");
	

	egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
	egu.getGridColumns().add(1, new GridColumn(GridColumn.ColType_Check));
	
	//�趨�г�ʼ���
	
	egu.getColumn("bianm").setWidth(100);
	egu.getColumn("mingc").setWidth(120);
	egu.getColumn("quanc").setWidth(170);

	egu.getColumn("faz").setWidth(80);
	egu.getColumn("shengfb_id").setWidth(80);

	
	egu.setGridType(ExtGridUtil.Gridstyle_Read);//
	egu.addPaging(50);//���÷�ҳ
	egu.setWidth(1000);//����ҳ��Ŀ��,������������ʱ��ʾ������
	

	
	//*************************������*****************************************88
	//����ʡ�ݵ�������
	ComboBox cb_shengf=new ComboBox();
	egu.getColumn("shengfb_id").setEditor(cb_shengf);
	cb_shengf.setEditable(true);
	String ShengfSql="select id,quanc from shengfb order by mingc";
	egu.getColumn("shengfb_id").setComboEditor(egu.gridId, new IDropDownModel(ShengfSql));
	

	
	//********************������************************************************
		
	
		
//		ˢ�°�ť
		/*StringBuffer rsb = new StringBuffer();
		rsb.append("function (){")
		.append(MainGlobal.getExtMessageBox("'����ˢ������,���Ժ�'",true))
		.append("document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("ˢ��",rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);*/
		
		String strs="var meikbm=gridDiv_sm.getSelected().get('BIANM');"+
		"var myObject = new Object();"+
		"myObject.Meikbianh =meikbm;"+
		"window.returnValue=myObject;"+
		"window.close();";
         egu.addTbarText("-");
         egu.addToolbarItem("{"+new GridButton("����ѡ����ú�����","function(){"+strs+"}").getScript()+"}");
		
         
         egu.addTbarText("-");
         //˵��:ѭ���Ա�ҳ���еı���,ȡ���ı����1��Ϊ�±���.���ҳ����û������,��ȡ���ݹ����Ĳ�������1001��Ϊ�±���
         String newMeikbm="  var hangs=gridDiv_ds.getCount();"+
				         "   var newBianm=0;"+
				         "   for(var i=0;i<hangs;i++){"+
				         "      var bianm=parseInt(gridDiv_ds.getAt(i).get('BIANM'));"+
				         "      if(bianm>newBianm){ "+
				         "          newBianm=bianm;"+
				         "       }"+
				         "   }"+
				         "   var MKbianm=newBianm+1;"+
				         //window.dialogArguments�õ�Daimpfҳ�洫�ݹ�����lianj����
				         " if(gridDiv_ds.getCount()==0){"+
				         "    var cans=window.dialogArguments;"+
				         "    if(cans.length==6){"+
				         "       MKbianm=cans+000001;  "+
				         "    }else{"+
				         "   	 MKbianm=cans+1001;"+
				         "    }"+
				         " }"+
 
				         " Ext.MessageBox.confirm('����','��ȷ��ʹ�������ɱ��:'+MKbianm+' ��Ϊ��ѡ���е�ú�������?',"+
				         "   function(btn) {"+
				         "      if(btn=='yes'){"+
				         "          var myObject1 = new Object();"+
				         "          myObject1.Meikbianh =''+MKbianm;"+
				         "          window.returnValue=myObject1;"+
				         "          window.close();"+
				         "       }else{"+
				         "           return;"+
				         "       }"+
				         "   }"+
				         "  )";
         egu.addToolbarItem("{"+new GridButton("������ú�����","function(){"+newMeikbm+"}").getScript()+"}");
 		
         
         

		setExtGrid(egu);
		con.Close();
	}

	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid3();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid3(extgrid);
	}

	public String getGridScript() {
		if (getExtGrid() == null) {
			return "";
		}
		if (getTbmsg() != null) {
			getExtGrid().addToolbarItem("'->'");
			getExtGrid().addToolbarItem(
					"'<marquee width=200 scrollamount=2>" + getTbmsg()
							+ "</marquee>'");
		}
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
		Visit visit2 = (Visit) getPage().getVisit();
		if (!visit2.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit2.setActivePageName(getPageName().toString());
			visit2.setList1(null);
			setTbmsg(null);
		}
		if (cycle.getRequestContext().getParameter("gonghdwbm") != null) {
			visit2.setString10(String.valueOf(cycle.getRequestContext().getParameter("gonghdwbm")));
			
		}
		getSelectData();
		
		
	}


	//�õ��糧��������ĵ糧���ƻ��߷ֹ�˾,���ŵ�����
	public String getIDropDownDiancmc(String diancmcId) {
		if(diancmcId==null||diancmcId.equals("")){
			diancmcId="1";
		}
		String IDropDownDiancmc = "";
		JDBCcon cn = new JDBCcon();
		
		String sql_diancmc = "select d.mingc from diancxxb d where d.id="+ diancmcId;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				IDropDownDiancmc = rs.getString("mingc");
			}
			rs.close();
		} catch (SQLException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		}finally{
			cn.Close();
		}

		return IDropDownDiancmc;

	}
	
	
	
	
	
	

}
