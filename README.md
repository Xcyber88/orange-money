This is the readme for the Orange Money App.

This app uses a H2 embeded database that saves the information on disk. Username&Password for the H2 DB: orange/money.

In order to have a initial setup of the DB and to populate it with some transactions, use the Json inside the "initialEntries.json"
to do a POST request (via POSTMAN) with the following endpoint: http://localhost:8080/validate-multiple (This is used only for mulptiple transactions only)

If you want to have 0 transactions of a certain type, delete the entries from the "initialEntries" before doing the first WS call.

If you want to do a test transaction, for validation purpose or for reporting purpose, use the following endpoint:
http://localhost:8080/validate-single/{showReport} (the last parameter is true/false, depending if you want to see a report or not)

All the responses and in the Response Body as strings.