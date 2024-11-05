#! /bin/bash -x

mkdir assets
mkdir assets/api_reference

JAVADOC_DIR=./misc/assets/api_reference

cd ..

update_javadoc_for_module() {
  DEST_JAVADOC_SUBDIR=$JAVADOC_DIR/$1
  MODULE_NAME=$1
  GENERATED_JAVADOC_DIR=./$MODULE_NAME/build/docs/javadoc/*

  echo starting building $1 javadocs
  if ./gradlew :$MODULE_NAME:androidSourcesJar;./gradlew $MODULE_NAME:androidJavadocs
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

update_javadoc_for_module bvanalytics
update_javadoc_for_module bvauthiovation
update_javadoc_for_module bvcommon
update_javadoc_for_module bvconversations
update_javadoc_for_module bvcurations
update_javadoc_for_module bvcurationsui
update_javadoc_for_module bvlocation
update_javadoc_for_module bvnotifications
update_javadoc_for_module bvrecommendations
update_javadoc_for_module bvstorenotifications
update_javadoc_for_module bvproductsentiments

echo Success: all javadocs updated
