
#! /bin/bash

baseDir=$(cd `dirname $0`; pwd)
java_pid=""

function findJarAndRun() {
   for item in `ls $1`
   do
   fileName=$1"/"$item
   if [ -d $fileName ];
   then
        echo $fileName"是目录, skipping"
   else
        isJar $fileName
        jarFlag=$?
        if [ "$jarFlag" = "1" ]; then
           echo "${fileName} is a jar"
           runJar $fileName
           echo "$java_pid" > $2
        else
           echo "${fileName} not a jar, skip run"
        fi
   fi
   done
}



function isJar() {
    fileName=$1
    if [ ! ${fileName##*.} = "jar" ];
    then
        return 0
    else
        return 1
    fi
}


function runJar() {
    jarName=$1
    nohup java -Xms512M -Xmx2048M -Xss1024k -XX:MaxNewSize=1536M -XX:PermSize=512M -XX:MaxPermSize=1024M -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=${baseDir}  -jar -Dremark=${remark} -Dbpath=${baseDir} $jarName >/dev/null 2>&1 &
    java_pid=$!
}




pidFile="${baseDir}/pid.txt"
remark=$1

if [ $# -lt 1 ]; then
 echo "error: no remark assigned"
 exit
fi

echo "remark is ${remark}"

echo "current dir is: ${baseDir}, the script is $0"
if [ -f $pidFile ]; then
   echo "pid file already exists! old  running process releated with old pid will be killed"
   oldPid=$(cat $pidFile) #将pid从文件中读取，并作为一个变量
   kill -9 $oldPid   #杀灭进程
fi

findJarAndRun $baseDir  $pidFile

echo "jar is running, and pid is: ${java_pid}"