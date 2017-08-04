# thrift 应用
(a)编写 thrift 文件
1. 指定命名空间，如java 的包名： namespace java com.laioin.im.thrift.model
2. 引入其他thrift 文件，include "thriftmodel.thrift"
3. 引入其他文件里声明的struct也是就Java里的对象，表示一个数据结构.
    如：typedef thriftmodel.TResultModel TResultModel（这个是定义的数据结构的名称）
4. 定义数据结构 关键字：

        namespace java com.laioin.im.thrift.model
        struct TResultModel{
            1:i32 code;  # 1 表示参数顺序，i32 表示数据类型，code 表示属性名。
            // 数据类型
            (1) 基本类型
                bool: 布尔类型，占一个字节
                byte: 有符号字节
                i16：16位有符号整型
                i32：32位有符号整型
                i64：64位有符号整型
                double：64位浮点数
                string：未知编码或者二进制的字符串
                注意：thrift不支持无符号整形，因为很多目标语言不存在无符号整形（比如java）
            (2)容器类型
                List<t1>：一系列t1类型的元素组成的有序列表，元素可以重复
                Set<t1>：一些t1类型的元素组成的无序集合，元素唯一不重复
                Map<t1,t2>：key/value对，key唯一
                容器中的元素类型可以是除service以外的任何合法的thrift类型，包括结构体和异常类型
            2.特殊类型（括号内为对应的Java类型）：
                binary（ByteBuffer）：未经过编码的字节流
        }
5. 定义service 关键字：

        namespace java com.laioin.im.thrift.model // 命名空间 包名
        include "thriftmodel.thrift"  // 引入其他thrift文件，这是同一级目录
        // 把引入的 thrift文件里，的一个数据结构进行声明
        typedef thriftmodel.TResultModel TResultModel

        service UserService{ // 定义 service 服务
            TResultModel searchUser(1:string userName,2:i32 userId);
            // 返回类型， 方法名，（参数列表，同数据结构的属性）
            string getUserName(1:i32 userId);
        }
6. 编译thrift 文件

        如:java -> thrift-0.9.2.exe --gen java userservie.thrift(thrift文件)
7. 详细语法，请参考-> http://www.cnblogs.com/yuananyun/p/5186430.html

(b)具体使用（Java）
1. 服务端实现service的方法，比如Java：实现UserService.Iface
2. 服务器端启动服务。

        //传输通道 - 非阻塞方式
        TNonblockingServerTransport serverTransport = new TNonblockingServerSocket(9099);
        //异步IO，需要使用TFramedTransport，它将分块缓存读取。
        TTransportFactory transportFactory = new TFramedTransport.Factory();
        //使用高密度二进制协议
        TProtocolFactory proFactory = new TCompactProtocol.Factory();
        //设置处理器 handler
        TProcessor processor = new UserService.Processor(new UserServerImpl());
        /**
         *   //设置处理器 多个 handler
         *   TMultiplexedProcessor processor = new TMultiplexedProcessor();
         *   processor.registerProcessor("TUserService", new TUserService.Processor<TUserService.Iface>(new TUserServiceImpl()));
         *   processor.registerProcessor("TUploadFile", new TUploadFile.Processor<TUploadFile.Iface>(new TUploadFileServiceImpl()));
         */

        //创建服务器
        /**
         * TThreadPoolServer         这个在多线程并发的时候，吞吐量高。每一个连接都是一个线程
         *  如果你能提前知道你的client数目并且也不介意多一点线程，TThreadPoolServer对你可能是个好选择
         * TThreadedSelectorServer  目前来说比较安全，的server
         * 允许你为网络I/O分配多个线程。它保持两个线程池，一个为了处理网络I/O, 一个处理请求。
         * 更高的吞吐量且更低的延迟
         */
        TServer server = new TThreadedSelectorServer(
                new TThreadedSelectorServer.Args(serverTransport)
                        .protocolFactory(proFactory)
                        .transportFactory(transportFactory)
                        .processor(processor)
        );
        server.server(); // 启动服务
3. 客户端,创建service 的客户端。

            UserService.Client client client = null;
            //设置传输通道，对于非阻塞服务，需要使用TFramedTransport，它将数据分块发送
            TTransport transport = new TFramedTransport(new TSocket("locahost", 9099));
            transport.open();
            //使用高密度二进制协议
            TProtocol protocol = new TCompactProtocol(transport);
            //创建Client
             UserService.Client client client = new UserService.Client(protocol);
             /**
             * // 获取多个 客户端
             *TMultiplexedProtocol userProtocol = new TMultiplexedProtocol(protocol, "TUserService"); // 跟service 里对应
             *TUserService.Client userServiceClient = new TUserService.Client(userProtocol);
             *TMultiplexedProtocol uploadProtocol = new TMultiplexedProtocol(protocol, "TUploadFile");
             *TUploadFile.Client uplaodServiceClient = new TUploadFile.Client(uploadProtocol);
             */
4. 客户端调用service 服务

        client.getUserName(11);  // 通过建立的 client 调用