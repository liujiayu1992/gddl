package com.zhiren.jt.zdt.xiasmxq;

import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

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
import com.zhiren.common.ext.form.TextField;

import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.ChessboardTable;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import com.zhiren.common.DateUtil;

import org.apache.tapestry.contrib.palette.SortMode;


/**
 * @author ly
 * @2009-08-21
 * �ۿڷ�����������̱�
 *
 */
public class GangkfyqkReport  extends BasePage implements PageValidateListener{

	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		String DiancTreeJib = this.getTreeid();
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
	
	private StringBuffer strgrouping = new StringBuffer();
	private StringBuffer strwhere = new StringBuffer();
	private StringBuffer strgroupby = new StringBuffer();
	private StringBuffer strsbgroupby = new StringBuffer();//�õ����ݵķ��� cube
	private StringBuffer strhaving = new StringBuffer();
	private StringBuffer strorderby = new StringBuffer();
	private String Rowtitle ="";
	
	public void getTiaoj(){
		strgrouping.setLength(0);
		strwhere.setLength(0);
		strgroupby.setLength(0);
		strsbgroupby.setLength(0);
		strhaving.setLength(0);
		strorderby.setLength(0);
		JDBCcon cn = new JDBCcon();
		int jib=this.getDiancTreeJib();
		
		//***********************************************************************************//
		if (getBaoblxValue().getValue().equals("�ֳ�")){
			Rowtitle="�糧";
			if(jib==1){//ѡ����ʱˢ�³����еĵ糧
				strgrouping.append("decode(grouping(dc.fgsmc)+grouping(dc.mingc),2,'�ϼ�',1,dc.fgsmc,dc.mingc) as �糧\n");
				strwhere.append("");
				strgroupby.append(" group by rollup(dc.fgsmc,dc.mingc)\n");
				strsbgroupby.append(" group by cube(dc.fgsmc,dc.mingc,c.mingc)\n");
				strhaving.append(" having not (grouping(dc.fgsmc) -grouping(dc.mingc)) =1\n");
				strorderby.append(" order by grouping(dc.fgsmc) desc,dc.fgsmc desc,grouping(dc.mingc) desc,dc.mingc desc\n");
			}else if (jib==2){//ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
				String ranlgs = " select id from diancxxb where shangjgsid= "+this.getTreeid();
				
				try{
					ResultSet rl = cn.getResultSet(ranlgs);
					if(rl.next()){//ȼ�Ϲ�˾
						strgrouping.append("decode(grouping(dc.rlgsmc)+grouping(dc.fgsmc)+grouping(dc.mingc),3,'�ϼ�',2,dc.rlgsmc,1,dc.fgsmc,dc.mingc) as �糧\n");
						strwhere.append(" and (dc.fgsid="+this.getTreeid()+" or dc.rlgsid= "+this.getTreeid()+") \n");
						strgroupby.append(" group by rollup(dc.rlgsmc,dc.fgsmc,dc.mingc)\n");
						strsbgroupby.append(" group by cube(dc.rlgsmc,dc.fgsmc,dc.mingc,c.mingc)\n");
						strhaving.append(" having not grouping(dc.rlgsmc)=1\n");
						strorderby.append(" order by grouping(dc.rlgsmc) desc,dc.rlgsmc desc,grouping(dc.fgsmc) desc,dc.fgsmc desc,grouping(dc.mingc) desc,dc.mingc desc\n");
					}else{//�ֹ�˾
						strgrouping.append("decode(grouping(dc.fgsmc)+grouping(dc.mingc),2,'�ϼ�',1,dc.fgsmc,dc.mingc) as �糧\n");
						strwhere.append(" and (dc.fgsid="+this.getTreeid()+" or dc.rlgsid= "+this.getTreeid()+") \n");
						strgroupby.append(" group by rollup(dc.fgsmc,dc.mingc)\n");
						strsbgroupby.append(" group by cube(dc.fgsmc,dc.mingc,c.mingc)\n");
						strhaving.append("");
						strorderby.append(" order by grouping(dc.fgsmc) desc,dc.fgsmc desc,grouping(dc.mingc) desc,dc.mingc desc\n");
					}
					rl.close();
					
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					cn.Close();
				}
			}else if (jib==3){//ѡ�糧ֻˢ�³��õ糧
				strgrouping.append("decode(grouping(dc.mingc),1,'�ϼ�',dc.mingc) as �糧\n");
				strwhere.append(" and dc.id=").append(this.getTreeid());
				strgroupby.append(" group by rollup(dc.mingc)\n");
				strsbgroupby.append(" group by cube(dc.mingc,c.mingc)\n");
				strhaving.append(" having not grouping(dc.mingc)=1\n");
				strorderby.append(" order by grouping(dc.mingc) desc,dc.mingc desc\n");
			}
//			***********************************************************************************//
		}else if (getBaoblxValue().getValue().equals("�ֿ�")){
			Rowtitle="���";
			strgrouping.append("decode(grouping(g.id),1,'�ϼ�',max(g.mingc)) as ���\n");
			strgroupby.append(" group by rollup(g.id)\n");
			strsbgroupby.append(" group by cube(g.id,c.mingc)\n");
			strorderby.append(" order by grouping(g.id) desc,g.id desc\n");
			 
			if(jib==1){//ѡ����ʱˢ�³����еĵ糧
				strwhere.append("");
			}else if (jib==2){//ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
				strwhere.append(" and (dc.fgsid="+this.getTreeid()+" or dc.rlgsid= "+this.getTreeid()+") \n");
			}else if (jib==3){//ѡ�糧ֻˢ�³��õ糧
				strwhere.append(" and dc.id=").append(this.getTreeid());
			}
			
		}else if (getBaoblxValue().getValue().equals("�ֳ��ֿ�")) {
			Rowtitle="�糧,���";
			if(jib==1){//ѡ����ʱˢ�³����еĵ糧
				strgrouping.append("decode(grouping(dc.fgsmc)+grouping(dc.mingc),2,'�ϼ�',1,dc.fgsmc,dc.mingc) as �糧,\n");
				strgrouping.append("decode(grouping(dc.mingc)+grouping(g.mingc),2,'',1,'С��',g.mingc) as ���\n");
				strwhere.append("");
				strgroupby.append(" group by rollup(dc.fgsmc,dc.mingc,g.mingc)\n");
				strsbgroupby.append(" group by cube(dc.fgsmc,dc.mingc,g.mingc,c.mingc)\n");
				strhaving.append("");
				strorderby.append(" order by grouping(dc.fgsmc) desc,dc.fgsmc desc,grouping(dc.mingc) desc,dc.mingc desc,grouping(g.mingc) desc,g.mingc\n");
			}else if (jib==2){//ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
				String ranlgs = "select id from diancxxb where shangjgsid= "+this.getTreeid();
				
				try{
					ResultSet rl = cn.getResultSet(ranlgs);
					if(rl.next()){//ȼ�Ϲ�˾
						strgrouping.append("decode(grouping(dc.rlgsmc)+grouping(dc.fgsmc)+grouping(dc.mingc),3,'�ϼ�',2,dc.rlgsmc,1,dc.fgsmc,dc.mingc) as �糧,\n");
						strgrouping.append("decode(grouping(dc.mingc)+grouping(g.mingc),2,'',1,'С��',g.mingc) as ���\n");
						strwhere.append(" and (dc.fgsid="+this.getTreeid()+" or dc.rlgsid= "+this.getTreeid()+") \n");
						strgroupby.append(" group by rollup(dc.rlgsmc,dc.fgsmc,dc.mingc,g.mingc)\n");
						strsbgroupby.append(" group by cube(dc.rlgsmc,dc.fgsmc,dc.mingc,g.mingc,c.mingc)\n");
						strhaving.append(" having not grouping(dc.rlgsmc)=1\n");
						strorderby.append(" order by grouping(dc.rlgsmc) desc,dc.rlgsmc desc,grouping(dc.fgsmc) desc,dc.fgsmc desc,grouping(dc.mingc) desc,dc.mingc desc,grouping(g.mingc) desc,g.mingc\n");
					}else{//�ֹ�˾
						strgrouping.append("decode(grouping(dc.fgsmc)+grouping(dc.mingc),2,'�ϼ�',1,dc.fgsmc,dc.mingc) as �糧,\n");
						strgrouping.append("decode(grouping(dc.mingc)+grouping(g.mingc),2,'',1,'С��',g.mingc) as ���\n");
						strwhere.append(" and (dc.fgsid="+this.getTreeid()+" or dc.rlgsid= "+this.getTreeid()+") \n");
						strgroupby.append(" group by rollup(dc.fgsmc,dc.mingc,g.mingc)\n");
						strsbgroupby.append(" group by cube(dc.fgsmc,dc.mingc,g.mingc,c.mingc)\n");
						strhaving.append("");
						strorderby.append(" order by grouping(dc.fgsmc) desc,dc.fgsmc desc,grouping(dc.mingc) desc,dc.mingc desc,grouping(g.mingc) desc,g.mingc\n");
					}
					rl.close();
					
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					cn.Close();
				}
			}else if (jib==3){//ѡ�糧ֻˢ�³��õ糧
				strgrouping.append("decode(grouping(dc.mingc),1,'�ϼ�',dc.mingc) as �糧,\n");
				strgrouping.append("decode(grouping(dc.mingc)+grouping(g.mingc),2,'',1,'С��',g.mingc) as ���\n");
				strwhere.append(" and dc.id=").append(this.getTreeid());
				strgroupby.append(" group by rollup(dc.mingc,g.mingc)\n");
				strsbgroupby.append(" group by cube(dc.mingc,g.mingc,c.mingc)\n");
				strhaving.append(" having not grouping(dc.mingc)=1\n");
				strorderby.append(" order by grouping(dc.mingc) desc,dc.mingc desc,grouping(g.mingc) desc,g.mingc\n");
			}
//			***********************************************************************************//			
		}else if(getBaoblxValue().getValue().equals("�ֿ�ֳ�")){
			Rowtitle="���,�糧";
			if(jib==1){//ѡ����ʱˢ�³����еĵ糧
				strgrouping.append("decode(grouping(g.mingc),1,'�ϼ�',g.mingc) as ���,\n");
				strgrouping.append("decode(grouping(g.mingc)+grouping(dc.fgsmc)+grouping(dc.mingc),3,'',2,'',1,dc.fgsmc||'С��',dc.mingc) as �糧\n");
				strwhere.append("");
				strgroupby.append(" group by rollup(g.mingc,dc.fgsmc,dc.mingc)\n");
				strsbgroupby.append(" group by cube(g.mingc,dc.fgsmc,dc.mingc,c.mingc)\n");
				strhaving.append("");
				strorderby.append(" order by grouping(g.mingc) desc,g.mingc,grouping(dc.fgsmc) desc,dc.fgsmc desc,grouping(dc.mingc) desc,dc.mingc desc\n");
			}else if (jib==2){//ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
				String ranlgs = " select id from diancxxb where shangjgsid= "+this.getTreeid();
				try{
					ResultSet rl = cn.getResultSet(ranlgs);
					if(rl.next()){//ȼ�Ϲ�˾
						strgrouping.append("decode(grouping(g.mingc),1,'�ϼ�',g.mingc) as ���,\n");
						strgrouping.append("decode(grouping(g.mingc)+grouping(dc.rlgsmc)+grouping(dc.mingc),3,'',2,'',1,dc.rlgsmc||'С��',dc.mingc) as �糧\n");
						strwhere.append(" and (dc.fgsid="+this.getTreeid()+" or dc.rlgsid= "+this.getTreeid()+") \n");
						strgroupby.append(" group by rollup(g.mingc,dc.rlgsmc,dc.fgsmc,dc.mingc)\n");
						strsbgroupby.append(" group by cube(g.mingc,dc.rlgsmc,dc.fgsmc,dc.mingc,c.mingc)\n");
						strhaving.append(" having not grouping(dc.rlgsmc)=1\n");
						strorderby.append(" order by grouping(g.mingc) desc,g.mingc,grouping(dc.rlgsmc) desc,dc.rlgsmc desc,grouping(dc.mingc) desc,dc.mingc desc\n");
					}else{//�ֹ�˾
						strgrouping.append("decode(grouping(g.mingc),1,'�ϼ�',g.mingc) as ���,\n");
						strgrouping.append("decode(grouping(g.mingc)+grouping(dc.fgsmc)+grouping(dc.mingc),3,'',2,'',1,dc.fgsmc||'С��',dc.mingc) as �糧\n");
						
						strwhere.append(" and (dc.fgsid="+this.getTreeid()+" or dc.rlgsid= "+this.getTreeid()+") \n");
						strgroupby.append(" group by rollup(g.mingc,dc.fgsmc,dc.mingc)\n");
						strsbgroupby.append(" group by cube(g.mingc,dc.fgsmc,dc.mingc,c.mingc)\n");
						strhaving.append("");
						strorderby.append(" order by grouping(g.mingc) desc,g.mingc,grouping(dc.fgsmc) desc,dc.fgsmc desc,grouping(dc.mingc) desc,dc.mingc desc\n");
					}
					rl.close();
					
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					cn.Close();
				}
			}else if (jib==3){//ѡ�糧ֻˢ�³��õ糧
				strgrouping.append("decode(grouping(g.mingc),1,'�ϼ�',g.mingc) as ���,\n");
				strgrouping.append("decode(grouping(dc.mingc)+grouping(g.mingc),2,'',1,'С��',dc.mingc) as �糧\n");
				strwhere.append(" and dc.id=").append(this.getTreeid());
				strgroupby.append(" group by rollup(g.mingc,dc.mingc)\n");
				strsbgroupby.append(" group by cube(g.mingc,dc.mingc,c.mingc)\n");
				strhaving.append(" having not grouping(g.mingc)=1\n");
				strorderby.append(" order by grouping(g.mingc) desc,g.mingc,grouping(dc.mingc) desc,dc.mingc desc\n");
			}
		}
	}
	
