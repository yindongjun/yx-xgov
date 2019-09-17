<template>
  <el-dialog
    title="质量任务配置"
    :visible.sync="qualityTaskVisible"
    :before-close="closeDialog"
    class="set-quality-task"
    :lock-scroll="false"
    width="760px"
    @open="openOuter"
    :close-on-click-modal="false"
  >
    <el-dialog
      :close-on-click-modal="false"
      width="760px"
      title="选择数据表"
      :visible.sync="innerVisible"
      class="inner-qualityTask"
      @open="openInner"
      :before-close="closeInner"
      append-to-body
    >
      <el-form
        :inline="true"
        :model="formInline"
        size="small"
        class="demo-form-inline"
      >
        <el-form-item label="所属数据源">
          <el-select
            :disabled="isConfTable"
            v-model="formInline.dataSource"
            @change="changeTableByBase"
          >
            <el-option
              :label="item.datasourceName"
              :value="item.id"
              :key="item.id"
              v-for="item in dataList"
            ></el-option>
          </el-select>
          <span v-if="isConfTable" style="color:#F06273">
            当前源无可配置的表</span
          >
        </el-form-item>
      </el-form>
      <div class="selectDatasource" style="font-size: 13px;">
        <p style="font-weight:bold">选择数据表</p>
        <el-transfer
          filterable
          filter-placeholder="请输入表名称"
          :titles="titles"
          v-model="selectedTable"
          :data="sourceAllTable"
        >
        <div slot-scope="{ option }" style="width: 130px; overflow: hidden;text-overflow: ellipsis;white-space: nowrap;">
          <label :title="option.label">{{ option.label }}</label>
        </div>
        </el-transfer>
        <p>
          <span style="color:#F06273"> * </span>
          此列表只展示配置了质量规则设计的数据表
        </p>
      </div>

      <span slot="footer" class="dialog-footer">
        <el-button type="info" size="small" @click="cancelInner"
          >取 消</el-button
        >
        <el-button type="danger" size="small" @click="submitInner"
          >保 存</el-button
        >
      </span>
    </el-dialog>

    <el-form
      :model="baseForm"
      ref="baseForm"
      :rules="rules"
      size="small"
      label-width="100px"
      label-position="right"
    >
      <el-form-item label="任务名称" prop="taskName">
        <el-input
          v-model="baseForm.taskName"
          maxlength="20"
          placeholder="最大长度为20个字符"
        ></el-input>
      </el-form-item>
      <el-form-item label="待稽核数据表" prop="tableName">
        <el-col :span="20">
          <el-button class="grey-btn" @click="selectTable">
            <span
              ><svg-icon
                :iconClass="selectedData.length ? '对号-o' : '对号-u'"
              ></svg-icon
            ></span>
            <span>{{ selectedData.length ? "已选择" : "请选择" }}</span>
          </el-button>
          <span
            :title="currentSourceName"
            style="font-size: 12px;margin-left: 20px"
            >{{ currentSourceName }}</span
          >
        </el-col>
      </el-form-item>
      <el-form-item label="调度规则" prop="cycleType">
        <el-radio-group v-model="baseForm.cycleType">
          <el-radio label="0">单次调度</el-radio>
          <el-radio label="1">周期性调度</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item
        label="执行时间"
        v-if="baseForm.cycleType === '0'"
        prop="triggerTime"
      >
        <el-date-picker
          v-model="baseForm.triggerTime"
          size="small"
          type="datetime"
          value-format="timestamp"
          placeholder="选择执行时间"
        >
        </el-date-picker>
      </el-form-item>
      <el-form-item
        label="执行频率"
        v-if="baseForm.cycleType === '1'"
        prop="jobCron"
      >
        <el-col :span="6">
          <el-select
            v-model="baseForm.jobCron"
            placeholder="请设置执行周期"
            @change="setCycle"
          >
            <el-option v-show="false" label="请选择频率" value="4"></el-option>
            <el-option label="按月执行" value="0"></el-option>
            <el-option label="按周执行" value="1"></el-option>
            <el-option label="按天执行" value="2"></el-option>
            <el-option label="按小时执行" value="3"></el-option>
          </el-select>
        </el-col>
        <el-col :span="17" class="year ml5" v-if="CycleSelected === '0'">
          <el-col :span="8" class="mr5">
            <el-input-number
              v-model="baseForm.exeDate1.value1"
              @change="handleChange"
              :min="1"
              :max="31"
              label="设置执行日"
            ></el-input-number>
          </el-col>
          <el-col :span="13">
            <el-time-picker
              v-model="baseForm.exeDate1.value2"
              placeholder="任意时间点"
              value-format="HH:mm:ss"
            >
            </el-time-picker>
          </el-col>
        </el-col>
        <el-col :span="16" class="year ml5" v-if="CycleSelected === '1'">
          <el-col :span="8">
            <el-select
              v-model="baseForm.exeDate2.value1"
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
              v-model="baseForm.exeDate2.value2"
              placeholder="任意时间点"
              value-format="HH:mm:ss"
            >
            </el-time-picker>
          </el-col>
        </el-col>
        <el-col :span="16" class="year ml5" v-if="CycleSelected === '2'">
          <el-col :span="6">
            <el-time-picker
              v-model="baseForm.exeDate3.value1"
              placeholder="任意时间点"
              value-format="HH:mm:ss"
            >
            </el-time-picker>
          </el-col>
        </el-col>
        <el-col :span="16" class="year ml5" v-if="CycleSelected === '3'">
          <el-select
            size="small"
            v-model="baseForm.exeDate4.value1"
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
        v-if="baseForm.cycleType === '1'"
        prop="effectTimeRange"
      >
        <el-date-picker
          v-model="baseForm.effectTimeRange"
          type="daterange"
          value-format="yyyy-MM-dd HH:mm:ss"
          size="small"
          :editable='false'
          start-placeholder="开始时间"
          end-placeholder="结束时间"
        >
        </el-date-picker>
      </el-form-item>

      <el-form-item label="任务描述" prop="desc">
        <el-input
          type="textarea"
          :rows="3"
          placeholder="请输入内容"
          v-model="baseForm.desc"
        >
        </el-input>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button type="info" size="small" @click="closeDialog">取 消</el-button>
      <el-button
        type="danger"
        size="small"
        @click="submitForm('baseForm')"
        :loading="buttonLoading"
        >保存</el-button
      >
    </span>
  </el-dialog>
