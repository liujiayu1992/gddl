package com.zhiren.dc.zonghcx;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.WriteLog;
import com.zhiren.dc.jilgl.Jilcz;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.RPTInit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Zonghcxbb extends BasePage {
	/*
	 * 作者:tzf
	 * 时间:2010-01-26
	 * 修改内容:根据龙江公司要求，盈亏和运损相互计算，否则报表账面不平，用参数控制。
	 */
	/*
	 * 作者:tzf
	 * 时间:2009-08-24
	 * 增加  质量明细 汇总行
	 */
	/*
	 * 作者:tzf
	 * 时间:2009-4-16
	 * 修改内容:数量综合查询  明细 中 由于 新增加字段  引起  列明与字段 值  不对应  现已经更正
	 */
	/*
	 * 作者:夏峥	
	 * 时间:2009-5-11
	 * 修改内容:将4个表中的and fahb.diancxxb_id =\n" + v.getDiancxxb_id()+变更为.append(Jilcz.filterDcid(v,"fahb")).append(" \n")
	 * 修改质量汇总不显示值的问题
	 */
	/*
	 * 作者:夏峥	
	 * 时间:2009-06-10
	 * 修改内容:综合查询数量明细表
	 * 检测系统信息表中的状态,设定相应的选择语句
	 * 根据系统信息表中的设置，设定相应的表头
	 * 在getPrintTable()中也做相应的修改
	 * 调整列宽
	 */
	
	/*
	 * 作者:夏峥	
	 * 时间:2009-06-15
	 * 修改内容:综合查询数报表
	 * 解析前台传入的参数
	 * 在表头根据用户的选择不同生成不同的内容
	 * 特指：发货日期，到货日期，化验日期
	 */
	/*
	 * 作者:夏峥	
	 * 时间:2009-06-15
	 * 修改内容:综合查询数量明细表
	 * 检测系统信息表中的状态,设定相应的选择语句
	 * 根据系统信息表中的设置，设定相应的表头
	 * 在getPrintTable()中也做相应的修改
	 * 调整列宽
	 */
	/*
	 * 作者: 夏峥	
	 * 时间: 2012-06-28
	 * 描述: 调整getZonghcx_ZLHZ中大卡热值的取值方式
	 */
	/*
	 * 作者: 夏峥	
	 * 时间: 2013-06-05
	 * 描述: 调整getZonghcx_ZL_MX中大卡热值的取值方式
	 */
	
	
//	界面用户提示
	private static final String RptType_SL_HZ = "SL_HZ";
	private static final String RptType_SL_MX = "SL_MX";
	private static final String RptType_ZL_HZ = "ZL_HZ";
	private static final String RptType_ZL_MX = "ZL_MX";
	private String RptType ="HZ";
	private String RptSqltmp = "";
	private String msg="";
	private String tmpSql=""; //定义一个临时变量去得到sql2的值
	private boolean ShowStar=true;
	private String Fahrq="";//定义发货日期和到货日期的变量
	private String Daohrq="";//定义发货日期和到货日期的变量
	private String Huaysj="";
	
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);;
	}
	protected void initialize() {
		super.initialize();
		setMsg("");
		setTbmsg(null);
	}
	private String tbmsg;
	public String getTbmsg() {
		return tbmsg;
	}
	public void setTbmsg(String tbmsg) {
		this.tbmsg = tbmsg;
	}
	
	private int _CurrentPage = -1;
	public int getCurrentPage() {
		return _CurrentPage;
	}
	public void setCurrentPage(int _value) {
		_CurrentPage= _value;
	}
	
	private int _AllPages = -1;
	public int getAllPages() {
		return _AllPages;
	}
	public void setAllPages(int _value) {
		_AllPages= _value;
	}

//	页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
	public boolean getRaw() {
		return true;
	}
	/*
	 * 数量综合中新增来煤量字段，增加电厂过滤
	 * 修改时间：2008-12-04
	 * 修改人：王磊
	 * 
	 */
	public String getZonghcx_SLHZ(){
		Visit v = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		
		
		String xhjs_str=" select * from xitxxb where mingc='数量报表盈亏运损相互计算' and zhi='是' and leib='数量' and zhuangt=1 ";//盈亏运损是否通过相互计算得到
		ResultSetList rsl_ys=con.getResultSetList(xhjs_str);
		String yuns_js=" sum(round_new(chepb.yuns,"+v.getShuldec()+")) yuns,\n";//火车的 或者 所有运输方式的 运损 计算表达式
		if(rsl_ys.next()){
			
			yuns_js=" sum(round_new(chepb.yingk,"+v.getShuldec()+")) + sum(round_new(chepb.biaoz,"+v.getShuldec()+")) " +
			"- sum(round_new(chepb.maoz-chepb.piz,"+v.getShuldec()+")) yuns,\n ";//火车的 或者 所有运输方式的 运损 计算表达式
	
		}
		rsl_ys.close();
		
		
		StringBuffer sb = new StringBuffer();
		sb.append("select decode(grouping(g.mingc),1,'总计',g.mingc) gongysb_id,\n")
		.append("decode(grouping(g.mingc)+grouping(m.mingc),1,'供应商小计',m.mingc) meikxxb_id,\n")
		.append("decode(grouping(m.mingc)+grouping(p.mingc),1,'煤矿小计',p.mingc)pinzb_id,\n")
		.append("decode(grouping(m.mingc)+grouping(p.mingc)+grouping(y.mingc),1,'运输单位小计',y.mingc) yunsdwb_id,\n")
		.append("sum(round_new(chepb.maoz-chepb.piz-chepb.koud,"+v.getShuldec()+")) laimsl, \n")
		.append("sum(round_new(chepb.biaoz,"+v.getShuldec()+")) biaoz, \n")
		.append("sum(round_new(chepb.maoz,"+v.getShuldec()+")) maoz, \n")
		.append("sum(round_new(chepb.piz,"+v.getShuldec()+")) piz, \n")
		.append("sum(round_new(chepb.maoz-chepb.piz,"+v.getShuldec()+")) jingz, \n")
		.append("sum(round_new(chepb.maoz-chepb.piz-chepb.koud,"+v.getShuldec()+")) jiessl, \n")
		.append("sum(round_new(chepb.yingk,"+v.getShuldec()+")) yingk, \n")
		.append("sum(round_new(chepb.yuns,"+v.getShuldec()+")) yuns, \n")
		.append(yuns_js)
		.append("count(chepb.id) ches, \n")
		.append("sum(round_new(chepb.zongkd,"+v.getShuldec()+")) zongkd \n")
		.append("from fahb , chepb , yunsdwb y, gongysb g, meikxxb m, pinzb p\n")
		.append("where gongysb_id = g.id and meikxxb_id = m.id and fahb.id=chepb.fahb_id and chepb.yunsdwb_id=y.id\n")
		.append("and pinzb_id = p.id ")
		.append(Jilcz.filterDcid(v,"fahb")).append(" \n")
		.append(RptSqltmp)
		.append("\n")
		.append("group by rollup(g.mingc,m.mingc,p.mingc,y.mingc)\n")
		.append(tmpSql)
		.append("\n")
		.append("order by decode(grouping(g.mingc),1,-999,1),max(g.xuh),\n")
		.append("decode(grouping(m.mingc),1,-999,1),max(m.xuh),\n")
		.append("decode(grouping(p.mingc),1,-999,1),max(p.xuh)\n");
		Report rt = new Report();
		ResultSetList rstmp = con.getResultSetList(sb.toString());
		if (rstmp == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult);
			setMsg(ErrorMessage.NullResult);
			return "";
		}
		ResultSetList rs = null;
		String[][] ArrHeader=null;
		String[] strFormat=null;
		int[] ArrWidth=null;
		String [] Zidm=null;
		StringBuffer SB=new StringBuffer();
		SB.append("select bp.zidm,bp.biaot,bp.xuh,bp.kuand,bp.format from baobpzzb bp,baobpzb b where bp.baobpzb_id=b.id and bp.shifxs=1 and b.guanjz='SL_HZ' order by xuh");
        ResultSetList rsl=con.getResultSetList(SB.toString());
        if(rsl.getRows()>0){
        	ArrWidth=new int[rsl.getRows()];
        	strFormat=new String[rsl.getRows()];
        	String biaot=rsl.getString(0,1);
        	String [] Arrbt=biaot.split("!@");
        	ArrHeader =new String [Arrbt.length][rsl.getRows()];
        	Zidm=new String[rsl.getRows()];
        	rs = new ResultSetList();
        	while(rsl.next()){
        		Zidm[rsl.getRow()]=rsl.getString("zidm");
        		ArrWidth[rsl.getRow()]=rsl.getInt("kuand");
        		strFormat[rsl.getRow()]=rsl.getString("format")==null?"":rsl.getString("format");
        		String[] title=rsl.getString("biaot").split("!@");
        		for(int i=0;i<title.length;i++){
        			ArrHeader[i][rsl.getRow()]=title[i];
        		}
        	}
        	rs.setColumnNames(Zidm);
        	while(rstmp.next()){
        		rs.getResultSetlist().add(rstmp.getArrString(Zidm));
        	}
        	rstmp.close();
        	rsl.close();
        	rsl=con.getResultSetList("select biaot from baobpzb where guanjz='SL_HZ'");
        	String Htitle="";
        	while(rsl.next()){
        		Htitle=rsl.getString("biaot");
        	}
        	rt.setTitle(Htitle, ArrWidth);
        	rsl.close();
        }else{
        	rs = rstmp;
        	ArrHeader =new String[1][14];
        	ArrHeader[0]=new String[] {"供应商","煤矿","运输单位","品种","来煤数量","票重","毛重","皮重","净重","结算量","盈亏","运损","车数","扣吨"};
        	
            ArrWidth =new int[] {150, 150, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65};
            rt.setTitle("数量综合查询汇总", ArrWidth);
        }

		//设置页标题
		
		rt.title.fontSize=10;
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.setDefaultTitle(1,2, "制表单位：" + ((Visit) this.getPage().getVisit()).getDiancqc(),
				Table.ALIGN_LEFT);