	public String getRow(){
		StringBuffer row = new StringBuffer();
		row.append("select ");
		row.append(strgrouping);
		row.append(
		",round_new(sum(x.hetl) / 10000, 2) as ��ͬ��,\n" +
		"     round_new((sum(x.jihn) + sum(x.jihw)) / 10000, 2) as �ϼ�,\n" + 
		"     round_new(sum(x.jihn) / 10000, 2) as �ƻ���,\n" + 
		"     round_new(sum(x.jihw) / 10000, 2) as �ƻ���,\n" + 
		"     decode(sum(nvl(x.hetl, 0)),\n" + 
		"            0,\n" + 
		"            0,\n" + 
		"            round_new(sum(x.jihn) / sum(x.hetl), 4) * 100) as ��ͬ������,\n" + 
		"     --\n" + 
		"     round_new(sum(decode(x.chezxxb_id,1005566921,x.hetl,0)) / 10000, 2) as ��ͬ��,\n" + 
		"     round_new(sum(decode(x.chezxxb_id,1005566921,x.jihn+jihw,0)) / 10000, 2) as �ϼ�,\n" + 
		"     round_new(sum(decode(x.chezxxb_id,1005566921,x.jihn,0)) / 10000, 2) as �ƻ���,\n" + 
		"     round_new(sum(decode(x.chezxxb_id,1005566921,x.jihw,0)) / 10000, 2) as �ƻ���,\n" + 
		"     decode(sum(decode(x.chezxxb_id,1005566921,x.jihn,0)),\n" + 
		"            0,\n" + 
		"            0,\n" + 
		"            round_new(sum(decode(x.chezxxb_id,1005566921,x.jihn,0)) / sum(decode(x.chezxxb_id,1005566921,x.hetl,0)), 4) * 100) as ��ͬ������,\n" + 
		"     --\n" + 
		"     round_new(sum(decode(x.chezxxb_id,1005566921,0,x.hetl)) / 10000, 2) as ��ͬ��,\n" + 
		"     round_new(sum(decode(x.chezxxb_id,1005566921,0,x.jihn+jihw)) / 10000, 2) as �ϼ�,\n" + 
		"     round_new(sum(decode(x.chezxxb_id,1005566921,0,x.jihn)) / 10000, 2) as �ƻ���,\n" + 
		"     round_new(sum(decode(x.chezxxb_id,1005566921,0,x.jihw)) / 10000, 2) as �ƻ���,\n" + 
		"     decode(sum(decode(x.chezxxb_id,1005566921,0,x.jihn)),\n" + 
		"            0,\n" + 
		"            0,\n" + 
		"            round_new(sum(decode(x.chezxxb_id,1005566921,0,x.jihn)) / sum(decode(x.chezxxb_id,1005566921,0,x.hetl)), 4) * 100) as ��ͬ������");
		row.append(" from xiasmfyqkb x,vwdianc dc,gongysb g ");
		row.append(" where x.diancxxb_id = dc.id and x.gongysb_id = g.id ");
		row.append(strwhere).append("\n");
		row.append(strgroupby).append("\n");
		row.append(strhaving).append("\n");
		row.append(strorderby).append("\n");
		return row.toString();
	}
	
