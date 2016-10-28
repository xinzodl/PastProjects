#!/bin/sh
#set -x

RES=`echo "$1" | grep -E '^ssoo_p1(_100[[:digit:]]{6}){1,2}.zip$'`

if [ ! -f "$1" ] ;then
	echo "File does not exist"
	exit
fi

if [ "$RES" = "" ] ;then
	echo "Incorrect file name/format"
	exit
fi

echo "Correct file name/format"

ZIP=$(basename "$1")
ZIP="${ZIP%.*}"
unzip -q $1 -d $ZIP
cd $ZIP

if [ ! -e mywc.c -o ! -e myenv.c -o ! -e myishere.c -o ! -e Makefile ]; then
	echo "One or more of the files were not found"
fi

make clean 2> /dev/null > /dev/null
make       2> /dev/null > /dev/null

if [ ! -e mywc ] || [ ! -e myenv ] || [ ! -e myishere ]; then
	echo "Compilation error"
	cd ..
	rm -r $ZIP
	exit
else
	echo "Compilation OK"

	RES=`./mywc mywc.c 2>/dev/null | grep '^[[:digit:]]\+[[:blank:]]\+[[:digit:]]\+[[:blank:]]\+[[:digit:]]\+[[:blank:]]\+[[:alnum:]]\+\.[[:alnum:]]\+$'`
	if [ "$RES" != "" ] ; then
		echo "mywc: correct output format"
	else
		echo "mywc: incorrect output format"
	fi

	RES=`./myishere . file 2>/dev/null | grep "File file is in directory ."`
	RES2=`./myishere . file 2>/dev/null | grep "File file is not in directory ."`
	if [ "$RES" != "" ] || [ "$RES2" != "" ] ; then
		echo "myishere: correct output format"
	else
		echo "myishere: incorrect output format"
	fi

	cd ..
	rm -r $ZIP
	exit
fi

