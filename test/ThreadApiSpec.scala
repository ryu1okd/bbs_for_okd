import models.{Comment, Comments}
import org.json.JSONString
import org.specs2.execute
import org.specs2.specification._
import org.specs2.execute.AsResult
import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._

import play.api.libs.json.Json
import play.api.mvc.AnyContentAsFormUrlEncoded
import play.api.Play
import play.api.test._
import play.api.test.Helpers._

/**
 * Created by okadaryuichi on 2014/06/17.
 */
@RunWith(classOf[JUnitRunner])
class ThreadApiSpec extends Specification{

  def dateIs(date: java.util.Date, str: String) = new java.text.SimpleDateFormat("yyyy-MM-dd").format(date) == str


  //  args(skipAll = true)
  abstract class WithDbData extends WithApplication(FakeApplication(additionalConfiguration = inMemoryDatabase(name = "default", options = Map("DB_CLOSE_DELAY" -> "-1")))) {
    override def around[T: AsResult](t: => T): execute.Result = super.around {
      setupData()
      t
    }

    def setupData() {
      //スレッド2に対して9999件のコメント作成
      for(i <- 1 to 9999) Comments.insert(Comment(None, 2.toLong, "コメント"+i, Some("OKD" + i), None))
    }


  }

  "ThreadAPI" should{

    " tests" in new WithDbData {
//
        // 全スレッド取得
        val Some(threads) = route(FakeRequest(GET,"/api/thread"))

        status(threads) must equalTo(OK)
        contentType(threads) must beSome.which(_ == "application/json")
        contentAsString(threads) must contain ("タイトル1")

        // 「ル1」でスレッド名検索
        val Some(threadsTitle) = route(FakeRequest(GET, "/api/thread?title=ル1"))

        status(threadsTitle) must equalTo(OK)
        contentType(threadsTitle) must beSome.which(_ == "application/json")
        contentAsString(threadsTitle) must contain ("タイトル1")
        contentAsString(threadsTitle) mustNotEqual contain ("タイトル2")

        // スレッド2内　コメント検索
        val Some(thread2searchComment) = route(FakeRequest(GET,"/api/thread/2?comment=ント9999"))
        contentType(thread2searchComment) must beSome.which(_ == "application/json")
        contentAsString(thread2searchComment) must contain ("コメント9999")
        contentAsString(thread2searchComment) mustNotEqual contain ("コメント9998")

      // スレッド2内　ユーザー名検索
        val Some(thread2searchUser) = route(FakeRequest(GET,"/api/thread/2?userName=OKD9999"))
        contentType(thread2searchUser) must beSome.which(_ == "application/json")
        contentAsString(thread2searchUser) must contain ("コメント9999")
        contentAsString(thread2searchUser) mustNotEqual contain ("コメント9998")

      // スレッド2の9ページ目を取得
        val Some(thread2Page9) = route(FakeRequest(GET, "/api/thread/2?page=9"))
        status(thread2Page9) must equalTo(OK)
        contentAsString(thread2Page9) must contain ("コメント8001")
        contentAsString(thread2Page9) must contain ("コメント9000")
        contentAsString(thread2Page9) mustNotEqual contain ("コメント9001")

      // コメント投稿用パラメータ
        val params = Json.toJson(Map("threadId" -> "2","comment" -> "test","userName" -> "おかだ"))
      // コメント投稿(10000件目)
        val Some(comment10000) = route(FakeRequest(POST, "/api/thread/2").withJsonBody(params))
        status(comment10000) must equalTo(OK)
      // コメント投稿(10001件目)
        val Some(comment10001) = route(FakeRequest(POST, "/api/thread/2").withJsonBody(params))
        status(comment10001) must equalTo(METHOD_NOT_ALLOWED)

      // コメント投稿400文字
        val comment = s"""1234567890123456789012345678901234567890
                      |1234567890123456789012345678901234567890
                      |1234567890123456789012345678901234567890
                      |1234567890123456789012345678901234567890
                      |1234567890123456789012345678901234567890
                      |1234567890123456789012345678901234567890
                      |1234567890123456789012345678901234567890
                      |1234567890123456789012345678901234567890
                      |1234567890123456789012345678901234567890
                      |1234567890123456789012345678901234567890"""
        val paramComment400 = Json.toJson(Map("threadId" -> "2","comment" -> comment,"userName" -> "おかだ"))
        val Some(commentEqual400) = route(FakeRequest(POST,"/api/thread/1").withJsonBody(paramComment400) )
        status(commentEqual400) must equalTo(BAD_REQUEST)
    }
  }

}
