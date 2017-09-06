package com.zhiren.jt.jiesgl.changfhs;

import java.sql.ResultSet;
import java.sql.SQLException;
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
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Liuc;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.Window;
import com.zhiren.common.ext.form.Checkbox;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.tree.TreeButton;
import com.zhiren.common.ext.tree.TreeNode;
import com.zhiren.dc.hesgl.jiesd.Dcbalancebean;
import com.zhiren.dc.hesgl.jiesd.Jiesdcz;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*
 * ���ߣ�tzf
 * ʱ��:2009-05-25
 * ����:��ɷֹ�˾�Գ����ϴ���jiesb  jiesyfb���ݵĵ��룬ʹ���ݵ��뵽��˾�ǵ�diancjsmkb
 * �� diancjsyfb������ѡ���������
 */
/*
 * ���ߣ�ly
 * ʱ��:2009-09-02
 * ����:ȥ���鿴�ֶΣ������¼�н��㵥���ֱ�Ӳ鿴
 */
public class Balancext extends BasePage implements PageValidateListener {
//	�����û���ʾ
	
	private final static String xiaosdd_xt="���۶���Ĭ������ID";//���۶��� ϵͳ��mingc�ֶ�
	private final static String caigdd_xt="�ɹ�����Ĭ������ID";//�ɹ����� ϵͳ��mingc�ֶ�
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
	}
	
	
	//��������mingc
	private String _liucmc;
	
	public void setLiucmc(String _value) {
		_liucmc = _value;
	}
	
	public String getLiucmc() {
		if (_liucmc == null) {
			_liucmc = "";
		}
		return _liucmc;
	}
	
	
	//�ɹ�����mingc
	private String _liucmc1;
	
	public void setLiucmc1(String _value) {
		_liucmc1 = _value;
	}
	
	public String getLiucmc1() {
		if (_liucmc1 == null) {
			_liucmc1 = "";
		}
		return _liucmc1;
	}
	protected void initialize() {
		super.initialize();
		setMsg("");
		this.setLiucmc("");
		this.setLiucmc1("");
		this.setChange("");
	}
	
	//��������
//	�������� DropDownBean8  
//  �������� ProSelectionModel8
	public IDropDownBean getLiucmcValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean8() == null) {
			((Visit) getPage().getVisit()).setDropDownBean8((IDropDownBean) getILiucmcModel().getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean8();
	}

	public void setLiucmcValue(IDropDownBean value) {
		
		if(((Visit) getPage().getVisit()).getDropDownBean8()!=value){
			
			((Visit) getPage().getVisit()).setDropDownBean8(value);
		}
	}

	public void setILiucmcModel(IPropertySelectionModel value) {
		
		((Visit) getPage().getVisit()).setProSelectionModel8(value);
	}

	public IPropertySelectionModel getILiucmcModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel8() == null) {
			
			getILiucmcModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel8();
	}

	public IPropertySelectionModel getILiucmcModels() {
		
		String sql="";
		sql="select liucb.id,liucb.mingc from liucb,liuclbb where liucb.liuclbb_id=liuclbb.id and liuclbb.mingc='����' order by mingc";

		((Visit) getPage().getVisit()).setProSelectionModel8(new IDropDownModel(sql,"��ѡ��"));
		return ((Visit) getPage().getVisit()).getProSelectionModel8();
	}
//   �������� end
	
	
	

	
	
	
	//�ɹ�����
