package com.zhiren.jt.zdt.monthreport.niandhtzxqk;

import com.zhiren.common.*;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*
* ʱ�䣺2010-05-06
* ���ߣ� ll
* �޸����ݣ�  ���������������ܡ�
*/
/* 
* ʱ�䣺2010-6-13
* ���ߣ� sy
* �޸����ݣ�ά��ҳ�����ά���ĳɰ���ά��
*          ���Ӹ������°�ť
*          �޸���������ǰ��ɾ������û�мӵ糧��Ϣ��������ֻҪ���ɻ�����е�������ɾ��
*          ��Ӧ�������˵�����ֻ��ʾ���걾�糧�±���yuetjkjb�����еĹ�Ӧ��
* 		   
*/ 
public class Niandhtzxqk extends BasePage implements PageValidateListener {
//	�����û���ʾ
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);;
	}
	protected void initialize() {
		super.initialize();
		setMsg("");
		setTbmsg(null); 
	}
	private String tbmsg;
	public String getTbmsg() {
		return tbmsg;
	}
	public void setTbmsg(String tbmsg) {
		this.tbmsg = tbmsg;
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
		Visit visit = (Visit) this.getPage().getVisit();
		visit.getExtGrid1().Save(getChange(), visit);
	}
		
	private boolean _SaveClick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveClick = true;
	}

	private boolean _Refreshclick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_Refreshclick = true;
	}
	
	private boolean _CreateClick = false;
	
	public void CreateButton(IRequestCycle cycle) {
		_CreateClick = true;
	}

	private boolean _CopyClick = false;

	public void CopyButton(IRequestCycle cycle) {
		_CopyClick = true;
	} 

	public void submit(IRequestCycle cycle) {
//		Visit visit = (Visit) this.getPage().getVisit();
		if (_SaveClick) {
			_SaveClick = false;
			Save();
			getSelectData(null);
		}
		if (_Refreshclick) {
			_Refreshclick = false;
			setRiq();
			getSelectData(null);
		}
		if (_CreateClick) {
			_CreateClick = false;
			CreateData();
			getSelectData(null);
		}
		if(_CopyClick){
			_CopyClick=false;
			CopyData();
			getSelectData(null);
		}
	}
	
	public void CopyData(){
		Visit visit =(Visit)getPage().getVisit();
		JDBCcon cn=new JDBCcon();
		cn.setAutoCommit(false);
		String CurrsDate=getNianf()+"-"+getYuef();
		String CurrZnDate=getNianf()+"��"+getYuef()+"��";
		String strSql="";
		String strdel="";
		int flag;
		StringBuffer sql=new StringBuffer("begin \n");
		strSql="select n.id,n.diancxxb_id,n.gongysb_id,n.beiz,n.riq,n.hetl,n.hetjzrz,n.hetjzckjg,n.hetjzdcjg,n.hetzjkhbz,n.yansfs " +
				"from niandhtzxqkb n where n.diancxxb_id="+visit.getDiancxxb_id()+" and n.riq=add_months((to_date('"+CurrsDate+"-01','yyyy-mm-dd')),-1) \n";
		ResultSetList rs=cn.getResultSetList(strSql);
		
		if (rs == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "��������SQL:" + strSql);
			setMsg(ErrorMessage.NullResult);
			cn.rollBack();
			cn.Close();
			return;
		} 
		
