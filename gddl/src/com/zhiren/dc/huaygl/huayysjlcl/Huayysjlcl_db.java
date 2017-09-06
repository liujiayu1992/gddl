package com.zhiren.dc.huaygl.huayysjlcl;

import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.dc.huaygl.Judge;
import com.zhiren.gangkjy.Local;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
/*
有关化验原始报告的信息
1.	默认接口原始项目传输状态0
2.	原始项目审核后2
3.	原始项目作废后为-1
4.	原始报告确认后1
5.	原始报告撤销后2

 * 
 * 作者：chh
 * 时间：2010-05-03
 * 描述：可以通过参数控制，增加自动审核的过程，减少在原始化验项目中的选来选去审核的环节
 */
/*
 * 
 * 
 * 作者：张立东
 * 时间：2009-08-15
 * 描述：修改数据计算不正确问题
 * 
 */
/*
 * 作者：王磊
 * 时间：2009-08-14
 * 描述：修改小数休约
 * 
 */
/*
 * 2009-05-13
 * 王磊
 * 修改更新质量临时表审核状态为3标识数据直接到一级审核
 */
/*
 * 作者:tzf
 * 时间:2009-4-27
 * 内容:根据xitxxb的配置，从huaygyfxb，huaylfb，huayrlb分别取数显示，确认 和 撤销.
 */
public class Huayysjlcl_db extends BasePage implements PageValidateListener {

	private final static String yuansbglly="原始报告硫来源";
	private final static String yuansbgqly="原始报告氢来源";
	private final static String yuansfx="元素分析";
	private final static String shougwh="手工维护";
	private final static String zidjs="自动计算";
	private final static String caijsj="采集数据";
	private final static String _liu="硫";
	private final static String _qing="氢";
	private final static String huaybm="化验编码";
	private final static String huayysjl_bianm="HYYSJL";//itemsort表中 对于的编码
	
	private final static long SELECT_WEISH=1;//未审核，下拉框中的ID
	private final static long SELECT_YISH=2;//已审核
		
	public static final String Qbad = "Qbad";
	public static final String Stad = "Stad";
	public static final String Mt = "Mt";
	public static final String Mad = "Mad";
	public static final String Aad = "Aad";
	public static final String Vad = "Vad";
	
	// 分析项目表 对应 的项目 名称
//	private final static String quansf="全水分";
//	private final static String shuif="水分";
//	private final static String huiff="挥发分";
//	private final static String huif="灰分";
//	private final static String quanl="全硫";
//	private final static String farl="发热量";
	
	private final static String round_count="###.00";//自动计算时  保留的小数点 尾数
	public boolean getRaw() {
		return true;
	}

	// 界面用户提示
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
		;
	}

	protected void initialize() {
		super.initialize();
		setMsg("");
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
		return getHuaybgd();
	}
	
	//从 xitxxb 中 取得 原始报告硫来源
	private String getLiuly(JDBCcon con){
		Visit visit=(Visit)this.getPage().getVisit();
		String zhi="";
		String sql=" select zhi from xitxxb where leib='化验' and zhuangt = 1 and mingc='"+yuansbglly+"' and diancxxb_id="+visit.getDiancxxb_id();
		ResultSetList rsl=con.getResultSetList(sql);
		if(rsl.next()){
			zhi=rsl.getString("zhi");
		}
		rsl.close();
		return zhi;
	}
	
//	从 xitxxb 中 取得 原始报告氢来源
	private String getQingly(JDBCcon con){
		Visit visit=(Visit)this.getPage().getVisit();
		String zhi="";
		String sql=" select zhi from xitxxb where leib='化验' and zhuangt = 1 and mingc='"+yuansbgqly+"' and diancxxb_id="+visit.getDiancxxb_id();
		ResultSetList rsl=con.getResultSetList(sql);
		if(rsl.next()){
			zhi=rsl.getString("zhi");
		}
		rsl.close();
		return zhi;
	}
	
	private double Round_New(double value){
		double rv=0;
		DecimalFormat dc=new DecimalFormat(round_count);
		String s=dc.format(value);
		try{
			rv=Double.valueOf(s).doubleValue();
		}catch(Exception e){
			return 0;
		}
		return rv;
	}
	
	private double Round_New(double value,String pattern){
		double rv=0;
		DecimalFormat dc=new DecimalFormat(pattern);
		String s=dc.format(value);
		
		try{
			rv=Double.valueOf(s).doubleValue();
		}catch(Exception e){
			return 0;
		}
		
		return rv;
	}
	
	//自动计算时  得到空干基氢的 值
	private String getKonggjq_v(JDBCcon con,String bianm){
		String value="";
		StringBuffer bf=new StringBuffer();
		bf.append(" select z.mt,z.mad,z.aad,z.vad,z.stad,z.qbad,z.had  \n");
		bf.append("  from zhillsb z, zhuanmb m, zhuanmlb l \n");
		bf.append(" where z.id = m.zhillsb_id and m.zhuanmlb_id = l.id \n");
		bf.append(" and l.mingc = '化验编码' and m.bianm='").append(bianm).append("' \n");
		
		ResultSetList rsl=con.getResultSetList(bf.toString());
		double Mt_v=0;
		double Mad_v=0;
		double Aad_v=0;
		double Vad_v=0;
		double Stad_v=0;
		double Qbad_v=0;
		double Had_v=0;
		if(rsl.next()){
			Mt_v=rsl.getDouble("mt");
			Mad_v=rsl.getDouble("mad");
			Aad_v=rsl.getDouble("aad");
			Vad_v=rsl.getDouble("vad");
			Stad_v=rsl.getDouble("stad");
			Qbad_v=rsl.getDouble("qbad");
			Had_v=rsl.getDouble("had");
			
		}
		double Qnetar_v=0;
		double Qgrd_v=0;
		//符合自动计算 条件
		if(Mt_v>0 && Mad_v>0 && Aad_v>0 && Vad_v>0 && Stad_v>0 && Qbad_v>0 ){
			
			double Aar_v = Round_New(Aad_v*(100-Mt_v)/(100-Mad_v));
			double Ad_v  = Round_New(Aar_v*100/(100-Aad_v));
			double Var_v = Round_New(Vad_v*(100-Mt_v)/(100-Mad_v));
			double Vd_v  = Round_New(Vad_v*100/(100-Mad_v));
			double Vdaf_v = Round_New(Vad_v*100/(100-Vd_v));
			double FCar_v = Round_New(100-Mt_v-Aar_v-Var_v);
			double FCad_v = Round_New(100-Mad_v-Aad_v-Vad_v);
			double FCd_v = Round_New(100-Ad_v-Vd_v);
			double Star_v = Round_New(Stad_v*(100-Mt_v)/(100-Mad_v));
			double Std_v = Round_New(Stad_v*100/(100-Mad_v));
			double Qgrad_v = 0.0;
			Had_v = Round_New((Vd_v*100/(100-Ad_v)*0.074+2.16)*(100-Mad_v-Aad_v)/100);
			
			
			double Hdaf_v = Round_New(Had_v * 100 / (100 - Mad_v - Aad_v));
			if(Qbad_v <= 16.7){
				Qgrad_v = Round_New((Qbad_v*1000-94.1*Stad_v-Qbad_v)/1000);
			}else{
				 if(Qbad_v > 16.7 && Qbad_v <= 25.1){
					Qgrad_v = Round_New((Qbad_v*1000-94.1*Stad_v-1.2*Qbad_v)/1000);
				 }else{
					Qgrad_v = Round_New((Qbad_v*1000-94.1*Stad_v-1.6*Qbad_v)/1000);
				 }
			}
			Qgrd_v  = Round_New(Qgrad_v*100/(100-Mad_v));
			
			Qnetar_v  = Round_New(((Qgrad_v*1000-206*Had_v)*(100-Aar_v)/(100-Mad_v)-23*Mt_v)/1000,"###.000");
			
		}else{
			Had_v=0;
		}
		
		value=Had_v+"";
		return value;
	}
	
	
	
	private void setArrayValue(String ArrHeader[][], boolean isOnlyQuery){
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		
		String wend_shid=""; //温度湿度  为空
		String caiyl="";//采样量  为空
		String zhiyr="";//制样人  为空
		
		StringBuffer bf=new StringBuffer();
		bf.append(" select max(zhillsbid) as zhillsbid,sum(jingz) as jingz,bianh,kuangb,faz,pinz,huaysj,max(chec) as chec,max(caiysj) as caiysj,meikdm from ");
		bf.append(" (select z.id as zhillsbid,f.jingz,nvl(m.bianm,'') bianh,nvl(mk.mingc,'') kuangb,nvl(c.mingc,'') faz,nvl(p.mingc,'') pinz,nvl(to_char(z.huaysj,'yyyy-mm-dd'),'') huaysj,nvl(f.chec,'') chec,nvl(to_char(cai.caiyrq,'yyyy-mm-dd'), '') caiysj \n");
		bf.append(",decode(cai.meikdm,null,' ',cai.meikdm) as meikdm from meikxxb mk,chezxxb c,pinzb p,zhillsb z,  \n");
		bf.append(" (select distinct fa.diancxxb_id,fa.zhilb_id, fa.meikxxb_id,max(fa.chec) as chec,fa.faz_id,fa.pinzb_id,sum(maoz-piz) as jingz from fahb fa group by fa.diancxxb_id,fa.zhilb_id, fa.meikxxb_id,fa.faz_id,fa.pinzb_id) f, zhuanmb m, caiyb cai,zhuanmlb l \n");
		bf.append(" where f.zhilb_id=z.zhilb_id  and f.meikxxb_id=mk.id and cai.zhilb_id = z.zhilb_id \n");
		bf.append(" and f.faz_id=c.id and f.pinzb_id=p.id  \n");
		bf.append(" and z.id = m.zhillsb_id and m.zhuanmlb_id = l.id  and l.mingc = '"+huaybm+"' \n");
		bf.append(" and m.bianm='").append(this.getBianmValue().getValue()).append("' \n");
		bf.append(" and f.diancxxb_id=").append(visit.getDiancxxb_id()).append(" \n");
		bf.append(" union select z.id as zhillsbid,0 as jingz,z.beiz as bianh,decode(q.meikmc,null,'',q.meikmc) as kuangb,'' as faz,'' as pinz,nvl(to_char(z.huaysj,'yyyy-mm-dd'),'') huaysj,'' as chec,decode(q.caiyrq,null,'',to_char(q.caiyrq,'yyyy-mm-dd')) caiysj,decode(q.meikmc,null,'',q.meikmc) as meikdm\n");
		bf.append(" from zhillsb z,qitycyb q where z.id=q.zhillsb_id(+) and z.beiz='").append(this.getBianmValue().getValue()).append("'");
		bf.append(") group  by bianh,kuangb,faz,pinz,huaysj,meikdm");
		ResultSetList rsl=con.getResultSetList(bf.toString());
		
		if(rsl.next()){
//			判断是否显示矿别还是显示代码
			boolean isKuangb = MainGlobal.getXitxx_item("化验", "化验报告单显示矿别", 
					String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id()),
					"是").equals("是");
						
			boolean isMeikdm = MainGlobal.getXitxx_item("化验", "化验报告单显示矿别代码", 
					String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id()),
					"是").equals("是");
			if (isKuangb){
				ArrHeader[0][2] = rsl.getString("kuangb"); // 煤矿单位名称
				ArrHeader[0][4] = rsl.getString("faz"); // 发站(港)
			}else{
				if(isMeikdm){
					ArrHeader[0][2]=rsl.getString("meikdm");
						
				}else{
					ArrHeader[0][2] ="";
				}
				ArrHeader[0][4]="";
			}
			
			
			/*   //由 发热量 那里 计算出 had的值  赋值 即可
			if(this.getQingly().equals(yuansfx)){//氢 来源 是元素分析
				if(this.getQingpz()){//氢 配置正确
					ArrHeader[0][7] = this.getQingjz(); // 氢
				}else{
					ArrHeader[0][7] = "";//配置不正确   为空
				}
			}else{ //自动计算
				ArrHeader[0][7] =this.getKonggjq_v(con, this.getBianmValue().getValue()) ;
			}
			*/
		
			ArrHeader[0][9] = String.valueOf(rsl.getDouble("jingz")); // 煤量
			ArrHeader[1][2] = rsl.getString("pinz"); // 品种
			ArrHeader[1][4] = this.getBianmValue().getValue(); // 编号
			if (!rsl.getString("huaysj").equals("")){
				ArrHeader[1][7] = rsl.getString("huaysj"); // 化验日期
			}else{
				ArrHeader[1][7] = getRiq();
			}
			
			String sql="select r.quanc from yangpdhb y,caiyryglb c,renyxxb r where y.id=c.yangpdhb_id and c.renyxxb_id=r.id "
				+ " and y.zhilblsb_id="+rsl.getLong("zhillsbid");
			String caiyry="";
			ResultSetList rscyry=con.getResultSetList(sql);
			while (rscyry.next()){
				if (caiyry.equals("")){
					caiyry=rscyry.getString("quanc");
				}else{
					caiyry=caiyry+","+rscyry.getString("quanc");
				}
			}
			rscyry.close();
			//制样人员
			String zhiyry="";
			
			sql="select r.quanc from yangpdhb y,zhiyryglb c,renyxxb r where y.id=c.yangpdhb_id and c.renyxxb_id=r.id "
				+ " and y.zhilblsb_id="+rsl.getLong("zhillsbid");
			ResultSetList rszyry=con.getResultSetList(sql);
			while (rszyry.next()){
				if (zhiyry.equals("")){
					zhiyry=rszyry.getString("quanc");
				}else{
					zhiyry=zhiyry+","+rszyry.getString("quanc");
				}
			}
			rszyry.close();
			
			
			if (caiyry.equals("")){
				rscyry=con.getResultSetList("select decode(q.caiyry,null,'',q.caiyry) caiyry,decode(q.zhiyry,null,'',q.zhiyry) as zhiyry from zhillsb z,qitycyb q where z.id=q.zhillsb_id(+) and z.beiz='"+this.getBianmValue().getValue()+"'");
				if (rscyry.next()){
					caiyry=rscyry.getString("caiyry");
					zhiyry=rscyry.getString("zhiyry");
				}
			}else{
				//zhiyry=caiyry;
			}
			
			ArrHeader[1][7] = caiyry; // 采样人员
			ArrHeader[2][2] = rsl.getString("chec"); // 船次
			ArrHeader[2][4] = wend_shid; // 湿度
			
			if(!rsl.getString("caiysj").equals("")){
				ArrHeader[2][7] = rsl.getString("caiysj"); // 采样时间
			}else{
				ArrHeader[2][7] = "";
			}
			
			ArrHeader[2][9] = zhiyry; // 制样人
		}
		
		if ("".equals(ArrHeader[1][4])) {
			ArrHeader[1][4] = this.getBianmValue().getValue(); // 编号
		}
		
		if ("".equals(ArrHeader[1][7])) {
			ArrHeader[1][7] = this.getRiqEnd();
		}
		rsl.close();
		Table tb = new Table(1, 1);
		
		
		//  工业分析  全水分
		bf.setLength(0);
		bf.append(" select h.huayz,h.cis,h.qimbh,h.qimzl,h.shenhy,h.huayy,\n");
		bf.append(" h.qimzl+h.meiyzl qishizl,h.meiyzl shiyzl,h.honghzzl,h.zongzl,\n");
		bf.append(" h.honghzzl1 jiancxsyhzzl,shenhzt,h.shebxh \n");
		bf.append(" from huaygyfxb h,item f,itemsort i \n");
		bf.append(" where h.fenxxmb_id=f.id and f.bianm='").append(Huayysjlsh.Mt).append("' \n");
		bf.append(" and h.bianm='").append(this.getBianmValue().getValue()).append("' \n");
		bf.append(" and h.diancxxb_id=").append(visit.getDiancxxb_id());
		bf.append("  and f.itemsortid=i.itemsortid and i.bianm='").append(huayysjl_bianm).append("' \n");
		
		//ww添加日期判读
		bf.append(" and h.riq>=(to_date('" + this.getRiqEnd() + "','yyyy-mm-dd')-2) and h.riq<=(to_date('" + this.getRiqEnd() + "','yyyy-mm-dd')+2) \n");
		
		bf.append(" and h.shenhzt>=1 order by h.cis");
		rsl=con.getResultSetList(bf.toString());
	
		int count=rsl.getRows();//返回记录个数  平均值用
		if (count>2) {
			this.setMsg("全水分数值超过2个");
			return;
		}
		//rsl.beforefirst();
		if(count==0){//无值   提示有误
			if(this.getBianmValue().getId()!=-1 && this.getMsg().equals("")){//  排除 请选择  时  提示的信息 
//				this.setMsg("读取全水分数据失败，请检查!");//禁用按钮
			}
			ToolbarButton tbn=(ToolbarButton)this.getToolbar().getItem("commit_id");
			tbn.setDisabled(true);
			
			ToolbarButton tbn1=(ToolbarButton)this.getToolbar().getItem("cancel_id");
			tbn1.setDisabled(true);
		}
		
		double quansf_sum=0;//全水分 总值
		String shenhry="";//审核人员
		String huayry="";//化验人员
		int colsnum =1;
		while(rsl.next()){
			////rsl.getInt("cis")
			int initRow = 4;
			int initCol = 2 + colsnum;
			double ganzhjszl=0;//干燥后煤样减少质量
			if(rsl.getString("shebxh").equals("SDTGA300") || rsl.getDouble("shiyzl")<11){
				ArrHeader[initRow++][initCol] = rsl.getString("qimbh");//称量瓶号
				ArrHeader[initRow++][initCol] = tb.format(rsl//称量瓶质量
						.getString("qimzl"), "0.0000");
				ArrHeader[initRow++][initCol] = tb.format(rsl//式样质量+称量瓶质量
						.getString("qishizl"), "0.0000");
				ArrHeader[initRow++][initCol] = tb.format(rsl//式样质量
						.getString("shiyzl"), "0.0000");
				ArrHeader[initRow++][initCol] = tb.format(rsl//烘干后总质量
						.getString("honghzzl"), "0.0000");
				ArrHeader[initRow++][initCol] = tb.format(rsl//检查性试验后质量
						.getString("jiancxsyhzzl"), "0.0000");
				
				ganzhjszl=rsl.getDouble("qishizl")-rsl.getDouble("honghzzl");
				ArrHeader[initRow++][initCol] = tb.format(ganzhjszl+"", "0.0000");
			}else{
				ArrHeader[initRow++][initCol] = rsl.getString("qimbh");//称量瓶号
				ArrHeader[initRow++][initCol] = tb.format(rsl//称量瓶质量
						.getString("qimzl"), "0.00");
				ArrHeader[initRow++][initCol] = tb.format(rsl//式样质量+称量瓶质量
						.getString("qishizl"), "0.00");
				ArrHeader[initRow++][initCol] = tb.format(rsl//式样质量
						.getString("shiyzl"), "0.00");
				ArrHeader[initRow++][initCol] = tb.format(rsl//烘干后总质量
						.getString("honghzzl"), "0.00");
				ArrHeader[initRow++][initCol] = tb.format(rsl//检查性试验后质量
						.getString("jiancxsyhzzl"), "0.00");
				
				ganzhjszl=rsl.getDouble("qishizl")-rsl.getDouble("honghzzl");
				ArrHeader[initRow++][initCol] = tb.format(ganzhjszl+"", "0.00");
			}
			
			
			//double quansMT=tb.Round_New(ganzhjszl/rsl.getDouble("shiyzl")*100,1);//全水分Mt
			double quansMT=tb.Round_New(rsl.getDouble("huayz"),2);//全水分Mt
			ArrHeader[initRow++][initCol] = tb.format(quansMT+"","0.00");
			quansf_sum+=Double.valueOf(tb.format(quansMT+"","0.00")).doubleValue();
			
			if(rsl.getString("shenhy")!=null){
				shenhry=rsl.getString("shenhy");
			}
			if(rsl.getString("huayy")!=null){
				huayry=rsl.getString("huayy");
			}
			colsnum=colsnum+1;
		}
		double pingjz=0;
		quansf_sum=Double.valueOf(tb.format(quansf_sum+"","0.00")).doubleValue();
		if(count==0){
			pingjz=0;
		}else{
			pingjz=tb.Round_New(quansf_sum/count,1);
		}
		
		ArrHeader[12][3] = tb.format(pingjz+"","0.0");
		
		ArrHeader[13][2]+=shenhry;
		ArrHeader[13][4]=huayry;
		
		rsl.close();
		
		
		
		//工业分析 水分
		
		bf.setLength(0);
		bf.append(" select h.huayz,h.cis,h.qimbh,h.qimzl,h.shenhy,h.huayy,\n");
		bf.append(" h.qimzl+h.meiyzl qishizl,h.meiyzl shiyzl,h.honghzzl,h.zongzl,\n");
		bf.append(" h.honghzzl1 jiancxsyhzzl \n");
		bf.append(" from huaygyfxb h,item f,itemsort i \n");
		bf.append(" where h.fenxxmb_id=f.id and f.bianm='").append(Huayysjlsh.Mad).append("' \n");
		bf.append(" and h.bianm='").append(this.getBianmValue().getValue()).append("' \n");
		bf.append(" and h.diancxxb_id=").append(visit.getDiancxxb_id());
		bf.append("  and f.itemsortid=i.itemsortid and i.bianm='").append(huayysjl_bianm).append("' \n");
		
		//ww添加日期判读
		bf.append(" and h.riq>=(to_date('" + this.getRiqEnd() + "','yyyy-mm-dd')-2) and h.riq<=(to_date('" + this.getRiqEnd() + "','yyyy-mm-dd')+2) \n");
		bf.append(" and  h.shenhzt>=1 order by h.cis");
		
		rsl=con.getResultSetList(bf.toString());
	
		count=rsl.getRows();//返回记录个数  平均值用
		if (count>2) {
			this.setMsg("水分数值超过2个");
			return;
		}
		if(count==0){//无值   提示有误
			
			if(this.getBianmValue().getId()!=-1 && this.getMsg().equals("")){//  排除 请选择  时  提示的信息 
//				this.setMsg("读取空干基水分数据失败，请检查!");//禁用按钮
			}
			
			ToolbarButton tbn=(ToolbarButton)this.getToolbar().getItem("commit_id");
			tbn.setDisabled(true);
			
			ToolbarButton tbn1=(ToolbarButton)this.getToolbar().getItem("cancel_id");
			tbn1.setDisabled(true);
		}
		
		double shuif_sum=0;//水分总值
				shenhry="";
				huayry="";
		colsnum =1;		
		while(rsl.next()){
			//int colsnum = count;//rsl.getInt("cis")
			int initRow = 4;
			int initCol = 7 + colsnum;
			ArrHeader[initRow++][initCol] = rsl.getString("qimbh");//称量瓶号
			ArrHeader[initRow++][initCol] = tb.format(rsl//称量瓶质量
					.getString("qimzl"), "0.0000");
			ArrHeader[initRow++][initCol] = tb.format(rsl//式样质量+称量瓶质量
					.getString("qishizl"), "0.0000");
			ArrHeader[initRow++][initCol] = tb.format(rsl//式样质量
					.getString("shiyzl"), "0.0000");
			ArrHeader[initRow++][initCol] = tb.format(rsl//烘干后总质量
					.getString("honghzzl"), "0.0000");
			ArrHeader[initRow++][initCol] = tb.format(rsl//检查性试验后质量
					.getString("jiancxsyhzzl"), "0.0000");
			
			double ganzhjszl=0;//干燥后煤样减少质量
			ganzhjszl=rsl.getDouble("qishizl")-rsl.getDouble("honghzzl");
			ArrHeader[initRow++][initCol] = tb.format(ganzhjszl+"", "0.0000");
			
			//double quansMT=tb.Round_New(ganzhjszl/rsl.getDouble("shiyzl")*100,2);//水分
			double quansMT=tb.Round_New(rsl.getDouble("huayz"),2);//水分
			ArrHeader[initRow++][initCol] = tb.format(quansMT+"","0.00");
			shuif_sum+=Double.valueOf(tb.format(quansMT+"","0.00")).doubleValue();
			
			if(rsl.getString("shenhy")!=null){
				shenhry=rsl.getString("shenhy");
			}
			if(rsl.getString("huayy")!=null){
				huayry=rsl.getString("huayy");
			}
			colsnum=colsnum+1;
		}
		double pingjz_shuif=0;
		shuif_sum=Double.valueOf(tb.format(shuif_sum+"","0.00")).doubleValue();
		if(count==0){
			pingjz_shuif=0;
		}else{
			pingjz_shuif=tb.Round_New(shuif_sum/count,2);
		}
		
		ArrHeader[12][8] = tb.format(pingjz_shuif+"","0.00");
		
		ArrHeader[13][7]+=shenhry;
		ArrHeader[13][9]=huayry;
		
		rsl.close();