//		根据值存在的情况制表
		if(Fahrq.length()>1 && Huaysj.length()>1){
			rt.setDefaultTitle(3, 4, "发货日期:"+ Fahrq+"<br>到货日期:"+ Huaysj,Table.ALIGN_LEFT);
		}else if(Fahrq.length()>1 && Huaysj.length()<1){
			rt.setDefaultTitle(3, 4, "发货日期: "+ Fahrq,Table.ALIGN_LEFT);
		}else if(Fahrq.length()<1 && Huaysj.length()>1){
			rt.setDefaultTitle(3, 4, "到货日期: "+ Huaysj,Table.ALIGN_LEFT);
		}else if(Daohrq.length()>1){
			rt.setDefaultTitle(3, 4, "到货日期: "+ Daohrq,Table.ALIGN_LEFT);
		}

		//数据
		rt.setBody(new Table(rs, 1, 0, 4));
		//rt.body.setColAlign(0, Table.ALIGN_CENTER);
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		rt.body.setColAlign(3, Table.ALIGN_CENTER);
		rt.body.setColAlign(4, Table.ALIGN_RIGHT);
		rt.body.setColAlign(5, Table.ALIGN_RIGHT);
		rt.body.setColAlign(6, Table.ALIGN_RIGHT);
		rt.body.setColAlign(7, Table.ALIGN_RIGHT);
		rt.body.setColAlign(8, Table.ALIGN_RIGHT);
		rt.body.setColAlign(9, Table.ALIGN_RIGHT);
		rt.body.setColAlign(10, Table.ALIGN_RIGHT);
		rt.body.setColAlign(11, Table.ALIGN_RIGHT);
		rt.body.setColAlign(12, Table.ALIGN_RIGHT);
		/*rt.body.setColAlign(13, Table.ALIGN_RIGHT);
		rt.body.setColAlign(14, Table.ALIGN_RIGHT);*/
//		rt.body.setCellVAlign(i, j, intAlign)
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
//		rt.body.setColFormat(arrFormat);
		rt.body.setPageRows(40);
		rt.body.mergeFixedCols();
		rt.body.mergeFixedRow();

