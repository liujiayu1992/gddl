package com.zhiren.shanxdted.baob.kuidkkb;

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
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.Checkbox;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Kuidkkb_wh extends BasePage implements PageValidateListener {

//	����
	public String getRiq() {
		if (((Visit) this.getPage().getVisit()).getString1() == null || ((Visit) this.getPage().getVisit()).getString1().equals("")) {
			((Visit) this.getPage().getVisit()).setString1(DateUtil.FormatDate(new Date()));
		}
		return ((Visit) this.getPage().getVisit()).getString1();
	}

	public void setRiq(String riq) {
		if (((Visit) this.getPage().getVisit()).getString1() != null && !((Visit) this.getPage().getVisit()).getString1().equals(riq)) {
			((Visit) this.getPage().getVisit()).setString1(riq);
		}
	}
	//����ӵ������ֶ�
	public String getRiq2() {
		if (((Visit) this.getPage().getVisit()).getString3() == null || ((Visit) this.getPage().getVisit()).getString3().equals("")) {
			((Visit) this.getPage().getVisit()).setString3(DateUtil.FormatDate(new Date()));
		}
		return ((Visit) this.getPage().getVisit()).getString3();
	}

	public void setRiq2(String riq2) {
		if (((Visit) this.getPage().getVisit()).getString3() != null && !((Visit) this.getPage().getVisit()).getString3().equals(riq2)) {
			((Visit) this.getPage().getVisit()).setString3(riq2);
		}
	}
	//����
	protected void initialize() {
		msg = "";
	}

	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
	}

	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	public void Save() {
		String strchange = getChange();
		JDBCcon con = new JDBCcon();
		String strId="";
		
		StringBuffer sql = new StringBuffer();
		ResultSetList rssb=getExtGrid().getDeleteResultSet(getChange());
		while(rssb.next()){
			String id=rssb.getString("id");
			if(id.equals("")||id==null){
				id="0";
			}
			sql.append("delete kuidkkb_wh where id ="+id+";\n");
			
		}
		rssb.close();
		ResultSetList mdrsl = getExtGrid().getModifyResultSet(strchange);
		while (mdrsl.next()) {
			if ("".equals(mdrsl.getString("ID"))) {
//				����id
				strId=MainGlobal.getNewID(con, getChangbValue().getId());
				sql.append("insert into kuidkkb_wh(ID,DIANCXXB_ID,MEIKXXB_ID,YUNSDWB_ID,DAOHRQ,fahrq,HETMJ,MEIJ,YUNF,HETBMDJ,KUANGFBMDJ,RUCBMDJ,KUANGFL,RUCL,\n" +
						"  BANGC,HETRZ,KUANGFRZ,RUCRZ,JIESRZ,HETKFRZC,KUANGFRCRZC,rucjsrzc,KUID,KUIDJE,KUIK,KUIKJE,KUIDKKHJJE,beiz,stad,fahb_id,jiesbmdj) values(\n" +
							strId + ",\n" +
							"(select max(id) from diancxxb where mingc ='"+mdrsl.getString("diancxxb_id")+"')" + ",\n" +
							"(select max(id) from meikxxb where mingc ='" + mdrsl.getString("meikxxb_id") + "')" + ",\n" +
							"'"+ mdrsl.getString("yunsdwb_id2")+ "' ,\n" +
							"to_date('"+mdrsl.getString("daohrq")+"','yyyy-mm-dd') ,\n" +
							"to_date('"+mdrsl.getString("fahrq")+"','yyyy-mm-dd') ,\n" +
							""+mdrsl.getDouble("hetmj")+" ,"+mdrsl.getDouble("meij")+" ,"+mdrsl.getDouble("yunf")+" ,\n" +
							""+mdrsl.getDouble("hetbmdj")+" ,"+mdrsl.getDouble("kuangfbmdj")+" ,"+mdrsl.getDouble("rucbmdj")+" ,\n" +
							""+mdrsl.getDouble("biaoz")+" ,"+mdrsl.getDouble("jingz")+" ,"+mdrsl.getDouble("bangc")+" ,\n" +
							""+mdrsl.getDouble("hetrz")+" ,"+mdrsl.getDouble("kuangfrz")+" ,"+mdrsl.getDouble("rucrz")+" ,\n" +
							""+mdrsl.getDouble("jiesrz")+" ,"+mdrsl.getDouble("hetkfrzc")+" ,"+mdrsl.getDouble("kuangfrcrzc")+" ,"+mdrsl.getDouble("rucjsrzc")+",\n" +
							""+mdrsl.getDouble("kuid")+" ,"+mdrsl.getDouble("kuidje")+" ,"+mdrsl.getDouble("kuik")+" ,\n" +
							""+mdrsl.getDouble("kuikje")+" ,"+mdrsl.getDouble("kuidkkhjje")+" ,'"+mdrsl.getString("beiz")+"'," +
							""+mdrsl.getDouble("stad")+","+mdrsl.getString("fid")+","+mdrsl.getDouble("jiesbmdj")+");\n");
				
			} else {
				sql.append("update kuidkkb_wh set " +
						   "	hetmj="+mdrsl.getDouble("hetmj")+" ,\n" +
						   "	meij="+mdrsl.getDouble("meij")+" ,\n" +
						   "	yunf="+mdrsl.getDouble("yunf")+" ,\n" +
						   "	hetbmdj="+mdrsl.getDouble("hetbmdj")+" ,\n" +
						   "	kuangfbmdj="+mdrsl.getDouble("kuangfbmdj")+" ,\n" +
						   "	rucbmdj="+mdrsl.getDouble("rucbmdj")+" ,\n" +
						   "	kuangfl="+mdrsl.getDouble("biaoz")+" ,\n" +
						   "	rucl="+mdrsl.getDouble("jingz")+" ,\n" +
						   "	bangc="+mdrsl.getDouble("bangc")+" ,\n" +
						   "	hetrz="+mdrsl.getDouble("hetrz")+" ,\n" +
						   "	kuangfrz="+mdrsl.getDouble("kuangfrz")+" ,\n" +
						   "	rucrz="+mdrsl.getDouble("rucrz")+" ,\n" +
						   "	stad="+mdrsl.getDouble("stad")+" ,\n" +
						   "	jiesrz="+mdrsl.getDouble("jiesrz")+" ,\n" +
						   "	hetkfrzc="+mdrsl.getDouble("hetkfrzc")+" ,\n" +
						   "	kuangfrcrzc="+mdrsl.getDouble("kuangfrcrzc")+" ,\n" +
						   "	rucjsrzc="+mdrsl.getDouble("rucjsrzc")+" ,\n" +
						   "	kuid="+mdrsl.getDouble("kuid")+" ,\n" +
						   "	kuidje="+mdrsl.getDouble("kuidje")+" ,\n" +
						   "	kuik="+mdrsl.getDouble("kuik")+" ,\n" +
						   "	kuikje="+mdrsl.getDouble("kuikje")+" ,\n" +
						   "	kuidkkhjje="+mdrsl.getDouble("kuidkkhjje")+", \n" +
						   "	beiz='"+mdrsl.getString("beiz")+"' ,\n" +
						   "	jiesbmdj="+mdrsl.getDouble("jiesbmdj")+" \n" +
						   " where id=" + mdrsl.getString("id") + ";\n");
				
			}
		}
		mdrsl.close();
		if(sql.length()>0){
			if(con.getUpdate("begin\n" + sql.toString() + "end;") >= 0){
				setMsg("����ɹ���");
			}else {
				setMsg("����ʧ�ܣ�");
			}
		}else {
			setMsg("����ʧ�ܣ�");
		}
		con.Close();
	}

	private boolean _RefreshChick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}
	
	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	private boolean _CopyClick = false;

	public void CopyButton(IRequestCycle cycle) {
		_CopyClick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		} else if(_RefreshChick){
			_RefreshChick=false;
			getSelectData();
		} else if (_CopyClick) {
			_CopyClick = false;
			 Copysykj();//��������
			 getSelectData();
		}
	}
	
	
	public void Copysykj(){
		
		
		JDBCcon con = new JDBCcon();
		String updatesql="update kuidkkb_wh w set w.zhuangt=1 where daohrq=date'"+this.getRiq()+"'";
		int falg=con.getUpdate(updatesql);
		if(falg!=-1){
		    	setMsg("���������ɹ���");
	     }else{
		    	setMsg("��������ʧ�ܣ�");
		 }
		con.Close();
		
	}

	public void getSelectData() {
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) this.getPage().getVisit();
		
		String where ="";
		if("0" != getTreeid()){
			where = "	and fh.meikxxb_id=" + getTreeid() + "\n";
		}
		String dianctiaoj="";
		if(this.getChangbValue().getId()==300){
			dianctiaoj="  ";
		}else {
			dianctiaoj=" and f.diancxxb_id="+this.getChangbValue().getId();
		}
		
		// ��������
		String Riq = this.getRiq();
		//String Riq2 = this.getRiq2();
		
		String sql =
			"select a.id,a.fid,a.diancxxb_id,a.meikxxb_id,a.yunsdwb_id,a.yunsdwb_id2,a.daohrq,a.fahrq,\n" +
			"a.hetmj,a.meijlx,a.jiesrzlx,a.meij,a.yunf,\n" + 
			"decode(a.hetrz,0,0,round_new((a.hetmj/1.17+a.yunf*0.93)*7000/a.hetrz,2)) as hetbmdj,\n" + 
			"decode(a.kuangfrz,0,0,round((a.meij/1.17+a.yunf*0.93)*7000/a.kuangfrz,2)) as kuangfbmdj,\n" + 
			"decode(a.rucrz,0,0,round((a.meij/1.17+a.yunf*0.93)*7000/a.rucrz,2))  as rucbmdj,\n" + 
			"decode(a.jiesrz,0,0,round((a.meij/1.17+a.yunf*0.93)*7000/a.jiesrz,2))  as jiesbmdj,\n" + 
			"a.biaoz,a.jingz,decode(a.biaoz,0,0,round((a.biaoz-a.jingz)/a.biaoz*100,2)) as bangc,\n" + 
			"a.hetrz,a.kuangfrz,a.rucrz as rucrz,a.stad,a.jiesrz,(a.hetrz-a.kuangfrz) as hetkfrzc,\n" + 
			"(a.kuangfrz-a.rucrz) as kuangfrcrzc,(a.rucrz-a.jiesrz) as rucjsrzc,(a.biaoz-a.jingz) as kuid,\n" + 
			"decode(a.id,null,round((a.biaoz-a.jingz)*a.meij/1.17/10000,2),a.kuidje) as kuidje,\n" + 
			"decode(a.id,null,decode(a.jiesrz,0,0,decode(a.rucrz,0,0,round((round((a.meij/1.17+a.yunf*0.93)*7000/a.rucrz,0)-(a.meij/1.17+a.yunf*0.93)*7000/a.jiesrz)*a.rucrz/7000,2))),a.kuik) as kuik,\n" + 
			"decode(a.id,null,decode(a.jiesrz,0,0,round( decode(a.rucrz,0,0,round((round((a.meij/1.17+a.yunf*0.93)*7000/a.rucrz,0)-(a.meij/1.17+a.yunf*0.93)*7000/a.jiesrz)*a.rucrz/7000,2))*a.jingz/10000,2)),a.kuikje) as kuikje,\n" + 
			"decode(a.id,null,(round((a.biaoz-a.jingz)*a.meij/1.17/10000,2)+" +
			"decode(a.jiesrz,0,0,round( decode(a.rucrz,0,0,round((round((a.meij/1.17+a.yunf*0.93)*7000/a.rucrz,0)-(a.meij/1.17+a.yunf*0.93)*7000/a.jiesrz)*a.rucrz/7000,2))*a.jingz/10000,2))),a.kuidkkhjje) as kuidkkhjje,\n" + 
			"a.beiz as beiz\n" + 
			"from\n" + 
			"(select kk.id, fh.id as fid,d.mingc as diancxxb_id,mk.mingc as meikxxb_id,ys.mingc as yunsdwb_id,fh.yunsdwb_id as yunsdwb_id2,fh.daohrq,fh.fahrq,\n" + 
			"decode(kk.id,null, kuidkk(mk.id,to_date('"+Riq+"','yyyy-mm-dd'),'hetmj'),kk.hetmj) as hetmj,\n" + 
			"kuidkk_meij_leix(mk.id,to_date('"+Riq+"','yyyy-mm-dd')) as meijlx,\n"+
			"kuidkk_jiesrz_Leix(mk.id,to_date('"+Riq+"','yyyy-mm-dd')) as jiesrzlx,\n"+
			"decode(kk.id,null,kuidkk_meij(mk.id,to_date('"+Riq+"','yyyy-mm-dd'),\n" +
					"decode(kk.id,null,round(zl.qnet_ar*1000/4.1816,0),kk.rucrz),\n" +
					"decode(kk.id,null,round(kf.qnet_ar*1000/4.1816,0),kk.kuangfrz),\n" +
					"decode(kk.id,null,kuidkk(mk.id,to_date('"+Riq+"','yyyy-mm-dd'),'hetrz'),kk.hetrz)," +
					"fh.biaoz,fh.jingz),kk.meij) as meij,\n" + 
			"decode(kk.id,null,kuidkk_yunf(fh.diancxxb_id,fh.meikxxb_id,fh.yunsdwb_id,to_date('"+Riq+"','yyyy-mm-dd'),'yunf') ,kk.yunf) as yunf,\n" + 
			"fh.biaoz,fh.jingz,kk.kuidje,kk.kuik,kk.kuikje,kk.kuidkkhjje,\n" + 
			"decode(kk.id,null,kuidkk(mk.id,to_date('"+Riq+"','yyyy-mm-dd'),'hetrz'),kk.hetrz) as hetrz,\n" + 
			"decode(kk.id,null,round(kf.qnet_ar*1000/4.1816,0),kk.kuangfrz) as kuangfrz,\n" + 
			"decode(kk.id,null,round(zl.qnet_ar*1000/4.1816,0),kk.rucrz)  as rucrz,\n" + 
			"decode(kk.id,null,zl.stad,kk.stad)  as stad,\n"+
			"decode(kk.id,null,kuidkk_jiesrz(mk.id,to_date('"+Riq+"','yyyy-mm-dd'),decode(kk.id,null,round(zl.qnet_ar*1000/4.1816,0),kk.rucrz),\n" + 
			"  decode(kk.id,null,round(kf.qnet_ar*1000/4.1816,0),kk.kuangfrz),\n" +
			"  decode(kk.id,null,kuidkk(mk.id,to_date('"+Riq+"','yyyy-mm-dd'),'hetrz'),kk.hetrz),fh.biaoz,fh.jingz,'jiesrz'),kk.jiesrz) as jiesrz,kk.beiz\n" + 
			"from\n" + 
			"( select * from ( select f.* ,\n" + 
			" nvl((select (select ys.id  from yunsdwb ys where ys.id=c.yunsdwb_id )\n" + 
			"  from chepb c where c.fahb_id=f.id and rownum=1),0)  yunsdwb_id\n" + 
			"  from fahb f where f.daohrq>=to_date('"+Riq+"','yyyy-mm-dd')\n" + 
			" and  f.daohrq<=to_date('"+Riq+"','yyyy-mm-dd')  "+dianctiaoj+"\n" + 
			" ))fh,zhilb zl,meikxxb mk,kuangfzlb kf,vwdianc d,kuidkkb_wh kk,yunsdwb ys\n" + 
			"where fh.zhilb_id=zl.id(+)\n" + 
			"and fh.kuangfzlb_id=kf.id(+)\n" + 
			"and fh.diancxxb_id=d.id\n" + 
			"and fh.meikxxb_id=mk.id\n" + 
			"and fh.yunsdwb_id=ys.id(+)\n" + 
			"and fh.id=kk.fahb_id(+)\n"+
			"order by fh.yunsfsb_id,mk.mingc,ys.mingc) a";






		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		// //���ñ��������ڱ���
		egu.setTableName("kuidkkb_wh");
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.setHeight("bodyHeight");
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);//�趨grid���Ա༭
		//���ö�ѡ��
		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
