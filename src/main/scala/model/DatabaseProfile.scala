package model

import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile

class DatabaseProfile {
  val db = DatabaseConfig.forConfig[JdbcProfile]("h2mem1")
}
