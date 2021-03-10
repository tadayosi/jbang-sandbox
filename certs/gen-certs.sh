#!/bin/sh

CNAME="cn=localhost,ou=Camel,o=Apache"

SERVER_KEY=server-keystore.jks
CLIENT_KEY=client-keystore.jks
CLIENT_TRUST=client-truststore.jks

keytool -genkey -alias server -keyalg RSA -keystore $SERVER_KEY -keypass secret -storepass secret -validity 7300 -storetype JKS -dname $CNAME
keytool -genkey -alias client -keyalg RSA -keystore $CLIENT_KEY -keypass secret -storepass secret -validity 7300 -storetype JKS -dname $CNAME
keytool -export -alias server -keystore $SERVER_KEY -file server -storepass secret
keytool -import -alias server -keystore $CLIENT_TRUST -file server -storetype JKS -storepass secret --no-prompt

rm server