//		egu.addColumn(1,new GridColumn(GridColumn.ColType_Check));
		// /������ʾ������
		
		egu.getColumn("id").setHeader("id");
		egu.getColumn("id").setEditor(null);
		egu.getColumn("id").setHidden(true);
		
		egu.getColumn("fid").setHeader("fid");
		egu.getColumn("fid").setEditor(null);
		egu.getColumn("fid").setHidden(true);
		
		egu.getColumn("diancxxb_id").setHeader("�糧");
		egu.getColumn("diancxxb_id").setWidth(100);
		egu.getColumn("diancxxb_id").setEditor(null);
		
		egu.getColumn("meikxxb_id").setHeader("ú��");
		egu.getColumn("meikxxb_id").setWidth(150);
		egu.getColumn("meikxxb_id").setEditor(null);
		
		egu.getColumn("yunsdwb_id").setHeader("���䵥λ");
		egu.getColumn("yunsdwb_id").setWidth(120);
		egu.getColumn("yunsdwb_id").setEditor(null);
		
		egu.getColumn("yunsdwb_id2").setHeader("���䵥λid");
		egu.getColumn("yunsdwb_id2").setHidden(true);
		egu.getColumn("yunsdwb_id2").setEditor(null);
		
		egu.getColumn("daohrq").setHeader("��������");
		egu.getColumn("daohrq").setEditor(null);
		egu.getColumn("daohrq").setWidth(90);
		
		egu.getColumn("fahrq").setHeader("��������");
		egu.getColumn("fahrq").setEditor(null);
		egu.getColumn("fahrq").setWidth(90);
		
		egu.getColumn("hetmj").setHeader("��ͬú��");
		egu.getColumn("hetmj").setWidth(60);
		
		egu.getColumn("meijlx").setHeader("ú������");
		egu.getColumn("meijlx").setWidth(60);
		egu.getColumn("meijlx").setEditor(null);
		egu.getColumn("meijlx").setHidden(true);
		
		egu.getColumn("jiesrzlx").setHeader("������ֵ����");
		egu.getColumn("jiesrzlx").setWidth(60);
		egu.getColumn("jiesrzlx").setEditor(null);
		egu.getColumn("jiesrzlx").setHidden(true);
		
		egu.getColumn("meij").setHeader("ú��");
		egu.getColumn("meij").setWidth(60);
		egu.getColumn("meij").setEditor(null);
		
		egu.getColumn("yunf").setHeader("�˼�");
		egu.getColumn("yunf").setWidth(60);
		
		egu.getColumn("hetbmdj").setHeader("��ͬ��<br>ú����");
		egu.getColumn("hetbmdj").setWidth(60);
		egu.getColumn("hetbmdj").setEditor(null);
		
		egu.getColumn("kuangfbmdj").setHeader("�󷽱�<br>ú����");
		egu.getColumn("kuangfbmdj").setWidth(60);
		egu.getColumn("kuangfbmdj").setEditor(null);
		
		egu.getColumn("rucbmdj").setHeader("�볧��<br>ú����");
		egu.getColumn("rucbmdj").setWidth(60);
		egu.getColumn("rucbmdj").setEditor(null);
		
		egu.getColumn("jiesbmdj").setHeader("�����<br>ú����");
		egu.getColumn("jiesbmdj").setWidth(60);
		egu.getColumn("jiesbmdj").setEditor(null);
		
		egu.getColumn("biaoz").setHeader("����");
		egu.getColumn("biaoz").setWidth(60);
		egu.getColumn("biaoz").setEditor(null);
		
		egu.getColumn("jingz").setHeader("�볧��");
		egu.getColumn("jingz").setEditor(null);
		egu.getColumn("jingz").setWidth(60);  
		
		egu.getColumn("bangc").setHeader("����(%)");
		egu.getColumn("bangc").setWidth(60);
		egu.getColumn("bangc").setEditor(null);
		
		egu.getColumn("hetrz").setHeader("��ͬ��ֵ");
		egu.getColumn("hetrz").setWidth(60);
		
		egu.getColumn("kuangfrz").setHeader("����ֵ");
		egu.getColumn("kuangfrz").setWidth(60);
		
		egu.getColumn("rucrz").setHeader("�볧��ֵ");
		egu.getColumn("rucrz").setWidth(60);
		egu.getColumn("rucrz").setEditor(null);
		
		egu.getColumn("stad").setHeader("�볧���");
		egu.getColumn("stad").setWidth(60);
		egu.getColumn("stad").setEditor(null);
		
		egu.getColumn("jiesrz").setHeader("������ֵ");
		egu.getColumn("jiesrz").setWidth(60);
		egu.getColumn("jiesrz").setEditor(null);
		
		egu.getColumn("hetkfrzc").setHeader("��ͬ��<br>��ֵ��");
		egu.getColumn("hetkfrzc").setEditor(null);
		egu.getColumn("hetkfrzc").setWidth(60);
		
		egu.getColumn("kuangfrcrzc").setHeader("���볧<br>��ֵ��");
		egu.getColumn("kuangfrcrzc").setWidth(60);
		egu.getColumn("kuangfrcrzc").setEditor(null);
		
		egu.getColumn("rucjsrzc").setHeader("�볧����<br>��ֵ��");
		egu.getColumn("rucjsrzc").setWidth(60);
		egu.getColumn("rucjsrzc").setEditor(null);
		
		egu.getColumn("kuid").setHeader("����");
		egu.getColumn("kuid").setWidth(60);
		egu.getColumn("kuid").setEditor(null);
		
		egu.getColumn("kuidje").setHeader("���ֽ��");
		egu.getColumn("kuidje").setWidth(70);
		((NumberField)egu.getColumn("kuidje").editor).setDecimalPrecision(2);
		//egu.getColumn("kuidje").setEditor(null);
		
		egu.getColumn("kuik").setHeader("����");
		egu.getColumn("kuik").setWidth(70);
		((NumberField)egu.getColumn("kuik").editor).setDecimalPrecision(2);
		//egu.getColumn("kuik").setEditor(null);
		
		egu.getColumn("kuikje").setHeader("�������");
		egu.getColumn("kuikje").setWidth(70);
		egu.getColumn("kuikje").setEditor(null);
		
		egu.getColumn("kuidkkhjje").setHeader("���ֿ����ϼ�");
		egu.getColumn("kuidkkhjje").setWidth(100);
		egu.getColumn("kuidkkhjje").setEditor(null);
		
		egu.getColumn("beiz").setHeader("��ע");
		egu.getColumn("beiz").setWidth(60);
		
		
		egu.addPaging(1000);
		
		

		// ********************������************************************************

		egu.addTbarText("��������:");
		DateField df = new DateField();
		df.setValue(this.getRiq());
		//df.Binding("RIQ", "");
		df.Binding("RIQ", "forms[0]");
		egu.addToolbarItem(df.getScript());
		/*egu.addTbarText("��");

		DateField df2 = new DateField();
		df2.setValue(this.getRiq2());
		df2.Binding("RIQ2", "");
		egu.addToolbarItem(df2.getScript());*/
		
		egu.addTbarText("-");
		// ������
		//egu.addTbarText(Locale.gongysb_id_fahb);
		String condition=" and daohrq=to_date('"+Riq+"','yyyy-MM-dd') \n " ;		
		ExtTreeUtil etu = new ExtTreeUtil("gongysTree",
				ExtTreeUtil.treeWindowCheck_gongys, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid(),condition);
		setTree(etu);
		//egu.addTbarTreeBtn("gongysTree");
		
