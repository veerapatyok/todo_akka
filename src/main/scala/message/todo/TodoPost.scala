package message.todo

import java.util.UUID

import io.circe.Decoder.Result
import io.circe.{Decoder, DecodingFailure, Encoder, HCursor, Json}

case class TodoPost(task: String,
                    status: Status)

object TodoPostJson {
  def toTodoModel(data: TodoPost, uuid: String) = {
    Todo(uuid, data.task, data.status)
  }

  implicit val encodeTodo = new Encoder[TodoPost] {
    override def apply(a: TodoPost): Json =
      Json.obj(
        ("task", Json.fromString(a.task)),
        ("status", Json.fromString(Status.toString(a.status))))
  }

  implicit val decodeTodo = new Decoder[TodoPost] {
    override def apply(c: HCursor): Result[TodoPost] = {
      for {
        task <- c.downField("task").as[String]
        status <- c.downField("status").as[String]
        todo <- Status.toStatus(status).left.map(e => DecodingFailure(e, Nil)).map(status => TodoPost(task, status))
      } yield todo
    }
  }
}
