package repository

import message.todo.Todo
import model.TodoTable

class TodoRepo(val todoTable: TodoTable) {
  import todoTable.db.db.profile.api._

  def createTable = {
    todoTable.todos.schema.createIfNotExists
  }

  def insert(data: Todo) = {
    todoTable.todos += data
  }

  def getAll = {
    todoTable.todos.result
  }
}
