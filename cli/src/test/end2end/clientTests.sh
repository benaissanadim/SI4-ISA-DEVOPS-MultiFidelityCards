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

echo "test 1: register client1"

result=$(runCommand "register john john@gmail.com")
expected="Client : {email=john@gmail.com, username=john, status=NORMAL}"
assertEquals "$expected" "$result"
globalResult+=($?)

echo "test 2: register client the same username"

result=$(runCommand "register john john2@gmail.com")
expected="403 : {\"error\":\"client with username john already exists\",\"details\":null}"
assertEquals "$expected" "$result"
globalResult+=($?)

echo "test 3: register client the same email"

result=$(runCommand "register john2 john@gmail.com")
expected="403 : {\"error\":\"client with email john@gmail.com already exists\",\"details\":null}"
assertEquals "$expected" "$result"
globalResult+=($?)


echo "test 4: register invalid name"

result=$(runCommand "register oo omar@gmail.com")
expected="422 : {\"error\":\"Cannot process Customer information\",\"details\":username username should be between 3 and 20 characters}"
assertEquals "$expected" "$result"
globalResult+=($?)

echo "test 4: register invalid email"

result=$(runCommand "register ommm omar123456")
expected="422 : {\"error\":\"Cannot process Customer information\",\"details\":email invalid email}"
assertEquals "$expected" "$result"
globalResult+=($?)


for localResult in "${globalResult[@]}"
do
    if [ $localResult -ne 0 ]; then
        printf "${RED}${BOLD}Test failed${NC}\n"
        exit 1
    fi
done
