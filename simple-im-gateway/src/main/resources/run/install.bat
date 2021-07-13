@echo off
echo install service......

cd /d %~dp0
set currentDir=%cd%
echo %currentDir%

rem java -Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=8997,suspend=y -jar xxx.jar
rem java -jar  xxx.jar


for /f  %%i in ('dir /b *.jar') do java -jar  %%i

ping -n 25 127.0.0.1>nul
exit
