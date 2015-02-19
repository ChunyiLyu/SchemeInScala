// Expression class
// Chunyi Lyu

import scala.collection.mutable.HashMap;
import scala.collection.mutable.ArrayBuffer;

sealed class Expression
  
case class IntExpression(var Number: Int) extends Expression

case class OperatorExpression(var name: String, var acceptsLists: Boolean , arity: Int) extends Expression
  
case class IdExpression(var id: String) extends Expression

case class NullExpression() extends Expression

case class BooleanExpression(var value: Boolean)  extends Expression

case class CallExpression(var op: Expression, var oprands: ArrayBuffer[Expression]) extends Expression

case class LetExpression(var bindings: ArrayBuffer[Tuple2[String, Expression]], var body: Expression) extends Expression 

case class LetrecExpression(var bindings: ArrayBuffer[Tuple2[String, Expression]], var body: Expression) extends Expression

case class LambdaExpression(var parameters: ArrayBuffer[String], var body: Expression) extends Expression {
  def setBody(b: Expression) {
    body = b;
  } 
}

case class IfExpression(var condition: Expression, var thenBranch: Expression, var elseBranch: Expression) extends Expression{
  def setCondition(e: Expression) {
    condition = e;
  }
  def setThen(e: Expression) {
     thenBranch = e;
  }  
  def setElse(e: Expression) {
    elseBranch = e;
  }
}


 






