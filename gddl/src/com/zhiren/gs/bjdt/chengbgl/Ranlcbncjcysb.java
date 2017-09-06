package com.zhiren.gs.bjdt.chengbgl;

import java.io.File;
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
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.tools.FtpCreatTxt;
import com.zhiren.common.tools.FtpUpload;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public  class Ranlcbncjcysb extends BasePage implements PageValidateListener {
	
	
	private boolean returnMsg=false;
	private boolean hasSaveMsg=false;
	private String _msg;
	public String getMsg() {
        if (_msg == null) {
            _msg = "";
        }
        return _msg;
    }
	public void setMsg(String _value) {
	    _msg = MainGlobal.getExtMessageBox(_value,false);
	}
    protected void initialize() {
        super.initialize();
        _msg = "";
    }
	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	//����ֻ��Ҫ�������ݣ�����Ҫ�κ�������ֻ��Ҫ����2�仰�Ϳ����ˡ�
	private void Save() {
  		Visit visit = (Visit) this.getPage().getVisit();
 		visit.getExtGrid1().Save(getChange(), visit);
 	}




	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	private boolean _Refreshclick = false;
	public void RefreshButton(IRequestCycle cycle) {
		_Refreshclick = true;
	}
	
	public void submit(IRequestCycle cycle) {//--------------submit()������save������getselectdate����������
		returnMsg=false;
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		
		if (_Refreshclick) {
			_Refreshclick = false;
			
			getSelectData();
		}
	}

	public void getSelectData() {
		
//		if(1==1){
//			this.setMsg("��������û����д,������д��������!");
//			returnMsg=true;
//		}else{
			this.setMsg("");
//		}
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
//		String riqTiaoj=this.getRiqi();
		String chaxun="";
//		if(riqTiaoj==null||riqTiaoj.equals("")){
//			riqTiaoj=DateUtil.FormatDate(new Date());
//		}
		String strdiancTreeID = "";
		int jib=this.getDiancTreeJib();
//		if(jib==1){
//			strdiancTreeID="";
//		}else 
			if (jib==2){ //ѡ��˾ʱֻˢ�³��ù�˾
			strdiancTreeID = "  dc.id= " +this.getTreeid();
		}else if (jib==3){//ѡ�糧ֻˢ�³��õ糧
			strdiancTreeID="  dc.id= " +this.getTreeid();
		} 
		
			//ȡÿ��ĵ�һ��
//			String riq=DateUtil.getYear(this.getRiqi())+"-01-01";"+strdiancTreeID+"
			String riq=getNianfValue().getValue()+"-01-01";
//			System.out.println(riq+"-----------------------------");
//			String riqTiaoj=riq;
		   	chaxun=
		   		
		   		"select   nvl(ys.id,0) as id," +
		   		"         bt.id as diancxxb_id,\n" +
		   		"         to_date('"+riq+"','yyyy-mm-dd') as riq,\n" + 
		   		"         nvl(bt.mingc,0) as leix,\n" + 
		   		"         nvl(ys.duns,0) as duns,\n" + 
		   		"         nvl(ys.danj,0) as danj,\n" + 
		   		"         nvl(ys.jine,0) as jine,\n" + 
		   		"         nvl(ys.biaomdjys,0) as biaomdjys\n" + 
		   		"\n" + 
		   		"from (select dc.id as id, dc.xuh as xuh, dc.mingc as mingc\n" + 
		   		"          from diancxxb dc\n" + 
		   		"         where "+strdiancTreeID+"\n" + 
		   		"           union\n" + 
		   		"           select "+this.getTreeid()+",xuh,fenx from vwchengbfxlx order by xuh) bt,\n" + 
		   		"\n" + 
		   		"     (select  rl.id as id,\n" + 
		   		"              rl.diancxxb_id as diancxxb_id,\n" + 
		   		"              rl.riq as riq,\n" + 
		   		"              rl.leix as leix,\n" + 
		   		"              nvl(rl.duns,0) as duns,\n" + 
		   		"              nvl(rl.danj,0) as danj,\n" + 
		   		"              nvl(rl.jine,0) as jine,\n" + 
		   		"              nvl(rl.biaomdjys,0) as biaomdjys\n" + 
		   		"     from ranlcbncjcysb rl\n" + 
		   		"     where rl.riq=to_date('"+riq+"','yyyy-mm-dd') and rl.diancxxb_id= "+this.getTreeid()+") ys\n" + 
		   		"\n" + 
		   		"\n" + 
		   		"where  bt.mingc=ys.leix(+) order by bt.xuh";

		   	//����������ѯ�õ�����ͼ����2������union����������

		   	//�����õĵ糧���ƶ��ǵ���ȷ���ģ������ѯ�糧�б�
//        System.out.println(chaxun);
//		System.out.println(riq);
		ResultSetList rsl = con.getResultSetList(chaxun);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("ranlcbncjcysb");//�������sql��ѯ���������ݴ洢
		
		egu.getColumn("id").setHeader("�糧id");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").setEditor(null);
		egu.getColumn("diancxxb_id").setHeader("�糧");
		egu.getColumn("diancxxb_id").setHidden(true);
		egu.getColumn("diancxxb_id").setEditor(null);
		egu.getColumn("riq").setHeader("����");
		egu.getColumn("riq").setHidden(true);
		egu.getColumn("riq").setEditor(null);
		egu.getColumn("leix").setHeader("��λ");
//		egu.getColumn("leix").setUpdate(false);
		egu.getColumn("leix").setEditor(null);
		egu.getColumn("duns").setHeader("����������<br>���֣�");
		egu.getColumn("danj").setHeader("�������<br>��Ԫ/�֣�");
		egu.getColumn("jine").setHeader("������<br>����Ԫ��");
		egu.getColumn("biaomdjys").setHeader("���Ԥ��<br>��ú����");

		
		
		//�趨�еĳ�ʼ���
		egu.getColumn("id").setWidth(100);
		egu.getColumn("diancxxb_id").setWidth(100);
		egu.getColumn("riq").setWidth(110);
		egu.getColumn("leix").setWidth(100);
		egu.getColumn("duns").setWidth(100);
		egu.getColumn("danj").setWidth(100);
		egu.getColumn("jine").setWidth(100);
		egu.getColumn("biaomdjys").setWidth(100);
		
		//�趨���ɱ༭�е���ɫ
		egu.getColumn("leix").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
//		egu.getColumn("leijhm").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);// �趨grid���Ա༭
		egu.addPaging(1000);
		egu.setWidth(1000);
		egu.setDefaultsortable(false);//�趨ҳ�治�Զ�����
		// *****************************************����Ĭ��ֵ****************************

		
//		//��ס�����õķ���,������html��page�н��а� ------------------------------------------------------------
		egu.addTbarText("���:");
//		DateField df=new DateField();
		ComboBox nian=new ComboBox();
		nian.setTransform("NianfDropDown");
//		nian.setValue("nian");
		nian.setWidth(60);
//		df.Binding("RIQI", "forms[0]");
		egu.addToolbarItem(nian.getScript());
		
		
		
		// ������

		
		egu.addTbarText("-");
		
		// �糧��
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(),getTreeid());
		setTree(etu);
		egu.addTbarText("-");// ���÷ָ���
		egu.addTbarText("��λ:");
		egu.addTbarTreeBtn("diancTree");

		
		//---------------ҳ��js�ļ��㿪ʼ   ���ɱ༭��------------------------------------------���ɱ༭��
		StringBuffer sb = new StringBuffer();

//		
//		
		sb.append("gridDiv_grid.on('beforeedit',function(e){");
//		sb.append("if(e.record.get('DIANCXXB_ID')=='�ϼ�'){e.cancel=true;}");//���糧�е�ֵ��"�ϼ�"ʱ,��һ�в�����༭
		sb.append(" if(e.field=='LEIX'){ e.cancel=true;}");//�糧�в�����༭
		sb.append("});");
//		
//		
//       //�趨�ϼ��в�����
//		sb.append("function gridDiv_save(record){if(record.get('diancxxb_id')=='�ϼ�') return 'continue';}");
//		 
//		egu.addOtherScript(sb.toString());
		//---------------ҳ��js�������--------------------------
		
		egu.addTbarText("-");
//		ˢ�°�ť
		StringBuffer rsb = new StringBuffer();
		rsb.append("function (){")
		.append(MainGlobal.getExtMessageBox("'����ˢ������,���Ժ�'",true))
		.append("document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("ˢ��",rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_SaveAll, "SaveButton");
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
			this.setTreeid(null);
//			this.setRiqi(null);
		}
		
		getSelectData();
		if (!returnMsg){
			setMsg("");
		}
		returnMsg=false;
	
		
	}

