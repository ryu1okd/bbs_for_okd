package models

import org.joda.time.DateTime
import scala.slick.driver.H2Driver.simple._
import com.github.tototoshi.slick.H2JodaSupport._

/**
 * コメントモデル
 * Created by okadaryuichi on 2014/06/17.
 */
case class Comment (id:Option[Long],threadId:Long,comment:String,userName:Option[String],createAt:Option[DateTime])

class Comments(tag:Tag) extends Table[Comment](tag,"COMMENT") {
  def id = column[Option[Long]]("ID",O.PrimaryKey,O.AutoInc)
  def threadId = column[Long]("THREAD_ID")
  def comment = column[String]("COMMENT")
  def userName = column[Option[String]]("USER_NAME")
  def createAt = column[Option[DateTime]]("CREATE_AT",O.AutoInc)
  def * = (id,threadId,comment,userName,createAt) <> (Comment.tupled,Comment.unapply)
}

object Comments extends DAO {

  /**
   * スレッド内のコメントを検索する
   * @param threadId
   * @param page
   * @param comment default None(コメント検索なし)
   * @return
   */
  def findCommentsByThreadId(threadId:Long,page:Option[Int],comment:Option[String] = None, userName:Option[String] = None) = db.withSession{ implicit session =>
    // 1ページの件数
    val count = 1000
    // ページ番号に指定がなければ1ページ目
    val pageNo = page match {
      case Some(p) => p
      case None => 1
    }

    // query
    var query = comments.filter(_.threadId === threadId)
    if(comment.isDefined) query = query.filter(_.comment like "%"+ comment.get +"%")
    if(userName.isDefined) query = query.filter(_.userName like "%"+ userName.get +"%")
    query = query.sortBy(_.id).take(count * pageNo)
    // 2ページ目以降はオフセット位置を進める
    if(pageNo > 1) query = query.drop(count * (pageNo - 1))

    query.list()
  }

  /**
   * コメント登録
   * @param comment
   * @return
   */
  def insert(comment:Comment) = db.withSession {implicit session =>
    comments += comment
  }

  /**
   * スレッド内のコメント数を返す
   * @param threadId
   * @return
   */
  def countByThreadId(threadId:Long) = db.withSession {implicit session =>
    Query(comments.filter(_.threadId === threadId).length).first
  }

}