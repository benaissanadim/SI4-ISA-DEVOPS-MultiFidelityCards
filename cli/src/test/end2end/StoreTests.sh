#!/bin/bash

RED='\033[0;31m'
NC='\033[0m'
GREEN='\033[0;32m'
BOLD='\033[1m'

function runCommand() {
    echo "$1" | socat - exec:"docker attach cli",pty
    sleep 1
    docker logs cli --tail=1
}

function waitForCLIStart(){
   printf '\n'
   waitFor="Started CliApplication in"
   while ! docker logs cli --tail=1 | grep -q "$waitFor"; do
        echo "Waiting for CLI to start ..."
        sleep 5
    done
    echo "CLI has started!"
    printf '\n'
}

function assertEquals(){
    echo "$2" |grep -Eq "$1"
    if [ $? -eq 0 ]; then
        printf "${GREEN}Test passed${NC}\n"
        return 0
    else
        printf "${RED}Test failed${NC}\n"
        return 1
    fi
}



#tests
echo "Starting tests"

echo "test 1: register store"

resultZone=$(runCommand "add paris")
result=$(runCommand "register-store PRIMARK Eiffeil paris")
expected="Store : {name=PRIMARK, address=Eiffeil, zone=paris}"
assertEquals "$expected" "$result"
globalResult+=($?)

echo "test 2: register store zone not found"

result=$(runCommand "register-store ZARA centre_ville tunis")
expected="404 : {\"error\":\"geographic zone not found\",\"details\":Geographic zone tunis does not exists}"
assertEquals "$expected" "$result"
globalResult+=($?)

for localResult in "${globalResult[@]}"
do
    if [ $localResult -ne 0 ]; then
        printf "${RED}${BOLD}Test failed${NC}\n"
        exit 1
    fi
done
