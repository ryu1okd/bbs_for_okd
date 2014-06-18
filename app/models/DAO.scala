package models

import play.api.db.DB
import scala.slick.driver.H2Driver.simple._
import play.api.Play.current

/**
 * Created by okadaryuichi on 2014/06/18.
 */
protected trait DAO{

  /* DB */
  val db = Database.forDataSource(DB.getDataSource())

  /* テーブルクエリ */
  val threads = TableQuery[Threads]
  val comments = TableQuery[Comments]
  val users = TableQuery[Users]
}
