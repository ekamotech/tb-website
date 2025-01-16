package com.example.website;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * アプリケーションの設定クラス。
 * 各種Beanの定義を行います。
 */
@Configuration
public class AppConfig {

    /**
     * ModelMapperのBeanを定義します。
     * 
     * @return ModelMapperのインスタンス
     */
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