	public String getCol(long intyear,long intmonth){
        String col =
        	"select distinct decode(grouping(c.mingc),1,'�ϼ�',c.mingc) as �ۿ�,'' as ʵ��ִ��\n" +
        	"from xiasmfyqkb x,chezxxb c,vwdianc dc\n" + 
        	"where x.chezxxb_id = c.id\n" + 
        	"      and c.leib = '�ۿ�'\n" + 
        	"		 and x.riq>=to_date('"+intyear+"-"+intmonth+"-01','yyyy-MM-dd') " +
            "		 and x.riq<add_months(to_date('"+intyear+"-"+intmonth+"-01','yyyy-mm-dd'),1) \n" +
        	"\n" + 
        	"group by rollup (c.mingc)\n" + 
        	"order by decode(�ۿ�,'�ϼ�',0,1),�ۿ�";
		return col;
	}
	
	public String getSb(){
		StringBuffer sb = new StringBuffer();
		sb.append("select ");
		sb.append(strgrouping);
		sb.append(",decode(grouping(c.mingc),0,c.mingc,'�ϼ�') as �ۿ�,'' as ʵ��ִ��,round_new(sum(x.hetl)/10000,2) as ��ͬ��,round_new((sum(x.jihn)+sum(x.jihw))/10000,2) as �ϼ�,round_new(sum(x.jihn)/10000,2) as �ƻ���,round_new(sum(x.jihw)/10000,2) as �ƻ���,\n");
		sb.append(" decode(sum(nvl(x.hetl,0)),0,0,round_new(sum(x.jihn)/sum(x.hetl),4)*100) as ��ͬ������\n");
		sb.append(" from xiasmfyqkb x,vwdianc dc,gongysb g,chezxxb c");
		sb.append(" where x.diancxxb_id = dc.id and x.gongysb_id = g.id and x.chezxxb_id = c.id");
		sb.append(strwhere);
		sb.append(strsbgroupby);
		sb.append(strhaving);
		//sb.append(strorderby);
		return sb.toString();
	}

