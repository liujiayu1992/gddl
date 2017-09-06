package com.zhiren.jt.zdt.shengcdy.diaoyxxcx;

import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import java.io.File;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;

import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.tools.FtpUpload;

import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;

import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import com.zhiren.common.DateUtil;
import com.zhiren.common.tools.FtpCreatTxt;


import org.apache.tapestry.contrib.palette.SortMode;

/**
 * chenzt
 * 2010-04-06
 * �������޸ĺӱ��ֹ�˾������Ʊ���һ�� ����������Ϊû�У�null��
 */

public class Diaoyxxcx  extends BasePage implements PageValidateListener{
//	�ж��Ƿ��Ǽ����û�
	public boolean isJitUserShow() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 1;// ����
	}

	public boolean isGongsUser() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 2;// ��˾
	}

	public boolean isDiancUser() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 3;// �糧
	}
	public void pageValidate(PageEvent arg0) {
		String PageName = arg0.getPage().getPageName();
		String ValPageName = Login.ValidateLogin(arg0.getPage());
		if (!PageName.equals(ValPageName)) {
			ValPageName = Login.ValidateAdmin(arg0.getPage());
			if(!PageName.equals(ValPageName)) {
				IPage ipage = arg0.getRequestCycle().getPage(ValPageName);
				throw new PageRedirectException(ipage);
			}
		}
	}
	public String getDiancName() {
		JDBCcon con = new JDBCcon();
		ResultSet rs;
		String diancmc = "";
		// long diancID = -1;
		String sql = "select dc.id,dc.quanc as mingc from diancxxb dc where dc.id="
			+ ((Visit) getPage().getVisit()).getDiancxxb_id();
		try {
			rs = con.getResultSet(sql);
			while (rs.next()) {
				// diancID = rs.getLong("id");
				diancmc = rs.getString("mingc");
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		return diancmc;
	}
	private String userName="";

	public void setUserName(String value) {
		userName=((Visit) getPage().getVisit()).getRenymc();
	}
	public String getUserName() {
		return userName;
	}

	private String OraDate(Date _date){
		if (_date == null) {
			return "to_date('"+DateUtil.Formatdate("yyyy-MM-dd", new Date())+"','yyyy-mm-dd')";
		}
		return "to_date('"+DateUtil.Formatdate("yyyy-MM-dd", _date)+"','yyyy-mm-dd')";
	}


	//��ʼ����v
	private Date _BeginriqValue = DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay);
	private boolean _BeginriqChange=false;
	public Date getBeginriqDate() {
		if (_BeginriqValue==null){
			_BeginriqValue =DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay);
		}
		return _BeginriqValue;
	}

	public void setBeginriqDate(Date _value) {
		if (FormatDate(_BeginriqValue).equals(FormatDate(_value))) {
			_BeginriqChange=false;
		} else {
			_BeginriqValue = _value;
			_BeginriqChange=true;
		}
		((Visit) getPage().getVisit()).setDate2(_BeginriqValue);
	}

	private String _msg;

	public void setMsg(String _value) {
		_msg = _value;
	}

	public String getMsg() {
		if (_msg == null) {
			_msg = "";
		}
		return _msg;
	}

	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	private boolean _ShangcChick = false;

	public void ShangcButton(IRequestCycle cycle) {
		_ShangcChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			Refurbish();
		}
		if(_ShangcChick){
			_ShangcChick=false;
		}
	}


	private void Refurbish() {
		//Ϊ"ˢ��" ��ť��Ӵ������
		isBegin=true;
		if(getBaoblxValue()!=null){
			if(getBaoblxValue().getValue().equals("װ��Ԥ��")){
				getZhuangcyb();
			}else if(getBaoblxValue().getValue().equals("��վԤ��")){
				getDaozyb();
			}else if(getBaoblxValue().getValue().equals("ȡ���ſ�")){
				getQuzpk();
			}
		}
	}

//	******************ҳ���ʼ����********************//
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {

		Visit visit = (Visit) getPage().getVisit();

		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());

			_BeginriqValue = DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay);
			visit.setDate2(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay));
			isBegin=true;
			visit.setList1(null);
			visit.setDropDownBean1(null);
			visit.setProSelectionModel1(null);
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
			this.setBaoblxValue(null);
			this.getIBaoblxModels();
			this.setTreeid(null);
