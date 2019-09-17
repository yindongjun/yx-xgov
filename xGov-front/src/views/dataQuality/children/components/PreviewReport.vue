<template>
  <el-dialog
    :title="previewReport.row.name"
    :visible.sync="previewReport.visible"
    :before-close="close"
    :fullscreen="true"
    @open="open"
    append-to-body
    class="preview__"
    :close-on-click-modal="false"
  >
    <div class="preview-report" v-loading="loading">
      <div class="banxin" id="exportBtn">
        <el-dropdown style="float:right" @command="handleCommand">
          <el-button size="mini" type="danger">导出</el-button>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="PDF">PDF</el-dropdown-item>
            <el-dropdown-item command="HTML">HTML</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
      <div ref="box" id="box">
        <h1 class="banxin all-title text-align-center">{{previewReport.row.name}}数据质量报告</h1>
        <div class="banxin sub-title text-align-right">生成时间：{{previewReport.row.createTime}}</div>
        <!-- 第一部分 -->
        <div v-if="modules.find(c => c == 1)" class="box1 banxin text-align-left">
          <h2 class="title">{{getIndex(1)}}. 综述</h2>
          <ul>
            <li class="clearfix">
              <div class="item-name fl">得分</div>
              <div class="delimiter fl">：</div>
              <div>{{totalScore}}</div>
            </li>
            <li class="clearfix">
              <div class="item-name fl">报告类型</div>
              <div class="delimiter fl">：</div>
              <div class="item-context fl">数据质量报告</div>
            </li>
            <li class="clearfix">
              <div class="item-name fl">总结</div>
              <div class="delimiter fl">：</div>
              <div
                class="item-context fl"
              >截至<span class="fontText"> {{previewReport.row.createTime}} </span>，累计校验<span class="fontText"> {{allSum || 0}} </span>行数据，总体评分为<span class="fontText"> {{previewReport.row.scoreGrade}} </span>。</div>
            </li>
          </ul>
        </div>
        <!-- 第二部分 -->
        <div v-if="modules.find(c => c == 2)" class="box2 banxin text-align-left">
          <h2 class="title">{{getIndex(2)}}. 数据质量评分雷达图</h2>
          <div class="drawBox2"></div>
          <el-table border style="width: 100%" :data="box2Data.tableData">
            <el-table-column align="center" prop="score" label>
              <template slot-scope="scope">
                <span v-if="scope.row.name == -1">得分</span>
                <span v-if="scope.row.name == -2">满分</span>
              </template>
            </el-table-column>
            <template v-for="(c, i) in box2Data.columns">
              <el-table-column v-if="c != 'name'" align="center" :key="i" :prop="c" :label="c"></el-table-column>
            </template>
          </el-table>
        </div>
        <!-- 第三部分 -->
        <div v-if="modules.find(c => c == 3)" class="box3 banxin text-algin-left">
          <h2 class="title">{{getIndex(3)}}. 标准数据占比总览</h2>
          <div class="drawBox3"></div>
          <p>所选的<span class="fontText"> {{box3Data.tableNum}} </span>张数据表，经质量规则校验，总体标准数据占比为<span class="fontText"> {{box3Data.percentage}}% </span>。其中，排行前5的数据表详情如下：</p>
          <el-table border :data="box3Data.tableData">
            <el-table-column align="center" type="index" width="60" label="序号"></el-table-column>
            <template v-for="(c, i) in box3Data.columns">
              <el-table-column align="center" :key="i" :prop="c" :label="c">
                <template slot-scope="scope">{{c == '合规率' ? scope.row[c] + '%' : scope.row[c]}}</template>
              </el-table-column>
            </template>
          </el-table>
        </div>
        <!-- 第四部分 -->
        <div v-if="modules.find(c => c == 4)" class="box4 banxin text-align-left">
          <h2 class="title">{{getIndex(4)}}. 问题数据触发规则分布</h2>
          <div class="drawBox4"></div>
          <el-table border :data="box4Data.tableData">
            <el-table-column
              v-for="(c, i) in box4Data.columns"
              align="center"
              :key="i"
              :prop="c"
              :label="c"
            ></el-table-column>
          </el-table>
        </div>
        <!-- 第五部分 -->
        <div v-if="modules.find(c => c == 5)" class="box5 banxin text-align-left">
          <h2 class="title">{{getIndex(5)}}. 问题数据处理进度</h2>
          <div class="drawBox5"></div>
          <p>以下展示的为待处理且问题数据排名前五的数据，若数据不足五个则显示对应条数：</p>
          <el-table style="width: 50%;margin: 0 auto" border :data="box5Data.tableData">
            <el-table-column align="center" prop="dataSourceName" label="数据源"></el-table-column>
            <el-table-column align="center" prop="tableName" label="表名"></el-table-column>
            <el-table-column align="center" prop="errorDataNum" label="问题数据"></el-table-column>
            <el-table-column align="center" prop="status" label="处理结果">
              <template slot-scope="scope">
                <span v-if="scope.row.status == 0">待处理</span>
                <span v-if="scope.row.status == 1">已处理</span>
              </template>
            </el-table-column>
            <el-table-column align="center" prop="owner" label="问题责任人"></el-table-column>
          </el-table>
        </div>
        <!-- 第六部分 -->
        <div v-if="modules.find(c => c == 6)" class="box6 banxin text-align-left">
          <h2 class="title">{{getIndex(6)}}. 各表评分明细</h2>
          <el-table border :data="box6Data.tableData">
            <el-table-column
              v-for="(c, i) in box6Data.columns"
              align="center"
              :key="i"
              :prop="c"
              :label="c"
            ></el-table-column>
          </el-table>
        </div>
      </div>
    </div>
  </el-dialog>
