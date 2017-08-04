namespace java com.laioin.frame.thrift.base.service
include "model.thrift"  // 引入thrift 文件

typedef model.TResultModel TResultModel  // 声明返回对象
typedef model.TUserInfoRequestParam TUserInfoRequestParam  // 获取用户信息请求对象
typedef model.TRegisterUserRequestParam TRegisterUserRequestParam  // 声明用户注册对象


service TUserService{
    TResultModel getUserInfo(1:TUserInfoRequestParam param); // 获取用户信息
    TResultModel registerUser(1:TRegisterUserRequestParam param); // 注册用户
}


service TUploadFile{ // 上传文件
    string uploadFile(1:binary file); // 上传文件
}
