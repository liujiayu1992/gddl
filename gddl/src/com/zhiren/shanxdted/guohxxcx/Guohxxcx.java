package com.zhiren.shanxdted.guohxxcx;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*
 * 作者:tzf
 * 时间:2009-08-16
 * 内容:完成过衡信息查询
 */

/*
 * 时间：2009-12-18
 * 修改人：ww
 * 修改内容：
 * 			由于导入大同三期数据也要统计过衡情况，而jianjghb中没有三期数据，
 * 			则联立chepb中的数据，三期数据在回皮后导入，故只在空衡中显示。
 * 			添加参数“显示厂级chepb”，值为“电厂ID”
 * 
 * 		insert into xitxxb values(
				getnewid(300),1,300,'显示厂级chepb','303','','过衡',1,'使用'
		)
 * 			
 */

public class Guohxxcx extends BasePage implements PageValidateListener {
//	界面用户提示
	private String msg="";
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
	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	private boolean _RefreshChick = false;
	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}
	
	
	public int Save(ExtGridUtil grid, String strchange, Visit visit,JDBCcon con) {
		
		StringBuffer sql = new StringBuffer("begin \n");

		String tableName=""+grid.tableName;
		ResultSetList delrsl = grid.getDeleteResultSet(strchange);
		while (delrsl.next()) {
			sql.append("delete from ").append(tableName).append(" where id =")
					.append(delrsl.getString(0)).append(";\n");
		}
		delrsl.close();
		ResultSetList mdrsl = grid.getModifyResultSet(strchange);
		while (mdrsl.next()) {
			StringBuffer sql2 = new StringBuffer();
			sql2.append("getnewid(").append(visit.getDiancxxb_id()).append(")");
			if ("0".equals(mdrsl.getString("ID"))) {
				sql.append("insert into ").append(tableName).append("(id");
				for (int i = 1; i < mdrsl.getColumnCount(); i++) {
					sql.append(",").append(mdrsl.getColumnNames()[i]);
					sql2.append(",").append(
							grid.getValueSql(grid.getColumn(mdrsl.getColumnNames()[i]),
									mdrsl.getString(i)));
				}
				sql.append(") values(").append(sql2).append(");\n");
			} else {
	
				if(!(grid.mokmc==null)&&! grid.mokmc.equals("")){
					String id = mdrsl.getString("id");
					//更改时增加日志
					MainGlobal.LogOperation(con,visit.getDiancxxb_id(),visit.getRenymc(),
							SysConstant.RizOpType_UP,grid.mokmc,
							tableName,id);
				}
				
				
				sql.append("update ").append(tableName).append(" set ");
				for (int i = 1; i < mdrsl.getColumnCount(); i++) {
					if("CHEX".equals(mdrsl.getColumnNames()[i])){
						sql.append(mdrsl.getColumnNames()[i]).append(" = '");
						sql.append(mdrsl.getString(mdrsl.getColumnNames()[i])).append("',");
					}else{
						sql.append(mdrsl.getColumnNames()[i]).append(" = ");
						sql.append(
								grid.getValueSql(grid.getColumn(mdrsl.getColumnNames()[i]),
										mdrsl.getString(i))).append(",");
					}
					
				}
				sql.deleteCharAt(sql.length() - 1);
				sql.append(" where id =").append(mdrsl.getString("ID")).append(
						";\n");
			}
		}
		mdrsl.close();
		sql.append("end;");
		int flag = con.getUpdate(sql.toString());
		return flag;
	}
	
	
	
	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con=new JDBCcon();
		
		con.setAutoCommit(false);
		
		ResultSetList mdrsl = this.getExtGrid().getModifyResultSet(this.getChange());
		ResultSetList rs=null;
		String sql="";
		int count=0;
		while(mdrsl.next()){
			
			if (!"0".equals(mdrsl.getString("ID"))) {
				
				rs=con.getResultSetList(" select * from jianjghb where id="+mdrsl.getString("id"));
				
				if(rs.next() && rs.getString("chepb_id")!=null && !rs.getString("chepb_id").equals("")){
					
					count++;
					sql+=" delete from chepb where id="+rs.getString("chepb_id")+";";
					ResultSetList rsl = con.getResultSetList("select id as fahb_id,ches from fahb where id =(select fahb_id from chepb where id=" + rs.getString("chepb_id") + ")");
					if (rsl.next()) {
					//更新发货表
						if (rsl.getInt("ches")==1) {
							sql+="delete from fahb where id=" + rsl.getString("fahb_id") + "\n;";
							
						} else {
							sql+="update fahb  set maoz=(select sum(c.maoz) from chepb c where c.fahb_id=" + rsl.getString("fahb_id") + ")\n";
							sql+=",piz=(select sum(nvl(c.piz,0)) from chepb c where c.fahb_id=" + rsl.getString("fahb_id") + ")\n";
							sql+=",jingz=(select sum(decode(nvl(c.piz,0),0,0,(c.maoz-c.piz-c.koud-c.kous-c.kouz))) from chepb c where c.fahb_id=" + rsl.getString("fahb_id") + ")\n";
							sql+=",biaoz=(select sum(decode(nvl(c.piz,0),0,0,(c.biaoz))) from chepb c where c.fahb_id=" + rsl.getString("fahb_id") + ")\n";
							sql+=",yingd=(select sum(c.yingd) from chepb c where c.fahb_id=" + rsl.getString("fahb_id") + ")\n";
							sql+=",yingk=(select sum(c.yingk) from chepb c where c.fahb_id=" + rsl.getString("fahb_id") + ")\n";
							sql+=",yuns=(select sum(c.yuns) from chepb c where c.fahb_id=" + rsl.getString("fahb_id") + ")\n";
							sql+=",koud=(select sum(c.koud) from chepb c where c.fahb_id=" + rsl.getString("fahb_id") + ")\n";
							sql+=",kous=(select sum(c.kous) from chepb c where c.fahb_id=" + rsl.getString("fahb_id") + ")\n";
							sql+=",kouz=(select sum(c.kouz) from chepb c where c.fahb_id=" + rsl.getString("fahb_id") + ")\n";
							sql+=",zongkd=(select sum(c.zongkd) from chepb c where c.fahb_id=" + rsl.getString("fahb_id") + ")\n";
							sql+=",ches=(select count(c.id) from chepb c where c.fahb_id=" + rsl.getString("fahb_id") + ")\n";
							sql+=",sanfsl=(select sum(c.sanfsl) from chepb c where c.fahb_id=" + rsl.getString("fahb_id") + ")";
							sql+="where id=" + rsl.getString("fahb_id") + ";\n";
						}
					}
					rsl.close();
					sql+=" update jianjghb set chepb_id='',IMPCOMPLETE=0  where id="+mdrsl.getString("id")+";";
					

					MainGlobal.LogOperation(con,visit.getDiancxxb_id(),visit.getRenymc(),
							SysConstant.RizOpType_UP,this.getExtGrid().mokmc,
							this.getExtGrid().tableName,mdrsl.getString("id"));
					
					
//					sql+=" update jianjghb set chepb_id=''  where id="+mdrsl.getString("id")+";";
				}			
			}
		}
		
		if(count>=1){
			sql=" begin \n"+sql+" end;";
			
			int biaoz=con.getUpdate(sql);
			
			if(biaoz>=0){
				this.setMsg("数据操作成功!");
			}else{
				this.setMsg("数据操作失败!");
				return;
			}
			
			
		}
		
		
		
		int flag=this.Save(this.getExtGrid(), this.getChange(), visit, con);
		
		if(flag>=0){
			this.setMsg("数据操作成功!");
		}else{
			this.setMsg("数据操作失败!");
		}
		
		con.commit();
		
		con.Close();
	}

	private void Save1() {
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con=new JDBCcon();
		
		con.setAutoCommit(false);
		
		ResultSetList mdrsl = this.getExtGrid2().getModifyResultSet(this.getChange());
		ResultSetList rs=null;
		String sql="";
		String strNewMeik = "";
		String strNewYd = "";
		int count=0;
		while(mdrsl.next()){
			
			if (!"0".equals(mdrsl.getString("ID"))) {
				
				rs=con.getResultSetList(" select * from jianjghb where id="+mdrsl.getString("id"));
				
				if(rs.next() && rs.getString("chepb_id")!=null && !rs.getString("chepb_id").equals("")){
					
					count++;
					sql+=" delete from chepb where id="+rs.getString("chepb_id")+";\n";
					
					ResultSetList rsl = con.getResultSetList("select id as fahb_id,ches from fahb where id =(select fahb_id from chepb where id=" + rs.getString("chepb_id") + ")");
					if (rsl.next()) {
					//更新发货表
						if (rsl.getInt("ches")==1) {
							sql+="delete from fahb where id=" + rsl.getString("fahb_id") + "\n;";
							
						} else {
							sql+="update fahb  set maoz=(select sum(c.maoz) from chepb c where c.fahb_id=" + rsl.getString("fahb_id") + ")\n";
							sql+=",piz=(select sum(nvl(c.piz,0)) from chepb c where c.fahb_id=" + rsl.getString("fahb_id") + ")\n";
							sql+=",jingz=(select sum(decode(nvl(c.piz,0),0,0,(c.maoz-c.piz-c.koud-c.kous-c.kouz))) from chepb c where c.fahb_id=" + rsl.getString("fahb_id") + ")\n";
							sql+=",biaoz=(select sum(decode(nvl(c.piz,0),0,0,(c.biaoz))) from chepb c where c.fahb_id=" + rsl.getString("fahb_id") + ")\n";
							sql+=",yingd=(select sum(c.yingd) from chepb c where c.fahb_id=" + rsl.getString("fahb_id") + ")\n";
							sql+=",yingk=(select sum(c.yingk) from chepb c where c.fahb_id=" + rsl.getString("fahb_id") + ")\n";
							sql+=",yuns=(select sum(c.yuns) from chepb c where c.fahb_id=" + rsl.getString("fahb_id") + ")\n";
							sql+=",koud=(select sum(c.koud) from chepb c where c.fahb_id=" + rsl.getString("fahb_id") + ")\n";
							sql+=",kous=(select sum(c.kous) from chepb c where c.fahb_id=" + rsl.getString("fahb_id") + ")\n";
							sql+=",kouz=(select sum(c.kouz) from chepb c where c.fahb_id=" + rsl.getString("fahb_id") + ")\n";
							sql+=",zongkd=(select sum(c.zongkd) from chepb c where c.fahb_id=" + rsl.getString("fahb_id") + ")\n";
							sql+=",ches=(select count(c.id) from chepb c where c.fahb_id=" + rsl.getString("fahb_id") + ")\n";
							sql+=",sanfsl=(select sum(c.sanfsl) from chepb c where c.fahb_id=" + rsl.getString("fahb_id") + ")";
							sql+="where id=" + rsl.getString("fahb_id") + ";\n";
						}
					}
					rsl.close();
					strNewMeik = this.getExtGrid2().getValueSql(this.getExtGrid2().getColumn("MEIKXXB_ID"), mdrsl.getString("MEIKXXB_ID"));
					strNewYd = this.getExtGrid2().getValueSql(this.getExtGrid2().getColumn("YUNSDWB_ID"), mdrsl.getString("YUNSDWB_ID"));
					if( !rs.getString("MEIKXXB_ID").equals(strNewMeik) 
							|| !rs.getString("YUNSDWB_ID").equals(strNewYd)){
						
						String zhiybh="0";
						String huaybh="0";
						ResultSetList rec = con.getResultSetList(
								"SELECT distinct zhiybh,huaybh FROM jianjghb WHERE to_char(zhongcsj,'yyyy-mm-dd')='" + this.getRiq()+ "'\n" +
								"AND meikxxb_id=" + strNewMeik + " AND yunsdwb_id=" + strNewYd +"\n" + 
								"AND diancxxb_id=" + rs.getString("diancxxb_id") + "\n" +
								"AND zhiybh IS NOT NULL AND huaybh IS NOT NULL"
								);
						
						if (rec.next()) {
							zhiybh=rec.getString("zhiybh");
							huaybh=rec.getString("huaybh");
						} else {
							zhiybh = "" + getBianm();
							huaybh = "" + getBianm();
						}
						sql+=" update jianjghb  set zhiybh='" + zhiybh + "',huaybh='" + huaybh + "' where id="+mdrsl.getString("id")+";";
					}
					sql+=" update jianjghb set chepb_id='',IMPCOMPLETE=0  where id="+mdrsl.getString("id")+";";
					
					
//					MainGlobal.LogOperation(con,visit.getDiancxxb_id(),visit.getRenymc(),
//							SysConstant.RizOpType_UP,this.getExtGrid2().mokmc,
//							this.getExtGrid2().tableName,mdrsl.getString("id"));
					
					
//					sql+=" update jianjghb set chepb_id=''  where id="+mdrsl.getString("id")+";";
				}							
			}
		}
		
		if(count>=1){
			sql=" begin \n"+sql+" end;";
			
			
			int biaoz=con.getUpdate(sql);
			
			if(biaoz>=0){
				this.setMsg("数据操作成功!");
				
			}else{
				this.setMsg("数据操作失败!");
				return;
			}
			
		}
		
		
		
		int flag=this.Save(this.getExtGrid2(), this.getChange(), visit, con);
		
		if(flag>=0){
			this.setMsg("数据操作成功!");
		}else{
			this.setMsg("数据操作失败!");
		}
		
		
		
		
		con.commit();
		
		con.Close();
	}
	
	private long getBianm() {
		long bianm = 0;
		JDBCcon con = new JDBCcon();
		String sql = "SELECT XL_BiascCode.nextval as seqval FROM dual";
		ResultSetList rsl = con.getResultSetList(sql);
		if (rsl.next()) {
			bianm = (rsl.getLong("seqval") % 3000) + 1025;
		}
		return bianm;
	}
	
	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	
	private boolean _SaveChick1 = false;

	public void SaveButton1(IRequestCycle cycle) {
		_SaveChick1 = true;
	}

	
	
	public void submit(IRequestCycle cycle) {
		if (_RefreshChick) {
			_RefreshChick = false;
			
			if(riqboo){
				riqboo=false;
				
				this.setXiaosModel(null);
				this.setXiaosValue(null);
				
				this.setDingzModel(null);
				this.setDingzValue(null);
			}
			
			getSelectData();
		}else if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}else if(_SaveChick1){
			_SaveChick1=false;
			Save1();
			this.getSelectData();
		}
		
		getXiaosModels();
		getDingzModels();
	}
	
	private StringBuffer getZhongcSql(){
		
		StringBuffer bf=new StringBuffer();
		
		String dcstr="";
		if(!this.hasDianc(this.getTreeid())){
			dcstr=" and j.diancxxb_id="+this.getTreeid()+" ";
		}
		
		String meikstr="";
		if(!this.getXiaosValue().getStrId().equals("0")){
			meikstr=" and j.meikxxb_id="+this.getXiaosValue().getStrId()+" ";
		}
		
		String yunsstr="";
		if(!this.getDingzValue().getStrId().equals("0")){
			yunsstr=" and j.yunsdwb_id="+this.getDingzValue().getStrId()+" ";
		}
		bf.append(" select j.id, m.mingc meikxxb_id,y.mingc yunsdwb_id ,j.cheh,j.maoz,j.biaoz,to_char(j.zhongcsj,'yyyy-MM-dd HH24:mi:ss') zhongcsj ,j.zhongcjjy,nvl(j.zhongchh||'号磅','') zhongchh, chex  from jianjghb j ,meikxxb m,yunsdwb y \n");
		
		bf.append(" where j.meikxxb_id=m.id and y.id=j.yunsdwb_id and \n");
		bf.append("  j.qingcsj is null and j.qingcjjy is null and to_char(j.zhongcsj,'yyyy-mm-dd')='"+this.getRiq()+"'\n");
		bf.append(dcstr+meikstr+yunsstr+"  and j.jufbz=0 \n");
		
		bf.append(" union\n");
		
		bf.append(" select * from (\n");
		bf.append(" select -1 id,'总计' meikxxb_id, '' yunsdwb_id,count(*)||'' cheh,sum(j.maoz) maoz,sum(j.biaoz) biaoz,max(to_char(j.zhongcsj,'yyyy-MM-dd HH24:mi:ss')) zhongcsj,'' zhongcjjy,'' zhongchh,'' chex \n");
		bf.append(" from jianjghb j ,meikxxb m,yunsdwb y \n");
		

		bf.append(" where j.meikxxb_id=m.id and y.id=j.yunsdwb_id and \n");
		bf.append("  j.qingcsj is null and j.qingcjjy is null and to_char(j.zhongcsj,'yyyy-mm-dd')='"+this.getRiq()+"'\n");
		bf.append(dcstr+meikstr+yunsstr+"  and j.jufbz=0 \n");
		bf.append("     ) where cheh<>0  \n");
		
		bf.append("  order by zhongcsj,meikxxb_id, yunsdwb_id ");
		return bf;
	}

	
	
	private StringBuffer getQongcSql(){
		
		/*
		 * 时间：2009-12-18
		 * 修改人：ww
		 * 修改内容：
		 * 			由于导入大同三期数据也要统计过衡情况，而jianjghb中没有三期数据，
		 * 			则联立chepb中的数据，三期数据在回皮后导入，故只在空衡中显示。
		 * 			添加参数“显示厂级chepb”，值为“电厂ID”
		 * 
		 * 		insert into xitxxb values(
						getnewid(300),1,300,'显示厂级chepb','303','','过衡',1,'使用'
				)
		 * 			
		 */
		
		String showChepByDiancID = MainGlobal.getXitxx_item("过衡", "显示厂级chepb", ""+((Visit)getPage().getVisit()).getDiancxxb_id(), "-1");
		
		StringBuffer bf=new StringBuffer();
		

		String dcstr="";
		if(!this.hasDianc(this.getTreeid())){
			dcstr=" and j.diancxxb_id="+this.getTreeid()+" ";
		}
		
		String meikstr="";
		if(!this.getXiaosValue().getStrId().equals("0")){
			meikstr=" and j.meikxxb_id="+this.getXiaosValue().getStrId()+" ";
		}
		
		String yunsstr="";
		if(!this.getDingzValue().getStrId().equals("0")){
			yunsstr=" and j.yunsdwb_id="+this.getDingzValue().getStrId()+" ";
		}
		
		bf.append(" select j.id, m.mingc meikxxb_id,y.mingc yunsdwb_id ,j.cheh,j.maoz,j.piz,j.jingz,j.kous kous, j.biaoz,to_char(j.zhongcsj,'yyyy-MM-dd HH24:mi:ss') zhongcsj,j.zhongcjjy,\n");
		bf.append("     to_char(j.qingcsj,'yyyy-MM-dd HH24:mi:ss') qingcsj,j.qingcjjy,j.beiz,decode(j.jufbz,0,'否','是') as jufbz,j.caiyd,chex\n");
		
		bf.append("from ( select j.id, j.meikxxb_id,j.yunsdwb_id ,j.cheh,j.maoz,j.piz,j.jingz,j.kous kous,j.biaoz,j.beiz, j.zhongcsj,j.zhongcjjy,\n" +
				"    j.qingcsj,j.qingcjjy,j.diancxxb_id,j.jufbz,j.caiyd,j.chex from jianjghb j\n" + 
				"    where j.qingcsj is not null and j.qingcjjy is not null and to_char(j.zhongcsj,'yyyy-mm-dd')='"+this.getRiq()+"' --and j.jufbz=0\n" +  //将拒收的车数显示红色
				"union\n" + 
				"(select c.id,f.meikxxb_id,c.yunsdwb_id,c.cheph as cheh,c.maoz,c.piz,(c.maoz-c.piz-c.koud) as jingz,\n" + 
				"    c.koud as kous,c.biaoz,c.beiz,c.zhongcsj,c.zhongcjjy, c.qingcsj,c.qingcjjy,f.diancxxb_id,0 as jufbz,'' caiyd,'' chex \n" + 
				"    from chepb c,fahb f where f.id=c.fahb_id and f.fahrq=to_date('"+this.getRiq()+"','yyyy-mm-dd') and f.diancxxb_id=" + showChepByDiancID + "\n" + 
				"   )\n" + 
				" ) j,"
				);
		
		bf.append("  meikxxb m,yunsdwb y\n");
		bf.append("  where j.meikxxb_id=m.id and y.id=j.yunsdwb_id \n");
		bf.append(dcstr+meikstr+yunsstr+"\n");
		
		
		bf.append(" union\n");
		
		bf.append(" select * from (\n");
		bf.append(" select -1 id, '总计' meikxxb_id,'' yunsdwb_id,count(*)||''  cheh,sum(j.maoz) maoz,sum(j.piz) piz,\n");
		bf.append(" sum(j.jingz) jingz,sum(j.kous) kous,sum(j.biaoz) biaoz,max(to_char(j.zhongcsj,'yyyy-MM-dd HH24:mi:ss')) zhongcsj,'' zhongcjjy,\n");
		bf.append(" '' qingcsj,'' qingcjjy,'' beiz,nvl('否','') as jufbz,'' caiyd,'' chex  \n");

		bf.append("from ( select j.id, j.meikxxb_id,j.yunsdwb_id ,j.cheh,j.maoz,j.piz,j.jingz,j.kous kous,j.biaoz,j.beiz, j.zhongcsj,j.zhongcjjy,\n" +
					"    j.qingcsj,j.qingcjjy,j.diancxxb_id,j.jufbz,j.caiyd,j.chex from jianjghb j\n" + 
					"    where j.qingcsj is not null and j.qingcjjy is not null and to_char(j.zhongcsj,'yyyy-mm-dd')='"+this.getRiq()+"' --and j.jufbz=0\n" + 
					"union\n" + 
					"(select c.id,f.meikxxb_id,c.yunsdwb_id,c.cheph as cheh,c.maoz,c.piz,(c.maoz-c.piz-c.koud) as jingz,\n" + 
					"    c.koud as kous,c.biaoz,c.beiz,c.zhongcsj,c.zhongcjjy, c.qingcsj,c.qingcjjy,f.diancxxb_id,0 as jufbz,'' caiyd ,'' chex\n" + 
					"    from chepb c,fahb f where f.id=c.fahb_id and f.fahrq=to_date('"+this.getRiq()+"','yyyy-mm-dd') and f.diancxxb_id=" + showChepByDiancID + "\n" + 
					"   )\n" + 
					" ) j,"
					);
		
		bf.append(" meikxxb m,yunsdwb y\n");
		bf.append("  where j.meikxxb_id=m.id and y.id=j.yunsdwb_id \n");
		bf.append(dcstr+meikstr+yunsstr+"\n");
		bf.append("     ) where cheh<>0  \n");
		
		bf.append("  order by qingcsj,meikxxb_id, yunsdwb_id,qingcsj\n");
		return bf;
	}
	
	
	private StringBuffer getToolSql(){
		
		String showChepByDiancID = MainGlobal.getXitxx_item("过衡", "显示厂级chepb", ""+((Visit)getPage().getVisit()).getDiancxxb_id(), "-1");
		
		StringBuffer bf=new StringBuffer();	

		String dcstr="";
		if(!this.hasDianc(this.getTreeid())){
			dcstr=" and diancxxb_id="+this.getTreeid()+" ";
			
			//选择树不是配置的电厂id则值为-1不显示该电厂Id数据
			if (!showChepByDiancID.equals(this.getTreeid())) {
				showChepByDiancID="-1";
			}
		}
		
		String riq="  and to_char(zhongcsj,'yyyy-MM-dd')='"+this.getRiq()+"' ";
		//过重
//		bf.append("  select 0 id,count(*) mingc from jianjghb j where  j.jufbz=0 and (zhuangt=3 or zhuangt=4)  "+dcstr+riq+"\n");
		bf.append(
				"select 0 id,sum(mingc) mingc from\n" +
				"(\n" + 
				"  select count(*) mingc from jianjghb j where  j.jufbz=0 and (zhuangt=3 or zhuangt=4)" + dcstr+riq + "\n" + 
				"union\n" + 
				"  select count(*) mingc from chepb c where c.fahb_id in (select id from fahb where diancxxb_id=" + showChepByDiancID +")" + 
				riq + "\n" +
				")\n"
		);
		
		bf.append("  union\n");
		//过空
//		bf.append("  select 1 id,count(*) mingc from jianjghb j where  j.jufbz=0 and zhuangt=4 and  \n");
//		bf.append(" j.qingcsj is not null and j.qingcjjy is not null  "+dcstr+riq+"\n");	
		bf.append(
				" select 1 id,sum(mingc) mingc from\n" +
				" (\n" + 
				"    select count(*) mingc from jianjghb j where  j.jufbz=0 and zhuangt=4 and\n" + 
				"    j.qingcsj is not null and j.qingcjjy is not null " + dcstr+riq + "\n" + 
				"union\n" + 
				"    select count(*) mingc from chepb c where c.fahb_id in (select id from fahb where diancxxb_id=" + showChepByDiancID +")" + 
				riq + "\n" + 
				"  )"
		);
		
		bf.append("   union\n");
		//拒收
		bf.append(" select 2 id,count(*) mingc from jianjghb j where j.jufbz=1 "+dcstr+riq+" \n");
		
		bf.append(" union\n");
		//收煤量
//		bf.append(" select 3 id,sum(j.jingz) mingc from jianjghb j  where j.jufbz=0 "+dcstr+riq+"\n");
		bf.append(
				"select 3 id,sum(jingz) mingc from\n" +
				"(\n" + 
				"   select sum(j.jingz) jingz from jianjghb j  where j.jufbz=0 "+dcstr+riq+" \n" +
				"union\n" + 
				"  select sum(maoz-piz-koud) jingz from chepb c where c.fahb_id in (select id from fahb where diancxxb_id=" + showChepByDiancID + ")\n" + 
				riq + "\n" + 
				")"
		);
		
		bf.append(" order by id asc \n");
		
		return bf;
	}
