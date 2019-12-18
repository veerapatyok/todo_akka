package model.h2

import model.todo.{Pending, Status, Todo}
import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile

class TodoTable(val db: DatabaseConfig[JdbcProfile]) {

  import db.profile.api._

  implicit val TodoTableColumnType = MappedColumnType.base[Status, String](
    status => Status.toString(status),
    statusString => Status.toStatus(statusString).getOrElse(Pending)
  )

  class TodoTable(tag: Tag) extends Table[Todo](tag, "TODO") {
    def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)
    def task = column[String]("TASK")
    def status = column[Status]("STATUS")

    def * = (id, task, status) <> (Todo.tupled, Todo.unapply)
  }

  val todos = TableQuery[TodoTable]

  def createTable = {
    db.db.run(todos.schema.createIfNotExists)
  }

  def insert(data: Todo) = {
    todos += data
  }

  def getAll = {
    todos.result
  }
}