//	�������� DropDownBean9  
//  �������� ProSelectionModel9
	public IDropDownBean getLiucmcValue1() {
		if (((Visit) getPage().getVisit()).getDropDownBean9() == null) {
			((Visit) getPage().getVisit()).setDropDownBean9((IDropDownBean) getILiucmcModel1().getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean9();
	}

	public void setLiucmcValue1(IDropDownBean value) {
		
		if(((Visit) getPage().getVisit()).getDropDownBean9()!=value){
			
			((Visit) getPage().getVisit()).setDropDownBean9(value);
		}
	}

	public void setILiucmcModel1(IPropertySelectionModel value) {
		
		((Visit) getPage().getVisit()).setProSelectionModel9(value);
	}

	public IPropertySelectionModel getILiucmcModel1() {
		if (((Visit) getPage().getVisit()).getProSelectionModel9() == null) {
			
			getILiucmcModels1();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel9();
	}

	public IPropertySelectionModel getILiucmcModels1() {
		
		String sql="";
		sql="select liucb.id,liucb.mingc from liucb,liuclbb where liucb.liuclbb_id=liuclbb.id and liuclbb.mingc='����' order by mingc";

		((Visit) getPage().getVisit()).setProSelectionModel9(new IDropDownModel(sql,"��ѡ��"));
		return ((Visit) getPage().getVisit()).getProSelectionModel9();
	}
//   �������� end
	
	
	
	
//��ù�Ӧ�� ������
	
//downbean7()
	public IDropDownBean getGongysValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean7() == null) {
			((Visit) getPage().getVisit()).setDropDownBean7((IDropDownBean) getILiucmcModel().getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean7();
	}

	public void setGongysValue(IDropDownBean value) {
		
		if(((Visit) getPage().getVisit()).getDropDownBean7()!=value){
			
			((Visit) getPage().getVisit()).setDropDownBean7(value);
		}
	}

	public void setGongysModel(IPropertySelectionModel value) {
		
		((Visit) getPage().getVisit()).setProSelectionModel7(value);
	}

	public IPropertySelectionModel getGongysModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel7() == null) {
			
			getGongysModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel7();
	}

	public IPropertySelectionModel getGongysModels() {
		
		String dianc_id=this.getTreeid();
		
		String s1="";
		String s2="";
		if(dianc_id==null || dianc_id.equals("0")){
			
		}else{
			s1+=" and js.diancxxb_id="+dianc_id+"\n";
			s2+=" and jf.diancxxb_id="+dianc_id+"\n";
		}
		String sql="";
		sql=" select distinct * from (\n" +
				" select -1 id,'ȫ��' mingc from dual\n" +
				" union\n" +
				" select rownum id,gongysmc  from (\n" +
				" select distinct js.gongysmc from jiesb js,diancxxb d where js.diancxxb_id=d.id\n" +
				" and js.diancjsmkb_id not in (select dm.id from diancjsmkb dm )\n" +
				s1+
				" union\n"+
				" select distinct jf.gongysmc from jiesyfb jf,diancxxb d  where jf.diancxxb_id=d.id\n" +
				" and jf.diancjsyfb_id not in (select dy.id from diancjsyfb dy)\n" +
				s2+
				" )\n"+
				" )\n" +
				" order by id\n" ;
				

		((Visit) getPage().getVisit()).setProSelectionModel7(new IDropDownModel(sql,"��ѡ��"));
		return ((Visit) getPage().getVisit()).getProSelectionModel7();
	}	
	
//***************************************************************************//
	
	private String riq;
	public void setRiq(String value) {
		riq = value;
	}
	public String getRiq() {
		if ("".equals(riq) || riq == null) {
			riq = DateUtil.FormatDate(new Date());
		}
		return riq;
	}
	public void setOriRiq(String value) {
		((Visit) getPage().getVisit()).setString1(value);
	}
	public String getOriRiq() {
		return ((Visit) getPage().getVisit()).getString1();
	}
	
	private String riq1;
	public void setRiq1(String value) {
		riq1 = value;
	}
	public String getRiq1() {
		if ("".equals(riq1) || riq1 == null) {
			riq1 = DateUtil.FormatDate(new Date());
		}
		return riq1;
	}
	public void setOriRiq1(String value) {
		((Visit) getPage().getVisit()).setString2(value);
	}
	public String getOriRiq1() {
		return ((Visit) getPage().getVisit()).getString2();
	}
	
//	 ҳ��仯��¼
	private String Change;
	public String getChange() {
		return Change;
	}
	public void setChange(String change) {
		Change = change;
	}
	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}
	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
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
	
	private String treeid = "";
	public String getTreeid() {
		if (treeid.equals("")) {

			treeid = "0";
		}
		return treeid;
	}
	public void setTreeid(String treeid) {
		if (!this.treeid.equals(treeid)) {

			((Visit) getPage().getVisit()).setboolean3(true);
			this.treeid = treeid;
		}else{
			((Visit) getPage().getVisit()).setboolean3(false);
		}
	}
	
	private String gongysmc="";
	public String getGongysmc(){
		if(this.gongysmc==null){
			return "";
		}
		return this.gongysmc;
	}
	public void setGongysmc(String gongysmc){
		this.gongysmc=gongysmc;
	}
	
	private String checklc="";
	public void setChecklc(String checklc){
		this.checklc=checklc;
	}
	public String getChecklc(){
		
		if(this.checklc==null ||  this.checklc.equals("")){
			return "false";
		}
		return this.checklc;
	}
	// �õ��糧����ֵ�ļ���,(jib:1,2,3,) 1�Ǽ���,2�Ƿֹ�˾,3�ǵ糧
	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		String DiancTreeJib = this.getTreeid();
		// System.out.println("jib:" + DiancTreeJib);
		if (DiancTreeJib == null || DiancTreeJib.equals("")) {
			DiancTreeJib = "0";
		}
		String sqlJib = "select d.jib from diancxxb d where d.id="
				+ DiancTreeJib;
		ResultSet rs = con.getResultSet(sqlJib.toString());

		try {
			while (rs.next()) {
				jib = rs.getInt("jib");
			}
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			con.Close();
		}
		return jib;
	}
	private boolean _ReturnChick = false;
	public void ReturnButton(IRequestCycle cycle) {
		_ReturnChick = true;
	}	
	private boolean _SaveChick = false;
	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	private boolean _DeleteChick=false;
	public void DeleteButton(IRequestCycle cycle){
		_DeleteChick=true;
	}
	
	//�趨Ĭ������
	private boolean _InsertChick=false;
	public void InsertButton(IRequestCycle cycle){
		_InsertChick=true;
	}
	public void submit(IRequestCycle cycle) {
		if (_ReturnChick) {
			_ReturnChick = false;
//			�������۵���ҳ��
			Return(cycle);

		} else if (_SaveChick) {
			_SaveChick = false;
			save();
			getSelectData();
		}else if(_DeleteChick){
			_DeleteChick=false;
			delete();
			getSelectData();
		}else if(_InsertChick){
			_InsertChick=false;
//			�趨Ĭ������
			shedlc();
			getSelectData();
		}
	}
	
	private void Return(IRequestCycle cycle){
//	�������۵���ҳ��
		cycle.activate("Balancemain");
	}
	
	//	�趨Ĭ������
	public void shedlc(){
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con=new JDBCcon();
		String sql="";
		String liuc="";
		
		if(!visit.getString10().equals("fgs_cg")){
//			ȡ����ǰҳ�����۵����̵��趨ֵ
			String liuc_id_xs=MainGlobal.getProperId_String(this.getILiucmcModel(), this.getLiucmc());
			
//			�趨��������
			if(!liuc_id_xs.equals("-1")){
				
//				�û���ҳ������������Ĭ������
				liuc=MainGlobal.getXitxx_item("����", xiaosdd_xt
						, String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id()), "");
				
				if(liuc.equals("")){
					
					sql+=	"insert into xitxxb\n" +
						"  (id, xuh, diancxxb_id, mingc, zhi, danw, leib, zhuangt, beiz)\n" + 
						"values\n" + 
						"  (getnewid("+((Visit) getPage().getVisit()).getDiancxxb_id()+"), \n" +
						"  	(select max(xuh)+1 from xitxxb), "+((Visit) getPage().getVisit()).getDiancxxb_id()+", '"+
							xiaosdd_xt+"', "+liuc_id_xs+", '', '����', 1, 'ʹ��');	\n";
				}else{
					
					sql+= "update xitxxb set zhi='"+liuc_id_xs+"' where mingc='"+xiaosdd_xt+"';	\n";
				}
			}
		}
		
//		ȡ�õ�ǰҳ��ɹ������̵��趨ֵ
		String liuc_id_cg=MainGlobal.getProperId_String(this.getILiucmcModel1(), this.getLiucmc1());
		
