<template>
  <el-dialog
    title="新增稽核字段"
    :visible.sync="addAuditVisible"
    :before-close="closeDialog"
    class="add-audit-fields"
    :lock-scroll="false"
    width="760px"
    @open="open"
    append-to-body
    :close-on-click-modal="false"
  >
    <!-- <el-button class="grey-btn" size="small" @click="addFields">新增</el-button>
    <el-button class="grey-btn" size="small" :disabled="!checkData.length" @click="deleteFields">删除</el-button> -->
    <el-table :data="tableData" @selection-change="changeFun">
      <!-- <el-table-column type="selection" width="55"> </el-table-column> -->
      <el-table-column
        v-for="item in colums[type]"
        :key="item.label"
        :label="item.label"
        :prop="item.prop"
      >
        <template slot-scope="scope">
          <el-input
            v-if="item.type === 'input'"
            v-model="scope.row[item.prop]"
          ></el-input>
          <el-select
            v-if="item.type === 'select'"
            clearable
            v-model="scope.row[item.prop]"
            @change="val => changeHandle(val, item.prop, scope)"
          >
            <el-option
              v-for="item in item.options"
              :key="item.value"
              :disabled="item.disabled"
              :label="item.label"
              :value="item.value"
            >
            </el-option>
          </el-select>
          <el-select
            v-if="item.type === 'select-group'"
            clearable
            v-model="scope.row[item.prop]"
            @change="val => changeHandle(val, item.prop, scope)"
          >
            <el-option-group
              v-for="group in item.options"
              :key="group.value"
              :label="group.value">
                <el-option
                  v-for="c in group.options" :key="c.value"
                  :disabled="c.disabled"
                  :title="c.tip"
                  :label="c.value"
                  :value="c.value">
                    <span style="float: left">{{ c.value }}</span>
                    <span style="float: right; color: #8492a6; font-size: 12px">{{ c.tip }}</span>
                </el-option>
              </el-option-group>
          </el-select>
          <el-cascader
            v-if="item.type === 'cascader'"
            clearable
            :options="item.options.options"
            :props="item.options.props"
            v-model="scope.row[item.prop]"
          >
          </el-cascader>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="50">
        <template slot-scope="scope">
          <span @click="deleteItem(scope.row)"
            ><svg-icon iconClass="删除"></svg-icon
          ></span>
        </template>
      </el-table-column>
    </el-table>
    <div class="addFields" @click="addFields">
      新增字段
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button type="info" size="small" @click="closeDialog">取 消</el-button>
      <el-button
        type="danger"
        size="small"
        @click="submitForm"
        :loading="buttonLoading"
        >保存</el-button
      >
    </span>
  </el-dialog>
</template>

