package com.zhiren.dc.gdxw.jianj;

import java.sql.PreparedStatement;
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

import com.zhiren.common.CustomMaths;
import com.zhiren.common.DateUtil;
import com.zhiren.common.ErrorMessage;
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
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.dc.huaygl.Caiycl;
import com.zhiren.dc.jilgl.Jilcz;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/**
 * @author wangzongbing
 * @since 2010-12-12 14:27:42
 * @version 1.0
 * @discription ���ڰᵹ��Ƥ
 */
public class Changnbd_hp extends BasePage implements PageValidateListener {
	private final static String Customkey = "Changnbd_hp";
	
	// �����û���ʾ
	private String msg = "";
	private double chelxxb_cheph_piz=0;
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
//	ˢ���������ڰ�
	private String riq;
	public void setRiq(String riq) {
		this.riq = riq;
	}
	public String getRiq() {
		return riq;
	}
	public IDropDownModel getYunsdwModel() {
		Visit visit = (Visit) this.getPage().getVisit();
		String sql = "select id,mingc from yunsdwb where diancxxb_id="
				+ visit.getDiancxxb_id();
		return new IDropDownModel(sql);
	}

	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
//	����
	private boolean _JusChick = false;

	public void JusButton(IRequestCycle cycle) {
		_JusChick = true;
	}






	private boolean _SavePizChick = false;

	public void SavePizButton(IRequestCycle cycle) {
		_SavePizChick = true;
	}