//			this.getShouhcrb();
//			this.getSelectData();

		}
		if (_BeginriqChange){
			_BeginriqChange=false;
			Refurbish();
		}
		if(_Baoblxchange){
			_Baoblxchange=false;
			Refurbish();
		}
		if(_fengschange){
			_fengschange=false;
			Refurbish();
		}
		setUserName(visit.getRenymc());
		getToolBars();
		Refurbish();
	}
	private String RT_HET="zhuangcyb";
	private String mstrReportName="zhuangcyb";
	public String getPrintTable(){
		if(!isBegin){
			return "";
		}
		isBegin=false;
		if (mstrReportName.equals(RT_HET)){
			if(getBaoblxValue()!=null){
				if(getBaoblxValue().getValue().equals("װ��Ԥ��")){
					return getZhuangcyb();
				}else if(getBaoblxValue().getValue().equals("��վԤ��")){
					return getDaozyb();
				}else if(getBaoblxValue().getValue().equals("ȡ���ſ�")){
					return getQuzpk();
				}
			}
			return "";
		}else{
			return "�޴˱���";
		}
	}

	public int getZhuangt() {
		return 1;
	}

	private int intZhuangt=0;
	public void setZhuangt(int _value) {
		intZhuangt=1;
	}

	private boolean isBegin=false;

	/**
	 * ���缯�ŵ�ú��Ϣ�ձ���
	 * @author xzy
	 */
	private String getZhuangcyb(){//װ��Ԥ�� leix��0
		_CurrentPage=1;
		_AllPages=1;
		JDBCcon cn = new JDBCcon();
		String riq=OraDate(_BeginriqValue);//��ǰ����
		String riq1=FormatDate(_BeginriqValue);
		//�����ͷ����
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		String titlename="װ��Ԥ��";
		int iFixedRows=0;//�̶��к�
		int iCol=0;//����
		String danwmc="";//��������
		String strGongsID = "";
		int jib=this.getDiancTreeJib();
		if(jib==1){//ѡ����ʱˢ�³����еĵ糧
			strGongsID=" ";
			danwmc="���Ź�˾�ϼ�";
		}else if (jib==2){//ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
			strGongsID = "  and (dc.fuid=  " +this.getTreeid()+" or dc.shangjgsid="+this.getTreeid()+")";
			danwmc=getTreeDiancmc(this.getTreeid())+"�ϼ�";
		}else if (jib==3){//ѡ�糧ֻˢ�³��õ糧
			strGongsID=" and dc.id= " +this.getTreeid();

		}else if (jib==-1){
			strGongsID=" and dc.id = "+((Visit) getPage().getVisit()).getDiancxxb_id();
		}
		//danwmc=getTreeDiancmc(this.getTreeid());
		//��������
		titlename=titlename+"";

		StringBuffer grouping_sql = new StringBuffer();
		StringBuffer where_sql = new StringBuffer();
		StringBuffer rollup_sql = new StringBuffer();
		StringBuffer having_sql = new StringBuffer();
		StringBuffer orderby_sql = new StringBuffer();

		StringBuffer strSQL = new StringBuffer();


		if (jib==1) {//ѡ����ʱˢ�³����еĵ糧
			grouping_sql.append(" select getLinkMingxTaiz('1',decode(grouping(dc.mingc),1,-1,max(dc.id)),decode(grouping(dc.mingc)+grouping(dc.fengs),2,'�ܼ�',1,dc.fengs,'&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc)) as danw,gy.mingc,cz.mingc,\n");

			where_sql.append(" ");
			rollup_sql.append(" group by rollup (dc.fengs,dc.mingc,gy.mingc,cz.mingc,zb.yujddsj) ");
			having_sql.append(" having not (grouping(dc.mingc) || grouping(zb.yujddsj) )=1 ");
			orderby_sql.append("  order by grouping(dc.fengs) desc ,dc.fengs,grouping(dc.mingc) desc ,max(dc.xuh1) ");
		}else if(jib==2) {//ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧

			String ranlgs = "select id from diancxxb where shangjgsid= "+this.getTreeid();

			try{
				ResultSetList rl = cn.getResultSetList(ranlgs);
				if(rl.getRows()!=0){//ȼ�Ϲ�˾
					grouping_sql.append(" select getLinkMingxTaiz('1',decode(grouping(dc.mingc),1,-1,max(dc.id)),decode(grouping(dc.quygs)+grouping(dc.fengs)+grouping(dc.mingc),3,'�ܼ�',\n");
					grouping_sql.append("2,dc.quygs,1,'&nbsp;&nbsp;'||dc.fengs,'&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc)) as danw,gy.mingc,cz.mingc,\n");

					where_sql.append(" and (dc.fuid=  " +this.getTreeid()+" or dc.shangjgsid= "+this.getTreeid()+") ").append("\n");
					rollup_sql.append(" group by rollup (dc.quygs,dc.fengs,dc.mingc,gy.mingc,cz.mingc,zb.yujddsj) ");
					having_sql.append(" having not (grouping(dc.quygs) || grouping(zb.yujddsj) )=1\n");
					orderby_sql.append(" order by grouping(dc.quygs) desc,dc.quygs,grouping(dc.fengs) desc ,dc.fengs,grouping(dc.mingc) desc,dc.mingc,max(dc.xuh1)\n ");
				}else{
					grouping_sql.append(" select getLinkMingxTaiz('1',decode(grouping(dc.mingc),1,-1,max(dc.id)),decode(grouping(dc.fengs)+grouping(dc.mingc),2,'�ܼ�',\n");
					grouping_sql.append("1,dc.fengs,'&nbsp;&nbsp;'||dc.mingc)) as danw,gy.mingc,cz.mingc,\n");

					where_sql.append(" and (dc.fuid=  " +this.getTreeid()+" or dc.shangjgsid= "+this.getTreeid()+") ").append("\n");
					rollup_sql.append(" group by rollup (dc.fengs,dc.mingc,gy.mingc,cz.mingc,zb.yujddsj) ");
					having_sql.append(" having not (grouping(dc.fengs) || grouping(zb.yujddsj) )=1 \n");
					orderby_sql.append(" order by grouping(dc.fengs) desc ,dc.fengs,grouping(dc.mingc) desc,dc.mingc,max(dc.xuh1)\n ");
				}
				rl.close();

			}catch(Exception e){
				e.printStackTrace();
			}finally{
				cn.Close();
			}

		}else{//ѡ��糧
			grouping_sql.append(" select getLinkMingxTaiz('1',decode(grouping(dc.mingc),1,-1,max(dc.id)),decode(grouping(dc.mingc),1,'�ܼ�',\n");
			grouping_sql.append("dc.mingc)) as danw,gy.mingc,cz.mingc,\n");

			where_sql.append(" and dc.id=").append(this.getTreeid()).append("  and riq="+riq+" \n");
			rollup_sql.append(" group by rollup (dc.mingc,gy.mingc,cz.mingc,zb.yujddsj) \n");
			having_sql.append(" having not (grouping(dc.mingc) || grouping(zb.yujddsj) )=1 \n");
			orderby_sql.append(" order by grouping(dc.mingc) desc,dc.mingc,max(dc.xuh1)\n ");
		}

		strSQL.append(grouping_sql.toString());
		strSQL.append("sum(zb.zuorzc) as zuorzc,to_char(zb.yujddsj,'yyyy-mm-dd') as yujddsj,sum(zb.jinrcr) as jinrcr,sum(zb.mingrqc) as mingrqc\n");//,sum(zb.mingrsd) as mingrsd


		strSQL.append("from zhuangcyb zb,gongysb gy,chezxxb cz, \n");


		strSQL.append("  ( select d.id,d.xuh as xuh1,d.mingc,d.jingjcml ,d.rijhm,dc.mingc as fengs,sf.id as quygsid,sf.mingc as quygs,d.xuh,dc.id as fuid,d.shangjgsid\n");
		strSQL.append(" from diancxxb d, diancxxb dc ,diancxxb sf\n");
		strSQL.append(" where d.jib = 3\n");
		strSQL.append(" and d.fuid=dc.id(+)  and d.shangjgsid=sf.id(+)) dc\n");

		strSQL.append(" where dc.id=zb.diancxxb_id(+) and zb.gongysb_id=gy.id(+) and zb.chezxxb_id=cz.id  and riq="+riq+"\n");

		strSQL.append(where_sql.toString());
		strSQL.append(rollup_sql.toString());
		strSQL.append(having_sql.toString());
		strSQL.append(orderby_sql.toString());





		ArrHeader =new String[1][7];
		ArrHeader[0]=new String[] {"��λ","ú��","��վ","����ʵ��<br>װ����","Ԥ�Ƶ���ʱ��","���ճ��ϳ���","�����복��"};//,"����ʵ������"

		ArrWidth=new int[] {150,150,100,80,80,80,80};


		ResultSet rs = cn.getResultSet(strSQL.toString());

		// ����
		//rt.setBody(new Table(rs,1, 0, 1));


		Table tb=new Table(rs,1, 0, 1);
		rt.setBody(tb);

		rt.setTitle(titlename, ArrWidth);

		rt.setDefaultTitle(1, 2, "�Ʊ�λ:"+((Visit) getPage().getVisit()).getDiancmc(), Table.ALIGN_LEFT);
		rt.setDefaultTitle(3, 2, riq1, Table.ALIGN_CENTER);
		rt.setDefaultTitle(6, 2, "��λ:��",Table.ALIGN_RIGHT);

		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(18);
		rt.body.setHeaderData(ArrHeader);// ��ͷ����
		rt.body.mergeFixedRow();

		rt.body.mergeFixedCols();
		rt.body.ShowZero = true;
		rt.body.setColAlign(2, Table.ALIGN_LEFT);
		rt.body.setColAlign(3, Table.ALIGN_LEFT);
		rt.body.ShowZero = false;

		if(jib==1){
			if(rt.body.getRows()>1){
				rt.body.setCellAlign(2, 1, Table.ALIGN_CENTER);
			}
		}

		//ҳ�� 

		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1,2,"�Ʊ�ʱ��:"+DateUtil.FormatDate(new Date()),Table.ALIGN_LEFT);
		rt.setDefautlFooter(3,2,"���:",Table.ALIGN_CENTER);
		if(((Visit) getPage().getVisit()).getDiancmc().equals("�ӱ�����")){
			
			rt.setDefautlFooter(6,2, "�Ʊ�:",Table.ALIGN_RIGHT);
			}else{
				
				rt.setDefautlFooter(6,2, "�Ʊ�:"+((Visit) getPage().getVisit()).getDiancmc

	(),Table.ALIGN_RIGHT);
			}