<script>
export default {
  name: "add-audit-fields",
  props: ["addAuditVisible", "currType", "tableInfo", "tableList"],
  data() {
    return {
      tableData: [],
      colums: {
        // 正则表达式校验
        "6": [
          {
            prop: "columnName",
            label: "字段名",
            type: "select",
            options: []
          },
          {
            prop: "regularValue",
            label: "正则表达式",
            type: "select",
            options: [
              {
                label: "身份证号码验证",
                value:
                  "^([1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3})|([1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X))$"
              },
              {
                label: "手机号码验证",
                value:
                  "^(17[0-9]|13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$"
              },
              {
                label: "IP地址验证",
                value:
                  "((25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))"
              },
              {
                label: "邮箱地址验证",
                value: "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$"
              }
            ]
          }
        ],
        // 值域
        "5": [
          {
            prop: "columnName",
            label: "字段名",
            type: "select",
            options: []
          },
          {
            prop: "codeSetId",
            label: "值域",
            type: "cascader",
            options: {
              props: {
                value: "id",
                label: "name"
              },
              options: []
            }
          }
        ],
        // 数据范围检验
        "4": [
          {
            prop: "columnName",
            label: "字段名",
            type: "select",
            options: []
          },
          {
            prop: "minValue",
            label: "最小值",
            type: "input"
          },
          {
            prop: "maxValue",
            label: "最大值",
            type: "input"
          }
        ],
        // 空值校验
        "3": [
          {
            prop: "columnName",
            label: "字段名",
            type: "select",
            options: []
          }
        ],
        // 字段格式校验
        "2": [
          {
            prop: "columnName",
            label: "字段名",
            type: "select",
            options: []
          },
          {
            prop: "formatType",
            label: "类型",
            type: "select-group",
            options: [
              {
                value: 'c',
                options: [
                  {value:'c', tip: '定长的字符'},
                  {value:'c..', tip: '最多xx位字符'}
                ]
              },
              {
                value: 'a',
                options: [
                  {value:'a', tip: '定长字母字符'},
                  {value:'a..', tip: '最多xx位字母字符'}
                ]
              },
              {
                value: 'n',
                options: [
                  {value:'n', tip: '定长数字字符'},
                  {value:'n..', tip: '最多xx位数字字符'},
                  {value:'n..()', tip: '浮点数'}
                ]
              },
              {
                value: 'an',
                options: [
                  {value:'an', tip: '定长字母和数字字符'},
                  {value:'an..', tip: '最多xx位字母和数字字符'}
                ]
              },
              {
                value: 'd',
                options: [
                  {value:'d', tip: 'YYYYMMDD日期'}
                ]
              },
              {
                value: 't',
                options: [
                  {value:'t', tip: 'hhmmss时间'}
                ]
              },
              {
                value: 'dt',
                options: [
                  {value:'dt', tip: 'YYYYMMDDhhmmss日期时间'}
                ]
              }
            ]
          },
          {
            prop: "formatLength",
            label: "长度",
            type: "input"
          }
        ]
      },
      checkData: [],
      buttonLoading: false
    };
  },
  computed: {
    type() {
      return this.currType;
    }
  },
  methods: {
    open() {
      this.getFieldsName();
      if (this.type == 5) {
        this.getLayerMenu();
      }
    },
    getLayerMenu() {
      this.$urlApi.dataStandard.getAuditedCodeSet().then(res => {
        this.colums[this.type][1].options.options = res.data.filter(
          c => !!c.id
        );
      });
    },
    getFieldsName() {
      this.$urlApi.dataStandard
        .searchByColumn({
          id: this.$props.tableInfo.id,
          tableName: this.$props.tableInfo.tableName,
          enableFuzzy: false
        })
        .then(res => {
          const fieldList = res.data[0]
            ? res.data[0].cols.map(c => ({
                label: c.name,
                value: c.name
              }))
            : [];
          const b = this.$props.tableList;
          const list = fieldList.map(c => {
            const isCun = b.find(k => k.columnName === c.label);
            if (isCun) {
              c.disabled = true;
            }
            return c;
          });
          const i = this.colums[this.type].findIndex(
            c => c.prop === "columnName"
          );
          this.colums[this.type][i].options = list;
        });
    },
    closeDialog() {
      this.$emit("addAuditFieldsBack");
      this.tableData = [];
      this.checkData = [];
    },
    deleteItem(row) {
      const index = this.tableData.findIndex(c => c.uuid === row.uuid);
      this.tableData.splice(index, 1);
    },
    changeHandle(val, prop, scope) {
      const { $index, row } = scope;
      if (prop === "columnName") {
        const i = this.tableData.findIndex((c, i) => {
          return c.columnName === row.columnName && i !== $index;
        });
        if (i !== -1) {
          const a = [...this.tableData];
          a[$index].columnName = "";
          this.tableData = a;
          this.$toast("error", "该字段已经被选择，请选择其他字段！");
        }
      }
    },
    changeFun(val) {
      this.checkData = val;
    },
    addFields() {
      const tableData = this.colums[this.type].map(c => c.prop);
      const a = {};
      tableData.forEach(c => {
        if (c.columnName === "codeSetId") {
          a[c] = [];
        } else {
          a[c] = "";
        }
      });
      a.uuid = +new Date();
      this.tableData.push(a);
    },
    deleteFields() {
      this.checkData.forEach(c => {
        this.deleteItem(c);
      });
    },
    submitForm() {
      if (!this.tableData.length) {
        return this.$toast('error', '请新增稽核字段')
      }
      if (this.type !== 2) {
        const isNull = this.tableData.map(c => {
          if (Object.values(c).some(c => !c)) {
            c.isnull = true;
          } else {
            c.isnull = false;
          }
          return c;
        });
        if (isNull.some(c => c.isnull)) {
          return this.$toast("error", "字段关联数据未填写完整！");
        }
      }
      let a = [];
      switch (this.type) {
        case 2:
          for (let i = 0; i < this.tableData.length; i++) {
            if(this.tableData[i].formatLength!==''&&this.tableData[i].formatType!=='n..()'){
                if(this.tableData[i].formatLength.indexOf(',')!=-1 || this.tableData[i].formatLength < 1 || this.tableData[i].formatLength > 4000){
                  return this.$toast('error', '请输入1-4000的整数！')
                }
            }else if(this.tableData[i].formatLength!==''&&this.tableData[i].formatType=='n..()'){
                if(this.tableData[i].formatLength.indexOf(',')!=-1 && !this.tableData[i].formatLength.endsWith(',')){
                    var m = this.tableData[i].formatLength.split(",");
                    if(m.length!==2){
                      return this.$toast('error', '请输入合法的校验长度和小数位数并以逗号分隔！')
                    }
                    if(parseInt(m[0])<=parseInt(m[1]) || parseInt(m[0])< 1 || parseInt(m[0])>4000 || parseInt(m[1])<0){
                      return this.$toast('error', '请输入合法的校验长度和小数位数并以逗号分隔！')
                    }
                }else{
                  return this.$toast('error', '请输入合法的校验长度和小数位数并以逗号分隔！')
                }
            } else if (!this.tableData[i].formatLength && this.tableData[i].formatType=='n..()') {
              return this.$toast('error', '请输入校验字符长度！')
            }
          }

          a = this.tableData.map(c => {
            return {
              columnName: c.columnName,
              datasourceId: this.$props.tableInfo.id,
              enable: 1,
              name: "字段格式校验",
              tableName: this.$props.tableInfo.tableName,
              verifyDetail: JSON.stringify({
                columnName: c.columnName,
                isStandard: "N",
                formatLength: c.formatLength,
                formatType: c.formatType
              }),
              verifyType: "2"
            };
          });
          break;
        case 3:
          a = this.tableData.map(c => {
            return {
              columnName: c.columnName,
              datasourceId: this.$props.tableInfo.id,
              enable: 1,
              name: "空值校验",
              tableName: this.$props.tableInfo.tableName,
              verifyDetail: JSON.stringify({
                columnName: c.columnName,
                isStandard: "N"
              }),
              verifyType: "3"
            };
          });
          break;
        case 4:
          for (let j = 0; j < this.tableData.length; j++) {
            if (isNaN(parseFloat(this.tableData[j].maxValue)) || isNaN(parseFloat(this.tableData[j].minValue))) {
              return this.$toast('error', '请输入正确的数据范围')
            }
            if (this.tableData[j].maxValue > 4000000000000000 || this.tableData[j].minValue < -4000000000000000) {
              return this.$toast('error', '请输入正确的数据范围')
            }
            if (parseFloat(this.tableData[j].maxValue) < parseFloat(this.tableData[j].minValue)) {
              return this.$toast('error', '请输入正确的数据范围')
            }
          }
          a = this.tableData.map(c => {
            return {
              columnName: c.columnName,
              datasourceId: this.$props.tableInfo.id,
              enable: 1,
              name: "数据范围校验",
              tableName: this.$props.tableInfo.tableName,
              verifyDetail: JSON.stringify({
                columnName: c.columnName,
                isStandard: "N",
                maxValue: c.maxValue,
                minValue: c.minValue
              }),
              verifyType: "4"
            };
          });
          break;
        case 5:
          const getCodeSetName = (id) => {
            let b = '';
            const a = (arr) => {
              arr.forEach(c => {
                if (id === c.id) {
                  b = c.name
                } else {
                  if (c.children) {
                    a(c.children)
                  }
                }
              })
            }
            a(this.colums['5'][1].options.options)
            return b
          }
          a = this.tableData.map(c => {
            return {
              columnName: c.columnName,
              datasourceId: this.$props.tableInfo.id,
              enable: 1,
              name: "值域校验",
              tableName: this.$props.tableInfo.tableName,
              verifyDetail: JSON.stringify({
                columnName: c.columnName,
                isStandard: "N",
                codeSetId: c.codeSetId[c.codeSetId.length - 1],
                codeSetName: getCodeSetName(c.codeSetId[c.codeSetId.length - 1])
              }),
              verifyType: "5"
            };
          });
          break;
        case 6:
          a = this.tableData.map(c => {
            return {
              columnName: c.columnName,
              datasourceId: this.$props.tableInfo.id,
              enable: 1,
              name: "正则校验",
              tableName: this.$props.tableInfo.tableName,
              verifyDetail: JSON.stringify({
                columnName: c.columnName,
                isStandard: "N",
                regularValue: c.regularValue
              }),
              verifyType: "6"
            };
          });
          break;

        default:
          break;
      }
      this.buttonLoading = true;
      this.$urlApi.dataQuality
        .addUserDefineQuality({
          tableInfoId: this.$props.tableInfo.tableID,
          jsonStr: JSON.stringify([...a])
        })
        .then(res => {
          this.buttonLoading = false;
          if (res.code === "200") {
            this.$toast("success", "新增成功");
            this.$emit("addAuditFieldsBack", 1);
            this.tableData = [];
            this.checkData = [];
          }
        })
        .catch(e => {
          this.buttonLoading = false;
        });
    }
  }
};
</script>


<style lang="scss" scoped>
.add-audit-fields {
  .addFields {
    width: 300px;
    height: 30px;
    margin: 10px auto;
    line-height: 30px;
    text-align: center;
    font-size: 12px;
    border-radius: 15px;
    border: 1px solid #86939e;
    cursor: pointer;
  }
}
</style>
