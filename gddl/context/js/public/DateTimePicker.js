//
// calendar -- a javascript date picker designed for easy localization.
//
// $Id: DateTimePicker.js,v 1.1.1.1 2014/05/12 05:30:27 xiaz Exp $
//
//
// Author: Per Norrman (pernorrman@telia.com)
// 
// Based on Tapestry 2.3-beta1 Datepicker by Paul Geerts
// 
// Thanks to:
//     Vladimir [vyc@quorus-ms.ru] for fixing the IE6 zIndex problem.
//
// The normal setup would be to have one text field for displaying the 
// selected date, and one button to show/hide the date picker control.
// This is  the recommended javascript code:
// 
//	<script language="javascript">
//		var cal;
//
//		function init() {
//			cal = new TimeCalendar();
//			cal.setIncludeWeek(true);
//			cal.setFormat("yyyy-MM-dd");
//			cal.setMonthNames(.....);
//			cal.setShortMonthNames(....);
//			cal.create();
//			
//			document.form.button1.onclick = function() {
//				cal.toggle(document.form.button1);
//			}
//			cal.onchange = function() {
//				document.form.textfield1.value  = cal.formatDate();
//			}
//		}
//	</script>
//
// The init function is invoked when the body is loaded.
//
//

function TimeCalendar(date) {
	if (arguments.length == 0) {
		this._currentDate = new Date();
		this._selectedDate = null;
	}
	else {
		this._currentDate = new Date(date);
		this._selectedDate = new Date(date);
	}

	// Accumulated days per month, for normal and for leap years.
	// Used in week number calculations.	
    TimeCalendar.NUM_DAYS = [0,31,59,90,120,151,181,212,243,273,304,334];
    
    TimeCalendar.LEAP_NUM_DAYS = [0,31,60,91,121,152,182,213,244,274,305,335];
    

	this._bw = new bw_check();
	this._showing = false;	
	this._includeWeek = false;
	this._hideOnSelect = true;
	this._alwaysVisible = false;
	
	this._dateSlot = new Array(42);
	this._weekSlot = new Array(6);
	
	this._firstDayOfWeek = 1;
	this._minimalDaysInFirstWeek = 4;
	
	this._monthNames = [	
		"January",		"February",		"March",	"April",
		"May",			"June",			"July",		"August",
		"September",	"October",		"November",	"December"
	];
	
	this._shortMonthNames = [ 
		"jan", "feb", "mar", "apr", "may", "jun", 
		"jul", "aug", "sep", "oct", "nov", "dec"
	];
	
	// Week days start with Sunday=0, ... Saturday=6
	this._weekDayNames = [
		"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" 
	];
	
	this._shortWeekDayNames = 
		["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" ];
	
	this._defaultFormat = "yyyy-MM-dd hh:mi:ss";
	
	this._format = this._defaultFormat; 

	this._calDiv = null;
	
	
}

/**
 *	CREATE the TimeCalendar DOM element
 */
