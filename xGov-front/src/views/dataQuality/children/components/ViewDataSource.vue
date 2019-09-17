<template>
<div class="collect-datasource-edit">
  <el-form style="overflow: hidden" ref="baseform" :model="baseform" label-width="120px">
    <el-form-item style="float: left; width:46%" label="数据源名称" prop="dataSourceName">
      <label class='view-datasource-text'>{{ baseform.dataSourceName }}</label>
    </el-form-item>
    <el-form-item style="float: left; width:46%" label="数据源类型" prop="databaseType">
      <label class='view-datasource-text'>{{ baseform.databaseType }}</label>
    </el-form-item>
    <el-form-item style="float: left; width:100%" label="数据源描述" prop="description">
      <label class='view-datasource-text'>{{ baseform.description }}</label>
    </el-form-item>
  </el-form>
  <el-form style='overflow: hidden' ref="extraform" :model="extraform" label-width="120px">
    <template v-for="(item, index) in extraformItems">
      <el-form-item
        v-if="item.type === 'input'"
        :key="item.label + index"
        :style="'float: left; width:' + (item.isWidth ? '100%' : '46%')"
        :label="item.label"
        :prop="item.prop"
      >
        <label class='view-datasource-text'>{{ extraform[item.prop] }}</label>
      </el-form-item>
      <el-form-item
        :label="item.label"
        :style="'float: left; width:' + (item.isWidth ? '100%' : '46%')"
        v-if="item.type === 'select'"
        :key="item.label + index"
        :prop="item.prop"
      >
        <label class='view-datasource-text'>{{ item.options.find(c => c.value === extraform[item.prop]).label }}</label>
      </el-form-item>
      <el-form-item
        v-if="item.type === 'radio'"
        :label="item.label"
        :style="'float: left; width:' + (item.isWidth ? '100%' : '46%')"
        :key="item.label + index"
        :prop="item.prop"
      >
        <label class='view-datasource-text'>{{ item.options.find(c => c.value === extraform[item.prop]).text }}</label>
      </el-form-item>
    </template>
  </el-form>
</div>

</template>

<script>
import dataSourceConfig from './config'

export default {
  name: 'form-edit',
  data () {
    return {
      baseform: {
        databaseType: '',
        dataSourceName: '',
        description: ''
      },
      // 其他的form配置
      extraform: {},
      extraformItems: []
    }
  },
  methods: {
    // 初始表单
    resetForm () {
      this.$refs.baseform && this.$refs.baseform.resetFields()
      this.$refs.extraform && this.$refs.extraform.resetFields()
    },
    // 初始化页面数据
    resetData () {
      this.resetForm()
      this.extraform = {}
      this.extraformItems = []
    },
    // 获取表单数据
    getForm (data) {
      const forms = {}
      data.forEach(c => {
        forms[c.prop] = c.value
      })
      return { ...forms }
    },
    // 编辑数据源将会初始化页面数据
    init (data) {
      // extra表单数据
      this.$urlApi.datasourceManage.getSourceById({ id: data.datasourceId || data.id }).then(res => {
        const resData = res.data
        this.baseform.databaseType = resData.databaseType
        this.baseform.dataSourceName = resData.datasourceName
        this.baseform.description = resData.description

        this.extraformItems = dataSourceConfig[resData.databaseType]
        this.extraformItems.forEach(c => {
          if (resData[c.prop] !== undefined) {
            c.value = resData[c.prop]
          }
        })
        this.extraform = this.getForm(this.extraformItems)
        // 特殊的数据库特殊处理
        if (resData.databaseType === 'Hbase') {
          this.extraform['zkQuorum'] = resData.ip
        }
        if (resData.databaseType === 'HDFS') {
          const hdfsSource = res.hdfsSource
          this.extraform['defaultFs'] = hdfsSource['defaultFs']
          this.extraform['userName'] = resData['userName']
          this.extraform['haveKerberos'] = hdfsSource['haveKerberos']
        }
        if (resData.databaseType === 'Hive' || resData.databaseType === 'Impala') {
          const hiveSource = res.hiveSource
          this.extraform['defaultFs'] = hiveSource['defaultFs']
          this.extraform['warehouseDir'] = hiveSource['warehouseDir']
          this.extraform['url'] = hiveSource['url']
          this.extraform['userName'] = hiveSource['userName']
          this.extraform['password'] = hiveSource['password']
          this.extraform['haveKerberos'] = hiveSource['haveKerberos']
        }
      })
    }
  }
}
</script>

<style lang="scss">
.collect-datasource-edit {
  .el-form-item {
    margin-bottom: 0;
    .view-datasource-text {
      font-size: 12px;
    }
  }
}
</style>
