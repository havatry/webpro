function login(type) {
    if (type == 'login' || type == 'register') {
        if ($('input[name="username"]').val() == '' || $('input[name="passwod"]').val() == '') {
            alert((type == 'login' ? '登录' : '注册') + '用户名和密码都不能为空');
            return;
        }
        $('#type').attr('value', type); // 赋值
        RequestPostX('http://localhost:8080/login', $('#loginForm').serialize(), function (data) {
            alert(data.msg);
            if (type == 'login' && data.msg == '用户登录成功') {
                // 关闭模态框
                $('#loginPage').modal('hide');
                // 刷新页面
//                    checkCookie();
//                    flush(true);
                init();
            } else if (type == 'register' && data.msg.indexOf('用户名已存在') > -1) {
                // 清空
                document.getElementById('loginForm').reset();
                $('input[name="username"]').focus();
            }
        });
    } else if (type == 'logout') {
        // 注销
        RequestPostX('http://localhost:8080/login', {type : type}, function (data) {
            alert(data.msg);
            if (data.msg == '用户注销成功') {
                // 刷新页面
//                    checkCookie();
//                    flush(true);
                init();
            }
        })
    }
}