</template>


<script>
import Echarts from "echarts";
import downloadHTML from "./download.js";

export default {
  name: "preview-report",
  props: ["previewReport"],
  data() {
    return {
      loading: false,
      allSum: "",
      totalScore: "",
      scoreDegree: "",
      box2Data: {},
      box3Data: {},
      box4Data: {},
      box5Data: {},
      box6Data: {},
      drawBox2Chart: null,
      drawBox3Chart: null,
      drawBox4Chart: null,
      drawBox5Chart: null
    };
  },
  methods: {
    getData() {
      this.loading = true;
      this.$urlApi.dataQuality
        .showReport({ reportId: this.previewReport.row.id })
        .then(res => {
          this.loading = false;
          if (res.code == 200) {
            const box2Data = JSON.parse(res.data.radarMap);
            const box3Data = JSON.parse(res.data.rightTop5);
            const box4Data = JSON.parse(res.data.errorDistribution);
            const box5Data = JSON.parse(res.data.errorDataHandle);
            const box6Data = JSON.parse(res.data.scoreDetail);
            this.totalScore = box2Data.find(c => c.name == -1)["总得分"];

            this.allSum = res.data.allSum;

            const get_box2Data_columns = data => {
              const columns = [...data];
              const a = columns.find(c => c == "总得分");
              const index = columns.findIndex(c => c == "总得分");
              columns.splice(index, 1);
              columns.push(a);
              return columns;
            };

            const get_box3Data_columns = data => {
              const columns = [
                "表名",
                "所属数据源",
                "原始数据",
                "标准数据",
                "合规率"
              ];
              return columns;
            };

            const get_box6Data_columns = data => {
              const columns = [...data];
              const a1 = data.find(c => c == "总得分");
              const index1 = data.findIndex(c => c == "总得分");
              columns.splice(index1, 1);
              columns.push(a1);

              const a2 = columns.find(c => c == "表名");
              const index2 = columns.findIndex(c => c == "表名");
              columns.splice(index2, 1);
              columns.unshift(a2);

              return columns;
            };

            this.box2Data = {
              columns: get_box2Data_columns(Object.keys(box2Data[0])),
              tableData: box2Data
            };
            this.box3Data = {
              columns: get_box3Data_columns(Object.keys(box3Data.top5List[0])),
              tableData: box3Data.top5List,
              tableNum: box3Data.tableNum,
              percentage: box3Data.percentage
            };
            this.box4Data = {
              columns: Object.keys(box4Data),
              tableData: [box4Data]
            };
            this.box5Data = {
              tableData: box5Data.top5List || []
            };
            this.box6Data = {
              columns: get_box6Data_columns(Object.keys(box6Data[0])),
              tableData: box6Data
            };
            this.modules.find(c => c == 2) && this.drawBox2(box2Data);
            this.modules.find(c => c == 3) && this.drawBox3(box3Data);
            this.modules.find(c => c == 4) && this.drawBox4(box4Data);
            this.modules.find(c => c == 5) && this.drawBox5(box5Data);
          }
        })
        .catch(e => {
          this.loading = false;
        });
    },
    open() {
      this.getData();
    },
    close() {
      this.$emit("previewReportCB");
    },
    handleCommand(commnad) {
      if (commnad === "PDF") {
        this.$nextTick(() => {
          this.$getPdf("#box", this.previewReport.row.name);
        })
      } else if (commnad === "HTML") {
        downloadHTML({
          content: this.getContent(),
          type: "text/html",
          encoding: "utf-8",
          fileName: this.previewReport.row.name + '.html'
        });
      }
    },
    getContent() {
      const imgList = JSON.stringify(this.getCanvasToImage());

      const canvasToImageScript = `${"<scr"}ipt>
        var body = document.querySelector('body')
        var app = document.querySelector('body>#app')
        body.removeChild(app);

        var parent = document.querySelector('.preview__ .is-fullscreen');
        var child = document.querySelector('.preview__ .el-dialog__header');
        parent.removeChild(child);

        var btnParent = document.querySelector('.preview-report');
        var btn = document.querySelector('#exportBtn');
        btnParent.removeChild(btn);

        var dropmenus = document.querySelectorAll('body .el-dropdown-menu');
        dropmenus.forEach(function(c) {
          c.style.display = 'none'
        })
        document.querySelector('#box').style.backgroundColor = '#fafafa';

        var a = ${imgList};
        a.forEach(function(c) {
            var parentDom = document.querySelector('.' + c.key);
            var childCanvas = document.querySelector('.' + c.key + '>div');
            if (parentDom && childCanvas) {
              parentDom.removeChild(childCanvas);
            }
            if (c.src) {
              var img = new Image();
              img.src = c.src;
              document.querySelector('.' + c.key).appendChild(img)
            }
        })
      ${"</scr"}ipt>
      `;
      return document.querySelector("html").innerHTML + canvasToImageScript;
    },
    getCanvasToImage() {
      const a = ["drawBox2", "drawBox3", "drawBox4", "drawBox5"];
      return a.map(c => {
        return {
          key: c,
          src: document.querySelector("." + c + " canvas")
            ? document
                .querySelector("." + c + " canvas")
                .toDataURL("image/png", 0.5)
            : ""
        };
      });
    },
    drawBox2(data) {
      this.drawBox2Chart = Echarts.init(document.querySelector(".drawBox2"));
      if (this.drawBox2Chart) {
        const options = {
          title: {
            text: "数据质量评分",
            left: "center",
            textStyle: {
              fontWeight: "normal"
            }
          },
          tooltip: {
            trigger: "axis"
          },
          radar: [
            {
              indicator: [
                { text: "规范性", max: 100, color: "#333" },
                { text: "关联性", max: 100, color: "#333" },
                { text: "唯一性", max: 100, color: "#333" },
                { text: "有效性", max: 100, color: "#333" },
                { text: "准确性", max: 100, color: "#333" },
                { text: "完整性", max: 100, color: "#333" }
              ],
              radius: 80,
              shape: "circle"
            }
          ],
          series: [
            {
              type: "radar",
              tooltip: {
                trigger: "item"
              },
              itemStyle: {
                normal: {
                  areaStyle: { type: "default" },
                  color: "#FEB3B8",
                  lineStyle: { color: "transparent" }
                }
              },
              data: [
                {
                  value: [60, 73, 85, 40, 50, 80],
                  name: "数据质量评分"
                }
              ]
            }
          ]
        };
        // 满分赋值
        const man = { ...data.find(c => c.name == -2) };
        delete man["总得分"];
        delete man["name"];
        options.radar[0].indicator = Object.keys(man).map(c => ({
          text: c,
          max: +man[c],
          color: "#333"
        }));
        // 得分赋值
        const de = { ...data.find(c => c.name == -1) };
        delete de["总得分"];
        delete de["name"];
        options.series[0].data[0].value = Object.values(de);
        this.drawBox2Chart.setOption(options);
      }
    },
    drawBox3(data) {
      this.drawBox3Chart = Echarts.init(document.querySelector(".drawBox3"));
      if (this.drawBox3Chart) {
        const options = {
          tooltip: {
            trigger: "item",
            formatter: "{a} <br/>{b}: {d}%"
          },
          title: {
            text: "标准数据占比",
            left: "center",
            textStyle: {
              fontWeight: "normal",
              fontSize: 16
            }
          },
          legend: {
            orient: "vertical",
            x: "left",
            data: [
              { name: "问题数据", icon: "circle" },
              { name: "标准数据", icon: "circle" }
            ],
            left: "20%"
          },
          series: [
            {
              name: "标准数据占比",
              type: "pie",
              radius: ["50%", "70%"],
              avoidLabelOverlap: false,
              clockwise: false,
              label: {
                normal: {
                  show: true,
                  position: "center",
                  formatter: "合规率 80%",
                  textStyle: {
                    fontSize: "20",
                    fontWeight: "bold",
                    color: "#c23531"
                  }
                }
              },
              labelLine: {
                normal: {
                  show: false
                }
              },
              data: [
                {
                  value: 20,
                  name: "问题数据",
                  itemStyle: { color: "#FEB3B8" }
                },
                { value: 80, name: "标准数据", itemStyle: { color: "#3BA3F8" } }
              ]
            }
          ]
        };
        options.series[0].label.normal.formatter = data.percentage + "%";
        options.series[0].data[0].value = 100 - +data.percentage;
        options.series[0].data[1].value = +data.percentage;
        this.drawBox3Chart.setOption(options);
      }
    },
    drawBox4(data) {
      this.drawBox4Chart = Echarts.init(document.querySelector(".drawBox4"));
      if (this.drawBox4Chart) {
        const options = {
          color: ["#4A90E2"],
          title: {
            text: "问题数据触发规则分布",
            left: "center",
            textStyle: {
              fontWeight: "normal"
            }
          },
          tooltip: {
            trigger: "axis",
            axisPointer: {
              type: "shadow"
            }
          },
          grid: {
            bottom: "3%",
            containLabel: true
          },
          xAxis: {
            type: "category",
            axisTick: {
              show: false
            },
            axisLabel: {
              rotate: 60,
              color: "#333"
            },
            data: [
              "格式校验",
              "正则校验",
              "空值校验",
              "精度校验",
              "值域校验",
              "数据范围",
              "数据唯一性",
              "字段值比对"
            ]
          },
          yAxis: {
            type: "value"
          },
          series: [
            {
              data: [153, 134, 200, 334, 390, 330, 220, 234],
              barWidth: "40%",
              type: "bar",
              label: {
                normal: {
                  show: true,
                  position: "top",
                  color: "#333"
                }
              }
            }
          ]
        };
        const xColumns = Object.keys(data);

        options.xAxis.data = xColumns;
        options.series[0].data = xColumns.map(c => data[c]);
        this.drawBox4Chart.setOption(options);
      }
    },
    drawBox5(data) {
      this.drawBox5Chart = Echarts.init(document.querySelector(".drawBox5"));
      if (this.drawBox5Chart) {
        const options = {
          title: {
            text: "问题数据处理进度",
            left: "center",
            textStyle: {
              fontWeight: "normal"
            }
          },
          tooltip: {
            trigger: "item",
            formatter: "{a} <br/> {b} : {c}%"
          },
          legend: {
            orient: "vertical",
            left: "left",
            data: [
              { name: "待处理", icon: "circle" },
              { name: "已处理", icon: "circle" }
            ],
            left: "20%"
          },
          series: [
            {
              name: "问题数据处理进度",
              type: "pie",
              radius: "55%",
              center: ["50%", "60%"],
              data: [
                { value: 20, name: "待处理", itemStyle: { color: "#FEB3B8" } },
                { value: 80, name: "已处理", itemStyle: { color: "#3BA3F8" } }
              ],
              clockwise: false,
              label: {
                fontSize: 14
              }
            }
          ]
        };
        options.series[0].data[0].value = data.percentage;
        options.series[0].data[1].value = 100 - data.percentage;
        this.drawBox5Chart.setOption(options);
      }
    },
    getIndex(i) {
      return this.modules.findIndex(c => c == i) + 1;
    }
  },
  computed: {
    modules() {
      return this.previewReport.row.exportModules
        ? this.previewReport.row.exportModules
            .split(",")
            .map(c => +c)
            .sort((a, b) => a - b)
        : [1, 2, 3, 4, 5, 6];
    }
  },
  mounted() {}
};
</script>

