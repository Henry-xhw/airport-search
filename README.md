## Already done:
* rest search airport:
    1. name fuzzy query
    2. iata exact query
* rest search airline and route
    code out some structures
* airport controller unit tests

## Can refactor:
* can use simple factory pattern in index creating part
* can use builder pattern to replace some setter methods
* extract some common files into BaseDto and BaseDomain
* can use org.apache.lucene.search.SearcherManager to manage searcher.
* ...


## Some notes:
1. search airports:

    *   url: 'http://localhost:8273/airports'
    *   content-type: 'application/vnd.henry.airport-search.v1+json'
    *   body:
                {"params":{"id":0,"name":null,"city":null,"country":null,"iata":null,"icao":null,"latitude":0.0,"altitude":0,"timeZone":0.0,"dst":null,"tzFormat":null,"type":null,"source":null,"longitude":0.0},"sort":null,"queryParam":{"searchKeyStr":"Tautii"}}
            
2. search airlines and routes:
    *   url: 'http://localhost:8273/lines_and_routes'
    *   content-type: 'application/vnd.henry.airport-search.v1+json'
    *   body: to be do...