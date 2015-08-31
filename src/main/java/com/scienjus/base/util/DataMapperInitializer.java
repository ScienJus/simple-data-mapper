package com.scienjus.base.util;

import org.apache.commons.lang.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.SystemPropertyUtils;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by Administrator on 2015/8/30.
 */
@Configuration
@ConfigurationProperties("initializer")
public class DataMapperInitializer {

    private String scanDomainPackage;

    public void setScanDomainPackage(String scanDomainPackage) {
        this.scanDomainPackage = scanDomainPackage;
    }

    @Bean(name = "dataSource", destroyMethod = "close")
    @ConfigurationProperties("dataSource")
    public DataSource dataSource() {
        return new org.apache.tomcat.jdbc.pool.DataSource();
    }


    @PostConstruct
    public void init() throws Exception {
        Set<Class> classes = scanClass();
        for (Class clazz : classes) {
            initTableMapper(clazz);
        }
    }

    private void initTableMapper(Class clazz) throws NoSuchMethodException {
        Table table = (Table) clazz.getAnnotation(Table.class);
        if (table != null) {
            TableUtil.setTableName(clazz, table.name());
            TableUtil.setSelectSQL(clazz, table.selectSQL());
            String[] keys = table.keys();
            TableUtil.setIdSelect(clazz, buildIdSelect(keys));
            TableUtil.setPrimaryKeyGetters(clazz, buildPrimaryKeyGetters(clazz, keys));
        }
    }

    private Map<String, Method> buildPrimaryKeyGetters(Class clazz, String[] keys) throws NoSuchMethodException {
        Map<String, Method> primaryKeyGetters = new LinkedHashMap<>();
        for (String key : keys) {
            String getterName = getFieldGetterName(key);
            Method getter = clazz.getMethod(getterName);
            primaryKeyGetters.put(key, getter);
        }
        return primaryKeyGetters;
    }

    private static String getFieldGetterName(String field) {
        String[] temp = field.split("_");
        StringBuilder getterName = new StringBuilder("get");
        for (int i = 0; i < temp.length; i++) {
            char[] chars = temp[i].toCharArray();
            chars[0]=Character.toUpperCase(chars[0]);
            temp[i] = new String(chars);
            getterName.append(temp[i]);
        }
        return getterName.toString();
    }

    private String buildIdSelect(String[] keys) {
        List<String> fields = new ArrayList<>();
        for (String key : keys) {
            fields.add(key.concat(" = ?"));
        }
        return  "where ".concat(StringUtils.join(fields, " and "));
    }

    private Set<Class> scanClass() throws IOException {
        String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
                + org.springframework.util.ClassUtils
                .convertClassNameToResourcePath(SystemPropertyUtils
                        .resolvePlaceholders(scanDomainPackage))
                + "/**/*.class";
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resourcePatternResolver.getResources(packageSearchPath);

        MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(
                resourcePatternResolver);
        Set<Class> classes = new HashSet<>();
        for (int i = 0; i < resources.length; i++) {
            Resource resource = resources[i];
            if (resource.isReadable()) {
                MetadataReader metadataReader = metadataReaderFactory
                        .getMetadataReader(resource);
                ScannedGenericBeanDefinition scan = new ScannedGenericBeanDefinition(metadataReader);
                if (scan.getMetadata().hasAnnotation(Table.class.getName())) {
                    try {
                        classes.add(Class.forName(metadataReader
                                .getClassMetadata().getClassName()));
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return classes;
    }
}
