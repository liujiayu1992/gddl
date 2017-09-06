package com.zhiren.dc.zaf;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.contrib.palette.SortMode;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
 

/*
 * ���ߣ����
 * ʱ�䣺2012-01-06
 * ������ ��ú����=�ӷ��ܺ�/��¯ú���ܺ�
 * 		��ʼ���糧��Ϊ��ϵ糧�� �������������ʱ������������ʾ��ϸ��Ϣ
 */

/*
 * ���ߣ����
 * ʱ�䣺2012-02-09
 * ����: �����е������û�Ϊ�������ʱֻ��ʾ���ϱ������ݣ���״̬Ϊ1��3�����ݣ���
 * 		 �����յ糧��Ž�������
 */
/*
 * ���ߣ���ʤ��
 * ʱ�䣺2012-12-14
 * ����: ������ͬ��һ�� ͬ��=����-�ۼ�
 * 		 
 */
/*
 * ���ߣ����
 * ʱ�䣺2013-03-15
 * ����: ��ú����=�ӷ��ܺ�/(������ú��+������ú��)
 */
/*
 * ���ߣ�����
 * ʱ�䣺2013-03-26
 * ����: ����ͬ��bug
 */
/*
 * ���ߣ�����
 * ʱ�䣺2013-03-27
 * ����: ���Ӷ�ú����ͬ����Ϣ
 */
public class Zafreport  extends BasePage implements PageValidateListener{
	
//	 �ж��Ƿ��Ǽ����û�
	public boolean isJitUserShow() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 1;// ����

	}

	public boolean isGongsUser() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 2;// ��˾
	}

	public boolean isDiancUser() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 3;// �糧
	}
	
	private String diancxxb_id;
	public void setDiancxxb_id(String diancxxb_id){
		this.diancxxb_id = diancxxb_id;
	}
	public String getDiancxxb_id(){
		return diancxxb_id;
	}
	
	//��ʼ����
	private Date _BeginriqValue = new Date();
//	private boolean _BeginriqChange=false;
	public Date getBeginriqDate() {
		if (_BeginriqValue==null){
			_BeginriqValue = new Date();
		}
		return _BeginriqValue;
	}
	
	public void setBeginriqDate(Date _value) {
		if (_BeginriqValue.equals(_value)) {
//			_BeginriqChange=false;
		} else {
			_BeginriqValue = _value;
//			_BeginriqChange=true;
		}
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
	
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			Refurbish();
		}
	}
	
	private void Refurbish() {
        //Ϊ "ˢ��" ��ť��Ӵ������
		isBegin=true;
		getSelectData();
	}

//******************ҳ���ʼ����********************//
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		setDiancxxb_id(cycle.getRequestContext().getParameter("strDiancxxb_id"));
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			setNianfValue(null);
			setYuefValue(null);
			getNianfModels();
			getYuefModels();
			this.setTreeid(null);
			this.getTree();
			initDiancTree();
			setToolbar(null);
			isBegin=true;
		}
	getToolBars() ;
	Refurbish();
	}
	
	private String RT_HET="Zafreport";//��ú��������±�
	private String mstrReportName="Zafreport";
	
	public String getPrintTable(){
		if(!isBegin){
			return "";
		}
		isBegin=false;
		if (mstrReportName.equals(RT_HET)){
			return getSelectData();
		}else{
			return "�޴˱���";
		}
	}
	
	public int getZhuangt() {
		return 1;
	}

