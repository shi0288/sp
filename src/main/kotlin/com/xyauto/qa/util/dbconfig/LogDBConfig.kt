package com.xyauto.qa.util.dbconfig

import com.xyauto.qa.util.handle.PgArrHandler
import org.apache.ibatis.session.SqlSessionFactory
import org.mybatis.spring.SqlSessionFactoryBean
import org.mybatis.spring.SqlSessionTemplate
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import tk.mybatis.spring.annotation.MapperScan
import javax.sql.DataSource

/**
 * Created by shiqm on 2018-02-01.
 */


@Configuration
@MapperScan(basePackages = arrayOf("com.xyauto.qa.mapper.log"), sqlSessionTemplateRef = "logSessionTemplate")
class LogDBConfig  {

    @Bean(name = arrayOf("logDataSource"))
    @ConfigurationProperties(prefix = "spring.datasource.log")
    fun dataSource(): DataSource = DataSourceBuilder.create().build()

    @Bean(name = arrayOf("logSqlSessionFactory"))
    @Throws(Exception::class)
    fun sqlSessionFactory(@Qualifier("logDataSource") dataSource: DataSource): SqlSessionFactory {
        val bean = SqlSessionFactoryBean()
        bean.setDataSource(dataSource)
        bean.setTypeHandlers(arrayOf(PgArrHandler()))
        bean.setMapperLocations(PathMatchingResourcePatternResolver().getResources("classpath:mapper/log/*.xml"))
        return bean.`object`
    }

    @Bean(name = arrayOf("logTransactionManager"))
    fun transactionManager(@Qualifier("logDataSource") dataSource: DataSource): DataSourceTransactionManager =
            DataSourceTransactionManager(dataSource)

    @Bean(name = arrayOf("logSessionTemplate"))
    @Throws(Exception::class)
    fun sqlSessionTemplate(@Qualifier("logSqlSessionFactory") sqlSessionFactory: SqlSessionFactory): SqlSessionTemplate =
            SqlSessionTemplate(sqlSessionFactory)


}