	//������״̬������쳣��������
	private void InsertSuocztb(String chepbtmpb_id,String cheph,double maoz,double piz){
		JDBCcon con = new JDBCcon();
		ResultSetList rs=null;
		Visit visit = (Visit) this.getPage().getVisit();
		long chelxxb_id=0;
		int flag=0;
		String chelxxbSql="select id from chelxxb where cheph='"+cheph+"'";
		 rs = con.getResultSetList(chelxxbSql); 
		 if(rs.next()){
			  chelxxb_id=rs.getLong("id");
		 }
		 
		 //�ж��Ƿ��ظ�����suocztb��
		 String Ischongf="select * from suocztb s where s.chepbtmp_id="+chepbtmpb_id+"";
		 rs = con.getResultSetList(Ischongf); 
		 if(!rs.next()){
			 //��suocztb���������
			 String newid = MainGlobal.getNewID(visit.getDiancxxb_id());
			 String InserSC="insert into suocztb (id,chelxxb_id,suocsj,suocry,suocyy,zt,chepbtmp_id,maoz,piz) values (" +
			 		""+newid+","+chelxxb_id+",sysdate,'"+visit.getRenymc()+"','Ƥ�س�����Χ',1,"+chepbtmpb_id+","+maoz+","+piz+"" +
			 		") ";
			 flag = con.getInsert(InserSC);
				if(flag == -1){
					WriteLog.writeErrorLog(this.getClass().getName() + 
					"\n����suocztb��Ϣʧ��!");
					setMsg(this.getClass().getName() + ":����suocztb��Ϣʧ��!");
					
				}
		 }
		 
		
		con.Close();	
	}
	

	
	private boolean IsChongfgh(String id) {
		boolean ischongfgh = true;
		JDBCcon con = new JDBCcon();
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		sql.append("select  c.qingcsj  from changnbdb c where c.id=");
		sql.append(id).append("");
		ps = con.getPresultSet(sql.toString());
		try {
			rs = ps.executeQuery();
			while (rs.next()) {
				if (rs.getString("qingcsj")==null) {
					ischongfgh = false;
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				ps.close();
				con.Close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return ischongfgh;
	}
	
	private void SavePiz() {
		if (getChange() == null || "".equals(getChange())) {
			setMsg("û�������κθ��ģ�");
			return;
		}
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		ResultSetList rsl = getPizGrid().getModifyResultSet(getChange());
		if (rsl == null) {
			WriteLog
					.writeErrorLog(ErrorMessage.NullResult
							+ "Changnbd_hp �� getExtGrid().getModifyResultSet(getChange()) ����û����ȷ���ؼ�¼����");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		IDropDownModel idm = new IDropDownModel(SysConstant.SQL_Meic);
		IDropDownModel ysdw = new IDropDownModel(SysConstant.SQL_Yunsdw);
		int flag = 0;
		String cheph ="";
		
		
		 
		
		if (rsl.next()) {
			String id=rsl.getString("id");
			 cheph = rsl.getString("cheph");
		
			
			//�ж��Ƿ��ظ�����,��������������������Ѿ�����,�������������Ϊûˢ��,����Ա��ѡ�иó����й������
			if(IsChongfgh(rsl.getString("id"))){
				setMsg("ѡ�񳵺Ŵ���,�ó��Ų�����,��ˢ��!");
				return;
			}
			
	
			
			
			long diancxxb_id = 0;
			if (visit.isFencb()) {
				diancxxb_id = (getExtGrid().getColumn("diancxxb_id").combo)
						.getBeanId(rsl.getString("diancxxb_id"));
			} else {
				diancxxb_id = visit.getDiancxxb_id();
			}
			
			
			
			double piz = rsl.getDouble("piz");

			String sql = "update changnbdb set piz =" + piz + ", cheph = '" + cheph +"',qingchh = '" +
			rsl.getString("qingchh") + "', qingcsj = sysdate, qingcjjy = '" +visit.getRenymc() + "', come_meicb_id = " + 
			idm.getBeanId(rsl.getString("come_meicb_id")) + ",go_meicb_id="+idm.getBeanId(rsl.getString("go_meicb_id"))+",meigy = '" + 
			rsl.getString("meigy") + "',pinz='"+rsl.getString("pinz")+"',yunsdwb_id="+ysdw.getBeanId(rsl.getString("yunsdwb_id"))+"" +
			", daohrq=to_date(to_char(sysdate,'yyyy-mm-dd'), 'yyyy-mm-dd'),shifhp=0 where id = " + id;
			flag = con.getUpdate(sql);
			if (flag == -1) {
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog("���³��ڰᵹ��ʧ��!");
				setMsg("���³��ڰᵹ��ʧ��!");
				return;
			}
	
			if(flag == -1) {
				con.rollBack();
				con.Close();
				return;
			}
	
		}
		con.commit();
		con.Close();

	
	   setMsg("�ᵹƤ�ر���ɹ�");
		
		
	}



	

	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			getSelectData();
		}
		
		if (_SavePizChick) {
			_SavePizChick = false;
			SavePiz();
			init();
		}
		if (_JusChick) {
			_JusChick = false;
			Jus();
			init();
		}
	}

	private void Jus() {

		if (getChange() == null || "".equals(getChange())) {
			setMsg("û�������κθ��ģ�");
			return;
		}
		
		
		String StrTmp[] = getChange().split(",");
		String id=StrTmp[0];
		String cheph=StrTmp[1];
		String lspz=StrTmp[2];
		String meigy=StrTmp[3];
		String come_meicb_id=StrTmp[4];
		String go_meicb_id=StrTmp[5];
		String yunsdwb_id=StrTmp[6];
		String pinz=StrTmp[7];
		
		if(lspz.equals("0")){
			this.setMsg("�ó�û����ʷƤ��,��������<������ʷƤ��>��ʽ��Ƥ!");
			return;
		}

		if(meigy.equals("")){
			this.setMsg("��úԱ����Ϊ��!");
			return;
		}
		
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		IDropDownModel idm = new IDropDownModel(SysConstant.SQL_Meic);
		IDropDownModel ysdw = new IDropDownModel(SysConstant.SQL_Yunsdw);
		int flag = 0;
		//�ж��Ƿ��ظ�����,��������������������Ѿ�����,�������������Ϊûˢ��,����Ա��ѡ�иó����й������
			if(IsChongfgh(id)){
				setMsg("ѡ�񳵺Ŵ���,�ó��Ų�����,��ˢ��!");
				return;
			}
			
			

			String sql = "update changnbdb set piz =" + lspz + ", cheph = '" + cheph +"',qingchh = '" +
			"', qingcsj = sysdate, qingcjjy = '" +visit.getRenymc() + "', come_meicb_id = " + 
			idm.getBeanId(come_meicb_id) + ",go_meicb_id="+idm.getBeanId(go_meicb_id)+",meigy = '" + 
			meigy + "',pinz='"+pinz+"',yunsdwb_id="+ysdw.getBeanId(yunsdwb_id)+"" +
			", daohrq=to_date(to_char(sysdate,'yyyy-mm-dd'), 'yyyy-mm-dd'),shifhp=1 where id = " + id;
			flag = con.getUpdate(sql);
			if (flag == -1) {
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog("���³��ڰᵹ��ʧ��!");
				setMsg("���³��ڰᵹ��ʧ��!");
				return;
			}
	
		
	
		
		con.commit();
		con.Close();

	
	   setMsg("�ᵹƤ�ر���ɹ�");
		
		
	}


	public void setChepid(String fahids) {
		((Visit) this.getPage().getVisit()).setString1(fahids);
	}



	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		ResultSetList rsl;

		
		
/*  Ƥ��grid��ʼ��  */
		
		String sql="select bd.id,bd.cheph,bd.maoz,bd.piz,nvl(get_changnbd_lispz(bd.cheph),0)  as lspz,meigy,mc1.mingc as\n" +
			" come_meicb_id,mc2.mingc as go_meicb_id,\n" + 
			"to_char(bd.zhongcsj,'yyyy-mm-dd hh24:mi:ss') as zhongcsj,ys.mingc as\n" + 
			" yunsdwb_id,bd.pinz,bd.zhongcjjy,bd.zhongchh,bd.qingchh\n" + 
			"from changnbdb bd,meicb mc1,meicb mc2,yunsdwb ys\n" + 
			"where bd.come_meicb_id=mc1.id(+)\n" + 
			"and bd.go_meicb_id=mc2.id(+)\n" + 
			"and bd.yunsdwb_id=ys.id(+)\n" + 
			"and bd.qingcsj is null\n" + 
			"order by bd.id";

		
		rsl = con.getResultSetList(sql);
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "��������SQL:" + sql);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu1 = new ExtGridUtil("gridDivPiz", rsl);
//		����ҳ��ɱ༭
		egu1.setGridType(ExtGridUtil.Gridstyle_Edit);
//		����ҳ����
		egu1.setWidth(Locale.Grid_DefaultWidth);
//		���ø�grid���з�ҳ
		egu1.addPaging(100);
//		��������
		egu1.getColumn("cheph").setHeader(Locale.cheph_chepb);
		egu1.getColumn("maoz").setHeader(Locale.maoz_chepb);
		egu1.getColumn("piz").setHeader(Locale.piz_chepb);
		egu1.getColumn("lspz").setHeader("��ʷƤ��");
		egu1.getColumn("lspz").setHidden(true);
		egu1.getColumn("meigy").setHeader("��úԱ");
		egu1.getColumn("come_meicb_id").setHeader("ú̿��Դ");
		egu1.getColumn("go_meicb_id").setCenterHeader("жú�ص�");
		egu1.getColumn("zhongcsj").setCenterHeader("�س�ʱ��");
		egu1.getColumn("yunsdwb_id").setHeader("���䵥λ");
		egu1.getColumn("pinz").setHeader("Ʒ��");
		egu1.getColumn("zhongcjjy").setHeader("�غ�Ա");
		egu1.getColumn("zhongchh").setHeader("�غ��");
		egu1.getColumn("qingchh").setHeader("�պ��");
	
		
//		�����п�

		egu1.getColumn("cheph").setWidth(110);
		egu1.getColumn("maoz").setWidth(70);
		egu1.getColumn("piz").setWidth(70);
		egu1.getColumn("meigy").setWidth(70);
		egu1.getColumn("come_meicb_id").setWidth(140);
		egu1.getColumn("go_meicb_id").setWidth(140);
		egu1.getColumn("zhongcsj").setWidth(180);
		egu1.getColumn("yunsdwb_id").setWidth(90);
		egu1.getColumn("pinz").setWidth(70);
		egu1.getColumn("zhongcjjy").setWidth(70);
		egu1.getColumn("zhongchh").setWidth(70);
		egu1.getColumn("qingchh").setWidth(70);
		
//		����������
		sql = "select * from shuzhlfwb where leib ='����' and mingc = '������Ƥ��' and diancxxb_id = "
			+ visit.getDiancxxb_id();
		rsl = con.getResultSetList(sql);
		if (rsl.next()) {
			egu1.getColumn("piz").editor.setMinValue(rsl.getString("xiax"));
			egu1.getColumn("piz").editor.setMaxValue(rsl.getString("shangx"));
		}
//		�������Ƿ�ɱ༭

		egu1.getColumn("maoz").setEditor(null);
		egu1.getColumn("zhongcsj").setEditor(null);
		egu1.getColumn("zhongcjjy").setEditor(null);
		egu1.getColumn("zhongchh").setEditor(null);
		egu1.getColumn("qingchh").setEditor(null);
		egu1.getColumn("lspz").setEditor(null);
//		����ú̿��Դ������
		ComboBox cmc= new ComboBox();
		egu1.getColumn("come_meicb_id").setEditor(cmc); 
		cmc.setEditable(true);
		String mcSql="select id,piny || '-' ||mingc from meicb order by xuh";
		egu1.getColumn("come_meicb_id").setComboEditor(egu1.gridId, new
		IDropDownModel(mcSql));
//		����ú̿ȥ��������
		ComboBox qxcmc= new ComboBox();
		egu1.getColumn("go_meicb_id").setEditor(qxcmc); 
		qxcmc.setEditable(true);
		String qxmcSql="select id,piny || '-' ||mingc from meicb order by xuh";
		egu1.getColumn("go_meicb_id").setComboEditor(egu1.gridId, new
		IDropDownModel(qxmcSql));
//      ��úԱ��Ӧ��ú��������
		ComboBox ymz= new ComboBox();
		egu1.getColumn("meigy").setEditor(ymz);
		ymz.setEditable(true);
		String ymy = "select id ,zhiw from renyxxb r where r.bum='��úԱ' order by r.zhiw";
		egu1.getColumn("meigy").setComboEditor(egu1.gridId,new IDropDownModel(ymy));
//		���䵥λ
		ComboBox yuns= new ComboBox();
		egu1.getColumn("yunsdwb_id").setEditor(yuns); 
		yuns.setEditable(true);
		String yunsSql="select id,mingc from yunsdwb";
		egu1.getColumn("yunsdwb_id").setComboEditor(egu1.gridId, new
		IDropDownModel(yunsSql));
		
//		Ʒ��
		ComboBox pinzmc= new ComboBox();
		egu1.getColumn("pinz").setEditor(pinzmc); 
		pinzmc.setEditable(true);
		String pinzSql=" select id,mingc from pinzb  where leib='ú'";
		egu1.getColumn("pinz").setComboEditor(egu1.gridId, new
		IDropDownModel(pinzSql));
		

		// ���복�ſ��Բ鵽ģ����Ӧ����Ϣ��-----------------------------------------------------------
		egu1.addTbarText("���복�ţ�");
		TextField theKey = new TextField();
		theKey.setId("theKey");
		theKey.setWidth(110);
		theKey.setListeners("change:function(thi,newva,oldva){  sta='';},specialkey:function(thi,e){if (e.getKey()==13) {chaxun();}}\n");
		egu1.addToolbarItem(theKey.getScript());
		// ����ext�еĵڶ���egu�����д���gridDiv�����ı������ȵ�һ����Piz������gridDiv----gridDivPiz.
		GridButton chazhao = new GridButton("(ģ��)����",
				"function(){chaxun();}");
		chazhao.setIcon(SysConstant.Btn_Icon_Search);
		egu1.addTbarBtn(chazhao);
		egu1.addTbarText("-");

		String otherscript = "var sta=''; function chaxun(){\n"
				+ "       if(theKey.getValue().toString()==''){Ext.MessageBox.alert('��ʾ','������ֵ');return;}\n"
				+ "       var len=gridDivPiz_data.length;\n"
				+ "       var count;\n"
				+ "       if(len%"
				+ egu1.getPagSize()
				+ "!=0){\n"
				+ "        count=parseInt(len/"
				+ egu1.getPagSize()
				+ ")+1;\n"
				+ "        }else{\n"
				+ "          count=len/"
				+ egu1.getPagSize()
				+ ";\n"
				+ "        }\n"
				+ "        for(var i=0;i<count;i++){\n"
				+ "           gridDivPiz_ds.load({params:{start:i*"
				+ egu1.getPagSize()
				+ ", limit:"
				+ egu1.getPagSize()
				+ "}});\n"
				+ "           var rec=gridDivPiz_ds.getRange();\n "
				+ "           for(var j=0;j<rec.length;j++){\n "
				+ "               if(rec[j].get('CHEPH').toString().indexOf(theKey.getValue().toString())!=-1){\n"
				+ "                 var nw=[rec[j]]\n"
				+ "                  if(sta.indexOf(rec[j].get('ID').toString())==-1){\n"
				+ "                      gridDivPiz_sm.selectRecords(nw);\n"
				+ "                      sta+=rec[j].get('ID').toString()+';';\n"
				+ "                       return;\n" + "                  }\n"
				+ "                \n" + "               }\n"
				+ "           }\n" + "        }\n" + "        if(sta==''){\n"
				+ "          Ext.MessageBox.alert('��ʾ','��Ҫ�ҵĳ��Ų�����');\n"
				+ "        }else{\n" + "           sta='';\n"
				+ "           Ext.MessageBox.alert('��ʾ','�����Ѿ�����β');\n"
				+ "         }\n" + "}\n";

		egu1.addOtherScript(otherscript);
		GridButton refurbish = new GridButton("ˢ��",
				"function (){document.getElementById('RefurbishButton').click();}");
		refurbish.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu1.addTbarBtn(refurbish);
		egu1.addTbarText("-");
		
		GridButton gbs_pz = new GridButton("����Ƥ��",GridButton.ButtonType_Save,
				"gridDivPiz", egu1.getGridColumns(), "SavePizButton");
		egu1.addTbarBtn(gbs_pz);
		egu1.addOtherScript("function  gridDivPiz_save(rec){if(confirm('�Ƿ񱣴� ����: '+rec.get('CHEPH') + '  Ƥ��: ' + rec.get('PIZ'))){return \"\";}else{return \"return\";}};");
		egu1.addTbarText("-");
		
		egu1.addOtherScript("gridDivPiz_grid.on('rowclick',function(){Mode='sel';DataIndex='PIZ';});");
		egu1.addOtherScript("gridDivPiz_grid.on('beforeedit',function(e){ "
				+"if(e.row!=row_maoz_index){ e.cancel=true; } \n"
				+"if(e.field == 'PIZ'){e.cancel=true;}});\n");
		

		egu1.addTbarText("->");// ���÷ָ���
		
		
		egu1.addTbarText("��ʷƤ�أ�");
		TextField Lispiz = new TextField();
		Lispiz.setId("ls");
		Lispiz.setWidth(80);
		egu1.addToolbarItem(Lispiz.getScript());
		egu1.addTbarText("-");// ���÷ָ���
		
		
//		����
		egu1.addTbarText("-");// ���÷ָ���
		String sPwHandler = "function(){"
			+"if(gridDivPiz_sm.getSelected() == null){"
			+"	 Ext.MessageBox.alert('��ʾ��Ϣ','��ѡ�г���!');"
			+"	 return;"
			+"}"
			+"var grid_rcd = gridDivPiz_sm.getSelected();"
		
			+"Ext.MessageBox.confirm('��ʾ��Ϣ','������ʷƤ�ر��泵��::&nbsp;&nbsp;&nbsp;'+ grid_rcd.get('CHEPH')+'&nbsp;&nbsp;&nbsp;Ƥ��:&nbsp;&nbsp;&nbsp;'+grid_rcd.get('LSPZ')+'&nbsp;&nbsp;&nbsp;ȷ����?&nbsp;&nbsp;&nbsp;',function(btn){"
			+"	 if(btn == 'yes'){"
			+"		    grid_history = grid_rcd.get('ID')+','+grid_rcd.get('CHEPH')+','+grid_rcd.get('LSPZ')+','+grid_rcd.get('MEIGY')+','+grid_rcd.get('COME_MEICB_ID')+','+grid_rcd.get('GO_MEICB_ID')+','+grid_rcd.get('YUNSDWB_ID')+','+grid_rcd.get('PINZ');"
			+"			var Cobj = document.getElementById('CHANGE');"
			+"			Cobj.value = grid_history;"
			+"			document.getElementById('JusButton').click();"
			+"	       	}"
			+"	  })"
			+"}";
		egu1.addTbarBtn(new GridButton("������ʷƤ��",sPwHandler));
		egu1.addTbarText("-");// ���÷ָ���
		
		
	
		
		
		
		egu1.addOtherScript(" gridDivPiz_grid.on('cellclick',function(grid,rowIndex,columnIndex,e){ \n " +
				" if(columnIndex==2){ \n" +
				" row_maoz_index=rowIndex;\n"+
				" theKey.setValue(gridDivPiz_grid.getStore().getAt(rowIndex).get('CHEPH'));\n"+
				" ls.setValue(gridDivPiz_grid.getStore().getAt(rowIndex).get('LSPZ'));\n"+
				" sta='';\n"+
				" gridDivPiz_grid.getView().refresh();\n"+
				" gridDivPiz_grid.getView().getRow(rowIndex).style.backgroundColor=\"red\";} \n"+
				" else { \n" +
//				" gridDiv_grid.getView().focusRow(row_maoz_index); \n" +
				" } \n"+
				" }); \n");
		
		egu1.addOtherScript(" gridDivPiz_grid.addListener('afteredit',function(e){\n " +
				" gridDivPiz_grid.getView().getRow(e.row).style.backgroundColor=\"red\"; \n" +
				"} ); \n");
		
		setPizGrid(egu1);
		
		
		
		
		
/*  Ƥ�ر������grid��ʼ��  */
		 
		 sql="select rownum as xuh,a.* from (\n" +
			 "select bd.id,bd.cheph,bd.maoz,bd.piz ,(bd.maoz-bd.piz) as jingz, meigy,mc1.mingc as\n" + 
			 " come_meicb_id,mc2.mingc as go_meicb_id,\n" + 
			 "to_char(bd.zhongcsj,'yyyy-mm-dd hh24:mi:ss') as zhongcsj,\n" + 
			 "to_char(bd.qingcsj,'yyyy-mm-dd hh24:mi:ss') as qingcsj,\n" + 
			 "ys.mingc as yunsdwb_id,bd.pinz,bd.zhongcjjy,bd.qingcjjy,bd.zhongchh,\n" + 
			 "bd.qingchh\n" + 
			 "from changnbdb bd,meicb mc1,meicb mc2,yunsdwb ys\n" + 
			 "where bd.come_meicb_id=mc1.id(+)\n" + 
			 "and bd.go_meicb_id=mc2.id(+)\n" + 
			 "and bd.yunsdwb_id=ys.id(+)\n" + 
			 "and bd.qingcsj is not null\n" + 
			 "and to_char(bd.qingcsj,'yyyy-mm-dd')='"+this.getRiq()+"'\n" + 
			 "order by bd.qingcsj )a order by xuh desc";

		rsl = con.getResultSetList(sql);
//		���û��ȡ��Ĭ������
		if(rsl == null){
			
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
//		����GRID���ɱ༭
		egu.setGridType(ExtGridUtil.Gridstyle_Read);
		// ����ҳ����
		egu.setWidth(Locale.Grid_DefaultWidth);
//		���ø�grid�����з�ҳ
		egu.addPaging(0);
//		��������
		egu.getColumn("id").setHeader("id");
		egu.getColumn("xuh").setHeader("���");
		egu.getColumn("cheph").setHeader(Locale.cheph_chepb);
		egu.getColumn("maoz").setHeader(Locale.maoz_chepb);
		egu.getColumn("piz").setHeader(Locale.piz_chepb);
		egu.getColumn("jingz").setHeader(Locale.jingz_chepb);
		egu.getColumn("meigy").setHeader("��úԱ");
		egu.getColumn("come_meicb_id").setHeader("ú̿��Դ");
		egu.getColumn("go_meicb_id").setHeader("жú�ص�");
		egu.getColumn("zhongcsj").setHeader("����ʱ��");
		egu.getColumn("qingcsj").setHeader("����ʱ��");
		egu.getColumn("yunsdwb_id").setHeader("���䵥λ");
		egu.getColumn("pinz").setHeader("Ʒ��");
		egu.getColumn("zhongcjjy").setHeader("�غ�Ա");
		egu.getColumn("qingcjjy").setHeader("�պ�Ա");
		egu.getColumn("zhongchh").setHeader("�غ��");
		egu.getColumn("qingchh").setHeader("�պ��");
//		�����п�
		egu.getColumn("xuh").setWidth(55);
		egu.getColumn("cheph").setWidth(110);
		egu.getColumn("maoz").setWidth(70);
		egu.getColumn("piz").setWidth(70);
		egu.getColumn("jingz").setWidth(70);
		egu.getColumn("meigy").setWidth(70);
		egu.getColumn("come_meicb_id").setWidth(130);
		egu.getColumn("go_meicb_id").setWidth(130);
		egu.getColumn("zhongcsj").setWidth(180);
		egu.getColumn("qingcsj").setWidth(180);
		egu.getColumn("yunsdwb_id").setWidth(130);
		egu.getColumn("zhongcjjy").setWidth(80);
		egu.getColumn("qingcjjy").setWidth(80);
		egu.getColumn("pinz").setWidth(70);
		egu.getColumn("zhongchh").setWidth(70);
		egu.getColumn("qingchh").setWidth(70);
		
//		�������Ƿ�����
//		egu.getColumn("id").setHidden(true);

		
		int zhongc=0;
		int kongc=0;
		double jingz=0.0;
		int jus=0;
		
		//���ذ�����
		sql="select count(*) as zhongc from changnbdb c\n" +
			"where c.zhongcsj>=to_date('"+this.getRiq()+"','yyyy-mm-dd')\n" + 
			"and c.zhongcsj<to_date('"+this.getRiq()+"','yyyy-mm-dd')+1 \n"+
			"and c.maoz>0\n" + 
			"and c.zhongcsj is not null";
		rsl = con.getResultSetList(sql);
		if (rsl.next()){
			zhongc=rsl.getInt("zhongc");
		}
		//��Ƥ����,����
		sql="select count(*) as huip,sum(c.maoz-c.piz) as jingz from changnbdb c\n" +
			"where c.qingcsj>=to_date('"+this.getRiq()+"','yyyy-mm-dd')\n" + 
			"and c.qingcsj<to_date('"+this.getRiq()+"','yyyy-mm-dd')+1 \n"+
			"and c.piz>0\n" + 
			"and c.qingcsj is not null";
		rsl = con.getResultSetList(sql);
		if (rsl.next()){
			kongc=rsl.getInt("huip");
			jingz=rsl.getDouble("jingz");
		}
		
		DateField dfRIQ = new DateField();
		dfRIQ.Binding("RIQ", "");
		dfRIQ.setValue(getRiq());
		egu.addToolbarItem(dfRIQ.getScript());
		egu.addTbarText("-");
		egu.addTbarText("-");
		GridButton refurbish1 = new GridButton("ˢ��",
		"function (){document.getElementById('RefurbishButton').click();}");
		refurbish1.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(refurbish1);
		egu.addTbarText("-");
		egu.addTbarText("&nbsp;&nbsp;" +
				""+this.getRiq()+":&nbsp;" +
				"&nbsp;�ᵹ����:"+zhongc+" ��,&nbsp;" +
				"����:"+kongc+" ��,&nbsp;&nbsp;�ᵹ��ú:"+jingz+"��");
		
		egu.addOtherScript(" if(gridDiv_ds.getCount()>=1){ gridDiv_grid.getView().getRow(0).style.backgroundColor=\"red\"; }\n");
		setExtGrid(egu);
		
		con.Close();
	}

	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}

