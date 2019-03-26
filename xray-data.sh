#!/bin/bash

function resolve() {
#    echo "--\n $1"
    NAME=$(echo $1 | jq '.name' | sed -e 's/^"//' -e 's/"$//')
#    echo $NAME
#    INITIALIZATION_TIME=""
#    INPUT_EVENT_TIME=""
#    RESIZE_EVENT_TIME=""
#    S3_TIME=""
#    RESIZE_OPERATION_TIME=""
#    WRITE_RESPONSE_TIME=""


    if [ "$NAME" == "ImageHandlerXRay" ]; then
        LAMBDA_ROOT="$(echo $1 | jq '.start_time'); $(echo $1 | jq '.end_time')"
        AWS_LAMBDA_REQ_ID=$(echo $1 | jq '.aws.request_id' | sed -e 's/^"//' -e 's/"$//')
    fi
    if [ "$NAME" == "Initialization" ]; then
#        echo "$t,$(echo $1| jq '.name'), $(echo $1 | jq '.start_time'), $(echo $1 | jq '.end_time')"
        INITIALIZATION_TIME="$(echo $1 | jq '.start_time'); $(echo $1 | jq '.end_time')"
    fi
    if [ "$NAME" = "input event" ]; then
#        echo "$t,$(echo $1| jq '.name'), $(echo $1 | jq '.start_time'), $(echo $1 | jq '.end_time')"
        INPUT_EVENT_TIME="$(echo $1 | jq '.start_time'); $(echo $1 | jq '.end_time')"
    fi
    if [ "$NAME" == "resize event" ]; then
#        echo "$t,$(echo $1| jq '.name'), $(echo $1 | jq '.start_time'), $(echo $1 | jq '.end_time')"
        RESIZE_EVENT_TIME="$(echo $1 | jq '.start_time'); $(echo $1 | jq '.end_time')"
    fi

    if [ "$NAME" == "write response" ]; then
#        echo "$t,$(echo $1| jq '.name'), $(echo $1 | jq '.start_time'), $(echo $1 | jq '.end_time')"
        WRITE_RESPONSE_TIME="$(echo $1 | jq '.start_time'); $(echo $1 | jq '.end_time')"
    fi


    SUBSEGMENTS=$(echo $1 | jq '.subsegments')
#    echo "SUB $SUBSEGMENTS"
    if [ "$SUBSEGMENTS" != null ]; then
        SUB_NAME_1=$(echo $SUBSEGMENTS | jq '.[0].name' | sed -e 's/^"//' -e 's/"$//')
        SUB_NAME_2=$(echo $SUBSEGMENTS | jq '.[1].name' | sed -e 's/^"//' -e 's/"$//')
        if [ "$SUB_NAME_1" == "resize operation" ]; then
#            echo "$t,$(echo $SUBSEGMENTS| jq '.[0].name'), $(echo $SUBSEGMENTS | jq '.[0].start_time'), $(echo $SUBSEGMENTS | jq '.[0].end_time')"
            RESIZE_OPERATION_TIME="$(echo $SUBSEGMENTS | jq '.[0].start_time'); $(echo $SUBSEGMENTS | jq '.[0].end_time')"
        fi
        if [ "$SUB_NAME_1" == "S3" ]; then
#            echo "$t,$(echo $SUBSEGMENTS| jq '.[0].name'), $(echo $SUBSEGMENTS | jq '.[0].start_time'), $(echo $SUBSEGMENTS | jq '.[0].end_time')"
            S3_TIME="$(echo $SUBSEGMENTS | jq '.[0].start_time'); $(echo $SUBSEGMENTS | jq '.[0].end_time')"
        fi
        if [ "$SUB_NAME_2" == "resize operation" ]; then
#            echo "$t,$(echo $SUBSEGMENTS| jq '.[1].name'), $(echo $SUBSEGMENTS | jq '.[1].start_time'), $(echo $SUBSEGMENTS | jq '.[1].end_time')"
            RESIZE_OPERATION_TIME="$(echo $SUBSEGMENTS | jq '.[1].start_time'); $(echo $SUBSEGMENTS | jq '.[1].end_time')"
        fi
        if [ "$SUB_NAME_2" == "S3" ]; then
#            echo "$t,$(echo $SUBSEGMENTS| jq '.[1].name'), $(echo $SUBSEGMENTS | jq '.[1].start_time'), $(echo $SUBSEGMENTS | jq '.[1].end_time')"
            S3_TIME="$(echo $SUBSEGMENTS | jq '.[1].start_time'); $(echo $SUBSEGMENTS | jq '.[1].end_time')"
        fi
    fi

}

