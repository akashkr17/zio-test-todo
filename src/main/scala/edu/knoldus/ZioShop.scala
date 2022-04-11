package edu.knoldus

import zio.Console._
import zio.{Task, UIO, ZEnv, ZIO, ZIOApp, ZIOAppArgs, ZLayer}

import scala.collection.mutable

case class NewOrderRequest() {
  def toOrder: Order = {Order()}
}
case class NewOrderResponse()
case class Order()

trait DAO {
  def insert(order: Order): Unit
  def size(): Int
}

object DAO {
  def make(): DAO = new DAO {
    val orders = new mutable.ArrayBuffer[Order]()

    override def insert(order: Order): Unit = {
      orders.+=(order)}

    override def size(): Int = orders.size
  }
}

trait MyRpcService {
  def newOrder(request: NewOrderRequest): NewOrderResponse
}

object MyRpcService extends ZIOApp {

  def apply(daoZ: DAO): MyRpcService = {
    (request: NewOrderRequest) => runtime.unsafeRun  {
      for {

        _ <- ZIO.attempt(daoZ.insert(request.toOrder))
        _ <- printLine(s"Added an Order.")
        currentSize <- ZIO.attempt(daoZ.size())
        _ <- printLine(s"Amount of orders: $currentSize.")
      } yield NewOrderResponse()
    }
  }

  override implicit def tag: zio.EnvironmentTag[MyRpcService.type] = ???

  override type Environment = this.type

  override def layer: ZLayer[ZIOAppArgs, Any, MyRpcService.type] = ???

  override def run: ZIO[MyRpcService.type with ZEnv with ZIOAppArgs, Any, Any] = ???
}

object RPCServerSimulator {
  def callEndpoints(rpcService: MyRpcService): Task[Unit] = {
    ZIO.attempt(rpcService.newOrder(NewOrderRequest())) *>
      ZIO.attempt(rpcService.newOrder(NewOrderRequest()))
  }
}

object MyApp extends zio.ZIOAppDefault {


  override def run: ZIO[ZEnv with ZIOAppArgs, Any, Any] =  for {
    service <- UIO(MyRpcService(DAO.make()))
    _ <- RPCServerSimulator.callEndpoints(service)
  } yield ()
}

