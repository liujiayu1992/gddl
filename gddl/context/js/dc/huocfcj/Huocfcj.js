var CommPort = "1";
var BaudRate = "4800";
var DataBits = "8";
var StopBits = "1";
var ParitySetting = "n";
var Jilhh = "A";
var DataMode = 1;
var isTest = 0;

//var CurrFile ;

function ResetMsCom(){
	if(isTest==0){
//	alert(CommPort + ","+ BaudRate + ","+ DataBits +"," + ParitySetting +"," + StopBits + "," + Jilhh + "," + DataMode);
		MSComm1.CommPort= CommPort;
		MSComm1.Settings=""+ BaudRate + ","+ ParitySetting +"," + DataBits +"," + StopBits + "";
		MSComm1.InputLen=0   ;   
		if(!MSComm1.PortOpen){
			MSComm1.PortOpen=true;
			document.getElementById("DTfont").style.color="yellow";
		}
		setTimeout('getData();',500);
	}
}
function setParam4Set(){
	var filePath = "D:/zhiren/huocfcj/cans/huocfcj_cans";
	setParam(filePath);
}
function setParam(filePath){
	var fso, fd, fc, f;
	fso = new ActiveXObject("Scripting.FileSystemObject");
	/*if(fso.FolderExists(filePath)){
		fd = fso.GetFolder(filePath);		
		fc = new Enumerator(fd.files);
		for (; !fc.atEnd(); fc.moveNext()){
			if(fso.FileExists(fc.item())){
				if(!isNaN(curfile)){
					if(fc.item() == curfile){
						continue;
					}
				}
				curfile = fc.item();
				f = fso.OpenTextFile(curfile, 1, false);	
			}	   					
		}
	}*/
	if(fso.FileExists(filePath)){
		f = fso.OpenTextFile(filePath, 1, false);
		var line = 0;
		while (!f.AtEndOfStream){
			switch (line++){
				case 0:CommPort = f.ReadLine().split("=")[1];break;
				case 1:BaudRate = f.ReadLine().split("=")[1];break;
				case 2:DataBits = f.ReadLine().split("=")[1];break;
				case 3:StopBits = f.ReadLine().split("=")[1];break;
				case 4:ParitySetting = f.ReadLine().split("=")[1];break;
				case 5:Jilhh = f.ReadLine().split("=")[1];break;
				case 6:DataMode = f.ReadLine().split("=")[1];break;
				case 7:isTest = f.ReadLine().split("=")[1];break;
				default : break;
			}
			if(line > 10)break;
		}
		f.Close();
	}
}

body.onload =function(){
	document.getElementById("dongtzl").value=0.0;
	document.getElementById("DTfont").style.color="red";
	document.getElementById("caijzl").value=0.0;
	setParam4Set();
	ResetMsCom();
}

