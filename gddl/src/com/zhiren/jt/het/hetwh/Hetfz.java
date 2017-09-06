package com.zhiren.jt.het.hetwh;

import java.util.Date;

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
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/**
 * 
 * @author yang 
 * ���ڣ�2010-3-9 
 * ��������ͬ����
 */

/**
 * @author yang �޸�
 * ���ڣ�2010-3-25 
 * �����������ͬ���Ƶ�����ͬ��
 * 		
 */
/**
 * ���ߣ����
 * ���ڣ�2014-02-27
 * ����������ú���ͬ�����Һ�ͬ����Ϊ����ʱ����̨���ݱ���BUG��
 * 		����Ϊ�ȸ��ƺ�ͬ�������е����ݣ�Ȼ����ݺ�ͬ���������������������¶�Ӧ��ID��
 */
public class Hetfz extends BasePage implements PageValidateListener {

	// �����û���ʾ
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, true);
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

	// ǩ������
	private String kriq;

	public void setKRiq(String kriq) {
		this.kriq = kriq;
	}

	public String getKRiq() {
		return this.kriq;
	}

	// ʧЧ����
	private String jriq;

	public void setJRiq(String jriq) {
		this.jriq = jriq;
	}

	public String getJRiq() {
		return this.jriq;
	}

	private boolean _FuzClick = false;

	public void FuzButton(IRequestCycle cycle) {
		_FuzClick = true;
	}
	
	// ����
	public void fuz(IRequestCycle cycle) {
		if(!this.getChange().equals("")){
			Visit visit = (Visit) getPage().getVisit();	
			JDBCcon con=new JDBCcon();
			StringBuffer sql=new StringBuffer();
			
			//Ҫ���Ƶĺ�ͬID����
			String[] hetID=this.getChange().split(",");						
			
			//���ƺ��ID���飬�ڷ��غ�ͬҳ��ʹ�ó�ʼ����ͬ������
			String newID="";
			
			sql.append("begin\n");	
			
			// ��¼��ͬ����
			String leix = visit.getString4();
			
			long fuid=0;
			if(page.equals("Hetbcxy")){
				fuid=visit.getLong5();
				leix = visit.getString17();
			}
			
			//�ж��Ƿ�Ϊ�����ͬ
			if(page.equals("Yunsht")){
				for(int i=0;i<hetID.length;i++){
					//��ͬ��ID
					String hetxxb_id=hetID[i];
					
					//���ƺ�ĺ�ͬID
					String newid=MainGlobal.getNewID(visit.getDiancxxb_id());
					newID+=newid+",";
					
					sql.append("  insert into hetys( HETBH,QIANDRQ,QIANDDD,YUNSDWB_ID,GONGFDWMC,GONGFDWDZ,GONGFDH,GONGFFDDBR, \n");
					sql.append("  GONGFWTDLR,GONGFDBGH,GONGFKHYH,GONGFZH,GONGFYZBM,GONGFSH,XUFDWMC,XUFDWDZ,XUFFDDBR,XUFWTDLR, \n");
					sql.append("  XUFDH,XUFDBGH,XUFKHYH,XUFZH,XUFYZBM,XUFSH,QISRQ,GUOQRQ,HETYS_MB_ID,MEIKMCS,LIUCZTB_ID, \n");
					sql.append("  LIUCGZID,ID,MINGC,FUID,DIANCXXB_ID,YUNSJGFAB_ID) \n");
					sql.append("  (select HETBH||'����',QIANDRQ,QIANDDD,YUNSDWB_ID,GONGFDWMC,GONGFDWDZ,GONGFDH,GONGFFDDBR, \n");
					sql.append("  GONGFWTDLR,GONGFDBGH,GONGFKHYH,GONGFZH,GONGFYZBM,GONGFSH,XUFDWMC,XUFDWDZ,XUFFDDBR,XUFWTDLR, \n");
					sql.append("  XUFDH,XUFDBGH,XUFKHYH,XUFZH,XUFYZBM,XUFSH,QISRQ,GUOQRQ,HETYS_MB_ID,MEIKMCS,0, \n");
					sql.append("  0,"+newid+",MINGC,0,DIANCXXB_ID,YUNSJGFAB_ID \n");
					sql.append("  from hetys where hetys.id="+hetxxb_id+");  \n");
					sql.append("  insert into hetysjgb(id,hetys_id,meikxxb_id,zhibb_id,tiaojb_id,shangx,xiax,danwb_id,yunja,yunjdw_id)  \n");
					sql.append("  (select getnewid("+visit.getDiancxxb_id()+"),"+newid+",meikxxb_id,zhibb_id,tiaojb_id,shangx,xiax,danwb_id,yunja,yunjdw_id from hetysjgb  \n");
					sql.append("  where hetys_id="+hetxxb_id+");  \n");
					sql.append("  insert into hetyszkkb(jis,jisdwid,kouj,koujdw,zengfj,zengfjdw,xiaoscl,jizzkj,jizzb,canzxm, \n");
					sql.append("  canzxmdw,canzsx,canzxx,hetjsxsb_id,yunsfsb_id,beiz,hetys_id,id,zhibb_id,tiaojb_id,shangx,xiax,danwb_id) \n");
					sql.append("  (select jis,jisdwid,kouj,koujdw,zengfj,zengfjdw,xiaoscl,jizzkj,jizzb,canzxm, \n");
					sql.append("  canzxmdw,canzsx,canzxx,hetjsxsb_id,yunsfsb_id,beiz,"+newid+",getnewid("+visit.getDiancxxb_id()+"),zhibb_id,tiaojb_id,shangx,xiax,danwb_id \n");
					sql.append("  from hetyszkkb where hetyszkkb.hetys_id="+hetxxb_id+");  \n");
					sql.append("  insert into hetyswzb(id,wenznr,hetys_id) \n");
					sql.append("  (select getnewid("+visit.getDiancxxb_id()+"),wenznr,"+newid+" from hetyswzb where hetyswzb.hetys_id="+hetxxb_id+");  \n");
					sql.append("  insert into hetysshrb (id, hetysb_id, shouhr_id)  \n");
					sql.append("  (select getnewid("+visit.getDiancxxb_id()+"),"+newid+", shouhr_id from hetysshrb where hetysshrb.hetysb_id="+hetxxb_id+");   \n");
				}
			}else{
				int leib = 0;
				if ("XS".equals(leix)) {
					leib = 1;
				}
				
				if ("CG".equals(leix)) {
					leib = 2;
				}
				
				for(int i=0;i<hetID.length;i++){
					//��ͬ��ID
					String hetxxb_id=hetID[i];
					//���ƺ�ĺ�ͬID
					String newid=MainGlobal.getNewID(visit.getDiancxxb_id());
					newID+=newid+",";
					
					sql.append("  insert into hetb( ID,FUID,DIANCXXB_ID,HETBH,QIANDRQ,QIANDDD,GONGFDWMC,GONGFDWDZ,GONGFDH, \n");
					sql.append("  GONGFFDDBR,GONGFWTDLR,GONGFDBGH,GONGFKHYH,GONGFZH,GONGFYZBM,GONGFSH,XUFDWMC,XUFDWDZ, \n");
					sql.append("  XUFFDDBR,XUFWTDLR,XUFDH,XUFDBGH,XUFKHYH,XUFZH,XUFYZBM,XUFSH,HETGYSBID,GONGYSB_ID, \n");
					sql.append("  QISRQ,GUOQRQ,HETB_MB_ID,JIHKJB_ID,LIUCZTB_ID,LIUCGZID,LEIB,HETJJFSB_ID,MEIKMCS,XIAF) \n");
					sql.append("  (select "+newid+","+fuid+",DIANCXXB_ID,HETBH||'����',QIANDRQ,QIANDDD,GONGFDWMC,GONGFDWDZ,GONGFDH, \n");
					sql.append("  GONGFFDDBR,GONGFWTDLR,GONGFDBGH,GONGFKHYH,GONGFZH,GONGFYZBM,GONGFSH,XUFDWMC,XUFDWDZ, \n");
					sql.append("  XUFFDDBR,XUFWTDLR,XUFDH,XUFDBGH,XUFKHYH,XUFZH,XUFYZBM,XUFSH,HETGYSBID,GONGYSB_ID, \n");
					sql.append("  QISRQ,GUOQRQ,HETB_MB_ID,JIHKJB_ID,0,0," + leib + ",HETJJFSB_ID,MEIKMCS,0 \n");
					sql.append("  from hetb where hetb.id="+hetxxb_id+"); \n");
					sql.append("  insert into hetslb(ID,PINZB_ID,YUNSFSB_ID,FAZ_ID,DAOZ_ID,DIANCXXB_ID,RIQ,HETL,HETB_ID,ZHUANGT) \n");
					sql.append("  (select id,PINZB_ID,YUNSFSB_ID,FAZ_ID,DAOZ_ID,DIANCXXB_ID,RIQ,HETL,"+newid+",ZHUANGT \n");
					sql.append("  from hetslb where hetb_id="+hetxxb_id+"); \n");
					sql.append("  insert into hetzlb(ID,ZHIBB_ID,TIAOJB_ID,SHANGX,XIAX,DANWB_ID,HETB_ID) \n");
					sql.append("  (select getnewid("+visit.getDiancxxb_id()+"),ZHIBB_ID,TIAOJB_ID,SHANGX,XIAX,DANWB_ID,"+newid+" \n");
					sql.append("  from hetzlb where hetb_id="+hetxxb_id+"); \n");
					sql.append("  insert into hetzkkb(JIS,JISDWID,KOUJ,KOUJGS,KOUJDW,ZENGFJ,ZENGFJGS,ZENGFJDW,XIAOSCL,JIZZKJ,JIZZB,CANZXM,CANZXMDW,CANZSX,CANZXX,HETJSXSB_ID \n");
					sql.append("  ,YUNSFSB_ID,BEIZ,HETB_ID,ID,ZHIBB_ID,TIAOJB_ID,SHANGX,XIAX,DANWB_ID,SHIYFW,PINZB_ID,JIJLX) \n");
					sql.append("  (select JIS,JISDWID,KOUJ,KOUJGS,KOUJDW,ZENGFJ,ZENGFJGS,ZENGFJDW,XIAOSCL,JIZZKJ,JIZZB,CANZXM,CANZXMDW,CANZSX,CANZXX,HETJSXSB_ID \n");
					sql.append(" 	,YUNSFSB_ID,BEIZ,"+newid+",getnewid("+visit.getDiancxxb_id()+"),ZHIBB_ID,TIAOJB_ID,SHANGX,XIAX,DANWB_ID,SHIYFW,PINZB_ID,JIJLX \n");
					sql.append("  from hetzkkb where hetb_id="+hetxxb_id+"); \n");
					sql.append("  insert into hetjgb(ID,ZHIBB_ID,TIAOJB_ID,SHANGX,XIAX,DANWB_ID,JIJ,JIJDWID,JIJGS,HETJSFSB_ID,HETJSXSB_ID,YUNJ,YUNJDW_ID \n");
					sql.append("  ,YINGDKF,YUNSFSB_ID,ZUIGMJ,HETB_ID,HETJJFSB_ID,FENGSJJ,JIJLX,PINZB_ID) \n");
					sql.append("  (select getnewid("+visit.getDiancxxb_id()+"),ZHIBB_ID,TIAOJB_ID,SHANGX,XIAX,DANWB_ID,JIJ,JIJDWID,JIJGS,HETJSFSB_ID,HETJSXSB_ID,YUNJ,YUNJDW_ID \n");
					sql.append("  ,YINGDKF,YUNSFSB_ID,ZUIGMJ,"+newid+",HETJJFSB_ID,FENGSJJ,JIJLX,PINZB_ID \n");
					sql.append("  from hetjgb where hetb_id="+hetxxb_id+"); \n");
					sql.append("  insert into hetwzb(ID,WENZNR,HETB_ID) \n");
					sql.append("  (select getnewid("+visit.getDiancxxb_id()+"),WENZNR,"+newid+" \n");
					sql.append("  from hetwzb where hetb_id="+hetxxb_id+"); \n");
//					���������Ϣ
					sql.append("  insert into hetblb(ID,HETB_ID,BIANLMC,VALUE) \n");
					sql.append("  (select getnewid("+visit.getDiancxxb_id()+"),"+newid+",BIANLMC,VALUE \n");
					sql.append("  from hetblb where hetb_id="+hetxxb_id+"); \n");
					
//					���ں�ͬ�п��ܴ��ڶ���������Ϣ����������ֱ������ID��Ϣ
					ResultSetList rsl = con.getResultSetList("select distinct ID as id from hetslb where hetb_id = " + hetxxb_id);
					long id = 0;
					while (rsl.next()) {
						String hetslid=MainGlobal.getNewID(visit.getDiancxxb_id());
						id = rsl.getLong("id");
						sql.append("update(SELECT * FROM hetslb WHERE hetb_id="+newid+" and id="+id+") set id="+hetslid+";\n");
					}
					
					if (MainGlobal.getXitxx_item("��ͬ", "�Ƿ���ú���ͬ��¼���ӷ���Ϣ", String.valueOf(visit.getDiancxxb_id()), "��").equals("��")) {
//						�����ӷ���Ϣ
						sql.append("  insert into hetzfb(ID,hetb_id,item_id,diancjszf) \n");
						sql.append("  (select getnewid("+visit.getDiancxxb_id()+"),"+newid+",item_id,diancjszf \n");
						sql.append("  from hetzfb where hetb_id="+hetxxb_id+"); \n");
					}					
				}								
			}
			sql.append("end;");
			
			con.getInsert(sql.toString());
			con.Close();
			
			//�����ƺ�ĺ�ͬID���غ�ͬҳ��
			if(page.equals("Diancgmht")){
				visit.setString12(newID);
			}else if(page.equals("Yunsht")){
				visit.setString13(newID);
			}else if(page.equals("Hetbcxy")){
				visit.setString14(newID);
			}
			
			//��¼���������ڷ���ԭҳ���ж��Ƿ�Ϊ���Ʋ���
			//visit.setbo(_FuzClick);
						
			_FuzClick = false;
						
			//���ƺ󷵻�ԭҳ��
			cycle.activate(page);
		}else{
			this.setMsg("'��ѡ���ͬ��'");
			return;
		}
		
	}

	// ����
	private boolean _ReturnClick = false;

	public void ReturnButton(IRequestCycle cycle) {
		_ReturnClick = true;
	}

	public void Return(IRequestCycle cycle) {
		Visit v = (Visit) this.getPage().getVisit();
		//����ԭҳ��
		v.setString12("fanhui");
		cycle.activate(page);
	}

	public void submit(IRequestCycle cycle) {
		if (_ReturnClick) {
			_ReturnClick = false;
			Return(cycle);
		}
		if (_FuzClick) {			
			fuz(cycle);			
		}
		getSelectData();
	}

	public void getSelectData() {
//		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		
		// ��ʼ����ѯ���
		String sql="";	
		
		//�鿴�ı�ҳ��ű�
		String urlStr="";

		//�ж��Ƿ�Ϊ�����ͬ
		if(page.equals("Yunsht")){
			urlStr= "var url = 'http://'+document.location.host+document.location.pathname;\n"+
		            "var end = url.indexOf(';');\n"+
					"url = url.substring(0,end);\n"+
		       	    "url = url + '?service=page/' + 'Yunshtshrz&hetys_id='+record.data['ID'];\n";
			
			sql = "select h.id,h.hetbh,h.gongfdwmc,h.xufdwmc,h.qiandrq\n" 
				 +"from hetys h,yunsdwb y\n"  
				 +"where h.yunsdwb_id=y.id\n" 
				 +"		 and h.diancxxb_id="+this.getDiancid()+"\n" 
				 +"      and h.qiandrq >= "+DateUtil.FormatOracleDate(this.getKRiq())+"\n"  
				 +"      and h.qiandrq <= "+DateUtil.FormatOracleDate(this.getJRiq())+"\n"  
				 +"\n";
			if(this.getGongfValue().getId()!=-1){
				sql+="		 and h.yunsdwb_id="
					 +this.getGongfValue().getId()+"\n";
			}
			if(this.getXufValue().getId()!=-1){
				sql+="		 and h.diancxxb_id="
				 +this.getXufValue().getId()
				 +"\n";
			}
			sql+="group by h.id,h.hetbh,h.gongfdwmc,h.xufdwmc,h.qiandrq\n";
			sql+="order by h.hetbh";
			
		}else{
			urlStr="var url = 'http://'+document.location.host+document.location.pathname;\n"+
		            "var end = url.indexOf(';');\n"+
					"url = url.substring(0,end);\n"+
		       	    "url = url + '?service=page/' + 'Shenhrz&hetb_id='+record.data['ID'];\n";
			
			sql = "select h.id,h.hetbh,h.gongfdwmc,h.xufdwmc,h.qiandrq,"
				+ "decode(h.leib,0,'�糧�ɹ�',1,'��������',2,'����ɹ�') leib,"
				+ "nvl((select sum(s.hetl) from hetslb s where s.hetb_id=h.id),0) hetl\n"
				+ "from hetb h\n"
				+ "where h.qiandrq >= "
				+ DateUtil.FormatOracleDate(this.getKRiq()) 
				+ "\n" 
				+ "     and h.qiandrq <= "
				+ DateUtil.FormatOracleDate(this.getJRiq()) 
				+ "\n";
			if(this.getGongfValue().getId()!=-1){
				sql+="		and h.gongysb_id="+this.getGongfValue().getId()+"\n";
			}
			if(this.getXufValue().getId()!=-1){
				sql+="		and h.diancxxb_id="+this.getXufValue().getId()+ "\n";
			}
			sql+="group by h.id,h.hetbh,h.gongfdwmc,h.xufdwmc,h.qiandrq,leib\n";
			sql+="order by h.hetbh";
		}		
//		System.out.println(sql);
		ResultSetList rsl = con.getResultSetList(sql);
		
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("hetb");
		egu.setWidth("bodyWidth");
		egu.setHeight("bodyHeight");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("hetbh").setHeader("��ͬ���");
		egu.getColumn("hetbh").setWidth(200);
		egu.getColumn("hetbh").setRenderer("function(value,p,record){\n"+urlStr+
				"return \"<a href=# onclick=window.open('\"+url+\"','newWin')>\"+value+\"</a>\"}"
		);
		
		
		egu.getColumn("gongfdwmc").setHeader("������λ");
		egu.getColumn("gongfdwmc").setWidth(250);
		egu.getColumn("xufdwmc").setHeader("�跽��λ");
		egu.getColumn("xufdwmc").setWidth(250);
		egu.getColumn("qiandrq").setHeader("ǩ������");
		
		//�������ͬ
		if(!page.equals("Yunsht")){
			egu.getColumn("leib").setHeader("���");
			egu.getColumn("hetl").setHeader("��ͬ��");
		}		
				
		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
		egu.getGridColumns().add(2, new GridColumn(GridColumn.ColType_Check));
		egu.addTbarText("-");
		//���ڿؼ�
		egu.addTbarText("ǩ������");
		DateField kais = new DateField();
		kais.Binding("KRIQ", "");
		kais.setValue(this.getKRiq());
		kais
				.setListeners("change:function(own,newValue,oldValue)"
						+ "{document.getElementById('KRIQ').value = newValue.dateFormat('Y-m-d');}");
		egu.addToolbarItem(kais.getScript());

		DateField jies = new DateField();
		jies.Binding("JRIQ", "");
		jies.setValue(this.getJRiq());
		jies
				.setListeners("change:function(own,newValue,oldValue)"
						+ "{document.getElementById('JRIQ').value = newValue.dateFormat('Y-m-d');}");
		egu.addToolbarItem(jies.getScript());

		egu.addTbarText("����");
		ComboBox comb1 = new ComboBox();
		comb1.setWidth(150);
		comb1.setTransform("GongfDropDown");
		comb1.setId("GongfDropDown");// ���Զ�ˢ�°�
		comb1.setLazyRender(true);// ��̬��
		comb1.setEditable(true);
		egu.addToolbarItem(comb1.getScript());
		egu
				.addOtherScript("GongfDropDown.on('select', function(o,record,index) {document.forms[0].submit();});");

		egu.addTbarText("�跽");
		ComboBox comb2 = new ComboBox();
		comb2.setWidth(150);
		comb2.setTransform("XufDropDown");
		comb2.setId("XufDropDown");// ���Զ�ˢ�°�
		comb2.setLazyRender(true);// ��̬��
		comb2.setEditable(true);
		egu.addToolbarItem(comb2.getScript());
		egu
				.addOtherScript("XufDropDown.on('select', function(o,record,index) {document.forms[0].submit();});");

		egu.addTbarText("-");// ���÷ָ���

		// ���ƺ�ͬ
		GridButton gbf = new GridButton("���ƺ�ͬ", ""
				+ "function(){\n"
				+ "		var rc = gridDiv_grid.getSelectionModel().getSelections();\n"		
				+ "		var value='';\n"							
				+ "		if(rc.length>0){\n" 	  
				+ "			for(var i=0;i<rc.length;i++){\n"		
				+ "				value+=rc[i].get('ID')+',';\n"		
				+ "			}\n"
				+ "			document.getElementById('CHANGE').value=value;\n"				
				+ "			document.getElementById('FuzButton').click();\n"		
				+ "		}else{\n"		
				+ "			Ext.MessageBox.alert('��ʾ��Ϣ','��ѡ��Ҫ�����ļ�¼!');\n"	
				+ "		}\n"
				+ "}\n");
		egu.addTbarBtn(gbf);
		egu.addTbarText("-");
		
		//ˢ��
		GridButton gbs = new GridButton(
				"ˢ��", "function(){document.forms[0].submit();}",
				SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbs);

		// ���ذ�ť
		GridButton gbr = new GridButton(
				"����","function(){document.getElementById('ReturnButton').click();}",
				SysConstant.Btn_Icon_Return);
		egu.addTbarBtn(gbr);

		setExtGrid(egu);
		rsl.close();
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

	private String page="";
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();

		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			//��¼��תҳ�棬�Ը��ƻ򷵻ز�������ԭҳ��
			//visit.setString17(visit.getActivePageName());
		
			page=visit.getActivePageName();
			
			//visit.setObject1(visit.getActivePageName());
			
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			visit.setString12(null);
			//visit.setboolean1(false);
			
			this.setKRiq(DateUtil.getYear(new Date()) + "-01-01");
			this.setJRiq(DateUtil.FormatDate(new Date()));
			this.getGongfModels();
			this.getXufModels();
			getSelectData();
		}
	}

	// ����������
	private IPropertySelectionModel _GongfModel;

	public IPropertySelectionModel getGongfModel() {		
		return _GongfModel;
	}

	private IDropDownBean _GongfValue;

	public IDropDownBean getGongfValue() {
		if(_GongfValue==null){
			_GongfValue=(IDropDownBean)this.getGongfModel().getOption(0);
		}
		return _GongfValue;
	}

	public void setGongfValue(IDropDownBean Value) {
		_GongfValue = Value;
	}

	public IPropertySelectionModel getGongfModels() {		
		if(!page.equals("Yunsht")){
			_GongfModel = new IDropDownModel(
					"select id,piny||' '||mingc from gongysb order by mingc","ѡ�񹩷�");
		}else{
			_GongfModel = new IDropDownModel(
				"select id,mingc from yunsdwb order by mingc","ѡ�񹩷�");
		}
		return _GongfModel;
	}

	public void setGongfModel(IPropertySelectionModel _value) {
		if(_GongfModel==null){
			this.getGongfModels();
		}else{
			_GongfModel = _value;
		}		
	}

	// �跽������
	private IPropertySelectionModel _XufModel;	

	private IDropDownBean _XufValue;

	public IDropDownBean getXufValue() {
		if(_XufValue==null){
			_XufValue=(IDropDownBean)this.getXufModel().getOption(0);
		}
		return _XufValue;
	}

	public void setXufValue(IDropDownBean Value) {
		_XufValue = Value;
	}

	public IPropertySelectionModel getXufModels() {
		String sql = "select id,mingc from diancxxb where  fuid="
				+ getDiancid() + "or  id=" + getDiancid();
		_XufModel = new IDropDownModel(sql,"ѡ���跽");
		return _XufModel;
	}

	public void setXufModel(IPropertySelectionModel _value) {
		if(_XufModel==null){
			this.getXufModels();
		}else{
			_XufModel = _value;
		}		
	}
	
	public IPropertySelectionModel getXufModel() {
		return _XufModel;
	}

	// �糧���
	private String diancid = "";

	public String getDiancid() {

		if (diancid == null || diancid.equals("")) {

			diancid = String.valueOf(((Visit) getPage().getVisit())
					.getDiancxxb_id());
		}
		return diancid;
	}

	public void setDiancid(String diancid) {

		if (!this.diancid.equals(diancid)) {
			this.diancid = diancid;
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