//	 绑定日期
	private String riq;

	private boolean riqboo=false;
	public String getRiq() {
		if(this.riq==null || this.riq.equals("")){
			this.riq=DateUtil.FormatDate(new Date());
		}
		return riq;
	}

	public void setRiq(String riq) {
		
		if(this.riq!=null && !this.riq.equals(riq)){
			riqboo=true;
		}
		this.riq = riq;
	}
	
	
	
//	煤矿-------------------
		
		private IDropDownBean XiaosValue;

		public IDropDownBean getXiaosValue() {
			if (XiaosValue == null) {
				XiaosValue = (IDropDownBean) getXiaosModel().getOption(0);
			}
			return XiaosValue;
		}

		public void setXiaosValue(IDropDownBean Value) {
			if (!(XiaosValue == Value)) {
				XiaosValue = Value;
			//	falg1 = true;
			}
		}
		
		private boolean hasDianc(String id){
			
			JDBCcon con=new JDBCcon();
			
			String sql=" select id from diancxxb where  fuid="+id;
			
			ResultSetList rsl=con.getResultSetList(sql);
			boolean t=false;
			if(rsl.next()){
				t= true;
			}
			
			rsl.close();
			con.Close();
			return t;
		}

		private IPropertySelectionModel XiaosModel;

		public void setXiaosModel(IPropertySelectionModel value) {
			XiaosModel = value;
		}

		public IPropertySelectionModel getXiaosModel() {
			if (XiaosModel == null) {
				getXiaosModels();
			}
			return XiaosModel;
		}

		public IPropertySelectionModel getXiaosModels() {
			
			String dcstr="";
			
			if(!this.hasDianc(this.getTreeid())){
				dcstr=" and j.diancxxb_id="+this.getTreeid();
			}
			String sql=" select m.id,m.mingc from jianjghb j,meikxxb m where j.meikxxb_id=m.id and j.jufbz=0 \n" +
					"  and to_char(j.zhongcsj,'yyyy-MM-dd')='"+this.getRiq()+"' \n" +dcstr+" \n"+
					"  union \n" +
					"  select 0 id,'全部' mingc from dual \n" +
					" order by id";
			
			XiaosModel = new IDropDownModel(sql);
			return XiaosModel;
		}
		
		//运输单位
		
		
		private IDropDownBean DingzValue;

		public IDropDownBean getDingzValue() {
			if (DingzValue == null) {
				DingzValue = (IDropDownBean) getDingzModel().getOption(0);
			}
			return DingzValue;
		}

		public void setDingzValue(IDropDownBean Value) {
			if (!(DingzValue == Value)) {
				DingzValue = Value;
			//	falg1 = true;
			}
		}

		private IPropertySelectionModel DingzModel;

		public void setDingzModel(IPropertySelectionModel value) {
			DingzModel = value;
		}

		public IPropertySelectionModel getDingzModel() {
			if (DingzModel == null) {
				getDingzModels();
			}
			return DingzModel;
		}

		public IPropertySelectionModel getDingzModels() {
			
			String dcstr="";
			
			if(!this.hasDianc(this.getTreeid())){
				dcstr=" and j.diancxxb_id="+this.getTreeid();
			}
			String sql=" select y.id,y.mingc from jianjghb j,yunsdwb y where j.yunsdwb_id=y.id and j.jufbz=0 \n" +
					"  and to_char(j.zhongcsj,'yyyy-MM-dd')='"+this.getRiq()+"' \n" +dcstr+" \n"+
					"  union \n" +
					"  select 0 id,'全部' mingc from dual \n" +
					" order by id";
			
			DingzModel = new IDropDownModel(sql);
			return DingzModel;
		}
		
	private boolean isFencb(){
		JDBCcon con = new JDBCcon();
		String sql = "select * from diancxxb where fuid=" + getTreeid();
		ResultSetList rs=con.getResultSetList(sql);
		if(rs.next()){
			return true;
		}
		return false;
	}
		
	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		
		String chexTj = "";
		if(isFencb()){
			chexTj = "select id,mingc from xiecfsb";
		}else{
			chexTj = "select id,mingc from xiecfsb where diancxxb_id = " + getTreeid();
		}
		
