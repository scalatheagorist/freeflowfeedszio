package org.scalatheagorist.freeflowfeedszio.view

import org.scalatheagorist.freeflowfeedszio.models.RSSFeed
import org.scalatheagorist.freeflowfeedszio.publisher.Lang
import org.scalatheagorist.freeflowfeedszio.publisher.Publisher
import zio.ULayer
import zio.ZLayer
import zio.stream.ZStream

import java.io.IOException

trait RSSBuilder {
  def build(
    publisher: Option[Publisher],
    lang: Option[Lang])(
    stream: ZStream[Any, IOException, RSSFeed]
  ): ZStream[Any, IOException, String]
}

object RSSBuilder {
  val layer: ULayer[RSSBuilder] =
    ZLayer.succeed {
      new RSSBuilder {
        def build(
          publisher: Option[Publisher],
          lang: Option[Lang])(
          stream: ZStream[Any, IOException, RSSFeed]
        ): ZStream[Any, IOException, String] = {
          val cards =
            publisher
              .map(p => stream.filter(_.publisher == p))
              .orElse(lang.map(l => stream.filter(_.lang == l)))
              .getOrElse(stream)
              .flatMap(feed => ZStream.succeed(generateCardElement(feed)))

          header ++ startCards ++ cards ++ closeCards ++ footer
        }
      }
    }

  private lazy val header = ZStream.succeed(getHeader)
  private lazy val footer = ZStream.succeed(getFooter)
  private lazy val startCards = ZStream.succeed(
    """
      |<div class="container grid-container">
      |<div class="custom-grid">
      |""".stripMargin
  )
  private lazy val closeCards = ZStream.succeed(
    """
      |</div>
      |</div>""".stripMargin
  )

  private def generateCardElement(rssFeed: RSSFeed): String =
    s"""
       |<div class="article-card">
       |    <div class="card mb-3 bg-primary text-white">
       |        <div class="card-body" onclick="window.open('${rssFeed.article.link}', '_blank');" style="cursor: pointer;">
       |          ${s"""<p>${rssFeed.author}</p>\n"""}
       |          ${s"""<p><span class="highlight-title">${rssFeed.article.title}</span></p>"""}
       |          ${s"""<p><strong>Link:</strong><br><a href="${rssFeed.article.link}" target="_blank">${rssFeed.article.link}</a></p>"""}
       |        </div>
       |    </div>
       |</div>
       |""".stripMargin

