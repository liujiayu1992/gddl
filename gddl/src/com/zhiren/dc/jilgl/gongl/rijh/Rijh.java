package com.zhiren.dc.jilgl.gongl.rijh;

import java.sql.ResultSet;
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
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.dc.chengbgl.Chengbjs;
import com.zhiren.dc.jilgl.Jilcz;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*
 * 2009-02-19
 * ����
 * �޸�RijhΪҳ��Ĭ�ϲ������޸��ռƻ���ѡ��ƻ��ھ�
 */
public class Rijh extends BasePage implements PageValidateListener {
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

	// �ƻ�����
	private String jihrq;

	public String getJihrq() {
		return jihrq;
	}

	public void setJihrq(String riq) {
		this.jihrq = riq;
	}

	public void setVJihrq(String sRiq) {
		((Visit) getPage().getVisit()).setString1(sRiq);
	}

	public String getVJihrq() {
		if(((Visit)this.getPage().getVisit()).getString1()== null){
			setVJihrq(DateUtil.FormatDate(DateUtil.AddDate(new Date(), 0,
					DateUtil.AddType_intDay)));
		}
		return ((Visit)this.getPage().getVisit()).getString1();
	}

	public String getQRiq(String ss) {
		if (ss == null || ss.equals("")) {
			String riq = getVJihrq();
			return riq.substring(0, 4) + "��" + riq.substring(5, 7) + "��"
					+ riq.substring(8, 10) + "��";
		} else {
			return ss.substring(0, 4) + "��" + ss.substring(5, 7) + "��"
					+ ss.substring(8, 10) + "��";
		}
	}

	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
	//�ռƻ�id,���䵥λid
	public String getParameters() {
		
		return ((Visit) getPage().getVisit()).getString2();
	}

	public void setParameters(String value) {
		
		((Visit) getPage().getVisit()).setString2(value);
	}

