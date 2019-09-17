<template>
  <section class="group-relation">
    <el-row class="group-relation-t">
      <div class="model-title">
        <span class="model-title-l">组合关系列表</span>
      </div>
      <el-table :data="groupData1" style="width: 100%">
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
        <el-table-column prop="modelCode" label="组合端代码"></el-table-column>
        <el-table-column prop="modelName" label="组合端名称"></el-table-column>
        <el-table-column prop="combinModelCode" label="被组合端代码">
        </el-table-column>
        <el-table-column prop="combinModelName" label="被组合端名称">
        </el-table-column>
      </el-table>
    </el-row>
    <el-row class="group-relation-b mt15">
      <div class="model-title">
        <span class="model-title-l">被组合关系列表</span>
      </div>
      <el-table :data="groupData2" style="width: 100%">
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
        <el-table-column prop="modelCode" label="组合端代码"> </el-table-column>
        <el-table-column prop="modelName" label="组合端名称"> </el-table-column>
        <el-table-column prop="combinModelCode" label="被组合端代码">
        </el-table-column>
        <el-table-column prop="combinModelName" label="被组合端名称">
        </el-table-column>
      </el-table>
    </el-row>
  </section>
</template>

<script>
export default {
  name: "",
  components: {},
  props: ["BasicMsgTab", "GroupRelationTabTime"],
  data() {
    return {
      groupData1: [],
      groupData2: []
    };
  },
  methods: {
    getMetamodelGroup() {
      this.$urlApi.metaData
        .getMetamodelGroup({ modelId: this.BasicMsgTab.id })
        .then(res => {
          this.groupData1 = res.data.com;
          this.groupData2 = res.data.comed;
        });
    }
  },
  mounted: function() {},
  watch: {
    GroupRelationTabTime(val, oldVal) {
      this.getMetamodelGroup();
    }
  }
};
</script>

<style lang="scss">
</style>
