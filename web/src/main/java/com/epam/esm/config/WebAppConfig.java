package com.epam.esm.config;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.mapper.DtoMapper;
import com.epam.esm.model.Certificate;
import com.epam.esm.model.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan("com.epam.esm")
@EnableTransactionManagement
public class WebAppConfig {

//    @Bean
//    public LocalContainerEntityManagerFactoryBean
//    entityManagerFactoryBean() {
//        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
//        emf.setPackagesToScan(new String[]{"com.epam.esm"});
//        emf.setJpaVendorAdapter(
//                new HibernateJpaVendorAdapter());
//        return emf;
//    }
//
//    @Bean
//    public PlatformTransactionManager transactionManager() {
//        JpaTransactionManager transactionManager
//                = new JpaTransactionManager();
//        transactionManager.setEntityManagerFactory(
//                entityManagerFactoryBean().getObject());
//        return transactionManager;
//    }

    @Bean
    public Tag tag() {
        return new Tag();
    }

    @Bean
    public Certificate certificate() {
        return new Certificate();
    }

    @Bean
    public TagDto tagDto() {
        return new TagDto();
    }

    @Bean
    public CertificateDto certificateDto() {
        return new CertificateDto();
    }

    @Bean
    public DtoMapper dtoMapper() {
        return new DtoMapper();
    }

    @Bean
    public ModelMapper mapper() {
        return new ModelMapper();
    }
}