//		加入电厂ID判断
//		原因： 三期班长级以上人员要求看到全厂信息，还要求修改本期过衡数据，这两个条件放到一起会修改其它分厂数据。
//		在分配权限时，人员在全厂级别上，分配哪个厂的guohxxxgz权限，就能修改哪个厂的数据
		String toaijsql=" select * from renyzqxb qx,zuxxb z,renyxxb r where qx.zuxxb_id=z.id and qx.renyxxb_id=r.id\n" +
				" and z.mingc='guohxxxgz' and z.diancxxb_id=" + getTreeid() + " and r.id="+visit.getRenyID();//zuxxb中组的名称
		ResultSetList rs=con.getResultSetList(toaijsql);
		boolean flag=false;//否
		if(rs.next()){
			flag=true;//页面是否可编辑
		}
		
		toaijsql=" select * from renyzqxb qx,zuxxb z,renyxxb r where qx.zuxxb_id=z.id and qx.renyxxb_id=r.id\n" +
				" and z.mingc='guohxxslxgz' and z.diancxxb_id=" + getTreeid() + " and r.id="+visit.getRenyID();//zuxxb中组的名称
		rs=con.getResultSetList(toaijsql);
		boolean shulflag = false;
		if(rs.next()){
			shulflag=true;//数量是否可编辑
		}
		
		String sql=this.getZhongcSql().toString();
		ResultSetList rsl = con.getResultSetList(sql);
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + sql);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setWidth(Locale.Grid_DefaultWidth);
		
		
		if(flag){
			egu.setGridType(ExtGridUtil.Gridstyle_Edit);//设置是否可以编辑
		}else{
			egu.setGridType(ExtGridUtil.Gridstyle_Read);//设置是否可以编辑
		}
		egu.setTableName("jianjghb");
		egu.setMokmc("检斤过衡表查询修改");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("meikxxb_id").setHeader("煤矿");
		egu.getColumn("yunsdwb_id").setHeader("运输单位");
		egu.getColumn("cheh").setHeader("车号/车数");
		egu.getColumn("maoz").setHeader("毛重");
		if(!shulflag){
			egu.getColumn("maoz").setEditor(null);
		}
		
		egu.getColumn("biaoz").setHeader("票重");
		egu.getColumn("biaoz").setEditor(new NumberField());
		egu.getColumn("zhongcsj").setHeader("重车时间");
		egu.getColumn("zhongcjjy").setHeader("重车检斤员");
		egu.getColumn("zhongchh").setHeader("衡器号");
		egu.getColumn("chex").setHeader("车型");
		
		egu.getColumn("chex").setEditor(new ComboBox());
		egu
			.getColumn("chex")
			.setComboEditor(egu.gridId,
						new IDropDownModel(chexTj));
		egu.getColumn("chex").setReturnId(true);
		((ComboBox)(egu.getColumn("chex").editor)).setEditable(true);
		
