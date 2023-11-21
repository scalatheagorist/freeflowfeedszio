package org.scalatheagorist.freeflowfeedszio.publisher

import org.scalatheagorist.freeflowfeedszio.models.Article
import org.scalatheagorist.freeflowfeedszio.models.HtmlResponse
import org.scalatheagorist.freeflowfeedszio.models.RSSFeed
import zio.Scope
import zio.ZIO
import zio.test.Spec
import zio.test.TestEnvironment
import zio.test.ZIOSpecDefault
import zio.test.assertTrue

object FreiheitsfunkenSpec extends ZIOSpecDefault {
  override def spec: Spec[TestEnvironment with Scope, Any] = suite("Freiheitsfunken") {
    test("toRSSFeedStream") {
      for {
        freiheitsfunken <- ZIO.succeed(Freiheitsfunken("https://www.freiheitsfunken.de"))
        htmlResp        <- ZIO.succeed(HtmlResponse(Publisher.FREIHEITSFUNKEN, htmlResponse))
        build           <- freiheitsfunken.toRSSFeedStream(htmlResp).runLast
      } yield assertTrue(build.contains(RSSFeed(
        author = "Prollius",
        article = Article(
          title = "Freiheitsespresso X: Der Hofstaat",
          link = "https://www.freiheitsfunken.de/2023/10/22/21185-freiheitsespresso-x-der-hofstaat"
        ),
        publisher = Publisher.FREIHEITSFUNKEN,
        lang = Lang.DE
      )))
    }
  }

