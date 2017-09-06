package com.zhiren.jt.zdt.monthreport.yuebshsb;

import org.apache.tapestry.html.BasePage;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import com.zhiren.common.DateUtil;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;


/* 
* ʱ�䣺2009-04-09
* ���ߣ� ll
* �޸����ݣ� 
*     ��ʾ������⣬���ӱ������ơ����磺cpirȼ�Ϲ���05��
*/
/* ʱ�䣺2009-04-13 
* ���ߣ� ll
* �޸����ݣ�  �޸ĵ糧����ʾ�Ĳ�ѯ״̬��δ�ϱ��ֹ�˾����
*/
/* 
* ʱ�䣺2009-05-5
* ���ߣ� ll
* �޸����ݣ�1���滻����,��yuezbb_zdt��Ϊyuezbb��
* 		   
*/ 
public class YuebshsbReport extends BasePage {
	
	private String shujbmc="";
	private String diancxxb_id="";
	private String riq="";
//	private String baobmc="";
	
	public boolean getRaw() {
		return true;
	}

	public void submit(IRequestCycle cycle) {
		
	}
	
	public String getBiaotmc() {
		String biaotmc = "";
		JDBCcon cn = new JDBCcon();
		String sql_biaotmc = "select  zhi from xitxxb where mingc='������ⵥλ����'";
		ResultSet rs = cn.getResultSet(sql_biaotmc);
		try {
			while (rs.next()) {
				biaotmc = rs.getString("zhi");
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			cn.Close();
		}
		return biaotmc;
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
		return getPrintData();
	}

	public Date getBeginriqDate() {
		if (((Visit)getPage().getVisit()).getDate1()==null){
			((Visit)getPage().getVisit()).setDate1(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay));
		}
		return ((Visit)getPage().getVisit()).getDate1();
	}
	
	
	private String getPrintData(){
		JDBCcon con = new JDBCcon();
		String diancjb="";
		String diancmc="";
		String baobmc="";
		
		String baobmcsql="select baobmc from yuebbdysjb where shujbmc='"+shujbmc+"'";
		ResultSet baobmc_bt=con.getResultSet(baobmcsql);
		try{
			while(baobmc_bt.next()){
				 baobmc=baobmc_bt.getString("baobmc");
			}
		}catch(SQLException e){
			e.fillInStackTrace();
		}finally{
			con.Close();
		}
		
	    if(getDiancJib()==1){
		   diancmc="dc.fgsmc";
		   diancjb="and dc.fuid in (select dcb.id from diancxxb dcb where dcb.fuid="+diancxxb_id+") \n";
	    }else{
		   diancmc="dc.mingc";
		   diancjb="and (dc.fuid="+diancxxb_id+" or dc.rlgsid="+diancxxb_id+")  \n";
	    }
		String biaomc="select "+diancmc+" as mingc,decode(min(sl.zhuangt),2,'�����',1,'δ���',0,'δ�ϱ��ֹ�˾') as zhuangt \n"+
					 "from "+shujbmc+" sl,yuetjkjb kj,vwdianc dc \n"+
					 "where sl.yuetjkjb_id=kj.id and kj.diancxxb_id=dc.id  \n"+
			         "and kj.riq=to_date('"+riq+"','yyyy-mm-dd') \n"+
			         diancjb+
			         "group by("+diancmc+") \n";
		
		String biaomc1="select "+diancmc+" as mingc,\n" +
						"decode(min(sl.zhuangt),2,'�����',1,'δ���',0,'δ�ϱ��ֹ�˾') as zhuangt\n" + 
						"from "+shujbmc+" sl,vwdianc dc\n" + 
						"where sl.diancxxb_id=dc.id\n" + 
						"and sl.riq=to_date('"+riq+"','yyyy-mm-dd')\n" + 
						diancjb + 
						"group by("+diancmc+")";

		
		StringBuffer sbsql = new StringBuffer("");
		
			if(shujbmc.equals("yueslb")){
				
				sbsql.append(biaomc);
			}else if(shujbmc.equals("yuezlb")){
				
				sbsql.append(biaomc);
			}else if(shujbmc.equals("yuercbmdj")){
				sbsql.append(biaomc);
			}else if(shujbmc.equals("yuecgjhb")){
				sbsql.append(biaomc1);
			}else if(shujbmc.equals("rucycbb")){
				sbsql.append(biaomc1);
			}else if(shujbmc.equals("yuezbb")){
				sbsql.append(biaomc1);
			}else if(shujbmc.equals("yueshchjb")){
				sbsql.append(biaomc1);
			}else if(shujbmc.equals("yueshcyb")){
				sbsql.append(biaomc1);
			}
			
			ResultSet rs = con.getResultSet(sbsql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			Report rt = new Report();
			String ArrHeader[][]=new String[1][2];
			ArrHeader[0]=new String[] {"�糧����","״̬"};

			int ArrWidth[]=new int[] {260,220};		
			Table bt=new Table(rs,1,0,2);
			rt.setBody(bt);
			bt.setColAlign(1, Table.ALIGN_CENTER);
			bt.setColAlign(2, Table.ALIGN_CENTER);
			
			rt.body.setPageRows(22);
			rt.body.setWidth(ArrWidth);
			rt.body.setHeaderData(ArrHeader);
			rt.body.mergeFixedRow();//�ϲ���
			rt.body.mergeFixedCols();//�Ͳ���
			rt.setTitle(baobmc+"�糧����ϱ�״̬", ArrWidth);

			rt.title.setRowHeight(2, 50);
			rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
			rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
//			rt.setDefaultTitle(1, 4, "�糧����:" +this.getDiancxxbmc()+"    �����:"+this.getJizbh()+"   ��������:"+this.getJizrl(),Table.ALIGN_LEFT);

			rt.createDefautlFooter(ArrWidth);
//			rt.setDefautlFooter(1, 3, "��ӡ����:"+FormatDate(DateUtil.getDate(DateUtil.FormatDate(new Date()))),Table.ALIGN_LEFT);

			// ����ҳ��
			_CurrentPage = 1;
			_AllPages = rt.body.getPages();
			if (_AllPages == 0) {
				_CurrentPage = 0;
			}
			con.Close();
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

	private String FormatDate(Date _date) {
		if (_date == null) {
			return "";
		}
		return DateUtil.Formatdate("yyyy��MM��dd��", _date);
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (visit.getRenyID() == -1) {
			visit.setboolean1(true);
			return;
		}

		blnIsBegin = true;
		
		//���ݱ�
		if(cycle.getRequestContext().getParameters("lx") !=null) {
			visit.setString9((cycle.getRequestContext().getParameters("lx")[0]));
			shujbmc = visit.getString9();
        }else{
        	if(!visit.getString1().equals("")) {
        		shujbmc = visit.getString9();
            }
        }
		//�糧ID
		if(cycle.getRequestContext().getParameters("diancxxb_id") !=null) {
			visit.setString10((cycle.getRequestContext().getParameters("diancxxb_id")[0]));
			diancxxb_id = visit.getString10();
        }else{
        	if(!visit.getString1().equals("")) {
        		diancxxb_id = visit.getString10();
            }
        }
		//��ʼ����
		if(cycle.getRequestContext().getParameters("riq") !=null) {
			visit.setString11((cycle.getRequestContext().getParameters("riq")[0]));
			riq = visit.getString11();
        }else{
        	if(!visit.getString1().equals("")) {
        		riq = visit.getString11();
            }
        }
		}
	public int getDiancJib() {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		String DiancJib =diancxxb_id;
		//System.out.println("jib:" + DiancTreeJib);
		if (DiancJib == null || DiancJib.equals("")) {
			DiancJib = "0";
		}
		String sqlJib = "select d.jib from diancxxb d where d.id="+ diancxxb_id;
		ResultSet rs = con.getResultSet(sqlJib.toString());
		
		try {
			while (rs.next()) {
				jib = rs.getInt("jib");
			}
			rs.close();
		} catch (SQLException e) {

			e.printStackTrace();
		}finally{
			con.Close();
		}

		return jib;
	}

}