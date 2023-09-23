.PHONY: front front-helios

swagger-ui:
	docker run -p 8080:8080 -e SWAGGER_JSON=/tmp/swagger.yaml -e BASE_URL=/swagger -v `pwd`:/tmp swaggerapi/swagger-ui

sh:
	ssh s313087@helios.se.ifmo.ru -p 2222

front:
	cd ./front && rm -rf .shadow-cljs && npx shadow-cljs release app

front-helios: front
	scp -P2222 -r  ./front/public s285574@helios.cs.ifmo.ru:~/public_html/soa