TimeCalendar.prototype.create = function() {
	var div;
	var table;
	var tbody;
	var tr;
	var td;
	var dp = this;
	
	// Create the top-level div element
	this._calDiv = document.createElement("div");
	//this._calDiv.className = "outTb";
	this._calDiv.style.width="206";
	this._calDiv.style.position = "absolute";
	this._calDiv.style.display = "none";
	this._calDiv.id="DateSelectDiv";
	this._calDiv.style.textAlign = "center";
	this._calDiv.style.background = "#e4e4e4";
	this._calDiv.style.zIndex = "400";
	this._calDiv.style.borderBottom="#716F64 1px solid";
//	this._calDiv.style.paddingTop="1px";
//	this._calDiv.style.paddingBottom="1px";
//	this._calDiv.style.paddingLeft="1px";
//	this._calDiv.style.paddingRight="1px";
	this._calDiv.style.borderLeft="#ECE9D8 1px solid";

	this._calDiv.style.borderRight="#716F64 2px solid";//ridge";

	this._calDiv.style.borderTop="#ECE9D8 1px solid";// outset";
	// header div
	div = document.createElement("div");
	//div.className = "ITb";
	div.style.borderBottom="blue 0px solid";
	div.style.borderLeft="#808080 0px solid";
	div.style.paddingLeft="1px";
	div.style.borderRight="#808080 0px solid";

	div.style.borderTop="#808080 0px solid";

	this._calDiv.appendChild(div);
	
	table = document.createElement("table");
	table.style.height="20";

	table.style.cellSpacing = "1";
	table.style.border= "0";
	table.style.cellPadding= "0";	
	table.style.width= "100%";	
	table.style.borderBottom="#808080 1px solid";
	table.style.borderLeft="#808080 1px solid";
	table.style.borderTop="#808080 1px solid";
	table.style.borderRight="#808080 1px solid";


	div.appendChild(table);
	
	tbody = document.createElement("tbody");
	table.appendChild(tbody);
	
	tr = document.createElement("tr");
	tbody.appendChild(tr);
	
	// Previous Month Button
	td = document.createElement("td");
	this._previousMonth = document.createElement("button");
	this._previousMonth.className = "prevMonthButton"
	this._previousMonth.appendChild(document.createTextNode("<<"));
	//this._previousMonth.appendChild(document.createTextNode(String.fromCharCode(9668)));
	td.appendChild(this._previousMonth);
	tr.appendChild(td);
	
		// 
	// Create the year drop down
	//
	td = document.createElement("td");
	td.className = "labelContainer";
	tr.appendChild(td);
	this._yearSelect = document.createElement("select");
	for(var i=1920; i < 2050; ++i) {
		var opt = document.createElement("option");
		opt.innerHTML = i;
		opt.value = i;
		if (i == this._currentDate.getFullYear()) {
			opt.selected = false;
		}
		this._yearSelect.appendChild(opt);
	}
	td.appendChild(this._yearSelect);
	
	
	td = document.createElement("td");
	
	//
	// Create the month drop down 
	//
	td = document.createElement("td");
	td.className = "labelContainer";
	tr.appendChild(td);
	this._monthSelect = document.createElement("select");
    for (var i = 0 ; i < this._monthNames.length ; i++) {
        var opt = document.createElement("option");
        opt.innerHTML = this._monthNames[i];
        opt.value = i;
        if (i == this._currentDate.getMonth()) {
            opt.selected = true;
        }
        this._monthSelect.appendChild(opt);
    }
	td.appendChild(this._monthSelect);
	


	this._nextMonth = document.createElement("button");
	this._nextMonth.appendChild(document.createTextNode(">>"));
	//this._nextMonth.appendChild(document.createTextNode(String.fromCharCode(9654)));
	this._nextMonth.className = "nextMonthButton";
	td.appendChild(this._nextMonth);
	tr.appendChild(td);
	
	// TimeCalendar body
	div = document.createElement("div");
	div.className = "calendarBody";
	this._calDiv.appendChild(div);
	this._table = div;
	
	// Create the inside of calendar body	
	
	var text;
	table = document.createElement("table");
	table.style.width			="204";
	table.style.height			="146";
	table.className 			= "date";
	table.style.font 	 		= "small-caption";
	table.style.fontWeight 		= "normal";
	table.style.textAalign		= "center";
	table.style.cursor			= "default";
	table.style.backgroundColor			="#c0c0c0";
	table.cellSpacing			= "1";
	table.style.border			= "0";
	//BORDER-RIGHT: #c0c0c0 1px solid;
	table.style.borderBottom="#c0c0c0 1px solid";
	table.style.borderLeft="#c0c0c0 1px solid";
	table.style.borderTop="#c0c0c0 1px solid";
	table.style.borderRight="#c0c0c0 1px solid";

//	table.bordercolordark		="#ffffff";
	table.cellpadding			="0";
//	table.bordercolor			="#ffffff";
//	table.bordercolorlight		="#AFB6C7";
		
    div.appendChild(table);
	var thead = document.createElement("thead");
	table.appendChild(thead);
	tr = document.createElement("tr");
	tr.style.backgroundColor			="#ffffff";
	thead.appendChild(tr);
	thead.cellSpacing		= "0";
	// weekdays header
	if (this._includeWeek) {
		td = document.createElement("th");
		text = document.createTextNode("w");
		td.appendChild(text);
//		td.className = "weekNumberHead";
		td.style.textAlign = "left";
		tr.appendChild(td);
	}
	for(i=0; i < 7; ++i) {
		td = document.createElement("th");
		text = document.createTextNode(this._shortWeekDayNames[(i+this._firstDayOfWeek)%7]);
		td.appendChild(text);
		td.className = "datehead";
		td.style.borderBottom = "1px solid #c0c0c0";
		tr.appendChild(td);
	}
	
	tbody = document.createElement("tbody");
	table.appendChild(tbody);
	for(week=0; week<6; ++week) {
		tr = document.createElement("tr");
		
		tr.style.backgroundColor="#ffffcc";

		//tr.bgcolor="#ffffcc";
		tbody.appendChild(tr);
		if (this._includeWeek) {
			td = document.createElement("td");
			td.style.fontWeight = "normal";
			td.className = "weekNumberHead";
			td.style.textAlign = "left";
			td.style.borderBottom = "1px solid";
			text = document.createTextNode(String.fromCharCode(160));
			td.appendChild(text);
           	setCursor(td);
            td.align="center";
            td.className = "weekNumberHead";
			tr.appendChild(td);
			var tmp = new Object();
			tmp.tag = "WEEK";
			tmp.value = -1;
			tmp.data = text;
			this._weekSlot[week] = tmp;
		}

		for(day=0; day<7; ++day) {
			td = document.createElement("td");
			
//			td.style.backgroundColor ="#ffffcc";
			td.style.borderBottom = "1px solid";
			text = document.createTextNode(String.fromCharCode(160));
			
			td.appendChild(text);
            setCursor(td);
            td.align="center";
            td.style.fontWeight="normal";
			tr.appendChild(td);
			var tmp = new Object();
			tmp.tag = "DATE";
			tmp.value = -1;
			tmp.data = text;
			this._dateSlot[(week*7)+day] = tmp;
			
		}
	}
	
	// TimeCalendar Footer
	div = document.createElement("div");
	this._calDiv.appendChild(div);
	
	table = document.createElement("table");
	table.style.width="100%";
	table.style.height="21";
	table.className="todayimg";
	table.cellSpacing = 0;
	div.appendChild(table);
	
	tbody = document.createElement("tbody");
	
	table.appendChild(tbody);
	
	tr = document.createElement("tr");
	tbody.appendChild(tr);
	
	
	td = document.createElement("td");
	td.className = "labelContainer";
	tr.appendChild(td);
	this._HourSelect = document.createElement("select");
	for(var i=0; i < 24; ++i) {
		var opt = document.createElement("option");
		opt.innerHTML = i;
		opt.value = i;
		if (i == this._currentDate.getHours()) {
			opt.selected = false;
		}
		this._HourSelect.appendChild(opt);
	}
	td.appendChild(this._HourSelect);
	td.appendChild(document.createTextNode(" ʱ"));
	
	td = document.createElement("td");
	td.className = "labelContainer";
	tr.appendChild(td);
	this._MinuteSelect = document.createElement("select");
	for(var i=0; i < 60; ++i) {
		var opt = document.createElement("option");
		opt.innerHTML = i;
		opt.value = i;
		if (i == this._currentDate.getMinutes()) {
			opt.selected = false;
		}
		this._MinuteSelect.appendChild(opt);
	}
	td.appendChild(this._MinuteSelect);
	td.appendChild(document.createTextNode(" ��"));
	
	td = document.createElement("td");
	td.className = "labelContainer";
	tr.appendChild(td);
	this._SecondSelect = document.createElement("select");
	for(var i=0; i < 60; ++i) {
		var opt = document.createElement("option");
		opt.innerHTML = i;
		opt.value = i;
		if (i == this._currentDate.getSeconds()) {
			opt.selected = false;
		}
		this._SecondSelect.appendChild(opt);
	}
	td.appendChild(this._SecondSelect);
	td.appendChild(document.createTextNode(" ��"));
/*
	td = document.createElement("td");

	var today = new Date();

	td.style.textAlign="center";
	td.appendChild(document.createTextNode("????" + today.getFullYear() + "?" +(today.getMonth() +1) + "?"  +  today.getDate() +"?"));

*/	
	tr.appendChild(td);

	
	
	this._update();
	this._updateHeader();
	


	// IE55+ extension		
	this._previousMonth.hideFocus = true;
	this._nextMonth.hideFocus = true;

	this._previousMonth.onclick = function () {
		dp.prevMonth();
	};

	this._nextMonth.onclick = function () {
		dp.nextMonth();
	};


	
	this._calDiv.onselectstart = function () {
		return false;
	};

	this._table.onclick = function (e) {
		// find event
		if (e == null) e = document.parentWindow.event;
		
		// find td
		var el = e.target != null ? e.target : e.srcElement;
		while (el.nodeType != 1)
			el = el.parentNode;
		while (el != null && el.tagName && el.tagName.toLowerCase() != "td")
			el = el.parentNode;
		
		// if no td found, return
		if (el == null || el.tagName == null || el.tagName.toLowerCase() != "td")
			return;
		
		var d = new Date(dp._currentDate);
		var n = Number(el.firstChild.data);
		if (isNaN(n) || n <= 0 || n == null)
			return;
		
		if (el.className == "date")
			return;
			
		d.setDate(n);
		dp.setSelectedDate(d);

		if (!dp._alwaysVisible && dp._hideOnSelect) {
			dp.hide();
		}
		
	};
	
	
	this._calDiv.onkeydown = function (e) {
		if (e == null) e = document.parentWindow.event;
		var kc = e.keyCode != null ? e.keyCode : e.charCode;

		if(kc == 13) {
			var d = new Date(dp._currentDate).valueOf();
			dp.setSelectedDate(d);

			if (!dp._alwaysVisible && dp._hideOnSelect) {
				dp.hide();
			}
			return false;
		}
			
		
		if (kc < 37 || kc > 40) return true;
		
		var d = new Date(dp._currentDate).valueOf();
		if (kc == 37) // left
			d -= 24 * 60 * 60 * 1000;
		else if (kc == 39) // right
			d += 24 * 60 * 60 * 1000;
		else if (kc == 38) // up
			d -= 7 * 24 * 60 * 60 * 1000;
		else if (kc == 40) // down
			d += 7 * 24 * 60 * 60 * 1000;

		dp.setCurrentDate(new Date(d));
		return false;
	}
	
	// ie6 extension
	this._calDiv.onmousewheel = function (e) {
		if (e == null) e = document.parentWindow.event;
		var n = - e.wheelDelta / 120;
		var d = new Date(dp._currentDate);
		var m = d.getMonth() + n;
		d.setMonth(m);
		
		
		dp.setCurrentDate(d);
		
		return false;
	}

	this._monthSelect.onchange = function(e) {
		if (e == null) e = document.parentWindow.event;
		e = getEventObject(e);
		dp.setMonth(e.value);
	}

	this._monthSelect.onclick = function(e) {
		if (e == null) e = document.parentWindow.event;
		e = getEventObject(e);
		e.cancelBubble = true;
	}
	
	this._yearSelect.onchange = function(e) {
		if (e == null) e = document.parentWindow.event;
		e = getEventObject(e);
		dp.setYear(e.value);
	}
	
	this._HourSelect.onchange = function(e) {
		if (e == null) e = document.parentWindow.event;
		e = getEventObject(e);
		dp.setHour(e.value);
	}
	
	this._MinuteSelect.onchange = function(e) {
		if (e == null) e = document.parentWindow.event;
		e = getEventObject(e);
		dp.setMinute(e.value);
	}
	
	this._SecondSelect.onchange = function(e) {
		if (e == null) e = document.parentWindow.event;
		e = getEventObject(e);
		dp.setSecond(e.value);
	}


	document.body.appendChild(this._calDiv);
	
	
	return this._calDiv;
}