  private def htmlResponse: String = {
    s"""
       |
       |<html lang="de" data-lt-installed="true"><head>
       |    <meta charset="utf-8">
       |    <meta http-equiv="X-UA-Compatible" content="IE=edge">
       |    <meta name="viewport" content="width=device-width, initial-scale=1">
       |    <title>FreiheitsFunken</title>
       |    <link rel="shortcut icon" href="/static/img/favicon.svg">
       |    <link rel="”mask-icon”" href="”/static/img/mask-icon.svg”" color="”#cfa34f&quot;">
       |    <link rel="apple-touch-icon" href="/static/img/apple-touch-icon.png">
       |    <link rel="manifest" href="/static/manifest.json">
       |
       |
       |        <meta name="description" content="Libertäres Portal und Stimme der Freiheit seit 2022. Erfrischend anders, erfrischend libertär, einfach Freiheitsfunken.">
       |        <meta property="og:url" content="https://freiheitsfunken.info/">
       |        <meta property="og:title" content="FreiheitsFunken">
       |        <meta property="og:description" content="Libertäres Portal und Stimme der Freiheit seit 2022. Erfrischend anders, erfrischend libertär, einfach Freiheitsfunken.">
       |        <meta property="og:site_name" content="FreiheitsFunken">
       |        <meta property="og:type" content="article">
       |        <meta property="og:locale" content="de_de">
       |        <meta property="article:publisher" content="freiheitsfunken">
       |        <meta property="twitter:site" content="@freiheitsfunken">
       |
       |            <meta name="keywords" content="Freiheitsfunken, Freiheit, Roland Baader, Libertär, Libertarismus, Marktwirtschaft, marktwirtschaftlich, Kapitalismus, kapitalistisch, Marktradikal, Marktradikalismus, Freiheit, freiheitlich, Liberal, Liberalismus, Magazin, Monatsmagazin, Freie Presse,
       |Konterrevolution, konterrevolutionär, Alternative Medien, Anti-Kommunistisch, Anti-sozialistisch, Hans-Hermann Hoppe, Ron Paul, Ayn Rand">
       |
       |
       |
       |
       |    <link href="/static/ef/style.7413d576.css" rel="stylesheet">
       |    <link rel="stylesheet" media="print" href="/static/ef/print.css">
       |    <!--[if lt IE 9]>
       |      <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
       |      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
       |    <![endif]-->
       |
       |    <link rel="alternate" type="application/rss+xml" title="News" href="/feed/rss/">
       |    <link rel="alternate" type="application/atom+xml" title="News" href="/feed/atom/">
       |
       |        <script type="text/javascript" async="" defer="" src="//stats.ef-magazin.de/matomo.js"></script><script async="" src="https://pagead2.googlesyndication.com/pagead/js/adsbygoogle.js?client=ca-pub-5425042520745476" crossorigin="anonymous"></script>
       |
       |
       |
       |</head>
       |<body>
       |            <div class="col-md-4">
       |
       |<article>
       |    <a class="artlead" href="/2023/10/22/21185-freiheitsespresso-x-der-hofstaat">
       |        <div class="artimg" style="background-image: url(/media/assets/article/2023/10/Operettenstaat.jpg.727x485_q75_box-15%2C0%2C4636%2C3078_crop_detail.jpg)"></div>
       |        <div class="autmask"></div>
       |
       |        <div class="autimg" style="background-image: url(/media/assets/authors/Gold-Prollius.jpg.388x485_q75_box-570%2C19%2C1230%2C844_crop_detail.jpg)"></div>
       |
       |    </a>
       |    <div class="">
       |
       |            <h2>
       |                <a href="/2023/10/22/21185-freiheitsespresso-x-der-hofstaat" rel="bookmark">
       |                    <small>Freiheitsespresso X<span class="hidden">:</span> </small>Der Hofstaat
       |
       |                </a>
       |            </h2>
       |
       |
       |            <p class="lead"><a href="/2023/10/22/21185-freiheitsespresso-x-der-hofstaat">Über das alternativlose „Weiter so!“ der Mächtigen</a></p>
       |            <p>Deutschland hat ein Problem. Dieses Problem ist alt. Es existiert seit über 5.000 Jahren in dieser Welt. Schon die alten Ägypter kannten es: Die Rede ist vom Hofstaat.</p>
       |
       |
       |            <p class="afoot small">
       |                <em class="author">von
       |
       |                    Michael von Prollius
       |                    </em>
       |                | 5 <i class="fa fa-thumbs-o-up"></i>
       |            </p>
       |
       |    </div>
       |</article>
       |            </div>
       |        </div>
       |
       |
       |
       |        <section class="category">
       |            <a href="https://ef-magazin.de/"><h1 class="category-header">Unser Partnermagazin: eigentümlich frei</h1></a>
       |            <div class="bg-gold pt-5 pl-5 pr-5 mb-5">
       |                <div class="row">
       |
       |                        <div class="col-md-2x4 col-xs-6 text-center mb-5 ">
       |                            <a class="cover" href="https://ef-magazin.de/archiv/ef/237/inhalt.html">
       |                                <img alt="Cover: ef 237" src="https://ef-magazin.de/media/assets/cover/ef-237.jpg" class="img-responsive">
       |                            </a>
       |                        </div>
       |
       |                        <div class="col-md-2x4 col-xs-6 text-center mb-5 ">
       |                            <a class="cover" href="https://ef-magazin.de/archiv/ef/236/inhalt.html">
       |                                <img alt="Cover: ef 236" src="https://ef-magazin.de/media/assets/cover/ef-236.jpg" class="img-responsive">
       |                            </a>
       |                        </div>
       |
       |                        <div class="col-md-2x4 col-xs-6 text-center mb-5 ">
       |                            <a class="cover" href="https://ef-magazin.de/archiv/ef/234/inhalt.html">
       |                                <img alt="Cover: ef 234" src="https://ef-magazin.de/media/assets/cover/ef-234.jpg" class="img-responsive">
       |                            </a>
       |                        </div>
       |
       |                        <div class="col-md-2x4 col-xs-6 text-center mb-5 ">
       |                            <a class="cover" href="https://ef-magazin.de/archiv/ef/233/inhalt.html">
       |                                <img alt="Cover: ef 233" src="https://ef-magazin.de/media/assets/cover/ef-233.jpg" class="img-responsive">
       |                            </a>
       |                        </div>
       |
       |                        <div class="col-md-2x4 col-xs-6 text-center mb-5 hidden-smx">
       |                            <a class="cover" href="https://ef-magazin.de/archiv/ef/232/inhalt.html">
       |                                <img alt="Cover: ef 232" src="https://ef-magazin.de/media/assets/cover/ef-232.jpg" class="img-responsive">
       |                            </a>
       |                        </div>
       |
       |                        <div class="col-md-2x4 col-xs-6 text-center mb-5 hidden-smx">
       |                            <a class="cover" href="https://ef-magazin.de/archiv/ef/231/inhalt.html">
       |                                <img alt="Cover: ef 231" src="https://ef-magazin.de/media/assets/cover/ef-231.jpg" class="img-responsive">
       |                            </a>
       |                        </div>
       |
       |                        <div class="col-md-2x4 col-xs-6 text-center mb-5 hidden-smx">
       |                            <a class="cover" href="https://ef-magazin.de/archiv/ef/229/inhalt.html">
       |                                <img alt="Cover: ef 229" src="https://ef-magazin.de/media/assets/cover/ef-229.jpg" class="img-responsive">
       |                            </a>
       |                        </div>
       |
       |                        <div class="col-md-2x4 col-xs-6 text-center mb-5 hidden-smx">
       |                            <a class="cover" href="https://ef-magazin.de/archiv/ef/228/inhalt.html">
       |                                <img alt="Cover: ef 228" src="https://ef-magazin.de/media/assets/cover/ef-228.jpg" class="img-responsive">
       |                            </a>
       |                        </div>
       |
       |                        <div class="col-md-2x4 col-xs-6 text-center mb-5 hidden-smx">
       |                            <a class="cover" href="https://ef-magazin.de/archiv/ef/227/inhalt.html">
       |                                <img alt="Cover: ef 227" src="https://ef-magazin.de/media/assets/cover/ef-227.jpg" class="img-responsive">
       |                            </a>
       |                        </div>
       |
       |                        <div class="col-md-2x4 col-xs-6 text-center mb-5 hidden-smx">
       |                            <a class="cover" href="https://ef-magazin.de/archiv/ef/226/inhalt.html">
       |                                <img alt="Cover: ef 226" src="https://ef-magazin.de/media/assets/cover/ef-226.jpg" class="img-responsive">
       |                            </a>
       |                        </div>
       |
       |                </div>
       |            </div>
       |        </section>
       |
       |
       |
       |        <div class="row">
       |            <div class="col-md-12">
       |</body></html>
       |""".stripMargin
  }
}
