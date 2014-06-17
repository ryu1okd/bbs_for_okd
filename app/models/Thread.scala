package models

import org.joda.time.DateTime
import play.api.db.DB
import play.api.Play.current
import scala.slick.driver.H2Driver.simple._
import scala.slick.jdbc._
import com.github.tototoshi.slick.H2JodaSupport._

/**
 * Created by okadaryuichi on 2014/06/17.
 */
case class Thread (id:Option[Long],title:String,userId:Long,createAt:DateTime,updateAt:DateTime)

class Threads(tag:Tag) extends Table[Thread](tag,"THREAD"){

  def id = column[Option[Long]]("ID",O.PrimaryKey,O.AutoInc)
  def title = column[String]("TITLE")
  def userId = column[Long]("USER_ID")
  def createAt = column[DateTime]("CREATE_AT")
  def updateAt = column[DateTime]("UPDATE_AT")

  def * = (id, title, userId, createAt, updateAt) <> (Thread.tupled,Thread.unapply)
}

/**
 * スレッドを扱う
 */
object Threads {

  /* DB */
  val db = Database.forDataSource(DB.getDataSource())

  /* テーブルクエリ */
  val threads = TableQuery[Threads]

  /**
   * 全スレッドを返す
   * @return
   */
  def findAll = db.withSession{ implicit session =>
    threads.list()
  }


  /**
   * タイトルで検索
   * @param title
   * @return
   */
  def find(title: Option[String]) = db.withSession { implicit session =>
    title match {
      case Some(title) => {
        threads.filter(_.title like "%" + title + "%").list()
      }
      case None => this.findAll
    }

  }

}