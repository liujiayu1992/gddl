package com.zhiren.gs.bjdt.diaoygl;

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

public class Fenkshcrbreport extends BasePage {
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

	private String _msg;
	public String getMsg() {
        if (_msg == null) {
            _msg = "";
        }
        return _msg;
    }
	public void setMsg(String _value) {
	    _msg = MainGlobal.getExtMessageBox(_value,false);
	}
    protected void initialize() {
        super.initialize();
        _msg = "";
    }
	
    private String RT_FENKSHOUHCRB_GS="Fenkshouhcrbgs";
	private String RT_FENKSHOUHCRB_JT="Fenkshouhcrbjt";
	private String mstrReportName="";
	
	private boolean blnIsBegin = false;

	public String getPrintTable() {
		if (!blnIsBegin) {
			return "";
		}
		blnIsBegin = false;
		if (mstrReportName.equals(RT_FENKSHOUHCRB_GS)){
			return getShouhcrbGS();
		}else if(mstrReportName.equals(RT_FENKSHOUHCRB_JT)){
			return getShouhcreportJT();
		}else{
			return "无此报表";
		}
//		return getShouhcreport();
	}
	// 
	private String getShouhcreportJT() {
		JDBCcon con = new JDBCcon();

		String End_riq=getEndriqDate();

		 String dcsql = " select distinct px.xuh,dc.mingc from diancxxb dc,dianckjpxb px where px.diancxxb_id = dc.id  and px.kouj='月报' and px.shujsbzt=1 order by px.xuh ";
		 
		 String mksql = " select distinct decode(gy.fuid,0,gy.id,gy.fuid) as id,gy.mingc from fenkshcrb rb,gongysb gy,vwdcgysmykjgl vw \n"
			 		  + "  where rb.gongysb_id=vw.id and vw.gongysb_id=gy.id and rb.riq=to_date('"+End_riq+"', 'yyyy-mm-dd') order by gy.mingc ";
		 
		 String mlsql = "select dc.mingc,decode(grouping(gy.mingc),1,'合计',gy.mingc) as kuangb,sum(sj.duns) as kuangfgyl\n" 
			 			+ "  from diancxxb dc,gongysb gy,\n" 
			 			+ "(select rb.diancxxb_id,decode(gy.fuid,0,gy.id,gy.fuid) as gyid,rb.duns from fenkshcrb rb,gongysb gy,vwdcgysmykjgl vw \n" 
			 			+ "  where rb.gongysb_id=vw.id and vw.gongysb_id=gy.id and rb.riq=to_date('"+End_riq+"', 'yyyy-mm-dd') and rb.leix='重点' ) sj\n" 
			 			+ " where sj.diancxxb_id=dc.id and sj.gyid=gy.id\n" 
			 			+ " group by rollup(dc.mingc,gy.mingc) having not grouping(dc.mingc)+grouping(gy.mingc)=2";

		 
		 ResultSetList dcrs = con.getResultSetList(dcsql);
		 ResultSetList mkrs = con.getResultSetList(mksql);
		 ResultSetList mlrs = con.getResultSetList(mlsql);
		 int diancnum = dcrs.getRows();
		 int meiknum = mkrs.getRows();
		 String Rib[][] = new String[meiknum+2][diancnum+1];
		 Rib[0][0] = "名称";
		 Rib[1][0] = "合计";
		 for(int i=2;mkrs.next();i++){
			 Rib[i][0] = mkrs.getString("mingc");
		 }
		 String strDiancmc = "";
		 for(int j=1;dcrs.next();j++){
			 strDiancmc = dcrs.getString("mingc").replaceAll("发电厂", "");
			 strDiancmc = strDiancmc.replaceAll("电厂", "");
			 strDiancmc = strDiancmc.replaceAll("热电", "");
			 Rib[0][j] = strDiancmc;
		 }
		 while(mlrs.next()){
			 String diancmc = mlrs.getString("mingc").replaceAll("发电厂", "");
			 diancmc = diancmc.replaceAll("电厂", "");
			 diancmc = diancmc.replaceAll("热电", "");
			 String meikdqmc = mlrs.getString("kuangb");
			 for(int x=1;x<diancnum+1;x++){
				if(diancmc.equals(Rib[0][x])){
					for(int y=1;y<meiknum+2;y++){
						if(meikdqmc.equals(Rib[y][0])){
							Rib[y][x] = String.valueOf(mlrs.getInt("kuangfgyl"));
							break;
						}
					}
					break;
				}
			 }
		 }
//		设置页标题
		Report rt = new Report();
		int ArrWidth[]=new int[diancnum+1];
		ArrWidth[0]=120;
		for(int i=1;i<ArrWidth.length;i++){
			ArrWidth[i]=70;
		}
		
		rt.setTitle("大唐集团燃料收耗存日报",ArrWidth);
		rt.setDefaultTitle(1,4,"填报日期:"+FormatDate(DateUtil.getDate(End_riq)),Table.ALIGN_LEFT);
		rt.setDefaultTitle(diancnum+1,1,"单位:吨",Table.ALIGN_CENTER);
		rt.title.setCellFont(2, 1, "宋体", 14, false);
		rt.title.setCellFont(3, 1, "宋体", 12, false);
		rt.title.setCellFont(3, diancnum+1, "宋体", 12, false);
		//数据
		rt.setBody(new Table(Rib,0,0,0));
		rt.body.setWidth(ArrWidth);
		rt.body.setRowHeight(30);
		rt.body.fontSize=12;
//		rt.body.setPageRows(24);
		rt.body.mergeFixedRow();
		rt.body.setBorder(1, 0, 1, 0);
		
		for(int i=0;i<rt.body.getRows();i++){//設置第一列居中
			rt.body.setCellAlign(i+1, 1, Table.ALIGN_CENTER);
		}
		for(int j=0;j<rt.body.getCols();j++){//設置第一行居中
			rt.body.setCellAlign(1, j+1, Table.ALIGN_CENTER);
		}
		for(int i=2;i<rt.body.getRows();i++){//設置数据区域靠右
			for(int j=2;j<rt.body.getCols();j++){
				rt.body.setCellAlign(i, j, Table.ALIGN_RIGHT);
			}
		}
		
		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		dcrs.close();
		mkrs.close();
		con.Close();
		return rt.getAllPagesHtml();

	}