//		�趨�ɹ�����
		if(!liuc_id_cg.equals("-1")){
			
//			�û���ҳ������������Ĭ������
			liuc=MainGlobal.getXitxx_item("����", caigdd_xt
					, String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id()), "");
			
			if(liuc.equals("")){
				
				sql+=	"insert into xitxxb\n" +
					"  (id, xuh, diancxxb_id, mingc, zhi, danw, leib, zhuangt, beiz)\n" + 
					"values\n" + 
					"  (getnewid("+((Visit) getPage().getVisit()).getDiancxxb_id()+"), \n" +
					"  	(select max(xuh)+1 from xitxxb), "+((Visit) getPage().getVisit()).getDiancxxb_id()+", '"+
						caigdd_xt+"', "+liuc_id_cg+", '', '����', 1, 'ʹ��');	\n";
			}else{
				
				sql+= "update xitxxb set zhi='"+liuc_id_cg+"' where mingc='"+caigdd_xt+"';	\n";
			}
		}
		
		if(sql.length()>0){
			sql=" begin \n"+sql+" end ;";
			int a=con.getUpdate(sql);
			
			if(a>=0){
				this.setMsg("����Ĭ��״̬���³ɹ�!");
			}
			con.Close();
			
			setLiucmcValue(null);
			setILiucmcModel(null);
			getILiucmcModels();

			setLiucmcValue1(null);
			setILiucmcModel1(null);
			getILiucmcModels1();
		}
	}
	
	//ֱ�Ӵ�jiesb��jiesyfb��ɾ���������νӵ�  �����¼��糧״̬
	public void delete(){
		Visit visit = (Visit) getPage().getVisit();
		String table1 = "jiesb";
		String table2 = "jiesyfb";
		if(visit.getString10().equals("fgs_cg")){
			table1 = "kuangfjsmkb";
			table2 = "kuangfjsyfb";
		}
		String[] raw1=this.getChange().split(";");
		
		for(int i=0;i<raw1.length;i++){//������¼
			
			String[] raw2=raw1[i].split(","); //�ֱ��ȡ jiesbid jiesyfbid
			
			String jiesbid=raw2[0];
			
			String jiesyfbid="";
			
			if(raw2.length>=2){
				jiesyfbid=raw2[1];
			}
			
			if(jiesbid!=null && !jiesbid.equals("")){
				
				if(jiesyfbid!=null && !jiesyfbid.equals("")){///��Ʊ����
					Jiesdcz.Zijsdlccl(table1, Long.parseLong(jiesbid), ((Visit) getPage().getVisit()).getRenyID(), "",0,"HT");
					Liuc.huit(table1, Long.parseLong(jiesbid), ((Visit) getPage().getVisit()).getRenyID(), "");
					Jiesdcz.Zijsdlccl(table2, Long.parseLong(jiesyfbid), ((Visit) getPage().getVisit()).getRenyID(), "",0,"HT");
					Liuc.huit(table2, Long.parseLong(jiesyfbid), ((Visit) getPage().getVisit()).getRenyID(), "");
		
				
				}else{//ú�����
					Jiesdcz.Zijsdlccl(table1, Long.parseLong(jiesbid), ((Visit) getPage().getVisit()).getRenyID(), "",0,"HT");
					Liuc.huit(table1, Long.parseLong(jiesbid), ((Visit) getPage().getVisit()).getRenyID(), "");
				}
			}else{//�˷ѽ���
				Jiesdcz.Zijsdlccl(table2, Long.parseLong(jiesyfbid), ((Visit) getPage().getVisit()).getRenyID(), "",0,"HT");
				Liuc.huit(table2, Long.parseLong(jiesyfbid), ((Visit) getPage().getVisit()).getRenyID(), "");
			}
			
		}
		
		this.setChange("");
	}
	
	public void UpdateSetKuangfjsmkb(JDBCcon con,String TableName,String TableID){
		StringBuffer bf = new StringBuffer();
		bf.append("update "+TableName+" set zhuangt = 1 where id = "+TableID+"\n");
		con.getUpdate(bf.toString());
	}
	
	public void save() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con=new JDBCcon();
//		System.out.println(this.getChecklc());
		String liuc_id="0";//��������
		String liuc_id_cg="0";//�ɹ�����  Ĭ��Ϊ0
		String tis="";
		String mes="";
		String value="";//����
		//��Ҫ��������
		if(this.getChecklc().equals("true")){
			String temps = ""; 
			if(!visit.getString10().equals("fgs_cg")){
//				String temps=this.getsdlc(null, xiaosdd_xt, true);
				temps=MainGlobal.getXitxx_item("����", xiaosdd_xt, 
						String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id()), "");
				
				if(temps!=null && !temps.equals("")){
					liuc_id=temps;
				}else{
					this.setMsg("��������������!");//��  �������� checkbox��ѡ�� ���������̱��뱻����
					return ;
				}
			}

			
			temps=MainGlobal.getXitxx_item("����", caigdd_xt, 
					String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id()), "");
			
			if(temps!=null && !temps.equals("")){
				liuc_id_cg=temps;
			}else{
				this.setMsg("�����òɹ�����!");//��  �������� checkbox��ѡ�� ���������̱��뱻����
				return ;
			}
		}else{
			
		}
//		System.out.println(this.getLiucmc());
		