TimeCalendar.prototype._update = function() {


	// Calculate the number of days in the month for the selected date
	var date = this._currentDate;
	var today = toISODate(new Date());
	
	
	var selected = "";
	if (this._selectedDate != null) {
		selected = toISODate(this._selectedDate);
	}
	var current = toISODate(this._currentDate);
	var d1 = new Date(date.getFullYear(), date.getMonth(), 1);
	var d2 = new Date(date.getFullYear(), date.getMonth()+1, 1);
	var monthLength = Math.round((d2 - d1) / (24 * 60 * 60 * 1000));
	
	// Find out the weekDay index for the first of this month
	var firstIndex = (d1.getDay() - this._firstDayOfWeek) % 7 ;
    if (firstIndex < 0) {
    	firstIndex += 7;
    }
	
	var index = 0;
	while(index < 42) {
		this._dateSlot[index].data.parentNode.style.backgroundColor="#ffffcc";
		index++;
	}
	index = 0;
	while (index < firstIndex) {
		this._dateSlot[index].value = -1;
		this._dateSlot[index].data.data = String.fromCharCode(160);
		this._dateSlot[index].data.parentNode.className = "";
		this._dateSlot[index].data.parentNode.style.fontWeight = "normal";
		this._dateSlot[index].data.parentNode.style.border= "none";		
		this._dateSlot[index].data.parentNode.style.backgroundColor="#ffffff";
		index++;
	}

    for (i = 1; i <= monthLength; i++, index++) {
		this._dateSlot[index].value = i;
		this._dateSlot[index].data.data = i;
		this._dateSlot[index].data.parentNode.className = "";
		this._dateSlot[index].data.parentNode.style.fontWeight = "normal";
		this._dateSlot[index].data.parentNode.style.border= "none";			
		this._dateSlot[index].data.parentNode.backgroundColor="#ffffcc";
		if (toISODate(d1) == today) {
			this._dateSlot[index].data.parentNode.className = "datetoday";
//			this._dateSlot[index].data.parentNode.style.fontWeight = "bold";
			this._dateSlot[index].data.parentNode.style.fontColor = "blue";
			this._dateSlot[index].data.parentNode.bordercolor="#009933";
			this._dateSlot[index].data.parentNode.backgroundColor="#ffffcc";
		}
		if (toISODate(d1) == current) {
			this._dateSlot[index].data.parentNode.className += " current";
			this._dateSlot[index].data.parentNode.style.border= "1px dotted blue";
			this._dateSlot[index].data.parentNode.backgroundColor="#ffffcc";
		}
		if (toISODate(d1) == selected) {
			this._dateSlot[index].data.parentNode.className = "dateselected";
			this._dateSlot[index].data.parentNode.style.border= "1px solid blue";
			this._dateSlot[index].data.parentNode.bordercolor="#009933";
			this._dateSlot[index].data.parentNode.backgroundColor="#ffffcc";
		}
		d1 = new Date(d1.getFullYear(), d1.getMonth(), d1.getDate()+1);
	}
	
	var lastDateIndex = index;
        
    while(index < 42) {
		this._dateSlot[index].value = -1;
		this._dateSlot[index].data.data = String.fromCharCode(160);
		this._dateSlot[index].data.parentNode.className = "";
		this._dateSlot[index].data.parentNode.style.fontWeight = "normal";
		this._dateSlot[index].data.parentNode.style.border= "none";		
		this._dateSlot[index].data.parentNode.style.backgroundColor="#ffffff";
		++index;
	}
	
	// Week numbers
	if (this._includeWeek) {
		d1 = new Date(date.getFullYear(), date.getMonth(), 1);
		for (i=0; i < 6; ++i) {
			if (i == 5 && lastDateIndex < 36) {
				this._weekSlot[i].data.data = String.fromCharCode(160);
				this._weekSlot[i].data.parentNode.style.borderRight = "none";
			} else {
				week = weekNumber(this, d1);
				this._weekSlot[i].data.data = week;
				this._weekSlot[i].data.parentNode.style.borderRight = "1px solid WindowText";
			}
			d1 = new Date(d1.getFullYear(), d1.getMonth(), d1.getDate()+7);
		}
	}
}