function getData(){
	if(MSComm1.InBufferCount>7){
		var Instring = MSComm1.Input;
		var str = Instring;
		var Stemp="";
		var temp1="";
		var temp2="";
		var blen = -1;
		var zhongl = 0;
		switch(DataMode){
			case "1": str = Instring.substring(1, 7);
				zhongl = parseInt(str, 10);
				break;
			case "70": str = Instring.substring(0, 22);
				for(var i=0;i<=22;i++){
					if(str.substr(i,1)=='+'){
						temp2=str.substring(i+1,i+6);
						break;
					}
				}
				zhongl = parseInt(temp2,10);
				zhongl=eval(zhongl*10);
				break;
			case "10": str = Instring.substring(0, 32);
				for(var i=0;i<=32;i++){
					if(str.substr(i,1)==' '){
						temp2=str.substring(i+1,i+10);
						break;
					}
				}
				zhongl = parseInt(temp2,10);
				zhongl=eval(zhongl/1000);
				break;   		
			case "20": str = Instring.substring(1, 9);
				for(var i = 8;i>= 0;i--){
					Stemp = Stemp + str.substr(i,1);
				}
				zhongl=Stemp*1000;
				break;
			case "30": str = Instring.substring(3, 14);
				zhongl= parseInt(str ,10);
				break;
			case "40": str = Instring.substring(4, 10);
        		zhongl = parseInt(str,10);
        		break;
        	case "50": str = Instring.substring(4, 9);
        		zhongl = parseInt(str,10);
        		break;
			case "60": str = Instring.substring(0, 16);
				for(var i=0;i<=7;i++){
					if(str.substr(i,1)=='='){
						temp1=str.substring(i+1,i+6);
					}
				}
				for(var i =(temp1.length-1);i>= 0;i--){
					if(temp1.substr(i,1)==' '||temp1.substr(i,1)=='='){
						Stemp=Stemp+'0';
					}else 
						Stemp = Stemp + temp1.substr(i,1);
				}
				zhongl=parseInt(Stemp,10);
				zhongl=eval(zhongl);
				break;

			case "170": str = Instring.substring(3, 10);			
        		zhongl = parseInt(str,10);
        		break;

			case "190": Stemp = Instring.substring(0, 12);
//				alert(Stemp);
				blen = Stemp.indexOf("+");
				if(blen!=-1){
					str = Stemp.substring(blen, blen+7);
					zhongl = parseInt(str, 10);
//					alert(zhongl);
				}
				break;

			case "200" :str = Instring.substring(0, 8);
				if(str.indexOf(".")==0 && str.indexOf("=")==7){
				for(var i = 6;i>= 1;i--){
					Stemp = Stemp + str.substr(i,1);
				}
				zhongl=Stemp;
				}else{
					zhongl = 0;
				}
				break;
			default: break;
		}
		if(isNaN(zhongl)){
			zhongl = 0.0;
		}
		var zl = eval(zhongl/1000);
		document.getElementById("dongtzl").value = zl;
		
        var Lastzl = eval(document.all.item("Lastzl").value);
        if(zl > 0){
        	if(Lastzl == zl){
        		document.getElementById("DTfont").style.color = '#00FF00';
				document.all.item("CaijButton").disabled = false;
        	}else{
        		document.all.item("CaijButton").disabled = true;
				document.getElementById("DTfont").style.color = 'yellow';
				document.all.item("Lastzl").value = zl;
        	}
        }else{
    		document.all.item("CaijButton").disabled = false;
        	document.getElementById("DTfont").style.color = 'yellow';
        }
	}
	setTimeout('getData();',500);
}
function caij1(){
	if(isTest==2){
		document.all.item("caijzl").value = eval(Math.round(Math.random()*1000))/100+20;
		isTest = 1;
	}else
		if(isTest==1){
			document.all.item("caijzl").value = eval(Math.round(Math.random()*1000))/100+60;
			isTest = 2;
		}else
			document.all.item("caijzl").value = eval(document.all.item("dongtzl").value);
	
	if(Ext.isReady){
		var rec;
		if(DataIndex=="MAOZ"){
			if(Mode == "sel")
				rec = grid.getSelectionModel().getSelected();
			else
				rec = grid.getStore().getAt(0);
			rec.set("ZHONGCHH",Jilhh);
		}else
		if(DataIndex=="PIZ"){
			if(Mode == "sel")
				rec = gridp.getSelectionModel().getSelected();
			else
				rec = gridp.getStore().getAt(0);
			rec.set("QINGCHH",Jilhh);
		}
		rec.set(DataIndex,eval(document.all.item("caijzl").value));
	}
	return true;
}