function resolve_segment() {

    DOC=$(echo $1)
    DOC_NAME=$(echo $DOC | jq '.name' | sed -e 's/^"//' -e 's/"$//')
    SUBSEGMENTS=$(echo $DOC | jq '.subsegments')


#    echo "$DOC_NAME ---- $SUBSEGMENTS"


#    if [ SUBSEGMENTS = null ] && [ "$DOC_NAME" = "ImageHandlerXRay" ]; then
#    if [ SUBSEGMENTS = null ]; then
#        echo "ENTRO!!"
#        resolve "$DOC"
#    fi

    if [ "$SUBSEGMENTS" != "null" ]; then
#        echo "RESOLVE!!"

        SUBSEGMENT_0=$(echo $SUBSEGMENTS | jq '.[0]')
        SUBSEGMENT_1=$(echo $SUBSEGMENTS | jq '.[1]')
        SUBSEGMENT_2=$(echo $SUBSEGMENTS | jq '.[2]')
        SUBSEGMENT_3=$(echo $SUBSEGMENTS | jq '.[3]')

        resolve "$SUBSEGMENT_0"
        resolve "$SUBSEGMENT_1"
        resolve "$SUBSEGMENT_2"
        resolve "$SUBSEGMENT_3"
    elif [ "$DOC_NAME" = "ImageHandlerXRay" ]; then
        resolve "$DOC"
    fi

}


#TRACEIDS=(1-5c91a1fd-dd1bee31baee5128f98b77f6 1-5c91a1cf-19da3bb7fbc96ceb66539d20 1-5c91a217-e6c6f3bb28069ab46aa75707 1-5c91a21a-3a9951f2f4a387edf468baee 1-5c91a214-1595cdf86122e5521f16b59e 1-5c91a20d-de39b7786e64a553da3b7701 1-5c91a202-536a4ad2f052a061612aab54 1-5c91a1f8-fd1936f91e76f959cd438483 1-5c91a210-d629b302c5febd8295d20373 1-5c91a208-4b0cbec41bed6ac61a5ff022 1-5c91a205-cc460253a0113ce44e1644e5 1-5c91a1f1-0ffd4af515b78531ec5a677a)
#TRACEIDS=(1-5c91a1fd-dd1bee31baee5128f98b77f6 1-5c91a1cf-19da3bb7fbc96ceb66539d20 1-5c91a205-cc460253a0113ce44e1644e5)
#TRACEIDS=(1-5c91a1fd-dd1bee31baee5128f98b77f6 1-5c91a1cf-19da3bb7fbc96ceb66539d20)
#TRACEIDS=1-5c91a1cf-19da3bb7fbc96ceb66539d20