	private boolean _RefreshChick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}

	private boolean _FenpchChick = false;

	public void FenpchButton(IRequestCycle cycle) {
		_FenpchChick = true;
	}
	
	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_RefreshChick) {
			_RefreshChick = false;
			setVJihrq(getJihrq());
//			getSelectData();
		} else if (_SaveChick) {
			_SaveChick = false;
			Save();
			
		} else if (_FenpchChick){
			
			_FenpchChick=false;
			GotoChelfp(cycle);
		}
	}
	
	 private void GotoChelfp(IRequestCycle cycle) {
		// TODO �Զ����ɷ������
		
		 	long Diancxxb_id=0;
			if(((Visit) getPage().getVisit()).isFencb()){
				if(getChangbValue().getId()==-1){
					
					setMsg("��ѡ��һ���糧��");
					return;
				}else{
					
					Diancxxb_id=this.getChangbValue().getId();
				}
			}else{
				
				Diancxxb_id=((Visit) getPage().getVisit()).getDiancxxb_id();
			}
			
//			�糧��Ϣ��id
			((Visit) getPage().getVisit()).setLong1(Diancxxb_id);
			
//			�ռƻ���id
			((Visit) getPage().getVisit()).setLong2(Long.parseLong(this.getParameters().substring(0,this.getParameters().indexOf(','))));
			
//			���䵥λ��id
			((Visit) getPage().getVisit()).setLong3(MainGlobal.getProperId(this.getYunsdwModel(),this.getParameters().substring(this.getParameters().indexOf(',')+1,this.getParameters().lastIndexOf(','))));
			
//			�ռƻ�����
			((Visit) getPage().getVisit()).setString3(this.getParameters().substring(this.getParameters().lastIndexOf(',')+1));
			
			cycle.activate("Chehfp");
	}

	private void Save() {
		 
		 Visit visit = (Visit) this.getPage().getVisit();
		 Save1(getChange(), visit);
	 }
	 
	 private void Save1(String strchange, Visit visit){
		 
		JDBCcon con = new JDBCcon();
		String tableName="qicrjhb";
		con.setAutoCommit(false);
		long Diancxxb_id=0;
		boolean Flage=false;	//������insert����
		boolean Flage_Xgysdw=false;	//�޸����䵥λ(ɾ�����䵥λƥ���¼)
		if(((Visit) getPage().getVisit()).isFencb()){
			
			if(getChangbValue().getId()==-1){
				
				this.setMsg("��ѡ��һ���糧");
				return;
			}else{
				
				Diancxxb_id=this.getChangbValue().getId();
			}
		}else{
			
			Diancxxb_id=((Visit) getPage().getVisit()).getDiancxxb_id();
		}
		
		StringBuffer sql = new StringBuffer("begin \n");
		
		ResultSetList delrsl = this.getExtGrid().getDeleteResultSet(strchange);
		while (delrsl.next()) {
			sql.append("delete from ").append(tableName).append(" where id =")
					.append(delrsl.getString(0)).append(";\n");
//			ɾ���ռƻ���������qicrjhcpb���м�¼
			sql.append("delete from ").append("qicrjhcpb").append(" where qicrjhb_id =")
					.append(delrsl.getString(0)).append(";\n");
		}
		delrsl.close();
		ResultSetList mdrsl = getExtGrid().getModifyResultSet(strchange);
		while (mdrsl.next()) {
			
			int flg = Jilcz.Hetkzjm(con, visit.getDiancxxb_id(),
					MainGlobal.getProperId(this.getMeikdwModel(), mdrsl.getString("MEIKXXB_ID")), this.getVJihrq(),SysConstant.YUNSFS_QIY);
			if (flg == -1) {
				con.rollBack();
				con.Close();
//				WriteLog.writeErrorLog(ErrorMessage.Yundlr002);
				setMsg("��ú��λû�ж�Ӧ�ĺ�ͬ���������ͬ������¼��!");
				return;
			}
			
			StringBuffer sql2 = new StringBuffer();
			sql2.append("getnewid(").append(visit.getDiancxxb_id()).append(")")
				.append(",").append(Diancxxb_id).append(",").append("to_date('"+this.getVJihrq()+"','yyyy-MM-dd')");
			if ("0".equals(mdrsl.getString("ID"))) {
				Flage=true;
				sql.append("insert into ").append(tableName).append("(id,diancxxb_id,riq");
				for (int i = 1; i < mdrsl.getColumnCount(); i++) {
					
					if(mdrsl.getColumnNames()[i].equals("MINGC")){
//						���������һ�����Ƿ��ظ�
						if(checkMingc(this.getVJihrq(),mdrsl.getString(i),"0")){
							
							setMsg("��<"+i+">�мƻ������ظ�,����ʧ�ܣ�");
							return;
						}else{
							sql.append(",").append(mdrsl.getColumnNames()[i]);
							sql2.append(",").append(
									getExtGrid().getValueSql(getExtGrid().getColumn(mdrsl.getColumnNames()[i]),
											mdrsl.getString(i)));
						}
					}else if(mdrsl.getColumnNames()[i].equals("GONGYS_ID")
								||mdrsl.getColumnNames()[i].equals("MEIKXXB_ID")
								||mdrsl.getColumnNames()[i].equals("YUNSDWB_ID_BAK")){
							
							if(mdrsl.getColumnNames()[i].equals("GONGYS_ID")){
								
								sql.append(",").append(mdrsl.getColumnNames()[i]);
								sql2.append(",").append(MainGlobal.getProperId(this.getGongysModel(), mdrsl.getString(i)));
							}else if(mdrsl.getColumnNames()[i].equals("MEIKXXB_ID")){
								
								sql.append(",").append(mdrsl.getColumnNames()[i]);
								sql2.append(",").append(MainGlobal.getProperId(this.getMeikdwModel(), mdrsl.getString(i)));
							}else if(mdrsl.getColumnNames()[i].equals("YUNSDWB_ID_BAK")){
								
//								break;
							}
					}else{
							
							sql.append(",").append(mdrsl.getColumnNames()[i]);
							sql2.append(",").append(
								getExtGrid().getValueSql(getExtGrid().getColumn(mdrsl.getColumnNames()[i]),
										mdrsl.getString(i)));
					}
				}
				sql.append(") values(").append(sql2).append(");\n");
			} else {
//				�����߼�:������zhilb_id
				sql.append("update ").append(tableName).append(" set ");
				String stryunsdw="";
				for (int i = 1; i < mdrsl.getColumnCount(); i++) {
					
					if(!mdrsl.getColumnNames()[i].equals("ZHILB_ID")){
						
						if(mdrsl.getColumnNames()[i].equals("MINGC")){
//							���������һ�����Ƿ��ظ�
							if(checkMingc(this.getVJihrq(),mdrsl.getString(i),mdrsl.getString("ID"))){
								
								setMsg("��<"+i+">�мƻ������ظ�,����ʧ�ܣ�");
								return;
							}else{
								sql.append(mdrsl.getColumnNames()[i]).append(" = ");
								
								sql.append(getExtGrid().getValueSql(getExtGrid().getColumn(mdrsl.getColumnNames()[i]),
										mdrsl.getString(i))).append(",");
							}
						
						}else if(mdrsl.getColumnNames()[i].equals("GONGYS_ID")
								||mdrsl.getColumnNames()[i].equals("MEIKXXB_ID")
								||mdrsl.getColumnNames()[i].equals("YUNSDWB_ID_BAK")
								||mdrsl.getColumnNames()[i].equals("YUNSDWB_ID")){
							
							if(mdrsl.getColumnNames()[i].equals("GONGYS_ID")){
								
								sql.append(mdrsl.getColumnNames()[i]).append(" = ");
								sql.append(MainGlobal.getProperId(this.getGongysModel(), mdrsl.getString(i))).append(",");
							}else if(mdrsl.getColumnNames()[i].equals("MEIKXXB_ID")){
								
								sql.append(mdrsl.getColumnNames()[i]).append(" = ");
								sql.append(MainGlobal.getProperId(this.getMeikdwModel(), mdrsl.getString(i))).append(",");
							}else if(mdrsl.getColumnNames()[i].equals("YUNSDWB_ID_BAK")){
								
								if(stryunsdw.equals(mdrsl.getString(i))){
									
//									break;
								}else{
									Flage_Xgysdw=true;
									
								}
							}else if(mdrsl.getColumnNames()[i].equals("YUNSDWB_ID")){
								
								stryunsdw=mdrsl.getString(i);
								
								sql.append(mdrsl.getColumnNames()[i]).append(" = ");
								sql.append(getExtGrid().getValueSql(getExtGrid().getColumn(mdrsl.getColumnNames()[i]),
										mdrsl.getString(i))).append(",");
							}
							
						}else{
							
							sql.append(mdrsl.getColumnNames()[i]).append(" = ");
							sql.append(getExtGrid().getValueSql(getExtGrid().getColumn(mdrsl.getColumnNames()[i]),
									mdrsl.getString(i))).append(",");
						}
					}
				}
				sql.deleteCharAt(sql.length() - 1);
				sql.append(" where id =").append(mdrsl.getString("ID")).append(
						";\n");
				
				if(Flage_Xgysdw){
					
					sql.append("delete from ").append("qicrjhcpb").append(" where qicrjhb_id =")
						.append(mdrsl.getString("ID")).append(";\n");
				}
			}
		}
		mdrsl.close();
		sql.append("end;");
		if(con.getUpdate(sql.toString())>=0){
			
			if(((Visit) getPage().getVisit()).getboolean1()&&Flage){
				
//				con.commit();
//				���ɲ������
				if(Jilcz.updateRijhzl(con, Diancxxb_id, SysConstant.YUNSFS_QIY, null)>-1){
					
					con.commit();
					setMsg("����ɹ���");
					getSelectData();
				}else{
					
					con.rollBack();
					setMsg(""+Locale.caiybm_caiyb+"����ʧ�ܣ�");
				}
			}else{
				
				con.commit();
				setMsg("����ɹ���");
				getSelectData();
			}
			
		}else{
			con.rollBack();
			setMsg("����ʧ�ܣ�");
		}
		con.Close();
	 }

	private boolean checkMingc(String riq, String mingc,String id) {
		// TODO �Զ����ɷ������
//		���������һ�����Ƿ��ظ�
		JDBCcon con=new JDBCcon();
		try{
			
			String sql="select * from qicrjhb where riq=to_date('"+riq+"','yyyy-MM-dd') and mingc='"+mingc+"' and id<>"+id;
			ResultSet rs=con.getResultSet(sql);
			if(rs.next()){
				
				return true;
			}
			rs.close();
		}catch(Exception e){
			
			e.printStackTrace();
		}finally{
			
			con.Close();
		}
		
		return false;
	}

	public void getSelectData() {
		JDBCcon con = new JDBCcon();
		// Visit visit = ((Visit) getPage().getVisit());
		long Diancxxb_id=0;
		if(((Visit) getPage().getVisit()).isFencb()){
			
			if(getChangbValue().getId()==-1){
				
				this.setMsg("��ѡ��һ���糧");
				return;
			}else{
				
				Diancxxb_id=this.getChangbValue().getId();
			}
			
		}else{
			
			Diancxxb_id=((Visit) getPage().getVisit()).getDiancxxb_id();
		}
		
		if(MainGlobal.getXitxx_item("����", "�ռƻ����ɲ���", String.valueOf(Diancxxb_id), "��").equals("��")){
			
//			�ռƻ����ɲ���
			((Visit) getPage().getVisit()).setboolean1(true);
		}
		String rq = DateUtil.FormatOracleDate(new Date());
		if(getVJihrq() != null){
			rq = DateUtil.FormatOracleDate(getVJihrq());
		}
		
		StringBuffer sb = new StringBuffer();
//		sb.append(" select jh.id, jh.mingc, gys.mingc as gongys_id,mk.mingc as meikxxb_id,pz.mingc as pinz_id,kj.mingc as kouj_id,	\n");
//		sb.append(" 	ysdw.mingc as yunsdwb_id,ysdw.mingc as yunsdwb_id_bak, nvl(zl.bianm,'0') as zhilb_id, jh.chec, jh.jihl, jh.beiz, nvl('"
//				+ ((Visit) getPage().getVisit()).getRenymc() + "','') as caozy	\n");
//		sb.append("	from qicrjhb jh,  gongysb gys,  meikxxb mk, pinzb pz,											\n");
//		sb.append("		jihkjb  kj,  yunsdwb ysdw, (select caiyb.zhilb_id as id,zhuanmb.bianm						\n"); 
//		sb.append("           							from zhuanmb,zhuanmlb,yangpdhb,caiyb						\n");
//		sb.append("   								where  zhuanmb.zhuanmlb_id=zhuanmlb.id and zhuanmlb.jib=1		\n");
//		sb.append("          							and yangpdhb.caiyb_id=caiyb.id 								\n");
//		sb.append("          							and zhuanmb.zhillsb_id=yangpdhb.zhilblsb_id) zl				\n");
//		sb.append("	 where jh.gongys_id=gys.id and jh.meikxxb_id=mk.id and jh.pinz_id=pz.id							\n");
//		sb.append("	  and jh.kouj_id=kj.id and jh.yunsdwb_id=ysdw.id and jh.zhilb_id=zl.id(+)						\n");
//		sb.append("	  and jh.diancxxb_id="+Diancxxb_id+" and jh.riq=to_date('"+this.getVJihrq()+"','yyyy-MM-dd')		");
		
		sb.append(" select jh.id, jh.mingc, gys.mingc as gongys_id,mk.mingc as meikxxb_id,kj.mingc as kouj_id,pz.mingc as pinz_id,	\n");
		sb.append(" 	ysdw.mingc as yunsdwb_id,ysdw.mingc as yunsdwb_id_bak, nvl(getqicrjh_caiybh(nvl(jh.zhilb_id,0)),0) as zhilb_id, jh.chec, jh.jihl, jh.beiz, nvl('"
				+ ((Visit) getPage().getVisit()).getRenymc() + "','') as caozy	\n");
		sb.append("	from qicrjhb jh,  gongysb gys,  meikxxb mk, pinzb pz,											\n");
		sb.append("		jihkjb  kj,  yunsdwb ysdw																	\n");
		sb.append("	 where jh.gongys_id=gys.id and jh.meikxxb_id=mk.id and jh.pinz_id=pz.id							\n");
		sb.append("	  and jh.kouj_id=kj.id and jh.yunsdwb_id=ysdw.id 												\n");
		sb.append("	  and jh.diancxxb_id="+Diancxxb_id+" and jh.riq="+rq+"");
		
		ResultSetList rsl = con.getResultSetList(sb.toString());
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "��������SQL:" + sb);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.setHeight("bodyHeight");
		
