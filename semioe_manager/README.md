#后台管理系统


## 用户登录接口

###### URL
```
/user/login/submit
```
###### API Type
```
POST
```
###### params
```
?userName=13240130405&password=123456
```
###### return
```javascript
{
    "status": 200,
    "resultCode": {
        "code": "ACCOUNT_EXISTS",
        "message": "已在账户中心注册,完善信息直接登录！"
    },
    "token": null,
    "data":  {
        "managerId": "4",
        "name": "汪华",
        "imageUrl": "",
        "type": "1",
        "company": "",
        "skills": "",
        "certificationsUrl": "",
        "roleId": 1,
        "userStatus": 1 //INIT("资料待完善"), PEDING("待审核"), PASS("审核通过"), REJECT("驳回申请"), DISABLE("禁用");
    },
    "successful": false
}
```
###### 备注
```
  1.用户登陆验证成功，需要生成用户TOKEN，并保存在Redis中。
```


## 用户注册接口

###### URL
```
/user/regist/submit
```
###### API Type
```
POST
```
###### params
```javascript
{
    "mobile":"13240130406",
    "email":"126@456.com",
    "userName":"abe",
    "idCode":"12345606",
    "password":"123456"
}
```
###### return
```javascript
{
    "status": 200,
    "resultCode": {
        "code": "SUCCESS",
        "message": "注册成功！"
    },
    "token": null,
    "data": null,
    "successful": true
}

{
    "status": 200,
    "resultCode": {
        "code": "PARAM_ERROR",
        "message": "参数错误！用户名不能为空"
    },
    "token": null,
    "data": null,
    "successful": false
}

{
    "status": 200,
    "resultCode": {
        "code": "ACCOUNT_REGISTERED",
        "message": "账户已注册，请直接登录完善信息！"
    },
    "token": null,
    "data": null,
    "successful": false
}
```
###### 备注
```
  1. 调用账户系统，保存账户信息
  2. （账户返回：成功）保存用户信息。
     （账户返回：用户已存在）返回页面，提示：账户已存在，请登录完善资料。
```


## 完善用户资料接口

###### URL
```
/user/update
```
###### API Type
```
POST
```
###### params
```javascript
{
    "managerId":"用户 id",
	"name":"个人姓名/机构名称",
	"type":"机构类型",
	"imageUrl":"头像/logo",
	"company":"工作单位",
	"skills":"擅长的技能/领域",
	"certificationsUrl":"资质证书/机构证明，多个路径，分号分隔",
	"roleId":"用户角色：1 个人专家，2 机构"
}
```
###### return
```javascript
{
    "status": 200,
    "resultCode": {
        "code": "SUCCESS",
        "message": "完善资料成功！"
    },
    "token": null,
    "data": null,
    "successful": false
}
```


## 修改用户密码接口

###### URL
```
/user/password/update
```
###### API Type
```
POST
```
###### params
```
header 添加 X-Auth-Token
?oldPassword=123456&newPassword=1234567
```
###### return
```javascript
{
    "status": 200,
    "resultCode": {
        "code": "SUCCESS",
        "message": "修改成功！"
    },
    "token": null,
    "data": null,
    "successful": false
}
```

## 分配用户角色接口

###### URL
```
/user/deployUserRole
```
###### API Type
```
POST
```
###### params
```javascript
{
    "userId": "123",  //用户账号ID
    "roleId": 1       //角色类型ID
}
```
###### return
```javascript
{
    "status": 200,
    "resultCode": {
        "code": "SUCCESS",
        "message": "修改成功！"
    },
    "token": null,
    "data": null,
    "successful": true
}
```



## 获取角色管理列表（all）

###### URL
```
/role/getRoleInfo
```
###### API Type
```
POST
```
###### params
```
无
```
###### return
```javascript
{
    "status": 200,
    "resultCode": {
        "code": "SUCCESS",
        "message": "查询成功！"
    },
    "token": null,
    "data": [
        {
            "id": 1,
            "itemName": "个人专家",
            "itemValue": null,
            "createTime": "2017-07-13 18:33:21",
            "updateTime": "2017-07-13 18:33:21",
            "inUse": 1
        },
        {
            "id": 2,
            "itemName": "机构",
            "itemValue": null,
            "createTime": "2017-07-13 18:33:59",
            "updateTime": "2017-07-13 18:33:59",
            "inUse": 1
        },
        {
            "id": 3,
            "itemName": "运营者",
            "itemValue": null,
            "createTime": "2017-07-13 18:34:10",
            "updateTime": "2017-07-13 18:34:10",
            "inUse": 1
        },
        {
            "id": 4,
            "itemName": "系统管理员",
            "itemValue": null,
            "createTime": "2017-07-13 18:34:16",
            "updateTime": "2017-07-13 18:34:16",
            "inUse": 1
        }
    ],
    "successful": true
}
```





