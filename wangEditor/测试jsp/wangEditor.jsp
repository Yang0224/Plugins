<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/common/base.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<style type="text/css">
	
</style>
</head>
<body>
	<!--用父容器来控制宽度-->
	<div id="parent_panel" style="width:1000px">
	    <!--用当前元素来控制高度-->
	    <div id="child_paenl" style="height:400px;max-height:500px;"></div>
	</div>
    
    <script type="text/javascript">
    	
    	//此处为全局配置
    	//wangEditor.config.printLog = false;		//阻止输出log  操作wangEditor会实时打印log
    	//wangEditor.config.mapAk = 'TVhjYjq1ICT2qqL5LdS8mwas';		//百度地图密钥   为页面所有的editor配置全局的密钥    此处换成自己申请的密钥
    
	    var editor = new wangEditor('child_paenl');
	    
	  	//配置 onchange 事件	 必须在执行 editor.create() 方法之前
	    //editor.onchange = function () {
	    	//console.log(this.$txt.html());		// 编辑区域内容变化时，实时打印出当前内容
	    //};
	    
	    //编辑器的配置 必须在执行 editor.create() 方法之前
	    
	    // 普通的自定义菜单
	    /* editor.config.menus = [
	        'source',
	        '|',     // '|' 是菜单组的分割线
	        'bold',
	        'underline',
	        'italic',
	        'strikethrough',
	        'eraser',
	        'forecolor',
	        'bgcolor'
	     ]; */
	    
	 	// 颜色
	   /*  editor.config.colors = {
	        '#880000': '暗红色',
	        '#800080': '紫色',
	        '#ff0000': '红色'
	    }; */

	    // 字体
	    /* editor.config.familys = [
	        '宋体', '黑体', '楷体', '微软雅黑',
	        'Arial', 'Verdana', 'Georgia'
	    ]; */

	    // 字号
	    /* editor.config.fontsizes = {
	        // 格式：'value': 'title'
	        1: '10px',
	        2: '13px',
	        3: '16px',
	        4: '19px',
	        5: '22px',
	        6: '25px',
	        7: '28px'
	    }; */
	    
	 	// 字号  字号的配置中，对象的value值必须是1 2 3 4 5 6 7这几个数字（全部或者选择其中几个），title可以自己修改
	   /*  editor.config.fontsizes = {
	        // 格式：'value': 'title'
	        2: '小',
	        3: '中',
	        5: '大',
	        7: '特大'
	    }; */
	 	
	 	//自定义表情   参考：https://api.weibo.com/2/emotions.json?source=1362404091  http://yuncode.net/code/c_524ba520e58ce30
	    //editor.config.emotionsShow = 'icon';	//选择显示的是 icon还是value 建议icon，因为value只是字符
	    /* editor.config.emotions = {
    	    // 支持多组表情

    	    // 第一组，id叫做 'default' 
    	    'default': {
    	        title: '默认',  // 组名称
    	        data: 'http://www.wangeditor.com/wangEditor/test/emotions.data'  // 服务器的一个json文件url，例如官网这里配置的是 http://www.wangeditor.com/wangEditor/test/emotions.data
    	    },
    	    // 第二组，id叫做'weibo'
    	    'weibo': {
    	        title: '微博表情',  // 组名称
    	        data: [  // data 还可以直接赋值为一个表情包数组
    	            // 第一个表情
    	            {
    	                'icon': 'http://img.t.sinajs.cn/t35/style/images/common/face/ext/normal/7a/shenshou_thumb.gif',
    	                'value': '[草泥马]'
    	            },
    	            // 第二个表情
    	            {
    	                'icon': 'http://img.t.sinajs.cn/t35/style/images/common/face/ext/normal/60/horse2_thumb.gif',
    	                'value': '[神马]'
    	            }
    	            // 下面还可以继续，第三个、第四个、第N个表情。。。
    	        ]
    	    }
    	    // 下面还可以继续，第三组、第四组、、、
    	}; */
	    
	    //百度地图使用配置
    	//editor.config.mapAk = 'TVhjYjq1ICT2qqL5LdS8mwas';  		// 为当前的editor配置密钥     此处换成自己申请的密钥
    	
    	//editor.config.menuFixed = false;	// 关闭菜单栏fixed（编辑器默认会固定菜单栏，即让菜单栏保持『吸顶』状态）
    	
        //editor.config.zindex = 20000;		// 将全屏时z-index修改为20000
	    
        //editor.config.pasteFilter = false;		//在编辑器中粘贴一段文字时，编辑器会去除其中的样式和属性	此处设置为保留样式和尺寸
	 	
	    //editor.destroy();		// 销毁编辑器
	    //editor.undestroy();	// 恢复编辑器
	    
	    //editor.$txt.html('我卡冻死打死就打死刻录机大是考虑');		// 初始化内容
	    
        //var html = editor.$txt.html();		// 获取编辑器区域完整html代码

        //var text = editor.$txt.text();		// 获取编辑器纯文本内容

        //var formatText = editor.$txt.formatText();		// 获取格式化后的纯文本
        
        //editor.$txt.append('<p>新追加的内容</p>');		//追加内容
        
        //editor.$txt.html('<p><br></p>');		// 清空内容。不能传入空字符串，而必须传入如下参数
        
        //editor.disable();	// 禁用
        
        //editor.enable();	// 启用
        
        editor.config.uploadImgUrl = '/flblive/manage/wangEditor/wangEditorUpload';		// 上传图片（举例）
        
        /* editor.config.uploadParams = {
            // token1: 'abcde',
            // token2: '12345'
        };
        editor.config.uploadImgFileName = 'myFileName';
         */
	   
	    /*  editor.config.uploadParams = {		// 配置自定义参数（举例）
	        token: 'abcdefg',
	        user: 'wangfupeng1988'
	    };*/
	   /*  editor.config.uploadHeaders = {		// 设置 headers（举例）
	        //'Accept' : 'text/x-json',
	        //'Content-Type' : 'multipart/form-data'
	    }; */
	    
	    //editor.config.hideLinkImg = true;		// 隐藏掉插入网络图片功能。该配置，只有在你正确配置了图片上传功能之后才可用。
         
	    editor.create();
    </script>
</body>
</html>