//		rt.setDefautlFooter(6,2,"�Ʊ�:",Table.ALIGN_RIGHT);
	//	rt.setDefautlFooter(rt.footer.getCols()-1,2,Table.PAGENUMBER_NORMAL,Table.ALIGN_RIGHT);



		//����ҳ��
//		rt.createDefautlFooter(ArrWidth);

		_CurrentPage=1;
		_AllPages=rt.body.getPages();
		if (_AllPages==0){
			_CurrentPage=0;
		}
		cn.Close();
		return rt.getAllPagesHtml();

	}
//	******************************************************************************

//	���缯�ŵ�ú��Ϣ�ձ���
	private String getDaozyb(){//��վԤ�� leix��1
		String strSQL="";
		_CurrentPage=1;
		_AllPages=1;
		JDBCcon cn = new JDBCcon();
		String riq=OraDate(_BeginriqValue);//��ǰ����
		String riq1=FormatDate(_BeginriqValue);
		//�����ͷ����
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		String titlename="��վԤ��";
		int iFixedRows=0;//�̶��к�
		int iCol=0;//����
		String danwmc="";//��������
		String strGongsID = "";
		int jib=this.getDiancTreeJib();
		if(jib==1){//ѡ����ʱˢ�³����еĵ糧
			strGongsID=" ";
			danwmc="���Ź�˾�ϼ�";
		}else if (jib==2){//ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
			strGongsID = "  and (dc.fuid=  " +this.getTreeid()+" or dc.shangjgsid="+this.getTreeid()+")";
			danwmc=getTreeDiancmc(this.getTreeid())+"�ϼ�";
		}else if (jib==3){//ѡ�糧ֻˢ�³��õ糧
			strGongsID=" and dc.id= " +this.getTreeid();

		}else if (jib==-1){
			strGongsID=" and dc.id = "+((Visit) getPage().getVisit()).getDiancxxb_id();
		}
		//danwmc=getTreeDiancmc(this.getTreeid());
		//��������
		titlename=titlename+"";
		if(jib==1){//ѡ���ŵ�ʱ���շֹ�˾ͳ��,�����İ��յ�������ͳ��
			strSQL=
				"select decode(grouping(gs.mingc)+grouping(dc.mingc),1,gs.mingc,'&nbsp;&nbsp;'||dc.mingc),lj.mingc,cz.mingc,tb.banjh,sum(tb.ches)\n" +
				"from tielyb tb,diancxxb dc,chezxxb cz,lujxxb lj,diancxxb gs\n" + 
				"where tb.diancxxb_id=dc.id and tb.chezxxb_id=cz.id  and gs.id=dc.fuid\n" + 
				"      and lj.id=cz.lujxxb_id and riq="+riq+strGongsID+"\n" + 
				"group by rollup(gs.mingc,(dc.mingc,lj.mingc,cz.mingc,tb.banjh))\n" + 
				"having  grouping(gs.mingc)=0\n" + 
				"order by grouping(gs.mingc) desc,gs.mingc,grouping(dc.mingc) desc,dc.mingc";


		}else{
			strSQL=
				"select decode(grouping(gs.mingc)+grouping(dc.mingc),1,gs.mingc,'&nbsp;&nbsp;'||dc.mingc),lj.mingc,cz.mingc,tb.banjh,sum(tb.ches)\n" +
				"from tielyb tb,diancxxb dc,chezxxb cz,lujxxb lj,diancxxb gs\n" + 
				"where tb.diancxxb_id=dc.id and tb.chezxxb_id=cz.id  and gs.id=dc.fuid\n" + 
				"      and lj.id=cz.lujxxb_id and riq="+riq+strGongsID+"\n" + 
				"group by rollup(gs.mingc,(dc.mingc,lj.mingc,cz.mingc,tb.banjh))\n" + 
				"having  grouping(gs.mingc)=0\n" + 
				"order by grouping(gs.mingc) desc,gs.mingc,grouping(dc.mingc) desc,dc.mingc";
		}

		ArrHeader =new String[1][5];
		ArrHeader[0]=new String[] {"��λ","·��","��վ","��ƻ�","����"};

		ArrWidth =new int[] {150,100,100,100,100};


		ResultSet rs = cn.getResultSet(strSQL);



		// ����
		rt.setBody(new Table(rs,1, 0, 1));

		rt.setTitle(titlename, ArrWidth);
//		rt.setTitle(intyear+"��"+intMonth+"��"+titlename, ArrWidth);
		rt.setDefaultTitle(1, 2, "�Ʊ�λ:"+getDiancName(), Table.ALIGN_LEFT);
		rt.setDefaultTitle(4, 2, riq1, Table.ALIGN_RIGHT);
	//	rt.setDefaultTitle(5, 0, "��λ:��", Table.ALIGN_RIGHT);

		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(12);
		rt.body.setHeaderData(ArrHeader);// ��ͷ����
		rt.body.mergeFixedRow();

		rt.body.mergeFixedCols();
		rt.body.ShowZero = false;
		rt.body.setColAlign(2, Table.ALIGN_LEFT);
		rt.body.setColAlign(3, Table.ALIGN_LEFT);
		//ҳ�� 

		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1,2,"�Ʊ�ʱ��:"+DateUtil.FormatDate(new Date()),Table.ALIGN_LEFT);
		rt.setDefautlFooter(3,2,"���:",Table.ALIGN_LEFT);
		rt.setDefautlFooter(5,0,"�Ʊ�:",Table.ALIGN_RIGHT);