TimeCalendar.prototype.show = function(element) {
	if(!this._showing) {
		var p = getPoint(element);
//		alert(p.y + "-----" + p.iph + "----" +element.offsetHeight );
		this._calDiv.style.display = "block";
		if ((p.y + element.offsetHeight + 189) > p.iph){
			this._calDiv.style.top = (p.y -185 - element.offsetHeight ) + "px";
		}else{
			this._calDiv.style.top = (p.y + element.offsetHeight + 1) + "px";
		}
		this._calDiv.style.left = p.x + "px";
		this._showing = true;
		
		/* -------- */
	   	if( this._bw.ie6 )
	   	{
	     	dw = this._calDiv.offsetWidth;
	     	dh = this._calDiv.offsetHeight;
	     	var els = document.getElementsByTagName("body");
	     	var body = els[0];
	     	if( !body ) return;
	 
	    	//paste iframe under the modal
		     var underDiv = this._calDiv.cloneNode(false); 
		     underDiv.style.zIndex="390";
		     underDiv.style.margin = "0px";
		     underDiv.style.padding = "0px";
		     underDiv.style.display = "block";
		     underDiv.style.width = dw;
		     underDiv.style.height = dh;
		     underDiv.style.border = "1px solid WindowText";
		     underDiv.innerHTML = "<iframe width=\"100%\" height=\"100%\" frameborder=\"0\"></iframe>";
		     body.appendChild(underDiv);
		     this._underDiv = underDiv;
	   }
		/* -------- */
		this._calDiv.focus();
		
	}
};

