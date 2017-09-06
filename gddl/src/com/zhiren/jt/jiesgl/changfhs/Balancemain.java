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
 * ����:tzf
 * ʱ��:2009-05-27
 * ����:������۶�����������ģ�ʵ�ֵ��룬�༭��ɾ���������ύ�ȵȹ��ܡ�
 */
public class Balancemain extends BasePage implements PageValidateListener {
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
			s1+=" and js.diancxxb_id="+dianc_id+"\n";
			s2+=" and jf.diancxxb_id="+dianc_id+"\n";
		}
		String sql="";
		sql=" select distinct * from (\n" +
				" select -1 id,'ȫ��' mingc from dual\n" +
				" union\n" +
				" select rownum id,gongysmc  from (\n" +
				" select distinct js.gongysmc from diancjsmkb js,diancxxb d where js.diancxxb_id=d.id\n" +
				s1+
				" union\n"+
				" select distinct jf.gongysmc from diancjsyfb jf,diancxxb d  where jf.diancxxb_id=d.id\n" +
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
	
	//���밴ť����
	private boolean _ImportChick = false;
	public void ImportButton(IRequestCycle cycle) {
		_ImportChick = true;
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
	
	//�ύ��������
	
	private boolean _CreateChick=false;
	public void CreateButton(IRequestCycle cycle){
		_CreateChick=true;
	}
	public void submit(IRequestCycle cycle) {
		if (_ImportChick) {
			_ImportChick = false;
//			ǰ�����㵥�������
			GotoJiesddr(cycle);
			getSelectData();
		} else if (_SaveChick) {
			_SaveChick = false;
			getSelectData();
		}else if(_DeleteChick){
			_DeleteChick=false;
			delete();
		}else if(_InsertChick){
			_InsertChick=false;
			
		}else if(_CreateChick){
			_CreateChick=false;
			tij();
		}
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
			
			String jiesbid=raw2[0];
			
			String jiesyfbid="";
			
			if(raw2.length>=2){
				jiesyfbid=raw2[1];
			}
			
			
			if(jiesbid!=null && !jiesbid.equals("")){
				
				if(jiesyfbid!=null && !jiesyfbid.equals("")){///��Ʊ����
					
					try {
						String lc_id=MainGlobal.getTableCol("diancjsmkb", "liucztb_id", "id", jiesbid);
						String value = MainGlobal.getTableCol("diancjsmkb", "bianm", "id", jiesbid);
						if(!lc_id.equals("0")){
							tis+="���:"+value+"&nbsp;&nbsp;"+"�ѽ�������<br>";
							continue;
						}
					} catch (Exception e) {
						// TODO �Զ����� catch ��
						e.printStackTrace();
					}
					
					Liuc.tij("diancjsmkb", Long.parseLong(jiesbid), visit.getRenyID(), "", Long.parseLong(liucb_id));
					Liuc.tij("diancjsyfb", Long.parseLong(jiesyfbid), visit.getRenyID(), "", Long.parseLong(liucb_id));
//					Liuc.tij("diancjsmkb", Long.parseLong(jiesbid), visit.getRenyID(), "");
//					Liuc.tij("diancjsyfb", Long.parseLong(jiesyfbid), visit.getRenyID(), "");
					Jiesdcz.Zijsdlccl("diancjsmkb", Long.parseLong(jiesbid),  visit.getRenyID(), "", Long.parseLong(liucb_id), "TJ");
					Jiesdcz.Zijsdlccl("diancjsyfb", Long.parseLong(jiesyfbid),  visit.getRenyID(), "", Long.parseLong(liucb_id), "TJ");
				}else{//ú�����
//					Liuc.tij("diancjsmkb", Long.parseLong(jiesbid), visit.getRenyID(), "");
					try {
						String lc_id=MainGlobal.getTableCol("diancjsmkb", "liucztb_id", "id", jiesbid);
						String value = MainGlobal.getTableCol("diancjsmkb", "bianm", "id", jiesbid);
						if(!lc_id.equals("0")){
							tis+="���:"+value+"&nbsp;&nbsp;"+"�ѽ�������<br>";
							continue;
						}
					} catch (Exception e) {
						// TODO �Զ����� catch ��
						e.printStackTrace();
					}
					Liuc.tij("diancjsmkb", Long.parseLong(jiesbid), visit.getRenyID(), "", Long.parseLong(liucb_id));
					Jiesdcz.Zijsdlccl("diancjsmkb", Long.parseLong(jiesbid),  visit.getRenyID(), "", Long.parseLong(liucb_id), "TJ");
				}
			}else{//�˷ѽ���
//				Liuc.tij("diancjsyfb", Long.parseLong(jiesyfbid), visit.getRenyID(), "");
				try {
					String lc_id=MainGlobal.getTableCol("diancjsyfb", "liucztb_id", "id", jiesyfbid);
					String value = MainGlobal.getTableCol("diancjsyfb", "bianm", "id", jiesyfbid);
					if(!lc_id.equals("0")){
						tis+="���:"+value+"&nbsp;&nbsp;"+"�ѽ�������<br>";
						continue;
					}
				} catch (Exception e) {
					// TODO �Զ����� catch ��
					e.printStackTrace();
				}
				Liuc.tij("diancjsyfb", Long.parseLong(jiesyfbid), visit.getRenyID(), "", Long.parseLong(liucb_id));
				Jiesdcz.Zijsdlccl("diancjsyfb", Long.parseLong(jiesyfbid),  visit.getRenyID(), "", Long.parseLong(liucb_id), "TJ");
			}
			
			
		}
		
		if(!tis.equals("")){
			this.setMsg(tis);
		}else{
			this.setMsg("�����ɹ�!");
		}
	}

	
	//
	public void delete(){
		
		JDBCcon con=new JDBCcon();
		ResultSetList rsl=null;
		String[] raw1=this.getChange().split(";");
		String tis="";
		for(int i=0;i<raw1.length;i++){//������¼
			
			String[] raw2=raw1[i].split(","); //�ֱ��ȡ jiesbid jiesyfbid
			
			String jiesbid=raw2[0];
			
			String jiesyfbid="";
			
			if(raw2.length>=2){
				jiesyfbid=raw2[1];
			}
			
			if(jiesbid!=null && !jiesbid.equals("")){
				
				if(jiesyfbid!=null && !jiesyfbid.equals("")){///��Ʊ����
					
					
					//��Ʊ����  ֻ���ж�һ�����ruzrq�Ƿ�ǿգ����������ݿ϶�һ�£������ٴ��ж�
					String sql=" select dm.ruzrq dcrzrq,km.ruzrq kfrzrq from diancjsmkb dm,kuangfjsmkb km where km.diancjsmkb_id(+)=dm.id and dm.id="+jiesbid;
					
					
					//------------------------����һ�ű�  ����Ҫ�ж���?
					rsl=con.getResultSetList(sql);
					
					if(rsl.next()){

						String dcrq=rsl.getString("dcrzrq");
						String kfrq=rsl.getString("kfrzrq");
						if((dcrq!=null && !dcrq.equals("")) || (kfrq!=null && !kfrq.equals(""))){//��һ�����ڷǿգ�������ɾ���ü�¼
							
							String value="";
							try {
								value = MainGlobal.getTableCol("diancjsmkb", "bianm", "id", jiesbid);
							} catch (Exception e) {
								// TODO �Զ����� catch ��
								e.printStackTrace();
							}
							tis+="���:"+value+"&nbsp;&nbsp;"+"���㵥�����ˣ�����ɾ��<br>";
							continue;
						}
						
					}
					String lc_id="";
					try {
						 lc_id=MainGlobal.getTableCol("diancjsmkb", "liucztb_id", "id", jiesbid);
					} catch (Exception e1) {
						// TODO �Զ����� catch ��
						e1.printStackTrace();
					}
					boolean t=true;
					if(!lc_id.equals("0")){
						sql=
							"select lc.leibztb_id\n" +
							"  from liucztb lc\n" + 
							" where lc.id in\n" + 
							"       (select dz.liucztqqid\n" + 
							"          from liucdzb dz\n" + 
							"         where dz.liuczthjid in (select dm.liucztb_id\n" + 
							"                                   from diancjsmkb dm\n" + 
							"                                  where dm.id = "+jiesbid+")\n" + 
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
									value = MainGlobal.getTableCol("diancjsmkb", "bianm", "id", jiesbid);
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
						//�����ܽ��㵥
						sql=" begin \n" +
								" delete from diancjsmkb where id="+jiesbid +";\n"+
								" delete from diancjsyfb where id="+jiesyfbid +";\n"+
								" delete from kuangfjsmkb where diancjsmkb_id="+jiesbid +";\n"+
								" delete from kuangfjsyfb where diancjsyfb_id="+jiesyfbid +";\n"+
								" update jiesb set diancjsmkb_id=0 where diancjsmkb_id="+jiesbid+";\n" +
								" update jiesyfb set diancjsyfb_id=0 where diancjsyfb_id="+jiesyfbid+";\n" +
								" end;";
						
							int a=con.getUpdate(sql);
							  //�����ӽ��㵥
							   sql="select * from diancjsmkb where fuid="+jiesbid;
							   rsl=con.getResultSetList(sql);
							   while(rsl.next()){
								   sql="begin \n"+
								       " delete from diancjsmkb where id="+rsl.getLong("id")+";\n"+
								       " delete from kuangfjsmkb where diancjsmkb_id="+rsl.getLong("id")+";\n"+
								       " update jiesb set diancjsmkb_id=0 where diancjsmkb_id="+rsl.getLong("id")+";\n"+
								       " end;";
								   int b=con.getUpdate(sql);
							   }
							   sql = "select * from diancjsyfb where fuid=" + jiesyfbid;
								rsl = con.getResultSetList(sql);
								while (rsl.next()) {
									sql = "begin \n"
											+ " delete from diancjsyfb where id="
											+ rsl.getLong("id")
											+ ";\n"
											+ " delete from kuangfjsyfb where diancjsyfb_id="
											+ rsl.getLong("id")
											+ ";\n"
											+ " update jiesyfb set diancjsyfb_id=0 where diancjsyfb_id="
											+ rsl.getLong("id") + ";\n" + " end;";
									int c = con.getUpdate(sql);
								}
					}
					
				
				}else{//ú�����
					

					
					String sql=" select dm.ruzrq dcrzrq,km.ruzrq kfrzrq from diancjsmkb dm,kuangfjsmkb km where km.diancjsmkb_id(+)=dm.id and dm.id="+jiesbid;
					
					rsl=con.getResultSetList(sql);
					
					if(rsl.next()){
						
						String dcrq=rsl.getString("dcrzrq");
						String kfrq=rsl.getString("kfrzrq");
						
						if((dcrq!=null && !dcrq.equals("")) || (kfrq!=null && !kfrq.equals(""))){//��һ�����ڷǿգ�������ɾ���ü�¼
							
							String value="";
							try {
								value = MainGlobal.getTableCol("diancjsmkb", "bianm", "id", jiesbid);
							} catch (Exception e) {
								// TODO �Զ����� catch ��
								e.printStackTrace();
							}
							tis+="���:"+value+"&nbsp;&nbsp;"+"���㵥�����ˣ�����ɾ��<br>";
							continue;
						}
						
					}
					String lc_id="";
					try {
						 lc_id=MainGlobal.getTableCol("diancjsmkb", "liucztb_id", "id", jiesbid);
					} catch (Exception e1) {
						// TODO �Զ����� catch ��
						e1.printStackTrace();
					}
					boolean t=true;
					if(!lc_id.equals("0")){
						sql="select lc.leibztb_id\n" +
						"  from liucztb lc\n" + 
						" where lc.id in\n" + 
						"       (select dz.liucztqqid\n" + 
						"          from liucdzb dz\n" + 
						"         where dz.liuczthjid in (select dm.liucztb_id\n" + 
						"                                   from diancjsmkb dm\n" + 
						"                                  where dm.id = "+jiesbid+")\n" + 
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
									value = MainGlobal.getTableCol("diancjsmkb", "bianm", "id", jiesbid);
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
					 //�ȴ����ܽ��㵥
						sql=" begin \n" +
								" delete from diancjsmkb where id="+jiesbid +";\n"+						
								" delete from kuangfjsmkb where diancjsmkb_id="+jiesbid +";\n"+			
								" update jiesb set diancjsmkb_id=0 where diancjsmkb_id="+jiesbid+";\n" +
								" end;";
						
							int a=con.getUpdate(sql);
					  //�����ӽ��㵥
					   sql="select * from diancjsmkb where fuid="+jiesbid;
					   rsl=con.getResultSetList(sql);
					   while(rsl.next()){
						   sql="begin \n"+
						       " delete from diancjsmkb where id="+rsl.getLong("id")+";\n"+
						       " delete from kuangfjsmkb where diancjsmkb_id="+rsl.getLong("id")+";\n"+
						       " update jiesb set diancjsmkb_id=0 where diancjsmkb_id="+rsl.getLong("id")+";\n"+
						       " end;";
						   int b=con.getUpdate(sql);
					   }
					}
					
					
					
				}
			}else{//�˷ѽ���

			String sql=" select dy.ruzrq dcrzrq,ky.ruzrq kfrzrq from diancjsyfb dy,kuangfjsyfb ky where ky.diancjsyfb_id(+)=dy.id and dy.id="+jiesyfbid;
				
				rsl=con.getResultSetList(sql);
				
				if(rsl.next()){
					
					String dcrq=rsl.getString("dcrzrq");
					String kfrq=rsl.getString("kfrzrq");
					
					if((dcrq!=null && !dcrq.equals("")) || (kfrq!=null && !kfrq.equals(""))){//��һ�����ڷǿգ�������ɾ���ü�¼
						String value="";
						try {
							value = MainGlobal.getTableCol("diancjsyfb", "bianm", "id", jiesyfbid);
						} catch (Exception e) {
							// TODO �Զ����� catch ��
							e.printStackTrace();
						}
						tis+="���:"+value+"&nbsp;&nbsp;"+"���㵥�����ˣ�����ɾ��<br>";
						continue;
					}
					
				}
				String lc_id="";
				try {
					 lc_id=MainGlobal.getTableCol("diancjsyfb", "liucztb_id", "id", jiesyfbid);
				} catch (Exception e1) {
					// TODO �Զ����� catch ��
					e1.printStackTrace();
				}
				boolean t=true;
				if(!lc_id.equals("0")){
					sql="select lc.leibztb_id\n" +
					"  from liucztb lc\n" + 
					" where lc.id in\n" + 
					"       (select dz.liucztqqid\n" + 
					"          from liucdzb dz\n" + 
					"         where dz.liuczthjid in (select dy.liucztb_id\n" + 
					"                                   from diancjsyfb dy\n" + 
					"                                  where dy.id = "+jiesyfbid+")\n" + 
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
								value = MainGlobal.getTableCol("diancjsyfb", "bianm", "id", jiesyfbid);
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
					//�����ܽ��㵥
					sql = " begin \n"
							+ " delete from diancjsyfb where id="
							+ jiesyfbid
							+ ";\n"
							+ " delete from kuangfjsyfb where diancjsyfb_id="
							+ jiesyfbid
							+ ";\n"
							+ " update jiesyfb set diancjsyfb_id=0 where diancjsyfb_id="
							+ jiesyfbid + ";\n" + " end;";

					int a = con.getUpdate(sql);
					// �����ӽ��㵥
					sql = "select * from diancjsyfb where fuid=" + jiesyfbid;
					rsl = con.getResultSetList(sql);
					while (rsl.next()) {
						sql = "begin \n"
								+ " delete from diancjsyfb where id="
								+ rsl.getLong("id")
								+ ";\n"
								+ " delete from kuangfjsyfb where diancjsyfb_id="
								+ rsl.getLong("id")
								+ ";\n"
								+ " update jiesyfb set diancjsyfb_id=0 where diancjsyfb_id="
								+ rsl.getLong("id") + ";\n" + " end;";
						int b = con.getUpdate(sql);
					}
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
	public void GotoJiesddr(IRequestCycle cycle) {
		
//		���㵥����
		cycle.activate("Balancext");
	}
	
	private StringBuffer getBaseSql(){
		StringBuffer bf=new StringBuffer();
		String diancxxb_id=this.getTreeid();
		String gysmc=this.getGongysValue().getValue();
		
		String str_js="";
		String str_jy="";
		if(diancxxb_id.equals("0")){
			
			if(this.getGongysValue().getStrId().equals("-1")){
				
			}else{
				str_js+=" and js.gongysmc='"+gysmc+"' ";
				str_jy+=" and jy.gongysmc='"+gysmc+"' ";
			}
		}else{
			
			
				str_js+=" and d.id="+diancxxb_id+"";
				str_jy+=" and d.id="+diancxxb_id+" ";
			
			if(!this.getGongysValue().getStrId().equals("-1")){
				str_js+=" and js.gongysmc='"+gysmc+"' ";
				str_jy+=" and jy.gongysmc='"+gysmc+"' ";
			}
		}
		//��Ҫ����  ����״̬������     diancmcjsb����������Ѿ�û����
		bf.append(" select distinct * from( \n");
		
		bf.append(" select js.id jiesbid,jy.id jiesyfbid,'<a style=\"cursor:hand\" onclick=chak('||'\"'||'changf,'||js.bianm||'\"'||')>'||js.bianm||'</a>'  bianh,js.gongysmc gongysmc,d.mingc dianc,'<a style=\"cursor:hand\" onclick=chak_ht(' || ht.id || ')>' || ht.hetbh || '</a>' hetbh,fl.mingc jieslx,js.hansdj hansdj,\n");
		bf.append(" js.fengsjj fengsjj,js.hansmk hansmc,jy.hansyf hansyf,\n");
		bf.append("  '<a style=\"cursor:hand\" onclick=chak('||'\"'||'changf,'||js.bianm||'\"'||')>�鿴</a>' chak, \n");
		bf.append("  decode(js.liucztb_id,0,'����',1,'����',(select lu.mingc from liucb lu where lu.id in (select lc.liucb_id from liucztb lc where lc.id = js.liucztb_id)))  liuczt,\n");
		bf.append("  nvl2((select km.id from kuangfjsmkb km where km.diancjsmkb_id=js.id),'<font color=\"green\">������</font>','<font color=\"red\">δ����</font>')  caigdzt, \n");
		bf.append("  (select m.liucztb_id from kuangfjsmkb m,diancjsmkb dm where dm.id = js.id and m.diancjsmkb_id = dm.id) as zt\n");
		bf.append("  from  diancjsmkb js,diancjsyfb jy,diancxxb d,hetb ht,feiylbb fl where \n");
//		bf.append(" ( js.diancxxb_id=d.id or  jy.diancxxb_id=d.id ) \n");
		bf.append(" ( js.diancxxb_id=d.id ) \n");
		bf.append("  and fl.id(+)=js.jieslx and js.liucztb_id=0 \n");
		bf.append("  and js.hetb_id=ht.id(+) and jy.diancjsmkb_id(+)=js.id\n");
		bf.append("  and js.fuid=0\n");
		bf.append(str_js+" \n");
		bf.append("  and js.jiesrq>=").append(DateUtil.FormatOracleDate(this.getRiq())).append(" and js.jiesrq<=").append(DateUtil.FormatOracleDate(this.getRiq1()));
		
		bf.append("  union\n");
		
		bf.append("  select js.id jiesbid,jy.id jiesyfbid, '<a style=\"cursor:hand\" onclick=chak('||'\"'||'changf,'||jy.bianm||'\"'||')>'||jy.bianm||'</a>' bianh,jy.gongysmc gongysmc,d.mingc dianc,'<a style=\"cursor:hand\" onclick=chak_ht(' || ht.id || ')>' || ht.hetbh || '</a>' hetbh,fl.mingc jieslx,js.hansdj hansdj,\n");
		bf.append("   js.fengsjj fengsjj,js.hansmk hansmc,jy.hansyf hansyf,\n");
		bf.append("  '<a style=\"cursor:hand\" onclick=chak('||'\"'||'changf,'||jy.bianm||'\"'||')>�鿴</a>' chak ,\n");
		bf.append("  decode(js.liucztb_id,0,'����',1,'����',(select lu.mingc from liucb lu where lu.id in (select lc.liucb_id from liucztb lc where lc.id = js.liucztb_id)))  liuczt,\n");
		bf.append("  nvl2((select kf.id from kuangfjsyfb kf where kf.diancjsyfb_id=jy.id),'<font color=\"green\">������</font>','<font color=\"red\">δ����</font>')  caigdzt, \n");
		bf.append("  (select y.liucztb_id from kuangfjsyfb y,diancjsyfb dy where dy.id = jy.id and y.diancjsyfb_id = dy.id) as zt\n");
		bf.append("   from diancjsmkb js,diancjsyfb jy,diancxxb d,hetb ht,feiylbb fl where \n");
//		bf.append(" (  js.diancxxb_id=d.id or jy.diancxxb_id=d.id  )\n");
		bf.append(" (  jy.diancxxb_id=d.id  )\n");
		bf.append("  and fl.id(+)=jy.jieslx and js.liucztb_id=0\n");
		bf.append("  and jy.hetb_id=ht.id(+) and jy.diancjsmkb_id=js.id(+)\n");
		bf.append("  and jy.fuid=0\n");
		bf.append("  and  nvl(jy.diancjsmkb_id,0) not in (select nvl(j.id,null) from diancjsmkb j ) \n");
		bf.append(str_jy+" \n");
		bf.append("  and jy.jiesrq>=").append(DateUtil.FormatOracleDate(this.getRiq())).append(" and jy.jiesrq<=").append(DateUtil.FormatOracleDate(this.getRiq1()));
		
		bf.append("  ) order by bianh\n");
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
		egu.getColumn("jiesbid").setHidden(true);
		egu.getColumn("jiesyfbid").setHidden(true);
		
		egu.getColumn("bianh").setHeader("������");
		egu.getColumn("bianh").setWidth(120);
		egu.getColumn("gongysmc").setHeader("��Ӧ��");
		egu.getColumn("dianc").setHeader("�糧");
		egu.getColumn("hetbh").setHeader("��ͬ���");
		egu.getColumn("jieslx").setHeader("��������");
		egu.getColumn("hansdj").setHeader("ú�˰����");
		egu.getColumn("fengsjj").setHeader("ú��ֹ�˾�Ӽ�");
		egu.getColumn("hansmc").setHeader("��˰ú��");
		egu.getColumn("hansyf").setHeader("��˰�˷�");
		egu.getColumn("chak").setHeader("�鿴");
		egu.getColumn("chak").editor=null;
		egu.getColumn("chak").setHidden(true);
		
		
		egu.getColumn("liuczt").setHeader("����״̬");
		egu.getColumn("caigdzt").setHeader("�ɹ�������״̬");
		
		egu.getColumn("zt").setHeader("��״̬");
		egu.getColumn("zt").editor=null;
		egu.getColumn("zt").setHidden(true);
		
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
		" document.all.item('ImportButton').click();" +
		"\n}";
		GridButton gbt = new GridButton("����",gb_fs);
		gbt.setIcon(SysConstant.Btn_Icon_Insert);
		egu.addTbarBtn(gbt);
		
		
		//¼�밴ť  ���ݾ����������м���˹���
		
//		egu.addTbarText("-");
//		GridButton gbt6 = new GridButton("¼��","");
//		gbt6.setIcon(SysConstant.Btn_Icon_Insert);
//		egu.addTbarBtn(gbt6);
		
		
		
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
//				" document.all.item('DeleteButton').click();\n" +
////				" Ext.Msg.progress('��ʾ��Ϣ','��ȴ�','���ݼ����С���');\n"+
//				" Ext.Msg.show({title: 'Please wait',msg: '���ڼ�������...',progressText: '���ݼ�����...',width:300,progress:true,closable:false});for(var i=0;i<=10;i++)setTimeout(function(){Ext.Msg.updateProgress(i/10,'���ݼ����С���','���ڼ�������');},i*100);"+
//				"\n}";
		GridButton gbt2 = new GridButton("ɾ��",gb2_fs);
		gbt2.setIcon(SysConstant.Btn_Icon_Delete);
		egu.addTbarBtn(gbt2);
		
		egu.addTbarText("-");
		

		
		String gb3_fs="function(){  \n"
			+	"var Mrcd = gridDiv_grid.getSelectionModel().getSelections(); \n"
			+   " if( Mrcd==null || Mrcd.length<=0){\n"
			+   "		Ext.Msg.alert('��ʾ��Ϣ','����ѡ���¼�ٽ��в���!');return;\n" 
			+   " }\n"
			+"   document.all.CHANGE.value='';\n"
			+   " for(i = 0; i< Mrcd.length; i++){\n"
			+   " var rc=Mrcd[i]; \n" 
			+   " if(rc.get('LIUCZT')=='����'){\n"
			+   " }else{\n"
			+   " 		Ext.Msg.alert('��ʾ��Ϣ','�ѽ���������������ύ!');return;\n"
			+   " }\n"
			+   " if(rc.get('ZT')==0){\n"
			+   " 		Ext.Msg.alert('��ʾ��Ϣ','�����ύ�ɹ���!');return;\n"
			+   " }\n"
			+   " document.all.CHANGE.value+=rc.get('JIESBID')+','+rc.get('JIESYFBID');\n"
			+   " if(i!=Mrcd.length-1){ document.all.CHANGE.value+=';';}\n"
			+   "}\n"			
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
//			+" Ext.Msg.progress('��ʾ��Ϣ','��ȴ�','���ݼ����С���');\n"
			+" Ext.Msg.show({title: 'Please wait',msg: '���ڼ�������...',progressText: '���ݼ�����...',width:300,progress:true,closable:false});for(var i=0;i<=10;i++)setTimeout(function(){Ext.Msg.updateProgress(i/10,'���ݼ����С���','���ڼ�������');},i*100);"
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
		bf.append(" select d.id,d.mingc,1 jib from diancjsmkb js,diancxxb d where js.diancxxb_id=d.id\n");
		bf.append(" union \n");
		bf.append(" select d.id,d.mingc,1 jib from diancjsyfb jf,diancxxb d  where jf.diancxxb_id=d.id \n");
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
			visit.setActivePageName(getPageName().toString());
			
			setLiucmcValue(null);
			setILiucmcModel(null);
			getILiucmcModels();
			
			visit.setboolean3(true);
			visit.setString10("");
			
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