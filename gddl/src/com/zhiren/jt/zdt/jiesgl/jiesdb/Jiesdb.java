//chh 2008-03-17 �޸Ĳ�ѯ��������ʽ
package com.zhiren.jt.zdt.jiesgl.jiesdb;

import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Jiesdb extends BasePage {

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

			return getFenctj();

	}
	
	
	//�����б�
	private String getFenctj() {
		JDBCcon con = new JDBCcon();
		
		Visit visit = (Visit) getPage().getVisit();
		String riq=FormatDate(DateUtil.getDate(getBeginriqDate()));
		String riq1=FormatDate(DateUtil.getDate(getEndriqDate()));
		//�糧����
		String strGongsID = "";
		String guolzj = "";
		int jib=this.getDiancTreeJib();
		if(jib==1){//ѡ����ʱˢ�³����еĵ糧
			strGongsID=" ";
		}else if (jib==2){//ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
			strGongsID = " and (dc.fuid=  " +this.getTreeid()+"or dc.shangjgsid="+this.getTreeid()+")";
			guolzj=" having not grouping(dc.fgsmc)=1\n";//�ֹ�˾�鿴����ʱ�����ܼơ�
		}else if (jib==3){//ѡ�糧ֻˢ�³��õ糧
			strGongsID=" and dc.id= " +this.getTreeid();
			guolzj=" having not grouping(dc.mingc)=1\n";
		}else if (jib==-1){
			strGongsID=" and dc.id = "+((Visit) getPage().getVisit()).getDiancxxb_id();
		}
		
		String strCondition="";
		
		if (getYunsfsDropDownValue().getId()!=-1){
			strCondition =" and ys.id=" +getYunsfsDropDownValue().getId() ;//ѡ�����䷽ʽ
		}
		
		if (-1!=(getJihkjDropDownValue().getId())){
			strCondition=strCondition+" and jh.id=" +getJihkjDropDownValue().getId();
		}
		
//		if (-1!=(getGongysValue().getId())){
//			strCondition=strCondition+" and gy.id=" +getGongysValue().getId();
//		}
		if(getGongysDropDownValue().getId()!=-1){
			strCondition=strCondition+" and js.shoukdw='" +getGongysDropDownValue().getValue()+"'";
		}
		
		String strt="";
		String strw="";
		if(getBaoblxValue().getValue().equals("�ֳ�")){		
			strt="select decode(grouping(dc.fgsmc)+grouping(dc.mingc),2,'�ϼ�',1,'&nbsp;&nbsp;'||dc.fgsmc,'&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc) as mingc \n";
			strw="group by rollup(dc.fgsmc,dc.mingc) \n"
				+guolzj+" \n"
				+"order by grouping(dc.fgsmc)desc,min(dc.fgsxh),grouping(dc.mingc)desc,min(dc.xuh) \n";
		}else if(getBaoblxValue().getValue().equals("�ֿ�")){
			String guolzj1="";
			if(jib!=2){
				guolzj1=guolzj;
			}else{
				guolzj1="";
			}
				
//			String Str="&'||'diancxxb_id='||dc.fgsid||'&'||'beginriq="+getBeginriqDate()+"'||'&'||'endriq="+getEndriqDate()+"'||'&'||'jihkjbid="+getJihkjDropDownValue().getId()+"'||'&'||'yunsfsid="+getYunsfsDropDownValue().getId()+"'||'&'||'lx='||";
			String Str="&'||'diancxxb_id='||"+this.getTreeid()+"||'&'||'beginriq="+getBeginriqDate()+"'||'&'||'endriq="+getEndriqDate()+"'||'&'||'jihkjbid="+getJihkjDropDownValue().getId()+"'||'&'||'yunsfsid="+getYunsfsDropDownValue().getId()+"'||'&'||'riqlx="+getLeixSelectValue().getId()+"'||'&'||'lx='||";
			strt="select decode(grouping(dc.fgsmc)+grouping(gy.dqmc),2,'�ϼ�',1,'&nbsp;&nbsp;'||dc.fgsmc,'&nbsp;&nbsp;&nbsp;&nbsp;'||getHtmlAllAlert('"+MainGlobal.getHomeContext(this)+"','Jiesdbgybreport','"+Str+"gy.dqid||'',gy.dqmc)) as mingc \n";
			strw="group by rollup(dc.fgsmc,(gy.dqmc,dc.fgsid,gy.dqid)) \n"
				+guolzj1+" \n"
				+"order by grouping(dc.fgsmc)desc,min(dc.fgsxh),grouping(gy.dqmc)desc,min(gy.xuh)";
		}else if(getBaoblxValue().getValue().equals("�ֳ��ֿ�")){
			strt="select decode(grouping(dc.fgsmc)+grouping(dc.mingc)+grouping(gy.dqmc),3,'�ܼ�',2,dc.fgsmc,1,'&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc,'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'||gy.dqmc) as mingc \n";
			strw="group by rollup(dc.fgsmc,dc.mingc,gy.dqmc)  \n"
				+guolzj+" \n"
				+"order by grouping(dc.fgsmc) desc,max(dc.fgsxh) ,dc.fgsmc, grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc, grouping(gy.dqmc) desc,max(gy.dqxh) ,gy.dqmc";
		}else if(getBaoblxValue().getValue().equals("�ֿ�ֳ�")){
			if(jib!=1){
				strt="select decode(grouping(dc.mingc)+grouping(gy.dqmc),2,'�ܼ�',1,gy.dqmc,'&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc) as mingc \n";
				strw="group by rollup(gy.dqmc,dc.mingc)  \n"
					+"order by  grouping(gy.dqmc) desc,max(gy.dqxh) ,gy.dqmc,grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc ";
			}else{
				strt="select decode(grouping(dc.fgsmc)+grouping(dc.mingc)+grouping(gy.dqmc),3,'�ܼ�',2,gy.dqmc,1,'&nbsp;&nbsp;&nbsp;&nbsp;'||dc.fgsmc,'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc) as mingc \n";
				strw="group by rollup(gy.dqmc,dc.fgsmc,dc.mingc)  \n"
					+"order by  grouping(gy.dqmc) desc,max(gy.dqxh) ,gy.dqmc,grouping(dc.fgsmc) desc,max(dc.fgsxh) ,dc.fgsmc,grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc ";
			}
		} 
		StringBuffer sbsql = new StringBuffer();
		if(getLeixSelectValue().getValue().equals("��������")){
				sbsql.append("select mingc,jiessl,rucsl,jiessl-rucsl as meilc,\n");
				sbsql.append("		 round(rez_js*4.1816/1000,2) as rezhi_js,rez_js as rez_jsdk,round(rez_rc*4.1816/1000,2) as rezhi_rc,rez_rc as rez_rcdk, \n");
				sbsql.append("       round(rez_js*4.1816/1000,2)-round(rez_rc*4.1816/1000,2) as rezc, rez_js-rez_rc as  rezcdk,  \n");
				sbsql.append("       round(decode(rez_js,0,0,(jiesbmdj-js_shuik-yf_shuik)*7000/rez_js),2) as jiesbmdj_b, \n");
				sbsql.append("		 round(decode(rez_rc,0,0,(rucbmdj-js_shuik-yf_shuik)*7000/rez_rc),2)  as rucbmdj_b, \n");
				sbsql.append("       round(decode(rez_js,0,0,(jiesbmdj-js_shuik-yf_shuik)*7000/rez_js),2)-round(decode(rez_rc,0,0,(rucbmdj-js_shuik-yf_shuik)*7000/rez_rc),2) as biaomdjc  \n");
				sbsql.append(" from ( \n");
				sbsql.append( strt+" \n");
				sbsql.append("         ,sum(js.jiessl) as jiessl,sum(cf.rucsl) as rucsl   \n");
				sbsql.append("         ,decode(sum(decode(rz.jiesrz,'',0,js.jiessl)),0,0,round_new(sum(js.jiessl*nvl(rz.jiesrz,0))/sum(decode(rz.jiesrz,'',0,js.jiessl)),0)) as rez_js    \n");
				sbsql.append("         ,decode(sum(cf.rucsl),0,0,round_new(sum(cf.rucsl*nvl(rz.rucrz,0))/sum(cf.rucsl),0)) as rez_rc   \n");
				sbsql.append("         ,decode(sum(js.jiessl),0,0,round(sum( js.hansmk+nvl(js.bukmk,0)+nvl(yf.guotyf,0)+nvl(yf.dityf,0)+nvl(yf.qiyf,0)+nvl(yf.bukyf,0)-nvl(yf.guotyfjf,0) +nvl(yf.guotzf,0)+nvl(yf.ditzf,0)-nvl(yf.guotzfjf,0))/sum(js.jiessl),2)) as jiesbmdj  \n");
				sbsql.append("		   ,decode(sum(js.jiessl),0,0,round(sum(js.shuik)/sum(js.jiessl),2)) as js_shuik,decode(sum(js.jiessl),0,0,round(sum(yf.shuik)/sum(js.jiessl),2)) as yf_shuik  \n");
				sbsql.append("         ,decode(sum(cf.rucsl),0,0,round(sum( js.hansmk+nvl(js.bukmk,0)+nvl(yf.guotyf,0)+nvl(yf.dityf,0)+nvl(yf.qiyf,0)+nvl(yf.bukyf,0)-nvl(yf.guotyfjf,0) +nvl(yf.guotzf,0)+nvl(yf.ditzf,0)-nvl(yf.guotzfjf,0))/sum(cf.rucsl),2)) as rucbmdj   \n");
		//		-----------------------------�ѵ糧��͹�Ӧ�̱���vwdianc dc,vwgongys y ��ͼ----------------------------
				sbsql.append("  from jiesb js,jiesyfb yf,vwdianc dc,vwgongys gy ,yunsfsb ys,jihkjb jh,  \n");
				sbsql.append("       (select jszb.jiesdid as id ,jszb.changf as rucsl from jieszbsjb jszb,zhibb zb,jiesb jsb  \n");
				sbsql.append("               where jszb.zhibb_id = zb.id and zb.bianm='����' and jszb.jiesdid= jsb.id  \n");
				sbsql.append("                     and jsb.jiesrq >=to_date('"+getBeginriqDate()+"','yyyy-mm-dd')and jsb.jiesrq<=to_date('"+getEndriqDate()+"','yyyy-mm-dd'))cf,  \n");
				sbsql.append("       (select jszb.jiesdid as id ,jszb.jies as jiesrz,jszb.changf as rucrz from jieszbsjb jszb,zhibb zb,jiesb jsb \n");
				sbsql.append("               where jszb.zhibb_id = zb.id and zb.bianm='�յ�����λ��ֵQnetar(MJ/Kg)' and jszb.jiesdid= jsb.id  \n");
				sbsql.append("                     and jsb.jiesrq >=to_date('"+getBeginriqDate()+"','yyyy-mm-dd')and jsb.jiesrq<=to_date('"+getEndriqDate()+"','yyyy-mm-dd'))rz  \n");
		//		-----------------------------�������vwdianc dc,vwgongys gy ��ͼ�Ĺ���----------------------------
				sbsql.append("  where js.diancxxb_id=dc.id  and js.gongysb_id=gy.id   \n");
				sbsql.append("        and yf.diancjsmkb_id=js.id  and js.id = cf.id and js.id = rz.id(+) and js.yunsfsb_id=ys.id and js.jihkjb_id=jh.id(+) \n");
				sbsql.append("        and js.jiesrq >=to_date('"+getBeginriqDate()+"','yyyy-mm-dd')and js.jiesrq<=to_date('"+getEndriqDate()+"','yyyy-mm-dd')  \n");
				sbsql.append("      "+strGongsID+strCondition+" \n");
				sbsql.append(strw).append("  ) \n"); 
		}else{
				sbsql.append("select mingc,jiessl,rucsl,jiessl-rucsl as meilc, \n");
				sbsql.append("       round(rez_js*4.1816/1000,2) as rezhi_js,rez_js as rez_jsdk,round(rez_rc*4.1816/1000,2) as rezhi_rc,rez_rc as rez_rcdk,  \n");
				sbsql.append("       round(rez_js*4.1816/1000,2)-round(rez_rc*4.1816/1000,2) as rezc, rez_js-rez_rc as  rezcdk,   \n");
				sbsql.append("       round(decode(rez_js,0,0,(jiesbmdj-js_shuik-yf_shuik)*7000/rez_js),2) as jiesbmdj_b,  \n");
				sbsql.append("       round(decode(rez_rc,0,0,(rucbmdj-js_shuik-yf_shuik)*7000/rez_rc),2)  as rucbmdj_b, \n");
				sbsql.append("       round(decode(rez_js,0,0,(jiesbmdj-js_shuik-yf_shuik)*7000/rez_js),2)-round(decode(rez_rc,0,0,(rucbmdj-js_shuik-yf_shuik)*7000/rez_rc),2) as biaomdjc   \n");
				sbsql.append(" from (  \n");
				sbsql.append( strt+" \n");
				sbsql.append("         ,sum(cf.jiessl) as jiessl,sum(cf.rucsl) as rucsl    \n");
				sbsql.append("         ,decode(sum(decode(rz.jiesrz,'',0,cf.jiessl)),0,0,round_new(sum(cf.jiessl*nvl(rz.jiesrz,0))/sum(decode(rz.jiesrz,'',0,cf.jiessl)),0)) as rez_js     \n");
				sbsql.append("         ,decode(sum(cf.rucsl),0,0,round_new(sum(cf.rucsl*nvl(rz.rucrz,0))/sum(cf.rucsl),0)) as rez_rc    \n");
				sbsql.append("         --,decode(sum(cf.jiessl),0,0,round(sum( js.hansmk+nvl(js.bukmk,0)+nvl(yf.guotyf,0)+nvl(yf.dityf,0)+nvl(yf.qiyf,0)+nvl(yf.bukyf,0)-nvl(yf.guotyfjf,0) +nvl(yf.guotzf,0)+nvl(yf.ditzf,0)-nvl(yf.guotzfjf,0))/sum(cf.jiessl),2)) as jiesbmdj   \n");
				sbsql.append("         ,decode(sum(cf.jiessl),0,0,round(sum(js.zongdj*cf.jiessl )/sum(cf.jiessl),2)) as jiesbmdj   \n");
				sbsql.append("         ,decode(sum(cf.jiessl),0,0,round(sum(js.shuikdj_js*cf.jiessl)/sum(cf.jiessl),2)) as js_shuik \n");
				sbsql.append("         ,decode(sum(cf.jiessl),0,0,round(sum(shuikdj_yf*cf.jiessl)/sum(cf.jiessl),2)) as yf_shuik \n");
				sbsql.append("         --,decode(sum(cf.jiessl),0,0,round(sum( js.hansmk+nvl(js.bukmk,0)+nvl(yf.guotyf,0)+nvl(yf.dityf,0)+nvl(yf.qiyf,0)+nvl(yf.bukyf,0)-nvl(yf.guotyfjf,0) +nvl(yf.guotzf,0)+nvl(yf.ditzf,0)-nvl(yf.guotzfjf,0))/sum(cf.rucsl),2)) as rucbmdj    \n");
				sbsql.append("         ,decode(sum(cf.jiessl),0,0,round(sum(js.zongdj*cf.rucsl)/sum(cf.rucsl),2)) as rucbmdj    \n");
				sbsql.append("  from vwdianc dc,vwgongys gy ,yunsfsb ys,jihkjb jh, \n");
				sbsql.append("       (select js.id as id,case when js.ches=sm.ches then jszb.changf else sm.shisl  end as rucsl, \n");
				sbsql.append("      					 case when js.ches = sm.ches then js.jiessl else round_new(sm.shisl/jszb.changf*js.jiessl,0) end as jiessl \n");
				sbsql.append("        from  \n");
				sbsql.append("        (select jiesb_id,sum(shisl) as shisl,sum(ches) as ches \n");
				sbsql.append("                   from (select lieid, jiesb_id,sum(ches) as ches, \n");
				sbsql.append("                                round_new (sum(biaoz),0)+round_new (sum(yingk),0)+round_new(sum(yingk),0) shisl \n");
				sbsql.append("                          from fahb  fh \n");
				sbsql.append("                          where fh.daohrq>=to_date('"+getBeginriqDate()+"','yyyy-mm-dd') and fh.daohrq<=to_date('"+getEndriqDate()+"','yyyy-mm-dd') \n");
				sbsql.append("                          group by lieid,jiesb_id) \n");
				sbsql.append("                      group by jiesb_id \n");
				sbsql.append("                ) sm,jiesb js,jieszbsjb jszb,zhibb zb \n");
				sbsql.append("         where sm.jiesb_id=js.id and jszb.jiesdid=js.id and jszb.zhibb_id=zb.id  \n");
				sbsql.append("               and zb.bianm='����' \n");
				sbsql.append("        )cf,   \n");
				sbsql.append("      ( select js.id as id,jszb.jies as jiesrz,jszb.changf as rucrz \n");
				sbsql.append("        from  \n");
				sbsql.append("        (select jiesb_id,sum(ches) as ches \n");
				sbsql.append("           from (select lieid, jiesb_id,sum(ches) as ches \n");
				sbsql.append("                 from fahb  fh \n");
				sbsql.append("                 where fh.daohrq>=to_date('"+getBeginriqDate()+"','yyyy-mm-dd') and fh.daohrq<=to_date('"+getEndriqDate()+"','yyyy-mm-dd') \n");
				sbsql.append("                 group by lieid,jiesb_id) \n");
				sbsql.append("              group by jiesb_id \n");
				sbsql.append("        ) sm,jiesb js,jieszbsjb jszb,zhibb zb \n");
				sbsql.append("        where sm.jiesb_id=js.id and jszb.jiesdid=js.id and jszb.zhibb_id=zb.id  \n");
				sbsql.append("       and zb.bianm='�յ�����λ��ֵQnetar(MJ/Kg)'  \n");
				sbsql.append("     )rz , \n");
				sbsql.append("     (select  js.id ,js.diancxxb_id as diancxxb_id,js.gongysb_id as gongysb_id,js.yunsfsb_id as yunsfsb_id,js.jihkjb_id as jihkjb_id,js.shoukdw as shoukdw,js.jiessl, \n");
				sbsql.append("          (js.hansmk/js.jiessl+js.bukmk/js.jiessl+yf.guotyf/js.jiessl+yf.dityf/js.jiessl+yf.qiyf/js.jiessl \n");
				sbsql.append("          +yf.bukyf/js.jiessl-yf.guotyfjf/js.jiessl+yf.guotzf/js.jiessl+yf.ditzf/js.jiessl-yf.guotzfjf/js.jiessl) as zongdj, \n");
				sbsql.append("          (js.shuik/js.jiessl) as shuikdj_js,(yf.shuik/js.jiessl) as shuikdj_yf  \n");
				sbsql.append("       from jiesb js,jiesyfb yf, \n");
				sbsql.append("          (select jiesb_id,sum(ches) as ches \n");
				sbsql.append("           from (select lieid, jiesb_id,sum(ches) as ches \n");
				sbsql.append("                 from fahb  fh \n");
				sbsql.append("                 where fh.daohrq>=to_date('"+getBeginriqDate()+"','yyyy-mm-dd') and fh.daohrq<=to_date('"+getEndriqDate()+"','yyyy-mm-dd') \n");
				sbsql.append("                 group by lieid,jiesb_id) \n");
				sbsql.append("              group by jiesb_id \n");
				sbsql.append("          ) sm \n");
				sbsql.append("       where yf.diancjsmkb_id=js.id  and  sm.jiesb_id=js.id  \n");
				sbsql.append("    )js \n");
				sbsql.append(" where js.diancxxb_id=dc.id  and js.gongysb_id=gy.id and js.yunsfsb_id=ys.id \n");
				sbsql.append("        and  js.jihkjb_id=jh.id and js.id = cf.id(+) and js.id = rz.id(+) \n");
				sbsql.append("      "+strGongsID+strCondition+" \n");
				sbsql.append(strw).append("  ) \n");
		}
		ResultSet rs = con.getResultSet(sbsql.toString());
		Report rt = new Report();
		String[][] ArrHeader;
		int[] ArrWidth;
		
			ArrHeader=new String[2][13];
	     	ArrHeader[0]=new String[] {"��λ","����ú��<br>(��)","�볧ú��<br>(��)","ú����<br>(��)","������ֵ","������ֵ","�볧��ֵ","�볧��ֵ","��ֵ��","��ֵ��","���㲻��˰��ú����<br>(Ԫ/��)","�볧����˰��ú����<br>(Ԫ/��)","��ú���۲�<br>(Ԫ/��)"};
	     	ArrHeader[1]=new String[] {"��λ","����ú��<br>(��)","�볧ú��<br>(��)","ú����<br>(��)","(MJ/kg)","(Kcal/kg)","(MJ/kg)","(Kcal/kg)","(MJ/kg)","(Kcal/kg)","���㲻��˰��ú����<br>(Ԫ/��)","�볧����˰��ú����<br>(Ԫ/��)","��ú���۲�<br>(Ԫ/��)"};
	     	
	     	ArrWidth =new int[] {150,70,70,70,70,70,70,70,70,70,70,70,70};
			rt.setBody(new Table(rs, 2, 0, 1));
			//
			rt.body.ShowZero=true;
			rt.body.setWidth(ArrWidth);
			rt.body.setHeaderData(ArrHeader);
			rt.body.mergeFixedRow();//�ϲ���
			rt.body.mergeFixedCols();//�Ͳ���

			rt.setTitle("ȼ�Ͻ���Աȱ�", ArrWidth);
			rt.title.setRowHeight(2, 50);
			rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
			rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
			rt.setDefaultTitle(1, 2, "�Ʊ�λ:" +((Visit) getPage().getVisit()).getDiancqc(),Table.ALIGN_LEFT);
//			rt.setDefaultTitle(12, 2, "��λ:(��)" ,Table.ALIGN_RIGHT);
			rt.setDefaultTitle(5, 3,getBeginriqDate()+"��"+getEndriqDate(),Table.ALIGN_CENTER);
			rt.body.setPageRows(24);
			rt.body.mergeCol(1);
//			rt.body.mergeCol(2);
			rt.createDefautlFooter(ArrWidth);
			rt.setDefautlFooter(1, 3, "��ӡ����:"+FormatDate(DateUtil.getDate(DateUtil.FormatDate(new Date()))),Table.ALIGN_LEFT);
//			rt.setDefautlFooter(7,1,"��λ:������",Table.ALIGN_RIGHT);
	
		// ����ҳ��
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
		return rt.getAllPagesHtml();
	}