//		�ֳ���
		egu.addTbarText("-");
		egu.addTbarText("��λ:");
		ComboBox comb = new ComboBox();
		comb.setTransform("ChangbDropDown");
		comb.setId("Changb");
		comb.setEditable(false);
		comb.setLazyRender(true);// ��̬��
		comb.setWidth(100);
		comb.setReadOnly(true);
		egu.addToolbarItem(comb.getScript());
		egu.addOtherScript("Changb.on('select',function(){document.forms[0].submit();});");
		
		//ҳ��js���㿪ʼ
		
		egu.addOtherScript(" gridDiv_grid.on('afteredit',function(e){ " +
					" if(e.field=='HETMJ' || e.field=='MEIJ' || e.field=='YUNF'|| e.field=='HETRZ'|| e.field=='KUANGFRZ'|| e.field=='RUCRZ'|| e.field=='JIESRZ'){ " +
					"     if(e.field=='HETRZ'|| e.field=='KUANGFRZ'||e.field=='RUCRZ'||e.field=='HETMJ'||e.field=='JIESRZ'){\n"+
					"        //��������ֵ�仯ʱ,������ֵ����,���¼�����������ֵ        \n"+
					"        if(e.record.get('JIESRZLX')==0){                                                  \n"+
					"              e.record.set('JIESRZ',eval(e.record.get('RUCRZ')||0));  \n                                               "+
					"        }else if(e.record.get('JIESRZLX')==1){\n"+
					"              e.record.set('JIESRZ',eval(e.record.get('KUANGFRZ')||0));  \n                                    "+
					"        }else if(e.record.get('JIESRZLX')==2){\n"+
					"               e.record.set('JIESRZ',Round((eval(e.record.get('KUANGFRZ')||0)*eval(e.record.get('BIAOZ')||0)+eval(e.record.get('RUCRZ')||0)*eval(e.record.get('JINGZ')||0))/(eval(e.record.get('BIAOZ')||0)+eval(e.record.get('JINGZ')||0)),0));    \n                                    "+
					"        }else if (e.record.get('JIESRZLX')==3){\n    "+
					"             e.record.set('JIESRZ',eval(e.record.get('HETRZ')||0));                        \n"+
					"        }"+
					"       \n"+
					"        //����ú�۵�5�ַ�ʽ,��5�ַ�ʽ����meijfsb��\n"+
					"        if(e.record.get('MEIJLX')==1){                                                     \n"+
					"              e.record.set('MEIJ',eval(e.record.get('HETMJ')||0));                         \n"+
					"        }else if (e.record.get('MEIJLX')==2){                                              \n"+
					"              e.record.set('MEIJ',Round(eval(e.record.get('HETMJ')||0)-(eval(e.record.get('HETRZ')||0)-eval(e.record.get('JIESRZ')||0))*0.1,2));     \n"+
					"        }else if (e.record.get('MEIJLX')==3){                                               \n"+
					"               e.record.set('MEIJ',Round(eval(e.record.get('HETMJ')||0)-(eval(e.record.get('HETRZ')||0)-eval(e.record.get('JIESRZ')||0))*0.08,2));     \n"+
					"        }else if (e.record.get('MEIJLX')==4){                                               \n"+
					"               e.record.set('MEIJ',Round(eval(e.record.get('HETMJ')||0)-(eval(e.record.get('HETRZ')||0)-100-eval(e.record.get('JIESRZ')||0))*0.1,2));     \n"+
					"        }else if (e.record.get('MEIJLX')==5){                                               \n"+
					"               e.record.set('MEIJ',Round(eval(e.record.get('HETMJ')||0)-(eval(e.record.get('HETRZ')||0)-100-eval(e.record.get('JIESRZ')||0))*0.08,2));     \n"+
					"        }\n"+
					"     }\n"+
					
					"    //��ͬ��ú���� " +"\n"+
					"    if(eval(e.record.get('HETRZ')||0)==0){"+
					"        e.record.set('HETBMDJ',0);   \n   "+
					"    }else {" +
					"      e.record.set('HETBMDJ',Round((eval(e.record.get('HETMJ')||0)/1.17+eval(e.record.get('YUNF')||0)*0.93)*7000/eval(e.record.get('HETRZ')||0),2)); " +
					"    }" +
					"    //�󷽱�ú���� " +"\n"+
					"    if(eval(e.record.get('KUANGFRZ')||0)==0){"+
					"        e.record.set('KUANGFBMDJ',0);   \n   "+
					"    }else {" +
					"      e.record.set('KUANGFBMDJ',Round((eval(e.record.get('MEIJ')||0)/1.17+eval(e.record.get('YUNF')||0)*0.93)*7000/eval(e.record.get('KUANGFRZ')||0),2)); " +
					"    }" +
					"    //�볧��ú���� " +"\n"+
					"    if(eval(e.record.get('RUCRZ')||0)==0){"+
					"        e.record.set('RUCBMDJ',0);   \n   "+
					"    }else {" +
					"      e.record.set('RUCBMDJ',Round((eval(e.record.get('MEIJ')||0)/1.17+eval(e.record.get('YUNF')||0)*0.93)*7000/eval(e.record.get('RUCRZ')||0),2)); " +
					"    }" +
					"    //�����ú���� " +"\n"+
					"    if(eval(e.record.get('JIESRZ')||0)==0){"+
					"        e.record.set('JIESBMDJ',0);   \n   "+
					"    }else {" +
					"      e.record.set('JIESBMDJ',Round((eval(e.record.get('MEIJ')||0)/1.17+eval(e.record.get('YUNF')||0)*0.93)*7000/eval(e.record.get('JIESRZ')||0),2)); " +
					"    }" +
					"    //��ͬ����ֵ�� " +"\n"+
					"      e.record.set('HETKFRZC',eval(e.record.get('HETRZ')||0)-eval(e.record.get('KUANGFRZ')||0)); " +
					"    //���볧��ֵ�� " +"\n"+
					"      e.record.set('KUANGFRCRZC',eval(e.record.get('KUANGFRZ')||0)-eval(e.record.get('RUCRZ')||0)); " +
					"    //�볧������ֵ�� " +"\n"+
					"      e.record.set('RUCJSRZC',eval(e.record.get('RUCRZ')||0)-eval(e.record.get('JIESRZ')||0)); " +
					"    //���ֽ�� " +"\n"+
					"      e.record.set('KUIDJE',Round(eval(e.record.get('KUID')||0)*eval(e.record.get('MEIJ')||0)/1.17/10000,2)); " +
					"    //�������� " +"\n"+
					"    if(eval(e.record.get('JIESRZ')||0)==0){"+
					"        e.record.set('KUIK',0);   \n   "+
					"    }else {" +
					"      e.record.set('KUIK',Round((eval(e.record.get('RUCBMDJ')||0)-(eval(e.record.get('MEIJ')||0)/1.17+eval(e.record.get('YUNF')||0)*0.93)*7000/eval(e.record.get('JIESRZ')||0))*eval(e.record.get('RUCRZ')||0)/7000,2));"+   
					"    }" +
					"    //������� " +"\n"+
					"      e.record.set('KUIKJE',Round(eval(e.record.get('KUIK')||0)*eval(e.record.get('JINGZ')||0)/10000,2)); " +
					"    //���ֿ���������� " +"\n"+
					"      e.record.set('KUIDKKHJJE',Round(eval(e.record.get('KUIDJE')||0)+eval(e.record.get('KUIKJE')||0),2)); " +
					
					" }  " +
					
					"if(e.field=='KUIDJE'||e.field=='KUIK'){"+//��������޸Ŀ��ֽ��ʱ,���ֿ����ϼ����¼���
					"      e.record.set('KUIKJE',Round(eval(e.record.get('KUIK')||0)*eval(e.record.get('JINGZ')||0)/10000,2)); " +
					"      e.record.set('KUIDKKHJJE',Round(eval(e.record.get('KUIDJE')||0)+eval(e.record.get('KUIKJE')||0),2)); " +
					"}"+
				 
				
				" } );");
		
		
		//ҳ��js�������
		
		
		