//		egu.getColumn("meik").setEditor(null);
//		egu.getColumn("yunsdw").setEditor(null);
		egu.getColumn("cheh").setEditor(null);
		egu.getColumn("zhongcsj").setEditor(null);
		egu.getColumn("zhongcjjy").setEditor(null);
		egu.getColumn("zhongchh").setEditor(null);
		
//		egu.getColumn("meik").setUpdate(false);
//		egu.getColumn("yunsdw").setUpdate(false);
		egu.getColumn("cheh").setUpdate(false);
		egu.getColumn("zhongcsj").setUpdate(false);
		egu.getColumn("zhongcjjy").setUpdate(false);
		egu.getColumn("zhongchh").setUpdate(false);
		
		egu.getColumn("zhongcsj").setWidth(190);
		
		ComboBox yunsdw=new ComboBox();
		egu.getColumn("yunsdwb_id").setEditor(yunsdw);
		egu.getColumn("yunsdwb_id").setReturnId(true);
		egu.getColumn("yunsdwb_id").setComboEditor(egu.gridId, new IDropDownModel(" select id ,mingc from yunsdwb where diancxxb_id=" + this.getTreeid()));
		
		

		ComboBox meik=new ComboBox();
		egu.getColumn("meikxxb_id").setEditor(meik);
		egu.getColumn("meikxxb_id").setReturnId(true);
		egu.getColumn("meikxxb_id").setComboEditor(egu.gridId, new IDropDownModel(" select id ,mingc from meikxxb "));
		
		for(int i=0;i<egu.getGridColumns().size();i++){
			((GridColumn)(egu.getGridColumns().get(i))).setRenderer(" function(value){return '<font size=4>'+value+'</font>';} ");
		}
		
		//设置Grid行数
		egu.addPaging(-1);
		
		DateField df = new DateField();
		df.setValue(this.getRiq());
		df.Binding("RIQ", "");// 与html页中的id绑定,并自动刷新
		df.setId("guohrq");
		egu.addToolbarItem(df.getScript());
		
		egu.addTbarText("-");
		
		egu.addTbarText("单位名称:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc,
				((Visit) getPage().getVisit()).getDiancxxb_id(),getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		
		egu.addTbarText("-");
		
		//煤矿
		

		egu.addTbarText("煤矿:");// 设置分隔符

		ComboBox xs=new ComboBox();
		xs.setWidth(120);
		xs.setTransform("XS");
		xs.setId("XS");//和自动刷新绑定
		xs.setLazyRender(true);//动态绑定
		egu.addToolbarItem(xs.getScript());
		
		
		egu.addTbarText("-");// 设置分隔符
		
		
		//运输单位
		
		egu.addTbarText("运输单位:");// 设置分隔符

		ComboBox dingz=new ComboBox();
		dingz.setWidth(120);
		dingz.setTransform("Dingz");
		dingz.setId("Dingz");//和自动刷新绑定
		dingz.setLazyRender(true);//动态绑定
		egu.addToolbarItem(dingz.getScript());
		
		
		egu.addTbarText("-");// 设置分隔符
		
		
		GridButton gbt = new GridButton("刷新","function(){document.getElementById('RefreshButton').click();}");
		gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbt);
		
		egu.addTbarText("-");// 设置分隔符
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		
		sql=this.getToolSql().toString();
		
		egu.addOtherScript(
				"gridDiv_grid.on('celldblclick',function(g,rowIndex,columnIndex,e){\n" +
				"\n" + 
				"  if(columnIndex==4){\n" + 
				"\n" + 
				"    //  双击车号得到id，弹出窗口\n" + 
				"    var rec = g.store.getAt(rowIndex);\n" + 
				"\n" + 
				"    var value = rec.get('ID');\n" + 
				"\n" + 
				"    /*\n" + 
				"    var url = \"http://\"+document.location.host+document.location.pathname;\n" + 
				"    var end = url.indexOf(';');\n" + 
				"    url = url.substring(0,end);\n" + 
				"    */\n" + 
				"\n" +
				"	if(value!=''){	\n" +
				"       var url = \"http://\"+document.location.host+document.location.pathname;\n" +
				"    	var newwin = url + '?service=page/' + 'Shipcx&lx='+value;\n" + 
				"    	window.open(newwin,'Shipc')\n" + 
				"	}else{\n" +
				"		Ext.MessageBox.alert('提示信息','没有找到视频文件名称!');" +
				"	}\n" + 
				"  }\n" + 
				"});"
		);
		
		String toolstr="";
		int guoz=0;
		int guop=0;
		int juf=0;
		double laiml=0;
		
		rsl=con.getResultSetList(sql);
		while(rsl.next()){
			
			if(rsl.getString("id").equals("0")){
				guoz=rsl.getInt("mingc");
			}
			if(rsl.getString("id").equals("1")){
				guop=rsl.getInt("mingc");
			}
			if(rsl.getString("id").equals("2")){
				juf=rsl.getInt("mingc");
			}
			if(rsl.getString("id").equals("3")){
				laiml=rsl.getDouble("mingc");
			}
		}
		
		int noguop=guoz-guop;//未回皮
		
		int yiscs=guop;//已收车数
		
		toolstr=" '    ','<font size=\"4\">过毛车数: "+guoz+"</font>','&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;','<font size=\"4\">过空车数: "+guop+"</font>','&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;','<font size=\"4\">未过空车数: "+noguop+"</font>','&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;','<font size=\"4\">接收车数: "+yiscs+"</font>','&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'," +
				"'<font size=\"4\">拒收车数: "+juf+"</font>','&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;','<font size=\"4\">今日来煤: "+laiml+"</font>'";
		egu.addOtherScript(" var tbar3 = new Ext.Toolbar({\n" +
				"renderTo : gridDiv_grid.tbar,\n" +
				"" +
				"items : ["+toolstr+"]" +
				"});");
		setExtGrid(egu);
		
		
		
		//设置轻车grid
		sql=this.getQongcSql().toString();
		rsl=con.getResultSetList(sql);
		ExtGridUtil egu1 = new ExtGridUtil("gridDiv1", rsl);
		egu1.setWidth(Locale.Grid_DefaultWidth);
		
		if(flag){
			egu1.setGridType(ExtGridUtil.Gridstyle_Edit);//设置是否可以编辑
		}else{
			egu1.setGridType(ExtGridUtil.Gridstyle_Read);//设置是否可以编辑
		}
		
		egu1.setTableName("jianjghb");
		egu1.setMokmc("检斤过衡表查询修改");
		egu1.getColumn("id").setHidden(true);
		egu1.getColumn("meikxxb_id").setHeader("煤矿");
		egu1.getColumn("yunsdwb_id").setHeader("运输单位");
		egu1.getColumn("cheh").setHeader("车号/车数");
		egu1.getColumn("maoz").setHeader("毛重");
		egu1.getColumn("piz").setHeader("皮重");
		egu1.getColumn("jingz").setHeader("净重");
		egu1.getColumn("kous").setHeader("扣吨");
		egu1.getColumn("biaoz").setHeader("票重");
		egu1.getColumn("biaoz").setEditor(new NumberField());
		egu1.getColumn("zhongcsj").setHeader("重车时间");
		egu1.getColumn("zhongcjjy").setHeader("重车检斤员");
		egu1.getColumn("qingcsj").setHeader("轻车时间");
		egu1.getColumn("qingcjjy").setHeader("轻车检斤员");
		egu1.getColumn("beiz").setHeader("质检员");
		egu1.getColumn("jufbz").setHeader("拒收");
		egu1.getColumn("jufbz").setHidden(true);
		egu1.getColumn("caiyd").setHeader("采样点");
		egu1.getColumn("caiyd").setEditor(null);
		
		egu1.getColumn("chex").setHeader("车型");
		egu1.getColumn("chex").setEditor(new ComboBox());
		egu1
			.getColumn("chex")
			.setComboEditor(egu.gridId,
						new IDropDownModel(chexTj));
		egu1.getColumn("chex").setReturnId(true);
		((ComboBox)(egu1.getColumn("chex").editor)).setEditable(true);
		
