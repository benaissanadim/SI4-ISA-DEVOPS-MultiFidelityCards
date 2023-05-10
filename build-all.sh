#!/bin/bash

function build_dir()  # $1 is the dir to get it
{
    cd $1
    ./build.sh
    cd ..
}

echo "** Building all"

build_dir "backend"

build_dir "cli"

build_dir "bank"

build_dir "notifier"

build_dir "parking"

echo "** Done all"