TimeCalendar.prototype.hide = function() {   
	if(this._showing) {
		this._calDiv.style.display = "none";
		this._showing = false;
		if( this._bw.ie6 ) {
		    if( this._underDiv ) this._underDiv.removeNode(true);
		}
	}
}

TimeCalendar.prototype.toggle = function(element) {
	if(window.event.srcElement==element){
		this.hide();
	}
	if(this._showing) {
		this.hide(); 
	} else {
		this.show(element);
	}
	
}



TimeCalendar.prototype.onchange = function() {};


TimeCalendar.prototype.setCurrentDate = function(date) {
	if (date == null) {
		return;
	}

	// if string or number create a Date object
	if (typeof date == "string" || typeof date == "number") {
		date = new Date(date);
	}
	
	
	// do not update if not really changed
	if (this._currentDate.getDate() != date.getDate() ||
		this._currentDate.getMonth() != date.getMonth() || 
		this._currentDate.getFullYear() != date.getFullYear()||
		this._currentDate.getHours() != date.getHours()||
		this._currentDate.getMinutes() != date.getMinutes()||
		this._currentDate.getSeconds() != date.getSeconds()) {
		this._currentDate = new Date(date);
		this._updateHeader();
		this._update();
	}
	
}

TimeCalendar.prototype.setSelectedDate = function(date) {
	this._selectedDate = new Date(date);
	this.setCurrentDate(this._selectedDate);
	if (typeof this.onchange == "function") {
		this.onchange();
	}
}

