import net.liftweb.json._
import net.liftweb.json.Serialization.write
import scala.collection.mutable.{ArrayBuffer, ListBuffer}
object  eCommerce extends  App {


  def casClassToMap(cc: AnyRef) =
    (Map[String, Any]() /: cc.getClass.getDeclaredFields) {
      (a, f) =>
        f.setAccessible(true)
        a + (f.getName -> f.get(cc))
    }

   def MapMerge(map1:Map[Any,Any],map2:Map[Any,Any]):Map[Any,Any]={
    return map1 ++ map2
  }


  case class Product(id:String,interest:Double)
  case class Record (visitorId:String,products:List[Product])
  case class RecordMerge(id:String,name:String,interest:Any)
  case class Products(prods:Array[String])
  case class FinalRecord(visitorId:String,products:List[String])



  implicit val formats = DefaultFormats


  val rec1: String =
    """{
    "visitorId": "v1",
    "products": [{
         "id": "i1",
         "interest": 0.68
    }, {
         "id": "i2",
         "interest": 0.42
    }]}"""



  val rec2: String = """{
    "visitorId": "v2",
    "products": [{
         "id": "i1",
         "interest": 0.78
    }, {
         "id": "i3",
         "interest": 0.11
    }]
}"""

  val productIdToNameMap = Map("i1" -> "Nike Shoes", "i2" -> "Umbrella", "i3" -> "Jeans").toMap[Any,Any]
  val RAWJSON = parse(rec1)
  val vistor = RAWJSON.extract[Record]

  val RAWJSON2 = parse(rec2)
  val vistor2 = RAWJSON2.extract[Record]
  val listProducts2:ListBuffer[String] = new ListBuffer[String]


  val listProducts:ListBuffer[String] = new ListBuffer[String]

  for (i <-( 0 to   vistor.products.length -1) ) {
    val Source =  casClassToMap(vistor.products(i)).transform((k, y) => ("name", productIdToNameMap.getOrElse(y, null), y))
    val IdInt = Source.transform( (q,r) => r._3 )
    val Finalproduct = Source.transform((q,r) => (r._1,r._2) )
    val FinalProductId = IdInt.toMap[Any,Any]
    val ProdList = Finalproduct("id").toString().replace("(","").replace(")","").split(",").map(_.trim)
    val FinalProductName = Map(ProdList(0) -> ProdList(1)).toMap[Any,Any]
    val MergeCombo = MapMerge(FinalProductId,FinalProductName)
    val  p_i= RecordMerge(MergeCombo("id").toString,MergeCombo("name").toString,MergeCombo("interest"))
    val jsonString = write(p_i)
    listProducts += jsonString.toString
  }

  for (i <-( 0 to   vistor2.products.length -1) ) {
    val Source2 =  casClassToMap(vistor2.products(i)).transform((k, y) => ("name", productIdToNameMap.getOrElse(y, null), y))
    val IdInt2 = Source2.transform( (q,r) => r._3 )
    val Finalproduct2 = Source2.transform((q,r) => (r._1,r._2) )
    val FinalProductId2 = IdInt2.toMap[Any,Any]
    val ProdList2 = Finalproduct2("id").toString().replace("(","").replace(")","").split(",").map(_.trim)
    val FinalProductName2 = Map(ProdList2(0) -> ProdList2(1)).toMap[Any,Any]
    val MergeCombo2 = MapMerge(FinalProductId2,FinalProductName2)
    val  p_i2= RecordMerge(MergeCombo2("id").toString,MergeCombo2("name").toString,MergeCombo2("interest"))
    val jsonString2 = write(p_i2)
    listProducts2 += jsonString2.toString
   }
  val enrichedRec1 = write(FinalRecord(vistor.visitorId.toString,listProducts.toList)).replace("""\""","")
  val enrichedRec2 = write(FinalRecord(vistor2.visitorId.toString,listProducts2.toList)).replace("""\""","")

  println(enrichedRec1)
  println(enrichedRec2)

  val output: Seq[String] = Seq(enrichedRec1, enrichedRec1)

  println(output)




}