//	________________________________________________________

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
	public String getBeginriqDate() {
		if (((Visit) getPage().getVisit()).getString4() == null
				|| ((Visit) getPage().getVisit()).getString4() == "") {
			Calendar stra = Calendar.getInstance();
			stra.set(DateUtil.getYear(new Date()), DateUtil
					.getMonth(new Date()), 1);
			stra.add(Calendar.MONTH, -1);
			((Visit) getPage().getVisit()).setString4(DateUtil.FormatDate(stra.getTime()));
		}
		return ((Visit) getPage().getVisit()).getString4();
	}

	private boolean BeginriqChange = false;
	public void setBeginriqDate(String value) {
		if(((Visit) getPage().getVisit()).getString4()!=null)
		if(((Visit) getPage().getVisit()).getString4().equals(value)){
			BeginriqChange=false;
		}else{
			BeginriqChange=true;
		}
		((Visit) getPage().getVisit()).setString4(value);
	}

	public String getEndriqDate(){
		if(((Visit) getPage().getVisit()).getString5()==null||((Visit) getPage().getVisit()).getString5()==""){
			((Visit) getPage().getVisit()).setString5(DateUtil.FormatDate(new Date()));
		}
		return ((Visit) getPage().getVisit()).getString5();
	}
	private boolean EndriqChange = false; 
	public void setEndriqDate(String value){
		if(((Visit) getPage().getVisit()).getString5()!=null)
		if(((Visit) getPage().getVisit()).getString5().equals(value)){
			EndriqChange=false;
		}else{
			EndriqChange=true;
		}
		((Visit) getPage().getVisit()).setString5(value);
	}

	private void getToolbars(){
		Toolbar tb1 = new Toolbar("tbdiv");
		
		tb1.addText(new ToolbarText("����:"));
		ComboBox cb1 = new ComboBox();
		cb1.setTransform("LeixSelect");
		cb1.setWidth(80);
		tb1.addField(cb1);
		tb1.addText(new ToolbarText("-"));
		
//		tb1.addText(new ToolbarText("��������:"));
		DateField df = new DateField();
		
		df.setValue(this.getBeginriqDate());
		df.Binding("qiandrq1","");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setWidth(40);
		tb1.addField(df);
		
		DateField df1 = new DateField();
		df1.setValue(this.getEndriqDate());
		df1.Binding("qiandrq2","");// ��htmlҳ�е�id��,���Զ�ˢ��
		df1.setWidth(40);
		tb1.addField(df1);
		tb1.addText(new ToolbarText("-"));
		
		
		Visit visit = (Visit) getPage().getVisit();
		DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win,"diancTree",""+visit.getDiancxxb_id(),"",null,getTreeid());
		visit.setDefaultTree(dt);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(80);
		tf.setValue(((IDropDownModel)getDiancmcModel()).getBeanValue(Long.parseLong(getTreeid()==null||"".equals(getTreeid())?"-1":getTreeid())));
		 
		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);
		
		tb1.addText(new ToolbarText("��λ:"));
		tb1.addField(tf);
		tb1.addItem(tb2);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("ͳ�ƿھ�:"));
		ComboBox cb = new ComboBox();
		cb.setTransform("BAOBLX");
		cb.setWidth(50);
		cb.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(cb);
		
		tb1.addText(new ToolbarText("-"));
		
		
		tb1.addText(new ToolbarText("��Ӧ��:"));
		ComboBox cb2 = new ComboBox();
		cb2.setTransform("GongysDropDown");
		cb2.setWidth(100);
		cb2.setEditable(true);
		//meik.setWidth(60);
		//meik.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(cb2);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("���䷽ʽ:"));
		ComboBox tbysfs = new ComboBox();
		tbysfs.setTransform("YunsfsDropDown");
		tbysfs.setWidth(60);
		tb1.addField(tbysfs);
		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("�ƻ��ھ�:"));
		ComboBox tbjhkj = new ComboBox();
		tbjhkj.setTransform("JihkjDropDown");
		tbjhkj.setWidth(60);
		tb1.addField(tbjhkj);
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
			visit.setString1(null);
			visit.setString4(null);
			visit.setString5(null);
			visit.setDropDownBean1(null);
			visit.setProSelectionModel1(null);
			visit.setDropDownBean2(null);
			visit.setProSelectionModel2(null);
			visit.setDropDownBean3(null);
			visit.setProSelectionModel3(null);
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
			visit.setDropDownBean5(null);
			visit.setProSelectionModel5(null);
			visit.setDropDownBean6(null);
			visit.setProSelectionModel6(null);
			visit.setDropDownBean10(null);
			visit.setProSelectionModel10(null);
			visit.setDropDownBean11(null);
			visit.setProSelectionModel11(null);
			visit.setDropDownBean12(null);
			visit.setProSelectionModel12(null);
			this.setTreeid(null);
			visit.setDefaultTree(null);
			setTreeid(null);
			this.setBeginriqDate(DateUtil.FormatDate(visit.getMorkssj()));
			this.setEndriqDate(DateUtil.FormatDate(visit.getMorjssj()));
		}
		if(_LeixSelectChange){
			_LeixSelectChange=false;
			getGongysDropDownModels();
		}
		if(BeginriqChange){
			BeginriqChange=false;
			getGongysDropDownModels();
		}
		if(EndriqChange){
			EndriqChange=false;
			getGongysDropDownModels();
		}
		if(treeid_change){
			treeid_change=false;
			getGongysDropDownModels();
		}
