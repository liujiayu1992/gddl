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
	var filePath = "D:/zhiren/shul/汽车衡参数设置";
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
		var temp4="";
		var blen = -1;
		var zhongl = 0;
//		alert(DataMode);
		switch(DataMode){
//			数据原型< +0265000>;
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
//        	数据原型<025000000000> 实际值为25.000
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

//			case "10": str = Instring.substring(0, 6);
//        		zhongl = parseInt(str,10);
//        		break;
//			数据原型<=005.5300> 实际值为35.5        		
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
//			数据原型<=00935  > 实际值为53.90
//			数据原型< =00935 > 实际值为53.90
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
//			国电邯郸电厂				
//			case "170": str = Instring.substring(3, 10);			
//        		zhongl = parseInt(str,10);
//        		break;
        	case "170": 
			str = Instring.substring(0, 20);
			for(var i=0;i<=20;i++){
			if(str.substr(i,1)==""){
				temp4 = str.substring(i+5,i+10);
				break;
				}
			}		
        		zhongl = parseInt(temp4,10);
        		break;
//			数据原型<+059580319> 实际值为59.580	
			case "190": Stemp = Instring.substring(0, 12);
//				alert(Stemp);
				blen = Stemp.indexOf("+");
				if(blen!=-1){
					str = Stemp.substring(blen, blen+7);
					zhongl = parseInt(str, 10);
//					alert(zhongl);
				}
				break;
//			广西合川电厂<.025600=  .084600=>				
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
			//大唐鲁北发电
			case "230": str = Instring.substring(0, 30);
				for (var i = 0; i <= 32; i++) {
					if (str.substr(i, 1) == ' ') {
						temp2 = str.substring(i + 1, i + 7);
						break;
					}
				}
				zhongl = parseInt(temp2, 10);
				zhongl = eval(zhongl);
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
function caij(Mode, DataIndex){
//	设置汽车衡测试参数
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
		if(Mode == "sel"){
			rec = grid.getSelectionModel().getSelected();
			if(rec == null){
				Ext.MessageBox.alert('提示信息','请选中采集皮重的车皮');
				return;
			}
		}else
			rec = grid.getStore().getAt(0);
		rec.set(DataIndex,eval(document.all.item("caijzl").value));
		if(DataIndex=="MAOZ"){
			rec.set("ZHONGCH",Jilhh);
		}else
		if(DataIndex=="PIZ"){
			rec.set("QINGCH",Jilhh);
		}
	}
	return true;
}
//检毛检皮在一个页面的采集按钮触发事件
function caij1(){
//	设置汽车衡测试参数
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
			rec.set("ZHONGCH",Jilhh);
		}else
		if(DataIndex=="PIZ"){
			if(Mode == "sel")
				rec = gridp.getSelectionModel().getSelected();
			else
				rec = gridp.getStore().getAt(0);
			rec.set("QINGCH",Jilhh);
		}
		
		if(DataIndex==null || DataIndex==''){Ext.Msg.alert('提示信息','请选择记录进行操作!');return;}
		rec.set(DataIndex,eval(document.all.item("caijzl").value));
		
		
		if(jingzXt<0){
				
				if(DataIndex=='PIZ'){ var jin_va=(  parseFloat(rec.get('MAOZ'))-parseFloat(rec.get('PIZ')) )+'';
		//		if(jin_va.lastIndexOf(".")>=0){ jin_va=jin_va.substring(0,jin_va.lastIndexOf("."))+jin_va.substr(jin_va.lastIndexOf("."),2);}
		
			rec.set('JINGZ',jin_va);}
				
		}else{
		
				if(DataIndex=='PIZ'){ var jin_va=(  parseFloat(rec.get('MAOZ'))-parseFloat(rec.get('PIZ')) )+'';
										var  koud_va="0";
										var s="0.";
										var km="";
										jin_va=jin_va.substring(0,jin_va.lastIndexOf("."))+jin_va.substr(jin_va.lastIndexOf("."),jingzXt+5);
				
				if(jin_va.lastIndexOf(".")>=0){
//						alert(jin_va.substring(jin_va.lastIndexOf(".")+1)+'---'+jingzXt);
						
						
									koud_va="0.";
									if( parseFloat(rec.get('MAOZ')) <  parseFloat(rec.get('PIZ'))){
										koud_va="-0.";
										s="-0.";
										km="-";//alert(km);
									}
									
							if(jin_va.substring(jin_va.lastIndexOf(".")+1).length>jingzXt){//尾数足够，要保留尾数到koud
								
									
									for(var i=0;i<jingzXt;i++){
										koud_va+="0";
										
										if(i!=jingzXt-1)
										s+="0";
									}
									
									var num_tem=Math.ceil(parseFloat("0."+jin_va.substr(jin_va.lastIndexOf(".")+1+jingzXt+1,2)));
									var k_t=parseFloat(jin_va.substr(jin_va.lastIndexOf(".")+1+jingzXt,1))+num_tem;
//									alert(k_t);
									if(k_t>9){ s+="1"; koud_va="0"; rec.set('QITZ',"0");}
									else{koud_va+=k_t;rec.set('QITZ',koud_va); }
									
								 
						}else{ s="0"; }
//						alert(parseFloat(jin_va.substring(0,jin_va.lastIndexOf(".")))+'---'+km+'---'+parseFloat(km+"0"+jin_va.substr(jin_va.lastIndexOf("."),jingzXt+1))+'---'+parseFloat(s));
					 jin_va=parseFloat(jin_va.substring(0,jin_va.lastIndexOf(".")))+parseFloat(km+"0"+jin_va.substr(jin_va.lastIndexOf("."),jingzXt+1))+parseFloat(s)+"";
					 
					 if( jin_va.lastIndexOf(".")!=-1 && jin_va.substring(jin_va.lastIndexOf(".")).length>jingzXt+1){ var kl="0.";for(var i=0;i<jingzXt;i++){kl+="0";} jin_va=parseFloat(jin_va.substring(0,jin_va.lastIndexOf(".")+jingzXt+1))+Math.ceil(parseFloat(kl+jin_va.substr(jin_va.lastIndexOf(".")+jingzXt+1,2))); }
					 }else{rec.set('QITZ',"0"); }
				
					
				rec.set('JINGZ',jin_va);
				if(koud_va=='0.' || koud_va=='-0.'){koud_va="0";}
				rec.set('QITZ',koud_va); 
				}
		}
	
	
	
	
	}
	return true;
}