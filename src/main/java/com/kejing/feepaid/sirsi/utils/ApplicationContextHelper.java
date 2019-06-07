package com.kejing.feepaid.sirsi.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
@Component
public class ApplicationContextHelper implements ApplicationContextAware {
	private static ApplicationContext appCtx;

	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		appCtx = applicationContext;
	}


	public static Object getBean(String beanName) {
		return appCtx.getBean(beanName);
	}
	
	/**
     * 通过class获取Bean.
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getBean(Class<T> clazz) {
        return appCtx.getBean(clazz);
    }

    /**
     * 通过name,以及Clazz返回指定的Bean
     *
     * @param name
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        return appCtx.getBean(name, clazz);
    }

}
