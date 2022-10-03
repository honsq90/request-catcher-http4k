# RequestCatcher

![Docker Image Version (latest by date)](https://img.shields.io/docker/v/honsq90/request-catcher-http4k?label=docker&style=plastic)

## How it works
- Uses [http4k](https://http4k.org) as a server
- Uses [Vue.js](https://v3.vuejs.org) to augment the frontend
- Uses Websockets to broadcast requests received
- Uses [mini.css](https://minicss.org/) as a lightweight framework
- Renders the received requests on the left in reverse order
- Can play/pause requests
- Can clear existing requests
- Can filter requests by payload

![](./docs/page.png)

### Running the published Docker image

https://hub.docker.com/repository/docker/honsq90/request-catcher-http4k

`docker run -p 9000:9000 honsq90/request-catcher-http4k`

### Building and running locally
```
./gradlew build distZip
docker compose up --build
```

### Example

Visit [http://localhost:9000/hello](http://localhost:9000/hello)

To test from the command line:
```shell
curl -X PUT -d '{"json":"body"}' http://localhost:9000/hello
curl -X PUT -d 'Hello World!' http://localhost:9000/hello
```
or in the browser Javascript console
```javascript
setInterval(() => {
    fetch("/hello", {method: "POST", body: JSON.stringify({hello: "world"})})
}, 1500)
```
