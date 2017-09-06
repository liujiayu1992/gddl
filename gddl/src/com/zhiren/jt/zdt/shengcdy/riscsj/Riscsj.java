package com.zhiren.jt.zdt.shengcdy.riscsj;

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
import com.zhiren.common.ResultSetList;
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

/*
 * ���ߣ�������
 * ʱ�䣺2010-01-29 
 * �������޸Ĺ��ڱ����A4ֽ�ĸ�ʽ���⣬
 * 		 �ѱ���ĸ�ʽ�趨ΪA4��ӡ��ʽ��
 */
/**
 * chenzt
 * 2010-04-06
 * �������޸ĺӱ��ֹ�˾������Ʊ���һ�� ����������Ϊû�У�null��
 */

public class Riscsj extends BasePage {
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

		if(getLeixSelectValue().getValue().equals("������Ϣ���")){
			return getZhiltj();
		}else if(getLeixSelectValue().getValue().equals("������Ϣ���")){//������Ϣ���
			return getZhiltjjb();
		}
		return "";
	}
	
	//������Ϣ���
	private String getZhiltjjb(){
		JDBCcon con = new JDBCcon();
		
		String riq=FormatDate(DateUtil.getDate(getBeginriqDate()));
		int jib=this.getDiancTreeJib();
		String diancCondition=
			"and dc.id in (select id\n" + 
			" from(\n" + 
			" select id from diancxxb\n" + 
			" start with id="+getTreeid()+"\n" + 
			" connect by (fuid=prior id or shangjgsid=prior id)\n" + 
			" )\n" + 
			" union\n" + 
			" select id\n" + 
			" from diancxxb\n" + 
			" where id="+getTreeid()+") " ; 
		String dianw1="";
		String dianw2="";
		String havingnot="";
		
		StringBuffer sbsql = new StringBuffer("");
		
		StringBuffer sbsql_sj = new StringBuffer("");//��������
		/*sbsql_sj.append("(select dc.id as diancxxb_id,jz.id as jizb_id,jz.jizbh,rs.gongdl as gongdl,rs.fadl as fadl,rs.shangwdl as shangwdl,rs.gongrl as gongrl\n");
		sbsql_sj.append("      from riscsjb rs,diancxxb dc,jizb jz\n");
		sbsql_sj.append("      where rs.diancxxb_id=dc.id and rs.jizb_id=jz.id and rs.riq>=to_date('"+getBeginriq1Date()+"','yyyy-mm-dd')\n");
		sbsql_sj.append("      and rs.riq<=to_date('"+getBeginriqDate()+"','yyyy-mm-dd') "+diancCondition+" ) r,\n");*/
		
		 
		sbsql_sj.append("(select dcjz.diancxxb_id,dcjz.jizb_id,dcjz.jizbh,nvl(rs.gongdl,0) as gongdl,  \n");
		sbsql_sj.append("       nvl(rs.fadl,0) as fadl,nvl(rs.shangwdl,0) as shangwdl,  \n");
		sbsql_sj.append("       nvl(rs.gongrl,0) as gongrl,nvl(dcjz.jizurl,0) as jizrl from               \n");
		sbsql_sj.append("(select rs.diancxxb_id,rs.jizb_id,sum(rs.fadl) as fadl,sum(rs.shangwdl) as shangwdl, \n");
		sbsql_sj.append("        sum(rs.gongrl) as gongrl,sum(rs.gongdl) as gongdl from riscsjb rs  \n");
		sbsql_sj.append(" where rs.riq>=to_date('"+getBeginriq1Date()+"','yyyy-mm-dd') and  rs.riq<=to_date('"+getBeginriqDate()+"','yyyy-mm-dd') \n");
		sbsql_sj.append(" group by rs.diancxxb_id,rs.jizb_id) rs,  \n");
		sbsql_sj.append("(select dc.id as diancxxb_id,jz.id as jizb_id,jz.jizbh,jz.jizurl,jz.jihdl   \n");
		sbsql_sj.append("        from diancxxb dc,jizb jz where jz.diancxxb_id=dc.id  ) dcjz  \n");
		sbsql_sj.append(" where rs.diancxxb_id(+)=dcjz.diancxxb_id and rs.jizb_id(+)=dcjz.jizb_id) r \n");
		
//		sbsql_sj.append("(select dc.mingc,dc.id as id,dc.xuh1,dc.xuh2,dc.jizid,dc.fuid,dc.jizbh as jizbh,dc.jizurl as jizurl, \n");
//		sbsql_sj.append("		  decode(yx.shebzt,'','����',yx.shebzt) as shebzt, \n");
//		sbsql_sj.append("		  decode(shebzt, '����','', to_char(kaisrq, 'yyyy-mm-dd')) as kaisrq, \n");
//		sbsql_sj.append("		  decode(shebzt, '����','', to_char(jiesrq, 'yyyy-mm-dd')) as jiesrq \n");
//		sbsql_sj.append(" from (select yx.* from jizyxqkb yx,diancxxb dc where yx.diancxxb_id=dc.id "+diancCondition+" \n");
////		sbsql_sj.append("		and yx.kaisrq=(case when to_date('"+getBeginriqDate()+"','yyyy-mm-dd')>=yx.kaisrq and to_date('"+getBeginriqDate()+"','yyyy-mm-dd')<=yx.jiesrq \n");
////		sbsql_sj.append(" then yx.kaisrq else to_date('"+getBeginriqDate()+"','yyyy-mm-dd') end)  \n");
//		sbsql_sj.append(" and yx.riq>=to_date('"+getBeginriq1Date()+"','yyyy-mm-dd') and yx.riq<=to_date('"+getBeginriqDate()+"','yyyy-mm-dd')");
//		sbsql_sj.append("	) yx,(select dc.mingc,dc.fuid as fuid,jz.jizbh,jz.jizurl,dc.xuh as xuh1, jz.xuh as xuh2, dc.id,jz.id as jizid  from diancxxb dc,jizb jz \n");
//		sbsql_sj.append("		 where dc.id=jz.diancxxb_id "+diancCondition+") dc \n");
//		sbsql_sj.append("  where yx.diancxxb_id(+)=dc.id and dc.jizid=yx.jizb_id(+) order by dc.xuh1,dc.xuh2 ) sheb  \n");
		
		if(jib==1){
			dianw1="dc.fgsmc";
			dianw2="dc.mingc";
			havingnot="";
			
		}else if(jib==2){
			String ranlgs = "select id from diancxxb where shangjgsid= "+this.getTreeid();
			
			try{
				ResultSetList rl = con.getResultSetList(ranlgs);
				if(rl.getRows()!=0){//ȼ�Ϲ�˾
					dianw1="dc.rlgsmc";
					dianw2="dc.mingc";
					havingnot="having not grouping("+dianw1+")=1 \n";
				}else{
					dianw1="dc.fgsmc";
					dianw2="dc.mingc";
					havingnot="having not grouping("+dianw1+")=1 \n";
				}
				rl.close();
				
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				con.Close();
			}
		}else{
			dianw1="dc.fgsmc";
			dianw2="dc.mingc";
			havingnot="having not grouping(dc.fgsmc)=1 and (grouping(dc.fgsmc) || grouping(dc.mingc))=0 \n";
		}
		
		sbsql.append("  select decode(grouping("+dianw1+")+grouping("+dianw2+"),2,'�ܼ�',1,"+dianw1+",'&nbsp;&nbsp;&nbsp;&nbsp;'||"+dianw2+") as dianc, \n");
		sbsql.append(" round(sum(r.jizrl),0) as jizurl,round(sum(r.fadl)/10000,2) as fadl,round(sum(r.gongdl)/10000,2) as gongdl \n");
		sbsql.append(" from \n");
		sbsql.append(sbsql_sj.toString());
		sbsql.append(" ,vwdianc dc \n");
		sbsql.append("  where r.diancxxb_id=dc.id  \n");
		sbsql.append(diancCondition);
		sbsql.append("group by rollup ("+dianw1+","+dianw2+")  \n");
		sbsql.append(havingnot).append("\n");
		sbsql.append("order by grouping("+dianw1+") desc,"+dianw1+",grouping("+dianw2+") desc,max(dc.xuh),"+dianw2+" \n");
		
		ResultSet rs = con.getResultSet(sbsql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		Report rt = new Report();
		
	    String ArrHeader[][]=new String[1][4];
		ArrHeader[0]=new String[] {"��λ","װ������<br>(����)","������<br>(��ǧ��ʱ)","������<br>(��ǧ��ʱ)"};

		 int ArrWidth[]=new int[] {150,100,100,100};


			Table bt=new Table(rs,1,0,2);
			rt.setBody(bt);
			bt.setColAlign(2, Table.ALIGN_CENTER);
			
			rt.body.setPageRows(20);
			
			if(rt.body.getRows()>1){
				rt.body.setCellAlign(2, 1, Table.ALIGN_CENTER);
			}
			//
			rt.body.ShowZero=false;
			rt.body.setWidth(ArrWidth);
			rt.body.setHeaderData(ArrHeader);
			rt.body.mergeFixedRow();//�ϲ���
			rt.body.mergeFixedCols();//�Ͳ���
			rt.setTitle( getBiaotmc()+"����������", ArrWidth);
			rt.title.setRowHeight(2, 50);
			rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
			rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
			rt.setDefaultTitle(1, 2, "�Ʊ�λ:" +((Visit) getPage().getVisit()).getDiancqc(),Table.ALIGN_LEFT);
			rt.setDefaultTitle(3, 2, riq,Table.ALIGN_RIGHT);
	//		rt.setDefaultTitle(12, 2, "��λ:(��)" ,Table.ALIGN_RIGHT);
			
			rt.body.setColFormat(3, "0.00");
			rt.body.setColFormat(4, "0.00");
			
			rt.createDefautlFooter(ArrWidth);
			rt.setDefautlFooter(1, 2, "�Ʊ�ʱ��:"+FormatDate(DateUtil.getDate(DateUtil.FormatDate(new Date()))),Table.ALIGN_LEFT);
			rt.setDefautlFooter(3, 1, "���:", Table.ALIGN_LEFT);
			if(((Visit) getPage().getVisit()).getDiancmc().equals("�ӱ�����")){
				
				rt.setDefautlFooter(4, 1, "�Ʊ�:",Table.ALIGN_RIGHT);
				}else{
					
					rt.setDefautlFooter(4, 1, "�Ʊ�:"+((Visit) getPage().getVisit()).getDiancmc

		(),Table.ALIGN_RIGHT);
				}
//			rt.setDefautlFooter(4, 1, "�Ʊ�:", Table.ALIGN_RIGHT);
		// ����ҳ��
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
		return rt.getAllPagesHtml();
	}
	// ��ͬ���ֳ��ֿ�ֿ�ֳ�ͳ�Ʊ���
	private String getZhiltj() {
		JDBCcon con = new JDBCcon();
		StringBuffer sbsql = new StringBuffer("");
		String riq=FormatDate(DateUtil.getDate(getBeginriqDate()));
		int jib=this.getDiancTreeJib();
		String diancCondition=
			"and sheb.id in (select id\n" + 
			" from(\n" + 
			" select id from diancxxb\n" + 
			" start with id="+getTreeid()+"\n" + 
			" connect by (fuid=prior id or shangjgsid=prior id)\n" + 
			" )\n" + 
			" union\n" + 
			" select id\n" + 
			" from diancxxb\n" + 
			" where id="+getTreeid()+") " ; 
		
		StringBuffer rs_sql=new StringBuffer();
		 
		rs_sql.append("(select dcjz.diancxxb_id,dcjz.jizb_id,dcjz.jizbh,nvl(rs.gongdl,0) as gongdl, \n");
		rs_sql.append("       nvl(rs.fadl,0) as fadl,nvl(rs.shangwdl,0) as shangwdl, \n");
		rs_sql.append("       nvl(rs.gongrl,0) as gongrl,nvl(dcjz.jizurl,0) as jizrl from              \n");
		rs_sql.append("(select * from riscsjb rs where rs.riq=to_date('"+getBeginriqDate()+"','yyyy-mm-dd')) rs, \n");
		rs_sql.append("(select dc.id as diancxxb_id,jz.id as jizb_id,jz.jizbh,jz.jizurl,jz.jihdl  \n");
		rs_sql.append("        from diancxxb dc,jizb jz where jz.diancxxb_id=dc.id  ) dcjz \n");
		rs_sql.append(" where rs.diancxxb_id=dcjz.diancxxb_id and rs.jizb_id=dcjz.jizb_id) r, \n");
		
		
//		---------------------------������Ϣ��ѯ�������豸״̬���豸���޿�ʼ���ں��豸���޽������ڵĲ�ѯ------------------------
		String Str="&'||'diancxxb_id='||sheb.id||'&'||'beginriq="+getBeginriqDate()+"'||'&'||'lx='||";
		
		String select=
			"             decode(grouping(sheb.mingc)+grouping(sheb.jizbh),1,getHtmlAllAlert('"+MainGlobal.getHomeContext(this)+"','Shengccxmxbreport','"+Str+"'xj'||'','С��'),getHtmlAllAlert('"+MainGlobal.getHomeContext(this)+"','Shengccxmxbreport','"+Str+"sheb.jizid||'',sheb.jizbh)) as jizbh,\n" + 
			"             sum(sheb.jizurl) as jizrl,sheb.shebzt,kaisrq,jiesrq,\n" + 
//			------------������������������������ת��Ϊ��ǧ��ʱ------------
			"round(sum(r.fadl)/10000,2) as fadl,round(sum(gongdl)/10000,2) as gongdl,round(sum(shangwdl)/10000,2) as shangwdl,sum(gongrl) as gongrl,\n" +
//			"round(decode(sum(sheb.jizurl),0,0,round(sum(r.fadl)/10000,2)/(sum(sheb.jizurl/10) * 24))*100,2) \n"+
//			-----------�õ��շ������Ļ��������󷢵縺����---------------
			"round(decode(sum(r.jizrl),0,0,round(sum(r.fadl)/10000,2)/(sum(r.jizrl/10) * 24))*100,2) as fadfhl \n"+
			"from diancxxb gs,";
			
	    String where= "      where r.diancxxb_id(+)=sheb.id  and r.jizb_id(+)=sheb.jizid  and sheb.fuid=gs.id \n";
	    String order= "      order by grouping(gs.mingc) desc,min(gs.xuh),gs.mingc,grouping(sheb.mingc) desc,min(sheb.xuh1),sheb.mingc,grouping(sheb.jizbh) desc,min(sheb.xuh2),sheb.jizbh";
		if(jib==3){
			sbsql.append("select sheb.mingc as dianc, \n");
			sbsql.append(select);
			sbsql.append(rs_sql.toString()+"\n");
			sbsql.append("(select dc.mingc,dc.id as id,dc.xuh1,dc.xuh2,dc.jizid,dc.fuid,dc.jizbh as jizbh,dc.jizurl as jizurl, \n");
			sbsql.append("		  decode(yx.shebzt,'','����',yx.shebzt) as shebzt, \n");
			sbsql.append("		  decode(shebzt, '����','', to_char(kaisrq, 'yyyy-mm-dd')) as kaisrq, \n");
			sbsql.append("		  decode(shebzt, '����','', to_char(jiesrq, 'yyyy-mm-dd')) as jiesrq \n");
			sbsql.append(" from (select yx.* from jizyxqkb yx,diancxxb dc where yx.diancxxb_id=dc.id and dc.id = " + getTreeid() + " \n");
//			sbsql.append("		and yx.kaisrq=(case when to_date('"+getBeginriqDate()+"','yyyy-mm-dd')>=yx.kaisrq and to_date('"+getBeginriqDate()+"','yyyy-mm-dd')<=yx.jiesrq then yx.kaisrq else to_date('"+getBeginriqDate()+"','yyyy-mm-dd') end) ");
			sbsql.append(" and yx.riq=to_date('"+getBeginriqDate()+"','yyyy-mm-dd') \n");
			sbsql.append("	) yx,(select dc.mingc,dc.fuid as fuid,jz.jizbh,jz.jizurl,dc.xuh as xuh1, jz.xuh as xuh2, dc.id,jz.id as jizid  from diancxxb dc,jizb jz \n");
			sbsql.append("		 where dc.id=jz.diancxxb_id and dc.id = " + getTreeid() + ") dc \n");
			sbsql.append("  where yx.diancxxb_id(+)=dc.id and dc.jizid=yx.jizb_id(+) order by dc.xuh1,dc.xuh2 ) sheb  \n");
			sbsql.append(where);
			sbsql.append(diancCondition);
			sbsql.append("group by rollup(gs.mingc,(sheb.id,sheb.mingc),(sheb.jizid,sheb.jizbh, sheb.shebzt,kaisrq,jiesrq)) \n");
			sbsql.append("having not (grouping(sheb.jizbh) || grouping(jiesrq)) =1 and grouping(sheb.mingc)=0 \n");
			sbsql.append(order);
			
		}else if(jib==2){
			
			String ranlgs = "select id from diancxxb where shangjgsid= "+this.getTreeid();
			
			try{
				ResultSetList rl = con.getResultSetList(ranlgs);
				if(rl.getRows()!=0){//ȼ�Ϲ�˾
					where= "      where r.diancxxb_id(+)=sheb.id  and r.jizb_id(+)=sheb.jizid  and sheb.shangjgsid=gs.id \n";
				    
					sbsql.append("select decode(grouping(sheb.mingc)+grouping(gs.mingc),2,'�ܼ�',1,gs.mingc,'&nbsp;&nbsp;&nbsp;&nbsp;'||sheb.mingc) as dianc,\n");
					sbsql.append(select);
					sbsql.append(rs_sql.toString()+"\n");
					sbsql.append("(select dc.mingc,dc.id as id,dc.xuh1,dc.xuh2,dc.jizid,dc.fuid,dc.shangjgsid,dc.jizbh as jizbh,dc.jizurl as jizurl,\n");
					sbsql.append("		  decode(yx.shebzt,'','����',yx.shebzt) as shebzt, \n");
//					sbsql.append("        (case when to_date('"+getBeginriqDate()+"','yyyy-mm-dd')>=yx.kaisrq and to_date('"+getBeginriqDate()+"','yyyy-mm-dd')<=yx.jiesrq then yx.shebzt else '����' end) as shebzt,");
					sbsql.append("		  decode(shebzt, '����','', to_char(kaisrq, 'yyyy-mm-dd')) as kaisrq, \n");
					sbsql.append("		  decode(shebzt, '����','', to_char(jiesrq, 'yyyy-mm-dd')) as jiesrq \n");
					sbsql.append(" from (select yx.* from jizyxqkb yx,diancxxb dc where  yx.diancxxb_id=dc.id and  (dc.fuid = "+ getTreeid() + " or dc.shangjgsid= "+this.getTreeid()+")  \n");
//					sbsql.append("		and yx.kaisrq=(case when to_date('"+getBeginriqDate()+"','yyyy-mm-dd')>=yx.kaisrq and to_date('"+getBeginriqDate()+"','yyyy-mm-dd')<=yx.jiesrq then yx.kaisrq else to_date('"+getBeginriqDate()+"','yyyy-mm-dd') end) ");
					sbsql.append(" and yx.riq=to_date('"+getBeginriqDate()+"','yyyy-mm-dd') \n");
					sbsql.append("	) yx,(select dc.mingc,dc.fuid as fuid,dc.shangjgsid,jz.jizbh,jz.jizurl,dc.xuh as xuh1, jz.xuh as xuh2, dc.id,jz.id as jizid  from diancxxb dc,jizb jz \n");
					sbsql.append("		 where dc.id=jz.diancxxb_id  and  (dc.fuid = "+ getTreeid() + " or dc.shangjgsid= "+this.getTreeid()+") ) dc \n");
					sbsql.append(" where yx.diancxxb_id(+)=dc.id and dc.jizid=yx.jizb_id(+) order by dc.xuh1,dc.xuh2) sheb \n");
					sbsql.append(where);
					sbsql.append(diancCondition);
					sbsql.append("group by rollup(gs.mingc,(sheb.id,sheb.mingc),(sheb.jizid,sheb.jizbh, sheb.shebzt,kaisrq,jiesrq))   \n");
					sbsql.append("having not (grouping(sheb.jizbh) || grouping(jiesrq)) =1 and grouping(gs.mingc)=0 \n");
					sbsql.append(order);
				}else{
					sbsql.append("select decode(grouping(sheb.mingc)+grouping(gs.mingc),2,'�ܼ�',1,gs.mingc,'&nbsp;&nbsp;&nbsp;&nbsp;'||sheb.mingc) as dianc,\n");
					sbsql.append(select);
					sbsql.append(rs_sql.toString()+"\n");
					sbsql.append("(select dc.mingc,dc.id as id,dc.xuh1,dc.xuh2,dc.jizid,dc.fuid,dc.jizbh as jizbh,dc.jizurl as jizurl,\n");
					sbsql.append("		  decode(yx.shebzt,'','����',yx.shebzt) as shebzt, \n");
//					sbsql.append("        (case when to_date('"+getBeginriqDate()+"','yyyy-mm-dd')>=yx.kaisrq and to_date('"+getBeginriqDate()+"','yyyy-mm-dd')<=yx.jiesrq then yx.shebzt else '����' end) as shebzt,");
					sbsql.append("		  decode(shebzt, '����','', to_char(kaisrq, 'yyyy-mm-dd')) as kaisrq, \n");
					sbsql.append("		  decode(shebzt, '����','', to_char(jiesrq, 'yyyy-mm-dd')) as jiesrq \n");
					sbsql.append(" from (select yx.* from jizyxqkb yx,diancxxb dc where  yx.diancxxb_id=dc.id and  (dc.fuid = "+ getTreeid() + " or dc.shangjgsid= "+this.getTreeid()+")  \n");
//					sbsql.append("		and yx.kaisrq=(case when to_date('"+getBeginriqDate()+"','yyyy-mm-dd')>=yx.kaisrq and to_date('"+getBeginriqDate()+"','yyyy-mm-dd')<=yx.jiesrq then yx.kaisrq else to_date('"+getBeginriqDate()+"','yyyy-mm-dd') end) ");
					sbsql.append(" and yx.riq=to_date('"+getBeginriqDate()+"','yyyy-mm-dd') \n");
					sbsql.append("	) yx,(select dc.mingc,dc.fuid as fuid,jz.jizbh,jz.jizurl,dc.xuh as xuh1, jz.xuh as xuh2, dc.id,jz.id as jizid  from diancxxb dc,jizb jz \n");
					sbsql.append("		 where dc.id=jz.diancxxb_id  and  (dc.fuid = "+ getTreeid() + " or dc.shangjgsid= "+this.getTreeid()+") ) dc \n");
					sbsql.append(" where yx.diancxxb_id(+)=dc.id and dc.jizid=yx.jizb_id(+) order by dc.xuh1,dc.xuh2) sheb \n");
					sbsql.append(where);
					sbsql.append(diancCondition);
					sbsql.append("group by rollup(gs.mingc,(sheb.id,sheb.mingc),(sheb.jizid,sheb.jizbh, sheb.shebzt,kaisrq,jiesrq))   \n");
					sbsql.append("having not (grouping(sheb.jizbh) || grouping(jiesrq)) =1 and grouping(gs.mingc)=0 \n");
					sbsql.append(order);
				}
				rl.close();
				
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				con.Close();
			}
			
		}else{
			sbsql.append("select decode(grouping(sheb.mingc)+grouping(gs.mingc),2,'�ܼ�',1,gs.mingc,'&nbsp;&nbsp;&nbsp;&nbsp;'||sheb.mingc) as dianc,\n\n");
			sbsql.append(select);
			sbsql.append(rs_sql.toString()+"\n");
			sbsql.append("(select dc.mingc,dc.id as id,dc.xuh1,dc.xuh2,dc.jizid,dc.fuid,dc.jizbh as jizbh,dc.jizurl as jizurl,\n");
			sbsql.append("		  decode(yx.shebzt,'','����',yx.shebzt) as shebzt, \n");
			sbsql.append("		  decode(shebzt, '����','', to_char(kaisrq, 'yyyy-mm-dd')) as kaisrq, \n");
			sbsql.append("		  decode(shebzt, '����','', to_char(jiesrq, 'yyyy-mm-dd')) as jiesrq \n");
			sbsql.append(" from (select yx.* from jizyxqkb yx,diancxxb dc where  yx.diancxxb_id=dc.id  and dc.jib=3  \n");
//			sbsql.append("		and yx.kaisrq=(case when to_date('"+getBeginriqDate()+"','yyyy-mm-dd')>=yx.kaisrq and to_date('"+getBeginriqDate()+"','yyyy-mm-dd')<=yx.jiesrq then yx.kaisrq else to_date('"+getBeginriqDate()+"','yyyy-mm-dd') end)");
			sbsql.append(" and yx.riq=to_date('"+getBeginriqDate()+"','yyyy-mm-dd') \n");
			sbsql.append("	) yx,(select dc.mingc,dc.fuid as fuid,jz.jizbh,jz.jizurl,dc.xuh as xuh1, jz.xuh as xuh2, dc.id,jz.id as jizid  from diancxxb dc,jizb jz \n");
			sbsql.append("		 where dc.id=jz.diancxxb_id  and dc.jib=3 ) dc \n");
			sbsql.append(" where yx.diancxxb_id(+)=dc.id and dc.jizid=yx.jizb_id(+) order by dc.xuh1,dc.xuh2) sheb \n");
			sbsql.append(where);
			sbsql.append(diancCondition);
			sbsql.append("group by rollup(gs.mingc,(sheb.id,sheb.mingc),(sheb.jizid,sheb.jizbh, sheb.shebzt,kaisrq,jiesrq))  \n");
			sbsql.append("having not (grouping(sheb.jizbh) || grouping(jiesrq)) =1 \n");
			sbsql.append(order);
		}
		
		ResultSet rs = con.getResultSet(sbsql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		Report rt = new Report();
		
		 String ArrHeader[][]=new String[1][11];
		 ArrHeader[0]=new String[] {"��λ","����","װ������<br>(����)","����<br>״̬","���޿�ʼ<br>ʱ��","���޽���<br>ʱ��","������<br>(��ǧ��ʱ)","������<br>(��ǧ��ʱ)","��������<br>(��ǧ��ʱ)","������<br>(����)","���縺����<br>(%)"};

		 int ArrWidth[]=new int[] {120,60,70,60,80,80,80,80,80,65,80};


			Table bt=new Table(rs,1,0,2);
			rt.setBody(bt);
			int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString1());//ȡ�ñ���ֽ������
			rt.getArrWidth(ArrWidth, aw);//��ӱ����A4����
			bt.setColAlign(2, Table.ALIGN_CENTER);
			
			rt.body.setPageRows(rt.PAPER_ROWS);
			
//			���ӳ��ȵ�����
			rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
			if(rt.body.getRows()>1){
				rt.body.setCellAlign(2, 1, Table.ALIGN_CENTER);
			}
			
			rt.body.setColFormat(7, "0.00");
			rt.body.setColFormat(8, "0.00");
			rt.body.setColFormat(9, "0.00");
			rt.body.setColFormat(10, "0.00");
			rt.body.setColFormat(11, "0.00");
			
			
			//
			rt.body.ShowZero=false;
			rt.body.setWidth(ArrWidth);
			rt.body.setHeaderData(ArrHeader);
			rt.body.mergeFixedRow();//�ϲ���
			rt.body.mergeFixedCols();//�Ͳ���
			rt.setTitle( getBiaotmc()+"����������", ArrWidth);
			rt.title.setRowHeight(2, 50);
			rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
			rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
			rt.setDefaultTitle(1, 3, "�Ʊ�λ:" +((Visit) getPage().getVisit()).getDiancqc(),Table.ALIGN_LEFT);
			rt.setDefaultTitle(5, 3, riq,Table.ALIGN_CENTER);
			rt.setDefaultTitle(10, 2, "��λ:����������" ,Table.ALIGN_RIGHT);
			
			rt.createDefautlFooter(ArrWidth);
			rt.setDefautlFooter(1, 3, "�Ʊ�ʱ��:"+FormatDate(DateUtil.getDate(DateUtil.FormatDate(new Date()))),Table.ALIGN_LEFT);
			rt.setDefautlFooter(6, 2, "���:", Table.ALIGN_LEFT);
			if(((Visit) getPage().getVisit()).getDiancmc().equals("�ӱ�����")){
				
				rt.setDefautlFooter(10, 2, "�Ʊ�:",Table.ALIGN_RIGHT);
				}else{
					
					rt.setDefautlFooter(10, 2, "�Ʊ�:"+((Visit) getPage().getVisit()).getDiancmc

		(),Table.ALIGN_RIGHT);
				}
//			rt.setDefautlFooter(10, 2, "�Ʊ�:", Table.ALIGN_RIGHT);
			
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
				Calendar stra=Calendar.getInstance();
//				stra.set(DateUtil.getYear(new Date()), 0, 1);
				stra.setTime(new Date());
				stra.add(Calendar.DATE,-1);
				((Visit) getPage().getVisit()).setString4(DateUtil.FormatDate(stra.getTime()));
		}
		return ((Visit) getPage().getVisit()).getString4();
	}
	public void setBeginriqDate(String value){
		((Visit) getPage().getVisit()).setString4(value);
	}
	
	public String getBeginriq1Date(){
		if(((Visit) getPage().getVisit()).getString5()==null||((Visit) getPage().getVisit()).getString5()==""){
				Calendar stra=Calendar.getInstance();
//				stra.set(DateUtil.getYear(new Date()), 0, 1);
				stra.setTime(new Date());
				stra.add(Calendar.DATE,-1);
				((Visit) getPage().getVisit()).setString5(DateUtil.FormatDate(DateUtil.getFirstDayOfMonth(stra.getTime())));
		}
		return ((Visit) getPage().getVisit()).getString5();
	}
	public void setBeginriq1Date(String value){
		((Visit) getPage().getVisit()).setString5(value);
	}

	private void getToolbars(){
		Toolbar tb1 = new Toolbar("tbdiv");
		tb1.addText(new ToolbarText("��ѯ����:"));
		
		if (getLeixSelectValue().getValue().equals("������Ϣ���")){
			DateField df1 = new DateField();
			df1.setValue(this.getBeginriq1Date());
			df1.Binding("qiandrq2","");// ��htmlҳ�е�id��,���Զ�ˢ��
			df1.setWidth(80);
			tb1.addField(df1);
			tb1.addText(new ToolbarText("-"));
			tb1.addText(new ToolbarText("��"));
			tb1.addText(new ToolbarText("-"));
		}
		
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
			
			this.setTreeid(null);
			this.getTreeid();
			visit.setString4(null);
			visit.setString5(null);
			
			//begin��������г�ʼ������
			visit.setString1(null);

				String pagewith = cycle.getRequestContext().getParameter("pw");// �ж��Ƿ�����������
				if (pagewith != null) {

					visit.setString1(pagewith);
				}
			//	visit.setString1(null);���洫�ݵķ�Ĭ��ֽ�ŵ���ʽ
				
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
//		�õ��糧��������ĵ糧���ƻ��߷ֹ�˾,���ŵ�����
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
//		 �ֹ�˾������
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
	
//		�õ�ϵͳ��Ϣ�������õı������ĵ�λ����
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
		
	public Toolbar getToolbar() {
		return ((Visit)this.getPage().getVisit()).getToolbar();
	}
	public void setToolbar(Toolbar tb1) {
		((Visit)this.getPage().getVisit()).setToolbar(tb1);
	}
	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}
	
//	 ����
	public IDropDownBean getLeixSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean4() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean4((IDropDownBean) getLeixSelectModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean4();
	}

	public void setLeixSelectValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean4(Value);

	}

	public void setLeixSelectModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel4(value);
	}

	public IPropertySelectionModel getLeixSelectModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
			getLeixSelectModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}

	public void getLeixSelectModels() {
		List list = new ArrayList();
		list.add(new IDropDownBean(1, "������Ϣ���"));
		list.add(new IDropDownBean(2, "������Ϣ���"));
		((Visit) getPage().getVisit())
				.setProSelectionModel4(new IDropDownModel(list));
		return;
	}
}