
ssh:
	@echo 'to run: cd ./soa/second-service ; /usr/local/openjdk11/bin/java -jar payara-micro-6.2023.9.jar --sslport 9001 --deploy secservice-1.0-SNAPSHOT.war'
	ssh -L 9001:localhost:9001 s313087@helios.se.ifmo.ru -p 2222


deploy:
	scp -P 2222 ./target/secservice-1.0-SNAPSHOT.war s313087@helios.se.ifmo.ru:/home/studs/s313087/soa/second-service/
	#scp -P 2222 ./payara-micro-6.2023.9.jar s313087@helios.se.ifmo.ru:/home/studs/s313087/soa/second-service/

package:
	mvn package

run:
	java -jar payara-micro-6.2023.9.jar --port 9001 --deploy target/secservice-1.0-SNAPSHOT.war
