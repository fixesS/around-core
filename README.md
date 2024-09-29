# Around core
## Installing
`git clone https://github.com/fixesS/around-core.git`
## Before developing
### Application properties
#### Just run
1. Fill application-properties file
2. Create .env file from 'env.example'
3. Fill .env file
4. enable .env in "Edit Configurations" option
#### Developing
1. Create 'application-dev.properties' file from original properties
2. Fill application-dev.properties file
3. Create .env file from 'env.example'
4. Fill .env file 
5. Use 'dev' profile by enabling it in "Edit Configurations" option
6. Do not forget to enable .env in "Edit Configurations" option
### Docker-compose
Run with docker-compose.yml
### RabbitMQ
Fill your .env
### PostgreSQL
Fill your .env
## WebSockets
We are using stomp over WebSockets.
### Authorizing
#### JWT
1. Get access token from `auth/login`
2. Set header Authorization with "Bearer <token>"
3. Connect `/ws`

### login and passcode
1. Set header login with email
2. Set header passcode with password
3. Connect `/ws`

### Usage
- Subscribe to `/topic/chunk.event` (type: List<ChunkDTO>)
- Subscribe to `/user/exchange/private.message/error` (type: ApiError)
- Send messages to `topic/chunk.changes` (type: ChunkDTO)