//		��������
		egu.getColumn("mingc").setHeader("�ƻ�����");
		egu.getColumn("gongys_id").setHeader(Locale.gongysb_id_fahb);
		egu.getColumn("meikxxb_id").setHeader(Locale.meikxxb_id_fahb);
		egu.getColumn("pinz_id").setHeader(Locale.pinzb_id_fahb);
		egu.getColumn("kouj_id").setHeader(Locale.jihkjb_id_fahb);
		egu.getColumn("yunsdwb_id").setHeader(Locale.yunsdw_id_chepb);
		egu.getColumn("chec").setHeader(Locale.chec_fahb);
		egu.getColumn("jihl").setHeader("�ƻ�ú��");
		egu.getColumn("beiz").setHeader(Locale.beiz_fahb);
		egu.getColumn("caozy").setHeader("����Ա");
		
//		����������
		egu.getColumn("id").setHidden(true);
		egu.getColumn("zhilb_id").setHidden(true);
		egu.getColumn("yunsdwb_id_bak").setHidden(true);
		
//		���ò��ɱ༭��
		egu.getColumn("gongys_id").setEditor(null);
		egu.getColumn("meikxxb_id").setEditor(null);
		egu.getColumn("pinz_id").setEditor(null);
		egu.getColumn("kouj_id").setEditor(null);
		egu.getColumn("yunsdwb_id").setEditor(null);
		egu.getColumn("caozy").setEditor(null);
		egu.getColumn("zhilb_id").setEditor(null);
		egu.getColumn("yunsdwb_id_bak").setEditor(null);
		