//		����ǰ��ɾ����������
	    strdel = "delete from niandhtzxqkb where to_char(riq,'yyyy-mm')='"	+ getNianf() + "-"+getYuef()+"' and diancxxb_id="+visit.getDiancxxb_id()+";\n";
	    sql.append(strdel);
		
	    while (rs.next()){

	    	strSql="insert into niandhtzxqkb(id,diancxxb_id,gongysb_id,riq,hetl,hetjzrz,hetjzckjg,hetjzdcjg,hetzjkhbz,yansfs,zhuangt,beiz) values("
				+Long.parseLong(MainGlobal.getNewID(visit.getDiancxxb_id()))
				+","
				+visit.getDiancxxb_id()+","
				+rs.getLong("gongysb_id")+",to_date('"+CurrsDate+"-01','yyyy-mm-dd'),"
				+rs.getDouble("hetl")+","
				+rs.getDouble("hetjzrz")+","
				+rs.getDouble("hetjzckjg")+","
				+rs.getDouble("hetjzdcjg")+","
                +rs.getDouble("hetzjkhbz")+",'"
                +rs.getString("yansfs")+"',0,'');\n";
			sql.append(strSql);
		}
		sql.append("end;");
		flag=cn.getInsert(sql.toString());
		if(flag==-1){
			WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail+"SQL:"+strSql);
			setMsg("���ɹ��̳��ִ�����Ⱥ�ͬ�������ʧ�ܣ�");
			cn.rollBack();
			cn.Close();
			return;
		}
		cn.commit();
		cn.Close();
		setMsg(CurrZnDate+"�����ݳɹ����ɣ�");

	}

	public void CreateData() {
		
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		String CurrZnDate=getNianf()+"��"+getYuef()+"��";
		String CurrSDate=getNianf()+"-"+getYuef();

		String strSql="";
		String strdel="";
		int flag;
		StringBuffer sqlljbc1 = new StringBuffer("begin \n");
		strSql=
		"select ht.diancxxb_id as diancxxb_id,gys.id as gongysb_id,\n" +
		"       round(sum(slb.hetl)/10000,2) as hetl,\n" + 
		"       round(decode(sum(slb.hetl),0,0,sum(slb.hetl*zlb.rz)/sum(slb.hetl)),0) as hetjzrz,\n" + 
		"       round(decode(sum(slb.hetl),0,0,sum(slb.hetl*hetjz_ck.jij)/sum(slb.hetl)),0) as hetjzckjg,\n" + 
		"       round(decode(sum(slb.hetl),0,0,sum(slb.hetl*hetjz_dc.jij)/sum(slb.hetl)),0) as hetjzdcjg\n" + 
		"from (select zl.hetb_id as hetb_id,zl.id,case danwb_id when  16 then decode(zl.shangx,0,zl.xiax,zl.shangx)\n" + 
		"             when  17 then round((decode(zl.shangx,0,0,zl.xiax)*1000/4.1816),0) end rz,danwb_id\n" + 
		"      from hetzlb zl,zhibb zb\n" + 
		"      where zl.zhibb_id=zb.id and zb.bianm='�յ�����λ��ֵQnetar(MJ/Kg)'\n" + 
		"      )zlb,\n" + 
		"     (select  sl.hetb_id as hetb_id,sl.id,sum(sl.hetl) as hetl\n" + 
		"      from hetslb sl where riq=to_date('"+CurrSDate+"-01','yyyy-mm-dd') and diancxxb_id="+visit.getDiancxxb_id()+"\n" + 
		"      group by (sl.id,sl.hetb_id))slb,\n" + 
		"     (select jg.hetb_id as het_id,jg.id,jg.jij,jsfs.mingc as yansfs,jg.jijdwid\n" + 
		"      from hetjgb jg,hetjsfsb jsfs\n" + 
		"      where  jg.hetjsfsb_id=jsfs.id and jsfs.id=2 and jijdwid=9)hetjz_ck,\n" + 
		"     (select jg.hetb_id as het_id,jg.id,jg.jij,jsfs.mingc as yansfs,jg.jijdwid\n" + 
		"      from hetjgb jg,hetjsfsb jsfs\n" + 
		"      where  jg.hetjsfsb_id=jsfs.id and jsfs.id=1 and jijdwid=9)hetjz_dc,\n" + 
		"      ( select h.id,dc.id as diancxxb_id,h.gongysb_id from hetb h,vwdianc dc\n" + 
		"        where  h.diancxxb_id=dc.id and dc.id="+visit.getDiancxxb_id()+"\n" + 
		"              and h.jihkjb_id in (1,3)  and h.qisrq>=getyearfirstdate(to_date('"+CurrSDate+"-01','yyyy-mm-dd')) and h.guoqrq<=getYearLastDate(to_date('"+CurrSDate+"-01','yyyy-mm-dd'))\n" + 
		"        ) ht,meikdqb gys\n" +
		"   where ht.id=zlb.hetb_id(+) and ht.id=slb.hetb_id(+) and ht.id=hetjz_ck.het_id(+) and ht.id=hetjz_dc.het_id(+)\n" + 
		"          and ht.gongysb_id=gys.id\n" + 
		"   group by (ht.diancxxb_id,gys.id)";

		ResultSetList rs=con.getResultSetList(strSql);
		if (rs == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "��������SQL:" + strSql);
			setMsg(ErrorMessage.NullResult);
			con.rollBack();
			con.Close();
			return;
		} 
		
		strdel = "delete from niandhtzxqkb where to_char(riq,'yyyy-mm')='"	+ getNianf() + "-"+getYuef()+"' and diancxxb_id="+visit.getDiancxxb_id()+";\n";
		
		sqlljbc1.append(strdel);
		
		while (rs.next()){

			strSql="insert into niandhtzxqkb(id,diancxxb_id,gongysb_id,riq,hetl,hetjzrz,hetjzckjg,hetjzdcjg,hetzjkhbz,yansfs,zhuangt,beiz) values("
				+Long.parseLong(MainGlobal.getNewID(visit.getDiancxxb_id()))
				+","
				+visit.getDiancxxb_id()+","
				+rs.getLong("gongysb_id")+",to_date('"+CurrSDate+"-01','yyyy-mm-dd'),"
				+rs.getDouble("hetl")+","
				+rs.getDouble("hetjzrz")+","
				+rs.getDouble("hetjzckjg")+","
				+rs.getDouble("hetjzdcjg")+",0,'��',0,'');\n";
			
			sqlljbc1.append(strSql);
		}
		sqlljbc1.append("end;");
		
		flag = con.getInsert(sqlljbc1.toString());
		if(flag == -1) {
			WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail+"SQL:"+strSql);
			setMsg("���ɹ��̳��ִ�����Ⱥ�ͬ�������ʧ�ܣ�");
			con.rollBack();
			con.Close();
			return;
		}
		con.commit(); 
		con.Close();
		setMsg(CurrZnDate+"�����ݳɹ����ɣ�");
	}

	public void getSelectData(ResultSetList rsl) {
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
		String zhuangt="";
		if(visit.isShifsh()==true){
			if(visit.getRenyjb()==3){
				zhuangt="";
			}else if(visit.getRenyjb()==2){
				zhuangt=" and (n.zhuangt=1 or n.zhuangt=2)";
			}else if(visit.getRenyjb()==1){
				zhuangt=" and n.zhuangt=2";
			}
		}		

	// ----------�糧��--------------
		String str = "";

		int treejib = this.getDiancTreeJib();
		if (treejib == 1) {// ѡ����ʱˢ�³����еĵ糧
			str = "";
		} else if (treejib == 2) {// ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
			str = "and (dc.id = " + getTreeid() + " or dc.fuid = "
					+ getTreeid() + ")";
		} else if (treejib == 3) {// ѡ�糧ֻˢ�³��õ糧
			str = "and dc.id = " + getTreeid() + "";
		}
//	---------------------------------------------------------------------	
		boolean isReture = false ;
		if(visit.getRenyjb()==1){
			if(treejib==1){
				isReture=true;
			}
		}else if(visit.getRenyjb()==2){
			if(treejib==2){
				isReture=true;
			}
		}
		
		if (rsl == null) {
			String strSql=

			"select n.id as id,n.riq as riq,dc.mingc as diancxxb_id,gys.mingc as gongysb_id,n.hetl as hetl,n.hetjzrz as hetjzrz,n.hetjzckjg as hetjzckjg\n" +
			",n.hetjzdcjg as hetjzdcjg,n.hetzjkhbz as hetzjkhbz,n.yansfs as yansfs,n.zhuangt as zhuangt\n" + 
			" from niandhtzxqkb n,diancxxb dc,meikdqb gys\n" + 
			"where to_char(n.riq,'yyyy-mm')='"+getNianf()+"-"+getYuef()+"' and n.gongysb_id=gys.id and n.diancxxb_id = dc.id "+ str+" "+zhuangt+"\n";

		rsl = con.getResultSetList(strSql);

		}
		
		boolean yincan=false;
		while(rsl.next()){
			if(visit.getRenyjb()==3){
				if(rsl.getLong("zhuangt")==0){
					yincan = false;
				}else{
					yincan = true;
				}
			}else if(visit.getRenyjb()==1||visit.getRenyjb()==2){
					yincan = true;
			}
			
		}
		rsl.beforefirst();
		boolean showBtn = false;
		if(rsl.next()){
			rsl.beforefirst();
			showBtn = false;
		}else{
			showBtn = true;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		// //���ñ��������ڱ���
		egu.setTableName("NIANDHTZXQKB");
		// /������ʾ������
		egu.setWidth("bodyWidth");
//		egu.setHeight("bodyHeight");
		egu.addPaging(0);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.getColumn("id").setHeader("id");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("riq").setHeader("����");
		egu.getColumn("riq").setHidden(true);
		egu.getColumn("riq").setEditor(null);
		egu.getColumn("diancxxb_id").setHeader("�糧����");
		egu.getColumn("diancxxb_id").setHidden(true);
		egu.getColumn("diancxxb_id").setEditor(null);
		egu.getColumn("gongysb_id").setHeader("��Ӧ��");
		egu.getColumn("gongysb_id").setWidth(90);

		egu.getColumn("hetl").setHeader("��ͬ����<br>(���)");
		egu.getColumn("hetl").setWidth(80);
		egu.getColumn("hetjzrz").setHeader("��ͬ��׼��ֵ<br>  (ǧ��/Kg)");
		egu.getColumn("hetjzrz").setWidth(120);
		egu.getColumn("hetjzckjg").setHeader("��ͬ��׼����/ƽ��/����۸�<br>  (Ԫ/��)");
		egu.getColumn("hetjzckjg").setWidth(160);
		egu.getColumn("hetjzdcjg").setHeader("��ͬ��׼�����۸�<br>  (Ԫ/��)");
		egu.getColumn("hetjzdcjg").setWidth(120);
		egu.getColumn("hetzjkhbz").setHeader("��ͬ�ʼۿ��˱�׼<br>  (Ԫ/ǧ��)");
		egu.getColumn("hetzjkhbz").setWidth(120);
		egu.getColumn("yansfs").setHeader("���շ�ʽ");
		egu.getColumn("yansfs").setWidth(90);
		egu.getColumn("zhuangt").setHeader("״̬");
		egu.getColumn("zhuangt").setHidden(true);
		egu.getColumn("zhuangt").setDefaultValue("0");
		egu.getColumn("zhuangt").setEditor(null);
		
//		 �趨�е�С��λ

		((NumberField) egu.getColumn("hetl").editor).setDecimalPrecision(2);
		((NumberField) egu.getColumn("hetjzrz").editor).setDecimalPrecision(2);
		((NumberField) egu.getColumn("hetjzckjg").editor).setDecimalPrecision(3);
		((NumberField) egu.getColumn("hetzjkhbz").editor).setDecimalPrecision(3);
		
		//�������ڵ�Ĭ��ֵ,
		egu.getColumn("riq").setDefaultValue(getNianf()+"-"+getYuef()+"-01");
		
//		�糧
		ComboBox c1 = new ComboBox();
		egu.getColumn("diancxxb_id").setEditor(c1);
		egu.getColumn("diancxxb_id").setComboEditor(egu.gridId,
				new IDropDownModel("select id,mingc from diancxxb  where fuid="+visit.getDiancxxb_id()+" or id ="+visit.getDiancxxb_id()+" "
						));
		
		ResultSetList r = con.getResultSetList("select id,mingc from diancxxb where id="+visit.getDiancxxb_id()+" order by mingc");
		String mingc="";
		if(r.next()){
			mingc=r.getString("mingc");
		}
		con.Close();
		egu.getColumn("diancxxb_id").setReturnId(true);
		egu.getColumn("diancxxb_id").setDefaultValue(mingc);
		
		//��Ӧ��
		ComboBox c4 = new ComboBox();
		egu.getColumn("gongysb_id").setEditor(c4);
		c4.setEditable(true);
		egu.getColumn("gongysb_id").setComboEditor(egu.gridId,
				new IDropDownModel("select id,mingc from meikdqb /* where (fuid=0 or fuid =-1) " +
						"and id in ( select distinct gongysb_id from yuetjkjb kj where  to_char(kj.riq,'yyyy')='"+getNianf()+"' and  kj.diancxxb_id="+visit.getDiancxxb_id()+")*/ order by mingc "
						));
		egu.getColumn("gongysb_id").setReturnId(true);
//		egu.getColumn("gongysb_id").setEditor(null);
		List l = new ArrayList();
		l.add(new IDropDownBean(0, "��"));
		l.add(new IDropDownBean(1, "����"));
//		l.add(new IDropDownBean(2,"��������"));
		egu.getColumn("yansfs").setEditor(new ComboBox());
		egu.getColumn("yansfs").setComboEditor(egu.gridId,
				new IDropDownModel(l));
		egu.getColumn("yansfs").setReturnId(false);
		egu.getColumn("yansfs").setDefaultValue("��");

		// /���ð�ť
		egu.addTbarText("���");
		ComboBox comb1=new ComboBox();
		comb1.setWidth(60);
		comb1.setTransform("NianfDropDown");
		comb1.setId("NianfDropDown");//���Զ�ˢ�°�
		comb1.setLazyRender(true);//��̬��
		comb1.setEditable(true);
		egu.addToolbarItem(comb1.getScript());

		egu.addTbarText("�·�");
		ComboBox comb2=new ComboBox();
		comb2.setWidth(50);
		comb2.setTransform("YuefDropDown");
		comb2.setId("YuefDropDown");//���Զ�ˢ�°�
		comb2.setLazyRender(true);//��̬��
		comb2.setEditable(true);
		egu.addToolbarItem(comb2.getScript());
		egu.addTbarText("-");
//		�糧��
		egu.addTbarText("��λ:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree", ExtTreeUtil.treeWindowType_Dianc,((Visit)this.getPage().getVisit()).getDiancxxb_id(),getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree"); 
		
		egu.addTbarText("-");// ���÷ָ���

		
//		ˢ�°�ť
		StringBuffer rsb = new StringBuffer();
		rsb.append("function (){")
		.append(MainGlobal.getExtMessageBox("'����ˢ��'+Ext.getDom('NianfDropDown').value+'��'+Ext.getDom('YuefDropDown').value+'�µ�����,���Ժ�'",true))
		.append("document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("ˢ��",rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);
		if(visit.getRenyjb()==3){
			if(yincan ==false){
		//		��Ӱ�ť
				GridButton gb_insert = new GridButton(GridButton.ButtonType_Insert, egu.gridId,egu.getGridColumns(), null);
				gb_insert.setId("INSERT");
				egu.addTbarBtn(gb_insert);
		//		���ɰ�ť
				GridButton gbc = new GridButton("����",getBtnHandlerScript("CreateButton"));
				gbc.setIcon(SysConstant.Btn_Icon_Create);
				egu.addTbarBtn(gbc);

		//		ɾ����ť
				egu.addToolbarButton(GridButton.ButtonType_Delete, null);
				
		//		���水ť
				egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
				
//				�������°�ť
				GridButton cp = new GridButton("��������",getBtnHandlerScript("CopyButton"));
				gbc.setIcon(SysConstant.Btn_Icon_Copy);
				egu.addTbarBtn(cp);
//				GridButton ght = new GridButton(GridButton.ButtonType_Save, egu.gridId,egu.getGridColumns(), "SaveButton",MainGlobal.getExtMessageShow("���ڱ�������,���Ժ�...", "������...", 200));
//				GridButton ght = new GridButton(GridButton.ButtonType_Save_condition, egu.gridId,egu.getGridColumns(), "SaveButton",MainGlobal.getExtMessageShow("���ڱ�������,���Ժ�...", "������...", 200));
//				GridButton ght = new GridButton(GridButton.ButtonType_SaveAll, egu.gridId,egu.getGridColumns(), "SaveButton");
//				ght.setId("SAVE");
//				egu.addTbarBtn(ght);
			}
		}

		setExtGrid(egu);
		con.Close();
	}
	
	public String getBtnHandlerScript(String btnName) {
//		��ť��script
		StringBuffer btnsb = new StringBuffer();
		String cnDate = getNianf()+"��"+getYuef()+"��";
		btnsb.append("function (){Ext.MessageBox.confirm('��ʾ��Ϣ','");
		if(btnName.endsWith("CreateButton")||btnName.endsWith("CopyButton")) {
			btnsb.append("���������ݽ�����")
			.append(cnDate).append("���Ѵ����ݣ��Ƿ������");
		}else {
			btnsb.append("�Ƿ�ɾ��").append(cnDate).append("�����ݣ�");
		}
		btnsb.append("',function(btn){if(btn == 'yes'){")
		.append("document.getElementById('").append(btnName).append("').click()")
//		-------------------------------------------------------------------
		.append(";Ext.MessageBox.show({msg:'���ڴ�������,���Ժ�...',progressText:'������...',")
		.append("width:300,wait:true,waitConfig: {interval:200},icon:Ext.MessageBox.INFO}); \n")
//		-------------------------------------------------------------------
		.append("}; // end if \n")
		.append("});}");
		return btnsb.toString();
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
		if(getExtGrid() == null) {
			return "";
		}
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
			visit.setString1(null);
			visit.setString2(null);
			visit.setString3(null);
			setNianfValue(null);
			setYuefValue(null);
			this.getNianfModels();
			this.getYuefModels();
			visit.setShifsh(true);
			this.setTreeid(null);
			setRiq();
			
		}
		if(visit.getRenyjb()==3){
			if(!this.getTreeid().equals(visit.getDiancxxb_id()+"")){
				visit.setActivePageName(getPageName().toString());
				visit.setList1(null);
				visit.setString1(null);
				visit.setString2(null);
				visit.setString3(null);
				setNianfValue(null);
				setYuefValue(null);
				this.getNianfModels();
				this.getYuefModels();
				visit.setShifsh(true);
				this.setTreeid(null);
				setRiq();
			}
		}
		getSelectData(null);
	}
	
	public String getNianf() {
		return ((Visit) getPage().getVisit()).getString1();
	}
	public void setNianf(String value) {
		((Visit) getPage().getVisit()).setString1(value);
	}
	
	public String getYuef(){
		int intYuef=Integer.parseInt(((Visit) getPage().getVisit()).getString3());
		if (intYuef<10){
			return "0"+intYuef;
		}else{
			return ((Visit) getPage().getVisit()).getString3();
		}
	}
	public void setYuef(String value){
		((Visit)getPage().getVisit()).setString3(value);
	}

	
	public void setRiq() {
		setNianf(getNianfValue().getValue());
		setYuef(getYuefValue().getValue());
	}


	 // ���������
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
                _nianf = _nianf - 1;
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
        for (i = 2004; i <= DateUtil.getYear(new Date())+1; i++) {
            listNianf.add(new IDropDownBean(i, String.valueOf(i)));
        }
        _NianfModel = new IDropDownModel(listNianf);
        return _NianfModel;
    }

    public void setNianfModel(IPropertySelectionModel _value) {
        _NianfModel = _value;
    }
//  �·�������
	private static IPropertySelectionModel _YuefModel;
	
	public IPropertySelectionModel getYuefModel() {
	    if (_YuefModel == null) {
	        getYuefModels();
	    }
	    return _YuefModel;
	}
	
	private IDropDownBean _YuefValue;
	
	public IDropDownBean getYuefValue() {
	    if (_YuefValue == null) {
	        int _yuef = DateUtil.getMonth(new Date());
	        if (_yuef == 1) {
	            _yuef = 12;
	        } else {
	            _yuef = _yuef - 1;
	        }
	        for (int i = 0; i < getYuefModel().getOptionCount(); i++) {
	            Object obj = getYuefModel().getOption(i);
	            if (_yuef == ((IDropDownBean) obj).getId()) {
	                _YuefValue = (IDropDownBean) obj;
	                break;
	            }
	        }
	    }
	    return _YuefValue;
	}
	
	public void setYuefValue(IDropDownBean Value) {
    	if  (_YuefValue!=Value){
    		_YuefValue = Value;
    	}
	}

    public IPropertySelectionModel getYuefModels() {
        List listYuef = new ArrayList();
        for (int i = 1; i < 13; i++) {
            listYuef.add(new IDropDownBean(i, String.valueOf(i)));
        }
        _YuefModel = new IDropDownModel(listYuef);
        return _YuefModel;
    }

    public void setYuefModel(IPropertySelectionModel _value) {
        _YuefModel = _value;
    }
//
////    �·�������
//    private static IPropertySelectionModel _YuefModel;
//    public IPropertySelectionModel getYuefModels(){
//    	List yuef=new ArrayList();
//    	for (int i=1 ;i<13;i++){
//    		yuef.add(new IDropDownBean(i,String.valueOf(yuef)));
//    	}
//    	_YuefModel= new IDropDownModel(yuef);
//    	return _YuefModel;
//    }
//
//    public IPropertySelectionModel getYuefModel(){
//    	if (_YuefModel==null){
//    		getYuefModels();
//    	}
//    	return _YuefModel;
//    }
//    
//    public IDropDownBean _YuefValue;
//    public IDropDownBean getYuefValue(){
//    	if(_YuefValue==null){
//    		int yuef=DateUtil.getMonth(new Date());
//    		if(yuef==1){
//    			yuef=12;
//    		}else{
//    			yuef=yuef-1;
//    		}
//    		for(int i=0;i<getYuefModel().getOptionCount();i++){
//    			Object obj=getYuefModel().getOption(i);
//    			if(yuef==((IDropDownBean)obj).getId()){
//    				_YuefValue=(IDropDownBean)obj;
//    				break;
//    			}
//
//    		}
//    	}
//    	return _YuefValue;
//    }
//    public void setYuefValue(IDropDownBean Value){
//    	if(_YuefValue==null){
//    		_YuefValue=Value;
//    	}
//    }
//    
 

//    �糧��

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
			// TODO �Զ����� catch ��
			e.printStackTrace();
		}finally{
			cn.Close();
		}

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
			// TODO �Զ����� catch ��
			e.printStackTrace();
		}finally{
			cn.Close();
		}

		return IDropDownDiancmc;

	}
	
	
	private String treeid;

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
	
	
//	�õ��糧����ֵ�ļ���,(jib:1,2,3,) 1�Ǽ���,2�Ƿֹ�˾,3�ǵ糧
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