TimeCalendar.prototype.clearSelectedDate = function() {
	this._selectedDate = null;
	if (typeof this.onchange == "function") {
		this.onchange();
	}
}

TimeCalendar.prototype.getElement = function() {
	return this._calDiv;
}

TimeCalendar.prototype.setIncludeWeek = function(v) {
	if (this._calDiv == null) {
		this._includeWeek = v;
	}
}

TimeCalendar.prototype.getSelectedDate = function () {
	if (this._selectedDate == null) {
		return null;
	} else {
		return new Date(this._selectedDate);
	}
}



TimeCalendar.prototype._updateHeader = function () {

	// 
	var options = this._monthSelect.options;
	var m = this._currentDate.getMonth();
	for(var i=0; i < options.length; ++i) {
		options[i].selected = false;
		if (options[i].value == m) {
			options[i].selected = true;
		}
	}
	
	options = this._yearSelect.options;
	var year = this._currentDate.getFullYear();
	for(var i=0; i < options.length; ++i) {
		options[i].selected = false;
		if (options[i].value == year) {
			options[i].selected = true;
		}
	}
	
	options = this._HourSelect.options;
	var year = this._currentDate.getHours();
	for(var i=0; i < options.length; ++i) {
		options[i].selected = false;
		if (options[i].value == year) {
			options[i].selected = true;
		}
	}
	
	options = this._MinuteSelect.options;
	var year = this._currentDate.getMinutes();
	for(var i=0; i < options.length; ++i) {
		options[i].selected = false;
		if (options[i].value == year) {
			options[i].selected = true;
		}
	}
	
	options = this._SecondSelect.options;
	var year = this._currentDate.getSeconds();
	for(var i=0; i < options.length; ++i) {
		options[i].selected = false;
		if (options[i].value == year) {
			options[i].selected = true;
		}
	}
	
}

TimeCalendar.prototype.setYear = function(year) {
	var d = new Date(this._currentDate);
	d.setFullYear(year);
	this.setCurrentDate(d);
}

TimeCalendar.prototype.setMonth = function (month) {
	var d = new Date(this._currentDate);
	d.setMonth(month);
	this.setCurrentDate(d);
}

TimeCalendar.prototype.nextMonth = function () {
	this.setMonth(this._currentDate.getMonth()+1);
}

TimeCalendar.prototype.prevMonth = function () {
	this.setMonth(this._currentDate.getMonth()-1);
}

TimeCalendar.prototype.setFirstDayOfWeek = function (nFirstWeekDay) {
	this._firstDayOfWeek = nFirstWeekDay;
}

TimeCalendar.prototype.getFirstDayOfWeek = function () {
	return this._firstDayOfWeek;
}

TimeCalendar.prototype.setMinimalDaysInFirstWeek = function(n) {
	this._minimalDaysInFirstWeek = n;
}


TimeCalendar.prototype.getMinimalDaysInFirstWeek = function () {
	return this._minimalDaysInFirstWeek;
}

TimeCalendar.prototype.setMonthNames = function(a) {
	// sanity test
	this._monthNames = a;
}

TimeCalendar.prototype.setShortMonthNames = function(a) {
	// sanity test
	this._shortMonthNames = a;
}

TimeCalendar.prototype.setWeekDayNames = function(a) {
	// sanity test
	this._weekDayNames = a;
}

TimeCalendar.prototype.setShortWeekDayNames = function(a) {
	// sanity test
	this._shortWeekDayNames = a;
}

TimeCalendar.prototype.getFormat = function() {
	return this._format;
}
	
TimeCalendar.prototype.setFormat = function(f) {
	this._format = f;
}

TimeCalendar.prototype.setHour = function(hour) {
	var d = new Date(this._currentDate);
	d.setHours(hour);
	this.setCurrentDate(d);
}

TimeCalendar.prototype.setMinute = function(Minute) {
	var d = new Date(this._currentDate);
	d.setMinutes(Minute);
	this.setCurrentDate(d);
}