//		rt.createDefautlFooter(ArrWidth);
		rt.createFooter(1,ArrWidth);
		
		//设置页脚
		rt.setDefautlFooter(1, 4, "打印日期：" + DateUtil.Formatdate("yyyy年MM月dd日", new Date()),
				Table.ALIGN_LEFT);
		rt.setDefautlFooter(5, 3, "审核：", Table.ALIGN_LEFT);
		rt.setDefautlFooter(9, 2, "制表：", Table.ALIGN_LEFT);
		rt.footer.setRowCells(1, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.footer.fontSize=10;
//		rt.footer.setRowHeight(1, 1);
		con.Close();
		if(rt.body.getPages()>0) {
			setCurrentPage(1);
			setAllPages(rt.body.getPages());
		}
		rt.body.setRowHeight(21);
		RPTInit.getInsertSql(v.getDiancxxb_id(),sb.toString(),rt,"数量综合查询汇总","SL_HZ");
		return rt.getAllPagesHtml();// ph;
	}
	
	/*
	 * 数量明细中增加电厂过滤
	 * 修改时间：2008-12-04
	 * 修改人：王磊
	 */
	
	/*
	 * 数量明细中查询语句修改
	 * 修改时间：2008-04-20
	 */
	
	/*
	 * 数量明细中查询语句修改
	 * 在查看明细数据当按编码分组时，汇总行不明显，
	 * 按照之前的版本在字段前添加“★”标识。
	 * 修改时间：2009-06-12
	 * 修改人：夏峥
	 */
	public String getZonghcx_SLMX(){
		Visit v = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		StringBuffer sb = new StringBuffer();
		boolean hasCaiybm=false;
//		sb.append("select g.mingc gmc,m.mingc mcm,cz.mingc cmc,p.mingc pmc,")
//			.append(" to_char(fahb.daohrq,'yyyy-mm-dd') daohrq,chepb.cheph,chepb.maoz,chepb.piz,chepb.maoz-chepb.piz-chepb.zongkd,chepb.yingk,chepb.yuns,chepb.zongkd ")
//			.append(" from gongysb g,meikxxb m,chezxxb cz,pinzb p,fahb ,chepb ")
//			.append(" where g.id = fahb.gongysb_id ")
//			.append(" and m.id = fahb.meikxxb_id ")
//			.append(" and fahb.diancxxb_id = ").append(v.getDiancxxb_id())
//			.append(" and cz.id = fahb.faz_id ")
//			.append(" and p.id = fahb.pinzb_id ")
//			.append(RptSqltmp)
//			.append("\n")
//			.append(" and chepb.fahb_id = fahb.id ");
//			.append(" order by cp.id");

//		检测系统信息表中的状态
		String BianmSel="";
		String BianmGroup="";
		String XitxxbSql="select * from xitxxb where mingc='采样编码显示' and zhi='是'";
		
		//根据显示星星的判断，给字符串赋值
		if(ShowStar){
			BianmSel="  decode(grouping(caiyb.bianm)+grouping(chepb.id),1,'★'||caiyb.bianm,caiyb.bianm) bianm, \n";
		}else{
			BianmSel="  caiyb.bianm,\n" ;
		}
		
//		boolean zhongQingcBoo=false;
		String shijStr="";
//		if(con.getHasIt(" select * from xitxxb where mingc='数量综合查询明细轻重车时间显示' and leib='数量' and zhuangt=1 and zhi='是' and diancxxb_id="+((Visit)this.getPage().getVisit()).getDiancxxb_id())){
//			zhongQingcBoo=true;
			shijStr=" ,max(to_char(chepb.zhongcsj,'yyyy-MM-dd HH24:mi:ss')) zhongcsj,max(to_char(chepb.qingcsj,'yyyy-MM-dd HH24:mi:ss')) qingcsj ";
//		}
		
		if(con.getHasIt(XitxxbSql)){
			hasCaiybm=true;
			BianmSel+= "GetCaibmFromZhilId(caiyb.zhilb_id) caiybm,GetHuaybmFromZhilId(caiyb.zhilb_id) huaybm, \n";
			BianmGroup=",GetCaibmFromZhilId(caiyb.zhilb_id),GetHuaybmFromZhilId(caiyb.zhilb_id)";
		}
		sb.append(
				"select g.mingc gmc,\n" + 
				"       m.mingc mcm,\n" + 
				"       cz.mingc cmc,\n" +

				"       p.mingc pmc,\n" +
						"y.mingc ymc,\n"+
				"       to_char(fahb.daohrq, 'yyyy-mm-dd') daohrq,\n" +
				BianmSel+
				"       decode(grouping(chepb.id),1,''||count(chepb.id),max(chepb.cheph)) cheph,\n" + 
				"       sum(chepb.maoz) maoz,\n" + 
				"       sum(chepb.piz) piz,\n" + 
				"       sum(chepb.maoz - chepb.piz - chepb.zongkd) jingz,\n" + 
				"       sum(chepb.biaoz) biaoz,\n" + 
				"       sum(chepb.yingk) yingk,\n" + 
				"       sum(chepb.yuns) yuns,\n" + 
				"       sum(chepb.zongkd) zongkd\n" +
				shijStr+" \n"+
				"  from gongysb g, meikxxb m, chezxxb cz, pinzb p, fahb, chepb,caiyb,yunsdwb y\n" +
				" where g.id = fahb.gongysb_id\n" + 
				"   and m.id = fahb.meikxxb_id\n" + 
				Jilcz.filterDcid(v,"fahb")+"\n"+
				"   and cz.id = fahb.faz_id\n" + 
				"   and p.id = fahb.pinzb_id and chepb.yunsdwb_id=y.id\n" +
				"\n" + 
				RptSqltmp
				+"   and chepb.fahb_id = fahb.id\n" + 
				"   and fahb.zhilb_id=caiyb.zhilb_id\n" + 
				"\n" + 
				" group by rollup(g.mingc,m.mingc,cz.mingc,y.mingc,p.mingc,to_char(fahb.daohrq, 'yyyy-mm-dd'),"+
				" caiyb.bianm,chepb.id" +BianmGroup+") \n")
				.append(tmpSql+"\n"+
						"order by daohrq, decode(grouping(g.mingc),1,-999,1),max(g.xuh)," +
						"decode(grouping(m.mingc),1,-999,1),max(m.xuh)," +
						"decode(grouping(p.mingc),1,-999,1),max(p.xuh)," +
						"decode(grouping(to_char(fahb.daohrq, 'yyyy-mm-dd')),1,-999,1)," +
						"decode(grouping(caiyb.bianm),1,-999,1),max(caiyb.xuh)," +
						"decode(grouping(chepb.id),1,-999,1)" );
				
		
		Report rt = new Report();
		ResultSetList rstmp = con.getResultSetList(sb.toString());
		if (rstmp == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult);
			setMsg(ErrorMessage.NullResult);
			return "";
		}
		ResultSetList rs = null;
		String[][] ArrHeader=null;
		String[] strFormat=null;
		int[] ArrWidth=null;
		String [] Zidm=null;
		StringBuffer SB=new StringBuffer();
		SB.append("select bp.zidm,bp.biaot,bp.xuh,bp.kuand,bp.format from baobpzzb bp,baobpzb b where bp.baobpzb_id=b.id and bp.shifxs=1 and b.guanjz='SL_MX' order by xuh");
        ResultSetList rsl=con.getResultSetList(SB.toString());

        if(rsl.getRows()>0){
        	ArrWidth=new int[rsl.getRows()];
        	strFormat=new String[rsl.getRows()];
        	String biaot=rsl.getString(0,1);
        	String [] Arrbt=biaot.split("!@");
        	ArrHeader =new String [Arrbt.length][rsl.getRows()];
        	Zidm=new String[rsl.getRows()];
        	rs = new ResultSetList();
        	while(rsl.next()){
        		Zidm[rsl.getRow()]=rsl.getString("zidm");
        		ArrWidth[rsl.getRow()]=rsl.getInt("kuand");
        		strFormat[rsl.getRow()]=rsl.getString("format")==null?"":rsl.getString("format");
        		String[] title=rsl.getString("biaot").split("!@");
        		for(int i=0;i<title.length;i++){
        			ArrHeader[i][rsl.getRow()]=title[i];
        		}
        	}
        	rs.setColumnNames(Zidm);
        	while(rstmp.next()){
        		rs.getResultSetlist().add(rstmp.getArrString(Zidm));
        	}
        	rstmp.close();
        	rsl.close();
        	rsl=con.getResultSetList("select biaot from baobpzb where guanjz='SL_MX'");
        	String Htitle="";
        	while(rsl.next()){
        		Htitle=rsl.getString("biaot");
        	}
        	rt.setTitle(Htitle, ArrWidth);
        	rsl.close();
        }else{
        	rs = rstmp;
//        	根据系统信息表中的设置，设定相应的表头
//        	调整列宽
        	if(hasCaiybm){
        		ArrHeader=new String[1][18];
            	ArrHeader[0]=new String[] {"供应商","煤矿","发站","品种","运输单位","到货日期","编码","采样编码", "化验编码","车号","毛重","皮重","净重","结算量","票重","盈亏","运损","扣吨","重车时间","轻车时间"};
                ArrWidth=new int[] {120, 120, 65, 65, 90, 75, 140, 140, 55, 55, 55, 55, 55, 55, 55, 55, 120, 120,120,120};
        	}else{

	        	ArrHeader=new String[1][16];
	//        	ArrHeader[0]=new String[] {"供应商","煤矿","发站","品种","到货日期","编码", "车号","毛重","皮重","净重","盈亏","运损","扣吨"};
	        	
	        	ArrHeader[0]=new String[] {"供应商","煤矿","发站","品种","运输单位","到货日期","编码", "车号","毛重","皮重","净重","结算量","票重","盈亏","运损","扣吨","重车时间","轻车时间"};
	        	
	            ArrWidth=new int[] {120, 120, 65, 65, 90, 75, 55, 55, 55, 55, 55, 55, 55, 55, 120, 120,120,120};
        	}
            rt.setTitle("数量综合查询明细", ArrWidth);
        	
    	}

		//设置页标题
		
		rt.title.fontSize=10;
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.setDefaultTitle(1, 4, "制表单位：" + ((Visit) this.getPage().getVisit()).getDiancqc(),
				Table.ALIGN_LEFT);
		
		//根据值存在的情况制表
		if(Fahrq.length()>1 && Huaysj.length()>1){
			rt.setDefaultTitle(5, 6, "发货日期:"+ Fahrq+"<br>到货日期:"+ Huaysj,Table.ALIGN_CENTER);
		}else if(Fahrq.length()>1 && Huaysj.length()<1){
			rt.setDefaultTitle(5, 6, "发货日期: "+ Fahrq,Table.ALIGN_CENTER);
		}else if(Fahrq.length()<1 && Huaysj.length()>1){
			rt.setDefaultTitle(5, 6, "到货日期: "+ Huaysj,Table.ALIGN_CENTER);
		}else if(Daohrq.length()>1){
			rt.setDefaultTitle(5, 6, "到货日期: "+ Daohrq,Table.ALIGN_CENTER);
		}

		//数据
		rt.setBody(new Table(rs, 1, 0, 4));
		//rt.body.setColAlign(0, Table.ALIGN_CENTER);
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		rt.body.setColAlign(3, Table.ALIGN_CENTER);
		rt.body.setColAlign(4, Table.ALIGN_CENTER);
		rt.body.setColAlign(5, Table.ALIGN_CENTER);
		rt.body.setColAlign(6, Table.ALIGN_CENTER);
		if(hasCaiybm){
			rt.body.setColAlign(7, Table.ALIGN_CENTER);
			rt.body.setColAlign(8, Table.ALIGN_CENTER);
			rt.body.setColAlign(14, Table.ALIGN_RIGHT);
			rt.body.setColAlign(15, Table.ALIGN_RIGHT);
		}else{
			rt.body.setColAlign(7, Table.ALIGN_RIGHT);
			rt.body.setColAlign(8, Table.ALIGN_RIGHT);
		}
		rt.body.setColAlign(9, Table.ALIGN_RIGHT);
		rt.body.setColAlign(10, Table.ALIGN_RIGHT);
		rt.body.setColAlign(11, Table.ALIGN_RIGHT);
		rt.body.setColAlign(12, Table.ALIGN_RIGHT);
		rt.body.setColAlign(13, Table.ALIGN_RIGHT);
		
		
