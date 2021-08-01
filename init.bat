


rem start E:\\software\zookeeper-3.4.14\bin\zkServer.cmd

rem E:\\software\kafka_2.12-2.4.1\bin\windows\kafka-server-start.bat E:\\software\kafka_2.12-2.4.1\config\server.properties




rem 递归删文件
for /r E:\software\kafka_2.12-2.4.1\logs   %%i in (\) do del /q %%i

rem 递归删目录
rem for /r E:\software\kafka_2.12-2.4.1\logs   %%i in (\) do rd /s /q %%i
