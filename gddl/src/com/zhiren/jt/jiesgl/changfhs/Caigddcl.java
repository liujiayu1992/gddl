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
import com.zhiren.dc.hesgl.jiesd.Jiesdcz;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*
 * ����:ly
 * ʱ��:2009-05-31
 */
public class Caigddcl extends BasePage implements PageValidateListener {
//	�����û���ʾ
	
	
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
	
	

	protected void initialize() {
		super.initialize(); 
		setMsg("");
		this.setLiucmc("");
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
			s1+=" and m.diancxxb_id="+dianc_id+"\n";
			s2+=" and y.diancxxb_id="+dianc_id+"\n";
		}
		String sql="";
		sql=" select distinct * from (\n" +
				" select -1 id,'ȫ��' mingc from dual\n" +
				" union\n" +
				" select rownum id,gongysmc  from (\n" +
				" select distinct m.gongysmc from kuangfjsmkb m,diancxxb d where m.diancxxb_id=d.id\n" +
				s1+
				" union\n"+
				" select distinct y.gongysmc from kuangfjsyfb y,diancxxb d where y.diancxxb_id=d.id\n" +
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
	
	// ���밴ť����
	private boolean _ImportChick = false;

	public void ImportButton(IRequestCycle cycle) {
		_ImportChick = true;
	}

	// �������۵���ť��
	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	// ˢ�°�ť
	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	// �޸İ�ť
	private boolean _ChangeChick = false;

	public void ChangeButton(IRequestCycle cycle) {
		_ChangeChick = true;
	}

	private boolean _DeleteChick = false;

	public void DeleteButton(IRequestCycle cycle) {
		_DeleteChick = true;
	}

	// �趨Ĭ������
	private boolean _InsertChick = false;

	public void InsertButton(IRequestCycle cycle) {
		_InsertChick = true;
	}

	// �ύ��������
	private boolean _CreateChick = false;

	public void CreateButton(IRequestCycle cycle) {
		_CreateChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_ImportChick) {
			_ImportChick = false;
			// ǰ�����㵥�������
			GotoJiesddr(cycle);
			getSelectData();
		}
		if (_ChangeChick) {
			_ChangeChick = false;
			Xiug(cycle);
			// setOriRiq(getRiq());
			// getSelectData();
		}
		if (_RefurbishChick) {
			_RefurbishChick = false;
			getSelectData();
		}
		if (_DeleteChick) {
			_DeleteChick = false;
			delete();
		} else if (_InsertChick) {
			_InsertChick = false;

		} else if (_CreateChick) {
			_CreateChick = false;
			tij();
		}
		
