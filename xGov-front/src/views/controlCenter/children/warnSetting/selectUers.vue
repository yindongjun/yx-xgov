<template>
  <el-dialog
    width="700px"
    title="选择告警接收人"
    :visible.sync="visible"
    :close-on-click-modal="false"
    :before-close="closeDialog"
    @open='open'
    class="selectedUsers"
    append-to-body>
      <div class="content">
        <div class="depart">
          <div class="title">部门列表</div>
          <!-- <ul>
            <li v-for="(item, i) in departList" :key="i" @click="clickDepart(item)">{{item.name}}</li>
          </ul> -->
          <!-- <vue-scroll> -->
            <el-tree ref="tree" :default-expand-all='true' highlight-current :data="departList" :props="defaultProps" node-key="id" :expand-on-click-node='false' @node-click="handleNodeClick"></el-tree>
          <!-- </vue-scroll> -->
        </div>
        <div class="user">
          <div class="title">用户列表</div>
            <el-checkbox v-if="userList.length" style="float: left;width: 100%;margin-top: 5px;" :indeterminate="isIndeterminate" v-model="checkAll" @change="handleCheckAllChange">全选</el-checkbox>
            <el-checkbox-group v-if="userList.length" v-model="selectData" @change="handleCheckedCitiesChange">
              <el-checkbox style="float: left;width: 100%;margin-top: 5px;" v-for="user in userList" :label="user" :key="user.id">{{user.username}}</el-checkbox>
            </el-checkbox-group>
            <div v-else style="margin: 30px auto; font-szie: 12px;margin-left: 100px; color:#909399;">暂无数据</div>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button type="info" @click="closeDialog" size="small">取 消</el-button>
        <el-button type="danger" size="small" @click="submitForm()" :loading="buttonLoading">保 存</el-button>
      </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'selected',
  props: ['visible', 'type', 'ids'],
  data() {
    return {
      buttonLoading: false,
      departList: [],
      userList: [],
      selectData: [],
      checkAll: false,
      isIndeterminate: null,
      defaultProps: {
        children: 'children',
        label: 'name'
      }
    }
  },
  methods: {
    open() {
      this.getDepart();
    },
    getDepart() {
      this.$urlApi.dispatchTask.getDepartment({}).then(res => {
        this.departList = res.data;
        if (this.type === 'add' && !this.ids.length) {
          this.$nextTick(() => {
            res.data[0] && this.handleNodeClick(res.data[0])
          })
        } else if (this.ids.length) {
          this.getUserById();
        }
      });
    },
    getUserById() {
      this.$urlApi.dispatchTask.getUserById({id: this.ids[0]}).then(res => {
        const a = {
          id: res.data.department
        }
        this.handleNodeClick(a, 'save');
      });
    },
    clickDepart(item) {

    },
    submitForm() {
      this.$emit('selectCallback', this.selectData);
      this.resetData();
    },
    closeDialog() {
      this.$emit('selectCallback');
      this.resetData();
    },
    handleCheckAllChange(val) {
      this.selectData = val ? this.userList : [];
      this.isIndeterminate = false;
    },
    handleCheckedCitiesChange(value) {
      let checkedCount = value.length;
      this.checkAll = checkedCount === this.userList.length;
      this.isIndeterminate = checkedCount > 0 && checkedCount < this.userList.length;
    },
    handleNodeClick(data, b) {
      this.$refs.tree.setCurrentKey(data.id);
      this.isIndeterminate = null;
      this.checkAll = false;
      this.selectData = [];
      this.$urlApi.dispatchTask.getDepartMentPeople({ids: data.id}).then(res => {
        this.userList = res.data;
        if (this.ids.length && b === 'save') {
          let b = [];
          this.ids.forEach(c => {
            b.push(this.userList.find(k => k.id == c));
          })
          this.selectData = b;
          if (this.userList.length <= this.selectData.length) {
            this.checkAll = true;
          } else {
            this.isIndeterminate = true;
          }
        }
      });
    },
    resetData() {
      this.buttonLoading = false;
      this.departList = [];
      this.userList = [];
      this.selectData = [];
      this.checkAll = false;
      this.isIndeterminate = null;
    }
  }
}
</script>

<style lang="scss">
.selectedUsers {
  .content {
    height: 300px;
    .depart {
      float: left;
      width: 250px;
      margin-right: 30px;
      border: 1px solid #DCDFE6;
      height: 100%;
      padding: 10px 20px;
      box-sizing: border-box;
      overflow: auto;
      .title {
        font-size: 14px;
        margin: 10px 0;
      }
    }
    .user {
      float: left;
      width: 300px;
      border: 1px solid #DCDFE6;
      height: 100%;
      padding: 10px 20px;
      box-sizing: border-box;
      overflow: auto;
      .title {
        font-size: 14px;
        margin: 10px 0;
      }
      .el-checkbox__label {
        font-size: 12px;
      }
    }
  }
}
</style>
