////2008-08-05 chh 
//�޸����� ��ȼ�Ϲ�˾���û����Բ鿴������
//		   ��ϸ������ʾ

package com.zhiren.jt.zdt.jihgl.meitdhxqzbreport;
import org.apache.tapestry.html.BasePage;
import java.sql.ResultSet;
import java.util.Date;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import com.zhiren.common.DateUtil;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class MeitdhxqmxReport extends BasePage {
	private String leix="";
	
	public boolean getRaw() {
		return true;
	}

	public void submit(IRequestCycle cycle) {  
		
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
		
		Visit visit = (Visit) getPage().getVisit();
		
		if (!blnIsBegin) {
			return "";
		}
		blnIsBegin = false;
		
		int intLen=0;
		String lx=getLeix();
		intLen=lx.indexOf(",");
		String diancid="";
		//String leix="";
		if (intLen>0){
			String [] pa=lx.split(",");
			if (pa.length==2){
				//leix=pa[0];//����
				diancid="" +pa[1];//�糧ID
			}else{
				diancid="-1";
			}
		}else{
			return "";
		}
		
		String jizzt="";//����״̬
		String title="";//��ͷ����
		if (visit.getString4()!=null){
			if (visit.getString4().equals("")){
				jizzt="";
			}else{
				if (visit.getString4().equals("0")){
					jizzt=" and yx.jizzt=0 ";
					title="���ۻ���";
				}else if (visit.getString4().equals("1")){
					jizzt=" and yx.jizzt=1 ";
					title="��������";
				}
			}
		}
		
		long nianf=Integer.parseInt(visit.getString3());
		return getMingx(diancid,nianf,jizzt,title);
	}

	public String getLeix(){
		Visit visit = (Visit) getPage().getVisit();
		if(!visit.getString9().equals("")) {
			return visit.getString9();
        } 
		return ""; 
	}

//	private String OraDate(Date _date){
//		if (_date == null) {
//			return "to_date('"+DateUtil.Formatdate("yyyy-MM-dd", new Date())+"','yyyy-mm-dd')";
//		}
//		return "to_date('"+DateUtil.Formatdate("yyyy-MM-dd", _date)+"','yyyy-mm-dd')";
//	}
	
	private String getMingx(String diancid,long nianf,String jizzt,String title){//�糧ID����ݣ����״̬
		
		long intyear;
		long lastyear;
		long nextyear;
		if (nianf == 0) {
			intyear = DateUtil.getYear(new Date())-1;
			lastyear=intyear-1;
			nextyear=DateUtil.getYear(new Date());
		} else {
			intyear = nianf-1;
			lastyear=intyear-1;
			nextyear=nianf;
		}
		
		Visit visit = (Visit) getPage().getVisit();
		String jizzt1=jizzt;
		
		if (visit.getString4()=="1"){
			jizzt="  and yx.jizzt=2  ";//����������z1,z2��ȡ��
		}
		
		_CurrentPage=1;
		_AllPages=1;
		JDBCcon cn = new JDBCcon();
		Date datEnd=((Visit)getPage().getVisit()).getDate2();
		
		String date=DateUtil.FormatDate(DateUtil.getFirstDayOfMonth(datEnd))+"��"+DateUtil.FormatDate(datEnd);
		
//		String riq1=FormatDate(_BeginriqValue);
		//�����ͷ����
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		String titlename=title+"��Ⱥ��������ܱ�";
		/*int iFixedRows=0;//�̶��к�
		int iCol=0;//����*/		
		//��������
		titlename=titlename+"";
		
		StringBuffer sbsql = new StringBuffer();
		
		sbsql.append("select lanc, \n");//decode(jizlx,null,'�ִ����',jizlx) as jizlx,
		sbsql.append("decode(mingc,null,decode(lanc,1,'������',2,'�����׼ú��',3,'�ۺϳ��õ���',4,'�����׼ú��',5,'������',6,'���ȱ�׼ú��', \n");
		sbsql.append("7,'�������ñ�ú��',8,'�������ñ�ú��',9,'���ñ�ú��',10,'�����ȼ����',11,'�ͷ�����',  \n");
		sbsql.append("12,'����ú�۱�ú��',13,'��¯��ֵ',14,'����ԭú��',15,'������',16,'����',17,'�ڳ����',18,'��δ���',19,'��������',mingc),mingc) as mingc,zz.danw,  \n");
		sbsql.append("sum(lastyear) as lastyear,sum(year) as year,sum(nextyear) as nextyear \n");
		//sbsql.append("decode(3,3,decode(zz.zhuangt,0,'δ�ύ','���ύ'),2,decode(zz.zhuangt,1,'δ���','�����'),3,decode(zz.zhuangt,2,'δ���','�����')) as zhuangt \n");
		sbsql.append("from (  \n");
		sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as year,z3.zhi as nextyear,'��ǧ��ʱ' as danw from \n");
		sbsql.append("(select 1 as lanc,'������' as mingc,'��ǧ��ʱ' dun from dual) t,\n");
		sbsql.append("(select 1 as lanc,sum(yx.fadl) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt+"  ) z1, \n");
		sbsql.append("(select 1 as lanc,sum(yx.fadl) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt+"   ) z2, \n");
		sbsql.append("(select 1 as lanc,sum(yx.fadl) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt1+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
		sbsql.append("union \n");
		sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as year,z3.zhi as nextyear,'��ǧ��ʱ' as danw from \n");
		sbsql.append("(select 2 as lanc,'�����׼ú��' as mingc,'��/ǧ��ʱ' dun from dual) t,\n");
		sbsql.append("(select 2 as lanc,sum(yx.gongdbzmh) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt+"   ) z1, \n");
		sbsql.append("(select 2 as lanc,sum(yx.gongdbzmh) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt+"   ) z2, \n");
		sbsql.append("(select 2 as lanc,sum(yx.gongdbzmh) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt1+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
		sbsql.append("union \n");
		sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as year,z3.zhi as nextyear,'��/ǧ��ʱ' as danw from \n");
		sbsql.append("(select 3 as lanc,'�ۺϳ��õ���' as mingc,'��/ǧ��ʱ' dun from dual) t,\n");
		sbsql.append("(select 3 as lanc,round_new(decode(sum(decode(yx.zonghcydl,0,0,yx.fadl)),0,0,sum(yx.zonghcydl*yx.fadl)/sum(decode(yx.zonghcydl,0,0,yx.fadl))),2) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt+"   ) z1, \n");
		sbsql.append("(select 3 as lanc,round_new(decode(sum(decode(yx.zonghcydl,0,0,yx.fadl)),0,0,sum(yx.zonghcydl*yx.fadl)/sum(decode(yx.zonghcydl,0,0,yx.fadl))),2) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt+"   ) z2, \n");
		sbsql.append("(select 3 as lanc,round_new(decode(sum(decode(yx.zonghcydl,0,0,yx.fadl)),0,0,sum(yx.zonghcydl*yx.fadl)/sum(decode(yx.zonghcydl,0,0,yx.fadl))),2) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt1+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
		sbsql.append("union \n");
		sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as year,z3.zhi as nextyear,'��/ǧ��ʱ' as danw from \n");
		sbsql.append("(select 4 as lanc,'�����׼ú��' as mingc,'��/ǧ��ʱ' dun from dual) t,\n");
		sbsql.append("(select 4 as lanc,round_new(decode(sum(decode(yx.fadbhm,0,0,yx.fadl)),0,0,sum(yx.fadbhm*yx.fadl)/sum(decode(yx.fadbhm,0,0,yx.fadl))),2) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt+"   ) z1, \n");
		sbsql.append("(select 4 as lanc,round_new(decode(sum(decode(yx.fadbhm,0,0,yx.fadl)),0,0,sum(yx.fadbhm*yx.fadl)/sum(decode(yx.fadbhm,0,0,yx.fadl))),2) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt+"   ) z2, \n");
		sbsql.append("(select 4 as lanc,round_new(decode(sum(decode(yx.fadbhm,0,0,yx.fadl)),0,0,sum(yx.fadbhm*yx.fadl)/sum(decode(yx.fadbhm,0,0,yx.fadl))),2) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt1+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
		sbsql.append("union \n");
		
		sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as year,z3.zhi as nextyear,'�򼪽�' as danw from \n");
		sbsql.append("(select 5 as lanc,'������' as mingc,'�򼪽�' dun from dual) t,\n");
		sbsql.append("(select 5 as lanc,sum(yx.gongrl) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt+"   ) z1, \n");
		sbsql.append("(select 5 as lanc,sum(yx.gongrl) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt+"   ) z2, \n");
		sbsql.append("(select 5 as lanc,sum(yx.gongrl) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt1+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
		sbsql.append("union \n");
		sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as year,z3.zhi as nextyear,'ǧ��/����' as danw from \n");
		sbsql.append("(select 6 as lanc,'���ȱ�׼ú��' as mingc,'ǧ��/����' dun from dual) t,\n");
		sbsql.append("(select 6 as lanc,round_new(decode(sum(decode(yx.gongrbzmh,0,0,yx.gongrl)),0,0,sum(yx.gongrbzmh*yx.gongrl)/sum(decode(yx.gongrbzmh,0,0,yx.gongrl))),2) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt+"   ) z1, \n");
		sbsql.append("(select 6 as lanc,round_new(decode(sum(decode(yx.gongrbzmh,0,0,yx.gongrl)),0,0,sum(yx.gongrbzmh*yx.gongrl)/sum(decode(yx.gongrbzmh,0,0,yx.gongrl))),2) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt+"   ) z2, \n");
		sbsql.append("(select 6 as lanc,round_new(decode(sum(decode(yx.gongrbzmh,0,0,yx.gongrl)),0,0,sum(yx.gongrbzmh*yx.gongrl)/sum(decode(yx.gongrbzmh,0,0,yx.gongrl))),2) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt1+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
		sbsql.append("union \n");
		
		sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as year,z3.zhi as nextyear,t.dun as danw from \n");
		sbsql.append("(select 7 as lanc,'�������ñ�ú��' as mingc,'���' dun from dual) t,\n");
		sbsql.append("(select 7 as lanc,round_new(sum(yx.fadxybml)/10000,2) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt+"   ) z1, \n");
		sbsql.append("(select 7 as lanc,round_new(sum(yx.fadxybml)/10000,2) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt+"   ) z2, \n");
		sbsql.append("(select 7 as lanc,round_new(sum(yx.fadxybml)/10000,2) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt1+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
		sbsql.append("union \n");
		sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as year,z3.zhi as nextyear,t.dun as danw from \n");
		sbsql.append("(select 8 as lanc,'�������ñ�ú��' as mingc,'���' dun from dual) t,\n");
		sbsql.append("(select 8 as lanc,round_new(sum(yx.gongrxybml)/10000,2) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt+"   ) z1, \n");
		sbsql.append("(select 8 as lanc,round_new(sum(yx.gongrxybml)/10000,2) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt+"   ) z2, \n");
		sbsql.append("(select 8 as lanc,round_new(sum(yx.gongrxybml)/10000,2) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt1+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
		sbsql.append("union \n");
		
		sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as year,z3.zhi as nextyear,t.dun as danw from \n");
		sbsql.append("(select 9 as lanc,'���ñ�ú��' as mingc,'���' dun from dual) t,\n");
		sbsql.append("(select 9 as lanc,round_new(sum(yx.fadxybml)/10000,2) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt+"   ) z1, \n");
		sbsql.append("(select 9 as lanc,round_new(sum(yx.fadxybml)/10000,2) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt+"   ) z2, \n");
		sbsql.append("(select 9 as lanc,round_new(sum(yx.fadxybml)/10000,2) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt1+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
		sbsql.append("union \n");
		/*sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as year,z3.zhi as nextyear,t.dun as danw from \n");
		sbsql.append("(select 10 as lanc,'������ԭú��' as mingc,'���' dun from dual) t,\n");
		sbsql.append("(select 10 as lanc,sum(yx.fadxyml) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt+"   ) z1, \n");
		sbsql.append("(select 10 as lanc,sum(yx.fadxyml) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt+"   ) z2, \n");
		sbsql.append("(select 10 as lanc,sum(yx.fadxyml) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
		sbsql.append("union \n");
		sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as year,z3.zhi as nextyear,t.dun as danw from \n");
		sbsql.append("(select 11 as lanc,'������ԭú��' as mingc,'���' dun from dual) t,\n");
		sbsql.append("(select 11 as lanc,sum(yx.gongrxyml) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt+"   ) z1, \n");
		sbsql.append("(select 11 as lanc,sum(yx.gongrxyml) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt+"   ) z2, \n");
		sbsql.append("(select 11 as lanc,sum(yx.gongrxyml) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
		sbsql.append("union \n");*/
		sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as year,z3.zhi as nextyear,t.dun as danw from \n");
		sbsql.append("(select 10 as lanc,'�����ȼ����' as mingc,'��' dun from dual) t,\n");
		sbsql.append("(select 10 as lanc,sum(yx.dianhzryl) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt+"   ) z1, \n");
		sbsql.append("(select 10 as lanc,sum(yx.dianhzryl) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt+"   ) z2, \n");
		sbsql.append("(select 10 as lanc,sum(yx.dianhzryl) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt1+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
		sbsql.append("union \n");
		sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as year,z3.zhi as nextyear,'Mj/kg' as danw from \n");
		sbsql.append("(select 11 as lanc,'�ͷ�����' as mingc,'Mj/kg' dun from dual) t,\n");
		sbsql.append("(select 11 as lanc,round_new(decode(sum(decode(yx.youfrl,0,0,yx.dianhzryl)),0,0,sum(yx.youfrl*(yx.dianhzryl))/sum(decode(yx.youfrl,0,0,yx.dianhzryl))),2) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt+"   ) z1, \n");
		sbsql.append("(select 11 as lanc,round_new(decode(sum(decode(yx.youfrl,0,0,yx.dianhzryl)),0,0,sum(yx.youfrl*(yx.dianhzryl))/sum(decode(yx.youfrl,0,0,yx.dianhzryl))),2) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt+"   ) z2, \n");
		sbsql.append("(select 11 as lanc,round_new(decode(sum(decode(yx.youfrl,0,0,yx.dianhzryl)),0,0,sum(yx.youfrl*(yx.dianhzryl))/sum(decode(yx.youfrl,0,0,yx.dianhzryl))),2) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt1+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
		sbsql.append("union \n");
		sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as year,z3.zhi as nextyear,'Mj/kg' as danw from \n");
		sbsql.append("(select 12 as lanc,'����ú�۱�ú��' as mingc,'���' dun from dual) t,\n");
		sbsql.append("(select 12 as lanc,round_new((sum(zongxql)-decode(sum(decode(yx.youfrl,0,0,yx.dianhzryl)),0,0,sum(yx.youfrl*yx.dianhzryl)/sum(decode(yx.youfrl,0,0,yx.dianhzryl))))/10000,2) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt+"   ) z1, \n");
		sbsql.append("(select 12 as lanc,round_new((sum(zongxql)-decode(sum(decode(yx.youfrl,0,0,yx.dianhzryl)),0,0,sum(yx.youfrl*yx.dianhzryl)/sum(decode(yx.youfrl,0,0,yx.dianhzryl))))/10000,2) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt+"   ) z2, \n");
		sbsql.append("(select 12 as lanc,round_new((sum(zongxql)-decode(sum(decode(yx.youfrl,0,0,yx.dianhzryl)),0,0,sum(yx.youfrl*yx.dianhzryl)/sum(decode(yx.youfrl,0,0,yx.dianhzryl))))/10000,2) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt1+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
		sbsql.append("union \n");
		sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as year,z3.zhi as nextyear,'Mj/kg' as danw from \n");
		sbsql.append("(select 13 as lanc,'��¯��ֵ' as mingc,'Mj/kg' dun from dual) t,\n");
		sbsql.append("(select 13 as lanc,round_new(decode(sum(decode(yx.rulrz,0,0,yx.xuyyml)),0,0,sum(yx.rulrz*(yx.xuyyml))/sum(decode(yx.rulrz,0,0,yx.xuyyml))),2) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt+"   ) z1, \n");
		sbsql.append("(select 13 as lanc,round_new(decode(sum(decode(yx.rulrz,0,0,yx.xuyyml)),0,0,sum(yx.rulrz*(yx.xuyyml))/sum(decode(yx.rulrz,0,0,yx.xuyyml))),2) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt+"   ) z2, \n");
		sbsql.append("(select 13 as lanc,round_new(decode(sum(decode(yx.rulrz,0,0,yx.xuyyml)),0,0,sum(yx.rulrz*(yx.xuyyml))/sum(decode(yx.rulrz,0,0,yx.xuyyml))),2) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt1+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
		sbsql.append("union \n");
		sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as year,z3.zhi as nextyear,t.dun as danw from \n");
		sbsql.append("(select 14 as lanc,'����ԭú��' as mingc,'���' dun from dual) t,\n");
		sbsql.append("(select 14 as lanc,round_new(sum(xuyyml)/10000,2) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt+"   ) z1, \n");
		sbsql.append("(select 14 as lanc,round_new(sum(xuyyml)/10000,2) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt+"   ) z2, \n");
		sbsql.append("(select 14 as lanc,round_new(sum(xuyyml)/10000,2) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt1+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
		sbsql.append("union \n");
//		�����������ã������ڳ���棬��δ���
		sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as year,z3.zhi as nextyear,t.dun as danw from \n");
		sbsql.append("(select 15 as lanc,'������' as mingc,'���' dun from dual) t,\n");
		sbsql.append("(select 15 as lanc,round_new(sum(yx.qity)/10000,2) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt+"   ) z1, \n");
		sbsql.append("(select 15 as lanc,round_new(sum(yx.qity)/10000,2) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt+"   ) z2, \n");
		sbsql.append("(select 15 as lanc,round_new(sum(yx.qity)/10000,2) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt1+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
		sbsql.append("union \n");
		sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as year,z3.zhi as nextyear,t.dun as danw from \n");
		sbsql.append("(select 16 as lanc,'����' as mingc,'���' dun from dual) t,\n");
		sbsql.append("(select 16 as lanc,round_new(sum(yx.yuns)/10000,2) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt+"   ) z1, \n");
		sbsql.append("(select 16 as lanc,round_new(sum(yx.yuns)/10000,2) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt+"   ) z2, \n");
		sbsql.append("(select 16 as lanc,round_new(sum(yx.yuns)/10000,2) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt1+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
		sbsql.append("union \n");
		sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as year,z3.zhi as nextyear,t.dun as danw from \n");
		sbsql.append("(select 17 as lanc,'�ڳ����' as mingc,'���' dun from dual) t,\n");
		sbsql.append("(select 17 as lanc,round_new(sum(yx.qickc)/10000,2) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt+"   ) z1, \n");
		sbsql.append("(select 17 as lanc,round_new(sum(yx.qickc)/10000,2) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt+"   ) z2, \n");
		sbsql.append("(select 17 as lanc,round_new(sum(yx.qickc)/10000,2) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt1+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
		sbsql.append("union \n");
		sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as year,z3.zhi as nextyear,t.dun as danw from \n");
		sbsql.append("(select 18 as lanc,'��δ���' as mingc,'���' dun from dual) t,\n");
		sbsql.append("(select 18 as lanc,round_new(sum(yx.qimkc)/10000,2) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt+"   ) z1, \n");
		sbsql.append("(select 18 as lanc,round_new(sum(yx.qimkc)/10000,2) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt+"   ) z2, \n");
		sbsql.append("(select 18 as lanc,round_new(sum(yx.qimkc)/10000,2) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt1+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
		sbsql.append("union \n");
		sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as year,z3.zhi as nextyear,t.dun as danw from \n");
		sbsql.append("(select 19 as lanc,'��������' as mingc,'���' dun from dual) t,\n");
		sbsql.append("(select 19 as lanc,round_new(sum(yx.zongxql)/10000,2) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt+"   ) z1, \n");
		sbsql.append("(select 19 as lanc,round_new(sum(yx.zongxql)/10000,2) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt+"   ) z2, \n");
		sbsql.append("(select 19 as lanc,round_new(sum(yx.zongxql)/10000,2) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt1+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
		
		sbsql.append(") zz \n");
		sbsql.append("group by (lanc,mingc,zz.danw) \n");
		sbsql.append("order by lanc asc \n");
		
		ArrHeader=new String[1][6];
		ArrHeader[0]=new String[] {"��Ŀ","��Ŀ","��λ",lastyear+"�����",intyear+"�������",nextyear+"��Ԥ��"};

		ArrWidth=new int[] {35,150,70,70,80,70};

		System.out.println(sbsql.toString());
		ResultSet rs = cn.getResultSet(sbsql.toString());
		
		// ����
		rt.setBody(new Table(rs,1, 0, 2));

		rt.setTitle(titlename, ArrWidth);
//			rt.setTitle(intyear+"��"+intMonth+"��"+titlename, ArrWidth);
		rt.setDefaultTitle(1, 2, "�Ʊ�λ:"+((Visit) getPage().getVisit()).getDiancqc(), Table.ALIGN_LEFT);
		rt.setDefaultTitle(5, 4,date, Table.ALIGN_CENTER);
//			rt.setDefaultTitle(8, 2, "��λ:��", Table.ALIGN_RIGHT);
		
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(19);
		rt.body.setHeaderData(ArrHeader);// ��ͷ����
		rt.body.mergeFixedRow();

		rt.body.mergeFixedCols();
		rt.body.ShowZero = true;
		//ҳ�� 
		
		  rt.createDefautlFooter(ArrWidth);
		  rt.setDefautlFooter(1,3,"��ӡ����:"+DateUtil.FormatDate(new Date()),Table.ALIGN_LEFT);
		  rt.setDefautlFooter(8,1,"��λ:������",Table.ALIGN_LEFT);
//			  rt.setDefautlFooter(6,3,"�Ʊ�:",Table.ALIGN_LEFT);
//			  rt.setDefautlFooter(12,2,"���:",Table.ALIGN_LEFT);
//			  rt.setDefautlFooter(rt.footer.getCols()-1,2,Table.PAGENUMBER_NORMAL,Table.ALIGN_RIGHT);
		
		_CurrentPage=1;
		_AllPages=rt.body.getPages();
		if (_AllPages==0){
			_CurrentPage=0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
		
	}
	
	
	public String getPageHome() {
		if (((Visit) getPage().getVisit()).getboolean1()) {
			return "window.location = '" + MainGlobal.getHomeContext(this)
					+ "';";
		} else {
			return "";
		}
	}

	/*private String FormatDate(Date _date) {
		if (_date == null) {
			return "";
		}
		return DateUtil.Formatdate("yyyy��MM��dd��", _date);
	}*/

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (visit.getRenyID() == -1) {
			visit.setboolean1(true);
			return;
		}

		blnIsBegin = true;
		if(cycle.getRequestContext().getParameters("lx") !=null) {
			visit.setString9((cycle.getRequestContext().getParameters("lx")[0]));
			leix = visit.getString9();
        }else{
        	if(!visit.getString1().equals("")) {
        		leix = visit.getString9();
            }
        }
	}

}