//工业分析  灰分
		
		bf.setLength(0);
		bf.append(" select h.huayz,h.cis,h.qimbh,h.qimzl,h.shenhy,h.huayy,\n");
		bf.append(" h.qimzl+h.meiyzl qishizl,h.meiyzl shiyzl,h.honghzzl,h.zongzl,\n");
		bf.append(" h.honghzzl1 jiancxsyhzzl \n");
		bf.append(" from huaygyfxb h,item f,itemsort i \n");
		bf.append(" where h.fenxxmb_id=f.id and f.bianm='").append(Huayysjlsh.Aad).append("' \n");
		bf.append(" and h.bianm='").append(this.getBianmValue().getValue()).append("' \n");
		bf.append(" and h.diancxxb_id=").append(visit.getDiancxxb_id());
		bf.append("  and f.itemsortid=i.itemsortid and i.bianm='").append(huayysjl_bianm).append("' \n");
		
		//ww添加日期判读
		bf.append(" and h.riq>=(to_date('" + this.getRiqEnd() + "','yyyy-mm-dd')-2) and h.riq<=(to_date('" + this.getRiqEnd() + "','yyyy-mm-dd')+2) \n");
		
		bf.append(" and h.shenhzt>=1 order by h.cis");
		
		rsl=con.getResultSetList(bf.toString());
		
		count=rsl.getRows();//返回记录个数  平均值用
		if (count>2) {
			this.setMsg("灰分数值超过2个");
			return;
		}
		if(count==0){//无值   提示有误
			if(this.getBianmValue().getId()!=-1 && this.getMsg().equals("")){//  排除 请选择  时  提示的信息 
//				this.setMsg("读取灰分数据失败，请检查!");//禁用按钮
			}
			ToolbarButton tbn=(ToolbarButton)this.getToolbar().getItem("commit_id");
			tbn.setDisabled(true);
			
			ToolbarButton tbn1=(ToolbarButton)this.getToolbar().getItem("cancel_id");
			tbn1.setDisabled(true);
		}
		
		double huif_sum=0;//灰分总值
				shenhry="";
				huayry="";
				colsnum =1;		
		while(rsl.next()){
			//int colsnum = count;//rsl.getInt("cis");
			int initRow = 14;
			int initCol = 7 + colsnum;
			ArrHeader[initRow++][initCol] = rsl.getString("qimbh");//称量瓶号
			ArrHeader[initRow++][initCol] = tb.format(rsl//称量瓶质量
					.getString("qimzl"), "0.0000");
			ArrHeader[initRow++][initCol] = tb.format(rsl//式样质量+称量瓶质量
					.getString("qishizl"), "0.0000");
			ArrHeader[initRow++][initCol] = tb.format(rsl//式样质量
					.getString("shiyzl"), "0.0000");
			ArrHeader[initRow++][initCol] = tb.format(rsl//烘干后总质量
					.getString("honghzzl"), "0.0000");
			ArrHeader[initRow++][initCol] = tb.format(rsl//检查性试验后质量
					.getString("jiancxsyhzzl"), "0.0000");
			
			double ganzhjszl=0;//干燥后煤样减少质量
			ganzhjszl=rsl.getDouble("honghzzl")-rsl.getDouble("qimzl");//   残留物 质量
			ArrHeader[initRow++][initCol] = tb.format(ganzhjszl+"", "0.0000");
			
			//double quansMT=tb.Round_New(ganzhjszl/rsl.getDouble("shiyzl")*100,2);//  灰分  Aad
			double quansMT=tb.Round_New(rsl.getDouble("huayz"),2);//  灰分  Aad
			ArrHeader[initRow++][initCol] = tb.format(quansMT+"","0.00");
			huif_sum+=Double.valueOf(tb.format(quansMT+"","0.00")).doubleValue();
			
			if(rsl.getString("shenhy")!=null){
				shenhry=rsl.getString("shenhy");
			}
			if(rsl.getString("huayy")!=null){
				huayry=rsl.getString("huayy");
			}
			colsnum =colsnum +1;
		}
		huif_sum=Double.valueOf(tb.format(huif_sum+"","0.00")).doubleValue();
		double pingjz_huif=0;
		if(count==0){
			pingjz_huif=0;
		}else{
			pingjz_huif=tb.Round_New(huif_sum/count,2);
		}
		
		ArrHeader[22][8] = tb.format(pingjz_huif+"","0.00");
		
		ArrHeader[23][7]+=shenhry;
		ArrHeader[23][9]=huayry;
		
		rsl.close();
		
		
		//工业分析  挥发分
		
		bf.setLength(0);
		bf.append(" select h.huayz,h.cis,h.qimbh,h.qimzl,h.shenhy,h.huayy,\n");
		bf.append(" h.qimzl+h.meiyzl qishizl,h.meiyzl shiyzl,h.honghzzl,h.zongzl,\n");
		bf.append(" h.honghzzl1 jiancxsyhzzl \n");
		bf.append(" from huaygyfxb h,item f,itemsort i \n");
		bf.append(" where h.fenxxmb_id=f.id and f.bianm='").append(Huayysjlsh.Vad).append("' \n");
		bf.append(" and h.bianm='").append(this.getBianmValue().getValue()).append("' \n");
		bf.append(" and h.diancxxb_id=").append(visit.getDiancxxb_id());
		bf.append("  and f.itemsortid=i.itemsortid and i.bianm='").append(huayysjl_bianm).append("' \n");
		//ww添加日期判读
		bf.append(" and h.riq>=(to_date('" + this.getRiqEnd() + "','yyyy-mm-dd')-2) and h.riq<=(to_date('" + this.getRiqEnd() + "','yyyy-mm-dd')+2) \n");
		bf.append(" and h.shenhzt>=1 order by h.cis");
		
		rsl=con.getResultSetList(bf.toString());
		
		count=rsl.getRows();//返回记录个数  平均值用
		if (count>2) {
			this.setMsg("挥发分数值超过2个");
			return;
		}
		if(count==0){//无值   提示有误
			if(this.getBianmValue().getId()!=-1 && this.getMsg().equals("")){//  排除 请选择  时  提示的信息 
//				this.setMsg("读取挥发分数据失败，请检查!");//禁用按钮
			}
			ToolbarButton tbn=(ToolbarButton)this.getToolbar().getItem("commit_id");
			tbn.setDisabled(true);
			
			ToolbarButton tbn1=(ToolbarButton)this.getToolbar().getItem("cancel_id");
			tbn1.setDisabled(true);
		}
		
		double huiff_sum=0;//挥发分总值
				shenhry="";
				huayry="";
		colsnum =1;		
		while(rsl.next()){
			//int colsnum =count;// rsl.getInt("cis");
			int initRow = 14;
			int initCol = 2 + colsnum;
			ArrHeader[initRow++][initCol] = rsl.getString("qimbh");//坩埚号
			ArrHeader[initRow++][initCol] = tb.format(rsl//坩埚质量
					.getString("qimzl"), "0.0000");
			ArrHeader[initRow++][initCol] = tb.format(rsl//式样质量+坩埚质量
					.getString("qishizl"), "0.0000");
			ArrHeader[initRow++][initCol] = tb.format(rsl//式样质量
					.getString("shiyzl"), "0.0000");
			ArrHeader[initRow++][initCol] = tb.format(rsl//烘干后总质量
					.getString("honghzzl"), "0.0000");
//			ArrHeader[initRow++][initCol] = tb.format(rsl//检查性试验后质量
//					.getString("jiancxsyhzzl"), "0.0");
			
			double ganzhjszl=0;//干燥后煤样减少质量
			ganzhjszl=rsl.getDouble("qishizl")-rsl.getDouble("honghzzl");
			ArrHeader[initRow++][initCol] = tb.format(ganzhjszl+"", "0.0000");
			
			//double quansMT=tb.Round_New(ganzhjszl/rsl.getDouble("shiyzl")*100-pingjz_shuif,2);//挥发分  -水分的均值
			double quansMT=tb.Round_New(rsl.getDouble("huayz"),2);//挥发分  -水分的均值
			ArrHeader[initRow++][initCol] = tb.format(quansMT+"","0.00");
			huiff_sum+=Double.valueOf(tb.format(quansMT+"","0.00")).doubleValue();
			
			if(rsl.getString("shenhy")!=null){
				shenhry=rsl.getString("shenhy");
			}
			if(rsl.getString("huayy")!=null){
				huayry=rsl.getString("huayy");
			}
			colsnum =colsnum+1;
		}
		huiff_sum=Double.valueOf(tb.format(huiff_sum+"","0.00")).doubleValue();
		double pingjz_huiff=0;
		if(count==0){
			pingjz_huiff=0;
		}
		else{
			pingjz_huiff=tb.Round_New(huiff_sum/count,2);
		}
		
		
		ArrHeader[21][3] = tb.format(pingjz_huiff+"","0.00");
		
		double Vdaf_v=this.getVdaf_v(pingjz_huiff, pingjz_shuif,pingjz_huif);
		ArrHeader[22][3] = tb.format(Vdaf_v+"", "0.00");