	public String getPrintTable() {
		JDBCcon cn = new JDBCcon();
		Report rt = new Report();
		
		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}
		long intmonth;
		if (getYuefValue() == null) {
			intmonth = DateUtil.getMonth(new Date());
		} else {
			intmonth = getYuefValue().getId();
		}
		
		getTiaoj();//ִ������
		StringBuffer strRow=new StringBuffer();
		
		System.out.print(getRow());
		//�õ��б�ͷ��������
		strRow.append(getRow());
		
		ResultSet rs = cn.getResultSet(strRow,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		String[][] ArrHeader =null;
		int ArrWidth[] =null;
		int col=1;
		if (getBaoblxValue().getValue().equals("�ֳ�")){
			
			ArrHeader=new String[3][16];
			ArrHeader[0]=new String[] {"�ջ���λ","�ϼ�","�ϼ�","�ϼ�","�ϼ�","�ϼ�","�ػʵ���","�ػʵ���","�ػʵ���","�ػʵ���","�ػʵ���","����","����","����","����","����"};
			ArrHeader[1]=new String[] {"�ջ���λ","��ͬ��","ʵ��ִ��","ʵ��ִ��","ʵ��ִ��","��ͬ������%","��ͬ��","ʵ��ִ��","ʵ��ִ��","ʵ��ִ��","��ͬ������%","��ͬ��","ʵ��ִ��","ʵ��ִ��","ʵ��ִ��","��ͬ������%"};
			ArrHeader[2]=new String[] {"�ջ���λ","��ͬ��","�ϼ�","�ƻ���","�ƻ���","��ͬ������%","��ͬ��","�ϼ�","�ƻ���","�ƻ���","��ͬ������%","��ͬ��","�ϼ�","�ƻ���","�ƻ���","��ͬ������%"};
			ArrWidth=new int[] {100,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45};
		}else if (getBaoblxValue().getValue().equals("�ֿ�")){
			ArrHeader=new String[3][16];
			ArrHeader[0]=new String[] {"���","�ϼ�","�ϼ�","�ϼ�","�ϼ�","�ϼ�","�ػʵ���","�ػʵ���","�ػʵ���","�ػʵ���","�ػʵ���","����","����","����","����","����"};
			ArrHeader[1]=new String[] {"���","��ͬ��","ʵ��ִ��","ʵ��ִ��","ʵ��ִ��","��ͬ������%","��ͬ��","ʵ��ִ��","ʵ��ִ��","ʵ��ִ��","��ͬ������%","��ͬ��","ʵ��ִ��","ʵ��ִ��","ʵ��ִ��","��ͬ������%"};
			ArrHeader[2]=new String[] {"���","��ͬ��","�ϼ�","�ƻ���","�ƻ���","��ͬ������%","��ͬ��","�ϼ�","�ƻ���","�ƻ���","��ͬ������%","��ͬ��","�ϼ�","�ƻ���","�ƻ���","��ͬ������%"};
			ArrWidth=new int[] {100,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45};
		}else if (getBaoblxValue().getValue().equals("�ֳ��ֿ�")){
			col=2;
			ArrHeader=new String[3][17];
			ArrHeader[0]=new String[] {"�ջ���λ","���","�ϼ�","�ϼ�","�ϼ�","�ϼ�","�ϼ�","�ػʵ���","�ػʵ���","�ػʵ���","�ػʵ���","�ػʵ���","����","����","����","����","����"};
			ArrHeader[1]=new String[] {"�ջ���λ","���","��ͬ��","ʵ��ִ��","ʵ��ִ��","ʵ��ִ��","��ͬ������%","��ͬ��","ʵ��ִ��","ʵ��ִ��","ʵ��ִ��","��ͬ������%","��ͬ��","ʵ��ִ��","ʵ��ִ��","ʵ��ִ��","��ͬ������%"};
			ArrHeader[2]=new String[] {"�ջ���λ","���","��ͬ��","�ϼ�","�ƻ���","�ƻ���","��ͬ������%","��ͬ��","�ϼ�","�ƻ���","�ƻ���","��ͬ������%","��ͬ��","�ϼ�","�ƻ���","�ƻ���","��ͬ������%"};
			ArrWidth=new int[] {100,120,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45};
		}else if (getBaoblxValue().getValue().equals("�ֿ�ֳ�")){
			col=2;
			ArrHeader=new String[3][17];
			ArrHeader[0]=new String[] {"���","�ջ���λ","�ϼ�","�ϼ�","�ϼ�","�ϼ�","�ϼ�","�ػʵ���","�ػʵ���","�ػʵ���","�ػʵ���","�ػʵ���","����","����","����","����","����"};
			ArrHeader[1]=new String[] {"���","�ջ���λ","��ͬ��","ʵ��ִ��","ʵ��ִ��","ʵ��ִ��","��ͬ������%","��ͬ��","ʵ��ִ��","ʵ��ִ��","ʵ��ִ��","��ͬ������%","��ͬ��","ʵ��ִ��","ʵ��ִ��","ʵ��ִ��","��ͬ������%"};
			ArrHeader[2]=new String[] {"���","�ջ���λ","��ͬ��","�ϼ�","�ƻ���","�ƻ���","��ͬ������%","��ͬ��","�ϼ�","�ƻ���","�ƻ���","��ͬ������%","��ͬ��","�ϼ�","�ƻ���","�ƻ���","��ͬ������%"};
			ArrWidth=new int[] {120,100,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45};
		}


		Table bt=new Table(rs,3,0,col);
		rt.setBody(bt);
		
		rt.body.ShowZero=false;
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.mergeFixedRow();//�ϲ���
		rt.body.mergeFixedCols();//�Ͳ���
		rt.setTitle(intyear+"��"+intmonth+"�·ݸ��ۿڵ�ú�ƻ��������ͳ�Ʊ�", ArrWidth);
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.setDefaultTitle(1, 2, "�Ʊ�λ:" +getTreeDiancmc(this.getTreeid()),Table.ALIGN_LEFT);
//			rt.setDefaultTitle(12, 2, "��λ:(��)" ,Table.ALIGN_RIGHT);
		rt.setDefaultTitle(7, 5,intyear+"��"+intmonth+"��",Table.ALIGN_CENTER);
		rt.body.setPageRows(24);
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 3, "��ӡ����:"+FormatDate(DateUtil.getDate(DateUtil.FormatDate(new Date()))),Table.ALIGN_LEFT);
		rt.setDefautlFooter(9, 2, "����ˣ�", Table.ALIGN_LEFT);
		rt.setDefautlFooter(15, 2, "��ˣ�", Table.ALIGN_LEFT);
		// ����ҳ��
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}
	
	private String FormatDate(Date _date) {
		if (_date == null) {
			return "";
		}
		return DateUtil.Formatdate("yyyy��MM��dd��", _date);
	}

	public String getLeix(){
		Visit visit = (Visit) getPage().getVisit();
		if(!visit.getString9().equals("")) {
			return visit.getString9();
        } 
		return ""; 
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
	
	boolean riqchange = false;

	private String riq;

	public String getBeginriqDateSelect() {
		if (riq == null || riq.equals("")) {
			riq = DateUtil.FormatDate(new Date());
		}
		return riq;
	}

	public void setBeginriqDateSelect(String riq) {

		if (this.riq != null && !this.riq.equals(riq)) {
			this.riq = riq;
			riqchange = true;
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
		}
	}


//******************ҳ���ʼ����********************//
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
//			_BeginriqValue = DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay);
			this.setNianfValue(null);
			this.getNianfModels();
			this.setYuefValue(null);
			this.getYuefModels();
			this.setTreeid(null);
		}
		getToolBars();
		
	}

