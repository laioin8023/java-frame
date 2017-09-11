# java 断点下载
1. 下载大文件时，如果出现下载一些就断了，这时候会重新进行下载。
    1. 下载重试 10 次，没次间隔 1 秒


2. 使用方法如下：

         // 1. 初始化下载对象
         DownloadManage m = new DownloadManage(new IProgressEvent() {
            // 2. 下载时，每秒回调一次，返回下载信息。
            public void changeProgressEvent(FileBean bean) {
                String fs = "文件大小：{%s} , 已下载: {%s} , 下载百分比：{%s} , 下载速度：{%s} ， 剩余下载时间：{%s}";
                LGR.info(String.format(fs, bean.getFormatFileSize(), bean.getFormatDownSize(), bean.getDownRatio(),
                        bean.getFormatDownSpeed(), bean.getFormatDownDate()));
            }
        });
        // 3. 执行下载，下载文件的地址，保存文件的路径
        m.download("http://192.168.1.222//song/DiskA/100237111/out.ts", "D:\\tmp\\t.s");