//		String liuc_id=MainGlobal.getProperId_String(this.getILiucmcModel(), this.getLiucmc());
//		System.out.println(liuc_id);
//		System.out.println(this.getChange());
		String[] raw1=this.getChange().split(";");
		
		for(int i=0;i<raw1.length;i++){//������¼
			
			con.setAutoCommit(false);
			String[] raw2=raw1[i].split(","); //�ֱ��ȡ jiesbid jiesyfbid xiaosht
			
			String jiesbid=raw2[0];
			
			String jiesyfbid="";
			
			String xiaosht=raw2[2];
			
			if(raw2.length>=2){
				jiesyfbid=raw2[1];
			}
			
			if(visit.getString10().equals("fgs_cg")){//�ɹ�����
				if(jiesbid!=null && !jiesbid.equals("")){
					
					if(jiesyfbid!=null && !jiesyfbid.equals("")){///��Ʊ����
						
						try{
							UpdateSetKuangfjsmkb(con,"kuangfjsmkb",jiesbid);
							UpdateSetKuangfjsmkb(con,"kuangfjsyfb",jiesyfbid);
							con.commit();
							if(this.getChecklc().equals("true")){
								Liuc.tij("kuangfjsmkb",Long.parseLong(jiesbid), ((Visit) getPage().getVisit()).getRenyID(), "", Long.parseLong(liuc_id_cg));
								Liuc.tij("kuangfjsyfb",Long.parseLong(jiesyfbid), ((Visit) getPage().getVisit()).getRenyID(), "", Long.parseLong(liuc_id_cg));
								Jiesdcz.Zijsdlccl("kuangfjsmkb", Long.parseLong(jiesbid),  ((Visit) getPage().getVisit()).getRenyID(), "", Long.parseLong(liuc_id), "TJ");
								Jiesdcz.Zijsdlccl("kuangfjsyfb", Long.parseLong(jiesyfbid),  ((Visit) getPage().getVisit()).getRenyID(), "", Long.parseLong(liuc_id), "TJ");
							}
						}catch(Exception e){
							if(e.getMessage().equals("error")){
								con.rollBack();
								tis+="��������<br>";
								continue;
							}
						}
						
						con.commit();
						
					
					}else{//ú�����
						
						String Str_kfmk="";
						
						try{
							UpdateSetKuangfjsmkb(con,"kuangfjsmkb",jiesbid);
							con.commit();
							if(this.getChecklc().equals("true")){
								Liuc.tij("kuangfjsmkb",Long.parseLong(jiesbid), ((Visit) getPage().getVisit()).getRenyID(), "", Long.parseLong(liuc_id_cg));
							}
							
						}catch(Exception e){
							if(e.getMessage().equals("error")){
								con.rollBack();
								tis+="��������<br>";
								continue;
							}
						}
						
						con.commit();
						
					}
				}else{//�˷ѽ���
					String Str_kfyf="";
					try{
						UpdateSetKuangfjsmkb(con,"kuangfjsyfb",jiesyfbid);
						con.commit();
						if(this.getChecklc().equals("true")){
							Liuc.tij("kuangfjsyfb",Long.parseLong(jiesyfbid), ((Visit) getPage().getVisit()).getRenyID(), "", Long.parseLong(liuc_id_cg));
						}
					}catch(Exception e){
						if(e.getMessage().equals("error")){
							con.rollBack();
							tis+="���:"+value+"&nbsp;&nbsp;"+"��������<br>";
							continue;
						}
					}
					
					con.commit();
				}
			}else{//����ҳ��
				if(jiesbid!=null && !jiesbid.equals("")){
					
					if(jiesyfbid!=null && !jiesyfbid.equals("")){///��Ʊ����
						
						String Str_kfmk="";//��ú���sql
						String Str_kfyf="";//���˷ѱ�sql
						try{
							
							value=MainGlobal.getTableCol("jiesb", "bianm", "id", jiesbid);
//							�ж�ú���Ƿ����ͬ���й���
							String SQL="select ht.id, ht.gongysb_id, ht.gongfdwmc, ht.gongfkhyh, ht.gongfzh\n" +
	                           "  from hetmkfzglb h, jiesb j, meikfzmxb m, hetb ht\n" + 
	                           " where m.meikfzb_id = h.meikfzb_id\n" + 
	                           "   and m.meikxxb_id = j.meikxxb_id\n" + 
	                           "   and j.diancxxb_id = h.diancxxb_id\n" + 
	                           "   and h.hetb_id = ht.id\n" + 
	                           "   and j.fahksrq >= h.qisrq\n" + 
	                           "   and j.fahjzrq <= h.jiezrq \n"+
	                           "   and j.id="+jiesbid;
				  	        ResultSet rsl=con.getResultSet(SQL);
							if(!rsl.next()){
								mes+="���:"+value+"δ���к�ͬú������������δ�ҵ���Ӧ��ͬ!<br>";
								continue;
							}else{				
								String ds_id=Jiesdcz.InsertIntoGSDiancjsb(con,"jiesb", jiesbid,"",Str_kfmk,xiaosht,((Visit) getPage().getVisit()).getDiancxxb_id());
								String dy_id=Jiesdcz.InsertIntoGSDiancjsb(con,"jiesyfb", jiesyfbid,ds_id,Str_kfyf,"",((Visit) getPage().getVisit()).getDiancxxb_id());
								String ks_id=Jiesdcz.InsertIntoGSKuangfjsb(con, "kuangfjsmkb", jiesbid, ds_id, "");
								String ky_id=Jiesdcz.InsertIntoGSKuangfjsb(con, "kuangfjsyfb", jiesyfbid, dy_id, ks_id);
								if(this.getChecklc().equals("true")){
									Liuc.tij("diancjsmkb",Long.parseLong(ds_id), ((Visit) getPage().getVisit()).getRenyID(), "", Long.parseLong(liuc_id));
									Liuc.tij("diancjsyfb",Long.parseLong(dy_id), ((Visit) getPage().getVisit()).getRenyID(), "", Long.parseLong(liuc_id));
									Liuc.tij("kuangfjsmkb",Long.parseLong(ks_id), ((Visit) getPage().getVisit()).getRenyID(), "", Long.parseLong(liuc_id_cg));
									Liuc.tij("kuangfjsyfb",Long.parseLong(ky_id), ((Visit) getPage().getVisit()).getRenyID(), "", Long.parseLong(liuc_id_cg));
									Jiesdcz.Zijsdlccl("diancjsmkb", Long.parseLong(ds_id),  ((Visit) getPage().getVisit()).getRenyID(), "", Long.parseLong(liuc_id), "TJ");
									Jiesdcz.Zijsdlccl("diancjsyfb", Long.parseLong(dy_id),  ((Visit) getPage().getVisit()).getRenyID(), "", Long.parseLong(liuc_id), "TJ");
									Jiesdcz.Zijsdlccl("kuangfjsmkb", Long.parseLong(ks_id),  ((Visit) getPage().getVisit()).getRenyID(), "", Long.parseLong(liuc_id), "TJ");
									Jiesdcz.Zijsdlccl("kuangfjsyfb", Long.parseLong(ky_id),  ((Visit) getPage().getVisit()).getRenyID(), "", Long.parseLong(liuc_id), "TJ");
								}
							}
						}catch(Exception e){
							if(e.getMessage().equals("error")){
								con.rollBack();
								tis+="���:"+value+"&nbsp;&nbsp;"+"��������<br>";
								continue;
							}
						}
						
						con.commit();
						
					
					}else{//ú�����
						
						String Str_kfmk="";
						
						try{
							value=MainGlobal.getTableCol("jiesb", "bianm", "id", jiesbid);
//							�ж�ú���Ƿ����ͬ���й���
							String SQL="select ht.id, ht.gongysb_id, ht.gongfdwmc, ht.gongfkhyh, ht.gongfzh\n" +
	                           "  from hetmkfzglb h, jiesb j, meikfzmxb m, hetb ht\n" + 
	                           " where m.meikfzb_id = h.meikfzb_id\n" + 
	                           "   and m.meikxxb_id = j.meikxxb_id\n" + 
	                           "   and j.diancxxb_id = h.diancxxb_id\n" + 
	                           "   and h.hetb_id = ht.id\n" + 
	                           "   and j.fahksrq >= h.qisrq\n" + 
	                           "   and j.fahjzrq <= h.jiezrq \n"+
	                           "   and j.id="+jiesbid;
				  	        ResultSet rsl=con.getResultSet(SQL);
							if(!rsl.next()){
								mes+="���:"+value+"δ���к�ͬú������������δ�ҵ���Ӧ��ͬ!<br>";
								continue;
							}else{		
								String ds_id=Jiesdcz.InsertIntoGSDiancjsb(con,"jiesb", jiesbid,"",Str_kfmk,xiaosht,((Visit) getPage().getVisit()).getDiancxxb_id());
								String ks_id=Jiesdcz.InsertIntoGSKuangfjsb(con, "kuangfjsmkb", jiesbid, ds_id, "");
								con.commit();
								if(this.getChecklc().equals("true")){
									Liuc.tij("diancjsmkb",Long.parseLong(ds_id), ((Visit) getPage().getVisit()).getRenyID(), "", Long.parseLong(liuc_id));
									Liuc.tij("kuangfjsmkb",Long.parseLong(ks_id), ((Visit) getPage().getVisit()).getRenyID(), "", Long.parseLong(liuc_id_cg));
								}
							}
							
						}catch(Exception e){
							if(e.getMessage().equals("error")){
								con.rollBack();
								tis+="���:"+value+"&nbsp;&nbsp;"+"��������<br>";
								continue;
							}
						}
						
						con.commit();
						
					}
				}else{//�˷ѽ���
					String Str_kfyf="";
					try{
						
						value=MainGlobal.getTableCol("jiesyfb", "bianm", "id", jiesyfbid);
						
						String dy_id=Jiesdcz.InsertIntoGSDiancjsb(con,"jiesyfb", jiesyfbid,"0",Str_kfyf,"",((Visit) getPage().getVisit()).getDiancxxb_id());
						String ky_id=Jiesdcz.InsertIntoGSKuangfjsb(con, "kuangfjsyfb", jiesyfbid, dy_id, "0");
						
						if(this.getChecklc().equals("true")){
							Liuc.tij("diancjsyfb",Long.parseLong(dy_id), ((Visit) getPage().getVisit()).getRenyID(), "", Long.parseLong(liuc_id));
							Liuc.tij("kuangfjsyfb",Long.parseLong(ky_id), ((Visit) getPage().getVisit()).getRenyID(), "", Long.parseLong(liuc_id_cg));
						}
					}catch(Exception e){
						if(e.getMessage().equals("error")){
							con.rollBack();
							tis+="���:"+value+"&nbsp;&nbsp;"+"��������<br>";
							continue;
						}
					}
					
					con.commit();
				}
			}
			
		}
		
		this.setChange("");
		con.Close();
		if(!mes.equals("")){
			this.setMsg(mes);
		}else{
			if(!tis.equals("")){
				this.setMsg(tis);
			}else{
				this.setMsg("�����ɹ�!");
			}
		}
	}
	
	private StringBuffer getBaseSql(){
		Visit visit = (Visit) getPage().getVisit();
		StringBuffer bf=new StringBuffer();
		String diancxxb_id=this.getTreeid();
		String gysmc=this.getGongysValue().getValue();
		
		String str_js="";	//jiesb		where����
		String str_jy="";	//jiesyfb	where����
		String tableName1 = "jiesb";
		String tableName2 = "jiesyfb";
		String isDaor1 = "  and nvl(js.diancjsmkb_id,0)=0 \n"; //where���� �ж��Ƿ��ѵ���
		String isDaor2 = "  and nvl(jy.diancjsyfb_id,0)=0 \n"; //where���� �ж��Ƿ��ѵ���
		
		if(!diancxxb_id.equals("0")){
			
			str_js+=" and d.id="+diancxxb_id+"";
			str_jy+=" and d.id="+diancxxb_id+" ";
			
		}
		
		if(!this.getGongysValue().getStrId().equals("-1")){
			
			str_js+=" and js.gongysmc='"+gysmc+"' ";
			str_jy+=" and jy.gongysmc='"+gysmc+"' ";
		}
		
		if(visit.getString10().equals("fgs_cg")){
			tableName1 = "kuangfjsmkb";
			tableName2 = "kuangfjsyfb";
			isDaor1 = "	and js.zhuangt = 0\n";
			isDaor2 = "	and jy.zhuangt = 0\n";
		}
		
		
		//��Ҫ����  ����״̬������     diancmcjsb����������Ѿ�û����
		bf.append(" select distinct * from( \n");
		
		bf.append(" select js.id jiesbid,jy.id jiesyfbid,'<a style=\"cursor:hand\" onclick=chak('||'\"'||'dianc,'||js.bianm||'\"'||')>'||js.bianm||'</a>' bianh,js.gongysmc gongysmc,d.mingc dianc,ht.hetbh hetbh,");
		if(!visit.getString10().endsWith("fgs_cg")){
			bf.append("GetXiaosht(js.id) as xiaosht,");
		}
		bf.append("fl.mingc jieslx,js.hansdj hansdj,\n");
		bf.append(" js.fengsjj fengsjj,js.hansmk hansmc,jy.hansyf hansyf\n");
//		bf.append("  '<a style=\"cursor:hand\" onclick=chak('||'\"'||'dianc,'||js.bianm||'\"'||')>�鿴</a>' chak \n");
		bf.append("  from  "+tableName1+" js,"+tableName2+" jy,diancxxb d,hetb ht,feiylbb fl where \n");
//		bf.append(" ( js.diancxxb_id=d.id or  jy.diancxxb_id=d.id ) \n");
		bf.append(" ( js.diancxxb_id=d.id ) \n");
		bf.append("  and fl.id(+)=js.jieslx \n");
		bf.append("  and js.hetb_id=ht.id(+) and jy.diancjsmkb_id(+)=js.id\n");
		bf.append("  and js.fuid=0\n");
		bf.append(isDaor1);
		bf.append(str_js+" \n");
		bf.append("  and js.jiesrq>=").append(DateUtil.FormatOracleDate(this.getRiq())).append(" and js.jiesrq<=").append(DateUtil.FormatOracleDate(this.getRiq1()));
		
		bf.append("  union\n");
		
		bf.append("  select js.id jiesbid,jy.id jiesyfbid, '<a style=\"cursor:hand\" onclick=chak('||'\"'||'dianc,'||jy.bianm||'\"'||')>'||jy.bianm||'</a>' bianh,jy.gongysmc gongysmc,d.mingc dianc,ht.hetbh hetbh,");
		if(!visit.getString10().endsWith("fgs_cg")){
			bf.append("GetXiaosht(js.id) as xiaosht,");
		}
		bf.append("fl.mingc jieslx,js.hansdj hansdj,\n");
		bf.append("   js.fengsjj fengsjj,js.hansmk hansmc,jy.hansyf hansyf\n");
//		bf.append("  '<a style=\"cursor:hand\" onclick=chak('||'\"'||'dianc,'||jy.bianm||'\"'||')>�鿴</a>' chak \n");
		bf.append("   from "+tableName1+" js,"+tableName2+" jy,diancxxb d,(select id,hetbh from hetb union select id,hetbh from hetys) ht,feiylbb fl where \n");
//		bf.append(" (  js.diancxxb_id=d.id or jy.diancxxb_id=d.id  )\n");
		bf.append("  jy.diancxxb_id=d.id \n");
		bf.append("  and fl.id(+)=jy.jieslx\n");
		bf.append("  and jy.hetb_id=ht.id(+) and jy.diancjsmkb_id=js.id(+)\n");
		bf.append("  and jy.fuid=0\n");
		bf.append(isDaor2);
		bf.append(str_jy+" \n");
		bf.append("  and jy.jiesrq>=").append(DateUtil.FormatOracleDate(this.getRiq())).append(" and jy.jiesrq<=").append(DateUtil.FormatOracleDate(this.getRiq1()));
		
		bf.append("  ) order by bianh\n");
	//	System.out.println(bf.toString());
		return bf;
		
	}
	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		
		String sSql=this.getBaseSql().toString();
		
		ResultSetList rsl = con.getResultSetList(sSql);
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "��������SQL:" + sSql);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv",rsl);
		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
		egu.getGridColumns().add(2, new GridColumn(GridColumn.ColType_Check));
