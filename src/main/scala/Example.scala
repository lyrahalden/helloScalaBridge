//import com.sun.deploy.net.HttpResponse
//import doodle.core._
//import doodle.core.Image._
//import doodle.syntax._
//import doodle.jvm.Java2DFrame._
//import doodle.backend.StandardInterpreter._

// in project folder, run sbt
// then can type console to open Scala Repl
// or type ~run to run/compile code interactively in the terminal
// and it will recompile every time you save

// To use this example, open the SBT console and type:
//
// Example.image.draw
//object Example {
////  val image = circle(10).fillColor(Color.red) on circle(20) on circle(30)
//
//  def main(args: Array[String]): Unit = {
//    image.draw
//    Number(2).add(2).show
//    val comparison = Number(3) == Number(3)
//    // This will be true if Number is a case class, false if Number is a normal class
//    // because class compares by reference, case class by value
//    println(comparison)
//
//    println(Complex(Number(2), Number(1)) == Complex(Number(2), Number(1)))

//    val builder = new StringBuilder()
//    for (i <- 0 until 3) {
//      builder.append("a")
//    }
//    println(builder.result())
//    }
//  }
//
//
//object Example1 {
//  val x = 3
//
//  def repeat(n: Int, str: String):Unit = for (i <- 0 until n) {
//    println(str)
//
//  }
//}
//
////object Main {
////
////  def main(args: Array[String]): Unit = {
////    Example1.repeat(3,"you!")
////  }
////}
//
//case class Number(n: Int) {
//
//  def add (other: Int): Unit = new Number(n + other)
////  def show: Unit = println("show" ++ n.toString)
//}
//
//object Number {
//  def fromString(str: String) = Number(str.toInt)
//}
//
//case class Complex(real: Number, imaginary: Number)

// pattern matching
//object Example {
//
//  def check(n: Int): String = n match {
//    case 2 => "two"
//    case 3 => "three"
//    case _ => s"I don't know what $n is"
//  }
//
//  def main(args: Array[String]): Unit = {
//    println(check(4))
//  }
//}


// Inheritance
// should use trait instead of abstract class
// traits are like mixins, can use for OO programming

//trait A {
//  def foo: Int
//}
//
//trait Barable {
//  def bar: Boolean
//}
//
//class B extends A with Barable {
//  def foo = 0
//  def bar = true
//}
//
//class C extends A {
//  def foo = 1
//}
//
//
//object Example {
//
//  case class Number(x: Int)
//  case class Complex(real: Number, imaginary: Number)
//
//  def check(z: Complex):String = z match {
//    case Complex(r, Number(4)) => s"The real part is $r"
//    case _ => "unknown complex"
//  }
//
//  def callFoo(a: A):Int = a.foo
//
//  def main(args: Array[String]): Unit = {
//    println(check(Complex(Number(3),Number(4))))
//    callFoo(new C)
//  }
//}


//object Example {
//
//  trait HttpRequest {
//    val host: String
//    val path: String
//  }
//
//  case class Get(host: String, path: String) extends HttpRequest
//  case class Post(host: String, path: String) extends HttpRequest
//
//  case class HttpResponse(status: Int, body: String)
//
//  case class Server (route: HttpRequest => HttpResponse) {
//    def handle(request: HttpRequest): HttpResponse = request match
//    {
//      case Get(host, path) => HttpResponse(200, path)
//      case Post(host, path) => HttpResponse(201, "file uploaded")
//      case _ => HttpResponse(500, "unknown request")
//    }
//  }
//
//  def main(args: Array[String]): Unit = {
//    val route = (request: HttpRequest) => HttpResponse(200, request.path)
//    val server: Server = Server(route)
//    val request = Get("localhost", "/foo.txt")
//    val response = server.handle(request)
//    println(response)
//  }
//}

// look up partial functions in Scala

//best practices version
trait HttpRequest {
  val host: String
  val path: String
}
case class Get(host: String, path: String) extends HttpRequest
case class Post(host: String, path: String) extends HttpRequest
case class Delete(host: String, path: String) extends HttpRequest

case class HttpResponse(status: Int, body: String)

case class Server(route: PartialFunction[HttpRequest, HttpResponse]) {
  def handle(request: HttpRequest): HttpResponse = {
    if (route.isDefinedAt(request)) {
      route(request)
    } else {
      HttpResponse(500, s"unknown request type $request")
    }

  }
}

object Example {

  def main(args: Array[String]): Unit = {
    //println(check(Complex(Number(3),Number(4))))

    val route1: PartialFunction[HttpRequest, HttpResponse] = {
      case Get(host, path) => HttpResponse(200, path)
      case Post("localhost", path) =>
        // upload a file
        HttpResponse(200, "file uploaded")
      case Post(_, path) =>
        HttpResponse(403, "forbidden")
    }

    val route2: PartialFunction[HttpRequest, HttpResponse] = {
      case Delete("localhost", _) => HttpResponse(200, "deleted")
    }


    val route = route1 orElse route2

    val server: Server = Server(route)


    val requests: List[HttpRequest] = List(
      Post("localhost", "foo.txt"),
      Get("localhost", "foo.txt"),
      Delete("localhost","foo.txt"),
      Get("localhost", "foo.txt")
    )

    val responses: List[HttpResponse] = requests.map(server.handle(_))

    for (response <- responses) {
      println(response)
    }

  }

}

//Seq - python lists
//Set - python sets
//Map - python dict
//Option - like a list of one element
//Either - Left . Right -- used for error messages - error type, message
//OptionT -- when iterating through list, opt to do something with only certain types, for example, ints
//Futures - ?