function caij(Mode, DataIndex){
	if(isTest==2){
		document.all.item("caijzl").value = eval(Math.round(Math.random()*1000))/100+20;
		isTest = 1;
	}else
		if(isTest==1){
			document.all.item("caijzl").value = eval(Math.round(Math.random()*1000))/100+60;
			isTest = 2;
		}else
			document.all.item("caijzl").value = eval(document.all.item("dongtzl").value);
	
	if(Ext.isReady){
		var rec;
		var raw_line=grid.getStore().getCount()-1;
		if(Mode == "sel"){
			rec = grid.getSelectionModel().getSelected();
			if(rec == null){
				Ext.MessageBox.alert('提示信息','请选择一条记录进行操作!');
				return;
			}
		}else{
			if(raw_line<0){
				var pl = new gp_plant({CHEPH: '',MAOZ: '0',PIZ: '0',SUD:'0'});
				grid.getStore().insert(0,pl);
				raw_line=0;
			}
			rec = grid.getStore().getAt(raw_line);
		}
			
//			alert(ljz);
			var lianjz=ljz;
			var new_zl=eval(document.all.item("caijzl").value);
			if(new_zl>lianjz){//  maoz
				if(rec.get('MAOZ')>0 ){
					Ext.Msg.confirm('提示信息','您没有添加新纪录，需要覆盖当前记录吗?',function(btn){
						if(btn=='yes'){
							rec.set('MAOZ',new_zl);
							createTempFile(grid.getStore());
						}
						if(btn=='no'){
							var pl = new gp_plant({CHEPH: '',MAOZ: new_zl,PIZ: '0',SUD:'0'});
							grid.getStore().insert(raw_line+1,pl);
							createTempFile(grid.getStore());
						}
					});
				}else{
					rec.set('MAOZ',new_zl);
					createTempFile(grid.getStore());
				}
			}else{//piz
				
					if(rec.get('PIZ')>0 ){
					Ext.Msg.confirm('提示信息','您没有添加新纪录，需要覆盖当前记录吗?',function(btn){
						if(btn=='yes'){
							rec.set('PIZ',new_zl);
							createTempFile(grid.getStore());
						}
						if(btn=='no'){
							var pl = new gp_plant({CHEPH: '',MAOZ: '0',PIZ: new_zl,SUD:'0'});
							grid.getStore().insert(raw_line+1,pl);
							createTempFile(grid.getStore());
						}
					});
				}else{
					rec.set('PIZ',new_zl);
					createTempFile(grid.getStore());
				}
			}
			
	//		createTempFile(grid.getStore());
	//	rec.set(DataIndex,eval(document.all.item("caijzl").value));
		//if(DataIndex=="MAOZ"){
	//		rec.set("ZHONGCHH",Jilhh);
	//	}else
	//	if(DataIndex=="PIZ"){
	//		rec.set("QINGCHH",Jilhh);
	//	}
	}
	return true;
}

function operater_file(){
	var filename=document.all.File_name.value;
//	alert(filename);
	if(filename!=null && filename!=""){//说明文件保存失败
	var folderpath="D:/zhiren/huocfcj/temp";
	var filepath="D:/zhiren/huocfcj/data";
	var fso=new ActiveXObject("Scripting.FileSystemObject");
	
	if(!fso.FolderExists(folderpath)){
		fso.CreateFolder(folderpath);
	}
	
	document.all.File_name.value="";
	 var data_file_path=filepath+"/"+filename+"A";
	 //alert(data_file_path);
	 var f1=fso.GetFile(data_file_path);
	 f1.Move(folderpath+"/huocfcj_temp");
	
	}
	
}
function getTempData(filepath){

var s=" var data=[";
var fso = new ActiveXObject("Scripting.FileSystemObject"); 

if(fso.FileExists(filepath)){

var f=fso.openTextFile(filepath);

while(!f.AtEndOfStream){
var tem_s=f.ReadLine().split(',');
s+="['"+tem_s[3]+"','"+tem_s[0]+"','"+tem_s[1]+"','"+tem_s[2]+"'],";
}
f.Close();
s=s.substring(0,s.lastIndexOf(","));
}
s+='];';
//alert(s);
eval(s);
return data;
}


function InitGridData(){

operater_file();

var filepath="D:/zhiren/huocfcj/temp/huocfcj_temp";
return getTempData(filepath);
}


