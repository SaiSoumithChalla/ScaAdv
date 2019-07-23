import org.apache.spark.{SparkContext,SparkConf}
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import org.json4s.native.Json
import org.json4s.DefaultFormats


object ECommerceDataframe extends App {

    val conf = new SparkConf().setAppName("EcommerceData").setMaster("local")
    val sc = new SparkContext(conf)
    val spark = SparkSession.builder().appName("ECommerceJSONNrch").getOrCreate()


    val rec1: String = """{
    "visitorId": "v1",
    "products": [{
         "id": "i1",
         "interest": 0.68
    }, {
         "id": "i2",
         "interest": 0.42
    }]
}"""

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
 import spark.implicits._

 var JsonList = List.empty[String]
  JsonList = JsonList :+ rec1
  JsonList = JsonList :+ rec2
  val JsonRdd = sc.parallelize(JsonList)
  val productIdToNameMap = Map("i1" -> "Nike Shoes", "i2" -> "Umbrella", "i3" -> "Jeans").toList.toDF().withColumnRenamed("_1","PrdId").withColumnRenamed("_2","ProdName")

  val js = Json(DefaultFormats).write(productIdToNameMap)
  val Record1 = spark.read.json(JsonRdd)
  val InitEx =  Record1.withColumn("Product",explode($"products")).withColumn("ProdId",$"Product.id").withColumn("ProdInterest",$"Product.interest").select("visitorId","ProdId","ProdInterest")

  InitEx.printSchema
  val FinalDf = InitEx.join(productIdToNameMap,InitEx("ProdId") === productIdToNameMap("PrdId"),"inner").select("visitorId","ProdId","ProdName","ProdInterest")

  val FinalEnrichData1 = FinalDf.filter($"visitorId" === "v1").withColumnRenamed("ProdId","id").withColumnRenamed("ProdName","name").withColumnRenamed("ProdInterest","interest")

  FinalEnrichData1.createOrReplaceTempView("FinalEnrichData1")
  val DFenrichedRec1 =  spark.sql("select visitorId,collect_list(struct(id,name,interest)) as Product from FinalEnrichData1 group by visitorId" ).toJSON.first()
  println(DFenrichedRec1)
  val FinalEnrichData2 = FinalDf.filter($"visitorId" === "v2").withColumnRenamed("ProdId","id").withColumnRenamed("ProdName","name").withColumnRenamed("ProdInterest","interest")
  FinalEnrichData2.createOrReplaceTempView("FinalEnrichData2")
  val DFenrichedRec2 = spark.sql("select visitorId,collect_list(struct(id,name,interest)) as Product from FinalEnrichData2 group by visitorId" ).toJSON.first()
println(DFenrichedRec2)

  val outputDF: Seq[String] = Seq(DFenrichedRec1, DFenrichedRec2)

  println(outputDF)
}