//		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
//		egu.addColumn(2,new GridColumn(GridColumn.ColType_Check));
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.setHeight("bodyHeight");
		egu.getColumn("jiesbid").setHidden(true);
		egu.getColumn("jiesbid").setEditor(null);
		egu.getColumn("jiesyfbid").setHidden(true);
		egu.getColumn("jiesyfbid").setEditor(null);
		egu.getColumn("bianh").setHeader("������");
		egu.getColumn("bianh").setEditor(null);
		egu.getColumn("bianh").setWidth(120);
		egu.getColumn("gongysmc").setHeader("��Ӧ��");
		egu.getColumn("gongysmc").setEditor(null);
		egu.getColumn("dianc").setHeader("�糧");
		egu.getColumn("dianc").setEditor(null);
		egu.getColumn("hetbh").setHeader("��ͬ���");
		egu.getColumn("hetbh").setEditor(null);
		if(!visit.getString10().endsWith("fgs_cg")){
			egu.getColumn("xiaosht").setHeader("���ۺ�ͬ");
			egu.getColumn("xiaosht").setWidth(250);
		}
		egu.getColumn("jieslx").setHeader("��������");
		egu.getColumn("jieslx").setEditor(null);
		egu.getColumn("hansdj").setHeader("ú�˰����");
		egu.getColumn("hansdj").setEditor(null);
		egu.getColumn("fengsjj").setHeader("ú��ֹ�˾�Ӽ�");
		egu.getColumn("fengsjj").setEditor(null);
		egu.getColumn("hansmc").setHeader("��˰ú��");
		egu.getColumn("hansmc").setEditor(null);
		egu.getColumn("hansyf").setHeader("��˰�˷�");
		egu.getColumn("hansyf").setEditor(null);
