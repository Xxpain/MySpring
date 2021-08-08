package com.spring;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xsf
 */
public class ShaoFeiApplicationContext {

    private Class configClass;

    private ConcurrentHashMap<String, Object> singletonObjects = new ConcurrentHashMap<>(); // 单例池

    private ConcurrentHashMap<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

    public ShaoFeiApplicationContext(Class configClass) {
        this.configClass = configClass;

        //拿到配置类然后获取扫描路径
        ComponentScan componentScan = (ComponentScan) configClass.getDeclaredAnnotation(ComponentScan.class);
        String path = componentScan.value(); // 扫描路径 com.zhiyuan.service
        path = path.replace(".", "/");
        ClassLoader classLoader = configClass.getClassLoader();
        URL resource = classLoader.getResource(path);
        File file = new File(resource.getFile());
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                String fileName = f.getAbsolutePath();
                String className = fileName.substring(fileName.indexOf("com"), fileName.indexOf(".class"));
                className = className.replace("\\", ".");
                try {
                    Class<?> aClass = classLoader.loadClass(className);
                    if (aClass.isAnnotationPresent(Component.class)) {
                        //这是一个bean 需要生成一个bean对象
                        Component declaredAnnotation = aClass.getDeclaredAnnotation(Component.class);
                        String beanName = declaredAnnotation.value();

                        BeanDefinition beanDefinition = new BeanDefinition();
                        beanDefinition.setClazz(aClass);

                        if (!aClass.isAnnotationPresent(Scope.class)) {
                            //单例
                            beanDefinition.setScope("singleton");
                            Object bean = createBean(beanDefinition);
                            singletonObjects.put(beanName,bean);
                        } else {
                            //todo 多例
                            beanDefinition.setScope("");
                        }
                        beanDefinitionMap.put(beanName, beanDefinition);

                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }

        }
        //获取扫描路径

    }

    public Object createBean(BeanDefinition beanDefinition) {
        Class cls = beanDefinition.getClazz();
        try {
            Constructor declaredConstructor = cls.getDeclaredConstructor();
            return declaredConstructor.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }


    public Object getBean(String beanName) {
        if (beanDefinitionMap.containsKey(beanName)) {
            BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
            if (beanDefinition.getScope().equals("singleton")) {
                Object o = singletonObjects.get(beanName);
                return o;
            } else {
                Object bean = createBean(beanDefinition);
                return bean;
            }
        } else {
            // 不存在对应的Bean
            throw new NullPointerException();
        }
    }


}