TRACEIDS=(1-5c9988fa-be3478f6111d478efd719a16 1-5c998918-65fc3750227cae102ceb6a70 1-5c998989-a9f65a521e527b00385abc77 1-5c99895a-78ebbc0781ec301a6085c7fa 1-5c99887b-18f9d20e05577abceb2d58cc 1-5c99895d-e1661d0015b5e50088ae02e0 1-5c998878-e6fcc843a8b6b0f827e63238 1-5c9989ec-3c9a6110c8996c209e17e750 1-5c9989e5-40b56910c3d85536b95fa10c 1-5c9988e5-4413eaf7c60b1d6164f00694 1-5c9989a0-44b9cbd48ce5e8780e656e98 1-5c998948-808ad84631f39e5f70b2ba82 1-5c99899d-0b7aefd083d5c8f0c0ae25f3 1-5c99893d-13289e9b9786add24fa305ef 1-5c9988de-4f2a532877a983d88b905048 1-5c9988ff-c1eebd3bd7bdd62a37714ecc 1-5c998911-1a746e785472a32a466a94b2 1-5c998999-26b65510cd1e993c65607670 1-5c9989ab-e5366cf0f704b4b88f769190 1-5c9988e1-76f0f81c9ee6b4e27159b582 1-5c9988f3-9650a3e0cbc484729f654258 1-5c9989e3-86d910d47aca65b7dafea2a8 1-5c998931-6351697083a54bc2ed63f78a 1-5c9989ea-fd650c904c324fd07f6152d8 1-5c99892f-43305513033ca33b14bebdcc 1-5c99897f-5e8deb0c713e1a1c3ab9d1f4 1-5c9988d7-f9fdf11e53649c3811cb80de 1-5c9989e1-36ba03b3e91dd2cf67c86631 1-5c998972-2c1b53b8323f549054069bd4 1-5c998901-0585c1081cb5070071e401b0 1-5c998976-ca0abc40712e2840d346ef90 1-5c998966-aa3c28f0624010060e570a10 1-5c99893b-128f5444aa19e78d41bf7491 1-5c99896f-46dbc2f0022bd550c04baf60 1-5c99896b-d0f6a75856a9bde0e4f6c5e8 1-5c9989a6-e3ef78d4f10c6f0c9e1edc18 1-5c9988f1-bda10caba7c28607e53ea710 1-5c998904-02375309aaec8420db9f7686 1-5c99898b-ee2933521e0d988c9b23e908 1-5c9988ec-0d822d5ca0088e14f5b1641e 1-5c99890f-cf7b91e099a9bc7c590b813c 1-5c998964-b6dfde8798a7b63cf46bf12b 1-5c998974-f807617069437850849d34e0 1-5c9988f6-063d6f26e49e44041e9aef20 1-5c998984-3a94dab3c8bfb5a2835bcd6e 1-5c9988da-1b8ff2f69c8ab57527b0d2ff 1-5c99897b-aa1085900b74d22896514ac0 1-5c998934-e78fa4521d83ba2c3f176a60 1-5c9989a4-1a29136efd82a3d51a8245b3 1-5c998938-e525b6d06e49c67ec0395172 1-5c998944-9c6a35b44ff9a9b397c38baf 1-5c998908-866376a4405409f7a124a704 1-5c998992-ea75ef8419bcd23805433766 1-5c998914-feaedeca7a7efe7efe328642 1-5c9989a9-24d9127376699c2726c2e57d 1-5c998990-ffaaf8fca2cf98eeae0d7f86 1-5c9989e8-1267726dbc8271c2752b53b7 1-5c998906-b968b00801994e14cbf6ef6c 1-5c998953-9439e9c8d13cd2e9493c6b02 1-5c9988dc-d440d599d97bf0e1a553122b 1-5c99891f-331f193af748f66462104ed4 1-5c99890d-6c1bf280a5394d74c85ddaf8 1-5c998876-c1c465f7488640fc916ed393 1-5c998986-5e8cd833dd262d08700e2a6f 1-5c9988ef-a3da538d7532e4b65e97e95a 1-5c99896d-5bdfb0d8d264d9f0741ba52c 1-5c9988fd-8d08acbc8e026e3c16627f48 1-5c998926-5ed1fcd66ae8d68ef191f052 1-5c99898e-f29a259bac1cd688159101d4 1-5c99897d-4ce2fdb472a86a2605d5ce74 1-5c998936-cbeff02a8b1864bf5265a4ba 1-5c99894f-5364de3602d751aafa50e3e0 1-5c998958-f14f821b14edd8914ab26389 1-5c99891d-f2dc3848444914c888208c82 1-5c9988e3-96e42d564ff3a322cd122b7f 1-5c99891a-a31df960434eeca0d9cd389c 1-5c998962-f64919850b3a3d232d514be7 1-5c9988f8-49fca474f8435ceaf954783e 1-5c998969-1530e945050c79489fb65f51 1-5c99895f-f7f6fd7ad41977a9b01bf2e8 1-5c998978-e556e65c2fb221e4a7c75606 1-5c99892b-613335d8c34b26d24b81e926 1-5c9989a2-5ab86830454a8610a3052798 1-5c9988e8-bd55fe7fda72eb5571042ccb 1-5c99887d-47cf2b08a49d3881dd7baec6 1-5c998994-45b6701fbf01fb288662dbf1 1-5c99894a-aaf942704cb2a0a179769159 1-5c99894d-f6cd24d1c8686606d01515b5 1-5c998942-462dcd22382f24d2a184e692 1-5c998916-aa94f0002daf6e0041ce0000 1-5c9988ea-63a427e196c59e47a5e5e14b 1-5c998956-1bc20ae62ced610672658f9c 1-5c998982-4f6c643e84ac2568f47bb788 1-5c998924-e62dc380bc3c04e01452b5a0 1-5c998928-82300180856ad65016484809 1-5c99890a-2bb2ec5c332154690ecf5757 1-5c998997-174683b649dfafd96e3d2e66 1-5c99852c-39ba8455e0aac5ab010ff3b6 1-5c99852a-fb8bdef27a0565005c84cd40 1-5c9983e3-6604b29c3e6fdacc5f057eac)
echo "x-ray-id; request-id; lambda-init; lambda-end; initialization-init; initialization-end; input-init; input-end; resize-init; resize-end; s3-init; s3-end; resize-oper-init; resize-oper-end; write-init; write-end"  >> xray-data/log-25-03-2019.csv
for t in ${TRACEIDS[@]} ; do
#    echo "ID: $t"
#    aws xray batch-get-traces --trace-ids $t --output text

    SEGMENT_0=$(aws xray batch-get-traces --trace-ids $t --query 'Traces[*].Segments[0].Document' --output text)
    SEGMENT_1=$(aws xray batch-get-traces --trace-ids $t --query 'Traces[*].Segments[1].Document' --output text)
    SEGMENT_2=$(aws xray batch-get-traces --trace-ids $t --query 'Traces[*].Segments[2].Document' --output text)

