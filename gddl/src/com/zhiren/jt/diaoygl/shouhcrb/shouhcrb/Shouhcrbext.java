package com.zhiren.jt.diaoygl.shouhcrb.shouhcrb;

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
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.tools.FtpCreatTxt;
import com.zhiren.common.tools.FtpUpload;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public  class Shouhcrbext extends BasePage implements PageValidateListener {
	private String ZhongNbm="000007";
	private String ZhongNyh="000007";
	private String ZhongNmm="000007";
	private String ZhongNip="210.77.187.2";
	
	
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

//	private void Save() {
//		Visit visit = (Visit) this.getPage().getVisit();
//		visit.getExtGrid1().Save(getChange(), visit);
//	}
	private void Save() {
		if(!this.isZuorkc()){
		
			return;
		}
		
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) this.getPage().getVisit();
		StringBuffer sql = new StringBuffer("begin \n");
		ResultSetList rsl = visit.getExtGrid1().getModifyResultSet(getChange());
		
		while (rsl.next()) {
			if(rsl.getString("DIANCXXB_ID").equals("�ϼ�")){

			}else{
				StringBuffer sql2 = new StringBuffer();
				sql2.append("getnewid(").append(visit.getDiancxxb_id()).append(")");
				if ("0".equals(rsl.getString("ID"))) {
					sql.append("insert into ").append("shouhcrbb").append("(id");
					for (int i = 1; i < rsl.getColumnCount(); i++) {
						sql.append(",").append(rsl.getColumnNames()[i]);
						sql2.append(",").append(
								visit.getExtGrid1().getValueSql(
										visit.getExtGrid1().getColumn(
												rsl.getColumnNames()[i]),
										rsl.getString(i)));
					}
					sql.append(") values(").append(sql2).append(");\n");
				} else {
					sql.append("update ").append("shouhcrbb").append(" set ");
					for (int i = 1; i < rsl.getColumnCount(); i++) {
						sql.append(rsl.getColumnNames()[i]).append(" = ");
						sql.append(
								visit.getExtGrid1().getValueSql(
										visit.getExtGrid1().getColumn(
												rsl.getColumnNames()[i]),
										rsl.getString(i))).append(",");
							}
					sql.deleteCharAt(sql.length() - 1);
					sql.append(" where id =").append(rsl.getString("ID")).append(";\n");
					}
				}
			}
		
		sql.append("end;");
		con.getUpdate(sql.toString());
		con.Close();
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}


	private boolean _ShangbChick = false;

	public void ShangbButton(IRequestCycle cycle) {
		_ShangbChick = true;
	}

	
	public void submit(IRequestCycle cycle) {
		returnMsg=false;
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		
		if (_ShangbChick){
			_ShangbChick=false;
			ShangbTXTFile();
			returnMsg=true;
		}
	}

	private void getSettings(){
		JDBCcon cn = new JDBCcon();
		try {
			ResultSet rs = cn.getResultSet("select zhi from xitxxb where mingc='���ܱ���'");
			while (rs.next()) {
				if (rs.getString("zhi")!=null ){
					ZhongNbm=rs.getString("zhi");
				};
			}
			rs.close();
			
			rs=cn.getResultSet("select zhi from xitxxb where mingc='����Ftp�û�'");
			while (rs.next()) {
				if (rs.getString("zhi")!=null ){
					ZhongNyh=rs.getString("zhi");
				};
			}
			rs.close();
			
			rs=cn.getResultSet("select zhi from xitxxb where mingc='����Ftp����'");
			while (rs.next()) {
				if (rs.getString("zhi")!=null ){
					ZhongNmm=rs.getString("zhi");
				};
			}
			rs.close();
			
			rs=cn.getResultSet("select zhi from xitxxb where mingc='����Ftp������ip'");
			while (rs.next()) {
				if (rs.getString("zhi")!=null ){
					ZhongNip=rs.getString("zhi");
				};
			}
			rs.close();
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			cn.closeRs();
			cn.Close();
		}
	}
	
	public void getSelectData() {
		if(!this.isZuorkc()){
			this.setMsg("��������û����д,������д��������!");
			returnMsg=true;
		}else{
			this.setMsg("");
		}
		
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String riqTiaoj=this.getRiqi();
		if(riqTiaoj==null||riqTiaoj.equals("")){
			riqTiaoj=DateUtil.FormatDate(new Date());
		}
		String strdiancTreeID = "";
		int jib=this.getDiancTreeJib();
		if(jib==1){//ѡ����ʱˢ�³����еĵ糧
			strdiancTreeID="";
		}else if (jib==2){//ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
			strdiancTreeID = " and dc.fuid= " +this.getTreeid();
			
		}else if (jib==3){//ѡ�糧ֻˢ�³��õ糧
			strdiancTreeID=" and dc.id= " +this.getTreeid();
		}

		
		String chaxun = "select sum(nvl(dt.id,0)) as id,\n"
				+ "       max(nvl( dt.riq,to_date('"+this.getRiqi()+"','yyyy-mm-dd'))) as riq,\n"
				+ "       decode(d.mingc,null,'�ϼ�',d.mingc) as diancxxb_id,\n"
				+ "		  sum(nvl(dt.fadl,0)) as fadl,"
				+ "       sum(nvl(zt.kuc,0)) as SHANGYKC,\n"
				+ "       sum(nvl(dt.dangrgm,0)) as dangrgm,\n"
				+ "       sum(nvl(lj.leijgm,0)) as leijgm,\n"
				+ "       sum(nvl(dt.haoyqkdr,0)) as haoyqkdr,\n"
				+ "       sum(nvl(lj.leijhm,0)) as leijhm,\n"
				+ "       sum(nvl(dt.tiaozl,0)) as tiaozl,\n"
				+ "		  sum(nvl(dt.kuc,0)) as kuc, \n"
				+ "		  sum(nvl(dt.shangbkc,0)) as shangbkc,\n"
			    + "		  sum(nvl(dt.kuc,0)-nvl(dt.shangbkc,0)) as chal, \n"
			    + "		  sum(nvl(dt.quemtjts,0)) as quemtjts, \n"
			    + "		  sum(nvl(dt.quemtjrl,0)) as quemtjrl \n"
				+ "  from (select *\n"
				+ "          from shouhcrbb h\n"
				+ "         where h.riq = to_date('"+riqTiaoj+"', 'yyyy-mm-dd')) dt,\n"
				+ "       (select h.diancxxb_id, h.kuc\n"
				+ "          from shouhcrbb h, diancxxb dc\n"
				+ "         where h.diancxxb_id = dc.id\n"
				+ "           and riq = to_date('"+riqTiaoj+"', 'yyyy-mm-dd') - 1) zt,\n"
				+ "       (select dc.id, dc.mingc, px.xuh\n"
				+ "          from diancxxb dc, dianckjpxb px\n"
				+ "         where dc.id = px.diancxxb_id(+)\n"
				+ "           "+strdiancTreeID+"\n"
				+ "           and (px.kouj = '����ȼ���ձ�' or px.kouj = '�պĴ��ձ�')) d,\n"
				+ "       (select rb.diancxxb_id,\n"
				+ "               sum(rb.dangrgm) as leijgm,\n"
				+ "               sum(rb.haoyqkdr) as leijhm\n"
				+ "          from shouhcrbb rb\n"
				+ "         where rb.riq >= First_day(to_date('"+riqTiaoj+"', 'yyyy-mm-dd'))\n"
				+ "           and rb.riq <= to_date('"+riqTiaoj+"', 'yyyy-mm-dd')\n"
				+ "         group by rb.diancxxb_id) lj\n"
				+ " where dt.diancxxb_id(+) = d.id\n"
				+ "   and zt.diancxxb_id(+) = d.id\n"
				+ "   and lj.diancxxb_id(+) = d.id\n"
				+ "   group by rollup (d.mingc)\n" + " order by max(d.xuh) ,d.mingc";

		// System.out.println(chaxun);
		ResultSetList rsl = con.getResultSetList(chaxun);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("shouhcrbb");
		
		egu.getColumn("riq").setHeader("����");
		egu.getColumn("diancxxb_id").setHeader("��λ");
		egu.getColumn("fadl").setHeader("������(��kWh)");
		egu.getColumn("dangrgm").setHeader("���չ�ú(t)");
		egu.getColumn("leijgm").setHeader("�ۼƹ�ú(t)");
		egu.getColumn("haoyqkdr").setHeader("���պ���(t)");
		egu.getColumn("leijhm").setHeader("�ۼƺ���(t)");
		egu.getColumn("leijhm").setUpdate(false);
		egu.getColumn("leijhm").setEditor(null);
		egu.getColumn("leijgm").setEditor(null);
		egu.getColumn("kuc").setEditor(null);
		egu.getColumn("SHANGYKC").setEditor(null);
		
		egu.getColumn("leijgm").setUpdate(false);
		egu.getColumn("riq").setHidden(true);
		egu.getColumn("riq").setEditor(null);
		egu.getColumn("SHANGYKC").setHeader("���տ��(t)");
		egu.getColumn("SHANGYKC").setUpdate(false);
		egu.getColumn("kuc").setHeader("���(t)");
		egu.getColumn("tiaozl").setHeader("������(t)");
		egu.getColumn("shangbkc").setHeader("�ϱ����(t)");
		egu.getColumn("shangbkc").setHidden(true);
		egu.getColumn("chal").setHeader("������(t)");
		egu.getColumn("chal").setHidden(true);
		egu.getColumn("shangbkc").setEditor(null);
		egu.getColumn("chal").setEditor(null);
		egu.getColumn("chal").setUpdate(false);
		egu.getColumn("quemtjts").setHeader("ȱúͣ��̨��(̨)");
		egu.getColumn("quemtjrl").setHeader("ȱúͣ������(��ǧ��)");
		
		
		//�趨�еĳ�ʼ���
		egu.getColumn("riq").setWidth(80);
		egu.getColumn("diancxxb_id").setWidth(110);
		egu.getColumn("fadl").setWidth(90);
		egu.getColumn("shangykc").setWidth(70);
		egu.getColumn("dangrgm").setWidth(70);
		egu.getColumn("leijgm").setWidth(80);
		egu.getColumn("haoyqkdr").setWidth(70);
		egu.getColumn("leijhm").setWidth(80);
		egu.getColumn("tiaozl").setWidth(60);
		egu.getColumn("kuc").setWidth(70);
		egu.getColumn("shangbkc").setWidth(70);
		egu.getColumn("chal").setWidth(70);
		egu.getColumn("quemtjts").setWidth(100);
		egu.getColumn("quemtjrl").setWidth(120);
		
		
		//�趨���ɱ༭�е���ɫ
		egu.getColumn("SHANGYKC").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("leijgm").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("leijhm").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("kuc").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("shangbkc").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("chal").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);// �趨grid���Ա༭
		egu.addPaging(100);
		egu.setWidth(1000);
		egu.setDefaultsortable(false);//�趨ҳ�治�Զ�����
		// *****************************************����Ĭ��ֵ****************************
		egu.getColumn("riq").setDefaultValue(this.getRiqi());
		//System.out.println(this.getRiqi());
		
		
		
		//*************************������*****************************************88
		//�糧������
		egu.getColumn("diancxxb_id").setEditor(new ComboBox());
		egu.getColumn("diancxxb_id").setComboEditor(egu.gridId, new IDropDownModel("select id,mingc from diancxxb order by mingc"));
		
		// ������
		egu.addTbarText("����:");
		DateField df = new DateField();
		df.setValue(this.getRiqi());
		df.Binding("RIQI","forms[0]");// ��htmlҳ�е�id��,���Զ�ˢ��
		egu.addToolbarItem(df.getScript());
		
		// �糧��
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(),getTreeid());
		setTree(etu);
		egu.addTbarText("-");// ���÷ָ���
		egu.addTbarText("��λ:");
		egu.addTbarTreeBtn("diancTree");

		
		//---------------ҳ��js�ļ��㿪ʼ------------------------------------------
		StringBuffer sb = new StringBuffer();
		sb.append("gridDiv_grid.on('afteredit',function(e){");
			//�����ۼƹ�ú
			sb.append("if(e.field == 'DANGRGM'){e.record.set('LEIJGM',parseFloat(e.value - e.originalValue) + parseFloat(e.record.get('LEIJGM')));}");
			//�����ۼƺ�ú
			sb.append("if(e.field == 'HAOYQKDR'){e.record.set('LEIJHM',parseFloat(e.value - e.originalValue) + parseFloat(e.record.get('LEIJHM')));}");
			//������ ;���=���չ�ú-���պ���+���տ��+������
			sb.append("e.record.set('KUC',parseFloat(e.record.get('DANGRGM')==''?0:e.record.get('DANGRGM'))-parseFloat(e.record.get('HAOYQKDR')==''?0:e.record.get('HAOYQKDR'))+parseFloat(e.record.get('SHANGYKC')==''?0:e.record.get('SHANGYKC'))+parseFloat(e.record.get('TIAOZL')==''?0:e.record.get('TIAOZL')));");
			
			sb.append("e.record.set('CHAL',parseFloat(e.record.get('KUC')==''?0:e.record.get('KUC'))-parseFloat(e.record.get('SHANGBKC')==''?0:e.record.get('SHANGBKC')));");
			
			//���չ�ú�����仯ʱ,�ϼ��е�(���չ�ú,�ۼƹ�ú,���)��������
			sb.append("rec = gridDiv_ds.getAt(gridDiv_ds.getCount()-1);").append("if(e.field=='DANGRGM'){").append("rec.set('DANGRGM', eval(rec.get('DANGRGM')||0) + eval(e.value||0) - eval(e.originalValue||0));").append("rec.set('LEIJGM', eval(rec.get('LEIJGM')||0) + eval(e.value||0) - eval(e.originalValue||0));").append("rec.set('KUC', eval(rec.get('KUC')||0) + eval(e.value||0) - eval(e.originalValue||0));").append("}");
			
			//���պ��÷����仯ʱ,�ϼ��е�(���պ���,�ۼƺ���,���)��������
			sb.append("if(e.field=='HAOYQKDR'){").append("rec.set('HAOYQKDR', eval(rec.get('HAOYQKDR')||0) + eval(e.value||0) - eval(e.originalValue||0));").append("rec.set('LEIJHM', eval(rec.get('LEIJHM')||0) + eval(e.value||0) - eval(e.originalValue||0));").append("rec.set('KUC', eval(rec.get('KUC')||0) - eval(e.value||0) + eval(e.originalValue||0));").append("}");
			
			//�����������仯ʱ,�ϼ��е�(������,���)�����仯
			sb.append("if(e.field=='TIAOZL'){").append("rec.set('TIAOZL', eval(rec.get('TIAOZL')||0) + eval(e.value||0) - eval(e.originalValue||0));").append("rec.set('KUC', eval(rec.get('KUC')||0) + eval(e.value||0) - eval(e.originalValue||0));").append("}");
			
			sb.append("rec.set('CHAL',rec.get('KUC'||0)-rec.get('SHANGBKC'||0));");
			
		sb.append("});");
		
		
		sb.append("gridDiv_grid.on('beforeedit',function(e){");
		sb.append("if(e.record.get('DIANCXXB_ID')=='�ϼ�'){e.cancel=true;}");//���糧�е�ֵ��"�ϼ�"ʱ,��һ�в�����༭
		sb.append(" if(e.field=='DIANCXXB_ID'){ e.cancel=true;}");//�糧�в�����༭
		sb.append("});");
		
		
       //�趨�ϼ��в�����
		sb.append("function gridDiv_save(record){if(record.get('diancxxb_id')=='�ϼ�') return 'continue';}");
		
		egu.addOtherScript(sb.toString());
		//---------------ҳ��js�������--------------------------
		
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		if (jib==1){
			egu.addTbarText("-");
			//egu.addToolbarButton("�ϱ�����",GridButton.ButtonType_Refresh  , "ShangbButton");
			egu.addToolbarItem("{"+new GridButton("�ϱ�����","function(){document.getElementById('ShangbButton').click();}").getScript()+"}");
		}
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
			this.setRiqi(null);
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
		cn.Close();
		return diancmc;

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
	
	private void getUploadFtp(String tableName){
        if(tableName == ""){
            setMsg("û�����ݿ��ϱ���");
        }else{
            try{
            	getSettings();
                FtpUpload fu = new FtpUpload();
                String ip =ZhongNip;
                String username = ZhongNyh;
                String password = ZhongNmm;
                String filepath = "";
                fu.connectServer(ip, username, password, filepath);
                String filename = "C://Ribsc//" + tableName + ".txt";
                
                if ("�ϴ��ɹ�".equals(fu.upload(filename, tableName + ".txt" + "\n"))){
                	 setMsg("�ϱ��ɹ�!");
                }else{
                	setMsg("�ϱ�ʧ��!");
                }
                fu.closeConnect();
            }catch(Exception e){
                e.printStackTrace();
                setMsg("�ϱ�ʧ��!");
            }
        }
    }

    private void ShangbTXTFile(){
    	//�ϴ��ӿ�Ԥ��,��ʱû��������
        String UploadFilename;
        if(!(new File("c://Ribsc")).isDirectory()){
            (new File("c://Ribsc")).mkdir();
        }
        JDBCcon con = new JDBCcon();
        FtpCreatTxt ct = new FtpCreatTxt();
        UploadFilename = "";
        StringBuffer fileline = new StringBuffer();
        String filedata = "";
        String date = "";
        String diancbm = "";
        double hetjh = 0;
        double dangrgm = 0;
        double leijgm = 0;
        double dangr = 0;
        double leij = 0;
        double dangrhy = 0;
        double leijhy = 0;
        double kuc = 0;
        try{
	        String gsbm =ZhongNbm;
	        String FileName = "HC" + gsbm.substring(0, 1) + gsbm.substring(3) + getRiqi().substring(8, 10);
	        ct.CreatTxt("c://Ribsc/" + FileName + ".txt");
                        
           StringBuffer sbsql = new StringBuffer();
           sbsql.append("select  to_char(to_date('" + getRiqi() + "','yyyy-mm-dd'),'yyyymmdd') as riq,dc.bianm as diancbm,0 as hetjh, \n");
           sbsql.append("      sum(nvl(dr.dangrgm,0)) as dangrgm,sum(nvl(lj.dangrgm,0)) as leijgm, \n");
           sbsql.append("      sum(nvl(dr.dangrgm,0)) as dangr,sum(nvl(lj.dangrgm,0)) as leij, \n");
           sbsql.append("      sum(nvl(dr.haoyqkdr,0)) as dangrhy,sum(nvl(lj.haoyqkdr,0)) as leijhy,sum(dr.kuc) as kuc \n");
           sbsql.append("from (select h.diancxxb_id,h.dangrgm as dangrgm,h.haoyqkdr as haoyqkdr,h.kuc  \n");
           sbsql.append("      from shouhcrbb h \n");
           sbsql.append("      where h.riq =to_date('" + getRiqi() + "','yyyy-mm-dd')) dr, \n");
           sbsql.append("     (select h.diancxxb_id,sum(h.dangrgm) as dangrgm,sum(h.haoyqkdr) as haoyqkdr \n");
           sbsql.append("       from shouhcrbb h \n");
           sbsql.append("       where h.riq >= First_day(to_date('" + getRiqi() + "','yyyy-mm-dd')) \n");
           sbsql.append("         and h.riq <= to_date('" + getRiqi() + "','yyyy-mm-dd') \n");
           sbsql.append("         group by h.diancxxb_id) lj,diancxxb dc,(select * from dianckjpxb kj where kouj='�պĴ��ձ�' and shujsbzt=1) kj \n");
           sbsql.append("  where dc.id=kj.diancxxb_id \n");
           sbsql.append("        and dc.id = lj.diancxxb_id(+) \n");
           sbsql.append("        and dc.id = dr.diancxxb_id(+) \n");
           sbsql.append("        group by (dc.bianm) \n");
           sbsql.append("        order by grouping(dc.bianm) desc \n");
           
            ResultSet rsdata = con.getResultSet(sbsql.toString());
            
            while(rsdata.next()){
                filedata = "";
                fileline.setLength(0);
                date = rsdata.getString("riq");
                diancbm = rsdata.getString("diancbm");
                hetjh = rsdata.getDouble("hetjh");
                dangrgm = rsdata.getDouble("dangrgm");
                leijgm = rsdata.getDouble("leijgm");
                dangr = rsdata.getDouble("dangr");
                leij = rsdata.getDouble("leij");
                dangrhy = rsdata.getDouble("dangrhy");
                leijhy = rsdata.getDouble("leijhy");
                kuc = rsdata.getDouble("kuc");
                fileline.append(getStr(8, date));
                fileline.append(getStr(6, diancbm));
                fileline.append(getNum(8, 0, String.valueOf(Math.round(hetjh))));
                fileline.append(getNum(8, 0, String.valueOf(Math.round(dangrgm))));
                fileline.append(getNum(8, 0, String.valueOf(Math.round(leijgm))));
                fileline.append(getNum(8, 0, String.valueOf(Math.round(dangr))));
                fileline.append(getNum(8, 0, String.valueOf(Math.round(leij))));
                fileline.append(getNum(8, 0, String.valueOf(Math.round(dangrhy))));
                fileline.append(getNum(8, 0, String.valueOf(Math.round(leijhy))));
                fileline.append(getNum(8, 0, String.valueOf(Math.round(kuc))));
                filedata = fileline.toString();
				ct.aLine(filedata);//д��������
            }
            rsdata.close();
            UploadFilename = FileName;
            ct.finish();
            getUploadFtp(UploadFilename);
        }
        catch(Exception e){
            e.printStackTrace();
            setMsg(new Date()+"���ɽӿ��ļ�ʧ��!");
        }finally{
            con.Close();
        }
    }
	
    private String getStr(int weis,String str){
		StringBuffer Str_zf = new StringBuffer();
		if(str==null || str.equals("")){
			for (int i=0;i<weis;i++){
				Str_zf.append(" ");
			}
		}else{
			char[] Str=str.toCharArray();
			int Str_lenght=Str.length;
			
			for (int j=0;j<Str_lenght;j++){
				String Strs=""+Str[j];
				Str_zf.append(Strs);
			}
			int cha=0;
			if (Str_lenght!=weis){
				cha=weis-Str_lenght;
				for (int i=0;i<cha;i++){
					Str_zf.append(" ");
				}
			}
		}
		return Str_zf.toString();
	}
	
	private String getNum(int weis,int xiaos,String Number){//�õ�λ����������
		StringBuffer Str_zf = new StringBuffer();
		String str="";
		str=Number;
		if(str.equals("") ){
			for (int j=0;j<weis-xiaos-2;j++){
				String Strs="";
				Str_zf.append(Strs);
			}
			Str_zf.append(0.);
			for (int j=0;j<xiaos;j++){
				Str_zf.append(0);
			}
		}else{
			int zhengsw=0; 
			if(xiaos!=0){//��С��λ��
				String[] c=str.split("\\.");
				String strs1=c[0];//����λ
				char[] Str1=strs1.toCharArray();//����λ
				String Strs2=c[1];//С��λ
				char[] Str2=Strs2.toCharArray();//С��λ
				//¼������λ
				zhengsw=weis-xiaos-1;
				if (Str1.length!=zhengsw){
					int cha=zhengsw-Str1.length;
					for (int i=0;i<cha;i++){
						Str_zf.append(" ");
					}
				}
				for (int j=0;j<Str1.length;j++){
					String Strs=""+Str1[j];
					Str_zf.append(Strs);
				}
				//¼��С��λ
				Str_zf.append(".");
				if(Str2.length!=xiaos){
					for (int j=0;j<Str2.length;j++){
						String Strs=""+Str2[j];
						Str_zf.append(Strs);
					}
					for (int j=0;j<xiaos-Str2.length;j++){
						Str_zf.append(0);
					}
				}else{
					for (int j=0;j<Str2.length;j++){
						String Strs=""+Str2[j];
						Str_zf.append(Strs);
					}
				}
			}else{//����С��λ
				char[] Str=str.toCharArray();
				int Str_lenght=Str.length;
				int cha=0;
				if (Str_lenght!=weis){//���ո�
					cha=weis-Str_lenght;
					for (int i=0;i<cha;i++){
						Str_zf.append(" ");
					}
				}
				for (int j=0;j<Str_lenght;j++){//¼������
					String Strs=""+Str[j];
					Str_zf.append(Strs);
				}
			}
		}
		return Str_zf.toString();
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

	//���ڿؼ�
	boolean riqichange=false;
	private String riqi;
	public String getRiqi() {
		if(riqi==null||riqi.equals("")){
			riqi=DateUtil.FormatDate(DateUtil.AddDate(new Date(),-1,DateUtil.AddType_intDay));
		}
		return riqi;
	}
	public void setRiqi(String riqi) {
		
		if(this.riqi!=null &&!this.riqi.equals(riqi)){
			this.riqi = riqi;
			riqichange=true;
		}
		 
	}
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
	public boolean isZuorkc(){//�ж����տ���Ƿ�������
		boolean isZuorkc=false;
		int treejib = this.getDiancTreeJib();
		
		if (treejib == 3) {
			JDBCcon con = new JDBCcon();
			String riqTiaoj=DateUtil.FormatDate(DateUtil.AddDate(DateUtil.getDate(getRiqi()), -1, DateUtil.AddType_intDay));
			
			String sqlJib = "select s.kuc from shouhcrbb s where s.riq=to_date('"+riqTiaoj +"','yyyy-mm-dd') and s.diancxxb_id="+this.getTreeid()+"";
			ResultSet rs = con.getResultSet(sqlJib.toString());

			try {
				while (rs.next()) {
					isZuorkc = true;
				}
				rs.close();
			} catch (SQLException e) {

				e.printStackTrace();
			} finally {
				con.Close();
			}

		}else{
			isZuorkc=true;
		}
		
		return isZuorkc;
	}
	
}
