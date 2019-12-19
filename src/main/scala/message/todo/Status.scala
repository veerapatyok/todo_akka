package message.todo

sealed trait Status
case object Pending extends Status
case object Done extends Status

object Status {
  def toString(status: Status): String = status match {
    case Pending => "pending"
    case Done => "done"
  }

  def toStatus(status: String): Either[String, Status] = status.trim.toLowerCase match {
    case "pending" => Right(Pending)
    case "done" => Right(Done)
    case _ => Left("invalid status")
  }
}