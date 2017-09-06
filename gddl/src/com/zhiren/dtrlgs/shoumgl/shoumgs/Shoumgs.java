package com.zhiren.dtrlgs.shoumgl.shoumgs;

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
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/**
 * 
 * @author ��һ��
 * 
 */
public class Shoumgs extends BasePage implements PageValidateListener {
	// �����û���ʾ
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
		;
	}

	protected void initialize() {
		super.initialize();
		setMsg("");
	}

	// ҳ��ˢ�����ڣ�жú���ڣ�
//	private String riq;
//
//	public String getRiq() {
//		return riq;
//	}
//
//	public void setRiq(String riq) {
//		this.riq = riq;
//	}

	// ҳ��ˢ�����ڣ�жú���ڣ�
	private String riqe;

	public String getRiqe() {
		return riqe;
	}

	public void setRiqe(String riqe) {
		this.riqe = riqe;
	}

	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	private String yangpbh;

	public String getYangpbh() {
		return yangpbh;
	}

	public void setYangpbh(String _yangpbh) {
		yangpbh = _yangpbh;
	}

	private double yangpzl;

	public double getYangpzl() {
		return yangpzl;
	}

	public void setYangpzl(double _yangpzl) {
		yangpzl = _yangpzl;
	}

	private String yangpfs;

	public String getYangpfs() {
		return yangpfs;
	}

	public void setYangpfs(String _yangpfs) {
		yangpfs = _yangpfs;
	}

	private String jieyr;

	public String getJieyr() {
		return jieyr;
	}

	public void setJieyr(String _jieyr) {
		jieyr = _jieyr;
	}

	private String beiz;

	public String getBeiz() {
		return beiz;
	}

	public void setBeiz(String _beiz) {
		beiz = _beiz;
	}

	private String jydw;

	public String getJianydw() {
		return jydw;
	}

	public void setJianydw(String _jydw) {
		jydw = _jydw;
	}

	// ������λ����Դ
	IDropDownBean songjdw;

	public IDropDownBean getSongjdw() {
		if (songjdw == null) {
			if (getSongjdwModel() != null) {
				setSongjdw((IDropDownBean) getSongjdwModel().getOption(0));
			} else {
				songjdw = new IDropDownBean();
			}
		}
		return songjdw;
	}

	public void setSongjdw(IDropDownBean _songjdw) {
		songjdw = _songjdw;
	}

	IPropertySelectionModel songjdwmodel;

	public IPropertySelectionModel getSongjdwModel() {
		if (songjdwmodel == null) {
			setSongjdwModelData();
		}
		return songjdwmodel;
	}

	public void setSongjdwModel(IPropertySelectionModel _songjdwmodel) {
		songjdwmodel = _songjdwmodel;
	}

	public void setSongjdwModelData() {
		String sql = "select t.id,t.mingc from item t, itemsort s\n"
				+ "where t.itemsortid = s.id and s.mingc = '�������鵥λ'";
		setSongjdwModel(new IDropDownModel(sql));
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
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getHtml();
	}

	private boolean _RefurbishClick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishClick = true;
	}



	private boolean _SaveClick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveClick = true;
	}
	
	private boolean _ChongxzgClick = false;

	public void ChongxzgButton(IRequestCycle cycle) {
		_ChongxzgClick = true;
	}
	
	
	public void submit(IRequestCycle cycle) {
		if (_RefurbishClick) {
			_RefurbishClick = false;
			initGrid();
		}
		if (_SaveClick) {
			_SaveClick = false;
			save();
			initGrid();
		}
		if (_ChongxzgClick) {
			_ChongxzgClick = false;
			Chongxzg();
			initGrid();
		}
	}
	
	private void Chongxzg(){
		JDBCcon con = new JDBCcon();
		String strxmrqeOra = DateUtil.FormatOracleDate(getRiqe());
		String sql = "select f.id as fahbid,f.diancxxb_id from fahb f where f.id not in\n"
				+ " ( select gs.fahbid from shoumgslsb gs,fahb f where gs.leix=4 and gs.fahbid=f.id and f.daohrq<"+ strxmrqeOra+ ")\n"
				+ " and f.daohrq<"+strxmrqeOra+" and hetb_id<>0 and f.daohrq>=to_date('2009-08-20','yyyy-mm-dd')";
//				+ "and f.daohrq>=to_date('2009-08-20','yyyy-mm-dd') and f.daohrq<="+strxmrqeOra;//2009��08��26����ǰ����ʷ���ݲ����ݹ�

//			"select f.id as fahbid,f.diancxxb_id \n" + 
//			"from (select max(id) rsid from shoumgslsb where leix <> 4 group by fahbid) rs,\n" + 
//			"     shoumgslsb sh,\n" + 
//			"     fahb f,\n" + 
//			"     gongysb g,\n" + 
//			"     diancxxb d,\n" + 
//			"     luncxxb l\n" + 
//			"where sh.id = rs.rsid\n" + 
//			"  and f.id = sh.fahbid\n" + 
//			"  and f.gongysb_id = g.id\n" + 
//			"  and f.luncxxb_id = l.id(+)\n" + 
//			"  and f.diancxxb_id = d.id(+)"                  
//             +" and f.daohrq<="+strxmrqeOra+"\n ";
		ResultSetList rsl = con.getResultSetList(sql);
		List fhlist=new ArrayList();
		while(rsl.next()){
			fhlist.add(new ShoumcbBean(rsl.getLong("diancxxb_id"),rsl.getLong("fahbid")));
		}
		ShoumzgInfo.CountChengb(fhlist,true);
		setMsg("�����ݹ����");
	}
	
	private void initGrid() {
//		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		// жú���ڵ�ora�ַ�����ʽ
//		String strxmrqOra = DateUtil.FormatOracleDate(getRiq());
		String strxmrqeOra = DateUtil.FormatOracleDate(getRiqe());
		// �糧ID
//		long dcid = visit.getDiancxxb_id();
		
		
		
		String sql =
			" select sh.id,sh.fahbid,g.mingc gongysb_id,d.mingc diancxxb_id,f.chec,sh.riq,\n" +
			"   f.jingz,sh.rez,decode(sh.leix,1,'δ����',2,'����',4,'����','����') as leix,sh.heth,sh.hetjg,sh.hetbz,\n" +
			"    sh.hetzk,sh.meij,sh.meis,sh.yunf,sh.zaf,sh.fazzf,sh.yunfs,sh.zhuangt,sh.heth hethy,d.id as dcid \n" +
			"    , round_new((sh.meij + sh.yunf +sh.zaf) * f.jingz, 2) as buhsmk\n" +
			"    ,round_new(round_new((sh.meij + sh.yunf + sh.zaf) * f.jingz, 2) * 0.17, 2) as shuik,\n" +
			"    round_new(round_new((sh.meij + sh.yunf + sh.zaf) * f.jingz, 2) * 1.17, 2) as  hansmk\n" +
			" from (select max(id) rsid from shoumgslsb group by fahbid) rs,\n" +
			"   shoumgslsb sh,\n" +
			"   fahb f,\n" +
			"   gongysb g,\n" +
			"   diancxxb d\n" +
			" where sh.id = rs.rsid and sh.leix<>4 \n" +
			"  and f.id = sh.fahbid\n" +
			"  and f.gongysb_id = g.id\n" +
			"  and f.diancxxb_id = d.id(+) \n" +
			"  and f.daohrq<="+strxmrqeOra+"\n";
			
			/*"select sh.id,sh.fahbid,g.mingc gongysb_id,d.mingc diancxxb_id,f.chec,\n" +
			"       l.mingc luncxxb_id,sh.rez,decode(sh.leix,1,'δ����',2,'����',4,'����','����') as leix,sh.heth,sh.hetjg,sh.hetbz,\n" + 
			"       sh.hetzk,sh.meij,sh.meis,sh.riq,sh.yunf,sh.zaf,sh.fazzf,sh.yunfs,sh.zhuangt,sh.heth hethy,d.id as dcid \n" + 
			"from (select max(id) rsid from shoumgslsb group by fahbid) rs,\n" + 
			"     shoumgslsb sh,\n" + 
			"     fahb f,\n" + 
			"     gongysb g,\n" + 
			"     diancxxb d,\n" + 
			"     luncxxb l\n" + 
			"where sh.id = rs.rsid and sh.leix<>4 \n" + 
			"  and f.id = sh.fahbid\n" + 
			"  and f.gongysb_id = g.id\n" + 
			"  and f.luncxxb_id = l.id(+)\n" + 
			"  and f.diancxxb_id = d.id(+)"                  
//             +" and f.daohrq>="+strxmrqOra+"\n"
             +" and f.daohrq<="+strxmrqeOra+"\n ";*/
	

		ResultSetList rsl = con.getResultSetList(sql);
		if (rsl == null) {
			WriteLog.writeErrorLog(this.getClass().getName() + "\n"
					+ ErrorMessage.NullResult + "\nSQL:" + sql);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		// �½�grid
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		// ����grid�ɱ༭
		 egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setGridSelModel(ExtGridUtil.GridselModel_Row_single);
		// ���ö�ѡ��
		//egu.addColumn(1, new GridColumn(GridColumn.ColType_Check));
		// ����ҳ����
		egu.setWidth(Locale.Grid_DefaultWidth);
		// egu.setHeight("bodyHeight");
		// �������ݲ���ҳ
		egu.addPaging(0);
		// ����grid�б���
		egu.getColumn("gongysb_id").setHeader("��Ӧ��");
		egu.getColumn("diancxxb_id").setHeader("�ջ���λ");
		egu.getColumn("chec").setHeader("����/����");
		egu.getColumn("jingz").setHeader("����");
		egu.getColumn("rez").setHeader("������ֵ");
		egu.getColumn("leix").setHeader("����");
		egu.getColumn("heth").setHeader("��ͬ��");
		egu.getColumn("hetjg").setHeader("��ͬ�۸�");
		egu.getColumn("hetbz").setHeader("��ͬ��׼");
		egu.getColumn("hetzk").setHeader("��ͬ����");
		egu.getColumn("meij").setHeader("ú��");
		egu.getColumn("meis").setHeader("ú��˰");
		egu.getColumn("riq").setHeader("����");
		egu.getColumn("yunf").setHeader("�˷�");
		egu.getColumn("zaf").setHeader("�ӷ�");
		egu.getColumn("fazzf").setHeader("��վ");
		egu.getColumn("yunfs").setHeader("�˷�˰");
//		egu.getColumn("fmisjksj").setHeader("�ӿڴ���ʱ��");
		egu.getColumn("zhuangt").setHeader("״̬");
		egu.getColumn("hethy").setHeader("��ͬ��");
		egu.getColumn("dcid").setHeader("DCID");
		egu.getColumn("buhsmk").setHeader("����˰ú��");
		egu.getColumn("shuik").setHeader("˰��");
		egu.getColumn("hansmk").setHeader("��˰ú��");
		// ����grid�п��
		egu.getColumn("gongysb_id").setWidth(80);
		egu.getColumn("diancxxb_id").setWidth(80);
		egu.getColumn("chec").setWidth(70);
		egu.getColumn("jingz").setWidth(60);
		egu.getColumn("rez").setWidth(90);
		egu.getColumn("leix").setWidth(80);
		egu.getColumn("heth").setWidth(120);
		egu.getColumn("hetjg").setWidth(60);
		egu.getColumn("hetbz").setWidth(110);
		egu.getColumn("hetzk").setWidth(60);
		egu.getColumn("meij").setWidth(80);
		egu.getColumn("meis").setWidth(50);
		egu.getColumn("riq").setWidth(70);
		egu.getColumn("yunf").setWidth(50);
		egu.getColumn("zaf").setWidth(50);
		egu.getColumn("fazzf").setWidth(50);
		egu.getColumn("yunfs").setWidth(50);
//		egu.getColumn("fmisjksj").setWidth(80);
		egu.getColumn("zhuangt").setWidth(80);
		egu.getColumn("buhsmk").setWidth(70);
		egu.getColumn("shuik").setWidth(70);
		egu.getColumn("hansmk").setWidth(70);
		//����������
		egu.getColumn("fahbid").setHidden(true);
		egu.getColumn("zhuangt").setHidden(true);
		egu.getColumn("hethy").setHidden(true);
		egu.getColumn("dcid").setHidden(true);
		egu.getColumn("yunf").setHidden(true);
		egu.getColumn("zaf").setHidden(true);
		egu.getColumn("fazzf").setHidden(true);
		egu.getColumn("yunfs").setHidden(true);
		
//		egu.getColumn("leix").setHidden(true);
		
		egu.getColumn("gongysb_id").setEditor(null);
		egu.getColumn("diancxxb_id").setEditor(null);
		egu.getColumn("chec").setEditor(null);
		egu.getColumn("jingz").setEditor(null);
		egu.getColumn("rez").setEditor(null);
		egu.getColumn("hetjg").setEditor(null);
		egu.getColumn("hetbz").setEditor(null);
		egu.getColumn("hetzk").setEditor(null);
		egu.getColumn("meis").setEditor(null);
		egu.getColumn("riq").setEditor(null);
		egu.getColumn("yunf").setEditor(null);
		egu.getColumn("zaf").setEditor(null);
		egu.getColumn("fazzf").setEditor(null);
		egu.getColumn("yunfs").setEditor(null);
		egu.getColumn("zhuangt").setEditor(null);
		egu.getColumn("fahbid").setEditor(null);
		egu.getColumn("zhuangt").setEditor(null);
		egu.getColumn("leix").setEditor(null);
		egu.getColumn("hethy").setEditor(null);
		egu.getColumn("dcid").setEditor(null);
		egu.getColumn("zaf").setEditor(null);
		egu.getColumn("fazzf").setEditor(null);
		egu.getColumn("yunfs").setEditor(null);
		egu.getColumn("buhsmk").setEditor(null);
		egu.getColumn("shuik").setEditor(null);
		egu.getColumn("hansmk").setEditor(null);
//		����������
//		ComboBox leix=new ComboBox();
//		egu.getColumn("leix").setEditor(leix);
//		leix.setEditable(true);
//		ArrayList list=new ArrayList();
//		list.add(new IDropDownBean(1,"������ֵ"));
//		list.add(new IDropDownBean(2,"������ֵ"));
//		list.add(new IDropDownBean(3,"�䶯"));
//		list.add(new IDropDownBean(4,"����"));
//		egu.getColumn("leix").setComboEditor(egu.gridId, new IDropDownModel(list));
//		
		//���ñ༭��
		ComboBox heth=new ComboBox();
		egu.getColumn("heth").setEditor(heth);
		heth.setEditable(true);
		String meikxxsql="select id,hetbh from hetb where to_date('"+this.getRiqe()+"','yyyy-mm-dd')>=qisrq and guoqrq>=to_date('"+this.getRiqe()+"','yyyy-mm-dd') and leib=2 ";
		egu.getColumn("heth").setComboEditor(egu.gridId, new IDropDownModel(meikxxsql));

		// ����������Ķ���
		// ��������ѡ��
//		egu.addTbarText("��������");
//		DateField df = new DateField();
//		df.setValue(getRiq());
//		df.Binding("RIQ", "");// ��htmlҳ�е�id��
//		df.setId("jiexrq");
//		egu.addToolbarItem(df.getScript());
		// ��������ѡ��
		egu.addTbarText("�ݹ�����");
		DateField df1 = new DateField();
		df1.setValue(getRiqe());
		df1.Binding("RIQE", "");// ��htmlҳ�е�id��
		df1.setId("jiexrqe");
		egu.addToolbarItem(df1.getScript());
		
//		GridButton refurbish = new GridButton(GridButton.ButtonType_Refresh,
//				"gridDiv", egu.getGridColumns(), "RefurbishButton");
//		egu.addTbarBtn(refurbish);
		egu.addToolbarButton(GridButton.ButtonType_Refresh, "RefurbishButton");
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		
		String scr=
			"function(){ Ext.MessageBox.confirm('����', 'ȷ�����¼����ݹ���', function(btn) { if(btn=='yes'){document.getElementById('ChongxzgButton').click();\n" +
			"Ext.MessageBox.show(\n" + 
			"    {msg:'���ڴ�������,���Ժ�...',\n" + 
			"     progressText:'������...',\n" + 
			"     width:300,\n" + 
			"     wait:true,\n" + 
			"     waitConfig: {interval:200},\n" + 
			"     icon:Ext.MessageBox.INFO\n" + 
			"     }\n" + 
			");}})}";
		egu.addTbarBtn(new GridButton("�����ݹ�",scr,SysConstant.Btn_Icon_SelSubmit));
		
		//��ɫ
		egu.getColumn("gongysb_id").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("diancxxb_id").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("chec").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("jingz").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("rez").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("leix").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("hetjg").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("hetbz").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");	
		egu.getColumn("hetzk").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("meis").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("riq").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("yunf").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");	
		egu.getColumn("zaf").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("fazzf").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("yunfs").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("zhuangt").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");	
		egu.getColumn("fahbid").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");	
		egu.getColumn("zhuangt").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("hethy").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("buhsmk").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");	
		egu.getColumn("shuik").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("hansmk").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		
		StringBuffer script = new StringBuffer();
		script.append( "gridDiv_grid.on('beforeedit', function(e) {\n") //�жϣ���ԭ���ıȽϡ�
				.append("	if(e.field=='MEIJ'){\n")
				.append("		if(e.record.get('HETH')!=e.record.get('HETHY')){\n")
				.append("			e.cancel=true;\n")
				.append("		}")
				.append("	}else{e.cancel=false;}\n")
				.append("});");
				
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addOtherScript(script.toString());
		setExtGrid(egu);
		con.Close();

	}

	private void save() {
		if (getChange() == null || "".equals(getChange())) {
			setMsg("û��δ�ύ�Ķ���");
			return;
		}
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
		con.setAutoCommit(false);
		String sql="";
		int flag;
		List fhlist=new ArrayList();
		 ResultSetList rsl2=this.getExtGrid().getModifyResultSet(getChange());
		 StringBuffer sb = new StringBuffer();
		 sb.append("begin \n");
		 int i = 0;
		while(rsl2.next()){
			
			String id = MainGlobal.getNewID(visit.getDiancxxb_id());
			String fahbid = rsl2.getString("fahbid");
			double rez = rsl2.getDouble("rez");
			int leix = 3;
//			int leix = rsl2.getInt("leix");
			String heth = rsl2.getString("heth");
			double hetjg = rsl2.getDouble("hetjg");
			String hetbz = rsl2.getString("hetbz");
			double hetzk = rsl2.getDouble("hetzk");
			double meij = rsl2.getDouble("meij");
			double meis = rsl2.getDouble("meis");
			String riq =rsl2.getString("riq");
			double yunf = rsl2.getDouble("yunf");
			double zaf = rsl2.getDouble("zaf");
			double fazzf = rsl2.getDouble("fazzf");
			double yunfs = rsl2.getDouble("yunfs");
			String hethy = rsl2.getString("hethy");
			long dcid = rsl2.getLong("dcid");
			if(!heth.equals(hethy)){
				String hethid=getExtGrid().getColumn("heth").combo.getBeanStrId(rsl2.getString("heth"));
				
				String sqlx="update fahb set hetb_id=" + hethid + " where id=" + fahbid;
				sb.append(sqlx +"; \n");
//				con.getUpdate(sqlx);
				fhlist.add(new ShoumcbBean(dcid,Long.parseLong(fahbid)));
				i++;
			}else{
//				String fmisjksj = DateUtil.FormatOracleTime(rsl2.getDateTimeString("fmisjksj"));
				int zhuangt = rsl2.getInt("zhuangt");
				sql ="insert into shoumgslsb(id,fahbid,rez,leix,heth,hetjg,hetbz,hetzk,meij,meis,riq,yunf,zaf,fazzf,yunfs,zhuangt)" +
			 		"values("
			          +id
			         +","
			         +fahbid
			         +","
			         +rez
			         +","
			         +leix
			         +",'"
			         +heth
			         +"',"
			         +hetjg
			         +",'"
			         +hetbz
			         +"',"
			         +hetzk
			         +","
			         +meij
			         +","
			         +meis
			         +","
			         +"to_date('"+riq+"','yyyy-mm-dd')"
			         +","
			         +yunf
			         +","
			         +zaf
			         +","
			         +fazzf
			         +","
			         +yunfs
//			         +","
//			         +fmisjksj
			         +","
			         +zhuangt
			 		 +");\n";
				
				i++;
				sb.append(sql);
				
			}

		}
		sb.append("end; ");
		if(i>0){
			flag = con.getUpdate(sb.toString());
			if (flag == -1) {
				WriteLog.writeErrorLog(this.getClass().getName() + "\n"
						+ ErrorMessage.InsertDatabaseFail + "\nSQL:" + sql);
				setMsg(ErrorMessage.InsertDatabaseFail);
				con.rollBack();
				return;
			}else{
				con.commit();
				ShoumzgInfo.CountChengb(fhlist,true);
			}
		}
		con.Close();
		setMsg(ErrorMessage.SaveSuccessMessage);
	}
	public static void addFahid(List list,String fahid) {
		if(list == null) {
			list = new ArrayList();
		}
		int i=0;
		for( ;i<list.size() ;i++) {
			if(((String)list.get(i)).equals(fahid)) {
				break;
			}
		}
		if(i == list.size()) {
			list.add(fahid);
		}
	}


	private void init() {
		setExtGrid(null);
		initGrid();
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			if (visit.getActivePageName().toString().equals("Zhuangccyxg")) {
//				setRiq(visit.getString7());
				setRiqe(visit.getString8());
			}
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
//			if (getRiq() == null) {
//				setRiq(DateUtil.FormatDate(new Date()));
//			}
			if (getRiqe() == null) {
				setRiqe(DateUtil.FormatDate(new Date()));
			}
			visit.setActivePageName(getPageName().toString());
			init();
		}
		visit.setString9("");
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