#    echo "SEG 1: $SEGMENT_0"
#    echo "SEG 2: $SEGMENT_1"
#    echo "SEG 3: $SEGMENT_2"

#    echo "REQ ID: $t"
    resolve_segment "$SEGMENT_0"
    resolve_segment "$SEGMENT_1"
    resolve_segment "$SEGMENT_2"


    if [ "$INITIALIZATION_TIME" = "" ]
    then
          echo "INITIALIZATION_TIME is empty"
          INITIALIZATION_TIME="0; 0"
    fi

    echo "INIT $INITIALIZATION_TIME"
    echo "$t - $AWS_LAMBDA_REQ_ID"
    echo "$t; $AWS_LAMBDA_REQ_ID; $LAMBDA_ROOT; $INITIALIZATION_TIME; $INPUT_EVENT_TIME; $RESIZE_EVENT_TIME; $S3_TIME; $RESIZE_OPERATION_TIME; $WRITE_RESPONSE_TIME" >> xray-data/log-25-03-2019.csv

    unset LAMBDA_ROOT
    unset AWS_LAMBDA_REQ_ID
    unset INITIALIZATION_TIME
    unset INPUT_EVENT_TIME
    unset RESIZE_EVENT_TIME
    unset S3_TIME
    unset RESIZE_OPERATION_TIME
    unset WRITE_RESPONSE_TIME



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


# DoublePDF[(0.124; 0.3)(0.723; 0.2)(0.179; 0.5)]

#1553562000000
#
#1553566500000
#
#aws xray get-trace-summaries --start-time 1553562000000 --end-time 1553566500000 --query 'TraceSummaries[*].Id' --output text