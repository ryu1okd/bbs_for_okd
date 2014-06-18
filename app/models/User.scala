package models

import org.joda.time.DateTime
import play.api.db.DB
import play.api.Play.current
import scala.slick.driver.H2Driver.simple._
import scala.slick.jdbc._
import com.github.tototoshi.slick.H2JodaSupport._

/**
 * ユーザモデル
 * Created by okadaryuichi on 2014/06/18.
 */
case class User(id:Option[Long],password:String,name:String,createAt:Option[DateTime])

class Users(tag:Tag) extends Table[User](tag,"USER"){

  def id = column[Option[Long]]("ID",O.PrimaryKey,O.AutoInc)
  def password = column[String]("PASSWORD")
  def name = column[String]("NAME")
  def createAt = column[Option[DateTime]]("CREATE_AT",O.AutoInc)
  def * = (id,password,name,createAt) <> (User.tupled,User.unapply)

}

object Users extends DAO{

  /**
   * ユーザ名で検索
   * @param name
   * @return
   */
  def findByName(name:String) = db.withSession{implicit session =>
    users.filter(_.name like "%" + name + "%").list()
  }


}