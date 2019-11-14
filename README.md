# SantaEMail

- Secret Santa between your friends using email notifications
- Example project for BSUG - http://www.meetup.com/Bucharest-Scala-User-Group/

### How-To
1. Edit the configuration file (`conf/prod.conf`) or create a new one and mount it.
2. Using Docker, publish the image locally and run the container.
```commandline
$ sbt docker:publishLocal

$ docker run -d -p 9000:9000 \
--restart unless-stopped \
--name santa \
radusw/santa-email:1.0 "conf/prod.conf"

$ docker logs santa --follow

$ curl http://localhost:9000/api/version
```
3. Open the browser and go to `http://localhost:9000/app`