function createTempFile(gridDiv_ds)
{	
	if(gridDiv_ds.getCount()<=0){
		return ;
	}
	  
	var filepath="D:/zhiren/huocfcj/temp/huocfcj_temp";
	var folderpath="D:/zhiren/huocfcj/temp";
	var fso=new ActiveXObject("Scripting.FileSystemObject");
	
	if(!fso.FolderExists(folderpath)){
		fso.CreateFolder("D:/zhiren/huocfcj/temp");
	}
	var f=fso.CreateTextFile(filepath,true);
	 	 var rec=gridDiv_ds.getRange();
           for(var j=0;j<rec.length;j++){
           	
           	var s1=rec[j].get('CHEPH');
			var s2=rec[j].get('MAOZ');
			var s3=rec[j].get('PIZ');
			var s4=rec[j].get('SUD');
			
			if(s1=='') {s1=' ';}
			if(s2=='') {s2='0';}
			if(s3=='') {s3='0';}
			if(s4=='') {s4='0';}
		
           	var tem_s=s2+","+s3+","+s4+","+s1;
           	f.WriteLine(tem_s);
           }
	 	
	
	f.Close();
}

function copy_file(){
	var filepath="D:/zhiren/huocfcj/temp/huocfcj_temp";
	var folderpath="D:/zhiren/huocfcj/data";
	var fso=new ActiveXObject("Scripting.FileSystemObject");
	
	if(!fso.FolderExists(folderpath)){
		fso.CreateFolder(folderpath);
	}
	
	var date=new Date();
	var fileName=""+date.getYear()+((date.getMonth()+1)<10?('0'+(date.getMonth()+1)):(date.getMonth()+1))+(date.getDate()<10?('0'+date.getDate()):date.getDate())+(date.getHours()<10?('0'+date.getHours()):date.getHours())+(date.getMinutes()<10?('0'+date.getMinutes()):date.getMinutes())+(date.getSeconds()<10?('0'+date.getSeconds()):date.getSeconds());
	document.all.File_name.value=fileName;
	 var data_file_path=folderpath+"/"+fileName+"A";
	//alert(data_file_path);
	 var f1=fso.GetFile(filepath);
	 f1.Move(data_file_path);
	
	  
	
}
function grid_save(gridDiv_ds){
	var gridDivsave_history = '';
	var Mrcd = gridDiv_ds.getRange();
	
	for(i = 0; i< Mrcd.length; i++){
		var s1=Mrcd[i].get('CHEPH');
		var s2=Mrcd[i].get('MAOZ');
		var s3=Mrcd[i].get('PIZ');
		var s4=Mrcd[i].get('SUD');
		if(s1==null || s1=='') {s1=' ';return "false";}
		if(s2=='') {s2='0';}
		if(s3=='') {s3='0';}
		if(s4=='') {s4='0';}
	
	gridDivsave_history +=s1+','+s2+','+s3+','+s4;
	 
	 if(i!=Mrcd.length-1){
	 	gridDivsave_history+=";";
	 }
	
 
	}
	
	if(gridDivsave_history!=''){
		copy_file();
	}
	return gridDivsave_history;
	
}
/*
function createTempFile(gridDiv_data,gridDiv_ds,page_size)
{	alert(gridDiv_ds.getCount());
	if(gridDiv_ds.getCount()<=0){
		return ;
	}
	// var len=gridDiv_data.length;
	 var len=gridDiv_ds.getCount();
       var count;
       if(len%page_size!=0){
        count=parseInt(len/page_size)+1;
        }else{
          count=len/page_size;
        }
        
	var filepath="D:/zhiren/huocfcj/temp/huocfcj_temp";
	var fso=new ActiveXObject("Scripting.FileSystemObject");
	var f=fso.CreateTextFile(filepath,true);
	alert(count);
	 for(var i=0;i<count;i++){
	 //	gridDiv_ds.load({params:{start:i*page_size, limit:page_size}});
	 	 var rec=gridDiv_ds.getRange();alert(rec.length);
           for(var j=0;j<rec.length;j++){
           	var tem_s=rec[j].get('CHEPH')+","+rec[j].get('MAOZ')+","+rec[j].get('PIZ');alert(tem_s);
           	f.WriteLine(tem_s);
           }
	 	
	 }
	f.Close();alert('OKOKOK!');
}
*/

