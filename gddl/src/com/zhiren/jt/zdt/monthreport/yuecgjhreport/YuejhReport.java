package  com.zhiren.jt.zdt.monthreport.yuecgjhreport;

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
import com.zhiren.common.SysConstant;
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

public class YuejhReport  extends BasePage {
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
	
	private String mstrReportName = "";

	private boolean blnIsBegin = false;

	public String getPrintTable() {
		
		if (!blnIsBegin) {
			return "";
		}
		blnIsBegin = false;

		if(getBaoblxValue()!=null){
			if (getBaoblxValue().getValue().equals("ú̿�ɹ��ƻ���")){
				return getYuecgjh();
			}else if(getBaoblxValue().getValue().equals("ú̿�걨�ƻ���")){
				return getYuesbjh();
			}else if(getBaoblxValue().getValue().equals("�е�����ճɱ�����")){
				return getZhiltj();
			}
		}else{
			return "";
		}
		return "";

	}
	
	//�²ɹ��ƻ���ѯ
	private String getYuecgjh(){
		StringBuffer strSQL= new StringBuffer();
		_CurrentPage=1;
		_AllPages=1;
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon cn = new JDBCcon();
		//�ж��û�����ϵͳ�����ݵĳ�ʼ״̬
		String zhuangt="";
		if(visit.getRenyjb()==3){
			zhuangt="";
		}else if(visit.getRenyjb()==2){
			zhuangt=" and (sl.zhuangt=1 or sl.zhuangt=2)";
		}else if(visit.getRenyjb()==1){
			zhuangt=" and sl.zhuangt=2";
		}
		//�õ�ʱ��������ֵ
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
//		 ���·���1��ʱ����ʾ01,
		String StrMonth = "";
		if (intMonth < 10) {
			StrMonth = "0" + intMonth;
		} else {
			StrMonth = "" + intMonth;
		}
		
		visit.setString3(""+intyear+"-"+StrMonth);//���

		String strGongsID = "";
		int jib=this.getDiancTreeJib();
		if(jib==1){//ѡ����ʱˢ�³����еĵ糧
			strGongsID=" ";
		}else if (jib==2){//ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
			strGongsID = "  and (dc.fuid=  " +this.getTreeid()+" or dc.shangjgsid="+this.getTreeid()+")";
		}else if (jib==3){//ѡ�糧ֻˢ�³��õ糧
			strGongsID=" and dc.id= " +this.getTreeid();
		}else if (jib==-1){
			strGongsID=" and dc.id = "+((Visit) getPage().getVisit()).getDiancxxb_id();
		}
		
		//�����ͷ����
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		String arrFormat[] =null;
		String titlename="ú̿�ɹ��ƻ���";
		//��������
		
		//���cg
		StringBuffer str_cg = new StringBuffer();
		str_cg.append("(select y.diancxxb_id,y.gongysb_id,y.jihkjb_id ,\n");
		str_cg.append("max(y.faz_id) as faz_id,max(y.daoz_id) as daoz_id,sum(y.yuejhcgl) as cgl,\n");
		str_cg.append("decode(sum(y.yuejhcgl),0,0,Round(sum(y.chebjg*y.yuejhcgl)/sum(y.yuejhcgl),2)) as chebj,\n");
		str_cg.append("decode(sum(y.yuejhcgl),0,0,Round(sum(y.huiff*y.yuejhcgl)/sum(y.yuejhcgl),2)) as huiff,\n");//�ӷ���
		str_cg.append("decode(sum(y.yuejhcgl),0,0,Round(sum(y.liuf*y.yuejhcgl)/sum(y.yuejhcgl),2)) as liuf,\n");//���
		str_cg.append("decode(sum(y.yuejhcgl),0,0,Round(sum(y.yunf*y.yuejhcgl)/sum(y.yuejhcgl),2)) as yunf,\n");
		str_cg.append("decode(sum(y.yuejhcgl),0,0,Round(sum(y.zaf*y.yuejhcgl)/sum(y.yuejhcgl),2)) as zaf,\n");
		str_cg.append("decode(sum(y.yuejhcgl),0,0,Round(sum(y.rez*y.yuejhcgl)/sum(y.yuejhcgl),2)) as rez,\n");
		str_cg.append("decode(sum(y.yuejhcgl),0,0,Round(sum(y.biaomdj*y.yuejhcgl)/sum(y.yuejhcgl),2)) as biaomdj,\n");
		str_cg.append("decode(sum(y.yuejhcgl),0,0,Round(sum((y.chebjg+y.yunf+y.zaf)*y.yuejhcgl)/sum(y.yuejhcgl),2)) as daocj,\n");
		str_cg.append("decode(sum(y.yuejhcgl),0,0,Round(sum(y.chebjg*y.yuejhcgl)/sum(y.yuejhcgl),2)) as kouj,\n");
		str_cg.append("max(y.jiakk) as jiakk,max(y.jihddsjysl) as sls \n");
		str_cg.append("from yuecgjhb y where y.riq=to_date('"+ intyear+ "-"+ intMonth+ "-01','yyyy-mm-dd') \n");
		str_cg.append("group by (y.diancxxb_id,y.jihkjb_id,y.gongysb_id)) cg,\n");
		//���zj
		StringBuffer str_zj = new StringBuffer();
		str_zj.append("(select n.diancxxb_id,n.gongysb_id,n.jihkjb_id ,sum(n.hej) as nianhj from niandhtqkb n\n");
		str_zj.append("where to_char(n.riq,'yyyy')='"+intyear+"' group by (n.diancxxb_id,n.jihkjb_id,n.gongysb_id)) zj,\n");
		//���shid
		StringBuffer str_shid = new StringBuffer();
		str_shid.append("( select kj.diancxxb_id,kj.gongysb_id,kj.jihkjb_id,sum(sl.laimsl) as shijlm\n");
		str_shid.append(" from yueslb sl,yuetjkjb kj where kj.riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') and\n");
		str_shid.append(" sl.fenx='�ۼ�' and sl.yuetjkjb_id=kj.id "+zhuangt+"\n");
		str_shid.append("group by (kj.diancxxb_id,kj.jihkjb_id,kj.gongysb_id)) shid,\n");
		//���jh
		StringBuffer str_jh = new StringBuffer();
		str_jh.append("(select n.diancxxb_id,n.gongysb_id,n.jihkjb_id,sum(n.hej) as leijjh\n");
		str_jh.append(" from niandhtqkb n where n.riq>=to_date('"+intyear+"-01-01','yyyy-mm-dd') and\n");
		str_jh.append(" n.riq<=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')\n");
		str_jh.append(" group by (n.diancxxb_id,n.jihkjb_id,n.gongysb_id)) jh,\n");
		
		/**
		 * ���ñ�������������Ĳ�ѯ����
		 * ���ͣ��ֳ��ֿ󣬷ֿ�ֳ����ֳ����ֿ�
		 * �Զ�����ַ�������dianwmc (���ݲ�ͬ�ı������������÷����ֶ�)
		 * *********************************************************
		 * �����û��ļ�������ѯ���ݵ�SQL
		 * jib: "1" ���ż���"2" �ֹ�˾��ȼ�Ϲ�˾�� "3" �糧��
		 */
		String dianwmc="";
		String groupby="";
		String orderby="";
		String havingnot="";
		if (getJihkjValue()!=null){
			if(getJihkjValue().getValue().equals("�ֳ��ֿ�")){
				titlename=titlename+"(�ֳ��ֿ�)";
				if(jib==1){//�����û�
					dianwmc=
						  /*"case when grouping(f.mingc)=1 then '�ܼ�' when grouping(j.mingc)=0 and \n"
						  + "grouping(f.mingc)=1  then j.mingc when grouping(f.mingc)=0  and grouping(dc.mingc)=1 then '&nbsp;&nbsp;'||f.mingc\n"
						  + "else '&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc end as danw,\n"*/
						  " getLinkMingxTaiz('1',decode(grouping(dc.mingc),1,-1,max(dc.id))," 
						  +"decode(grouping(dc.mingc)+grouping(f.mingc),2,'�ܼ�',1,f.mingc,'&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc)) as danw,"
						  + "case when grouping(g.mingc)=0 then '&nbsp;&nbsp;&nbsp;&nbsp;'||g.mingc else\n"
						  + "case when grouping(j.mingc)=0 then j.mingc else\n"
						  + "case when grouping(dc.mingc)=0 then 'С��' else '�ϼ�' end end  end ��ú��λ,\n"
						  + "decode(grouping(g.mingc),1,'',max(c1.mingc)) as faz,\n"
						  + "decode(grouping(g.mingc),1,'',max(c2.mingc)) as daoz,\n";
					groupby="group by  grouping sets (j.mingc,f.mingc,(f.mingc,j.mingc),\n"
						  + "(f.mingc,dc.mingc),(f.mingc,dc.mingc,j.mingc),(f.mingc,dc.mingc,j.mingc,g.mingc))\n";
					orderby="oorder by grouping(f.mingc) desc,min(f.xuh),f.mingc,grouping(dc.mingc) desc,min(dc.xuh),dc.mingc,grouping(j.mingc) desc,min(j.xuh),j.mingc,grouping(g.mingc) desc ,g.mingc";

				}else if (jib==2){//�ֹ�˾��ȼ�Ϲ�˾
					String ranlgs = "select id from diancxxb where shangjgsid= "+this.getTreeid();
					try{
						ResultSet rl = cn.getResultSet(ranlgs);
						if(rl.next()){//ȼ�Ϲ�˾
							dianwmc=
								  /*"case when grouping(vdc.rlgsmc)=0 then vdc.rlgsmc when grouping(j.mingc)=0 and \n"
								  + "grouping(f.mingc)=1  then j.mingc when grouping(f.mingc)=0  and grouping(dc.mingc)=1 then '&nbsp;&nbsp;'||f.mingc \n"
								  + "else '&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc end as danw,\n"*/
								 " getLinkMingxTaiz('1',decode(grouping(dc.mingc),1,-1,max(dc.id))," 
								  +"decode(grouping(dc.mingc)+grouping(vdc.rlgsmc),2,'�ܼ�',1,vdc.rlgsmc,'&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc)) as danw,"
								  + "case when grouping(g.mingc)=0 then '&nbsp;&nbsp;&nbsp;&nbsp;'||g.mingc else\n"
								  + "case when grouping(j.mingc)=0 then j.mingc else\n"
								  + "case when grouping(dc.mingc)=0 then 'С��' else '�ϼ�' end end  end ��ú��λ,\n"
								  + "decode(grouping(g.mingc),1,'',max(c1.mingc)) as faz,\n"
								  + "decode(grouping(g.mingc),1,'',max(c2.mingc)) as daoz,\n";
							groupby="group by  grouping sets (j.mingc,vdc.rlgsmc,(vdc.rlgsmc,j.mingc),\n"
								  + "(vdc.rlgsmc,dc.mingc),(vdc.rlgsmc,dc.mingc,j.mingc),(vdc.rlgsmc,dc.mingc,j.mingc,g.mingc))\n";
							orderby="order by grouping(vdc.rlgsmc) desc,min(vdc.rlgsxh),vdc.rlgsmc,grouping(dc.mingc) desc,min(dc.xuh),dc.mingc,grouping(j.mingc) desc,min(j.xuh),j.mingc,grouping(g.mingc) desc ,g.mingc";
						}else{//�ֹ�˾
							dianwmc=
								  /*"case when grouping(f.mingc)=1 then '�ܼ�' when grouping(j.mingc)=0 and \n"
								  + "grouping(f.mingc)=1  then j.mingc when grouping(f.mingc)=0  and grouping(dc.mingc)=1 then '&nbsp;&nbsp;'||f.mingc\n"
								  + "else '&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc end as danw,\n"*/
								  "  getLinkMingxTaiz('1',decode(grouping(dc.mingc),1,-1,max(dc.id)),"
								  +" decode(grouping(f.mingc)+grouping(dc.mingc),2,'�ܼ�',"
								  +" 1,f.mingc,'&nbsp;&nbsp;'||dc.mingc)) as danw,\n"
								  + "case when grouping(g.mingc)=0 then '&nbsp;&nbsp;&nbsp;&nbsp;'||g.mingc else\n"
								  + "case when grouping(j.mingc)=0 then j.mingc else\n"
								  + "case when grouping(dc.mingc)=0 then 'С��' else '�ϼ�' end end  end ��ú��λ,\n"
								  + "decode(grouping(g.mingc),1,'',max(c1.mingc)) as faz,\n"
								  + "decode(grouping(g.mingc),1,'',max(c2.mingc)) as daoz,\n";
							groupby="group by  grouping sets (j.mingc,f.mingc,(f.mingc,j.mingc),\n"
								  + "(f.mingc,dc.mingc),(f.mingc,dc.mingc,j.mingc),(f.mingc,dc.mingc,j.mingc,g.mingc))\n";
							havingnot="having not grouping(f.mingc)=1\n";
							orderby="order by grouping(f.mingc) desc,min(f.xuh),f.mingc,grouping(dc.mingc) desc,min(dc.xuh),dc.mingc,grouping(j.mingc) desc,min(j.xuh),j.mingc,grouping(g.mingc) desc ,g.mingc";
						}
						rl.close();
					}catch(Exception e){
						e.printStackTrace();
					}finally{
						cn.Close();
					}
				}else {//�糧
					dianwmc=
						  /*"case when grouping(f.mingc)=1 then '�ܼ�' when grouping(j.mingc)=0 and \n"
						  + "grouping(f.mingc)=1  then j.mingc when grouping(f.mingc)=0  and grouping(dc.mingc)=1 then '&nbsp;&nbsp;'||f.mingc\n"
						  + "else '&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc end as danw,\n"*/
						  "  getLinkMingxTaiz('1',decode(grouping(dc.mingc),1,-1,max(dc.id))," 
						  + "decode(grouping(dc.mingc),1,'�ܼ�',dc.mingc)) as danw,\n"
						  + "case when grouping(g.mingc)=0 then '&nbsp;&nbsp;&nbsp;&nbsp;'||g.mingc else\n"
						  + "case when grouping(j.mingc)=0 then j.mingc else\n"
						  + "case when grouping(dc.mingc)=0 then 'С��' else '�ϼ�' end end  end ��ú��λ,\n"
						  + "decode(grouping(g.mingc),1,'',max(c1.mingc)) as faz,\n"
						  + "decode(grouping(g.mingc),1,'',max(c2.mingc)) as daoz,\n";
					groupby="group by  grouping sets (j.mingc,f.mingc,(f.mingc,j.mingc),\n"
						  + "(f.mingc,dc.mingc),(f.mingc,dc.mingc,j.mingc),(f.mingc,dc.mingc,j.mingc,g.mingc))\n";
					havingnot="having not grouping(dc.mingc)=1\n";
					orderby="order by grouping(f.mingc) desc,min(f.xuh),f.mingc,grouping(dc.mingc) desc,min(dc.xuh),dc.mingc,grouping(j.mingc) desc,min(j.xuh),j.mingc,grouping(g.mingc) desc ,g.mingc";
				}
			}else if (getJihkjValue().getValue().equals("�ֿ�ֳ�")){
				titlename=titlename+"(�ֿ�ֳ�)";
				if(jib==1){//�����û�
					dianwmc="case when grouping(g.mingc)=1 then j.mingc else '&nbsp;&nbsp;'||g.mingc end as danw,\n"
						  + "decode(grouping(j.mingc)+grouping(g.mingc)+grouping(dc.mingc),3,'�ܼ�',2,'�ϼ�',1,'С��','&nbsp;&nbsp;'||dc.mingc) as mingc,\n"
						  + "decode(grouping(g.mingc),1,'',max(c1.mingc)) as faz,\n"
						  + "decode(grouping(g.mingc),1,'',max(c2.mingc)) as daoz,\n";
					groupby="group by  rollup (j.mingc,g.mingc,dc.mingc)\n";
					orderby="order by grouping(j.mingc) desc ,j.mingc desc,grouping(g.mingc) desc ,g.mingc,grouping(dc.mingc) desc ,dc.mingc desc,max(dc.xuh)";
				}else if (jib==2){//�ֹ�˾��ȼ�Ϲ�˾
					String ranlgs = "select id from diancxxb where shangjgsid= "+this.getTreeid();
					try{
						ResultSet rl = cn.getResultSet(ranlgs);
						if(rl.next()){//ȼ�Ϲ�˾
							dianwmc="case when grouping(g.mingc)=1 then j.mingc else '&nbsp;&nbsp;'||g.mingc end as danw,\n"
								  + "decode(grouping(j.mingc)+grouping(g.mingc)+grouping(dc.mingc),3,'�ܼ�',2,'�ϼ�',1,'С��','&nbsp;&nbsp;'||dc.mingc) as mingc,\n"
								  + "decode(grouping(g.mingc),1,'',max(c1.mingc)) as faz,\n"
								  + "decode(grouping(g.mingc),1,'',max(c2.mingc)) as daoz,\n";
							groupby="group by  rollup (j.mingc,g.mingc,dc.mingc)\n";
							orderby="order by grouping(j.mingc) desc ,j.mingc desc,grouping(g.mingc) desc ,g.mingc,grouping(dc.mingc) desc ,dc.mingc desc";
						}else{//�ֹ�˾
							dianwmc="case when grouping(g.mingc)=1 then j.mingc else '&nbsp;&nbsp;'||g.mingc end as danw,\n"
								  + "decode(grouping(j.mingc)+grouping(g.mingc)+grouping(dc.mingc),3,'�ܼ�',2,'�ϼ�',1,'С��','&nbsp;&nbsp;'||dc.mingc) as mingc,\n"
								  + "decode(grouping(g.mingc),1,'',max(c1.mingc)) as faz,\n"
								  + "decode(grouping(g.mingc),1,'',max(c2.mingc)) as daoz,\n";
							groupby="group by  rollup (j.mingc,g.mingc,dc.mingc)\n";
							orderby="order by grouping(j.mingc) desc ,j.mingc desc,grouping(g.mingc) desc ,g.mingc,grouping(dc.mingc) desc ,dc.mingc desc,max(dc.xuh)";
						}
						rl.close();
					}catch(Exception e){
						e.printStackTrace();
					}finally{
						cn.Close();
					}
				}else {//�糧
					dianwmc="case when grouping(g.mingc)=1 then j.mingc else '&nbsp;&nbsp;'||g.mingc end as danw,\n"
						  + "decode(grouping(j.mingc)+grouping(g.mingc)+grouping(dc.mingc),3,'�ܼ�',2,'�ϼ�',1,'С��','&nbsp;&nbsp;'||dc.mingc) as mingc,\n"
						  + "decode(grouping(g.mingc),1,'',max(c1.mingc)) as faz,\n"
						  + "decode(grouping(g.mingc),1,'',max(c2.mingc)) as daoz,\n";
					groupby="group by  rollup (j.mingc,g.mingc,dc.mingc)\n";
					orderby="order by grouping(j.mingc) desc ,j.mingc desc,grouping(g.mingc) desc ,g.mingc,grouping(dc.mingc) desc ,dc.mingc desc,max(dc.xuh)";
				}
			}else if (getJihkjValue().getValue().equals("�ֳ�")){
				titlename=titlename+"(�ֳ�)";
				if(jib==1){//�����û�
					dianwmc=
						  /*"case when grouping(f.mingc)=1 then '�ܼ�' when grouping(j.mingc)=0 and \n"
						  + "grouping(f.mingc)=1  then j.mingc when grouping(f.mingc)=0  and grouping(dc.mingc)=1 then '&nbsp;&nbsp;'||f.mingc\n"
						  + "else '&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc end as danw,\n"*/
						  " getLinkMingxTaiz('1',decode(grouping(dc.mingc),1,-1,max(dc.id))," 
						  +"decode(grouping(dc.mingc)+grouping(f.mingc),2,'�ܼ�',1,f.mingc,'&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc)) as danw,"
						  + "case when grouping(j.mingc)=0 then j.mingc else \n"
						  + "case when grouping(dc.mingc)=0 then 'С��' else '�ϼ�' end end  ��ú��λ,\n"
						  + "decode(grouping(f.mingc)+grouping(j.mingc),1,'',max(c1.mingc)) as faz,\n"
						  + "decode(grouping(f.mingc)+grouping(j.mingc),1,'',max(c2.mingc))  as daoz,\n";
					groupby="group by  grouping sets (j.mingc,f.mingc,(f.mingc,j.mingc),\n (f.mingc,dc.mingc),(f.mingc,dc.mingc,j.mingc))\n";
					orderby="order by grouping(f.mingc) desc,min(f.xuh),f.mingc,grouping(dc.mingc) desc,min(dc.xuh),dc.mingc,grouping(j.mingc) desc,min(j.xuh),j.mingc";
				}else if (jib==2){//�ֹ�˾��ȼ�Ϲ�˾
					String ranlgs = "select id from diancxxb where shangjgsid= "+this.getTreeid();
					try{
						ResultSet rl = cn.getResultSet(ranlgs);
						if(rl.next()){//ȼ�Ϲ�˾
							dianwmc=
								  /*"case when grouping(vdc.rlgsmc)=1 then '�ܼ�' when grouping(j.mingc)=0 and \n"
								  + "grouping(vdc.rlgsmc)=1  then j.mingc when grouping(vdc.rlgsmc)=0  and grouping(dc.mingc)=1 then '&nbsp;&nbsp;'||vdc.rlgsmc\n"
								  + "else '&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc end as danw,\n"*/
								" getLinkMingxTaiz('1',decode(grouping(dc.mingc),1,-1,max(dc.id))," 
								  +"decode(grouping(dc.mingc)+grouping(vdc.rlgsmc),2,'�ܼ�',1,vdc.rlgsmc,'&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc)) as danw,"
								  + "case when grouping(j.mingc)=0 then j.mingc else \n"
								  + "case when grouping(dc.mingc)=0 then 'С��' else '�ϼ�' end end  ��ú��λ,\n"
								  + "decode(grouping(vdc.rlgsmc)+grouping(dc.mingc),1,'',max(c1.mingc)) as faz,\n"
								  + "decode(grouping(vdc.rlgsmc)+grouping(dc.mingc),1,'',max(c2.mingc))  as daoz,\n";
							groupby="group by  grouping sets (j.mingc,vdc.rlgsmc,(vdc.rlgsmc,j.mingc),\n (vdc.rlgsmc,dc.mingc),(vdc.rlgsmc,dc.mingc,j.mingc))\n";
							orderby="order by grouping(vdc.rlgsmc) desc,min(vdc.rlgsxh),vdc.rlgsmc,grouping(dc.mingc) desc,min(dc.xuh),dc.mingc,grouping(j.mingc) desc,min(j.xuh),j.mingc";
						}else{//�ֹ�˾
							dianwmc=
								  /*"case when grouping(f.mingc)=1 then '�ܼ�' when grouping(j.mingc)=0 and \n"
								  + "grouping(f.mingc)=1  then j.mingc when grouping(f.mingc)=0  and grouping(dc.mingc)=1 then '&nbsp;&nbsp;'||f.mingc\n"
								  + "else '&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc end as danw,\n"*/
								  "  getLinkMingxTaiz('1',decode(grouping(dc.mingc),1,-1,max(dc.id)),"
								  +" decode(grouping(f.mingc)+grouping(dc.mingc),2,'�ܼ�',"
								  +" 1,f.mingc,'&nbsp;&nbsp;'||dc.mingc)) as danw,\n"
								  + "case when grouping(j.mingc)=0 then j.mingc else \n"
								  + "case when grouping(dc.mingc)=0 then 'С��' else '�ϼ�' end end  ��ú��λ,\n"
								  + "decode(grouping(f.mingc)+grouping(dc.mingc),1,'',max(c1.mingc)) as faz,\n"
								  + "decode(grouping(f.mingc)+grouping(dc.mingc),1,'',max(c2.mingc))  as daoz,\n";
							groupby="group by  grouping sets (j.mingc,f.mingc,(f.mingc,j.mingc),\n (f.mingc,dc.mingc),(f.mingc,dc.mingc,j.mingc))\n";
							havingnot="having not grouping(f.mingc)=1\n";
							orderby="order by grouping(f.mingc) desc,min(f.xuh),f.mingc,grouping(dc.mingc) desc,min(dc.xuh),dc.mingc,grouping(j.mingc) desc,min(j.xuh),j.mingc";
						}
						rl.close();
					}catch(Exception e){
						e.printStackTrace();
					}finally{
						cn.Close();
					}
				}else {//�糧
					dianwmc=
						  /*"case when grouping(f.mingc)=1 then '�ܼ�' when grouping(j.mingc)=0 and \n"
						  + "grouping(f.mingc)=1  then j.mingc when grouping(f.mingc)=0  and grouping(dc.mingc)=1 then '&nbsp;&nbsp;'||f.mingc\n"
						  + "else '&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc end as danw,\n"*/
						  "  getLinkMingxTaiz('1',decode(grouping(dc.mingc),1,-1,max(dc.id))," 
						  + "decode(grouping(dc.mingc),1,'�ܼ�',dc.mingc)) as danw,\n"
						  + "case when grouping(j.mingc)=0 then j.mingc else \n"
						  + "case when grouping(dc.mingc)=0 then 'С��' else '�ϼ�' end end  ��ú��λ,\n"
						  + "decode(grouping(f.mingc)+grouping(dc.mingc),1,'',max(c1.mingc)) as faz,\n"
						  + "decode(grouping(f.mingc)+grouping(dc.mingc),1,'',max(c2.mingc))  as daoz,\n";
					groupby="group by  grouping sets (j.mingc,f.mingc,(f.mingc,j.mingc),\n (f.mingc,dc.mingc),(f.mingc,dc.mingc,j.mingc))\n";
					havingnot="having not grouping(dc.mingc)=1 \n";
					orderby="order by grouping(f.mingc) desc,min(f.xuh),f.mingc,grouping(dc.mingc) desc,min(dc.xuh),dc.mingc,grouping(j.mingc) desc,min(j.xuh),j.mingc";
				}
			}else if (getJihkjValue().getValue().equals("�ֿ�")){
				titlename=titlename+"(�ֿ�)";
				if(jib==1){//�����û�
					dianwmc="decode(grouping(g.mingc)+grouping(j.mingc),2,'�ܼ�',1,j.mingc,'&nbsp;&nbsp;&nbsp;&nbsp;'||g.mingc) as danw,\n"
						  + "decode(grouping(g.mingc),1,'',max(c1.mingc)) as faz,\n"
						  + "decode(grouping(g.mingc),1,'',max(c2.mingc))  as daoz,\n";
					groupby="group by  rollup (j.mingc,g.mingc)\n";
					orderby="order by grouping(j.mingc) desc ,j.mingc desc,grouping(g.mingc) desc ,g.mingc desc";
				}else if (jib==2){//�ֹ�˾��ȼ�Ϲ�˾
					String ranlgs = "select id from diancxxb where shangjgsid= "+this.getTreeid();
					try{
						ResultSet rl = cn.getResultSet(ranlgs);
						if(rl.next()){//ȼ�Ϲ�˾
							dianwmc="decode(grouping(g.mingc)+grouping(j.mingc),2,'�ܼ�',1,j.mingc,'&nbsp;&nbsp;&nbsp;&nbsp;'||g.mingc) as danw,\n"
								  + "decode(grouping(g.mingc),1,'',max(c1.mingc)) as faz,\n"
								  + "decode(grouping(g.mingc),1,'',max(c2.mingc))  as daoz,\n";
							groupby="group by  rollup (j.mingc,g.mingc)\n";
							orderby="order by grouping(j.mingc) desc ,j.mingc desc,grouping(g.mingc) desc ,g.mingc desc";
						}else{//�ֹ�˾
							dianwmc="decode(grouping(g.mingc)+grouping(j.mingc),2,'�ܼ�',1,j.mingc,'&nbsp;&nbsp;&nbsp;&nbsp;'||g.mingc) as danw,\n"
								  + "decode(grouping(g.mingc),1,'',max(c1.mingc)) as faz,\n"
								  + "decode(grouping(g.mingc),1,'',max(c2.mingc))  as daoz,\n";
							groupby="group by  rollup (j.mingc,g.mingc)\n";
							orderby="order by grouping(j.mingc) desc ,j.mingc desc,grouping(g.mingc) desc ,g.mingc desc";
						}
						rl.close();
					}catch(Exception e){
						e.printStackTrace();
					}finally{
						cn.Close();
					}
				}else {//�糧
					dianwmc="decode(grouping(g.mingc)+grouping(j.mingc),2,'�ܼ�',1,j.mingc,'&nbsp;&nbsp;&nbsp;&nbsp;'||g.mingc) as danw,\n"
						  + "decode(grouping(g.mingc),1,'',max(c1.mingc)) as faz,\n"
						  + "decode(grouping(g.mingc),1,'',max(c2.mingc))  as daoz,\n";
					groupby="group by  rollup (j.mingc,g.mingc)\n";
					orderby="order by grouping(j.mingc) desc ,j.mingc desc,grouping(g.mingc) desc ,g.mingc desc";
				}
			}
		}else{
			return "";
		}
		
		//*********************************************��SQL*********************************************//
		strSQL.append(" select \n");
		//�Զ��������
		strSQL.append(dianwmc);
		//����Ĭ������
		strSQL.append( " sum(zj.nianhj)*10000 as zj,\n");
		strSQL.append( " decode(sum(jh.leijjh),0,0,Round(sum(shid.shijlm)*100/(sum(jh.leijjh)*10000),2)) as daohl,\n");
		strSQL.append( " sum(cg.cgl) as cgl,\n");
		strSQL.append( " decode(sum(cg.cgl),0,0,Round(sum(cg.rez*cg.cgl)/sum(cg.cgl),2)) as rez,\n");//��ֵ
		strSQL.append( " decode(sum(cg.cgl),0,0,Round(sum(cg.huiff*cg.cgl)/sum(cg.cgl),2)) as huiff,\n");//�ӷ���
		strSQL.append( " decode(sum(cg.cgl),0,0,Round(sum(cg.liuf*cg.cgl)/sum(cg.cgl),2)) as liuf,\n");//���
		strSQL.append( " decode(sum(cg.cgl),0,0,Round(sum(cg.chebj*cg.cgl)/sum(cg.cgl),2)) as chebj,\n");
		strSQL.append( " decode(sum(cg.cgl),0,0,Round(sum(cg.yunf*cg.cgl)/sum(cg.cgl),2)) as yunf,\n");
		strSQL.append( " decode(sum(cg.cgl),0,0,Round(sum(cg.zaf*cg.cgl)/sum(cg.cgl),2)) as zaf,\n");
		strSQL.append( " decode(sum(cg.cgl),0,0,Round(sum((cg.chebj+cg.yunf+cg.zaf)*cg.cgl)/sum(cg.cgl),2)) as daocj,\n");
		//strSQL.append( "decode(sum(cg.cgl),0,0,Round(sum(cg.biaomdj*cg.cgl)/sum(cg.cgl),2)) as biaomdj\n);"
		strSQL.append( " Round(decode(sum(cg.rez*cg.cgl),0,0,Round((sum((cg.chebj+cg.yunf+cg.zaf)*cg.cgl)/sum(cg.cgl)),2)*29.271/");
		strSQL.append( " Round((sum(cg.rez*cg.cgl)/sum(cg.cgl)),2)),2) as biaomdj \n");
        //����˰��ú����
		strSQL.append( " ,Round(decode(sum(cg.rez*cg.cgl),0,0,Round((sum((cg.chebj/(1+0.17)+cg.yunf*(1-0.07)+cg.zaf)*cg.cgl)/sum(cg.cgl)),2)*29.271/");
		strSQL.append( " Round((sum(cg.rez*cg.cgl)/sum(cg.cgl)),2)),2) as buhs_biaomdj \n");
		//�������ı�
		strSQL.append( " from \n ");
		strSQL.append(str_cg.toString()).append(str_zj.toString()).append(str_shid.toString()).append(str_jh.toString());
		//���ã�where,group by ,order by 
		strSQL.append( " diancxxb dc,gongysb g,jihkjb j,chezxxb c1,chezxxb c2,vwfengs f,vwdianc vdc\n");
		strSQL.append( " where cg.diancxxb_id=zj.diancxxb_id(+)\n");
		strSQL.append( " and   cg.diancxxb_id=shid.diancxxb_id(+)\n");
		strSQL.append( " and   cg.diancxxb_id=jh.diancxxb_id(+)\n");
		strSQL.append( " and   cg.diancxxb_id=dc.id\n");
		strSQL.append( " and   cg.gongysb_id=zj.gongysb_id(+)\n");
		strSQL.append( " and   cg.gongysb_id=shid.diancxxb_id(+)\n");
		strSQL.append( " and   cg.gongysb_id=jh.gongysb_id(+)\n");
		strSQL.append( " and   cg.gongysb_id=g.id\n");
		strSQL.append( " and   cg.jihkjb_id=zj.jihkjb_id(+)\n");
		strSQL.append( " and   cg.jihkjb_id=shid.jihkjb_id(+)\n");
		strSQL.append( " and   cg.jihkjb_id=jh.jihkjb_id(+)\n");
		strSQL.append( " and   cg.jihkjb_id=j.id\n");
		strSQL.append( " and   cg.faz_id=c1.id\n");
		strSQL.append( " and   cg.daoz_id=c2.id\n");
		strSQL.append( " and   dc.id=vdc.id \n");
		strSQL.append( "  "+strGongsID+"\n");
		strSQL.append( " and   dc.fuid=f.id\n");
	    /*strSQL.append( " and   j.id!=3\n");*/	
		strSQL.append(groupby);
		strSQL.append(havingnot);
		strSQL.append(orderby);
		
		//���ñ���ı�ͷ��
		if(getJihkjValue().getValue().equals("�ֳ��ֿ�")){
			ArrHeader=new String[2][16];
			ArrHeader[0]=new String[]{"��λ","��Ӧ��λ","��վ(��)","��վ(��)","��ȶ�<br>����","�ۼƵ�<br>����%","�ƻ���<br>������","����","����","����","�۸�","�۸�","�۸�","�۸�","�۸�","�۸�"};
		    ArrHeader[1]=new String[] {"��λ","��Ӧ��λ","��վ(��)","��վ(��)","��ȶ�<br>����","�ۼƵ�<br>����%","�ƻ���<br>������","��ֵ","�ӷ���","���","ú��","�˷�","�ӷ�","������","��ú����","����˰��ú����"};
			ArrWidth =new int[] {150,150,60,60,60,60,60,50,50,50,50,50,50,50,50,50};
		    arrFormat = new String[] { "", "","","", "0", "0.00", "0","0.00", "0.00", "0.00","0.00", "0.00","0.00","0.00","0.00","0.00"};
		}else if(getJihkjValue().getValue().equals("�ֿ�ֳ�")){
			ArrHeader=new String[2][16];
			ArrHeader[0]=new String[]{"��Ӧ��λ","��λ","��վ(��)","��վ(��)","��ȶ�<br>����","�ۼƵ�<br>����%","�ƻ���<br>������","����","����","����","�۸�","�۸�","�۸�","�۸�","�۸�","�۸�"};
		    ArrHeader[1]=new String[] {"��Ӧ��λ","��λ","��վ(��)","��վ(��)","��ȶ�<br>����","�ۼƵ�<br>����%","�ƻ���<br>������","��ֵ","�ӷ���","���","ú��","�˷�","�ӷ�","������","��ú����","����˰��ú����"};
			ArrWidth =new int[] {150,150,60,60,60,60,60,50,50,50,50,50,50,50,50,50};
		    arrFormat = new String[] { "", "","","", "0", "0.00", "0","0.00", "0.00", "0.00","0.00", "0.00","0.00","0.00","0.00","0.00"};
		}else if(getJihkjValue().getValue().equals("�ֿ�")){
			ArrHeader=new String[2][15];
			ArrHeader[0]=new String[]{"��Ӧ��λ","��վ(��)","��վ(��)","��ȶ�<br>����","�ۼƵ�<br>����%","�ƻ���<br>������","����","����","����","�۸�","�۸�","�۸�","�۸�","�۸�","�۸�"};
		    ArrHeader[1]=new String[] {"��Ӧ��λ","��վ(��)","��վ(��)","��ȶ�<br>����","�ۼƵ�<br>����%","�ƻ���<br>������","��ֵ","�ӷ���","���","ú��","�˷�","�ӷ�","������","��ú����","����˰��ú����"};
			ArrWidth =new int[] {150,60,60,60,60,60,50,50,50,50,50,50,50,50,50};
		    arrFormat = new String[] { "","","", "0", "0.00", "0","0.00", "0.00", "0.00","0.00", "0.00","0.00","0.00","0.00","0.00"};
		}else if(getJihkjValue().getValue().equals("�ֳ�")){
			ArrHeader=new String[2][16];
			ArrHeader[0]=new String[]{"��λ","�ƻ��ھ�","��վ(��)","��վ(��)","��ȶ�<br>����","�ۼƵ�<br>����%","�ƻ���<br>������","����","����","����","�۸�","�۸�","�۸�","�۸�","�۸�","�۸�"};
		    ArrHeader[1]=new String[] {"��λ","�ƻ��ھ�","��վ(��)","��վ(��)","��ȶ�<br>����","�ۼƵ�<br>����%","�ƻ���<br>������","��ֵ","�ӷ���","���","ú��","�˷�","�ӷ�","������","��ú����","����˰��ú����"};
			ArrWidth =new int[] {150,80,60,60,60,60,60,50,50,50,50,50,50,50,50,50};
		    arrFormat = new String[] { "","","","", "0", "0.00", "0","0.00", "0.00", "0.00","0.00", "0.00","0.00","0.00","0.00","0.00"};
		}
		
		ResultSet rs = cn.getResultSet(strSQL.toString());
		// ����
		rt.setBody(new Table(rs,2, 0, 4));
		rt.setTitle(intyear+"��"+intMonth+"��"+titlename, ArrWidth);
		rt.setDefaultTitle(1, 2, "���λ:"+((Visit) getPage().getVisit()).getDiancmc(), Table.ALIGN_LEFT);
		rt.setDefaultTitle(5, 3, "�����:"+intyear+"��"+intMonth+"��", Table.ALIGN_CENTER);
		rt.setDefaultTitle(9,3, "��λ:�֡�Ԫ/�֡�MJ/Kg", Table.ALIGN_RIGHT);
		rt.setDefaultTitle(13, 2, "cpi����09��", Table.ALIGN_RIGHT);
		
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(18);
		rt.body.setHeaderData(ArrHeader);// ��ͷ����
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		rt.body.ShowZero = true;
		if(rt.body.getRows()>2){
			rt.body.setCellAlign(3, 1, Table.ALIGN_CENTER);
		}
		if(jib==3){
			 rt.body.setColAlign(2, Table.ALIGN_LEFT);
		     rt.body.setColAlign(3, Table.ALIGN_LEFT);
		}
		if(jib==2){
			if(getJihkjValue().getValue().equals("�ֳ��ֿ�")){
				rt.body.setCellValue(3, 2, "�ܼ�");
			}
			rt.body.setColAlign(2, Table.ALIGN_LEFT);
		    rt.body.setColAlign(3, Table.ALIGN_LEFT);
		    rt.body.setColAlign(4, Table.ALIGN_LEFT);
		}
		rt.body.setColFormat(arrFormat);
		//ҳ�� 
		
		 rt.createDefautlFooter(ArrWidth);
		 rt.setDefautlFooter(1,3,"��ӡ����:"+DateUtil.FormatDate(new Date()),Table.ALIGN_LEFT);
		 rt.setDefautlFooter(7,2,"���:",Table.ALIGN_LEFT);
		 rt.setDefautlFooter(11,2,"�Ʊ�:",Table.ALIGN_RIGHT);
		
		_CurrentPage=1;
		_AllPages=rt.body.getPages();
		if (_AllPages==0){
			_CurrentPage=0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}
	
	//���걨�ƻ�
	private String getYuesbjh(){
		StringBuffer strSQL= new StringBuffer();
		_CurrentPage=1;
		_AllPages=1;
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon cn = new JDBCcon();
		//�ж��û�����ϵͳ�����ݵĳ�ʼ״̬
		String zhuangt="";
		if(visit.getRenyjb()==3){
			zhuangt="";
		}else if(visit.getRenyjb()==2){
			zhuangt=" and (sl.zhuangt=1 or sl.zhuangt=2)";
		}else if(visit.getRenyjb()==1){
			zhuangt=" and sl.zhuangt=2";
		}
		//�õ�ʱ��������ֵ
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
//		 ���·���1��ʱ����ʾ01,
		String StrMonth = "";
		if (intMonth < 10) {
			StrMonth = "0" + intMonth;
		} else {
			StrMonth = "" + intMonth;
		}
		
		visit.setString3(""+intyear+"-"+StrMonth);//���

		String strGongsID = "";
		int jib=this.getDiancTreeJib();
		if(jib==1){//ѡ����ʱˢ�³����еĵ糧
			strGongsID=" ";
		}else if (jib==2){//ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
			strGongsID = "  and (dc.fuid=  " +this.getTreeid()+" or dc.shangjgsid="+this.getTreeid()+")";
		}else if (jib==3){//ѡ�糧ֻˢ�³��õ糧
			strGongsID=" and dc.id= " +this.getTreeid();
		}else if (jib==-1){
			strGongsID=" and dc.id = "+((Visit) getPage().getVisit()).getDiancxxb_id();
		}
		
		//�����ͷ����
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		String arrFormat[] =null;
		String titlename="ú̿�걨�ƻ���";
		//��������
		
		//���cg
		StringBuffer str_cg = new StringBuffer();
		str_cg.append("(select y.diancxxb_id,y.gongysb_id,y.jihkjb_id ,\n");
		str_cg.append("max(y.faz_id) as faz_id,max(y.daoz_id) as daoz_id,sum(y.yuejhcgl) as cgl,\n");
		str_cg.append("decode(sum(y.yuejhcgl),0,0,Round(sum(y.chebjg*y.yuejhcgl)/sum(y.yuejhcgl),2)) as chebj,\n");
		str_cg.append("decode(sum(y.yuejhcgl),0,0,Round(sum(y.huiff*y.yuejhcgl)/sum(y.yuejhcgl),2)) as huiff,\n");//�ӷ���
		str_cg.append("decode(sum(y.yuejhcgl),0,0,Round(sum(y.liuf*y.yuejhcgl)/sum(y.yuejhcgl),2)) as liuf,\n");//���
		str_cg.append("decode(sum(y.yuejhcgl),0,0,Round(sum(y.yunf*y.yuejhcgl)/sum(y.yuejhcgl),2)) as yunf,\n");
		str_cg.append("decode(sum(y.yuejhcgl),0,0,Round(sum(y.zaf*y.yuejhcgl)/sum(y.yuejhcgl),2)) as zaf,\n");
		str_cg.append("decode(sum(y.yuejhcgl),0,0,Round(sum(y.rez*y.yuejhcgl)/sum(y.yuejhcgl),2)) as rez,\n");
		str_cg.append("decode(sum(y.yuejhcgl),0,0,Round(sum(y.biaomdj*y.yuejhcgl)/sum(y.yuejhcgl),2)) as biaomdj,\n");
		str_cg.append("decode(sum(y.yuejhcgl),0,0,Round(sum((y.chebjg+y.yunf+y.zaf)*y.yuejhcgl)/sum(y.yuejhcgl),2)) as daocj,\n");
		str_cg.append("decode(sum(y.yuejhcgl),0,0,Round(sum(y.chebjg*y.yuejhcgl)/sum(y.yuejhcgl),2)) as kouj,\n");
		str_cg.append("max(y.jiakk) as jiakk,max(y.jihddsjysl) as sls \n");
		str_cg.append("from yuesbjhb y where y.riq=to_date('"+ intyear+ "-"+ intMonth+ "-01','yyyy-mm-dd') \n");
		str_cg.append("group by (y.diancxxb_id,y.jihkjb_id,y.gongysb_id)) cg,\n");
		//���zj
		StringBuffer str_zj = new StringBuffer();
		str_zj.append("(select n.diancxxb_id,n.gongysb_id,n.jihkjb_id ,sum(n.hej) as nianhj from niandhtqkb n\n");
		str_zj.append("where to_char(n.riq,'yyyy')='"+intyear+"' group by (n.diancxxb_id,n.jihkjb_id,n.gongysb_id)) zj,\n");
		//���shid
		StringBuffer str_shid = new StringBuffer();
		str_shid.append("( select kj.diancxxb_id,kj.gongysb_id,kj.jihkjb_id,sum(sl.laimsl) as shijlm\n");
		str_shid.append(" from yueslb sl,yuetjkjb kj where kj.riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') and\n");
		str_shid.append(" sl.fenx='�ۼ�' and sl.yuetjkjb_id=kj.id "+zhuangt+"\n");
		str_shid.append("group by (kj.diancxxb_id,kj.jihkjb_id,kj.gongysb_id)) shid,\n");
		//���jh
		StringBuffer str_jh = new StringBuffer();
		str_jh.append("(select n.diancxxb_id,n.gongysb_id,n.jihkjb_id,sum(n.hej) as leijjh\n");
		str_jh.append(" from niandhtqkb n where n.riq>=to_date('"+intyear+"-01-01','yyyy-mm-dd') and\n");
		str_jh.append(" n.riq<=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')\n");
		str_jh.append(" group by (n.diancxxb_id,n.jihkjb_id,n.gongysb_id)) jh,\n");
		
		/**
		 * ���ñ�������������Ĳ�ѯ����
		 * ���ͣ��ֳ��ֿ󣬷ֿ�ֳ����ֳ����ֿ�
		 * �Զ�����ַ�������dianwmc (���ݲ�ͬ�ı������������÷����ֶ�)
		 * *********************************************************
		 * �����û��ļ�������ѯ���ݵ�SQL
		 * jib: "1" ���ż���"2" �ֹ�˾��ȼ�Ϲ�˾�� "3" �糧��
		 */
		String dianwmc="";
		String groupby="";
		String orderby="";
		String havingnot="";
		if (getJihkjValue()!=null){
			if(getJihkjValue().getValue().equals("�ֳ��ֿ�")){
				titlename=titlename+"(�ֳ��ֿ�)";
				if(jib==1){//�����û�
					dianwmc=
						  /*"case when grouping(f.mingc)=1 then '�ܼ�' when grouping(j.mingc)=0 and \n"
						  + "grouping(f.mingc)=1  then j.mingc when grouping(f.mingc)=0  and grouping(dc.mingc)=1 then '&nbsp;&nbsp;'||f.mingc\n"
						  + "else '&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc end as danw,\n"*/
						  " getLinkMingxTaiz('1',decode(grouping(dc.mingc),1,-1,max(dc.id))," 
						  +"decode(grouping(dc.mingc)+grouping(f.mingc),2,'�ܼ�',1,f.mingc,'&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc)) as danw,"
						  + "case when grouping(g.mingc)=0 then '&nbsp;&nbsp;&nbsp;&nbsp;'||g.mingc else\n"
						  + "case when grouping(j.mingc)=0 then j.mingc else\n"
						  + "case when grouping(dc.mingc)=0 then 'С��' else '�ϼ�' end end  end ��ú��λ,\n"
						  + "decode(grouping(g.mingc),1,'',max(c1.mingc)) as faz,\n"
						  + "decode(grouping(g.mingc),1,'',max(c2.mingc)) as daoz,\n";
					groupby="group by  grouping sets (j.mingc,f.mingc,(f.mingc,j.mingc),\n"
						  + "(f.mingc,dc.mingc),(f.mingc,dc.mingc,j.mingc),(f.mingc,dc.mingc,j.mingc,g.mingc))\n";
					orderby="order by grouping(f.mingc)+grouping(dc.mingc) desc,\n" +
							"case when grouping(f.mingc)+grouping(dc.mingc) > 0 then 0 else max(dc.xuh) end ,\n" + 
							"decode(��ú��λ,'�ϼ�',0,'С��',1,'�ص㶩��',2,3),��ú��λ,grouping(g.mingc) desc ,g.mingc";

				}else if (jib==2){//�ֹ�˾��ȼ�Ϲ�˾
					String ranlgs = "select id from diancxxb where shangjgsid= "+this.getTreeid();
					try{
						ResultSet rl = cn.getResultSet(ranlgs);
						if(rl.next()){//ȼ�Ϲ�˾
							dianwmc=
								  /*"case when grouping(vdc.rlgsmc)=0 then vdc.rlgsmc when grouping(j.mingc)=0 and \n"
								  + "grouping(f.mingc)=1  then j.mingc when grouping(f.mingc)=0  and grouping(dc.mingc)=1 then '&nbsp;&nbsp;'||f.mingc \n"
								  + "else '&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc end as danw,\n"*/
								 " getLinkMingxTaiz('1',decode(grouping(dc.mingc),1,-1,max(dc.id))," 
								  +"decode(grouping(dc.mingc)+grouping(vdc.rlgsmc),2,'�ܼ�',1,vdc.rlgsmc,'&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc)) as danw,"
								  + "case when grouping(g.mingc)=0 then '&nbsp;&nbsp;&nbsp;&nbsp;'||g.mingc else\n"
								  + "case when grouping(j.mingc)=0 then j.mingc else\n"
								  + "case when grouping(dc.mingc)=0 then 'С��' else '�ϼ�' end end  end ��ú��λ,\n"
								  + "decode(grouping(g.mingc),1,'',max(c1.mingc)) as faz,\n"
								  + "decode(grouping(g.mingc),1,'',max(c2.mingc)) as daoz,\n";
							groupby="group by  grouping sets (j.mingc,vdc.rlgsmc,(vdc.rlgsmc,j.mingc),\n"
								  + "(vdc.rlgsmc,dc.mingc),(vdc.rlgsmc,dc.mingc,j.mingc),(vdc.rlgsmc,dc.mingc,j.mingc,g.mingc))\n";
							orderby="order by grouping(vdc.rlgsmc)+grouping(dc.mingc) desc,\n" +
									"case when grouping(vdc.rlgsmc)+grouping(dc.mingc) > 0 then 0 else max(dc.xuh) end ,\n" + 
									"decode(��ú��λ,'�ϼ�',0,'С��',1,'�ص㶩��',2,3),��ú��λ,grouping(g.mingc) desc ,g.mingc";
						}else{//�ֹ�˾
							dianwmc=
								  /*"case when grouping(f.mingc)=1 then '�ܼ�' when grouping(j.mingc)=0 and \n"
								  + "grouping(f.mingc)=1  then j.mingc when grouping(f.mingc)=0  and grouping(dc.mingc)=1 then '&nbsp;&nbsp;'||f.mingc\n"
								  + "else '&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc end as danw,\n"*/
								  "  getLinkMingxTaiz('1',decode(grouping(dc.mingc),1,-1,max(dc.id)),"
								  +" decode(grouping(f.mingc)+grouping(dc.mingc),2,'�ܼ�',"
								  +" 1,f.mingc,'&nbsp;&nbsp;'||dc.mingc)) as danw,\n"
								  + "case when grouping(g.mingc)=0 then '&nbsp;&nbsp;&nbsp;&nbsp;'||g.mingc else\n"
								  + "case when grouping(j.mingc)=0 then j.mingc else\n"
								  + "case when grouping(dc.mingc)=0 then 'С��' else '�ϼ�' end end  end ��ú��λ,\n"
								  + "decode(grouping(g.mingc),1,'',max(c1.mingc)) as faz,\n"
								  + "decode(grouping(g.mingc),1,'',max(c2.mingc)) as daoz,\n";
							groupby="group by  grouping sets (j.mingc,f.mingc,(f.mingc,j.mingc),\n"
								  + "(f.mingc,dc.mingc),(f.mingc,dc.mingc,j.mingc),(f.mingc,dc.mingc,j.mingc,g.mingc))\n";
							havingnot="having not grouping(f.mingc)=1\n";
							orderby="order by grouping(f.mingc)+grouping(dc.mingc) desc,\n" +
									"case when grouping(f.mingc)+grouping(dc.mingc) > 0 then 0 else max(dc.xuh) end ,\n" + 
									"decode(��ú��λ,'�ϼ�',0,'С��',1,'�ص㶩��',2,3),��ú��λ,grouping(g.mingc) desc ,g.mingc";
						}
						rl.close();
					}catch(Exception e){
						e.printStackTrace();
					}finally{
						cn.Close();
					}
				}else {//�糧
					dianwmc=
						  /*"case when grouping(f.mingc)=1 then '�ܼ�' when grouping(j.mingc)=0 and \n"
						  + "grouping(f.mingc)=1  then j.mingc when grouping(f.mingc)=0  and grouping(dc.mingc)=1 then '&nbsp;&nbsp;'||f.mingc\n"
						  + "else '&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc end as danw,\n"*/
						  "  getLinkMingxTaiz('1',decode(grouping(dc.mingc),1,-1,max(dc.id))," 
						  + "decode(grouping(dc.mingc),1,'�ܼ�',dc.mingc)) as danw,\n"
						  + "case when grouping(g.mingc)=0 then '&nbsp;&nbsp;&nbsp;&nbsp;'||g.mingc else\n"
						  + "case when grouping(j.mingc)=0 then j.mingc else\n"
						  + "case when grouping(dc.mingc)=0 then 'С��' else '�ϼ�' end end  end ��ú��λ,\n"
						  + "decode(grouping(g.mingc),1,'',max(c1.mingc)) as faz,\n"
						  + "decode(grouping(g.mingc),1,'',max(c2.mingc)) as daoz,\n";
					groupby="group by  grouping sets (j.mingc,f.mingc,(f.mingc,j.mingc),\n"
						  + "(f.mingc,dc.mingc),(f.mingc,dc.mingc,j.mingc),(f.mingc,dc.mingc,j.mingc,g.mingc))\n";
					havingnot="having not grouping(dc.mingc)=1\n";
					orderby="order by grouping(f.mingc)+grouping(dc.mingc) desc,\n" +
							"case when grouping(f.mingc)+grouping(dc.mingc) > 0 then 0 else max(dc.xuh) end ,\n" + 
							"decode(��ú��λ,'�ϼ�',0,'С��',1,'�ص㶩��',2,3),��ú��λ,grouping(g.mingc) desc ,g.mingc";
				}
			}else if (getJihkjValue().getValue().equals("�ֿ�ֳ�")){
				titlename=titlename+"(�ֿ�ֳ�)";
				if(jib==1){//�����û�
					dianwmc="case when grouping(g.mingc)=1 then j.mingc else '&nbsp;&nbsp;'||g.mingc end as danw,\n"
						  + "decode(grouping(j.mingc)+grouping(g.mingc)+grouping(dc.mingc),3,'�ܼ�',2,'�ϼ�',1,'С��','&nbsp;&nbsp;'||dc.mingc) as mingc,\n"
						  + "decode(grouping(g.mingc),1,'',max(c1.mingc)) as faz,\n"
						  + "decode(grouping(g.mingc),1,'',max(c2.mingc)) as daoz,\n";
					groupby="group by  rollup (j.mingc,g.mingc,dc.mingc)\n";
					orderby="order by grouping(j.mingc) desc ,j.mingc desc,grouping(g.mingc) desc ,g.mingc,grouping(dc.mingc) desc ,dc.mingc desc,max(dc.xuh)";
				}else if (jib==2){//�ֹ�˾��ȼ�Ϲ�˾
					String ranlgs = "select id from diancxxb where shangjgsid= "+this.getTreeid();
					try{
						ResultSet rl = cn.getResultSet(ranlgs);
						if(rl.next()){//ȼ�Ϲ�˾
							dianwmc="case when grouping(g.mingc)=1 then j.mingc else '&nbsp;&nbsp;'||g.mingc end as danw,\n"
								  + "decode(grouping(j.mingc)+grouping(g.mingc)+grouping(dc.mingc),3,'�ܼ�',2,'�ϼ�',1,'С��','&nbsp;&nbsp;'||dc.mingc) as mingc,\n"
								  + "decode(grouping(g.mingc),1,'',max(c1.mingc)) as faz,\n"
								  + "decode(grouping(g.mingc),1,'',max(c2.mingc)) as daoz,\n";
							groupby="group by  rollup (j.mingc,g.mingc,dc.mingc)\n";
							orderby="order by grouping(j.mingc) desc ,j.mingc desc,grouping(g.mingc) desc ,g.mingc,grouping(dc.mingc) desc ,dc.mingc desc";
						}else{//�ֹ�˾
							dianwmc="case when grouping(g.mingc)=1 then j.mingc else '&nbsp;&nbsp;'||g.mingc end as danw,\n"
								  + "decode(grouping(j.mingc)+grouping(g.mingc)+grouping(dc.mingc),3,'�ܼ�',2,'�ϼ�',1,'С��','&nbsp;&nbsp;'||dc.mingc) as mingc,\n"
								  + "decode(grouping(g.mingc),1,'',max(c1.mingc)) as faz,\n"
								  + "decode(grouping(g.mingc),1,'',max(c2.mingc)) as daoz,\n";
							groupby="group by  rollup (j.mingc,g.mingc,dc.mingc)\n";
							orderby="order by grouping(j.mingc) desc ,j.mingc desc,grouping(g.mingc) desc ,g.mingc,grouping(dc.mingc) desc ,dc.mingc desc,max(dc.xuh)";
						}
						rl.close();
					}catch(Exception e){
						e.printStackTrace();
					}finally{
						cn.Close();
					}
				}else {//�糧
					dianwmc="case when grouping(g.mingc)=1 then j.mingc else '&nbsp;&nbsp;'||g.mingc end as danw,\n"
						  + "decode(grouping(j.mingc)+grouping(g.mingc)+grouping(dc.mingc),3,'�ܼ�',2,'�ϼ�',1,'С��','&nbsp;&nbsp;'||dc.mingc) as mingc,\n"
						  + "decode(grouping(g.mingc),1,'',max(c1.mingc)) as faz,\n"
						  + "decode(grouping(g.mingc),1,'',max(c2.mingc)) as daoz,\n";
					groupby="group by  rollup (j.mingc,g.mingc,dc.mingc)\n";
					orderby="order by grouping(j.mingc) desc ,j.mingc desc,grouping(g.mingc) desc ,g.mingc,grouping(dc.mingc) desc ,dc.mingc desc,max(dc.xuh)";
				}
			}else if (getJihkjValue().getValue().equals("�ֳ�")){
				titlename=titlename+"(�ֳ�)";
				if(jib==1){//�����û�
					dianwmc=
						  /*"case when grouping(f.mingc)=1 then '�ܼ�' when grouping(j.mingc)=0 and \n"
						  + "grouping(f.mingc)=1  then j.mingc when grouping(f.mingc)=0  and grouping(dc.mingc)=1 then '&nbsp;&nbsp;'||f.mingc\n"
						  + "else '&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc end as danw,\n"*/
						  " getLinkMingxTaiz('1',decode(grouping(dc.mingc),1,-1,max(dc.id))," 
						  +"decode(grouping(dc.mingc)+grouping(f.mingc),2,'�ܼ�',1,f.mingc,'&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc)) as danw,"
						  + "case when grouping(j.mingc)=0 then j.mingc else \n"
						  + "case when grouping(dc.mingc)=0 then 'С��' else '�ϼ�' end end  ��ú��λ,\n"
						  + "decode(grouping(f.mingc)+grouping(j.mingc),1,'',max(c1.mingc)) as faz,\n"
						  + "decode(grouping(f.mingc)+grouping(j.mingc),1,'',max(c2.mingc))  as daoz,\n";
					groupby="group by  grouping sets (j.mingc,f.mingc,(f.mingc,j.mingc),\n (f.mingc,dc.mingc),(f.mingc,dc.mingc,j.mingc))\n";
					orderby="order by grouping(f.mingc)+grouping(dc.mingc) desc,\n" +
							"case when grouping(f.mingc)+grouping(dc.mingc) > 0 then 0 else max(dc.xuh) end ,\n" + 
							"decode(��ú��λ,'�ϼ�',0,'С��',1,'�ص㶩��',2,3),��ú��λ";
				}else if (jib==2){//�ֹ�˾��ȼ�Ϲ�˾
					String ranlgs = "select id from diancxxb where shangjgsid= "+this.getTreeid();
					try{
						ResultSet rl = cn.getResultSet(ranlgs);
						if(rl.next()){//ȼ�Ϲ�˾
							dianwmc=
								  /*"case when grouping(vdc.rlgsmc)=1 then '�ܼ�' when grouping(j.mingc)=0 and \n"
								  + "grouping(vdc.rlgsmc)=1  then j.mingc when grouping(vdc.rlgsmc)=0  and grouping(dc.mingc)=1 then '&nbsp;&nbsp;'||vdc.rlgsmc\n"
								  + "else '&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc end as danw,\n"*/
								" getLinkMingxTaiz('1',decode(grouping(dc.mingc),1,-1,max(dc.id))," 
								  +"decode(grouping(dc.mingc)+grouping(vdc.rlgsmc),2,'�ܼ�',1,vdc.rlgsmc,'&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc)) as danw,"
								  + "case when grouping(j.mingc)=0 then j.mingc else \n"
								  + "case when grouping(dc.mingc)=0 then 'С��' else '�ϼ�' end end  ��ú��λ,\n"
								  + "decode(grouping(vdc.rlgsmc)+grouping(dc.mingc),1,'',max(c1.mingc)) as faz,\n"
								  + "decode(grouping(vdc.rlgsmc)+grouping(dc.mingc),1,'',max(c2.mingc))  as daoz,\n";
							groupby="group by  grouping sets (j.mingc,vdc.rlgsmc,(vdc.rlgsmc,j.mingc),\n (vdc.rlgsmc,dc.mingc),(vdc.rlgsmc,dc.mingc,j.mingc))\n";
							orderby="order by grouping(vdc.rlgsmc)+grouping(dc.mingc) desc,\n" +
									"case when grouping(vdc.rlgsmc)+grouping(dc.mingc) > 0 then 0 else max(dc.xuh) end ,\n" + 
									"decode(��ú��λ,'�ϼ�',0,'С��',1,'�ص㶩��',2,3),��ú��λ";
						}else{//�ֹ�˾
							dianwmc=
								  /*"case when grouping(f.mingc)=1 then '�ܼ�' when grouping(j.mingc)=0 and \n"
								  + "grouping(f.mingc)=1  then j.mingc when grouping(f.mingc)=0  and grouping(dc.mingc)=1 then '&nbsp;&nbsp;'||f.mingc\n"
								  + "else '&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc end as danw,\n"*/
								  "  getLinkMingxTaiz('1',decode(grouping(dc.mingc),1,-1,max(dc.id)),"
								  +" decode(grouping(f.mingc)+grouping(dc.mingc),2,'�ܼ�',"
								  +" 1,f.mingc,'&nbsp;&nbsp;'||dc.mingc)) as danw,\n"
								  + "case when grouping(j.mingc)=0 then j.mingc else \n"
								  + "case when grouping(dc.mingc)=0 then 'С��' else '�ϼ�' end end  ��ú��λ,\n"
								  + "decode(grouping(f.mingc)+grouping(dc.mingc),1,'',max(c1.mingc)) as faz,\n"
								  + "decode(grouping(f.mingc)+grouping(dc.mingc),1,'',max(c2.mingc))  as daoz,\n";
							groupby="group by  grouping sets (j.mingc,f.mingc,(f.mingc,j.mingc),\n (f.mingc,dc.mingc),(f.mingc,dc.mingc,j.mingc))\n";
							havingnot="having not grouping(f.mingc)=1\n";
							orderby="order by grouping(f.mingc)+grouping(dc.mingc) desc,\n" +
									"case when grouping(f.mingc)+grouping(dc.mingc) > 0 then 0 else max(dc.xuh) end ,\n" + 
									"decode(��ú��λ,'�ϼ�',0,'С��',1,'�ص㶩��',2,3),��ú��λ";
						}
						rl.close();
					}catch(Exception e){
						e.printStackTrace();
					}finally{
						cn.Close();
					}
				}else {//�糧
					dianwmc=
						  /*"case when grouping(f.mingc)=1 then '�ܼ�' when grouping(j.mingc)=0 and \n"
						  + "grouping(f.mingc)=1  then j.mingc when grouping(f.mingc)=0  and grouping(dc.mingc)=1 then '&nbsp;&nbsp;'||f.mingc\n"
						  + "else '&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc end as danw,\n"*/
						  "  getLinkMingxTaiz('1',decode(grouping(dc.mingc),1,-1,max(dc.id))," 
						  + "decode(grouping(dc.mingc),1,'�ܼ�',dc.mingc)) as danw,\n"
						  + "case when grouping(j.mingc)=0 then j.mingc else \n"
						  + "case when grouping(dc.mingc)=0 then 'С��' else '�ϼ�' end end  ��ú��λ,\n"
						  + "decode(grouping(f.mingc)+grouping(dc.mingc),1,'',max(c1.mingc)) as faz,\n"
						  + "decode(grouping(f.mingc)+grouping(dc.mingc),1,'',max(c2.mingc))  as daoz,\n";
					groupby="group by  grouping sets (j.mingc,f.mingc,(f.mingc,j.mingc),\n (f.mingc,dc.mingc),(f.mingc,dc.mingc,j.mingc))\n";
					havingnot="having not grouping(dc.mingc)=1 \n";
					orderby="order by grouping(f.mingc)+grouping(dc.mingc) desc,\n" +
							"case when grouping(f.mingc)+grouping(dc.mingc) > 0 then 0 else max(dc.xuh) end ,\n" + 
							"decode(��ú��λ,'�ϼ�',0,'С��',1,'�ص㶩��',2,3),��ú��λ";
				}
			}else if (getJihkjValue().getValue().equals("�ֿ�")){
				titlename=titlename+"(�ֿ�)";
				if(jib==1){//�����û�
					dianwmc="decode(grouping(g.mingc)+grouping(j.mingc),2,'�ܼ�',1,j.mingc,'&nbsp;&nbsp;&nbsp;&nbsp;'||g.mingc) as danw,\n"
						  + "decode(grouping(g.mingc),1,'',max(c1.mingc)) as faz,\n"
						  + "decode(grouping(g.mingc),1,'',max(c2.mingc))  as daoz,\n";
					groupby="group by  rollup (j.mingc,g.mingc)\n";
					orderby="order by grouping(j.mingc) desc ,j.mingc desc,grouping(g.mingc) desc ,g.mingc desc";
				}else if (jib==2){//�ֹ�˾��ȼ�Ϲ�˾
					String ranlgs = "select id from diancxxb where shangjgsid= "+this.getTreeid();
					try{
						ResultSet rl = cn.getResultSet(ranlgs);
						if(rl.next()){//ȼ�Ϲ�˾
							dianwmc="decode(grouping(g.mingc)+grouping(j.mingc),2,'�ܼ�',1,j.mingc,'&nbsp;&nbsp;&nbsp;&nbsp;'||g.mingc) as danw,\n"
								  + "decode(grouping(g.mingc),1,'',max(c1.mingc)) as faz,\n"
								  + "decode(grouping(g.mingc),1,'',max(c2.mingc))  as daoz,\n";
							groupby="group by  rollup (j.mingc,g.mingc)\n";
							orderby="order by grouping(j.mingc) desc ,j.mingc desc,grouping(g.mingc) desc ,g.mingc desc";
						}else{//�ֹ�˾
							dianwmc="decode(grouping(g.mingc)+grouping(j.mingc),2,'�ܼ�',1,j.mingc,'&nbsp;&nbsp;&nbsp;&nbsp;'||g.mingc) as danw,\n"
								  + "decode(grouping(g.mingc),1,'',max(c1.mingc)) as faz,\n"
								  + "decode(grouping(g.mingc),1,'',max(c2.mingc))  as daoz,\n";
							groupby="group by  rollup (j.mingc,g.mingc)\n";
							orderby="order by grouping(j.mingc) desc ,j.mingc desc,grouping(g.mingc) desc ,g.mingc desc";
						}
						rl.close();
					}catch(Exception e){
						e.printStackTrace();
					}finally{
						cn.Close();
					}
				}else {//�糧
					dianwmc="decode(grouping(g.mingc)+grouping(j.mingc),2,'�ܼ�',1,j.mingc,'&nbsp;&nbsp;&nbsp;&nbsp;'||g.mingc) as danw,\n"
						  + "decode(grouping(g.mingc),1,'',max(c1.mingc)) as faz,\n"
						  + "decode(grouping(g.mingc),1,'',max(c2.mingc))  as daoz,\n";
					groupby="group by  rollup (j.mingc,g.mingc)\n";
					orderby="order by grouping(j.mingc) desc ,j.mingc desc,grouping(g.mingc) desc ,g.mingc desc";
				}
			}
		}else{
			return "";
		}
		
		//*********************************************��SQL*********************************************//
		strSQL.append(" select \n");
		//�Զ��������
		strSQL.append(dianwmc);
		//����Ĭ������
		strSQL.append( " sum(zj.nianhj)*10000 as zj,\n");
		strSQL.append( " decode(sum(jh.leijjh),0,0,Round(sum(shid.shijlm)*100/(sum(jh.leijjh)*10000),2)) as daohl,\n");
		strSQL.append( " sum(cg.cgl) as cgl,\n");
		strSQL.append( " decode(sum(cg.cgl),0,0,Round(sum(cg.rez*cg.cgl)/sum(cg.cgl),2)) as rez,\n");//��ֵ
		strSQL.append( " decode(sum(cg.cgl),0,0,Round(sum(cg.huiff*cg.cgl)/sum(cg.cgl),2)) as huiff,\n");//�ӷ���
		strSQL.append( " decode(sum(cg.cgl),0,0,Round(sum(cg.liuf*cg.cgl)/sum(cg.cgl),2)) as liuf,\n");//���
		strSQL.append( " decode(sum(cg.cgl),0,0,Round(sum(cg.chebj*cg.cgl)/sum(cg.cgl),2)) as chebj,\n");
		strSQL.append( " decode(sum(cg.cgl),0,0,Round(sum(cg.yunf*cg.cgl)/sum(cg.cgl),2)) as yunf,\n");
		strSQL.append( " decode(sum(cg.cgl),0,0,Round(sum(cg.zaf*cg.cgl)/sum(cg.cgl),2)) as zaf,\n");
		strSQL.append( " decode(sum(cg.cgl),0,0,Round(sum((cg.chebj+cg.yunf+cg.zaf)*cg.cgl)/sum(cg.cgl),2)) as daocj,\n");
		//strSQL.append( "decode(sum(cg.cgl),0,0,Round(sum(cg.biaomdj*cg.cgl)/sum(cg.cgl),2)) as biaomdj\n);"
		strSQL.append( " Round(decode(sum(cg.rez*cg.cgl),0,0,Round((sum((cg.chebj+cg.yunf+cg.zaf)*cg.cgl)/sum(cg.cgl)),2)*29.271/");
		strSQL.append( " Round((sum(cg.rez*cg.cgl)/sum(cg.cgl)),2)),2) as biaomdj \n");
		
		strSQL.append( " ,Round(decode(sum(cg.rez*cg.cgl),0,0,Round((sum((cg.chebj/(1+0.17)+cg.yunf*(1-0.07)+cg.zaf)*cg.cgl)/sum(cg.cgl)),2)*29.271/");
		strSQL.append( " Round((sum(cg.rez*cg.cgl)/sum(cg.cgl)),2)),2) as buhs_biaomdj \n");
		//�������ı�
		strSQL.append( " from \n ");
		strSQL.append(str_cg.toString()).append(str_zj.toString()).append(str_shid.toString()).append(str_jh.toString());
		//���ã�where,group by ,order by 
		strSQL.append( " diancxxb dc,gongysb g,jihkjb j,chezxxb c1,chezxxb c2,vwfengs f,vwdianc vdc\n");
		strSQL.append( " where cg.diancxxb_id=zj.diancxxb_id(+)\n");
		strSQL.append( " and   cg.diancxxb_id=shid.diancxxb_id(+)\n");
		strSQL.append( " and   cg.diancxxb_id=jh.diancxxb_id(+)\n");
		strSQL.append( " and   cg.diancxxb_id=dc.id\n");
		strSQL.append( " and   cg.gongysb_id=zj.gongysb_id(+)\n");
		strSQL.append( " and   cg.gongysb_id=shid.diancxxb_id(+)\n");
		strSQL.append( " and   cg.gongysb_id=jh.gongysb_id(+)\n");
		strSQL.append( " and   cg.gongysb_id=g.id\n");
		strSQL.append( " and   cg.jihkjb_id=zj.jihkjb_id(+)\n");
		strSQL.append( " and   cg.jihkjb_id=shid.jihkjb_id(+)\n");
		strSQL.append( " and   cg.jihkjb_id=jh.jihkjb_id(+)\n");
		strSQL.append( " and   cg.jihkjb_id=j.id\n");
		strSQL.append( " and   cg.faz_id=c1.id\n");
		strSQL.append( " and   cg.daoz_id=c2.id\n");
		strSQL.append( " and   dc.id=vdc.id \n");
		strSQL.append( "  "+strGongsID+"\n");
		strSQL.append( " and   dc.fuid=f.id\n");
		strSQL.append( " and   j.id!=3\n");
		strSQL.append(groupby);
		strSQL.append(havingnot);
		strSQL.append(orderby);
		
		//���ñ���ı�ͷ��
		if(getJihkjValue().getValue().equals("�ֳ��ֿ�")){
			ArrHeader=new String[2][16];
			ArrHeader[0]=new String[]{"��λ","��Ӧ��λ","��վ(��)","��վ(��)","��ȶ�<br>����","�ۼƵ�<br>����%","�ƻ���<br>������","����","����","����","�۸�","�۸�","�۸�","�۸�","�۸�","�۸�"};
		    ArrHeader[1]=new String[] {"��λ","��Ӧ��λ","��վ(��)","��վ(��)","��ȶ�<br>����","�ۼƵ�<br>����%","�ƻ���<br>������","��ֵ","�ӷ���","���","ú��","�˷�","�ӷ�","������","��ú����","����˰��ú����"};
			ArrWidth =new int[] {150,150,60,60,60,60,60,50,50,50,50,50,50,50,50,50};
		    arrFormat = new String[] { "", "","","", "0", "0.00", "0","0.00", "0.00", "0.00","0.00", "0.00","0.00","0.00","0.00"};
		}else if(getJihkjValue().getValue().equals("�ֿ�ֳ�")){
			ArrHeader=new String[2][16];
			ArrHeader[0]=new String[]{"��Ӧ��λ","��λ","��վ(��)","��վ(��)","��ȶ�<br>����","�ۼƵ�<br>����%","�ƻ���<br>������","����","����","����","�۸�","�۸�","�۸�","�۸�","�۸�","�۸�","����˰��ú����"};
		    ArrHeader[1]=new String[] {"��Ӧ��λ","��λ","��վ(��)","��վ(��)","��ȶ�<br>����","�ۼƵ�<br>����%","�ƻ���<br>������","��ֵ","�ӷ���","���","ú��","�˷�","�ӷ�","������","��ú����","����˰��ú����"};
			ArrWidth =new int[] {150,150,60,60,60,60,60,50,50,50,50,50,50,50,50,50};
		    arrFormat = new String[] { "", "","","", "0", "0.00", "0","0.00", "0.00", "0.00","0.00", "0.00","0.00","0.00","0.00"};
		}else if(getJihkjValue().getValue().equals("�ֿ�")){
			ArrHeader=new String[2][15];
			ArrHeader[0]=new String[]{"��Ӧ��λ","��վ(��)","��վ(��)","��ȶ�<br>����","�ۼƵ�<br>����%","�ƻ���<br>������","����","����","����","�۸�","�۸�","�۸�","�۸�","�۸�","�۸�"};
		    ArrHeader[1]=new String[] {"��Ӧ��λ","��վ(��)","��վ(��)","��ȶ�<br>����","�ۼƵ�<br>����%","�ƻ���<br>������","��ֵ","�ӷ���","���","ú��","�˷�","�ӷ�","������","��ú����","����˰��ú����"};
			ArrWidth =new int[] {150,60,60,60,60,60,50,50,50,50,50,50,50,50,50};
		    arrFormat = new String[] { "","","", "0", "0.00", "0","0.00", "0.00", "0.00","0.00", "0.00","0.00","0.00","0.00"};
		}else if(getJihkjValue().getValue().equals("�ֳ�")){
			ArrHeader=new String[2][16];
			ArrHeader[0]=new String[]{"��λ","�ƻ��ھ�","��վ(��)","��վ(��)","��ȶ�<br>����","�ۼƵ�<br>����%","�ƻ���<br>������","����","����","����","�۸�","�۸�","�۸�","�۸�","�۸�","�۸�"};
		    ArrHeader[1]=new String[] {"��λ","�ƻ��ھ�","��վ(��)","��վ(��)","��ȶ�<br>����","�ۼƵ�<br>����%","�ƻ���<br>������","��ֵ","�ӷ���","���","ú��","�˷�","�ӷ�","������","��ú����","����˰��ú����"};
			ArrWidth =new int[] {150,80,60,60,60,60,60,50,50,50,50,50,50,50,50,50};
		    arrFormat = new String[] { "","","","", "0", "0.00", "0","0.00", "0.00", "0.00","0.00", "0.00","0.00","0.00","0.00"};
		}
		
		ResultSet rs = cn.getResultSet(strSQL.toString());
		// ����
		rt.setBody(new Table(rs,2, 0, 4));
		rt.setTitle(intyear+"��"+intMonth+"��"+titlename, ArrWidth);
		rt.setDefaultTitle(1, 2, "���λ:"+((Visit) getPage().getVisit()).getDiancmc(), Table.ALIGN_LEFT);
		rt.setDefaultTitle(5, 3, "�����:"+intyear+"��"+intMonth+"��", Table.ALIGN_CENTER);
		rt.setDefaultTitle(9,3, "��λ:�֡�Ԫ/�֡�MJ/Kg", Table.ALIGN_RIGHT);
		rt.setDefaultTitle(13, 2, "cpi����09��", Table.ALIGN_RIGHT);
		
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(18);
		rt.body.setHeaderData(ArrHeader);// ��ͷ����
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		rt.body.ShowZero = true;
		if(rt.body.getRows()>2){
			rt.body.setCellAlign(3, 1, Table.ALIGN_CENTER);
		}
		if(jib==3){
			 rt.body.setColAlign(2, Table.ALIGN_LEFT);
		     rt.body.setColAlign(3, Table.ALIGN_LEFT);
		}
		if(jib==2){
			if(getJihkjValue().getValue().equals("�ֳ��ֿ�")){
				rt.body.setCellValue(3, 2, "�ܼ�");
			}
			rt.body.setColAlign(2, Table.ALIGN_LEFT);
		    rt.body.setColAlign(3, Table.ALIGN_LEFT);
		    rt.body.setColAlign(4, Table.ALIGN_LEFT);
		}
		rt.body.setColFormat(arrFormat);
		//ҳ�� 
		
		 rt.createDefautlFooter(ArrWidth);
		 rt.setDefautlFooter(1,3,"��ӡ����:"+DateUtil.FormatDate(new Date()),Table.ALIGN_LEFT);
		 rt.setDefautlFooter(7,2,"���:",Table.ALIGN_LEFT);
		 rt.setDefautlFooter(11,2,"�Ʊ�:",Table.ALIGN_RIGHT);
		
		_CurrentPage=1;
		_AllPages=rt.body.getPages();
		if (_AllPages==0){
			_CurrentPage=0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}
	
//	 ��ͬ���ֳ��ֿ�ֿ�ֳ�ͳ�Ʊ���
	private String getZhiltj() {
		JDBCcon con = new JDBCcon();
		
		String Start_riq="";
		String riq="";
//		�õ�ʱ��������ֵ
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
		
		//���·���1��ʱ����ʾ01,
		String StrMonth = "";
		if (intMonth < 10) {
			StrMonth = "0" + intMonth;
		} else {
			StrMonth = "" + intMonth;
		}
		
		long sysmonth = DateUtil.getMonth(new Date());//�õ�ϵͳ��ǰ�·�
		
		long sysdate=DateUtil.getDay(new Date());//�õ�ϵͳ��ǰ��
		
		if (intMonth==sysmonth){//�ж�ѡ����·��Ƿ����ϵͳ�ĵ�ǰ�·�
			
			riq=intyear+"-"+StrMonth+"-"+sysdate;
			
			if (sysdate==1){//�жϵ�ǰ���Ƿ���1��,�����1��ȡ�ϸ��µ����һ�졣
				Start_riq="to_date('"+riq+"','yyyy-mm-dd')-1";
			}else{//�������1�ţ�ʱ��ȡ���µ�һ�ŵ���ǰ��Ϊֹ��
				Start_riq="to_date('"+riq+"','yyyy-mm-dd')";
			}
		}else{//ʱ��ȡѡ����
			riq=intyear+"-"+StrMonth+"-01";
			Start_riq="to_date('"+riq+"','yyyy-mm-dd')";
		}

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
			//"   drLAIML,drcbl,drrezdk,drmeij,dryunf+drzaf as yunzf,round_new(decode(drrez,0,0,(drmeij/1.17+dryunf*0.93+drzaf)*29.271/drrez),2) as drbmj,\n" + 
			"   ljLAIML,ljcbl,round(ljLAIML/(jihl/to_number(to_char(last_day("+Start_riq+"),'dd'))\n" + 
			"   *to_number(to_char("+Start_riq+",'dd')))*100,2) as daohl,\n" + 
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
//			"   --�������\n" + 
//			"   SUM(wcdr.LAIML) AS drLAIML,sum(wcdr.cbsl) as drcbl,\n" + 
//			"   round_new(round_new(decode(sum(nvl(wcdr.cbsl,0)),0,0,sum(nvl(wcdr.cbsl*wcdr.farl,0))/sum(nvl(wcdr.cbsl,0))),2)*1000/4.1816,0) as drrezdk,\n" + 
//			"   round_new(decode(sum(nvl(wcdr.cbsl,0)),0,0,sum(nvl(wcdr.cbsl*wcdr.farl,0))/sum(nvl(wcdr.cbsl,0))),2) as drrez,\n" + 
//			"   round_new(decode(sum(nvl(wcdr.cbsl,0)),0,0,sum(nvl(wcdr.cbsl*wcdr.meij,0))/sum(nvl(wcdr.cbsl,0))),2) as   drmeij,\n" + 
//			"   round_new(decode(sum(nvl(wcdr.cbsl,0)),0,0,sum(nvl(wcdr.cbsl*wcdr.yunf,0))/sum(nvl(wcdr.cbsl,0))),2) as   dryunf,\n" + 
//			"   round_new(decode(sum(nvl(wcdr.cbsl,0)),0,0,sum(nvl(wcdr.cbsl*wcdr.zaf,00))/sum(nvl(wcdr.cbsl,0))),2) as   drzaf  ,\n" + 
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
			"        and f.daohrq>=first_day("+Start_riq+") and f.daohrq<="+Start_riq+"\n" + 
			"   union select dc.mingc, dc.id,dc.xuh,dc.fgsmc,dc.fgsxh, gy.dqmc gongys, gy.dqid\n" + 
			"        from yuecgjhb jh,vwdianc dc,vwgongys gy\n" + 
			"        where jh.diancxxb_id=dc.id and jh.gongysb_id=gy.id\n" +   strGongsID+" \n"+
			"        and jh.riq>=first_day("+Start_riq+") and jh.riq<="+Start_riq+"\n" + 
			"        and jh.yuejhcgl<>0 ) dc,   --��ͷ����������\n" + 
			/*"   (select diancid,dqid,SUM(LAIML) AS LAIML,sum(cbsl) as cbsl,\n" + 
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
			"        and f.daohrq="+Start_riq+"\n" + 
			"     group by (gy.dqid,f.lieid,dc.id)) dd\n" + 
			"     group by diancid,dqid)  wcdr,--�ճɱ�\n" + */
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
			"        and f.daohrq>=first_day("+Start_riq+") and f.daohrq<="+Start_riq+"\n" + 
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
			"        and jh.riq>=first_day("+Start_riq+")  and jh.riq<="+Start_riq+"\n" + 
			"        and jh.yuejhcgl<>0\n" + 
			"    group by dc.id,gy.dqid) jh\n" + 
			"where dc.id=wclj.diancid(+)\n" + 
			"     and dc.dqid=wclj.dqid(+)\n" + 
			"     and dc.id=jh.diancid(+)\n" + 
			"     and dc.dqid=jh.dqid(+)\n" + 
			groupby+ordeby;
//			"group by rollup(dc.mingc,gongys)\n" + 
//			"order by grouping(dc.mingc) desc,max(dc.xuh),grouping(gongys) desc,max(gongysxh))";

			

		System.out.println(sbsql);
		ResultSet rs = con.getResultSet(sbsql);
		Report rt = new Report();
		
		String ArrHeader[][]=new String[2][14];
		ArrHeader[0]=new String[] {"��λ","������λ","����Ԥ��","����Ԥ��","����Ԥ��","����","����","����","����","����","����","����","����","����"};
		ArrHeader[1]=new String[] {"��λ","������λ","�ɹ���","��ֵ","��ú����","�ɹ���","�ɱ���","���ȵ�����","��ֵ","��Ԥ��ֵ��ֵ","ú��","�˼�","��ú����","��Ԥ��ֵ��ֵ"};

		
		int ArrWidth[]=new int[] {100,100,60,40,50,50,60,40,40,50,40,40,50,60};


		Table bt=new Table(rs,2,0,2);
		rt.setBody(bt);
//	
		
		bt.setColAlign(2, Table.ALIGN_CENTER);
		if(rt.body.getRows()>3){
			rt.body.setCellAlign(2, 1,Table.ALIGN_CENTER);
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
		
		for (int i=3;i<rows;i++){
			if (rt.body.getCellValue(i, 2).equals("�ϼ�")){
				for (int j=2;j<cols+1;j++){
					rt.body.getCell(i, j).backColor="#F2F2F2";
				}
			}
			if (!rt.body.getCell(i, 6).value.equals(rt.body.getCell(i, 7).value)){
				rt.body.getCell(i, 7).foreColor="blue";
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
		rt.setDefaultTitle(1, 3, "�Ʊ�λ:" +((Visit) getPage().getVisit()).getDiancqc(),Table.ALIGN_LEFT);
		rt.setDefaultTitle(5, 4,"����:"+intyear+"��"+intMonth+"��",Table.ALIGN_CENTER);
		rt.setDefaultTitle(10, 5, "��λ:��,Ԫ/��,kcal/kg,%" ,Table.ALIGN_RIGHT);
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 2, "��ӡ����:"+FormatDate(DateUtil.getDate(DateUtil.FormatDate(new Date()))),Table.ALIGN_LEFT);
		rt.setDefautlFooter(5, 4, "��ˣ�", Table.ALIGN_LEFT);
		rt.setDefautlFooter(10, 5, "�Ʊ�", Table.ALIGN_LEFT);
		// ����ҳ��
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
		return rt.getAllPagesHtml();

	}
	
	private String getLaimlField(){
		return SysConstant.LaimField;
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
		fahdwList.add(new IDropDownBean(0,"ú̿�ɹ��ƻ���"));
		fahdwList.add(new IDropDownBean(1,"ú̿�걨�ƻ���"));
		fahdwList.add(new IDropDownBean(2,"�е�����ճɱ�����"));

		_IBaoblxModel = new IDropDownModel(fahdwList);
		
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.Close();
		}
		return _IBaoblxModel;
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
			for (int i = 0; i < _YuefModel.getOptionCount(); i++) {
				Object obj = _YuefModel.getOption(i);
				if (DateUtil.getMonth(new Date()) == ((IDropDownBean) obj)
						.getId()) {
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
	
	private void getToolbars(){
		Toolbar tb1 = new Toolbar("tbdiv");
		tb1.addText(new ToolbarText("���:"));
		ComboBox nianf = new ComboBox();
		nianf.setTransform("NIANF");
		nianf.setWidth(60);
		nianf.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(nianf);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("�·�:"));
		ComboBox yuef = new ComboBox();
		yuef.setTransform("YUEF");
		yuef.setWidth(60);
		yuef.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(yuef);
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
		
		tb1.addText(new ToolbarText("����ʽ:"));
		ComboBox cb = new ComboBox();
		cb.setTransform("BaoblxDropDown");
		cb.setListeners("select:function(){document.Form0.submit();}");
		cb.setId("Baoblx");
		cb.setWidth(130);
		tb1.addField(cb);
		tb1.addText(new ToolbarText("-"));
		
		if (!getBaoblxValue().getValue().equals("�е�����ճɱ�����")){
			tb1.addText(new ToolbarText("ͳ�ƿھ�:"));
			ComboBox cb1 = new ComboBox();
			cb1.setTransform("JihkjDropDown");
			cb1.setListeners("select:function(){document.Form0.submit();}");
			cb1.setId("Jihkj");
			cb1.setWidth(120);
			tb1.addField(cb1);
			tb1.addText(new ToolbarText("-"));
			
		}
		
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
			this.setBaoblxValue(null);
			this.getIBaoblxModels();
			this.setJihkjValue(null);
			this.getIJihkjModels();
			visit.setDefaultTree(null);
			
			this.setTreeid(null);
			this.getTreeid();
			visit.setString3(null);
			//visit.setString4(null);
			setNianfValue(null);
			setYuefValue(null);
			getNianfModels();
			getYuefModels();
		}
		if(_Baoblxchange){
			_Baoblxchange=false;
			getPrintTable();
		}
		if(_Jihkjchange){
			_Jihkjchange=false;
			getPrintTable();
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
	
//	 ��������
	public boolean _Jihkjchange = false;

	private IDropDownBean _JihkjValue;

	public IDropDownBean getJihkjValue() {
		if (_JihkjValue == null) {
			_JihkjValue = (IDropDownBean) getIJihkjModels().getOption(0);
		}
		return _JihkjValue;
	}

	public void setJihkjValue(IDropDownBean Value) {
		long id = -2;
		if (_JihkjValue != null) {
			id = _JihkjValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_Jihkjchange = true;
			} else {
				_Jihkjchange = false;
			}
		}
		_JihkjValue = Value;
	}

	private IPropertySelectionModel _IJihkjModel;

	public void setIJihkjModel(IPropertySelectionModel value) {
		_IJihkjModel = value;
	}

	public IPropertySelectionModel getIJihkjModel() {
		if (_IJihkjModel == null) {
			getIJihkjModels();
		}
		return _IJihkjModel;
	}

	public IPropertySelectionModel getIJihkjModels() {
		JDBCcon con = new JDBCcon();
		try {
			List fahdwList = new ArrayList();
			fahdwList.add(new IDropDownBean(0, "�ֳ�"));
			fahdwList.add(new IDropDownBean(1, "�ֿ�"));
			fahdwList.add(new IDropDownBean(2, "�ֳ��ֿ�"));
			fahdwList.add(new IDropDownBean(3, "�ֿ�ֳ�"));
			_IJihkjModel = new IDropDownModel(fahdwList);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		return _IJihkjModel;
	}
	
}
