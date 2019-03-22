#!/bin/bash

function resolve() {
#    echo "--\n $1"
    NAME=$(echo $1 | jq '.name' | sed -e 's/^"//' -e 's/"$//')
#    echo $NAME
    if [ "$NAME" == "Initialization" ]; then
        echo "$t,$(echo $1| jq '.name'), $(echo $1 | jq '.start_time'), $(echo $1 | jq '.end_time')"
    fi
    if [ "$NAME" = "input event" ]; then
        echo "$t,$(echo $1| jq '.name'), $(echo $1 | jq '.start_time'), $(echo $1 | jq '.end_time')"
    fi
    if [ "$NAME" == "resize event" ]; then
        echo "$t,$(echo $1| jq '.name'), $(echo $1 | jq '.start_time'), $(echo $1 | jq '.end_time')"
    fi

    if [ "$NAME" == "write response" ]; then
        echo "$t,$(echo $1| jq '.name'), $(echo $1 | jq '.start_time'), $(echo $1 | jq '.end_time')"
    fi

    SUBSEGMENTS=$(echo $1 | jq '.subsegments')
#    echo "SUB $SUBSEGMENTS"
    if [ "$SUBSEGMENTS" != null ]; then
        SUB_NAME_1=$(echo $SUBSEGMENTS | jq '.[0].name' | sed -e 's/^"//' -e 's/"$//')
        SUB_NAME_2=$(echo $SUBSEGMENTS | jq '.[1].name' | sed -e 's/^"//' -e 's/"$//')
        if [ "$SUB_NAME_1" == "resize operation" ]; then
            echo "$t,$(echo $SUBSEGMENTS| jq '.[0].name'), $(echo $SUBSEGMENTS | jq '.[0].start_time'), $(echo $SUBSEGMENTS | jq '.[0].end_time')"
        fi
        if [ "$SUB_NAME_1" == "S3" ]; then
            echo "$t,$(echo $SUBSEGMENTS| jq '.[0].name'), $(echo $SUBSEGMENTS | jq '.[0].start_time'), $(echo $SUBSEGMENTS | jq '.[0].end_time')"
        fi
        if [ "$SUB_NAME_2" == "resize operation" ]; then
            echo "$t,$(echo $SUBSEGMENTS| jq '.[1].name'), $(echo $SUBSEGMENTS | jq '.[1].start_time'), $(echo $SUBSEGMENTS | jq '.[1].end_time')"
        fi
        if [ "$SUB_NAME_2" == "S3" ]; then
            echo "$t,$(echo $SUBSEGMENTS| jq '.[1].name'), $(echo $SUBSEGMENTS | jq '.[1].start_time'), $(echo $SUBSEGMENTS | jq '.[1].end_time')"
        fi
    fi

}

function resolve_segment() {

    SUBSEGMENTS=$(echo $1 | jq '.subsegments')
    SUBSEGMENT_0=$(echo $SUBSEGMENTS | jq '.[0]')
    SUBSEGMENT_1=$(echo $SUBSEGMENTS | jq '.[1]')
    SUBSEGMENT_2=$(echo $SUBSEGMENTS | jq '.[2]')
    SUBSEGMENT_3=$(echo $SUBSEGMENTS | jq '.[3]')
    resolve "$SUBSEGMENT_0"
    resolve "$SUBSEGMENT_1"
    resolve "$SUBSEGMENT_2"
    resolve "$SUBSEGMENT_3"
}


