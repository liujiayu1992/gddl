window.onresize = function() {
	var bodyHeight;
	var bodyWidth;
	var bodyTop;
	var tablemainHeight;
	var tablemainWidth;
	var SelectDataHeight;
	var SelectDataWidth;
	var EditDataHeight;
	var EditDataWidth;

	var ConditionHeight;
	var ConditionWidth;

	cal = new Calendar();
	cal.setMonthNames(new Array("\u4E00\u6708", "\u4E8C\u6708", "\u4E09\u6708",
			"\u56DB\u6708", "\u4E94\u6708", "\u516D\u6708", "\u4E03\u6708",
			"\u516B\u6708", "\u4E5D\u6708", "\u5341\u6708",
			"\u5341\u4E00\u6708", "\u5341\u4E8C\u6708"));
	cal.setShortMonthNames(new Array("\u4E00\u6708", "\u4E8C\u6708",
			"\u4E09\u6708", "\u56DB\u6708", "\u4E94\u6708", "\u516D\u6708",
			"\u4E03\u6708", "\u516B\u6708", "\u4E5D\u6708", "\u5341\u6708",
			"\u5341\u4E00\u6708", "\u5341\u4E8C\u6708"));
	cal.setWeekDayNames(new Array("\u65E5", "\u4E00", "\u4E8C", "\u4E09",
			"\u56DB", "\u4E94", "\u516D"));
	cal.setShortWeekDayNames(new Array("\u65E5", "\u4E00", "\u4E8C", "\u4E09",
			"\u56DB", "\u4E94", "\u516D"));
	cal.setFormat("yyyy-MM-dd");
	cal.setFirstDayOfWeek(0);
	cal.setMinimalDaysInFirstWeek(1);
	cal.setIncludeWeek(false);
	cal.create();
	cal.onchange = function() {
		if (calenDate.value != cal.formatDate()) {
			calenDate.value = cal.formatDate();
			calenDate.onchange();
		}
	}

	body.style.overflow = "hidden";
	bodyHeight = body.clientHeight;
	bodyWidth = body.clientWidth;
	bodyTop = body.clientTop;

	tablemainHeight = bodyHeight - bodyTop - 15;
	tablemainWidth = bodyWidth - 15;
	ConditionHeight = 20;
	ConditionWidth = tablemainWidth;
	SelectDataHeight = tablemainHeight - ConditionHeight - 22;
	SelectDataWidth = tablemainWidth + 15;

	SelectFrmDiv.style.position = "relative";

	trcondition.style.height = 20;
	tablemain.style.left = 0;

	tablemain.style.width = bodyWidth - 15;
	tablemain.style.height = tablemainHeight;

	SelectData.style.height = SelectDataHeight;
	SelectData.style.width = SelectDataWidth;

	SelectFrmDiv.style.height = SelectDataHeight;
	SelectFrmDiv.style.width = SelectDataWidth;
	SelectFrmDiv.style.posTop = 0;
}