//			
		ArrHeader[23][2]+=shenhry;
		ArrHeader[23][4]=huayry;
		
		rsl.close();
		
		
		
		
		
		//  硫分表  全硫
		
		double Stad_v=0;
		double Std_v=0;
		String sign="";
		if(this.getLiuly().equals(yuansfx)){//  系统配置 是 元素分析
			if(this.getLiupz()){//硫配置 正确
				
			    sign=this.getYuansSign(con, _liu, visit.getDiancxxb_id()+"");
				
				if(sign.toUpperCase().indexOf("STAD")!=-1){//系统配置的是 stad
					
					if(this.getBianmValue().getId()!=-1){//  当 是 请选择 时  不要显示
						ArrHeader[29][3]=tb.format(this.getLiujz(), "0.00");
						Stad_v=Double.valueOf(this.getLiujz()).doubleValue();
						Std_v=this.getStd_v(Double.valueOf(this.getLiujz()).doubleValue(), pingjz_shuif);
						ArrHeader[30][3]=tb.format(Std_v+"", "0.00");
					}
					
				}
				if(sign.toUpperCase().indexOf("STD")!=-1){//系统配置的是 std
					if(this.getBianmValue().getId()!=-1){
						Stad_v=this.getStad_v(Double.valueOf(this.getLiujz()).doubleValue(), pingjz_shuif);
						ArrHeader[29][3]=tb.format(Stad_v+"", "0.00");
						Std_v=Double.valueOf(this.getLiujz()).doubleValue();
						ArrHeader[30][3]=tb.format(this.getLiujz(), "0.00");
					}
					
				}
			}else{
				
			}
		}else{//自动计算  取值  赋值
			
			
			bf.setLength(0);
			bf.append(" select h.cis,h.liuf, h.qimbh,h.qimzl,h.qimzl+h.meiyzl qishizl,\n");
			bf.append(" h.meiyzl shiyzl,h.shenhy,h.huayy \n");
			bf.append(" from huaylfb h,item f,itemsort i \n");
			bf.append(" where h.fenxxmb_id=f.id and f.bianm='").append(Huayysjlsh.Stad).append("' \n");
			bf.append(" and h.bianm='").append(this.getBianmValue().getValue()).append("' \n");
			bf.append(" and h.diancxxb_id=").append(visit.getDiancxxb_id());
			bf.append("  and f.itemsortid=i.itemsortid and i.bianm='").append(huayysjl_bianm).append("' \n");
			//ww添加日期判读
			bf.append(" and h.riq>=(to_date('" + this.getRiqEnd() + "','yyyy-mm-dd')-2) and h.riq<=(to_date('" + this.getRiqEnd() + "','yyyy-mm-dd')+2) \n");
			bf.append(" and h.shenhzt>=1 order by h.cis");
			
			rsl=con.getResultSetList(bf.toString());
			
			count=rsl.getRows();//返回记录个数  平均值用
			if (count>2) {
				this.setMsg("全硫数值超过2个");
				return;
			}
			if(count==0){//无值   提示有误
				if(this.getBianmValue().getId()!=-1 && this.getMsg().equals("")){//  排除 请选择  时  提示的信息 
//					this.setMsg("读取硫分数据失败，请检查!");//禁用按钮
				}
				ToolbarButton tbn=(ToolbarButton)this.getToolbar().getItem("commit_id");
				tbn.setDisabled(true);
				
				ToolbarButton tbn1=(ToolbarButton)this.getToolbar().getItem("cancel_id");
				tbn1.setDisabled(true);
			}
			
			double quanl_sum=0;//全硫总值
					shenhry="";
					huayry="";
			colsnum =1;		
			while(rsl.next()){
				//int colsnum =count;// rsl.getInt("cis");
				int initRow = 24;
				int initCol = 2 + colsnum;
				ArrHeader[initRow++][initCol] = rsl.getString("qimbh");//称量瓶号
				if (rsl.getDouble("qimzl")>0){
					ArrHeader[initRow++][initCol] = tb.format(rsl//称量瓶质量
							.getString("qimzl"), "0.0000");
				}else{
					ArrHeader[initRow++][initCol]="";
				}
				if (rsl.getDouble("qimzl")>0){
					ArrHeader[initRow++][initCol] = tb.format(rsl//式样质量+称量瓶质量
							.getString("qishizl"), "0.0000");
				}else{
					ArrHeader[initRow++][initCol]="";
				}
				
				ArrHeader[initRow++][initCol] = tb.format(rsl//式样质量
						.getString("shiyzl"), "0.0000");
		
				
				double liuf=tb.Round_New(rsl.getDouble("liuf"),2);
				quanl_sum+=liuf;
				
				ArrHeader[initRow++][initCol] = tb.format(liuf+"", "0.00");
				
				if(rsl.getString("shenhy")!=null){
					shenhry=rsl.getString("shenhy");
				}
				if(rsl.getString("huayy")!=null){
					huayry=rsl.getString("huayy");
				}
				colsnum =colsnum+1;
			}
			quanl_sum=Double.valueOf(tb.format(quanl_sum+"","0.00")).doubleValue();
			double pingjz_quanl=0;
			if(count==0){
				pingjz_quanl=0;
			}else{
				pingjz_quanl=tb.Round_New(quanl_sum/count,2);
			}
			
			Stad_v=pingjz_quanl;
			ArrHeader[29][3] = tb.format(pingjz_quanl+"","0.00");
			Std_v=this.getStd_v(pingjz_quanl, pingjz_shuif);
			ArrHeader[30][3] = tb.format(this.getStd_v(pingjz_quanl, pingjz_shuif)+"","0.00");
			              
			ArrHeader[31][2]+=shenhry;
			ArrHeader[31][4]=huayry;
			
			rsl.close();
			
			
		}
		
		
		
		//发热量 
	
		
		bf.setLength(0);
		bf.append(" select h.cis,h.qimzl,h.farl,h.meiyzl shiyzl,h.shenhy,h.huayy \n");
		bf.append(" from huayrlb h,item f,itemsort i  \n");
		bf.append(" where h.fenxxmb_id=f.id and f.bianm='").append(Huayysjlsh.Qbad).append("' \n");
		bf.append(" and h.bianm='").append(this.getBianmValue().getValue()).append("' \n");
		bf.append(" and h.diancxxb_id=").append(visit.getDiancxxb_id());
		bf.append("  and f.itemsortid=i.itemsortid and i.bianm='").append(huayysjl_bianm).append("' \n");
		//ww添加日期判读
		bf.append(" and h.riq>=(to_date('" + this.getRiqEnd() + "','yyyy-mm-dd')-2) and h.riq<=(to_date('" + this.getRiqEnd() + "','yyyy-mm-dd')+2) \n");
		bf.append(" and h.shenhzt>=1 order by h.cis");
		
		rsl=con.getResultSetList(bf.toString());
		
		count=rsl.getRows();//返回记录个数  平均值用
		if (count>2) {
			this.setMsg("发热量数值超过2个");
			return;
		}
		if(count==0){//无值   提示有误
			if(this.getBianmValue().getId()!=-1 && this.getMsg().equals("")){//  排除 请选择  时  提示的信息 
//				this.setMsg("读取弹筒热数据失败，请检查!");//禁用按钮
			}
			ToolbarButton tbn=(ToolbarButton)this.getToolbar().getItem("commit_id");
			tbn.setDisabled(true);
			
			ToolbarButton tbn1=(ToolbarButton)this.getToolbar().getItem("cancel_id");
			tbn1.setDisabled(true);
		}
		
		double farl_sum=0;//灰分总值
				shenhry="";
				huayry="";
		colsnum =1;		
		while(rsl.next()){
			//int colsnum =count;// rsl.getInt("cis");
			int initRow = 24;
			int initCol = 7 + colsnum;
		
			if (rsl.getDouble("qimzl")>0){
				ArrHeader[initRow++][initCol] = tb.format(rsl//坩埚质量
						.getString("qimzl"), "0.0000");
			}else{
				ArrHeader[initRow++][initCol] = "";
			}
			ArrHeader[initRow++][initCol]="";//
			ArrHeader[initRow++][initCol] = tb.format(rsl//天平 式样质量
					.getString("shiyzl"), "0.0000");
			
			ArrHeader[initRow++][initCol]=tb.format(rsl.getString("farl"), "0");
			farl_sum+=Double.valueOf(tb.format(rsl.getDouble("farl")+"","0")).doubleValue();
			
			
			if(rsl.getString("shenhy")!=null){
				shenhry=rsl.getString("shenhy");
			}
			if(rsl.getString("huayy")!=null){
				huayry=rsl.getString("huayy");
			}
			colsnum =colsnum+1;
		}
		farl_sum=Double.valueOf(tb.format(farl_sum+"","0")).doubleValue();
		double pingjz_farl=0;
		if(count==0){
			pingjz_farl=0;
		}else{
			pingjz_farl=tb.Round_New(farl_sum/count,0);
		}
		
		
		ArrHeader[28][8] = tb.format(pingjz_farl+"","0");
		double Qgrad=tb.Round_New(this.getQgrad_v(pingjz_farl/1000, Stad_v),0);
		ArrHeader[29][8] = tb.format(Qgrad+"","0");
		
		//得到 xitxxb 中 氢的符号  had？ hdaf？
		String sig_had=this.getYuansSign(con, _qing, visit.getDiancxxb_id()+"");
		
		double Had_v=0;
		double Hdaf_v=0;
		double Qnetar=0;
		if(this.getBianmValue().getId()!=-1){
			
			//ww 2010-05-11 
			//如果是从元素分析中取数据，根据煤矿取得最近的氢值 
			if(this.getQingly(con).equals(yuansfx)){
				
				ResultSetList rsHad = con.getResultSetList(
					"select ysf.zhi as zhi from yuansfxb ysf ,yuansxmb xm,\n" +
					"(SELECT meikxxb_id FROM yangpdhb WHERE zhilblsb_id ="+
					"(SELECT zhillsb_id FROM zhuanmb WHERE zhuanmlb_id=100663 AND bianm='" + this.getBianmValue().getValue() + "' AND ROWNUM=1)) m \n" +
					"where ysf.yuansxmb_id=xm.id\n" + 
					"and m.meikxxb_id = ysf.meikxxb_id\n" + 
					"and xm.mingc='" + sig_had + "'\n" + 
					"and ysf.zhuangt = 1\n" + 
					"AND ysf.riq< (\n" +
					"SELECT MAX(riq) AS riq FROM  huaygyfxb  WHERE  bianm= '" + this.getBianmValue().getValue() + "'\n" + 
					"--AND riq BETWEEN to_date('"+ this.getRiq() +"','yyyy-mm-dd') AND to_date('" + this.getRiqEnd() + "','yyyy-mm-dd')\n" + 
					") \n" +
					"order by ysf.riq desc"
					);
				if (rsHad.next()) {
				//重新更新氢值
				this.setQingpz(true);
				this.setQingjz(rsHad.getString("zhi"));
				} else {
					//清空氢值
//					this.setQingpz(false);
					this.setQingjz("0");
				}
			}
			
			if(sig_had.toUpperCase().equals("HAD")){
				Had_v=this.getHad_v(con,pingjz_huif, pingjz_shuif, pingjz_huiff, pingjz,sig_had,this.getBianmValue().getValue());
				Hdaf_v=this.getHdaf_v_ByHad(pingjz_huiff, pingjz_shuif, Had_v);
			}else if(sig_had.toUpperCase().equals("HDAF")){
				Hdaf_v=this.getHad_v(con,pingjz_huif, pingjz_shuif, pingjz_huiff, pingjz,sig_had,this.getBianmValue().getValue());
				Had_v=this.getHad_v_ByHdaf(pingjz_huif, pingjz_shuif, Hdaf_v);
			}
			
			ArrHeader[0][7] = tb.format(Had_v+"","0.00");
			
			Qnetar=this.getQnetar_v(Qgrad/1000, pingjz_huif, pingjz, Had_v, pingjz_shuif);
			ArrHeader[30][8] = tb.format(Qnetar+"", "0.00");             
		}
		
		ArrHeader[31][7]+=shenhry;
		ArrHeader[31][9]=huayry;
		
		rsl.close();
		
		con.Close();
