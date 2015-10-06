#GoNamTaxi Hackathon Prototype 2015
Developed under the team moniker of 'Logic++'

#Description
There are two components to this system.
* The Server, providing a ticket transaction API and a web based administration interface.
* Till for Taxi, an Android app which scans coded tickets and provides transaction logging coupled to GPS coordinates.
* A third component for counting taxi passengers was prototyped but not completed by the deadline.


###Admin Interface
[<img src="README.images/admin.png" width="500">](README.images/admin.png)

###TillApp
[<img src="README.images/barcode.png" width="250">](README.images/barcode.png)
[<img src="README.images/tillapp.png" width="250">](README.images/tillapp.png)

#Server Prerequisites
  * [Node](http:https://nodejs.org/)
  * Google Maps API Keys, for both Server and Browser.
  
#Server Installation

Clone the repository and install NPM dependencies:
```
git clone https://github.com/abrie/go-nam-taxi.git
cd go-nam-taxi
npm install
```

The server will not work without a file describing the API keys. These should be stored in the ./private directory, but are not checked into the repository for security reasons. You'll need to generate your own keys. Follow the instructions provided by Google:

* https://developers.google.com/maps/documentation/javascript/get-api-key#key
* You'll need both a Browser Key and a Server Key.

#Running the application

Install your Google API keys into a file named 'api keys.json' into the private/ directory:
```
{
  "google_maps_server_key":"paste-your-server-key-here",
  "google_maps_browser_key":"paste-your-browser-key-here"
}
```

You may then start the server:

```
node server.js
```
This will start a server on localhost, and if all is good you'll be greeted with the following:
```
#GoNamTaxi Prototype Server
````
The server is then listening on port 8080. You may then open a browser and connect to the server using the url: [http://localhost:8080/client](http://localhost:8080/client).
