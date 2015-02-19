// Chunyi Lyu
// Evaluator class takes in program (ArrayBuffer[Expression]), evaluate to Expression

import scala.collection.mutable.HashMap;
import scala.collection.mutable.ArrayBuffer;

class Evaluator {

  def initialize(): Environment = {
    var env = new Environment(new HashMap[String, Tuple2[Expression, Environment]])
    // initialize environment with built in OperatorExpression
    env.put("+", OperatorExpression("+", true, 2), null)
    env.put("*", OperatorExpression("*", true, 2), null)
    env.put("-", OperatorExpression("-", false, 2), null)
    env.put("/", OperatorExpression("/", false, 2), null)
    env.put("<", OperatorExpression("<", false, 2), null)
    env.put(">", OperatorExpression(">", false, 2), null)
    env.put("=", OperatorExpression("=", false, 2), null)
    env.put("!", OperatorExpression("not", false, 1), null)
    env.put("odd?", OperatorExpression("odd?", false, 1), null)
    env.put("remainder", OperatorExpression("remainder", false, 2), null)
    env.put("modulo", OperatorExpression("remainder", false, 2), null)
    env.put("quotient", OperatorExpression("quotient", false, 2), null)
    env.put("even?", OperatorExpression("even?", false, 1), null)
    env
  }

  // evaluate every expression from program, output the result of the last expression
  def evaluateProgram(program: Program): Expression = {
    var result: Expression = IntExpression(0)
    var temp = program.expressions.length
    for (expression <- program.expressions) {
      result = evaluateExpression(expression, initialize())
      println(result)
    }
    result
  }

  // recursively evaluate expression
  def evaluateExpression(e: Expression, env: Environment): Expression = {
    e match {
      case IdExpression(s) => env.getExpression(s)      
      case LetExpression(bindings, body) => letEval(LetExpression(bindings, body), env)
      case LetrecExpression(bindings, body) => letrecEval(LetrecExpression(bindings, body), env)
      case CallExpression(op, oprands) => callEval(CallExpression(op, oprands), env)
      case IfExpression(con, then, elseb) => ifEval(IfExpression(con, then, elseb), env)
      case _ => e
    }
  }
  