//		����Ĭ��ֵ
		egu.getColumn("zhilb_id").setDefaultValue("0");
		egu.getColumn("caozy").setDefaultValue(((Visit) getPage().getVisit()).getRenymc());
		egu.getColumn("jihl").setDefaultValue("0");
		
//		�����п�
		egu.getColumn("pinz_id").setWidth(70);
		egu.getColumn("kouj_id").setWidth(80);
		egu.getColumn("jihl").setWidth(80);
		egu.getColumn("caozy").setWidth(60);
		egu.getColumn("chec").setWidth(60);
		egu.getColumn("gongys_id").setWidth(110);
		egu.getColumn("yunsdwb_id").setWidth(100);
		
		if(((Visit) getPage().getVisit()).getboolean1()){
			
			egu.getColumn("zhilb_id").setHeader(Locale.caiybm_caiyb);
			egu.getColumn("zhilb_id").setHidden(false);
		}
		
//		������
		DefaultTree dt = new DefaultTree(DefaultTree.tree_gys_mk_kj,
				"gongysTree", "" + Diancxxb_id, null, null, null);
		Toolbar bbar = dt.getTree().getTreePanel().getBbar();
		bbar.deleteItem();
		StringBuffer handler = new StringBuffer();
		handler.append("function() { \n")
		.append("var cks = gongysTree_treePanel.getSelectionModel().getSelectedNode(); \n")
		.append("if(cks==null){gongysTree_window.hide();return;} \n")
		.append("if(cks.getDepth() < 3){ \n")
		.append("Ext.MessageBox.alert('��ʾ��Ϣ','��ѡ���Ӧ�ļƻ��ھ���');")
		.append("return; } \n")
		.append("rec = gridDiv_grid.getSelectionModel().getSelected(); \n")
		.append("rec.set('GONGYS_ID', cks.parentNode.parentNode.text); \n")
		.append("rec.set('MEIKXXB_ID', cks.parentNode.text); rec.set('KOUJ_ID', cks.text);\n")
		.append("gongysTree_window.hide(); \n")
		.append("return;")
		.append("}");
		ToolbarButton btn = new ToolbarButton(null, "ȷ��", handler.toString());
		bbar.addItem(btn);
		((Visit) getPage().getVisit()).setDefaultTree(dt);
		
