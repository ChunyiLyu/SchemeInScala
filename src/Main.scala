// Chunyi Lyu
// test class for SchemeInScala, main function contains 4 testing examples

import scala.collection.mutable.ArrayBuffer;
import scala.collection.mutable.HashMap;
object Main {
  def main(args: Array[String]) {
    // used integers
    var x0 = new IntExpression(0)
    var x1 = new IntExpression(1)
    var x2 = new IntExpression(2)
    var x3 = new IntExpression(3)
    var x4 = new IntExpression(4)
    var x5 = new IntExpression(5)
    var x7 = new IntExpression(7)
    var x10 = new IntExpression(10) 
    var x100 = new IntExpression(100)
    // used relational and arithmetic operator
    var xEq = new IdExpression("=")
    var xLt = new IdExpression("<")
    var xMinus = new IdExpression("-")
    var xPlus = new IdExpression("+")
    var xTimes = new IdExpression("*")   
    // used idExp
    var xN = new IdExpression("n")
    var xX = new IdExpression("x")
    var xY = new IdExpression("y")
    
//---------------------building examples---------------------------------------
    
    // simpleIf: (if (< 3 4) (* 4 7) (- 10 7))
    var ifCon: ArrayBuffer[Expression] = ArrayBuffer.apply(x3, x4)
    var ifThen: ArrayBuffer[Expression] = ArrayBuffer.apply(x4, x7)
    var ifElse: ArrayBuffer[Expression] = ArrayBuffer.apply(x10, x7)    
    var simpleIf = new IfExpression(new CallExpression(xLt,ifCon),new CallExpression(xTimes,ifThen),new CallExpression(xMinus,ifElse))

    //simpleLet: (let ((x 3) (y (* 4 5))) (+ x y))
    var letB0: ArrayBuffer[Expression] = ArrayBuffer.apply(x4, x5)
    var letBody: ArrayBuffer[Expression] = ArrayBuffer.apply(xX, xY)
    var simpleLetBindings = new ArrayBuffer[Tuple2[String, Expression]]()    
    simpleLetBindings+=(("x", x3))
    simpleLetBindings+=(("y", new CallExpression(xTimes,letB0)))   
    var simpleLet = new LetExpression(simpleLetBindings,  new CallExpression(xPlus,letBody))

    // factorial lambda in Letrec (factorial of 5)
    var xFact = new IdExpression("fact")
    var minusLi: ArrayBuffer[Expression] = ArrayBuffer.apply(xN, x1)
    var xFactMinus1 = new CallExpression(xMinus, minusLi) // (- x 1)
    var xFactMinus1List: ArrayBuffer[Expression] = ArrayBuffer.apply(xFactMinus1)   
    var xFactRecurse =new CallExpression(xFact,xFactMinus1List) // (fact (- x 1))
    var nS = ArrayBuffer.apply("n")
    var eq: ArrayBuffer[Expression] = ArrayBuffer.apply(xN, x0) // (n 0)
    var recurse: ArrayBuffer[Expression] = ArrayBuffer.apply(xN, xFactRecurse) // (n (fact (- x 1)))
    var para: ArrayBuffer[Expression] = ArrayBuffer.apply(x5) // (fact 5)  
    var xFactLambda: LambdaExpression = new LambdaExpression(nS,
        new IfExpression( new CallExpression(xEq, eq), x1 ,new CallExpression(xTimes,recurse))); // (* n (fact (- x 1)))
    var factBindings = new ArrayBuffer[Tuple2[String, Expression]]()  
    factBindings+=(("fact", xFactLambda))       
    var factorial = new LetrecExpression(factBindings, new CallExpression(xFact,para))
    
    // sum of 0 to 100 in Letrec
    var sum = new IdExpression("sum")
    var l: ArrayBuffer[Expression] = ArrayBuffer.apply(xN, x1) //(x 1)
    var callL: ArrayBuffer[Expression] =  ArrayBuffer.apply(new CallExpression(xMinus, l))
    var Recurse0 = new CallExpression(sum,callL) // (sum (- x 1)) 
    var sumLambda: LambdaExpression = new LambdaExpression(ArrayBuffer.apply("n"),
      new IfExpression(new CallExpression(xEq, ArrayBuffer.apply(x0, xN)), x0,
          new CallExpression(xPlus, ArrayBuffer.apply(xN, Recurse0)))) 
    var sumBindings = new ArrayBuffer[Tuple2[String, Expression]]()  
    sumBindings+=(("sum", sumLambda)) 
    var sumF = new LetrecExpression(sumBindings, new CallExpression(sum, ArrayBuffer.apply(x100)))
    
    // testing examples
    var examples: ArrayBuffer[Expression] = ArrayBuffer.apply(simpleIf, simpleLet, factorial, sumF)
    var Evaluator = new Evaluator()
    var v = Evaluator.evaluateProgram(new Program(examples))
        v match {
        case IntExpression(n) => printf("The last funciton in the program evaluates to: " + n)
        case BooleanExpression(b) => printf("The last funciton in the program evaluates to: " + b)
        case _ => println("The last funciton in the program evaluates to neither int or bool")
    }   
  }
}