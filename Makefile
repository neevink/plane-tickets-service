.PHONY: front front-helios

swagger-ui:
	docker run -p 8080:8080 -e SWAGGER_JSON=/tmp/swagger.yaml -e BASE_URL=/swagger -v `pwd`:/tmp swaggerapi/swagger-ui

sh:
	ssh s313087@helios.se.ifmo.ru -p 2222

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
