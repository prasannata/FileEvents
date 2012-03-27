#!/bin/bash

function usage()
{
   echo "Usage: ./run.sh <options>"
   echo "When no options are provided, the java program is started provided the jar has been already built."
   echo "Options are: "
   echo "  clean 		Clean, removes target directory"
   echo "  install 		Compile sources and generates jar"
   echo "  clean install 	Clean, Compile sources and generates jar"
   echo "  help			Display this"
}

function clean()
{
   currentDir=`pwd`

   echo "Cleaning $currentDir/$TARGET_DIR ..."
   rm -fr $TARGET_DIR

   echo "Cleaning $currentDir/$DOC_DIR ..."
   rm -fr $DOC_DIR
}

function install()
{
   if [ ! -f $TARGET_DIR ];
   then
      mkdir $TARGET_DIR
   fi

   if [ ! -f $CLASSES_DIR ];
   then
      mkdir $CLASSES_DIR
   fi

   if [ ! -f $DOC_DIR ];
   then
      mkdir $DOC_DIR
   fi

   currentDir=`pwd`
   echo "Building $currentDir/$CLASSES_DIR ..."
   find $SOURCE_DIR -name "*.java" | xargs javac -d $CLASSES_DIR

   echo "Installing $currentDir/$CLASSES_DIR/$JAR_NAME"
   jar $JAR_OPTS $TARGET_DIR/$JAR_NAME $MANIFEST_FILE -C $CLASSES_DIR .

   echo "Generating javadoc $currentDir/$DOC_DIR ..."
   javadoc -d $DOC_DIR -quiet -sourcepath $SOURCE_DIR -subpackages $ROOT_PKG
   echo "Build successful"
}

JAR_NAME=FileEvents.jar
SOURCE_DIR=src/main/java
TARGET_DIR=target
DOC_DIR=docs
CLASSES_DIR=$TARGET_DIR/classes
ROOT_PKG=com
MANIFEST_FILE=manifest.txt
JAR_OPTS=cfm

if [ $# -gt 2 ]; then
   usage
fi

if [ $# -eq 0 ]; then
   if [ -f $TARGET_DIR/$JAR_NAME ]; then
       java -jar $TARGET_DIR/$JAR_NAME
   else
       echo "$TARGET_DIR/$JAR_NAME not found. Run ./run.sh -install"
   fi
else
   for var in "$@"
   do
      case "$var" in
         "clean")
            clean
            ;;

         "install")
            install
            ;;

         "help")
            usage
            ;;

         *)
            echo "Unknow option $var"
            usage
            ;;
   esac
  done
fi
