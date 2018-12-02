#!/bin/bash

pushd $(dirname $0) > /dev/null
basepath=$(pwd)
popd > /dev/null

cat << EndOfMessage
HELP: 
./client.sh ip_address
	- url: l'addresse url de l'API
	- requestType: type de requête à transmettre
	- fileName: nom du fichier JSON à manipuler

EndOfMessage

java -cp "$basepath"/client.jar -Djava.security.policy="$basepath"/policy ca.polymtl.log8430.tp4.client.Client $*
