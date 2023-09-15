:: ssh soa "rm -rf ~/wildfly-21.0.0.Final/standalone1/deployments/first-service.*"
ssh soa "rm -rf ~/first-service-{1,2}.jar"
ssh soa "rm -rf ~/wildfly-21.0.0.Final/standalone2/deployments/second-service*"
ssh soa "rm -rf ~/wildfly-21.0.0.Final/standalone21/deployments/second-service*"

:: scp "first-service/target/first-service.war" soa:~/wildfly-21.0.0.Final/standalone1/deployments
scp "first-service/builds/first-service-*.jar" soa:~/
:: scp "second-service/backend/target/second-service-backend.war" soa:~/wildfly-21.0.0.Final/standalone2/deployments
:: scp "second-service/EJB/target/second-service-ejb.war" soa:~/wildfly-21.0.0.Final/standalone2/deployments
scp "second-service/builds/1/second-service-*.war" soa:~/wildfly-21.0.0.Final/standalone2/deployments
scp "second-service/builds/2/second-service-*.war" soa:~/wildfly-21.0.0.Final/standalone21/deployments
