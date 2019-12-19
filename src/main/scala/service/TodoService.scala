package service

import message.todo.Todo
import model.DatabaseProfile
import repository.TodoRepo

class TodoService(val todoRepo: TodoRepo) {
  def getAllTodo(h2DatabaseProfile: DatabaseProfile) = {
    h2DatabaseProfile.db.db.run(todoRepo.getAll)
  }

  def insert(data: Todo)(h2DatabaseProfile: DatabaseProfile) = {
    h2DatabaseProfile.db.db.run(todoRepo.insert(data))
  }
}
