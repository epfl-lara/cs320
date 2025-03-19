/* Copyright 2020 EPFL, Lausanne
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package calculator

import scallion.*
import silex.*

sealed trait Token
case class NumberToken(value: Int) extends Token
case class OperatorToken(operator: Char) extends Token
case class ParenthesisToken(isOpen: Boolean) extends Token
case object SpaceToken extends Token
case class UnknownToken(content: String) extends Token

object CalcLexer extends Lexers with CharLexers {
  type Position = Unit
  type Token = calculator.Token

  val lexer = Lexer(
    // Operators
    oneOf("-+/*!")
      |> { cs => OperatorToken(cs.head) },

    // Parentheses
    elem('(') |> ParenthesisToken(true),
    elem(')') |> ParenthesisToken(false),

    // Spaces
    many1(whiteSpace) |> SpaceToken,

    // Numbers
    {
      elem('0') |
      nonZero ~ many(digit)
    }
      |> { cs => NumberToken(cs.mkString.toInt) }
  ) onError {
    (cs, _) => UnknownToken(cs.mkString)
  }


  def apply(it: String): Iterator[Token] = {
    val source = Source.fromString(it, NoPositioner)

    val tokens = lexer(source)

    tokens.filter((token: Token) => token != SpaceToken)
  }
}

sealed abstract class TokenKind(text: String) {
  override def toString = text
}
case object NumberClass extends TokenKind("<number>")
case class OperatorClass(op: Char) extends TokenKind(op.toString)
case class ParenthesisClass(isOpen: Boolean) extends TokenKind(if (isOpen) "(" else ")")
case object OtherClass extends TokenKind("?")

sealed abstract class Expr
case class LitExpr(value: Int) extends Expr
case class BinaryExpr(op: Char, left: Expr, right: Expr) extends Expr
case class UnaryExpr(op: Char, inner: Expr) extends Expr

object CalcParser extends Parsers {
  type Token = calculator.Token
  type Kind = calculator.TokenKind

  import Implicits._

  override def getKind(token: Token): TokenKind = token match {
    case NumberToken(_) => NumberClass
    case OperatorToken(c) => OperatorClass(c)
    case ParenthesisToken(o) => ParenthesisClass(o)
    case _ => OtherClass
  }

  val number: Syntax[Expr] = accept(NumberClass) {
    case NumberToken(n) => LitExpr(n)
  }

  def binOp(char: Char): Syntax[Char] = accept(OperatorClass(char)) {
    case _ => char
  }

  val plus = binOp('+')
  val minus = binOp('-')
  val times = binOp('*')
  val div = binOp('/')

  val fac: Syntax[Char] = accept(OperatorClass('!')) {
    case _ => '!'
  }

  def parens(isOpen: Boolean) = elem(ParenthesisClass(isOpen))
  val open = parens(true)
  val close = parens(false)

  lazy val expr: Syntax[Expr] = recursive {
    (term ~ moreTerms).map {
      case first ~ opNexts => opNexts.foldLeft(first) {
        case (acc, op ~ next) => BinaryExpr(op, acc, next)
      }
    }
  }

  lazy val term: Syntax[Expr] = (factor ~ moreFactors).map {
    case first ~ opNexts => opNexts.foldLeft(first) {
      case (acc, op ~ next) => BinaryExpr(op, acc, next)
    }
  }

  lazy val moreTerms: Syntax[Seq[Char ~ Expr]] = recursive {
    epsilon(Seq.empty[Char ~ Expr]) |
    ((plus | minus) ~ term ~ moreTerms).map {
      case op ~ t ~ ots => (op ~ t) +: ots
    }
  }

  lazy val factor: Syntax[Expr] = (basic ~ fac.opt).map {
    case e ~ None => e
    case e ~ Some(op) => UnaryExpr(op, e)
  }

  lazy val moreFactors: Syntax[Seq[Char ~ Expr]] = recursive {
    epsilon(Seq.empty[Char ~ Expr]) |
    ((times | div) ~ factor ~ moreFactors).map {
      case op ~ t ~ ots => (op ~ t) +: ots
    }
  }

  lazy val basic: Syntax[Expr] = number | open.skip ~ expr ~ close.skip


  // Or, using operators...
  //
  // lazy val expr: Syntax[Expr] = recursive {
  //   operators(factor)(
  //     (times | div).is(LeftAssociative),
  //     (plus | minus).is(LeftAssociative)
  //   ) {
  //     case (l, op, r) => BinaryExpr(op, l, r)
  //   }
  // }
  //
  // Then, you can get rid of term, moreTerms, and moreFactors.

  def apply(tokens: Iterator[Token]): Option[Expr] = Parser(expr)(tokens).getValue
}

object Main {
  def main(args: Array[String]): Unit = {
    if (!CalcParser.expr.isLL1) {
      CalcParser.debug(CalcParser.expr, false)
      return
    }

    println("Welcome to the awesome calculator expression parser.")
    while (true) {
      print("Enter an expression: ")
      val line = scala.io.StdIn.readLine()
      if (line.isEmpty) {
        return
      }
      CalcParser(CalcLexer(line)) match {
        case None => println("Could not parse your line...")
        case Some(parsed) => println("Syntax tree: " + parsed)
      }
    }
  }
}
