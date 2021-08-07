package com.spring;

/**
 * @author xsf
 */
public class ShaoFeiApplicationContext {

    private Class configClass;

    public ShaoFeiApplicationContext(Class configClass) {
        this.configClass = configClass;

        //拿到配置类然后获取扫描路径
        configClass.getAnnotation(ComponentScan.class);
    }

    public Object getBean(String beanName){
        return null;
    }
}
