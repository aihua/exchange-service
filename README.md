# Foreign Exchange Rate Service

Provide a simple API to fetch euro foreign exchange rates.

## Data Source

Exchange rates as provided by [European Central Bank](http://www.ecb.europa.eu/stats/exchange/eurofxref/html/index.en.html)

## Under the hood

* On application startup, the rates are imported from ECB XML feed.
* Uses both daily and 90-day-historic XML feeds.
* Rates are stored in an in-memory database.
* Rates are updated every hour.

## Lets go!

NOTE: this project uses [Lombok](https://projectlombok.org/). For full IDE support you might need to install a plugin.

Running the application

    mvn spring-boot:run
    
Use the service

    curl -i -H "Accept: application/json" localhost:8080/api/status
    curl -i -H "Accept: application/json" localhost:8080/api/currencies
    curl -i -H "Accept: application/json" localhost:8080/api/rates/USD/2016-01-21

## Endpoints

### `/api/status`

Response:

    {"isReady":true}

`isReady:false` until rates have been loaded from webservice for the first time.
`isReady:true`  after rates have been loaded once.

### `/api/currencies`

Provides all strings available to use as `currency` parameter when querying `/api/rates/{currency}/{date}`

Response:

    ["AUD","BGN","BRL","CAD","CHF","CNY","CZK","DKK","GBP","HKD","HRK","HUF","IDR","ILS","INR","JPY","KRW","MXN","MYR","NOK","NZD","PHP","PLN","RON","RUB","SEK","SGD","THB","TRY","USD","ZAR"]


### `/api/rates/{currency}/{date}`

Path-Variables:
* `{currency}` use any string as provided by `/api/currencies`
* `{date}` [ISO 8601 formatted date](https://en.wikipedia.org/wiki/ISO_8601)

Request & Response:
    
    curl -H "Accept: application/json" localhost:8080/api/rates/USD/2016-01-21
    {"rate":1.09}
    
Where '1.09' is the foreign exchange rate for EUR to USD on 2016-01-21.

Possible http-status codes:

* 200 - requested rate found and returned
* 404 - requested rate not found for currency & date combination (history is until 90 days back, no rates on weekends)
* 503 - if rates have not been loaded yet. corresponds to `{"isReady":false}` of `/api/status`

## Improvement ideas

* expose metrics/health/etc. (e.g. via [spring boot actuator](https://docs.spring.io/spring-boot/docs/current/reference/html/production-ready-enabling.html)
* make scheduling bullet-proof
* improve error handling in case rates-update fails
* add [HATEOAS](https://en.wikipedia.org/wiki/HATEOAS)
* add interactive documentation with [swagger](http://swagger.io/)
