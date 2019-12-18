# Todo app with Akka http, Circe and Slick

this server use port `8080`

## API
### POST
create todo task
`path` = `/todos`

json format
```json
{
  "task": "test(String)",
  "status": "done(pending | done)"
}
```

return `200`

### GET
get all todo task

`path` = `/todos`

return list of todo task