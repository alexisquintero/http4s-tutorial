package com.example.quickstart

import cats.effect.Sync
import io.circe.Encoder
import io.circe.generic.semiauto.deriveEncoder
import org.http4s.EntityEncoder
import org.http4s.circe.jsonEncoderOf

trait Tweets[F[_]] {
  def getTweet(tweetId: Int): F[Tweets.Tweet]
  def getPopularTweets(): F[Seq[Tweets.Tweet]]
}

case object Tweets {

  final case class Tweet(id: Int, message: String)

  object Tweet {
    implicit val tweetEncoder: Encoder[Tweet] = deriveEncoder[Tweet]
    implicit def tweetEntityEncoder[F[_]]: EntityEncoder[F, Tweet] =
      jsonEncoderOf[F, Tweet]

    implicit def tweetsEncoder[F[_]]: EntityEncoder[F, Seq[Tweet]] =
      jsonEncoderOf[F, Seq[Tweet]]
  }

  def impl[F[_]: Sync](): Tweets[F] = new Tweets[F] {
    def getTweet(tweetId: Int): F[Tweet] =
      Sync[F].delay(Tweet(tweetId, "tweet"))
    def getPopularTweets(): F[Seq[Tweet]] =
      Sync[F].delay(List(Tweet(1, "tweet"), Tweet(1, "tweet")))
  }

}