## 添加新角色

###### URL
```
/role/addRoleInfo
```
###### API Type
```
POST
```
###### params
```javascript
{
	"itemName": "个人专家"
}
```
###### return
```javascript
{
    "status": 200,
    "resultCode": {
        "code": "SUCCESS",
        "message": "添加成功！"
    },
    "token": null,
    "data": null,
    "successful": true
}
```



## 修改角色

###### URL
```
/role/updateRoleInfo
```
###### API Type
```
POST
```
###### params
```javascript
{
    "id": 1,
	"itemName": "个人专家"
}
```
###### return
```javascript
{
    "status": 200,
    "resultCode": {
        "code": "SUCCESS",
        "message": "修改成功！"
    },
    "token": null,
    "data": null,
    "successful": true
}
```




## 根据id查询角色详情

###### URL
```
/role/getRoleInfoById?id=1
```
###### API Type
```
POST
```
###### params
```javascript
无
```
###### return
```javascript
{
    "status": 200,
    "resultCode": {
        "code": "SUCCESS",
        "message": "获取成功！"
    },
    "token": null,
    "data": {
        "id": 1,
        "itemName": "个人专家",
        "itemValue": null,
        "createTime": "2017-07-13 18:33:21",
        "updateTime": "2017-07-13 18:33:21",
        "inUse": 1
    },
    "successful": true
}
```




## 用户管理中获取角色列表

###### URL
```
/role/getRoleInfoListByUserManage
```
###### API Type
```
POST
```
###### params
```javascript
无
```
###### return
```javascript
{
    "status": 200,
    "resultCode": {
        "code": "SUCCESS",
        "message": "查询成功！"
    },
    "token": null,
    "data": [
        {
            "id": 3,
            "itemName": "运营者",
            "itemValue": null,
            "createTime": "2017-07-13 18:34:10",
            "updateTime": "2017-07-13 18:34:10",
            "inUse": 1
        },
        {
            "id": 4,
            "itemName": "系统管理员",
            "itemValue": null,
            "createTime": "2017-07-13 18:34:16",
            "updateTime": "2017-07-13 18:34:16",
            "inUse": 1
        }
    ],
    "successful": true
}
```


## 上传文件接口

###### URL
```
/file/upload
```
###### API Type
```
POST
```
###### params
```
file
```
###### return
```javascript
{
    "status": 200,
    "resultCode": {
        "code": "SUCCESS",
        "message": "上传成功！"
    },
    "token": null,
    "data": "文件路径,多个路径分号分隔",
    "successful": true
}
```


## 获取手机验证码

###### URL
```
/user/randomCode?mobile=17000000001
```
###### API Type
```
POST
```
###### params
```javascript
无
```
###### return
```javascript
{
    "status": 200,
    "resultCode": {
        "code": "SUCCESS",
        "message": "验证码发送成功！"
    },
    "token": null,
    "data": null,
    "successful": true
}
```

## 查询单用户

###### URL
```
/backstage/queryOne?managerId=17000000001
```
###### API Type
```
POST
```
###### params
```javascript
无
```
###### return
```javascript
{
    "status": 200,
    "resultCode": {
        "code": "SUCCESS",
        "message": "验证码发送成功！"
    },
    "token": null,
    "data": null,
    "successful": true
}
```

## 用户审核

###### URL
```
/backstage/audit?managerId=17000000001&auditMessage=3434&userStatus=3
```
###### API Type
```
POST
```
###### params
```javascript
无
```
###### return
```javascript
{
    "status": 200,
    "resultCode": {
        "code": "SUCCESS",
        "message": "审核成功！"
    },
    "token": null,
    "data": null,
    "successful": true
}
```

## 运营设置 查询