//		rt.body.setColAlign(ArrHeader.length-1, Table.ALIGN_CENTER);
//		rt.body.setColAlign(ArrHeader.length, Table.ALIGN_CENTER);
		
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
//		rt.body.setColFormat(arrFormat);
		rt.body.setPageRows(40);
//		rt.body.mergeFixedCols();
//		rt.body.mergeFixedRow();

//		rt.createDefautlFooter(ArrWidth);
		rt.createFooter(1,ArrWidth);
		
		//设置页脚
		rt.setDefautlFooter(1, 4, "打印日期：" + DateUtil.Formatdate("yyyy年MM月dd日", new Date()),
				Table.ALIGN_LEFT);
		rt.setDefautlFooter(5, 4, "审核：", Table.ALIGN_LEFT);
		rt.setDefautlFooter(10, 4, "制表：", Table.ALIGN_LEFT);
		rt.footer.setRowCells(1, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.footer.fontSize=10;
//		rt.footer.setRowHeight(1, 1);
		con.Close();
		if(rt.body.getPages()>0) {
			setCurrentPage(1);
			setAllPages(rt.body.getPages());
		}
		rt.body.setRowHeight(21);
		RPTInit.getInsertSql(v.getDiancxxb_id(),sb.toString(),rt,"数量综合查询明细","SL_MX");
		return rt.getAllPagesHtml();// ph;
	}
	/*
	 * 质量综合	1、将检质数量和质量加权值由 净重 改为 来煤数量 
	 * 			2、并根据系统设定进行数据修约
	 * 			3、加入电厂过滤
	 * 修改时间：2008-12-04
	 * 修改人：王磊
	 * 
	 * 修改时间：2009-04-20
	 * 修改SQL语句，增加小计
	 * 修改SQL语句，增加排序
	 */
	public String getZonghcx_ZLHZ(){
		Visit v = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		StringBuffer sb = new StringBuffer();
		sb.append("select decode(grouping(gongysb.mingc),1,'总计',gongysb.mingc) meikdq," +
				"decode(grouping(gongysb.mingc)+grouping(meikxxb.mingc),1,'供应商小计',meikxxb.mingc) miekdw," +
				"decode(grouping(meikxxb.mingc)+grouping(chezxxb.mingc),1,'煤矿小计',chezxxb.mingc) faz,")
               .append("pinzb.mingc as pinz,sum(fahb.ches) as ches,sum(round_new(fahb.laimsl,"+v.getShuldec()+")) as jingz,")
               .append("decode(sum(round_new(fahb.laimsl,"+v.getShuldec()+")),0,0,round_new(round_new(sum(round_new(zhilb.qnet_ar,"+v.getFarldec()+") * round_new(fahb.laimsl,"+v.getShuldec()+")) / sum(round_new(fahb.laimsl,"+v.getShuldec()+")),"+v.getFarldec()+")/4.1816 * 1000,0)) as farl1,")
               .append("decode(sum(round_new(fahb.laimsl,"+v.getShuldec()+")),0,0,round_new(round_new(sum(round_new(zhilb.qnet_ar,"+v.getFarldec()+") * round_new(fahb.laimsl,"+v.getShuldec()+")) / sum(round_new(fahb.laimsl,"+v.getShuldec()+")),"+v.getFarldec()+") * 1000,0)) as farl2,")
               .append("decode(sum(round_new(fahb.laimsl,"+v.getShuldec()+")),0,0,round_new(sum(round_new(zhilb.mt,"+v.getMtdec()+") * round_new(fahb.laimsl,"+v.getShuldec()+")) / sum(round_new(fahb.laimsl,"+v.getShuldec()+")),"+v.getMtdec()+")) as mt,")
               .append("decode(sum(round_new(fahb.laimsl,"+v.getShuldec()+")),0,0,round_new(sum(zhilb.mad * round_new(fahb.laimsl,"+v.getShuldec()+")) / sum(round_new(fahb.laimsl,"+v.getShuldec()+")), 2)) as mad,")
               .append("decode(sum(round_new(fahb.laimsl,"+v.getShuldec()+")),0,0,round_new(sum(zhilb.aar * round_new(fahb.laimsl,"+v.getShuldec()+")) / sum(round_new(fahb.laimsl,"+v.getShuldec()+")), 2)) as aar,")
               .append("decode(sum(round_new(fahb.laimsl,"+v.getShuldec()+")),0,0,round_new(sum(zhilb.aad * round_new(fahb.laimsl,"+v.getShuldec()+")) / sum(round_new(fahb.laimsl,"+v.getShuldec()+")), 2)) as aad,")
               .append("decode(sum(round_new(fahb.laimsl,"+v.getShuldec()+")),0,0,round_new(sum(round_new(zhilb.qbad,"+v.getFarldec()+") * round_new(fahb.laimsl,"+v.getShuldec()+")) / sum(round_new(fahb.laimsl,"+v.getShuldec()+")) * 1000,0)) as qbad,")
               .append("decode(sum(round_new(fahb.laimsl,"+v.getShuldec()+")),0,0,round_new(sum(zhilb.ad * round_new(fahb.laimsl,"+v.getShuldec()+")) / sum(round_new(fahb.laimsl,"+v.getShuldec()+")),2)) as ad,")
               .append("decode(sum(round_new(fahb.laimsl,"+v.getShuldec()+")),0,0,round_new(sum(zhilb.vdaf * round_new(fahb.laimsl,"+v.getShuldec()+")) / sum(round_new(fahb.laimsl,"+v.getShuldec()+")), 2)) as vdaf,")
               .append("decode(sum(round_new(fahb.laimsl,"+v.getShuldec()+")),0,0,round_new(sum(zhilb.vad * round_new(fahb.laimsl,"+v.getShuldec()+")) / sum(round_new(fahb.laimsl,"+v.getShuldec()+")), 2)) as vad,")
               .append("decode(sum(round_new(fahb.laimsl,"+v.getShuldec()+")),0,0,round_new(sum(zhilb.sdaf * round_new(fahb.laimsl,"+v.getShuldec()+")) / sum(round_new(fahb.laimsl,"+v.getShuldec()+")), 2)) as sdaf,")
               .append("decode(sum(round_new(fahb.laimsl, " + v.getShuldec() + ")), 0, 0, round_new(sum(zhilb.stad * round_new(fahb.laimsl, " + v.getShuldec() + ")) / sum(round_new(fahb.laimsl, " + v.getShuldec() + ")), 2)) as stad,")
               .append("decode(sum(round_new(fahb.laimsl,"+v.getShuldec()+")),0,0,round_new(sum(zhilb.std * round_new(fahb.laimsl,"+v.getShuldec()+")) / sum(round_new(fahb.laimsl,"+v.getShuldec()+")), 2)) as std,")
               .append("decode(sum(round_new(fahb.laimsl,"+v.getShuldec()+")),0,0,round_new(sum(zhilb.hdaf * round_new(fahb.laimsl,"+v.getShuldec()+")) / sum(round_new(fahb.laimsl,"+v.getShuldec()+")), 2)) as hdaf ")
               .append("from fahb, zhilb, gongysb, meikxxb, chezxxb, pinzb ")
               .append("where fahb.zhilb_id = zhilb.id ")
               .append("and fahb.gongysb_id = gongysb.id ")
               .append("and fahb.meikxxb_id = meikxxb.id ")
               .append("and fahb.faz_id = chezxxb.id ")
               .append("and fahb.pinzb_id = pinzb.id ")
               .append(Jilcz.filterDcid(v,"fahb")).append(" \n")
               .append(RptSqltmp).append("\n")
               .append("group by rollup(gongysb.mingc,meikxxb.mingc,chezxxb.mingc,pinzb.mingc)")
               .append("\n")
               .append(tmpSql)
               .append("\n")
               .append("order by decode(grouping(gongysb.mingc),1,-999,1),max(gongysb.xuh)," +
               		"decode(grouping(meikxxb.mingc),1,-999,1),max(meikxxb.xuh)," +
               		"decode(grouping(chezxxb.mingc),1,-999,1),max(chezxxb.xuh),"+
               		"decode(grouping(pinzb.mingc),1,-999,1),max(pinzb.xuh)");

		Report rt = new Report();
		ResultSetList rstmp = con.getResultSetList(sb.toString());
		if (rstmp == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult);
			setMsg(ErrorMessage.NullResult);
			return "";
		}
		ResultSetList rs = null;
		String[][] ArrHeader=null;
		String[] strFormat=null;
		int[] ArrWidth=null;
		String [] Zidm=null;
		StringBuffer SB=new StringBuffer();
		SB.append("select bp.zidm,bp.biaot,bp.xuh,bp.kuand,bp.format from baobpzzb bp,baobpzb b where bp.baobpzb_id=b.id and bp.shifxs=1 and b.guanjz='ZL_HZ' order by xuh");
        ResultSetList rsl=con.getResultSetList(SB.toString());
        if(rsl.getRows()>0){
        	ArrWidth=new int[rsl.getRows()];
        	strFormat=new String[rsl.getRows()];
        	String biaot=rsl.getString(0,1);
        	String [] Arrbt=biaot.split("!@");
        	ArrHeader =new String [Arrbt.length][rsl.getRows()];
        	Zidm=new String[rsl.getRows()];
        	rs = new ResultSetList();
        	while(rsl.next()){
        		Zidm[rsl.getRow()]=rsl.getString("zidm");
        		ArrWidth[rsl.getRow()]=rsl.getInt("kuand");
        		strFormat[rsl.getRow()]=rsl.getString("format")==null?"":rsl.getString("format");
        		String[] title=rsl.getString("biaot").split("!@");
        		for(int i=0;i<title.length;i++){
        			ArrHeader[i][rsl.getRow()]=title[i];
        		}
        	}
        	rs.setColumnNames(Zidm);
        	while(rstmp.next()){
        		rs.getResultSetlist().add(rstmp.getArrString(Zidm));
        	}
        	rstmp.close();
        	rsl.close();
        	rsl=con.getResultSetList("select biaot from baobpzb where guanjz='ZL_HZ'");
        	String Htitle="";
        	while(rsl.next()){
        		Htitle=rsl.getString("biaot");
        	}
        	rt.setTitle(Htitle, ArrWidth);
        	rsl.close();
        } else {
        	rs = rstmp;
            ArrHeader = new String[1][20];
        	ArrHeader[0] = new String[] {"煤矿地区", "煤矿单位", "发站", "品种", "车数", "检质数量(吨)", "收到基低位热值(Kcal<br>/kg)", 
        				"收到基低位热值(j/g)", "全水分(%)Mt", "空气干燥基水分(%)Mad", "收到基灰分(%)Aar", "空气干燥基灰分(%)Aad", 
        				"弹筒<br>热值<br>(j/g)<br>Qb,ad", "干燥基灰分(%)Ad", "干燥无灰基挥发分(%)Vdaf", "空气干燥基挥发分(%)Vad", 
        				"干燥无灰基硫(%)Sdaf", "空气干燥基硫(%)St,ad", "干燥基全硫(%)S,td", "干燥无灰基氢(%)Hdaf"};
        	
            ArrWidth = new int[] {120, 120, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60};
            rt.setTitle("质量综合查询汇总", ArrWidth);
        }


		//设置页标题
		
		rt.title.fontSize=10;
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.setDefaultTitle(1, 4, "制表单位：" + ((Visit) this.getPage().getVisit()).getDiancqc(),
				Table.ALIGN_LEFT);
		
		//根据值存在的情况制表
		int i=0;
		String tj="";
		if(Fahrq.length()>1){
			tj += "发货日期:"+ Fahrq;
			i++;
		}
		if(Daohrq.length()>1){
			if(i>0){
				tj += "<br>到货日期:"+ Daohrq;
			}else{
				tj += "到货日期:"+ Daohrq;
			}
			i++;
		}
		if(Huaysj.length()>1){
			if(i>0){
				tj += "<br>化验日期:"+ Huaysj;
			}else{
				tj += "化验日期:"+ Huaysj;
			}
			i++;
		}
		rt.setDefaultTitle(5, 5, tj,Table.ALIGN_LEFT);
		
		
