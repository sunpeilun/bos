// 发送ajax获取数据，先不考虑分页，一页获取4条数据，让这4条数据显示
$(function () {
    //默认获取第一页的数据
    var page = 1;//页码
    var rows = 4;//每页条数
    var total = -1;//总条数

    // 初始化页面数据
    getData(page,rows);

    function formatDate(time){
        return time.substring(0,10).replace("-",".").replace("-",".");
    }
    
    // 分页插件的使用
    // 第一个参数：总条数
    // 第二个参数：分页工具栏的设置
    $("#Pagination").pagination(total,{
        items_per_page:rows,// 每页条数
        prev_text:"上一页",//工具栏的文字提醒
        next_text:"下一页",
        callback:callbackPagination// 当点击分页的页码的时候，触发的函数
    })

    // 定义回调函数
    // 第一个参数：当前点击的页码
    // 第二个参数：当前的pagination分页组件
    function callbackPagination(p,jq){
        // 通过page请求后台，获取数据
        getData(p+1,rows);
    }


    function getData(page,rows){
        $.ajax({
            url:"/sales/findPromotionByPage?page="+page+"&rows="+rows,
            type:"GET",
            async:false,// ajax默认发异步请求，但是当把async设置false的时候，自动发同步请求
            statusCode:{
                200:function (data) {

                    //console.log(data)
                    data = eval("("+data+")");
                    total = data.total;
                    var rows = data.rows;
                    console.log(total);
                    console.log(rows);

                    // 清空原始数据
                    $("#promotionresult").html("");

                    // 遍历
                    $.each(rows,function (i,item) {
                        var status = "未开始";
                        if(item.status==1){
                            status = "进行中";
                        }else if(item.status==2){
                            status = "已结束";
                        }


                        var str = "";
                        str+="<div class='col-sm-6 col-md-3'>";
                        str+="<div class='thumbnail'>";
                        str+="<img src='"+item.titleImg+"' alt='活动一'>";
                        str+="<div class='caption'>";
                        str+="<p>"+item.title+"</p>";
                        str+="<p class='text-right status'><span>"+status+"</span></p>";
                        str+="<p class='text-right grey'>"+formatDate(item.startDate)+"-"+formatDate(item.endDate)+"</p>";
                        str+="<p class='text-right grey'>"+item.activeScope+"</p>";
                        str+="</div>";
                        str+="</div>";
                        str+="</div>";

                        // 追加到广告div中
                        $("#promotionresult").append(str);

                    })


                }
            }

        })
    }


})
