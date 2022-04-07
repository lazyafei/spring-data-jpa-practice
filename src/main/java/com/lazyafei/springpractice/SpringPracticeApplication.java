package com.lazyafei.springpractice;

import com.lazyafei.springpractice.persistence.impl.BatchSaveRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"com.lazyafei.springpractice"},exclude = {DataSourceAutoConfiguration.class, MailSenderAutoConfiguration.class})
@EntityScan({"com.lazyafei.springpractice.model.entity"})
@EnableJpaRepositories(value="com.lazyafei.springpractice.persistence",repositoryBaseClass = BatchSaveRepositoryImpl.class)

public class SpringPracticeApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringPracticeApplication.class, args);
    }

}
