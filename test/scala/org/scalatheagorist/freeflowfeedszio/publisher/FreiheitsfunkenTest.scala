package org.scalatheagorist.freeflowfeedszio.publisher

import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.scraper.ContentExtractors.elementList

object FreiheitsfunkenTest extends App {

  val browser = JsoupBrowser()

  (browser.parseString(htmlResponse) >> elementList("article")).foreach { elem =>
    val article = elem >> element("article")
    lazy val link = article >?> element("a") >> attr("href")
    lazy val title = (article >?> elements("div") >?> elements("h2") >?> text("a")).flatten.flatten
    lazy val author = (article >?> elements("div") >?> elements("p") >?> text("em"))
      .flatten
      .flatten
      .getOrElse("Freiheitsfunken")
      .split("von ")
      .last


    println(link.mkString)
    println(title.mkString)
    println(author.mkString)
    println()
  }

  def htmlResponse: String = {
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
       |                <a class="logo" href="/"><img src="/static/img/logo.png" alt="FreiheitsFunken&quot;/"></a>
       |                <div class="nav-top hidden-print">
       |                    <ul class="nav">
       |                        <li><a href="/warum-ff/">Freiheitsfunken</a></li>
       |                        <li><a href="/werkzeuge-spenden/">Werkzeuge spenden</a></li>
       |                        <li><a href="/werkstatt-ausbauen/">Werkstatt ausbauen</a></li>
       |                        <li><a href="/autoren/">Autoren</a></li>
       |                        <li><a href="/adverts/">Marktplatz</a></li>
       |                        <li><a href="/impressum/">Impressum</a></li>
       |                        <li class="social hidden-xs hidden-sm">
       |                            <a href="https://www.facebook.com/freiheitsfunken.info/" title="Freiheitsfunken bei Facebook" target="_blank">
       |                                <svg width="38.7" height="38.7" version="1.1" viewBox="0 0 38.7 38.7" xmlns="http://www.w3.org/2000/svg">
       | <path d="m31.444 0q2.9982 0 5.1272 2.129 2.129 2.129 2.129 5.1272v24.187q0 2.9982-2.129 5.1272-2.129 2.129-5.1272 2.129h-4.7367v-14.991h5.0139l0.75586-5.8453h-5.7697v-3.7289q0-1.4109 0.59209-2.1164 0.59209-0.70547 2.3054-0.70547l3.0738-0.0252v-5.2154q-1.5873-0.22676-4.4848-0.22676-3.4266 0-5.48 2.0156-2.0534 2.0156-2.0534 5.6941v4.3084h-5.0391v5.8453h5.0391v14.991h-13.404q-2.9982 0-5.1272-2.129-2.129-2.129-2.129-5.1272v-24.187q0-2.9982 2.129-5.1272 2.129-2.129 5.1272-2.129z" stroke-width=".025195"></path>
       |</svg>
       |
       |                            </a>
       |                        </li>
       |                        <li class="social hidden-xs hidden-sm">
       |                            <a href="https://twitter.com/freiheitsfunke" title="Freiheitsfunken auf Twitter" target="_blank">
       |                                <svg width="38.7" height="38.7" version="1.1" viewBox="0 0 38.7 38.7" xmlns="http://www.w3.org/2000/svg">
       | <path d="m38.699 7.3669q-1.6452 2.4064-3.978 4.1008 0.02456 0.34378 0.02456 1.0313 0 3.1922-0.93311 6.3721t-2.8362 6.102q-1.903 2.9221-4.5305 5.1689-2.6274 2.2468-6.3353 3.5851-3.7079 1.3383-7.9314 1.3383-6.6545 0-12.18-3.5605 0.85944 0.09822 1.9153 0.09822 5.525 0 9.8467-3.3887-2.5783-0.04911-4.6164-1.5838t-2.7993-3.9166q0.81033 0.12278 1.4979 0.12278 1.0559 0 2.0872-0.27011-2.7502-0.56478-4.555-2.7379-1.8048-2.1732-1.8048-5.0461v-0.09822q1.6698 0.93311 3.5851 1.0068-1.6207-1.0804-2.5783-2.8239-0.95766-1.7434-0.95766-3.7815 0-2.1609 1.0804-4.0025 2.9712 3.6588 7.2316 5.8565 4.2604 2.1977 9.1224 2.4433-0.19644-0.93311-0.19644-1.8171 0-3.2904 2.3205-5.6109t5.6109-2.3205q3.4378 0 5.7951 2.5047 2.6765-0.51566 5.0339-1.9153-0.90855 2.8239-3.4869 4.3709 2.2837-0.24555 4.5673-1.2278z" stroke-width=".024555"></path>
       |</svg>
       |
       |                            </a>
       |                        </li>
       |                        <li class="social hidden-xs hidden-sm">
       |                            <a href="https://www.instagram.com/freiheitsfunken.info/" title="Freiheitsfunken bei Instagram" target="_blank">
       |                                <svg width="38.7" height="38.7" version="1.1" viewBox="0 0 38.7 38.7" xmlns="http://www.w3.org/2000/svg">
       | <path d="m25.8 19.35q0-2.6707-1.8896-4.5604-1.8896-1.8896-4.5604-1.8896t-4.5604 1.8896q-1.8896 1.8896-1.8896 4.5604t1.8896 4.5604q1.8896 1.8896 4.5604 1.8896t4.5604-1.8896q1.8896-1.8896 1.8896-4.5604zm3.477 0q0 4.132-2.8975 7.0295t-7.0295 2.8975-7.0295-2.8975q-2.8975-2.8975-2.8975-7.0295t2.8975-7.0295q2.8975-2.8975 7.0295-2.8975t7.0295 2.8975q2.8975 2.8975 2.8975 7.0295zm2.7211-10.33q0 0.95742-0.68027 1.6377-0.68027 0.68027-1.6377 0.68027t-1.6377-0.68027q-0.68027-0.68027-0.68027-1.6377t0.68027-1.6377q0.68027-0.68027 1.6377-0.68027t1.6377 0.68027q0.68027 0.68027 0.68027 1.6377zm-12.648-5.543q-0.17637 0-1.9274-0.012598-1.7511-0.012598-2.6581 0t-2.4313 0.075586q-1.5243 0.062989-2.5951 0.25195-1.0708 0.18896-1.8015 0.46611-1.2598 0.50391-2.2172 1.4613-0.95742 0.95742-1.4613 2.2172-0.27715 0.73066-0.46611 1.8015-0.18896 1.0708-0.25195 2.5951-0.062988 1.5243-0.075586 2.4313t0 2.6581q0.012598 1.7511 0.012598 1.9274t-0.012598 1.9274q-0.012598 1.7511 0 2.6581t0.075586 2.4313q0.062988 1.5243 0.25195 2.5951 0.18896 1.0708 0.46611 1.8015 0.50391 1.2598 1.4613 2.2172 0.95742 0.95742 2.2172 1.4613 0.73066 0.27715 1.8015 0.46611 1.0708 0.18896 2.5951 0.25195 1.5243 0.06299 2.4313 0.07559t2.6581 0q1.7511-0.0126 1.9274-0.0126t1.9274 0.0126q1.7511 0.0126 2.6581 0t2.4313-0.07559q1.5243-0.06299 2.5951-0.25195t1.8015-0.46611q1.2598-0.50391 2.2172-1.4613t1.4613-2.2172q0.27715-0.73066 0.46611-1.8015 0.18896-1.0708 0.25195-2.5951 0.06299-1.5243 0.07559-2.4313t0-2.6581q-0.0126-1.7511-0.0126-1.9274t0.0126-1.9274q0.0126-1.7511 0-2.6581t-0.07559-2.4313q-0.062989-1.5243-0.25195-2.5951-0.18896-1.0708-0.46611-1.8015-0.50391-1.2598-1.4613-2.2172-0.95742-0.95742-2.2172-1.4613-0.73066-0.27715-1.8015-0.46611-1.0708-0.18896-2.5951-0.25195-1.5243-0.062988-2.4313-0.075586t-2.6581 0q-1.7511 0.012598-1.9274 0.012598zm19.35 15.873q0 5.7697-0.12598 7.9869-0.25195 5.2406-3.1242 8.1129-2.8723 2.8723-8.1129 3.1242-2.2172 0.12598-7.9869 0.12598t-7.9869-0.12598q-5.2406-0.25195-8.1129-3.1242-2.8723-2.8723-3.1242-8.1129-0.12598-2.2172-0.12598-7.9869t0.12598-7.9869q0.25195-5.2406 3.1242-8.1129 2.8723-2.8723 8.1129-3.1242 2.2172-0.12598 7.9869-0.12598t7.9869 0.12598q5.2406 0.25195 8.1129 3.1242 2.8723 2.8723 3.1242 8.1129 0.12598 2.2172 0.12598 7.9869z" stroke-width=".025195"></path>
       |</svg>
       |
       |                            </a>
       |                        </li>
       |                        <li class="social hidden-xs hidden-sm">
       |                            <a href="https://t.me/freiheitsfunken" title="Freiheitsfunken bei Telegram" target="_blank">
       |                                <svg width="38.7" height="38.7" version="1.1" viewBox="0 0 38.7 38.7" xmlns="http://www.w3.org/2000/svg">
       | <path d="m25.678 28.226 3.1746-14.966q0.19436-0.95022-0.22676-1.3605-0.42112-0.41032-1.1122-0.15117l-18.659 7.1915q-0.62628 0.23756-0.85304 0.5399t-0.05399 0.57229q0.17277 0.26995 0.69107 0.42112l4.7727 1.4901 11.079-6.9755q0.45352-0.30234 0.69107-0.12958 0.15117 0.10798-0.08638 0.32394l-8.9623 8.0985-0.34554 4.9239q0.49671 0 0.97182-0.47511l2.3324-2.246 4.8375 3.5633q1.3821 0.77746 1.7493-0.82065zm13.022-8.8759q0 3.9305-1.5333 7.5154-1.5333 3.5849-4.1248 6.1765t-6.1765 4.1248q-3.5849 1.5333-7.5154 1.5333t-7.5154-1.5333q-3.5849-1.5333-6.1765-4.1248-2.5915-2.5915-4.1248-6.1765-1.5333-3.5849-1.5333-7.5154t1.5333-7.5154q1.5333-3.5849 4.1248-6.1765 2.5915-2.5915 6.1765-4.1248 3.5849-1.5333 7.5154-1.5333t7.5154 1.5333q3.5849 1.5333 6.1765 4.1248 2.5915 2.5915 4.1248 6.1765 1.5333 3.5849 1.5333 7.5154z" stroke-width=".021596"></path>
       |</svg>
       |
       |                            </a>
       |                        </li>
       |                        <li class="social hidden-xs hidden-sm">
       |                            <a href="https://gettr.com/user/freiheitsfunken1" title="Freiheitsfunken bei Gettr" target="_blank">
       |                                <svg width="38.7" height="38.7" version="1.1" viewBox="0 0 38.7 38.7" xmlns="http://www.w3.org/2000/svg">
       |    <path d="M 2.4042969 1.3496094 C 1.8202377 1.3496094 1.3496094 1.8202377 1.3496094 2.4042969 L 1.3496094 36.294922 C 1.3496094 36.878981 1.8202377 37.349609 2.4042969 37.349609 L 36.294922 37.349609 C 36.878981 37.349609 37.349609 36.878981 37.349609 36.294922 L 37.349609 2.4042969 C 37.349609 1.8202377 36.878981 1.3496094 36.294922 1.3496094 L 2.4042969 1.3496094 z M 19.419922 2.5742188 C 19.536721 2.7711928 19.668234 2.9613423 19.769531 3.1660156 C 20.150391 3.9354754 20.364163 4.7547318 20.451172 5.6054688 C 20.501962 6.102128 20.518291 6.6030317 20.550781 7.1015625 C 20.551614 7.1111625 20.562144 7.1187207 20.583984 7.1503906 C 20.835082 6.8879536 20.949664 6.5612742 21.050781 6.2363281 C 21.15178 5.9119214 21.213984 5.5761548 21.298828 5.2207031 C 21.339908 5.2619051 21.387764 5.2967341 21.417969 5.34375 C 21.774277 5.8986321 21.984763 6.5113197 22.138672 7.1484375 C 22.296499 7.8019506 22.352076 8.4671882 22.417969 9.1328125 C 22.419669 9.1493985 22.427949 9.1656779 22.443359 9.2167969 C 22.494859 9.1619069 22.542293 9.1271819 22.570312 9.0800781 C 22.794579 8.7029026 22.895673 8.287454 22.941406 7.8554688 C 22.946306 7.8094828 22.952044 7.762504 22.958984 7.7167969 C 22.960184 7.7071969 22.972498 7.6995072 22.992188 7.6757812 C 23.022317 7.7028242 23.054626 7.721158 23.072266 7.75 C 23.512756 8.4726391 23.861405 9.2370854 24.105469 10.048828 C 24.304119 10.709458 24.429975 11.381424 24.451172 12.070312 C 24.483382 13.118357 24.185343 14.078024 23.636719 14.964844 C 23.112125 15.812834 22.430085 16.528008 21.736328 17.234375 C 21.182141 17.79862 20.616741 18.353859 20.085938 18.939453 C 19.830888 19.220868 19.644688 19.563158 19.427734 19.878906 C 19.405264 19.911786 19.387416 19.949068 19.353516 20.007812 C 19.293386 19.962163 19.2361 19.928347 19.191406 19.882812 C 18.278209 18.952421 17.389836 18.001279 16.5625 16.992188 C 16.037983 16.352491 15.546018 15.690079 15.117188 14.982422 C 14.808333 14.472736 14.562074 13.93273 14.394531 13.363281 C 14.191127 12.671939 14.215287 11.974466 14.421875 11.28125 C 14.692084 10.374498 15.189712 9.590905 15.744141 8.8417969 C 16.377088 7.9865242 17.040267 7.1536434 17.673828 6.2988281 C 18.222076 5.559083 18.705653 4.7778842 19.009766 3.9023438 C 19.131174 3.5528437 19.201596 3.1853684 19.294922 2.8261719 C 19.315102 2.748551 19.334416 2.6716104 19.353516 2.59375 C 19.376076 2.58712 19.397342 2.5808537 19.419922 2.5742188 z M 10.021484 21.384766 L 28.677734 21.384766 L 28.677734 22.222656 C 28.598084 22.238016 28.512434 22.260824 28.423828 22.271484 C 27.401489 22.395161 26.43286 22.688091 25.546875 23.21875 C 24.854935 23.633216 24.296055 24.187922 23.933594 24.916016 C 23.820584 25.142952 23.745846 25.390266 23.673828 25.634766 C 23.633168 25.772759 23.562996 25.822392 23.425781 25.822266 C 20.717247 25.818066 18.009275 25.818359 15.300781 25.818359 C 15.232831 25.818235 15.164807 25.806228 15.089844 25.798828 C 15.027674 25.607014 14.975566 25.42434 14.910156 25.246094 C 14.658785 24.560659 14.220571 24.01184 13.652344 23.5625 C 12.993698 23.041661 12.239669 22.71129 11.435547 22.5 C 11.032286 22.394043 10.615957 22.34254 10.205078 22.265625 C 10.148968 22.255155 10.093087 22.241962 10.021484 22.226562 L 10.021484 21.384766 z M 23.300781 26.521484 C 23.336491 26.521634 23.3723 26.524916 23.408203 26.533203 C 23.698237 26.600123 23.891706 26.921731 23.824219 27.216797 C 23.619389 28.112481 23.415768 29.008615 23.210938 29.904297 C 23.193008 29.982667 23.165682 30.058543 23.138672 30.146484 L 22.736328 30.173828 C 22.655318 30.629371 22.576945 31.079916 22.492188 31.556641 L 21.767578 31.556641 C 21.602383 33.094893 21.438713 34.606015 21.275391 36.126953 L 17.347656 36.126953 C 17.188764 34.617085 17.02996 33.10571 16.867188 31.558594 L 16.181641 31.558594 C 16.087821 31.071631 16.00058 30.623044 15.914062 30.173828 C 15.763813 30.161258 15.644083 30.151865 15.505859 30.140625 C 15.450899 29.920928 15.395774 29.707928 15.345703 29.494141 C 15.173473 28.757934 15.00378 28.021407 14.832031 27.285156 C 14.774751 27.039615 14.76956 26.80847 14.998047 26.638672 C 15.328992 26.392715 15.767222 26.585477 15.839844 27.009766 C 15.970225 27.771402 16.096212 28.533245 16.226562 29.294922 C 16.273372 29.568429 16.329775 29.841107 16.380859 30.111328 L 17.203125 30.111328 C 17.151635 29.719627 17.105071 29.351562 17.054688 28.984375 C 16.972478 28.384963 16.883141 27.785375 16.804688 27.185547 C 16.762208 26.860892 16.885804 26.651231 17.150391 26.560547 C 17.458672 26.454922 17.795136 26.675961 17.818359 27.003906 C 17.878419 27.851813 17.934292 28.699093 17.994141 29.546875 C 18.007061 29.73074 18.031071 29.913976 18.050781 30.113281 L 18.847656 30.113281 C 18.847656 29.770856 18.853003 29.457366 18.845703 29.144531 C 18.829463 28.451772 18.803889 27.759125 18.787109 27.066406 C 18.780809 26.806965 18.945633 26.608834 19.195312 26.552734 C 19.42657 26.500764 19.667377 26.61927 19.765625 26.847656 C 19.796245 26.918866 19.800778 27.006403 19.798828 27.085938 C 19.776968 27.976585 19.750322 28.865294 19.726562 29.755859 C 19.723563 29.869725 19.726562 29.984147 19.726562 30.111328 L 20.558594 30.111328 C 20.571684 29.955806 20.586493 29.81935 20.595703 29.681641 C 20.651323 28.851127 20.705345 28.019839 20.761719 27.189453 C 20.767619 27.102513 20.780805 27.015427 20.796875 26.929688 C 20.843145 26.682234 21.109494 26.49259 21.353516 26.529297 C 21.611238 26.568007 21.826914 26.82327 21.794922 27.072266 C 21.702142 27.793906 21.601706 28.513442 21.503906 29.234375 C 21.471816 29.47076 21.438253 29.708636 21.408203 29.945312 C 21.401203 30.000292 21.40625 30.05649 21.40625 30.125 L 22.197266 30.125 C 22.216996 30.0654 22.244209 30.002347 22.255859 29.9375 C 22.354339 29.38803 22.451842 28.839117 22.546875 28.289062 C 22.622085 27.853874 22.691857 27.417321 22.769531 26.982422 C 22.818137 26.710293 23.050816 26.520435 23.300781 26.521484 z "></path>
       |</svg>
       |
       |                            </a>
       |                        </li>
       |                    </ul>
       |                </div>
       |                <div class="btn-group hidden-print" style="position: absolute; right: 10px; top: 10px;">
       |
       |        <a class="btn btn-default dropdown-toggle" href="/accounts/login/" data-toggle="dropdown" aria-expanded="false">
       |            Anmelden <span class="caret"></span>
       |        </a>
       |        <ul class="dropdown-menu pull-right" role="menu">
       |            <li><a href="/accounts/login/?next=/">Anmelden</a></li>
       |            <li class="divider"></li>
       |            <li><a href="/accounts/password/reset/">Passwort zurücksetzen</a></li>
       |            <li><a href="/accounts/register/">Registrierung</a></li>
       |        </ul>
       |
       |</div>
       |            </header>
       |        </div>
       |    </div>
       |</div>
       |
       |    <main class="container">
       |
       |        <div class="row">
       |            <div class="col-md-12">
       |
       |<article>
       |    <a class="artlead" href="/2023/10/24/21172-sendung-ferngespraech-folge-57-tv-luis-pazos-und-andreas-tank-wir-stellen-uns-persoenlich-vor">
       |        <div class="artimg" style="background-image: url(/media/assets/article/2023/10/ZOOM0002_Tr3.00_14_06_17.Standbild040.bmp.727x485_q75_box-403%2C147%2C1690%2C1004_crop_detail.jpg)"></div>
       |        <div class="autmask"></div>
       |
       |        <div class="autimg" style="background-image: url(/media/assets/authors/Blau-Ferngespr%C3%A4che.png.388x485_q75_box-0%2C0%2C2400%2C3000_crop_detail.png)"></div>
       |
       |    </a>
       |    <div class="">
       |
       |            <h2>
       |                <a href="/2023/10/24/21172-sendung-ferngespraech-folge-57-tv-luis-pazos-und-andreas-tank-wir-stellen-uns-persoenlich-vor" rel="bookmark">
       |                    <small>Sendung „Ferngespräch“ Folge 57 (TV)<span class="hidden">:</span> </small>Luis Pazos und Andreas Tank: Wir stellen uns persönlich vor!
       |
       |                </a>
       |            </h2>
       |
       |
       |            <p class="lead"><a href="/2023/10/24/21172-sendung-ferngespraech-folge-57-tv-luis-pazos-und-andreas-tank-wir-stellen-uns-persoenlich-vor">Mit Andreas Tank</a></p>
       |            <p>In der aktuellen Ausgabe "Ferngespräch" sprechen Luis Pazos und Andreas Tank über ihren Weg zu den Freiheitsfunken.</p>
       |
       |
       |            <p class="afoot small">
       |                <em class="author">von
       |
       |                    Ferngespräch
       |                    </em>
       |                | 1 <i class="fa fa-thumbs-o-up"></i>
       |            </p>
       |
       |    </div>
       |</article>
       |                <hr class="mt-0">
       |
       |<article>
       |    <a class="artlead" href="/2023/10/23/21186-islam-und-er-gehoert-immer-noch-nicht-zu-deutschland">
       |        <div class="artimg" style="background-image: url(/media/assets/article/2023/10/45._Kolumne_Islam_in_Deutschland.webp.727x485_q75_box-0%2C25%2C1080%2C746_crop_detail.jpg)"></div>
       |        <div class="autmask"></div>
       |
       |        <div class="autimg" style="background-image: url(/media/assets/authors/Gold-Mende.jpg.388x485_q75_box-294%2C0%2C871%2C720_crop_detail.jpg)"></div>
       |
       |    </a>
       |    <div class="">
       |
       |            <h2>
       |                <a href="/2023/10/23/21186-islam-und-er-gehoert-immer-noch-nicht-zu-deutschland" rel="bookmark">
       |                    <small>Islam<span class="hidden">:</span> </small>…und er gehört immer noch nicht zu Deutschland
       |
       |                </a>
       |            </h2>
       |
       |
       |            <p class="lead"><a href="/2023/10/23/21186-islam-und-er-gehoert-immer-noch-nicht-zu-deutschland">Wenn man die Auswirkungen dessen erlebt, was „Schwurbler“ jahrelang vorhersagten</a></p>
       |            <p>Ich und viele andere haben nichts „zugelassen“, sondern wurden und werden aufgrund des in sich völlig widersprüchlichen Systems der „alternativlosen“ Pöbelherrschaft dazu gezwungen, Ereignisse zu beobachten, zu erdulden und nicht zuletzt zu finanzieren, die in ihrer Perversion, Destruktivität und Sinnlosigkeit atemberaubend waren und sind.</p>
       |
       |
       |            <p class="afoot small">
       |                <em class="author">von
       |
       |                    Philipp A. Mende
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
       |        <section id="online-kolumnen" class="category">
       |            <h1 class="category-header">Unsere Kolumnisten</h1>
       |
       |<div class="ef-carousel bg-gold mb-5 pt-5">
       |    <a href="javascript:void(0)" class="left disabled"><i class="fa fa-2x fa-chevron-left"></i></a>
       |    <div class="flex-grid-container">
       |
       |            <div class="flex-grid-item-2 flex-grid-item-md-5 ef-carousel-item">
       |                <a class="kolumne" href="/autor/oliver-gorus">
       |                    <div class="square-thumb">
       |                        <img alt="" src="/media/assets/authors/Gold-Gorus.jpg.640x640_q75_box-111%2C0%2C832%2C720_crop_detail.jpg">
       |                    </div>
       |                    <h3>Oliver Gorus
       |                         <br><small>mittwochs um 6 Uhr</small>
       |                    </h3>
       |                </a>
       |            </div>
       |
       |            <div class="flex-grid-item-2 flex-grid-item-md-5 ef-carousel-item">
       |                <a class="kolumne" href="/autor/joachim-kuhnle">
       |                    <div class="square-thumb">
       |                        <img alt="" src="/media/assets/authors/Blau-Kuhnle_QPaxCPE.jpg.640x640_q75_box-282%2C0%2C1003%2C720_crop_detail.jpg">
       |                    </div>
       |                    <h3>Joachim Kuhnle
       |                         <br><small>samstags um 16 Uhr</small>
       |                    </h3>
       |                </a>
       |            </div>
       |
       |            <div class="flex-grid-item-2 flex-grid-item-md-5 ef-carousel-item hidden-smx">
       |                <a class="kolumne" href="/autor/robert-groezinger">
       |                    <div class="square-thumb">
       |                        <img alt="" src="/media/assets/authors/Gold-Groezinger_OHCIjB8.jpg.640x640_q75_box-388%2C0%2C1109%2C720_crop_detail.jpg">
       |                    </div>
       |                    <h3>Robert Grözinger
       |                         <br><small>montags um 16 Uhr</small>
       |                    </h3>
       |                </a>
       |            </div>
       |
       |            <div class="flex-grid-item-2 flex-grid-item-md-5 ef-carousel-item hidden-smx">
       |                <a class="kolumne" href="/autor/stephan-unruh">
       |                    <div class="square-thumb">
       |                        <img alt="" src="/media/assets/authors/Gold-Unruh.jpg.640x640_q75_box-282%2C0%2C1003%2C720_crop_detail.jpg">
       |                    </div>
       |                    <h3>Stephan Unruh
       |                         <br><small>sonntags um 21 Uhr</small>
       |                    </h3>
       |                </a>
       |            </div>
       |
       |            <div class="flex-grid-item-2 flex-grid-item-md-5 ef-carousel-item hidden-smx">
       |                <a class="kolumne" href="/autor/carlos-a-gebauer">
       |                    <div class="square-thumb">
       |                        <img alt="" src="/media/assets/authors/Gold-Gebauer_xFlVPh3.jpg.640x640_q75_box-179%2C0%2C900%2C720_crop_detail.jpg">
       |                    </div>
       |                    <h3>Carlos A. Gebauer
       |                         <br><small>freitags um 10 Uhr</small>
       |                    </h3>
       |                </a>
       |            </div>
       |
       |            <div class="flex-grid-item-2 flex-grid-item-md-5 ef-carousel-item hidden-mdx hidden-smx">
       |                <a class="kolumne" href="/autor/markus-krall">
       |                    <div class="square-thumb">
       |                        <img alt="" src="/media/assets/authors/Gold-Krall.jpg.640x640_q75_box-397%2C0%2C938%2C540_crop_detail.jpg">
       |                    </div>
       |                    <h3>Markus Krall
       |                         <br><small>mittwochs um 16 Uhr</small>
       |                    </h3>
       |                </a>
       |            </div>
       |
       |            <div class="flex-grid-item-2 flex-grid-item-md-5 ef-carousel-item hidden-mdx hidden-smx">
       |                <a class="kolumne" href="/autor/michael-von-prollius">
       |                    <div class="square-thumb">
       |                        <img alt="" src="/media/assets/authors/Gold-Prollius.jpg.640x640_q75_box-557%2C32%2C1338%2C812_crop_detail.jpg">
       |                    </div>
       |                    <h3>Michael von Prollius
       |                         <br><small>sonntags um 6 Uhr</small>
       |                    </h3>
       |                </a>
       |            </div>
       |
       |            <div class="flex-grid-item-2 flex-grid-item-md-5 ef-carousel-item hidden-mdx hidden-smx">
       |                <a class="kolumne" href="/autor/sascha-koll">
       |                    <div class="square-thumb">
       |                        <img alt="" src="/media/assets/authors/Gold-Koll_d1cboNY.jpg.640x640_q75_box-277%2C0%2C998%2C720_crop_detail.jpg">
       |                    </div>
       |                    <h3>Sascha Koll
       |                         <br><small>donnerstags um 10 Uhr</small>
       |                    </h3>
       |                </a>
       |            </div>
       |
       |            <div class="flex-grid-item-2 flex-grid-item-md-5 ef-carousel-item hidden-mdx hidden-smx">
       |                <a class="kolumne" href="/autor/thorsten-brueckner">
       |                    <div class="square-thumb">
       |                        <img alt="" src="/media/assets/authors/Gold-Brueckner.jpg.640x640_q75_box-282%2C0%2C1003%2C720_crop_detail.jpg">
       |                    </div>
       |                    <h3>Thorsten Brückner
       |                         <br><small>samstags um 21 Uhr</small>
       |                    </h3>
       |                </a>
       |            </div>
       |
       |            <div class="flex-grid-item-2 flex-grid-item-md-5 ef-carousel-item hidden-mdx hidden-smx">
       |                <a class="kolumne" href="/autor/monika-hausammann">
       |                    <div class="square-thumb">
       |                        <img alt="" src="/media/assets/authors/Gold-Jordan.jpg.640x640_q75_box-209%2C0%2C930%2C720_crop_detail.jpg">
       |                    </div>
       |                    <h3>Monika Hausammann (Pausiert)
       |                         <br><small>pausiert</small>
       |                    </h3>
       |                </a>
       |            </div>
       |
       |            <div class="flex-grid-item-2 flex-grid-item-md-5 ef-carousel-item hidden-mdx hidden-smx">
       |                <a class="kolumne" href="/autor/reinhard-guenzel">
       |                    <div class="square-thumb">
       |                        <img alt="" src="/media/assets/authors/Gold-Guenzel.jpg.640x640_q75_box-559%2C0%2C1280%2C720_crop_detail.jpg">
       |                    </div>
       |                    <h3>Reinhard Günzel
       |                         <br><small>sonntags um 11 Uhr</small>
       |                    </h3>
       |                </a>
       |            </div>
       |
       |            <div class="flex-grid-item-2 flex-grid-item-md-5 ef-carousel-item hidden-mdx hidden-smx">
       |                <a class="kolumne" href="/autor/andreas-tiedtke">
       |                    <div class="square-thumb">
       |                        <img alt="" src="/media/assets/authors/Gold-Tiedtke.jpg.640x640_q75_box-452%2C0%2C1173%2C720_crop_detail.jpg">
       |                    </div>
       |                    <h3>Andreas Tiedtke
       |                         <br><small>dienstags um 21 Uhr</small>
       |                    </h3>
       |                </a>
       |            </div>
       |
       |            <div class="flex-grid-item-2 flex-grid-item-md-5 ef-carousel-item hidden-mdx hidden-smx">
       |                <a class="kolumne" href="/autor/dachthekenduett">
       |                    <div class="square-thumb">
       |                        <img alt="" src="/media/assets/authors/Dachthekenduett_cover_8CJlIzK.png.640x640_q75_box-100%2C333%2C767%2C1000_crop_detail.png">
       |                    </div>
       |                    <h3>Dachthekenduett
       |                         <br><small>donnerstags um 18 Uhr</small>
       |                    </h3>
       |                </a>
       |            </div>
       |
       |            <div class="flex-grid-item-2 flex-grid-item-md-5 ef-carousel-item hidden-mdx hidden-smx">
       |                <a class="kolumne" href="/autor/michael-werner">
       |                    <div class="square-thumb">
       |                        <img alt="" src="/media/assets/authors/Gold-Werner.jpg.640x640_q75_box-303%2C0%2C1024%2C720_crop_detail.jpg">
       |                    </div>
       |                    <h3>Michael Werner
       |                         <br><small>montags um 11 Uhr</small>
       |                    </h3>
       |                </a>
       |            </div>
       |
       |            <div class="flex-grid-item-2 flex-grid-item-md-5 ef-carousel-item hidden-mdx hidden-smx">
       |                <a class="kolumne" href="/autor/christian-paulwitz">
       |                    <div class="square-thumb">
       |                        <img alt="" src="/media/assets/authors/Gold-Paulwitz.jpg.640x640_q75_box-286%2C0%2C1007%2C720_crop_detail.jpg">
       |                    </div>
       |                    <h3>Christian Paulwitz
       |                         <br><small>dienstags um 16 Uhr</small>
       |                    </h3>
       |                </a>
       |            </div>
       |
       |            <div class="flex-grid-item-2 flex-grid-item-md-5 ef-carousel-item hidden-mdx hidden-smx">
       |                <a class="kolumne" href="/autor/karl-friedrich-israel">
       |                    <div class="square-thumb">
       |                        <img alt="" src="/media/assets/authors/Gold-Israel.jpg.640x640_q75_box-422%2C0%2C1504%2C1080_crop_detail.jpg">
       |                    </div>
       |                    <h3>Karl-Friedrich Israel
       |                         <br><small>samstags um 11 Uhr</small>
       |                    </h3>
       |                </a>
       |            </div>
       |
       |            <div class="flex-grid-item-2 flex-grid-item-md-5 ef-carousel-item hidden-mdx hidden-smx">
       |                <a class="kolumne" href="/autor/jung-brutal-marktradikal">
       |                    <div class="square-thumb">
       |                        <img alt="" src="/media/assets/authors/jbm3.png.640x640_q75_box-815%2C405%2C1500%2C1090_crop_detail.png">
       |                    </div>
       |                    <h3>Jung Brutal Marktradikal
       |                         <br><small>montags um 6 Uhr</small>
       |                    </h3>
       |                </a>
       |            </div>
       |
       |            <div class="flex-grid-item-2 flex-grid-item-md-5 ef-carousel-item hidden-mdx hidden-smx">
       |                <a class="kolumne" href="/autor/olivier-kessler">
       |                    <div class="square-thumb">
       |                        <img alt="" src="/media/assets/authors/kessler_gold.png.640x640_q75_box-87%2C10%2C777%2C699_crop_detail.png">
       |                    </div>
       |                    <h3>Olivier Kessler
       |                         <br><small>donnerstags um 6 Uhr</small>
       |                    </h3>
       |                </a>
       |            </div>
       |
       |            <div class="flex-grid-item-2 flex-grid-item-md-5 ef-carousel-item hidden-mdx hidden-smx">
       |                <a class="kolumne" href="/autor/manuel-maggio">
       |                    <div class="square-thumb">
       |                        <img alt="" src="/media/assets/authors/Gold-maggio3.jpg.640x640_q75_box-269%2C0%2C990%2C720_crop_detail.jpg">
       |                    </div>
       |                    <h3>Manuel Maggio
       |                         <br><small>samstags um 6 Uhr</small>
       |                    </h3>
       |                </a>
       |            </div>
       |
       |            <div class="flex-grid-item-2 flex-grid-item-md-5 ef-carousel-item hidden-mdx hidden-smx">
       |                <a class="kolumne" href="/autor/thomas-jahn">
       |                    <div class="square-thumb">
       |                        <img alt="" src="/media/assets/authors/Gold-Jahn_ZZFIZFU.png.640x640_q75_box-139%2C61%2C1094%2C1016_crop_detail.png">
       |                    </div>
       |                    <h3>Thomas Jahn
       |                         <br><small>freitags um 18 Uhr</small>
       |                    </h3>
       |                </a>
       |            </div>
       |
       |            <div class="flex-grid-item-2 flex-grid-item-md-5 ef-carousel-item hidden-mdx hidden-smx">
       |                <a class="kolumne" href="/autor/philipp-a-mende">
       |                    <div class="square-thumb">
       |                        <img alt="" src="/media/assets/authors/Gold-Mende.jpg.640x640_q75_box-269%2C0%2C990%2C720_crop_detail.jpg">
       |                    </div>
       |                    <h3>Philipp A. Mende
       |                         <br><small>montags um 21 Uhr</small>
       |                    </h3>
       |                </a>
       |            </div>
       |
       |            <div class="flex-grid-item-2 flex-grid-item-md-5 ef-carousel-item hidden-mdx hidden-smx">
       |                <a class="kolumne" href="/autor/martin-moczarski">
       |                    <div class="square-thumb">
       |                        <img alt="" src="/media/assets/authors/Blau-Moczarski_FAm7CD6.jpg.1280x720_q75_box-001280719_crop_detail.jpg.640x640_q75_box-128%2C0%2C849%2C719_crop_detail.jpg">
       |                    </div>
       |                    <h3>Martin Moczarski
       |                         <br><small>freitags um 14 Uhr</small>
       |                    </h3>
       |                </a>
       |            </div>
       |
       |            <div class="flex-grid-item-2 flex-grid-item-md-5 ef-carousel-item hidden-mdx hidden-smx">
       |                <a class="kolumne" href="/autor/david-andres">
       |                    <div class="square-thumb">
       |                        <img alt="" src="/media/assets/authors/KolAndres.jpg.640x640_q75_box-260%2C0%2C981%2C720_crop_detail.jpg">
       |                    </div>
       |                    <h3>David Andres
       |                         <br><small>dienstags um 11 Uhr</small>
       |                    </h3>
       |                </a>
       |            </div>
       |
       |            <div class="flex-grid-item-2 flex-grid-item-md-5 ef-carousel-item hidden-mdx hidden-smx">
       |                <a class="kolumne" href="/autor/andre-f-lichtschlag">
       |                    <div class="square-thumb">
       |                        <img alt="" src="/media/assets/authors/Gold-Lichtschlag.jpg.640x640_q75_box-333%2C0%2C1054%2C720_crop_detail.jpg">
       |                    </div>
       |                    <h3>André F. Lichtschlag
       |                         <br><small>donnerstags um 14 Uhr</small>
       |                    </h3>
       |                </a>
       |            </div>
       |
       |            <div class="flex-grid-item-2 flex-grid-item-md-5 ef-carousel-item hidden-mdx hidden-smx">
       |                <a class="kolumne" href="/autor/tyler-durden">
       |                    <div class="square-thumb">
       |                        <img alt="" src="/media/assets/authors/Gold-Durden.png.640x640_q75_box-324%2C16%2C1028%2C721_crop_detail.png">
       |                    </div>
       |                    <h3>Tyler Durden
       |                         <br><small>donnerstags um 22 Uhr</small>
       |                    </h3>
       |                </a>
       |            </div>
       |
       |            <div class="flex-grid-item-2 flex-grid-item-md-5 ef-carousel-item hidden-mdx hidden-smx">
       |                <a class="kolumne" href="/autor/sascha-tamm">
       |                    <div class="square-thumb">
       |                        <img alt="" src="/media/assets/authors/Tamm-Blau_550oW35.jpg.640x640_q75_box-269%2C0%2C990%2C719_crop_detail.jpg">
       |                    </div>
       |                    <h3>Sascha Tamm
       |                         <br><small>sonntags um 16 Uhr</small>
       |                    </h3>
       |                </a>
       |            </div>
       |
       |            <div class="flex-grid-item-2 flex-grid-item-md-5 ef-carousel-item hidden-mdx hidden-smx">
       |                <a class="kolumne" href="/autor/axel-bc-krauss">
       |                    <div class="square-thumb">
       |                        <img alt="" src="/media/assets/authors/Gold-Krauss_GnVF7E0.jpg.640x640_q75_box-354%2C0%2C1075%2C720_crop_detail.jpg">
       |                    </div>
       |                    <h3>Axel B.C. Krauss
       |                         <br><small>mittwochs um 11 Uhr</small>
       |                    </h3>
       |                </a>
       |            </div>
       |
       |            <div class="flex-grid-item-2 flex-grid-item-md-5 ef-carousel-item hidden-mdx hidden-smx">
       |                <a class="kolumne" href="/autor/robert-paul-sprecher">
       |                    <div class="square-thumb">
       |                        <img alt="" src="/media/assets/authors/Blau-Paul_E3fc5uv.png.640x640_q75_box-269%2C0%2C990%2C720_crop_detail.png">
       |                    </div>
       |                    <h3>Robert Paul (Sprecher)
       |                         <br><small>mittwochs um 21 Uhr</small>
       |                    </h3>
       |                </a>
       |            </div>
       |
       |            <div class="flex-grid-item-2 flex-grid-item-md-5 ef-carousel-item hidden-mdx hidden-smx">
       |                <a class="kolumne" href="/autor/benjamin-mudlack">
       |                    <div class="square-thumb">
       |                        <img alt="" src="/media/assets/authors/Gold-Mudlack.jpg.640x640_q75_box-303%2C0%2C1024%2C720_crop_detail.jpg">
       |                    </div>
       |                    <h3>Benjamin Mudlack
       |                         <br><small>freitags um 22 Uhr</small>
       |                    </h3>
       |                </a>
       |            </div>
       |
       |            <div class="flex-grid-item-2 flex-grid-item-md-5 ef-carousel-item hidden-mdx hidden-smx">
       |                <a class="kolumne" href="/autor/ferngespraech">
       |                    <div class="square-thumb">
       |                        <img alt="" src="/media/assets/authors/Blau-Ferngespr%C3%A4che.png.640x640_q75_box-0%2C0%2C3000%2C3000_crop_detail.png">
       |                    </div>
       |                    <h3>Ferngespräch
       |                         <br><small>dienstags um 6 Uhr</small>
       |                    </h3>
       |                </a>
       |            </div>
       |
       |            <div class="flex-grid-item-2 flex-grid-item-md-5 ef-carousel-item hidden-mdx hidden-smx">
       |                <a class="kolumne" href="/autor/stefan-blankertz">
       |                    <div class="square-thumb">
       |                        <img alt="" src="/media/assets/authors/Gold-Blankertz.jpg.640x640_q75_box-346%2C0%2C1067%2C720_crop_detail.jpg">
       |                    </div>
       |                    <h3>Stefan Blankertz
       |                         <br><small>freitags um 6 Uhr</small>
       |                    </h3>
       |                </a>
       |            </div>
       |
       |    </div>
       |    <a href="javascript:void(0)" class="right"><i class="fa fa-2x fa-chevron-right"></i></a>
       |</div>
       |        </section>
       |
       |        <h3 class="ahd">Anzeige</h3><div class="sbad"><div class="wrp"><ins class="adsbygoogle" style="display: block;" data-ad-client="ca-pub-5425042520745476" data-ad-slot="7209020886" data-ad-format="auto" data-full-width-responsive="true"><iframe id="aswift_0" style="height: 1px !important; max-height: 1px !important; max-width: 1px !important; width: 1px !important;"><iframe id="google_ads_frame0"></iframe></iframe></ins><script defer="">(adsbygoogle = window.adsbygoogle || []).push({});</script></div></div>
       |        <hr class="mt-3">
       |
       |
       |        <div class="row">
       |            <div class="col-md-4">
       |
       |<article>
       |    <a class="artlead" href="/2023/10/23/21187-reaktion-auf-uebergriff-der-hamas-in-israel-dem-geist-kains-mit-schonungsloser-wahrheitssuche-begegnen">
       |        <div class="artimg" style="background-image: url(/media/assets/article/2023/10/Jordan_Peterson_Israel.jpg.727x485_q75_box-0%2C0%2C5996%2C4000_crop_detail.jpg)"></div>
       |        <div class="autmask"></div>
       |
       |        <div class="autimg" style="background-image: url(/media/assets/authors/Gold-Groezinger_OHCIjB8.jpg.388x485_q75_box-474%2C0%2C1050%2C720_crop_detail.jpg)"></div>
       |
       |    </a>
       |    <div class="">
       |
       |            <h2>
       |                <a href="/2023/10/23/21187-reaktion-auf-uebergriff-der-hamas-in-israel-dem-geist-kains-mit-schonungsloser-wahrheitssuche-begegnen" rel="bookmark">
       |                    <small>Reaktion auf Übergriff der Hamas in Israel<span class="hidden">:</span> </small>Dem „Geist Kains“ mit schonungsloser Wahrheitssuche begegnen
       |
       |                </a>
       |            </h2>
       |
       |
       |            <p class="lead"><a href="/2023/10/23/21187-reaktion-auf-uebergriff-der-hamas-in-israel-dem-geist-kains-mit-schonungsloser-wahrheitssuche-begegnen">Nach einem etwas unüberlegten Schnellschuss setzt Jordan Peterson wieder Zeichen differenzierender Nachdenklichkeit</a></p>
       |            <p>Der „Nervenarzt der Welt“ bleibt seinem Ruf treu, die Dinge beim verdienten Namen zu nennen. In Richtung westlicher und israelischer Politiker könnte er jedoch noch deutlicher werden.</p>
       |
       |
       |            <p class="afoot small">
       |                <em class="author">von
       |
       |                    Robert Grözinger
       |                    </em>
       |                | 3 <i class="fa fa-thumbs-o-up"></i>| 3 <i class="fa fa-comments-o"></i>
       |            </p>
       |
       |    </div>
       |</article>
       |            </div>
       |            <div class="col-md-4">
       |
       |<article>
       |    <a class="artlead" href="/2023/10/23/21188-gestahlfedert-pallywood-in-germany-reichsparteitag-der-heuchler-und-gratismutigen">
       |        <div class="artimg" style="background-image: url(/media/assets/article/2023/10/Judenhass_in_Deutschland.jpg.727x485_q75_box-0%2C0%2C5484%2C3662_crop_detail.jpg)"></div>
       |        <div class="autmask"></div>
       |
       |        <div class="autimg" style="background-image: url(/media/assets/authors/Gold-Werner.jpg.388x485_q75_box-333%2C0%2C910%2C720_crop_detail.jpg)"></div>
       |
       |    </a>
       |    <div class="">
       |
       |            <h2>
       |                <a href="/2023/10/23/21188-gestahlfedert-pallywood-in-germany-reichsparteitag-der-heuchler-und-gratismutigen" rel="bookmark">
       |                    <small>Gestahlfedert: Pallywood in Germany<span class="hidden">:</span> </small>Reichsparteitag der Heuchler und Gratismutigen
       |
       |                </a>
       |            </h2>
       |
       |
       |            <p class="lead"><a href="/2023/10/23/21188-gestahlfedert-pallywood-in-germany-reichsparteitag-der-heuchler-und-gratismutigen">„Schwätzer und Schweiger“ versus „Talk Like An Egyptian“</a></p>
       |            <p>Seit dem Massaker vom 7. Oktober entlädt sich nun mit einer Brachialgewalt, die man beim besten Willen nicht mehr verharmlosen oder gar wegignorieren kann, auf deutschen Straßen das, was die alljährlichen Aufmärsche zum al-Quds-Tag bisher nur leise erahnen ließen: Der fanatische, tödliche, pogromartige Judenhass friedensreligiöser Prägung, Reichskristallnacht Reloaded, Oriental Style.</p>
       |
       |
       |            <p class="afoot small">
       |                <em class="author">von
       |
       |                    Michael Werner
       |                    </em>
       |                | 6 <i class="fa fa-thumbs-o-up"></i>| 1 <i class="fa fa-comments-o"></i>
       |            </p>
       |
       |    </div>
       |</article>
       |            </div>
       |            <div class="col-md-4">
       |
       |<article>
       |    <a class="artlead" href="/2023/10/23/21191-jung-brutal-marktradikal-folge-xxx-tv-xxx">
       |        <div class="artimg" style="background-image: url(/media/assets/article/2023/10/pexels-harrison-haines-3536235.jpg.727x485_q75_box-0%2C2144%2C3638%2C4568_crop_detail.jpg)"></div>
       |        <div class="autmask"></div>
       |
       |        <div class="autimg" style="background-image: url(/media/assets/authors/jbm3.png.388x485_q75_box-925%2C390%2C1425%2C1015_crop_detail.png)"></div>
       |
       |    </a>
       |    <div class="">
       |
       |            <h2>
       |                <a href="/2023/10/23/21191-jung-brutal-marktradikal-folge-xxx-tv-xxx" rel="bookmark">
       |                    <small>Jung Brutal Marktradikal Folge 107 (TV)<span class="hidden">:</span> </small>GZSZ mit Waffen macht BIP-positiv
       |
       |                </a>
       |            </h2>
       |
       |
       |            <p class="lead"><a href="/2023/10/23/21191-jung-brutal-marktradikal-folge-xxx-tv-xxx">Dein Wochenrückblick</a></p>
       |            <p>„Jung Brutal Marktradikal“ ist Dein Wochenrückblick mit „unerwarteten“ Konsequenzen, dem Neusten aus der Clownswelt und den Lichtblicken der Woche.</p>
       |
       |
       |            <p class="afoot small">
       |                <em class="author">von
       |
       |                    Jung Brutal Marktradikal
       |                    </em>
       |                | 2 <i class="fa fa-thumbs-o-up"></i>
       |            </p>
       |
       |    </div>
       |</article>
       |            </div>
       |        </div>
       |
       |        <div class="row">
       |            <div class="col-md-4">
       |
       |<article>
       |    <a class="artlead" href="/2023/10/22/21182-sendung-a-sozial-folge-242-tv-platon-und-die-freiheit--teil-3">
       |        <div class="artimg" style="background-image: url(/media/assets/article/2023/10/shutterstock_2228268935_r14jzlU.jpg.727x485_q75_crop_detail.jpg)"></div>
       |        <div class="autmask"></div>
       |
       |        <div class="autimg" style="background-image: url(/media/assets/authors/Tamm-Blau_550oW35.jpg.388x485_q75_box-286%2C0%2C863%2C719_crop_detail.jpg)"></div>
       |
       |    </a>
       |    <div class="">
       |
       |            <h2>
       |                <a href="/2023/10/22/21182-sendung-a-sozial-folge-242-tv-platon-und-die-freiheit--teil-3" rel="bookmark">
       |                    <small>Sendung „A-Sozial“ Folge 242 (TV)<span class="hidden">:</span> </small>Platon und die Freiheit – Teil 3
       |
       |                </a>
       |            </h2>
       |
       |
       |            <p class="lead"><a href="/2023/10/22/21182-sendung-a-sozial-folge-242-tv-platon-und-die-freiheit--teil-3">Der marktradikale Kommentar zur Politik und dem ganzen Rest</a></p>
       |            <p>In der neuen Ausgabe der Sendung „A-Sozial“ erscheint heute die Serie „Platon und die Freiheit“ mit dem dritten Teil.</p>
       |
       |
       |            <p class="afoot small">
       |                <em class="author">von
       |
       |                    Sascha Tamm
       |                    </em>
       |
       |            </p>
       |
       |    </div>
       |</article>
       |            </div>
       |            <div class="col-md-4">
       |
       |<article>
       |    <a class="artlead" href="/2023/10/22/21177-sendung-auf-den-punkt-folge-168-tv-die-ampel-vor-dem-aus">
       |        <div class="artimg" style="background-image: url(/media/assets/article/2023/10/shutterstock_621883967.jpg.727x485_q75_box-0%2C625%2C3674%2C3078_crop_detail.jpg)"></div>
       |        <div class="autmask"></div>
       |
       |        <div class="autimg" style="background-image: url(/media/assets/authors/Blau-Kuhnle_QPaxCPE.jpg.388x485_q75_box-294%2C0%2C871%2C720_crop_detail.jpg)"></div>
       |
       |    </a>
       |    <div class="">
       |
       |            <h2>
       |                <a href="/2023/10/22/21177-sendung-auf-den-punkt-folge-168-tv-die-ampel-vor-dem-aus" rel="bookmark">
       |                    <small>Sendung „Auf den Punkt“ Folge 168 (TV)<span class="hidden">:</span> </small>Die Ampel vor dem Aus?
       |
       |                </a>
       |            </h2>
       |
       |
       |            <p class="lead"><a href="/2023/10/22/21177-sendung-auf-den-punkt-folge-168-tv-die-ampel-vor-dem-aus">Die Videokolumne von Joachim Kuhnle</a></p>
       |            <p>In der aktuellen Sendung „Auf den Punkt“ stellt sich Joachim Kuhnle die Frage, ob die Ampel-Koalition vor dem Aus steht und ob sie durch ein rot-schwarzes Bündnis ersetzt werden könnte.</p>
       |
       |
       |            <p class="afoot small">
       |                <em class="author">von
       |
       |                    Joachim Kuhnle
       |                    </em>
       |                | 1 <i class="fa fa-thumbs-o-up"></i>
       |            </p>
       |
       |    </div>
       |</article>
       |            </div>
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
