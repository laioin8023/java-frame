namespace java com.laioin.frame.thrift.base.model

// 定义 model -- struct

// 返回的数据结构
struct TResultModel{
    1:i32 code; // 返回码
    2:string message; // 返回的信息
    3:string data; // 返回的数据,JSON 数据
}

// 获取用户信息 请求参数 对象
struct TUserInfoRequestParam{
    1:string userName; // 用户名
    2:i32 userId; // 用户id
    3:string phone; // 手机号
}

// 用户注册 service，请求参数对象
struct TRegisterUserRequestParam{
    1:string userName;
    2:i32 age;
    3:string phone;
    4:string email;
}
