package org.scalatheagorist.freeflowfeedszio.publisher

import org.scalatheagorist.freeflowfeedszio.models.Article
import org.scalatheagorist.freeflowfeedszio.models.HtmlResponse
import org.scalatheagorist.freeflowfeedszio.models.RSSFeed
import zio._
import zio.test._

object EfMagazinTest extends ZIOSpecDefault {
  override def spec: Spec[TestEnvironment with Scope, Any] = suite("EfMagazin") {
    test("toRSSFeedStream") {
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
       |
       |
       |
       |<style>
       |    .social-links a {
       |        display: block;
       |        margin-bottom: 11px;
       |    }
       |    .social-links a > svg {
       |        fill: #0066CC;
       |    }
       |
       |</style>
       |<div class="container">
       |    <div class="row">
       |        <div class="col-sm-12">
       |            <header>
       |                <a class="logo" href="/"><img src="/static/img/ef-logo.png" alt="eigentümlich frei"/></a>
       |                <div class="nav-top hidden-print">
       |                    <ul class="nav">
       |                        <li><a href="/warum-ef/">Warum ef?</a></li>
       |                        <li><a href="/accounts/order/">Bestellen</a></li>
       |                        <li><a href="/autoren/">Autoren</a></li>
       |                        <li><a href="/archiv/">Archiv</a></li>
       |                        <li><a href="/accounts/book-order/">Buchverkauf</a></li>
       |                        <li><a href="/adverts/">Marktplatz</a></li>
       |                        <li><a href="/impressum/">Impressum</a></li>
       |                        <li class="social hidden-xs hidden-sm">
       |                            <a href="https://facebook.com/efmagazin" title="ef bei Facebook" target="_blank">
       |                                <svg width="38.7" height="38.7" version="1.1" viewBox="0 0 38.7 38.7" xmlns="http://www.w3.org/2000/svg">
       |                                    <path d="m31.444 0q2.9982 0 5.1272 2.129 2.129 2.129 2.129 5.1272v24.187q0 2.9982-2.129 5.1272-2.129 2.129-5.1272 2.129h-4.7367v-14.991h5.0139l0.75586-5.8453h-5.7697v-3.7289q0-1.4109 0.59209-2.1164 0.59209-0.70547 2.3054-0.70547l3.0738-0.0252v-5.2154q-1.5873-0.22676-4.4848-0.22676-3.4266 0-5.48 2.0156-2.0534 2.0156-2.0534 5.6941v4.3084h-5.0391v5.8453h5.0391v14.991h-13.404q-2.9982 0-5.1272-2.129-2.129-2.129-2.129-5.1272v-24.187q0-2.9982 2.129-5.1272 2.129-2.129 5.1272-2.129z" stroke-width=".025195"/>
       |                                </svg>
       |
       |                            </a>
       |                        </li>
       |                        <li class="social hidden-xs hidden-sm">
       |                            <a href="https://twitter.com/efonline" title="ef auf Twitter" target="_blank">
       |                                <svg width="38.7" height="38.7" version="1.1" viewBox="0 0 38.7 38.7" xmlns="http://www.w3.org/2000/svg">
       |                                    <path d="m38.699 7.3669q-1.6452 2.4064-3.978 4.1008 0.02456 0.34378 0.02456 1.0313 0 3.1922-0.93311 6.3721t-2.8362 6.102q-1.903 2.9221-4.5305 5.1689-2.6274 2.2468-6.3353 3.5851-3.7079 1.3383-7.9314 1.3383-6.6545 0-12.18-3.5605 0.85944 0.09822 1.9153 0.09822 5.525 0 9.8467-3.3887-2.5783-0.04911-4.6164-1.5838t-2.7993-3.9166q0.81033 0.12278 1.4979 0.12278 1.0559 0 2.0872-0.27011-2.7502-0.56478-4.555-2.7379-1.8048-2.1732-1.8048-5.0461v-0.09822q1.6698 0.93311 3.5851 1.0068-1.6207-1.0804-2.5783-2.8239-0.95766-1.7434-0.95766-3.7815 0-2.1609 1.0804-4.0025 2.9712 3.6588 7.2316 5.8565 4.2604 2.1977 9.1224 2.4433-0.19644-0.93311-0.19644-1.8171 0-3.2904 2.3205-5.6109t5.6109-2.3205q3.4378 0 5.7951 2.5047 2.6765-0.51566 5.0339-1.9153-0.90855 2.8239-3.4869 4.3709 2.2837-0.24555 4.5673-1.2278z" stroke-width=".024555"/>
       |                                </svg>
       |
       |                            </a>
       |                        </li>
       |                        <li class="social hidden-xs hidden-sm">
       |                            <a href="https://www.instagram.com/efmagazin/" title="ef bei Instagram" target="_blank">
       |                                <svg width="38.7" height="38.7" version="1.1" viewBox="0 0 38.7 38.7" xmlns="http://www.w3.org/2000/svg">
       |                                    <path d="m25.8 19.35q0-2.6707-1.8896-4.5604-1.8896-1.8896-4.5604-1.8896t-4.5604 1.8896q-1.8896 1.8896-1.8896 4.5604t1.8896 4.5604q1.8896 1.8896 4.5604 1.8896t4.5604-1.8896q1.8896-1.8896 1.8896-4.5604zm3.477 0q0 4.132-2.8975 7.0295t-7.0295 2.8975-7.0295-2.8975q-2.8975-2.8975-2.8975-7.0295t2.8975-7.0295q2.8975-2.8975 7.0295-2.8975t7.0295 2.8975q2.8975 2.8975 2.8975 7.0295zm2.7211-10.33q0 0.95742-0.68027 1.6377-0.68027 0.68027-1.6377 0.68027t-1.6377-0.68027q-0.68027-0.68027-0.68027-1.6377t0.68027-1.6377q0.68027-0.68027 1.6377-0.68027t1.6377 0.68027q0.68027 0.68027 0.68027 1.6377zm-12.648-5.543q-0.17637 0-1.9274-0.012598-1.7511-0.012598-2.6581 0t-2.4313 0.075586q-1.5243 0.062989-2.5951 0.25195-1.0708 0.18896-1.8015 0.46611-1.2598 0.50391-2.2172 1.4613-0.95742 0.95742-1.4613 2.2172-0.27715 0.73066-0.46611 1.8015-0.18896 1.0708-0.25195 2.5951-0.062988 1.5243-0.075586 2.4313t0 2.6581q0.012598 1.7511 0.012598 1.9274t-0.012598 1.9274q-0.012598 1.7511 0 2.6581t0.075586 2.4313q0.062988 1.5243 0.25195 2.5951 0.18896 1.0708 0.46611 1.8015 0.50391 1.2598 1.4613 2.2172 0.95742 0.95742 2.2172 1.4613 0.73066 0.27715 1.8015 0.46611 1.0708 0.18896 2.5951 0.25195 1.5243 0.06299 2.4313 0.07559t2.6581 0q1.7511-0.0126 1.9274-0.0126t1.9274 0.0126q1.7511 0.0126 2.6581 0t2.4313-0.07559q1.5243-0.06299 2.5951-0.25195t1.8015-0.46611q1.2598-0.50391 2.2172-1.4613t1.4613-2.2172q0.27715-0.73066 0.46611-1.8015 0.18896-1.0708 0.25195-2.5951 0.06299-1.5243 0.07559-2.4313t0-2.6581q-0.0126-1.7511-0.0126-1.9274t0.0126-1.9274q0.0126-1.7511 0-2.6581t-0.07559-2.4313q-0.062989-1.5243-0.25195-2.5951-0.18896-1.0708-0.46611-1.8015-0.50391-1.2598-1.4613-2.2172-0.95742-0.95742-2.2172-1.4613-0.73066-0.27715-1.8015-0.46611-1.0708-0.18896-2.5951-0.25195-1.5243-0.062988-2.4313-0.075586t-2.6581 0q-1.7511 0.012598-1.9274 0.012598zm19.35 15.873q0 5.7697-0.12598 7.9869-0.25195 5.2406-3.1242 8.1129-2.8723 2.8723-8.1129 3.1242-2.2172 0.12598-7.9869 0.12598t-7.9869-0.12598q-5.2406-0.25195-8.1129-3.1242-2.8723-2.8723-3.1242-8.1129-0.12598-2.2172-0.12598-7.9869t0.12598-7.9869q0.25195-5.2406 3.1242-8.1129 2.8723-2.8723 8.1129-3.1242 2.2172-0.12598 7.9869-0.12598t7.9869 0.12598q5.2406 0.25195 8.1129 3.1242 2.8723 2.8723 3.1242 8.1129 0.12598 2.2172 0.12598 7.9869z" stroke-width=".025195"/>
       |                                </svg>
       |
       |                            </a>
       |                        </li>
       |                        <li class="social hidden-xs hidden-sm">
       |                            <a href="https://www.youtube.com/user/eigentuemlichfreitv" title="ef auf YouTube" target="_blank">
       |                                <svg width="38.7" height="38.7" version="1.1" viewBox="0 0 38.7 38.7" xmlns="http://www.w3.org/2000/svg">
       |                                    <path d="m23.154 29.604v-3.9557q0-1.2598-0.73066-1.2598-0.42832 0-0.83144 0.40312v5.6438q0.40312 0.40312 0.83144 0.40312 0.73066 0 0.73066-1.2346zm4.6359-3.0738h1.6629v-0.85664q0-1.285-0.83144-1.285t-0.83144 1.285zm-14.387-6.702v1.7637h-2.0156v10.658h-1.8645v-10.658h-1.9652v-1.7637zm5.0643 3.1746v9.2467h-1.6881v-1.0078q-0.98262 1.1338-1.9148 1.1338-0.83144 0-1.0582-0.70547-0.15117-0.42832-0.15117-1.3605v-7.3066h1.6629v6.8027q0 0.60469 0.0252 0.65508 0.0252 0.37793 0.37793 0.37793 0.50391 0 1.0582-0.78105v-7.0547zm6.3492 2.7967v3.6785q0 1.3102-0.17637 1.8393-0.30234 1.0582-1.3354 1.0582-0.88184 0-1.7133-1.033v0.90703h-1.6881v-12.421h1.6881v4.0564q0.80625-1.0078 1.7133-1.0078 1.033 0 1.3354 1.0582 0.17637 0.5291 0.17637 1.8645zm6.324 3.2502v0.22676q0 0.73066-0.05039 1.0834-0.07559 0.5543-0.37793 1.0078-0.68027 1.0078-2.0156 1.0078-1.3102 0-2.0408-0.95742-0.5291-0.68027-0.5291-2.1668v-3.2502q0-1.4865 0.50391-2.1668 0.73066-0.95742 2.0156-0.95742t1.9652 0.95742q0.5291 0.73066 0.5291 2.1668v1.9148h-3.351v1.6377q0 1.285 0.85664 1.285 0.60469 0 0.75586-0.65508 0-0.0252 0.0126-0.17637 0.0126-0.15117 0.0126-0.41572v-0.5417zm-11.363-20.761v3.9305q0 1.285-0.80625 1.285t-0.80625-1.285v-3.9305q0-1.3102 0.80625-1.3102t0.80625 1.3102zm13.429 17.964q0-4.4596-0.47871-6.5508-0.25195-1.1086-1.0834-1.8519-0.83144-0.74326-1.9148-0.86924-3.4266-0.37793-10.38-0.37793-6.9287 0-10.355 0.37793-1.1086 0.12598-1.9274 0.86924-0.81885 0.74326-1.0708 1.8519-0.50391 2.192-0.50391 6.5508 0 4.4344 0.50391 6.5508 0.25195 1.0834 1.0708 1.8393 0.81885 0.75586 1.9022 0.88184 3.4518 0.37793 10.38 0.37793t10.38-0.37793q1.0834-0.12598 1.9022-0.88184 0.81885-0.75586 1.0708-1.8393 0.50391-2.1164 0.50391-6.5508zm-19.022-16.402 2.2676-7.4578h-1.8896l-1.285 4.9131-1.3354-4.9131h-1.9652q0.17637 0.57949 0.57949 1.7385l0.60469 1.7385q0.88184 2.5951 1.159 3.9809v5.0643h1.8645zm7.2814 2.0408v-3.2754q0-1.4613-0.5291-2.192-0.73066-0.95742-1.9652-0.95742-1.285 0-1.9652 0.95742-0.5291 0.73066-0.5291 2.192v3.2754q0 1.4613 0.5291 2.192 0.68027 0.95742 1.9652 0.95742 1.2346 0 1.9652-0.95742 0.5291-0.68027 0.5291-2.192zm4.5604 3.0234h1.6881v-9.3223h-1.6881v7.1303q-0.5543 0.78106-1.0582 0.78106-0.37793 0-0.40312-0.40312-0.0252-0.05039-0.0252-0.65508v-6.8531h-1.6881v7.3822q0 0.93223 0.15117 1.3857 0.27715 0.68027 1.0834 0.68027 0.90703 0 1.94-1.1338zm12.673-7.6594v24.188q0 2.9982-2.129 5.1272t-5.1272 2.129h-24.188q-2.9982 0-5.1272-2.129t-2.129-5.1272v-24.188q0-2.9982 2.129-5.1272t5.1272-2.129h24.188q2.9982 0 5.1272 2.129t2.129 5.1272z" stroke-width=".025195"/>
       |                                </svg>
       |
       |                            </a>
       |                        </li>
       |                        <li class="social hidden-xs hidden-sm">
       |                            <a href="https://t.me/efmagazin" title="ef bei Telegram" target="_blank">
       |                                <svg width="38.7" height="38.7" version="1.1" viewBox="0 0 38.7 38.7" xmlns="http://www.w3.org/2000/svg">
       |                                    <path d="m25.678 28.226 3.1746-14.966q0.19436-0.95022-0.22676-1.3605-0.42112-0.41032-1.1122-0.15117l-18.659 7.1915q-0.62628 0.23756-0.85304 0.5399t-0.05399 0.57229q0.17277 0.26995 0.69107 0.42112l4.7727 1.4901 11.079-6.9755q0.45352-0.30234 0.69107-0.12958 0.15117 0.10798-0.08638 0.32394l-8.9623 8.0985-0.34554 4.9239q0.49671 0 0.97182-0.47511l2.3324-2.246 4.8375 3.5633q1.3821 0.77746 1.7493-0.82065zm13.022-8.8759q0 3.9305-1.5333 7.5154-1.5333 3.5849-4.1248 6.1765t-6.1765 4.1248q-3.5849 1.5333-7.5154 1.5333t-7.5154-1.5333q-3.5849-1.5333-6.1765-4.1248-2.5915-2.5915-4.1248-6.1765-1.5333-3.5849-1.5333-7.5154t1.5333-7.5154q1.5333-3.5849 4.1248-6.1765 2.5915-2.5915 6.1765-4.1248 3.5849-1.5333 7.5154-1.5333t7.5154 1.5333q3.5849 1.5333 6.1765 4.1248 2.5915 2.5915 4.1248 6.1765 1.5333 3.5849 1.5333 7.5154z" stroke-width=".021596"/>
       |                                </svg>
       |
       |                            </a>
       |                        </li>
       |                        <li class="social hidden-xs hidden-sm">
       |                            <a href="https://parler.com/profile/efmagazin/posts" title="ef bei Parler" target="_blank">
       |                                <svg width="38.7" height="38.7" version="1.1" viewBox="0 0 38.7 38.7" xmlns="http://www.w3.org/2000/svg">
       |                                    <path d="m22.583 32.234h-9.6987v-3.2805c0-1.7591 1.4262-3.1379 3.1378-3.1379h6.5134c5.3723 0 9.7463-4.3264 9.7463-9.6512s-4.3264-9.6512-9.6988-9.6512h-0.0951l-2.0443-0.0476h-20.444c0-3.5657 2.9001-6.4658 6.4658-6.4658h14.073l2.0919 0.0475c8.843 0.0476 16.07 7.2741 16.07 16.117s-7.2265 16.07-16.117 16.07z"/>
       |                                    <path d="m6.4658 38.7c-3.5657 0-6.4183-2.9001-6.4183-6.4183v-13.122c-0.0475-3.4706 2.7575-6.2756 6.2282-6.2756h16.307c1.7591 0 3.2329 1.4262 3.2329 3.2329 0 1.7591-1.4263 3.2329-3.2329 3.2329h-12.932c-1.7591 0-3.1854 1.4263-3.1854 3.1854z"/>
       |                                </svg>
       |
       |                            </a>
       |                        </li>
       |                        <li class="social hidden-xs hidden-sm">
       |                            <a href="https://lbry.tv/@efmagazin:6" title="ef bei lbry" target="_blank">
       |                                <svg width="38.7" height="38.7" version="1.1" viewBox="0 0 38.7 38.7" xmlns="http://www.w3.org/2000/svg">
       |                                    <path d="m0 19.35v-19.35c0.56937 0.0044313 19.35 0 19.35 0h19.35v38.7h-38.7zm12.244 13.557c0.03614-0.04355 0.04941-0.26782 0.04063-0.68691l-0.01303-0.62226-2.3984-0.02486v-2.6732c0-1.4703-0.01272-2.7064-0.028268-2.7469-0.023899-0.06228-0.12825-0.07366-0.67527-0.07366h-0.647v3.4152c0 1.8783 0.013971 3.4291 0.031047 3.4462 0.017076 0.01708 0.8423 0.03105 1.8338 0.03105 1.4812 0 1.8124-0.01153 1.8564-0.06466zm4.4052-0.0721c0.71837-0.23514 1.1295-0.56344 1.3874-1.1078 0.13815-0.29166 0.14925-0.35076 0.14678-0.78123-0.0033-0.57192-0.11772-0.90657-0.41672-1.2186l-0.19985-0.20858 0.19956-0.18395c0.31954-0.29454 0.41752-0.57139 0.4187-1.1831 9.26e-4 -0.48166-8e-3 -0.53124-0.14934-0.82966-0.20655-0.43608-0.5547-0.76452-1.0154-0.95791-0.58518-0.24564-0.93227-0.28808-2.2404-0.274l-1.1577 0.01247-0.01205 3.3764c-0.0066 1.857-0.0013 3.4045 0.01181 3.4388 0.01942 0.05085 0.27261 0.05888 1.3626 0.04319 1.24-0.01784 1.3628-0.02713 1.6647-0.12597zm-1.6998-1.9305v-0.68045h0.56151c0.67762 0 0.93002 0.05668 1.1418 0.2564 0.14398 0.13579 0.15952 0.17534 0.15952 0.40593 0 0.204-0.02217 0.2794-0.11003 0.3742-0.22503 0.24281-0.37554 0.28502-1.0892 0.30541l-0.66363 0.01896zm0.01036-2.7412 0.01292-0.66363 0.58213 0.0033c0.67699 0.0039 0.90212 0.05621 1.108 0.25751 0.131 0.12808 0.14938 0.17645 0.14938 0.39309 0 0.50613-0.31459 0.67332-1.267 0.67332h-0.59839zm6.203 4.7801c0.06482-0.02488 0.07367-0.18999 0.07367-1.3759v-1.3476l0.65577 0.02736 0.37113 0.81498c0.20412 0.44824 0.40143 0.87785 0.43847 0.95469 0.03704 0.07684 0.15074 0.32308 0.25267 0.5472l0.18533 0.40749h0.69882c0.38435 0 0.70994-0.018 0.72354-0.04 0.02685-0.04345-0.19184-0.54956-0.85142-1.9704-0.23387-0.50377-0.42505-0.93338-0.42485-0.95469 1.86e-4 -0.02131 0.08708-0.08828 0.19307-0.14882 0.92207-0.5267 1.249-1.5764 0.80147-2.5736-0.20444-0.45556-0.69004-0.84185-1.2976-1.0322-0.46476-0.14563-0.8729-0.1791-2.0025-0.16421l-1.1177 0.01473-0.01205 3.3764c-0.0066 1.857-2e-3 3.4026 0.01018 3.4346 0.0162 0.04243 0.18574 0.05821 0.6253 0.05821 0.33169 0 0.63622-0.01272 0.67673-0.02827zm0.07367-4.794v-0.68624l0.61706 0.02695c0.71952 0.03143 0.92156 0.08596 1.1088 0.29925 0.25363 0.28887 0.1658 0.69112-0.19615 0.89833-0.18484 0.10582-0.26034 0.11732-0.86612 0.13193l-0.66363 0.01601zm7.6375 3.1923 0.02328-1.6067 0.18343-0.25614c0.10089-0.14088 0.23711-0.33232 0.30271-0.42542 0.0656-0.09311 0.39171-0.55231 0.72469-1.0205 0.33298-0.46814 0.65782-0.92636 0.72187-1.0183 0.06405-0.0919 0.21599-0.30597 0.33764-0.47571 0.33917-0.47325 0.35562-0.45979-0.56211-0.45979h-0.7833l-0.5679 0.80334c-0.31234 0.44184-0.66715 0.94409-0.78846 1.1161-0.1213 0.17203-0.23103 0.31303-0.24384 0.31334-0.01281 3.26e-4 -0.38187-0.49671-0.82013-1.1045l-0.79685-1.105-0.79819-0.01274c-0.639-0.0102-0.79819-4.19e-4 -0.79819 0.04912 0 0.03402 0.17289 0.3021 0.38421 0.59572 0.21131 0.29362 0.44708 0.62356 0.52392 0.73321 0.07684 0.10964 0.25497 0.36188 0.39585 0.56054 0.14088 0.19865 0.47094 0.66768 0.73348 1.0423l0.47735 0.68109v1.5789c0 0.8684 0.01454 1.5935 0.0323 1.6112 0.01777 0.01777 0.3164 0.0265 0.66363 0.0194l0.63133-0.0129zm-9.0489-7.766c0.25115-0.15277 0.73956-0.45177 1.0853-0.66445 0.34579-0.21268 1.0059-0.6153 1.467-0.89471s0.91162-0.55586 1.0013-0.61433c0.08965-0.05847 0.65548-0.4066 1.2574-0.77363 0.60192-0.36703 1.4192-0.86632 1.8162-1.1095 0.39701-0.24321 1.0781-0.65974 1.5135-0.92563 0.83972-0.51274 0.97629-0.59629 2.3911-1.4628 0.49548-0.30344 0.90614-0.55172 0.91258-0.55172 0.0064 0-0.01701 0.1519-0.05211 0.33757-0.07564 0.40003-0.04974 0.55971 0.11134 0.68641 0.1585 0.12468 0.53631 0.12968 0.65729 0.0087 0.13353-0.13352 0.59782-2.023 0.54216-2.2064-0.05286-0.17413-0.20173-0.30307-0.40927-0.35446-0.10704-0.02651-0.40419-0.10124-0.66033-0.16607-1.3041-0.33006-1.4837-0.31756-1.5921 0.11086-0.09211 0.36408 0.05131 0.54112 0.52774 0.65146l0.31922 0.07392-0.18628 0.12143c-0.17046 0.11111-2.6303 1.6201-3.3996 2.0855-0.1793 0.10846-0.69273 0.42323-1.141 0.69949-0.44824 0.27625-1.1293 0.69383-1.5135 0.92795-0.38421 0.23412-1.0234 0.62452-1.4204 0.86756-0.39701 0.24304-0.93141 0.56932-1.1875 0.72507-0.25614 0.15575-0.80101 0.49003-1.2108 0.74283-0.40982 0.25281-0.92089 0.56751-1.1357 0.69935l-0.39059 0.2397-0.49425-0.2459c-0.27184-0.13524-1.7412-0.8632-3.2652-1.6177s-3.9969-1.9789-5.4953-2.7209c-1.4984-0.74203-2.8101-1.3898-2.9149-1.4395l-0.19052-0.09039-0.030689-1.1236c-0.048384-1.7714-0.041823-2.159 0.037668-2.2249 0.037596-0.0312 0.18953-0.13052 0.33764-0.2207 0.37118-0.22602 1.1079-0.67798 1.9924-1.2224 0.40982-0.25223 0.93374-0.57285 1.1643-0.71249 0.23052-0.13964 0.64966-0.39636 0.93141-0.57048 0.28175-0.17412 0.62754-0.38693 0.76841-0.47289 0.48184-0.29404 1.746-1.0693 2.1888-1.3423 0.24333-0.15003 0.76725-0.47177 1.1643-0.71498 0.39701-0.24321 0.95236-0.58447 1.2341-0.75836 0.28175-0.17388 0.63802-0.39117 0.7917-0.48285 0.15368-0.091683 0.47851-0.29094 0.72184-0.4428 0.24333-0.15186 0.56816-0.35097 0.72184-0.44248 0.15368-0.091502 0.46304-0.28029 0.68746-0.41954l0.40804-0.25317 0.59322 0.28356c0.65882 0.31491 1.4491 0.69436 2.3629 1.1345 1.1202 0.53956 1.814 0.87158 2.1888 1.0475 0.20491 0.096148 0.70787 0.33701 1.1177 0.53526 0.91275 0.44153 1.8563 0.89552 2.4449 1.1764 0.24333 0.1161 0.83012 0.39789 1.304 0.6262 0.47385 0.22831 1.0246 0.49256 1.2238 0.58722 0.35343 0.16792 0.36194 0.17592 0.34928 0.32828-0.01345 0.16175-0.11081 0.23178-1.4334 1.0309-0.25231 0.15246-0.86407 0.52788-1.5601 0.95741-0.28175 0.17387-0.77423 0.47714-1.0944 0.67394-0.32017 0.1968-0.86505 0.53189-1.2108 0.74464-0.96248 0.5922-1.7032 1.0476-2.5542 1.5704-0.42657 0.26206-0.96419 0.59242-1.1947 0.73413-0.23052 0.14171-0.75444 0.46352-1.1643 0.71512-0.40982 0.2516-0.97565 0.60003-1.2574 0.77428-0.28175 0.17425-0.60802 0.37397-0.72505 0.44383l-0.21278 0.12702-0.48911-0.23832c-0.26901-0.13107-0.781-0.37856-1.1378-0.54997-1.5375-0.7387-2.1553-1.035-3.1867-1.5283-0.60192-0.28788-1.3773-0.65941-1.7231-0.82563-0.34578-0.16622-0.8697-0.41745-1.1643-0.55828s-0.81848-0.39257-1.1643-0.55941c-0.34579-0.16684-0.70427-0.31355-0.79663-0.32601-0.40408-0.05451-0.64085 0.33968-0.43876 0.73047 0.063873 0.12352 0.23844 0.22455 1.1736 0.6792 0.60419 0.29375 1.1046 0.5341 1.112 0.5341 0.0074 0 0.40809 0.19064 0.89041 0.42364s1.0865 0.52446 1.3427 0.64768 0.86069 0.41459 1.3435 0.64748c0.48276 0.23289 0.88446 0.42344 0.89266 0.42344 0.0082 0 0.51435 0.24261 1.1248 0.53914 2.236 1.0862 2.2452 1.0894 2.5786 0.91183 0.08866-0.04722 0.4022-0.23492 0.69676-0.4171 0.29456-0.18219 0.61939-0.38122 0.72184-0.4423 0.10246-0.06108 0.4168-0.25436 0.69856-0.42952 0.64021-0.39801 1.4637-0.90512 1.7697-1.0898 0.12807-0.07729 0.44681-0.27215 0.70832-0.43302 0.26151-0.16087 0.83345-0.51254 1.271-0.78149 0.43753-0.26894 0.98849-0.60874 1.2244-0.75511s0.55458-0.34105 0.70826-0.43262c0.15368-0.09157 0.50995-0.31018 0.7917-0.4858s0.82662-0.51119 1.2108-0.74572c0.38421-0.23453 0.94284-0.57726 1.2414-0.76162 0.29858-0.18436 0.84772-0.52076 1.2203-0.74756 0.37259-0.2268 0.71194-0.46501 0.7541-0.52936 0.10487-0.16006 0.07974-1.1753-0.03434-1.3868-0.08496-0.15754-0.05064-0.13898-1.8542-1.0032-1.8062-0.86545-2.2177-1.0628-2.9572-1.4179-0.35859-0.17221-0.87203-0.41825-1.141-0.54676-0.26894-0.12851-0.79286-0.38014-1.1643-0.55917-0.83494-0.40249-1.3886-0.6683-2.8175-1.3528-0.61473-0.29446-1.4111-0.6779-1.7697-0.85208-0.46105-0.22396-0.70654-0.31743-0.83827-0.31917-0.15691-0.00208-0.3111 0.074062-0.97798 0.48295-0.43543 0.26698-1.1165 0.68453-1.5135 0.92789-0.39701 0.24336-0.87537 0.53822-1.063 0.65524-0.36894 0.23008-0.36373 0.22691-0.77651 0.47433-0.15368 0.092119-0.45755 0.27829-0.67527 0.41372-0.21772 0.13543-0.8045 0.49609-1.304 0.80147-0.49947 0.30538-1.0443 0.64029-1.2108 0.74425-0.16649 0.10396-0.36558 0.22569-0.44242 0.27052-0.07684 0.044829-0.27593 0.1671-0.44242 0.27172s-0.42845 0.26482-0.58213 0.35601c-0.15368 0.091187-0.58329 0.35385-0.95469 0.58369-0.3714 0.22984-0.91627 0.56496-1.2108 0.74471-0.29456 0.17975-0.87087 0.53436-1.2807 0.78802-0.40982 0.25366-0.93036 0.57137-1.1568 0.70601-0.2264 0.13464-0.44394 0.29413-0.48344 0.35441-0.062186 0.09491-0.070816 0.37642-0.064354 2.0994 0.0041 1.0944 0.019632 2.0577 0.034504 2.1408 0.038357 0.21423 0.25489 0.38431 0.8649 0.67936 0.28915 0.13986 1.9717 0.97121 3.7391 1.8475 1.7673 0.87624 4.0097 1.9869 4.983 2.468s2.1993 1.0886 2.7244 1.3499c0.82046 0.40822 0.97888 0.4718 1.1268 0.45226 0.09672-0.01278 0.37202-0.14437 0.6287-0.3005z" stroke-width=".04657"/>
       |                                </svg>
       |
       |                            </a>
       |                        </li>
       |                        <li class="social hidden-xs hidden-sm">
       |                            <a href="https://www.bitchute.com/channel/efmagazin/" title="ef bei bitchute" target="_blank">
       |                                <svg width="38.7" height="38.7" version="1.1" viewBox="0 0 38.7 38.7" xmlns="http://www.w3.org/2000/svg">
       |                                    <path d="m15.069 38.135c-7.3337-1.6964-12.843-7.2678-14.528-14.691-0.67497-2.9742-0.66938-5.2055 0.020665-8.2476 1.676-7.3889 7.2382-12.937 14.656-14.62 1.4593-0.33103 2.965-0.52148 4.123-0.52148 2.2331 0 5.7239 0.75854 7.8335 1.7022 1.4205 0.63541 4.1914 2.3489 4.1795 2.5845-0.0031 0.059957-0.32929 0.35046-0.72503 0.64555-0.39574 0.2951-1.8861 1.4789-3.3119 2.6306l-2.5923 2.0941-1.603-0.5381c-3.1134-1.0451-7.0614-0.64899-9.5691 0.96015-1.5361 0.98571-3.4107 3.1078-4.1733 4.7244-0.63833 1.3531-0.65949 1.4954-0.68476 4.6079l-0.026081 3.2117-0.83395 0.66866c-3.739 2.9979-5.4824 4.4855-5.3188 4.5384 0.10592 0.03426 1.9285-0.41469 4.0501-0.99766l3.8575-1.0599 1.4364 1.3867c0.92639 0.89435 1.9571 1.6333 2.9029 2.0813 2.9234 1.0062 6.6825 1.1913 9.1573-0.02316 1.7411-0.85714 3.8487-2.7702 4.8084-4.3646 0.38241-0.63532 0.87745-1.8724 1.1001-2.7491 0.33445-1.317 0.49332-1.6204 0.91432-1.746 0.28025-0.08364 2.0548-0.55886 3.9434-1.056 1.8886-0.49719 3.5546-0.95012 3.7021-1.0065 0.45593-0.17425 0.31608 2.6128-0.25812 5.144-0.984 4.3378-3.3179 8.1459-6.6033 10.775-4.8275 3.8624-10.607 5.2202-16.458 3.8666z" stroke-width=".21462"/>
       |                                </svg>
       |
       |                            </a>
       |                        </li>
       |                    </ul>
       |                </div>
       |                <div class="btn-group hidden-print" style="position: absolute; right: 10px; top: 10px;">
       |
       |                    <a class="btn btn-default dropdown-toggle" href="/accounts/login/" data-toggle="dropdown" aria-expanded="false">
       |                        Anmelden <span class="caret"></span>
       |                    </a>
       |                    <ul class="dropdown-menu pull-right" role="menu">
       |                        <li><a href="/accounts/login/?next=/">Anmelden</a></li>
       |                        <li class="divider"></li>
       |                        <li><a href="/accounts/password/reset/">Passwort zurücksetzen</a></li>
       |                        <li><a href="/accounts/register/">Registrierung für Abonnenten</a></li>
       |                    </ul>
       |
       |                </div>
       |            </header>
       |        </div>
       |    </div>
       |</div>
       |
       |<section id="top" class="container">
       |    <div class="row">
       |        <div class="col-md-12">
       |
       |            <article>
       |                <h2 class="mt-0">
       |                    <div class="article-lead-image"><a href="/2023/10/01/20837-javier-milei-rechter-populismus-als-erfolgsstrategie"><img class="img-responsive" src="/media/assets/article/2023/09/IMG_20221009_154412.jpg.1280x720_q75_box-0%2C0%2C3264%2C1836_crop_detail.jpg"></a></div>
       |                    <a href="/2023/10/01/20837-javier-milei-rechter-populismus-als-erfolgsstrategie" rel="bookmark">
       |                        <small>Javier Milei<span class="hidden">:</span> </small>Rechter Populismus als Erfolgsstrategie <span class="small" title="Erschienen in ef 236"><i alt="Icon: Heft" class="fa fa-newspaper-o fa-smgr"></i></span></a></h2>
       |                <p class="lead"><a href="/2023/10/01/20837-javier-milei-rechter-populismus-als-erfolgsstrategie">Vamos, Javier! Es lebe die Freiheit!</a></p>
       |                <p>Philipp Bagus: „Er kann die Wahl gewinnen. Er kann Präsident von Argentinien werden. Ein Anarchokapitalist. Die richtigen Ideen und Theorien sind vorhanden, aber sie sind bis jetzt noch nicht erfolgreich in der Praxis. Wie lässt sich das ändern?“</p>
       |
       |                <p class="afoot small mb-0">
       |                    <em class="author">von <a href="/autor/philipp-bagus">Philipp Bagus</a></em>
       |                    | 4 <i class="fa fa-thumbs-o-up"></i>| 1 <i class="fa fa-comments-o"></i>
       |                </p>
       |            </article>
       |        </div>
       |    </div>
       |    <hr>
       |    <div class="row">
       |        <div class="col-md-12">
       |
       |            <article>
       |                <h2 class="mt-0">
       |                    <div class="article-lead-image"><a href="/2023/09/30/20836-vivek-ramaswamy-politikertypus-alter-schule"><img class="img-responsive" src="/media/assets/article/2023/09/shutterstock_2351694243.jpg.1280x720_q75_box-0%2C320%2C6000%2C3695_crop_detail.jpg"></a></div>
       |                    <a href="/2023/09/30/20836-vivek-ramaswamy-politikertypus-alter-schule" rel="bookmark">
       |                        <small>Vivek Ramaswamy<span class="hidden">:</span> </small>Politikertypus alter Schule <span class="small" title="Erschienen in ef 236"><i alt="Icon: Heft" class="fa fa-newspaper-o fa-smgr"></i></span></a></h2>
       |                <p class="lead"><a href="/2023/09/30/20836-vivek-ramaswamy-politikertypus-alter-schule">Bekommt der gesunde Menschenverstand im Westen eine neue Chance?</a></p>
       |                <p>Robert Grözinger: „Wie die Präsidentschaftswahl in den USA ausgehen wird, ist überhaupt noch nicht absehbar. Jedoch tut sich im Vorfeld dieser Wahl im Kandidatenfeld etwas, das für die Politik im 21. Jahrhundert wegweisend sein könnte.“</p>
       |
       |                <p class="afoot small mb-0">
       |                    <em class="author">von <a href="/autor/robert-groezinger">Robert Grözinger</a></em>
       |                    | 6 <i class="fa fa-thumbs-o-up"></i>
       |                </p>
       |            </article>
       |        </div>
       |    </div>
       |    <hr>
       |
       |    <div class="row">
       |        <div class="col-md-12">
       |
       |            <article class="issue row">
       |                <header class="col-sm-12 mb-3">
       |                    <h2><a href="/archiv/ef/236/inhalt.html"><small>ef 236</small><span>Junger libertärer Populismus</span></a></h2>
       |                </header>
       |                <div class="col-sm-3 col-sm-push-9 text-right hidden-xs">
       |                    <a class="cover" href="/archiv/ef/236/inhalt.html"><img class="img-responsive" src="/media/assets/cover/ef-236.jpg" alt="Cover: ef236"/></a>
       |
       |                </div>
       |                <header class="col-sm-9 col-sm-pull-3">
       |                    <ul>
       |                        <li><a href='/archiv/ef/236/inhalt.html'>Inhalt</a></li>
       |
       |
       |                        <li><a href="/2023/09/18/20820-ef-236-editorial">ef 236: Editorial</a></li>
       |
       |
       |
       |                        <li><a href="/2023/09/22/20825-make-love-not-law-warnung-vor-einer-rechtsstaatlichen-zeitbombe">Make love not law: Warnung vor einer rechtsstaatlichen Zeitbombe</a></li>
       |
       |
       |
       |
       |
       |                        <li><a href="/2023/09/23/20826-deutschlandbrief-wege-aus-der-blockade">DeutschlandBrief: Wege aus der Blockade</a></li>
       |
       |
       |
       |                        <li><a href="/2023/09/24/20827-im-visier-die-waffenkolumne-waffen-kampfmoral-und-wirtschaftskraft">Im Visier, die Waffenkolumne: Waffen, Kampfmoral und Wirtschaftskraft</a></li>
       |
       |
       |
       |
       |
       |                        <li><a href="/2023/09/25/20828-aufstieg-der-konfederacja-in-polen-libertaerer-populismus-als-erfolgsmodell">Aufstieg der Konfederacja in Polen: Libertärer Populismus als Erfolgsmodell?</a></li>
       |
       |
       |
       |                        <li><a href="/2023/09/29/20835-politischer-umbruch-polen--ein-land-zwei-staemme">Politischer Umbruch: Polen – ein Land, zwei Stämme</a></li>
       |
       |
       |
       |                        <li><a href="/2023/09/30/20836-vivek-ramaswamy-politikertypus-alter-schule">Vivek Ramaswamy: Politikertypus alter Schule</a></li>
       |
       |
       |
       |                        <li><a href="/2023/10/01/20837-javier-milei-rechter-populismus-als-erfolgsstrategie">Javier Milei: Rechter Populismus als Erfolgsstrategie</a></li>

       |                    </ul>
       |                </header>
       |            </article>
       |
       |        </div>
       |    </div>
       |    <hr>
       |    <section>
       |        <div class="row">
       |            <div class="col-md-4 mb-3">
       |
       |                <article>
       |                    <h2 class="mt-0">
       |                        <div class="article-lead-image"><a href="/2023/09/29/20835-politischer-umbruch-polen--ein-land-zwei-staemme"><img class="img-responsive" src="/media/assets/article/2023/09/shutterstock_2189401223.jpg.1280x720_q75_box-0%2C425%2C4540%2C2984_crop_detail.jpg"></a></div>
       |                        <a href="/2023/09/29/20835-politischer-umbruch-polen--ein-land-zwei-staemme" rel="bookmark">
       |                            <small>Politischer Umbruch<span class="hidden">:</span> </small>Polen – ein Land, zwei Stämme <span class="small" title="Erschienen in ef 236"><i alt="Icon: Heft" class="fa fa-newspaper-o fa-smgr"></i></span></a></h2>
       |                    <p class="lead"><a href="/2023/09/29/20835-politischer-umbruch-polen--ein-land-zwei-staemme">Die Jugend sucht eine Alternative</a></p>
       |
       |
       |                    <p class="afoot small mb-0">
       |                        <em class="author">von <a href="/autor/rafa-a-ziemkiewicz">Rafał A. Ziemkiewicz</a></em>
       |                        | 6 <i class="fa fa-thumbs-o-up"></i>| 1 <i class="fa fa-comments-o"></i>
       |                    </p>
       |                </article>
       |            </div>
       |            <div class="col-md-4 mb-3">
       |
       |                <article>
       |                    <div class="row">
       |                        <div class="article-main" >
       |                            <a href="/2023/09/27/20834-artikel-der-woche-radio-was-ist-agorismus-freiheit-in-der-schattenwirtschaft"><img class="img-responsive" src="/media/assets/article/2023/09/shutterstock_386834047.jpg.1280x720_q75_crop_detail.jpg" alt="Artikelbild"/></a>
       |                            <h2>
       |                                <a href="/2023/09/27/20834-artikel-der-woche-radio-was-ist-agorismus-freiheit-in-der-schattenwirtschaft" rel="bookmark">
       |                                    <small>Artikel der Woche (Radio)<span class="hidden">:</span> </small>Was ist Agorismus? Freiheit in der Schattenwirtschaft
       |                                </a>
       |                            </h2>
       |                            <p class="lead"><a href="/2023/09/27/20834-artikel-der-woche-radio-was-ist-agorismus-freiheit-in-der-schattenwirtschaft">Ein erfolgreicher Artikel von Michael Bubendorf</a></p>
       |
       |                            <p class="afoot small">
       |                                <em class="author">von <a href="/autor/robert-paul-sprecher">Robert Paul (Sprecher)</a></em>
       |                                | 3 <i class="fa fa-thumbs-o-up"></i>
       |                            </p>
       |                        </div>
       |
       |
       |                    </div>
       |                </article>
       |            </div>
       |            <div class="col-md-4 mb-3">
       |
       |                <article>
       |                    <div class="row">
       |                        <div class="article-main" >
       |                            <a href="/2023/09/25/20833-wochenstart-25092023-tv-markus-krall-macht-ernst-parteigruendung-schon-in-wenigen-wochen"><img class="img-responsive" src="/media/assets/article/2023/09/MB_EFK2020_660.jpg.1280x720_q75_box-0%2C320%2C5689%2C3520_crop_detail.jpg" alt="Artikelbild"/></a>
       |                            <h2>
       |                                <a href="/2023/09/25/20833-wochenstart-25092023-tv-markus-krall-macht-ernst-parteigruendung-schon-in-wenigen-wochen" rel="bookmark">
       |                                    <small>Wochenstart 25.09.2023 (TV)<span class="hidden">:</span> </small>Markus Krall macht Ernst: Parteigründung schon in wenigen Wochen?
       |                                </a>
       |                            </h2>
       |                            <p class="lead"><a href="/2023/09/25/20833-wochenstart-25092023-tv-markus-krall-macht-ernst-parteigruendung-schon-in-wenigen-wochen">Medienschau am Montag</a></p>
       |
       |                            <p class="afoot small">
       |                                <em class="author">von <a href="/autor/martin-moczarski">Martin Moczarski</a></em>
       |                                | 17 <i class="fa fa-thumbs-o-up"></i>| 11 <i class="fa fa-comments-o"></i>
       |                            </p>
       |                        </div>
       |
       |
       |                    </div>
       |                </article>
       |            </div>
       |        </div>
       |        <div class="row">
       |            <div class="col-md-4 mb-3">
       |
       |                <article>
       |                    <h2 class="mt-0">
       |                        <div class="article-lead-image"><a href="/2023/09/25/20828-aufstieg-der-konfederacja-in-polen-libertaerer-populismus-als-erfolgsmodell"><img class="img-responsive" src="/media/assets/article/2023/09/shutterstock_704626399.jpg.1280x720_q75_box-0%2C213%2C4000%2C2464_crop_detail.jpg"></a></div>
       |                        <a href="/2023/09/25/20828-aufstieg-der-konfederacja-in-polen-libertaerer-populismus-als-erfolgsmodell" rel="bookmark">
       |                            <small>Aufstieg der Konfederacja in Polen<span class="hidden">:</span> </small>Libertärer Populismus als Erfolgsmodell? <span class="small" title="Erschienen in ef 236"><i alt="Icon: Heft" class="fa fa-newspaper-o fa-smgr"></i></span></a></h2>
       |                    <p class="lead"><a href="/2023/09/25/20828-aufstieg-der-konfederacja-in-polen-libertaerer-populismus-als-erfolgsmodell">Im Osten was Neues mit viel Bier, im Westen die alte deutsche Trostlosigkeit</a></p>
       |
       |
       |                    <p class="afoot small mb-0">
       |                        <em class="author">von <a href="/autor/andre-f-lichtschlag">André F. Lichtschlag</a></em>
       |                        | 13 <i class="fa fa-thumbs-o-up"></i>
       |                    </p>
       |                </article>
       |            </div>
       |            <div class="col-md-4 mb-3">
       |
       |                <article>
       |                    <h2 class="mt-0">
       |                        <div class="article-lead-image"><a href="/2023/09/24/20827-im-visier-die-waffenkolumne-waffen-kampfmoral-und-wirtschaftskraft"><img class="img-responsive" src="/media/assets/article/2023/09/unbasiert_A_nuclear_explosion.png.1280x720_q75_box-4%2C0%2C1070%2C600_crop_detail.jpg"></a></div>
       |                        <a href="/2023/09/24/20827-im-visier-die-waffenkolumne-waffen-kampfmoral-und-wirtschaftskraft" rel="bookmark">
       |                            <small>Im Visier, die Waffenkolumne<span class="hidden">:</span> </small>Waffen, Kampfmoral und Wirtschaftskraft <span class="small" title="Erschienen in ef 236"><i alt="Icon: Heft" class="fa fa-newspaper-o fa-smgr"></i></span></a></h2>
       |                    <p class="lead"><a href="/2023/09/24/20827-im-visier-die-waffenkolumne-waffen-kampfmoral-und-wirtschaftskraft">Die Grenzen technischer Überlegenheit</a></p>
       |
       |
       |                    <p class="afoot small mb-0">
       |                        <em class="author">von <a href="/autor/andreas-toegel">Andreas Tögel</a></em>
       |                        | 9 <i class="fa fa-thumbs-o-up"></i>| 4 <i class="fa fa-comments-o"></i>
       |                    </p>
       |                </article>
       |            </div>
       |            <div class="col-md-4 mb-3">
       |
       |                <article>
       |                    <h2 class="mt-0">
       |                        <div class="article-lead-image"><a href="/2023/09/23/20826-deutschlandbrief-wege-aus-der-blockade"><img class="img-responsive" src="/media/assets/article/2023/09/unbasiert_A_wall_of_fire_brought_down.png.1280x720_q75_box-4%2C0%2C1070%2C600_crop_detail.jpg"></a></div>
       |                        <a href="/2023/09/23/20826-deutschlandbrief-wege-aus-der-blockade" rel="bookmark">
       |                            <small>DeutschlandBrief<span class="hidden">:</span> </small>Wege aus der Blockade <span class="small" title="Erschienen in ef 236"><i alt="Icon: Heft" class="fa fa-newspaper-o fa-smgr"></i></span></a></h2>
       |                    <p class="lead"><a href="/2023/09/23/20826-deutschlandbrief-wege-aus-der-blockade">Wolfgang Reitzle spricht Klartext</a></p>
       |
       |
       |                    <p class="afoot small mb-0">
       |                        <em class="author">von <a href="/autor/bruno-bandulet">Bruno Bandulet</a></em>
       |                        | 14 <i class="fa fa-thumbs-o-up"></i>| 1 <i class="fa fa-comments-o"></i>
       |                    </p>
       |                </article>
       |            </div>
       |        </div>
       |    </section>
       |    <section class="category">
       |        <h1><a href="https://freiheitsfunken.info/">Unser Partnerportal: Freiheitsfunken</a></h1>
       |        <div class="flex-grid-container bg-gold">
       |
       |            <div class="flex-grid-item-1 flex-grid-item-md-2 bg-body">
       |                <article>
       |                    <a class="artlead" href="https://freiheitsfunken.info/2023/10/01/21096-sendung-a-sozial-folge-240-tv-platon-und-die-freiheit--teil-1">
       |                        <div class="artimg" style="background-image: url(https://freiheitsfunken.info/media/assets/article/2023/09/shutterstock_2228268935.jpg.727x485_q75_box-307%2C0%2C3550%2C2160_crop_detail_upscale.jpg)"></div>
       |                        <div class="autmask"></div>
       |                        <div class="autimg" style="background-image: url(https://freiheitsfunken.info/media/assets/authors/Tamm-Blau_550oW35.jpg.388x485_q75_box-286%2C0%2C863%2C719_crop_detail_upscale.jpg)"></div>
       |                    </a>
       |                    <div class="ff-syndication-body">
       |                        <h2>
       |                            <a href="https://freiheitsfunken.info/2023/10/01/21096-sendung-a-sozial-folge-240-tv-platon-und-die-freiheit--teil-1" rel="bookmark">
       |                                <small>Sendung „A-Sozial“ Folge 240 (TV)<span class="hidden">:</span> </small>Platon und die Freiheit – Teil 1
       |                            </a>
       |                        </h2>
       |                        <p class="lead"><a href="https://freiheitsfunken.info/2023/10/01/21096-sendung-a-sozial-folge-240-tv-platon-und-die-freiheit--teil-1">Der marktradikale Kommentar zur Politik und dem ganzen Rest</a></p>
       |                        <p>In der neuen Ausgabe der Sendung „A-Sozial“ startet heute die Serie „Platon und die Freiheit“ mit dem ersten Teil.</p>
       |
       |                        <p class="afoot small">
       |                            <em class="author">von Sascha Tamm</em>
       |                        </p>
       |
       |                    </div>
       |                </article>
       |            </div>
       |
       |            <div class="flex-grid-item-1 flex-grid-item-md-2 bg-body">
       |                <article>
       |                    <a class="artlead" href="https://freiheitsfunken.info/2023/10/01/21098-freiheitsespresso-vii-denken-wie-die-anderen">
       |                        <div class="artimg" style="background-image: url(https://freiheitsfunken.info/media/assets/article/2023/09/Freiheitsflamme.jpg.727x485_q75_box-0%2C315%2C3738%2C2806_crop_detail_upscale.jpg)"></div>
       |                        <div class="autmask"></div>
       |                        <div class="autimg" style="background-image: url(https://freiheitsfunken.info/media/assets/authors/Gold-Prollius.jpg.388x485_q75_box-570%2C19%2C1230%2C844_crop_detail_upscale.jpg)"></div>
       |                    </a>
       |                    <div class="ff-syndication-body">
       |                        <h2>
       |                            <a href="https://freiheitsfunken.info/2023/10/01/21098-freiheitsespresso-vii-denken-wie-die-anderen" rel="bookmark">
       |                                <small>Freiheitsespresso VII<span class="hidden">:</span> </small>Denken wie die anderen
       |                            </a>
       |                        </h2>
       |                        <p class="lead"><a href="https://freiheitsfunken.info/2023/10/01/21098-freiheitsespresso-vii-denken-wie-die-anderen">Wie erreichen Freiheitsfreunde andere Menschen?</a></p>
       |                        <p>Freiheitsfreunde tun sich schwer mit ihren Botschaften. Doch wir werden besser werden können und müssen, denn es stehen harte Zeiten an. Wir müssen unsere Einsichten auf ein höheres Niveau bringen.</p>
       |
       |                        <p class="afoot small">
       |                            <em class="author">von Michael von Prollius</em>
       |                        </p>
       |
       |                    </div>
       |                </article>
       |            </div>
       |
       |            <div class="flex-grid-item-1 flex-grid-item-md-2 bg-body">
       |                <article>
       |                    <a class="artlead" href="https://freiheitsfunken.info/2023/09/30/21097-homeschooling-in-den-usa-deutscher-familie-droht-die-abschiebung">
       |                        <div class="artimg" style="background-image: url(https://freiheitsfunken.info/media/assets/article/2023/09/Hausunterricht.jpg.727x485_q75_box-0%2C21%2C6439%2C4315_crop_detail_upscale.jpg)"></div>
       |                        <div class="autmask"></div>
       |                        <div class="autimg" style="background-image: url(https://freiheitsfunken.info/media/assets/authors/Gold-Brueckner.jpg.388x485_q75_box-294%2C0%2C871%2C720_crop_detail_upscale.jpg)"></div>
       |                    </a>
       |                    <div class="ff-syndication-body">
       |                        <h2>
       |                            <a href="https://freiheitsfunken.info/2023/09/30/21097-homeschooling-in-den-usa-deutscher-familie-droht-die-abschiebung" rel="bookmark">
       |                                <small>Homeschooling in den USA<span class="hidden">:</span> </small>Deutscher Familie droht die Abschiebung
       |                            </a>
       |                        </h2>
       |                        <p class="lead"><a href="https://freiheitsfunken.info/2023/09/30/21097-homeschooling-in-den-usa-deutscher-familie-droht-die-abschiebung">Flucht vor dem deutschen Staat</a></p>
       |                        <p>2008 floh die Familie Romeike vor den deutschen Behörden in die USA mit dem Ziel, dort ihre Kinder zu Hause zu unterrichten. Doch nun sollen die Romeikes die USA verlassen. Dabei sind zwei ihrer Kinder US-Staatsbürger.</p>
       |
       |                        <p class="afoot small">
       |                            <em class="author">von Thorsten Brückner</em>
       |                        </p>
       |
       |                    </div>
       |                </article>
       |            </div>
       |
       |            <div class="flex-grid-item-1 flex-grid-item-md-2 bg-body">
       |                <article>
       |                    <a class="artlead" href="https://freiheitsfunken.info/2023/09/30/21087-sendung-auf-den-punkt-folge-165-tv-sahra-wagenknecht-und-markus-krall-gruenden-neue-parteien-was-steckt-dahinter">
       |                        <div class="artimg" style="background-image: url(https://freiheitsfunken.info/media/assets/article/2023/09/wagenknecht_krall.png.727x485_q75_box-154%2C0%2C1775%2C1080_crop_detail_upscale.png)"></div>
       |                        <div class="autmask"></div>
       |                        <div class="autimg" style="background-image: url(https://freiheitsfunken.info/media/assets/authors/Blau-Kuhnle_QPaxCPE.jpg.388x485_q75_box-294%2C0%2C871%2C720_crop_detail_upscale.jpg)"></div>
       |                    </a>
       |                    <div class="ff-syndication-body">
       |                        <h2>
       |                            <a href="https://freiheitsfunken.info/2023/09/30/21087-sendung-auf-den-punkt-folge-165-tv-sahra-wagenknecht-und-markus-krall-gruenden-neue-parteien-was-steckt-dahinter" rel="bookmark">
       |                                <small>Sendung „Auf den Punkt“ Folge 165 (TV)<span class="hidden">:</span> </small>Sahra Wagenknecht und Markus Krall gründen neue Parteien: Was steckt dahinter?
       |                            </a>
       |                        </h2>
       |                        <p class="lead"><a href="https://freiheitsfunken.info/2023/09/30/21087-sendung-auf-den-punkt-folge-165-tv-sahra-wagenknecht-und-markus-krall-gruenden-neue-parteien-was-steckt-dahinter">Die Videokolumne von Joachim Kuhnle</a></p>
       |                        <p>In der aktuellen Sendung „Auf den Punkt“ geht es um Sahra Wagenknecht und Dr. Markus Krall. Beide haben neue Partei-Gründungen angekündigt. Was steckt dahinter? Werden sie Erfolg haben?</p>
       |
       |                        <p class="afoot small">
       |                            <em class="author">von Joachim Kuhnle</em>
       |                        </p>
       |
       |                    </div>
       |                </article>
       |            </div>
       |
       |        </div>
       |    </section>
       |    <hr>
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
       |                        <a class="article-image left" href="/2023/09/22/20825-make-love-not-law-warnung-vor-einer-rechtsstaatlichen-zeitbombe">
       |                            <img class="img-responsive" src="/media/assets/article/2023/09/unbasiert_Several_politicians_making.png.640x640_q75_box-236%2C0%2C835%2C600_crop_detail.jpg" alt="Artikelbild"/>
       |                        </a>
       |
       |                        <div class="article-main img1" >
       |
       |                            <h2>
       |                                <a href="/2023/09/22/20825-make-love-not-law-warnung-vor-einer-rechtsstaatlichen-zeitbombe" rel="bookmark">
       |                                    <small>Make love not law<span class="hidden">:</span> </small>Warnung vor einer rechtsstaatlichen Zeitbombe  <span class="small" title="Erschienen in ef 236"><i alt="Icon: Heft" class="fa fa-newspaper-o fa-smgr"></i></span>
       |                                </a>
       |                            </h2>
       |
       |
       |                            <p class="lead"><a href="/2023/09/22/20825-make-love-not-law-warnung-vor-einer-rechtsstaatlichen-zeitbombe">Oliver Lepsius dekonstruiert juristischen Unverstand</a></p>
       |
       |
       |
       |                            <p class="afoot small">
       |                                <em class="author">von <a href="/autor/carlos-a-gebauer">Carlos A. Gebauer</a></em>
       |                                | 20 <i class="fa fa-thumbs-o-up"></i>| 7 <i class="fa fa-comments-o"></i>
       |                            </p>
       |
       |
       |
       |                        </div>
       |
       |
       |                    </div>
       |                </article>
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