//		egu.getColumn("chak").setHeader("�鿴");
//		egu.getColumn("chak").setEditor(null);
		
		if(!visit.getString10().endsWith("fgs_cg")){
			egu.getColumn("xiaosht").setEditor(new ComboBox());
			String sql = 
				"select distinct h.id,h.hetbh||' '||g.mingc||' '||min(jg.jij) as xx\n" +
				"from jiesb j,hetb h,gongysb g,hetjgb jg\n" + 
				"where j.gongysb_id = h.gongysb_id\n" + 
				"      and j.jihkjb_id = h.jihkjb_id\n" + 
				"      and h.gongysb_id = g.id\n" + 
				"      and jg.hetb_id = h.id\n" + 
				"      and j.yansksrq>=h.qisrq\n" + 
				"      and j.yansjzrq<=h.guoqrq\n" + 
				"      and h.leib = 1\n" + 
				"group by h.id,h.hetbh,g.mingc";

			egu.getColumn("xiaosht").setComboEditor(egu.gridId,new IDropDownModel(sql));
		}
		
//		egu.getColumn("xiaosht").setReturnId(true);

		GridButton gbt1 = new GridButton("����","function(){document.getElementById('ReturnButton').click();}");
		gbt1.setIcon(SysConstant.Btn_Icon_Return);
		egu.addTbarBtn(gbt1);
		
		egu.addTbarText("-");
		
		
		egu.addTbarText("��������:");
		DateField dStart = new DateField();
		dStart.Binding("RIQ","forms[0]");
		dStart.setValue(getRiq());
		egu.addToolbarItem(dStart.getScript());
		
		egu.addTbarText("��");
		
	
		DateField dEnd = new DateField();
		dEnd.Binding("RIQ1","forms[0]");
		dEnd.setValue(getRiq1());
		egu.addToolbarItem(dEnd.getScript());
		
		
		egu.addTbarText("-");
		egu.addTbarText("��λ����:");
//		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
//				ExtTreeUtil.treeWindowType_Dianc,
//				((Visit) getPage().getVisit()).getDiancxxb_id(),getTreeid());
		
		ExtTreeUtil etu=new ExtTreeUtil("diancTree");
		etu.defaultSelectid=this.getTreeid();
		//etu.getDefaultTree("diancTree", con.getResultSetList(this.getDTsql().toString()), false);
		this.getDefaultTree(etu, "diancTree", con.getResultSetList(this.getDTsql().toString()), false);
		
		etu.window = new Window("diancTree");
		etu.window.setItems("diancTree"+"_treePanel");
		TreeButton tb=new TreeButton("ȷ��","function(){" +
				" var cks = diancTree_treePanel.getSelectionModel().getSelectedNode();\n" +
				" if(cks==null){diancTree_window.hide();return;}\n" +
				" var obj0 = document.getElementById('diancTree_id');obj0.value = cks.id;diancTree_text.setValue(cks.text);\n" +
//				" if(cks.leaf){ document.all.diancmc.value=cks.parentNode.text;\n"+
//				" document.all.gongysmc.value=cks.text;}\n"+
//				" else{ document.all.diancmc.value=cks.text;\n"+
//				" document.all.gongysmc.value='';}\n"+
				" diancTree_window.hide();\n" +
				"document.forms[0].submit();}");
		//etu.addBbarButton(TreeButton.ButtonType_Window_Ok, "SaveButton");
		etu.addBbarButton(tb);
		etu.setWidth(200);
		etu.setTitle("��λѡ��");
		
		
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");
		
		
		egu.addTbarText("��Ӧ��:");
		ComboBox comb1 = new ComboBox();
		comb1.setTransform("GONGYS");
		comb1.setId("GONGYS");// ���Զ�ˢ�°�
		comb1.setLazyRender(true);// ��̬��
		comb1.setWidth(120);
		comb1.setListWidth(200);
		comb1.setListeners("select:function(combo,record,index){document.forms[0].submit();}");
		egu.addToolbarItem(comb1.getScript());
		
		egu.addTbarText("-");
		
		
		
		String gb_fs="function(){\n" +
		" var Mrcd = gridDiv_grid.getSelectionModel().getSelections(); \n" +
		" if( Mrcd==null || Mrcd.length<=0){Ext.Msg.alert('��ʾ��Ϣ','����ѡ���¼�ٽ��в���!');return;}\n" +
		" document.all.CHANGE.value='';\n" +
		" for(i = 0; i< Mrcd.length; i++){\n" +
		" var rc=Mrcd[i]; \n";
		if(!visit.getString10().endsWith("fgs_cg")){
			gb_fs+=" if( rc.get('XIAOSHT')==null || rc.get('XIAOSHT')==''){Ext.Msg.alert('��ʾ��Ϣ','����ѡ�����ۺ�ͬ�ٽ��в���!');return;}\n";
		}
		gb_fs+=
			" document.all.CHANGE.value+=rc.get('JIESBID')+','+rc.get('JIESYFBID')+','+rc.get('XIAOSHT');\n" +
			" if(i!=Mrcd.length-1){ document.all.CHANGE.value+=';';}\n" +
			" }\n" +
			" document.all.item('SaveButton').click();" +
	//		" Ext.Msg.progress('��ʾ��Ϣ','��ȴ�','���ݼ����С���');\n"+
			" Ext.Msg.show({title: 'Please wait',msg: '���ڼ�������...',progressText: '���ݼ�����...',width:300,progress:true,closable:false});for(var i=0;i<=10;i++)setTimeout(function(){Ext.Msg.updateProgress(i/10,'���ݼ����С���','���ڼ�������');},i*100);"+
			"\n}";
		
		GridButton gbt = new GridButton("����",gb_fs);
		gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbt);
		
		egu.addTbarText("-");
		
		String gb2_fs="function(){\n" +
		" var Mrcd = gridDiv_grid.getSelectionModel().getSelections(); \n" +
		" if( Mrcd==null || Mrcd.length<=0){Ext.Msg.alert('��ʾ��Ϣ','����ѡ���¼�ٽ��в���!');return;}\n" +
		" Ext.MessageBox.confirm('��Ϣ��ʾ','�Ƿ�ȷ��ɾ��?',function(btn){" +
		" 		if (btn == 'yes') {" +
		" 			document.all.CHANGE.value='';\n" +
		" 			for(i = 0; i< Mrcd.length; i++){\n" +
		" 			var rc=Mrcd[i]; document.all.CHANGE.value+=rc.get('JIESBID')+','+rc.get('JIESYFBID');\n" +
		" 			if(i!=Mrcd.length-1){ document.all.CHANGE.value+=';';}\n" +
		" 			}\n" +
		" 			document.all.item('DeleteButton').click();\n" +
		" 			Ext.Msg.progress('��ʾ��Ϣ','��ȴ�','���ݼ����С���');\n"+
		" 		}" +
		" 	})" +
		
		"\n}";
		
		
