#!/bin/bash

NUMBER_OF_INVOCATIONS=$1

echo $NUMBER_OF_INVOCATIONS

for (( i=1; i<=NUMBER_OF_INVOCATIONS; i++ ))
do
    echo "I VALUE IS: $i"
    aws lambda invoke --function-name ImageHandlerXRay --log-type Tail --payload file://src/test/resources/image-request-event.json test-output.log
    sleep 1
done
