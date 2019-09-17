<template>
  <div class="report-template">
    <el-scrollbar style="height: 100%">
      <div class="degree">
        <h2 class="template-title">评分等级</h2>
        <div class="degree-content template-content">
          <Slider
            :grades="grades"
            v-if="!!Object.keys({ ...this.grades }).length"
            ref="slider"
          />
        </div>
      </div>
      <div class="award">
        <h2 class="template-title">
          评分模板 &nbsp;&nbsp;&nbsp;&nbsp;
          <el-popover placement="right-start" trigger="hover">
            <div class="award-standard">
              <h5 class="award-standard-title">打分规则</h5>
              <div class="award-standard-item">
                1.每项总默认权重不可调整，规则权重可以依据实际情况调整权重分值。
              </div>
              <div class="award-standard-item">2.打分要求：</div>
              <ul class="award-standard-ul">
                <li>1）问题数据占比低于10%则不扣分</li>
                <li>2）问题数据占比10%-25%则扣1分</li>
                <li>3）问题数据占比25%-40%则扣3分</li>
                <li>4）问题数据占比高于40%则不得分</li>
              </ul>
              <div class="award-standard-item">
                占比计算公式 = 问题数据字段数 / 总校验数据字段数
              </div>
              <div class="award-standard-item">3.修正分：</div>
              <ul class="award-standard-ul">
                <li>
                  1）如校验数据不包含该规则，为不影响整体评分，该检查项赋予满分
                </li>
                <li>
                  2）同一条问题数据同时触发多次校验规则，按照各自的扣分项分别记录
                </li>
              </ul>
            </div>
            <span slot="reference" class="cell">
              <svg-icon iconClass="help-circle"></svg-icon>
            </span>
          </el-popover>
        </h2>
        <div class="award-content template-content">
          <el-button
            :disabled="isSetWeight"
            size="mini"
            class="grey-btn"
            @click="setWeight"
            >配置权重</el-button
          >
          <el-button
            size="mini"
            class="red-btn"
            v-if="isSetWeight"
            @click="saveSetWeight"
            >保存配置</el-button
          >
          <el-button
            size="mini"
            class="red-btn"
            v-if="isSetWeight"
            @click="cancelSetWeight"
            >取消配置</el-button
          >
          <div style="margin-top: 20px"></div>
          <el-table
            border
            :data="tableData"
            :span-method="objectSpanMethod"
            style="width:100%"
            :cell-class-name="tableRowClassName"
            @cell-mouse-leave="cellMouseLeave"
            @cell-mouse-enter="cellMouseEnter"
          >
            <el-table-column
              align="center"
              prop="pMeasureIndex"
              label="评分标准"
            ></el-table-column>
            <el-table-column
              align="center"
              prop="pWeight"
              label="默认权重"
            ></el-table-column>
            <el-table-column
              align="center"
              prop="measureIndex"
              label="校验规则"
            ></el-table-column>
            <el-table-column align="center" label="规则权重">
              <template slot-scope="scope">
                <span v-if="!isSetWeight">{{ scope.row.weight }}</span>
                <el-input
                  v-else
                  style="width: 50%"
                  @change="val => changeSetWeight(scope.row.id, val)"
                  v-model="scope.row.weight"
                  class="edit-weight-input"
                ></el-input>
                <br />
                <span
                  ref="tips"
                  v-if="scope.row.pWeight != getAllWeight(scope.row.id)"
                  style="color: red;font-size: 12px"
                  >默认总权重为{{ scope.row.pWeight }}， 当前总权重值为{{
                    getAllWeight(scope.row.id)
                  }}</span
                >
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
      <div class="config">
        <h2 class="template-title">报表导出配置</h2>
        <div class="config-content template-content">
          <div style="margin: 15px 0;"></div>
          <div class="check-config">
            <el-checkbox-group v-model="checkData">
              <el-checkbox v-for="c in configs" :label="c.id" :key="c.id">{{
                c.label
              }}</el-checkbox>
            </el-checkbox-group>
          </div>
        </div>
      </div>
      <div class="submit">
        <el-button @click="resetTemplate" type="info" size="small"
          >重置</el-button
        >
        <el-button @click="submit" type="danger" size="small">保存</el-button>
      </div>
    </el-scrollbar>
  </div>
</template>

<script>
import Slider from "./components/slider.vue";

