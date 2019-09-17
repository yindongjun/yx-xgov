<template>
  <div class="slider">
    <div class="main">
      <div class="op1 op">
        <div class="c c1"></div>
      </div>
      <div class="op2 op">
        <div class="c c2"></div>
      </div>
      <div class="op3 op">
        <div class="c c3"></div>
      </div>
      <div class="op4 op"></div>
    </div>
    <div class="sample">
      <div class="sp1 sp">
        <div class="f1 ff"></div>
        <div class="txt1 txt">不合格</div>
        <div class="value1 valueShow">{{ `0 ~ ${value.value1 - 1}` }}</div>
      </div>
      <div class="sp2 sp">
        <div class="f2 ff"></div>
        <div class="txt2 txt">合格</div>
        <div class="value2 valueShow">
          {{ `${value.value1} ~ ${value.value2 - 1}` }}
        </div>
      </div>
      <div class="sp3 sp">
        <div class="f3 ff"></div>
        <div class="txt3 txt">良</div>
        <div class="value3 valueShow">
          {{ `${value.value2} ~ ${value.value3 - 1}` }}
        </div>
      </div>
      <div class="sp4 sp">
        <div class="f4 ff"></div>
        <div class="txt4 txt">优</div>
        <div class="value4 valueShow">{{ `${value.value3} ~ 100` }}</div>
      </div>
    </div>
  </div>
</template>


<script>
import $ from "jquery";

export default {
  name: "slider",
  props: ["grades"],
  data() {
    return {
      value1: "",
      value2: "",
      value3: ""
    };
  },
  methods: {
    init() {
      var _this = this;
      var width = parseFloat($(".main").css("width"));
      var getX = i => {
        return (i / 100) * width;
      };
      $(".op1").css({ width: getX(this.$props.grades.value1) });
      $(".op2").css({
        width: getX(this.$props.grades.value2 - this.$props.grades.value1)
      });
      $(".op3").css({
        width: getX(this.$props.grades.value3 - this.$props.grades.value2)
      });
      $(".op4").css({ width: getX(100 - this.$props.grades.value3) });
      _this.value1 = this.$props.grades.value1;
      _this.value2 = this.$props.grades.value2 - this.$props.grades.value1;
      _this.value3 = this.$props.grades.value3 - this.$props.grades.value2;
      var cp1 = {
        handle1: false,
        startX1: "",
        width1: ""
      };
      var cp2 = {
        handle2: false,
        startX2: "",
        width2: ""
      };
      var cp3 = {
        handle3: false,
        startX3: "",
        width3: ""
      };
      var cp4 = {
        handle4: false,
        startX4: "",
        width4: ""
      };

      var percentOne = width / 100;
      // cp1
      $(".c1").mousedown(function(event) {
        cp1.handle1 = true;
        cp1.startX1 = event.pageX;
        cp1.width1 = parseFloat($(".op1").css("width"));
        cp2.width2 = parseFloat($(".op2").css("width"));
      });
      $(document).mouseup(function(e) {
        cp1.handle1 = false;
      });
      $(document).mousemove(function(e) {
        if (cp1.handle1) {
          var chaZhi = cp1.startX1 - e.pageX;
          var width1, width2;
          if (chaZhi > 0 && cp1.width1 - chaZhi <= 8) {
            width1 = 8;
            width2 = cp1.width1 + cp2.width2 - 8;

            $(".op1").css({ width: width1 });
            $(".op2").css({ width: width2 });
            _this.value1 = ((width1 - 8) / width) * 100;
            _this.value2 = ((width2 + 8) / width) * 100;
          } else if (chaZhi < 0 && cp2.width2 - Math.abs(chaZhi) <= 8) {
            width1 = cp1.width1 + cp2.width2 - 8;
            width2 = 8;
            $(".op1").css({ width: width1 });
            $(".op2").css({ width: width2 });
            _this.value1 = ((width1 + 8) / width) * 100;
            _this.value2 = ((width2 - 8) / width) * 100;
          } else {
            width1 = cp1.width1 - chaZhi;
            width2 = cp2.width2 + chaZhi;
            $(".op1").css({ width: width1 });
            $(".op2").css({ width: width2 });
            _this.value1 = (width1 / width) * 100;
            _this.value2 = (width2 / width) * 100;
          }
        }
      });
      // cp2
      $(".c2").mousedown(function(event) {
        cp2.handle2 = true;
        cp2.startX2 = event.pageX;
        cp2.width2 = parseFloat($(".op2").css("width"));
        cp3.width3 = parseFloat($(".op3").css("width"));
      });
      $(document).mouseup(function(e) {
        cp2.handle2 = false;
      });
      $(document).mousemove(function(e) {
        if (cp2.handle2) {
          var chaZhi = cp2.startX2 - e.pageX;
          var width2, width3;
          if (chaZhi > 0 && cp2.width2 - chaZhi <= 8) {
            width2 = 8;
            width3 = cp2.width2 + cp3.width3 - 8;
            $(".op2").css({ width: width2 });
            $(".op3").css({ width: width3 });
            _this.value2 = ((width2 - 8) / width) * 100;
            _this.value3 = ((width3 + 8) / width) * 100;
          } else if (chaZhi < 0 && cp3.width3 - Math.abs(chaZhi) <= 8) {
            width2 = cp2.width2 + cp3.width3 - 8;
            width3 = 8;
            $(".op2").css({ width: width2 });
            $(".op3").css({ width: width3 });
            _this.value2 = ((width2 + 8) / width) * 100;
            _this.value3 = ((width3 - 8) / width) * 100;
          } else {
            width2 = cp2.width2 - chaZhi;
            width3 = cp3.width3 + chaZhi;
            $(".op2").css({ width: width2 });
            $(".op3").css({ width: width3 });
            _this.value2 = (width2 / width) * 100;
            _this.value3 = (width3 / width) * 100;
          }
        }
      });
      // cp3
      $(".c3").mousedown(function(event) {
        cp3.handle3 = true;
        cp3.startX3 = event.pageX;
        cp3.width3 = parseFloat($(".op3").css("width"));
        cp4.width4 = parseFloat($(".op4").css("width"));
      });
      $(document).mouseup(function(e) {
        cp3.handle3 = false;
      });
      $(document).mousemove(function(e) {
        if (cp3.handle3) {
          var chaZhi = cp3.startX3 - e.pageX;
          var width3, width4;
          if (chaZhi > 0 && cp3.width3 - chaZhi <= 8) {
            width3 = 8;
            width4 = cp3.width3 + cp4.width4 - 8;
            $(".op3").css({ width: width3 });
            $(".op4").css({ width: width4 });
            _this.value3 = ((width3 - 8) / width) * 100;
          } else if (chaZhi < 0 && cp4.width4 - Math.abs(chaZhi) <= 8) {
            width3 = cp3.width3 + cp4.width4;
            width4 = 0;
            $(".op3").css({ width: width3 });
            $(".op4").css({ width: width4 });
            _this.value3 = (width3 / width) * 100;
          } else {
            width3 = cp3.width3 - chaZhi;
            width4 = cp4.width4 + chaZhi;
            $(".op3").css({ width: width3 });
            $(".op4").css({ width: width4 });
            _this.value3 = (width3 / width) * 100;
          }
        }
      });
    },
    getValue() {
      return {
        value1: this.value.value1,
        value2: this.value.value2,
        value3: this.value.value3,
        value4: this.value.value4
      };
    }
  },
  computed: {
    value() {
      return {
        value1: Math.round(this.value1),
        value2: Math.round(this.value1 + this.value2),
        value3: Math.round(this.value1 + this.value2 + this.value3),
        value4: 100
      };
    }
  },
  mounted() {
    this.init();
  }
};
</script>


