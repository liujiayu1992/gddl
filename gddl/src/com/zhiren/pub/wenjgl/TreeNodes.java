package com.zhiren.pub.wenjgl;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.zhiren.common.JDBCcon;
/*
 * 作者：ww
 * 时间：2010-01-21
 * 描述：修改sql2的语句，使得fuid不为0也可以显示上级
 */
public class TreeNodes {
	//根据输入的表名、id号、显示字段、三种显示类型，并根据默认的id、fuid，返回树节点
	//三种类型包括：0,id本身及所有子代1,id及所有上级2,0、1的合并
	private String tableName;
	private String displayField;
	private long id;
	private int leix;
	private boolean isCheck;
	private StringBuffer bufferResult=null;
	private String sql0="";
	private String sql1="";
	private String sql2="";
	private String sql3="";
	public TreeNodes(String tableName,long id, String displayField, int leix,boolean isCheck) {
		super();
		this.tableName = tableName;
		this.displayField = displayField;
		this.id = id;
		this.leix = leix;
		this.isCheck=isCheck;
		bufferResult=new StringBuffer();
		sql0="select "+tableName+".id, "+displayField+",level,CONNECT_BY_ISLEAF from "+tableName+"\n" +
		"start with id=" +id+ 
		"\n" + 
		"connect by fuid=prior id\n" + 
		"order SIBLINGS by  xuh";
		
		JDBCcon con=new JDBCcon();
		ResultSet rs = con.getResultSet("select fuid from diancxxb where id=" + id);
		String fuid="0";
		try {
			rs.next();
			fuid=rs.getString("fuid");
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		
		sql1=

			"select id,"+displayField+",level,CONNECT_BY_ISLEAF\n" +
			"from(\n" + 
			" select id,"+displayField+",fuid\n" + 
			" from "+tableName+"\n" + 
			" start with id=" +id+  
			" connect by prior fuid= id\n" + 
			" )\n" + 
			" start with fuid=0\n" + 
			" connect by fuid=prior id";

//		sql2=
//
//			"select id,"+displayField+",level,CONNECT_BY_ISLEAF\n" +
//			"from(\n" + 
//			" select id,"+displayField+",fuid\n" + 
//			" from "+tableName+"\n" + 
//			" start with id=" +id+  
//			" connect by prior fuid= id\n" + 
//			"union\n" + 
//			" select id,"+displayField+",fuid\n" + 
//			" from "+tableName+"\n" + 
//			" start with fuid=" +id+  
//			" connect by fuid=prior id\n" + 
//			" )\n" + 
//			"  start with fuid=0\n" + 
//			" connect by fuid=prior id";
		
		// 2010-01-20 ww
		// 修改sql2的语句，start with fuid=0 改为 start with id=" + fuid + "
		sql2=

			"select id,"+displayField+",level,CONNECT_BY_ISLEAF\n" +
			"from(\n" + 
			" select id,"+displayField+",fuid\n" + 
			" from "+tableName+"\n" + 
			" start with id=" +id+  
			" connect by prior fuid= id\n" + 
			"union\n" + 
			" select id,"+displayField+",fuid\n" + 
			" from "+tableName+"\n" + 
			" start with fuid=" +id+  
			" connect by fuid=prior id\n" + 
			" )\n" + 
			"  start with id=" + fuid + "\n" + 
			" connect by fuid=prior id";

		sql3=
			
			"select id,"+displayField+",level,CONNECT_BY_ISLEAF\n" +
			"from(\n" + 
			" select id,"+displayField+",fuid\n" + 
			" from "+tableName+"\n" + 
			" start with id=" +id+  
			" connect by fuid= prior id\n" + 
			" )\n" + 
			" start with fuid=" + fuid + "\n" + 
			" connect by fuid=prior id";
	}
	/*
	 *   var node0 = new Ext.tree.TreeNode({id:'0',text:'资源权限'});
		var node4 = new Ext.tree.TreeNode({id:'4',text:'合同管理(电厂)',checked:true});
		node0.appendChild([node4]);
	 */
	public String getScript(){//判断这个记录要么与上个记录作兄弟要么做儿子要么为长辈，总之就是一个找父亲的过程
		int p=-1;//上个记录的父亲标识
		int shangj=-1;//上个记录的级别
		JDBCcon con=new JDBCcon();
		bufferResult.setLength(0);
		ResultSet rs=null;
		int[] zhangbl=new int[10]; //当前节点的长辈链,深度不超过10
		int j=0;//长辈链索引
		int i=0;//节点标识
		try{
			switch(leix){
			case 2:
				rs=con.getResultSet(sql2);
				break;
			case 1:
				rs=con.getResultSet(sql1);
				break;
			case 3:
				rs=con.getResultSet(sql3);
				break;
			default :
				rs=con.getResultSet(sql0);
			}
				while(rs.next()){//展现
					if(i!=0){//如果有上个记录，即有父亲节点//不是根节点
						
						if(shangj==rs.getInt("level")){// 如果上个记录的级别不等于这个记录的级别，做兄弟
							bufferResult.append(" var node"+i+" = new Ext.tree.TreeNode({");
							bufferResult.append("id:'"+rs.getString("id")+"',");
							bufferResult.append( "text:'"+rs.getString(displayField)+"',");
							if(isCheck){//如果带下拉框
								bufferResult.append(  "checked:false," );
							}
							
							bufferResult.append(  "leaf :"+(rs.getInt("CONNECT_BY_ISLEAF")==1?"true":"false"));
							bufferResult.append(  "});\n");//这个节点的值
							bufferResult.append(" node"+(p)+".appendChild([node"+i+"]);\n");//插入父亲节点(上一个的父亲)
//							zhangbl[j][0]=i;//平级的长辈链是一个
//							zhangbl[j++][1]=rs.getInt("level");
							
							
						}else if(shangj<rs.getInt("level")){// 如果这个记录的级别大于上级级别
							bufferResult.append(" var node"+i+" = new Ext.tree.TreeNode({" );
							bufferResult.append("id:'"+rs.getString("id")+"'," );
							bufferResult.append( "text:'"+rs.getString(displayField)+"'," );
//							bufferResult.append(  "checked:"+isCheck+"," );
							if(isCheck){//如果带下拉框
								bufferResult.append(  "checked:false," );
							}
							bufferResult.append( "leaf :"+(rs.getInt("CONNECT_BY_ISLEAF")==1?"true":"false"));
							bufferResult.append(  "});\n");//这个节点的值
							bufferResult.append(" node"+(i-1)+".appendChild([node"+i+"]);\n");//插入父亲节点（上一个）
							p=i-1;
							zhangbl[(rs.getInt("level")-2)]=i-1;//平级的长辈链是一个
							
						}else{// 如果这个记录的级别小于上级级别
							bufferResult.append(" var node"+i+" = new Ext.tree.TreeNode({" );
							bufferResult.append("id:'"+rs.getString("id")+"'," );
							bufferResult.append( "text:'"+rs.getString(displayField)+"'," );
//							bufferResult.append( "checked:"+isCheck+"," );
							if(isCheck){//如果带下拉框
								bufferResult.append(  "checked:false," );
							}
							bufferResult.append( "leaf :"+(rs.getInt("CONNECT_BY_ISLEAF")==1?"true":"false"));
							bufferResult.append( "});\n");//这个节点的值
							//上个节点的长辈链中找到父亲，因为链中必有此节点父亲
							bufferResult.append(" node"+zhangbl[(rs.getInt("level")-2)]+".appendChild([node"+i+"]);\n");//插入父亲节点（根节点0）
							p=zhangbl[(rs.getInt("level")-2)];
						}
					}else{
						bufferResult.append(" var node"+i+" = new Ext.tree.TreeNode({" );
						bufferResult.append("id:'"+rs.getString("id")+"'," );
						bufferResult.append( "text:'"+rs.getString(displayField)+"',");
//						bufferResult.append( "checked:"+isCheck+"," );
						if(isCheck){//如果带下拉框
							bufferResult.append(  "checked:false," );
						}
						bufferResult.append(  "leaf :"+(rs.getInt("CONNECT_BY_ISLEAF")==1?"true":"false"));
						bufferResult.append(  "});\n");//这个节点i值
					}
					shangj=rs.getInt("level");//上个记录的级别
					i++;
				}
				
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.Close();
		}
		return bufferResult.toString();
	}
	


}
