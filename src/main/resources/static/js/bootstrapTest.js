window.onload=function(){
	console.log("I am in .");
}

function topClick(id) {
	$.ajax({
		url : "/vuePage/"+id,
		data : {},
		type : 'get',
		cache : false,
		dataType : "html",
		success : function(data) {
			// alert(data);
			$("#middleHtml").html(data);
		},
		error : function() {
			alert("出错了");
		}
	});
}

function topClick2(id) {
	$("#middleHtml").load(id);
}


function contactUs(){
	alert("Email:12345@qq.com");
}


//排序的函数
function objKeySort(arys) {
	// 先用Object内置类的keys方法获取要排序对象的属性名，再利用Array原型上的sort方法对获取的属性名进行排序，newkey是一个数组
	var newkey = Object.keys(arys).sort();
	// console.log('newkey='+newkey);
	var newObj = {}; // 创建一个新的对象，用于存放排好序的键值对
	for (var i = 0; i < newkey.length; i++) {
		// 遍历newkey数组
		newObj[newkey[i]] = arys[newkey[i]];
		// 向新创建的对象中按照排好的顺序依次增加键值对
	}
	return newObj; // 返回排好序的新对象
}