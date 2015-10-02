Experimental platform for developing a #GoNamTaxi Hackathon Prototype

#Prerequisites
  * [Node](http:https://nodejs.org/)
  * Google Maps API Keys, for both Server and Browser.
  
#Installation

Clone the repository and install NPM dependencies:
```
git clone https://github.com/abrie/go-nam-taxi.git
cd go-nam-taxi
npm install
```

The server will not work without a file describing the API keys. These should be stored in the ./private directory, but are not checked into the repository for security reasons. You'll need to generate your own keys. Follow the instructions provided by Google:

* https://developers.google.com/maps/documentation/javascript/get-api-key#key
* You need two keys, a Browser Key and a Server Key. The Google Console page [looks like this](/screenshots/create-api-keys.png?raw=true).

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
