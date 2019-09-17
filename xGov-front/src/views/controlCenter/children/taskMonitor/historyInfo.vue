<template>
  <el-dialog
    title="执行历史"
    :visible.sync="historyInfoVisible"
    :before-close="closeDialog"
    class="quality-task-info"
    :lock-scroll="false"
    width="760px"
    @open="open"
    :close-on-click-modal="false"
  >
    <el-tabs v-model="tableID" type="card" @tab-click="getLogsByName">
      <el-tab-pane v-for="(item, key) in tableList" :key='key' :label="item.tableName" :name="item.id">
        <!-- <el-table :data='contentData'>
          <el-table-column label='时间' width='150' prop='createTime'></el-table-column>
          <el-table-column label='详情' prop='logDetail' show-overflow-tooltip></el-table-column>
        </el-table> -->
        <ul style="font-family: consolas; line-height: 1.5; font-size: 13px;">
          <li v-for="item in contentData" :key="item.id">
            <span style="margin-right: 10px">{{item.createTime}}</span>
            <span>{{item.logDetail}}</span>
          </li>
        </ul>
      </el-tab-pane>
    </el-tabs>
  </el-dialog>
</template>

<script>
import { changeUTCtoDateTime } from "@/common/common_tools.js";

export default {
  name: 'historyInfo',
  props: ['historyInfoVisible', 'taskID', 'logsId'],
  data() {
    return {
      tableID: '',
      tableList: [],
      contentData: []
    }
  },
  methods: {
    open() {
      this.getTaskTable(this.taskID);
    },
    getHistoryById(options) {
      this.$urlApi.dispatchTask.getLogsByTable({taskHisId: options.id, tableName: options.name}).then(res => {
        this.contentData = res.data.map(c => {
          c.createTime = changeUTCtoDateTime("yyyy-MM-dd HH:mm:ss", c.createTime)
          return c
        });
      });
    },
    getTaskTable(id) {
      this.$urlApi.dataQuality
        .getTaskTables({ taskId: id })
        .then(res => {
          if (res.code === '200') {
            if (res.data.length) {
              this.tableID = res.data[0].id.toString();
              this.getHistoryById({id: this.$props.logsId, name: res.data[0].tableName})
            }
            this.tableList = res.data.map(c => {
              c.id = c.id.toString();
              return c
            });
          }
        })
    },
    getLogsByName(val) {
      this.contentData= []
      const options = {
        id: this.$props.logsId,
        name: val.label
      }
      this.getHistoryById(options)
    },
    closeDialog() {
      this.$emit('historyInfoBack');
      this.contentData= [];
      this.tableID = '';
      this.tableList = [];
    }
  }
}
</script>