//		����������
		egu.getColumn("pinz_id").setEditor(new ComboBox());
		egu.getColumn("pinz_id").setComboEditor(egu.gridId,
				new IDropDownModel(" select id,mingc from pinzb  where leib='ú' order by mingc"));
		egu.getColumn("pinz_id").setReturnId(true);
		egu.getColumn("pinz_id").setDefaultValue(egu.getColumn("pinz_id").combo.getLabel(0));
		
		egu.getColumn("kouj_id").setEditor(new ComboBox());
		egu.getColumn("kouj_id").setComboEditor(egu.gridId,
				new IDropDownModel(" select id,mingc from jihkjb order by mingc"));
		egu.getColumn("kouj_id").setReturnId(true);
		egu.getColumn("kouj_id").setDefaultValue(egu.getColumn("kouj_id").combo.getLabel(0));
		
		egu.getColumn("yunsdwb_id").setEditor(new ComboBox());
		egu.getColumn("yunsdwb_id").setComboEditor(egu.gridId,
				new IDropDownModel(" select id,mingc from yunsdwb order by mingc"));
		egu.getColumn("yunsdwb_id").setReturnId(true);
		egu.getColumn("yunsdwb_id").setDefaultValue(egu.getColumn("yunsdwb_id").combo.getLabel(0));

		
