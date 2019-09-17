<template>
  <el-dialog
    title="质量任务详情"
    :visible.sync="taskInfoVisible"
    :before-close="closeDialog"
    class="quality-task-info"
    :lock-scroll="false"
    width="760px"
    @open="open"
    :close-on-click-modal="false"
  >
    <el-form
      :model="taskForm"
      size="small"
      label-width="100px"
      label-position="right"
    >
      <el-form-item label="任务名称：">
        <label style="font-size:12px">{{taskForm.taskName}}</label>
      </el-form-item>
      <el-form-item label="调度规则：">
        <label style="font-size:12px" v-if="taskForm.cycleType === '0'">单次调度</label>
        <label style="font-size:12px" v-if="taskForm.cycleType === '1'">周期性调度</label>
      </el-form-item>
      <el-form-item v-if="taskForm.cycleType === '1'" label="执行频率：">
        <label style="font-size:12px">
          <span v-if="taskForm.jobCron === '3'">按小时执行——{{taskForm.jobCronValue[0] + "   " + taskForm.jobCronValue[1]}}</span>
          <span v-if="taskForm.jobCron === '2'">按天执行——{{taskForm.jobCronValue[0] + "   " + taskForm.jobCronValue[1]}}</span>
          <span v-if="taskForm.jobCron === '1'">按周执行——{{taskForm.jobCronValue[0] + "   " + taskForm.jobCronValue[1]}}</span>
          <span v-if="taskForm.jobCron === '0'">按月执行——{{taskForm.jobCronValue[0] + "号   " + taskForm.jobCronValue[1]}}</span>
        </label>
      </el-form-item>
      <el-form-item v-if="taskForm.cycleType === '0'" label="执行时间：">
        <label style="font-size:12px">{{taskForm.triggerTime}}</label>
      </el-form-item>
      <el-form-item  v-if="taskForm.cycleType === '1'" label="生效时间：">
        <label v-if="taskForm.cycleType === '1'" style="font-size:12px">{{taskForm.effectTimeRange[0]}} ~ {{taskForm.effectTimeRange[1]}}</label>
      </el-form-item>
      <el-form-item label="任务描述：">
        <label style="font-size:12px">{{taskForm.desc}}</label>
      </el-form-item>
    </el-form>
    <div class="regular">
      <div class="title" style="">规则详情：</div>
      <div class="taskinfo">
        <el-tabs v-model="tableID" type="card" @tab-click="getTaskTableById">
          <el-tab-pane v-for="(item, key) in taskTableInfos" :key='key' :label="item.tableName" :name="item.id">
            <el-table :data="fieldsTableData">
              <el-table-column label="字段名称" prop="fieldName"></el-table-column>
              <el-table-column label="规则名称" show-overflow-tooltip prop="fieldRegular"></el-table-column>
            </el-table>
          </el-tab-pane>
        </el-tabs>
      </div>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button type="info" size="small" @click="closeDialog">关 闭</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { changeUTCtoDateTime } from "@/common/common_tools.js";

export default {
  name: 'task-info',
  props: ['taskInfoVisible', 'taskID'],
  data() {
    return {
      taskForm: {
        taskName: '',
        cycleType: '',
        jobCron: '',
        triggerTime: '',
        jobCronValue: [],
        effectTimeRange: [],
        desc: '',
      },
      taskTableInfos: [],
      fieldsTableData: [],
      tableID: ''
    }
  },
  methods: {
    open() {
      this.getTaskInfo(this.taskID);
      this.getTaskTable(this.taskID);
    },
    getTaskInfo(id) {
      this.$urlApi.dataQuality
        .qualitytaskdetail({ taskInfoId: id })
        .then(res => {
          let row = res.data;
          this.taskForm.cycleType = row.cycleType;
          this.taskForm.taskName = row.taskName;
          this.taskForm.jobCron = row.jobCron;
          this.taskForm.desc = row.description;

          if (this.taskForm.cycleType == "1") {
            switch (row.jobCron) {
              case "0":
                this.taskForm.jobCronValue[0] = row.triggerTime.substring(
                  0,
                  row.triggerTime.indexOf(":")
                );
                this.taskForm.jobCronValue[1] = row.triggerTime.substring(
                  row.triggerTime.indexOf(":") + 1,
                  row.triggerTime.length
                );
                break;
              case "1":
                const week = row.triggerTime.substring(
                  0,
                  row.triggerTime.indexOf(":")
                );
                const weeks = {SUN: '周日', MON: '周一', TUE: '周二', WED: '周三', THU: '周四', FRI: '周五', SAT: '周六'}
                this.taskForm.jobCronValue[0] = weeks[week]
                this.taskForm.jobCronValue[1] = row.triggerTime.substring(
                  row.triggerTime.indexOf(":") + 1,
                  row.triggerTime.length
                );
                break;
              case "2":
                this.taskForm.jobCronValue[0] = row.triggerTime;
                this.taskForm.jobCronValue[1] = '';
                break;
              case "3":
                this.taskForm.jobCronValue[0] = row.triggerTime.split(",");
                this.taskForm.jobCronValue[1] = '';
                break;
            }
            this.taskForm.effectTimeRange[0] = changeUTCtoDateTime(
              "yyyy-MM-dd HH:mm:ss",
              row.startTime
            );
            this.taskForm.effectTimeRange[1] = changeUTCtoDateTime(
              "yyyy-MM-dd HH:mm:ss",
              row.endTime
            );
          } else {
            this.taskForm.triggerTime = changeUTCtoDateTime(
              "yyyy-MM-dd HH:mm:ss",
              row.triggerTime
            );;
          }
        });
    },
    getTaskTableById(tab) {
      this.$urlApi.dataQuality
        .getTaskTablesDetail({ taskId: this.taskID, tableName: tab.label })
        .then(res => {
          if (res.code === '200') {
            this.fieldsTableData = res.data;
          }
        })
    },
    getTaskTable(id) {
      this.$urlApi.dataQuality
        .getTaskTables({ taskId: id })
        .then(res => {
          if (res.code === '200') {
            if (res.data.length) {
              this.tableID = res.data[0].id.toString();
              this.getTaskTableById({label: res.data[0].tableName})
            }
            this.taskTableInfos = res.data.map(c => {
              c.id = c.id.toString();
              return c
            });
          }
        })
    },
    closeDialog() {
      this.$emit('taskInfoBack');
      this.taskTableInfos = [];
      this.fieldsTableData = [];
      this.tableID = '';
      this.taskForm.taskName = '';
      this.taskForm.cycleType = '';
      this.taskForm.jobCron = [];
      this.taskForm.effectTimeRange = [];
      this.taskForm.desc = '';
    }
  }
}
</script>

<style lang="scss" scoped>
.quality-task-info {
  .regular {
    .title {
      width: 88px;
      margin-bottom: 20px;
      text-align: right;
      font-size: 12px
    }
  }
}
</style>

