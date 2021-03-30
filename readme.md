# Multicard


## local development

## Frontend

###start Frontend: 
install:    npm install --prefix ./multicard-ui/src/main/webapp/

run:        npm start --prefix ./multicard-ui/src/main/webapp/

URL:        http://localhost:4200/app 

### build Frontend
run build:      ./multicard-ui/src/main/webapp/npm build

run prod build: ./multicard-ui/src/main/webapp/npm build--prd

The build artifacts will be stored in the `dist/` directory


## Backend with Interface
compile:      mvn clean install -f ./multicard-api
compile:      mvn clean install -f ./multicard-backend/

run:          mvn spring-boot:run -f ./multicard-backend/


## running instance in Cloud - Heroku

URL:            https://multicardgame.herokuapp.com 
