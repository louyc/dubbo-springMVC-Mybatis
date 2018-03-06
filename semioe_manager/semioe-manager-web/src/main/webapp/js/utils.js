(function (global) {
  var requestFrame = window.requestAnimationFrame ? function (fn) {
    return requestAnimationFrame(fn)
  } : function (fn) {
    return setTimeout(fn, 20)
  }
  var bounce = {
    easeIn: function(t,b,c,d){
      return c - Tween.Bounce.easeOut(d-t, 0, c, d) + b;
    },
    easeOut: function(t,b,c,d){
      if ((t/=d) < (1/2.75)) {
        return c*(7.5625*t*t) + b;
      } else if (t < (2/2.75)) {
        return c*(7.5625*(t-=(1.5/2.75))*t + .75) + b;
      } else if (t < (2.5/2.75)) {
        return c*(7.5625*(t-=(2.25/2.75))*t + .9375) + b;
      } else {
        return c*(7.5625*(t-=(2.625/2.75))*t + .984375) + b;
      }
    },
    easeInOut: function(t,b,c,d){
      if (t < d/2) return Tween.Bounce.easeIn(t*2, 0, c, d) * .5 + b;
      else return Tween.Bounce.easeOut(t*2-d, 0, c, d) * .5 + c*.5 + b;
    }
  }
  //circle chart
  function CircleChart(setting) {
    this.svg = setting.svg
    this.centerX = parseFloat(getComputedStyle(this.svg, '').width) / 2
    this.centerY = parseFloat(getComputedStyle(this.svg, '').height) / 2
    this.start = setting.start
    this.margin = setting.margin
    this.arcs = setting.arcs
    this.radius = setting.radius
    this.strokeWidth = setting.strokeWidth || 20
    this.title = setting.title
    this.titleSize = setting.titleSize || 16
    this.transFrames = 50
    this.angleMargin = this.margin / this.radius * Math.PI * 2
    this.drawArcs()
    this.drawTitle()
  }
  CircleChart.prototype.drawArcs = function () {
    var that = this
    for(var i = 0;i < this.arcs.length;i++) {
      if(!this.arcs[i].value) {
        this.arcs.splice(i--, 1)
      }
    }
    var assignableAngle = Math.PI * 2 - this.angleMargin * this.arcs.length
    var dataSum = 0
    this.arcs.map(function (arc) {
      dataSum += arc.value
    })
    this.arcs.map(function (arc) {
      arc.path = that.create('path')
      arc.path.setAttribute('stroke', arc.color)
      arc.path.setAttribute('stroke-width', that.strokeWidth)
      arc.path.setAttribute('fill', 'none')
      arc.value = arc.value / dataSum
      arc.path.__data__ = arc
      that.addOverlay(arc.path)
      that.svg.appendChild(arc.path)
    })
    this.expand(assignableAngle, dataSum)
  }
  CircleChart.prototype.expand = function (assignableAngle, dataSum) {
    var curFrame = 0
    var dstFrame = this.transFrames
    var that = this
    requestFrame(function frame() {
      if(++curFrame < dstFrame){
        requestFrame(frame)
      }
      var curAssignableAngle = bounce.easeOut(curFrame, 0, assignableAngle, dstFrame)
      var currentAngle = that.start
      for(var i = 0;i < that.arcs.length;i++) {
        var arcAngle = that.arcs[i].value * curAssignableAngle
        var path = that.arcs[i].path
        path.setAttribute('d', that.toArc(currentAngle, currentAngle + arcAngle, that.radius))
        currentAngle += arcAngle + that.angleMargin
      }
    })
  }
  CircleChart.prototype.drawTitle = function () {
    if(!this.title){
      return
    }
    var text = this.create('text')
    text.setAttribute('x', this.centerX)
    text.setAttribute('y', this.centerY)
    text.setAttribute('text-anchor', 'middle')
    text.setAttribute('dy', '.3em')
    text.style.setProperty('font-size', this.titleSize + 'px')
    text.textContent = this.title
    this.svg.appendChild(text)
  }
  CircleChart.prototype.addOverlay = function (elem) {
    var that = this
    elem.addEventListener('mousemove', function (e) {
    }, false)
    elem.addEventListener('mouseenter', function () {
      this.setAttribute('stroke-width', that.strokeWidth * 1.2)
    }, false)
    elem.addEventListener('mouseleave', function (e) {
      this.setAttribute('stroke-width', that.strokeWidth)
    }, false)
  }
  CircleChart.prototype.ns = 'http://www.w3.org/2000/svg'
  CircleChart.prototype.create = function (element) {
    return document.createElementNS(this.ns, element)
  }
  CircleChart.prototype.toCartesian = function (radius, degree) {
    var that = this
    return {
      x: that.centerX + radius * Math.cos(degree),
      y: that.centerY + radius * Math.sin(degree)
    }
  }
  CircleChart.prototype.toArc = function (startAngle, endAngle, radius) {
    var start = this.toCartesian(radius, startAngle)
    var end = this.toCartesian(radius, endAngle)
    return ['M', start.x, start.y, 'A', radius, radius, 0, (Math.abs(startAngle - endAngle) > Math.PI ? 1 : 0), 1, end.x, end.y].join(' ')
  }
  CircleChart.prototype.update = function () {
    this.centerX = parseFloat(getComputedStyle(this.svg, '').width) / 2
    this.centerY = parseFloat(getComputedStyle(this.svg, '').height) / 2
    this.svg.innerHTML = ''
    this.drawArcs()
    this.drawTitle()
  }
  CircleChart.prototype.adjustOnResize = function () {
    var that = this
    if(getComputedStyle(this.svg.parentNode, '').position === 'static'){
      this.svg.parentNode.style.position = 'relative'
    }
    var frame = document.createElement('iframe')
    frame.style.cssText = 'position:absolute;z-index:-1;width:100%;height:100%;opacity:0;'
    this.svg.parentNode.appendChild(frame)
    frame.contentDocument.defaultView.addEventListener('resize', function () {
      that.update()
    }, false)
  }
  global.CircleChart = CircleChart
})(window)

