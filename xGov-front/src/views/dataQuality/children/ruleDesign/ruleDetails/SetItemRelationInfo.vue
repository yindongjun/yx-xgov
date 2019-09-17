<template>
    <el-dialog
        :title="isItemRelation.name"
        :visible.sync="setItemRelationInfoVisible"
        :before-close="closeDialog"
        class="set-Item-info"
        :lock-scroll="false"
        width="800px"
        @open="openItem"
        append-to-body
        :close-on-click-modal="false">
        <el-form :model="baseForm" ref="baseForm"  :rules="rules" size="small"  label-width="100px" label-position="right">
            <el-form-item label="规则名称" prop="stepName">
                <el-input :disabled="true" v-model="baseForm.stepName"></el-input>
            </el-form-item>
        </el-form>
        <div class="operate">
          <el-button :disabled="currentStatus == 2" class="grey-btn" size="small" @click="addRelation">新增</el-button>
          <el-button :disabled="currentStatus == 2 || checkData.length === 0" class="grey-btn" size="small" @click="() => deleteRelation()">删除</el-button>
        </div>
        <el-table
          :data="tableData"
          style="width: 100%"
          height="300px"
          @selection-change="handleSelectionChange"
        >
          <el-table-column
            type="selection"
            width="55">
          </el-table-column>
          <el-table-column
              prop="fieldName"
              label="字段名"
          >
          <template slot-scope="scope">
            <el-select :disabled="currentStatus == 2" v-model="scope.row.field" @change="(val) => changeTableField(val, scope)" filterable placeholder="请选择字段">
                <el-option
                  v-for="item in fieldList"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
          </template>
          </el-table-column>
          <el-table-column
              label="对比表"
          >
          <template slot-scope="scope">
            <el-select :disabled="currentStatus == 2" v-model="scope.row.relationTable" @change="changeTable" filterable placeholder="请选择对比表">
                <el-option
                  v-for="item in relationTableList"
                  :disabled="item.label === isItemRelation.tableName"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
          </template>
          </el-table-column>
          <el-table-column
              label="对比字段"
          >
          <template slot-scope="scope">
            <el-select :disabled="currentStatus == 2" v-model="scope.row.relationField" filterable placeholder="请选择对比字段">
                <el-option
                  v-for="item in relationFieldList"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
          </template>
          </el-table-column>
          <el-table-column label="操作">
              <template slot-scope="scope">
                  <el-button v-if="currentStatus != 2" size="mini" class="operate-btn" @click="() => deleteRelation(scope)" type="text">删除</el-button>
                  <!-- <i class="table-icon-menu" @click="() => deleteRelation(scope)"><svg-icon iconClass="删除"></svg-icon></i> -->
              </template>
          </el-table-column>
        </el-table>
        <div class="delete-tip"><i class="el-icon-warning"></i> 删除或修改数据后，请保存使配置生效！</div>
        <span slot="footer" class="dialog-footer">
            <el-button type="info" size="small" @click="closeDialog" >关 闭</el-button>
            <el-button type="danger" v-if="currentStatus != 2" size="small" @click="submitForm('baseForm')" :loading="buttonLoading">保存</el-button>
        </span>
    </el-dialog>
</template>

<script>
    export default {
        name: "",
        props:['setItemRelationInfoVisible','isItemRelation', 'currentTable', 'status'],
        data() {
            return {
                buttonLoading: false,
                baseForm:{
                    stepName:'',
                },
                rules: {},
                tableData: [],
                fieldList: [],
                relationTableList: [],
                relationFieldList:[],
                checkData: [],
                currType:''
            }
        },
        methods: {
            closeDialog(){
                this.$refs['baseForm'].resetFields();
                this.$emit("setItemRelationInfoBack", this.isItemRelation.add ? 1 : 0);
                this.resetData();
            },
            submitForm(formName){
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        const isNull = this.tableData.some(c => {
                          return !c.field || !c.relationTable || !c.relationField
                        })
                        if (isNull) {
                          return this.$toast('error', '字段关联数据未填写完整！')
                        }
                        this.buttonLoading = true;
                        this.$emit("setItemRelationInfoBack", [...this.tableData]);
                        this.$refs['baseForm'].resetFields();
                        // this.resetData();
                        setTimeout(() => {
                            this.buttonLoading = false;
                        }, 1000)
                    } else {
                        return false;
                    }
                });
            },
            openItem(){
                this.currType=this.isItemRelation.type;
                this.baseForm.stepName=this.isItemRelation.name;
                this.relationTableList = this.currentTable.map(c => ({
                  value: c.tableName,
                  label: c.tableName
                }))
                this.tableData = (this.isItemRelation.tableData || []).map(c => {
                  const a = JSON.parse(c.verifyDetail);
                  return {
                    id: c.id,
                    field: a.columnName,
                    relationTable: a.targetTable,
                    relationField: a.targetColumn
                  }
                });
                this.getFieldByTable({id: this.isItemRelation.id, tableName: this.isItemRelation.tableName}).then(data => {
                  this.fieldList = data[0] ? data[0].cols.map(c => ({
                    label: c.name,
                    value: c.name
                  })) : [];
                })
            },
            getFieldByTable(options) {
              return new Promise((resolve, reject) => {
                this.$urlApi.dataStandard.searchByColumn({id: options.id, tableName: options.tableName, enableFuzzy: false}).then((res) => {
                  resolve(res.data)
                })
              })
            },
            changeTable(val) {
              this.getFieldByTable({id: this.isItemRelation.id, tableName: val}).then(data => {
                  this.relationFieldList = data[0] ? data[0].cols.map(c => ({
                    label: c.name,
                    value: c.name
                  })) : [];
              })
            },
            changeTableField(val, scope) {
              const {$index, row} = scope
              const i = this.tableData.findIndex((c, i) => {
                return c.field === row.field && i !== $index
              })
              if (i !== -1) {
                const a = [...this.tableData]
                a[$index].field = '';
                this.tableData = a;
                this.$toast('error', '该字段已经被关联，请选择其他字段！')
              }
            },
            handleSelectionChange(val) {
              this.checkData = val;
            },
            addRelation() {
              this.tableData.push({
                field: '',
                relationTable: '',
                relationField: ''
              })
            },
            deleteRelation(scope) {
              if (scope) {
                const {$index, row} = scope;
                this.tableData.splice($index, 1);
              } else {
                this.checkData.forEach(k => {
                  const index = this.tableData.findIndex(c => c.field === k.field && c.relationTable === k.relationTable && c.relationField === k.relationField);
                  index !== -1 && this.tableData.splice(index, 1);
                })
              }
            },
            resetData() {
                this.tableData = [];
                this.fieldList = [];
                this.relationTableList = [];
                this.relationFieldList = [];
                this.checkData = [];
            }
        },
        computed:{
          currentStatus() {
            return this.$props.status
          }
        },
        mounted: function () {

        }
    }
</script>

<style lang="scss">
.set-Item-info {
  .delete-tip {
    width: 100%;
    text-align: center;
    color: #f78989;
    font-size: 13px;
    margin-top: 10px;
  }
}
</style>