export default {
  name: "report-template",
  components: { Slider },
  data() {
    return {
      grades: {},
      tableData: [],
      checkData: [],
      configs: [
        {
          id: 1,
          label: "1. 综述"
        },
        {
          id: 2,
          label: "2. 数据质量评分雷达图"
        },
        {
          id: 3,
          label: "3. 标准数据占比总览"
        },
        {
          id: 4,
          label: "4. 问题数据触发规则分布"
        },
        {
          id: 5,
          label: "5. 问题数据处理进度"
        },
        {
          id: 6,
          label: "6. 各表评分明细"
        }
      ],
      isSetWeight: false,
      rowIndex: "-1",
      OrderIndexArr: [],
      hoverOrderArr: [],
      gradesData: []
    };
  },
  methods: {
    getData() {
      this.$urlApi.dataQuality
        .showReportTemplte({ templateId: "1" })
        .then(res => {
          if (res.code == 200) {
            this.tableData = this.transferTableData(res.data.measureIndexs);
            this.getOrderNumber();
            this.gradesData = res.data.grades;
            const grades = res.data.grades
              .reverse()
              .filter((c, i) => i < 4)
              .map(k => k.upperLimit);
            this.grades = {
              value1: grades[0],
              value2: grades[1],
              value3: grades[2]
            };
            this.checkData = res.data.exportModules.split(",").map(c => +c);
            // 存储本地，用于后续重置
            localStorage.setItem(
              "Rtemplate",
              JSON.stringify({
                tableData: this.tableData,
                grades,
                exportModules: this.checkData
              })
            );
          }
        });
    },
    setWeight() {
      if (!this.tableData.length) {
        return this.$toast("error", "无数据可以配置！");
      }
      if (!this.isSetWeight) {
        this.isSetWeight = true;
        sessionStorage.setItem(
          "reportTableData",
          JSON.stringify(this.tableData)
        );
      }
    },
    saveSetWeight() {
      if (this.$refs.tips) {
        return this.$toast("error", "权重未配置正确，无法保存，请重新配置！");
      }
      if (this.isSetWeight) {
        this.isSetWeight = false;
        sessionStorage.setItem(
          "reportTableData",
          JSON.stringify(this.tableData)
        );
        return this.$toast("success", "权重配置成功！");
      }
    },
    cancelSetWeight() {
      this.isSetWeight = false;
      this.tableData = JSON.parse(sessionStorage.getItem("reportTableData"));
    },
    getAllWeight(id) {
      const pMeasureIndex = this.tableData.find(c => c.id === id).pMeasureIndex;
      const a = this.tableData.filter(c => c.pMeasureIndex === pMeasureIndex);
      let sum = 0;
      a.forEach(c => {
        sum = sum + ((((Number.isNaN(+c.weight)) || (+c.weight < 0)) ? 0 : +c.weight));
      });
      return sum;
    },
    submit() {
      if (this.isSetWeight) {
        return this.$toast("error", "请先保存权重配置");
      }
      const value = this.$refs.slider.getValue();
      const measureIndexs = this.tableData.map(c => ({
        id: c.id,
        weight: c.weight,
        templateId: 1
      }));
      const _this = this;
      const post = {
        grades: JSON.stringify([
          {
            templateId: 1,
            gradeName: "不合格",
            lowerLimit: 0,
            id: _this.gradesData.find(c => c.gradeName === "不合格").id,
            upperLimit: value.value1
          },
          {
            templateId: 1,
            gradeName: "合格",
            lowerLimit: value.value1,
            id: _this.gradesData.find(c => c.gradeName === "合格").id,
            upperLimit: value.value2
          },
          {
            templateId: 1,
            gradeName: "良",
            lowerLimit: value.value2,
            id: _this.gradesData.find(c => c.gradeName === "良").id,
            upperLimit: value.value3
          },
          {
            templateId: 1,
            gradeName: "优",
            id: _this.gradesData.find(c => c.gradeName === "优").id,
            lowerLimit: value.value3,
            upperLimit: 100
          }
        ]),
        measureIndexs: JSON.stringify(measureIndexs),
        exportModules: this.checkData.join(",")
      };
      this.$urlApi.dataQuality
        .addReportTemplte(post)
        .then(res => {
          if (res.code == 200) {
            this.$toast("success", "模板保存成功");
            this.getData();
          }
        })
        .catch(e => {
          this.$toast("error", "模板保存失败");
          this.getData();
        });
    },
    objectSpanMethod({ row, column, rowIndex, columnIndex }) {
      if (columnIndex === 0 || columnIndex === 1) {
        const a = {};
        this.tableData.forEach((c, i) => {
          if (a[c.pMeasureIndex]) {
            a[c.pMeasureIndex].push(i);
          } else {
            a[c.pMeasureIndex] = [i];
          }
        });
        const isContain = Object.keys(a).find(c => c === row.pMeasureIndex);
        if (!!isContain && rowIndex === a[isContain][0]) {
          return {
            rowspan: a[isContain].length,
            colspan: 1
          };
        } else {
          return {
            rowspan: 0,
            colspan: 0
          };
        }
      }
    },
    transferTableData(data) {
      const flattenByRecursive = (array, child = "children") => {
        const newArray = [];
        const a = (array, parentObj = {}) => {
          array.forEach(c => {
            if (c[child]) {
              a(c[child], { pWeight: c.weight, pMeasureIndex: c.measureIndex });
            } else {
              newArray.push({ ...c, ...parentObj });
            }
          });
        };
        a(array);
        return newArray.map(k => {
          if (k[child]) {
            delete k[child];
          }
          return k;
        });
      };
      return flattenByRecursive(data);
    },
    changeSetWeight(id, val) {
      const index = this.tableData.findIndex(c => c.id === id);
      if (index != -1) {
        const a = [...this.tableData];
        a[index].weight = (Number.isNaN(parseInt(val)) || (parseInt(val) < 0)) ? 0 : parseInt(val);
        this.tableData = a;
      }
    },
    cellMouseEnter(row, column, cell, event) {
      this.rowIndex = row.rowIndex;
      this.hoverOrderArr = [];
      this.OrderIndexArr.forEach(element => {
        if (element.indexOf(this.rowIndex) >= 0) {
          this.hoverOrderArr = element;
        }
      });
    },
    cellMouseLeave(row, column, cell, event) {
      this.rowIndex = "-1";
      this.hoverOrderArr = [];
    },
    tableRowClassName({ row, rowIndex }) {
      let arr = this.hoverOrderArr;
      for (let i = 0; i < arr.length; i++) {
        if (rowIndex == arr[i]) {
          return "hovered-row";
        }
      }
    },
    getOrderNumber() {
      let obj = {};
      this.tableData.forEach((c, i) => {
        c.rowIndex = i;
        if (obj[c.pMeasureIndex]) {
          obj[c.pMeasureIndex].push(i);
        } else {
          obj[c.pMeasureIndex] = [i];
        }
      });

      for (let k in obj) {
        if (obj[k].length > 1) {
          this.OrderIndexArr.push(obj[k]);
        }
      }
    },
    resetTemplate() {
      const Rtemplate = JSON.parse(localStorage.getItem("Rtemplate"));
      // 重置权重
      this.tableData = Rtemplate.tableData;
      // 重置评分
      this.grades = {};
      this.$nextTick(() => {
        this.grades = {
          value1: Rtemplate.grades[0],
          value2: Rtemplate.grades[1],
          value3: Rtemplate.grades[2]
        };
      });
      // 重置报告配置
      this.checkData = Rtemplate.exportModules;
      this.$toast("success", "报告模板已经重置");
    }
  },
  mounted() {
    this.getData();
  }
};
</script>

