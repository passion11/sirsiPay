package com.kejing.feepaid.sirsi.config;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;

import feign.codec.Encoder;
import feign.form.FormEncoder;

@Configuration
public class FromUrlEncodedClientConfiguration {
	@Autowired
    private ObjectFactory<HttpMessageConverters> messageConverters;

    @Bean
    @Scope(value=ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    @Primary
    Encoder feignFormEncoder() {
        return new FormEncoder(new SpringEncoder(this.messageConverters));
    }


}
