#!/bin/sh
export ProjectPath=$(cd "../$(dirname "$1")"; pwd)
export TargetClassName="com.milton.common.demo.activity.jni.HelloJniActivity"

export SourceFile="${ProjectPath}/demo/src/main/java"
export TargetPath="${ProjectPath}/jni/src/main/jni"

cd "${SourceFile}"
javah -d ${TargetPath} -classpath "${SourceFile}" "${TargetClassName}"
echo -d ${TargetPath} -classpath "${SourceFile}" "${TargetClassName}"