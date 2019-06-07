package com.kejing.feepaid.sirsi.config;


import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class DataSourceConfig {
	@Bean(name = "primaryDataSource") 
	@Qualifier("primaryDataSource") 
	@ConfigurationProperties(prefix="spring.datasource.primary")
	@Primary
	public DataSource primaryDataSource() 
	{ 
		return DataSourceBuilder.create().build(); 
		//return DataSourceBuilder.create().type(com.mchange.v2.c3p0.ComboPooledDataSource.class).build();
		
	} 


}
