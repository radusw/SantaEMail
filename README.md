```commandline
$sbt docker:publishLocal
$docker run -d -p 9000:9000 --restart unless-stopped --name santa radusw/santa-email:1.0

$docker logs santa --follow

$curl http://localhost:9000/api/version
```

Open the browser and go to:
 * `http://localhost:9000/app`
