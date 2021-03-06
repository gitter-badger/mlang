package c_surface_syntax

import java.util.concurrent.atomic.AtomicLong


/**
  * surface syntax is a **equivalence** of what user write
  */
object surface {
  sealed trait Surface
  sealed trait Term
  sealed trait TeleItem
  case class NamedTeleItem(names: Seq[String], term: Term) extends TeleItem
  case class UnnamedTeleItem(term: Term) extends TeleItem
  type Tele = Seq[NamedTeleItem]
  type UnnamedTele = Seq[TeleItem]


  case class Definition(name: String, tele: Option[Tele], ty: Option[Term], term: Option[Term]) extends Surface
  case class Definitions(defs: Seq[Definition]) extends Term

  case class Let(defs: Seq[Definition], body: Term) extends Term

  case class Primitive(name: String) extends Term

  case class Ascription(term: Term, right: Term) extends Term
  case class Pi(seq: UnnamedTele, body: Term) extends Term
  case class Lambda(seq: Tele, body: Term) extends Term
  case class App(left: Term, right: Seq[Term]) extends Term

  case class Record(seq: Seq[(String, Term)]) extends Term
  case class Make(term: Term, seq: Seq[(String, Term)]) extends Term
  case class Projection(term: Term, str: String) extends Term

  case class Inductive(ts: Seq[(String, Tele)]) extends Term
  case class Construct(ty: Term, name: String, v: Option[Seq[Term]]) extends Term
  case class Split(term: Term, right: Seq[(String, Option[Seq[String]], Term)]) extends Term
  case class Reference(t: String) extends Term


  case object Absent extends Term

}


