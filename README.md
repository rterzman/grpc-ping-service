**Handle information about ping web urls**

Service is based on Spring-Boot and gRPC frameworks

**Run**
* recompile the project (_mvn clean compile_)
* run test for checking application (_mvn test_)
* startup application from ide or from console (java -jar target/grpc-ping-service-0.0.1-SNAPSHOT.jar)
* implement gRPC client or use from project

Application starts on default host and port (`localhost:6565`). If you want to change port you can change it in `application.yml` file `grpc.port=6565`