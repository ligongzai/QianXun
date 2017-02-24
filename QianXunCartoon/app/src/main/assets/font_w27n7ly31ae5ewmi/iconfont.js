;(function(window) {

  var svgSprite = '<svg>' +
    '' +
    '<symbol id="icon-xiazai1" viewBox="0 0 1024 1024">' +
    '' +
    '<path d="M495.897106 752.797251c0.645733 0.937348 1.371286 1.836835 2.202245 2.669806 0.831982 0.829901 1.727412 1.551332 2.660705 2.194992 0.096195 0.065492 0.184203 0.140193 0.279374 0.203638 0.083915 0.058328 0.173969 0.104377 0.25993 0.157589 3.006597 1.956561 6.588315 3.103688 10.441221 3.103688 3.855976 0 7.437694-1.147126 10.441221-3.103688 0.085961-0.054235 0.176016-0.102331 0.261977-0.157589 0.096195-0.064468 0.186249-0.138146 0.279374-0.203638 0.933293-0.64366 1.828723-1.36509 2.660705-2.194992 0.833005-0.832971 1.558559-1.732457 2.204292-2.671852l304.705998-304.71091c7.400854-7.383154 7.400854-19.394722 0-26.797319-7.400854-7.421017-19.413937-7.421017-26.814791 0L530.931429 695.823656l0-529.530258c0-10.605545-8.582821-19.188015-19.187777-19.188015s-19.189824 8.582469-19.189824 19.188015l0 529.530258L218.005898 421.286975c-7.401877-7.421017-19.414961-7.421017-26.815814 0-7.401877 7.399527-7.401877 19.414165 0 26.797319L495.897106 752.797251zM876.332912 837.879022 147.152344 837.879022c-10.60598 0-19.188801 8.582469-19.188801 19.189038 0 10.587126 8.582821 19.186991 19.188801 19.186991l729.180568 0c10.58756 0 19.189824-8.599865 19.189824-19.186991C895.522736 846.461491 886.920472 837.879022 876.332912 837.879022z"  ></path>' +
    '' +
    '</symbol>' +
    '' +
    '<symbol id="icon-xiala" viewBox="0 0 1024 1024">' +
    '' +
    '<path d="M844.8 204.8l-802.133333 0C28.535467 204.8 17.066667 216.251733 17.066667 230.4l0 17.066667C17.066667 261.597867 28.535467 273.066667 42.666667 273.066667l802.133333 0c14.1312 0 25.6-11.4688 25.6-25.6l0-17.066667C870.4 216.251733 858.9312 204.8 844.8 204.8z" fill="#8a8a8a" ></path>' +
    '' +
    '<path d="M969.7792 235.912533m-39.645867 0a2.323 2.323 0 1 0 79.291733 0 2.323 2.323 0 1 0-79.291733 0Z" fill="#8a8a8a" ></path>' +
    '' +
    '<path d="M844.8 477.866667l-802.133333 0C28.535467 477.866667 17.066667 489.3184 17.066667 503.466667l0 17.066667C17.066667 534.664533 28.535467 546.133333 42.666667 546.133333l802.133333 0c14.1312 0 25.6-11.4688 25.6-25.6l0-17.066667C870.4 489.3184 858.9312 477.866667 844.8 477.866667z" fill="#8a8a8a" ></path>' +
    '' +
    '<path d="M969.7792 516.266667m-39.662933 0a2.324 2.324 0 1 0 79.325867 0 2.324 2.324 0 1 0-79.325867 0Z" fill="#8a8a8a" ></path>' +
    '' +
    '<path d="M844.8 750.933333l-802.133333 0C28.535467 750.933333 17.066667 762.402133 17.066667 776.533333l0 17.066667C17.066667 807.7312 28.535467 819.2 42.666667 819.2l802.133333 0c14.1312 0 25.6-11.4688 25.6-25.6l0-17.066667C870.4 762.402133 858.9312 750.933333 844.8 750.933333z" fill="#8a8a8a" ></path>' +
    '' +
    '<path d="M969.7792 779.537067m-39.662933 0a2.324 2.324 0 1 0 79.325867 0 2.324 2.324 0 1 0-79.325867 0Z" fill="#8a8a8a" ></path>' +
    '' +
    '</symbol>' +
    '' +
    '</svg>'
  var script = function() {
    var scripts = document.getElementsByTagName('script')
    return scripts[scripts.length - 1]
  }()
  var shouldInjectCss = script.getAttribute("data-injectcss")

  /**
   * document ready
   */
  var ready = function(fn) {
    if (document.addEventListener) {
      if (~["complete", "loaded", "interactive"].indexOf(document.readyState)) {
        setTimeout(fn, 0)
      } else {
        var loadFn = function() {
          document.removeEventListener("DOMContentLoaded", loadFn, false)
          fn()
        }
        document.addEventListener("DOMContentLoaded", loadFn, false)
      }
    } else if (document.attachEvent) {
      IEContentLoaded(window, fn)
    }

    function IEContentLoaded(w, fn) {
      var d = w.document,
        done = false,
        // only fire once
        init = function() {
          if (!done) {
            done = true
            fn()
          }
        }
        // polling for no errors
      var polling = function() {
        try {
          // throws errors until after ondocumentready
          d.documentElement.doScroll('left')
        } catch (e) {
          setTimeout(polling, 50)
          return
        }
        // no errors, fire

        init()
      };

      polling()
        // trying to always fire before onload
      d.onreadystatechange = function() {
        if (d.readyState == 'complete') {
          d.onreadystatechange = null
          init()
        }
      }
    }
  }

  /**
   * Insert el before target
   *
   * @param {Element} el
   * @param {Element} target
   */

  var before = function(el, target) {
    target.parentNode.insertBefore(el, target)
  }

  /**
   * Prepend el to target
   *
   * @param {Element} el
   * @param {Element} target
   */

  var prepend = function(el, target) {
    if (target.firstChild) {
      before(el, target.firstChild)
    } else {
      target.appendChild(el)
    }
  }

  function appendSvg() {
    var div, svg

    div = document.createElement('div')
    div.innerHTML = svgSprite
    svgSprite = null
    svg = div.getElementsByTagName('svg')[0]
    if (svg) {
      svg.setAttribute('aria-hidden', 'true')
      svg.style.position = 'absolute'
      svg.style.width = 0
      svg.style.height = 0
      svg.style.overflow = 'hidden'
      prepend(svg, document.body)
    }
  }

  if (shouldInjectCss && !window.__iconfont__svg__cssinject__) {
    window.__iconfont__svg__cssinject__ = true
    try {
      document.write("<style>.svgfont {display: inline-block;width: 1em;height: 1em;fill: currentColor;vertical-align: -0.1em;font-size:16px;}</style>");
    } catch (e) {
      console && console.log(e)
    }
  }

  ready(appendSvg)


})(window)