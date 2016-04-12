#!/bin/bash

KOTLINC_JS=kotlinc-js
which $KOTLINC_JS &> /dev/null
if [ $? -ne 0 ] ; then
    if [ -z $KOTLIN_HOME ] ; then
        echo "Neither kotlinc-js was found in PATH, nor KOTLIN_HOME environment variable was not specified. Exiting."
        exit 1
    fi
    KOTLINC_JS=$KOTLIN_HOME/bin/$KOTLINC_JS
fi

KOTLINC_CMD="$KOTLINC_JS -source-map -meta-info -main call -module-kind commonjs"
mkdir -p out/production
rm -rf out/production/*

function build_module {
    MODULE_NAME=$1
    echo "Building '$MODULE_NAME' module"
    mkdir -p out/production/$MODULE_NAME
    CMDLINE="$KOTLINC_CMD -output out/production/$MODULE_NAME/$MODULE_NAME.js"
    if ! [ -z $2 ] ; then
        CMDLINE="$CMDLINE -library-files $2"
    fi
    $CMDLINE $MODULE_NAME/src/*
}

build_module commons
build_module game out/production/commons
build_module webui out/production/commons,out/production/game

echo "Copying Kotlin libraries"
mkdir -p out/production/lib
unzip -u $KOTLIN_HOME/lib/kotlin-jslib.jar kotlin.js builtins.js stdlib.js -d out/production/lib

echo "Running webpack"
webpack