//		����Toolbar����		
		egu.addTbarText("�ƻ�����:");
		DateField dStart = new DateField();
		dStart.Binding("JIHRQ", "");
		dStart.setValue(this.getVJihrq());
		egu.addToolbarItem(dStart.getScript());
		egu.addTbarText("&nbsp&nbsp");
		
		if(((Visit) getPage().getVisit()).isFencb()){	//�ֳ���
			
			egu.addTbarText(Locale.diancxxb_id_fahb+":");
			ComboBox Changb = new ComboBox();
			Changb.setTransform("ChangbDropDown");
			Changb.setId("Changb");
			Changb.setEditable(false);
			Changb.setLazyRender(true);// ��̬��
			Changb.setWidth(80);
			Changb.setReadOnly(true);
			egu.addToolbarItem(Changb.getScript());
			egu.addOtherScript("Changb.on('select',function(){document.forms[0].submit();});");
		}
		
		egu.addTbarText("-");
//		����Toolbar��ť	
		GridButton gRefresh = new GridButton("ˢ��",
				"function(){document.getElementById('RefreshButton').click();}");
		gRefresh.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gRefresh);
		// StringBuffer sShoucHandler= new StringBuffer();
		// sShoucHandler.append("function(){Ext.MessageBox.confirm('��ʾ��Ϣ','�Ƿ����"
		// + getQRiq(getSRiq())+ "�ճ�������',")
		// .append("function(btn){if (btn == 'yes') {")
		// .append("document.getElementById('ShoucButton').click();")
		// .append("}}")
		// .append(")}");
		String ButtonText="";
		if(((Visit) getPage().getVisit()).getString4().equals("Tzclzt")){
//			��������״̬
			egu.setGridType(ExtGridUtil.Gridstyle_Read);
			ButtonText="��������״̬";
		}else if(((Visit) getPage().getVisit()).getString4().equals("Rjh")){
//			�����ƻ�ά��

//			������
			egu.addOtherScript("gridDiv_grid.on('cellclick',function(own, irow, icol, e){ "
					+ "row = irow; \n"
					+ "if('GONGYS_ID' == gridDiv_grid.getColumnModel().getDataIndex(icol)){"
					+ "gongysTree_window.show();}});\n");
			
			
			egu.setGridType(ExtGridUtil.Gridstyle_Edit);
			sb.setLength(0);
			sb.append(" var jihmc='';	\n")
				.append(" var j=0;		\n")
				.append(" var rec = gridDiv_ds.getRange(); 	\n")
				.append(" for(i = 0; i< rec.length; i++){	\n")
				.append("	jihmc=rec[i].get('MINGC');	\n")
				.append("	for(j=i+1;j<rec.length;j++){	\n")
				.append("		if(jihmc==rec[j].get('MINGC')){		\n")
				.append("			Ext.MessageBox.alert('��ʾ��Ϣ','��<'+(i+1)+'>�����ƺ͵�<'+(j+1)+'>�������ظ�');")	
				.append("			return;		\n")	
				.append("		}")
				.append(" 	}")
				.append(" }");
				
			egu.addTbarText("-");
			egu.addToolbarButton(GridButton.ButtonType_Insert, "");
			egu.addTbarText("-");
			egu.addToolbarButton(GridButton.ButtonType_Delete, "");
			egu.addTbarText("-");
			egu.addToolbarButton(GridButton.ButtonType_Save_condition, "SaveButton",sb.toString());
			ButtonText="���䳵��";
		}

		String str2=
			" if(gridDiv_sm.getSelections().length <= 0 " 
		    + "|| gridDiv_sm.getSelections().length > 1){ \n"
		    + " 	Ext.MessageBox.alert('��ʾ��Ϣ','��ѡ��һ���ƻ�!'); \n"
		    + " 	return;"
	        +"}"
	        + " var rec = gridDiv_sm.getSelected(); \n"
	        + " if(rec.get('ID') == 0){ \n"
		    + " 	Ext.MessageBox.alert('��ʾ��Ϣ','�ڷ��䳵��֮ǰ���ȱ���!'); \n"
		    + "  	return;"
	        +"}"
	        +" gridDiv_history = rec.get('ID')+','+rec.get('YUNSDWB_ID')+','+rec.get('MINGC');	\n"
	        +" document.getElementById('PARAMETERS').value=gridDiv_history; \n"
	        +" document.getElementById('FenpchButton').click(); \n";
		egu.addTbarText("-");
        egu.addToolbarItem("{"+new GridButton(""+ButtonText+"","function(){"+str2+"}").getScript()+"}");
