<template>
  <el-dialog
    :visible.sync="ruleDetail"
    :lock-scroll="false"
    fullscreen
    class="ruleDetail-dialog"
    @open="openRuleDetailDialog"
    :before-close="closeDialog"
    v-loading="loading"
    :close-on-click-modal="false"
  >
    <span slot="title">
      <a class="ruleDetail-dialog-title" href="#/quality/default/">数据质量</a>
      <a class="ruleDetail-dialog-title" href="javascript:;">/</a>
      <a class="ruleDetail-dialog-title" href="#/quality/default/">规则设计</a>
      <a class="ruleDetail-dialog-title" href="javascript:;">/</a>
      <a class="ruleDetail-dialog-title" href="#/quality/default/design/list"
        >质量规则</a
      >
    </span>
    <section class="rule-design-detail">
      <el-row class="rule-t">
        <div class="rule-t-l">
          <el-input
            placeholder="按照数据表名查询"
            class="search-input mb10"
            size="small"
            v-model="searchName"
          >
            <i slot="prefix" class="el-input__icon el-icon-search"></i>
          </el-input>
          <div class="rule-t-l-search mb10">
            <el-tooltip
              class="item"
              effect="dark"
              content="刷新"
              placement="top"
            >
              <span class="tree-t-set" @click="refreshTable"
                ><svg-icon iconClass="g-更新"></svg-icon
              ></span>
            </el-tooltip>
          </div>
          <div class="rule-table-list">
            <vue-scroll>
              <div
                ref="listInfo"
                class="list-info"
                :style="
                  debuggerFailedTable.find(c => c === item.tableName)
                    ? 'background: #FFE6E9'
                    : ''
                "
                :key="item + index + '111'"
                v-for="(item, index) in Newitems"
                @click="previewData(item, index)"
              >
                <span
                  v-if="item.status == 0"
                  :class="[{ statusNo: item.status == 0 }, 'data-status']"
                  ><i></i> 草稿</span
                >
                <span
                  v-if="item.status == 4"
                  :class="[{ statusLoading: item.status == 4 }, 'data-status']"
                  ><i></i> 变更中</span
                >
                <span
                  v-if="item.status == 3"
                  :class="[{ statusWait: item.status == 3 }, 'data-status']"
                  ><i></i> 待配置</span
                >
                <span
                  v-if="item.status == 2"
                  :class="[{ statusSuccess: item.status == 2 }, 'data-status']"
                  ><i></i> 已提交</span
                >

                <span :title="item.tableName" class="table-name over">{{
                  item.tableName
                }}</span>

                <el-tooltip
                  v-if="item.status == 2"
                  class="item"
                  effect="dark"
                  content="退回草稿"
                  placement="right"
                >
                  <span
                    style="font-size: 16px;display: inline-block"
                    v-if="item.status == 2"
                    @click="backDraft(item)"
                    type="text"
                    class="table-icon"
                    size="mini"
                    ><svg-icon iconClass="退回"></svg-icon
                  ></span>
                </el-tooltip>
                <el-tooltip
                  v-if="item.status == 3 && item.ruleId"
                  class="item"
                  effect="dark"
                  content="生成质量任务"
                  placement="right"
                >
                  <span
                    v-if="item.status == 3 && item.ruleId"
                    @click="createQuality(item)"
                    type="text"
                    class="table-icon"
                    size="mini"
                    ><svg-icon iconClass="nav2-3"></svg-icon
                  ></span>
                </el-tooltip>
                <span
                  title="调试失败"
                  style="color:#F67F89; margin-left: 5px;"
                  v-if="debuggerFailedTable.find(c => c === item.tableName)"
                  type="text"
                  class="table-icon"
                  size="mini"
                  ><i  class="el-icon-warning"></i
                ></span>
              </div>
            </vue-scroll>
          </div>
        </div>
        <div class="rule-t-c">
          <div class="rule-detail-title">
            <p style="margin: 0; padding: 0;float: left">
              稽核数据源 {{ dataSourceName }}
            </p>
            <el-dropdown
              style="float: right;margin-right: 10px"
              @command="handleCommand"
            >
              <el-button type="danger" class="btn-debuf-red"
                >自动调试并保存</el-button
              >
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item
                  :style="status == 0 ? '' : 'cursor: not-allowed'"
                  command="current"
                >
                  <div class="auto_debugger">当前表调试</div>
                </el-dropdown-item>
                <el-dropdown-item
                  :style="
                    Newitems.some(c => c.status == 0)
                      ? ''
                      : 'cursor: not-allowed'
                  "
                  command="batch"
                >
                  <div class="auto_debugger">批量表调试</div>
                </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </div>
          <div class="rule-t-r">
            <p class="rule-title">校验组件</p>
            <ul class="rule-item">
              <p style="width: 100%; cursor:pointer; user-select:none; margin: 10px 0" @click="clickComponent">
                <i style="color: #ACB5C3" v-if="componentStatus" class="el-icon-caret-right"></i>
                <i style="color: #ACB5C3" v-else class="el-icon-caret-bottom"></i>
                 通用规则</p>
              <div ref="component" v-if="!componentStatus">
                <li
                  draggable="true"
                  @dragstart="drag($event, item.type)"
                  @dragend="dragend($event, item.type)"
                  :key="item.type + index + '444'"
                  v-for="(item, index) in modelList"
                >
                  <span :title="item.name"
                    ><svg-icon :iconClass="item.icon"></svg-icon></span
                  ><br />
                  <span :title="item.name" style="font-size:12px">{{item.name.length > 4 ? (item.name.substr(0, 4) + '...') : item.name}}</span>
                </li>
              </div>
              <p style="width: 100%; cursor:pointer; user-select:none; margin: 10px 0" @click="clickCustom">
                <i style="color: #ACB5C3" v-if="customStatus" class="el-icon-caret-right"></i>
                <i style="color: #ACB5C3" v-else class="el-icon-caret-bottom"></i>
                 自定义规则</p>
            </ul>
          </div>
          <div class="drop-container">
            <div class="drop-t" @click="setTableInfo">
              {{ isTableRelation.tableName }}
            </div>
            <div
              class="drop-b"
              @dragover="allowDrop($event)"
              @drop="drop($event)"
            >
              <el-scrollbar style="height: 100%">
                <draggable element="ul" v-model="itemList" @start="dragStart">
                  <li
                    v-for="(item, index) in itemList"
                    :key="item.name + index + '222'"
                  >
                    <span>
                      <span @click="setItemInfo(item)">{{ item.name }}</span>
                      <span style="float: right; margin-right: 5px;cursor: pointer" v-if="[31,32,33].find(c => c === item.type) && (status == 0)" @click="deleteItem(item)"><svg-icon iconClass="删除"></svg-icon></span>
                    </span>
                  </li>
                  <li class="dragYindao">拖动校验组件到此区域</li>
                </draggable>
              </el-scrollbar>
            </div>
          </div>
        </div>
      </el-row>
      <el-row class="rule-c">
        <el-tabs v-model="activeName2" type="card" @tab-click="handleClick">
          <el-tab-pane label="数据预览" name="first">
            <el-table
              class="tb-edit"
              highlight-current-row
              height="300"
              :data="tableData"
              style="width: 100%"
              v-loading="isLoadingTable"
            >
              <template v-for="(col, index) in cols">
                <el-table-column
                  :key="index + col.prop + '333'"
                  :show-overflow-tooltip="true"
                  :prop="col.prop"
                  :render-header="labelHeader"
                  :label="col.label"
                >
                  <template slot-scope="scope">
                    {{ scope.row[scope.column.property] }}
                  </template>
                </el-table-column>
              </template>
            </el-table>
          </el-tab-pane>
          <el-tab-pane label="调试日志" name="second">
            <div style="padding: 0 10px;height:290px;overflow:auto">
              <div v-for="item in contentData" :key="item.tableName">
                <div v-if="item.data.length" style="line-height: 1.5;font-family: consolas; font-size: 14px; font-weight: bold">
                  <span>{{ item.tableName }}</span>
                </div>
                <ul>
                  <li style="line-height: 1.5;font-family: consolas; font-size: 13px;" v-for="c in item.data" :key="c.id">
                    <span :style="'margin-right: 5px; color:' + getColor(c.level)">{{c.createTime}}</span>
                    <span :style="'color:' + getColor(c.level)" v-if="c.level">{{ '[' + c.level + ']'}}</span>
                    <span :style="'color:' + getColor(c.level)">{{c.logDetail}}</span>
                  </li>
                </ul>
                <hr />
              </div>
            </div>
          </el-tab-pane>
        </el-tabs>
      </el-row>
      <set-table-info
        :setTableInfoVisible="setTableInfoVisible"
        :isTableRelation="isTableRelation"
        @setTableInfoBack="setTableInfoBack"
      ></set-table-info>
      <set-item-info
        :setItemInfoVisible="setItemInfoVisible"
        :isItemRelation="isItemRelation"
        :status="status"
        @setItemInfoBack="setItemInfoBack"
      ></set-item-info>
      <set-item-only-info
        :setItemOnlyInfoVisible="setItemOnlyInfoVisible"
        :isItemRelation="isItemRelation"
        @setItemOnlyInfoBack="setItemOnlyInfoBack"
        :status="status"
      ></set-item-only-info>
      <set-item-relation-info
        :setItemRelationInfoVisible="setItemRelationInfoVisible"
        :isItemRelation="isItemRelation"
        :currentTable="tableList"
        @setItemRelationInfoBack="setItemRelationInfoBack"
        :status="status"
      ></set-item-relation-info>
    </section>
  </el-dialog>
