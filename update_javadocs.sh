#! /bin/bash

JAVADOC_DIR=./javadocs

update_javadoc_for_module() {
  DEST_JAVADOC_SUBDIR=$JAVADOC_DIR/$1
  MODULE_NAME=bvandroidsdk$1
  GENERATED_JAVADOC_DIR=./bvandroidsdk$1/build/docs/javadoc/*

  echo starting building $1 javadocs
  if ./gradlew $MODULE_NAME:androidSourcesJar;./gradlew $MODULE_NAME:androidJavadocs
  then
    echo successfully built $1
  else
    echo ERROR: Failed to build $1
    exit 1
  fi
  echo removing old $1 javadocs
  rm -rf $THIS_JAVADOC_SUBDIR
  echo moving to $DEST_JAVADOC_SUBDIR directory
  mkdir $DEST_JAVADOC_SUBDIR
  mv $GENERATED_JAVADOC_DIR $DEST_JAVADOC_SUBDIR
  echo finished $1 javadocs
}

update_javadoc_for_module common
update_javadoc_for_module advertising
update_javadoc_for_module conversations
update_javadoc_for_module recommendations

echo Success: all javadocs updated
