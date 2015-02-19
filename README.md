# SchemeInScala

Chunyi Lyu
Compiler
12/19/2014
Mike Siff

Conference Project "SchemeInScala"

My conference project is a basic scheme interpreter written in Scala. The project does not have lexing and parsing support, so every expression needs to be created in the form of abstract expression. 

The file Expression.scala contains all of the scheme expressions that this project is able to evaluate. It has: IntExpression, OperatorExpression, IdExpression, NullExpression, BooleanExpression, CallExpression, LetExpression, LetrecExpression, LambdaExpression, and IfExpression. Program is the class which takes in a list(ArrayBuffer in scala) of Expressions. 

The environment class remembers all of the bindings which are defined in LetExpressions or LetrecExpressions in a HashMap. It can record the original environment of each binding as well. Evaluator starts out as initializing environment with built-in simple OperatorExpressions. They are: +, *, -, /, <, > =, !, odd?, even?, modulo, and quotien. The Evaluator class takes in a program, recursively evaluates every expression of the program, and later output the result of the last expression. Main.scala is an object class for testing. It has already contained 4 expressions for evaluation. 

This conference project is written based on the summer research project "Scheme_Preliminary".  The transition from java to scala is not as straight-forward as I expected, but writing an interpreter is definitely a fun way to learn a new language. If I had more time, I would love to make my parser work to test more examples, as well as to see whether any scala feature could help optimize the evaluation process.