//		if(Fahrq.length()>1 && Huaysj.length()>1){
//			rt.setDefaultTitle(5, 5, "发货日期:"+ Fahrq+"<br>化验日期:"+ Huaysj,Table.ALIGN_LEFT);
//		}else if(Fahrq.length()>1 && Huaysj.length()<1){
//			rt.setDefaultTitle(5, 5, "发货日期: "+ Fahrq,Table.ALIGN_LEFT);
//		}else if(Fahrq.length()<1 && Huaysj.length()>1){
//			rt.setDefaultTitle(5, 5, "化验日期: "+ Huaysj,Table.ALIGN_LEFT);
//		}

		//数据
		rt.setBody(new Table(rs, 1, 0, 4));
		//rt.body.setColAlign(0, Table.ALIGN_CENTER);
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		rt.body.setColAlign(3, Table.ALIGN_CENTER);
		rt.body.setColAlign(4, Table.ALIGN_CENTER);
		rt.body.setColAlign(5, Table.ALIGN_RIGHT);
		rt.body.setColAlign(6, Table.ALIGN_RIGHT);
		rt.body.setColAlign(7, Table.ALIGN_RIGHT);
		rt.body.setColAlign(8, Table.ALIGN_RIGHT);
		rt.body.setColAlign(9, Table.ALIGN_RIGHT);
		rt.body.setColAlign(10, Table.ALIGN_RIGHT);
		rt.body.setColAlign(11, Table.ALIGN_RIGHT);
		rt.body.setColAlign(12, Table.ALIGN_RIGHT);
		rt.body.setColAlign(13, Table.ALIGN_RIGHT);
		rt.body.setColAlign(14, Table.ALIGN_RIGHT);
		rt.body.setColAlign(15, Table.ALIGN_RIGHT);
		rt.body.setColAlign(16, Table.ALIGN_RIGHT);
		rt.body.setColAlign(17, Table.ALIGN_RIGHT);
		rt.body.setColAlign(18, Table.ALIGN_RIGHT);
		rt.body.setColAlign(19, Table.ALIGN_RIGHT);
//		rt.body.setCellVAlign(i, j, intAlign)
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
//		rt.body.setColFormat(arrFormat);
		rt.body.setPageRows(40);
		rt.body.mergeFixedCols();
		rt.body.mergeFixedRow();

