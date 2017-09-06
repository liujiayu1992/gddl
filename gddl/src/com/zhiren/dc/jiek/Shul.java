package com.zhiren.dc.jiek;

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
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.dc.chengbgl.Chengbjs;
import com.zhiren.dc.jilgl.Jilcz;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Shul extends BasePage implements PageValidateListener {
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
	}
//	������
	private String riq;

	public String getRiq() {
		return riq;
	}

	public void setRiq(String riq) {
		this.riq = riq;
	}
	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	private boolean _ShenhClick = false;
	public void ShangcButton(IRequestCycle cycle) {
		_ShenhClick = true;
	}
	
	private void Shenh() {//�ϴ�
	//1����֤����ȫ����ˣ�����ȫ��������ˣ����������Ʊ�����
	//2,Զ����֤��Ӧ�̱��������ڣ�ú�����������Ʊ�����
	//3����վ��Ʒ��Զ�̲����ھ͹��������Ϣ����sql���//�޸�Ϊú��ҲҪ����Ӧ�̴�����Ϊ�������ú����ҵ����������Ʊ�����
	//4,����fahbtmp����������������Ʊ�����
	//5��ִ��
	//6����ʾִ�н��
	//7,//������Ӧ������д�������״̬��д��fahb��״̬��˵���Ѿ��ϴ��ɹ�
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		try {
		
		String sql="select fahb.hedbz,zhilb.shenhzt\n" +
		"from fahb,zhilb\n" + 
		"where fahb.zhilb_id=zhilb.id(+)\n" + 
		"and to_char(fahb.daohrq,'yyyy-mm-dd')='"+getRiq()+"' and (hedbz<>3 or shenhzt<>1)" +
		" and fahb.meikxxb_id in(select meikxxb_id from shangcsjpz where kaisrq<=fahb.fahrq and jiesrq>=fahb.fahrq)";
		ResultSet rs=con.getResultSet(sql);
		if(rs.next()){
			setMsg("���ݲ����������˺��ϱ���");
			return;
		}
	//2

		sql="select substr(gongysb.shangjgsbm,1,6)shangjgsbm\n" +
		"from fahb,gongysb\n" + 
		"where fahb.gongysb_id=gongysb.id\n" + 
		"  and fahb.meikxxb_id in(select meikxxb_id from shangcsjpz where kaisrq<=fahb.fahrq and jiesrq>=fahb.fahrq) "+
		"and to_char(fahb.daohrq,'yyyy-mm-dd')='"+getRiq()+"' and fahb.shangc=0 and not exists (\n" + 
		"select * from gongysb@gangkjy Ygongysb where Ygongysb.bianm=substr(gongysb.shangjgsbm,1,6)\n" + 
		")";
		ResultSet rs1=con.getResultSet(sql);
		
			if(rs1.next()){
				setMsg(rs1.getString("shangjgsbm")+"��Ӧ�̱��벻��ʶ��");
				return;
			}
	//2

		sql="select  meikxxb.shangjgsbm\n" +
		"from fahb,meikxxb\n" + 
		"where fahb.meikxxb_id=meikxxb.id\n" + 
		"  and fahb.meikxxb_id in(select meikxxb_id from shangcsjpz where kaisrq<=fahb.fahrq and jiesrq>=fahb.fahrq) "+
		"and to_char(fahb.daohrq,'yyyy-mm-dd')='"+getRiq()+"' and fahb.shangc=0 and not exists (\n" + 
		"select * from meikxxb@gangkjy Ymeikxxb where Ymeikxxb.bianm=meikxxb.shangjgsbm\n" + 
		")";
		ResultSet rs2=con.getResultSet(sql);
		if(rs2.next()){
			setMsg(rs2.getString("shangjgsbm")+"ú����벻��ʶ��");
			return;
		}
	//3��վ

		sql="insert into chezxxb@gangkjy(id,xuh,bianm,mingc,quanc,lujxxb_id,leib)\n" + 
		"\n" + 
		"select    xl_xul_id.nextval@gangkjy  ,1,mingc,mingc,mingc,2,'��վ'\n" + 
		"from(\n" + 
		"select  fz.mingc,fz.id\n" + 
		"from fahb,chezxxb fz\n" + 
		"where fahb.faz_id=fz.id\n" + 
		"and to_char(fahb.daohrq,'yyyy-mm-dd')='"+getRiq()+"'and fahb.shangc=0\n" + 
		"  and fahb.meikxxb_id in(select meikxxb_id from shangcsjpz where kaisrq<=fahb.fahrq and jiesrq>=fahb.fahrq) "+
		"union\n" + 
		"select  dz.mingc,dz.id\n" + 
		"from fahb,chezxxb dz\n" + 
		"where fahb.daoz_id=dz.id\n" + 
		"and to_char(fahb.daohrq,'yyyy-mm-dd')='"+getRiq()+"'and fahb.shangc=0\n" + 
		"  and fahb.meikxxb_id in(select meikxxb_id from shangcsjpz where kaisrq<=fahb.fahrq and jiesrq>=fahb.fahrq) "+
		")cz\n" + 
		" where not exists (\n" + 
		"select * from chezxxb@gangkjy Ychezxxb where Ychezxxb.mingc=cz.mingc\n" + 
		")";
		con.getInsert(sql);
	//3Ʒ��

		sql="insert into pinzb@gangkjy(id,xuh,bianm,mingc,leib)\n" + 
		"select xl_xul_id.nextval@gangkjy,1,bianm,mingc,'ú' from(select distinct  1,pz.mingc bianm,pz.mingc\n" + 
		"from fahb,pinzb pz\n" + 
		"where fahb.pinzb_id=pz.id\n" + 
		"and to_char(fahb.daohrq,'yyyy-mm-dd')='"+getRiq()+"'and fahb.shangc=0\n" + 
//		"  and fahb.meikxxb_id in(select meikxxb_id from shangcsjpz where kaisrq<=fahb.fahrq and jiesrq>=fahb.fahrq) "+
		" and not exists (\n" + 
		"select * from pinzb@gangkjy Ypinzb where Ypinzb.mingc=pz.mingc\n" + 
		"))";
		con.getInsert(sql);
	//4fahbtmp

		sql="insert into fahbtmp@gangkjy\n" +
		"  (id, yuanid, diancxxb_id, gongysb_id, meikxxb_id, pinzb_id, faz_id, daoz_id, jihkjb_id, fahrq, daohrq, zhilb_id,\n" + 
		"  jiesb_id, yunsfsb_id, chec, maoz, piz, jingz, biaoz, yingd, yingk, yuns, yunsl, koud, kous, kouz, koum, zongkd,\n" + 
		"   sanfsl, ches, tiaozbz, yansbhb_id, lie_id, yuandz_id, yuanshdwb_id, kuangfzlb_id, liucb_id, liucztb_id, hedbz ,\n" + 
		" lieid, laimsl, laimzl, laimkc, guohsj,\n" + 
		" gujm, gujy, heth, hetj, jiekscsj, shangccz, xiugzt, danjc,leix_id)\n" + 
		"\n" + 
		"  select  xl_xul_id.nextval@gangkjy,xl_xul_id.nextval@gangkjy,fahb.diancxxb_id,\n" + 
		"  (select id from gongysb@gangkjy ygongysb where ygongysb.bianm=substr(gongysb.shangjgsbm,1,6))gongysb_id,\n" + 
		"  (select id from meikxxb@gangkjy ymeikxxb where ymeikxxb.bianm=meikxxb.shangjgsbm)meikxxb_id,\n" + 
		"  (select id from pinzb@gangkjy ypinzb where ypinzb.mingc=pinzb.mingc)pinzb_id,\n" + 
		"  (select id from chezxxb@gangkjy ychezxxb where ychezxxb.mingc=fz.mingc)fz_id,\n" + 
		"  (select id from chezxxb@gangkjy ychezxxb where ychezxxb.mingc=dz.mingc)dz_id,\n" + 
		"   (select id from jihkjb@gangkjy yjihkjb where yjihkjb.bianm=jihkjb.bianm)jihkj_id,\n" + 
		"  fahb.fahrq,fahb.daohrq,zhilb_id,jiesb_id,yunsfsb_id, chec, maoz, piz, jingz, biaoz, yingd, yingk, yuns,\n" + 
		"   yunsl, koud, kous, kouz, koum, zongkd, sanfsl, ches,\n" + 
		"   tiaozbz, yansbhb_id, lie_id, yuandz_id, yuanshdwb_id, kuangfzlb_id, liucb_id, liucztb_id, hedbz,\n" + 
		"    fahb_id, laimsl, laimzl, laimkc, daohrq,\n" + 
		"    meij,yunf,heth,hetjg,sysdate,0,0,0,3\n" + 
		"  from fahb,gongysb,chezxxb fz,chezxxb dz,jihkjb,meikxxb,pinzb,guslsb\n" + 
		"  where fahb.gongysb_id=gongysb.id and fahb.faz_id=fz.id and fahb.daoz_id=dz.id and fahb.jihkjb_id=jihkjb.id\n" + 
		"  and fahb.meikxxb_id=meikxxb.id and fahb.pinzb_id=pinzb.id and fahb.id=guslsb.fahb_id and guslsb.leix=2\n" + 
		"   and to_char(fahb.daohrq,'yyyy-mm-dd')='"+getRiq()+"' and fahb.shangc=0\n" + 
		"  and fahb.meikxxb_id in(select meikxxb_id from shangcsjpz where kaisrq<=fahb.fahrq and jiesrq>=fahb.fahrq) "+
		"";
		int flag=con.getInsert(sql);
		if(flag!=-1){//�����ȷ
			//Ҫ��ɾ�����������������˻��˺��޷��ϴ�
			sql="delete from zhilb@gangkjy  Yzhilb where  Yzhilb.id in(\n" +
			"select zhilb.id "+
			"from zhilb,fahb\n" + 
			"where fahb.zhilb_id=zhilb.id\n" + 
			"and to_char(fahb.daohrq,'yyyy-mm-dd')='"+getRiq()+"'\n" + 
			"and fahb.shangc=0"+
			"  and fahb.meikxxb_id in(select meikxxb_id from shangcsjpz where kaisrq<=fahb.fahrq and jiesrq>=fahb.fahrq) "+
			")";
			con.getDelete(sql);
			//������Ӧ����
			sql="insert into zhilb@gangkjy(id,caiyb_id,huaybh,huaysj,qnet_ar,aar,ad,vdaf,mt,stad,aad,mad,had,vad,std,qgrad,qgrad_daf,shenhzt,liucztb_id)\n" +
			"select zhilb.id,0,huaybh,huaysj,qnet_ar,aar,ad,vdaf,mt,stad,aad,mad,had,vad,std,qgrad,qgrad_daf,shenhzt,zhilb.liucztb_id\n" + 
			"from zhilb,fahb\n" + 
			"where fahb.zhilb_id=zhilb.id\n" + 
			"and to_char(fahb.daohrq,'yyyy-mm-dd')='"+getRiq()+"'\n" + 
			"and fahb.shangc=0"+
			"  and fahb.meikxxb_id in(select meikxxb_id from shangcsjpz where kaisrq<=fahb.fahrq and jiesrq>=fahb.fahrq) ";
			con.getInsert(sql);
			
			//20091125����������ʱ����
			sql="delete from zhillsb@gangkjy  Yzhilb where  Yzhilb.zhilb_id in(\n" +
			"select zhilb.id "+
			"from zhilb,fahb\n" + 
			"where fahb.zhilb_id=zhilb.id\n" + 
			"and to_char(fahb.daohrq,'yyyy-mm-dd')='"+getRiq()+"'\n" + 
			"and fahb.shangc=0"+
			"  and fahb.meikxxb_id in(select meikxxb_id from shangcsjpz where kaisrq<=fahb.fahrq and jiesrq>=fahb.fahrq) "+
			")";
			con.getDelete(sql);
			

			sql="insert into zhillsb@gangkjy(id,zhilb_id,huaysj,qnet_ar,aar,ad,vdaf,mt,stad,aad,mad,qbad,had,vad,fcad,std,qgrad,hdaf,qgrad_daf,sdaf,lury,shenhzt,huaylb\n" +
			",bumb_id,har,qgrd)\n" + 
			"select zhillsb.id,fahb.zhilb_id,huaysj,qnet_ar,aar,ad,vdaf,mt,stad,aad,mad,qbad,had,vad,fcad,std,qgrad,hdaf,qgrad_daf,sdaf,lury,shenhzt,huaylb\n" + 
			",bumb_id,har,qgrd\n" + 
			"      from zhillsb,fahb\n" + 
			"      where fahb.zhilb_id=zhillsb.zhilb_id\n" + 
			"      and to_char(fahb.daohrq,'yyyy-mm-dd')='"+getRiq()+"'\n" + 
			"      and fahb.shangc=0\n" + 
			"        and fahb.meikxxb_id in(select meikxxb_id from shangcsjpz where kaisrq<=fahb.fahrq and jiesrq>=fahb.fahrq)" ;
			con.getInsert(sql);
			//��������־

			sql="select *\n" +
			"from jiekscjl@gangkjy "+
			"where  to_char(jiekscjl.riq,'yyyy-mm-dd')='"+getRiq()+"' and diancxxb_id= "+visit.getDiancxxb_id()+" and leix='����'";
			ResultSet rs6=con.getResultSet(sql);
			if(rs6.next()){//����Ѿ�������־���·������
				sql="update jiekscjl@gangkjy\n" +
				"set zhuangt=1\n" + 
				"where  to_char(jiekscjl.riq,'yyyy-mm-dd')='"+getRiq()+"' and diancxxb_id= "+visit.getDiancxxb_id()+" and leix='����'";
				con.getUpdate(sql);
			}else{
				sql="insert into jiekscjl@gangkjy\n" +
				"   (id, riq, diancxxb_id, shangcsj, gengxsj, caozy, zhuangt, leix)\n" + 
				" values\n" + 
				"   (xl_xul_id.nextval@gangkjy,to_date('"+getRiq()+"','yyyy-mm-dd'), "+visit.getDiancxxb_id()+", sysdate, sysdate, '"+visit.getRenymc()+"', 1, '����')\n" ; 
				con.getInsert(sql);
			}
			//�ͻ�����־
			sql="update fahb\n" +
			"set fahb.shangc=1\n" + 
			"where   to_char(fahb.daohrq,'yyyy-mm-dd')='"+getRiq()+"'\n" + 
			"and fahb.shangc=0"+
			"  and fahb.meikxxb_id in(select meikxxb_id from shangcsjpz where kaisrq<=fahb.fahrq and jiesrq>=fahb.fahrq) ";
			con.getInsert(sql);
			setMsg("�ϴ����ݳɹ���");
		}else{
			setMsg("��������ʱ��������");
			return;
		}
		} catch (SQLException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		}finally{
			con.Close();
		}
	}
	
	private boolean _QuxshClick = false;
	public void QuxButton(IRequestCycle cycle) {
		_QuxshClick = true;
	}
	
	private void Quxsh() {//ȡ��
		//1,����ȡ����fahb.id��fahbtmp.lieid��Ӧ
		//2,�ж�fahbtmp.xiugzt��״̬,���0,ֱ��ɾ��,�����ó�-1����
		//3,д�������־,���칤��δ��;д������־,fahb�ó�δ�ϴ�״̬
		//4,��ʾȡ���ɹ�
		Visit visit = (Visit) this.getPage().getVisit();
		ResultSetList rsl = visit.getExtGrid1().getModifyResultSet(getChange());
		JDBCcon con = new JDBCcon();
	try{
			if(rsl.next()){//ѡ��һ��
				String id=rsl.getString("ID");
				String sql="select xiugzt\n" +
				"from fahbtmp@gangkjy\n" + 
				"where xiugzt>=0 and fahbtmp.lieid="+id;
				ResultSet rs=con.getResultSet(sql);
				if(rs.next()){//���������и�����
					if(rs.getInt("xiugzt")==0){//û�о�������
						sql="delete from fahbtmp@gangkjy where xiugzt=0 and lieid="+id;
						con.getDelete(sql);
					}else{//�Ѿ�ʹ��
						sql="update fahbtmp@gangkjy\n" +
						"set xiugzt=-1\n" + 
						"where xiugzt>0 and lieid="+id;
						con.getUpdate(sql);
					}
					//3,д��־
					sql="update jiekscjl@gangkjy\n" +
					"set zhuangt=0\n" + 
					"where  to_char(jiekscjl.riq,'yyyy-mm-dd')='"+getRiq()+"' and diancxxb_id= "+visit.getDiancxxb_id()+" and leix='����'";
					con.getUpdate(sql);
					//3,δ�ϴ�״̬
					sql="update fahb\n" +
					"set shangc=0\n" + 
					"where id="+id;
					con.getUpdate(sql);
					setMsg("�ɹ�ȡ�����ݣ�");
				}else{
					setMsg("���������޴����ݣ�����������볧����ϵ��");
					return;
				}
			}
		}catch(Exception e){
			setMsg("ȡ��ʱ����δ֪�쳣�����볧����ϵ��");
			return;
		}finally{
			con.Close();
		}
	}
	
	public void submit(IRequestCycle cycle) {
		if (_ShenhClick) {
			_ShenhClick = false;
			Shenh();
		}
		if (_QuxshClick) {
			_QuxshClick = false;
			Quxsh();
		}
	}
	
	private void initGrid() {
		JDBCcon con = new JDBCcon();
		StringBuffer sb = new StringBuffer();
		sb.append("select fahb.id,gongysb.mingc,meikxxb.mingc mkmc,pinzb.mingc pinz,jihkjb.mingc jhkj,yunsfsb.mingc ysfs,fz.mingc fzmc,dz.mingc dzmc,fahb.ches,fahb.laimsl\n" +
		"from fahb,gongysb,meikxxb,chezxxb fz,chezxxb dz,jihkjb,pinzb,yunsfsb\n" + 
		"where fahb.gongysb_id=gongysb.id and fahb.meikxxb_id=meikxxb.id\n" + 
		"and fahb.faz_id=fz.id and fahb.daoz_id=dz.id and fahb.jihkjb_id=jihkjb.id\n" + 
		" and fahb.meikxxb_id in(select meikxxb_id from shangcsjpz where kaisrq<=fahb.fahrq and jiesrq>=fahb.fahrq)"+
		"and fahb.pinzb_id=pinzb.id and fahb.shangc="+getLeixSelectValue().getId()+" and fahb.yunsfsb_id =yunsfsb.id and to_char(fahb.daohrq,'yyyy-mm-dd')='"+getRiq()+"'");
		ResultSetList rsl = con.getResultSetList(sb.toString());
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.getColumn("id").setHeader("��ʶ");
		egu.getColumn("id").setWidth(150);
		egu.getColumn("id").setEditor(null);
		egu.getColumn("id").setHidden(true);
		
		egu.getColumn("mingc").setHeader("��Ӧ������");
		egu.getColumn("mingc").setWidth(150);
		egu.getColumn("mingc").setEditor(null);
		
		egu.getColumn("mkmc").setHeader("ú������");
		egu.getColumn("mkmc").setWidth(150);
		egu.getColumn("mkmc").setEditor(null);
		
		egu.getColumn("pinz").setHeader("Ʒ��");
		egu.getColumn("pinz").setWidth(70);
		egu.getColumn("pinz").setEditor(null);
		
		egu.getColumn("jhkj").setHeader("�ƻ��ھ�");
		egu.getColumn("jhkj").setWidth(70);
		egu.getColumn("jhkj").setEditor(null);
		
		egu.getColumn("ysfs").setHeader("���䷽ʽ");
		egu.getColumn("ysfs").setWidth(70);
		egu.getColumn("ysfs").setEditor(null);
		
		egu.getColumn("fzmc").setHeader("��վ");
		egu.getColumn("fzmc").setWidth(70);
		egu.getColumn("fzmc").setEditor(null);
		
		egu.getColumn("dzmc").setHeader("��վ");
		egu.getColumn("dzmc").setWidth(70);
		egu.getColumn("dzmc").setEditor(null);
		
		egu.getColumn("ches").setHeader("����");
		egu.getColumn("ches").setWidth(50);
		egu.getColumn("ches").setEditor(null);
		
		egu.getColumn("laimsl").setHeader("��ú��");
		egu.getColumn("laimsl").setWidth(100);
		egu.getColumn("laimsl").setEditor(null);
		//����ҳ����
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.addPaging(0);
		egu.setGridSelModel(ExtGridUtil.GridselModel_Row_single);
		//����ÿҳ��ʾ����
		egu.addPaging(25);
		
		egu.addTbarText("�������ڣ�");
		DateField df = new DateField();
		df.setValue(getRiq());
		df.Binding("RIQ", "Form0");// ��htmlҳ�е�id��,���Զ�ˢ��
		egu.addToolbarItem(df.getScript());
		
		egu.addTbarText("״̬:");
		ComboBox comb2=new ComboBox();
		comb2.setId("zhuangt");
		comb2.setWidth(100);
	
		comb2.setTransform("zhuangtSelect");
		comb2.setLazyRender(true);//��̬��weizSelect
		egu.addToolbarItem(comb2.getScript());
		egu.addOtherScript("zhuangt.on('select',function(){document.forms[0].submit()});");
		if(getLeixSelectValue().getId()==0){//Ϊ�ύ
			egu.addToolbarButton("�ϴ�",GridButton.ButtonType_SaveAll, "ShangcButton", null);
		}else{
			egu.addToolbarButton("ȡ��",GridButton.ButtonType_SubmitSel, "QuxButton", null);
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
		if(getExtGrid()==null){
			return "";
		}
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		if(getExtGrid()==null){
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
//	 ����
	public IDropDownBean getLeixSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean3() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean3((IDropDownBean) getLeixSelectModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean3();
	}

	public void setLeixSelectValue(IDropDownBean Value) {
		
		((Visit) getPage().getVisit()).setDropDownBean3(Value);
	}

	public void setLeixSelectModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel3(value);
	}

	public IPropertySelectionModel getLeixSelectModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {
			getLeixSelectModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}

	public void getLeixSelectModels() {
		List list = new ArrayList();
		list.add(new IDropDownBean(0, "δ�ϴ�"));
		list.add(new IDropDownBean(1,"���ϴ�"));
		((Visit) getPage().getVisit())
				.setProSelectionModel3(new IDropDownModel(list));
		return;
	}
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			if(getRiq() == null) {
				setRiq(DateUtil.FormatDate(new Date()));
			}
			((Visit) getPage().getVisit())
			.setProSelectionModel3(null);
		}
		initGrid();
	} 
}