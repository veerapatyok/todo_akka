package message.todo

import io.circe.{Encoder, Json}

case class Todo(id: String,
                task: String,
                status: Status)

object TodoJson {
  implicit val encodeTodo = new Encoder[Todo] {
    override def apply(a: Todo): Json =
      Json.obj(("id", Json.fromString(a.id)),
        ("task", Json.fromString(a.task)),
        ("status", Json.fromString(Status.toString(a.status))))
  }
}