//		rt.createDefautlFooter(ArrWidth);
		rt.createFooter(1,ArrWidth);
		
		//设置页脚
		rt.setDefautlFooter(1, 6, "打印日期：" + DateUtil.Formatdate("yyyy年MM月dd日", new Date()),
				Table.ALIGN_LEFT);
		rt.setDefautlFooter(7, 6, "审核：", Table.ALIGN_LEFT);
		rt.setDefautlFooter(13, 7, "制表：", Table.ALIGN_LEFT);
		rt.footer.setRowCells(1, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.footer.fontSize=10;
//		rt.footer.setRowHeight(1, 1);
		con.Close();
		if(rt.body.getPages()>0) {
			setCurrentPage(1);
			setAllPages(rt.body.getPages());
		}
		rt.body.setRowHeight(21);
		RPTInit.getInsertSql(v.getDiancxxb_id(),sb.toString(),rt,"质量综合查询汇总","ZL_HZ");
		return rt.getAllPagesHtml();// ph;
	}
	/*
	 * 质量综合	1、将检质数量由 净重 改为 来煤数量 
	 * 			2、并根据系统设定进行数据修约
	 * 			3、加入电厂过滤
	 * 修改时间：2008-12-04
	 * 修改人：王磊
	 */
	public String getZonghcx_ZL_MX(){
		Visit v = (Visit) getPage().getVisit();
		JDBCcon con= new JDBCcon();
		StringBuffer sb= new StringBuffer();
		sb.append("SELECT huaybh,meikdq,miekdw,faz,pinz,ches,laimsl,\n");
		sb.append("round_new((farl2/1000)/4.1816*1000,0) farl1,\n");
		sb.append("farl2,mt,mad,aar,aad,qbad,ad,vdaf,vad,sdaf,stad,std,hdaf,huaysj,huayy\n");
		sb.append("FROM(\n");
		sb.append("select \n")
		.append("       zhilb.huaybh,\n")
		.append("       gongysb.mingc as meikdq,\n")
		.append("       meikxxb.mingc as miekdw,\n")
		.append("       chezxxb.mingc as faz,\n" )
		.append("       pinzb.mingc as pinz,\n" )
	    .append("       fahb.ches,\n" )
		.append("       round_new(fahb.laimsl,"+v.getShuldec()+") as laimsl,\n" )
		.append("       round_new(round_new(zhilb.qnet_ar,"+v.getFarldec()+") / 4.1816 * 1000, 0) as farl1,\n") 
		.append("       round_new(round_new(zhilb.qnet_ar,"+v.getFarldec()+") * 1000, 0) as farl2,\n" )
		.append("       round_new(zhilb.mt, "+v.getMtdec()+") as mt,\n" )
		.append("       round_new(zhilb.mad, 2) as mad,\n") 
		.append("       round_new(zhilb.aar, 2) as aar,\n" ) 
	    .append("       round_new(zhilb.aad, 2) as aad,\n" ) 
	    .append("       round_new(round_new(zhilb.qbad,"+v.getFarldec()+") * 1000, 0) as qbad,\n" ) 
	    .append("       round_new(zhilb.ad, 2) as ad,\n" )
	    .append("       round_new(zhilb.vdaf, 2) as vdaf,\n" ) 
	    .append("       round_new(zhilb.vad, 2) as vad,\n" ) 
	    .append("       round_new(zhilb.sdaf, 2) as sdaf,\n" ) 
	    .append("       round_new(zhilb.stad, 2) as stad,\n" ) 
	    .append("       round_new(zhilb.std, 2) as std,\n" ) 
	    .append("       round_new(zhilb.hdaf, 2) as hdaf,\n" ) 
	    .append("       to_char(zhilb.huaysj,'yyyy-mm-dd') as huaysj,\n" )
	    .append("       zhilb.huayy\n" )
	    .append("  from fahb, zhilb, gongysb, meikxxb, chezxxb, pinzb\n" ) 
	    .append(" where fahb.zhilb_id = zhilb.id\n" )
	    .append("   and fahb.gongysb_id = gongysb.id\n" ) 
	    .append("   and fahb.meikxxb_id = meikxxb.id\n" ) 
	    .append("   and fahb.faz_id = chezxxb.id\n" )
	    .append("   and fahb.pinzb_id = pinzb.id\n" ) 
	    .append(Jilcz.filterDcid(v,"fahb")).append(" \n")
	    .append(RptSqltmp).append("\n");
		
		if(tmpSql!=null && tmpSql.indexOf("1")!=-1){//增加合计行
			sb.append(" union \n");
			sb.append("select \n")
			.append("       '合计' huaybh,\n")
			.append("       '' as meikdq,\n")
			.append("       '' as miekdw,\n")
			.append("       '' as faz,\n" )
			.append("       '' as pinz,\n" )
		    .append("      sum(fahb.ches) as ches,\n" )
			.append("       sum(round_new(fahb.laimsl,"+v.getShuldec()+")) as laimsl,\n" )
			.append("decode(sum(round_new(fahb.laimsl,"+v.getShuldec()+")),0,0,round_new((sum(round_new(zhilb.qnet_ar,"+v.getFarldec()+") * round_new(fahb.laimsl,"+v.getShuldec()+")) / sum(round_new(fahb.laimsl,"+v.getShuldec()+"))) /4.1816 * 1000,0)) as farl1,\n")
			.append("decode(sum(round_new(fahb.laimsl,"+v.getShuldec()+")),0,0,round_new(sum(round_new(zhilb.qnet_ar,"+v.getFarldec()+") * round_new(fahb.laimsl,"+v.getShuldec()+")) / sum(round_new(fahb.laimsl,"+v.getShuldec()+")) * 1000,0)) as farl2,\n")
			.append("decode(sum(round_new(fahb.laimsl,"+v.getShuldec()+")),0,0,round_new(sum(round_new(zhilb.mt,"+v.getMtdec()+") * round_new(fahb.laimsl,"+v.getShuldec()+")) / sum(round_new(fahb.laimsl,"+v.getShuldec()+")),"+v.getMtdec()+")) as mt,\n")
			.append("decode(sum(round_new(fahb.laimsl,"+v.getShuldec()+")),0,0,round_new(sum(zhilb.mad * round_new(fahb.laimsl,"+v.getShuldec()+")) / sum(round_new(fahb.laimsl,"+v.getShuldec()+")), 2)) as mad,\n")
			.append("decode(sum(round_new(fahb.laimsl,"+v.getShuldec()+")),0,0,round_new(sum(zhilb.aar * round_new(fahb.laimsl,"+v.getShuldec()+")) / sum(round_new(fahb.laimsl,"+v.getShuldec()+")), 2)) as aar,\n")
			.append("decode(sum(round_new(fahb.laimsl,"+v.getShuldec()+")),0,0,round_new(sum(zhilb.aad * round_new(fahb.laimsl,"+v.getShuldec()+")) / sum(round_new(fahb.laimsl,"+v.getShuldec()+")), 2)) as aad,\n")
			.append("decode(sum(round_new(fahb.laimsl,"+v.getShuldec()+")),0,0,round_new(sum(round_new(zhilb.qbad,"+v.getFarldec()+") * round_new(fahb.laimsl,"+v.getShuldec()+")) / sum(round_new(fahb.laimsl,"+v.getShuldec()+")) * 1000,0)) as qbad,\n")
			.append("decode(sum(round_new(fahb.laimsl,"+v.getShuldec()+")),0,0,round_new(sum(zhilb.ad * round_new(fahb.laimsl,"+v.getShuldec()+")) / sum(round_new(fahb.laimsl,"+v.getShuldec()+")),2)) as ad,\n")
			.append("decode(sum(round_new(fahb.laimsl,"+v.getShuldec()+")),0,0,round_new(sum(zhilb.vdaf * round_new(fahb.laimsl,"+v.getShuldec()+")) / sum(round_new(fahb.laimsl,"+v.getShuldec()+")), 2)) as vdaf,\n")
			.append("decode(sum(round_new(fahb.laimsl,"+v.getShuldec()+")),0,0,round_new(sum(zhilb.vad * round_new(fahb.laimsl,"+v.getShuldec()+")) / sum(round_new(fahb.laimsl,"+v.getShuldec()+")), 2)) as vad,\n")
			.append("decode(sum(round_new(fahb.laimsl,"+v.getShuldec()+")),0,0,round_new(sum(zhilb.sdaf * round_new(fahb.laimsl,"+v.getShuldec()+")) / sum(round_new(fahb.laimsl,"+v.getShuldec()+")), 2)) as sdaf,\n")
			.append("decode(sum(round_new(fahb.laimsl, " + v.getShuldec() + ")), 0, 0, round_new(sum(zhilb.stad * round_new(fahb.laimsl, " + v.getShuldec() + ")) / sum(round_new(fahb.laimsl, " + v.getShuldec() + ")), 2)) as stad,\n")
			.append("decode(sum(round_new(fahb.laimsl,"+v.getShuldec()+")),0,0,round_new(sum(zhilb.std * round_new(fahb.laimsl,"+v.getShuldec()+")) / sum(round_new(fahb.laimsl,"+v.getShuldec()+")), 2)) as std,\n")
			.append("decode(sum(round_new(fahb.laimsl,"+v.getShuldec()+")),0,0,round_new(sum(zhilb.hdaf * round_new(fahb.laimsl,"+v.getShuldec()+")) / sum(round_new(fahb.laimsl,"+v.getShuldec()+")), 2)) as hdaf,\n ")
		    .append("       '' as huaysj,\n" )
		    .append("       '' as huayy\n" )
		    .append("  from fahb, zhilb, gongysb, meikxxb, chezxxb, pinzb\n" ) 
		    .append(" where fahb.zhilb_id = zhilb.id\n" )
		    .append("   and fahb.gongysb_id = gongysb.id\n" ) 
		    .append("   and fahb.meikxxb_id = meikxxb.id\n" ) 
		    .append("   and fahb.faz_id = chezxxb.id\n" )
		    .append("   and fahb.pinzb_id = pinzb.id\n" ) 
		    .append(Jilcz.filterDcid(v,"fahb")).append(" \n")
		    .append(RptSqltmp).append("\n");
		}
		sb.append(" order by huaysj)");
		
		Report rt = new Report();
		ResultSetList rstmp = con.getResultSetList(sb.toString());
		if (rstmp == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult);
			setMsg(ErrorMessage.NullResult);
			return "";
		}
		ResultSetList rs = null;
		String[][] ArrHeader=null;
		String[] strFormat=null;
		int[] ArrWidth=null;
		String [] Zidm=null;
		StringBuffer SB=new StringBuffer();
		SB.append("select bp.zidm,bp.biaot,bp.xuh,bp.kuand,bp.format from baobpzzb bp,baobpzb b where bp.baobpzb_id=b.id and bp.shifxs=1 and b.guanjz='ZL_MX' order by xuh");
        ResultSetList rsl=con.getResultSetList(SB.toString());
        if(rsl.getRows()>0){
        	ArrWidth=new int[rsl.getRows()];
        	strFormat=new String[rsl.getRows()];
        	String biaot=rsl.getString(0,1);
        	String [] Arrbt=biaot.split("!@");
        	ArrHeader =new String [Arrbt.length][rsl.getRows()];
        	Zidm=new String[rsl.getRows()];
        	rs = new ResultSetList();
        	while(rsl.next()){
        		Zidm[rsl.getRow()]=rsl.getString("zidm");
        		ArrWidth[rsl.getRow()]=rsl.getInt("kuand");
        		strFormat[rsl.getRow()]=rsl.getString("format")==null?"":rsl.getString("format");
        		String[] title=rsl.getString("biaot").split("!@");
        		for(int i=0;i<title.length;i++){
        			ArrHeader[i][rsl.getRow()]=title[i];
        		}
        	}
        	rs.setColumnNames(Zidm);
        	while(rstmp.next()){
        		rs.getResultSetlist().add(rstmp.getArrString(Zidm));
        	}
        	rstmp.close();
        	rsl.close();
        	rsl=con.getResultSetList("select biaot from baobpzb where guanjz='ZL_MX'");
        	String Htitle="";
        	while(rsl.next()){
        		Htitle=rsl.getString("biaot");
        	}
        	rt.setTitle(Htitle, ArrWidth);
        	rsl.close();
        } else {
        	rs = rstmp;
        	ArrHeader = new String[1][23];
        	ArrHeader[0] = new String[] {"化验编号", "煤矿地区", "煤矿单位", "发站", "品种", "车数", "检质数量(吨)", "收到基低位热值(Kcal<br>/kg)", 
        				"收到基低位热值(j/g)", "全<br>水<br>分<br>(%)<br>Mt", "空气干燥基水分(%)Mad", "收到基灰分(%)Aar", "空气干燥基灰分(%)Aad", 
        				"弹筒<br>热值<br>(j/g)<br>Qb,ad", "干燥基灰分(%)Ad", "干燥无灰基挥发分(%)Vdaf", "空气干燥基挥发分(%)Vad", 
        				"干燥无灰基硫(%)Sdaf", "空气干燥基硫(%)St,ad", "干燥基全硫(%)S,td", "干燥无灰基氢(%)Hdaf", "化验<br>日期", "化验人员"};
        	
        	ArrWidth=new int[] {54, 100, 100, 100, 54, 54, 54, 54, 54, 54, 54, 54, 54, 54, 54, 54, 54, 54, 54, 54, 54, 65, 54};
        	rt.setTitle("质量综合查询明细", ArrWidth);
        }