  private def getHeader: String =
    s"""
       |<!DOCTYPE html>
       |<html>
       |<head>
       |    <link rel="icon"
       |          href="https://image.nostr.build/86fed7343053943f87b8370dd1c38637a28b555f04f09c0acda3bf5ac675fe9f.png"
       |          type="image/png">
       |    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
       |    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
       |    <link rel="stylesheet" href="https://unpkg.com/terminal.css@0.7.2/dist/terminal.min.css" />
       |    <link rel="stylesheet" href="https://unpkg.com/terminal.css@0.7.1/dist/terminal.min.css" />
       |    <link rel="canonical" href="https://www.liblit.org/articles">
       |
       |    <meta charset="UTF-8">
       |    <meta http-equiv="X-UA-Compatible" content="ie=edge">
       |    <meta name="description" content="Freiheitliche Meta Feeds">
       |    <meta name="robots" content="index, follow">
       |    <meta name="keywords" content="Liberty Freiheit Marktradikal Ankap Libertarismus Liberalismus">
       |    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=0.75, user-scalable=0">
       |
       |    <meta property="og:locale" content="de_DE">
       |    <meta property="og:site_name">
       |    <meta property="og:title" content="Liberty Literature">
       |    <meta property="og:description" content="Freiheitliche Meta Feeds">
       |    <meta property="og:image" content="https://image.nostr.build/99b3afb4def317934f3fbc2b8a854b3ded800c80ca2fba445303fe6d6d9ace57.jpg">
       |    <meta property="og:image:width" content="1200">
       |    <meta property="og:image:height" content="543">
       |    <meta property="og:url" content="https://www.liblit.org">
       |    <meta property="og:type" content="website">
       |
       |    <meta name="twitter:title" content="liblit">
       |    <meta name="twitter:description" content="Liberty Literature">
       |    <meta name="twitter:image" content="https://image.nostr.build/99b3afb4def317934f3fbc2b8a854b3ded800c80ca2fba445303fe6d6d9ace57.jpg">
       |    <meta name="twitter:card" content="summary_large_image">
       |    $getCSS
       |    <title>liberty literature</title>
       |</head>
       |<body>
       |<nav class="navbar navbar-expand-lg navbar-light fixed-top">
       |    <a class="navbar-brand" href="https://www.die-marktradikalen.de/" target="_blank">
       |        <img src="https://image.nostr.build/7af55e65d295f26b0cfe84f5cfab1b528b934c7150308cd97397ec9af1e0b42b.png"
       |             alt="Die Marktradikalen" class="logo" title="Zu den Marktradikalen">
       |    </a>
       |    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav" id="navbar-nav-toggle">
       |        <span class="navbar-toggler-icon"></span>
       |    </button>
       |    <div class="collapse navbar-collapse" id="navbarNav">
       |        <ul class="navbar-nav">
       |            <li class="nav-item text-center mr-auto">
       |               <form class="form-inline">
       |                <input class="form-control" type="search" placeholder="Search..." aria-label="Search" id="search-input">
       |            </form>
       |            </li>
       |            <li class="nav-item text-center mr-auto">
       |                <a class="btn btn-secondary nav-btn" href="/articles">Home</a>
       |            </li>
       |            <li class="nav-item text-center dropdown mr-auto">
       |                <button class="btn btn-secondary dropdown-toggle nav-btn" type="button" id="dropdownMenuButton"
       |                        data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" title="all magazines">
       |                    Magazine
       |                </button>
       |                <div class="dropdown-menu dropdown-menu-right" aria-labelledby="dropdownMenuButton">
       |                    <a class="dropdown-item" href="/articles">all magazines</a>
       |                    <a class="dropdown-item" href="/articles/misesde">MisesDE (German)</a>
       |                    <a class="dropdown-item" href="/articles/hayekinstitut">Hayek Institut (German)</a>
       |                    <a class="dropdown-item" href="/articles/schweizermonat">Schweizer Monat (German)</a>
       |                    <a class="dropdown-item" href="/articles/efmagazin">EigentümlichFrei (German)</a>
       |                    <a class="dropdown-item" href="/articles/freiheitsfunken">Freiheitsfunken (German)</a>
       |                    <a class="dropdown-item" href="/articles/diemarktradikalen">Die Marktradikalen (German)</a>
       |                    <a class="dropdown-item" href="/articles/dersandwirt">Der Sandwirt (German)</a>
       |                </div>
       |            </li>
       |            <li class="nav-item text-center dropdown mr-auto">
       |                <button class="btn btn-secondary dropdown-toggle nav-btn" type="button" id="dropdownMenuButton"
       |                        data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
       |                    Language
       |                </button>
       |                <div class="dropdown-menu dropdown-menu-right" aria-labelledby="dropdownMenuButton">
       |                    <a class="dropdown-item" href="/articles">language</a>
       |                    <a class="dropdown-item" href="/articles/english">English</a>
       |                    <a class="dropdown-item" href="/articles/german">German</a>
       |                </div>
       |            </li>
       |            <li class="nav-item text-center mr-auto">
       |                <button type="button" class="btn btn-secondary nav-btn" data-toggle="modal" data-target="#impressumModal">
       |                    Legal
       |                </button>
       |                <div class="modal fade" id="impressumModal" tabindex="-1" role="dialog" aria-labelledby="impressumModalLabel" aria-hidden="true">
       |                    <div class="modal-dialog" role="document">
       |                        <div class="modal-content">
       |                            <div class="modal-header">
       |                                <h5 class="modal-title" id="impressumModalLabel">Legal</h5>
       |                                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
       |                                    <span aria-hidden="true">&times;</span>
       |                                </button>
       |                            </div>
       |                            <div class="modal-body text-left">
       |                                <p>This site is operated exclusively on a voluntary and private basis. There are no business relationships with the linked websites.</p>
       |                                <p>Contact: lightningrises@proton.me</p>
       |                            </div>
       |                            <div class="modal-footer">
       |                                <button type="button" class="btn btn-secondary" data-dismiss="modal">close</button>
       |                            </div>
       |                        </div>
       |                    </div>
       |                </div>
       |            </li>
       |        </ul>
       |        <span class="navbar-text" id="info">English article publishers coming soon...</span>
       |    </div>
       |</nav>
       |<a id="opensource-band" href="https://github.com/scalatheagorist/freeflowfeeds" target="_blank" class="open-source-badge">
       |    100% Open Source
       |</a>
       |<a href="#" id="scrollToTopButton"><i class="fas fa-arrow-up"></i></a>
       |""".stripMargin