//		rt.setDefautlFooter(rt.footer.getCols()-1,2,Table.PAGENUMBER_NORMAL,Table.ALIGN_RIGHT);



		//����ҳ��
//		rt.createDefautlFooter(ArrWidth);

		_CurrentPage=1;
		_AllPages=rt.body.getPages();
		if (_AllPages==0){
			_CurrentPage=0;
		}
		cn.Close();
		return rt.getAllPagesHtml();

	}

	private String getQuzpk(){//ȡ���ſ� leix��2
		String strSQL="";
		_CurrentPage=1;
		_AllPages=1;
		JDBCcon cn = new JDBCcon();
		String riq=OraDate(_BeginriqValue);//��ǰ����
		String riq1=FormatDate(_BeginriqValue);
		//�����ͷ����
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		String titlename="����ȡ���ſ����";
		int iFixedRows=0;//�̶��к�
		int iCol=0;//����
		String danwmc="";//��������
		String strGongsID = "";
		int jib=this.getDiancTreeJib();
		if(jib==1){//ѡ����ʱˢ�³����еĵ糧
			strGongsID=" ";
			danwmc="���Ź�˾�ϼ�";
		}else if (jib==2){//ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
			strGongsID = "  and (dc.fuid=  " +this.getTreeid()+" or dc.shangjgsid="+this.getTreeid()+")";
		}else if (jib==3){//ѡ�糧ֻˢ�³��õ糧
			strGongsID=" and dc.id= " +this.getTreeid();
		}else if (jib==-1){
			strGongsID=" and dc.id = "+((Visit) getPage().getVisit()).getDiancxxb_id();
		}
		//danwmc=getTreeDiancmc(this.getTreeid());
		//��������
		titlename=titlename+"";
		if(jib==1){//ѡ���ŵ�ʱ���շֹ�˾ͳ��,�����İ��յ�������ͳ��
			strSQL=
				"select getLinkMingxTaiz(2,dc.id,dc.mingc) as mingc,q.quz6,q.paik6,q.daix6,q.quz18,q.paik18,q.daix18,q.CHANGCKC,\n" +//q.changtcs,
				"            q.changtcs,q.JIAOJZC,q.DAOZC,q.changtcs,q.changtsj\n" + 
				"       from quzpkb q,diancxxb dc\n" +  //,jiaojjlb j
				"       where q.diancxxb_id=dc.id and q.riq="+riq+strGongsID+"  ";//and j.diancxxb_id(+)=dc.id

		}else{
			strSQL=
				"select getLinkMingxTaiz(2,dc.id,dc.mingc) as mingc,q.quz6,q.paik6,q.daix6,q.quz18,q.paik18,q.daix18,q.CHANGCKC,\n" +//q.changtcs,
				"            q.changtcs,q.JIAOJZC,q.DAOZC,q.changtcs,q.changtsj\n" + 
				"       from quzpkb q,diancxxb dc\n" +  //,jiaojjlb j
				"       where q.diancxxb_id=dc.id and q.riq="+riq+strGongsID+"  "; //and j.diancxxb_id(+)=dc.id
		}


		int intZuor =DateUtil.getDay(getBeginriqDate());
		int intQianr=DateUtil.getDay(DateUtil.AddDate(getBeginriqDate(), -1, DateUtil.AddType_intDay));
		//chh 2008-12-16 ��ǰ�գ������滻Ϊ�������ڵ���
		ArrHeader =new String[2][13];
		//ArrHeader[0]=new String[] {"�糧","(ǰ��)18ʱ---(����)6ʱ","(ǰ��)18ʱ---(����)6ʱ","(ǰ��)18ʱ---(����)6ʱ","(ǰ��)18ʱ---(����)18ʱ","(ǰ��)18ʱ---(����)18ʱ","(ǰ��)18ʱ---(����)18ʱ","���Ӽ�¼","���Ӽ�¼","���Ӽ�¼","���Ӽ�¼","��ͣʱ��","��ͣʱ��"};
		ArrHeader[0]=new String[] {"�糧","("+intQianr+"��)18ʱ---("+intZuor+"��)6ʱ","("+intQianr+"��)18ʱ---("+intZuor+"��)6ʱ","("+intQianr+"��)18ʱ---("+intZuor+"��)6ʱ","("+intQianr+"��)18ʱ---("+intZuor+"��)18ʱ","("+intQianr+"��)18ʱ---("+intZuor+"��)18ʱ","("+intQianr+"��)18ʱ---("+intZuor+"��)18ʱ","���Ӽ�¼","���Ӽ�¼","���Ӽ�¼","���Ӽ�¼","��ͣʱ��","��ͣʱ��"};
		ArrHeader[1]=new String[] {"�糧","ȡ��","�ſ�","��ж","ȡ��","�ſ�","��ж","����ճ�","�����س�","����վ��","վ��","��ͣ����","ƽ��ʱ��"};

		ArrWidth =new int[] {120,50,50,50,50,50,50,50,50,50,50,50,50};


		ResultSet rs = cn.getResultSet(strSQL);




		// ����
		rt.setBody(new Table(rs,2, 0, 1));

		rt.setTitle(titlename, ArrWidth);
//		rt.setTitle(intyear+"��"+intMonth+"��"+titlename, ArrWidth);
		rt.setDefaultTitle(1, 3, "�Ʊ�λ:"+getDiancName(), Table.ALIGN_LEFT);
		rt.setDefaultTitle(6, 4, riq1, Table.ALIGN_CENTER);
		rt.setDefaultTitle(12, 2, "��λ:��", Table.ALIGN_RIGHT);

		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(18);
		rt.body.setHeaderData(ArrHeader);// ��ͷ����
		rt.body.mergeFixedRow();

		rt.body.mergeFixedCols();
		rt.body.ShowZero = true;
		//ҳ�� 

		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1,3,"�Ʊ�ʱ��:"+DateUtil.FormatDate(new Date()),Table.ALIGN_LEFT);
		rt.setDefautlFooter(6,3,"���:",Table.ALIGN_CENTER);
		rt.setDefautlFooter(12,2,"�Ʊ�:",Table.ALIGN_RIGHT);
    //	rt.setDefautlFooter(rt.footer.getCols()-1,2,Table.PAGENUMBER_NORMAL,Table.ALIGN_RIGHT);



		//����ҳ��
