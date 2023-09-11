

swagger-ui:
	docker run -p 8080:8080 -e SWAGGER_JSON=/tmp/swagger.yaml -e BASE_URL=/swagger -v `pwd`:/tmp swaggerapi/swagger-ui

sh:
	ssh s313087@helios.se.ifmo.ru -p 2222
