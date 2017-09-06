package com.zhiren.dc.huaygl.huaybb.huaydb;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.contrib.palette.SortMode;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
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
/**
 * huochaoyuan
 * 2010-01-13����������ʾ�����ڸñ���������������������һֱδ������ʹ�ã��ʴ˴���Ը�������糧��Ҫ��������
 * ��ʾzhillsb�����ݣ�������һ�����ζ���������ĵ糧��ѯÿ��������ֵ���ҷ�������ʾ����������ƽ��ֵ������ʾ����ƽ��ֵ�Ļ��ܼ�Ȩ���
 */
public class Huaydb extends BasePage {

	public Date getMonthFirstday(Date dat) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dat);
		cal.set(Calendar.DATE, cal.getActualMinimum(Calendar.DATE));
		return cal.getTime();
	}

	boolean riqchange = false;

	private String riq;

	public String getRiq() {
		if (riq == null || riq.equals("")) {
			riq = DateUtil.FormatDate(new Date());
		}
		return riq;
	}

	public void setRiq(String riq) {

		if (this.riq != null && !this.riq.equals(riq)) {
			this.riq = riq;
			riqchange = true;
		}

	}

	boolean afterchange = false;

	private String after;

	public String getAfter() {
		if (after == null || after.equals("")) {
			after = DateUtil.FormatDate(new Date());
		}
		return after;
	}

	public void setAfter(String after) {

		if (this.after != null && !this.after.equals(after)) {
			this.after = after;
			afterchange = true;
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


	public String getPrintTable() {
		setMsg(null);
		return getHuaydb();

	}
	private ArrayList getZhuanmsz(String id){
		ArrayList list=new ArrayList();
		JDBCcon con=new JDBCcon();
		PreparedStatement ps=null;
		ResultSet rst=null;
		StringBuffer sql=new StringBuffer();
		sql.append("select l.zhuanmsz from huaydbszb sz,huaydbszxxb xx,leibb l where sz.id=xx.huaydbszb_id and xx.leibb_id=l.id and sz.id=");
		sql.append(id);
		try {
			ps=con.getPresultSet(sql.toString());
			rst=ps.executeQuery();
			while(rst.next()){
				list.add(rst.getString("zhuanmsz"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
        con.Close();
		return list;
	}
	private Report getQueryReport(){
		Visit v = (Visit) getPage().getVisit();
		Report rt = new Report();
		JDBCcon con = new JDBCcon();

		StringBuffer buffer = new StringBuffer();

//		buffer
//				.append("select fhdw,\n"
//						+ "       mkdw,\n"
//						+ "       pz,\n"
//						+ "       fz,\n"
//						+ "       jingz,\n"
//						+ "       ches,\n"
//						+ "       huaybh,\n"
//						+ "       mt,\n"
//						+ "       mad,\n"
//						+ "       aad,\n"
//						+ "       ad,\n"
//						+ "       aar,\n"
//						+ "       vad,\n"
//						+ "       vdaf,\n"
//						+ "       qbad*1000,\n"
//						+ "       farl*1000,\n"
//						+ "       qbar,\n"		
//						+ "       sdaf,stad,\n"
//						+ "       std,\n"
//						+ "       star,\n"
//						+ "       hdaf,\n"
//						+ "       had\n"
//						+ "  from (select decode(grouping(g.mingc), 1, '�ϼ�', g.mingc) as fhdw,\n"
//						+ "               m.mingc mkdw,\n"
//						+ "               p.mingc pz,\n"
//						+ "               c.mingc fz,\n"
//						+ "               decode(grouping(g.mingc), 1, 'ƽ��ֵ', z.huaybh) as huaybh, \n"
//						+ "               sum(round_new(f.laimsl,"+v.getShuldec()+")) jingz,\n"
//						+ "               sum(f.biaoz) biaoz,\n"
//						+ "               sum(f.yuns) yuns,\n"
//						+ "               sum(f.yingk) yingk,\n"
//						+ "               sum(f.zongkd) zongkd,\n"
//						+ "               sum(f.ches) ches,\n"
//						+ "               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n"
//						+ "                      0,\n"
//						+ "                      0,\n"
//						+ "                      round_new(sum(round_new(z.mt,"+v.getMtdec()+") * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), "+v.getMtdec()+")) as mt,\n"
//						+ "               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n"
//						+ "                      0,\n"
//						+ "                      0,\n"
//						+ "                      round_new(sum(z.mad * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), 2)) as mad,\n"
//						+ "               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n"
//						+ "                      0,\n"
//						+ "                      0,\n"
//						+ "                      round_new(sum(z.aad * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), 2)) as aad,\n"
//						+ "               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n"
//						+ "                      0,\n"
//						+ "                      0,\n"
//						+ "                      round_new(sum(z.ad * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), 2)) as ad,\n"
//						+ "               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n"
//						+ "                      0,\n"
//						+ "                      0,\n"
//						+ "                      round_new(sum(z.aar * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), 2)) as aar,\n"
//						+ "               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n"
//						+ "                      0,\n"
//						+ "                      0,\n"
//						+ "                      round_new(sum(z.vad * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), 2)) as vad,\n"
//						+ "               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n"
//						+ "                      0,\n"
//						+ "                      0,\n"
//						+ "                      round_new(sum(z.vdaf * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), 2)) as vdaf,\n"
//						+ "               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n"
//						+ "                      0,\n"
//						+ "                      0,\n"
//						+ "                      round_new(sum(round_new(z.qbad,"+v.getFarldec()+") * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), "+v.getFarldec()+")) as qbad,\n"
//						+ "               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n"
//						+ "                      0,\n"
//						+ "                      0,\n"
//						+ "                      round_new(sum(round_new(z.qnet_ar,"+v.getFarldec()+") * round_new(f.laimsl,"+v.getShuldec()+")) /\n"
//						+ "                                          sum(round_new(f.laimsl,"+v.getShuldec()+"))\n"
//						+ "                                           * 1000 / 4.1816,\n"
//						+ "                                0)) as qbar,\n"
//						+ "               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n"
//						+ "                      0,\n"
//						+ "                      0,\n"
//						+ "                      round_new(sum(round_new(z.qnet_ar,"+v.getFarldec()+") * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), "+v.getFarldec()+")) as farl,\n"
//						+ "               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n"
//						+ "                      0,\n"
//						+ "                      0,\n"
//						+ "                      round_new(sum(z.sdaf * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), 2)) as sdaf,\n"
//						+ "               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n"
//						+ "                      0,\n"
//						+ "                      0,\n"
//						+ "                      round_new(sum(z.stad * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), 2)) as stad,\n"
//						+ "               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n"
//						+ "                      0,\n"
//						+ "                      0,\n"
//						+ "                      round_new(sum(z.std * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), 2)) as std,\n"
//						+ "               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n"
//						+ "                      0,\n"
//						+ "                      0,\n"
//						+ "                      round_new(sum((round_new(z.stad*(100-z.mt)/(100-z.mad),2)) * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), 2)) as star,\n"
//						+ "               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n"
//						+ "                      0,\n"
//						+ "                      0,\n"
//						+ "                      round_new(sum(z.hdaf * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), 2)) as hdaf,"
//						+ "               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n"
//						+ "                      0,\n"
//						+ "                      0,\n"
//						+ "                      round_new(sum(z.had * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), 2)) as had\n"
//						+ "          from fahb f, gongysb g, meikxxb m, pinzb p, chezxxb c, zhilb z\n"
//						+ "         where f.gongysb_id = g.id\n"
//						+ "           and f.meikxxb_id = m.id\n"
//						+ "           and f.pinzb_id = p.id\n"
//						+ "           and f.faz_id = c.id\n"
//						+ "           and f.zhilb_id = z.id\n"
//						+ "           and f.daohrq >= to_date('"
//						+ getRiq()
//						+ "', 'yyyy-mm-dd')\n"
//						+ "           and f.daohrq <= to_date('"
//						+ getAfter()
//						+ "', 'yyyy-mm-dd')\n"
//						+ "           and f.yunsfsb_id = "
//						+ getYunsfsValue().getId()
//						+ "\n"
//						+ "         group by rollup(g.mingc, m.mingc, p.mingc, c.mingc,z.huaybh)\n"
//						+ "         having (grouping(z.huaybh)=0 or grouping(g.mingc)=1 ) )\n"
//						+ "UNION ALL \n"
//						+ "select fhdw,\n"
//						+ "       mkdw,\n"
//						+ "       pz,\n"
//						+ "       fz,\n"
//						+ "       jingz,\n"
//						+ "       ches,\n"
//						+ "       huaybh,\n"
//						+ "       mt,\n"
//						+ "       mad,\n"
//						+ "       aad,\n"
//						+ "       ad,\n"
//						+ "       aar,\n"
//						+ "       vad,\n"
//						+ "       vdaf,\n"
//						+ "       qbad*1000,\n"
//						+ "       farl*1000,\n"
//						+ "       qbar,\n"
//						
//						+ "       sdaf,stad,\n"
//						+ "       std,\n"
//						+ "       star,\n"
//						+ "       hdaf,\n"
//						+ "       had\n"
//						+ "  from (select decode(grouping(g.mingc)+grouping(m.mingc)\n"
//                        + "               +grouping(p.mingc)+grouping(c.mingc)+grouping(zm.bianm),\n"
//                        + "               5, '�ϼ�', g.mingc) as fhdw,\n"
//						+ "               m.mingc mkdw,\n"
//						+ "               p.mingc pz,\n"
//						+ "               c.mingc fz,\n"
//						+ "               decode(grouping(g.mingc)+grouping(zm.bianm),"
//                        + "                      1," 
//                        + "                      'ƽ��ֵ',"
//                        + "                      2,"
//                        + "                      '��ƽ��ֵ',"
//                        + "                      zm.bianm) as huaybh,\n"
//						+ "               sum(round_new(f.laimsl,"+v.getShuldec()+")) jingz,\n"
//						+ "               sum(f.biaoz) biaoz,\n"
//						+ "               sum(f.yuns) yuns,\n"
//						+ "               sum(f.yingk) yingk,\n"
//						+ "               sum(f.zongkd) zongkd,\n"
//						+ "               sum(f.ches) ches,\n"
//						+ "               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n"
//						+ "                      0,\n"
//						+ "                      0,\n"
//						+ "                      round_new(sum(round_new(z.mt,"+v.getMtdec()+") * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), "+v.getMtdec()+")) as mt,\n"
//						+ "               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n"
//						+ "                      0,\n"
//						+ "                      0,\n"
//						+ "                      round_new(sum(z.mad * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), 2)) as mad,\n"
//						+ "               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n"
//						+ "                      0,\n"
//						+ "                      0,\n"
//						+ "                      round_new(sum(z.aad * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), 2)) as aad,\n"
//						+ "               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n"
//						+ "                      0,\n"
//						+ "                      0,\n"
//						+ "                      round_new(sum(z.ad * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), 2)) as ad,\n"
//						+ "               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n"
//						+ "                      0,\n"
//						+ "                      0,\n"
//						+ "                      round_new(sum(z.aar * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), 2)) as aar,\n"
//						+ "               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n"
//						+ "                      0,\n"
//						+ "                      0,\n"
//						+ "                      round_new(sum(z.vad * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), 2)) as vad,\n"
//						+ "               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n"
//						+ "                      0,\n"
//						+ "                      0,\n"
//						+ "                      round_new(sum(z.vdaf * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), 2)) as vdaf,\n"
//						+ "               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n"
//						+ "                      0,\n"
//						+ "                      0,\n"
//						+ "                      round_new(sum(round_new(z.qbad,"+v.getFarldec()+") * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), "+v.getFarldec()+")) as qbad,\n"
//						+ "               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n"
//						+ "                      0,\n"
//						+ "                      0,\n"
//						+ "                      round_new(sum(round_new(z.qnet_ar,"+v.getFarldec()+") * round_new(f.laimsl,"+v.getShuldec()+")) /\n"
//						+ "                                          sum(round_new(f.laimsl,"+v.getShuldec()+"))\n"
//						+ "                                           * 1000 / 4.1816,\n"
//						+ "                                0)) as qbar,\n"
//						+ "               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n"
//						+ "                      0,\n"
//						+ "                      0,\n"
//						+ "                      round_new(sum(round_new(z.qnet_ar,"+v.getFarldec()+") * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), "+v.getFarldec()+")) as farl,\n"
//						+ "               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n"
//						+ "                      0,\n"
//						+ "                      0,\n"
//						+ "                      round_new(sum(z.sdaf * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), 2)) as sdaf,\n"
//						+ "               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n"
//						+ "                      0,\n"
//						+ "                      0,\n"
//						+ "                      round_new(sum(z.stad * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), 2)) as stad,\n"
//						+ "               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n"
//						+ "                      0,\n"
//						+ "                      0,\n"
//						+ "                      round_new(sum(z.std * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), 2)) as std,\n"
//						+ "               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n"
//						+ "                      0,\n"
//						+ "                      0,\n"
//						+ "                      round_new(sum((round_new(z.stad*(100-z.mt)/(100-z.mad),2)) * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), 2)) as star,\n"
//						+ "               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n"
//						+ "                      0,\n"
//						+ "                      0,\n"
//						+ "                      round_new(sum(z.hdaf * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), 2)) as hdaf,"
//						+ "               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n"
//						+ "                      0,\n"
//						+ "                      0,\n"
//						+ "                      round_new(sum(z.had * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), 2)) as had\n"
//						+ "          from fahb f, gongysb g, meikxxb m, pinzb p, chezxxb c, zhillsb z,zhuanmb zm,zhuanmlb zml\n"
//						+ "         where f.gongysb_id = g.id\n"
//						+ "           and f.meikxxb_id = m.id\n"
//						+ "           and f.pinzb_id = p.id\n"
//						+ "           and f.faz_id = c.id\n"
//						+ "           and f.zhilb_id = z.zhilb_id\n"
//						+ "			  and zml.jib=3\n"
//						+ "			  and zm.zhuanmlb_id=zml.id\n"
//						+ "			  and zm.zhillsb_id=z.id \n"
//						+ "           and f.daohrq >= to_date('"
//						+ getRiq()
//						+ "', 'yyyy-mm-dd')\n"
//						+ "           and f.daohrq <= to_date('"
//						+ getAfter()
//						+ "', 'yyyy-mm-dd')\n"
//						);
//						for(int i=0;i<getZhuanmsz(Long.toString(getLeibValue().getId())).size();i++){
//						    if(i==0){
//								buffer.append("         and (zm.bianm like '"	);
//								buffer.append(getZhuanmsz(Long.toString(getLeibValue().getId())).get(i));
//								buffer.append("%'\n");
//						    }else{
//						    	buffer.append("         or zm.bianm like '"	);
//								buffer.append(getZhuanmsz(Long.toString(getLeibValue().getId())).get(i));
//								buffer.append("%'\n");
//						    }
//						};
//						buffer.append(")\n"
//						+ "           and f.yunsfsb_id = "
//						+ getYunsfsValue().getId()
//						+ "\n"
//						+ "         group by rollup(g.mingc, m.mingc,p.mingc, c.mingc,zm.bianm)\n"
//						+ "         having (grouping(c.mingc)=0 or grouping(g.mingc)=1) )"
//						);

//		ResultSet rs = con.getResultSet(buffer,
//				ResultSet.t.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		//System.out.println(buffer);
        String yunssql="";
        String gmsql="";
        if(!getYunsfsValue().getValue().equals("ȫ��")){
        	yunssql="           and f.yunsfsb_id = "+getYunsfsValue().getId()+"\n";
        }
        if(getTreeid()!=null&&!getTreeid().equals("0")&&!getTreeid().equals("")){
        	gmsql="and (f.gongysb_id="+getTreeid()+" or f.meikxxb_id="+getTreeid()+" )\n";
        }
		buffer
		.append("select a.gid,a.mid,a.pid,a.bianm,a.zb,a.lm,a.mt,a.mad,a.aad,a.aar,a.vad,a.vdaf,a.stad,a.star,a.qbad*1000,a.qnet_ar*1000,round_new(a.qnet_ar*1000/4.1816,0) as qbar,a.fcad\n" +
		"from\n" + 
		"(select  1 as xuh,c.bianm as bianm,\n" + 
		"       jc.gid as gid,\n" + 
		"       jc.mid as mid,\n" + 
		"       jc.pid as pid,\n" + 
		"       round_new(jc.lm,2) as lm,\n" + 
		"       decode(zm.bianm, null, '����ƽ��', zm.bianm) as zb,\n" + 
		"       round_new(sum(round_new(z.mt,2)) / count(c.bianm), 2) as mt,\n" + 
		"       round_new(sum(round_new(z.mad,2)) / count(c.bianm), 2) as mad,\n" + 
		"       round_new(sum(round_new(z.aad,2)) / count(c.bianm), 2) as aad,\n" + 
		"       round_new(sum(round_new(z.aar,2)) / count(c.bianm), 2) as aar,\n" + 
		"       round_new(sum(round_new(z.vad,2)) / count(c.bianm), 2) as vad,\n" + 
		"       round_new(sum(round_new(z.vdaf,2)) / count(c.bianm), 2) as vdaf,\n" + 
		"       round_new(sum(round_new(z.stad,2)) / count(c.bianm), 2) as stad,\n" + 
		"       round_new(sum(round_new(round_new(z.stad*(100-z.mt)/(100-z.mad),2),2)) / count(c.bianm), 2) as star,\n" + 
		"       round_new(sum(round_new(z.qbad,2)) / count(c.bianm), 2) as qbad,\n" + 
		"       round_new(sum(round_new(z.fcad,2)) / count(c.bianm), 2) as fcad,\n" + 
		"       round_new(sum(round_new(z.qnet_ar,2)) / count(c.bianm), 2) as qnet_ar\n" + 
		"  from (select z.id zid,\n" + 
		"               g.mingc gid,\n" + 
		"               m.mingc mid,\n" + 
		"               p.mingc pid,\n" + 
		"               sum(f.laimsl) lm\n" + 
		"          from fahb f, zhillsb z, gongysb g, meikxxb m, pinzb p\n" + 
		"         where to_char(daohrq, 'yyyy-mm-dd') >= '"+getRiq()+"'\n" + 
		"           and to_char(daohrq, 'yyyy-mm-dd') <= '"+getAfter()+"'\n" + yunssql+gmsql+
		"           and f.zhilb_id = z.zhilb_id\n" + 
		"           and f.gongysb_id = g.id\n" + 
		"           and f.meikxxb_id = m.id\n" + 
		"           and f.pinzb_id = p.id\n" + 
		"           and z.qnet_ar is not null\n" + 
		"         group by z.id, g.mingc, m.mingc, p.mingc) jc,\n" + 
		"       zhillsb z,\n" + 
		"       zhuanmb zm,\n" + 
		"       caiyb c\n" + 
		" where jc.zid = z.id\n" + 
		"   and jc.zid = zm.zhillsb_id\n" + 
		"   and zm.zhuanmlb_id = 100663\n");
		if(!getLeibValue().getValue().equals("ȫ��")){
		for(int i=0;i<getZhuanmsz(Long.toString(getLeibValue().getId())).size();i++){
		    if(i==0){
				buffer.append("         and (zm.bianm like '"	);
				buffer.append(getZhuanmsz(Long.toString(getLeibValue().getId())).get(i));
				buffer.append("%'\n");
		    }else{
		    	buffer.append("         or zm.bianm like '"	);
				buffer.append(getZhuanmsz(Long.toString(getLeibValue().getId())).get(i));
				buffer.append("%'\n");
		    }
		};		    buffer.append(")\n");};
		buffer
		.append("   and c.zhilb_id = z.zhilb_id\n" + 
		"\n" + 
		" group by rollup(c.bianm, jc.gid, jc.mid, jc.pid, jc.lm, zm.bianm)\n" + 
		"having grouping(jc.lm) = 0\n" + 
		"union\n" + 
		"select 0 as xuh,'ƽ��ֵ��Ȩ'as biam,'ƽ��ֵ��Ȩ' as gid,'ƽ��ֵ��Ȩ'as mid,'ƽ��ֵ��Ȩ'as pid,sum(round_new(a.lm,2)) as lm,'ƽ��ֵ��Ȩ'as zb,\n" + 
		"round_new(sum(round_new(a.lm,2)*a.mt)/sum(round_new(a.lm,2)),2) as mt,\n" + 
		"round_new(sum(round_new(a.lm,2)*a.mad)/sum(round_new(a.lm,2)),2) as mad,\n" + 
		"round_new(sum(round_new(a.lm,2)*a.aad)/sum(round_new(a.lm,2)),2) as aad,\n" + 
		"round_new(sum(round_new(a.lm,2)*a.aar)/sum(round_new(a.lm,2)),2) as aar,\n" + 
		"round_new(sum(round_new(a.lm,2)*a.vad)/sum(round_new(a.lm,2)),2) as vad,\n" + 
		"round_new(sum(round_new(a.lm,2)*a.vdaf)/sum(round_new(a.lm,2)),2) as vdaf,\n" + 
		"round_new(sum(round_new(a.lm,2)*a.stad)/sum(round_new(a.lm,2)),2) as stad,\n" + 
		"round_new(sum(round_new(a.lm,2)*a.star)/sum(round_new(a.lm,2)),2) as star,\n" + 
		"round_new(sum(round_new(a.lm,2)*a.qbad)/sum(round_new(a.lm,2)),2) as qbad,\n" + 
		"round_new(sum(round_new(a.lm,2)*a.fcad)/sum(round_new(a.lm,2)),2) as fcad,\n" + 
		"round_new(sum(round_new(a.lm,2)*a.qnet_ar)/sum(round_new(a.lm,2)),2) as qnet_ar\n" + 
		"from\n" + 
		"(select 1 as id  ,\n" + 
		"c.bianm,\n" + 
		"       jc.gid,\n" + 
		"       jc.mid,\n" + 
		"       jc.pid,\n" + 
		"       jc.lm,\n" + 
		"       decode(zm.bianm, null, '����ƽ��', zm.bianm) as zb,\n" + 
		"       round_new(sum(round_new(z.mt,2)) / count(c.bianm), 2) as mt,\n" + 
		"       round_new(sum(round_new(z.mad,2)) / count(c.bianm), 2) as mad,\n" + 
		"       round_new(sum(round_new(z.aad,2)) / count(c.bianm), 2) as aad,\n" + 
		"       round_new(sum(round_new(z.aar,2)) / count(c.bianm), 2) as aar,\n" + 
		"       round_new(sum(round_new(z.vad,2)) / count(c.bianm), 2) as vad,\n" + 
		"       round_new(sum(round_new(z.vdaf,2)) / count(c.bianm), 2) as vdaf,\n" + 
		"       round_new(sum(round_new(z.stad,2)) / count(c.bianm), 2) as stad,\n" + 
		"       round_new(sum(round_new(round_new(z.stad*(100-z.mt)/(100-z.mad),2),2)) / count(c.bianm), 2) as star,\n" + 
		"       round_new(sum(round_new(z.qbad,2)) / count(c.bianm), 2) as qbad,\n" + 
		"       round_new(sum(round_new(z.fcad,2)) / count(c.bianm), 2) as fcad,\n" + 
		"       round_new(sum(round_new(z.qnet_ar,2)) / count(c.bianm), 2) as qnet_ar\n" + 
		"  from (select z.id zid,\n" + 
		"               g.mingc gid,\n" + 
		"               m.mingc mid,\n" + 
		"               p.mingc pid,\n" + 
		"               sum(f.laimsl) lm\n" + 
		"          from fahb f, zhillsb z, gongysb g, meikxxb m, pinzb p\n" + 
		"         where to_char(daohrq, 'yyyy-mm-dd') >= '"+getRiq()+"'\n" + 
		"           and to_char(daohrq, 'yyyy-mm-dd') <= '"+getAfter()+"'\n" + yunssql+gmsql+
		"           and f.zhilb_id = z.zhilb_id\n" + 
		"           and f.gongysb_id = g.id\n" + 
		"           and f.meikxxb_id = m.id\n" + 
		"           and f.pinzb_id = p.id\n" + 
		"           and z.qnet_ar is not null\n" + 
		"         group by z.id, g.mingc, m.mingc, p.mingc) jc,\n" + 
		"       zhillsb z,\n" + 
		"       zhuanmb zm,\n" + 
		"       caiyb c\n" + 
		" where jc.zid = z.id\n" + 
		"   and jc.zid = zm.zhillsb_id\n" + 
		"   and zm.zhuanmlb_id = 100663\n");
		if(!getLeibValue().getValue().equals("ȫ��")){
		for(int i=0;i<getZhuanmsz(Long.toString(getLeibValue().getId())).size();i++){
		    if(i==0){
				buffer.append("         and (zm.bianm like '"	);
				buffer.append(getZhuanmsz(Long.toString(getLeibValue().getId())).get(i));
				buffer.append("%'\n");
		    }else{
		    	buffer.append("         or zm.bianm like '"	);
				buffer.append(getZhuanmsz(Long.toString(getLeibValue().getId())).get(i));
				buffer.append("%'\n");
		    }
		};
		buffer.append(")\n");
		};
		buffer
		.append("    and c.zhilb_id = z.zhilb_id\n" + 
		" group by rollup(c.bianm, jc.gid, jc.mid, jc.pid, jc.lm, zm.bianm)\n" + 
		"having grouping(jc.lm) = 0\n" + 
		")a\n" + 
		"where a.zb='����ƽ��'\n" + 
		")a\n" + 
		"order by a.xuh asc,a.gid,a.mid,a.pid,a.bianm,a.zb");

		ResultSetList rs = con.getResultSetList(buffer.toString());
	//	System.out.println(buffer.toString());
		String[][] ArrHeader = new String[1][18];

		ArrHeader[0] = new String[] { "������λ", "ú��λ", "Ʒ��","��������",
				"������","����", "ȫˮ<br>��<br>(%)Mt",
				"����<br>����<br>��ˮ<br>��<br>(%)Mad",
				"����<br>����<br>����<br>��<br>(%)Aad",
				"�յ�<br>��<br>�ҷ�<br>(%)Aar", 
				"������<br>�����<br>����<br>(%)Vad",
				"������<br>�һ���<br>����<br>(%)Vdaf", 
				"����<br>����<br>����<br>(%)<br>Stad",
				"�յ�<br>��ȫ<br>��(%)<br>St,ar",
				"��Ͳ��<br>����<br>(J/g)<br>Qb,ad",
				"�յ���<br>��λ��<br>����(J/g)<br>Qnet,ar",
				"�յ�<br>����<br>λ��<br>ֵ(Kcal<br>/Kg)",
				"�̶�<br>̼<br>(%)<br>Fcad"};
		int[] ArrWidth = new int[18];

		ArrWidth = new int[] {100, 100, 50,70, 60,60,40, 40, 40, 40, 40, 40, 40,
				40, 40, 40, 40,40};

		rt.setTitle("�� �� �� �� �� �� �� ��", ArrWidth);
		rt.title.setRowHeight(2, 40);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 18);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);

		rt.setDefaultTitle(1, 5, "жú����:" + getRiq() + "��" + getAfter(),
				Table.ALIGN_LEFT);
		rt.title.setRowCells(3, Table.PER_FONTSIZE, 10);

		String[] strFormat = new String[] { "", "", "", "", "", "0.0","0.0","0.00",
				"0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "", "", "","0.00"};

		rt.setBody(new Table(rs, 1, 0, 4));
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.setPageRows(25);
		rt.body.mergeFixedCols();
		//rt.body.mergeFixedRow();
		rt.body.merge(2, 1, 2, 5);
		rt.body.setColFormat(strFormat);
		for (int i = 1; i <= 18; i++) {
			rt.body.setColAlign(i, Table.ALIGN_CENTER);
		}

		rt.createDefautlFooter(ArrWidth);

		rt.setDefautlFooter(1, 3, "��ӡ����:"
				+ DateUtil.FormatDate(new Date()),
				Table.ALIGN_LEFT);
		rt.setDefautlFooter(6, 2, "����:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(11, 3, "���:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(14, 4, "�Ʊ�:", Table.ALIGN_LEFT);
		rt.footer.setRowCells(2, Table.PER_FONTSIZE, 10);

		// ����ҳ��
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
		rt.body.setRowHeight(21);
		return rt;
	}
	private String getHuaydb() {
		if(!_isShow){
			return "";
		}
		_isShow = false;
		Report rt = getQueryReport();
		if(rt == null){
			return "";
		}
		return rt.getAllPagesHtml();

	}

	private boolean _QueryClick = false;

	public void QueryButton(IRequestCycle cycle) {
		_QueryClick = true;
	}
	
	private boolean _isShow = true;

	public void submit(IRequestCycle cycle) {
		if (_QueryClick) {
			_QueryClick = false;
			_isShow = true;
		}
	}
    private String treeid;
	public String getTreeid() {
		return treeid;
	}
	public void setTreeid(String a) {
		treeid=a;
	}
	
    public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}
    
	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
		int jib = -1;
		String DiancTreeJib = this.getTreeid();
		// System.out.println("jib:" + DiancTreeJib);
		if (DiancTreeJib == null || DiancTreeJib.equals("")) {
			DiancTreeJib = "0";
		}
		String sqlJib = "select d.jib from diancxxb d where d.id="
				+ DiancTreeJib;
		ResultSet rs = con.getResultSet(sqlJib.toString());

		try {
			while (rs.next()) {
				jib = rs.getInt("jib");
			}
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			con.Close();
		}

		return jib;
	}
	
	public void getSelectData() {
		Toolbar tb1 = new Toolbar("tbdiv");
		Visit visit = (Visit) getPage().getVisit();
		tb1.addText(new ToolbarText("��������:"));
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getRiq());
		df.Binding("riq", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setId("riq");
		tb1.addField(df);
		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("��:"));
		DateField df1 = new DateField();
		df1.setReadOnly(true);
		df1.setValue(this.getAfter());
		df1.Binding("after", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		df1.setId("after");
		tb1.addField(df1);
		tb1.addText(new ToolbarText("-"));

		long diancid=visit.getDiancxxb_id();
		DefaultTree dt = new DefaultTree(DefaultTree.tree_gys_mk,"diancTree",""+diancid,null,null,null);
		visit.setDefaultTree(dt);
		
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setValue("��ѡ��ú��");
		tf.setWidth(70);
		//dt.getTree().getSelectedNodeid()
		//tf.setValue(dt.getTree().getSelectedNodeid());
		//tf.setValue(((IDropDownModel)getDiancmcModel()).getBeanValue(Long.parseLong(getTreeid()==null||"".equals(getTreeid())?"-1":getTreeid())));
				
		
		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);
		
		tb1.addText(new ToolbarText("������λ:"));
		tb1.addField(tf);
		tb1.addItem(tb2);
		
		tb1.addText(new ToolbarText("���䷽ʽ:"));
		ComboBox meik = new ComboBox();
		meik.setTransform("YUNSFSSelect");
		meik.setEditable(true);
		meik.setWidth(100);
		tb1.addField(meik);

		tb1.addText(new ToolbarText("���:"));
		ComboBox leib = new ComboBox();
		leib.setTransform("LEIBSelect");
		leib.setEditable(true);
		leib.setWidth(100);
		tb1.addField(leib);

		ToolbarButton tb = new ToolbarButton(null, "ˢ��",
				"function(){document.getElementById('QueryButton').click();}");
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

	public String getcontext() {
		return "";

	}

	public SortMode getSort() {
		return SortMode.USER;
	}

	private String _pageLink;

	public String getpageLink() {
		if (!_pageLink.equals("")) {
			return _pageLink;
		} else {
			return "";
		}
	}

	// Page����
	protected void initialize() {
		_pageLink = "";
	}
	
	// ���䷽ʽ������
	private boolean falg1 = false;

	private IDropDownBean YunsfsValue;

	public IDropDownBean getYunsfsValue() {
		if (YunsfsValue == null) {
			YunsfsValue = (IDropDownBean) YunsfsModel.getOption(0);
		}
		return YunsfsValue;
	}

	public void setYunsfsValue(IDropDownBean Value) {
		if (!(YunsfsValue == Value)) {
			YunsfsValue = Value;
			falg1 = true;
		}
	}

	private IPropertySelectionModel YunsfsModel;

	public void setYunsfsModel(IPropertySelectionModel value) {
		YunsfsModel = value;
	}

	public IPropertySelectionModel getYunsfsModel() {
		if (YunsfsModel == null) {
			getYunsfsModels();
		}
		return YunsfsModel;
	}

	public IPropertySelectionModel getYunsfsModels() {
		String sql = "select id,mingc from yunsfsb";
		YunsfsModel = new IDropDownModel(sql,"ȫ��");
		return YunsfsModel;
	}

	// ���������
	private boolean falg2 = false;

	private IDropDownBean LeibValue;

	public IDropDownBean getLeibValue() {
		if (LeibValue == null) {
			LeibValue = (IDropDownBean) LeibModel.getOption(0);
		}
		return LeibValue;
	}

	public void setLeibValue(IDropDownBean Value) {
		if (!(LeibValue == Value)) {
			LeibValue = Value;
			falg2 = true;
		}
	}

	private IPropertySelectionModel LeibModel;

	public void setLeibModel(IPropertySelectionModel value) {
		LeibModel = value;
	}

	public IPropertySelectionModel getLeibModel() {
		if (LeibModel == null) {
			getLeibModels();
		}
		return LeibModel;
	}

	public IPropertySelectionModel getLeibModels() {
		String sql = "select id,leibmc from huaydbszb";
		LeibModel = new IDropDownModel(sql,"ȫ��");
		return LeibModel;
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();

		if (!visit.getActivePageName().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			visit.setString1("");
			getSelectData();
			visit.setProSelectionModel10(null);
			visit.setDropDownBean10(null);
			visit.setProSelectionModel1(null);
			visit.setDropDownBean1(null);
			visit.setProSelectionModel5(null);
			visit.setDropDownBean5(null);
			getLeibModels();
			
		}

		getSelectData();

	}

}