//--------------------- 通过计算公式  计算值  是否正确	判断条件	-------------
		
		
		boolean t1=false;
		boolean t2=false;
		if(this.getLiuly().equals(yuansfx)){//  系统配置 是 元素分析  硫
			
			if(Stad_v<=0){
				t1=true;//硫 的值  也需要 判断 是否满足
			}
			
			
			
		}else{   //采集数据  也需要判断？
			
		}
		
		if(this.getQingly().equals(yuansfx)){ //  系统配置 是 元素分析  氢
			
			if(Had_v<=0){
				t2=true;//氢的 值  也需要判断 是否满足
			}
			
		}else{
			
		}
		
		
	
	if(pingjz<=0 || pingjz_shuif<=0 || pingjz_huiff<=0 || pingjz_huif<=0  || pingjz_farl<=0 || t1 || t2)
	{
		//数据 计算 没有意义 清空
		
		if(!this.getQingly().equals(yuansfx)){ //  不是 元素分析  要清空
			
			Had_v=0;
			ArrHeader[0][7] = "";
		}else{
			if(sig_had.toUpperCase().equals("HDAF")){
				Had_v=0;
				ArrHeader[0][7] = "";
			}else{
				Hdaf_v=0;
				}
		}
		
		if(!this.getLiuly().equals(yuansfx)){//  不是 元素分析  要清空
			Stad_v=0;
		}else{
			if(sign.toUpperCase().equals("STD")){
				Stad_v=0;
				ArrHeader[29][3]="";
			}else{
				Std_v=0;
				ArrHeader[30][3]= "";//Std
			}
		}
//		Vdaf_v=0;
//		ArrHeader[22][3] = "";//vdaf
		
		Qgrad=0;
		ArrHeader[29][8] = "";//Qgrad
		Qnetar=0;
		ArrHeader[30][8] = "";//Qnetar
		
	
		
		this.setAar_v(0);
		this.setAd_v(0);
		this.setFcad_v(0);
		this.setQgrd_v(0);
		
	}else{		
		this.setAar_v(tb.Round_New(pingjz_huif*(100-pingjz)/(100-pingjz_shuif),2));
		this.setAd_v(tb.Round_New(pingjz_huif*100/(100-pingjz_shuif),2));
		this.setFcad_v(tb.Round_New(100-pingjz_shuif-pingjz_huif-pingjz_huiff,2));
		this.setQgrd_v(tb.Round_New(Qgrad*100/(100-pingjz_shuif)/1000,3));
	}
		

		
		
		//--------存放计算要用的变量值
		
	this.setMt_v(pingjz);
	this.setMad_v(pingjz_shuif);
	this.setVad_v(pingjz_huiff);
	this.setAad_v(pingjz_huif);
	this.setStad_v(Stad_v);
	this.setQbad_v(pingjz_farl/1000);
	this.setHad_v(Had_v);
	this.setHdaf_v(Hdaf_v);
	
	this.setVdaf_v(Vdaf_v);
	this.setStd_v(Std_v);
	this.setQgrad_v(Qgrad/1000);
	this.setQnetar_v(Qnetar);
	
		
	
	}
	
	//计算 Had
	
	private double getHad_v(JDBCcon con,double Aad_v,double Mad_v,double Vad_v,double Mt_v,String sign,String bianm ){
		Table tb = new Table(1, 1);
		double had=0;
		double hdaf=0;
		String sql="";
		if(this.getQingly(con).equals(yuansfx)){//是元素分析
			
			if(this.getQingpz()){
				if(sign.toUpperCase().indexOf("HAD")!=-1){
					had=tb.Round_New(Double.valueOf(this.getQingjz()).doubleValue(),2);
					hdaf=tb.Round_New(had * 100 / (100 - Mad_v - Aad_v),2);
				}else{ //系统中设置的是 hdaf，据此 反求出 had
					hdaf=tb.Round_New(Double.valueOf(this.getQingjz()).doubleValue(),2);
					had=tb.Round_New(Double.valueOf(this.getQingjz()).doubleValue()*(100 - Mad_v - Aad_v)/100,2);
				}
				
				//2010-05-18  ww 
				//入厂样取元素分析
				//其他样化验有可能手动维护氢值	
				if ("0".equals(this.getQingjz())) {
					try{
						sql = "select nvl(q.had,0) as had from zhillsb z,qitycyb q where z.id=q.zhillsb_id(+) and z.beiz='"+bianm+"'" 
							+ " and z.huaysj>=(to_date('" + this.getRiqEnd() + "','yyyy-mm-dd')-2) and z.huaysj<=(to_date('" + this.getRiqEnd() + "','yyyy-mm-dd')+2) \n";
						ResultSet rs=con.getResultSet(sql);
							if (rs.next()){
								had=tb.Round_New(rs.getDouble("had"),2);
								hdaf=tb.Round_New(had * 100 / (100 - Mad_v - Aad_v),2);
							} 
						}catch(Exception e){
							e.printStackTrace();
						}
					}
			} 
			
		}else if (this.getQingly(con).equals(shougwh)){
			try{
				sql="select cai.had from zhuanmb m, caiyb cai,zhuanmlb l,zhillsb z"
					+ " where z.id = m.zhillsb_id and m.zhuanmlb_id = l.id  and l.mingc = '"+huaybm+"'"
					+ " and cai.zhilb_id=z.zhilb_id and m.bianm='"+bianm+"'"
					+ " union select nvl(q.had,0) as had from zhillsb z,qitycyb q where z.id=q.zhillsb_id(+) and z.beiz='"+bianm+"'";
				ResultSet rs=con.getResultSet(sql);
				if (rs.next()){
					had=tb.Round_New(rs.getDouble("had"),2);
					hdaf=tb.Round_New(had * 100 / (100 - Mad_v - Aad_v),2);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}else{//自动计算
			
			if(Aad_v<=0 || Mad_v<=0 || Vad_v<=0 || Mt_v<=0 ){
				return 0;
			}
			
			double Aar_v = tb.Round_New(Aad_v*(100-Mt_v)/(100-Mad_v),2);
			double Vd_v  = tb.Round_New(Vad_v*100/(100-Mad_v),2);
			double Ad_v  = tb.Round_New(Aar_v*100/(100-Aad_v),2);
			had=tb.Round_New((Vd_v*100/(100-Ad_v)*0.074+2.16)*(100-Mad_v-Aad_v)/100,2);
			hdaf=tb.Round_New(had * 100 / (100 - Mad_v - Aad_v),2);
		}
		if(sign.toUpperCase().indexOf("HDAF")!=-1){
			return hdaf;
		}
		return had;
	}
	
	private double getHdaf_v_ByHad(double Aad_v,double Mad_v,double Had_v){
		Table tb = new Table(1, 1);
		double hdaf=tb.Round_New(Had_v * 100 / (100 - Mad_v - Aad_v),2);
		
		return hdaf;
	}
	
	private double getHad_v_ByHdaf(double Aad_v,double Mad_v,double Hdaf_v){
		Table tb = new Table(1, 1);
		double had=tb.Round_New(Hdaf_v* (100 - Mad_v - Aad_v)/100,2);
		return had;
	}
	//计算  Qnetar
	
	private double getQnetar_v(double Qgrad_v,double Aad_v,double Mt_v,double Had_v,double Mad_v){
		Table tb = new Table(1, 1);
		if(Qgrad_v<=0 || Aad_v<=0 || Mt_v<=0 || Had_v<=0 ||  Mad_v<=0){
			return 0;
		}
		double qne=0;
		double Aar_v=tb.Round_New(Aad_v*(100-Mt_v)/(100-Mad_v),2);
	
		qne  = tb.Round_New(((Qgrad_v-0.206*Had_v)*(100-Mt_v)/(100-Mad_v)-0.023*Mt_v),3);
		qne  = tb.Round_New(qne,2);
		return qne;
	}
	//计算 Qgrad
	private double getQgrad_v(double Qbad_v,double Stad_v){
		double qgr_v=0;
		Table tb = new Table(1, 1);
		if(Qbad_v <= 16.700){
			qgr_v = tb.Round_New((Qbad_v-0.0941*Stad_v-0.001*Qbad_v)*1000,0);
		}else{
			 if(Qbad_v > 16.700 && Qbad_v <= 25.100){
				 qgr_v = tb.Round_New((Qbad_v-0.0941*Stad_v-0.0012*Qbad_v)*1000,0);
			 }else{
				 qgr_v = tb.Round_New((Qbad_v-0.0941*Stad_v-0.0016*Qbad_v)*1000,0);
			 }
		}
		
		return qgr_v;
	}
	// 计算 stad的值
	private double getStad_v(double Std_v,double Mad_v){
		double stad_v=0;
		Table tb = new Table(1, 1);
		stad_v=tb.Round_New(Std_v*(100-Mad_v)/100,2);
		return stad_v;
	}
	//计算 std的值
	private double getStd_v(double Stad_v,double Mad_v){
		
		double std_v=0;
		Table tb = new Table(1, 1);
		std_v=tb.Round_New(Stad_v*100/(100-Mad_v),2);
		
		return std_v;
	}
	
	//计算公式 计算  干燥无灰基挥发分Vdaf
	private double getVdaf_v(double Vad_v,double Mad_v,double Aad_v){
		double vdaf_v=0;
		Table tb = new Table(1, 1);
		double Vd_v  = tb.Round_New(Vad_v*100/(100-Mad_v),2);
		vdaf_v=tb.Round_New(Vad_v*100/(100-Aad_v-Mad_v),2);
		
		return vdaf_v;
	}
	
	//------------------------设置  变量值------------
	
	private void setMt_v(double value){
		Visit visit=(Visit)this.getPage().getVisit();
		visit.setDouble4(value);
	}
	
	private double getMt_v(){
		Visit visit=(Visit)this.getPage().getVisit();
		return visit.getDouble4();
	}
	private void setMad_v(double value){
		Visit visit=(Visit)this.getPage().getVisit();
		visit.setDouble5(value);
	}
	private double getMad_v(){
		Visit visit=(Visit)this.getPage().getVisit();
		return visit.getDouble5();
	}
	private void setVad_v(double value){
		Visit visit=(Visit)this.getPage().getVisit();
		visit.setDouble6(value);
	}
	private double getVad_v(){
		Visit visit=(Visit)this.getPage().getVisit();
		return visit.getDouble6();
	}
	private void setAad_v(double value){
		Visit visit=(Visit)this.getPage().getVisit();
		visit.setDouble7(value);
	}
	private double getAad_v(){
		Visit visit=(Visit)this.getPage().getVisit();
		return visit.getDouble7();
	}
	private void setStad_v(double value){
		Visit visit=(Visit)this.getPage().getVisit();
		visit.setDouble8(value);
	}
	private double getStad_v(){
		Visit visit=(Visit)this.getPage().getVisit();
		return visit.getDouble8();
	}
	private void setQbad_v(double value){
		Visit visit=(Visit)this.getPage().getVisit();
		visit.setDouble9(value);
	}
	private double getQbad_v(){
		Visit visit=(Visit)this.getPage().getVisit();
		return visit.getDouble9();
	}
	private void setHad_v(double value){
		Visit visit=(Visit)this.getPage().getVisit();
		visit.setDouble10(value);
	}
	private double getHad_v(){
		Visit visit=(Visit)this.getPage().getVisit();
		return visit.getDouble10();
	}
	private void setStd_v(double value){
		Visit visit=(Visit)this.getPage().getVisit();
		visit.setDouble11(value);
	}
	private double getStd_v(){
		Visit visit=(Visit)this.getPage().getVisit();
		return visit.getDouble11();
	}
	private void setQgrad_v(double value){
		Visit visit=(Visit)this.getPage().getVisit();
		visit.setDouble12(value);
	}
	private double getQgrad_v(){
		Visit visit=(Visit)this.getPage().getVisit();
		return visit.getDouble12();
	}
	private void setQnetar_v(double value){
		Visit visit=(Visit)this.getPage().getVisit();
		visit.setDouble13(value);
	}
	private double getQnetar_v(){
		Visit visit=(Visit)this.getPage().getVisit();
		return visit.getDouble13();
	}
	private void setVdaf_v(double value){
		Visit visit=(Visit)this.getPage().getVisit();
		visit.setDouble14(value);
	}
	private double getVdaf_v(){
		Visit visit=(Visit)this.getPage().getVisit();
		return visit.getDouble14();
	}
	
	private void setAar_v(double Aar_v){
		Visit visit=(Visit)this.getPage().getVisit();
		visit.setDouble16(Aar_v);
	}
	private double getAar_v(){
		
		
		Visit visit=(Visit)this.getPage().getVisit();
		return visit.getDouble16();
	}
	
	private void setAd_v(double Ad_v){
		Visit visit=(Visit)this.getPage().getVisit();
		visit.setDouble17(Ad_v);
	}
	private double getAd_v(){
		Visit visit=(Visit)this.getPage().getVisit();
		
		return visit.getDouble17();
	}
	
	private void setFcad_v(double Fcad_v){
		Visit visit=(Visit)this.getPage().getVisit();
		visit.setDouble18(Fcad_v);
	}
	private double getFcad_v(){
		Visit visit=(Visit)this.getPage().getVisit();
		
		return visit.getDouble18();
	}
	
	private void setHdaf_v(double value){
		Visit visit=(Visit)this.getPage().getVisit();
		visit.setDouble15(value);
	}
	private double getHdaf_v(){

//		double value= Round_New(this.getHad_v() * 100 / (100 - this.getMad_v() - this.getAad_v()));
		Visit visit=(Visit)this.getPage().getVisit();
		
		return visit.getDouble15();
	}
	
	private void setQgrd_v(double Qgrd_v){
		Visit visit=(Visit)this.getPage().getVisit();
		visit.setDouble19(Qgrd_v);
	}
	//Qgrd_v
	private double getQgrd_v(){
//		double value=Round_New(this.getQgrad_v()*100/(100-this.getMad_v()));
		Visit visit=(Visit)this.getPage().getVisit();
		return visit.getDouble19();
	}
	
	private double  getSdaf_v(double Std,double Ad){
		double value=0;
		Table tb=new Table(1,1);
		value=tb.Round_New(Std*100/(100-Ad), 2);
		return value;
	}
	
	private double getQgrad_daf_v(double Qgrad,double Aad,double Mad){
		double dblQgrdaf=0;
		Table tb=new Table(1,1);
		dblQgrdaf=tb.Round_New(Qgrad*100/(100-Aad-Mad), 2);
		return dblQgrdaf;
	}
	private String getVar_v(){
		return "''";
	}
	private String getQbrad_v(){
		return "''";
	}
	private double  getHar_v(double Had,double Mt,double Mad){
		double dblHar=0;
		Table tb=new Table(1,1);
		dblHar=tb.Round_New(Had*(100-Mt)/(100-Mad), 2);
		return dblHar;
	}
	
	
	//-----------------------
	// 燃料采购部指标完成情况日报
	private String getHuaybgd() {
		
		Visit visit=(Visit)this.getPage().getVisit();
		
		if(!visit.getboolean5() || !visit.getboolean6()){//硫 和 氢  没有配置正确 页面不用显示
			if (this.getMsg().equals("")){
				this.setMsg("元素分析项目硫或氢值配置不正确!");
			}
//			return "";
		}
		String rltitle1 = "坩埚质量+试样质量(g)";
		String rltitle2 = "试样质量(g)";
//		if (isRelbalance()) {
//			rltitle1 = "天平 试样质量(g)";
//			rltitle2 = "自动 试样质量(g)";
//		}
		
		String sp = "<br><br><br><br>";
		String ArrHeader[][] = new String[33][10];
		ArrHeader[0] = new String[] { "矿 别","矿 别", "", "发站(港)", "",  "空干基氢","空干基氢","", "煤量(t)", "" };
		ArrHeader[1] = new String[] { "品 种", "品 种","", "编 号", "",  "化验日期", "化验日期", "","采样人", ""};
		ArrHeader[2] = new String[] { "车次/船次", "车次/船次","", "温度湿度", "", "采样日期","采样日期", "","制样人", "",  };
		ArrHeader[3] = new String[] { "全"+sp+"水"+sp+"分"+sp+"Mt","", "", "第一次", "第二次",  "水"+sp+"分"+sp+"Mad","","", "第一次", "第二次"};
		ArrHeader[4] = new String[] { "全"+sp+"水"+sp+"分"+sp+"Mt","称量瓶(盘)号", "称量瓶(盘)号", "", "", "水"+sp+"分"+sp+"Mad", "称量瓶号","称量瓶号", "", ""};
		ArrHeader[5] = new String[] { "全"+sp+"水"+sp+"分"+sp+"Mt","称量瓶(盘)质量(g)", "称量瓶(盘)质量(g)", "", "", "水"+sp+"分"+sp+"Mad", "称量瓶质量(g)","称量瓶质量(g)", "", ""};
		ArrHeader[6] = new String[] { "全"+sp+"水"+sp+"分"+sp+"Mt","称量瓶(盘)质量+试样质量(g)", "称量瓶(盘)质量+试样质量(g)", "", "", "水"+sp+"分"+sp+"Mad", "称量瓶质量+试样质量(g)","称量瓶质量+试样质量(g)", "", ""};
		ArrHeader[7] = new String[] { "全"+sp+"水"+sp+"分"+sp+"Mt","试样质量m(g)", "试样质量m(g)", "", "", "水"+sp+"分"+sp+"Mad", "试样质量m(g)", "试样质量m(g)", "", ""};
		ArrHeader[8] = new String[] { "全"+sp+"水"+sp+"分"+sp+"Mt","烘干后总质量(g)", "烘干后总质量(g)", "", "","水"+sp+"分"+sp+"Mad", "烘干后总质量(g)","烘干后总质量(g)", "", "" };
		ArrHeader[9] = new String[] { "全"+sp+"水"+sp+"分"+sp+"Mt","检查性试验后总质量(g)", "检查性试验后总质量(g)", "", "","水"+sp+"分"+sp+"Mad", "检查性试验后总质量(g)","检查性试验后总质量(g)", "", "" };
		ArrHeader[10] = new String[] { "全"+sp+"水"+sp+"分"+sp+"Mt","干燥后煤样减少的质量m1(g)", "干燥后煤样减少的质量m1(g)", "", "", "水"+sp+"分"+sp+"Mad", "干燥后煤样减少的质量m1(g)","干燥后煤样减少的质量m1(g)", "", "" };
		ArrHeader[11] = new String[] { "全"+sp+"水"+sp+"分"+sp+"Mt","全水分Mt=m1/m*100(%)", "全水分Mt=m1/m*100(%)", "", "", "水"+sp+"分"+sp+"Mad", "水分Mad=m1/m*100(%)","水分Mad=m1/m*100(%)", "", "" };
		ArrHeader[12] = new String[] { "全"+sp+"水"+sp+"分"+sp+"Mt","平均值Mt(%)", "平均值Mt(%)", "", "","水"+sp+"分"+sp+"Mad", "平均值Mad(%)","平均值Mad(%)", "", "" };
		ArrHeader[13] = new String[] { "全"+sp+"水"+sp+"分"+sp+"Mt","审核:", "审核:", "化验:", "化验:", "水"+sp+"分"+sp+"Madd", "审核:","审核:", "化验:", "化验:" };
		ArrHeader[14] = new String[] { "挥"+sp+"发"+sp+"分"+sp+"Vad","坩埚号", "坩埚号", "", "", "灰"+sp+"分"+sp+"Aad", "灰皿号", "灰皿号", "", ""};
		ArrHeader[15] = new String[] { "挥"+sp+"发"+sp+"分"+sp+"Vad","坩埚质量(g)", "坩埚质量(g)", "", "", "灰"+sp+"分"+sp+"Aad", "灰皿质量(g)","灰皿质量(g)", "", "" };
		ArrHeader[16] = new String[] { "挥"+sp+"发"+sp+"分"+sp+"Vad","坩埚质量+试样质量(g)", "坩埚质量+试样质量(g)", "", "", "灰"+sp+"分"+sp+"Aad", "灰皿质量+试样质量(g)","灰皿质量+试样质量(g)", "", ""};
		ArrHeader[17] = new String[] { "挥"+sp+"发"+sp+"分"+sp+"Vad","试样质量m(g)", "试样质量m(g)", "", "", 	"灰"+sp+"分"+sp+"Aad", "试样质量m(g)","试样质量m(g)", "", ""};
		ArrHeader[18] = new String[] { "挥"+sp+"发"+sp+"分"+sp+"Vad","加热后总质量(g)", "加热后总质量(g)", "", "", "灰"+sp+"分"+sp+"Aad", "加热后总质量(g)","加热后总质量(g)", "", ""};
		ArrHeader[19] = new String[] { "挥"+sp+"发"+sp+"分"+sp+"Vad","煤样加热后减少的质量m1(g)", "煤样加热后减少的质量m1(g)", "", "", "灰"+sp+"分"+sp+"Aad", "检查性试验后总质量(g)","检查性试验后总质量(g)", "", ""};
		ArrHeader[20] = new String[] { "挥"+sp+"发"+sp+"分"+sp+"Vad","挥发分Vad=m1/m*100-Mad(%)", "挥发分Vad=m1/m*100-Mad(%)", "","", "灰"+sp+"分"+sp+"Aad", "残留物的质量m1(g)","残留物的质量m1(g)", "", "" };
		ArrHeader[21] = new String[] { "挥"+sp+"发"+sp+"分"+sp+"Vad","平均值Vad(%)", "平均值Vad(%)", "", "", "灰"+sp+"分"+sp+"Aad", "灰分Aad(%)","灰分Aad(%)", "", "" };
		ArrHeader[22] = new String[] { "挥"+sp+"发"+sp+"分"+sp+"Vad","平均值Vdaf(%)", "平均值Vdaf(%)", "", "", 	"灰"+sp+"分"+sp+"Aad", "平均值(%)","平均值(%)", "", "" };
		ArrHeader[23] = new String[] { "挥"+sp+"发"+sp+"分"+sp+"Vad","审核:", "审核:", "化验:", "化验:",  "灰"+sp+"分"+sp+"Aad","审核:", "审核:", "化验:", "化验:" };
		ArrHeader[24] = new String[] { "全"+sp+"硫", "器皿号", "器皿号", "", "", "发"+sp+"热"+sp+"量","坩埚质量(g)","坩埚质量(g)", "", ""};
		ArrHeader[25] = new String[] { "全"+sp+"硫", "器皿质量(g)","器皿质量(g)", "", "", "发"+sp+"热"+sp+"量",rltitle1,rltitle1, "", ""};
		ArrHeader[26] = new String[] { "全"+sp+"硫", "器皿质量+试样质量(g)","器皿质量+试样质量(g)", "", "","发"+sp+"热"+sp+"量", rltitle2,rltitle2, "", ""};
		ArrHeader[27] = new String[] { "全"+sp+"硫", "试样质量(g)","试样质量(g)", "", "", "发"+sp+"热"+sp+"量","Qb,ad(J/g)","Qb,ad(J/g)", "", "" };
		ArrHeader[28] = new String[] { "全"+sp+"硫", "全硫St,ad(%)","全硫St,ad(%)", "", "", "发"+sp+"热"+sp+"量", "平均值Qb,ad(J/g)", "平均值Qb,ad(J/g)", "", "" };
		ArrHeader[29] = new String[] { "全"+sp+"硫", "平均值St,ad(%)","平均值St,ad(%)", "", "",	"发"+sp+"热"+sp+"量", "Qgr,ad(J/g)","Qgr,ad(J/g)", "", "" };
		ArrHeader[30] = new String[] { "全"+sp+"硫", "平均值St,d(%)","平均值St,d(%)", "", "", 	"发"+sp+"热"+sp+"量", "Qnet,ar(MJ/kg)","Qnet,ar(MJ/kg)", "", "" };
		ArrHeader[31] = new String[] { "全"+sp+"硫", "审核:","审核:", "化验:", "化验:","发"+sp+"热"+sp+"量", "审核:", "审核:", "化验:", "化验:" };
		ArrHeader[32] = new String[] { "全<br>硫<br>原<br>始<br>记<br>录<br>打<br>印<br>单", "", "", "", "", 
				"发<br>热<br>量<br>原<br>始<br>记<br>录<br>打<br>印<br>单", "", "", "", ""};
		int ArrWidth[] = new int[] { 30,40, 130, 70, 70, 30, 30, 130, 70, 70 };
		/*
		 * 给数组赋值
		 */
		String strRcRl="";
		if (visit.getString12().equals("RC")) {//入厂化验  要打印的数据
			setArrayValue(ArrHeader, true);
			strRcRl="入厂煤";
		} else {//入炉化验时 要打印的数据
			
		}

		// 赋值结束
		Report rt = new Report();
		rt.setTitle(visit.getDiancqc()+"<br>"+strRcRl+"化验原始记录", ArrWidth);
		
		rt.setBody(new Table(33, 10));
		// rt.setDefaultTitleLeft("填制单位：", 1);
		// rt.setDefaultTitleLeft("日期", 1);
		// rt.setDefaultTitleRight("编号：", 1);
		//rt.setBody(new Table(ArrHeader, 0, false, Table.ALIGN_LEFT));
		rt.body.setWidth(ArrWidth);
		rt.body.setRowHeight(22);
		rt.body.setRowHeight(33, 200);
		
		
		//赋值
		
		for (int i = 0; i < 33; i++) {
			for (int j = 0; j < 10; j++) {
				if (ArrHeader[i][j] == null || ArrHeader[i][j].length() == 0) {
					ArrHeader[i][j] = "";
				}
				rt.body.setCellValue(i+1 , j+1 , ArrHeader[i][j]);
			}
		}
		
		// 合并单元格
		for (int i=1;i<4;i++) {
			rt.body.mergeCell(i, 1, i, 2);
			rt.body.mergeCell(i, 6, i, 7);
		}
		
		
		rt.body.mergeCell(4, 1, 14, 1);
		rt.body.mergeCell(15, 1, 24, 1);
		rt.body.mergeCell(25, 1, 32, 1);
		
		rt.body.mergeCell(4, 6, 14, 6);
		rt.body.mergeCell(15, 6, 24, 6);
		rt.body.mergeCell(25, 6, 32, 6);
		
		for (int i=4;i<=32;i++) {
			rt.body.mergeCell(i, 2, i, 3);
			rt.body.mergeCell(i, 7, i, 8);
		}
		
		rt.body.mergeCell(13, 4, 13, 5);
		rt.body.mergeCell(14, 4, 14, 5);
		rt.body.mergeCell(13, 9, 13, 10);
		rt.body.mergeCell(14, 9, 14, 10);
		
		rt.body.mergeCell(22, 4, 22, 5);
		rt.body.mergeCell(23, 4, 23, 5);
		rt.body.mergeCell(24, 4, 24, 5);	
		rt.body.mergeCell(23, 9, 23, 10);
		rt.body.mergeCell(24, 9, 24, 10);
		
		rt.body.mergeCell(30, 4, 30, 5);
		rt.body.mergeCell(31, 4, 31, 5);
		rt.body.mergeCell(32, 4, 32, 5);
		
		rt.body.mergeCell(29, 9, 29, 10);
		rt.body.mergeCell(30, 9, 30, 10);
		rt.body.mergeCell(31, 9, 31, 10);
		rt.body.mergeCell(32, 9, 32, 10);
		
		rt.body.mergeCell(33, 3, 33, 5);
		rt.body.mergeCell(33, 8, 33, 10);
		
		
		rt.body.setCellBorderLeft(33, 3, 0);
		rt.body.setCellBorderRight(33, 2, 0);
		rt.body.setCellBorderLeft(33, 8, 0);
		rt.body.setCellBorderRight(33, 7, 0);
		
		rt.body.setCells(1, 1, 4, 10, Table.PER_ALIGN, Table.ALIGN_CENTER);
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(6, Table.ALIGN_CENTER);
		
//		rt.body.setCells(6, 3, 12, 6, Table.PER_ALIGN, Table.ALIGN_RIGHT);
		rt.body.setCells(5, 4, 12, 5, Table.PER_ALIGN, Table.ALIGN_RIGHT);
		rt.body.setCells(13, 4, 13, 5, Table.PER_ALIGN, Table.ALIGN_CENTER);
		
//		rt.body.setCells(6, 9, 12, 11, Table.PER_ALIGN, Table.ALIGN_RIGHT);
		rt.body.setCells(5, 9, 12, 10, Table.PER_ALIGN, Table.ALIGN_RIGHT);
		rt.body.setCells(13, 9, 13, 10, Table.PER_ALIGN, Table.ALIGN_CENTER);
		
//		rt.body.setCells(16, 3, 21, 6, Table.PER_ALIGN, Table.ALIGN_RIGHT);
		rt.body.setCells(15, 4, 21, 5, Table.PER_ALIGN, Table.ALIGN_RIGHT);
		rt.body.setCells(22, 4, 22, 5, Table.PER_ALIGN, Table.ALIGN_CENTER);
		rt.body.setCells(23, 4, 23, 5, Table.PER_ALIGN, Table.ALIGN_CENTER);
		
//		rt.body.setCells(16, 9, 22, 11, Table.PER_ALIGN, Table.ALIGN_RIGHT);
		rt.body.setCells(15, 9, 22, 10, Table.PER_ALIGN, Table.ALIGN_RIGHT);
		rt.body.setCells(23, 9, 23, 10, Table.PER_ALIGN, Table.ALIGN_CENTER);
		
		
//		rt.body.setCells(28, 3, 29, 6, Table.PER_ALIGN, Table.ALIGN_RIGHT);
		rt.body.setCells(27, 4, 29, 5, Table.PER_ALIGN, Table.ALIGN_RIGHT);
		rt.body.setCells(28, 4, 29, 5, Table.PER_ALIGN, Table.ALIGN_CENTER);
		
		rt.body.setCells(25, 4, 30, 5, Table.PER_ALIGN, Table.ALIGN_RIGHT);
//		rt.body.setCells(26, 9, 28, 11, Table.PER_ALIGN, Table.ALIGN_RIGHT);
		rt.body.setCells(25, 9, 28, 10, Table.PER_ALIGN, Table.ALIGN_RIGHT);
	//	rt.body.setCells(26, 9, 28, 11, Table.PER_ALIGN, Table.ALIGN_CENTER);
		
		//-----------
		rt.body.setCells(30, 4, 30, 5, Table.PER_ALIGN, Table.ALIGN_CENTER);
		rt.body.setCells(30, 9, 30, 10, Table.PER_ALIGN, Table.ALIGN_CENTER);
		rt.body.setCells(29, 4, 29, 5, Table.PER_ALIGN, Table.ALIGN_RIGHT);
		rt.body.setCells(29, 9, 29, 10, Table.PER_ALIGN, Table.ALIGN_CENTER);
		
		rt.body.setCells(31, 4, 31, 5, Table.PER_ALIGN, Table.ALIGN_CENTER);
		rt.body.setCells(31, 9, 31, 10, Table.PER_ALIGN, Table.ALIGN_CENTER);
		return rt.getHtml();
	
	}

	// 按钮的监听事件
	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}
	//确认
	private boolean _SaveButton=false;
	
	public void SaveButton(IRequestCycle cycle){
		_SaveButton=true;
	}
	//撤销
	private boolean _CancelButton=false;
	
	public void CancelButton(IRequestCycle cycle){
		_CancelButton=true;
	}
	// 表单按钮提交监听
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			
			getSelectData();
		}
		if(_SaveButton){
			_SaveButton=false;
			this.BT_Commit();
			this.setBianmValue(null);
			setBianmModels();
		}
		if(_CancelButton){
			_CancelButton=false;
			this.BT_Cancel();
			setBianmModels();
		}
	}
	
	//确认
	private void BT_Commit(){
		JDBCcon con=new JDBCcon();
		Visit visit=(Visit)this.getPage().getVisit();
		String diancxxb_id=visit.getDiancxxb_id()+"";
		StringBuffer bf=new StringBuffer(" begin \n");
		
		bf.append("   update  huaygyfxb  set shenhzt=1  where shenhzt=2 and bianm='").append(this.getBianmValue().getValue()).append("' \n").append(" and diancxxb_id=").append(diancxxb_id).append(";\n");
		bf.append("   update huaylfb set shenhzt=1  where shenhzt=2 and bianm='").append(this.getBianmValue().getValue()).append("' \n").append(" and diancxxb_id=").append(diancxxb_id).append(";\n");
		bf.append("   update huayrlb set shenhzt=1  where shenhzt=2 and bianm='").append(this.getBianmValue().getValue()).append("' \n").append(" and diancxxb_id=").append(diancxxb_id).append(";\n");
//		bf.append("   update zhillsb set shenhzt=1  where bianm='").append(this.getBianmValue().getValue()).append("' \n").append(" and diancxxb_id=").append(diancxxb_id).append(";\n");
		bf.append("   update zhillsb set shenhzt=3 \n")
		.append(" ,huaysj=").append(DateUtil.FormatOracleDate(getRiq())).append("\n")
		.append(" ,QNET_AR=").append(this.getQnetar_v()).append(" \n")
		.append(" ,AAR=").append(this.getAar_v()).append(" \n")
		.append(" ,AD=").append(this.getAd_v()).append(" \n")
		.append(" ,VDAF=").append(this.getVdaf_v()).append(" \n")
		.append(" ,MT=").append(this.getMt_v()).append("\n")
		.append(" ,STAD=").append(this.getStad_v()).append(" \n")
		.append(" ,AAD=").append(this.getAad_v()).append(" \n")
		.append(" ,MAD=").append(this.getMad_v()).append(" \n")
		.append(" ,QBAD=").append(this.getQbad_v()).append(" \n")
		.append(" ,HAD=").append(this.getHad_v()).append(" \n")
		.append(" ,VAD=").append(this.getVad_v()).append(" \n")
		.append(" ,FCAD=").append(this.getFcad_v()).append(" \n")
		.append(" ,STD=").append(this.getStd_v()).append(" \n")
		.append(" ,QGRAD=").append(this.getQgrad_v()).append(" \n")
		.append(" ,HDAF=").append(this.getHdaf_v()).append(" \n")
		.append(" ,QGRD=").append(this.getQgrd_v()).append(" \n")
		.append(" ,SDAF=").append(this.getSdaf_v(this.getStd_v(),this.getStd_v())).append(" \n")
		.append(" ,QGRAD_DAF=").append(this.getQgrad_daf_v(this.getQgrad_v(),this.getAad_v(),this.getMad_v())).append(" \n")
		.append(" ,HAR=").append(this.getHar_v(this.getHad_v(),this.getMt_v(),this.getMad_v())).append(" \n")
		.append(" ,VAR=").append(this.getVar_v()).append(" \n")
		.append(" ,QBRAD=").append(this.getQbrad_v()).append(" \n")
		.append(" where id in (")
		.append(" select z.id from zhillsb z, zhuanmb m, zhuanmlb l \n")
		.append(" where z.id = m.zhillsb_id and m.zhuanmlb_id = l.id \n")
		.append(" and l.mingc = '"+huaybm+"' and m.bianm='").append(this.getBianmValue().getValue()).append("'")
		.append(" union select id from zhillsb where beiz='").append(this.getBianmValue().getValue()).append("'")
		.append(" ); \n");

		bf.append(" end; ");
		int a=con.getUpdate(bf.toString());
		if(a==-1){
			this.setMsg("数据操作失败!");
		}else{
			this.setMsg("数据操作成功!");
		}
		
	}
	//取消
	private void BT_Cancel(){
		JDBCcon con=new JDBCcon();
		Visit visit=(Visit)this.getPage().getVisit();
		ResultSet rs=null;
		String diancxxb_id=visit.getDiancxxb_id()+"";
		try{
			String sql="select shenhzt  from zhillsb where id in ("
				+ " select z.id from zhillsb z, zhuanmb m, zhuanmlb l \n"
				+ " where z.id = m.zhillsb_id and m.zhuanmlb_id = l.id \n"
				+" and l.mingc = '"+huaybm+"' and m.bianm='"+this.getBianmValue().getValue()+"')";
			rs=con.getResultSet(sql);
			if (rs.next()){
				if (rs.getInt("shenhzt")==7){
					this.setMsg("数据已经二级审核，不允许撤销!");
					return;
				}
			}
			rs.close();
		}catch(Exception e){
			e.printStackTrace();
			return;
		}
		StringBuffer bf=new StringBuffer(" begin \n");
		
		bf.append("   update  huaygyfxb  set shenhzt=2  where shenhzt=1 and bianm='").append(this.getBianmValue().getValue()).append("' \n").append(" and diancxxb_id=").append(diancxxb_id).append(";\n");
		bf.append("   update huaylfb set shenhzt=2  where shenhzt=1 and bianm='").append(this.getBianmValue().getValue()).append("' \n").append(" and diancxxb_id=").append(diancxxb_id).append(";\n");
		bf.append("   update huayrlb set shenhzt=2  where shenhzt=1 and bianm='").append(this.getBianmValue().getValue()).append("' \n").append(" and diancxxb_id=").append(diancxxb_id).append(";\n");
//		bf.append("   update zhillsb set shenhzt=1  where bianm='").append(this.getBianmValue().getValue()).append("' \n").append(" and diancxxb_id=").append(diancxxb_id).append(";\n");
		bf.append("   update zhillsb set shenhzt=0 \n")
		.append(" ,QNET_AR=").append("''").append(" \n")
		.append(" ,AAR=").append("''").append(" \n")
		.append(" ,AD=").append("''").append(" \n")
		.append(" ,VDAF=").append("''").append(" \n")
		.append(" ,MT=").append("''").append("\n")
		.append(" ,STAD=").append("''").append(" \n")
		.append(" ,AAD=").append("''").append(" \n")
		.append(" ,MAD=").append("''").append(" \n")
		.append(" ,QBAD=").append("''").append(" \n")
		.append(" ,HAD=").append("''").append(" \n")
		.append(" ,VAD=").append("''").append(" \n")
		.append(" ,FCAD=").append("''").append(" \n")
		.append(" ,STD=").append("''").append(" \n")
		.append(" ,QGRAD=").append("''").append(" \n")
		.append(" ,HDAF=").append("''").append(" \n")
		.append(" ,QGRD=").append("''").append(" \n")
		.append(" ,SDAF=").append("''").append(" \n")
		.append(" ,QGRAD_DAF=").append("''").append(" \n")
		.append(" ,HAR=").append("''").append(" \n")
		.append(" ,VAR=").append("''").append(" \n")
		.append(" ,QBRAD=").append("''").append(" \n")
		.append(" where id in (")
		.append(" select z.id from zhillsb z, zhuanmb m, zhuanmlb l \n")
		.append(" where z.id = m.zhillsb_id and m.zhuanmlb_id = l.id \n")
		.append(" and l.mingc = '"+huaybm+"' and m.bianm='").append(this.getBianmValue().getValue()).append("'")
		.append(" union select id from zhillsb where beiz='").append(this.getBianmValue().getValue()).append("'")
		.append(" );\n");
		
		bf.append(" end; ");
		int a=con.getUpdate(bf.toString());
		if(a==-1){
			this.setMsg("数据操作失败!");
		}else{
			this.setMsg("数据操作成功!");
		}
		
	}
	//取出 化验 类别 中 入厂化验硫   入厂化验氢 的 zhi  
	private  String getYuansSign(JDBCcon con, String Yuans, String diancxxb_id){
		String sign ="";
		Visit visit=(Visit)this.getPage().getVisit();
		String s="";
		if(visit.getString12().equals("RC")){
			s="入厂";
		}else{
			s="入炉";
		}
		String sql = "select zhi from xitxxb where mingc = '"+s+"化验"+Yuans+"'\n"
		+ " and zhuangt = 1 and diancxxb_id = " + diancxxb_id+" and leib='"+s+"'";
		
		ResultSetList rsl = con.getResultSetList(sql);
		if(rsl.next()){
			sign = rsl.getString("zhi");
		}
		rsl.close();
		return sign;
	}
	//根据 xitxxb 中 氢 和 硫 的zhi，取出 yuansfxb中对应的zhi
	private String getyuansfxb(JDBCcon con, String Yuans, String diancxxb_id){
		
		String zhi="";
		String sign=this.getYuansSign(con, Yuans, diancxxb_id);
		String sql=" select fx.zhi from yuansfxb fx,yuansxmb fs where fx.yuansxmb_id=fs.id \n"
					+" and fx.zhi!=0 and fs.zhuangt=1 and fx.zhuangt=1 and  fs.mingc='"+sign+"'  ";
		
		ResultSetList rsl = con.getResultSetList(sql);
		if(rsl.next()){
			zhi=rsl.getString("zhi");
		}
		rsl.close();
		return zhi;
	}

	private String getShenhzt(JDBCcon con){//得到 编码 对应记录的 审核状态
		String zhuangt="";
		String sql=" select h.shenhzt from huaygyfxb h where  h.bianm='"+this.getBianmValue().getValue()+"'";
		
		ResultSetList rsl=con.getResultSetList(sql);
		
		if(rsl.next()){
			zhuangt=rsl.getString("shenhzt");
		}
		rsl.close();
		return zhuangt;
	}
	
	//硫 来源
	private void setLiuly(String liuly){
		Visit visit=(Visit)this.getPage().getVisit();
		visit.setString7(liuly);
	}
	private String getLiuly(){
		Visit visit=(Visit)this.getPage().getVisit();
		return visit.getString7();
	}
	//氢 来源
	private void setQingly(String qingly){
		Visit visit=(Visit)this.getPage().getVisit();
		visit.setString8(qingly);
	}
	private String getQingly(){
		Visit visit=(Visit)this.getPage().getVisit();
		return visit.getString8();
	}
	
	//  元素分析时，硫的配置 是否正确
	private void setLiupz(boolean t){
		Visit visit=(Visit)this.getPage().getVisit();
		visit.setboolean5(t);
	}
	
	private boolean getLiupz(){
		Visit visit=(Visit)this.getPage().getVisit();
		return visit.getboolean5();
	}
	
	//元素分析时  氢的配置是否正确
	
	private void setQingpz(boolean t){
		Visit visit=(Visit)this.getPage().getVisit();
		visit.setboolean6(t);
	}
	
	private boolean getQingpz(){
		Visit visit=(Visit)this.getPage().getVisit();
		return visit.getboolean6();
	}
	
	//元素分析时，配置正确，硫的值
	private void setLiujz(String s){
		Visit visit=(Visit)this.getPage().getVisit();
		visit.setString13(s);
	}
	
	private String getLiujz(){
		Visit visit=(Visit)this.getPage().getVisit();
		if(visit.getString13()==null){
			return "0";
		}
		return visit.getString13();
	}
	
	//元素分析时，配置正确，氢的值
	private void setQingjz(String s){
		Visit visit=(Visit)this.getPage().getVisit();
		visit.setString14(s);
	}
	
	private String getQingjz(){
		Visit visit=(Visit)this.getPage().getVisit();
		if(visit.getString14()==null){
			return "0";
		}
		return visit.getString14();
	}
	
	
	// 刷新衡单列表
	public void getSelectData() {
		Visit visit = (Visit) this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");
		JDBCcon con=new JDBCcon();
		
		String liuly=this.getLiuly(con);//获得硫来源
		String qingly=this.getQingly(con);//获得氢来源
		String diancxxb_id=visit.getDiancxxb_id()+"";
		
		//当系统没有设置 或者 为空时  设置默认值
		if(liuly.equals("")){ 
			liuly=caijsj;
		}
		if(qingly.equals("")){
			qingly=zidjs;
		}
		
		this.setLiuly(liuly);
		this.setQingly(qingly);
		
		//visit.setboolean5(true);
		// 元素分析 硫系统有值  配置正确 能正确显示
		this.setLiupz(true);
		//visit.setboolean6(true);
		// 元素分析 氰系统有值  配置正确 能正确显示
		this.setQingpz(true);
		
		//假如 来源是 元素分析 时  要 通过 元素分析表  判断 是否有错
		if(liuly.equals(yuansfx)){
			String zhi=this.getyuansfxb(con, _liu, diancxxb_id);
			
			if(zhi!=null && !zhi.equals("")){
			
				//visit.setString13(zhi);
				//存放 硫的平均值
				this.setLiujz(zhi);
				//visit.setboolean5(true);
				// 硫系统有值  配置正确 能正确显示
				this.setLiupz(true);
			}else{
				//visit.setboolean5(false);
				this.setLiupz(false);
			}
		}
		if(qingly.equals(yuansfx)){
			String zhi=this.getyuansfxb(con, _qing, diancxxb_id);
			
			if(zhi!=null && !zhi.equals("")){
			
				//visit.setString14(zhi);
				//存放 氢的平均值
				this.setQingjz(zhi);
				//visit.setboolean6(true);
				// 氰系统有值  配置正确 能正确显示
				this.setQingpz(true);
			}else{
				//visit.setboolean6(false);
				this.setQingpz(false);
			}
		}
		
		
//		if (visit.isFencb()) {
//			tb1.addText(new ToolbarText("厂别:"));
//			ComboBox changbcb = new ComboBox();
//			changbcb.setTransform("ChangbSelect");
//			changbcb.setWidth(130);
//			changbcb
//					.setListeners("select:function(own,rec,index){Ext.getDom('ChangbSelect').selectedIndex=index}");
//			tb1.addField(changbcb);
//			tb1.addText(new ToolbarText("-"));
//		}
		
		tb1.addText(new ToolbarText("日期:"));
//		DateField df = new DateField();
//		df.setValue(getRiq());
//		df.Binding("RIQ", "Form0");// 与html页中的id绑定,并自动刷新
//		df.setId("huayrq");
//		tb1.addField(df);
			
//		tb1.addText(new ToolbarText("至"));

		DateField df1 = new DateField();
		df1.setValue(getRiq());
		df1.Binding("RiqEnd", "Form0");// 与html页中的id绑定,并自动刷新
		df1.setId("huayrqEnd");
		tb1.addField(df1);
		
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("审核状态:"));
		ComboBox zhuangt = new ComboBox();
		zhuangt.setTransform("ZhuangtSelect");
		zhuangt.setWidth(130);
		zhuangt.setListeners("select:function(own,rec,index){Ext.getDom('ZhuangtSelect').selectedIndex=index;Form0.submit();}");
		tb1.addField(zhuangt);
		
		tb1.addText(new ToolbarText("-"));
		
		if(visit.getString12().indexOf("RC")!=-1){  //入厂化验  编号
			tb1.addText(new ToolbarText("化验编码:"));
			ComboBox shij = new ComboBox();
			shij.setTransform("BianmSelect");
			shij.setWidth(130);
			shij
					.setListeners("select:function(own,rec,index){Ext.getDom('BianmSelect').selectedIndex=index;Form0.submit();}");
			tb1.addField(shij);
		}else{//入炉化验 编号
			
		}
		
		tb1.addText(new ToolbarText("-"));

		ToolbarButton rbtn = new ToolbarButton(null, "查询",
				"function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
		rbtn.setId("search_id");
		
		ToolbarButton rbtn1 = new ToolbarButton(null, "确认",
		"function(){document.getElementById('SaveButton').click();}");
		rbtn1.setIcon(SysConstant.Btn_Icon_SelSubmit);
		rbtn1.setId("commit_id");
		
		ToolbarButton rbtn2 = new ToolbarButton(null, "撤销",
		"function(){document.getElementById('CancelButton').click();}");
		rbtn2.setIcon(SysConstant.Btn_Icon_Cancel);
		rbtn2.setId("cancel_id");
		
		if(!this.getLiupz() || !this.getQingpz()){// 元素分析  硫 和 氢 有一个 配置不正确  按钮禁用 ，提示信息
		//	rbtn.setDisabled(true);
			rbtn1.setDisabled(true);
			rbtn2.setDisabled(true);
		}
		
		String shenhzt=this.getShenhzt(con);
		if(shenhzt.equals("1")){//确认 按钮 失效
			rbtn1.setDisabled(true);
		}
		if(shenhzt.equals("2")){//撤销 按钮失效
			rbtn2.setDisabled(true);
		}
	
		con.Close();
		tb1.setWidth("bodyWidth");
		tb1.addItem(rbtn);
		tb1.addItem(rbtn1);
		tb1.addItem(rbtn2);
		tb1.addFill();
		// tb1.addText(new ToolbarText("<marquee width=300
		// scrollamount=2></marquee>"));
		setToolbar(tb1);
		
		//在得到数据前进行自动审核
		if (getZhuangtValue().getId()==SELECT_WEISH){		
			String strMsg = AutoFix().toString();
			if (!"".equals(strMsg)) {
				this.setMsg(AutoFix().toString());//如果是
			}
		}
	}

	// 工具栏使用的方法
	public Toolbar getToolbar() {
		return ((Visit) this.getPage().getVisit()).getToolbar();
	}

	public void setToolbar(Toolbar tb1) {
		((Visit) this.getPage().getVisit()).setToolbar(tb1);
	}

	public String getToolbarScript() {
		// if(getTbmsg()!=null) {
		// getToolbar().deleteItem();
		// getToolbar().addText(new ToolbarText("<marquee width=300
		// scrollamount=2>"+getTbmsg()+"</marquee>"));
		// }
//		((DateField) getToolbar().getItem("huayrq")).setValue(getRiq());
		((DateField) getToolbar().getItem("huayrqEnd")).setValue(getRiqEnd());
		
		return getToolbar().getRenderScript();
	}
	
	
	private int getTtype(String leix){
		int tType=0;
		if(Qbad.equals(leix)){
			tType = Judge.T_TYPE_Qbad;
		}else if(Stad.equals(leix)){
			tType = Judge.T_TYPE_Stad;
		}else if(Mt.equals(leix)){
			tType = Judge.T_TYPE_Mt;
		}else if(Mad.equals(leix)){
			tType = Judge.T_TYPE_Mad;
		}else if(Aad.equals(leix)){
			tType = Judge.T_TYPE_Aad;
		}else if(Vad.equals(leix)){
			tType = Judge.T_TYPE_Vad;
		}
		return tType;
	}
	
	private String getItemName(String leix){
		if(Qbad.equals(leix)){
			return "弹筒热";
		}else if(Stad.equals(leix)){
			return "硫分";
		}else if(Mt.equals(leix)){
			return "全水分";
		}else if(Mad.equals(leix)){
			return "空干基水分";
		}else if(Aad.equals(leix)){
			return "灰分";
		}else if(Vad.equals(leix)){
			return "挥发份";
		}
		return "";
	}
	
	private StringBuffer  AutoFix(){
		StringBuffer sb=new StringBuffer();
		if (getBianmValue().getId()!=-1){
			sb.append(AutoFixItem (Qbad));
			sb.append(AutoFixItem (Stad));
			sb.append(AutoFixItem (Mt));
			sb.append( AutoFixItem (Mad));
			sb.append(AutoFixItem (Aad));
			sb.append(AutoFixItem (Vad));
		}
		return sb;
	}
	
	private String AutoFixItem(String leix){
		JDBCcon con = new JDBCcon();
		Visit visit=(Visit)this.getPage().getVisit();
		
		String sql="";
		String strTableName="";
		String strResultName="";
		
		int itemDataRows=0;
		int itemType=getTtype(leix);
		con.setAutoCommit(false);
		
		if (itemType==Judge.T_TYPE_Qbad){//热量
			sql="select h.id, h.farl from huayrlb h where h.bianm='"+getBianmValue().getValue()
				+"' and h.diancxxb_id="+visit.getDiancxxb_id()
				+" and h.riq>=(to_date('" + this.getRiqEnd() + "','yyyy-mm-dd')-2) and h.riq<=(to_date('" + this.getRiqEnd() + "','yyyy-mm-dd')+2) \n"
				+" and shenhzt>-1 and shenhzt<>1";			
			strResultName="farl";
			strTableName="huayrlb";
		}else if(itemType==Judge.T_TYPE_Stad){//硫分
			sql="select h.id,h.liuf from huaylfb h where h.bianm='"+getBianmValue().getValue()
				+"' and h.diancxxb_id="+visit.getDiancxxb_id()
				+" and h.riq>=(to_date('" + this.getRiqEnd() + "','yyyy-mm-dd')-2) and h.riq<=(to_date('" + this.getRiqEnd() + "','yyyy-mm-dd')+2) \n"
				+" and shenhzt>-1 and shenhzt<>1";
			strResultName="liuf";
			strTableName="huaylfb";
		}else {//工业分析
			sql="select h.id,huayz ,i.mingc ,i.bianm   from huaygyfxb h, item i, itemsort s"
				+" where h.fenxxmb_id = i.id"  
				+" and i.itemsortid = s.id"
				+"	and h.diancxxb_id="+visit.getDiancxxb_id()
				+" and i.bianm='"+leix+"'" 
				+" and h.bianm='"+getBianmValue().getValue()+"' "
				+" and h.riq>=(to_date('" + this.getRiqEnd() + "','yyyy-mm-dd')-2) and h.riq<=(to_date('" + this.getRiqEnd() + "','yyyy-mm-dd')+2) \n"
				+" and shenhzt>-1 and shenhzt<>1";;
			strResultName="huayz";
			strTableName="huaygyfxb";
		}
		
		ResultSetList rs =con.getResultSetList(sql);
		double[] array = new double[rs.getRows()];
		itemDataRows=array.length;
		if (itemDataRows==0 ){
			return "无"+getItemName(leix)+"数据！";
		}
		while(rs.next()){
			if("1".equals(rs.getString("shenhzt"))){
				setMsg("已结束数据不能进行审核！");
				return getItemName(leix)+"已结束数据不能进行审核";
			}
			array[rs.getRow()] = rs.getDouble(strResultName);
		}
		
		Judge ju = new Judge();
		
		double[] dbarrjudge = ju.getJudgeData(itemType, array);
		if  (itemDataRows>=2 && dbarrjudge==null){
			return getItemName(leix) + "超差";
		}
		
//		if (dbarrjudge==null) {
//			return getItemName(leix) + "只有一个值";
//		}
		
		for(int i = 0; i < dbarrjudge.length ; i++){
			rs.beforefirst();
			while(rs.next()){
				if(dbarrjudge[i] == rs.getDouble(strResultName)){
					UpdateZt(con,strTableName,rs.getString("id"),2);
					rs.Remove(rs.getRow());
					break;
				}
			}
		}
		rs.beforefirst();
		while(rs.next()){
			UpdateZt(con,strTableName,rs.getString("id"),-1);
		}
		rs.close();
		con.commit();
		con.Close();
		return "";
	}

	private int UpdateZt(JDBCcon con, String tableName, String id, int zt){
		String sql = "update "+tableName+" set shenhzt = "+zt+" where id =" + id;
		return con.getUpdate(sql);
	}
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			
			visit.setString12("RC");//存放跳转过来的页面名词  默认是  入场 化验 
			
			String pageName=this.getPageName().toString();
			if(pageName!=null && pageName.toUpperCase().equals("RL")){
				visit.setString12(pageName);
			}
			
			visit.setActivePageName(getPageName().toString());