//		getGongysDropDownModels();

		getToolbars();
		blnIsBegin = true;
	}
	  //����
	private boolean _LeixSelectChange=false;
    public IDropDownBean getLeixSelectValue() {
    	if( ((Visit) getPage().getVisit()).getDropDownBean12()==null){
   		 ((Visit) getPage().getVisit()).setDropDownBean12((IDropDownBean)getLeixSelectModel().getOption(0));
   	}
       return  ((Visit) getPage().getVisit()).getDropDownBean12();
    }
    public void setLeixSelectValue(IDropDownBean Value) {
    	if (((Visit)getPage().getVisit()).getDropDownBean12()==Value) {
    		_LeixSelectChange = false;
		}else{
			_LeixSelectChange = true;
		}
	    	((Visit) getPage().getVisit()).setDropDownBean12(Value);

    }
    
    public void setLeixSelectModel(IPropertySelectionModel value) {
    	((Visit) getPage().getVisit()).setProSelectionModel12(value);
    }

    public IPropertySelectionModel getLeixSelectModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel12() == null) {
            getLeixSelectModels();
        }
        
        return ((Visit) getPage().getVisit()).getProSelectionModel12();
    }
    public void getLeixSelectModels() {
        List list=new ArrayList();
        list.add(new IDropDownBean(0,"��������"));
        list.add(new IDropDownBean(1,"�볧����"));
        ((Visit) getPage().getVisit()).setProSelectionModel12(new IDropDownModel(list)) ;
        return ;
    }
	private String treeid;
	public String getTreeid() {
		if(treeid==null||treeid.equals("")){
			treeid=String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id());
		}
		return treeid;
	}
	private boolean treeid_change = false;
	public void setTreeid(String treeid) {
		if(treeid!=null) {
			if(!treeid.equals(this.treeid)) {
				((TextField)getToolbar().getItem("diancTree_text")).setValue
				(((IDropDownModel)getDiancmcModel()).getBeanValue(Long.parseLong(treeid)));
				((Visit) this.getPage().getVisit()).getDefaultTree().getTree().setSelectedNodeid(treeid);
				treeid_change=true;
			}else{
				treeid_change=false;
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
//		�õ��糧����ֵ�ļ���,(jib:1,2,3,) 1�Ǽ���,2�Ƿֹ�˾,3�ǵ糧
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

//	�ƻ��ھ�
	public IDropDownBean getJihkjDropDownValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean5() == null) {
			((Visit) getPage().getVisit()).setDropDownBean5((IDropDownBean) getJihkjDropDownModel().getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean5();
	}

	public void setJihkjDropDownValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean5(Value);
	}

	public void setJihkjfsDropDownModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel5(value);
	}

	public IPropertySelectionModel getJihkjDropDownModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {
			getJihkjDropDownModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}

	public void getJihkjDropDownModels() {
		String sql = "select id,mingc\n" + "from jihkjb \n";
		((Visit) getPage().getVisit()).setProSelectionModel5(new IDropDownModel(sql, "ȫ��"));
		return;
	}	
	
//	���䷽ʽ
	public IDropDownBean getYunsfsDropDownValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean6() == null) {
			((Visit) getPage().getVisit()).setDropDownBean6((IDropDownBean) getYunsfsDropDownModel().getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean6();
	}

	public void setYunsfsDropDownValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean6(Value);
	}

	public void setYunsfsDropDownModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel6(value);
	}

	public IPropertySelectionModel getYunsfsDropDownModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel6() == null) {
			getYunsfsDropDownModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel6();
	}

	public void getYunsfsDropDownModels() {
		String sql = "select id,mingc\n" + "from yunsfsb \n";
		((Visit) getPage().getVisit()).setProSelectionModel6(new IDropDownModel(sql, "ȫ��"));
		return;
	}

