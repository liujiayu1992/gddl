function Pingb(){
	if(event.keyCode==13){
		event.keyCode=0;
     	event.returnValue=false;	
     }else if(event.keyCode==8){
			if(event.srcElement.tagName!="INPUT"){
				event.keyCode=0;
		     	event.returnValue=false;
			}else if(event.srcElement.type!= "text"){
				event.keyCode=0;
		     	event.returnValue=false;				
			}else if(event.srcElement.readOnly){
				event.keyCode=0;
		     	event.returnValue=false;
			}		     
     }
}