  private def getCSS: String =
    """
      |<style>
      |#info {
      |    margin-left: 30px;
      |    margin-top: 20px;
      |}
      |.navbar {
      |    background-color: #ffb400 !important;
      |}
      |
      |.navbar-brand {
      |    height: 90px;
      |}
      |
      |a.navbar-brand:focus, a.navbar-brand:active {
      |    outline: none !important;
      |}
      |
      |.ankapstore-logo {
      |    height: 50px;
      |    margin-bottom: -15px;
      |}
      |
      |#ankapstore:hover {
      |    background: darkred;
      |}
      |
      |.nav-btn {
      |    width: 125px;
      |    height: 50px;
      |}
      |
      |.grid-container {
      |    margin-top: 7%;
      |    position: relative;
      |    left: -170px;
      |}
      |
      |.input-group {
      |    width: 100%;
      |    max-width: 400px;
      |}
      |
      |.logo {
      |    max-width: 160px;
      |    margin-top: -7px;
      |}
      |
      |.logo-link {
      |    max-width: 160px;
      |    height: auto;
      |    margin-right: 27%;
      |    margin-left: 3%;
      |}
      |
      |#search-input {
      |    min-width: 100px !important;
      |    height: 100% !important;
      |}
      |
      |.input-group {
      |    width: 100%;
      |    max-width: 400px;
      |}
      |
      |.card {
      |    width: 130%;
      |    height: 200px;
      |}
      |
      |.highlight-title {
      |    font-weight: bold;
      |    font-style: italic;
      |}
      |
      |.card a {
      |    white-space: nowrap;
      |    overflow: hidden;
      |    text-overflow: ellipsis;
      |    display: inline-block;
      |    max-width: 90%;
      |}
      |
      |.card.mb-3 {
      |    background-color: #30311f !important;
      |    color: white !important;
      |    transform: translateY(0);
      |    transition: transform 0.3s ease, box-shadow 0.3s ease;
      |    box-shadow: none;
      |}
      |
      |.card:hover {
      |    transform: translateY(-10px);
      |    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
      |}
      |
      |a:hover {
      |    text-decoration: none;
      |    outline: none;
      |}
      |
      |.btn {
      |    background-color: #30311f !important;
      |    padding: 10px 20px;
      |    font-size: 18px;
      |}
      |
      |.open-source-badge {
      |    position: fixed;
      |    bottom: 145px;
      |    right: -45px;
      |    background-color: #ffb400 !important;
      |    color: #000;
      |    padding: 20px 48px;
      |    border-radius: 5px;
      |    transform: rotate(-45deg);
      |    transform-origin: bottom right;
      |    font-size: 18px;
      |    line-height: 1;
      |    border: 2px solid #000;
      |}
      |
      |.modal-backdrop {
      |    position: inherit !important;
      |    top: 0;
      |    left: 0;
      |    z-index: 1040;
      |    width: 100vw;
      |    height: 100vh;
      |    background-color: #000;
      |}
      |
      |
      |a.keyframe-image {
      |    display: block;
      |    width: 100%;
      |    height: 100%;
      |    position: absolute;
      |    top: 0;
      |    left: 0;
      |}
      |
      |#scrollToTopButton {
      |    display: none;
      |    position: fixed;
      |    bottom: 20px;
      |    left: 20px;
      |    background: #373827;
      |    color: #feb60c;
      |    border-radius: 20%;
      |    padding: 16px;
      |    text-align: center;
      |    font-size: 16px;
      |    cursor: pointer;
      |    z-index: 999;
      |}
      |
      |#scrollToTopButton:hover {
      |    background: darkred;
      |}
      |
      |body {
      |    background-color: #0f0f0f;
      |    margin: 0;
      |    padding: 0;
      |    animation: backgroundChange 512s cubic-bezier(0.4, 0.0, 0.2, 1) infinite;
      |    background-attachment: fixed;
      |    background-position: right 130px;
      |    background-size: auto 70%;
      |    background-repeat: no-repeat;
      |
      |}
      |
      |@media (max-width: 768px) {
      |    #info {
      |        display: none;
      |    }
      |
      |    .grid-container {
      |        margin-top: 28%;
      |        left: 0px;
      |    }
      |
      |    #ankapstore {
      |       display: none;
      |    }
      |
      |    #opensource-band {
      |       display: none;
      |    }
      |
      |    body {
      |        background-color: #0f0f0f !important;
      |        margin: 0;
      |        padding: 0;
      |        animation: none;
      |        background-attachment: fixed;
      |        background-position: right top;
      |        background-size: auto 100%;
      |        background-repeat: repeat-y;
      |    }
      |
      |    .custom-grid {
      |        display: grid;
      |        grid-template-columns: auto !important;
      |        grid-gap: 10px;
      |        justify-content: start;
      |        margin-top: 1%;
      |    }
      |
      |    .card-container {
      |        display: flex;
      |        justify-content: center;
      |        align-items: center;
      |    }
      |
      |    .card {
      |        margin-bottom: 20px;
      |        max-width: 469px;
      |    }
      |
      |}
      |</style>
      |""".stripMargin