</template>

<script type="text/jsx">
import draggable from "vuedraggable";
import SetTableInfo from "./ruleDetails/SetTableInfo.vue";
import SetItemInfo from "./ruleDetails/SetItemInfo.vue";
import SetItemOnlyInfo from "./ruleDetails/SetItemOnlyInfo.vue";
import SetItemRelationInfo from "./ruleDetails/SetItemRelationInfo.vue";
import { checkType, changeUTCtoDateTime } from '@/common/common_tools.js';

export default {
  name: "",
  components: {
    SetItemInfo,
    SetTableInfo,
    SetItemOnlyInfo,
    SetItemRelationInfo,
    draggable
  },
  props: ["ruleDetail", "id", "name"],
  data() {
    return {
      searchName: "",
      tableList: [],
      modelList: [
        { name: "值域校验", type: "5", icon: "g-阈值校验" },
        { name: "数据范围校验", type: "4", icon: "data-check" },
        { name: "空值校验", type: "3", icon: "g-空值校验" },
        { name: "正则表达式校验", type: "6", icon: "RE" },
        { name: "字段格式校验", type: "2", icon: "Field-check" },
        { name: "唯一性校验", type: "31", icon: "g-唯一性校验" },
        { name: "字段关联比对校验", type: "32", icon: "字段关联比对" },
        { name: "数据精度校验", type: "33", icon: "数据精度校验" }
      ],
      itemList: [],
      currModel: "",
      dragStartItem: false,

      setTableInfoVisible: false,
      isTableRelation: {
        currTime: new Date().getTime(),
        id: "",
        tableName: ""
      },
      setItemInfoVisible: false,
      setItemOnlyInfoVisible: false,
      setItemRelationInfoVisible: false,
      isItemRelation: {
        currTime: new Date().getTime(),
        id: ""
      },

      cols: [],
      tableData: [],
      isLoadingTable: false,

      loadingSave: false,
      loadingDebugger: false,
      status: "1",
      currId: "",
      isdebuggerOk: false,
      activeName2: "first",
      debuggerFailedTable: [],
      loading: false,
      index: null,
      componentStatus: false,
      customStatus: false,
      contentData: []
    };
  },
  computed: {
    setSaveDisabled() {
      if (this.status == 0) {
        if (this.isdebuggerOk) {
          return false;
        } else {
          return true;
        }
      } else {
        return true;
      }
    },
    setDebugger() {
      if (this.status == 0) {
        if (this.isdebuggerOk) {
          return true;
        } else {
          return false;
        }
      } else {
        return true;
      }
    },
    Newitems() {
      let _this = this;
      let Newitems = [];
      _this.tableList.map(item => {
        let tmp = item.tableName.toLowerCase();
        if (tmp.search(_this.searchName.toLowerCase()) != -1) {
          Newitems.push(item);
        }
      });
      return Newitems;
    },
    sourceInfoId() {
      return this.$props.id;
    },
    dataSourceName() {
      return this.$props.name;
    }
  },
  methods: {
    async refreshTable() {
      this.debuggerFailedTable = [];
      await this.getTableList();
      // setTimeout(() => {
        this.previewData(this.Newitems[0], 0);
      // }, 500);
    },
    getTableList() {
      return this.$urlApi.dataQuality
        .refreshTableDesignList({ sourceId: this.sourceInfoId })
        .then(res => {
          this.tableList = res.data;
        });
    },
    drag(event, num) {
      event.target.style.backgroundColor = '#F0F1F4';
      this.currModel = num;
    },
    dragStart(event) {
      this.dragStartItem = true;
    },
    dragend(event) {
      event.target.style.backgroundColor = '#FFF';
    },
    drop(event) {
      // $(this.$refs['component']).children().css("background-color", '#fff');
      event.preventDefault();
      let isRepete = false;
      this.itemList.forEach(item => {
        if (item.type == this.currModel) {
          isRepete = true;
        }
      });
      if (isRepete) {
        return this.$toast('info', '已存在此规则！')
      }
      if (this.dragStartItem) {
        this.dragStartItem = false;
        return;
      }
      switch (this.currModel) {
        case "5":
          const g = {
            name: "值域校验",
            type: 5,
            status: 1,
            add: true
          };
          this.itemList.push(g);
          this.setItemInfo(g);
          break;
        case "4":
          const f = {
            name: "数据范围校验",
            type: 4,
            status: 1,
            add: true
          };
          this.itemList.push(f);
          this.setItemInfo(f);
          break;
        case "3":
          const e = {
            name: "空值校验",
            type: 3,
            status: 0,
            add: true
          };
          this.itemList.push(e);
          this.setItemInfo(e);
          break;
        case "6":
          const d = {
            name: "正则表达式校验",
            type: 6,
            status: 0,
            add: true
          };
          this.itemList.push(d);
          this.setItemInfo(d);
          break;
        case "2":
          const c = {
            name: "字段格式校验",
            type: 2,
            status: 1,
            add: true
          };
          this.itemList.push(c);
          this.setItemInfo(c);
          break;
        case "31":
          const a = {
            name: "唯一性校验",
            type: 31,
            status: 1,
            add: true
          };
          this.itemList.push(a);
          this.setItemInfo(a);
          break;
        case "32":
          const b = {
            name: "字段关联比对校验",
            type: 32,
            status: 1,
            add: true
          };
          this.itemList.push(b);
          this.setItemInfo(b);
          break;
        case "33":
          this.itemList.push({
            name: "数据精度校验",
            type: 33,
            status: 1,
            add: true
          });
          break;
      }
    },
    deleteItem(item) {
      let params = {
        datasourceId: this.$props.id,
        verifyType: item.type,
        tableName:this.isTableRelation.tableName
      };
      this.$confirm("确认要删除吗", "系统消息", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        cancelButtonClass: "cancelButton",
        confirmButtonClass: "confirmButton",
        closeOnClickModal: false
      }).then(() => {
        this.$urlApi.dataQuality.removeQualityOnTableByType(params).then(res => {
          if (res.code == 200) {
            this.$toast("success", "删除成功");
            this.getBackData({ id: this.currId });
          } else {
            this.$toast("error", res.message);
          }
        });
      })
    },
    allowDrop(event) {
      event.preventDefault();
    },
    setTableInfo() {
      let tempDate = new Date().getTime();
      this.setTableInfoVisible = true;
      this.isTableRelation.currTime = tempDate;
    },
    setTableInfoBack(cb) {
      this.setTableInfoVisible = false;
    },

    setItemInfo(item) {
      if (
        item.type === 2 ||
        item.type === 3 ||
        item.type === 4 ||
        item.type === 5 ||
        item.type === 6
      ) {
        if (item.add) {
          this.isItemRelation.add = true;
        }
        this.isItemRelation.tableName = this.isTableRelation.tableName;
        this.isItemRelation.id = this.$props.id;
        this.isItemRelation.tableID = this.currId;
        this.setItemInfoVisible = true;
      } else if (item.type === 31) {
        this.setItemOnlyInfoVisible = true;
        this.isItemRelation.tableName = this.isTableRelation.tableName;
        this.isItemRelation.id = this.$props.id;
        this.isItemRelation.isEdit = false;
        if (item.rule) {
          this.isItemRelation.isEdit = true;
          this.isItemRelation.filedName = item.rule[0].columnName;
          this.isItemRelation.componentID = item.rule[0].id;
        }
        if (item.add) {
          this.isItemRelation.add = true;
        }
      } else if (item.type === 32) {
        this.setItemRelationInfoVisible = true;
        this.isItemRelation.tableName = this.isTableRelation.tableName;
        this.isItemRelation.id = this.$props.id;
        this.isItemRelation.isEdit = false;
        if (item.rule) {
          this.isItemRelation.isEdit = true;
          this.isItemRelation.tableData = item.rule;
        } else {
          this.isItemRelation.tableData = [];
        }
        if (item.add) {
          this.isItemRelation.add = true;
        }
      }
      let tempDate = new Date().getTime();
      this.isItemRelation.currTime = tempDate;
      this.isItemRelation.name = item.name;
      this.isItemRelation.type = item.type;
      this.isItemRelation.rule = item.rule;
      this.isItemRelation.name = item.name;
    },
    async setItemInfoBack(cb) {
      if (checkType(cb) === 'array') {
        if (this.status == 3 && !cb.length) {
          return this.$toast('error', '校验规则为空，请添加校验规则后保存')
        }
        this.$urlApi.dataQuality
          .updateEnable({
            jsonString: JSON.stringify(cb),
            designTableId: this.isTableRelation.id
          })
          .then(res => {
            this.$toast("success", "保存成功！");
            this.setItemInfoVisible = false;
            this.isItemRelation.add = false;
            this.getBackData({ id: this.currId });
          });
      } else if (checkType(cb) === 'number') {
        if (cb === 1) {
          this.itemList.pop();
        }
        if (cb === 2) {
          this.debuggerFailedTable = [];
          await this.getTableList();
          // setTimeout(() => {
            this.previewData(
              this.Newitems.find(c => c.tableName === this.index),
              this.Newitems.findIndex(c => c.tableName === this.index)
            );
            this.getBackData(this.Newitems.find(c => c.tableName === this.index))
          // }, 1000);
        }
        this.setItemInfoVisible = false;
        this.isItemRelation.add = false;
      }
    },
    setItemOnlyInfoBack(cb) {
      if (checkType(cb) === 'object') {
        if (this.status == 3 && !cb.filedName) {
          return this.$toast('error', '校验规则为空，请添加校验规则后保存')
        }
        const a = {
          columnName: cb.filedName,
          datasourceId: this.$props.id,
          enable: 1,
          ...(this.isItemRelation.isEdit
            ? { id: this.isItemRelation.componentID }
            : {}),
          name: cb.stepName,
          tableName: this.isTableRelation.tableName,
          verifyDetail: JSON.stringify({
            columnName: cb.filedName,
            isStandard: "N"
          }),
          verifyType: "31"
        };
        this.$urlApi.dataQuality
          .addUserDefineQuality({
            tableInfoId: this.currId,
            jsonStr: JSON.stringify([a])
          })
          .then(async res => {
            this.$toast("success", "保存成功！");
            this.setItemOnlyInfoVisible = false;
            await this.getTableList();
            // setTimeout(() => {
              this.previewData(
                this.Newitems.find(c => c.tableName === this.index),
                this.Newitems.findIndex(c => c.tableName === this.index)
              );
              this.isItemRelation.isEdit = false;
              this.isItemRelation.filedName = "";
              this.isItemRelation.componentID = "";
              this.isItemRelation.add = false;
            // }, 1000);
          });
      } else if (checkType(cb) === 'number') {
        if (cb === 1) {
          this.itemList.pop();
        }
        this.setItemOnlyInfoVisible = false;
        this.isItemRelation.tableName = "";
        this.isItemRelation.id = "";
        this.isItemRelation.isEdit = false;
        this.isItemRelation.filedName = "";
        this.isItemRelation.componentID = "";
        this.isItemRelation.add = false;
      }
    },
    setItemRelationInfoBack(cb) {
      if (checkType(cb) === 'array') {
        if (this.status == 3 && !cb.length) {
          return this.$toast('error', '校验规则为空，请添加校验规则后保存')
        }
        const a = cb.map(c => {
          return {
            columnName: c.field,
            datasourceId: this.$props.id,
            enable: 1,
            ...(c.id ? { id: c.id } : {}),
            name: "字段关联比对校验",
            tableName: this.isTableRelation.tableName,
            verifyDetail: JSON.stringify({
              columnName: c.field,
              isStandard: "N",
              targetTable: c.relationTable,
              targetColumn: c.relationField
            }),
            verifyType: "32"
          };
        });
        this.$urlApi.dataQuality
          .addUserDefineQuality({
            tableInfoId: this.currId,
            jsonStr: JSON.stringify([...a])
          })
          .then(async res => {
            this.$toast("success", "保存成功！");
            this.setItemRelationInfoVisible = false;
            await this.getTableList();
            // setTimeout(() => {
              this.previewData(
                this.Newitems.find(c => c.tableName === this.index),
                this.Newitems.findIndex(c => c.tableName === this.index)
              );
            // }, 1000);
            this.isItemRelation.isEdit = false;
            this.isItemRelation.tableData = [];
            this.isItemRelation.add = false;
          });
      } else if (checkType(cb) === 'number') {
        if (cb === 1) {
          this.itemList.pop();
        }
        this.setItemRelationInfoVisible = false;
        this.isItemRelation.tableName = "";
        this.isItemRelation.id = "";
        this.isItemRelation.isEdit = false;
        this.isItemRelation.tableData = [];
        this.isItemRelation.add = false;
      }
    },
    previewData(item, i) {
      this.$refs.listInfo &&
        this.$refs.listInfo.forEach(c => (c.style.backgroundColor = "#f5f7fa"));
      this.$refs.listInfo[i] &&
        (this.$refs.listInfo[i].style.backgroundColor = "#e1e5e7");
      this.currId = item.id;
      this.status = item.status;
      this.isTableRelation.tableName = item.tableName;
      this.isTableRelation.id = item.id;
      this.getTableData(item);
      this.getBackData(item);
      this.index = item.tableName;
      this.activeName2 = 'first'
    },
    getTableData(item) {
      this.isLoadingTable = true;
      this.$urlApi.dataQuality
        .getTablePreviewData({
          tableName: item.tableName,
          sourceId: this.sourceInfoId
        })
        .then(res => {
          this.isLoadingTable = false;
          let heads = [];
          for (let item in res.tableColumns) {
            heads.push({
              label: res.tableColumns[item].name,
              prop: res.tableColumns[item].name
            });
          }
          this.cols = heads;
          this.tableData = res.data;
        });
    },
    deleteTable(event, item) {
      event.preventDefault();
      this.$urlApi.dataQuality
        .removeQualityOnTable({ tableDesignId: item.id })
        .then(res => {
          this.getTableList();
          this.itemList = [];
        });
    },
    createQuality(item) {
      this.currId = item.id;
      this.getCenterList();
    },
    getCenterList() {
      this.itemList = [];
      this.$urlApi.dataQuality
        .buildTransByTable({ id: this.currId })
        .then(res => {
          this.getTableList();
          this.status = 0;
          this.itemList = res.data
            .filter(c => c.type !== "1")
            .map(k => ({
              name: c.name,
              type: +c.type,
              status: 0,
              rule: c.rules
            }));
        });
    },
    getBackData(item) {
      this.itemList = [];
      this.$urlApi.dataQuality
        .getVerifyDetail({ designTableId: item.id })
        .then(res => {
          this.itemList = res.data
            .filter(c => c.type !== "1")
            .map(c => ({
              name: c.name,
              type: +c.type,
              status: 0,
              rule: c.rules
            }));
        });
    },
    saveData() {
      this.loadingSave = true;
      this.$urlApi.dataQuality
        .saveQualityTask({ tableDesignId: this.isTableRelation.id })
        .then(res => {
          this.getTableList();
          this.loadingSave = false;
          this.isdebuggerOk = false;
          this.status = 2;
          this.$toast("success", "保存成功！");
        });
    },
    debuggerData() {
      this.loadingDebugger = true;
      this.$urlApi.dataQuality
        .executeTask({ designTableInfoId: this.isTableRelation.id, isDebug: 1 })
        .then(res => {
          this.getTableList();
          this.loadingDebugger = false;
          this.$toast("success", "调试成功");
          this.isdebuggerOk = true;
        });
      this.loadingDebugger = false;
    },
    handleClick(val) {},
    handleCommand(command) {
      switch (command) {
        case "current":
          if (this.status != 0) {
            return false;
          }
          this.currentTableDebug();
          break;
        case "batch":
          if (this.Newitems.some(c => c.status == 0)) {
            this.batchTableDebug();
          }
          break;
        default:
          break;
      }
    },
    currentTableDebug() {
      this.loading = true;
      this.$urlApi.dataQuality
        .executeTask({ designTableInfoId: this.isTableRelation.id, isDebug: 1 })
        .then(async res => {
          this.loading = false;
          const contentData = res.data.map(c => {
            c.createTime = changeUTCtoDateTime("yyyy-MM-dd HH:mm:ss", c.createTime)
            return c
          });
          const tableName = res.data[0] ? res.data[0].tableName : ''
          this.contentData = [{
            data: contentData,
            tableName: tableName
          }]
          await this.getTableList();
          // setTimeout(() => {
          await this.previewData(
              this.Newitems.find(c => c.tableName === this.index),
              this.Newitems.findIndex(c => c.tableName === this.index)
            );
          // }, 1000);
          const failedTable = res.data.find(c => c.level === 'ERROR')
          if (failedTable) {
            this.debuggerFailedTable = [failedTable.tableName];
            this.$toast("error", this.debuggerFailedTable.join('，') + '表调试失败，请查看日志详情');
          } else {
            this.$toast("success", "调试完成")
          }
        })
        .catch(e => {
          this.loading = false;
        });
    },
    batchTableDebug() {
      this.loading = true;
      this.$urlApi.dataQuality
        .debugAndSave({ datasourceId: this.sourceInfoId })
        .then(res => {
          this.loading = false;
          if (res.code == 200) {
            const resultData = JSON.parse(res.data)
            const contentData = [];
            const failedTable = [];
            for (let key in resultData) {
              contentData.push({
                tableName: key,
                data: resultData[key].map(c => {
                        c.createTime = changeUTCtoDateTime("yyyy-MM-dd HH:mm:ss", c.createTime)
                        return c
                      })
              })
              const a = resultData[key].find(c => c.level === 'ERROR')
              if (a) {
                failedTable.push(a.tableName)
              }
            }
            this.contentData = contentData;
            this.debuggerFailedTable = failedTable;
            if (failedTable.length) {
              this.$toast("error", failedTable.join('，') + '表调试失败，请查看日志详情');
            } else {
              this.$toast("success", "调试完成")
            }
            this.getTableList();
          }
        })
        .catch(e => {
          this.loading = false;
        });
    },
    getColor(level) {
      const colors = {
        'ERROR': 'red',
        'WARN': '#ffb90f',
        'INFO': '#606266',
      }
      return colors[level]
    },
    closeDialog() {
      this.$emit("setRuleDetailfoBack");
      this.tableList = [];
      this.itemList = [];
      this.tableData = [];
      this.isTableRelation.tableName = "";
      this.contentData = [];
      this.debuggerFailedTable = [];
      this.activeName2 = 'first'
    },
    async openRuleDetailDialog() {
      await this.getTableList();
      // setTimeout(() => {
        this.previewData(this.Newitems[0], 0);
      // }, 1000);
    },
    backDraft(item) {
      this.$urlApi.dataQuality.toDraft({ tableInfoId: item.id }).then(async res => {
        if (res.code == 200) {
          this.$toast("success", "退回草稿成功！");
          await this.getTableList();
          // setTimeout(() => {
            this.previewData(
              this.Newitems.find(c => c.tableName === this.index),
              this.Newitems.findIndex(c => c.tableName === this.index)
            );
          // }, 1000);
        } else {
          this.$toast("error", res.msg);
        }
      });
    },
    labelHeader(h, { column, index }) {
      return (
        <el-tooltip
          class="item"
          effect="dark"
          content={column.label}
          placement="top"
        >
          <div class="table-label-head" style={{ width: "100%" }}>
            {column.label}
          </div>
        </el-tooltip>
      );
    },
    clickComponent() {
      this.componentStatus = !this.componentStatus;
    },
    clickCustom() {
      this.customStatus = !this.customStatus;
    }
  },
  mounted: function() {}
};
</script>