//		rt.createDefautlFooter(ArrWidth);

		_CurrentPage=1;
		_AllPages=rt.body.getPages();
		if (_AllPages==0){
			_CurrentPage=0;
		}
		cn.Close();
		return rt.getAllPagesHtml();

	}

	//	�糧����
	public boolean _diancmcchange = false;
	private IDropDownBean _DiancmcValue;

	public IDropDownBean getDiancmcValue() {
		if(_DiancmcValue==null){
			_DiancmcValue=(IDropDownBean)getIDiancmcModel().getOption(0);
		}
		return _DiancmcValue;
	}

	public void setDiancmcValue(IDropDownBean Value) {
		long id = -2;
		if (_DiancmcValue != null) {
			id = _DiancmcValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_diancmcchange = true;
			} else {
				_diancmcchange = false;
			}
		}
		_DiancmcValue = Value;
	}

	private IPropertySelectionModel _IDiancmcModel;

	public void setIDiancmcModel(IPropertySelectionModel value) {
		_IDiancmcModel = value;
	}

	public IPropertySelectionModel getIDiancmcModel() {
		if (_IDiancmcModel == null) {
			getIDiancmcModels();
		}
		return _IDiancmcModel;
	}

	public void getIDiancmcModels() {
		_IDiancmcModel = new IDropDownModel("select d.id,d.mingc from diancxxb d order by d.mingc desc");
	}

