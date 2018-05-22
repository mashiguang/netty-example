
1. 安装protoc-3.5.1-win32.zip
https://github.com/google/protobuf/releases

2. 编辑*.proto文件，如SubscribeReq.proto

3. 在.proto所在目录执行下面的命令

        protoc.exe --java_out=..\java .\SubscribeReq.proto
    