//		egu1.getColumn("meik").setEditor(null);
//		egu1.getColumn("yunsdw").setEditor(null);
		egu1.getColumn("cheh").setEditor(null);
		egu1.getColumn("beiz").setEditor(null);
		egu1.getColumn("zhongcsj").setEditor(null);
		egu1.getColumn("zhongcjjy").setEditor(null);
		egu1.getColumn("qingcsj").setEditor(null);
		egu1.getColumn("qingcjjy").setEditor(null);
		egu1.getColumn("jufbz").setEditor(null);
//		egu1.getColumn("jingz").setEditor(null);
		
//		egu1.getColumn("meik").setUpdate(false);
//		egu1.getColumn("yunsdw").setUpdate(false);
		egu1.getColumn("cheh").setUpdate(false);
		egu1.getColumn("beiz").setUpdate(false);
		egu1.getColumn("jufbz").setUpdate(false);
		egu1.getColumn("zhongcsj").setUpdate(false);
		egu1.getColumn("zhongcjjy").setUpdate(false);
		egu1.getColumn("qingcsj").setUpdate(false);
		egu1.getColumn("qingcjjy").setUpdate(false);
//		egu1.getColumn("jingz").setUpdate(false);
	
		ComboBox yunsdw1=new ComboBox();
		egu1.getColumn("yunsdwb_id").setEditor(yunsdw1);
		egu1.getColumn("yunsdwb_id").setReturnId(true);
		egu1.getColumn("yunsdwb_id").setComboEditor(egu1.gridId, new IDropDownModel(" select id ,mingc from yunsdwb where diancxxb_id=" + this.getTreeid()));
		
		
		ComboBox meik1=new ComboBox();
		egu1.getColumn("meikxxb_id").setEditor(meik1);
		egu1.getColumn("meikxxb_id").setReturnId(true);
		egu1.getColumn("meikxxb_id").setComboEditor(egu1.gridId, new IDropDownModel(" select id ,mingc from meikxxb "));
		
		if(!shulflag){
			egu1.getColumn("maoz").setEditor(null);
			egu1.getColumn("piz").setEditor(null);
			egu1.getColumn("jingz").setEditor(null);
		}else{
			NumberField mz=new NumberField();
			mz.setDecimalPrecision(2);
			egu1.getColumn("maoz").setEditor(mz);
			

			NumberField pz=new NumberField();
			pz.setDecimalPrecision(2);
			egu1.getColumn("piz").setEditor(pz);
			

			NumberField jz=new NumberField();
			jz.setDecimalPrecision(2);
			egu1.getColumn("jingz").setEditor(jz);
		}
		
		for(int i=0;i<egu1.getGridColumns().size();i++){
			((GridColumn)(egu1.getGridColumns().get(i))).setRenderer(" function(value){return '<font size=4>'+value+'</font>';} ");
		}
		
		
		egu1.getColumn("maoz").setWidth(60);
		egu1.getColumn("piz").setWidth(60);
		egu1.getColumn("jingz").setWidth(60);
		egu1.getColumn("kous").setWidth(60);
		egu1.getColumn("biaoz").setWidth(60);
		egu1.getColumn("zhongcsj").setWidth(180);
		egu1.getColumn("zhongcjjy").setWidth(60);
		egu1.getColumn("qingcsj").setWidth(180);
		egu1.getColumn("qingcjjy").setWidth(60);
		egu1.getColumn("beiz").setWidth(60);
		
		String viewConfig = 
			"{   \n" +
			"    getRowClass : function(record,rowIndex,rowParams,store){\n" + 
			"        //拒收数据显示红色\n" + 
			"        if(record.get('JUFBZ')=='是'){\n" + 
			"            return 'x-grid-record-red';\n" + 
			"        }else{\n" + 
			"            return '';\n" + 
			"        }\n" + 
			"\n" + 
			"    }\n" + 
			"}";
		egu1.setViewConfig(viewConfig);
		
		//设置Grid行数
		egu1.addPaging(-1);
		
		
		egu1.addTbarText("轻磅");
		egu1.addTbarText("-");
		egu1.addToolbarButton(GridButton.ButtonType_Save, "SaveButton1");
		
