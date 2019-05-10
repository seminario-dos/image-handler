#!/bin/bash

RESIZE_DIMENSIONS=("?width=30&height=30" "?width=60&height=60" "?width=100&height=100" "?width=110&height=110" "?width=128&height=128" "?width=150&height=200" "?width=161&height=161" "?width=165&height=165" "?width=180&height=180" "?width=300&height=300" "?width=400&height=400")

NUMBER_OF_INVOCATIONS=1000

for (( i=1; i<=$NUMBER_OF_INVOCATIONS; i++ )); do

    RESIZE_OPT=$(jot -r 1 0 10)
    PIC_ID=$(jot -r 1 1 1000)
    PIC_NAME="pic-$PIC_ID.jpeg"

    echo "--------------------------------"
    echo "$i - $PIC_NAME"
    echo ${RESIZE_DIMENSIONS[$RESIZE_OPT]}
    echo "---------------------------------"

#   Lambda simple
    curl -vv https://bub67sv1i7.execute-api.us-west-2.amazonaws.com/dev/images/$PIC_NAME${RESIZE_DIMENSIONS[$RESIZE_OPT]}


#   Lambda XRAY
#    curl -vv https://uirpu9zwae.execute-api.us-west-2.amazonaws.com/dev/images/$PIC_NAME${RESIZE_DIMENSIONS[$RESIZE_OPT]}

    sleep 1
done