# Multicard


## local development

## Frontend

### start Frontend: 
install:    npm install --prefix ./multicard-ui/src/main/webapp/

run:        npm start --prefix ./multicard-ui/src/main/webapp/

URL:        http://localhost:4200/app 

### build Frontend
run build:      ./multicard-ui/src/main/webapp/npm build

run prod build: ./multicard-ui/src/main/webapp/npm build--prd

The build artifacts will be stored in the `dist/` directory


## Backend with Interface
compile:      mvn clean install

run:          mvn spring-boot:run -f ./multicard-backend/

swagger:      http://localhost:8080/swagger-ui/index.html#/

database:     http://localhost:8080/h2 (url: jdbc:h2:./db/h2_db; user: sa and pwd is empty)


## running instance in Cloud - Heroku

ATTENTION: Heroku stops all running instances after 30 minutes of inactivity. After each restart or wakeup all data will be lost.

URL:            https://multicardgame.herokuapp.com 

swagger:        https://multicardgame.herokuapp.com/swagger-ui/index.html#/

database:       https://multicardgame.herokuapp.com/h2 (url: jdbc:h2:./db/h2_db; user: sa and pwd is empty)
