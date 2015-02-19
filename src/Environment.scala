// Chunyi Lyu 
// class represent current environment we use to do evaluation
import scala.collection.mutable.HashMap;

class Environment (var mEnv: HashMap[String, Tuple2[Expression, Environment]]){
  
  def this(copy: Environment) {
    this(copy.mEnv)
  }
  
  def put(id: String, e: Expression, env: Environment) {
    mEnv.put(id, new Tuple2[Expression, Environment](e, env));
  }
  
  def getExpression(id: String): Expression = {
    var t: Tuple2[Expression, Environment] = mEnv.getOrElse(id, null);   
    if (t == null) {
      println("t: " + t)      
      println("t._1 : " + t._1)
      for (temp <- mEnv) {      
         println("printing")
          println("found id: " + temp._1)       
      }
    }
    if (t._1 == null) {
      println("t._1 is null")
    }
    return t._1;
  }
  
  def getEnvironment(id: String) : Environment = {
    return mEnv.getOrElse(id, null)._2;
  }
  
  def getPair(id: String): Tuple2[Expression, Environment] = {
    return mEnv.getOrElse(id, null);
  }
  
  def size() : Int = {
    return mEnv.size
  }
  
  def getString() {
    for (temp <- mEnv) {
      println(temp._1);
    }
  }

}