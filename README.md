# walkingkooka-spreadsheet-dominokit
A fat browser spreadsheet using the wonderful dominoKit library.



# GWT Code server

Run jetty server
```
git clone walkingkooka-spreadsheet-server-platform

java walkingkooka.spreadsheet.server.platfor.JettyHttpServerSpreadsheetHttpServer2
```



Run GWT code server
```bash
mvn build-helper:add-source gwt:codeserver -X -f pom.xml
```



Open in a browser
```bash
http://localhost:12345/walkingkooka.spreadsheet.dominokit.App/index.html
```