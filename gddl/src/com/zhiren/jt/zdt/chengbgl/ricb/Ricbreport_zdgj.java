
package com.zhiren.jt.zdt.chengbgl.ricb;
/* 
* ʱ�䣺2009-07-31
* ���ߣ� ll
* �޸����ݣ�1���޸ĳɱ�������ʽ��
* 		   
*/ 
/* 
* ʱ�䣺2009-08-5
* ���ߣ� ll
* �޸����ݣ�1�����Ӻϼ���ɫ������ǰ��ɫ
* 		   
*/ 
/* 
* ʱ�䣺2009-08-27
* ���ߣ� ll
* �޸����ݣ�1���жϡ���Ԥ��ֵ��ֵ������0�ĵ糧���ϼ��е�������ɫΪ��ɫ��
* 		   
*/ 

/* 
* ʱ�䣺2009-08-27
* ���ߣ� chh
* �޸����ݣ�1���޸�������������fahb ��diiancxxb_id ��vwdianc ,zhilb_id ��zhilb����,
* 			  fahb�ĵ�������������
* 			  �볧�����ݱ���һ�¡�
* 		   
*/ 

/* 
* ʱ�䣺2009-08-28
* ���ߣ� chh
* �޸����ݣ�1�����ӵ��ɱ��������ۻ��ɱ������У���ʾ�гɱ��Ľ�ú����������ú������������ɫ������ʾ
*/ 
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Ricbreport_zdgj extends BasePage {
	public boolean getRaw() {
		return true;
	}
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

	private boolean blnIsBegin = false;

	public String getPrintTable() {
		if (!blnIsBegin) {
			return "";
		}
		blnIsBegin = false;
     	return getZhiltj();

	}
	// ��ͬ���ֳ��ֿ�ֿ�ֳ�ͳ�Ʊ���
	private String getZhiltj() {
		JDBCcon con = new JDBCcon();

		String Start_riq=getBeginriqDate();

		String strConditonTitle="";

		String strGongsID = "";
		String notHuiz="";
		int jib=this.getDiancTreeJib();
		if(jib==1){//ѡ����ʱˢ�³����еĵ糧
			strGongsID=" ";

		}else if (jib==2){//ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
			strGongsID = "  and (dc.fuid=  " +this.getTreeid()+"or dc.shangjgsid="+this.getTreeid()+") ";
			notHuiz=" and not grouping(gs.mingc)=1 ";//���糧���Ƿֹ�˾ʱ,ȥ�����Ż���
		}else if (jib==3){//ѡ�糧ֻˢ�³��õ糧
			strGongsID=" and dc.id= " +this.getTreeid();
			notHuiz=" and not  grouping(dc.mingc)=1";//���糧���ǵ糧ʱ,ȥ���ֹ�˾�ͼ��Ż���
		}else if (jib==-1){
			strGongsID=" and dc.id = "+((Visit) getPage().getVisit()).getDiancxxb_id();
		}
		
		String diancmc="";
		String gongysmc="";

		String groupby = "";
		String ordeby ="";

		JDBCcon cn = new JDBCcon();

		if (jib==1){
			diancmc="select decode(grouping(dc.fgsmc)+grouping(dc.mingc),2,'�ܼ�',1,dc.fgsmc||'�ܼ�',dc.mingc) as dianc,\n";

			groupby ="group by rollup(dc.fgsmc,dc.mingc,gongys)\n";
			ordeby="order by grouping(dc.fgsmc) desc,max(dc.fgsxh),grouping(dc.mingc) desc,max(dc.xuh),grouping(gongys) desc,gongys)\n";
	
			
		}else {

			diancmc="select decode(grouping(dc.mingc),1,'�ܼ�',dc.mingc) as dianc,\n";
			
			groupby ="group by rollup(dc.mingc,gongys)\n";
			ordeby="order by grouping(dc.mingc) desc,max(dc.xuh),grouping(gongys) desc,gongys)\n";
		}


	
		String sbsql =

			"select dianc,gonghdw,jihl,jihrez_dk,round_new(decode(jihrez,0,0,round_new(jihchebjg/1.17+jihyunf*0.93+jihzaf,2)*29.271/jihrez),2) as jihbmdj,\n" +
			"   drLAIML,drcbl,drrezdk,drmeij,dryunf+drzaf as yunzf,round_new(decode(drrez,0,0,(drmeij/1.17+dryunf*0.93+drzaf)*29.271/drrez),2) as drbmj,\n" + 
			"   ljLAIML,ljcbl,round(ljLAIML/(jihl/to_number(to_char(last_day(to_date('"+Start_riq+"','yyyy-mm-dd')),'dd'))\n" + 
			"   *to_number(to_char(to_date('"+Start_riq+"','yyyy-mm-dd'),'dd')))*100,2) as daohl,\n" + 
			"   ljrezdk,ljrezdk-jihrez_dk as ljrezc, ljmeij,ljyunf+ljzaf as ljyunj,\n" + 
			"    round_new(decode(ljrez,0,0,((ljmeij/1.17+ljyunf*0.93+ljzaf)*29.271/ljrez)),2) as ljbmdj,\n" + 
			"   round_new(decode(ljrez,0,0,(ljmeij/1.17+ljyunf*0.93+ljzaf)*29.271/ljrez),2)-\n" + 
			"   round_new(decode(jihrez,0,0,round_new(jihchebjg/1.17+jihyunf*0.93+jihzaf,2)*29.271/jihrez),2) as ljbmdjc\n" + 
			"\n" + 
			"from (\n" + 
			diancmc+ 
			"   decode(grouping(gongys)+grouping(dc.mingc),1,'�ϼ�',gongys) as gonghdw,sum(jihl) as jihl,\n" + 
			"   round_new(round_new(decode(sum(nvl(jihl,0)),0,0,sum(nvl(rez*jihl,0))/sum(nvl(jihl,0))),2)*1000/4.1816,0) as jihrez_dk,\n" + 
			"   round_new(decode(sum(nvl(jihl,0)),0,0,sum(nvl(rez*jihl,0))/sum(nvl(jihl,0))),2) as jihrez,\n" + 
			"   round_new(decode(sum(nvl(jihl,0)),0,0,sum(jh.chebjg*jihl)/sum(jihl)),2) as jihchebjg,\n" + 
			"   round_new(decode(sum(nvl(jihl,0)),0,0,sum(jh.yunf*jihl)/sum(jihl)),2) as jihyunf,\n" + 
			"   decode(sum(nvl(jihl,0)),0,0,sum(jh.zaf*jihl)/sum(jihl)) as jihzaf,\n" + 
			"   --�������\n" + 
			"   SUM(wcdr.LAIML) AS drLAIML,sum(wcdr.cbsl) as drcbl,\n" + 
			"   round_new(round_new(decode(sum(nvl(wcdr.cbsl,0)),0,0,sum(nvl(wcdr.cbsl*wcdr.farl,0))/sum(nvl(wcdr.cbsl,0))),2)*1000/4.1816,0) as drrezdk,\n" + 
			"   round_new(decode(sum(nvl(wcdr.cbsl,0)),0,0,sum(nvl(wcdr.cbsl*wcdr.farl,0))/sum(nvl(wcdr.cbsl,0))),2) as drrez,\n" + 
			"   round_new(decode(sum(nvl(wcdr.cbsl,0)),0,0,sum(nvl(wcdr.cbsl*wcdr.meij,0))/sum(nvl(wcdr.cbsl,0))),2) as   drmeij,\n" + 
			"   round_new(decode(sum(nvl(wcdr.cbsl,0)),0,0,sum(nvl(wcdr.cbsl*wcdr.yunf,0))/sum(nvl(wcdr.cbsl,0))),2) as   dryunf,\n" + 
			"   round_new(decode(sum(nvl(wcdr.cbsl,0)),0,0,sum(nvl(wcdr.cbsl*wcdr.zaf,00))/sum(nvl(wcdr.cbsl,0))),2) as   drzaf  ,\n" + 
			"   --�ۼƵ����\n" + 
			"   SUM(wclj.LAIML) AS ljLAIML,sum(wclj.cbsl) as ljcbl,\n" + 
			"   round_new(round_new(decode(sum(nvl(wclj.cbsl,0)),0,0,sum(nvl(wclj.cbsl*wclj.farl,0))/sum(nvl(wclj.cbsl,0))),2)*1000/4.1816,0) as ljrezdk,\n" + 
			"   round_new(decode(sum(nvl(wclj.cbsl,0)),0,0,sum(nvl(wclj.cbsl*wclj.farl,0))/sum(nvl(wclj.cbsl,0))),2) as ljrez,\n" + 
			"   round_new(decode(sum(nvl(wclj.cbsl,0)),0,0,sum(nvl(wclj.cbsl*wclj.meij,0))/sum(nvl(wclj.cbsl,0))),2) as   ljmeij,\n" + 
			"   round_new(decode(sum(nvl(wclj.cbsl,0)),0,0,sum(nvl(wclj.cbsl*wclj.yunf,0))/sum(nvl(wclj.cbsl,0))),2) as   ljyunf,\n" + 
			"   round_new(decode(sum(nvl(wclj.cbsl,0)),0,0,sum(nvl(wclj.cbsl*wclj.zaf,0))/sum(nvl(wclj.cbsl,0))),2) as   ljzaf\n" + 
			"from\n" + 
			"  (select dc.mingc, dc.id,dc.xuh,dc.fgsmc,dc.fgsxh, gy.dqmc as gongys, gy.dqid\n" + 
			"        from fahb f,vwdianc dc,vwgongys gy\n" + 
			"        where f.diancxxb_id=dc.id and f.gongysb_id=gy.id\n" +   strGongsID+" \n"+
			"        and f.daohrq>=first_day(to_date('"+Start_riq+"','yyyy-mm-dd')) and f.daohrq<=to_date('"+Start_riq+"','yyyy-mm-dd')\n" + 
			"   union select dc.mingc, dc.id,dc.xuh,dc.fgsmc,dc.fgsxh, gy.dqmc gongys, gy.dqid\n" + 
			"        from yuecgjhb jh,vwdianc dc,vwgongys gy\n" + 
			"        where jh.diancxxb_id=dc.id and jh.gongysb_id=gy.id\n" +   strGongsID+" \n"+
			"        and jh.riq>=first_day(to_date('"+Start_riq+"','yyyy-mm-dd')) and jh.riq<=to_date('"+Start_riq+"','yyyy-mm-dd')\n" + 
			"        and jh.yuejhcgl<>0 ) dc,   --��ͷ����������\n" + 
			"   (select diancid,dqid,SUM(LAIML) AS LAIML,sum(cbsl) as cbsl,\n" + 
			"       decode(sum(cbsl),0,0,sum(cbsl*farl)/sum(cbsl)) as farl,\n" + 
			"       decode(sum(cbsl),0,0,sum(cbsl*meij)/sum(cbsl)) as   meij,\n" + 
			"       decode(sum(cbsl),0,0,sum(cbsl*yunf)/sum(cbsl)) as   yunf,\n" + 
			"       decode(sum(cbsl),0,0,sum(cbsl*zaf)/sum(cbsl)) as   zaf\n" + 
			"  from ( select f.lieid,dc.id as diancid,gy.dqid,\n" + 
			"            round_new(sum(f.biaoz),0)+round_new(sum(f.yingd),0)-round_new(sum(f.yingd-f.yingk),0)   as laiml,\n" + 
			"            round_new(sum(decode(nvl(r.id,0),0,0,f.biaoz)),0)+round_new(sum(decode(nvl(r.id,0),0,0,f.yingd)),0)-round_new(sum(decode(nvl(r.id,0),0,0,f.yingd-f.yingk)),0) as cbsl, --�ɱ�����,\n" + 
			"            decode(sum(decode(nvl(r.id,0),0,0,f.laimsl)),0,0,sum(round_new(nvl(zl.qnet_ar,0),2)*decode(nvl(r.id,0),0,0,f.laimsl))/sum(decode(nvl(r.id,0),0,0,f.laimsl))) as farl,\n" + 
			"            decode(sum(decode(nvl(r.id,0),0,0,f.laimsl)),0,0,sum(nvl(r.meij,0)*decode(nvl(r.id,0),0,0,f.laimsl))/sum(decode(nvl(r.id,0),0,0,f.laimsl))) as meij,\n" + 
			"            decode(sum(decode(nvl(r.id,0),0,0,f.laimsl)),0,0,sum(nvl(r.disyf+r.qiyf+r.kuangyf+r.yingdyf+r.daozzf ,0)*decode(nvl(r.id,0),0,0,f.laimsl))/sum(decode(nvl(r.id,0),0,0,f.laimsl))) as yunf,\n" + 
			"            decode(sum(decode(nvl(r.id,0),0,0,f.laimsl)),0,0,sum(nvl(r.jiaohqzf+r.qitzf,0)*decode(nvl(r.id,0),0,0,f.laimsl))/sum(decode(nvl(r.id,0),0,0,f.laimsl))) as zaf\n" + 
			"        from Rigjb r,fahb f,zhilb zl,vwdianc dc,vwgongys gy\n" + 
			"        where r.fahb_id(+)=f.lieid\n" + 
			"        and f.diancxxb_id=dc.id\n" + 
			"        and f.zhilb_id =zl.id(+)\n" + 
			"        and f.gongysb_id=gy.id\n" + strGongsID+" \n"+
			"        and f.daohrq=to_date('"+Start_riq+"','yyyy-mm-dd')\n" + 
			"     group by (gy.dqid,f.lieid,dc.id)) dd\n" + 
			"     group by diancid,dqid)  wcdr,--�ճɱ�\n" + 
			"  (select diancid,dqid,SUM(LAIML) AS LAIML,sum(cbsl) as cbsl,\n" + 
			"       decode(sum(cbsl),0,0,sum(cbsl*farl)/sum(cbsl)) as farl,\n" + 
			"       decode(sum(cbsl),0,0,sum(cbsl*meij)/sum(cbsl)) as   meij,\n" + 
			"       decode(sum(cbsl),0,0,sum(cbsl*yunf)/sum(cbsl)) as   yunf,\n" + 
			"       decode(sum(cbsl),0,0,sum(cbsl*zaf)/sum(cbsl)) as   zaf\n" + 
			"  from ( select f.lieid,dc.id as diancid,gy.dqid,round_new(sum(f.biaoz),0)+round_new(sum(f.yingd),0)-round_new(sum(f.yingd-f.yingk),0)   as laiml,\n" + 
			"            round_new(sum(decode(nvl(r.id,0),0,0,f.biaoz)),0)+round_new(sum(decode(nvl(r.id,0),0,0,f.yingd)),0)-round_new(sum(decode(nvl(r.id,0),0,0,f.yingd-f.yingk)),0) as cbsl, --�ɱ�����,\n" + 
			"            decode(sum(decode(nvl(r.id,0),0,0,f.laimsl)),0,0,sum(round_new(nvl(zl.qnet_ar,0),2)*decode(nvl(r.id,0),0,0,f.laimsl))/sum(decode(nvl(r.id,0),0,0,f.laimsl))) as farl,\n" + 
			"            decode(sum(decode(nvl(r.id,0),0,0,f.laimsl)),0,0,sum(nvl(r.meij,0)*decode(nvl(r.id,0),0,0,f.laimsl))/sum(decode(nvl(r.id,0),0,0,f.laimsl))) as meij,\n" + 
			"            decode(sum(decode(nvl(r.id,0),0,0,f.laimsl)),0,0,sum(nvl(r.disyf+r.qiyf+r.kuangyf+r.yingdyf+r.daozzf ,0)*decode(nvl(r.id,0),0,0,f.laimsl))/sum(decode(nvl(r.id,0),0,0,f.laimsl))) as yunf,\n" + 
			"            decode(sum(decode(nvl(r.id,0),0,0,f.laimsl)),0,0,sum(nvl(r.jiaohqzf+r.qitzf,0)*decode(nvl(r.id,0),0,0,f.laimsl))/sum(decode(nvl(r.id,0),0,0,f.laimsl))) as zaf\n" + 
			"        from Rigjb r,fahb f,zhilb zl,vwdianc dc,vwgongys gy\n" + 
			"        where r.fahb_id(+)=f.lieid\n" + 
			"        and f.diancxxb_id=dc.id\n" + 
			"        and f.zhilb_id =zl.id(+)\n" + 
			"        and f.gongysb_id=gy.id\n" + strGongsID+" \n"+
			"        and f.daohrq>=first_day(to_date('"+Start_riq+"','yyyy-mm-dd')) and f.daohrq<=to_date('"+Start_riq+"','yyyy-mm-dd')\n" + 
			"     group by (gy.dqid,f.lieid,dc.id)) dd\n" + 
			"     group by diancid,dqid)  wclj,--�ճɱ�\n" + 
			"    (select dc.id  as diancid,gy.dqid,sum(jh.yuejhcgl) as jihl,\n" + 
			"           decode(sum(jh.yuejhcgl),0,0,sum(rez*yuejhcgl)/sum(yuejhcgl)) as rez,\n" + 
			"           decode(sum(jh.yuejhcgl),0,0,sum(jh.chebjg*yuejhcgl)/sum(yuejhcgl)) as chebjg,\n" + 
			"           decode(sum(jh.yuejhcgl),0,0,sum(jh.yunf*yuejhcgl)/sum(yuejhcgl)) as yunf,\n" + 
			"           decode(sum(jh.yuejhcgl),0,0,sum(jh.zaf*yuejhcgl)/sum(yuejhcgl)) as zaf\n" + 
			"    from yuecgjhb jh,vwdianc dc,vwgongys gy\n" + 
			"        where jh.diancxxb_id=dc.id\n" + 
			"        and jh.gongysb_id=gy.id \n" +strGongsID+" \n"+
			"        and jh.riq>=first_day(to_date('"+Start_riq+"','yyyy-mm-dd'))  and jh.riq<=to_date('"+Start_riq+"','yyyy-mm-dd')\n" + 
			"        and jh.yuejhcgl<>0\n" + 
			"    group by dc.id,gy.dqid) jh\n" + 
			"where dc.id=wcdr.diancid(+)\n" + 
			"     and dc.dqid=wcdr.dqid(+)\n" + 
			"     and dc.id=wclj.diancid(+)\n" + 
			"     and dc.dqid=wclj.dqid(+)\n" + 
			"     and dc.id=jh.diancid(+)\n" + 
			"     and dc.dqid=jh.dqid(+)\n" + 
			groupby+ordeby;
//			"group by rollup(dc.mingc,gongys)\n" + 
//			"order by grouping(dc.mingc) desc,max(dc.xuh),grouping(gongys) desc,max(gongysxh))";

			

		//System.out.println(sbsql);
		ResultSet rs = con.getResultSet(sbsql);
		Report rt = new Report();
		
		String ArrHeader[][]=new String[3][20];
		ArrHeader[0]=new String[] {"��λ","������λ","����Ԥ��","����Ԥ��","����Ԥ��","ʵ��������","ʵ��������","ʵ��������","ʵ��������","ʵ��������","ʵ��������","ʵ��������","ʵ��������","ʵ��������","ʵ��������","ʵ��������","ʵ��������","ʵ��������","ʵ��������","ʵ��������"};
		ArrHeader[1]=new String[] {"��λ","������λ","����Ԥ��","����Ԥ��","����Ԥ��","����","����","����","����","����","����","����","����","����","����","����","����","����","����","����"};
		ArrHeader[2]=new String[] {"��λ","������λ","�ɹ���","��ֵ","��ú����","�ɹ���","�ɱ���","��ֵ","ú��","�˼�","��ú����","�ɹ���","�ɱ���","���ȵ�����","��ֵ","��Ԥ��ֵ��ֵ","ú��","�˼�","��ú����","��Ԥ��ֵ��ֵ"};

		
//		int ArrWidth[]=new int[] {120,100,80,60,60,70,60,60,60,60,60,60,60,60,60,60,60,60};
		int ArrWidth[]=new int[] {100,100,60,40,50,60,40,40,40,40,40,50,60,40,40,50,40,40,50,60};


		Table bt=new Table(rs,3,0,2);
		rt.setBody(bt);
//	
		
		bt.setColAlign(2, Table.ALIGN_CENTER);
		if(rt.body.getRows()>4){
			rt.body.setCellAlign(3, 1,Table.ALIGN_CENTER);
		}
		//
//		rt.body.setUseDefaultCss(true);
		rt.body.ShowZero=false;
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.setPageRows(22);
		rt.body.mergeFixedRow();		//�ϲ���
		rt.body.mergeFixedCols();		//�Ͳ���
		
		int rows=rt.body.getRows();
		int cols=rt.body.getCols();
		
		for (int i=4;i<rows;i++){
			if (rt.body.getCellValue(i, 2).equals("�ϼ�")){
				for (int j=2;j<cols+1;j++){
					rt.body.getCell(i, j).backColor="#F2F2F2";
				}
			}
			if (!rt.body.getCell(i, 6).value.equals(rt.body.getCell(i, 7).value)){
				rt.body.getCell(i, 7).foreColor="blue";
			}
			
			if (!rt.body.getCell(i, 12).value.equals(rt.body.getCell(i, 13).value)){
				rt.body.getCell(i, 13).foreColor="blue";
			}
			
			String aa=rt.body.getCellValue(i, cols);
			double dangrhm=0;
			if (aa.equals("") || aa==null){
				dangrhm =-1;
			}else{
 				dangrhm=Double.parseDouble(aa);
			}
			if (rt.body.getCellValue(i, 2).equals("�ϼ�")&&dangrhm>0){
				for (int j=2;j<cols+1;j++){
					rt.body.getCell(i, j).foreColor="red";
				}
			}
		}
		
		rt.setTitle("ȼ�ϲɹ����ָ���������ձ�", ArrWidth);
		rt.setDefaultTitle(1, 5, "�Ʊ�λ:" +((Visit) getPage().getVisit()).getDiancqc(),Table.ALIGN_LEFT);
		rt.setDefaultTitle(7, 5,"����:"+FormatDate(DateUtil.getDate(getBeginriqDate())),Table.ALIGN_RIGHT);
		rt.setDefaultTitle(16, 5, "��λ:��,Ԫ/��,kcal/kg,%" ,Table.ALIGN_RIGHT);
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 2, "��ӡ����:"+FormatDate(DateUtil.getDate(DateUtil.FormatDate(new Date()))),Table.ALIGN_LEFT);
		rt.setDefautlFooter(9, 2, "��ˣ�", Table.ALIGN_LEFT);
		rt.setDefautlFooter(15, 2, "�Ʊ�", Table.ALIGN_LEFT);
		// ����ҳ��
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
		return rt.getAllPagesHtml();

	}

	private boolean _QueryClick = false;

	public void QueryButton(IRequestCycle cycle) {
		_QueryClick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_QueryClick) {
			_QueryClick = false;
		}
	}

	public String getPageHome() {
		if (((Visit) getPage().getVisit()).getboolean1()) {
			return "window.location = '" + MainGlobal.getHomeContext(this)
			+ "';";
		} else {
			return "";
		}
	}
	private String FormatDate(Date _date) {
		if (_date == null) {
			return "";
		}
		return DateUtil.Formatdate("yyyy��MM��dd��", _date);
	}
	///////
	public String getBeginriqDate(){
		if(((Visit) getPage().getVisit()).getString4()==null||((Visit) getPage().getVisit()).getString4()==""){
//			Calendar stra=Calendar.getInstance();
//			stra.set(DateUtil.getYear(new Date()), DateUtil.getMonth(new Date()), 1);
//			stra.add(Calendar.MONTH,-1);
			((Visit) getPage().getVisit()).setString4(DateUtil.FormatDate(new Date()));
		}
		return ((Visit) getPage().getVisit()).getString4();
	}
	public void setBeginriqDate(String value){
		((Visit) getPage().getVisit()).setString4(value);
	}
	

	private void getToolbars(){
		Toolbar tb1 = new Toolbar("tbdiv");
		tb1.addText(new ToolbarText("����:"));
		DateField df = new DateField();

		df.setValue(this.getBeginriqDate());
		df.Binding("qiandrq1","");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setWidth(80);
		tb1.addField(df);
		tb1.addText(new ToolbarText("-"));

		Visit visit = (Visit) getPage().getVisit();
		DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win,"diancTree",""+visit.getDiancxxb_id(),"",null,getTreeid());
		visit.setDefaultTree(dt);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		tf.setValue(((IDropDownModel)getDiancmcModel()).getBeanValue(Long.parseLong(getTreeid()==null||"".equals(getTreeid())?"-1":getTreeid())));

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
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (visit.getRenyID() == -1) {
			visit.setboolean1(true);
			return;
		}
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());

			visit.setList1(null);
			visit.setDropDownBean1(null);
			visit.setProSelectionModel1(null);
			visit.setDropDownBean2(null);
			visit.setProSelectionModel2(null);
			visit.setDropDownBean3(null);
			visit.setProSelectionModel3(null);
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
			visit.setDefaultTree(null);
			visit.setString4(null);
			visit.setString5(null);
			this.setTreeid(null);

		}
		getToolbars();

		blnIsBegin = true;

	}



	private String treeid;
	public String getTreeid() {
		if(treeid==null||treeid.equals("")){
			treeid=String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id());
		}
		return treeid;
	}
	public void setTreeid(String treeid) {
		if(treeid!=null) {
			if(!treeid.equals(this.treeid)) {
				((TextField)getToolbar().getItem("diancTree_text")).setValue
				(((IDropDownModel)getDiancmcModel()).getBeanValue(Long.parseLong(treeid)));
				((Visit) this.getPage().getVisit()).getDefaultTree().getTree().setSelectedNodeid(treeid);
			}
		}
		this.treeid = treeid;
	}
	public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}