//	******************************************************************************
	//���
	private static IPropertySelectionModel _NianfModel;
	
	public IPropertySelectionModel getNianfModel() {
		if (_NianfModel == null) {
			getNianfModels();
		}
		return _NianfModel;
	}

	private IDropDownBean _NianfValue;

	public IDropDownBean getNianfValue() {
		if (_NianfValue == null||_NianfValue.equals("")) {
			for (int i = 0; i < _NianfModel.getOptionCount(); i++) {
				Object obj = _NianfModel.getOption(i);
				if (DateUtil.getYear(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					_NianfValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _NianfValue;
	}

	public boolean nianfchanged;

	public void setNianfValue(IDropDownBean Value) {
		if (_NianfValue != Value) {
			nianfchanged = true;
		}
		_NianfValue = Value;
	}

	public IPropertySelectionModel getNianfModels() {
		List listNianf = new ArrayList();
		int i;
		for (i = 2004; i <= DateUtil.getYear(new Date()) + 1; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_NianfModel = new IDropDownModel(listNianf);
		return _NianfModel;
	}

	public void setNianfModel(IPropertySelectionModel _value) {
		_NianfModel = _value;
	}
	
//	�·�
	public boolean Changeyuef = false;

	private static IPropertySelectionModel _YuefModel;

	public IPropertySelectionModel getYuefModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel5() == null) {
			getYuefModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel5();
	}

	private IDropDownBean _YuefValue;

	public IDropDownBean getYuefValue() {
		if (((Visit)getPage().getVisit()).getDropDownBean5() == null) {
			for (int i = 0; i < getYuefModel().getOptionCount(); i++) {
				Object obj = getYuefModel().getOption(i);
				if (DateUtil.getMonth(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					((Visit)getPage().getVisit()).setDropDownBean5((IDropDownBean) obj );
					break;
				}
			}
		}
		return ((Visit)getPage().getVisit()).getDropDownBean5();
	}

	public void setYuefValue(IDropDownBean Value) {
		long id = -2;
		if (((Visit)getPage().getVisit()).getDropDownBean5()!= null) {
			id = getYuefValue().getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				Changeyuef = true;
			} else {
				Changeyuef = false;
			}
		}
		((Visit)getPage().getVisit()).setDropDownBean5(Value);
		
	}

	public IPropertySelectionModel getYuefModels() {
		List listYuef = new ArrayList();
		// listYuef.add(new IDropDownBean(-1,"��ѡ��"));
		for (int i = 1; i < 13; i++) {
			listYuef.add(new IDropDownBean(i, String.valueOf(i)));
		}
		((Visit)getPage().getVisit()).setProSelectionModel5(new IDropDownModel(listYuef));
		return ((Visit)getPage().getVisit()).getProSelectionModel5();
	}

	public void setYuefModel(IPropertySelectionModel _value) {
		((Visit)getPage().getVisit()).setProSelectionModel5(_value);
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

	
	public void getToolBars() {
		Toolbar tb1 = new Toolbar("tbdiv");
		
		tb1.addText(new ToolbarText("ͳ�ƿھ�:"));
		ComboBox cb = new ComboBox();
		cb.setTransform("BaoblxDropDown");
		cb.setId("Tongjkj");
		cb.setWidth(120);
		tb1.addField(cb);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("���:"));
		ComboBox cb1 = new ComboBox();
		cb1.setTransform("NIANF");
		cb1.setWidth(60);
		tb1.addField(cb1);
		
		tb1.addText(new ToolbarText("�·�"));
		ComboBox yuef = new ComboBox();
		yuef.setTransform("YUEF");
		yuef.setWidth(60);
//		yuef.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(yuef);
		
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
		
		ToolbarButton tb = new ToolbarButton(null,"ˢ��","function(){document.getElementById('RefurbishButton').click();}");
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
	
	
//	�󱨱�����
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
		fahdwList.add(new IDropDownBean(0,"�ֳ�"));
		fahdwList.add(new IDropDownBean(1,"�ֿ�"));
		fahdwList.add(new IDropDownBean(2,"�ֳ��ֿ�"));
		fahdwList.add(new IDropDownBean(3,"�ֿ�ֳ�"));

		_IBaoblxModel = new IDropDownModel(fahdwList);
		
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.Close();
		}
		return _IBaoblxModel;
	}
	

}