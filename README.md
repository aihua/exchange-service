# Foreign Exchange Rate Service

Running the application

    mvn spring-boot:run
    
Use the service

    curl -i -H "Accept: application/json" localhost:8080/api/status
    curl -i -H "Accept: application/json" localhost:8080/api/currencies
    curl -i -H "Accept: application/json" localhost:8080/api/rates/USD/2016-01-21


