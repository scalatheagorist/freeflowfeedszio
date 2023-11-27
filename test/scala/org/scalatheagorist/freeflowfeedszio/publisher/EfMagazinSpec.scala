package org.scalatheagorist.freeflowfeedszio.publisher

import org.scalatheagorist.freeflowfeedszio.models.Article
import org.scalatheagorist.freeflowfeedszio.models.HtmlResponse
import org.scalatheagorist.freeflowfeedszio.models.RSSFeed
import zio._
import zio.test._

object EfMagazinSpec extends ZIOSpecDefault {
  override def spec: Spec[TestEnvironment with Scope, Any] = suite("EfMagazin") {
    test("toRSSFeedStream must convert html code to a RSSFeed") {
      for {
        efMagazin <- ZIO.succeed(EfMagazin("https://www.ef-magazin.de"))
        htmlResp  <- ZIO.succeed(HtmlResponse(Publisher.EFMAGAZIN, htmlResponse))
        build     <- efMagazin.toRSSFeedStream(htmlResp).runLast
      } yield assertTrue(build.contains(RSSFeed(
        author = "Jan Mehring",
        article = Article(
          title = "Werden Stadt und Land allmählich sturmreif reformiert?",
          link = "https://www.ef-magazin.de/2023/09/22/20822-absenkung-des-wahlalters-make-berlin-gruen-again"
        ),
        publisher = Publisher.EFMAGAZIN,
        lang = Lang.DE
      )))
    }
  }

  private def htmlResponse: String = {
    s"""
       |
       |<!DOCTYPE html>
       |<html lang="de">
       |<head>
       |    <meta charset="utf-8">
       |    <meta http-equiv="X-UA-Compatible" content="IE=edge">
       |    <meta name="viewport" content="width=device-width, initial-scale=1">
       |    <title>eigentümlich frei - erfrischend libertär seit 1998</title>
       |    <link rel="shortcut icon" href="/static/img/favicon.ico">
       |
       |
       |    <meta name="description" content="Libertäres Magazin und Stimme der Freiheit seit 1998. Erfrischend anders, erfrischend libertär, einfach eigentümlich frei."/>
       |    <meta property="og:url" content="https://ef-magazin.de/"/>
       |    <meta property="og:title" content="eigentümlich frei - erfrischend libertär seit 1998"/>
       |    <meta property="og:description" content="Libertäres Magazin und Stimme der Freiheit seit 1998. Erfrischend anders, erfrischend libertär, einfach eigentümlich frei."/>
       |    <meta property="og:site_name" content="eigentümlich frei"/>
       |    <meta property="og:type" content="article"/>
       |    <meta property="og:locale" content="de_de"/>
       |    <meta property="article:publisher" content="efmagazin">
       |    <meta property="twitter:site" content="@efonline"/>
       |
       |    <meta name="keywords" content="Libertär, Libertarismus, Marktwirtschaft, marktwirtschaftlich, Kapitalismus, kapitalistisch, Marktradikal, Marktradikalismus, Freiheit, freiheitlich, Liberal, Liberalismus, Magazin, Monatsmagazin, Freie Presse,
       |Konterrevolution, konterrevolutionär, Alternative Medien, Anti-Kommunistisch, Anti-sozialistisch, Hans-Hermann Hoppe, Roland Baader, Ron Paul, Ayn Rand"/>
       |
       |
       |
       |
       |    <link href="/static/ef/style.ea786551.css" rel="stylesheet">
       |    <link rel="stylesheet" media="print" href="/static/ef/print.css">
       |    <!--[if lt IE 9]>
       |    <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
       |    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
       |    <![endif]-->
       |
       |    <link rel="alternate" type="application/rss+xml" title="News" href="/feed/rss/"/>
       |    <link rel="alternate" type="application/atom+xml" title="News" href="/feed/atom/"/>
       |
       |
       |</head>
       |<body>
       |
       |    <section>
       |        <div class="row">
       |            <div class="col-md-4 col-md-height col-mrect-left">
       |                <aside>
       |
       |
       |
       |                    <div id="marktplatz" class="sbads">
       |                        <h3 class="ahd"><a href="/adverts/">Marktplatz</a></h3>
       |
       |                        <div class="sbad">
       |                            <a class="head" href="/adverts/redirect/164/">Freiheitsfunken</a><img height="1" width="1" class="trk" alt="" src="/adtrk/tr.gif?ad=164"/>
       |                            <div class="body">
       |
       |                                <a href="/adverts/redirect/164/"><img src="/media/assets/adverts/Werbebanner_klein.jpg.300x80_q95_crop_upscale.jpg" alt=""/></a>
       |
       |                            </div>
       |                            <a class="domain" href="/adverts/redirect/164/">https://freiheitsfunken.info/</a>
       |                        </div>
       |
       |                        <div class="sbad">
       |                            <a class="head" href="/adverts/redirect/158/">Geldzeitenwende</a><img height="1" width="1" class="trk" alt="" src="/adtrk/tr.gif?ad=158"/>
       |                            <div class="body large">
       |
       |                                <a href="/adverts/redirect/158/"><img src="/media/assets/adverts/Cover-Mudlack_klein.jpg.300x400_q95_crop_upscale.jpg" alt=""/></a>
       |
       |                            </div>
       |                            <a class="domain" href="/adverts/redirect/158/">https://ef-magazin.de/accounts/book-order/</a>
       |                        </div>
       |
       |                    </div>
       |                </aside>
       |            </div>
       |            <div class="col-md-7 col-md-offset-1">
       |
       |                <article>
       |                    <div class="row">
       |
       |                        <a class="article-image left" href="/2023/09/22/20822-absenkung-des-wahlalters-make-berlin-gruen-again">
       |                            <img class="img-responsive" src="/media/assets/article/2023/09/Kai-Wegner-Foto-Sven-Teschke.jpg.640x640_q75_box-0%2C0%2C794%2C792_crop_detail.jpg" alt="Artikelbild"/>
       |                        </a>
       |
       |                        <div class="article-main img1" >
       |
       |                            <h2>
       |                                <a href="/2023/09/22/20822-absenkung-des-wahlalters-make-berlin-gruen-again" rel="bookmark">
       |                                    <small>Absenkung des Wahlalters<span class="hidden">:</span> </small>Make Berlin grün again <span class="small" title="Kostenpflichtiger Artikel"><i alt="Icon: Schlüssel" class="fa fa-key fa-smgr"></i></span>
       |                                </a>
       |                            </h2>
       |
       |
       |                            <p class="lead"><a href="/2023/09/22/20822-absenkung-des-wahlalters-make-berlin-gruen-again">Werden Stadt und Land allmählich sturmreif reformiert?</a></p>
       |
       |
       |
       |                            <p class="afoot small">
       |                                <em class="author">von <a href="/autor/jan-mehring">Jan Mehring</a></em>
       |                                | 5 <i class="fa fa-thumbs-o-up"></i>| 1 <i class="fa fa-comments-o"></i>
       |                            </p>
       |
       |
       |
       |                        </div>
       |
       |
       |                    </div>
       |                </article>
       |</html>
       |""".stripMargin
  }
}
