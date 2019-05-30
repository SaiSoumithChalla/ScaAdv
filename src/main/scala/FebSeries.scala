object FebSeries extends Serializable {
  def FebS(n:Int):Int={
       def febCal(n:Int,pre:Int,cur:Int):Int={
         if (n==0)pre else febCal(n-1,cur,pre+cur)
       }
       febCal(n,0,1)
  }
  def StringReverse(s:String):String={
    return s.reverse
  }
  def SecondaryStringReverse(x:String):String={
    val buffer = new StringBuilder
    val legth = x.length
    for(i <- 0 until legth){
      buffer.append(x.charAt(legth - i - 1))

    }
    buffer.toString
  }
}