<style lang="scss">
.preview-report {
  width: 100%;
  .el-table {
    font-size: 14px !important;
  }
  .el-table--border::after,
  .el-table--group::after,
  .el-table::before {
    background-color: #5e6977 !important;
  }
  .el-table td,
  .el-table th.is-leaf {
    border-bottom: 1px solid #5e6977 !important;
  }
  .el-table--border td,
  .el-table--border th,
  .el-table__body-wrapper
    .el-table--border.is-scrolling-left
    ~ .el-table__fixed {
    border-right: 1px solid #5e6977 !important;
  }
  .el-table--border,
  .el-table--group {
    border: 1px solid #5e6977 !important;
    border-bottom-width: 0px !important;
    border-right-width: 0px !important;
  }
  .el-table thead {
    color: #333333;
  }
  .el-table td,
  .el-table th {
    padding: 0 !important;
    background-color: #fff !important;
    height: 30px !important;
    line-height: 30px !important;
    text-align: center!important;
    font-size: 14px!important;
  }
  .el-table th {
    background-color: #e7f2ff !important;
  }
  .el-table .cell {
    white-space: normal;
    word-break: break-all;
  }

  .banxin {
    min-width: 1080px;
    width: 1080px;
    margin: 0 auto;
    .title {
      font-size: 14px;
      margin: 0;
      padding: 7px 0;
      margin: 50px 0;
      background-color: #7e8ebf;
      color: #fff;
      padding-left: 25px;
      font-weight: normal;
    }
  }
  .text-align-center {
    text-align: center;
  }
  .text-align-left {
    text-align: left;
  }
  .text-align-right {
    text-align: right;
  }
  .fl {
    float: left;
  }
  .fr {
    float: right;
  }

  　.clearfix:before,
  .clearfix:after {
    content: "";
    display: block;
    clear: both;
  }
  .clearfix {
    zoom: 1;
  }
  #box {
    padding: 50px 0;
    box-sizing: border-box;
    font-family: "MicroSoft YaHei";
    color: #5e6977;
    .all-title {
      font-size: 24px;
      font-weight: normal;
      color: #647094;
    }
    .sub-title {
      font-size: 16px;
      color: #647094;
    }
    .box1 {
      ul {
        padding-left: 50px;
        li {
          list-style: none;
          margin: 10px 0;
        }
        .item-name {
          font-weight: bold;
          width: 80px;
          text-align: right;
        }
      }
    }
  }
  .drawBox2,
  .drawBox3,
  .drawBox5 {
    height: 300px;
    width: 100%;
  }
  .drawBox4 {
    height: 400px;
    width: 50%;
    margin: 0 auto;
  }
  .fontText {
    color: #F67F89;
    font-weight: bold;
  }
}
</style>
