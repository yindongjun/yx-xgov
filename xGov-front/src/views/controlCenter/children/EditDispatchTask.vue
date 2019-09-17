<template>
  <div>
    <el-dialog
      :close-on-click-modal="false"
      width="760px"
      title="编辑调度任务"
      :visible.sync="EditDispatchTaskVisible"
      :before-close="closeDialog"
      @closed="clearData"
      @open="openDialog"
      class="edit-task"
      append-to-body
    >
      <el-form
        :model="ruleForm"
        ref="ruleForm"
        :rules="rules"
        size="small"
        label-width="100px"
        label-position="right"
      >
        <el-form-item label="任务名称:">
          {{ ruleForm.taskName }}
        </el-form-item>
        <el-form-item label="任务类型:">
          {{ ruleForm.taskType }}
        </el-form-item>
        <el-form-item label="调度规则:" prop="cycleType">
          <el-radio-group v-model="ruleForm.cycleType">
            <el-radio label="0">单次</el-radio>
            <el-radio label="1">周期性</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item
          label="执行时间:"
          v-if="ruleForm.cycleType === '0'"
          prop="triggerTime"
        >
          <el-date-picker
            v-model="ruleForm.triggerTime"
            size="small"
            type="datetime"
            value-format="timestamp"
            placeholder="选择执行时间"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item
          label="采集频率:"
          v-if="ruleForm.cycleType === '1'"
          prop="jobCron"
        >
          <el-col :span="6">
            <el-select
              v-model="ruleForm.jobCron"
              placeholder="请设置执行周期"
              @change="setCycle"
            >
              <el-option
                v-show="false"
                label="请选择频率"
                value="4"
              ></el-option>
              <el-option label="按月执行" value="0"></el-option>
              <el-option label="按周执行" value="1"></el-option>
              <el-option label="按天执行" value="2"></el-option>
              <el-option label="按小时执行" value="3"></el-option>
            </el-select>
          </el-col>
          <el-col :span="17" class="year ml5" v-if="CycleSelected === '0'">
            <el-col :span="8" class="mr5">
              <el-input-number
                v-model="ruleForm.exeDate1.value1"
                @change="handleChange"
                :min="1"
                :max="31"
                label="设置执行日"
              ></el-input-number>
            </el-col>
            <el-col :span="13">
              <el-time-picker
                v-model="ruleForm.exeDate1.value2"
                placeholder="任意时间点"
                value-format="HH:mm:ss"
              >
              </el-time-picker>
            </el-col>
          </el-col>
          <el-col :span="16" class="year ml5" v-if="CycleSelected === '1'">
            <el-col :span="8">
              <el-select
                v-model="ruleForm.exeDate2.value1"
                placeholder="请设置执行周期"
              >
                <el-option label="周日" value="SUN"></el-option>
                <el-option label="周一" value="MON"></el-option>
                <el-option label="周二" value="TUE"></el-option>
                <el-option label="周三" value="WED"></el-option>
                <el-option label="周四" value="THU"></el-option>
                <el-option label="周五" value="FRI"></el-option>
                <el-option label="周六" value="SAT"></el-option>
              </el-select>
            </el-col>
            <el-col :span="6" class="ml5">
              <el-time-picker
                v-model="ruleForm.exeDate2.value2"
                placeholder="任意时间点"
                value-format="HH:mm:ss"
              >
              </el-time-picker>
            </el-col>
          </el-col>
          <el-col :span="16" class="year ml5" v-if="CycleSelected === '2'">
            <el-col :span="6">
              <el-time-picker
                v-model="ruleForm.exeDate3.value1"
                placeholder="任意时间点"
                value-format="HH:mm:ss"
              >
              </el-time-picker>
            </el-col>
          </el-col>
          <el-col :span="16" class="year ml5" v-if="CycleSelected === '3'">
            <el-select
              size="small"
              v-model="ruleForm.exeDate4.value1"
              multiple
              placeholder="请选择时间（可多选）"
            >
              <el-option
                v-for="item in timeOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              >
              </el-option>
            </el-select>
          </el-col>
        </el-form-item>
        <el-form-item
          label="生效时间"
          v-if="ruleForm.cycleType === '1'"
          prop="effectTimeRange"
        >
          <el-date-picker
            v-model="ruleForm.effectTimeRange"
            type="daterange"
            value-format="yyyy-MM-dd HH:mm:ss"
            size="small"
            start-placeholder="开始时间"
            end-placeholder="结束时间"
          >
          </el-date-picker>
        </el-form-item>
        <el-row>
          <el-col :span="18">
            <el-form-item label="告警规则:">
              <el-input v-model="ruleForm.alarmName" disabled></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6" class="ar">
            <el-button
              type="danger"
              size="mini"
              style="margin-top: 5px"
              @click="showModal"
              >选择告警规则</el-button
            >
          </el-col>
        </el-row>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="closeDialog" type="info" size="small"
          >取 消</el-button
        >
        <el-button
          type="danger"
          size="small"
          @click="submitForm('ruleForm')"
          :loading="buttonLoading"
          >保 存</el-button
        >
      </span>
    </el-dialog>

    <el-dialog
      :close-on-click-modal="false"
      title="选择告警规则"
      width="60%"
      :visible.sync="warnListVisible"
    >
      <el-table
        :data="warnData"
        highlight-current-row
        @current-change="handleCurrentRow"
      >
        <el-table-column label="选择" width="60">
          <template slot-scope="scope">
            <el-radio
              @change.native="getCurrentRow(scope.row)"
              :label="scope.$index"
              v-model="radio"
              class="textRadio"
              >&nbsp;</el-radio
            >
          </template>
        </el-table-column>
        <el-table-column
          prop="alarmName"
          label="告警任务名称"
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column label="告警原因" show-overflow-tooltip>
          <template slot-scope="scope">
            <el-tag class="success-tag" v-if="scope.row.alarmReasion == 1"
              >成功</el-tag
            >
            <el-tag class="error-tag" v-if="scope.row.alarmReasion == 0"
              >失败</el-tag
            >
            <el-tag class="going-tag" v-if="scope.row.alarmReasion == 2"
              >成功/失败</el-tag
            >
          </template>
        </el-table-column>
        <el-table-column label="告警方式" show-overflow-tooltip>
          <template slot-scope="scope">
            <span v-if="scope.row.alarmMethod == 1">邮件</span>
            <span v-if="scope.row.alarmMethod == 2">短信</span>
            <span v-if="scope.row.alarmMethod == 3">邮件/短信</span>
          </template>
        </el-table-column>
        <el-table-column
          prop="receivePeople"
          label="接收人"
          show-overflow-tooltip
        >
        </el-table-column>
      </el-table>
      <!-- <el-pagination
        class="ar mt15"
        background
        @current-change="handleCurrentChange"
        @size-change="handleSizeChange"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="pageLength"
        :current-page="page"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
      >
      </el-pagination> -->
      <div slot="footer" class="dialog-footer">
        <el-button @click="closeAlarmDialog" type="info" size="small"
          >取 消</el-button
        >
        <el-button type="danger" size="small" @click="saveDataSource"
          >保 存</el-button
        >
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { changeUTCtoDateTime } from "@/common/common_tools.js";
export default {
  name: "",
  components: {},
  props: ["EditDispatchTaskVisible", "isEditRelation"],
  data() {
    const getTimeList = _ => {
      let Arr = [];
      for (let i = 0; i <= 59; i++) {
        Arr.push({
          value: i,
          label: i
        });
      }
      return Arr;
    };
    return {
      buttonLoading: false,
      total: 0,
      page: 1,
      pageLength: 10,
      radio: "",
      ruleForm: {
        id: 0,
        taskName: "",
        alarmName: "",
        alarmId: "",
        taskType: "",
        status: "",
        datasourceId: "",
        metadataMenuId: "",
        reMetadataMenuId: [],
        triggerTime: "",
        effectTimeRange: [],
        cycleType: "0",
        jobCron: "",
        exeDate1: {
          value1: "",
          value2: ""
        },
        exeDate2: {
          value1: "",
          value2: ""
        },
        exeDate3: {
          value1: ""
        },
        exeDate4: {
          value1: ""
        }
      },

      CycleSelected: "",

      warnListVisible: false,
      warnData: [],
      sourceRowData: {},

      timeOptions: getTimeList(),

      rules: {
        taskName: [
          { required: true, message: "请填写任务名称", trigger: "blur" }
        ],
        datasourceId: [
          { required: true, message: "请选择数据源", trigger: "change" }
        ],
        reMetadataMenuId: [
          { required: true, message: "请选择挂载目录", trigger: "change" }
        ],
        cycleType: [
          { required: true, message: "请选择调度方式", trigger: "change" }
        ],
        jobCron: [
          { required: true, message: "请设置执行频率", trigger: "change" },
          { validator: this.checkjobCron, trigger: 'blur' }
        ],
        triggerTime: [
          { required: true, message: "请选择执行时间", trigger: "change" }
        ],
        effectTimeRange: [
          { required: true, message: "请选择生效时间", trigger: "blur" }
        ]
      },
      allUser: []
    };
  },
  methods: {
    // 校验采集频率
    checkjobCron (rule, value, callback) {
        if (!value) {
            return callback(new Error('请选择采集频率'))
        }
        if (value === '0') {
            if (!this.ruleForm.exeDate1.value1 || !this.ruleForm.exeDate1.value2) {
                return callback(new Error('采集频率请填写完整'))
            } else {
                callback()
            }
        } else if (value === '1') {
            if (!this.ruleForm.exeDate2.value1 || !this.ruleForm.exeDate2.value2) {
                return callback(new Error('采集频率请填写完整'))
            } else {
                callback()
            }
        } else if (value === '2') {
            if (!this.ruleForm.exeDate3.value1) {
                return callback(new Error('采集频率请填写完整'))
            } else {
                callback()
            }
        } else if (value === '3') {
            if (!this.ruleForm.exeDate4.value1 || !this.ruleForm.exeDate4.value1.length) {
                return callback(new Error('采集频率请填写完整'))
            } else {
                callback()
            }
        }
    },
    setCycle(cb) {
      this.CycleSelected = cb;
    },
    handleChange(val) {
      this.ruleForm.exeDate1.value1 = val;
    },
    showModal() {
      this.warnListVisible = true;
      this.updateTable();
    },
    handleCurrentRow(newRow, oldRow) {
      this.sourceRowData = newRow;
      this.radio = newRow.count;
    },
    getCurrentRow(data) {
      this.sourceRowData = data;
    },
    handleCurrentChange(val) {
      this.page = val;
      this.radio = "";
      this.updateTable();
    },
    handleSizeChange(val) {
      this.pageLength = val;
      this.updateTable();
    },
    updateTable() {
      let param = {
        start: (this.page - 1) * this.pageLength,
        length: this.pageLength
      };
      this.$urlApi.dispatchTask.queryAlarmList(param).then(async res => {
        res.data.forEach(item => {
          if (item.status === 1) {
            item.status = true;
          } else {
            item.status = false;
          }
          if (res.code === "200") {
            let count = 0;
            res.data.forEach(item => {
              item.count = count++;
            });
          }
        });
        // 回显选中
        if (this.ruleForm.alarmName) {
          const selectedAlarm = res.data.find(c => c.alarmName === this.ruleForm.alarmName);
          this.radio = selectedAlarm.count
        }
        const data = await this.$urlApi.dispatchTask.getUserList({pageNumber: 1, pageSize: 10000000})
        const allUser = data.data;
        res.data.forEach(c => {
          const a = c.receivePeople.split(',').map(k => +k);
          const b = []
          a.forEach(i => {
            const x = allUser.find(j => i === j.id)
            if (!!x) {
              b.push(x.username)
            }
          })
          c.receivePeople = b.join(',')
        })
        this.warnData = res.data;
        this.total = res.recordsFiltered;
      });
    },
    saveDataSource() {
      this.ruleForm.alarmName = this.sourceRowData.alarmName;
      this.ruleForm.alarmId = this.sourceRowData.id;
      this.warnListVisible = false;
    },
    closeAlarmDialog() {
      // this.ruleForm.alarmName = "";
      // this.ruleForm.alarmId = "";
      this.warnListVisible = false;
    },
    getDataById() {
      this.$urlApi.dispatchTask
        .getDispatchTaskById({ id: this.isEditRelation.id })
        .then(res => {
          let row = res.data;
          this.ruleForm.cycleType = row.cycleType;
          this.CycleSelected = row.jobCron;
          this.ruleForm.taskName = row.taskName;
          this.ruleForm.alarmName = row.alarmName;
          if (row.taskType == 0) {
            this.ruleForm.taskType = "采集任务";
          } else {
            this.ruleForm.taskType = "质量任务";
          }
          this.dataSourceName = row.dataSourceName;

          this.ruleForm.jobCron = row.jobCron;
          this.ruleForm.status = row.status;
          if (this.ruleForm.cycleType == "1") {
            switch (row.jobCron) {
              case "0":
                this.ruleForm.exeDate1.value1 = row.triggerTime.substring(
                  0,
                  row.triggerTime.indexOf(":")
                );
                this.ruleForm.exeDate1.value2 = row.triggerTime.substring(
                  row.triggerTime.indexOf(":") + 1,
                  row.triggerTime.length
                );
                break;
              case "1":
                this.ruleForm.exeDate2.value1 = row.triggerTime.substring(
                  0,
                  row.triggerTime.indexOf(":")
                );
                this.ruleForm.exeDate2.value2 = row.triggerTime.substring(
                  row.triggerTime.indexOf(":") + 1,
                  row.triggerTime.length
                );
                break;
              case "2":
                this.ruleForm.exeDate3.value1 = row.triggerTime;
                break;
              case "3":
                this.ruleForm.exeDate4.value1 = row.triggerTime.split(",");
                break;
            }
            this.ruleForm.effectTimeRange[0] = changeUTCtoDateTime(
              "yyyy-MM-dd HH:mm:ss",
              row.startTime
            );
            this.ruleForm.effectTimeRange[1] = changeUTCtoDateTime(
              "yyyy-MM-dd HH:mm:ss",
              row.endTime
            );
          } else {
            this.ruleForm.triggerTime = row.triggerTime;
          }
        });
    },
    clearData() {
      this.ruleForm.alarmName = "";
      this.ruleForm.triggerTime = "";
      this.ruleForm.cycleType = "";
      this.ruleForm.reMetadataMenuId = [];
      this.ruleForm.exeDate1 = {
        value1: "",
        value2: ""
      };
      this.ruleForm.exeDate2 = {
        value1: "",
        value2: ""
      };
      this.ruleForm.exeDate3 = {
        value1: ""
      };
      this.ruleForm.exeDate4 = {
        value1: ""
      };
      this.radio = "";
    },
    submitForm(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          let param = {};
          if (this.ruleForm.cycleType === "0") {
            param.jobCron = 4;
            param.triggerTime = this.ruleForm.triggerTime;
          } else {
            switch (this.ruleForm.jobCron) {
              case "0":
                param.triggerTime =
                  this.ruleForm.exeDate1.value1 +
                  ":" +
                  this.ruleForm.exeDate1.value2;
                break;
              case "1":
                param.triggerTime =
                  this.ruleForm.exeDate2.value1 +
                  ":" +
                  this.ruleForm.exeDate2.value2;
                break;
              case "2":
                param.triggerTime = this.ruleForm.exeDate3.value1;
                break;
              case "3":
                param.triggerTime = this.ruleForm.exeDate4.value1.join(",");
                break;
            }
            param.jobCron = this.ruleForm.jobCron;
            param.startTime = this.ruleForm.effectTimeRange[0];
            param.endTime = this.ruleForm.effectTimeRange[1].replace('00:00:00', '23:59:59');

            if ((+new Date(param.endTime)) - (+new Date()) < 0) {
              return this.$toast(
                "error",
                "当前调度结束时间必须大于当前时间！"
              );
            }
          }
          param.cycleType = this.ruleForm.cycleType;
          param.id = this.ruleForm.id;
          param.alarmId = this.ruleForm.alarmId;
          param.taskName = this.ruleForm.taskName;
          if (this.ruleForm.taskType === "采集任务") {
            param.taskType = "0";
          } else {
            param.taskType = "1";
          }
          param.status = this.ruleForm.status;
          this.buttonLoading = true;
          this.$urlApi.dispatchTask
            .dispatchTaskConfig(param)
            .then(res => {
              this.buttonLoading = false;
              this.$toast("success", "操作成功");
              this.clearData();
              this.$emit("EditDispatchTaskVisibleBack", 1);
            })
            .catch(e => {
              this.buttonLoading = false;
            });
        } else {
          return false;
        }
      });
    },
    closeDialog() {
      this.$emit("EditDispatchTaskVisibleBack", 0);
      this.clearData();
    },
    openDialog() {}
  },
  mounted: function() {},
  watch: {
    isEditRelation: {
      handler(val, oldVal) {
        this.updateTable();
        if (val.id != -1) {
          this.ruleForm.id = val.id;
          this.getDataById();
        }
      },
      deep: true
    }
  }
};
</script>

<style lang="scss">
.edit-task {
  .el-dialog__body {
    .business-basic-t {
      height: 90px;
      padding: 15px 10px;
      line-height: 30px;
    }
    .business-basic-b {
      height: -moz-calc(100% - 100px);
      height: -webkit-calc(100% - 100px);
      height: calc(100% - 100px);
      .el-table {
        td,
        .el-table th {
          padding: 6px 0;
        }
      }
    }
  }
}
</style>