	private String getShouhcrbGS(){//公司收耗存日报
		JDBCcon cn = new JDBCcon();
		Report rt = new Report();
		String End_riq=getEndriqDate();
		//报表内容
		try{
			String sql1 = "";
			if(getBaoblxValue().getValue().equals("全部煤源")){
				sql1 = 
					"select meiymc,decode(gongyskjb_id,1,'重点','补充') as leix from (\n" +
					"select distinct\n" + 
					"       case when vw.gongyskjb_id=1 and grouping(my.mingc)=1 then '重点合计' else\n" + 
					"       case when vw.gongyskjb_id=2 and grouping(sf.quanc)=1 then '补充合计' else\n" + 
					"       decode(vw.gongyskjb_id,2,nvl(sf.quanc,my.mingc),my.mingc) end end as meiymc,\n" + 
					"       vw.gongyskjb_id,\n" + 
					"       case when vw.gongyskjb_id=1 and grouping(my.mingc)=1 then -1 else\n" + 
					"       case when vw.gongyskjb_id=2 and grouping(sf.quanc)=1 then -1 else\n" + 
					"       decode(vw.gongyskjb_id,2,max(sf.xuh),min(my.xuh)) end end as xuh\n" + 
					"  from meiyxxb my,vwdcgysmykjgl vw,shengfb sf\n" + 
					" where vw.meiyxxb_id=my.id and my.shengfb_id=sf.id(+)\n" + 
					"   group by rollup(vw.gongyskjb_id,(sf.quanc,my.mingc))\n" + 
					"   having not grouping(vw.gongyskjb_id)=1\n" + 
					" ) order by leix desc,xuh ,meiymc";

			}else{
				sql1 = "select meiymc,decode(gongyskjb_id,1,'重点','补充') as leix from (\n"
					+ "select distinct\n"
					+ "       case when vw.gongyskjb_id=1 and grouping(my.mingc)=1 then '重点合计' else\n"
					+ "       case when vw.gongyskjb_id=2 and grouping(sf.quanc)=1 then '补充合计' else\n"
					+ "       decode(vw.gongyskjb_id,2,nvl(sf.quanc,my.mingc),my.mingc) end end as meiymc,\n"
					+ "       vw.gongyskjb_id,\n"
					+ "       case when vw.gongyskjb_id=1 and grouping(my.mingc)=1 then -1 else\n"
					+ "       case when vw.gongyskjb_id=2 and grouping(sf.quanc)=1 then -1 else\n"
					+ "       decode(vw.gongyskjb_id,2,max(sf.xuh),0) end end as xuh\n"
					+ "  from meiyxxb my,vwdcgysmykjgl vw,fenkshcrb rb,shengfb sf\n"
					+ " where rb.gongysb_id=vw.id and rb.riq>=to_date('"
					+ DateUtil.getYear(DateUtil.getDate(End_riq))
					+ "-01-01', 'yyyy-mm-dd')\n"
					+ "   and rb.riq<=to_date('"
					+ End_riq
					+ "','yyyy-mm-dd') and vw.meiyxxb_id=my.id and my.shengfb_id=sf.id(+) \n"
					+ "   group by rollup(vw.gongyskjb_id,(sf.quanc,my.mingc))\n"
					+ "   having not grouping(vw.gongyskjb_id)=1\n"
					+ " ) order by leix desc,xuh ,meiymc";
			}
			
//			String sql2 = "select decode(grouping(d.mingc), 1, '合计', d.mingc) as jianc,\n"
//					+ "       sum(nvl(dr.dangrgm,0)) as dangrgm,sum(nvl(lj.leijgm,0)) as leijgm,\n"
//					+ "       sum(nvl(dr.haoyqkdr,0)) haoyqkdr,sum(nvl(lj.leijhm,0)) as leijhm,sum(nvl(dr.kuc,0)) as kuc\n"
//					+ "  from (select dc.mingc    as mingc, rb.dangrgm  as dangrgm,rb.haoyqkdr as haoyqkdr,rb.kuc as kuc\n"
//					+ "          from shouhcrbb rb, diancxxb dc\n"
//					+ "         where rb.diancxxb_id = dc.id and rb.riq = to_date('"+End_riq+"', 'yyyy-mm-dd')) dr,\n"
//					+ "       (select dc.mingc as mingc,sum(rb.dangrgm) as leijgm,sum(rb.haoyqkdr) as leijhm\n"
//					+ "          from shouhcrbb rb, diancxxb dc\n"
//					+ "         where rb.diancxxb_id = dc.id\n"
//					+ "           and rb.riq >= First_day(to_date('"+End_riq+"', 'yyyy-mm-dd'))\n"
//					+ "           and rb.riq <= to_date('"+End_riq+"', 'yyyy-mm-dd') group by dc.mingc) lj,\n"
//					+ "       (select dc.id, dc.mingc, px.xuh from diancxxb dc, dianckjpxb px\n"
//					+ "         where dc.id = px.diancxxb_id(+) and (px.kouj = '月报')) d\n"
//					+ " where dr.mingc(+) = d.mingc and lj.mingc(+) = d.mingc\n"
//					+ " group by rollup((d.mingc, d.xuh)) order by grouping(d.mingc) desc, d.xuh";

			String sql2 = "select decode(grouping(d.mingc)+grouping(d.leibmc), 2, '总计',1,'★'||d.leibmc||'合计',d.mingc) as jianc,\n"
					+ "       sum(nvl(dr.dangrgm,0)) as dangrgm,sum(nvl(lj.leijgm,0)) as leijgm,\n"
					+ "       sum(nvl(dr.haoyqkdr,0)) haoyqkdr,sum(nvl(lj.leijhm,0)) as leijhm,sum(nvl(dr.kuc,0)) as kuc\n"
					+ "  from (select dc.mingc    as mingc, rb.dangrgm  as dangrgm,rb.haoyqkdr as haoyqkdr,rb.kuc as kuc\n"
					+ "          from shouhcrbb rb, diancxxb dc\n"
					+ "         where rb.diancxxb_id = dc.id and rb.riq = to_date('"+End_riq+"', 'yyyy-mm-dd')) dr,\n"
					+ "       (select dc.mingc as mingc,sum(rb.dangrgm) as leijgm,sum(rb.haoyqkdr) as leijhm\n"
					+ "          from shouhcrbb rb, diancxxb dc\n"
					+ "         where rb.diancxxb_id = dc.id\n"
					+ "           and rb.riq >= First_day(to_date('"+End_riq+"', 'yyyy-mm-dd'))\n"
					+ "           and rb.riq <= to_date('"+End_riq+"', 'yyyy-mm-dd') group by dc.mingc) lj,\n"
					+ "       (select dc.id, dc.mingc, px.xuh,lb.mingc as leibmc,lb.xuh as leibxh from diancxxb dc, dianckjpxb px,dianclbb lb\n"
					+ "         where dc.id = px.diancxxb_id(+) and dc.dianclbb_id=lb.id and (px.kouj = '月报')) d\n"
					+ " where dr.mingc(+) = d.mingc and lj.mingc(+) = d.mingc\n"
					+ " group by rollup(d.leibmc,(d.mingc, d.xuh)) order by grouping(d.leibmc) desc,min(d.leibxh),grouping(d.mingc) desc, d.xuh";

			
			ResultSet krs = cn.getResultSet(sql1);
			ResultSet crs = cn.getResultSet(sql2);
			krs.last();
			crs.last();
			int cols = krs.getRow();
			int rows = crs.getRow();
			krs.beforeFirst();
			crs.beforeFirst();
			cols = 3*cols;//矿分当日供煤和累计供煤两列
			String Header[][]= new String[rows+3][cols+6];
			int ArrWidth[]=new int[cols+6];
//			先将Header[][]数组里的元素全部初始化为“0”,以防出现空指针异常
			for(int hang=0;hang<rows+3;hang++){
				for(int lie=0;lie<cols+6;lie++){
					Header[hang][lie]="0";
				}
			}
//			收耗存区域标题赋值并设定列宽
			Header[0][0]="电厂";
			Header[0][1]="当日供煤";
			Header[0][2]="累计供煤";
			Header[0][3]="当日耗煤";
			Header[0][4]="累计耗煤";
			Header[0][5]="库存";
			Header[1][0]="电厂";
			Header[1][1]="当日供煤";
			Header[1][2]="累计供煤";
			Header[1][3]="当日耗煤";
			Header[1][4]="累计耗煤";
			Header[1][5]="库存";
			Header[2][0]="电厂";
			Header[2][1]="当日供煤";
			Header[2][2]="累计供煤";
			Header[2][3]="当日耗煤";
			Header[2][4]="累计耗煤";
			Header[2][5]="库存";
			ArrWidth[0]=90;
			ArrWidth[1]=55;
			ArrWidth[2]=55;
			ArrWidth[3]=55;
			ArrWidth[4]=55;
			ArrWidth[5]=50;
			
//			为后面的煤源列赋值，并设置列宽
			String kuangb = "";
			String leix = "";
			int colZDhj = 6;//重点当日合计的列号
			int colBChj = 0;//补充当日合计的列号
			for(int i=6;krs.next();i++){
				leix = krs.getString("leix");
				kuangb = krs.getString("meiymc");
				
				if(kuangb.equals("重点合计")){
					colZDhj = i;
				}
				if(kuangb.equals("补充合计")){
					colBChj = i;
				}
				Header[0][i] = leix;
				Header[1][i] = kuangb;
				Header[2][i] = "当日";
				ArrWidth[i]=45;
				i++;
				Header[0][i] = leix;
				Header[1][i] = kuangb;
				Header[2][i] = "月累计";
				ArrWidth[i]=50;
				i++;
				Header[0][i] = leix;
				Header[1][i] = kuangb;
				Header[2][i] = "年累计";
				ArrWidth[i]=50;
			}
			
//			填写收耗存区域的数据集
			int ZdhjIndex = 4;//直达合计行号
			int KkhjIndex = 0;//坑口合计行号
			int SlhjIndex = 0;//水陆联运合计行号
			for(int j=0;crs.next();j++){
//				for(int r=0;r<cols+6;r++){
				if(crs.getString("jianc").indexOf("直达")>0){
					ZdhjIndex = j+3;
					
				}else if(crs.getString("jianc").indexOf("坑口")>0){
					KkhjIndex = j+3;
					
				}else if(crs.getString("jianc").indexOf("水陆联运")>0){
					SlhjIndex = j+3;
				}
				Header[j+3][0] = crs.getString("jianc");
				Header[j+3][1] = crs.getString("dangrgm");
				Header[j+3][2] = crs.getString("leijgm");
				Header[j+3][3] = crs.getString("haoyqkdr");
				Header[j+3][4] = crs.getString("leijhm");
				Header[j+3][5] = crs.getString("kuc");
//					Header[j+2][r] = "0";
//				}
			}
//			String sql3 = "select lj.diancmc,lj.meiymc,round(nvl(dr.duns,0),0) as dangrgm,round(nvl(lj.duns,0),0) as leijgm\n"
//					+ "  from (select dc.mingc as diancmc, my.mingc as meiymc, sum(rb.duns) as duns\n"
//					+ "          from fenkshcrb rb, diancxxb dc, vwdcgysmykjgl vw,meiyxxb my\n"
//					+ "         where rb.gongysb_id = vw.id and vw.diancxxb_id=dc.id and vw.meiyxxb_id = my.id\n"
//					+ "           and rb.riq = to_date('"+End_riq+"', 'yyyy-mm-dd') group by (my.mingc,dc.mingc)) dr,\n"
//					+ "       (select dc.mingc as diancmc, my.mingc as meiymc, sum(rb.duns) as duns\n"
//					+ "          from fenkshcrb rb, diancxxb dc, vwdcgysmykjgl vw,meiyxxb my\n"
//					+ "         where rb.gongysb_id = vw.id and vw.diancxxb_id = dc.id and vw.meiyxxb_id = my.id\n"
//					+ "           and rb.riq >= First_day(to_date('"+End_riq+"', 'yyyy-mm-dd'))\n"
//					+ "           and rb.riq <= to_date('"+End_riq+"', 'yyyy-mm-dd') group by (my.mingc, dc.mingc)  ) lj\n"
//					+ " where dr.diancmc(+) = lj.diancmc and dr.meiymc(+) = lj.meiymc";

			String sql3 = "select nlj.diancmc,nlj.meiymc,round(nvl(dr.duns,0),0) as dangrgm,round(nvl(ylj.duns,0),0) as yuelj,round(nvl(nlj.duns,0),0) as nianlj\n"
					+ "  from (select dc.mingc as diancmc, my.mingc as meiymc, sum(rb.duns) as duns\n"
					+ "          from fenkshcrb rb, diancxxb dc, vwdcgysmykjgl vw,meiyxxb my\n"
					+ "         where rb.gongysb_id = vw.id and vw.diancxxb_id=dc.id and vw.meiyxxb_id = my.id\n"
					+ "           and rb.riq = to_date('"+End_riq+"', 'yyyy-mm-dd') group by (my.mingc,dc.mingc)) dr,\n"
					+ "       (select dc.mingc as diancmc, my.mingc as meiymc, sum(rb.duns) as duns\n"
					+ "          from fenkshcrb rb, diancxxb dc, vwdcgysmykjgl vw,meiyxxb my\n"
					+ "         where rb.gongysb_id = vw.id and vw.diancxxb_id = dc.id and vw.meiyxxb_id = my.id\n"
					+ "           and rb.riq >= First_day(to_date('"+End_riq+"', 'yyyy-mm-dd'))\n"
					+ "           and rb.riq <= to_date('"+End_riq+"', 'yyyy-mm-dd') group by (my.mingc, dc.mingc)  ) ylj,\n"
					+ "       (select dc.mingc as diancmc, my.mingc as meiymc, sum(rb.duns) as duns\n"
					+ "          from fenkshcrb rb, diancxxb dc, vwdcgysmykjgl vw,meiyxxb my\n"
					+ "         where rb.gongysb_id = vw.id and vw.diancxxb_id = dc.id and vw.meiyxxb_id = my.id\n"
					+ "           and rb.riq >= to_date('"+DateUtil.getYear(DateUtil.getDate(End_riq))+"-01-01', 'yyyy-mm-dd')\n"
					+ "           and rb.riq <= to_date('"+End_riq+"', 'yyyy-mm-dd') group by (my.mingc, dc.mingc)  ) nlj\n"
					+ " where dr.diancmc(+) = nlj.diancmc and dr.meiymc(+) = nlj.meiymc\n"
					+ "   and ylj.diancmc(+) = nlj.diancmc and ylj.meiymc(+) = nlj.meiymc";

			
			ResultSet rs = cn.getResultSet(sql3);
//			填写煤源区域的数据集，不包括重点和补充的合计部分
			for(int m=0;rs.next();m++){
				for(int r=0;r<rows+3;r++){
					if(rs.getString("diancmc").equals(Header[r][0])){
						for(int c=0;c<cols+6;c++){
							
							if(rs.getString("meiymc").equals(Header[1][c])){
								Header[r][c] = rs.getString("dangrgm");
								c++;
								Header[r][c] = rs.getString("yuelj");
								c++;
								Header[r][c] = rs.getString("nianlj");
								break;
							}
						}
						break;
					}
				}
			}
			
			if(Header.length>1){
				for(int c=6;c<cols+6;c++){
//					计算直达合计行数据
					for(int r=ZdhjIndex;r<KkhjIndex;r++){
						Header[ZdhjIndex][c]=String.valueOf(Integer.parseInt(Header[r][c])+Integer.parseInt(Header[ZdhjIndex][c]));
					}
//					计算坑口合计行数据
					for(int r=KkhjIndex;r<SlhjIndex;r++){
						Header[KkhjIndex][c]=String.valueOf(Integer.parseInt(Header[r][c])+Integer.parseInt(Header[KkhjIndex][c]));
					}
//					计算水陆联运合计行数据
					for(int r=SlhjIndex;r<rows+3;r++){
						Header[SlhjIndex][c]=String.valueOf(Integer.parseInt(Header[r][c])+Integer.parseInt(Header[SlhjIndex][c]));
					}
//					计算总计行数据
					Header[3][c]=String.valueOf(Integer.parseInt(Header[ZdhjIndex][c])+Integer.parseInt(Header[KkhjIndex][c])+Integer.parseInt(Header[SlhjIndex][c]));
				}
				
				for(int r=3;r<rows+3;r++){
//					计算重点合计
					for(int c=colZDhj+3;c<colBChj;c++){
//						int yuelj = c++;
//						int nianlj = c++;
						Header[r][colZDhj]=String.valueOf(Integer.parseInt(Header[r][colZDhj])+Integer.parseInt(Header[r][c]));
						Header[r][colZDhj+1]=String.valueOf(Integer.parseInt(Header[r][colZDhj+1])+Integer.parseInt(Header[r][c+1]));
						Header[r][colZDhj+2]=String.valueOf(Integer.parseInt(Header[r][colZDhj+2])+Integer.parseInt(Header[r][c+2]));
						c = c+2;
					}
//					计算补充合计
					for(int c=colBChj+3;c<cols+6;c++){
//						int yuelj = c++;
//						int nianlj = c++;
						Header[r][colBChj]=String.valueOf(Integer.parseInt(Header[r][colBChj])+Integer.parseInt(Header[r][c]));
						Header[r][colBChj+1]=String.valueOf(Integer.parseInt(Header[r][colBChj+1])+Integer.parseInt(Header[r][c+1]));
						Header[r][colBChj+2]=String.valueOf(Integer.parseInt(Header[r][colBChj+2])+Integer.parseInt(Header[r][c+2]));
						c = c+2;
					}
				}
			}
			// 数据
			rt.setTitle("分矿收耗存日报", ArrWidth);
			rt.setDefaultTitle(1, 5, "制表单位：大唐国际发电股份有限公司燃料管理部", Table.ALIGN_LEFT);
			rt.setDefaultTitle((cols+6)/2-1, 3, FormatDate(DateUtil.getDate(End_riq)), Table.ALIGN_CENTER);
			rt.setBody(new Table(Header,0,0,0));
//			rt.setBody(new Table(Header,0,true,Table.ALIGN_CENTER));
			rt.body.setWidth(ArrWidth);
			rt.body.setPageRows(30);
			rt.body.setRowHeight(26);
			rt.body.setRowHeight(1, 30);
			rt.body.ShowZero = false;
			
			rt.body.mergeCell(1, 1, 3, 1);
			rt.body.mergeCell(1, 2, 3, 2);
			rt.body.mergeCell(1, 3, 3, 3);
			rt.body.mergeCell(1, 4, 3, 4);
			rt.body.mergeCell(1, 5, 3, 5);
			rt.body.mergeCell(1, 6, 3, 6);
			
			rt.body.mergeCell(1, colZDhj+1, 1, colBChj);
			rt.body.mergeCell(1, colBChj+1, 1, cols+6);
			for(int l=colZDhj;l<cols+6;l++){
				rt.body.mergeCell(2, l+1, 2, l+3);
				l = l+2;
			}
			
			
			for(int j=0;j<rt.body.getCols();j++){//設置第一行居中
				rt.body.setCellAlign(1, j+1, Table.ALIGN_CENTER);
				rt.body.setCellAlign(2, j+1, Table.ALIGN_CENTER);
				rt.body.setCellAlign(3, j+1, Table.ALIGN_CENTER);
			}
			rt.body.setCellAlign(4, 1, Table.ALIGN_CENTER);
			
			for(int i=4;i<=rt.body.getRows();i++){//設置数据区域靠右
				for(int j=2;j<=rt.body.getCols();j++){
					rt.body.setCellAlign(i, j, Table.ALIGN_RIGHT);
				}
			}
			krs.close();
			crs.close();
			rs.close();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			cn.Close();
		}
		//设置页数
		_CurrentPage=1;
		_AllPages=rt.body.getPages();
		if (_AllPages==0){
			_CurrentPage=0;
		}
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
		return DateUtil.Formatdate("yyyy年MM月dd日", _date);
	}
	///////
	public String getBeginriqDate(){
		if(((Visit) getPage().getVisit()).getString4()==null||((Visit) getPage().getVisit()).getString4()==""){
			Calendar stra=Calendar.getInstance();
			stra.set(DateUtil.getYear(new Date()), DateUtil.getMonth(new Date()), 1);
			stra.add(Calendar.MONTH,-1);
			((Visit) getPage().getVisit()).setString4(DateUtil.FormatDate(stra.getTime()));
		}
		return ((Visit) getPage().getVisit()).getString4();
	}
	public void setBeginriqDate(String value){
		((Visit) getPage().getVisit()).setString4(value);
	}
	public String getEndriqDate(){
		if(((Visit) getPage().getVisit()).getString5()==null||((Visit) getPage().getVisit()).getString5()==""){
			((Visit) getPage().getVisit()).setString5(DateUtil.FormatDate(DateUtil.AddDate(new Date(),-1,DateUtil.AddType_intDay)));
		}
		return ((Visit) getPage().getVisit()).getString5();
	}
	public void setEndriqDate(String value){
		((Visit) getPage().getVisit()).setString5(value);
	}

