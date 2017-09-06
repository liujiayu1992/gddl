package com.zhiren.dc.caiygl.choucy;

import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/*
 * ����:���ܱ�
 * ����:2010-5-20 10:09:52
 * ����:�����ά������
 * 
 * 
 */
/*
 * ���ߣ�songy
 * ʱ�䣺2011-03-23 
 * �������޸������˵�������Ҫ�������ƽ�������
 */
public class Choucywh extends BasePage implements PageValidateListener {
	private final static String Customkey = "Choucygl";
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}
	protected void initialize() {
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
//		Visit visit = (Visit) this.getPage().getVisit();
//		visit.getExtGrid1().Save(getChange(), visit);
		
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		
		
		
		StringBuffer sbsqlDel = new StringBuffer("begin\n");
		ResultSetList delrsl = visit.getExtGrid1().getDeleteResultSet(getChange());
		while (delrsl.next()) {
			sbsqlDel.append("delete from choucyb where id = ").append(delrsl.getString("id")).append(";\n");
			String IsHaveQnet_ar="select * from choucyb where id="+delrsl.getString("id")+" and (shenhzt=3 or shenhzt=5)";
			if(con.getHasIt(IsHaveQnet_ar)){//����ֵ�Ѿ��ύ,������ɾ��
				this.setMsg("��������:"+delrsl.getString("huaybh")+" ����ֵ��¼��,������ɾ��!");
				con.Close();
				return;
				
			}
		}
		sbsqlDel.append("end;");
		if(sbsqlDel.length()>15){
			con.getUpdate(sbsqlDel.toString());
		}
		
		delrsl.close();
		
		StringBuffer sbsql = new StringBuffer("begin\n");
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(getChange());
		while(mdrsl.next()) {
			if("0".equals(mdrsl.getString("id"))) {
				sbsql.append("insert into choucyb(id, meikxxb_id,cheh,caiysj, huaybh, caiyy,pizr,  ruc_liuf,ruc_rez,yuc_biz,yuc_rez,beiz2) values(getnewid(")
				.append(visit.getDiancxxb_id()).append("), ")
				.append((getExtGrid().getColumn("meikxxb_id").combo).getBeanId(mdrsl.getString("meikxxb_id")))
				.append(",'").append(mdrsl.getString("cheh")).append("', ")
				.append(" to_date('").append(mdrsl.getString("caiysj")).append("', 'yyyy-mm-dd'), '")
				.append(mdrsl.getString("huaybh")).append("', '")
				.append(mdrsl.getString("caiyy")).append("', '")
				.append(mdrsl.getString("pizr")).append("', ")
				.append(mdrsl.getDouble("ruc_liuf")).append(", ")
				.append(mdrsl.getLong("ruc_rez")).append(", ").append(mdrsl.getDouble("yuc_biz"))
				.append(", ").append(mdrsl.getLong("yuc_rez")).append(", '")
				.append(mdrsl.getString("beiz2")).append("');\n");
			} else {
				sbsql.append("update choucyb set ")
				.append("meikxxb_id = ").append((getExtGrid().getColumn("meikxxb_id").combo).getBeanId(mdrsl.getString("meikxxb_id")))
				//.append(",meikdwmc='").append(mdrsl.getString("meikdwmc"))
				.append(", caiysj = ").append(" to_date('").append(mdrsl.getString("caiysj")).append("', 'yyyy-mm-dd'),huaybh='")
				.append(mdrsl.getString("huaybh")).append("', caiyy = '").append(mdrsl.getString("caiyy"))
				.append("', pizr = '").append(mdrsl.getString("pizr"))
				.append("', ruc_liuf = ").append(mdrsl.getDouble("ruc_liuf"))
				.append(", ruc_rez = ").append(mdrsl.getLong("ruc_rez"))
				.append(", yuc_biz = ").append(mdrsl.getDouble("yuc_biz"))
				.append(", yuc_rez=").append(mdrsl.getLong("yuc_rez"))
				.append(", beiz2 = '").append(mdrsl.getString("beiz2"))
				.append("', cheh = '").append(mdrsl.getString("cheh"))
				.append("' where id = ").append(mdrsl.getString("id")).append(";\n");
			}
		}
		sbsql.append("end;");
		if(sbsql.length()>15){
			con.getUpdate(sbsql.toString());
		}
		
		mdrsl.close();
		con.Close();
		
		
		
		
		
		
		
		
	}
	
	// ���ڿؼ�
	boolean riqichange = false;

	private String riqi;

	public String getRiqi() {
		if (riqi == null || riqi.equals("")) {
			riqi = DateUtil.FormatDate(new Date());
		}
		return riqi;
	}

	public void setRiqi(String riqi) {

		if (this.riqi != null && !this.riqi.equals(riqi)) {
			this.riqi = riqi;
			riqichange = true;
		}
	}
	
	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

  
	
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
		}

		if (_RefurbishChick) {
			_RefurbishChick = false;;
		}
		
		
		getSelectData();
	}



	
	public void getSelectData() {
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
		String sql = 
			
		

		"select c.id,c.caiysj,mk.mingc as meikxxb_id,c.cheh,c.huaybh,c.caiyy,c.pizr,c.stad as chouy_liuf,\n" +
		"round_new(c.qnet_ar*1000/4.1816,0) as chouy_rez,c.ruc_liuf,c.ruc_rez,\n" + 
		"c.yuc_biz,c.yuc_rez,c.beiz2\n" + 
		" from choucyb c ,meikxxb mk\n" + 
		"where c.meikxxb_id=mk.id\n" + 
		"and c.caiysj=to_date('"+this.getRiqi()+"','yyyy-mm-dd')\n" + 
		"order by c.id";



		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl,Customkey);
		egu.setTableName("choucyb");
		egu.setWidth("bodyWidth");

		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").setEditor(null);
		egu.getColumn("caiysj").setHeader("����ʱ��");
		egu.getColumn("caiysj").setDefaultValue(getRiqi());
		egu.getColumn("caiysj").setWidth(100);
		
		egu.getColumn("meikxxb_id").setHeader("ú��");
        // ú��������
		ComboBox meik = new ComboBox();
		egu.getColumn("meikxxb_id").setEditor(meik);
		meik.setEditable(true);
		sql = "select id, mingc from meikxxb order by mingc";
		egu.getColumn("meikxxb_id").setComboEditor(egu.gridId,
				new IDropDownModel(sql));
		egu.getColumn("meikxxb_id").setWidth(120);
		//egu.getColumn("meikxxb_id").editor.setAllowBlank(true);
		
		
		egu.getColumn("cheh").setHeader("����");
		egu.getColumn("cheh").setWidth(80);
		
		egu.getColumn("huaybh").setHeader("�������");
		egu.getColumn("huaybh").editor.setAllowBlank(false);
		egu.getColumn("huaybh").setEditor(null);
		egu.getColumn("huaybh").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		
		egu.getColumn("caiyy").setHeader("����Ա");
		ComboBox caiyy = new ComboBox();
		egu.getColumn("caiyy").setEditor(caiyy);
		meik.setEditable(true);
		sql = "select r.id,r.quanc from renyxxb r where r.bum='��úԱ' order by r.quanc";
		egu.getColumn("caiyy").setComboEditor(egu.gridId,
				new IDropDownModel(sql));
		egu.getColumn("caiyy").setWidth(80);
		
		
		egu.getColumn("pizr").setHeader("��׼��");
		ComboBox pizr = new ComboBox();
		egu.getColumn("pizr").setEditor(pizr);
		meik.setEditable(true);
		sql = "select r.id,r.quanc from renyxxb r where r.bum='ú������Ա' order by r.quanc";
		egu.getColumn("pizr").setComboEditor(egu.gridId,
				new IDropDownModel(sql));
		egu.getColumn("pizr").setWidth(80);
		
		
		egu.getColumn("chouy_liuf").setHeader("�������");
		egu.getColumn("chouy_liuf").setWidth(70);
		egu.getColumn("chouy_liuf").setEditor(null);
		egu.getColumn("chouy_liuf").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		
		egu.getColumn("chouy_rez").setHeader("������ֵ");
		egu.getColumn("chouy_rez").setWidth(70);
		egu.getColumn("chouy_rez").setEditor(null);
		egu.getColumn("chouy_rez").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		
		egu.getColumn("ruc_liuf").setHeader("�볧���");
		egu.getColumn("ruc_liuf").setWidth(80);
		
		egu.getColumn("ruc_rez").setHeader("�볧��ֵ");
		egu.getColumn("ruc_rez").setWidth(80);
		
		egu.getColumn("yuc_biz").setHeader("Ԥ�����");
		egu.getColumn("yuc_biz").setWidth(80);
		
		egu.getColumn("yuc_rez").setHeader("Ԥ����ֵ");
		egu.getColumn("yuc_rez").setWidth(80);
		
		egu.getColumn("beiz2").setHeader("��ע");
		egu.getColumn("beiz2").setWidth(200);
		
		

		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);

        // ����
		egu.addTbarText("����ʱ��:");
		DateField df = new DateField();
		df.setValue(this.getRiqi());
		df.Binding("RIQI", "forms[0]");// ��htmlҳ�е�id��,���Զ�ˢ��
		egu.addToolbarItem(df.getScript());
		egu.addTbarText("-");
		
		// �糧��
		egu.addTbarText("��λ:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc,
				((Visit) getPage().getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");

		GridButton refurbish = new GridButton("ˢ��",
				"function (){document.getElementById('RefurbishButton').click();}");
		refurbish.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(refurbish);
		
		//����������Ƿ���"C201008045"��Ϊ�������,��ĸ"C"+���+�·�+��λ˳���,
		boolean ChoucyBm = MainGlobal.getXitxx_item("����", "����������Ƿ�����ĸC+���+�·�+��λ˳�����Ϊ�������", 
				String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id()),
				"��").equals("��");
		String sPwHandler="";
		 String newBianh="";
		if(ChoucyBm){//C201008045
				 newBianh=GetBianh();
				 sPwHandler = "function(){"
					+"var zhi3;"
					+"if (gridDiv_ds.getCount()!=0) {"
					+"	 var h1=gridDiv_ds.getAt(gridDiv_ds.getCount()-1);"
					+"	 var zhi=h1.get('HUAYBH').substring(1,h1.get('HUAYBH').length);"
					+"   var zhi2=parseInt(zhi)+1;"
					+"   zhi3='C'+zhi2;"
					+"}else{"
					+"   zhi3='"+newBianh+"';      "
					+"        "
					+"             "
					+"}"
					+"  var plant = new gridDiv_plant({ID: '0',CAIYSJ: '"+this.getRiqi()+"',MEIKXXB_ID: '',CHEH:'',HUAYBH: '',CAIYY: '',PIZR:'',CHOUY_LIUF: '',CHOUY_REZ: '',RUC_LIUF: '',RUC_REZ: '',YUC_BIZ: '',YUC_REZ: '',BEIZ2: ''});"
					+"  gridDiv_ds.insert(gridDiv_ds.getCount(),plant);"
					+"	gridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('HUAYBH',zhi3);"
					+"	 "
					
				
					+"}";
		}else{//C08045
			
			  newBianh=GetBianh_new();
			 sPwHandler = "function(){"
				+"var zhi3;"
				+"if (gridDiv_ds.getCount()!=0) {"
				+"	 var h1=gridDiv_ds.getAt(gridDiv_ds.getCount()-1);"
				+"	 var zhi=h1.get('HUAYBH').substring(1,h1.get('HUAYBH').length);"
				+"   if(zhi.substring(0,1)=='0'){"
				+"       var zhi2=parseInt(zhi.substring(1,zhi.length))+1;   "
				+"         zhi3='C0'+zhi2;   "
				+"    }else{       "
				+"       var zhi2=parseInt(zhi)+1;  "
				+"       zhi3='C'+zhi2;"
				+"     }"
				+"}else{"
				+"   zhi3='"+newBianh+"';      "
				+"        "
				+"             "
				+"}"
				+"  var plant = new gridDiv_plant({ID: '0',CAIYSJ: '"+this.getRiqi()+"',MEIKXXB_ID: '',CHEH:'',HUAYBH: '',CAIYY: '',PIZR:'',CHOUY_LIUF: '',CHOUY_REZ: '',RUC_LIUF: '',RUC_REZ: '',YUC_BIZ: '',YUC_REZ: '',BEIZ2: ''});"
				+"  gridDiv_ds.insert(gridDiv_ds.getCount(),plant);"
				+"	gridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('HUAYBH',zhi3);"
				+"	 "
				
			
				+"}";
			
			
			
		}
       
		egu.addTbarBtn(new GridButton("���",sPwHandler));
		
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save,"SaveButton");
		setExtGrid(egu);
		con.Close();
	}
	
	//�õ��������ı��C201008120����ĸC+���+�·�+��λ˳���
	   private String GetBianh() {
	    	JDBCcon con = new JDBCcon();
	    	ResultSetList rsl = null;
	    	String bianh = "";
	    	String newBianh="";
	    	String sql = "select max(huaybh) as bianh\n" +
	    		"  from choucyb k\n" + 
	    		" where k.caiysj> = First_day(to_date('"+this.getRiqi()+"','yyyy-mm-dd'))\n" + 
	    		" and k.caiysj<=last_day(to_date('"+this.getRiqi()+"','yyyy-mm-dd'))";

	    	rsl = con.getResultSetList(sql);
	    	if (rsl.next()) {
	    		if (rsl.getString("bianh") == null || rsl.getString("bianh").equals("")) {
	    			newBianh = 'C'+getRiqi().substring(0, 4) + getRiqi().substring(5, 7) + "001";
	    		} else {
	    			bianh = Long.parseLong(rsl.getString("bianh").substring(1,rsl.getString("bianh").length())) + 1 + "";
	    			newBianh='C'+bianh;
	    		}
	    	}
	    	
	    	rsl.close();
	    	con.Close();
	    	return newBianh;
	    }
	   
	   
		//�õ��������ı�� C080120����ĸC+�·�+3λ˳���
	   private String GetBianh_new() {
	    	JDBCcon con = new JDBCcon();
	    	ResultSetList rsl = null;
	    	String bianh = "";
	    	String newBianh="";
	    	String sql = "select max(huaybh) as bianh\n" +
	    		"  from choucyb k\n" + 
	    		" where k.caiysj> = First_day(to_date('"+this.getRiqi()+"','yyyy-mm-dd'))\n" + 
	    		" and k.caiysj<=last_day(to_date('"+this.getRiqi()+"','yyyy-mm-dd'))";

	    	rsl = con.getResultSetList(sql);
	    	if (rsl.next()) {
	    		if (rsl.getString("bianh") == null || rsl.getString("bianh").equals("")) {
	    			newBianh = 'C'+ getRiqi().substring(5, 7) + "001";
	    		} else {
	    			bianh = Long.parseLong(rsl.getString("bianh").substring(1,rsl.getString("bianh").length())) + 1 + "";
	    			if(bianh.length()==4){//1��9�¼�0
	    				newBianh="C0"+bianh;
	    			}else{
	    				newBianh="C"+bianh;
	    			}
	    			
	    			
	    			
	    		}
	    	}
	    	
	    	rsl.close();
	    	con.Close();
	    	return newBianh;
	    }
	public String getBtnHandlerScript(String btnName) {
		// ��ť��script
		StringBuffer btnsb = new StringBuffer();
		String cnDate = getRiqi();
		cnDate = cnDate.substring(0, 4) + "��" + cnDate.substring(5, 7) + "��" + cnDate.substring(8, 10);
		btnsb.append("function (){Ext.MessageBox.confirm('��ʾ��Ϣ','");
		if (btnName.endsWith("CopyButton")) {
			btnsb.append("���������ݽ�����").append(cnDate).append("���Ѵ����ݣ��Ƿ������");
		} else {
			btnsb.append("�Ƿ�ɾ��").append(cnDate).append("�����ݣ�");
		}
		btnsb.append("',function(btn){if(btn == 'yes'){").append(
				"document.getElementById('").append(btnName).append(
				"').click()").append("}; // end if \n").append("});}");
		return btnsb.toString();
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
			
		}
		getSelectData();
	}

	boolean treechange = false;

	private String treeid = "";

	public String getTreeid() {

		if (treeid == null || treeid.equals("")) {

			treeid = String.valueOf(((Visit) getPage().getVisit())
					.getDiancxxb_id());
		}
		return treeid;
	}

	public void setTreeid(String treeid) {

		if (!this.treeid.equals(treeid)) {

			((Visit) getPage().getVisit()).setboolean3(true);
			this.treeid = treeid;
		}
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
}
