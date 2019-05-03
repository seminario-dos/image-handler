#!/bin/bash

NUMBER_OF_IMAGES=50
NUMBER_OF_IMAGE_VERSIONS=20
IMAGE_COUNTER=1

# Para generar el tamano aleatorio
MIN_NUMBER=100
MAX_NUMBER=1800

FOLDER_NAME=cluster-a
LOCAL_FOLDER_NAME=img/$FOLDER_NAME


IMAGE_IDS=$(curl -s https://picsum.photos/v2/list?page=1\&limit=$NUMBER_OF_IMAGES | jq '.[] | .id' | sed -e 's/^"//' -e 's/"$//' | tr "\n" " ")

echo $IMAGE_IDS

for t in ${IMAGE_IDS[@]} ; do

    echo "image number : $t"
    echo "=================="

    for (( v=0; v<$NUMBER_OF_IMAGE_VERSIONS; v++ )); do
        echo "Version #$v"

        RANDOM_IMAGE_SIZE=$(jot -r 1 $MIN_NUMBER $MAX_NUMBER)
        LOCAL_IMAGE_NAME="$LOCAL_FOLDER_NAME/pic-$IMAGE_COUNTER.jpeg"
        S3_KEY_NAME="$FOLDER_NAME/pic-$IMAGE_COUNTER.jpeg"
        IMAGE_URL="https://picsum.photos/id/$t/$RANDOM_IMAGE_SIZE"

        IMAGE_COUNTER=$((IMAGE_COUNTER + 1))

        wget -O $LOCAL_IMAGE_NAME $IMAGE_URL

        aws s3api put-object --bucket images-repo.dev --key $S3_KEY_NAME --body $LOCAL_IMAGE_NAME

        echo "$RANDOM_IMAGE_SIZE - $S3_KEY_NAME - $LOCAL_IMAGE_NAME - $IMAGE_URL - $IMAGE_COUNTER"

        rm $LOCAL_IMAGE_NAME

    done

done