	private void getToolbars(){
		Toolbar tb1 = new Toolbar("tbdiv");
		tb1.addText(new ToolbarText("日期:"));
		DateField df = new DateField();

		DateField df1 = new DateField();
		df1.setValue(this.getEndriqDate());
		df1.Binding("qiandrq2","");// 与html页中的id绑定,并自动刷新
		df1.setWidth(80);
		tb1.addField(df1);
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
		tb1.addText(new ToolbarText("-"));
//		tb1.addText(new ToolbarText("单位:"));
//		tb1.addField(tf);
//		tb1.addItem(tb2);

//		供煤类型
		tb1.addText(new ToolbarText("统计类型:"));
		ComboBox cb2 = new ComboBox();
		cb2.setTransform("BaoblxDropDown");
		cb2.setId("Baoblx");
		cb2.setWidth(100);
		tb1.addField(cb2);
		tb1.addText(new ToolbarText("-"));
		
		ToolbarButton tb = new ToolbarButton(null,"刷新","function(){document.Form0.submit();}");
		tb1.addItem(tb);

		setToolbar(tb1);
	}
	
	
	public String getShangbry(){//获取有日报上传权限的用户名
		JDBCcon con = new JDBCcon();
		String strUserName = "";
		String sql = "select xt.zhi from xitxxb xt where xt.mingc='日报上报人员'";
		ResultSetList rsl = con.getResultSetList(sql);
		if(rsl.next()){
			strUserName = rsl.getString("zhi");
		}
		con.Close();
		return strUserName;
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
			visit.setString2("");

		}
		if(cycle.getRequestContext().getParameters("lx") !=null) {
			visit.setString2(cycle.getRequestContext().getParameters("lx")[0]);
			mstrReportName =visit.getString2();
        }else{
        	if(visit.getString2().equals("")) {
        		mstrReportName =visit.getString2();
            }
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
//	电厂名称
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
//	得到电厂树的值的级别,(jib:1,2,3,) 1是集团,2是分公司,3是电厂
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
//	得到电厂树下拉框的电厂名称或者分公司,集团的名称
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
//	分公司下拉框
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
		setDiancxxModel(new IDropDownModel(sql,"中国大唐集团"));
	}

//	得到系统信息表中配置的报表标题的单位名称
	public String getBiaotmc(){
		String biaotmc="";
		JDBCcon cn = new JDBCcon();
		String sql_biaotmc="select  zhi from xitxxb where mingc='报表标题单位名称'";
		ResultSet rs=cn.getResultSet(sql_biaotmc);
		try {
			while(rs.next()){
				biaotmc=rs.getString("zhi");
			}
			rs.close();
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}finally{
			cn.Close();
		}

		return biaotmc;

	}

//	报表类型
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
		List leixList = new ArrayList();
		leixList.add(new IDropDownBean(1,"全部煤源"));
		leixList.add(new IDropDownBean(2,"供煤煤源"));

		_IBaoblxModel = new IDropDownModel(leixList);
		
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.Close();
		}
		return _IBaoblxModel;
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