//		设置页标题
			
			rt.title.fontSize=10;
			rt.title.setRowHeight(2, 50);
			rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
			rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
			rt.setDefaultTitle(1, 4, "制表单位：" + ((Visit) this.getPage().getVisit()).getDiancqc(),
					Table.ALIGN_LEFT);
			//根据值存在的情况制表
			if(Fahrq.length()>1 && Huaysj.length()>1){
				rt.setDefaultTitle(9, 5, "发货日期:"+ Fahrq+"<br>化验日期:"+ Huaysj,Table.ALIGN_LEFT);
			}else if(Fahrq.length()>1 && Huaysj.length()<1){
				rt.setDefaultTitle(9, 5, "发货日期: "+ Fahrq,Table.ALIGN_LEFT);
			}else if(Fahrq.length()<1 && Huaysj.length()>1){
				rt.setDefaultTitle(9, 5, "化验日期: "+ Huaysj,Table.ALIGN_LEFT);
			}else if(Daohrq.length()>1){
				rt.setDefaultTitle(9, 5, "到货日期: "+ Daohrq,Table.ALIGN_LEFT);
			}
//			数据
			rt.setBody(new Table(rs, 1, 0, 4));
			//rt.body.setColAlign(0, Table.ALIGN_CENTER);
			rt.body.setColAlign(1, Table.ALIGN_CENTER);
			rt.body.setColAlign(2, Table.ALIGN_CENTER);
			rt.body.setColAlign(3, Table.ALIGN_CENTER);
			rt.body.setColAlign(4, Table.ALIGN_CENTER);
			rt.body.setColAlign(5, Table.ALIGN_CENTER);
			rt.body.setColAlign(6, Table.ALIGN_RIGHT);
			rt.body.setColAlign(7, Table.ALIGN_RIGHT);
			rt.body.setColAlign(8, Table.ALIGN_RIGHT);
			rt.body.setColAlign(9, Table.ALIGN_RIGHT);
			rt.body.setColAlign(10, Table.ALIGN_RIGHT);
			rt.body.setColAlign(11, Table.ALIGN_RIGHT);
			rt.body.setColAlign(12, Table.ALIGN_RIGHT);
			rt.body.setColAlign(13, Table.ALIGN_RIGHT);
			rt.body.setColAlign(14, Table.ALIGN_RIGHT);
			rt.body.setColAlign(15, Table.ALIGN_RIGHT);
			rt.body.setColAlign(16, Table.ALIGN_RIGHT);
			rt.body.setColAlign(17, Table.ALIGN_RIGHT);
			rt.body.setColAlign(18, Table.ALIGN_RIGHT);
			rt.body.setColAlign(19, Table.ALIGN_RIGHT);
			rt.body.setColAlign(20, Table.ALIGN_RIGHT);
			rt.body.setColAlign(21, Table.ALIGN_RIGHT);
			rt.body.setColAlign(22, Table.ALIGN_RIGHT);
			rt.body.setWidth(ArrWidth);
			rt.body.setHeaderData(ArrHeader);
