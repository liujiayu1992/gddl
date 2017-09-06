////2008-08-05 chh 
//�޸����� ��ȼ�Ϲ�˾���û����Բ鿴������
//		   ��ϸ������ʾ

package com.zhiren.jt.zdt.monthreport.yuecgjhreport;
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

public class YuejhmxReport extends BasePage {
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
		
//		String shenhzt;//����״̬
//		if (visit.getString4().equals("")){
//			shenhzt="";
//		}else{
//			shenhzt=visit.getString4();
//		}
		String riq=visit.getString3();
		return getMingx(diancid,riq);
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
	
	private String getMingx(String diancid,String riq){//�糧ID����ݣ����״̬
		
		_CurrentPage=1;
		_AllPages=1;
		JDBCcon cn = new JDBCcon();
	
		//�����ͷ����
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		String titlename="��Ⱥ��������ܱ�";
		/*int iFixedRows=0;//�̶��к�
		int iCol=0;//����*/		
		//��������
		titlename=titlename+"";
		
		StringBuffer sbsql = new StringBuffer();
		
		sbsql.append("select decode(grouping(dc.mingc),1,'�ܼ�',dc.mingc) as danw,\n");
		sbsql.append("sum(yx.fadl) as fadl,sum(yx.fadml)+sum(yx.gongrml) as quanbhml,round(decode(sum(yx.fadl),0,0,sum(yx.fadmh*yx.fadl)/sum(yx.fadl)),0) as fadmh,sum(yx.fadml) as fadml,\n");
		sbsql.append("sum(yx.gongrl) as gongrl,round(decode(sum(yx.gongrl),0,0,sum(yx.gongrmh*yx.gongrl)/sum(yx.gongrl)),0)  as gongrmh,sum(yx.gongrml),\n");
		sbsql.append("sum(yx.sunh) as sunh,sum(yx.qity) as qity,sum(yx.yuekc) as yuekc,\n");
		sbsql.append("sum(yx.yuemkc) as yuemkc,sum(yx.xuqsl) as xuqsl\n");
		sbsql.append("from yuexqjhh yx,(select d.id, d.mingc,d.jingjcml ,d.rijhm,dc.xuh,dc.mingc as fengs, dc.id as fuid,dc.shangjgsid from diancxxb d, diancxxb dc  where d.jib = 3  and d.fuid = dc.id(+)) dc\n");
		sbsql.append("where yx.diancxxb_id=dc.id \n");
		sbsql.append("and yx.riq=to_date('" + riq + "-01','yyyy-mm-dd')\n");
		sbsql.append("and dc.id=").append(diancid);
		sbsql.append("group by rollup (dc.mingc)\n");
		sbsql.append("order by grouping(dc.mingc) desc,dc.mingc\n");
		
		ArrHeader=new String[1][12];
		ArrHeader[0]=new String[] {"��λ","������<br>(��ǧ��ʱ)","ȫ������ԭú��<br>(��)","����ԭú��<br>(��/ǧ��ʱ)","����ԭú��<br>(��)",
				 "������<br>(�򼪽�)","����ԭú��<br>(ǧ��/����)","������ԭú��<br>(��)","�������<br>(��)",
				 "������<br>(��)","�³���<br>(��)","��ĩ���<br>(��)","��������<br>(��)"};

		ArrWidth=new int[] {150,80,90,80,80,80,80,80,80,60,60,60,60};



		ResultSet rs = cn.getResultSet(sbsql.toString());
		
		// ����
		Table bt=new Table(rs,1,0,1);
		rt.setBody(bt);
		bt.setColAlign(2, Table.ALIGN_CENTER);
		
		if(rt.body.getRows()>1){
			rt.body.setCellAlign(2, 1, Table.ALIGN_CENTER);
		}
		//
		rt.body.ShowZero=false;
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.mergeFixedRow();//�ϲ���
		rt.body.mergeFixedCols();//�Ͳ���
		rt.setTitle( "���������ƻ�", ArrWidth);//getBiaotmc()+
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.setDefaultTitle(1, 2, "�Ʊ�λ:" +((Visit) getPage().getVisit()).getDiancqc(),Table.ALIGN_LEFT);
//		rt.setDefaultTitle(12, 2, "��λ:(��)" ,Table.ALIGN_RIGHT);
		rt.setDefaultTitle(4, 4,""+riq,Table.ALIGN_CENTER);
		rt.body.setPageRows(21);
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 3, "��ӡ����:"+FormatDate(DateUtil.getDate(DateUtil.FormatDate(new Date()))),Table.ALIGN_LEFT);
		rt.setDefautlFooter(4, 4, "����ˣ�", Table.ALIGN_CENTER);
		rt.setDefautlFooter(8, 4, "��ˣ�", Table.ALIGN_RIGHT);
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