//		����Toolbar��ť	
		egu.addTbarText("-");
		GridButton gRefresh = new GridButton("ˢ��",
				"function(){document.getElementById('RefreshButton').click();}");
		gRefresh.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gRefresh);
		
		
		
		
		
		
		
		if(getShujzt(con)){
			egu.addTbarText("-");
			egu.addTbarText("���������Ѿ�����,�������޸ĺ�ɾ��!");
		}else{
			egu.addTbarText("-");
			egu.addToolbarButton(GridButton.ButtonType_Delete, null);
			
			egu.addTbarText("-");
			egu.addToolbarButton(GridButton.ButtonType_SaveAll, "SaveButton");
			
			egu.addTbarText("-");
			egu.addTbarText("-");
			egu.addTbarText("->");
			
			egu.addTbarText("-");
			GridButton gbt = new GridButton("��������","function(){document.getElementById('CopyButton').click();}");
			gbt.setIcon(SysConstant.Btn_Icon_SelSubmit);
			egu.addTbarBtn(gbt);
		}
		
		
		
		
		setExtGrid(egu);
		con.Close();
	}
	
	public boolean getShujzt(JDBCcon con){
		boolean isShenh=false;
		String sql="select zhuangt from kuidkkb_wh where daohrq=date'"+this.getRiq()+"' order by zhuangt desc ";
		ResultSetList rs2=con.getResultSetList(sql);
		if(rs2.next()){
			if(rs2.getInt("zhuangt")==1){
				isShenh=true;
			}
		}
		return isShenh;
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
			init();
		}
		getSelectData();
	}
	
	private void init() {
		
		((Visit) getPage().getVisit()).setString1("");			//riq1
		((Visit) getPage().getVisit()).setString3("");			//riq2
		((Visit) getPage().getVisit()).setString2("");			//Treeid
		((Visit) getPage().getVisit()).setboolean1(false);		//����
		this.setChangbModel(null);		//IPropertySelectionModel1
		this.setChangbValue(null);		//IDropDownBean1
		this.getChangbModels();
		
		this.setTree(null);
		this.setTreeid("0");
	}
	
//	����
	public IDropDownBean getChangbValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean1() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean1((IDropDownBean) getChangbModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean1();
	}
	public void setChangbValue(IDropDownBean Value) {
		if(Value!=((Visit) getPage().getVisit()).getDropDownBean1()){
			((Visit) getPage().getVisit()).setboolean1(true);
		}
		((Visit) getPage().getVisit()).setDropDownBean1(Value);
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
		String sql ="select id,mingc from diancxxb d order by id";
		((Visit) getPage().getVisit())
		.setProSelectionModel1(new IDropDownModel(sql));
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}
//	����_End
	
	public String getTreeid() {
		if(((Visit) getPage().getVisit()).getString2()==null||((Visit) getPage().getVisit()).getString2().equals("")){
			((Visit) getPage().getVisit()).setString2("0");
		}
		return ((Visit) getPage().getVisit()).getString2();
	}

	public void setTreeid(String treeid) {
		if(!((Visit) getPage().getVisit()).getString2().equals(treeid)){
			((Visit) getPage().getVisit()).setString2(treeid);
			((Visit) getPage().getVisit()).setboolean2(true);
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