//		 ��Ӧ��
		private boolean GongysDropChange =false;
	    public IDropDownBean getGongysDropDownValue() {
	    	if( ((Visit) getPage().getVisit()).getDropDownBean10()==null){
	   		 ((Visit) getPage().getVisit()).setDropDownBean10((IDropDownBean)getGongysDropDownModel().getOption(0));
	   	}
	       return  ((Visit) getPage().getVisit()).getDropDownBean10();
	    }
	    public void setGongysDropDownValue(IDropDownBean Value) {
	    	long id = -2;
			if (((Visit)getPage().getVisit()).getDropDownBean10()!= null) {
				id = getGongysDropDownValue().getId();
			}
			if (Value != null) {
				if (Value.getId() != id) {
					GongysDropChange = true;
				} else {
					GongysDropChange = false;
				}
			}
//			((Visit)getPage().getVisit()).setDropDownBean3(Value);
	    	
	    	((Visit) getPage().getVisit()).setDropDownBean10(Value);
	    }
	    public void setGongysDropDownModel(IPropertySelectionModel value) {
	    	((Visit) getPage().getVisit()).setProSelectionModel10(value);
	    }

	    public IPropertySelectionModel getGongysDropDownModel() {
	        if (((Visit) getPage().getVisit()).getProSelectionModel10() == null) {
	            getGongysDropDownModels();
	        }
	        return ((Visit) getPage().getVisit()).getProSelectionModel10();
	    }
	    public void getGongysDropDownModels() {
			String tiaoj="";
			String fahtj="";
	    	if(getLeixSelectValue().getValue().equals("��������")){
	    		fahtj="";
	    		tiaoj="and j.jiesrq>=to_date('"+getBeginriqDate()+"','yyyy-mm-dd') and j.jiesrq<=to_date('"+getEndriqDate()+"','yyyy-mm-dd')\n";
	    	}else{
	    		fahtj="  ,(select jiesb_id,sum(ches) as ches\n" + 
	    			"           from (select lieid, jiesb_id,sum(ches) as ches\n" + 
	    			"                 from fahb  fh\n" + 
	    			"                 where fh.daohrq>=to_date('"+getBeginriqDate()+"','yyyy-mm-dd')\n" + 
	    			"                        and fh.daohrq<=to_date('"+getEndriqDate()+"','yyyy-mm-dd')\n" + 
	    			"                 group by lieid,jiesb_id)\n" + 
	    			"              group by jiesb_id\n" + 
	    			"        ) sm \n";

	    		tiaoj="and j.id=sm.jiesb_id \n";
	    	}
	    	

	    	String  sql ="";
		    	sql="select distinct max(j.id) as id,j.shoukdw as mingc from jiesb j,diancxxb dc"+fahtj+" \n" +
					"where j.diancxxb_id in (select id \n"+
					 "from( select id from diancxxb start with id="+this.getTreeid()+" connect by (fuid=prior id or shangjgsid=prior id))"+
					 "union"+
					" select id"+
					" from diancxxb"+
					" where id="+this.getTreeid()+")"+tiaoj+
//					 "and j.jiesrq>=to_date('"+getBeginriqDate()+"','yyyy-mm-dd') and j.jiesrq<=to_date('"+getEndriqDate()+"','yyyy-mm-dd')\n"+
			        "group by (j.shoukdw) order by j.shoukdw";

	    	((Visit) getPage().getVisit()).setProSelectionModel10(new IDropDownModel(sql,"ȫ��")) ;
	        return ;
	    }
	 
