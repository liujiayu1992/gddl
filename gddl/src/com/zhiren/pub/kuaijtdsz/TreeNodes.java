package com.zhiren.pub.kuaijtdsz;

import java.sql.ResultSet;

import com.zhiren.common.JDBCcon;

public class TreeNodes {
	//根据输入的表名、id号、显示字段、三种显示类型，并根据默认的id、fuid，返回树节点
	//三种类型包括：0,id本身及所有子代1,id及所有上级2,0、1的合并
	private String tableName;
	private String displayField;
	private long id;
	private int leix;
	private boolean isCheck;
	private String renyid;
	private StringBuffer bufferResult=null;
	private String sql0="";
	private String sql1="";
	private String sql2="";
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
		sql1=

			"select id,"+displayField+",level,CONNECT_BY_ISLEAF\n" +
			"from(\n" + 
			" select id,"+displayField+",fuid\n" + 
			" from "+tableName+"\n" + 
			" start with id=" +id+  
			" connect by prior fuid= id\n" + 
			" )\n" + 
			" start with fuid=-1\n" + 
			" connect by fuid=prior id";

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
			"  start with fuid=-1\n" + 
			" connect by fuid=prior id";

	}
	
	public TreeNodes(String tableName,long id, String displayField, int leix,boolean isCheck,String renyid) {
		super();
		this.tableName = tableName;
		this.displayField = displayField;
		this.id = id;
		this.leix = leix;
		this.renyid = renyid;
		this.isCheck=isCheck;
		bufferResult=new StringBuffer();
		sql0="select "+tableName+".id, "+displayField+",level,CONNECT_BY_ISLEAF from "+tableName+"\n" +
		"start with id=" +id+ 
		"\n" + 
		"connect by fuid=prior id\n" + 
		"order SIBLINGS by  xuh";
		sql1=

			"select id,"+displayField+",level,CONNECT_BY_ISLEAF\n" +
			"from(\n" + 
			" select id,"+displayField+",fuid\n" + 
			" from "+tableName+"\n" + 
			" start with id=" +id+  
			" connect by prior fuid= id\n" + 
			" )\n" + 
			" start with fuid=-1\n" + 
			" connect by fuid=prior id";

		sql2=
			"select id,"+displayField+",level,CONNECT_BY_ISLEAF\n" +
			"from(\n" + 
			"select zy.id,mingc,fuid\n" + 
			"from "+tableName+" zy\n" + 
			"where zy.leib = (select nvl(max(zy.leib),1) leib from zuxxb z,ziyxxb zy,zuqxb zu where zu.zuxxb_id = z.id and z.mingc =\n" + 
			"(select getrenyfz(id) zu from renyxxb where id = "+renyid+"))\n" + 
			"start with id =0 connect by fuid= id\n" + 
			"union\n" + 
			"select zy.id,"+displayField+",fuid\n" + 
			"from "+tableName+" zy\n" + 
			"where zy.leib = (select nvl(max(zy.leib),1) leib from zuxxb z,ziyxxb zy,zuqxb zu where zu.zuxxb_id = z.id and z.mingc =\n" + 
			"(select getrenyfz(id) zu from renyxxb where id = "+renyid+"))\n" + 
			"start with fuid =0 connect by fuid= prior id\n" + 
			" )\n" + 
			"  start with fuid=0\n" + 
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