//	 �õ���½�û����ڵ糧���߷ֹ�˾������
	public String getDiancmc() {
		String diancmc = "";
		JDBCcon cn = new JDBCcon();
		long diancid = ((Visit) getPage().getVisit()).getDiancxxb_id();
		String sql_diancmc = "select d.quanc from diancxxb d where d.id="
				+ diancid;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				diancmc = rs.getString("quanc");
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			cn.Close();
		}

		return diancmc;

	}
//	 �õ��Ƿ��������ϵͳ���ò���
	private String getBaohys() {
		String baohys = "";
		JDBCcon cn = new JDBCcon();
		long diancid = ((Visit) getPage().getVisit()).getDiancxxb_id();
		String sql= "select zhi from xitxxb where mingc='�Ƿ��������' and diancxxb_id="
				+ diancid;
		ResultSet rs = cn.getResultSet(sql);
		try {
			while (rs.next()) {
				baohys = rs.getString("zhi");
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			cn.Close();
		}

		return baohys;

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
			e.printStackTrace();
		}finally{
			cn.Close();
		}
		return IDropDownDiancmc;
	}
	
	//�õ��糧��Ĭ�ϵ�վ
	public String getDiancDaoz(){
		String daoz = "";
		String treeid=this.getTreeid();
		if(treeid==null||treeid.equals("")){
			treeid="1";
		}
		JDBCcon con = new JDBCcon();
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("select dc.mingc, cz.mingc  as daoz\n");
			sql.append("  from diancxxb dc, chezxxb cz,diancdzb dd\n");
			sql.append(" where dd.diancxxb_id=dc.id\n");
			sql.append(" and  dd.chezxxb_id=cz.id\n");
			sql.append("   and dc.id = "+treeid+"");

			ResultSet rs = con.getResultSet(sql.toString());
			
			while (rs.next()) {
				 daoz = rs.getString("daoz");
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			con.Close();
		}
		
		return daoz;
	}
	
	boolean treechange=false;
	private String treeid;
