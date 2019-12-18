package model.h2

import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile

object H2Profile {
  val db = DatabaseConfig.forConfig[JdbcProfile]("h2mem1")
}