//	�糧����
	private IPropertySelectionModel _IDiancModel;
	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel1() == null) {
			getDiancmcModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel1();
	}

	private boolean _DiancmcChange=false;
	public IDropDownBean getDiancmcValue() {
		if(((Visit)getPage().getVisit()).getDropDownBean1()==null){
			((Visit)getPage().getVisit()).setDropDownBean1((IDropDownBean)getDiancmcModel().getOption(0));
		}
		return ((Visit)getPage().getVisit()).getDropDownBean1();
	}

	public void setDiancmcValue(IDropDownBean Value) {
		if (((Visit)getPage().getVisit()).getDropDownBean1()==Value) {
			_DiancmcChange = false;
		}else{
			_DiancmcChange = true;
		}
		((Visit)getPage().getVisit()).setDropDownBean1(Value);
	}

	public IPropertySelectionModel getDiancmcModels() {
		String sql = "select id,mingc from diancxxb";
		setDiancmcModel(new IDropDownModel(sql)) ;
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public void setDiancmcModel(IPropertySelectionModel _value) {
		((Visit)getPage().getVisit()).setProSelectionModel1(_value);
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

//	�õ�ϵͳ��Ϣ�������õı������ĵ�λ����
	public String getBiaotmc(){
		String biaotmc="";
		JDBCcon cn = new JDBCcon();
		String sql_biaotmc="select  zhi from xitxxb where mingc='������ⵥλ����'";
		ResultSet rs=cn.getResultSet(sql_biaotmc);
		try {
			while(rs.next()){
				biaotmc=rs.getString("zhi");
			}
			rs.close();
		} catch (SQLException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		}finally{
			cn.Close();
		}

		return biaotmc;

	}

	//
	public Toolbar getToolbar() {
		return ((Visit)this.getPage().getVisit()).getToolbar();
	}
	public void setToolbar(Toolbar tb1) {
		((Visit)this.getPage().getVisit()).setToolbar(tb1);
	}
	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}
}