//	public String getTreeid() {
//		if(treeid==null||treeid.equals("")){
//			treeid=String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id());
//		}
//		return treeid;
//	}
//	public void setTreeid(String treeid) {
//		this.treeid = treeid;
//		treechange=true;
//	}
	public String getTreeid() {
		String treeid=((Visit) getPage().getVisit()).getString2();
		if(treeid==null||treeid.equals("")){
			((Visit) getPage().getVisit()).setString2(String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString2();
	}
	public void setTreeid(String treeid) {
		((Visit) getPage().getVisit()).setString2(treeid);
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


	 // ���������(�¼ӵ�)--------------------------------------------------------------------------------
    private static IPropertySelectionModel _NianfModel;
    public IPropertySelectionModel getNianfModel() {
        if (_NianfModel == null) {
            getNianfModels();
        }
        return _NianfModel;
    }
    
	private IDropDownBean _NianfValue;
	
    public IDropDownBean getNianfValue() {
        if (_NianfValue == null) {
            int _nianf = DateUtil.getYear(new Date());
            int _yuef = DateUtil.getMonth(new Date());
            if (_yuef == 1) {
//            	 _nianf = _nianf-1 ;//��Ҫ�ϸ��µ� 
                _nianf = _nianf ;
            }
            for (int i = 0; i < getNianfModel().getOptionCount(); i++) {
                Object obj = getNianfModel().getOption(i);
                if (_nianf == ((IDropDownBean) obj).getId()) {
                    _NianfValue = (IDropDownBean) obj;
                    break;
                }
            }
        }
        return _NianfValue;
    }
	
    public void setNianfValue(IDropDownBean Value) {
    	if  (_NianfValue!=Value){
    		_NianfValue = Value;
    	}
    }

    public IPropertySelectionModel getNianfModels() {
        List listNianf = new ArrayList();
        int i;
        for (i = 2003; i <= DateUtil.getYear(new Date())+1; i++) {
            listNianf.add(new IDropDownBean(i, String.valueOf(i)));
        }
        _NianfModel = new IDropDownModel(listNianf);
        return _NianfModel;
    }

    public void setNianfModel(IPropertySelectionModel _value) {
        _NianfModel = _value;
    }
    
    //---------------------------------------------------------------------------------------------------
    
    
	//�õ��糧����ֵ�ļ���,(jib:1,2,3,) 1�Ǽ���,2�Ƿֹ�˾,3�ǵ糧
	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		String DiancTreeJib = this.getTreeid();
		//System.out.println("jib:" + DiancTreeJib);
		if (DiancTreeJib == null || DiancTreeJib.equals("")) {
			DiancTreeJib = "0";
		}
		String sqlJib = "select d.jib from diancxxb d where d.id="+ DiancTreeJib;
		ResultSet rs = con.getResultSet(sqlJib.toString());
		
		try {
			while (rs.next()) {
				jib = rs.getInt("jib");
			}
			rs.close();
		} catch (SQLException e) {

			e.printStackTrace();
		}finally{
			
			con.Close();
		}

		return jib;
	}

	
}
