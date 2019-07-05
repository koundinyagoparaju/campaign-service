mvn clean install
chmod +x ./target/campaign-service-1.0-SNAPSHOT.jar
java -jar ./target/campaign-service-1.0-SNAPSHOT.jar server config.yml
