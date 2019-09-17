<template>
  <el-dialog
    title="关联数据源"
    :visible.sync="dialog.visible"
    :before-close="closeDialog"
    :lock-scroll="false"
    :close-on-click-modal="false"
    class="relation-source"
    @open='openDialog'
    width="30rem">
    <el-form :rules="rules" ref="form" label-width="80px" :model="form">
      <el-form-item label="当前分层">
        <label style='font-size: 12px'>{{form.layer}}</label>
      </el-form-item>
      <el-form-item label="关联数据源">
        <el-select size="mini" style="width: 100%;" v-model="form.datasource" placeholder="请选择需要关联的数据源">
          <el-option v-for="(item, i) in datasourcelist" :key="i" :label="item.datasourceName" :value='item.id'></el-option>
        </el-select>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button class="reset-btn" type="info" size="small"  @click="cancelForm">取 消</el-button>
      <el-button type="danger" size="small" @click="submitForm" :loading="buttonLoading">保 存</el-button>
    </span>
  </el-dialog>
</template>

<script>
  export default {
    name: "relationsSource",
    props: {
      dialog: {
        type: Object,
        default: {}
      }
    },
    data () {
      return {
        form: {
          layer: '',
          datasource: ''
        },
        rules: {
          datasource: [
            { required: true, message: '请选择需要关联的数据源', trigger: 'change' }
          ]
        },
        datasourcelist: [],
        buttonLoading: false
      }
    },
    methods: {
      cancelForm() {
        this.$emit('relationSource')
        this.resetData()
      },
      submitForm() {
        this.$refs['form'].validate((valid) => {
          if (valid) {
            const params = {
              layerId: this.$props.dialog.data.layerId,
              sourceId: this.form.datasource
            }
            this.$urlApi.dataSource.addSourceOnLayer(params).then(res => {
              this.$toast('success', '关联成功！')
              this.$emit('relationSource', 1)
              this.resetData()
            });
          }
        })
      },
      resetData() {
        this.form = {
          layer: '',
          datasource: ''
        }
        this.datasourcelist = []
      },
      openDialog () {
        this.$urlApi.dataSource.getUnrelationSource().then(res => {
          this.datasourcelist = res.data
        });
        this.$urlApi.dataSource.getLayerPath({ layerId: this.$props.dialog.data.layerId }).then(res => {
          this.form.layer = res.data
        });
      },
      closeDialog () {
        this.$emit('relationSource')
        this.resetData()
      }
    }
  }
</script>

<style lang="scss">

</style>