TimeCalendar.prototype.setSecond = function(Second) {
	var d = new Date(this._currentDate);
	d.setSeconds(Second);
	this.setCurrentDate(d);
}
TimeCalendar.prototype.formatDate = function() {  
	if (this._selectedDate == null) {
		return "";
	}
    var bits = new Array();
    // work out what each bit should be
    var date = this._selectedDate;
    bits['d'] = date.getDate();
    bits['dd'] = pad(date.getDate(),2);
    bits['ddd'] = this._shortWeekDayNames[date.getDay()];
    bits['dddd'] = this._weekDayNames[date.getDay()];

    bits['M'] = date.getMonth()+1;
    bits['MM'] = pad(date.getMonth()+1,2);
    bits['MMM'] = this._shortMonthNames[date.getMonth()];
    bits['MMMM'] = this._monthNames[date.getMonth()];
    
    var yearStr = "" + date.getFullYear();
    yearStr = (yearStr.length == 2) ? '19' + yearStr: yearStr;
    bits['yyyy'] = yearStr;
    bits['yy'] = bits['yyyy'].toString().substr(2,2);

    // do some funky regexs to replace the format string
    // with the real values
    var frm = new String(this._format);
    var sect;
    for (sect in bits) {
      frm = eval("frm.replace(/\\b" + sect + "\\b/,'" + bits[sect] + "');");
    }
    //frm = frm + " 12:12:12";
    return date.getFullYear()+"-"+pad(date.getMonth()+1,2)
    +"-"+pad(date.getDate(),2)+" "+pad(date.getHours(),2)+":"
    +pad(date.getMinutes(),2)+":"+pad(date.getSeconds(),2);
    return frm;
}
	
                                                                                                       
function isLeapYear(year) {
	return ((year%4 == 0) && ((year%100 != 0) || (year%400 == 0)));
}

function yearLength(year) {
	if (isLeapYear(year))
		return 366;
	else
		return 365;
}

function dayOfYear(date) {
	var a = TimeCalendar.NUM_DAYS;
	if (isLeapYear(date.getFullYear())) {
		a = TimeCalendar.LEAP_NUM_DAYS;
	}
	var month = date.getMonth();
	
	return a[month] + date.getDate();
}

// ---------------------------------------------
// Week number stuff
// ---------------------------------------------

function weekNumber(cal, date) {

	var dow = date.getDay();
	var doy = dayOfYear(date);
	var year = date.getFullYear();

	// Compute the week of the year.  Valid week numbers run from 1 to 52
	// or 53, depending on the year, the first day of the week, and the
	// minimal days in the first week.  Days at the start of the year may
	// fall into the last week of the previous year; days at the end of
	// the year may fall into the first week of the next year.
	var relDow = (dow + 7 - cal.getFirstDayOfWeek()) % 7; // 0..6
	var relDowJan1 = (dow - doy + 701 - cal.getFirstDayOfWeek()) % 7; // 0..6
	var week = Math.floor((doy - 1 + relDowJan1) / 7); // 0..53
	if ((7 - relDowJan1) >= cal.getMinimalDaysInFirstWeek()) {
		++week;
	}

	if (doy > 359) { // Fast check which eliminates most cases
		// Check to see if we are in the last week; if so, we need
		// to handle the case in which we are the first week of the
		// next year.
		var lastDoy = yearLength(year);
		var lastRelDow = (relDow + lastDoy - doy) % 7;
		if (lastRelDow < 0) {
			lastRelDow += 7;
		}
		if (((6 - lastRelDow) >= cal.getMinimalDaysInFirstWeek())
			&& ((doy + 7 - relDow) > lastDoy)) {
			week = 1;
		}
	} else if (week == 0) {
		// We are the last week of the previous year.
		var prevDoy = doy + yearLength(year - 1);
		week = weekOfPeriod(cal, prevDoy, dow);
	}

	return week;
}

function weekOfPeriod(cal, dayOfPeriod, dayOfWeek) {
	// Determine the day of the week of the first day of the period
	// in question (either a year or a month).  Zero represents the
	// first day of the week on this calendar.
	var periodStartDayOfWeek =
		(dayOfWeek - cal.getFirstDayOfWeek() - dayOfPeriod + 1) % 7;
	if (periodStartDayOfWeek < 0) {
		periodStartDayOfWeek += 7;
	}

	// Compute the week number.  Initially, ignore the first week, which
	// may be fractional (or may not be).  We add periodStartDayOfWeek in
	// order to fill out the first week, if it is fractional.
	var weekNo = Math.floor((dayOfPeriod + periodStartDayOfWeek - 1) / 7);

	// If the first week is long enough, then count it.  If
	// the minimal days in the first week is one, or if the period start
	// is zero, we always increment weekNo.
	if ((7 - periodStartDayOfWeek) >= cal.getMinimalDaysInFirstWeek()) {
		++weekNo;
	}

	return weekNo;
}




