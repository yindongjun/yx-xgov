<template>
  <section class="depend-relation">
    <el-row class="depend-relation-t">
      <div class="model-title">
        <span class="model-title-l">依赖关系列表</span>
      </div>
      <el-table :data="dependData1" style="width: 100%">
        <el-table-column
          prop="code"
          :show-overflow-tooltip="true"
          label="关系代码"
        >
        </el-table-column>
        <el-table-column
          prop="name"
          :show-overflow-tooltip="true"
          label="关系名称"
        >
        </el-table-column>
        <el-table-column prop="modelCode" label="依赖端代码"> </el-table-column>
        <el-table-column prop="modelName" label="依赖端名称"> </el-table-column>
        <el-table-column prop="depModelCode" label="被依赖端代码">
        </el-table-column>
        <el-table-column prop="depModelName" label="被依赖端名称">
        </el-table-column>
      </el-table>
    </el-row>
    <el-row class="depend-relation-b mt15">
      <div class="model-title">
        <span class="model-title-l">被依赖关系列表</span>
      </div>
      <el-table :data="dependData2" style="width: 100%">
        <el-table-column
          prop="code"
          :show-overflow-tooltip="true"
          label="关系代码"
        >
        </el-table-column>
        <el-table-column
          prop="name"
          :show-overflow-tooltip="true"
          label="关系名称"
        >
        </el-table-column>
        <el-table-column prop="modelCode" label="依赖端代码"> </el-table-column>
        <el-table-column prop="modelName" label="依赖端名称"> </el-table-column>
        <el-table-column prop="depModelCode" label="被依赖端代码">
        </el-table-column>
        <el-table-column prop="depModelName" label="被依赖端名称">
        </el-table-column>
      </el-table>
    </el-row>
  </section>
</template>

<script>
export default {
  name: "",
  components: {},
  props: ["BasicMsgTab", "DependRelationTabTime"],
  data() {
    return {
      dependData1: [],
      dependData2: []
    };
  },
  methods: {
    getMetamodelDepend() {
      this.$urlApi.metaData
        .getMetamodelDepend({ modelId: this.BasicMsgTab.id })
        .then(res => {
          this.dependData1 = res.data.dep;
          this.dependData2 = res.data.deped;
        });
    }
  },
  mounted: function() {},
  watch: {
    DependRelationTabTime(val, oldVal) {
      this.getMetamodelDepend();
    }
  }
};
</script>

<style lang="scss">
</style>