</template>

<script>
import { changeUTCtoDateTime } from "@/common/common_tools.js";

export default {
  name: "",
  props: ["qualityTaskVisible", "isqualityTask"],
  components: {},
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

      timeOptions: getTimeList(),
      baseForm: {
        taskName: "",
        desc: "",
        tableName: "1111",
        triggerTime: "",
        tableId: "",

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
        },
        effectTimeRange: []
      },
      formInline: {
        dataSource: ""
      },

      rules: {
        taskName: [
          { required: true, message: "请填写任务名称", trigger: "blur" }
        ],
        tableName: [{ required: true, message: "请选择稽核表" }],
        cycleType: [
          { required: true, message: "请选择调度规则", trigger: "change" }
        ],
        triggerTime: [
          { required: true, message: "请选择执行时间", trigger: "change" }
        ],
        jobCron: [
          { required: true, message: "请设置执行频率", trigger: "change" },
          { validator: this.checkjobCron, trigger: 'blur' }
        ],
        effectTimeRange: [
          { required: true, message: "请选择生效时间", trigger: "blur" }
        ],
        desc: [
          {
            min: 1,
            max: 255,
            message: "长度在 1 到 255 个字符",
            trigger: "blur"
          }
        ]
      },
      innerVisible: false,
      tableData: [],
      radio: "",
      currentRow: "",
      currBaseId: "",

      dataList: [],
      CycleSelected: "",
      titles: ["待选择数据表", "已选择数据表"],
      selectedTable: [],
      selectedData: [],
      sourceAllTable: [],
      isConfTable: false,
      currentSourceName: "",
      pickerOptions: {
        disabledDate(time) {
          return time.getTime() + 86400000 < Date.now();
        }
      }
    };
  },
  methods: {
    // 校验采集频率
    checkjobCron (rule, value, callback) {
        if (!value) {
            return callback(new Error('请选择采集频率'))
        }
        if (value === '0') {
            if (!this.baseForm.exeDate1.value1 || !this.baseForm.exeDate1.value2) {
                return callback(new Error('采集频率请填写完整'))
            } else {
                callback()
            }
        } else if (value === '1') {
            if (!this.baseForm.exeDate2.value1 || !this.baseForm.exeDate2.value2) {
                return callback(new Error('采集频率请填写完整'))
            } else {
                callback()
            }
        } else if (value === '2') {
            if (!this.baseForm.exeDate3.value1) {
                return callback(new Error('采集频率请填写完整'))
            } else {
                callback()
            }
        } else if (value === '3') {
            if (!this.baseForm.exeDate4.value1 || !this.baseForm.exeDate4.value1.length) {
                return callback(new Error('采集频率请填写完整'))
            } else {
                callback()
            }
        }
    },
    closeDialog() {
      this.$refs["baseForm"].resetFields();
      this.baseForm.desc = "";
      this.formInline.dataSource = "";
      this.baseForm.exeDate1.value1 = "";
      this.baseForm.exeDate1.value2 = "";
      this.baseForm.exeDate2.value1 = "";
      this.baseForm.exeDate2.value2 = "";
      this.baseForm.exeDate3.value1 = "";
      this.baseForm.exeDate4.value1 = "";
      this.baseForm.triggerTime = "";
      this.baseForm.jobCron = "";
      this.$emit("qualityTaskBack");
      this.selectedTable = [];
      this.selectedData = [];
      this.sourceAllTable = [];
      this.currentSourceName = "";
    },
    getDataById(id) {
      this.$urlApi.dataQuality
        .qualitytaskdetail({ taskInfoId: id })
        .then(res => {
          let row = res.data;
          this.baseForm.cycleType = row.cycleType;
          this.CycleSelected = row.jobCron;
          this.baseForm.taskName = row.taskName;
          this.baseForm.tableId = row.id;
          this.baseForm.jobCron = row.jobCron;
          this.baseForm.desc = row.description;
          this.selectedData = row.tableInfoIds.split(",").map(c => +c);
          if (this.baseForm.cycleType == "1") {
            switch (row.jobCron) {
              case "0":
                this.baseForm.exeDate1.value1 = row.triggerTime.substring(
                  0,
                  row.triggerTime.indexOf(":")
                );
                this.baseForm.exeDate1.value2 = row.triggerTime.substring(
                  row.triggerTime.indexOf(":") + 1,
                  row.triggerTime.length
                );
                break;
              case "1":
                this.baseForm.exeDate2.value1 = row.triggerTime.substring(
                  0,
                  row.triggerTime.indexOf(":")
                );
                this.baseForm.exeDate2.value2 = row.triggerTime.substring(
                  row.triggerTime.indexOf(":") + 1,
                  row.triggerTime.length
                );
                break;
              case "2":
                this.baseForm.exeDate3.value1 = row.triggerTime;
                break;
              case "3":
                this.baseForm.exeDate4.value1 = row.triggerTime.split(",");
                break;
            }
            this.baseForm.effectTimeRange[0] = changeUTCtoDateTime(
              "yyyy-MM-dd HH:mm:ss",
              row.startTime
            );
            this.baseForm.effectTimeRange[1] = changeUTCtoDateTime(
              "yyyy-MM-dd HH:mm:ss",
              row.endTime
            );
          } else {
            this.baseForm.triggerTime = row.triggerTime;
          }
        });
    },
    openOuter() {
      if (this.isqualityTask.id !== "") {
        this.getDataById(this.isqualityTask.id);
        this.currentSourceName = this.isqualityTask.row.datasourceName;
      }
    },
    submitForm(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          let param = {};
          if (this.isqualityTask.id) {
            param.id = this.isqualityTask.id;
          }
          if (this.baseForm.cycleType === "0") {
            param.jobCron = 4;
            param.triggerTime = this.baseForm.triggerTime;
          } else {
            switch (this.baseForm.jobCron) {
              case "0":
                param.triggerTime =
                  this.baseForm.exeDate1.value1 +
                  ":" +
                  this.baseForm.exeDate1.value2;
                break;
              case "1":
                param.triggerTime =
                  this.baseForm.exeDate2.value1 +
                  ":" +
                  this.baseForm.exeDate2.value2;
                break;
              case "2":
                param.triggerTime = this.baseForm.exeDate3.value1;
                break;
              case "3":
                param.triggerTime = this.baseForm.exeDate4.value1.join(",");
                break;
            }
            param.jobCron = this.baseForm.jobCron;
            param.startTime = this.baseForm.effectTimeRange[0];
            param.endTime = this.baseForm.effectTimeRange[1].replace('00:00:00', '23:59:59');

            if ((+new Date(param.endTime)) - (+new Date()) < 0) {
              return this.$toast(
                "error",
                "当前调度结束时间必须大于当前时间！"
              );
            }
          }
          param.taskName = this.baseForm.taskName;
          param.tableIds = this.selectedData.join(",");
          param.cycleType = this.baseForm.cycleType;
          param.description = this.baseForm.desc;
          if (!this.selectedData.length) {
            this.$toast("warning", "请选择待稽核数据表后再保存");
          } else {
            this.buttonLoading = true;
            this.$urlApi.dataQuality
              .saveDesignTasks(param)
              .then(res => {
                this.buttonLoading = false;
                this.$emit("qualityTaskBack");
                this.$refs["baseForm"].resetFields();
                this.baseForm.desc = "";
                this.baseForm.exeDate1.value1 = "";
                this.baseForm.exeDate1.value2 = "";
                this.baseForm.exeDate2.value1 = "";
                this.baseForm.exeDate2.value2 = "";
                this.baseForm.exeDate3.value1 = "";
                this.baseForm.exeDate4.value1 = "";
                this.baseForm.triggerTime = "";
                this.baseForm.jobCron = ""
                this.selectedTable = [];
                this.selectedData = [];
                this.sourceAllTable = [];
                this.currentSourceName = "";
                this.$toast("success", "操作成功");
              })
              .catch(e => {
                this.buttonLoading = false;
              });
          }
        } else {
          return false;
        }
      });
    },
    selectTable() {
      this.innerVisible = true;
    },
    changeTableByBase(cb) {
      this.sourceAllTable = [];
      this.selectedTable = [];
      this.currBaseId = cb;
      this.getList();
    },
    onSubmit() {
      this.getList();
    },
    getList(cb) {
      let param = {
        sourceId: this.currBaseId,
        taskId: this.isqualityTask.id || -1
      };
      this.$urlApi.dataQuality.findTaskTables(param).then(res => {
        this.sourceAllTable = [...res.data, ...res.tables].map(c => ({
          key: c.designId,
          label: c.tableName
        }));
        this.selectedTable = [];
      });
    },
    openInner() {
      this.sourceAllTable = [];
      this.selectedTable = [];
      this.$urlApi.dataQuality.sourceTask().then(res => {
        this.dataList = res.data;
        if (this.isqualityTask.id) {
          if (
            !this.dataList.find(
              c => c.id === this.isqualityTask.row.datasourceId
            )
          ) {
            this.formInline.dataSource = "";
            this.isConfTable = true;
          } else {
            this.formInline.dataSource = this.isqualityTask.row.datasourceId;
            let param = {
              sourceId: this.formInline.dataSource,
              taskId: this.isqualityTask.id || -1
            };
            this.$urlApi.dataQuality.findTaskTables(param).then(res => {
              this.sourceAllTable = [...res.data, ...res.tables].map(c => ({
                key: c.designId,
                label: c.tableName
              }));
              this.selectedTable = this.selectedData.length
                ? this.selectedData
                : res.tables.map(c => c.designId);
            });
          }
        } else {
          this.formInline.dataSource = this.dataList[0].id;
          let param = {
            sourceId: this.formInline.dataSource,
            taskId: this.isqualityTask.id || -1
          };
          this.$urlApi.dataQuality.findTaskTables(param).then(res => {
            this.sourceAllTable = [...res.data, ...res.tables].map(c => ({
              key: c.designId,
              label: c.tableName
            }));
            this.selectedTable = this.selectedData.length
              ? this.selectedData
              : res.tables.map(c => c.designId);
          });
        }
      });
    },
    submitInner() {
      if (this.selectedTable.length === 0) {
        this.$toast("warning", "请选择数据表");
      } else if (this.selectedTable.length > 50) {
        this.$toast("warning", "已选择数据表个数不能超过50个");
      } else {
        this.innerVisible = false;
        this.selectedData = [...this.selectedTable];
        this.currentSourceName = this.dataList.find(
          c => c.id === this.formInline.dataSource
        ).datasourceName;
        this.isConfTable = false;
      }
    },
    cancelInner() {
      this.innerVisible = false;
      this.isConfTable = false;
    },
    closeInner() {
      this.innerVisible = false;
      this.isConfTable = false;
    },
    setCycle(cb) {
      this.CycleSelected = cb;
    },
    handleChange(val) {
      this.baseForm.exeDate1.value1 = val;
    }
  }
};
</script>

<style lang="scss">
.inner-qualityTask {
  .el-form,
  .demo-form-inline .el-form-item {
    margin-bottom: 0px;
  }
}
.selectDatasource {
  .el-transfer-panel
    .el-transfer-panel__header
    .el-checkbox
    .el-checkbox__label {
    font-size: 13px !important;
    color: #3e4456 !important;
  }
  .el-checkbox__label {
    font-size: 13px !important;
  }
  .el-transfer__button {
    border-radius: 50% !important;
  }
  .el-transfer__button,
  .el-transfer__button:hover {
    background-color: #3ba3f8;
    border: 0 none;
  }
  .el-transfer__button.is-disabled,
  .el-transfer__button.is-disabled:hover {
    border: 1px solid #dcdfe6;
    background-color: #f5f7fa;
    color: #c0c4cc;
  }
  .el-transfer-panel__item:hover {
    color: #3ba3f8 !important;
  }
  .el-checkbox__input.is-checked + .el-checkbox__label {
    color: #3ba3f8 !important;
  }
}
</style>