function getEventObject(e) {  // utility function to retrieve object from event
    if (navigator.appName == "Microsoft Internet Explorer") {
        return e.srcElement;
    } else {  // is mozilla/netscape
        // need to crawl up the tree to get the first "real" element
        // i.e. a tag, not raw text
        var o = e.target;
        while (!o.tagName) {
            o = o.parentNode;
        }
        return o;
    }
}

function addEvent(name, obj, funct) { // utility function to add event handlers

    if (navigator.appName == "Microsoft Internet Explorer") {
        obj.attachEvent("on"+name, funct);
    } else {  // is mozilla/netscape
        obj.addEventListener(name, funct, false);
    }
}


function deleteEvent(name, obj, funct) { // utility function to delete event handlers

    if (navigator.appName == "Microsoft Internet Explorer") {
        obj.detachEvent("on"+name, funct);
    } else {  // is mozilla/netscape
        obj.removeEventListener(name, funct, false);
    }
}

function setCursor(obj) {
   if (navigator.appName == "Microsoft Internet Explorer") {
        obj.style.cursor = "hand";
    } else {  // is mozilla/netscape
        obj.style.cursor = "pointer";
    }
}

function Point(iX, iY,ipHeight)
{
   this.x = iX;
   this.y = iY;
   this.iph = ipHeight;
}


function getPoint(aTag)
{
   var oTmp = aTag;  
   var point = new Point(0,0,0);
   var divWidth=0;
   var divtf=false;
   do 
   {
		if (oTmp.tagName =="DIV" && oTmp.style.overflow=="auto"){
			point.y =point.y - oTmp.scrollTop;
			point.x =point.x - oTmp.scrollLeft;
			divWidth=oTmp.offsetWidth;
			divtf =true;
			oData.offsetWidth
		}
      point.x += oTmp.offsetLeft;
      point.y += oTmp.offsetTop;
      oTmp = oTmp.offsetParent;
   } 
   while (oTmp.tagName != "BODY");
   point.iph =oTmp.offsetHeight;
   if(divtf){
	   if (eval(point.x + 189)>divWidth){
		   point.x=eval(divWidth -189);
	   }
   }
   return point;

}

function toISODate(date) {
	var s = date.getFullYear();
	var m = date.getMonth() + 1;
	if (m < 10) {
		m = "0" + m;
	}
	var day = date.getDate();
	if (day < 10) {
		day = "0" + day;
	}
	var h = date.getHours();
	if (h < 10) {
		h = "0" + h;
	}
	var i = date.getMinutes();
	if (i < 10) {
		i = "0" + i;
	}
	var e = date.getSeconds();
	if (e < 10) {
		e = "0" + e
	}
	//alert("toISODate");
	return String(s) + String(m) + String(day);//+String(h)+String(i)+String(e);
	
}

function pad(number,X) {   // utility function to pad a number to a given width
	X = (!X ? 2 : X);
	number = ""+number;
	while (number.length < X) {
	    number = "0" + number;
	}
	return number;
}

function bw_check()
{
    var is_major = parseInt( navigator.appVersion );
    this.nver = is_major;
    this.ver = navigator.appVersion;
    this.agent = navigator.userAgent;
    this.dom = document.getElementById ? 1 : 0;
    this.opera = window.opera ? 1 : 0;
    this.ie5 = ( this.ver.indexOf( "MSIE 5" ) > -1 && this.dom && !this.opera ) ? 1 : 0;
    this.ie6 = ( this.ver.indexOf( "MSIE 6" ) > -1 && this.dom && !this.opera ) ? 1 : 0;
    this.ie4 = ( document.all && !this.dom && !this.opera ) ? 1 : 0;
    this.ie = this.ie4 || this.ie5 || this.ie6;
    this.mac = this.agent.indexOf( "Mac" ) > -1;
    this.ns6 = ( this.dom && parseInt( this.ver ) >= 5 ) ? 1 : 0;
    this.ie3 = ( this.ver.indexOf( "MSIE" ) && ( is_major < 4 ) );
    this.hotjava = ( this.agent.toLowerCase().indexOf( 'hotjava' ) != -1 ) ? 1 : 0;
    this.ns4 = ( document.layers && !this.dom && !this.hotjava ) ? 1 : 0;
    this.bw = ( this.ie6 || this.ie5 || this.ie4 || this.ns4 || this.ns6 || this.opera );
    this.ver3 = ( this.hotjava || this.ie3 );
    this.opera7 = ( ( this.agent.toLowerCase().indexOf( 'opera 7' ) > -1 ) || ( this.agent.toLowerCase().indexOf( 'opera/7' ) > -1 ) );
    this.operaOld = this.opera && !this.opera7;
    return this;
};