<style lang="scss">
.slider {
  width: 100%;
  .main {
    width: 800px;
    height: 10px;
    background-color: #ddd;
    position: relative;
    &::before {
      position: absolute;
      content: "";
      width: 2px;
      height: 30px;
      left: 0;
      top: -10px;
      background-color: #f26565;
    }
    &::after {
      position: absolute;
      content: "";
      width: 2px;
      height: 30px;
      right: 0;
      top: -10px;
      background-color: #8ddecc;
    }
    .op {
      position: relative;
      float: left;
      height: 100%;
    }
    .op1 {
      background-color: #f26565;
      .c1 {
        background: linear-gradient(to right, #f26565, #f59a78);
        // background-color: #F26565;
        position: absolute;
        right: 0;
        top: -10px;
      }
    }
    .op2 {
      background-color: #f59a78;
      .c2 {
        background: linear-gradient(to right, #f59a78, #f8e287);
        // background-color: #F59A78;
        position: absolute;
        right: 0;
        top: -10px;
      }
    }
    .op3 {
      background-color: #f8e287;
      .c3 {
        background: linear-gradient(to right, #f8e287, #8ddecc);
        background-color: #f8e287;
        position: absolute;
        right: 0;
        top: -10px;
      }
    }
    .op4 {
      background-color: #8ddecc;
    }
    .c {
      width: 8px;
      height: 30px;
      cursor: grab;
      border-radius: 2px;
    }
  }
  .sample {
    margin-top: 30px;
    overflow: hidden;
    .sp {
      overflow: hidden;
      padding: 5px 0;
      box-sizing: border-box;
      float: left;
      width: 25%;
    }
    .ff {
      float: left;
      width: 40px;
      height: 20px;
      border-radius: 4px;
      margin-top: 10px;
    }
    .txt {
      float: left;
      margin-left: 10px;
      font-size: 14px;
      color: #606266;
      line-height: 40px;
    }
    .valueShow {
      float: left;
      margin-left: 10px;
      font-size: 14px;
      color: #606266;
      line-height: 40px;
    }
    .f1 {
      background-color: #f26565;
    }
    .f2 {
      background-color: #f59a78;
    }
    .f3 {
      background-color: #f8e287;
    }
    .f4 {
      background-color: #8ddecc;
    }
  }
}
</style>