//		egu1.addOtherScript(" var tbar4 = new Ext.Toolbar({\n" +
//				"renderTo : gridDiv1_grid.tbar,\n" +
//				"" +
//				"items : ['轻磅']\n" +
//				"});");
		
		
		egu1.addOtherScript(
				"gridDiv1_grid.on('celldblclick',function(g,rowIndex,columnIndex,e){\n" +
				"\n" + 
				"  if(columnIndex==4){\n" + 
				"\n" + 
				"    //  双击车号得到id，弹出窗口\n" + 
				"    var rec = g.store.getAt(rowIndex);\n" + 
				"\n" + 
				"    var value = rec.get('ID');\n" + 
				"\n" + 
				"    /*\n" + 
				"    var url = \"http://\"+document.location.host+document.location.pathname;\n" +  
				"    var end = url.indexOf(';');\n" + 
				"    url = url.substring(0,end);\n" + 
				"    */\n" + 
				"\n" +
				"	if(value!=''){	\n" +
				"		var url = \"http://\"+document.location.host+document.location.pathname;\n" +
				"    	var newwin = url + '?service=page/' + 'Shipcx&lx='+value;\n" + 
				"    	window.open(newwin,'Shipc')\n" + 
				"	}else{\n" +
				"		Ext.MessageBox.alert('提示信息','没有找到视频文件名称!');" +
				"	}\n" + 
				"  }\n" + 
				"});"
		);
