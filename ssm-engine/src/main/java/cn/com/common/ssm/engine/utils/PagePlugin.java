package cn.com.common.ssm.engine.utils;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.executor.ExecutorException;
import org.apache.ibatis.executor.statement.BaseStatementHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.property.PropertyTokenizer;
import org.apache.ibatis.scripting.xmltags.ForEachSqlNode;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.common.ssm.engine.mapper.Page;

@Intercepts({ @Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class, Integer.class }) })
public class PagePlugin<T> implements Interceptor {

	private static Logger logger = LoggerFactory.getLogger(PagePlugin.class);
	 
    private static String pageSqlId = "";

    @SuppressWarnings("unchecked")
    public Object intercept(Invocation ivk) throws Throwable {

        if (ivk.getTarget() instanceof RoutingStatementHandler) {
            RoutingStatementHandler statementHandler = (RoutingStatementHandler) ivk.getTarget();
            BaseStatementHandler delegate = (BaseStatementHandler) ReflectHelper.getValueByFieldName(statementHandler, "delegate");
            MappedStatement mappedStatement = (MappedStatement) ReflectHelper.getValueByFieldName(delegate, "mappedStatement");

            if (mappedStatement.getId().matches(pageSqlId)) {
                BoundSql boundSql = delegate.getBoundSql();
                Object parameterObject = boundSql.getParameterObject();
                if (parameterObject == null) {
                    throw new NullPointerException("parameterObject error");
                } else {
                    Page<T> page = null;
                    if (parameterObject instanceof Page) {
                        page = (Page<T>) parameterObject;
                    } else if(parameterObject instanceof Map){
                        Map<String, Object> map = (Map<String, Object>)parameterObject;
                        if(map.get("page") instanceof Page){
	                        page = (Page<T>)map.get("page");
	                        if(page == null)
	                            page = new Page<T>();
                        }else{
                        	throw new NoSuchFieldException("PageInfo not instanceof com.panshi.basic.tools.PageInfo"); 
                        }
                    }else {
                        Field pageField = ReflectHelper.getFieldByFieldName(
                                parameterObject, "page");
                        if (pageField != null) {
                            page = (Page<T>) ReflectHelper.getValueByFieldName(
                                    parameterObject, "page");
                            if (page == null)
                                page = new Page<T>();
                            ReflectHelper.setValueByFieldName(parameterObject,
                                    "page", page);
                        } else {
                            throw new NoSuchFieldException(parameterObject
                                    .getClass().getName());
                        }
                    }
                    int rowCount = 0; 	//总记录数
                    if(page.getCount()==0){		//不重复查询总数 
	                    Connection connection = (Connection) ivk.getArgs()[0];
	                    String sql = boundSql.getSql();
	                    int fromIndex = sql.indexOf("from");
	                    fromIndex = fromIndex>-1?fromIndex:sql.indexOf("FROM");
	                    String countSql = "select count(0) "+sql.substring(fromIndex);
	//                    System.out.println("总数sql 语句:"+countSql);
	                    PreparedStatement countStmt = connection
	                            .prepareStatement(countSql);
	                    BoundSql countBS = new BoundSql(
	                            mappedStatement.getConfiguration(), countSql,
	                            boundSql.getParameterMappings(), parameterObject);
	                    setParameters(countStmt, mappedStatement, countBS,
	                            parameterObject);
	                    ResultSet rs = countStmt.executeQuery();
	                    if (rs.next()) {
	                    	rowCount = rs.getInt(1);
	                    }
	                    rs.close();
	                    countStmt.close();
                    }else{
                    	rowCount = page.getCount();
                    }
                    page.setCount(rowCount);
//                    page.compute(page.getNum(), rowCount);  //==================计算分页
                    String pageSql = generatePageSql(boundSql.getSql(), page);
                    logger.debug("生成的SQL为: " + pageSql);
//                    System.out.println("page sql:"+pageSql);
                    ReflectHelper.setValueByFieldName(boundSql, "sql", pageSql);
                }
            }
        }
        return ivk.proceed();
    }

    private void setParameters(PreparedStatement ps,
            MappedStatement mappedStatement, BoundSql boundSql,
            Object parameterObject) throws SQLException {
        ErrorContext.instance().activity("setting parameters")
                .object(mappedStatement.getParameterMap().getId());
        List<ParameterMapping> parameterMappings = boundSql
                .getParameterMappings();
        if (parameterMappings != null) {
            Configuration configuration = mappedStatement.getConfiguration();
            TypeHandlerRegistry typeHandlerRegistry = configuration
                    .getTypeHandlerRegistry();
            MetaObject metaObject = parameterObject == null ? null
                    : configuration.newMetaObject(parameterObject);
            for (int i = 0; i < parameterMappings.size(); i++) {
                ParameterMapping parameterMapping = parameterMappings.get(i);
                if (parameterMapping.getMode() != ParameterMode.OUT) {
                    Object value;
                    String propertyName = parameterMapping.getProperty();
                    PropertyTokenizer prop = new PropertyTokenizer(propertyName);
                    if (parameterObject == null) {
                        value = null;
                    } else if (typeHandlerRegistry
                            .hasTypeHandler(parameterObject.getClass())) {
                        value = parameterObject;
                    } else if (boundSql.hasAdditionalParameter(propertyName)) {
                        value = boundSql.getAdditionalParameter(propertyName);
                    } else if (propertyName
                            .startsWith(ForEachSqlNode.ITEM_PREFIX)
                            && boundSql.hasAdditionalParameter(prop.getName())) {
                        value = boundSql.getAdditionalParameter(prop.getName());
                        if (value != null) {
                            value = configuration.newMetaObject(value)
                                    .getValue(
                                            propertyName.substring(prop
                                                    .getName().length()));
                        }
                    } else {
                        value = metaObject == null ? null : metaObject
                                .getValue(propertyName);
                    }
                    TypeHandler typeHandler = parameterMapping.getTypeHandler();
                    if (typeHandler == null) {
                        throw new ExecutorException(
                                "There was no TypeHandler found for parameter "
                                        + propertyName + " of statement "
                                        + mappedStatement.getId());
                    }
                    typeHandler.setParameter(ps, i + 1, value,parameterMapping.getJdbcType());
                }
            }
        }
    }


    private String generatePageSql(String sql, Page page) {
        if (page != null) {
            StringBuffer pageSql = new StringBuffer();
                pageSql.append(sql);
                pageSql.append(" limit " + page.getStart() + ","
                        + page.getLength());
            return pageSql.toString();
        } else {
            return sql;
        }
    }

    public Object plugin(Object arg0) {
        return Plugin.wrap(arg0, this);
    }

    public void setProperties(Properties p) {
        pageSqlId = p.getProperty("pageSqlId");
    }

}