<style lang="scss">
.report-template {
  height: calc(100% - 70px);
  padding: 20px 30px;
  margin: 45px 20px;
  margin-bottom: 25px;
  background-color: #fff;
  box-sizing: border-box;
  .el-scrollbar__wrap {
    overflow-x: hidden;
  }
  .template-title {
    width: 98%;
    background-color: #7e8ebf;
    padding: 7px 0;
    padding-left: 20px;
    font-size: 14px;
    color: #fff;
    font-weight: normal;
  }
  .template-content {
    padding: 30px 50px;
  }
  .award .award-content {
    .el-table {
      font-size: 14px !important;
    }
    .el-table .el-table__row {
      background: #fff !important;
    }
    .el-table .hovered-row {
      background-color: rgba(234, 236, 243, 1) !important;
    }
    .el-table .edit-weight-input {
      input {
        text-align: center;
        font-size: 14px;
      }
    }
  }
  .config {
    .el-checkbox__input.is-checked + .el-checkbox__label {
      color: #606266 !important;
    }
    .el-checkbox__input.is-checked .el-checkbox__inner,
    .el-checkbox__input.is-indeterminate .el-checkbox__inner {
      background-color: #3ba3f8 !important;
      border-color: #3ba3f8 !important;
    }
    .check-config {
      width: 50%;
      .el-checkbox-group {
        overflow: hidden;
      }
      .el-checkbox {
        margin: 10px 0 !important;
        float: left;
        width: 50%;
      }
    }
  }
  .submit {
    text-align: center;
    margin: 20px 0;
  }
}
.award-standard-title {
  margin: 0;
  padding: 0;
  padding: 10px 0;
  font-weight: normal;
  font-size: 15px;
}
.award-standard {
  padding-left: 30px;
}
.award-standard-item {
  font-size: 13px;
  padding: 10px 0;
}
.award-standard-ul {
  padding-left: 20px;
  li {
    list-style: none;
    padding: 5px 0;
  }
}
</style>
