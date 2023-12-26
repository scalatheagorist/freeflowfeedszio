package org.scalatheagorist.freeflowfeedszio.view

object IndexHtml {
  def apply(gridElements: String): String = html(gridElements) + scripts

  private def html(gridElements: String): String = s"""
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
      |    <style>
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
      |               <form class="form-inline" id="search-form">
      |                  <input class="form-control" type="search" placeholder="Search..." aria-label="Search" id="search-input">
      |                  <button class="btn btn-secondary" type="submit">Search</button>
      |               </form>
      |               <div class="modal fade" id="searchErrorModal" tabindex="-1" role="dialog" aria-labelledby="searchErrorModalLabel" aria-hidden="true">
      |                 <div class="modal-dialog" role="document">
      |                   <div class="modal-content">
      |                     <div class="modal-header">
      |                       <h5 class="modal-title" id="searchErrorModalLabel">Error</h5>
      |                       <button type="button" class="close" data-dismiss="modal" aria-label="Close">
      |                         <span aria-hidden="true">&times;</span>
      |                       </button>
      |                     </div>
      |                     <div class="modal-body">
      |                       The search term must contain at least 3 letters!
      |                       Der Suchbegriff muss mindestens 3 Buchstaben enthalten!
      |                     </div>
      |                     <div class="modal-footer">
      |                       <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
      |                     </div>
      |                   </div>
      |                 </div>
      |               </div>
      |            </li>
      |            <li>
      |            </li>
      |            <li class="nav-item text-center mr-auto">
      |                <a class="btn btn-secondary nav-btn" href="/articles/1">Home</a>
      |            </li>
      |            <li class="nav-item text-center dropdown mr-auto">
      |                <button class="btn btn-secondary dropdown-toggle nav-btn" type="button" id="dropdownMenuButton"
      |                        data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" title="all magazines">
      |                    Magazine
      |                </button>
      |                <div class="dropdown-menu dropdown-menu-right" aria-labelledby="dropdownMenuButton">
      |                    <a class="dropdown-item" href="/articles/1">all magazines</a>
      |                    <a class="dropdown-item" href="/misesde/1">MisesDE (German)</a>
      |                    <a class="dropdown-item" href="/hayekinstitut/1">Hayek Institut (German)</a>
      |                    <a class="dropdown-item" href="/schweizermonat/1">Schweizer Monat (German)</a>
      |                    <a class="dropdown-item" href="/efmagazin/1">EigentümlichFrei (German)</a>
      |                    <a class="dropdown-item" href="/freiheitsfunken/1">Freiheitsfunken (German)</a>
      |                </div>
      |            </li>
      |            <li class="nav-item text-center dropdown mr-auto">
      |                <button class="btn btn-secondary dropdown-toggle nav-btn" type="button" id="dropdownMenuButton"
      |                        data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
      |                    Language
      |                </button>
      |                <div class="dropdown-menu dropdown-menu-right" aria-labelledby="dropdownMenuButton">
      |                    <a class="dropdown-item" href="/articles/1">language</a>
      |                    <a class="dropdown-item" href="/english/1">English articles</a>
      |                    <a class="dropdown-item" href="/german/1">Deutsche Artikel</a>
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
      |    </div>
      |</nav>
      |<a id="opensource-band" href="https://github.com/scalatheagorist/freeflowfeeds" target="_blank" class="open-source-badge">
      |    100% Open Source
      |</a>
      |<a href="#" id="scrollToTopButton"><i class="fas fa-arrow-up"></i></a>
      |
      |<div class="container grid-container">
      |    <div class="custom-grid" id="custom-grid">
      |        $gridElements
      |    </div>
      |</div>
      |<div class="container">
      |    <div class="row">
      |        <div class="col-12 text-center mt-5" id="loading-bar" style="display: none;">
      |            <div class="spinner-border text-primary" role="status">
      |                <span class="visually-hidden"></span>
      |            </div>
      |        </div>
      |    </div>
      |</div>
      |
      |""".stripMargin

  private def scripts: String = """
      |
      |<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
      |<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
      |
      |<script>
      |
      |const grid = document.getElementById('custom-grid');
      |const loadingBar = document.getElementById('loading-bar');
      |const loadingProgress = document.getElementById('loading-progress');
      |
      |let pageNumber = 1;
      |let isLoading = false;
      |
      |async function loadPage(page) {
      |    if (isLoading) return;
      |
      |    isLoading = true;
      |    loadingBar.style.display = 'block';
      |
      |    const currentPath = window.location.pathname;
      |    const newPath = currentPath.substring(0, currentPath.lastIndexOf('/'));
      |    const searchTerm = document.getElementById('search-input').value.trim();
      |
      |    if (searchTerm.length < 3) {
      |      try {
      |        const response = await fetch(`${newPath}/${page}`);
      |        if (!response.ok) {
      |            throw new Error('Network response was not ok.');
      |        }
      |
      |        const newData = await response.text();
      |
      |        grid.innerHTML += newData;
      |      } catch (error) {
      |          console.error('Error fetching data:', error);
      |      } finally {
      |          loadingBar.style.display = 'none';
      |          isLoading = false;
      |      }
      |    }
      |}
      |
      |window.addEventListener('scroll', () => {
      |    const { scrollTop, scrollHeight, clientHeight } = document.documentElement;
      |
      |    if (scrollTop + clientHeight >= scrollHeight - 100) {
      |       if (!isLoading) {
      |           pageNumber++;
      |           loadPage(pageNumber);
      |       }
      |    }
      |});
      |
      |
      |// search
      |document.getElementById('search-form').addEventListener('submit', async function(event) {
      |    event.preventDefault();
      |
      |    const searchTerm = document.getElementById('search-input').value.trim();
      |    if (searchTerm.length >= 3) {
      |        try {
      |            const response = await fetch(`/search?term=${searchTerm}`);
      |            if (!response.ok) {
      |                throw new Error('Network response was not ok.');
      |            }
      |
      |            const newData = await response.text();
      |
      |            grid.innerHTML = "";
      |            grid.innerHTML += newData;
      |        } catch (error) {
      |            console.error('Error fetching data:', error);
      |        }
      |    } else {
      |        $('#searchErrorModal').modal('show');
      |    }
      |});
      |
      |// go to top
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
      |
      |
      |</script>
      |</body>
      |</html>
      |""".stripMargin
}