###### URL
```
/implement/query?type="0"
```
###### API Type
```
POST
```
###### params
```

```
###### return
```javascript
{
    "status": 200,
    "resultCode": {
        "code": "SUCCESS",
        "message": "查询运营设置成功！"
    },
    "token": null,
    "data": 
	    {
			"id”:"运营设置id",
            "name":"名称（地址）",
			"type":"类型" （0：banner 1：发现 2：PHP）,
			"imgUrl":"图片url",
            "createTime": "2017-07-13 18:34:16",
            "updateTime": "2017-07-13 18:34:16",
            "inUse": 1
        },
        {
			"id”:"运营设置id"
            "name":"名称（地址）",
			"type":"类型" （0：banner 1：发现 2：PHP）,
			"imgUrl":"图片url",
            "createTime": "2017-07-13 18:34:16",
            "updateTime": "2017-07-13 18:34:16",
            "inUse": 1
        }
    "successful": true
}
```

## 运营设置 添加

###### URL
```
/implement/add
```
###### API Type
```
POST
```
###### params
```javascript
{
    "name":"名称（地址）",
	"type":"类型" （0：banner 1：发现 2：PHP）,
	"imgUrl":"图片url",
}
```
###### return
```javascript
{
    "status": 200,
    "resultCode": {
        "code": "SUCCESS",
        "message": "添加运营设置成功！"
    },
    "token": null,
    "data": null,
    "successful": false
}
```
## 运营设置 修改

###### URL
```
/implement/update
```
###### API Type
```
POST
```
###### params
```javascript
{
    "id”:"运营设置id",
    "name":"名称（地址）",
	"type":"类型" （0：banner 1：发现 2：PHP）,
	"imgUrl":"图片url",
}
```
###### return
```javascript
{
    "status": 200,
    "resultCode": {
        "code": "SUCCESS",
        "message": "修改运营设置成功！"
    },
    "token": null,
    "data": null,
    "successful": false
}
```


## 运营设置 删除

###### URL
```
/implement/remove？id="344"
```
###### API Type
```
POST
```
###### params
```
```
###### return
```javascript
{
    "status": 200,
    "resultCode": {
        "code": "SUCCESS",
        "message": "删除运营设置成功！"
    },
    "token": null,
    "data": null,
    "successful": false
}
```


## 分页查询系统消息

###### URL
```
/message/getPageMessages
```
###### API Type
```
POST
```
###### params
```javascript
{"messageTo": "4","showCount": 10,"currentPage": 1}
"showCount": 10, // 分页大小
"currentPage": 1, // 当前页码
"id": "045039f9-44ae-4a8d-bb39-dd17db293080", //消息id
"messageFrom": "1", // 发送人id
"messageTo": "4", // 接收人id
"type": null, // 信息类型
"content": null, // 信息内容
"status": null, // 状态
```
###### return
```javascript
{
    "status": 200,
    "resultCode": {
        "code": "SUCCESS",
        "message": "查询成功！"
    },
    "token": null,
    "data": null,
    "items": [
        {
            "showCount": 10,
            "totalPage": 0,
            "totalResult": 0,
            "currentPage": 0,
            "currentResult": 0,
            "sortField": null,
            "order": null,
            "id": "045039f9-44ae-4a8d-bb39-dd17db293080",
            "messageFrom": "1",
            "messageTo": "4",
            "type": null,
            "content": null,
            "status": null,
			"userStatus": 1,
            "createTime": 1501493486000,
            "updateTime": 1501493484000,
            "inUse": 1
        }
    ],
    "pageSize": 10,
    "pageNumber": 1,
    "pagesCount": 1,
    "totalItemsCount": 1,
    "successful": true
}
```

## 查询当前用户流程列表

###### URL
```
/procedures/getProceduresByToken
```
###### API Type
```
POST
```
###### params
```
header 添加 X-Auth-Token
```
###### return
```javascript
{
    "status": 200,
    "resultCode": {
        "code": "SUCCESS",
        "message": "查询成功！"
    },
    "token": null,
    "data": [
        {
            "uid": 61,
            "created_at": "2017-08-03 17:51:26",
            "id": 656,
            "title": "后台管理测试12"
        },
        {
            "uid": 61,
            "created_at": "2017-08-03 17:24:59",
            "id": 655,
            "title": "后台测试用例数据"
        }
    ],
    "successful": true
}
```