//		String gb2_fs="function(){\n" +
//				" var Mrcd = gridDiv_grid.getSelectionModel().getSelections(); \n" +
//				" if( Mrcd==null || Mrcd.length<=0){Ext.Msg.alert('��ʾ��Ϣ','����ѡ���¼�ٽ��в���!');return;}\n" +
//				" document.all.CHANGE.value='';\n" +
//				" for(i = 0; i< Mrcd.length; i++){\n" +
//				" var rc=Mrcd[i]; document.all.CHANGE.value+=rc.get('JIESBID')+','+rc.get('JIESYFBID');\n" +
//				" if(i!=Mrcd.length-1){ document.all.CHANGE.value+=';';}\n" +
//				" }\n" +
//				" document.all.item('DeleteButton').click();" +
//				" Ext.Msg.show({title: 'Please wait',msg: '���ڼ�������...',progressText: '���ݼ�����...',width:300,progress:true,closable:false});for(var i=0;i<=10;i++)setTimeout(function(){Ext.Msg.updateProgress(i/10,'���ݼ����С���','���ڼ�������');},i*100);"+
//				"\n}";
		GridButton gbt2 = new GridButton("ɾ��",gb2_fs);
		gbt2.setIcon(SysConstant.Btn_Icon_Delete);
		egu.addTbarBtn(gbt2);
		
		egu.addTbarText("-");
		
		Checkbox cb=new Checkbox();
//		cb.setText("�����������");
		cb.setId("Iscb");
		cb.setBoxLabel("��������");
		
		if(this.getChecklc().equals("true")){
			cb.setChecked(true);
		}
		cb.setListeners("check:function(own,checked){if(checked){document.all.ISCHECKED.value='true'}else{document.all.ISCHECKED.value='false'}}");
		egu.addToolbarItem(cb.getScript());
		
		egu.addTbarText("-");
		
		String xiaos_va="";
		String caig_va="";
		
		String sx = MainGlobal.getXitxx_item("����", xiaosdd_xt, String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id()), "");
		String sc= MainGlobal.getXitxx_item("����", caigdd_xt, String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id()), "");
		
		if(sx!=null && !sx.equals("")){
			xiaos_va=" Ext.getDom('dayzt1').value='"+sx+"';\n";
		}
		if(sc!=null && !sc.equals("")){
			caig_va=" Ext.getDom('dayzt2').value='"+sc+"';\n";
		}
		
		egu.addOtherScript(xiaos_va+caig_va);
		String gb3_fs="function(){  \n"