<style lang="scss">
.ruleDetail-dialog {
  .ruleDetail-dialog-title {
    color: #647094;
    font-size: 16px;
    text-decoration: none;
  }
  .el-dialog .el-dialog__body {
    height: calc(100% - 45px);
    box-sizing: border-box;
    overflow: auto;
  }
  .rule-design-detail {
    height: 100%;
    .rule-title {
      margin-top: 0;
      font-size: 13px;
      font-weight: bold;
    }
    .rule-t {
      height: 500px;
      display: flex;
      .rule-t-l {
        width: 254px;
        height: 100%;
        background: rgba(245, 247, 250, 1);
        padding: 15px;
        box-sizing: border-box;
        .el-input__icon {
          cursor: pointer;
        }
        .rule-t-l-search {
          display: flex;
          justify-content: flex-start;
          .tree-t-set {
            width: 28px;
            height: 26px;
            display: block;
            border-radius: 2px;
            border: 1px solid #e1e5ec;
            margin-right: 7px;
            cursor: pointer;
            background: rgba(240, 241, 244, 1);
            font-size: 14px;
            line-height: 26px;
            text-align: center;
          }
        }
        .rule-table-list {
          height: -moz-calc(100% - 80px);
          height: -webkit-calc(100% - 80px);
          height: calc(100% - 80px);
          .list-info {
            display: flex;
            justify-content: flex-start;
            height: 34px;
            line-height: 34px;
            cursor: pointer;
            span {
              display: block;
              &.data-status {
                width: 60px;
                margin-right: 20px;
                i {
                  display: inline-block;
                  width: 8px;
                  height: 8px;
                  border-radius: 50%;
                  margin-right: 5px;
                }
              }
              &.table-name {
                color: #3e4456;
                font-size: 13px;
                font-weight: 400;
                display: inline-block;
                width: 105px;
              }
              &.table-icon {
                color: rgba(51, 55, 67, 1);
                font-size: 14px;
              }
              &.statusNo {
                color: rgba(59, 163, 248, 1);
                i {
                  background: rgba(59, 163, 248, 1);
                }
              }
              &.statusLoading {
                color: #d45ce5;
                i {
                  background: #d45ce5;
                }
              }
              &.statusWait {
                color: #6cac47;
                i {
                  background: #6cac47;
                }
              }
              &.statusSuccess {
                color: #26b9c0;
                i {
                  background: #26b9c0;
                }
              }
            }
          }
          .list-info:hover {
            background-color: #e1e5ec;
          }
        }
      }
      .rule-t-c {
        flex: 1;
        height: 100%;
        margin-left: 25px;
        .rule-detail-title {
          margin: 0;
          height: 52px;
          line-height: 52px;
          background-color: rgba(225, 229, 236, 0.44);
          font-size: 14px;
          padding-left: 25px;
          box-sizing: border-box;
          font-weight: bold;
          .btn-debuf-red {
            background-color: rgba(246, 127, 137, 0.8);
            border: 0 none;
          }
        }
        .drop-container {
          width: 215px;
          margin: 30px auto;
          overflow: auto;
          height: calc(100% - 125px);
          .drop-t {
            height: 36px;
            background: rgba(255, 255, 255, 1);
            border-radius: 2px;
            border: 1px solid rgba(240, 241, 244, 1);
            color: #333743;
            line-height: 36px;
            text-align: center;
            font-size: 13px;
            cursor: pointer;
          }
          .drop-b {
            padding: 9px;
            box-sizing: border-box;
            height: 230px;
            background: #F5F7FA;
            .el-scrollbar__wrap {
              overflow-x: hidden;
            }
            li {
              &.dragYindao {
                border: 1px dashed #D1D2D5;
                background-color: #EFEFEF;
                color: #3ba3f8;
                font-size: 13px;
              }
              height: 36px;
              background: #ffffff;
              font-size: 13px;
              line-height: 36px;
              text-align: center;
              margin-bottom: 5px;
              position: relative;
              cursor: move;
              .delte-item {
                position: absolute;
                right: 15px;
                display: none;
                top: 0;
                cursor: pointer;
              }
              &:hover {
                .delte-item {
                  display: block;
                }
              }
              .item-status-ok {
                color: #26b9c0;
                position: absolute;
                left: 15px;
              }
              .item-status-no {
                color: #3ba3f8;
                position: absolute;
                left: 15px;
              }
            }
          }
        }
      }
      .rule-t-r {
        height: calc(100% - 85px);
        width: 200px;
        padding: 8px;
        box-sizing: border-box;
        position: relative;
        float: right;
        background-color: #FFFFFF;
        border: 1px solid #E1E5EC;
        margin-top: 30px;
          overflow: auto;
        .rule-title {
          color: #333743;
          font-size: 13px;
          text-align: center;
          line-height: 30px;
          border-bottom: 1px solid #E1E5EC;
          padding-top: 5px;
        }
        .rule-item {
          display: flex;
          justify-content: flex-start;
          flex-wrap: wrap;
          float: left;
          li {
            width: 50%;
            // padding: 10px 30px;
            float: left;
            text-align: center;
            cursor: pointer;
            padding: 10px 0;
            span {
              &:nth-child(1) {
                font-size: 22px;
                margin-bottom: 13px;
              }
              &.item-name {
                font-size: 12px;
                color: #333743;
                line-height: 30px;
              }
            }
          }
        }
      }
    }
    .rule-c {
      height: 350px;
      border-radius: 2px;
      border: 1px solid rgba(240, 241, 244, 1);
      margin-top: 20px;
      .el-table td,
      .el-table th {
        padding: 8px 0;
      }
    }
    .rule-b {
      height: 50px;
      text-align: right;
      padding-top: 14px;
      box-sizing: border-box;
    }
  }
}
.auto_debugger {
  text-align: center;
  padding: 0 10px;
  font-family: 'Microsoft YaHei';
}
</style>
