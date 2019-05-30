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

}



