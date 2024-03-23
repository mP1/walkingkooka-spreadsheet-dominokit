# walkingkooka-spreadsheet-dominokit
A fat browser spreadsheet using the wonderful dominoKit library.

# Client / Server

The spreadsheet is a combination of a rich web application which executes in the browser and a variety of services
provided
by a server. All the server code transpiles to javascript so it should be possible to move the server into web workers.

## Server

Run jetty server
```
git clone walkingkooka-spreadsheet-server-platform

java walkingkooka.spreadsheet.server.platfor.JettyHttpServerSpreadsheetHttpServer2
```

This will provide a variety of services that are consumed by the rich web (this repo).

## Client

Run GWT code server.
```bash
mvn build-helper:add-source gwt:codeserver -X -f pom.xml
```

Open in a browser, this will load the rich web-app, ready to be used.
```bash
http://localhost:12345/walkingkooka.spreadsheet.dominokit.App/index.html
```

# Architecture | Design

- [HistoryToken](https://github.com/mP1/walkingkooka-spreadsheet-dominokit/blob/master/src/main/java/walkingkooka/spreadsheet/dominokit/history/HistoryToken.java#L71)
- [SpreadsheetDeltaFetcher](https://github.com/mP1/walkingkooka-spreadsheet-dominokit/blob/master/src/main/java/walkingkooka/spreadsheet/dominokit/net/SpreadsheetDeltaFetcher.java#L34)
- [SpreadsheetMetadataFetcher](https://github.com/mP1/walkingkooka-spreadsheet-dominokit/blob/master/src/main/java/walkingkooka/spreadsheet/dominokit/net/SpreadsheetMetadataFetcher.java#L34)