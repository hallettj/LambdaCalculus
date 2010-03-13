package us.sitr.lambdacalculus

import scala.util.parsing.combinator._

sealed abstract class Expression {
  def substitute(orig: Var, sub: Expression): Expression
  def toString: String
}

case class Var(name: String) extends Expression {
  def substitute(orig: Var, sub: Expression): Expression = this
  override def toString: String = name
}

case class Function(argument: Var, body: Expression) extends Expression {
  def substitute(orig: Var, sub: Expression): Expression = {
    if (orig != argument)
      Function(argument, body.substitute(orig, sub))
    else
      this
  }

  override def toString: String = String.format("λ%s.%s", argument, body)
}

case class Application(function: Expression, argument: Expression) extends Expression {
  def substitute(orig: Var, sub: Expression): Expression =
    Application(function.substitute(orig, sub), argument.substitute(orig, sub))

  override def toString: String = {
    val left = function match {
      case Function(_, _) => "("+ function +")"
      case a => a
    }
    val right = argument match {
      case Var(_) => argument
      case b => "("+ b +")"
    }
    left +" "+ right
  }
}

class LambdaParsers extends RegexParsers {
  def expression: Parser[Expression] = (
      function
    | identifier
    | "("~>expression<~")"
    | application
  )

  def function: Parser[Expression] =
    lambda~>arguments~"."~expression ^^ { 
      case args~"."~exp => (args :\ exp) { Function(_, _) }
    }

  def application: Parser[Application] =
    expression~expression ^^ {
      case func~arg => Application(func, arg)
    }

  def arguments: Parser[List[Var]] = rep1(identifier)

  def lambda: Parser[String] = """\\|λ""".r

  def identifier: Parser[Var] = """[a-z]|[A-Z0-9+\-*/]+""".r ^^ { Var(_) }
}

object Expression extends LambdaParsers {
  def apply(input: String): Expression = {
    (parseAll(expression, input): @unchecked) match {
      case Success(e, _) => e
    }
  }

  def betaReduction(exp: Expression): Expression = exp match {
    case Application(Function(arg, body), b) => body.substitute(arg, b)
    case _ => exp
  }
}
