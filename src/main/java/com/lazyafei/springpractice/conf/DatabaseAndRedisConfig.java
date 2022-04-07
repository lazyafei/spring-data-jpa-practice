package com.lazyafei.springpractice.conf;

//import gov.cnao.security.service.EncryptDecryptService;
//import gov.cnao.security.service.impl.EncryptDecryptServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
//import redis.clients.jedis.JedisPoolConfig;

import javax.sql.DataSource;


@Configuration
@Slf4j
//@ConditionalOnProperty(value = "sjs.password.encrypt", havingValue = "true")
public class DatabaseAndRedisConfig {
//    @Autowired
//    private EncryptDecryptService encryptDecryptService;

//    @Value("${oa-server.redis.host}")
//    private String redisHost;
//
//    @Value("${oa-server.redis.port}")
//    private Integer redisPort;
//
//    @Value("${oa-server.redis.pword}")
//    private String redisPassword;
//
//    @Value("${oa-server.redis.pool.max-idle}")
//    private Integer redisPoolMaxIdle;
//
//    @Value("${oa-server.redis.pool.min-idle}")
//    private Integer redisPoolMinIdle;
//
//    @Value("${oa-server.redis.pool.max-active}")
//    private Integer redisPoolMaxActive;
//
//    @Value("${oa-server.redis.pool.max-wait}")
//    private Integer redisPoolMaxWait;
//
//    @Value("${oa-server.redis.pool.test-on-borrow:true}")
//    private Boolean testOnBorrow;
//
//    @Value("${oa-server.redis.pool.test-while-idle:true}")
//    private Boolean testWhileIdle;
//
//    @Value("${oa-server.redis.pool.test-on-return:true}")
//    private Boolean testOnReturn;
//
//    @Value("${oa-server.redis.timeout}")
//    private Integer redisTimeout;
//    @Value("${oa-server.redis.database:0}")
//    private Integer redisDatabase;


    @Value("${practice.datasource.pword}")
    private String datasourcePassword;

    @Bean
    @Primary
    @ConfigurationProperties("practice.datasource")
    public DataSourceProperties dataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public DataSource dataSource() {
        DataSourceProperties properties = dataSourceProperties();
        String password;
        try {
            //password = encryptDecryptService.decrypt(datasourcePassword);
            password = datasourcePassword;
            properties.setPassword(password);
        } catch (Exception e) {
            log.error("decrypt oa-server.datasource.password error, use origin password");
            properties.setPassword(datasourcePassword);
        }

        DataSource dataSource = properties.initializeDataSourceBuilder().type(org.apache.tomcat.jdbc.pool.DataSource.class).build();
        if (dataSource instanceof org.apache.tomcat.jdbc.pool.DataSource) {
            ((org.apache.tomcat.jdbc.pool.DataSource) dataSource).setValidationQuery("SELECT 1");
            ((org.apache.tomcat.jdbc.pool.DataSource) dataSource).setTestWhileIdle(true);
        }
        log.info("-------初始化神通数据库OK------");
        return dataSource;
    }


//    @Bean
//    public RedisConnectionFactory redisConnectionFactory() {
//        JedisConnectionFactory factory = new JedisConnectionFactory();
//        factory.setHostName(redisHost);
//        factory.setPort(redisPort);
//        factory.setDatabase(redisDatabase);
//        String password;
//        try {
//            password = encryptDecryptService.decrypt(redisPassword);
//        } catch (Exception e) {
//            log.error("decrypt oa-server.redis.password error, use origin password");
//            password = redisPassword;
//        }
//        factory.setPassword(password);
//        factory.setTimeout(redisTimeout);
//        JedisPoolConfig poolConfig = new JedisPoolConfig();
//        poolConfig.setMaxIdle(redisPoolMaxIdle);
//        poolConfig.setMinIdle(redisPoolMinIdle);
//        poolConfig.setMaxTotal(redisPoolMaxActive);
//        poolConfig.setMaxWaitMillis(redisPoolMaxWait);
//        poolConfig.setTestOnBorrow(testOnBorrow);
//        poolConfig.setTestWhileIdle(testWhileIdle);
//        poolConfig.setTestOnReturn(testOnReturn);
//        factory.setPoolConfig(poolConfig);
//        factory.setTimeout(redisTimeout);
//        return factory;
//    }
//
//    @Bean
//    public EncryptDecryptService encryptDecryptService() {
//        return new EncryptDecryptServiceImpl();
//    }


}
