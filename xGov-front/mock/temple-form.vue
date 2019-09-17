<el-form :model="ruleForm" ref="ruleForm"  label-width="100px" label-position="right">
    <el-form-item label="采集任务名称">
        <el-input v-model="ruleForm.name" placeholder="请填写采集任务名称" size="medium"></el-input>
    </el-form-item>
    <el-row>
        <el-col :span="12">
            <el-form-item label="元模型">
                <el-input v-model="ruleForm.model" size="medium"></el-input>
            </el-form-item>
        </el-col>
        <el-col :span="12">
            <el-form-item label="数据源名称">
                <el-input v-model="ruleForm.dataSourceName" size="medium"></el-input>
            </el-form-item>
        </el-col>
    </el-row>
    <el-row>
        <el-col :span="12">
            <el-form-item label="挂载目录">
                <el-input v-model="ruleForm.mulu" size="medium"></el-input>
            </el-form-item>
        </el-col>
        <el-col :span="12">
            <el-form-item label="调度方式">
                <el-select v-model="ruleForm.dispatch" size="medium" placeholder="请选择调度方式">
                    <el-option value="1" label="采集一次"></el-option>
                    <el-option value="2" label="多次采集"></el-option>
                </el-select>
            </el-form-item>
        </el-col>
    </el-row>
</el-form>
