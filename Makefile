.PHONY: front front-helios

swagger-ui:
	docker run -p 8080:8080 -e SWAGGER_JSON=/tmp/swagger.yaml -e BASE_URL=/swagger -v `pwd`:/tmp swaggerapi/swagger-ui

sh:
	ssh -L 8500:localhost:8500 -L 18800:localhost:18800 -L 18888:localhost:18888 s313087@helios.se.ifmo.ru -p 2222

deploy-helios:
	#scp -P2222 back/first-service/target/soa-0.0.1-SNAPSHOT.jar s313087@helios.cs.ifmo.ru:~/soa/soa-0.0.1-SNAPSHOT.jar
	scp -P2222 Makefile s313087@helios.cs.ifmo.ru:~/soa/Makefile
	scp -P2222 back/haproxy/haproxy.cfg s313087@helios.cs.ifmo.ru:~/soa/haproxy.cfg
	scp -P2222 back/haproxy/mydomain.pem s313087@helios.cs.ifmo.ru:~/soa/mydomain.pem

	#second-service
	scp -P2222 back/Eureka-Ribbon-Zuul/eureka-naming-server/target/eureka-naming-server-0.0.1-SNAPSHOT.jar s313087@helios.cs.ifmo.ru:~/soa/eureka-naming-server-0.0.1-SNAPSHOT.jar
	scp -P2222 back/Eureka-Ribbon-Zuul/server-app/target/server-app-0.0.1-SNAPSHOT.jar s313087@helios.cs.ifmo.ru:~/soa/server-app-0.0.1-SNAPSHOT.jar
	scp -P2222 back/Eureka-Ribbon-Zuul/zuul-api-server/target/zuul-api-server-0.0.1-SNAPSHOT.jar s313087@helios.cs.ifmo.ru:~/soa/zuul-api-server-0.0.1-SNAPSHOT.jar

run-haproxy-on-helios:
	../haproxy/haproxy -m 128 -f ./haproxy.cfg &

run-consul-on-helios:
	CONSUL_LOCAL_CONFIG='{"server": true}' ../consul/consul agent -dev -client=0.0.0.0 -server-port=18726 -serf-wan-port=18727 -serf-lan-port=18728 -dns-port=18729 -grpc-tls-port=18730 &

run-first-service-on-helios:
	PORT=18801 java -jar soa-0.0.1-SNAPSHOT.jar &
	sleep 3
	PORT=18802 java -jar soa-0.0.1-SNAPSHOT.jar &

run-eureka-naming-server-on-helios:
	java -jar -Xmx128m -Xms128m eureka-naming-server-0.0.1-SNAPSHOT.jar &
	sleep 10

run-server-app-on-helios:
	java -jar -Dspring.profiles.active=inst1 -Xmx128m -Xms128m server-app-0.0.1-SNAPSHOT.jar &
	sleep 10
	java -jar -Dspring.profiles.active=inst1 -Xmx128m -Xms128m server-app-0.0.1-SNAPSHOT.jar &
	sleep 10

run-zuul-api-server-on-helios:
	java -jar -Xmx128m -Xms128m zuul-api-server-0.0.1-SNAPSHOT.jar &
	sleep 10

front:
	cd ./front && rm -rf .shadow-cljs && npx shadow-cljs release app

front-helios: front
	scp -P2222 -r  ./front/public s285574@helios.cs.ifmo.ru:~/public_html/soa

build-second:
	cd back/Eureka-Ribbon-Zuul && mvn clean install

move-second: build-second
	rm -rf helios
	mkdir -p helios
	cp -r back/Eureka-Ribbon-Zuul/eureka-naming-server/target/eureka-naming-server-0.0.1-SNAPSHOT.jar helios
	cp -r back/Eureka-Ribbon-Zuul/server-app/target/server-app-0.0.1-SNAPSHOT.jar helios
	cp -r back/Eureka-Ribbon-Zuul/zuul-api-server/target/zuul-api-server-0.0.1-SNAPSHOT.jar helios

run-local-second: move-second
	java -jar helios/eureka-naming-server-0.0.1-SNAPSHOT.jar &
	sleep 10
	java -jar -Dspring.profiles.active=inst1  helios/server-app-0.0.1-SNAPSHOT.jar &
	sleep 10
	java -jar -Dspring.profiles.active=inst2  helios/server-app-0.0.1-SNAPSHOT.jar &
	sleep 10
	java -jar -Dspring.profiles.active=inst3  helios/server-app-0.0.1-SNAPSHOT.jar &
	sleep 10
	java -jar helios/zuul-api-server-0.0.1-SNAPSHOT.jar &
	sleep 10

kill-all:
	pkill -f 'java -jar'
