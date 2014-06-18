package controllers

import play.api.mvc.{Action, Controller}
import models.{Comments, Comment, Thread, Threads}
import play.api.libs.json.Json
import play.api.data._
import play.api.data.Forms._

/**
 * スレッドAPIアクション
 * Created by okadaryuichi on 2014/06/17.
 */
object ThreadApi extends Controller{

  /**
   * スレッドフォーム
   */
  val threadForm = Form(
    mapping(
      "id" -> optional(longNumber),
      "title" -> nonEmptyText(maxLength = 40),
      "userId" -> longNumber,
      "createAt" -> optional(jodaDate)
    )(Thread.apply)(Thread.unapply)
  )

  /**
   * コメントフォーム
   */
  val commentForm = Form(
    mapping(
      "id" -> optional(longNumber),
      "threadId" -> longNumber,
      "comment" -> nonEmptyText(maxLength = 400),
      "userName" -> optional(text),
      "create_at" -> optional(jodaDate)
    )(Comment.apply)(Comment.unapply)
  )

  implicit def threadFormat = Json.format[Thread]
  implicit def commentFormat = Json.format[Comment]

  /**
   * スレッドタイトルで検索
   * @param title
   * @return
   */
  def find(title:Option[String]) = Action{
    Ok(Json.toJson(Threads.findByTitle(title)))
  }

  /**
   * コメントを取得
   * @param threadId
   * @param page
   * @return
   */
  def detail(threadId:Long,page:Option[Int],comment:Option[String],userName:Option[String]) = Action{
    Ok(Json.toJson(Comments.findCommentsByThreadId(threadId,page,comment,userName)))
  }

  /**
   * 新規スレッド作成
   * @return
   */
  def create = Action{ implicit request =>
    val form = threadForm.bindFromRequest()
    form.fold(
      hasErrors => {
        // パラメータ不正
        BadRequest
      }
      ,success => {
        // 登録処理
        val id = Threads.insert(success)
        // 登録したスレ情報を返す
        Threads.findById(id) match {
          case Some(t) => Ok(Json.toJson(t))
          case None => InternalServerError
        }

      }
    )
  }

  /**
   * コメント投稿
   * @param threadId
   * @return
   */
  def comment(threadId:Long) = Action { implicit request =>
    val form = commentForm.bindFromRequest()
    form.fold(
      hasErrors => {
        BadRequest
      },
      success => {
        // スレッドのコメント件数が10000件なら投稿不可
        if(Comments.countByThreadId(threadId) >= 10000){
          MethodNotAllowed
        } else {
          //コメント登録
          Comments.insert(Comment(None,threadId,success.comment,success.userName,None))
          Ok
        }
      }
    )
  }

}
