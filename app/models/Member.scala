package models

import org.joda.time.DateTime
import scala.slick.driver.PostgresDriver.simple._
import com.github.tototoshi.slick.PostgresJodaSupport._

/**
 * ユーザモデル
 * Created by okadaryuichi on 2014/06/18.
 */
case class Member(id:Option[Long],password:String,name:String,createAt:Option[DateTime])

class Members(tag:Tag) extends Table[Member](tag,"member"){

  def id = column[Option[Long]]("id",O.PrimaryKey,O.AutoInc)
  def password = column[String]("password")
  def name = column[String]("name")
  def createAt = column[Option[DateTime]]("create_at",O.AutoInc)
  def * = (id,password,name,createAt) <> (Member.tupled,Member.unapply)

}

object Members extends DAO{

  /**
   * ユーザ名で検索
   * @param name
   * @return
   */
  def findByName(name:String) = db.withSession{implicit session =>
    members.filter(_.name like "%" + name + "%").list()
  }


}