TRACEIDS=(1-5c91a1fd-dd1bee31baee5128f98b77f6 1-5c91a1cf-19da3bb7fbc96ceb66539d20 1-5c91a217-e6c6f3bb28069ab46aa75707 1-5c91a21a-3a9951f2f4a387edf468baee 1-5c91a214-1595cdf86122e5521f16b59e 1-5c91a20d-de39b7786e64a553da3b7701 1-5c91a202-536a4ad2f052a061612aab54 1-5c91a1f8-fd1936f91e76f959cd438483 1-5c91a210-d629b302c5febd8295d20373 1-5c91a208-4b0cbec41bed6ac61a5ff022 1-5c91a205-cc460253a0113ce44e1644e5 1-5c91a1f1-0ffd4af515b78531ec5a677a)
#TRACEIDS=(1-5c91a1fd-dd1bee31baee5128f98b77f6 1-5c91a1cf-19da3bb7fbc96ceb66539d20 1-5c91a205-cc460253a0113ce44e1644e5)
#TRACEIDS=(1-5c91a1fd-dd1bee31baee5128f98b77f6 1-5c91a1cf-19da3bb7fbc96ceb66539d20)
for t in ${TRACEIDS[@]} ; do
#    echo "ID: $t"
#    aws xray batch-get-traces --trace-ids $t --output text

    SEGMENT_0=$(aws xray batch-get-traces --trace-ids $t --query 'Traces[*].Segments[0].Document' --output text)
    SEGMENT_1=$(aws xray batch-get-traces --trace-ids $t --query 'Traces[*].Segments[1].Document' --output text)
    SEGMENT_2=$(aws xray batch-get-traces --trace-ids $t --query 'Traces[*].Segments[2].Document' --output text)

#    echo "SEG 1: $SEGMENT_0"
#    echo "SEG 2: $SEGMENT_1"
#    echo "SEG 3: $SEGMENT_2"

    echo "REQ ID: $t"
    resolve_segment "$SEGMENT_0"
    resolve_segment "$SEGMENT_1"
    resolve_segment "$SEGMENT_2"
    echo "---"

done


#    echo "PARAM: $1"
#    SUBSEGMENTS_LENGTH=$(echo $1 | jq '.subsegments | length')
#    INITIALIZATION_EVENT=$(echo $SUBSEGMENTS | jq '.[0]')
#    INPUT_EVENT=$(echo $SUBSEGMENTS | jq '.[2]')
#    RESIZE_EVENT=$(echo $SUBSEGMENTS | jq '.[1]')
#    S3_EVENT=$(echo $SUBSEGMENTS | jq '.[1].subsegments[0]')
#    RESIZE_OPERATION=$(echo $SUBSEGMENTS | jq '.[1].subsegments[1]')
#    WRITE_EVENT=$(echo $SUBSEGMENTS | jq '.[3]')

#    if [ $(echo $SUBSEGMENTS | jq '.[5]') = null ]; then
#        echo "no hay 5"
#    fi

#    echo "$t,$(echo $INITIALIZATION_EVENT| jq '.name'), $(echo $INITIALIZATION_EVENT | jq '.start_time'), $(echo $INITIALIZATION_EVENT | jq '.end_time')"
#    echo "$t,$(echo $INPUT_EVENT | jq '.name'), $(echo $INPUT_EVENT | jq '.start_time'), $(echo $INPUT_EVENT | jq '.end_time')" >> xray-data/input-event.csv
#    echo "$t,$(echo $RESIZE_EVENT | jq '.name'), $(echo $RESIZE_EVENT | jq '.start_time'), $(echo $RESIZE_EVENT | jq '.end_time')"
#    echo "$t,$(echo $S3_EVENT | jq '.name'), $(echo $S3_EVENT | jq '.start_time'), $(echo $S3_EVENT | jq '.end_time')"
#    echo "$t,$(echo $RESIZE_OPERATION | jq '.name'), $(echo $RESIZE_OPERATION | jq '.start_time'), $(echo $RESIZE_OPERATION | jq '.end_time')"
#    echo "$t,$(echo $WRITE_EVENT | jq '.name'), $(echo $WRITE_EVENT | jq '.start_time'), $(echo $WRITE_EVENT | jq '.end_time')"

#TRACEIDS=$(aws xray get-trace-summaries --start-time 1552968000000 --end-time 1552971600000 --query 'TraceSummaries[*].Id' --output text)