//			rt.body.setColFormat(arrFormat);
			rt.body.setPageRows(40);
			rt.body.mergeFixedCols();
			rt.body.mergeFixedRow();

//			rt.createDefautlFooter(ArrWidth);
			rt.createFooter(1,ArrWidth);
			
			
//			设置页脚
			rt.setDefautlFooter(1, 4, "打印日期：" + DateUtil.Formatdate("yyyy年MM月dd日", new Date()),
					Table.ALIGN_LEFT);
			rt.setDefautlFooter(5, 4, "审核：", Table.ALIGN_LEFT);
			rt.setDefautlFooter(10, 4, "制表：", Table.ALIGN_LEFT);
			rt.footer.setRowCells(1, Table.PER_VALIGN, Table.VALIGN_TOP);
			rt.footer.fontSize=10;
//			rt.footer.setRowHeight(1, 1);
			con.Close();
			if(rt.body.getPages()>0) {
				setCurrentPage(1);
				setAllPages(rt.body.getPages());
			}
			rt.body.setRowHeight(21);
			RPTInit.getInsertSql(v.getDiancxxb_id(),sb.toString(),rt,"质量综合查询明细","ZL_MX");
		return  rt.getAllPagesHtml();// ph;
	}
	
	public String getPrintTable(){
		if(RptType.equals(RptType_SL_HZ)){
			//解析从数量汇总sql2中传过来的数
			String sqltmp2=tmpSql;
			if(sqltmp2!=null){
				tmpSql= "having grouping(p.mingc)=0";
				String[] srctmp = {"1","2","3"};
				String[] streval = {"grouping(g.mingc)=1","grouping(m.mingc)+grouping(g.mingc)=1",
						"grouping(m.mingc)=0"};
				String[] stmp = sqltmp2.split(",");
				for(int i = 0; i < srctmp.length; i++){
					int m = 0; 
					for(; m < stmp.length; m++){
						if(stmp[m].equals(srctmp[i])){
							tmpSql+= " or " + streval[i];
							break;
						}
					}

				}
				
			}
			//完成
			return getZonghcx_SLHZ();
		}
		if(RptType.equals(RptType_SL_MX)){
//			解析从数量明细sql2中传过来的数
			String sqltmp2=tmpSql;
//			从系统信息表中判断是否显示采样编码
			String BianmGrouping="";
			String XitxxbSql="select * from xitxxb where mingc='采样编码显示' and zhi='是'";
			JDBCcon con=new JDBCcon();
			if(con.getHasIt(XitxxbSql)){
				BianmGrouping="GetHuaybmFromZhilId(caiyb.zhilb_id)";
			}else{
				BianmGrouping="chepb.id";
			}
			con.Close();
			if(sqltmp2!=null){
				tmpSql= "having grouping("+BianmGrouping+")=0";
				String[] srctmp = {"1","2","3","4","5","6"};
				String[] streval = {"grouping(g.mingc)+ grouping(m.mingc)=1",
						"grouping(m.mingc) + grouping(cz.mingc)=1",
						"grouping(p.mingc)+grouping(cz.mingc)=1",
						"grouping(p.mingc)+grouping(to_char(fahb.daohrq, 'yyyy-mm-dd'))=1",
						"grouping(to_char(fahb.daohrq, 'yyyy-mm-dd'))+ grouping(caiyb.bianm) = 1",
						"grouping(caiyb.bianm)=0"};
				String[] stmp = sqltmp2.split(",");
				//判断一共筛选多少个值,多余一个值那么不显示星星
				if(stmp.length>1){ShowStar=false;}
				
				for(int i = 0; i < srctmp.length; i++){
					int m = 0; 
					for(; m < stmp.length; m++){
						if(stmp[m].equals(srctmp[i])){
							tmpSql+= " or " + streval[i];
							break;
						}
					}

				}
				
			}
//			完成
			return getZonghcx_SLMX();
		}
		if(RptType.equals(RptType_ZL_HZ)){
//			解析从质量汇总sql2中传过来的数
			String sqltmp2=tmpSql;
			if(sqltmp2!=null){
				tmpSql= "having grouping(pinzb.mingc)=0";
				String[] srctmp = {"1","2","3"};
				String[] streval = {"grouping(gongysb.mingc)=1",
						"grouping(gongysb.mingc)+grouping(meikxxb.mingc)=1",
						"grouping(meikxxb.mingc)+grouping(chezxxb.mingc)=1"};
				String[] stmp = sqltmp2.split(",");
				for(int i = 0; i < srctmp.length; i++){
					int m = 0; 
					for(; m < stmp.length; m++){
						if(stmp[m].equals(srctmp[i])){
							tmpSql+= " or " + streval[i];
							break;
						}
					}

				}
				
			}
			//完成
			return getZonghcx_ZLHZ();
		}
		if(RptType.equals(RptType_ZL_MX)){
			return getZonghcx_ZL_MX();
		}
		return null;
	}
	
//	页面初始化
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		String sqltmp="";
		String reportType="";
		Visit visit = (Visit) getPage().getVisit();
//		sqltmp = cycle.getRequestContext().getParameter("sql").getBytes();
//		tmpSql = cycle.getRequestContext().getParameter("sql2");
//		String reportType = cycle.getRequestContext().getParameter("lx");
//		Fahrq = cycle.getRequestContext().getParameter("fahrq").replaceFirst(" ", "至");
//		Daohrq = cycle.getRequestContext().getParameter("daohrq").replaceFirst(" ","至");
		/*
		 * 修改时间：2009-06-28
		 * 修改人：  王伟
		 * 修改内容：
		 * 		中文乱码问题，如：车号中带有汉字（蒙B1234）"蒙"
		 */
		try {
			sqltmp = new String(cycle.getRequestContext().getParameter("sql").getBytes("iso8859-1"), "gb2312");
			tmpSql = new String(cycle.getRequestContext().getParameter("sql2").getBytes("iso8859-1"), "gb2312");
			reportType = cycle.getRequestContext().getParameter("lx");
			if(visit.getActivePageName().toString().equals("Zonghcx_zl")){
				Huaysj = cycle.getRequestContext().getParameter("huaysj").replaceFirst(" ", "至");
			}
			Fahrq = cycle.getRequestContext().getParameter("fahrq").replaceFirst(" ", "至");
			Daohrq = cycle.getRequestContext().getParameter("daohrq").replaceFirst(" ", "至");
			//Huaysj = cycle.getRequestContext().getParameter("huaysj").replaceFirst(" ","至");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if(sqltmp != null) {
			RptSqltmp = sqltmp;
		}
		if(reportType != null) {
			RptType = reportType.trim();
		}
	}

//	页面登陆验证
	public void pageValidate(PageEvent arg0) {
		String PageName = arg0.getPage().getPageName();
		String ValPageName = Login.ValidateLogin(arg0.getPage());
		if (!PageName.equals(ValPageName)) {
			ValPageName = Login.ValidateAdmin(arg0.getPage());
			if (!PageName.equals(ValPageName)) {
				IPage ipage = arg0.getRequestCycle().getPage(ValPageName);
				throw new PageRedirectException(ipage);
			}
		}
	}
}