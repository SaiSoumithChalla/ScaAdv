import org.scalatest.{FlatSpec, Matchers}


class JsonParserUnitTest extends FlatSpec {


    val InpStg  : String =
      """{
    "visitorId": "v1",
    "products": [{
         "id": "i1",
         "interest": 0.68
    }, {
         "id": "i2",
         "interest": 0.42
    }]}""".stripMargin

  "Input String" should "Split Valid Json " in {
    val Source1 = InpStg
    val RealJs =s""" {"visitorId" : "v1","products": [{"\id\": "i1",\"interest\": 0.68}, {"\id\": "i2",\"interest\": 0.42}]}"""
    println(Source1)
    println(RealJs)
    assert(RealJs === Source1)
  }

}