//		
		egu1.addOtherScript("gridDiv1_grid.addListener('afteredit',function(e){" +
				"" +
				"if(e.field=='MAOZ' || e.field=='PIZ' || e.field=='KOUS'){\n" +
				" var mv=e.record.get('MAOZ');\n" +
				" var pv=e.record.get('PIZ');\n" +
				" var kv=e.record.get('KOUS');\n"+
				" if(mv==null || mv=='') mv=0;\n" +
				" if(pv==null || pv=='') pv=0;\n" +
				" if(kv==null || kv=='') kv=0;\n" +
				" e.record.set('JINGZ',(mv-pv-kv));\n" +
				"\n}" +
				"" +
				"\n});");
		
		
		egu1.addOtherScript("gridDiv1_grid.addListener('beforeedit',function(e){" +
				"" +
				"if(e.record.get('ID')=='-1'){\n" +
				" e.cancel=true;\n" +
				"\n}" +
				"" +
				"\n});");
		
		
		this.setExtGrid2(egu1);
		
		con.Close();
	}

	//重车grid
	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}

	public String getGridScript() {
		if(getExtGrid()==null){
			return "";
		}
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		if(getExtGrid()==null){
			return "";
		}
		return getExtGrid().getHtml();
	}
	
	
	//轻车grid
	
	public ExtGridUtil getExtGrid2() {
		return ((Visit) this.getPage().getVisit()).getExtGrid2();
	}

	public void setExtGrid2(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid2(extgrid);
	}

	public String getGridScript2() {
		if(getExtGrid2()==null){
			return "";
		}
		return getExtGrid2().getGridScript();
	}

	public String getGridHtml2() {
		if(getExtGrid2()==null){
			return "";
		}
		return getExtGrid2().getHtml();
	}
	
	//
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
	
	private String treeid = "";
	public String getTreeid() {
		if (treeid==null||treeid.equals("")) {

			treeid = String.valueOf(((Visit) getPage().getVisit())
					.getDiancxxb_id());
		}
		return treeid;
	}
	public void setTreeid(String treeid) {
		if (!this.treeid.equals(treeid)) {

			((Visit) getPage().getVisit()).setboolean3(true);
			this.treeid = treeid;
		}
	}
	// 得到电厂树的值的级别,(jib:1,2,3,) 1是集团,2是分公司,3是电厂
	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
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

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			setTreeid(null);
			
			this.setXiaosModel(null);
			this.setXiaosValue(null);
			
			this.setDingzModel(null);
			this.setDingzValue(null);
			
			this.setRiq(null);
		}
		init();
	}
	private void init() {
		getSelectData();
	}
}
