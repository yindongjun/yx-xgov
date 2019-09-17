<el-dialog
    title="提示"
    :visible.sync="dialogVisible"
    width="760px">
    <el-form :model="catalogueForm" ref="ruleCatalogue"  label-width="100px" label-position="right">
        <el-form-item label="上级目录">
            <el-input v-model="catalogueForm.prevName" size="medium"></el-input>
        </el-form-item>
        <el-form-item label="目录名称">
            <el-input v-model="catalogueForm.currName" size="medium"></el-input>
        </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false"  type="info" size="small">取 消</el-button>
        <el-button type="danger" size="small" @click="submitForm('ruleForm')" >确 定</el-button>
    </span>
</el-dialog>
