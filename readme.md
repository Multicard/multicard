# Multicard


## setup local environment

## Frontend

### start Frontend: 

cd ./multicard-ui/src/main/webapp/

install:    npm install

run:        npm start

URL:        http://localhost:4200/app 



## Backend
compile:      mvn clean install

run:          mvn spring-boot:run -f ./multicard-backend/

swagger:      http://localhost:8080/swagger-ui/index.html#/

database:     http://localhost:8080/h2 (url: jdbc:h2:./db/h2_db; user: sa and pwd is empty)



## running instance in Cloud - Heroku

ATTENTION: Heroku stops all running instances after 30 minutes of inactivity. After each restart or wakeup all data will be lost.

URL:            https://multicardgame.herokuapp.com 

swagger:        https://multicardgame.herokuapp.com/swagger-ui/index.html#/

database:       https://multicardgame.herokuapp.com/h2 (url: jdbc:h2:./db/h2_db; user: sa and pwd is empty)