//	�������
	public boolean _meikdqmcchange = false;
	private IDropDownBean _MeikdqmcValue;

	public IDropDownBean getMeikdqmcValue() {
		if(_MeikdqmcValue==null){
			_MeikdqmcValue=(IDropDownBean)getIMeikdqmcModels().getOption(0);
		}
		return _MeikdqmcValue;
	}

	public void setMeikdqmcValue(IDropDownBean Value) {
		long id = -2;
		if (_MeikdqmcValue != null) {
			id = _MeikdqmcValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_meikdqmcchange = true;
			} else {
				_meikdqmcchange = false;
			}
		}
		_MeikdqmcValue = Value;
	}

	private IPropertySelectionModel _IMeikdqmcModel;

	public void setIMeikdqmcModel(IPropertySelectionModel value) {
		_IDiancmcModel = value;
	}

	public IPropertySelectionModel getIMeikdqmcModel() {
		if (_IMeikdqmcModel == null) {
			getIMeikdqmcModels();
		}
		return _IMeikdqmcModel;
	}

	public IPropertySelectionModel getIMeikdqmcModels() {
		JDBCcon con = new JDBCcon();
		try{


			String sql="";
			sql = "select id,meikdqmc from meikdqb order by meikdqmc";
			_IMeikdqmcModel = new IDropDownModel(sql);

		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.Close();
		}
		return _IMeikdqmcModel;
	}


	public SortMode getSort() {
		return SortMode.USER;
	}

	private String _pageLink;

	public boolean getRaw() {
		return true;
	}

	public String getpageLink() {
		if (!_pageLink.equals("")) {
			return _pageLink;
		} else {
			return "";
		}
	}

	// Page����
	protected void initialize() {
		_msg = "";
		_pageLink = "";
	}



	private String FormatDate(Date _date) {
		if (_date == null) {
			return "";
		}
		return DateUtil.Formatdate("yyyy��MM��dd��", _date);
	}