		if (_SaveChick) {
			_SaveChick = false;
			shengcxsjsd(cycle);
		}
	}

	public void GotoJiesddr(IRequestCycle cycle) {
		// ���㵥����
		cycle.activate("Balancext");
	}
	
	// �������۽��㵥
	public void shengcxsjsd(IRequestCycle cycle) {
		cycle.activate("Xiaosjsdsdsc");
	}

	//�ύ��������
	private void tij(){
		Visit visit=(Visit)this.getPage().getVisit();
		String tis="";
//		System.out.println(this.getLiucmc());
		String liucb_id=MainGlobal.getProperId_String(this.getILiucmcModel(), this.getLiucmc());
		String[] raw1=this.getChange().split(";");
		for(int i=0;i<raw1.length;i++){//������¼
			
			String[] raw2=raw1[i].split(","); //�ֱ��ȡ jiesbid jiesyfbid
			
			String jiesmkbid=raw2[0];
			
			String jiesyfbid="";
			
			if(raw2.length>=2){
				jiesyfbid=raw2[1];
			}
			
			
			if(jiesmkbid!=null && !jiesmkbid.equals("")){
				
				if(jiesyfbid!=null && !jiesyfbid.equals("")){///��Ʊ����
					
					try {
						String lc_id = MainGlobal.getTableCol("kuangfjsmkb", "liucztb_id", "id", jiesmkbid);
						String value = MainGlobal.getTableCol("kuangfjsmkb", "bianm", "id", jiesmkbid);
						if(!lc_id.equals("0")){
							tis+="���:"+value+"&nbsp;&nbsp;"+"�ѽ�������<br>";
							continue;
						}
					} catch (Exception e) {
						// TODO �Զ����� catch ��
						e.printStackTrace();
					}
					
					Liuc.tij("kuangfjsmkb", Long.parseLong(jiesmkbid), visit.getRenyID(), "", Long.parseLong(liucb_id));
					Liuc.tij("kuangfjsyfb", Long.parseLong(jiesyfbid), visit.getRenyID(), "", Long.parseLong(liucb_id));
//					Liuc.tij("diancjsmkb", Long.parseLong(jiesbid), visit.getRenyID(), "");
//					Liuc.tij("diancjsyfb", Long.parseLong(jiesyfbid), visit.getRenyID(), "");
					Jiesdcz.Zijsdlccl("kuangfjsmkb", Long.parseLong(jiesmkbid),  visit.getRenyID(), "", Long.parseLong(liucb_id), "TJ");
					Jiesdcz.Zijsdlccl("kuangfjsyfb", Long.parseLong(jiesyfbid),  visit.getRenyID(), "", Long.parseLong(liucb_id), "TJ");
				}else{//ú�����
//					Liuc.tij("diancjsmkb", Long.parseLong(jiesbid), visit.getRenyID(), "");
					try {
						String lc_id = MainGlobal.getTableCol("kuangfjsmkb", "liucztb_id", "id", jiesmkbid);
						String value = MainGlobal.getTableCol("kuangfjsmkb", "bianm", "id", jiesmkbid);
						if(!lc_id.equals("0")){
							tis+="���:"+value+"&nbsp;&nbsp;"+"�ѽ�������<br>";
							continue;
						}
					} catch (Exception e) {
						// TODO �Զ����� catch ��
						e.printStackTrace();
					}
					Liuc.tij("kuangfjsmkb", Long.parseLong(jiesmkbid), visit.getRenyID(), "", Long.parseLong(liucb_id));
					Jiesdcz.Zijsdlccl("kuangfjsmkb", Long.parseLong(jiesmkbid),  visit.getRenyID(), "", Long.parseLong(liucb_id), "TJ");
				}
			}else{//�˷ѽ���
//				Liuc.tij("diancjsyfb", Long.parseLong(jiesyfbid), visit.getRenyID(), "");
				try {
					String lc_id=MainGlobal.getTableCol("kuangfjsyfb", "liucztb_id", "id", jiesyfbid);
					String value = MainGlobal.getTableCol("kuangfjsyfb", "bianm", "id", jiesyfbid);
					if(!lc_id.equals("0")){
						tis+="���:"+value+"&nbsp;&nbsp;"+"�ѽ�������<br>";
						continue;
					}
				} catch (Exception e) {
					// TODO �Զ����� catch ��
					e.printStackTrace();
				}
				Liuc.tij("kuangfjsyfb", Long.parseLong(jiesyfbid), visit.getRenyID(), "", Long.parseLong(liucb_id));
				Jiesdcz.Zijsdlccl("kuangfjsyfb", Long.parseLong(jiesyfbid),  visit.getRenyID(), "", Long.parseLong(liucb_id), "TJ");
			}
			
			
		}
		
		if(!tis.equals("")){
			this.setMsg(tis);
		}else{
			this.setMsg("�����ɹ�!");
		}
	}

	
	//ֱ�Ӵ�jiesb��jiesyfb��ɾ���������νӵ�  �����¼��糧״̬
	public void delete(){
		Visit visit=(Visit)this.getPage().getVisit();
		JDBCcon con=new JDBCcon();
		ResultSetList rsl=null;
		String[] raw1=this.getChange().split(";");
		String tis="";
		for(int i=0;i<raw1.length;i++){//������¼
			
			String[] raw2=raw1[i].split(","); //�ֱ��ȡ jiesmkbid jiesyfbid
			
			String jiesmkid=raw2[0];
			
			String jiesyfbid="";
			
			if(raw2.length>=2){
				jiesyfbid=raw2[1];
			}
			
			if(jiesmkid!=null && !jiesmkid.equals("")){
				
				if(jiesyfbid!=null && !jiesyfbid.equals("")){///��Ʊ����
					
					
					//��Ʊ����  ֻ���ж�һ�����ruzrq�Ƿ�ǿգ����������ݿ϶�һ�£������ٴ��ж�
					String sql=" select m.ruzrq mrzrq,y.ruzrq yrzrq from kuangfjsmkb m,kuangfjsyfb y where m.id(+)=y.kuangfjsmkb_id and m.id="+jiesmkid;
					
					rsl=con.getResultSetList(sql);
					
					if(rsl.next()){

						String mrzrq=rsl.getString("mrzrq");
						String yrzrq=rsl.getString("yrzrq");
						if((mrzrq!=null && !mrzrq.equals("")) || (yrzrq!=null && !yrzrq.equals(""))){//��һ�����ڷǿգ�������ɾ���ü�¼
							
							String value="";
							try {
								value = MainGlobal.getTableCol("kuangfjsmkb", "bianm", "id", jiesmkid);
							} catch (Exception e) {
								// TODO �Զ����� catch ��
								e.printStackTrace();
							}
							tis+="���:"+value+"&nbsp;&nbsp;"+"���㵥�����ˣ�����ɾ��<br>";
							continue;
						}
						
					}
					
					boolean t=true;
					if(!jiesmkid.equals("0")){
						sql="select lc.leibztb_id\n" +
						"  from liucztb lc\n" + 
						" where lc.id in\n" + 
						"       (select dz.liucztqqid\n" + 
						"          from liucdzb dz\n" + 
						"         where dz.liuczthjid in (select m.liucztb_id\n" + 
						"                                   from kuangfjsmkb m\n" + 
						"                                  where m.id = "+jiesmkid+")\n" + 
						"         and dz.mingc != '����'\n" + 
						"       )\n";
				
						rsl=con.getResultSetList(sql);
						if(rsl.next()){
							if(rsl.getInt("leibztb_id")==0){
								t=true;
							} else {//˵����״̬������ʼ״̬ ��������ɾ��
								String value="";
								try {
									value = MainGlobal.getTableCol("kuangfjsmkb", "bianm", "id", jiesmkid);
								} catch (Exception e) {
									// TODO �Զ����� catch ��
									e.printStackTrace();
								}
								tis+="���:"+value+"&nbsp;&nbsp;"+"���㵥�ѽ������̲��ύ������ɾ��<br>";
							}
							t=false;
						}else{
							t=true;
						}
					}
					
					if(t){
						if(visit.getString10().equals("fgs_cg")){
							sql = " begin \n" +
							" update kuangfjsmkb set zhuangt = 0 where id = "+jiesmkid +";\n"+
							" update kuangfjsyfb set zhuangt = 0 where id = "+jiesyfbid +";\n"+
							" end;";
						} else {
							sql=" begin \n" +
							" delete from kuangfjsmkb where id="+jiesmkid +";\n"+
							" delete from kuangfjsyfb where id="+jiesyfbid +";\n"+
							" end;";
						}
						
						int a=con.getUpdate(sql);
					}
				
				}else{//ú�����
					
					String sql=" select m.ruzrq mrzrq from kuangfjsmkb m where m.id="+jiesmkid;
					
					rsl=con.getResultSetList(sql);
					
					if(rsl.next()){
						
						String mrzrq=rsl.getString("mrzrq");
						
						if(mrzrq!=null && !mrzrq.equals("")){//���ڷǿգ�������ɾ���ü�¼
							
							String value="";
							try {
								value = MainGlobal.getTableCol("kuangfjsmkb", "bianm", "id", jiesmkid);
							} catch (Exception e) {
								// TODO �Զ����� catch ��
								e.printStackTrace();
							}
							tis+="���:"+value+"&nbsp;&nbsp;"+"���㵥�����ˣ�����ɾ��<br>";
							continue;
						}
						
					}
					
					boolean t=true;
					if(!jiesmkid.equals("0")){
						sql="select lc.leibztb_id\n" +
						"  from liucztb lc\n" + 
						" where lc.id in\n" + 
						"       (select dz.liucztqqid\n" + 
						"          from liucdzb dz\n" + 
						"         where dz.liuczthjid in (select m.liucztb_id\n" + 
						"                                   from kuangfjsmkb m\n" + 
						"                                  where m.id = "+jiesmkid+")\n" + 
						"         and dz.mingc != '����'\n" + 
						"       )\n";

						rsl=con.getResultSetList(sql);
						if(rsl.next()){
							if(rsl.getInt("leibztb_id")==0){
								t=true;
							} else {//˵����״̬������ʼ״̬ ��������ɾ��
								t=false;
								String value="";
								try {
									value = MainGlobal.getTableCol("kuangfjsmkb", "bianm", "id", jiesmkid);
								} catch (Exception e) {
									// TODO �Զ����� catch ��
									e.printStackTrace();
								}
								tis+="���:"+value+"&nbsp;&nbsp;"+"���㵥�ѽ������̲��ύ������ɾ��<br>";
							}
						}else{
							t=true;
						}
					}
					
					if(t){
						if(visit.getString10().equals("fgs_cg")){
							sql = " begin \n" +
							" update kuangfjsmkb set zhuangt = 0 where id = "+jiesmkid +";\n"+
							" end;";
						} else {
							sql=" begin \n" +
									" delete from kuangfjsmkb where id="+jiesmkid +";\n"+						
									" end;";
						}
						int a=con.getUpdate(sql);
							
					}
					
					
					
				}
			}else{//�˷ѽ���

			String sql=" select y.ruzrq yrzrq from kuangfjsyfb y where y.id="+jiesyfbid;
				
				rsl=con.getResultSetList(sql);
				
				if(rsl.next()){
					
					String yrzrq=rsl.getString("yrzrq");
					
					if(yrzrq!=null && !yrzrq.equals("")){//���ڷǿգ�������ɾ���ü�¼
						String value="";
						try {
							value = MainGlobal.getTableCol("kuangfjsyfb", "bianm", "id", jiesyfbid);
						} catch (Exception e) {
							// TODO �Զ����� catch ��
							e.printStackTrace();
						}
						tis+="���:"+value+"&nbsp;&nbsp;"+"���㵥�����ˣ�����ɾ��<br>";
						continue;
					}
					
				}
				
				boolean t=true;
				if(!jiesmkid.equals("0")){
					sql="select lc.leibztb_id\n" +
					"  from liucztb lc\n" + 
					" where lc.id in\n" + 
					"       (select dz.liucztqqid\n" + 
					"          from liucdzb dz\n" + 
					"         where dz.liuczthjid in (select y.liucztb_id\n" + 
					"                                   from kuangfjsyfb y\n" + 
					"                                  where y.id = "+jiesyfbid+")\n" + 
					"         and dz.mingc != '����'\n" + 
					"       )\n";
			
					rsl=con.getResultSetList(sql);
					if(rsl.next()){//˵����״̬������ʼ״̬ ��������ɾ��
						if(rsl.getInt("leibztb_id")==0){
							t=true;
						} else {
							t=false;
							String value="";
							try {
								value = MainGlobal.getTableCol("kuangfjsyfb", "bianm", "id", jiesyfbid);
							} catch (Exception e) {
								// TODO �Զ����� catch ��
								e.printStackTrace();
							}
							tis+="���:"+value+"&nbsp;&nbsp;"+"���㵥�ѽ������̲��ύ������ɾ��<br>";
						}
					}else{
						t=true;
					}
				}
				
				if(t){
					if(visit.getString10().equals("fgs_cg")){
						sql = " begin \n" +
						" update kuangfjsyfb set zhuangt = 0 where id = "+jiesyfbid +";\n"+
						" end;";
					} else {
						sql=" begin \n" +
						" delete from kuangfjsyfb where id="+jiesyfbid +";\n"+
						" end;";
					}
						int a=con.getUpdate(sql);	
				}
				
			}
			
			
		}
		
		if(tis.equals("")){
			this.setMsg("�����ɹ�!");
		}else{
			this.setMsg(tis);
		}
		
		this.setChange("");
	}
	
	public void Xiug(IRequestCycle cycle) {
		((Visit) getPage().getVisit()).setString3(this.getChange());
		cycle.activate("Kuangfjs");
	}
	
	private StringBuffer getBaseSql(){
		StringBuffer bf=new StringBuffer();
		String diancxxb_id=this.getTreeid();
		String gysmc=this.getGongysValue().getValue();
		String sql = "";
		String str_m="";
		String str_y="";
		if(diancxxb_id.equals("0")){
			
			if(this.getGongysValue().getStrId().equals("-1")){
				
			}else{
				str_m+=" and m.gongysmc='"+gysmc+"'\n";
				str_y+=" and y.gongysmc='"+gysmc+"'\n";
			}
		}else{
			
			
				str_m+=" and d.id="+diancxxb_id+"\n";
				str_y+=" and d.id="+diancxxb_id+"\n";
			
			if(!this.getGongysValue().getStrId().equals("-1")){
				str_m+=" and m.gongysmc='"+gysmc+"'\n";
				str_y+=" and y.gongysmc='"+gysmc+"'\n";
			}
		}
		
		sql = 
			"select distinct * from\n" +
			"(select m.id as meikjsid,\n" + 
			"       y.id as yunfjsid,\n" + 
			"		'<a style=\"cursor:hand\" onclick=chak('||'\"'||'kuangf,'||m.bianm||'\"'||')>'||m.bianm||'</a>' as jiesbh,\n" +
			"       d.mingc as diancxxb_id,\n" + 
			"       m.gongysmc as gongys,\n" + 
			"       '<a style=\"cursor:hand\" onclick=chak_ht(' || h.id || ')>' || h.hetbh || '</a>' hetbh,\n" + 
			"       f.mingc as jieslx,\n" + 
			"       m.hansdj,\n" + 
			"       m.fengsjj,\n" + 
			"       m.hansmk,\n" + 
			"       y.hansyf,\n" + 
			"       decode(m.liucztb_id,0,'����',1,'����',(select lu.mingc\n" +
			"                    from liucb lu\n" + 
			"                   where lu.id in (select lc.liucb_id\n" + 
			"                                     from liucztb lc\n" + 
			"                                    where lc.id = m.liucztb_id))) liuczt,\n"+
//			"		nvl((select lu.mingc from liucb lu where lu.id  in(\n" +
//			"      		select lc.liucb_id from liucztb lc where lc.id=m.liucztb_id)),'')  liuczt,\n" +
			"		dj.bianm as xiaosdbh\n" +
			"from kuangfjsmkb m,kuangfjsyfb y,diancxxb d,hetb h,feiylbb f,diancjsmkb dj\n" + 
			"where m.diancxxb_id = d.id\n" + 
			"      and m.hetb_id = h.id(+)\n" + 
			"      and m.jieslx = f.id(+)\n" + 
//			"      and zt.liucb_id = lc.id\n" + 
			"      and m.fuid=0	\n" + 
			"      and m.liucztb_id=0\n"+
			"	   and m.diancjsmkb_id = dj.id(+)\n" +
			"      and y.kuangfjsmkb_id(+) = m.id\n" + 
			str_m +
			"	   and m.zhuangt = 1\n" +
			"	   and m.jiesrq >= " + DateUtil.FormatOracleDate(this.getRiq()) +
			"	   and m.jiesrq <= " + DateUtil.FormatOracleDate(this.getRiq1()) + "\n" +
			"union\n" + 
			"select m.id as meikjsid,\n" + 
			"       y.id as yunfjsid,\n" + 
			"		'<a style=\"cursor:hand\" onclick=chak('||'\"'||'kuangf,'||y.bianm||'\"'||')>'||y.bianm||'</a>' as jiesbh,\n" +
//			"       y.bianm as jiesbh,\n" + 
			"       d.mingc as diancxxb_id,\n" + 
			"       y.gongysmc as gongys,\n" + 
			"       '<a style=\"cursor:hand\" onclick=chak_ht(' || h.id || ')>' || h.hetbh || '</a>' hetbh,\n" + 
			"       f.mingc as jieslx,\n" + 
			"       m.hansdj,\n" + 
			"       m.fengsjj,\n" + 
			"       m.hansmk,\n" + 
			"       y.hansyf,\n" +
			"       decode(m.liucztb_id,0,'����',1,'����',(select lu.mingc\n" +
			"                    from liucb lu\n" + 
			"                   where lu.id in (select lc.liucb_id\n" + 
			"                                     from liucztb lc\n" + 
			"                                    where lc.id = m.liucztb_id))) liuczt,\n"+
//			"		nvl((select lu.mingc from liucb lu where lu.id  in(\n" +
//			"      		select lc.liucb_id from liucztb lc where lc.id=y.liucztb_id)),'')  liuczt,\n" +
			"		dj.bianm as xiaosdbh\n" +
			"from kuangfjsmkb m,kuangfjsyfb y,diancxxb d,hetb h,feiylbb f,diancjsmkb dj\n" + 
			"where y.diancxxb_id = d.id\n" + 
			"      and y.hetb_id = h.id(+)\n" + 
			"      and y.jieslx = f.id(+)\n" + 
//			"      and zt.liucb_id = lc.id\n" + 
			"      and y.fuid = 0\n" + 
			"      and m.liucztb_id=0\n"+
			"	   and m.diancjsmkb_id = dj.id(+)\n" +
			"      and y.kuangfjsmkb_id = m.id(+)\n" + 
			"      and y.kuangfjsmkb_id not in (select m.id from kuangfjsmkb m)\n" + 
			str_y +
			"	   and y.zhuangt = 1\n" +
			"	   and y.jiesrq >= " + DateUtil.FormatOracleDate(this.getRiq()) +
			"	   and y.jiesrq <= " + DateUtil.FormatOracleDate(this.getRiq1()) + "\n" +
			")\n" + 
			"order by jiesbh\n";

		bf.append(sql);

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
//		egu.setGridType(ExtGridUtil.Gridstyle_Read);
//		egu.addColumn(1,new GridColumn(GridColumn.ColType_Check));
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.setHeight("bodyHeight");
		egu.getColumn("meikjsid").setHidden(true);
		egu.getColumn("yunfjsid").setHidden(true);
		
		egu.getColumn("jiesbh").setHeader("������");
		egu.getColumn("diancxxb_id").setHeader("�糧����");
		egu.getColumn("gongys").setHeader("��Ӧ��");
		egu.getColumn("hetbh").setHeader("��ͬ���");
		egu.getColumn("jieslx").setHeader("��������");
		egu.getColumn("hansdj").setHeader("��˰����");
		egu.getColumn("fengsjj").setHeader("�ֹ�˾�Ӽ�");
		egu.getColumn("hansmk").setHeader("��˰ú��");
		egu.getColumn("hansyf").setHeader("��˰�˷�");
//		egu.getColumn("chak").setHeader("�鿴");
//		egu.getColumn("chak").editor=null;
//		egu.getColumn("chak").setHidden(true);
		egu.getColumn("liuczt").setHeader("����״̬");
		egu.getColumn("xiaosdbh").setHeader("���۵����");
		
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
				" var obj0 = document.getElementById('diancTree_id');" +
				" obj0.value = cks.id;" +
				" diancTree_text.setValue(cks.text);\n" +
				" diancTree_window.hide();\n" +
				" document.forms[0].submit();}");
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
		
		if(visit.getString10().equals("fgs_cg")){
			String dr="function(){\n" +
			" document.all.item('ImportButton').click();" +
			"\n}";
			GridButton gbt_dr = new GridButton("����",dr);
			gbt_dr.setIcon(SysConstant.Btn_Icon_Insert);
			egu.addTbarBtn(gbt_dr);
		}
		
		if(visit.isDCUser()){
			
//			�糧���û�������ǵ糧�û���ֻ�ܲ鿴�������㵥�������д�ӡ��
			egu.addToolbarItem("{"+new GridButton("ˢ��","function(){document.getElementById('RefurbishButton').click();}").getScript()+"}");
		}else {
//			��˾�����ż��û�

			String gb_fs="function(){\n" +
				" var Mrcd = gridDiv_grid.getSelectionModel().getSelections(); \n" +
				" if( Mrcd==null || Mrcd.length<=0){Ext.Msg.alert('��ʾ��Ϣ','����ѡ���¼�ٽ��в���!');return;}\n" +
				" if(Mrcd.length>1){Ext.Msg.alert('��ʾ��Ϣ','�޸Ĳ���ֻ�����һ���ɹ���!');return;}\n" +
				" document.all.CHANGE.value='';\n" +
				" for(i = 0; i< Mrcd.length; i++){\n" +
				" 		var rc=Mrcd[i]; \n document.all.CHANGE.value+=rc.get('MEIKJSID')+','+rc.get('YUNFJSID')+';';\n" +
				" }\n" +
				" document.all.item('ChangeButton').click();	\n" +
				"}";
			GridButton gbt = new GridButton("�޸�",gb_fs);
			gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
			egu.addTbarBtn(gbt);
			egu.addTbarText("-");
			
			// �������۵�
			GridButton shengc = new GridButton("�������۵�", "function(){ document.Form0.SaveButton.click(); }",
					SysConstant.Btn_Icon_Create);
			egu.addTbarBtn(shengc);
			egu.addTbarText("-");
			
			String gb2_fs="function(){\n" +
					" var Mrcd = gridDiv_grid.getSelectionModel().getSelections(); \n" +
					" if( Mrcd==null || Mrcd.length<=0){Ext.Msg.alert('��ʾ��Ϣ','����ѡ���¼�ٽ��в���!');return;}\n" +
					" Ext.MessageBox.confirm('��Ϣ��ʾ','ˢ�½�����������ĸ��Ľ���������,�Ƿ����?',function(btn){" +
					" 		if (btn == 'yes') {" +
					" 			document.all.CHANGE.value='';\n" +
					" 			for(i = 0; i< Mrcd.length; i++){\n" +
					" 			var rc=Mrcd[i]; document.all.CHANGE.value+=rc.get('MEIKJSID')+','+rc.get('YUNFJSID');\n" +
					" 			if(i!=Mrcd.length-1){ document.all.CHANGE.value+=';';}\n" +
					" 			}\n" +
					" 			document.all.item('DeleteButton').click();\n" +
					" 			Ext.Msg.progress('��ʾ��Ϣ','��ȴ�','���ݼ����С���');\n"+
					" 		}" +
					" 	})" +
					
					"\n}";
			GridButton gbt2 = new GridButton("ɾ��",gb2_fs);
			gbt2.setIcon(SysConstant.Btn_Icon_Delete);
			egu.addTbarBtn(gbt2);
			egu.addTbarText("-");
			
			String gb3_fs="function(){  \n"
				+	"var Mrcd = gridDiv_grid.getSelectionModel().getSelections(); \n"
				+   " if( Mrcd==null || Mrcd.length<=0){Ext.Msg.alert('��ʾ��Ϣ','����ѡ���¼�ٽ��в���!');return;}\n"
				+"   document.all.CHANGE.value='';\n"
				+   " for(i = 0; i< Mrcd.length; i++){\n"
				+   " var rc=Mrcd[i]; "
				+   " if(rc.get('LIUCZT')=='����'){\n"
				+   " }else{\n"
				+   " 		Ext.Msg.alert('��ʾ��Ϣ','�ѽ���������������ύ!');return;\n"
				+   " }\n"
				+   " document.all.CHANGE.value+=rc.get('MEIKJSID')+','+rc.get('YUNFJSID');\n"
				+   " if(i!=Mrcd.length-1){ document.all.CHANGE.value+=';';}\n"
				+   "}\n"
				
				+ " if(!win){	\n" 
				+ "	\tvar form = new Ext.form.FormPanel({	\n" 
				+ " \tbaseCls: 'x-plain',	\n" 		
				+ " \tlabelAlign:'right',	\n" 
				+ " \tdefaultType: 'textfield',	\n"
				+ " \titems: [{		\n"
				+ " \txtype:'fieldset',	\n"
				+ " \ttitle:'��ѡ������',	\n"
				+ " \tautoHeight:false,	\n"
				+ " \theight:220,	\n"
				+ " \titems:[	\n"
				+ " \tlcmccb=new Ext.form.ComboBox({	\n" 
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
				+ " \twin.hide();	\n"
				+ " \tif(lcmccb.getRawValue()=='��ѡ��'){		\n" 
				+ "	\t	alert('��ѡ���������ƣ�');		\n"
				+ " \t}else{" 
				+ " \t\t document.getElementById('TEXT_LIUCMCSELECT_VALUE').value=lcmccb.getRawValue();	\n"
				+ " \t\t document.all.item('CreateButton').click();	\n"
				+" Ext.Msg.progress('��ʾ��Ϣ','��ȴ�','���ݼ����С���');\n"
				+ " \t}	\n"
				+ " \t}	\n"
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
			GridButton gbt3 = new GridButton("�ύ��������",gb3_fs);
			gbt3.setIcon(SysConstant.Btn_Icon_Create);
			egu.addPaging(0);
			egu.addTbarBtn(gbt3);
		}
		
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
	}
	
	
	//��õ糧 ��Ӧ�̵ĵ糧���νṹ��sql
	private StringBuffer getDTsql(){
		
		Visit visit=(Visit)this.getPage().getVisit();
		StringBuffer bf=new StringBuffer();
		
		bf.append(" select distinct * from ( \n");
		bf.append(" select 0 id,'ȫ��' mingc,0 jib from dual\n");
		bf.append(" union\n");
		bf.append(" select d.id,d.mingc,1 jib from kuangfjsmkb m,diancxxb d where m.diancxxb_id=d.id\n");
		bf.append(" union \n");
		bf.append(" select d.id,d.mingc,1 jib from kuangfjsyfb y,diancxxb d where y.diancxxb_id=d.id\n");
		bf.append(" ) \n");
		bf.append(" order by id,mingc\n");
		
		return bf;
	}
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			visit.setString10("");
			String lx = cycle.getRequestContext().getParameter("lx");
			if(lx!=null){
				visit.setString10(lx);
			}
			
			setLiucmcValue(null);
			setILiucmcModel(null);
			getILiucmcModels();
			
			visit.setboolean3(true);
			
			visit.setString3("");	//��ú��˷�id
			
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
