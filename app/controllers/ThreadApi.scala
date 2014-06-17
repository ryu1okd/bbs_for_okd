package controllers

import play.api.mvc.{Action, Controller}
import models.{Thread, Threads}
import play.api.libs.json.Json

/**
 * Created by okadaryuichi on 2014/06/17.
 */
object ThreadApi extends Controller{

  implicit val threadFormat = Json.format[Thread]

  /**
   * 全件
   * @return
   */
  def index = Action {
    Ok(Json.toJson(Threads.findAll))
  }

  /**
   * スレッドタイトルで検索
   * @param title
   * @return
   */
  def search(title:Option[String]) = Action{
    Ok(Json.toJson(Threads.find(title)))
  }

}
