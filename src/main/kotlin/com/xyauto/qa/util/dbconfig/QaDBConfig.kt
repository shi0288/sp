package com.xyauto.qa.util.dbconfig

import com.xyauto.qa.util.interceptor.ModuleTimeInterceptor
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
 * Created by shiqm on 2018-01-22.
 */
@Configuration
@MapperScan(basePackages = arrayOf("com.xyauto.qa.mapper.qa"), sqlSessionTemplateRef = "qaSessionTemplate")
class QaDBConfig {


    @Bean(name = arrayOf("qaDataSource"))
    @ConfigurationProperties(prefix = "spring.datasource.qa")
    fun dataSource(): DataSource = DataSourceBuilder.create().build()


    @Bean(name = arrayOf("qaSqlSessionFactory"))
    @Throws(Exception::class)
    fun sqlSessionFactory(@Qualifier("qaDataSource") dataSource: DataSource,@Qualifier("moduleTimeInterceptor") moduleTimeInterceptor: ModuleTimeInterceptor): SqlSessionFactory {
        val bean = SqlSessionFactoryBean()
        bean.setDataSource(dataSource)
        bean.setPlugins(arrayOf(moduleTimeInterceptor))
        bean.setMapperLocations(PathMatchingResourcePatternResolver().getResources("classpath:mapper/qa/*.xml"))
        return bean.`object`
    }

    @Bean(name = arrayOf("qaTransactionManager"))
    fun transactionManager(@Qualifier("qaDataSource") dataSource: DataSource): DataSourceTransactionManager =
            DataSourceTransactionManager(dataSource)

    @Bean(name = arrayOf("qaSessionTemplate"))
    @Throws(Exception::class)
    fun sqlSessionTemplate(@Qualifier("qaSqlSessionFactory") sqlSessionFactory: SqlSessionFactory): SqlSessionTemplate =
            SqlSessionTemplate(sqlSessionFactory)


}