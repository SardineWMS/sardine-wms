const qs = require('qs')
const Mock = require('mockjs')
const Cookie = require("js-cookie")

module.exports = {
  'POST /api/login' (req, res) {
    const userItem =  qs.parse(req.body);
    const response = {
      success: false,
      loginId: "",
      token : "",
      message: ""
    }

    console.dir(qs.parse(req.body));
    if(userItem.username === 'admin' && userItem.password === '123'){
        const now = new Date()
        now.setDate(now.getDate() + 1);
        response.message = "登录成功";
        response.success = true;
        response.loginId = userItem.username;
        response.token =  Math.random().toString(36).substring(7);
     }else if(userItem.username === 'admin'){
        response.message = "密码不正确";
     } else {
       response.message = "用户不存在"
    }

    res.json(response);
  },

  'GET /api/userInfo' (req, res) {
    const response = {
      success: Cookie.get('user_session')&&Cookie.get('user_session') > new Date().getTime() ? true :false,
      username: Cookie.get('user_name')||'',
      message: ""
    }
    res.json(response)
  },

  'POST /api/signIn' (req, res) {
     const {signItem} =  qs.parse(req.body);
     const response = {
      success: true,
      message: "注册成功"
    }

    console.dir(qs.parse(req.body));
    setTimeout(function () {
       res.json(response);
    }, 2000);  
  },

  'POST /api/logout' (req, res) {
    res.json({
      success: true,
      message: "退出成功"
    })
  }
}