//	private int intZhuangt=0;
	public void setZhuangt(int _value) {
//		intZhuangt=1;
	}

	private boolean isBegin=false;
	private String getSelectData(){
		boolean huiz=(null==getDiancxxb_id()||"".equals(getDiancxxb_id()));
		if(huiz){    //�鿴��������
			return getHuiz();
		}else{
			return getMingx();
		}
		
	}
	
	
	private String getHuiz(){
		StringBuffer strSQL= new StringBuffer();
		_CurrentPage=1;
		_AllPages=1;
		
		JDBCcon cn = new JDBCcon();
		long intyear;
		if (getNianfValue() == null){
			intyear=DateUtil.getYear(new Date());
		}else{
			intyear=getNianfValue().getId();
		}
		long intMonth;
		if(getYuefValue() == null){
			intMonth = DateUtil.getMonth(new Date());
		}else{
			intMonth = getYuefValue().getId();
		}
		
		//�����ͷ����
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		String arrFormat[]=null;
		
		Visit visit = (Visit) getPage().getVisit();
		String tiaoj="";
		if(visit.getDiancxxb_id()==112){tiaoj="AND (z.ZHUANGT=1 or z.ZHUANGT=3)";}
		
		//��������
			strSQL.append(
				"SELECT decode(grouping(je.fuid) + grouping(je.mc),2,'��˾�ϼ�',getLinkZaf(min(je.diancxxb_id),decode(grouping(je.fuid) + grouping(je.mc),2,'��˾�ϼ�',je.mc))) dc,\n" +
//				"SELECT decode(grouping(je.fuid) + grouping(je.mc),2,'��˾�ϼ�',je.mc) dc,\n" +
				" nvl('С��', '') zf,\n"+
				"  sum(jine_bq_by) jine_bq_by,\n" + 
				"  round(decode(nvl(sum(ml.bbyml),0),0,0,sum(jine_bq_by)/sum(ml.bbyml)),2) bbydmfy,\n" + 
				"  sum(jine_tq_by) jine_tq_by,\n" + 
				"   round(decode(nvl(sum(ml.tbyml),0),0,0,sum(jine_tq_by)/sum(ml.tbyml)),2) tbydmfy,\n" +
				"(sum(nvl(jine_bq_by,0))- sum(nvl(jine_tq_by,0)) )jine_tb_by,\n"+
				"  round(decode(nvl(sum(ml.bbyml),0),0,0,sum(jine_bq_by)/sum(ml.bbyml)),2)-round(decode(nvl(sum(ml.tbyml),0),0,0,sum(jine_tq_by)/sum(ml.tbyml)),2) bbydmfytb,\n" +
				"  sum(jine_bq_lj) jine_bq_lj ,\n" + 
				"    round(decode(nvl(sum(ml.bljml),0),0,0,sum(jine_bq_lj)/sum(ml.bljml)),2) bljdmfy,\n" + 
				"  sum(jine_tq_lj) jine_tq_lj,\n" + 
				"  round( decode(nvl(sum(ml.tljml),0),0,0,sum(jine_tq_lj)/sum(ml.tljml)),2) tljdmfy,\n" +
				"(sum(nvl(jine_bq_lj,0))-sum(nvl(jine_tq_lj,0)))jine_tb_lj,\n"+
				"    round(decode(nvl(sum(ml.bljml),0),0,0,sum(jine_bq_lj)/sum(ml.bljml)),2)-round( decode(nvl(sum(ml.tljml),0),0,0,sum(jine_tq_lj)/sum(ml.tljml)),2) bljdmfytb\n" + 
				"    FROM\n" + 
				"( SELECT  min(dc.id) diancxxb_id,dc.mingc,\n" + 
				" sum(benq_by.jine) jine_bq_by,\n" + 
				" sum(tongq_by.jine) jine_tq_by,\n" + 
				" sum(benq_lj.jine) jine_bq_lj,\n" + 
				" sum(tongq_lj.jine) jine_tq_lj,\n" + 
				" (dc.fuid) fuid , (dc.mingc) mc,(dc.xuh)xuh,\n" + 
				"grouping(dc.fuid) + grouping(dc.mingc) gp\n" + 
				"from (select diancxxb_id, zf.mingc zfmc\n" + 
				"    from (select distinct diancxxb_id, z.mingc\n" + 
				"            from zafb z\n" + 
				"           where z.riq >= last_year_today(date '"+intyear+"-1-1') and z.riq <= date'"+intyear+"-"+intMonth+"-01' " +
				tiaoj+
				") zf WHERE zf.diancxxb_id IN ("+getTreeid()+")) fx,\n" + 
				" (select z.diancxxb_id, z.mingc zafmc, sum(z.jine) jine\n" + 
				"    from zafb z\n" + 
				"   where z.riq = date '"+intyear+"-"+intMonth+"-01'\n" + 
				"   group by z.diancxxb_id, z.mingc) benq_by,\n" + 
				" (select z.diancxxb_id, z.mingc zafmc, sum(z.jine) jine\n" + 
				"    from zafb z\n" + 
				"   where z.riq >= date '"+intyear+"-1-1'\n" + 
				"     and z.riq <= date '"+intyear+"-"+intMonth+"-01'\n" + 
				"   group by z.diancxxb_id, z.mingc) benq_lj,\n" + 
				" (select z.diancxxb_id, z.mingc zafmc, sum(z.jine) jine\n" + 
				"    from zafb z\n" + 
				"   where z.riq = last_year_today(date '"+intyear+"-"+intMonth+"-01')\n" + 
				"   group by z.diancxxb_id, z.mingc) tongq_by,\n" + 
				" (select z.diancxxb_id, z.mingc zafmc, sum(z.jine) jine\n" + 
				"    from zafb z\n" + 
				"   where z.riq >= last_year_today(date '"+intyear+"-1-1')\n" + 
				"     and z.riq <= last_year_today(date '"+intyear+"-"+intMonth+"-01')\n" + 
				"   group by z.diancxxb_id, z.mingc) tongq_lj,diancxxb dc\n" + 
				"where fx.diancxxb_id = benq_by.diancxxb_id(+)\n" + 
				"  and fx.diancxxb_id = benq_lj.diancxxb_id(+)\n" + 
				"  and fx.diancxxb_id = tongq_by.diancxxb_id(+)\n" + 
				"  and fx.diancxxb_id = tongq_lj.diancxxb_id(+)\n" + 
				"  and fx.zfmc = benq_by.zafmc(+)\n" + 
				"  and fx.zfmc = benq_lj.zafmc(+)\n" + 
				"  and fx.zfmc = tongq_by.zafmc(+)\n" + 
				"  and fx.zfmc = tongq_lj.zafmc(+)\n" + 
				"  and fx.diancxxb_id = dc.id(+)\n" + 
				" group by  (dc.fuid,dc.xuh, dc.mingc))je,\n" + 
				" (   SELECT fx.diancxxb_id,bby.rulml bbyml,blj.rulml bljml,tby.rulml tbyml,tlj.rulml tljml\n" + 
				"  FROM\n" + 
				"  (select distinct diancxxb_id\n" + 
				"            from yuezbb z\n" + 
				"           where z.riq >= last_year_today(date '"+intyear+"-1-1') and z.riq <= last_year_today(date'"+intyear+"-"+intMonth+"-01') " +
				tiaoj+
				") fx,\n" + 
				" (SELECT YZ.DIANCXXB_ID,\n" + 
				"          SUM(YZ.FADYTRML + YZ.GONGRYTRML ) RULML\n" + 
				"     FROM YUEZBB YZ\n" + 
				"    WHERE FENX = '����'\n" + 
				"      AND YZ.RIQ = date '"+intyear+"-"+intMonth+"-01'   GROUP BY YZ.DIANCXXB_ID)bby,\n" + 
				"\n" + 
				"     (SELECT YZ.DIANCXXB_ID,\n" + 
				"          SUM(YZ.FADYTRML + YZ.GONGRYTRML ) RULML\n" + 
				"     FROM YUEZBB YZ\n" + 
				"    WHERE FENX = '����'\n" + 
				"      AND  yz.riq >= date '"+intyear+"-1-1'\n" + 
				"     and yz.riq <= date '"+intyear+"-"+intMonth+"-01'\n" + 
				"    GROUP BY YZ.DIANCXXB_ID)blj,\n" + 
				"\n" + 
				"    ( SELECT YZ.DIANCXXB_ID,\n" + 
				"          SUM(YZ.FADYTRML + YZ.GONGRYTRML) RULML\n" + 
				"     FROM YUEZBB YZ\n" + 
				"    WHERE FENX = '����'\n" + 
				"      AND  yz.riq = last_year_today(date '"+intyear+"-"+intMonth+"-01')\n" + 
				"    GROUP BY YZ.DIANCXXB_ID)tby,\n" + 
				"\n" + 
				"    ( SELECT YZ.DIANCXXB_ID,\n" + 
				"          SUM(YZ.FADYTRML + YZ.GONGRYTRML) RULML\n" + 
				"     FROM YUEZBB YZ\n" + 
				"    WHERE FENX = '����'\n" + 
				"      AND  yz.riq >= last_year_today(date '"+intyear+"-1-1')\n" + 
				"     and yz.riq <= last_year_today(date '"+intyear+"-"+intMonth+"-01')\n" + 
				"    GROUP BY YZ.DIANCXXB_ID)tlj\n" + 
				"    WHERE fx.diancxxb_id= bby.diancxxb_id(+)\n" + 
				"          AND  fx.diancxxb_id=blj.diancxxb_id(+)\n" + 
				"        AND  fx.diancxxb_id=tby.diancxxb_id(+)\n" + 
				"        AND  fx.diancxxb_id=tlj.diancxxb_id(+)\n" + 
				
				")ml\n" + 
				"WHERE ml.diancxxb_id=je.diancxxb_id\n" + 
				"group by ROLLUP (je.fuid,je.xuh, je.mc)\n" + 
				"having not grouping(je.fuid) + grouping(je.mingc) = 1\n" + 
				"order by grouping(je.fuid) desc,je.fuid,grouping(je.xuh)desc, je.xuh,grouping(je.mingc) desc,je.mingc");
			 ArrHeader=new String[2][12];
			 ArrHeader[0]=new String[] {"��λ����","�ӷ�����","���½��","���½��","���½��","���½��","���½��","���½��","�ۼƽ��","�ۼƽ��","�ۼƽ��","�ۼƽ��","�ۼƽ��","�ۼƽ��"};
			 ArrHeader[1]=new String[] {"��λ����","�ӷ�����","���ڷ���","����<br>��ú<br>����","ͬ�ڷ���","ͬ��<br>��ú<br>����","����ͬ��","��ú<br>����<br>ͬ��","���ڷ���","����<br>��ú<br>����","ͬ�ڷ���","ͬ��<br>��ú<br>����","����ͬ��","��ú<br>����<br>ͬ��"};
			 
			 ArrWidth=new int[] {100,100,80,60,80,60,80,60,80,60,80,60,80,60};
//			 iFixedRows=1;
			 arrFormat=new String[]{"","","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00"};
		
			ResultSet rs = cn.getResultSet(strSQL.toString());
			 
			// ����
			Table tb = new Table(rs,2, 0, 1);
			rt.setBody(tb);
			
			rt.setTitle(intyear+"��"+intMonth+"���ӷѲ�ѯ��", ArrWidth);
			String[] str=getTreeid().split(",");
			if(str.length>1){
				rt.setDefaultTitle(1, 4, "���λ(����):��ϵ糧", Table.ALIGN_LEFT);
			}else{
				rt.setDefaultTitle(1, 4, "���λ(����):"+getDiancmcById(str[0]), Table.ALIGN_LEFT);
			}
			rt.setDefaultTitle(6, 3, "�����:"+intyear+"��"+intMonth+"��", Table.ALIGN_RIGHT);
			rt.setDefaultTitle(12, 3, "��λ:Ԫ��Ԫ/��", Table.ALIGN_RIGHT);
			
			rt.body.setWidth(ArrWidth);
			rt.body.setPageRows(18);
			rt.body.setHeaderData(ArrHeader);// ��ͷ����
			rt.body.merge(2,1,rt.body.getRows(),1);   //�Ե�һ�н��кϲ�
			rt.body.merge(1,1,2,rt.body.getCols());   //�Ե�һ�����н��кϲ�
			rt.body.ShowZero = true;
			rt.body.setColFormat(arrFormat);
		 
			rt.body.setColAlign(1, Table.ALIGN_CENTER);
			rt.body.setColAlign(2, Table.ALIGN_CENTER);
			
			//ҳ�� 
			 rt.createDefautlFooter(ArrWidth);
			  rt.setDefautlFooter(1,3,"��ӡ����:"+DateUtil.FormatDate(new Date()),Table.ALIGN_LEFT);
			  rt.setDefautlFooter(10,2,"���:",Table.ALIGN_LEFT);
			  rt.setDefautlFooter(14,3,"�Ʊ�:",Table.ALIGN_LEFT);
			  rt.setDefautlFooter(rt.footer.getCols()-1,2,Table.PAGENUMBER_NORMAL,Table.ALIGN_RIGHT);
			 
			_CurrentPage=1;
			_AllPages=rt.body.getPages();
			if (_AllPages==0){
				_CurrentPage=0;
			}
			cn.Close();
			return rt.getAllPagesHtml();
	}
	
	private String getMingx(){

		StringBuffer strSQL= new StringBuffer();
		_CurrentPage=1;
		_AllPages=1;
		
		JDBCcon cn = new JDBCcon();
		long intyear;
		if (getNianfValue() == null){
			intyear=DateUtil.getYear(new Date());
		}else{
			intyear=getNianfValue().getId();
		}
		long intMonth;
		if(getYuefValue() == null){
			intMonth = DateUtil.getMonth(new Date());
		}else{
			intMonth = getYuefValue().getId();
		}
		
		//�����ͷ����
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		String arrFormat[]=null;
		
		strSQL.append("select decode(grouping(dc.mingc) + grouping(fx.zfmc),1,dc.mingc || '�ϼ�',dc.mingc) dc,\n"+
			      " decode(grouping(fx.zfmc), 1, 'С��', fx.zfmc) zf,\n"+
			      " sum(benq_by.jine) jine_bq_by,\n"+
			      " sum(tongq_by.jine) jine_tq_by,\n"+
			      "  (sum(nvl(benq_by.jine,0))-sum(nvl(tongq_by.jine,0)))jine_tb_by,\n"+
			      " sum(benq_lj.jine) jine_bq_lj,\n"+
			      " sum(tongq_lj.jine) jine_tq_lj,\n"+
                 "(sum(nvl(benq_lj.jine,0))- sum(nvl(tongq_lj.jine,0))) jine_tb_lj\n"+
			 " from (select diancxxb_id, zf.mingc zfmc\n"+
			      "    from (select distinct diancxxb_id, z.mingc\n"+
			      "            from zafb z\n"+
			      "           where ((z.riq >= date '"+intyear+"-1-1' and z.riq <= date'"+intyear+"-"+intMonth+"-01') or\n"+
			      "                 (z.riq >= last_year_today(date '"+intyear+"-1-1') and\n"+
			      "                 z.riq <= last_year_today(date '"+intyear+"-"+intMonth+"-01')))) zf) fx,\n"+
			      " (select z.diancxxb_id, z.mingc zafmc, sum(z.jine) jine\n"+
			      "    from zafb z\n"+
			      "   where z.riq = date '"+intyear+"-"+intMonth+"-01'\n"+
			      "   group by z.diancxxb_id, z.mingc) benq_by,\n"+
			      " (select z.diancxxb_id, z.mingc zafmc, sum(z.jine) jine\n"+
			      "   from zafb z\n"+
			      "   where z.riq >= date '"+intyear+"-1-1'\n"+
			      "     and z.riq <= date '"+intyear+"-"+intMonth+"-01'\n"+
			      "   group by z.diancxxb_id, z.mingc) benq_lj,\n"+
			      " (select z.diancxxb_id, z.mingc zafmc, sum(z.jine) jine\n"+
			      "    from zafb z\n"+
			      "   where z.riq = last_year_today(date '"+intyear+"-"+intMonth+"-01')\n"+
			      "   group by z.diancxxb_id, z.mingc) tongq_by,\n"+
			      " (select z.diancxxb_id, z.mingc zafmc, sum(z.jine) jine\n"+
			      "    from zafb z\n"+
			      "   where z.riq >= last_year_today(date '"+intyear+"-1-1')\n"+
			      "     and z.riq <= last_year_today(date '"+intyear+"-"+intMonth+"-01')\n"+
			      "   group by z.diancxxb_id, z.mingc) tongq_lj,diancxxb dc\n"+
			 "where fx.diancxxb_id = benq_by.diancxxb_id(+)\n"+
			 "  and fx.diancxxb_id = benq_lj.diancxxb_id(+)\n"+
			 "  and fx.diancxxb_id = tongq_by.diancxxb_id(+)\n"+
			 "  and fx.diancxxb_id = tongq_lj.diancxxb_id(+)\n"+
			 "  and fx.zfmc = benq_by.zafmc(+)\n"+
			 "  and fx.zfmc = benq_lj.zafmc(+)\n"+
			 "  and fx.zfmc = tongq_by.zafmc(+)\n"+
			 "  and fx.zfmc = tongq_lj.zafmc(+)\n"+
			 "  and (dc.id in( "+getDiancxxb_id()+"))\n"+
			 "  and fx.diancxxb_id = dc.id(+)\n"+
			 "group by rollup((dc.mingc, dc.xuh), fx.zfmc)\n"+
			"having not grouping(dc.mingc) + grouping(fx.zfmc) = 2\n"+
			" order by grouping(dc.mingc) desc,dc.mingc,grouping(fx.zfmc) desc,fx.zfmc");
			 ArrHeader=new String[2][6];
			 ArrHeader[0]=new String[] {"��λ����","�ӷ�����","���½��","���½��","���½��","�ۼƽ��","�ۼƽ��","�ۼƽ��"};
			 ArrHeader[1]=new String[] {"��λ����","�ӷ�����","����","ͬ��","ͬ��","����","ͬ��","ͬ��"};
			 
			 ArrWidth=new int[] {150,100,80,80,80,80,80,80};
			 arrFormat=new String[]{"","","0.000","0.000","0.000","0.000","0.000","0.000"};
		
			ResultSet rs = cn.getResultSet(strSQL.toString());
			 
			// ����
			Table tb = new Table(rs,2, 0, 1);
			rt.setBody(tb);
			
			rt.setTitle(intyear+"��"+intMonth+"���ӷѲ�ѯ��", ArrWidth);
			rt.setDefaultTitle(1, 3, "���λ(����):"+getDiancmcById(getDiancxxb_id()), Table.ALIGN_LEFT);

			rt.setDefaultTitle(4, 3, "�����:"+intyear+"��"+intMonth+"��", Table.ALIGN_RIGHT);
			rt.setDefaultTitle(15, 3, "��λ:Ԫ", Table.ALIGN_RIGHT);
			
			rt.body.setWidth(ArrWidth);
			rt.body.setPageRows(18);
			rt.body.setHeaderData(ArrHeader);// ��ͷ����
			rt.body.merge(2,1,rt.body.getRows(),1);   //�Ե�һ�н��кϲ�
			rt.body.merge(1,1,2,rt.body.getCols());   //�Ե�һ�����н��кϲ�
			rt.body.ShowZero = false;
			rt.body.setColFormat(arrFormat);
		 
			rt.body.setColAlign(1, Table.ALIGN_CENTER);
			rt.body.setColAlign(2, Table.ALIGN_CENTER);
			
			//ҳ�� 
			 rt.createDefautlFooter(ArrWidth);
			  rt.setDefautlFooter(1,3,"��ӡ����:"+DateUtil.FormatDate(new Date()),Table.ALIGN_LEFT);
			  rt.setDefautlFooter(10,2,"���:",Table.ALIGN_LEFT);
			  rt.setDefautlFooter(14,3,"�Ʊ�:",Table.ALIGN_LEFT);
			  rt.setDefautlFooter(rt.footer.getCols()-1,2,Table.PAGENUMBER_NORMAL,Table.ALIGN_RIGHT);
			 
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
		String sql="";
		sql = "select d.id,d.mingc as jianc from diancxxb d order by d.mingc desc";
		_IDiancmcModel = new IDropDownModel(sql);

	}
	