  // CallExpression evaluate. Operator has to be OperatorExpression or LambdaExpression
  def callEval(e: CallExpression, env: Environment): Expression = {
    e.op match {
      case IdExpression(id) => {
        evaluateExpression(IdExpression(id), env) match {
          // operator is LambdaExpression
          case LambdaExpression(parameters, body) => {
            var exp: LambdaExpression = LambdaExpression(parameters, body)
            var origin: Environment = env.getEnvironment(id)
            if (e.oprands.size != exp.parameters.size) {
              println("oprands and parameters sizes do not match")
              null
            }
            var copy: Environment = new Environment(origin)
            var i = 0
            for (s <- parameters) {
              var value: Expression = evaluateExpression(e.oprands(i), env)
              copy.put(s, value, env)
              i = i + 1
            }
            evaluateExpression(body, copy)
          }
          // if operator is OperatorExpression (operator that are built in environment)
          case OperatorExpression(name, acceptsLists, arity) => {
            var op: OperatorExpression = new OperatorExpression(name, acceptsLists, arity)
            var items: ArrayBuffer[Expression] = e.oprands
            if ((!op.acceptsLists) && items.size != op.arity) {
              null
            }
            if ((op.acceptsLists) && items.size < op.arity) {
              null
            }
            var t: BooleanExpression = new BooleanExpression(true);
            var f: BooleanExpression = new BooleanExpression(false);
            if (op.name.equals("odd?")) {
              evaluateExpression(items(0), env) match {
                case IntExpression(n) => {
                  if (n % 2 == 0) {
                    return f
                  } else {
                    return t
                  }
                }
              }
            } else if (op.name.equals("even?")) {
              evaluateExpression(items(0), env) match {
                case IntExpression(n) => {
                  if (n % 2 == 0) {
                    return t
                  } else {
                    return f
                  }
                }
              }
            } else if (op.name.equals("remainder")) {
              evaluateExpression(items(0), env) match {
                case IntExpression(n0) => {
                  evaluateExpression(items(1), env) match {
                    case IntExpression(n1) => {
                      IntExpression(n0 % n1)
                    }
                  }
                }
              }
            } else if (op.name.equals("=")) {
              //env.getString()
              evaluateExpression(items(0), env) match {
                case IntExpression(n0) => {
                  evaluateExpression(items(1), env) match {
                    case IntExpression(n1) => {
                      if (n0 == n1) return t
                      else return f
                    }
                  }
                }
              }
            } else if (op.name.equals("<")) {
              evaluateExpression(items(0), env) match {
                case IntExpression(n0) => {
                  evaluateExpression(items(1), env) match {
                    case IntExpression(n1) => {
                      if (n0 < n1) {
                        return t
                      } else {
                        return f
                      }

                    }
                  }
                }
              }
            } else if (op.name.equals(">")) {
              evaluateExpression(items(0), env) match {
                case IntExpression(n0) => {
                  evaluateExpression(items(1), env) match {
                    case IntExpression(n1) => {
                      if (n0 > n1) return t
                      else return f
                    }
                  }
                }
              }
            } else if (op.name.equals("+")) {
              var temp: Expression = new IntExpression(0)
              var result = 0
              for (temp <- items) {
                evaluateExpression(temp, env) match {
                  case IntExpression(n) => result = result + n
                }
              }
              return IntExpression(result)
            } else if (op.name.equals("*")) {
              var temp: Expression = new IntExpression(0)
              var result = 1
              for (temp <- items) {
                evaluateExpression(temp, env) match {
                  case IntExpression(n) => {
                    result = result * n
                  }
                }
              }
              return IntExpression(result)
            } else if (op.name.equals("-")) {
              evaluateExpression(items(0), env) match {
                case IntExpression(n) => {
                  evaluateExpression(items(1), env) match {
                    case IntExpression(n1) => IntExpression(n - n1)
                  }
                }
              }
            } else if (op.name.equals("quotient")) {
              evaluateExpression(items(0), env) match {
                case IntExpression(n) => {
                  evaluateExpression(items(1), env) match {
                    case IntExpression(n1) => IntExpression(n / n1)
                  }
                }
              }
            } else {
              println(" error, operator not defined, name of op: " + op.name)
              null
            }
          }
        }
      }
      case _ => {
        println("error found, operator neither operatorExpression, nor lambda")
        null
      }
    }
  }

  // LetExpression evaluate
  def letEval(e: LetExpression, env: Environment): Expression = {
    var augmentedEnv: Environment = new Environment(env)
    for (temp <- e.bindings) {
      augmentedEnv.put(temp._1, evaluateExpression(temp._2, env), env)
    }
    return evaluateExpression(e.body, augmentedEnv)
  }

  // LetRecExpression
  def letrecEval(e: LetrecExpression, env: Environment): Expression = {
    var augmentedEnv: Environment = new Environment(env)
    var temp: Tuple2[String, Expression] = ("temp", IntExpression(0))
    for (temp <- e.bindings) {
      temp._2 match {
        case LambdaExpression(n, m) => {
          augmentedEnv.put(temp._1, temp._2, augmentedEnv)
        }
        case _ => {
          println(evaluateExpression(temp._2, env))
          augmentedEnv.put(temp._1, evaluateExpression(temp._2, env), env)
        }
      }
    }
    return evaluateExpression(e.body, augmentedEnv)
  }

  // IfExpression, conditions evaluate
  def ifEval(e: IfExpression, env: Environment): Expression = {
    var cond: Expression = evaluateExpression(e.condition, env)
    cond match {
      case BooleanExpression(n) => {
        if (n) {
          return evaluateExpression(e.thenBranch, env)
        } else {
          return evaluateExpression(e.elseBranch, env)
        }
      }
      case _ =>
        {
          println("if condition not return boolean value")
          return null
        }
    }
  }
  
}