//		grid ���㷽��
		egu.addOtherScript("gridDiv_grid.on('afteredit',MsgChangYunsdw);");
		
		setExtGrid(egu);
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
		// if(getTbmsg()!=null) {
		// getExtGrid().addToolbarItem("'->'");
		// getExtGrid().addToolbarItem("'<marquee width=300
		// scrollamount=2>"+getTbmsg()+"</marquee>'");
		// }
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getHtml();
	}
	
	public String getTreeScript() {
		// System.out.print(((Visit)
		// this.getPage().getVisit()).getDefaultTree().getScript());
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
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

	// ����
	public IDropDownBean getChangbValue() {

		if (((Visit) getPage().getVisit()).getDropDownBean1() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean1((IDropDownBean) getChangbModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean1();
	}

	public void setChangbValue(IDropDownBean Value) {

		if (Value != ((Visit) getPage().getVisit()).getDropDownBean1()) {

			((Visit) getPage().getVisit()).setDropDownBean1(Value);
		}
	}

	public IPropertySelectionModel getChangbModel() {

		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {

			getChangbModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public void setChangbModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel1(value);
	}

	public IPropertySelectionModel getChangbModels() {

		String sql = "select id,mingc from diancxxb d where d.fuid="
				+ ((Visit) getPage().getVisit()).getDiancxxb_id()
				+ "order by mingc";

		((Visit) getPage().getVisit())
				.setProSelectionModel1(new IDropDownModel(sql,"��ѡ��"));
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}
	
	//��Ӧ��Model
	
	public IPropertySelectionModel getGongysModel() {

		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {

			getGongysModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public void setGongysModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel2(value);
	}

	public IPropertySelectionModel getGongysModels() {
		
		long Diancxxb_id=0;
		if(((Visit) getPage().getVisit()).isFencb()){
				
			Diancxxb_id=this.getChangbValue().getId();
			
		}else{
			
			Diancxxb_id=((Visit) getPage().getVisit()).getDiancxxb_id();
		}

		String sql = "select g.id,g.mingc from gongysb g,gongysdcglb l where 	\n"
				+ " l.gongysb_id=g.id and l.diancxxb_id=	\n"
				+ Diancxxb_id
				+ "order by g.mingc";

		((Visit) getPage().getVisit())
				.setProSelectionModel2(new IDropDownModel(sql));
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}
	
	//��Ӧ��Model_end
	
//	ú��Model
	
	public IPropertySelectionModel getMeikdwModel() {

		if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {

			getMeikdwModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}

	public void setMeikdwModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel3(value);
	}

	public IPropertySelectionModel getMeikdwModels() {
		
		long Diancxxb_id=0;
		if(((Visit) getPage().getVisit()).isFencb()){
				
			Diancxxb_id=this.getChangbValue().getId();
			
		}else{
			
			Diancxxb_id=((Visit) getPage().getVisit()).getDiancxxb_id();
		}

		String sql = "select m.id,m.mingc from gongysb g,meikxxb m,gongysdcglb lg,gongysmkglb lm where g.id=lg.gongysb_id	\n"
				+ " and lm.gongysb_id=g.id and lm.meikxxb_id=m.id and lg.diancxxb_id=	\n"
				+ Diancxxb_id
				+ "order by m.mingc";

		((Visit) getPage().getVisit())
				.setProSelectionModel3(new IDropDownModel(sql));
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}
	
//	ú��Model_end
	
//	���䵥λModel
	public IPropertySelectionModel getYunsdwModel() {

		if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {

			getYunsdwModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}

	public void setYunsdwModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel4(value);
	}

	public IPropertySelectionModel getYunsdwModels() {
		
		long Diancxxb_id=0;
		if(((Visit) getPage().getVisit()).isFencb()){
				
			Diancxxb_id=this.getChangbValue().getId();
			
		}else{
			
			Diancxxb_id=((Visit) getPage().getVisit()).getDiancxxb_id();
		}

		String sql = "select id,mingc from yunsdwb where yunsdwb.diancxxb_id=	\n"
				+ Diancxxb_id
				+ "order by mingc";

		((Visit) getPage().getVisit())
				.setProSelectionModel4(new IDropDownModel(sql));
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}
	
//	���䵥λModel_end
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		
		String lx = cycle.getRequestContext().getParameter("lx");
		if(lx != null) {
			visit.setString4(lx);
        }
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			if(lx==null || "".equals(lx)){
				visit.setString4("Rjh");
			}
			init();
			if(!visit.getActivePageName().toString().equalsIgnoreCase("Chehfp")){
				setVJihrq(null);
			}
//		�õ�Pageҳ����ʱ�Ĳ���
			visit.setActivePageName(this.getPageName().toString());
		}
		getSelectData();
	}
	
	

	private void init() {
//		else{
//			setJihrq(DateUtil.FormatDate(new Date())); // ҳ������
//			setVJihrq(getJihrq()); 								//visit�е�����String1
//		}
		((Visit) getPage().getVisit()).setDefaultTree(null);
		if(((Visit) getPage().getVisit()).isFencb()){
//			�ֳ���
			setChangbValue(null);	//1
			setChangbModel(null);	//1
			getChangbModels();
		}
		((Visit) getPage().getVisit()).setboolean1(false);	//�ռƻ����ɲ���
		((Visit) getPage().getVisit()).setLong1(0);			//�糧��Ϣ��id
		((Visit) getPage().getVisit()).setLong2(0);			//�ռƻ�id
		((Visit) getPage().getVisit()).setLong3(0);			//���䵥λ��id
		((Visit) getPage().getVisit()).setString2("");		//ҳ�洫�ݲ���
		((Visit) getPage().getVisit()).setString3("");		//�ƻ����ƣ�Chehfpҳ��ʹ�ã�
//		((Visit) getPage().getVisit()).setString4("");		//��Pageҳ����ʱ�Ĳ���,������ΪRjh��Tzclzt
		
		setGongysModel(null);		//2
		getGongysModels();			//2
		
		setMeikdwModel(null);		//3
		getMeikdwModels();			//3
		
		setYunsdwModel(null);		//4
		getYunsdwModels();			//4
	}
}