//	���
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
			for (int i = 0; i < _NianfModel.getOptionCount(); i++) {
				Object obj = _NianfModel.getOption(i);
				if (_nianf == ((IDropDownBean) obj).getId()) {
					_NianfValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _NianfValue;
	}
	public boolean nianfchanged = false;
	public void setNianfValue(IDropDownBean Value) {
		if (_NianfValue != Value) {
			nianfchanged = true;
		}
		_NianfValue = Value;
	}

	public IPropertySelectionModel getNianfModels() {
		List listNianf = new ArrayList();
		int i;
		for (i = 2000; i <= DateUtil.getYear(new Date())+1; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_NianfModel = new IDropDownModel(listNianf);
		return _NianfModel;
	}

	public void setNianfModel(IPropertySelectionModel _value) {
		_NianfModel = _value;
	}
	
	/**
	 * �·�
	 */
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
			for (int i = 0; i < _YuefModel.getOptionCount(); i++) {
				Object obj = _YuefModel.getOption(i);
				if (_yuef == ((IDropDownBean) obj).getId()) {
					_YuefValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _YuefValue;
	}
	public boolean yuefchanged = false;
	public void setYuefValue(IDropDownBean Value) {
		if (_YuefValue != Value) {
			yuefchanged = true;
		}
		_YuefValue = Value;
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
	 //	ҳ���ж�����
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

//	�õ��糧�ļ���,(jib:1,2,3,) 1�Ǽ���,2�Ƿֹ�˾,3�ǵ糧
    public int getDiancmcJib(String diancId){
    	JDBCcon con = new JDBCcon();
    	int jib = -1;
    	ResultSetList rsl = new ResultSetList();
    	StringBuffer sqlJib = new StringBuffer();
    	sqlJib.append("select d.jib from diancxxb d where d.id = '"+diancId+"'");
    	rsl = con.getResultSetList(sqlJib.toString());
    	rsl.next();
    	jib = rsl.getInt("jib");
    	con.Close();
    	return jib;
    }
    
    public String getDiancmcById(String diancid){
    	JDBCcon con = new JDBCcon();
    	String quanc = "";
    	ResultSetList rsl = new ResultSetList();
    	StringBuffer sqlJib = new StringBuffer();
    	sqlJib.append("select d.quanc from diancxxb d where d.id = '"+diancid+"'");
    	rsl = con.getResultSetList(sqlJib.toString());
    	rsl.next();
    	quanc = rsl.getString("quanc");
    	con.Close();
    	return quanc;
    }

	public void getToolBars() {
		Toolbar tb1 = new Toolbar("tbdiv");
		
		tb1.addText(new ToolbarText("���:"));
		ComboBox nianf = new ComboBox();
		nianf.setTransform("NIANF");
		nianf.setWidth(60);
		tb1.addField(nianf);
		tb1.addText(new ToolbarText("-"));
		tb1.addText(new ToolbarText("�·�:"));
		ComboBox yuef = new ComboBox();
		yuef.setTransform("YUEF");
		yuef.setWidth(60);
		tb1.addField(yuef);
		tb1.addText(new ToolbarText("-"));
		
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowCheck_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid(),null,true);
		setTree(etu);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		String[] str=getTreeid().split(",");
		if(str.length>1){
			tf.setValue("��ϵ糧");
		}else{
			tf.setValue(((IDropDownModel)getIDiancmcModel()).getBeanValue(Long.parseLong(str[0])));
		}

		
		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);
		
		tb1.addText(new ToolbarText("��λ:"));
		tb1.addField(tf);
		tb1.addItem(tb2);
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
		if(null==getDiancxxb_id()||"".equals(getDiancxxb_id())){   //�´��ڲ���ʾ������
			return getToolbar().getRenderScript()+getOtherScript("diancTree");
		}else{
			return "";
		}
	}

//	���ӵ糧��ѡ���ļ���
	public String getOtherScript(String treeid){
		String str=" var "+treeid+"_history=\"\";\n"+
			treeid+"_treePanel.on(\"checkchange\",function(node,checked){\n" +
				"    if(checked){\n" + 
				"      addNode(node);\n" + 
				"    }else{\n" + 
				"      subNode(node);\n" + 
				"    }\n" + 
				"    node.expand();\n" + 
				"    node.attributes.checked = checked;\n" + 
				"    node.eachChild(function(child) {\n" + 
				"      if(child.attributes.checked != checked){\n" + 
				"        if(checked){\n" + 
				"          addNode(child);\n" + 
				"        }else{\n" + 
				"          subNode(child);\n" + 
				"        }\n" + 
				"        child.ui.toggleCheck(checked);\n" + 
				"              child.attributes.checked = checked;\n" + 
				"              child.fireEvent('checkchange', child, checked);\n" + 
				"      }\n" + 
				"    });\n" + 
				"  },"+treeid+"_treePanel);\n" + 
				"  function addNode(node){\n" + 
				"    var history = '+,'+node.id+\";\";\n" + 
				"    writesrcipt(node,history);\n" + 
				"  }\n" + 
				"\n" + 
				"  function subNode(node){\n" + 
				"    var history = '-,'+node.id+\";\";\n" + 
				"    writesrcipt(node,history);\n" + 
				"  }\n" + 
				"function writesrcipt(node,history){\n" +
				"\t\tif("+treeid+"_history==\"\"){\n" + 
				"\t\t\t"+treeid+"_history = history;\n" + 
				//" 	   document.getElementById('GONGYSMLTTREE_ID').value=gongysTree_history;\n"	+
				"\t\t}else{\n" + 
				"\t\t\tvar his = "+treeid+"_history.split(\";\");\n" + 
				"\t\t\tvar reset = false;\n" + 
				"\t\t\tfor(i=0;i<his.length;i++){\n" + 
				"\t\t\t\tif(his[i].split(\",\")[1] == node.id){\n" + 
				"\t\t\t\t\this[i] = \"\";\n" + 
				"\t\t\t\t\treset = true;\n" + 
				"\t\t\t\t\tbreak;\n" + 
				"\t\t\t\t}\n" + 
				"\t\t\t}\n" + 
				"\t\tif(reset){\n" + 
				"\t\t\t  "+treeid+"_history = his.join(\";\");\n" + 
				//"      	 document.getElementById('GONGYSMLTTREE_ID').value=gongysTree_history;\n" + 
				"    }else{\n" + 
				"      	 "+treeid+"_history += history;\n" + 
				//"        document.getElementById('GONGYSMLTTREE_ID').value=gongysTree_history;\n" + 
				"    }\n" + 
				"  }\n" + 
				"\n" + 
				"}";
		return str;
	}
	
//	��ʼ����ѡ�糧���е�Ĭ��ֵ
	private void initDiancTree(){
		Visit visit = (Visit) getPage().getVisit();
		String sql="SELECT ID\n" +
			"  FROM DIANCXXB\n" + 
			" WHERE JIB > 2\n" + 
			" START WITH ID = "+visit.getDiancxxb_id()+"\n" + 
			"CONNECT BY FUID = PRIOR ID";

		JDBCcon con=new JDBCcon();
		ResultSetList rsl = con.getResultSetList(sql); 
		String TreeID="";
		while(rsl.next()){
			TreeID+=rsl.getString("ID")+",";
		}
		if(TreeID.length()>1){
			TreeID=TreeID.substring(0, TreeID.length()-1);
			setTreeid(TreeID);
		}else{
			setTreeid(visit.getDiancxxb_id() + "");
		}
		rsl.close();
		con.Close();
	}
	
//	private String treeid;

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