//			+	"var Mrcd = gridDiv_grid.getSelectionModel().getSelections(); \n"
//			+   " if( Mrcd==null || Mrcd.length<=0){Ext.Msg.alert('��ʾ��Ϣ','����ѡ���¼�ٽ��в���!');return;}\n"
//			+"   document.all.CHANGE.value='';\n"
//			+   " for(i = 0; i< Mrcd.length; i++){\n"
//			+   " var rc=Mrcd[i]; document.all.CHANGE.value+=rc.get('JIESBID')+','+rc.get('JIESYFBID');\n"
//			+   " if(i!=Mrcd.length-1){ document.all.CHANGE.value+=';';}\n"
//			+   "}\n"
			
			+ " if(!win){	\n" 
			+ "	\tvar form = new Ext.form.FormPanel({	\n" 
			+ " \tbaseCls: 'x-plain',	\n" 		
			+ " \tlabelAlign:'right',	\n" 
			+ " \tdefaultType: 'textfield',	\n"
			+ " \titems: [{		\n"
			+ " \txtype:'fieldset',	\n"
			+ " \ttitle:'��ѡ����������',	\n"
			+ " \tautoHeight:false,	\n"
			+ " \theight:220,	\n"
			+ " \titems:[	\n";
			
			if(!visit.getString10().endsWith("fgs_cg")){
				gb3_fs+=" \tlcmccb=new Ext.form.ComboBox({	\n" 
					+ " \twidth:150,	\n"
					+ " \tid:'lcmccb',	\n"
					+ " \tselectOnFocus:true,	\n"
					+ "	\ttransform:'LiucmcDropDown',	\n"		
					+ " \tlazyRender:true,	\n"	
					+ " \tfieldLabel:'��������',		\n" 
					+ " \ttriggerAction:'all',	\n"
					+ " \ttypeAhead:true,	\n"	
					+ " \tforceSelection:true,	\n"
					+ " \teditable:false	\n"					
					+ " \t}),	\n";
			}
			
			gb3_fs+=
			" \tlcmccb1=new Ext.form.ComboBox({	\n" 
			+ " \twidth:150,	\n"
			+ " \tid:'lcmccb1',	\n"
			+ " \tselectOnFocus:true,	\n"
			+ "	\ttransform:'LiucmcDropDown1',	\n"		
			+ " \tlazyRender:true,	\n"	
			+ " \tfieldLabel:'�ɹ�����',		\n" 
			+ " \ttriggerAction:'all',	\n"
			+ " \ttypeAhead:true,	\n"	
			+ " \tforceSelection:true,	\n"
			+ " \teditable:false	\n"					
			+ " \t})	\n"
			
			+ " \t]		\n"
			+ " \t}]	\n"		
			+ " \t});	\n"
			
			+ " \twin = new Ext.Window({	\n"
			+ " \tel:'hello-win',	\n"
			+ " \tlayout:'fit',	\n"
			+ " \twidth:500,	\n"	
			+ " \theight:300,	\n"
			+ " \tcloseAction:'hide',	\n"
			+ " \tplain: true,	\n"
			+ " \ttitle:'����',	\n"
			+ " \titems: [form],	\n"
			+ " \tbuttons: [{	\n"
			+ " \ttext:'ȷ��',	\n"
			+ " \thandler:function(){	\n"  
			+ " \twin.hide();	\n";
			
			if(!visit.getString10().endsWith("fgs_cg")){
				gb3_fs+=" \tif(lcmccb.getRawValue()=='��ѡ��' && lcmccb1.getRawValue()=='��ѡ��'){		\n" 
					+ "	\t	alert('��ѡ���������ƣ�');		\n"
					+ " \t}else{" 
					+ " \t\t document.getElementById('TEXT_LIUCMCSELECT_VALUE').value=lcmccb.getRawValue();	\n"
					+ " \t\t document.getElementById('TEXT_LIUCMCSELECT_VALUE1').value=lcmccb1.getRawValue();	\n"
					+ " \t\t document.all.item('InsertButton').click();	\n"
					+" Ext.Msg.show({title: 'Please wait',msg: '���ڼ�������...',progressText: '���ݼ�����...',width:300,progress:true,closable:false});for(var i=0;i<=10;i++)setTimeout(function(){Ext.Msg.updateProgress(i/10,'���ݼ����С���','���ڼ�������');},i*100);"
					+ " \t}	\n";
			} else {
				gb3_fs+=" \tif(lcmccb1.getRawValue()=='��ѡ��'){		\n"
					+ "	\t	alert('��ѡ���������ƣ�');		\n"
					+ " \t}else{" 
					+ " \t\t document.getElementById('TEXT_LIUCMCSELECT_VALUE1').value=lcmccb1.getRawValue();	\n"
					+ " \t\t document.all.item('InsertButton').click();	\n"
					+" Ext.Msg.show({title: 'Please wait',msg: '���ڼ�������...',progressText: '���ݼ�����...',width:300,progress:true,closable:false});for(var i=0;i<=10;i++)setTimeout(function(){Ext.Msg.updateProgress(i/10,'���ݼ����С���','���ڼ�������');},i*100);"
					+ " \t}	\n";
			}
			gb3_fs+=
				 " \t}	\n"
				+ " \t},{	\n"
				+ " \ttext: 'ȡ��',	\n"
				+ " \thandler: function(){	\n"
				+ " \twin.hide();	\n"
				+ " \tdocument.getElementById('TEXT_LIUCMCSELECT_VALUE').value='';	\n"	
				+ " \t}		\n"
				+ " \t}]	\n"
				+ " \t});}	\n" 
				+ " \twin.show(this);	\n"

			+ " \tif(document.getElementById('TEXT_LIUCMCSELECT_VALUE').value!=''){	\n"	
			//+ " \tChangb.setRawValue(document.getElementById('TEXT_LIUCMCSELECT_VALUE').value);	\n"	
			+ " \t}	\n"	
			+ " \t}";
		GridButton gbt3 = new GridButton("�趨Ĭ������",gb3_fs);
		gbt3.setIcon(SysConstant.Btn_Icon_Create);
		egu.addTbarBtn(gbt3);
		egu.addPaging(0);
		setExtGrid(egu);
		con.Close();
	}
	
	
	public void getDefaultTree(ExtTreeUtil etu,String treeId,ResultSetList rsl,boolean checkbox) {
		
		etu.treeId=treeId;
		etu.init();
		TreeNode parentNode = null;
		TreeNode RootNode = null;
		int lastjib = 0;
		while(rsl.next()) {
			int curjib = rsl.getInt(2);
			TreeNode node = new TreeNode(rsl.getString(0),rsl.getString(1));
			node.setCheckbox(checkbox);
			if(parentNode==null) {
				RootNode = node;
				node.setCheckbox(false);
				parentNode = node;
				lastjib = curjib+1;
				continue;
			}
			if(lastjib < curjib) {
				parentNode = (TreeNode)parentNode.getLastChild();
			}else if(lastjib > curjib){
				for(int i=0;i<lastjib - curjib;i++)
					parentNode = (TreeNode)parentNode.getParentNode();
			}
			lastjib = curjib;
			parentNode.appendChild(node);
		}
		etu.setRootNode(RootNode);
		//this.setWidth(200);
		//this.addButton(TreeButton.ButtonType_Ok, "SaveButton");
		//this.setTitle("��λѡ��");
	}
	
	
	//��õ糧 ��Ӧ�̵ĵ糧���νṹ��sql
	private StringBuffer getDTsql(){
		
		Visit visit=(Visit)this.getPage().getVisit();
		StringBuffer bf=new StringBuffer();
		
		bf.append(" select distinct * from ( \n");
		bf.append(" select 0 id,'ȫ��' mingc,0 jib from dual\n");
		bf.append(" union\n");
		bf.append(" select d.id,d.mingc,1 jib from jiesb js,diancxxb d where js.diancxxb_id=d.id\n");
		bf.append(" and nvl(js.diancjsmkb_id,0) not in (select nvl(dm.id,null) from diancjsmkb dm )\n");
		bf.append(" union \n");
		bf.append(" select d.id,d.mingc,1 jib from jiesyfb jf,diancxxb d  where jf.diancxxb_id=d.id \n");
		bf.append(" and nvl(jf.diancjsyfb_id,0) not in (select nvl(dy.id,null) from diancjsyfb dy)\n");
		bf.append(" ) \n");
		bf.append(" order by id,mingc\n");
	
		
		/*
		bf.append("   select     decode(id,0,id,rownum-1) id,mingc,fuid  from ( \n");
		bf.append("  select 0 id,'ȫ��' mingc,-1 fuid  from dual \n");
		
		bf.append("  union \n");
		
		bf.append("  select  dc.id ,dc.mingc,dc.fuid from  (  \n");
		bf.append(" select distinct d.id,d.mingc,0 fuid from jiesb js ,diancxxb d where js.diancxxb_id=d.id and js.shoukdw='"+visit.getDiancqc()+"'\n");
		bf.append("  union \n");
		bf.append("   select distinct d.id,d.mingc,0 fuid from jiesyfb jy,diancxxb d where jy.diancxxb_id=d.id and jy.shoukdw='"+visit.getDiancqc()+"' \n");
		bf.append("  ) dc");
		
		bf.append("   union\n");
		
		bf.append(" select js.id,js.gongysmc mingc,d.id  fuid from jiesb js,diancxxb d where\n");
		bf.append("  js.diancxxb_id=d.id and d.id in  (   \n");
		bf.append("   select distinct d.id from jiesb js ,diancxxb d where js.diancxxb_id=d.id and js.shoukdw='"+visit.getDiancqc()+"'\n");
		bf.append(" union\n");
		bf.append("   select distinct d.id from jiesyfb jy,diancxxb d where jy.diancxxb_id=d.id and jy.shoukdw='"+visit.getDiancqc()+"' \n");
		bf.append(" ) \n");
		
		bf.append("  union\n");
		
		bf.append("  select jy.id,jy.gongysmc mingc,d.id  fuid from jiesyfb jy,diancxxb d where\n");
		bf.append("   jy.diancxxb_id=d.id and d.id in (   \n");
		bf.append("  select distinct d.id from jiesb js ,diancxxb d where js.diancxxb_id=d.id and js.shoukdw='"+visit.getDiancqc()+"'\n");
		bf.append("  union\n");
		bf.append("   select distinct d.id from jiesyfb jy,diancxxb d where jy.diancxxb_id=d.id and jy.shoukdw='"+visit.getDiancqc()+"'\n");
		bf.append(" ) \n");
	
		bf.append("  )  start with id=0  connect by  prior id=  fuid \n");
		bf.append(" order siblings  by fuid ");*/
		
		
		return bf;
	}
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			
			visit.setString14(visit.getActivePageName());
			visit.setActivePageName(getPageName().toString());
			
			setLiucmcValue(null);
			setILiucmcModel(null);
			getILiucmcModels();
			
			
			setLiucmcValue1(null);
			setILiucmcModel1(null);
			getILiucmcModels1();
			
			visit.setboolean3(true);
			
			this.setRiq("");
			this.setRiq1("");
		}
		//�������������ε糧ֵ�ı仯���仯
		if(visit.getboolean3()){
			this.setGongysValue(null);
			this.setGongysModel(null);
			this.getGongysModels();
		}
		init();
	}
	private void init() {
	//	setOriRiq(getRiq());
	//	this.setRiq1(this.getRiq1());
		getSelectData();
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