#!/usr/bin/env bash

fileName="insert_all"
groovyPath="groovy"

scriptPath=$(dirname "$0")
if [ ! -d "$scriptPath" ]; then
	mkdir $scriptPath
fi
cd $scriptPath

pidsPath="$scriptPath/pids"
if [ ! -d "$pidsPath" ]; then
	mkdir $pidsPath
fi

logsPath="$scriptPath/logs"
if [ ! -d "$logsPath" ]; then
	mkdir $logsPath
fi

dumpPath="$scriptPath/dumps"
if [ ! -d "$dumpPath" ]; then
	mkdir $dumpPath
fi

# Checks if a process is already running
if [ -f "$pidsPath/$fileName.pid" ]; then
    echo "A process is already running."
    exit 0
fi

# Checks if there is java in the machine
command -v java > /dev/null 2>&1 || { echo "Command 'java' not found. Finishing execution." >&2; exit 1; }

# Check if there is groovy in the machine
command -v groovy > /dev/null 2>&1 || { echo "Command 'groovy' not found. Finishing execution." >&2; exit 1; }

# Write in the import.pids file the process PID
echo $$ > $pidsPath/$fileName.pid

echo "***************************************************************************************************"
echo "*"
echo "*"
java -version
echo "*"
echo "*"
echo "***************************************************************************************************"
echo "*"
echo "*"
groovy -version
echo "*"
echo "*"
echo "***************************************************************************************************"

mongoDatabase="user"
mongoUserName="user-admin"
mongoPassword="admin123"
mongoHostnameAndPort="user-mongo:27017"
mongoOptions="?readPreference=primaryPreferred" # example: "?replicaSet=rsMpComprasPRD&readPreference=secondaryPreferred"

jvmParams="-server -Xms2G -Xmx2G -XX:+UseG1GC"
jvmParams="$jvmParams -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=$dumpPath"

echo "***************************************************************************************************"
echo "*"
echo "*"
echo "* Running the command:"
echo "*"
echo "*"
echo "* JVM parasm = $jvmParams"
echo "*"
echo "*"
echo "* $groovyPath \ "
echo "* 	-Dgroovy.grape.report.downloads=true Import.groovy \ "
echo "* 	$mongoDatabase \ "
echo "* 	$mongoUserName \ " 
echo "* 	$mongoPassword \ "
echo "* 	$mongoHostnameAndPort \ "
echo "* 	$mongoOptions >> $logsPath/$fileName.log"
echo "*"
echo "*"
echo "***************************************************************************************************"

export JAVA_OPTS="$jvmParams"

$groovyPath \
	-Dgroovy.grape.report.downloads=true \
	Import.groovy \
	$mongoDatabase \
	$mongoUserName \
	$mongoPassword \
	$mongoHostnameAndPort \
	$mongoOptions >> $logsPath/$fileName.log

# Removing the import.pids file, if it exists
if [ -f "$pidsPath/$fileName.pid" ]; then
	rm -f $pidsPath/$fileName.pid
fi