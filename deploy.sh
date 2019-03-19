#!/bin/bash

mvn clean package shade:shade

sam package --template-file template.yaml --output-template-file packaged.yaml --s3-bucket image-handler-artifact-bucket

sam deploy --template-file ./packaged.yaml --stack-name image-handler-simple-measuring-stack --capabilities CAPABILITY_IAM