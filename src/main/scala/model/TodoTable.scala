package model

import message.todo.{Pending, Status, Todo}

class TodoTable(val db: DatabaseProfile) {

  import db.db.profile.api._

  implicit val TodoTableColumnType = MappedColumnType.base[Status, String](
    status => Status.toString(status),
    statusString => Status.toStatus(statusString).getOrElse(Pending)
  )

  class TodoTable(tag: Tag) extends Table[Todo](tag, "TODO") {
    def id = column[String]("ID", O.PrimaryKey)
    def task = column[String]("TASK")
    def status = column[Status]("STATUS")

    def * = (id, task, status) <> (Todo.tupled, Todo.unapply)
  }

  val todos = TableQuery[TodoTable]
}