//	***************************�����ʼ����***************************//
	private int _CurrentPage = -1;

	public int getCurrentPage() {
		return _CurrentPage;
	}

	public void setCurrentPage(int _value) {
		_CurrentPage = _value;
	}

	private int _AllPages = -1;

	public int getAllPages() {
		return _AllPages;
	}

	public void setAllPages(int _value) {
		_AllPages = _value;
	}

	public Date getYesterday(Date dat){
		Calendar cal = Calendar.getInstance();
		cal.setTime(dat);
		cal.add(Calendar.DATE,-1);
		return cal.getTime();
	}

	public Date getMonthFirstday(Date dat){
		Calendar cal = Calendar.getInstance();
		cal.setTime(dat);
		cal.set(Calendar.DATE,cal.getActualMinimum(Calendar.DATE));
		return cal.getTime();
	}

	protected String getpageLinks() {
		String PageLink = "";
		IRequestCycle cycle = this.getRequestCycle();
		if (cycle.isRewinding())
			return "";
		String _servername = cycle.getRequestContext().getRequest()
		.getServerName();
		String _scheme = cycle.getRequestContext().getRequest().getScheme();
		int _ServerPort = cycle.getRequestContext().getRequest()
		.getServerPort();
		if (_ServerPort != 80) {
			PageLink = _scheme + "://" + _servername + ":" + _ServerPort
			+ this.getEngine().getContextPath();
		} else {
			PageLink = _scheme + "://" + _servername
			+ this.getEngine().getContextPath();
		}
		return PageLink;
	}
