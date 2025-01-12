# Telegram Chrono Bot
### Описание
Телеграм бот для подсчета личного времени.  
Пример работы микросервисной архитектуры. Состоит из нескольких приложений. Приложение Producer принимает и обрабатывает сообщения из телеграма и передаёт их приложению Consumer через Kafka. Consumer пишет данные в базу. Запускается в докере в виде 5ти контейеров:
- producer
- consumer
- database
- kafka
- zookeeper

### Используемые технологии
- Java Oracle OpenJDK
- Spring Boot
- Kafka
- PostgreSQL
- Liquibase
- Lombok