	public ExtGridUtil getPizGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid2();
	}

	public void setPizGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid2(extgrid);
	}

	public String getGridScript() {
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getGridScript();
	}

	public String getGridScriptPiz() {
		if (getPizGrid() == null) {
			return "";
		}
		return getPizGrid().getGridScript();
	}

	public String getGridHtml() {
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getHtml();
	}
	public String getGridPizHtml() {
		if (getPizGrid() == null) {
			return "";
		}
		return getPizGrid().getHtml();
	}

	public DefaultTree getDefaultTree() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree();
	}

	public void setDefaultTree(DefaultTree dftree1) {
		((Visit) this.getPage().getVisit()).setDefaultTree(dftree1);
	}

	public String getTreeScript() {
		if(getDefaultTree()==null){
			return "";
		}
		// System.out.print(((Visit)
		// this.getPage().getVisit()).getDefaultTree().getScript());
		return getDefaultTree().getScript();
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

	public IPropertySelectionModel getGongysModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel1() == null) {
			setGongysModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel1();
	}

	public void setGongysModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel1(_value);
	}

	public void setGongysModels() {
		String sql = "select id,mingc from gongysb order by xuh,mingc";
		setGongysModel(new IDropDownModel(sql));
	}

	public IPropertySelectionModel getMeikModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel2() == null) {
			setMeikModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel2();
	}

	public void setMeikModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel2(_value);
	}

	public void setMeikModels() {
		String sql = "select id,mingc from meikxxb order by xuh,mingc";
		setMeikModel(new IDropDownModel(sql));
	}

	private void setJianjdmodel() {
		JDBCcon con = new JDBCcon();
		String sql = "select zhi from xitxxb where mingc='�������ﵥģʽ' and zhuangt = 1 and diancxxb_id ="
				+ ((Visit) this.getPage().getVisit()).getDiancxxb_id();
		ResultSetList rsl = con.getResultSetList(sql);
		String model = "";
		if (rsl.next()) {
			model = rsl.getString("zhi");
		}
		rsl.close();
		((Visit) this.getPage().getVisit()).setString15(model);
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			setRiq(DateUtil.FormatDate(new Date()));
			visit.setString1(null);
			init();
		}
	}

	private void init() {
		setGongysModels();
		setMeikModels();
		setJianjdmodel();
		setExtGrid(null);
		setPizGrid(null);
		setDefaultTree(null);
		getSelectData();
	}
}