//		 ͳ�ƿھ�
		public boolean _Baoblxchange = false;
		private IDropDownBean _BaoblxValue;

		public IDropDownBean getBaoblxValue() {
			if(((Visit)getPage().getVisit()).getDropDownBean11()==null){
				((Visit)getPage().getVisit()).setDropDownBean11((IDropDownBean)getIBaoblxModels().getOption(0));
			}
			return ((Visit)getPage().getVisit()).getDropDownBean11();
		}

		public void setBaoblxValue(IDropDownBean Value) {
			long id = -2;
			if (((Visit)getPage().getVisit()).getDropDownBean11() != null) {
				id = ((Visit)getPage().getVisit()).getDropDownBean11().getId();
			}
			if (Value != null) {
				if (Value.getId() != id) {
					_Baoblxchange = true;
				} else {
					_Baoblxchange = false;
				}
			}
			((Visit)getPage().getVisit()).setDropDownBean11(Value);
		}

		private IPropertySelectionModel _IBaoblxModel;

		public void setIBaoblxModel(IPropertySelectionModel value) {
			((Visit)getPage().getVisit()).setProSelectionModel11(value);
		}

		public IPropertySelectionModel getIBaoblxModel() {
			if (((Visit)getPage().getVisit()).getProSelectionModel11() == null) {
				getIBaoblxModels();
			}
			return ((Visit)getPage().getVisit()).getProSelectionModel11();
		}

		public IPropertySelectionModel getIBaoblxModels() {
			
			List List = new ArrayList();
			List.add(new IDropDownBean(0,"�ֳ�"));
			List.add(new IDropDownBean(1,"�ֿ�"));
			List.add(new IDropDownBean(2,"�ֳ��ֿ�"));
			List.add(new IDropDownBean(3,"�ֿ�ֳ�"));
			((Visit)getPage().getVisit()).setProSelectionModel11(new IDropDownModel(List));
			
			return ((Visit)getPage().getVisit()).getProSelectionModel11();
		}
		
}

