package org.scalatheagorist.freeflowfeedszio.publisher

import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.scraper.ContentExtractors.elementList

object SchweizerMonatTest extends App {
  val browser = JsoupBrowser()

  val list = (browser.parseString(htmlResponse) >> elementList(".teaser__link")).map { elem =>
    val title = elem >?> attr("title")
    val href = elem >?> attr("href")
    val author = (elem >?> element("a") >?> text(".meta-author")).flatten

    (title, href.mkString, author.getOrElse("SchweizerMonat"))
  }

  list.distinctBy(_._2).foreach { {
    case (maybeString, str, str1) =>
    println("Title: " + maybeString.mkString)
    println("Author: " + str1)
    println("Link: " + str)
  }}

  def htmlResponse: String = {
    s"""
       |<body class="archive tax-dossier term-der-aufbruch-von-1848 term-30917 theme-monat woocommerce-js hfeed grid-free is-cursor-left">
       |<div id="page" class="site">
       |    <a class="skip-link screen-reader-text" href="#content">Zum Inhalt springen</a>
       |
       |	<header id="masthead" class="site-header">
       |    <div class="site-header__container site-container">
       |        <button class="hamburger hamburger--elastic" type="button" aria-label="Menu" aria-controls="navigation" aria-expanded="false">
       |                            <span class="hamburger-box">
       |                              <span class="hamburger-inner"></span>
       |                            </span>
       |        </button>
       |
       |		<div class="site-branding">
       |	        <p class="site-title"><a href="https://schweizermonat.ch/" rel="home"><svg height="39" viewBox="0 0 145 39" width="145" xmlns="http://www.w3.org/2000/svg"><path d="M125.342 14.323h-3.276v4.705h3.276v13.514c0 4.244 2.538 6.458 7.29 6.458 1.57 0 3.23-.323 5.63-1.153l.231-4.567c-1.2.461-2.26.738-3.138.738-2.676 0-3.553-.83-3.553-4.243V19.028h6.46v-4.705h-6.46v-5.35l-6.46.368zm-10.886 13.883c0 4.429-2.739 6.09-5.478 6.09-2.69 0-3.766-1.477-3.766-2.907 0-2.075 1.761-3.183 7.337-3.183zm.098-3.966c-4.989 0-8.462.23-11.054 1.107-2.69.922-5.136 3.182-5.136 6.642 0 3.782 2.788 7.011 8.56 7.011 4.305 0 6.652-1.706 8.364-5.35h.098c-.245.922-.343 1.753-.343 3.32v1.477h8.609V33.74h-2.886c.098-3.413.244-6.734.244-10.7 0-7.011-4.352-9.272-11.2-9.272-4.55 0-8.56 1.615-10.223 2.491v6.135h5.87v-3.229c1.173-.415 3.276-.692 4.254-.692 3.229 0 4.843.969 4.843 4.613zm-42.193 9.5h-2.354v4.706H81.59V33.74h-2.77v-8.025c0-3.644 1.708-6.412 5.63-6.412 2.445 0 4.199 1.2 4.199 4.52v9.917h-2.63v4.706h11.859V33.74H95.11V23.18c0-5.766-2.63-9.41-8.352-9.41-4.2 0-7.152 2.353-8.352 5.535h-.093c.37-1.752.37-3.32.37-4.981h-9.46v4.705h3.138zm-20.996-6.964c0-4.382 1.708-7.75 5.676-7.75 3.969 0 5.676 2.584 5.676 7.75 0 4.336-1.707 6.964-5.676 6.964-3.968 0-5.676-2.628-5.676-6.964zm-7.014-.139c0 8.21 4.984 12.362 12.69 12.362s12.69-5.166 12.69-12.362c0-8.395-4.569-12.869-12.69-12.869-7.89 0-12.69 5.95-12.69 12.87zm-11.028 11.81h11.583V33.74h-2.769V23.18c0-5.766-2.63-9.41-8.352-9.41-4.245 0-6.922 2.076-8.075 5.443h-.093c-.646-3.598-3.137-5.443-7.106-5.443-4.014 0-7.014 1.937-8.352 5.443h-.092c.369-1.66.369-3.229.369-4.89H.976v4.706h3.138V33.74h-2.63v4.706h11.859V33.74h-2.769v-8.025c0-3.644 1.708-6.412 5.122-6.412 2.446 0 4.2 1.2 4.2 4.52v9.917h-2.354v4.706h11.582V33.74h-2.768v-8.025c0-3.875 1.984-6.412 5.122-6.412 2.445 0 4.199 1.2 4.199 4.52v9.917h-2.354zm111.11-22.739V13.65h-.474v.675h-2.785v.526l.693 1.241.429-.28-.377-.596c-.042-.064-.084-.114-.13-.173h2.17v.665zm0 3.58v-2.604h-.53v1.27c0 .108.013.242.027.355h-.01c-.623-.833-1.224-1.522-1.881-1.522-.698 0-.945.645-.945 1.172 0 1.04.708 1.31 1.08 1.31v-.69c-.307-.034-.549-.236-.549-.59 0-.202.112-.483.447-.483.647 0 1.668 1.409 1.914 1.783zm-.014 2.99c.08-.25.093-.452.093-.708 0-1.183-.782-1.749-1.872-1.749-1.057 0-1.546.58-1.546 1.345 0 .871.638 1.206 1.174 1.206.675 0 1.014-.512 1.014-1.034 0-.335-.107-.601-.315-.803l.008-.01c.49.064 1.063.315 1.063 1.084 0 .182-.052.409-.126.625zm-2.203-.555c-.177 0-.59-.084-.59-.553 0-.34.227-.63.898-.63a.865.865 0 0 1 .256.596c0 .315-.205.587-.564.587zm2.217 3.127v-2.06h-.474v.675h-2.785v.526l.693 1.241.429-.28-.377-.596c-.042-.064-.084-.113-.13-.172h2.17v.665zm-2.338 2.295v-.59l-.921.029v3.142l.921.029v-.59l-.447-.03v-.621h2.31v.31h.475v-1.34h-.474v.311h-2.31v-.62zm1.864 4.265v.31h.474v-1.339h-.474v.31h-2.31v-.31h-.475v1.339h.474v-.31zm0 1.985v.857h-.951v-.867h-.502v.867h-.858v-.842l.485-.03v-.59l-.96.03v2.52h.475v-.37h2.31v.311h.475v-2.477l-1.024-.039v.59zm-1.892 3.743v-.65h-.777c-.107.296-.172.714-.172 1.133 0 .975.456 1.359.972 1.359.504 0 .764-.241.937-.95l.093-.38c.112-.463.233-.561.4-.561.247 0 .439.187.439.61 0 .203-.014.4-.085.582h-.428V39h.861c.144-.463.182-.857.182-1.237 0-.98-.451-1.422-1.071-1.422-.675 0-.834.56-.955 1.068l-.098.414c-.05.227-.12.409-.358.409-.181 0-.415-.153-.415-.63 0-.168.037-.336.08-.464zM65.78 11.006h1.701V7.382c0-1.68.926-2.607 2.09-2.607.24 0 .493.044.732.073v-1.62c-.15-.015-.254-.045-.642-.045-1.045 0-2.06.87-2.254 1.68h-.03V3.39H65.78zm-6.973-4.478c.045-1.135.852-2.019 2.03-2.019 1.15 0 1.882.958 1.942 2.019zm5.674 1.105c.283-2.284-1.21-4.45-3.643-4.45-2.3 0-3.732 1.857-3.732 4.023 0 2.342 1.358 4.006 3.777 4.006 1.687 0 3.12-.927 3.493-2.592h-1.612c-.299.84-.896 1.267-1.881 1.267-1.419 0-2.076-1.061-2.076-2.254zM49.746 4.716h3.97l-4.269 5.112v1.178h6.793V9.68h-4.643l4.419-5.112V3.39h-6.27zM46.46 2.079h1.702V.487H46.46zm0 8.927h1.702V3.39H46.46zm-7.017-4.478c.045-1.135.851-2.019 2.03-2.019 1.15 0 1.882.958 1.942 2.019zm5.673 1.105c.284-2.284-1.209-4.45-3.642-4.45-2.3 0-3.733 1.857-3.733 4.023 0 2.342 1.36 4.006 3.777 4.006 1.688 0 3.12-.927 3.494-2.592H43.4c-.299.84-.896 1.267-1.882 1.267-1.418 0-2.075-1.061-2.075-2.254zm-16.93 3.373h1.793l1.417-5.657h.03l1.433 5.657h1.747l2.419-7.616h-1.732l-1.568 5.686h-.03L32.309 3.39h-1.717l-1.448 5.686h-.03L27.606 3.39h-1.807zm-10.255 0h1.701V6.513c0-1.12.732-2.004 1.866-2.004 1 0 1.479.516 1.508 1.71v4.787h1.702v-5.23c0-1.709-1.06-2.593-2.732-2.593-1.015 0-1.896.516-2.314 1.208h-.03V.488h-1.701zM16.543 5.94c-.164-1.872-1.687-2.756-3.478-2.756-2.538 0-3.807 1.798-3.807 4.096 0 2.24 1.328 3.934 3.747 3.934 1.985 0 3.225-1.09 3.538-3.065h-1.702c-.164 1.09-.85 1.739-1.836 1.739-1.478 0-2.045-1.34-2.045-2.608 0-2.313 1.239-2.77 2.12-2.77.94 0 1.627.501 1.761 1.43zM1.465 8.56c.03 1.797 1.478 2.652 3.418 2.652 1.598 0 3.449-.663 3.449-2.476 0-1.502-1.254-1.944-2.493-2.224-1.254-.28-2.508-.398-2.508-1.223 0-.663.91-.78 1.403-.78.747 0 1.418.22 1.568 1.016h1.776C7.87 3.832 6.436 3.183 4.854 3.183c-1.404 0-3.225.516-3.225 2.18 0 1.548 1.224 1.99 2.478 2.255 1.24.28 2.478.383 2.523 1.267.045.87-1.075 1.002-1.717 1.002-.91 0-1.657-.354-1.746-1.326z" fill-rule="evenodd"></path></svg></a></p>
       |	</div><!-- .site-branding -->
       |					            <div class="search-container">
       |                <a href="https://schweizermonat.ch/?s=" id="search-toggle" class="search-button">
       |					<svg height="14" viewBox="0 0 14 14" width="14" xmlns="http://www.w3.org/2000/svg"><path d="m13.7430947 12.5740557-3.83300005-3.83449998c.68707075-.93709637 1.05513185-2.07002349 1.05000005-3.232-.0131097-3.03314425-2.46637806-5.48998128-5.49950005-5.5075-1.45151984-.00656729-2.84537862.56765657-3.87106401 1.59475074-1.0256854 1.02709418-1.59799557 2.4217398-1.58943599 3.87324926.01310971 3.03342039 2.46660187 5.49048128 5.5 5.50799998 1.16671019.0050567 2.30377649-.3673005 3.2415-1.06149998l.004-.003 3.82950005 3.83149998c.2070488.2172135.5155657.3051364.8060149.2297027.2904491-.0754336.5171924-.3023711.5923775-.5928847.0751851-.2905135-.0130018-.5989551-.2303924-.805818zm-8.24700005-2.69599998c-2.42658594-.01395899-4.38934775-1.9793973-4.4-4.406-.00654941-1.16105718.45133639-2.2765496 1.27173022-3.09816425.82039383-.82161466 1.93520415-1.28115868 3.09626978-1.27633575 2.42658594.01395898 4.38934775 1.97939729 4.4 4.406.00654941 1.16105718-.45133639 2.2765496-1.27173022 3.09816425s-1.93520415 1.28115867-3.09626978 1.27633575z" fill-rule="evenodd" transform=""></path></svg>                </a>
       |                <a href="#" class="search-close">×</a>
       |
       |<section class="site-search">
       |
       |    <div class="site-search__wrap">
       |        <span class="site-search__claim">
       |                    Der Kleingeist hält Ordnung – das Genie aber überblickt das Chaos. Was suchen Sie?
       |	                </span>
       |
       |        <form role="search" method="get" class="search-form" action="https://schweizermonat.ch/">
       |
       |            <label for="search-form-652e36f95620b">
       |                <span class="screen-reader-text">Search for:</span>
       |            </label>
       |            <input type="search" id="search-form-652e36f95620b" class="search-field" value="" name="s">
       |            <button type="submit" class="search-submit"><svg height="14" viewBox="0 0 14 14" width="14" xmlns="http://www.w3.org/2000/svg"><path d="m13.7430947 12.5740557-3.83300005-3.83449998c.68707075-.93709637 1.05513185-2.07002349 1.05000005-3.232-.0131097-3.03314425-2.46637806-5.48998128-5.49950005-5.5075-1.45151984-.00656729-2.84537862.56765657-3.87106401 1.59475074-1.0256854 1.02709418-1.59799557 2.4217398-1.58943599 3.87324926.01310971 3.03342039 2.46660187 5.49048128 5.5 5.50799998 1.16671019.0050567 2.30377649-.3673005 3.2415-1.06149998l.004-.003 3.82950005 3.83149998c.2070488.2172135.5155657.3051364.8060149.2297027.2904491-.0754336.5171924-.3023711.5923775-.5928847.0751851-.2905135-.0130018-.5989551-.2303924-.805818zm-8.24700005-2.69599998c-2.42658594-.01395899-4.38934775-1.9793973-4.4-4.406-.00654941-1.16105718.45133639-2.2765496 1.27173022-3.09816425.82039383-.82161466 1.93520415-1.28115868 3.09626978-1.27633575 2.42658594.01395898 4.38934775 1.97939729 4.4 4.406.00654941 1.16105718-.45133639 2.2765496-1.27173022 3.09816425s-1.93520415 1.28115867-3.09626978 1.27633575z" fill-rule="evenodd" transform=""></path></svg>                <span class="screen-reader-text">Search</span>
       |            </button>
       |        </form>
       |    </div>
       |</section>
       |
       |            </div>
       |            <div class="header-service">
       |				<a href="https://shop.schweizermonat.ch/" class="cta-button cta-button__header" data-octavius="is-header-abo-button">Mitgliedschaft</a>
       |                <a href="https://schweizermonat.ch/newsletter/" class="cta-button__header header-newsletter-link"><svg height="32" viewBox="0 0 32 32" width="32" xmlns="http://www.w3.org/2000/svg"><path d="m20.6381516 20-2.2175822 1.9827889.0993775-.0005185-.0199469.0177306h-3.3785069l1.6966971-.0088521-2.2671862-.010839-2.2443512-1.9803099zm1.1184157-1 3.2434327-2.9000244v-8.09134983c0-.55704863-.4547089-1.00862577-.9999602-1.00862577h-15.0000796c-.55226277 0-.9999602.44373571-.9999602 1.00207596v8.19793624l3.1733195 2.7999878zm-8.6656582-13 3.4090909-3 3.4090909 3h4.0934872c1.1064965 0 1.9974219.89525812 1.9974219 1.99961498v3.36038502l3 2.64v14.0059397c0 1.0995465-.89704 1.9940603-2.0035949 1.9940603h-20.99281024c-1.11383162 0-2.00359486-.8927712-2.00359486-1.9940603v-14.0059397l3-2.64v-3.36038502c0-1.11218169.89427625-1.99961498 1.99742191-1.99961498zm5.3369258 0-1.9278349-1.69999981-1.9278349 1.69999981zm7.5721651 6.6772727 1.5 1.3227273-1.5 1.3333335zm-19 2.6560608-1.5-1.3333335 1.5-1.3227272zm6.5 7.6666665-7 6h20l-7-6zm14.18514 5.7251701-7.68514-6.7080803 8-7.0170898v6.5000199 6.5000199c0 .2831139-.1210051.5418171-.31486.7251303zm-22.37028 0 7.68514-6.7080803-8-7.0170898v6.5000199 6.5000199c0 .2831139.12100514.5418171.31486.7251303zm4.68514-18.7251701v1h13v-1zm0 3v1h13v-1zm0 3v1h13v-1z" fill-rule="evenodd"></path></svg>                    Newsletter</a>
       |
       |
       |<section class="site-login">
       |
       |
       |        <a href="#" class="menu-link login-link__button" id="header-login">Login</a>
       |
       |        <form method="post" class="login-form " id="login-layer">
       |
       |            <div class="field-wrapper">
       |                <label for="login-username" class="login-label">Benutzername</label>
       |                <input name="edp_username" value="" type="text" class="login-input login-input__username" placeholder="Benutzern@me">
       |            </div>
       |
       |            <div class="field-wrapper">
       |                <label for="login-password">Passwort</label>
       |                <input name="edp_password" value="" type="password" class="login-input login-input__password" placeholder="Passwort">
       |            </div>
       |
       |            <div class="field-wrapper">
       |                <input name="edp_keep_me_logged_in" value="yes" type="checkbox" id="remember-login">
       |                <label for="remember-login" class="">Login merken
       |                </label>
       |            </div>
       |
       |
       |            <button name="flexwall_lets_login" class="button--green">Anmelden</button>
       |
       |            <a href="https://shop.schweizermonat.ch/?login" class="login__lost-password">Passwort
       |                vergessen</a>
       |
       |            <hr>
       |            <p class="login-form__text">
       |                Sie haben bereits ein Abo, aber noch keinen Benutzernamen und Passwort? <br>
       |                Dann können Sie das <a href="https://shop.schweizermonat.ch/?Registration/mit_Abo">hier ändern</a>.
       |            </p>
       |            <hr>
       |
       |            <p class="login-form__text">
       |                Sie haben noch kein Abo? <br> <a href="https://shop.schweizermonat.ch/">Abo jetzt
       |                    hier
       |                    abschliessen</a>
       |            </p>
       |
       |        </form>
       |
       |
       |</section>
       |
       |
       |            </div>
       |
       |
       |        <nav class="site-nav">
       |            <div id="site-navigation" class="main-navigation">
       |				<ul id="header-menu" class="header-menu"><li id="menu-item-47" class="menu-item menu-item-type-post_type menu-item-object-page menu-item-47"><a href="https://schweizermonat.ch/ausgaben/">Ausgaben</a></li>
       |<li id="menu-item-53599" class="menu-item menu-item-type-post_type menu-item-object-page menu-item-53599"><a href="https://schweizermonat.ch/dossiers/">Dossiers</a></li>
       |<li id="menu-item-53617" class="menu-item menu-item-type-post_type menu-item-object-page menu-item-53617"><a href="https://schweizermonat.ch/schwerpunkte/">Schwerpunkte</a></li>
       |<li id="menu-item-60857" class="menu-item menu-item-type-post_type menu-item-object-page menu-item-60857"><a href="https://schweizermonat.ch/kultur/">Kultur</a></li>
       |<li id="menu-item-79221" class="menu-item menu-item-type-post_type menu-item-object-page menu-item-79221"><a href="https://schweizermonat.ch/kolumnen/">Kolumnen</a></li>
       |<li id="menu-item-68379" class="menu-item menu-item-type-post_type menu-item-object-page menu-item-68379"><a href="https://schweizermonat.ch/video/">Studio Schweizer Monat</a></li>
       |<li id="menu-item-65293" class="menu-item menu-item-type-post_type menu-item-object-page menu-item-65293"><a href="https://schweizermonat.ch/apero/">Apéro</a></li>
       |<li id="menu-item-51" class="menu-item menu-item-type-post_type menu-item-object-page menu-item-51"><a href="https://schweizermonat.ch/autoren/">Autoren</a></li>
       |<li id="menu-item-40195" class="menu-item menu-item-type-post_type menu-item-object-page menu-item-40195"><a href="https://schweizermonat.ch/ueber-den-schweizermonat/">Über</a></li>
       |<li id="menu-item-73713" class="menu-item menu-item-type-custom menu-item-object-custom menu-item-73713"><a href="https://literarischermonat.ch/archiv/">Literarischer Monat</a></li>
       |</ul>            </div><!-- #site-navigation -->
       |        </nav>
       |    </div>
       |</header><!-- #masthead -->
       |
       |<div id="content" class="site-content">
       |
       |
       |    <div id="primary" class="content-area">
       |        <main id="main" class="site-main">
       |
       |
       |            <div class="issue-page__content">
       |
       |                <header class="page-header">
       |                    <h1 class="page-title">
       |                        «Der Aufbruch von 1848»                    </h1>
       |		            <div class="archive-description"><p>«Von den revolutionären Erhebungen, die 1848 in vielen europäischen Ländern zu Umwälzungen führten, war die Schweiz die einzige, in der die liberale und nationale Bewegung erfolgreich war.» Nina Tannenwald</p>
       |</div>                </header><!-- .page-header -->
       |
       |				<!--fwp-loop-->
       |<article id="post-85693" class="teaser teaser--list post-85693 post type-post status-publish format-standard has-post-thumbnail hentry category-dossier tag-30779 tag-4117 tag-august-2023 tag-der-letzte-auftritt-des-alten-europas tag-dossier tag-juli-2023 tag-volker-reinhardt entry-header-portrait dossier-der-aufbruch-von-1848 issue-ausgabe-1108-juli-2023">
       |
       |
       |    <figure class="teaser__thumbnail">
       |        <a href="https://schweizermonat.ch/der-letzte-auftritt-des-alten-europas/" class="teaser__link teaser__image-link u-uid  u-url" title="Der letzte Auftritt des  alten Europas">
       |			<img width="2003" height="2277" src="https://schweizermonat.ch/wp-content/uploads/2023/06/3_Volker-Reinhardt-zvg.jpg" class="attachment-original size-original" alt="" decoding="async" loading="lazy" srcset="https://schweizermonat.ch/wp-content/uploads/2023/06/3_Volker-Reinhardt-zvg.jpg 2003w, https://schweizermonat.ch/wp-content/uploads/2023/06/3_Volker-Reinhardt-zvg-264x300.jpg 264w, https://schweizermonat.ch/wp-content/uploads/2023/06/3_Volker-Reinhardt-zvg-901x1024.jpg 901w, https://schweizermonat.ch/wp-content/uploads/2023/06/3_Volker-Reinhardt-zvg-768x873.jpg 768w, https://schweizermonat.ch/wp-content/uploads/2023/06/3_Volker-Reinhardt-zvg-1351x1536.jpg 1351w, https://schweizermonat.ch/wp-content/uploads/2023/06/3_Volker-Reinhardt-zvg-1802x2048.jpg 1802w, https://schweizermonat.ch/wp-content/uploads/2023/06/3_Volker-Reinhardt-zvg-13x15.jpg 13w, https://schweizermonat.ch/wp-content/uploads/2023/06/3_Volker-Reinhardt-zvg-132x150.jpg 132w, https://schweizermonat.ch/wp-content/uploads/2023/06/3_Volker-Reinhardt-zvg-281x320.jpg 281w, https://schweizermonat.ch/wp-content/uploads/2023/06/3_Volker-Reinhardt-zvg-660x750.jpg 660w, https://schweizermonat.ch/wp-content/uploads/2023/06/3_Volker-Reinhardt-zvg-1056x1200.jpg 1056w, https://schweizermonat.ch/wp-content/uploads/2023/06/3_Volker-Reinhardt-zvg-600x682.jpg 600w" sizes="(max-width: 2003px) 100vw, 2003px">
       |
       |        </a>
       |        <figcaption class="teaser__thumbnail-caption">
       |			Volker Reinhardt, zvg.        </figcaption>
       |    </figure>
       |
       |    <div class="teaser__inner">
       |        <header class="teaser__header">
       |
       |            <div class="teaser__meta">
       |
       |    <a href="https://schweizermonat.ch/dossier/der-aufbruch-von-1848/" class="teaser__meta-link">
       |        Dossier: «Der Aufbruch von 1848»    </a><br>
       |
       |
       |    <a href="https://schweizermonat.ch/issue/ausgabe-1108-juli-2023/" class="teaser__meta-link">
       |		Ausgabe 1108 – Juli / August 2023    </a>
       |
       |	            </div>
       |
       |            <span class="teaser__headline">
       |                <a href="https://schweizermonat.ch/der-letzte-auftritt-des-alten-europas/" class="teaser__link" title="Der letzte Auftritt des <br /> alten Europas">
       |			            Der letzte Auftritt des <br> alten Europas                    </a>
       |            </span>
       |        </header>
       |        <div class="teaser__body">
       |            <div class="teaser__excerpt">
       |			    <p>In Deutschland scheiterte die Revolution 1848 an ihren tiefen Spaltungen. In der Schweiz hingegen gelang es den Freisinnigen, die Dominanz einer neuen Elite durchzusetzen. Erst 1874 und 1891 brachten der breiteren Bevölkerung aktive Mitbestimmung.</p>
       |            </div>
       |        </div>
       |        <footer class="teaser__footer">
       |	        <div class="teaser__meta--author">
       |
       |<div class="meta-authors">
       |
       |
       |
       |        <span class="meta-author__prefix">
       |             von         </span>
       |
       |        <a href="https://schweizermonat.ch/author/volkerreinhardt/" class="meta-author vcard">Volker Reinhardt</a>
       |
       |</div>
       |</div>	        <span class="teaser__reading-time">6 Minuten Lesezeit</span>        </footer>
       |    </div>
       |
       |
       |</article>
       |<article id="post-85711" class="teaser teaser--list post-85711 post type-post status-publish format-standard has-post-thumbnail hentry category-dossier tag-30779 tag-4117 tag-august-2023 tag-bundesstaatsgruendung tag-daniel-speich-chasse tag-der-aufbruch-von-1848 tag-die-bundesstaatsgruendung-war-nicht-der-grosse-bruch tag-dossier tag-juli-2023 tag-schweiz tag-verfassunf entry-header-portrait dossier-der-aufbruch-von-1848 issue-ausgabe-1108-juli-2023">
       |
       |
       |    <figure class="teaser__thumbnail">
       |        <a href="https://schweizermonat.ch/die-bundesstaatsgruendung-war-nicht-der-grosse-bruch/" class="teaser__link teaser__image-link u-uid  u-url" title="Die Bundesstaatsgründung war nicht der grosse Bruch">
       |			<img width="2685" height="3797" src="https://schweizermonat.ch/wp-content/uploads/2023/06/68_Daniel-Speich-Chass-zvg.jpg" class="attachment-original size-original" alt="" decoding="async" loading="lazy" srcset="https://schweizermonat.ch/wp-content/uploads/2023/06/68_Daniel-Speich-Chass-zvg.jpg 2685w, https://schweizermonat.ch/wp-content/uploads/2023/06/68_Daniel-Speich-Chass-zvg-212x300.jpg 212w, https://schweizermonat.ch/wp-content/uploads/2023/06/68_Daniel-Speich-Chass-zvg-724x1024.jpg 724w, https://schweizermonat.ch/wp-content/uploads/2023/06/68_Daniel-Speich-Chass-zvg-768x1086.jpg 768w, https://schweizermonat.ch/wp-content/uploads/2023/06/68_Daniel-Speich-Chass-zvg-1086x1536.jpg 1086w, https://schweizermonat.ch/wp-content/uploads/2023/06/68_Daniel-Speich-Chass-zvg-1448x2048.jpg 1448w, https://schweizermonat.ch/wp-content/uploads/2023/06/68_Daniel-Speich-Chass-zvg-20x27.jpg 20w, https://schweizermonat.ch/wp-content/uploads/2023/06/68_Daniel-Speich-Chass-zvg-11x15.jpg 11w, https://schweizermonat.ch/wp-content/uploads/2023/06/68_Daniel-Speich-Chass-zvg-106x150.jpg 106w, https://schweizermonat.ch/wp-content/uploads/2023/06/68_Daniel-Speich-Chass-zvg-226x320.jpg 226w, https://schweizermonat.ch/wp-content/uploads/2023/06/68_Daniel-Speich-Chass-zvg-530x750.jpg 530w, https://schweizermonat.ch/wp-content/uploads/2023/06/68_Daniel-Speich-Chass-zvg-849x1200.jpg 849w, https://schweizermonat.ch/wp-content/uploads/2023/06/68_Daniel-Speich-Chass-zvg-600x848.jpg 600w" sizes="(max-width: 2685px) 100vw, 2685px">
       |
       |        </a>
       |        <figcaption class="teaser__thumbnail-caption">
       |			Daniel Speich Chassé, zvg.        </figcaption>
       |    </figure>
       |
       |    <div class="teaser__inner">
       |        <header class="teaser__header">
       |
       |            <div class="teaser__meta">
       |
       |    <a href="https://schweizermonat.ch/dossier/der-aufbruch-von-1848/" class="teaser__meta-link">
       |        Dossier: «Der Aufbruch von 1848»    </a><br>
       |
       |
       |    <a href="https://schweizermonat.ch/issue/ausgabe-1108-juli-2023/" class="teaser__meta-link">
       |		Ausgabe 1108 – Juli / August 2023    </a>
       |
       |	            </div>
       |
       |            <span class="teaser__headline">
       |                <a href="https://schweizermonat.ch/die-bundesstaatsgruendung-war-nicht-der-grosse-bruch/" class="teaser__link" title="Die Bundesstaatsgründung war nicht der grosse Bruch">
       |			            Die Bundesstaatsgründung war nicht der grosse Bruch                    </a>
       |            </span>
       |        </header>
       |        <div class="teaser__body">
       |            <div class="teaser__excerpt">
       |			    <p>Auch nach 1848 blieben die Kantone die Hauptakteure. Der Bund war bis zum Ersten Weltkrieg für die Modernisierung der Schweiz wenig relevant.</p>
       |            </div>
       |        </div>
       |        <footer class="teaser__footer">
       |	        <div class="teaser__meta--author">
       |
       |<div class="meta-authors">
       |
       |
       |
       |        <span class="meta-author__prefix">
       |             von         </span>
       |
       |        <a href="https://schweizermonat.ch/author/danielspeichchasse/" class="meta-author vcard">Daniel Speich Chassé</a>
       |
       |</div>
       |</div>	        <span class="teaser__reading-time">5 Minuten Lesezeit</span>        </footer>
       |    </div>
       |
       |
       |</article>
       |<article id="post-85769" class="teaser teaser--list post-85769 post type-post status-publish format-standard has-post-thumbnail hentry category-dossier tag-30779 tag-4117 tag-august-2023 tag-bundesstaat-schweiz tag-der-weg-zum-bundesstaat tag-dossier tag-juli-2023 tag-schweiz tag-selina-seiler entry-header-portrait dossier-der-aufbruch-von-1848 issue-ausgabe-1108-juli-2023">
       |
       |
       |    <figure class="teaser__thumbnail">
       |        <a href="https://schweizermonat.ch/der-weg-zum-bundesstaat/" class="teaser__link teaser__image-link u-uid  u-url" title="Der Weg zum Bundesstaat">
       |			<img width="5153" height="3575" src="https://schweizermonat.ch/wp-content/uploads/2023/06/71_1848_low.jpg" class="attachment-original size-original" alt="" decoding="async" loading="lazy" srcset="https://schweizermonat.ch/wp-content/uploads/2023/06/71_1848_low.jpg 5153w, https://schweizermonat.ch/wp-content/uploads/2023/06/71_1848_low-300x208.jpg 300w, https://schweizermonat.ch/wp-content/uploads/2023/06/71_1848_low-1024x710.jpg 1024w, https://schweizermonat.ch/wp-content/uploads/2023/06/71_1848_low-768x533.jpg 768w, https://schweizermonat.ch/wp-content/uploads/2023/06/71_1848_low-1536x1066.jpg 1536w, https://schweizermonat.ch/wp-content/uploads/2023/06/71_1848_low-2048x1421.jpg 2048w, https://schweizermonat.ch/wp-content/uploads/2023/06/71_1848_low-27x20.jpg 27w, https://schweizermonat.ch/wp-content/uploads/2023/06/71_1848_low-15x10.jpg 15w, https://schweizermonat.ch/wp-content/uploads/2023/06/71_1848_low-150x104.jpg 150w, https://schweizermonat.ch/wp-content/uploads/2023/06/71_1848_low-320x222.jpg 320w, https://schweizermonat.ch/wp-content/uploads/2023/06/71_1848_low-750x520.jpg 750w, https://schweizermonat.ch/wp-content/uploads/2023/06/71_1848_low-1200x833.jpg 1200w, https://schweizermonat.ch/wp-content/uploads/2023/06/71_1848_low-600x416.jpg 600w" sizes="(max-width: 5153px) 100vw, 5153px">
       |
       |        </a>
       |        <figcaption class="teaser__thumbnail-caption">
       |			Ausschnitt aus dem Gedenkblatt, das anlässlich der Einführung der Bundesverfassung
       |vom 12. September 1848 erschien. Lithografie von C. Studer,
       |Winterthur,
       |gedruckt von J. J. Ulrich, Zürich. Bild: Burgerbibliothek Bern.        </figcaption>
       |    </figure>
       |
       |    <div class="teaser__inner">
       |        <header class="teaser__header">
       |
       |            <div class="teaser__meta">
       |
       |    <a href="https://schweizermonat.ch/dossier/der-aufbruch-von-1848/" class="teaser__meta-link">
       |        Dossier: «Der Aufbruch von 1848»    </a><br>
       |
       |
       |    <a href="https://schweizermonat.ch/issue/ausgabe-1108-juli-2023/" class="teaser__meta-link">
       |		Ausgabe 1108 – Juli / August 2023    </a>
       |
       |	            </div>
       |
       |            <span class="teaser__headline">
       |                <a href="https://schweizermonat.ch/der-weg-zum-bundesstaat/" class="teaser__link" title="Der Weg zum Bundesstaat">
       |			            Der Weg zum Bundesstaat                    </a>
       |            </span>
       |        </header>
       |        <div class="teaser__body">
       |            <div class="teaser__excerpt">
       |			    <p>Innere Spaltungen und Druck von aussen prägten die Entwicklung von einem losen Staatenbund zur liberalen Demokratie: eine Chronologie.</p>
       |            </div>
       |        </div>
       |        <footer class="teaser__footer">
       |	        <div class="teaser__meta--author">
       |
       |<div class="meta-authors">
       |
       |
       |
       |        <span class="meta-author__prefix">
       |             von         </span>
       |
       |        <a href="https://schweizermonat.ch/author/selinaseiler/" class="meta-author vcard">Selina Seiler</a>
       |
       |</div>
       |</div>	        <span class="teaser__reading-time">3 Minuten Lesezeit</span>        </footer>
       |    </div>
       |
       |
       |</article>
       |<article id="post-85725" class="teaser teaser--list post-85725 post type-post status-publish format-standard has-post-thumbnail hentry category-dossier tag-30779 tag-4117 tag-august-2023 tag-clemens-faessler tag-der-aufbruch-von-1848 tag-im-schnellzug-zum-fortschritt tag-juli-2023 tag-pioniere tag-textil-zur-maschinenindustrie tag-unternehmen entry-header-portrait dossier-der-aufbruch-von-1848 issue-ausgabe-1108-juli-2023">
       |
       |
       |    <figure class="teaser__thumbnail">
       |        <a href="https://schweizermonat.ch/im-schnellzug-zum-fortschritt/" class="teaser__link teaser__image-link u-uid  u-url" title="Im Schnellzug zum Fortschritt">
       |			<img width="899" height="899" src="https://schweizermonat.ch/wp-content/uploads/2023/06/73_Titelkarte_FA╠a┬nssler_nur-Bild.jpg" class="attachment-original size-original" alt="" decoding="async" loading="lazy" srcset="https://schweizermonat.ch/wp-content/uploads/2023/06/73_Titelkarte_FA╠a┬nssler_nur-Bild.jpg 899w, https://schweizermonat.ch/wp-content/uploads/2023/06/73_Titelkarte_FA╠a┬nssler_nur-Bild-300x300.jpg 300w, https://schweizermonat.ch/wp-content/uploads/2023/06/73_Titelkarte_FA╠a┬nssler_nur-Bild-150x150.jpg 150w, https://schweizermonat.ch/wp-content/uploads/2023/06/73_Titelkarte_FA╠a┬nssler_nur-Bild-768x768.jpg 768w, https://schweizermonat.ch/wp-content/uploads/2023/06/73_Titelkarte_FA╠a┬nssler_nur-Bild-30x30.jpg 30w, https://schweizermonat.ch/wp-content/uploads/2023/06/73_Titelkarte_FA╠a┬nssler_nur-Bild-500x500.jpg 500w, https://schweizermonat.ch/wp-content/uploads/2023/06/73_Titelkarte_FA╠a┬nssler_nur-Bild-750x750.jpg 750w, https://schweizermonat.ch/wp-content/uploads/2023/06/73_Titelkarte_FA╠a┬nssler_nur-Bild-15x15.jpg 15w, https://schweizermonat.ch/wp-content/uploads/2023/06/73_Titelkarte_FA╠a┬nssler_nur-Bild-320x320.jpg 320w, https://schweizermonat.ch/wp-content/uploads/2023/06/73_Titelkarte_FA╠a┬nssler_nur-Bild-600x600.jpg 600w, https://schweizermonat.ch/wp-content/uploads/2023/06/73_Titelkarte_FA╠a┬nssler_nur-Bild-100x100.jpg 100w" sizes="(max-width: 899px) 100vw, 899px">
       |
       |        </a>
       |        <figcaption class="teaser__thumbnail-caption">
       |			Clemens Fässler, fotografiert von Hanna Wenger.        </figcaption>
       |    </figure>
       |
       |    <div class="teaser__inner">
       |        <header class="teaser__header">
       |
       |            <div class="teaser__meta">
       |
       |    <a href="https://schweizermonat.ch/dossier/der-aufbruch-von-1848/" class="teaser__meta-link">
       |        Dossier: «Der Aufbruch von 1848»    </a><br>
       |
       |
       |    <a href="https://schweizermonat.ch/issue/ausgabe-1108-juli-2023/" class="teaser__meta-link">
       |		Ausgabe 1108 – Juli / August 2023    </a>
       |
       |	            </div>
       |
       |            <span class="teaser__headline">
       |                <a href="https://schweizermonat.ch/im-schnellzug-zum-fortschritt/" class="teaser__link" title="Im Schnellzug zum Fortschritt">
       |			            Im Schnellzug zum Fortschritt                    </a>
       |            </span>
       |        </header>
       |        <div class="teaser__body">
       |            <div class="teaser__excerpt">
       |			    <p>Der heutige Wohlstand lässt vergessen, dass die Schweiz vor 1848 enormen Aufholbedarf hatte. Es war die Eisenbahn, die aus der Armut und in die Freiheit führte.</p>
       |            </div>
       |        </div>
       |        <footer class="teaser__footer">
       |	        <div class="teaser__meta--author">
       |
       |<div class="meta-authors">
       |
       |
       |
       |        <span class="meta-author__prefix">
       |             von         </span>
       |
       |        <a href="https://schweizermonat.ch/author/clemensfaessler/" class="meta-author vcard">Clemens Fässler</a>
       |
       |</div>
       |</div>	        <span class="teaser__reading-time">6 Minuten Lesezeit</span>        </footer>
       |    </div>
       |
       |
       |</article>
       |<article id="post-85733" class="teaser teaser--list post-85733 post type-post status-publish format-standard has-post-thumbnail hentry category-dossier tag-30779 tag-4117 tag-august-2023 tag-der-aufbruch-von-1848 tag-dossier tag-juli-2023 tag-verdanken-wir-das-referendum-dem-gotthard entry-header-portrait dossier-der-aufbruch-von-1848 issue-ausgabe-1108-juli-2023">
       |
       |
       |    <figure class="teaser__thumbnail">
       |        <a href="https://schweizermonat.ch/verdanken-wir-das-referendum-dem-gotthard/" class="teaser__link teaser__image-link u-uid  u-url" title="Verdanken wir das Referendum dem Gotthard?">
       |			<img width="454" height="454" src="https://schweizermonat.ch/wp-content/uploads/2023/06/75_Alexandre-Zindel-zvg.jpg" class="attachment-original size-original" alt="" decoding="async" loading="lazy" srcset="https://schweizermonat.ch/wp-content/uploads/2023/06/75_Alexandre-Zindel-zvg.jpg 454w, https://schweizermonat.ch/wp-content/uploads/2023/06/75_Alexandre-Zindel-zvg-300x300.jpg 300w, https://schweizermonat.ch/wp-content/uploads/2023/06/75_Alexandre-Zindel-zvg-150x150.jpg 150w, https://schweizermonat.ch/wp-content/uploads/2023/06/75_Alexandre-Zindel-zvg-30x30.jpg 30w, https://schweizermonat.ch/wp-content/uploads/2023/06/75_Alexandre-Zindel-zvg-15x15.jpg 15w, https://schweizermonat.ch/wp-content/uploads/2023/06/75_Alexandre-Zindel-zvg-320x320.jpg 320w, https://schweizermonat.ch/wp-content/uploads/2023/06/75_Alexandre-Zindel-zvg-100x100.jpg 100w" sizes="(max-width: 454px) 100vw, 454px">
       |
       |        </a>
       |        <figcaption class="teaser__thumbnail-caption">
       |			Alexandre Zindel, zvg.        </figcaption>
       |    </figure>
       |
       |    <div class="teaser__inner">
       |        <header class="teaser__header">
       |
       |            <div class="teaser__meta">
       |
       |    <a href="https://schweizermonat.ch/dossier/der-aufbruch-von-1848/" class="teaser__meta-link">
       |        Dossier: «Der Aufbruch von 1848»    </a><br>
       |
       |
       |    <a href="https://schweizermonat.ch/issue/ausgabe-1108-juli-2023/" class="teaser__meta-link">
       |		Ausgabe 1108 – Juli / August 2023    </a>
       |
       |	            </div>
       |
       |            <span class="teaser__headline">
       |                <a href="https://schweizermonat.ch/verdanken-wir-das-referendum-dem-gotthard/" class="teaser__link" title="Verdanken wir das Referendum dem Gotthard?">
       |			            Verdanken wir das Referendum dem Gotthard?                    </a>
       |            </span>
       |        </header>
       |        <div class="teaser__body">
       |            <div class="teaser__excerpt">
       |			    <p>Die erste Revision der Bundesverfassung brachte 1874 den Durchbruch für die direkte Demokratie. Womöglich war sie das Ergebnis eines Kuhhandels.</p>
       |            </div>
       |        </div>
       |        <footer class="teaser__footer">
       |	        <div class="teaser__meta--author">
       |
       |<div class="meta-authors">
       |
       |
       |
       |        <span class="meta-author__prefix">
       |             von         </span>
       |
       |        <a href="https://schweizermonat.ch/author/alexandrezindel/" class="meta-author vcard">Alexandre Zindel</a>
       |
       |</div>
       |</div>	        <span class="teaser__reading-time">5 Minuten Lesezeit</span>        </footer>
       |    </div>
       |
       |
       |</article>
       |<article id="post-85755" class="teaser teaser--list post-85755 post type-post status-publish format-standard has-post-thumbnail hentry category-dossier tag-30779 tag-4117 tag-august-2023 tag-die-revolution-von-1848-ermoeglichte-die-genfer-konvention tag-genf tag-genfer-konvention tag-juli-2023 tag-krieg tag-nina-tannenwald tag-rotes-kreuz tag-schweiz entry-header-portrait dossier-der-aufbruch-von-1848 issue-ausgabe-1108-juli-2023">
       |
       |
       |    <figure class="teaser__thumbnail">
       |        <a href="https://schweizermonat.ch/die-revolution-von-1848-ermoeglichte-die-genfer-konvention/" class="teaser__link teaser__image-link u-uid  u-url" title="Die Revolution von 1848  ermöglichte die Genfer Konvention">
       |			<img width="1970" height="2786" src="https://schweizermonat.ch/wp-content/uploads/2023/06/77_Nina-Tannenwald-zvg-1.jpg" class="attachment-original size-original" alt="" decoding="async" loading="lazy" srcset="https://schweizermonat.ch/wp-content/uploads/2023/06/77_Nina-Tannenwald-zvg-1.jpg 1970w, https://schweizermonat.ch/wp-content/uploads/2023/06/77_Nina-Tannenwald-zvg-1-212x300.jpg 212w, https://schweizermonat.ch/wp-content/uploads/2023/06/77_Nina-Tannenwald-zvg-1-724x1024.jpg 724w, https://schweizermonat.ch/wp-content/uploads/2023/06/77_Nina-Tannenwald-zvg-1-768x1086.jpg 768w, https://schweizermonat.ch/wp-content/uploads/2023/06/77_Nina-Tannenwald-zvg-1-1086x1536.jpg 1086w, https://schweizermonat.ch/wp-content/uploads/2023/06/77_Nina-Tannenwald-zvg-1-1448x2048.jpg 1448w, https://schweizermonat.ch/wp-content/uploads/2023/06/77_Nina-Tannenwald-zvg-1-20x27.jpg 20w, https://schweizermonat.ch/wp-content/uploads/2023/06/77_Nina-Tannenwald-zvg-1-11x15.jpg 11w, https://schweizermonat.ch/wp-content/uploads/2023/06/77_Nina-Tannenwald-zvg-1-106x150.jpg 106w, https://schweizermonat.ch/wp-content/uploads/2023/06/77_Nina-Tannenwald-zvg-1-226x320.jpg 226w, https://schweizermonat.ch/wp-content/uploads/2023/06/77_Nina-Tannenwald-zvg-1-530x750.jpg 530w, https://schweizermonat.ch/wp-content/uploads/2023/06/77_Nina-Tannenwald-zvg-1-849x1200.jpg 849w, https://schweizermonat.ch/wp-content/uploads/2023/06/77_Nina-Tannenwald-zvg-1-600x849.jpg 600w" sizes="(max-width: 1970px) 100vw, 1970px">
       |
       |        </a>
       |        <figcaption class="teaser__thumbnail-caption">
       |			Nina Tannenwald, zvg.        </figcaption>
       |    </figure>
       |
       |    <div class="teaser__inner">
       |        <header class="teaser__header">
       |
       |            <div class="teaser__meta">
       |
       |    <a href="https://schweizermonat.ch/dossier/der-aufbruch-von-1848/" class="teaser__meta-link">
       |        Dossier: «Der Aufbruch von 1848»    </a><br>
       |
       |
       |    <a href="https://schweizermonat.ch/issue/ausgabe-1108-juli-2023/" class="teaser__meta-link">
       |		Ausgabe 1108 – Juli / August 2023    </a>
       |
       |	            </div>
       |
       |            <span class="teaser__headline">
       |                <a href="https://schweizermonat.ch/die-revolution-von-1848-ermoeglichte-die-genfer-konvention/" class="teaser__link" title="Die Revolution von 1848 <br /> ermöglichte die Genfer Konvention">
       |			            Die Revolution von 1848 <br> ermöglichte die Genfer Konvention                    </a>
       |            </span>
       |        </header>
       |        <div class="teaser__body">
       |            <div class="teaser__excerpt">
       |			    <p>Kurz nach der Bundesstaatsgründung wurde das Rote Kreuz ins Leben gerufen. Dass die Organisation in der Schweiz entstand, ist kein Zufall.</p>
       |            </div>
       |        </div>
       |        <footer class="teaser__footer">
       |	        <div class="teaser__meta--author">
       |
       |<div class="meta-authors">
       |
       |
       |
       |        <span class="meta-author__prefix">
       |             von         </span>
       |
       |        <a href="https://schweizermonat.ch/author/ninatannenwald/" class="meta-author vcard">Nina Tannenwald</a>
       |
       |</div>
       |</div>	        <span class="teaser__reading-time">7 Minuten Lesezeit</span>        </footer>
       |    </div>
       |
       |
       |</article>
       |<article id="post-85743" class="teaser teaser--list post-85743 post type-post status-publish format-standard has-post-thumbnail hentry category-dossier category-im-orginial tag-4117 tag-august-2023 tag-calvinist tag-der-aufbruch-von-1848 tag-geneva tag-juli-2023 tag-nina-tannenwald tag-swiss tag-switzerland entry-header-portrait dossier-der-aufbruch-von-1848 issue-ausgabe-1108-juli-2023">
       |
       |
       |    <figure class="teaser__thumbnail">
       |        <a href="https://schweizermonat.ch/how-the-revolution-of-1848-enabled-the-geneva-conventions/" class="teaser__link teaser__image-link u-uid  u-url" title="How the Revolution of 1848  Enabled the Geneva Conventions">
       |			<img width="1970" height="2786" src="https://schweizermonat.ch/wp-content/uploads/2023/06/77_Nina-Tannenwald-zvg-1.jpg" class="attachment-original size-original" alt="" decoding="async" loading="lazy" srcset="https://schweizermonat.ch/wp-content/uploads/2023/06/77_Nina-Tannenwald-zvg-1.jpg 1970w, https://schweizermonat.ch/wp-content/uploads/2023/06/77_Nina-Tannenwald-zvg-1-212x300.jpg 212w, https://schweizermonat.ch/wp-content/uploads/2023/06/77_Nina-Tannenwald-zvg-1-724x1024.jpg 724w, https://schweizermonat.ch/wp-content/uploads/2023/06/77_Nina-Tannenwald-zvg-1-768x1086.jpg 768w, https://schweizermonat.ch/wp-content/uploads/2023/06/77_Nina-Tannenwald-zvg-1-1086x1536.jpg 1086w, https://schweizermonat.ch/wp-content/uploads/2023/06/77_Nina-Tannenwald-zvg-1-1448x2048.jpg 1448w, https://schweizermonat.ch/wp-content/uploads/2023/06/77_Nina-Tannenwald-zvg-1-20x27.jpg 20w, https://schweizermonat.ch/wp-content/uploads/2023/06/77_Nina-Tannenwald-zvg-1-11x15.jpg 11w, https://schweizermonat.ch/wp-content/uploads/2023/06/77_Nina-Tannenwald-zvg-1-106x150.jpg 106w, https://schweizermonat.ch/wp-content/uploads/2023/06/77_Nina-Tannenwald-zvg-1-226x320.jpg 226w, https://schweizermonat.ch/wp-content/uploads/2023/06/77_Nina-Tannenwald-zvg-1-530x750.jpg 530w, https://schweizermonat.ch/wp-content/uploads/2023/06/77_Nina-Tannenwald-zvg-1-849x1200.jpg 849w, https://schweizermonat.ch/wp-content/uploads/2023/06/77_Nina-Tannenwald-zvg-1-600x849.jpg 600w" sizes="(max-width: 1970px) 100vw, 1970px">
       |
       |        </a>
       |        <figcaption class="teaser__thumbnail-caption">
       |			Nina Tannenwald, zvg.        </figcaption>
       |    </figure>
       |
       |    <div class="teaser__inner">
       |        <header class="teaser__header">
       |
       |            <div class="teaser__meta">
       |
       |    <a href="https://schweizermonat.ch/dossier/der-aufbruch-von-1848/" class="teaser__meta-link">
       |        Dossier: «Der Aufbruch von 1848»    </a><br>
       |
       |
       |    <a href="https://schweizermonat.ch/issue/ausgabe-1108-juli-2023/" class="teaser__meta-link">
       |		Ausgabe 1108 – Juli / August 2023    </a>
       |
       |	            </div>
       |
       |            <span class="teaser__headline">
       |                <a href="https://schweizermonat.ch/how-the-revolution-of-1848-enabled-the-geneva-conventions/" class="teaser__link" title="How the Revolution of 1848 <br /> Enabled the Geneva Conventions">
       |			            How the Revolution of 1848 <br> Enabled the Geneva Conventions                    </a>
       |            </span>
       |        </header>
       |        <div class="teaser__body">
       |            <div class="teaser__excerpt">
       |			                </div>
       |        </div>
       |        <footer class="teaser__footer">
       |	        <div class="teaser__meta--author">
       |
       |<div class="meta-authors">
       |
       |
       |
       |        <span class="meta-author__prefix">
       |             von         </span>
       |
       |        <a href="https://schweizermonat.ch/author/ninatannenwald/" class="meta-author vcard">Nina Tannenwald</a>
       |
       |</div>
       |</div>	        <span class="teaser__reading-time">7 Minuten Lesezeit</span>        </footer>
       |    </div>
       |
       |
       |</article>
       |
       |            </div>
       |        </main><!-- #main -->
       |    </div><!-- #primary -->
       |
       |
       |
       |    <footer id="colophon" class="site-footer">
       |
       |    <section class="site-signature">
       |        <div class="site-container">
       |            <div class="footer-column">
       |				<div class="footer-logo">
       |	<svg height="39" viewBox="0 0 145 39" width="145" xmlns="http://www.w3.org/2000/svg"><path d="M125.342 14.323h-3.276v4.705h3.276v13.514c0 4.244 2.538 6.458 7.29 6.458 1.57 0 3.23-.323 5.63-1.153l.231-4.567c-1.2.461-2.26.738-3.138.738-2.676 0-3.553-.83-3.553-4.243V19.028h6.46v-4.705h-6.46v-5.35l-6.46.368zm-10.886 13.883c0 4.429-2.739 6.09-5.478 6.09-2.69 0-3.766-1.477-3.766-2.907 0-2.075 1.761-3.183 7.337-3.183zm.098-3.966c-4.989 0-8.462.23-11.054 1.107-2.69.922-5.136 3.182-5.136 6.642 0 3.782 2.788 7.011 8.56 7.011 4.305 0 6.652-1.706 8.364-5.35h.098c-.245.922-.343 1.753-.343 3.32v1.477h8.609V33.74h-2.886c.098-3.413.244-6.734.244-10.7 0-7.011-4.352-9.272-11.2-9.272-4.55 0-8.56 1.615-10.223 2.491v6.135h5.87v-3.229c1.173-.415 3.276-.692 4.254-.692 3.229 0 4.843.969 4.843 4.613zm-42.193 9.5h-2.354v4.706H81.59V33.74h-2.77v-8.025c0-3.644 1.708-6.412 5.63-6.412 2.445 0 4.199 1.2 4.199 4.52v9.917h-2.63v4.706h11.859V33.74H95.11V23.18c0-5.766-2.63-9.41-8.352-9.41-4.2 0-7.152 2.353-8.352 5.535h-.093c.37-1.752.37-3.32.37-4.981h-9.46v4.705h3.138zm-20.996-6.964c0-4.382 1.708-7.75 5.676-7.75 3.969 0 5.676 2.584 5.676 7.75 0 4.336-1.707 6.964-5.676 6.964-3.968 0-5.676-2.628-5.676-6.964zm-7.014-.139c0 8.21 4.984 12.362 12.69 12.362s12.69-5.166 12.69-12.362c0-8.395-4.569-12.869-12.69-12.869-7.89 0-12.69 5.95-12.69 12.87zm-11.028 11.81h11.583V33.74h-2.769V23.18c0-5.766-2.63-9.41-8.352-9.41-4.245 0-6.922 2.076-8.075 5.443h-.093c-.646-3.598-3.137-5.443-7.106-5.443-4.014 0-7.014 1.937-8.352 5.443h-.092c.369-1.66.369-3.229.369-4.89H.976v4.706h3.138V33.74h-2.63v4.706h11.859V33.74h-2.769v-8.025c0-3.644 1.708-6.412 5.122-6.412 2.446 0 4.2 1.2 4.2 4.52v9.917h-2.354v4.706h11.582V33.74h-2.768v-8.025c0-3.875 1.984-6.412 5.122-6.412 2.445 0 4.199 1.2 4.199 4.52v9.917h-2.354zm111.11-22.739V13.65h-.474v.675h-2.785v.526l.693 1.241.429-.28-.377-.596c-.042-.064-.084-.114-.13-.173h2.17v.665zm0 3.58v-2.604h-.53v1.27c0 .108.013.242.027.355h-.01c-.623-.833-1.224-1.522-1.881-1.522-.698 0-.945.645-.945 1.172 0 1.04.708 1.31 1.08 1.31v-.69c-.307-.034-.549-.236-.549-.59 0-.202.112-.483.447-.483.647 0 1.668 1.409 1.914 1.783zm-.014 2.99c.08-.25.093-.452.093-.708 0-1.183-.782-1.749-1.872-1.749-1.057 0-1.546.58-1.546 1.345 0 .871.638 1.206 1.174 1.206.675 0 1.014-.512 1.014-1.034 0-.335-.107-.601-.315-.803l.008-.01c.49.064 1.063.315 1.063 1.084 0 .182-.052.409-.126.625zm-2.203-.555c-.177 0-.59-.084-.59-.553 0-.34.227-.63.898-.63a.865.865 0 0 1 .256.596c0 .315-.205.587-.564.587zm2.217 3.127v-2.06h-.474v.675h-2.785v.526l.693 1.241.429-.28-.377-.596c-.042-.064-.084-.113-.13-.172h2.17v.665zm-2.338 2.295v-.59l-.921.029v3.142l.921.029v-.59l-.447-.03v-.621h2.31v.31h.475v-1.34h-.474v.311h-2.31v-.62zm1.864 4.265v.31h.474v-1.339h-.474v.31h-2.31v-.31h-.475v1.339h.474v-.31zm0 1.985v.857h-.951v-.867h-.502v.867h-.858v-.842l.485-.03v-.59l-.96.03v2.52h.475v-.37h2.31v.311h.475v-2.477l-1.024-.039v.59zm-1.892 3.743v-.65h-.777c-.107.296-.172.714-.172 1.133 0 .975.456 1.359.972 1.359.504 0 .764-.241.937-.95l.093-.38c.112-.463.233-.561.4-.561.247 0 .439.187.439.61 0 .203-.014.4-.085.582h-.428V39h.861c.144-.463.182-.857.182-1.237 0-.98-.451-1.422-1.071-1.422-.675 0-.834.56-.955 1.068l-.098.414c-.05.227-.12.409-.358.409-.181 0-.415-.153-.415-.63 0-.168.037-.336.08-.464zM65.78 11.006h1.701V7.382c0-1.68.926-2.607 2.09-2.607.24 0 .493.044.732.073v-1.62c-.15-.015-.254-.045-.642-.045-1.045 0-2.06.87-2.254 1.68h-.03V3.39H65.78zm-6.973-4.478c.045-1.135.852-2.019 2.03-2.019 1.15 0 1.882.958 1.942 2.019zm5.674 1.105c.283-2.284-1.21-4.45-3.643-4.45-2.3 0-3.732 1.857-3.732 4.023 0 2.342 1.358 4.006 3.777 4.006 1.687 0 3.12-.927 3.493-2.592h-1.612c-.299.84-.896 1.267-1.881 1.267-1.419 0-2.076-1.061-2.076-2.254zM49.746 4.716h3.97l-4.269 5.112v1.178h6.793V9.68h-4.643l4.419-5.112V3.39h-6.27zM46.46 2.079h1.702V.487H46.46zm0 8.927h1.702V3.39H46.46zm-7.017-4.478c.045-1.135.851-2.019 2.03-2.019 1.15 0 1.882.958 1.942 2.019zm5.673 1.105c.284-2.284-1.209-4.45-3.642-4.45-2.3 0-3.733 1.857-3.733 4.023 0 2.342 1.36 4.006 3.777 4.006 1.688 0 3.12-.927 3.494-2.592H43.4c-.299.84-.896 1.267-1.882 1.267-1.418 0-2.075-1.061-2.075-2.254zm-16.93 3.373h1.793l1.417-5.657h.03l1.433 5.657h1.747l2.419-7.616h-1.732l-1.568 5.686h-.03L32.309 3.39h-1.717l-1.448 5.686h-.03L27.606 3.39h-1.807zm-10.255 0h1.701V6.513c0-1.12.732-2.004 1.866-2.004 1 0 1.479.516 1.508 1.71v4.787h1.702v-5.23c0-1.709-1.06-2.593-2.732-2.593-1.015 0-1.896.516-2.314 1.208h-.03V.488h-1.701zM16.543 5.94c-.164-1.872-1.687-2.756-3.478-2.756-2.538 0-3.807 1.798-3.807 4.096 0 2.24 1.328 3.934 3.747 3.934 1.985 0 3.225-1.09 3.538-3.065h-1.702c-.164 1.09-.85 1.739-1.836 1.739-1.478 0-2.045-1.34-2.045-2.608 0-2.313 1.239-2.77 2.12-2.77.94 0 1.627.501 1.761 1.43zM1.465 8.56c.03 1.797 1.478 2.652 3.418 2.652 1.598 0 3.449-.663 3.449-2.476 0-1.502-1.254-1.944-2.493-2.224-1.254-.28-2.508-.398-2.508-1.223 0-.663.91-.78 1.403-.78.747 0 1.418.22 1.568 1.016h1.776C7.87 3.832 6.436 3.183 4.854 3.183c-1.404 0-3.225.516-3.225 2.18 0 1.548 1.224 1.99 2.478 2.255 1.24.28 2.478.383 2.523 1.267.045.87-1.075 1.002-1.717 1.002-.91 0-1.657-.354-1.746-1.326z" fill-rule="evenodd"></path></svg></div>
       |                <div class="site-description">
       |                    <p>
       |						Der «Schweizer Monat» ist das Debattenmagazin für Politik, Wirtschaft und Kultur aus Zürich. Wir pflegen den freiheitlichen Wettbewerb der Ideen unter den besten Autorinnen und Autoren der Schweiz und der Welt.                    </p>
       |                </div>
       |            </div>
       |            <div class="footer-column">
       |                <div class="footer-address">
       |                    <span class="footer-headline">Kontakt</span>
       |					<p>
       |	SCHWEIZER MONAT<br>
       |	SMH VERLAG AG<br>
       |    Sihlstrasse 95<br>
       |    8001 Zürich<br>
       |	Schweiz<br>
       |	<br>
       |	Telefon + 41 (0)44 361 26 06<br>
       |	<a href="//schweizermonat.ch">www.schweizermonat.ch</a><br>
       |	<a href="mailto:abo@schweizermonat.ch">abo@schweizermonat.ch</a>
       |</p>
       |                </div>
       |            </div>
       |            <div class="footer-column">
       |
       |				<span class="footer-headline">Folgen Sie uns auf</span>				<nav class="footer-menu__wrap"><ul id="footer-social" class="footer-menu"><li id="menu-item-101" class="menu-item menu-item-type-custom menu-item-object-custom menu-item-101"><a href="https://twitter.com/schweizermonat">Twitter</a></li>
       |<li id="menu-item-58329" class="menu-item menu-item-type-custom menu-item-object-custom menu-item-58329"><a href="https://www.linkedin.com/company/schweizer-monat">LinkedIn</a></li>
       |<li id="menu-item-37170" class="menu-item menu-item-type-custom menu-item-object-custom menu-item-37170"><a href="https://www.instagram.com/schweizermonat/">Instagram</a></li>
       |<li id="menu-item-99" class="menu-item menu-item-type-custom menu-item-object-custom menu-item-99"><a href="https://www.facebook.com/schweizermonat">Facebook</a></li>
       |<li id="menu-item-82233" class="menu-item menu-item-type-custom menu-item-object-custom menu-item-82233"><a href="https://www.youtube.com/@SchweizerMonat">YouTube</a></li>
       |<li id="menu-item-82235" class="menu-item menu-item-type-custom menu-item-object-custom menu-item-82235"><a href="https://t.me/schweizermonat">Telegram</a></li>
       |</ul></nav>            </div>
       |            <div class="footer-column">
       |                <div class="site-description">
       |                    <p>
       |			            <span class="footer-headline">Unterstützen Sie uns</span>
       |<br>
       |Der «Schweizer Monat» ist dem Wettbewerb der Ideen verpflichtet. Um im Wettbewerb der Aufmerksamkeit überleben zu können und unabhängig und liberal zu bleiben, sind wir auf Ihre Unterstützung angewiesen.
       |<a href="https://schweizermonat.ch/ueber-den-schweizermonat/unterstuetzung//">Gerne können Sie einen frei wählbaren Betrag überweisen.</a>                    </p>
       |                </div>
       |            </div>
       |        </div>
       |    </section>
       |
       |    <section class="site-info">
       |        <div class="site-container site-info__container">
       |
       |
       |                <span class="site-info__copyright">
       |    © 1921 – 2023 Schweizer Monat                </span>
       |
       |			<nav class="footer-info__wrap"><ul id="footer-info" class="footer-info"><li id="menu-item-61" class="menu-item menu-item-type-post_type menu-item-object-page menu-item-privacy-policy menu-item-61"><a rel="privacy-policy" href="https://schweizermonat.ch/datenschutzerklaerung/">Datenschutzerklärung</a></li>
       |<li id="menu-item-75" class="menu-item menu-item-type-post_type menu-item-object-page menu-item-75"><a href="https://schweizermonat.ch/allgemeine-geschaeftsbedingungen/">Allgemeine Geschäftsbedingungen</a></li>
       |<li id="menu-item-63" class="menu-item menu-item-type-post_type menu-item-object-page menu-item-63"><a href="https://schweizermonat.ch/impressum/">Impressum</a></li>
       |</ul></nav>        </div>
       |
       |    </section>
       |
       |</footer><!-- #colophon -->
       |""".stripMargin
  }
}