//	�ֹ�˾������
	private boolean _fengschange = false;

	public IDropDownBean getFengsValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean4() == null) {
			((Visit) getPage().getVisit())
			.setDropDownBean4((IDropDownBean) getFengsModel()
					.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean4();
	}

	public void setFengsValue(IDropDownBean Value) {
		if (getFengsValue().getId() != Value.getId()) {
			_fengschange = true;
		}
		((Visit) getPage().getVisit()).setDropDownBean4(Value);
	}

	public IPropertySelectionModel getFengsModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
			getFengsModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}

	public void setDiancxxModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel4(value);
	}

	public void getFengsModels() {
		String sql;
		sql = "select id ,mingc from diancxxb where jib=2 order by id";
		setDiancxxModel(new IDropDownModel(sql,"�й����Ƽ���"));
	}

	public boolean _Baoblxchange = false;
	private IDropDownBean _BaoblxValue;

	public IDropDownBean getBaoblxValue() {
		if(_BaoblxValue==null){
			_BaoblxValue=(IDropDownBean)getIBaoblxModels().getOption(0);
		}
		return _BaoblxValue;
	}

	public void setBaoblxValue(IDropDownBean Value) {
		long id = -2;
		if (_BaoblxValue != null) {
			id = _BaoblxValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_Baoblxchange = true;
			} else {
				_Baoblxchange = false;
			}
		}
		_BaoblxValue = Value;
	}

	private IPropertySelectionModel _IBaoblxModel;

	public void setIBaoblxModel(IPropertySelectionModel value) {
		_IBaoblxModel = value;
	}

	public IPropertySelectionModel getIBaoblxModel() {
		if (_IBaoblxModel == null) {
			getIBaoblxModels();
		}
		return _IBaoblxModel;
	}

	public IPropertySelectionModel getIBaoblxModels() {
		JDBCcon con = new JDBCcon();
		try{
			List fahdwList = new ArrayList();
			fahdwList.add(new IDropDownBean(0,"װ��Ԥ��"));
			fahdwList.add(new IDropDownBean(1,"��վԤ��"));
			fahdwList.add(new IDropDownBean(2,"ȡ���ſ�"));

			_IBaoblxModel = new IDropDownModel(fahdwList);

		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.Close();
		}
		return _IBaoblxModel;
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
		} catch (SQLException e) {

			e.printStackTrace();
		}finally{
			con.Close();
		}

		return jib;
	}
//	�õ��糧��������ĵ糧���ƻ��߷ֹ�˾,���ŵ�����
	public String getTreeDiancmc(String diancmcId) {
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
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			cn.Close();
		}
		return IDropDownDiancmc;
	}

	public void getToolBars() {
		Toolbar tb1 = new Toolbar("tbdiv");

		tb1.addText(new ToolbarText("����:"));
		DateField df = new DateField();
		df.setValue(DateUtil.FormatDate(this.getBeginriqDate()));
		df.Binding("riqDateSelect","forms[0]");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setWidth(100);
		//df.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(df);
		tb1.addText(new ToolbarText("-"));

		ExtTreeUtil etu = new ExtTreeUtil("diancTree",ExtTreeUtil.treeWindowType_Dianc,((Visit)this.getPage().getVisit()).getDiancxxb_id(),"-1".equals(getTreeid())?null:getTreeid());
		setTree(etu);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		tf.setValue(((IDropDownModel)getIDiancmcModel()).getBeanValue(Long.parseLong(getTreeid())));

		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);

		tb1.addText(new ToolbarText("��λ:"));
		tb1.addField(tf);
		tb1.addItem(tb2);
		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("��������:"));
		ComboBox cb = new ComboBox();
		cb.setTransform("BaoblxDropDown");
		cb.setListeners("select:function(){document.Form0.submit();}");
		cb.setId("Baoblx");
		cb.setWidth(120);
		tb1.addField(cb);
		tb1.addText(new ToolbarText("-"));

		ToolbarButton tb = new ToolbarButton(null,"ˢ��","function(){document.Form0.submit();}");
		tb1.addItem(tb);
		setToolbar(tb1);
	}

	public Toolbar getToolbar() {
		return ((Visit) this.getPage().getVisit()).getToolbar();
	}

	public void setToolbar(Toolbar tb1) {
		((Visit) this.getPage().getVisit()).setToolbar(tb1);
	}

	public String getToolbarScript() {
		return getToolbar().getRenderScript();
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

}