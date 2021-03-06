package c_surface_syntax

import java.util.concurrent.atomic.AtomicLong

import scala.collection.mutable
import scala.language.implicitConversion
import scala.util.parsing.combinator.syntactical.StandardTokenParsers
import scala.util.parsing.combinator.{ImplicitConversions, PackratParsers}
import mlang.core.Term._
import c_surface_syntax.surface.{NamedTeleItem, UnnamedTeleItem}




/**
  *
  *
  *
  * VERY ULGY PARSER for now
  *
  *
  */
trait Parser extends StandardTokenParsers with PackratParsers with ImplicitConversions {


  lexical.reserved ++= List("match", "make") ++ Primitives.keys
  lexical.delimiters ++= List("{", "}", "[", "]", ":", ",", "(", ")", "=>", "->", "+", "-", ";", "|", "=", "@", "\\")

  def delimited[T](a: String, t: Parser[T], b: String): Parser[T] = a ~> t <~ b


  lazy val let: PackratParser[surface.Let] = delimited("{", rep(definition) ~ term, "}") ^^ { a => surface.Let(a._1, a._2)}

  lazy val definitions: PackratParser[surface.Definitions] =  keyword("make") ~> delimited( "{", rep(definition) , "}") ^^ { a => surface.Definitions(a)}

  lazy val tele: PackratParser[surface.Tele] = "(" ~> rep1sep((rep1(ident)) ~ opt(":" ~> term), ",") <~ ")" ^^ {a => a.map(a => NamedTeleItem(a._1, a._2.getOrElse(surface.Absent)))}

  lazy val typedTele: PackratParser[surface.Tele] = "(" ~> rep1sep((rep1(ident) <~ ":") ~ term, ",") <~ ")" ^^ {a => a.map(a => NamedTeleItem(a._1, a._2))}

  lazy val typedTelePossibleNoName: PackratParser[surface.UnnamedTele] = "(" ~> rep1sep(opt((rep1(ident) <~ ":")) ~ term, ",") <~ ")" ^^ { a =>
    a.map(a => a._1 match {
      case Some(l) =>
        NamedTeleItem(a._1.get, a._2)
      case None =>
        UnnamedTeleItem(a._2)
    })
  }

  lazy val definition: PackratParser[surface.Definition] =
    ident ~ opt(typedTele) ~ opt(":" ~> term) ~ opt( "=" ~> term) <~ ";" ^^ {a => surface.Definition(a._1._1._1, a._1._1._2, a._1._2, a._2) }


  lazy val term: PackratParser[surface.Term] =
        ascription |
        definitions |
        let |
        pi |
        lambda |
        app|
        record |
        make |
        projection |
        inductive |
        construct |
        Primitives.keys.foldLeft[PackratParser[surface.Term]](split) { (p, n) =>
          p | (keyword(n) ^^ {_ =>  surface.Primitive(n) })
        } |
        ident ^^ {a => surface.Reference(a)}

  lazy val ascription: PackratParser[surface.Ascription] = delimited("(", (term <~ ":") ~ term, ")") ^^ {a => surface.Ascription(a._1, a._2)}

  lazy val pi: PackratParser[surface.Pi] =
    typedTelePossibleNoName ~ ("=>" ~> term) ^^ {a => surface.Pi(a._1, a._2)}

  lazy val lambda: PackratParser[surface.Lambda] =
    tele ~ ("->" ~> term) ^^ {a => surface.Lambda(a._1, a._2) }

  lazy val app: PackratParser[surface.App] = term ~ delimited("(", repsep(term, ","), ")") ^^ {a => surface.App(a._1, a._2)}

  lazy val record: PackratParser[surface.Record] =
    delimited("{", rep(ident ~ (":" ~> term) <~ ";"),"}") ^^ {a => surface.Record(a.map(b => (b._1, b._2)))}

  lazy val make: PackratParser[surface.Make] =
    keyword("make")~> delimited("(", term , ")") ~ delimited("{", rep((ident <~ "=") ~ term <~ ";"), "}") ^^ {a => surface.Make(a._1, a._2.map(a => (a._1, a._2)))}

  lazy val projection: PackratParser[surface.Projection] = (term <~ ".") ~ ident ^^ {a => surface.Projection(a._1, a._2)}

  lazy val inductive: PackratParser[surface.Inductive] =
    delimited("[", repsep(ident ~ opt(tele),","),"]") ^^ {a => surface.Inductive(a.map(k => (k._1, k._2.getOrElse(Seq.empty))))}

  lazy val construct: PackratParser[surface.Construct] =
    (term <~ ":") ~ ident ~ opt(delimited("(", repsep(term, ","), ")")) ^^ {a => surface.Construct(a._1._1, a._1._2, a._2)}

  lazy val split: PackratParser[surface.Split] =
    (keyword("match") ~> term) ~ delimited("{", rep((ident ~ opt(delimited("(", repsep(ident, ",") ,")"))) ~ ("->" ~> term <~ ";")), "}") ^^ {a => surface.Split(a._1, a._2.map(k => (k._1._1, k._1._2, k._2)))}

  def parse(a: String): ParseResult[Seq[surface.Definition]] = rep(definition)(new PackratReader(new lexical.Scanner(a)))
}

