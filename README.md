# Description 
## Long pull telegram bot, what helps you monitor new it-orders in few freelance exchanges (russian)
# Installation
## Create application.properties under resources folder, and set next properties (must be found in docker-compose.yml):
* spring.datasource.url
* spring.datasource.username
* spring.datasource.password
* spring.datasource.driver-class-name
* spring.jpa.properties.hibernate.dialect (org.hibernate.dialect.PostgreSQLDialect)
* bot.username
* bot.token
## Build dockerfile
* docker build --tag '<tag_name>'
## Run docker-compose
* cd ./docker
* docker-compose up
# Demo
![commands](https://github.com/Sprokof/ITFreelanceBot/assets/90979711/2d9dd6c1-3eb7-46ca-a0d0-c16513b07011)
* commands

![orders](https://github.com/Sprokof/ITFreelanceBot/assets/90979711/2344302f-4567-4836-8918-33683f4b28e5)
* orders by subscription