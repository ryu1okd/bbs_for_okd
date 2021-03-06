package models

import org.joda.time.DateTime
import scala.slick.driver.PostgresDriver.simple._
import com.github.tototoshi.slick.PostgresJodaSupport._

/**
 * スレッドモデル
 * Created by okadaryuichi on 2014/06/17.
 */
case class Thread (id:Option[Long],title:String,userId:Long,createAt:Option[DateTime])
//TODO JsonFormat時にDateTimeを日付文字列に整形するFormat



class Threads(tag:Tag) extends Table[Thread](tag,"thread"){

  def id = column[Option[Long]]("id",O.PrimaryKey,O.AutoInc)
  def title = column[String]("title")
  def userId = column[Long]("user_id")
  def createAt = column[Option[DateTime]]("create_at",O.AutoInc)

  def * = (id, title, userId, createAt) <> (Thread.tupled,Thread.unapply)
}

/**
 * スレッドを扱う
 */
object Threads extends DAO{

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
  def findByTitle(title: Option[String]) = db.withSession { implicit session =>
    title match {
      case Some(title) => {
        threads.filter(_.title like "%" + title + "%").list()
      }
      case None => this.findAll
    }
  }

  /**
   * IDで検索
   * @param id
   * @return
   */
  def findById(id:Long) = db.withSession { implicit session =>
    threads.filter(_.id === id).firstOption()
  }

  /**
   * 登録
   * @param thread
   * @return
   */
  def insert(thread:Thread) = db.withTransaction {implicit session =>
    threads += thread
  }

}