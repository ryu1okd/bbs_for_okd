import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import play.api.test.Helpers._
import play.api.test.{FakeRequest, WithApplication}

/**
 * Created by okadaryuichi on 2014/06/17.
 */
@RunWith(classOf[JUnitRunner])
class ThreadApiSpec extends Specification {

  "Thread" should{
    "/api/threadへアクセス" in new WithApplication{
      val thread = route(FakeRequest(GET, "/api/thread")).get

      status(thread) must equalTo(OK)
      contentType(thread) must beSome.which(_ == "application/json")
      contentAsString(thread) must contain ("タイトル1")
    }

    "スレッドタイトル検索" in new WithApplication{
      val thread = route()
    }
  }

}