//			setRiq(DateUtil.FormatDate(DateUtil.AddDate(new Date(),-7,DateUtil.AddType_intDay)));
			setRiq(DateUtil.FormatDate(new Date()));
			setRiqEnd(DateUtil.FormatDate(new Date()));
			
			setZhuangtValue(null);
			setZhuangtModel(null);
			
			setChangbValue(null);
			setChangbModel(null);
			setBianmValue(null);
			setBianmModel(null);
			visit.setDouble1(0);
			visit.setDouble2(0);
			visit.setDouble3(0);
			visit.setDouble4(0);
			visit.setDouble5(0);
			visit.setDouble6(0);
			visit.setDouble7(0);
			visit.setDouble8(0);
			visit.setDouble9(0);
			visit.setDouble10(0);
			visit.setDouble11(0);
			visit.setDouble12(0);
			visit.setDouble13(0);
			visit.setDouble14(0);
			visit.setDouble15(0);
			visit.setDouble16(0);
			visit.setDouble17(0);
			visit.setDouble18(0);
			visit.setDouble19(0);
			visit.setDouble20(0);
			
			visit.setString13("");
			visit.setString14("");
			
			visit.setString7("");
			visit.setString8("");
			
			visit.setboolean5(false);
			visit.setboolean6(false);
			
		}
		if (riqchange || zhuangtChange) {
			riqchange = false;
			zhuangtChange=false;
			setBianmValue(null);
			setBianmModel(null);
		}
		getSelectData();
	}

	// 日期是否变化
	private boolean riqchange = false;

	// 绑定日期
	private String riq;

	public String getRiq() {
		if (this.riq==null){
			riq = DateUtil.FormatDate(new Date());
		}
		
		return riq;
	}

	public void setRiq(String riq) {
		if (this.riq != null) {
			if (!this.riq.equals(riq))
				riqchange = true;
		}
		this.riq = riq;
	}

	private String riqEnd;
	public String getRiqEnd() {
		if (riqEnd==null) {
			riqEnd = DateUtil.FormatDate(new Date());
		}
		return riqEnd;
	}

	public void setRiqEnd(String riqEnd) {
		if (this.riqEnd != null) {
			if (!this.riqEnd.equals(riqEnd))
				riqchange = true;
		}
		this.riq = riqEnd;
		this.riqEnd = riqEnd;
	}
	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	private boolean zhuangtChange=false;
	public IDropDownBean getZhuangtValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean3() == null) {
			if (getZhuangtModel().getOptionCount() > 0) {
				setZhuangtValue((IDropDownBean) getZhuangtModel().getOption(0));
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean3();
	}

	public void setZhuangtValue(IDropDownBean value) {
		if (value==null){
			zhuangtChange=true;
		}else{
			if (value.equals(((Visit) this.getPage().getVisit()).getDropDownBean3())){
				zhuangtChange=false;
			}else{
				zhuangtChange=true;
			}
		}
		((Visit) this.getPage().getVisit()).setDropDownBean3(value);
	}

	public IPropertySelectionModel getZhuangtModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel3() == null) {
			setZhuangtModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel3();
	}

	public void setZhuangtModel(IPropertySelectionModel model) {
		((Visit) this.getPage().getVisit()).setProSelectionModel3(model);
	}

	private void setZhuangtModels() {
		List dropdownlist =new ArrayList(); ;
		dropdownlist.add(new IDropDownBean(SELECT_WEISH, "未审核"));
		dropdownlist.add(new IDropDownBean(SELECT_YISH, "已审核"));
		setZhuangtModel( new IDropDownModel(dropdownlist));
	}
	
	public IDropDownBean getBianmValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean1() == null) {
			if (getBianmModel().getOptionCount() > 0) {
				setBianmValue((IDropDownBean) getBianmModel().getOption(0));
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean1();
	}

	public void setBianmValue(IDropDownBean value) {
		((Visit) this.getPage().getVisit()).setDropDownBean1(value);
	}

	public IPropertySelectionModel getBianmModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel1() == null) {
			setBianmModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel1();
	}

	public void setBianmModel(IPropertySelectionModel model) {
		((Visit) this.getPage().getVisit()).setProSelectionModel1(model);
	}

	private void setBianmModels() {
		StringBuffer sb = new StringBuffer();
		Visit visit=(Visit)this.getPage().getVisit();
		
		if (getZhuangtValue().getId()==SELECT_WEISH){//未审核的编码
//			sb.append(" select rownum as id,f.bianm  from (")
//			.append(" select distinct h.bianm from huaygyfxb h ")
//			.append(" where h.riq>=").append(DateUtil.FormatOracleDate(getRiq())).append("\n")
//			.append(" and h.riq<=").append(DateUtil.FormatOracleDate(getRiqEnd())).append("\n")
//			.append(" and h.diancxxb_id=").append(visit.getDiancxxb_id()).append("\n")
//			.append(" and h.shenhzt in (0,2) \n")
//			.append(" ) f order by f.bianm\n");
			
//			sb.append(
//					"SELECT  rownum as id,g.bianm from\n" +
//					"( select DISTINCT h.bianm  from huaygyfxb h\n" + 
//					" where h.riq>=").append(DateUtil.FormatOracleDate(getRiq())).append("-1\n" +
//					"and h.riq<=").append(DateUtil.FormatOracleDate(getRiqEnd())).append("\n" +
//					"and h.diancxxb_id=" + visit.getDiancxxb_id() + "\n" + 
//					"and h.shenhzt in (0,2)\n" + 
//					") g,\n" + 
//					"(select DISTINCT h.bianm  from huayrlb h\n" + 
//					" where h.riq>=").append(DateUtil.FormatOracleDate(getRiq())).append("-1\n" +
//					"and h.riq<=").append(DateUtil.FormatOracleDate(getRiqEnd())).append("\n" +
//					"and h.diancxxb_id=" + visit.getDiancxxb_id() + "\n" + 
//					"and h.shenhzt in (0,2)\n" + 
//					") r\n" +
//					"where r.bianm=g.bianm \n" +
//					"order by bianm"
//			);
			sb.append(
					"SELECT  rownum as id,bianm\n" +
					"from  (\n" + 
					"( select DISTINCT h.bianm  from huaygyfxb h\n" + 
					" where h.riq>=").append(DateUtil.FormatOracleDate(getRiq())).append("\n" + 
					"and h.riq<=").append(DateUtil.FormatOracleDate(getRiqEnd())).append("\n" + 
					"and h.diancxxb_id=" + visit.getDiancxxb_id() + "\n" + 
					"and h.shenhzt in (0,2)\n" + 
					")\n" + 
					"UNION\n" + 
					"(select DISTINCT h.bianm  from huayrlb h\n" + 
					" where h.riq>=").append(DateUtil.FormatOracleDate(getRiq())).append("\n" + 
					"and h.riq<=").append(DateUtil.FormatOracleDate(getRiqEnd())).append("\n" + 
					"and h.diancxxb_id=" + visit.getDiancxxb_id() + "\n" + 
					"and h.shenhzt in (0,2)\n" + 
					")\n" + 
					"UNION\n" + 
					"(select DISTINCT h.bianm  from huaylfb h\n" + 
					" where h.riq=").append(DateUtil.FormatOracleDate(getRiq())).append("\n" + 
					"and h.riq=").append(DateUtil.FormatOracleDate(getRiqEnd())).append("\n" + 
					"and h.diancxxb_id=" + visit.getDiancxxb_id() + "\n" + 
					"AND bianm<>'废样'\n" + 
					"and h.shenhzt in (0,2)\n" + 
					")\n" + 
					")\n" + 
					"order by bianm"
					);
			
		}else{//已审核的编码
//			sb.append(" select rownum as id,f.bianm  from (")
//			.append(" select distinct h.bianm from huaygyfxb h ")
//			.append(" where h.riq=").append(DateUtil.FormatOracleDate(getRiq())).append("\n")
//			.append(" and h.diancxxb_id=").append(visit.getDiancxxb_id()).append("\n")
//			.append(" and h.shenhzt in (1) \n")
//			.append(" ) f order by f.bianm\n");
		
//		sb.append(
//				"SELECT rownum as id, g.bianm from\n" +
//				"( select DISTINCT h.bianm as bianm from huaygyfxb h\n" + 
//				" where h.riq>=").append(DateUtil.FormatOracleDate(getRiq())).append("-1\n" +
//				"and h.riq<=").append(DateUtil.FormatOracleDate(getRiqEnd())).append("\n" +
//				"and h.diancxxb_id=" + visit.getDiancxxb_id() + "\n" + 
//				"and h.shenhzt in (1)\n" + 
//				") g ,\n" + 
//				"(\n" + 
//				"select DISTINCT h.bianm as bianm from huayrlb h\n" + 
//				" where h.riq>=").append(DateUtil.FormatOracleDate(getRiq())).append("-1\n" +
//				"and h.riq<=").append(DateUtil.FormatOracleDate(getRiqEnd())).append("\n" +
//				"and h.diancxxb_id=" + visit.getDiancxxb_id() + "\n" + 
//				"and h.shenhzt in (1)\n" + 
//				") r\n" +  
//				"where r.bianm=g.bianm \n" +
//				"order by bianm"
//				);
			sb.append(
				"SELECT  rownum as id,bianm\n" +
				"from  (\n" + 
				"( select DISTINCT h.bianm  from huaygyfxb h\n" + 
				" where h.riq>=").append(DateUtil.FormatOracleDate(getRiq())).append("\n" + 
				"and h.riq<=").append(DateUtil.FormatOracleDate(getRiqEnd())).append("\n" + 
				"and h.diancxxb_id=" + visit.getDiancxxb_id() + "\n" + 
				"and h.shenhzt in (1)\n" + 
				")\n" + 
				"UNION\n" + 
				"(select DISTINCT h.bianm  from huayrlb h\n" + 
				" where h.riq>=").append(DateUtil.FormatOracleDate(getRiq())).append("\n" + 
				"and h.riq<=").append(DateUtil.FormatOracleDate(getRiqEnd())).append("\n" + 
				"and h.diancxxb_id=" + visit.getDiancxxb_id() + "\n" + 
				"and h.shenhzt in (1)\n" + 
				")\n" + 
				"UNION\n" + 
				"(select DISTINCT h.bianm  from huaylfb h\n" + 
				" where h.riq=").append(DateUtil.FormatOracleDate(getRiq())).append("\n" + 
				"and h.riq=").append(DateUtil.FormatOracleDate(getRiqEnd())).append("\n" + 
				"and h.diancxxb_id=" + visit.getDiancxxb_id() + "\n" + 
				"AND bianm<>'废样'\n" + 
				"and h.shenhzt in (1)\n" + 
				")\n" + 
				")\n" + 
				"order by bianm"
				);
		}
		setBianmModel(new IDropDownModel(sb.toString(), "请选择"));
	}

	// 厂别下拉框
	public IDropDownBean getChangbValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean2() == null) {
			if (getChangbModel().getOptionCount() > 0) {
				setChangbValue((IDropDownBean) getChangbModel().getOption(0));
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean2();
	}

	public void setChangbValue(IDropDownBean value) {
		((Visit) this.getPage().getVisit()).setDropDownBean2(value);
	}

	public IPropertySelectionModel getChangbModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel2() == null) {
			setChangbModel();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel2();
	}

	public void setChangbModel(IPropertySelectionModel value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel2(value);
	}

	public void setChangbModel() {
		Visit visit = (Visit) this.getPage().getVisit();
		StringBuffer sb = new StringBuffer();
		if (visit.isFencb()) {
			sb.append("select id,mingc from diancxxb where fuid="
					+ visit.getDiancxxb_id());
		} else {
			sb.append("select id,mingc from diancxxb where id="
					+ visit.getDiancxxb_id());
		}
		setChangbModel(new IDropDownModel(sb.toString()));
	}

	public void pageValidate(PageEvent arg0) {
		// TODO 自动生成方法存根
		
	}

}