  private def getFooter: String =
    s"""
      |<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
      |<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
      |</body>
      |</html>
      |<script>
      |  $getJS
      |</script>
      |""".stripMargin

  private def getJS: String =
    """
      |function initializeNavbar() {
      |    const searchForm = document.querySelector('.navbar .form-inline');
      |    const searchInput = document.querySelector('.navbar #search-input');
      |    let cards = document.querySelectorAll('.card');
      |    let anyVisible = false;
      |
      |    if (searchForm && searchInput) {
      |        searchForm.addEventListener('submit', function (event) {
      |            event.preventDefault();
      |        });
      |
      |        searchInput.addEventListener('input', function () {
      |            window.scrollTo({ top: 0, behavior: 'smooth' });
      |            let searchTerm = this.value.toLowerCase();
      |
      |            if (searchTerm === '') {
      |                cards.forEach(function (card) {
      |                    card.style.display = 'block';
      |                });
      |                anyVisible = true;
      |            } else {
      |                anyVisible = false;
      |                cards.forEach(function (card) {
      |                    let cardText = card.textContent.toLowerCase();
      |                    if (cardText.includes(searchTerm)) {
      |                        card.style.display = 'block';
      |                        anyVisible = true;
      |                    } else {
      |                        card.style.display = 'none';
      |                    }
      |                });
      |            }
      |
      |            let customGrid = document.querySelector('.custom-grid');
      |            customGrid.innerHTML = '';
      |
      |            cards.forEach(function (card) {
      |                if (anyVisible || card.style.display !== 'none') {
      |                    customGrid.appendChild(card);
      |                }
      |            });
      |        });
      |    }
      |}
      |
      |function searchFunction(searchTerm) {
      |    alert('Suche nach: ' + searchTerm);
      |}
      |
      |window.addEventListener('load', initializeNavbar);
      |
      |$(document).ready(function () {
      |    $(window).scroll(function () {
      |        if ($(this).scrollTop() > 100) {
      |            $('#scrollToTopButton').fadeIn();
      |        } else {
      |            $('#scrollToTopButton').fadeOut();
      |        }
      |    });
      |
      |    $('#scrollToTopButton').click(function () {
      |        $('html, body').animate({ scrollTop: 0 }, 800);
      |        return false;
      |    });
      |});
      |""".stripMargin
}
