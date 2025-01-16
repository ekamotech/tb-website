package com.example.website;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * アプリケーションのエントリーポイントクラス。
 * Spring Bootアプリケーションを起動します。
 */
@SpringBootApplication
public class TbWebsiteApplication extends SpringBootServletInitializer {

    /**
     * アプリケーションのエントリーポイント。
     * Spring Bootアプリケーションを起動します。
     *
     * @param args コマンドライン引数
     */
    public static void main(String[] args) {
        SpringApplication.run(TbWebsiteApplication.class, args);
    }
    

    /**
     * JARファイルとしてデプロイするための設定を行います。
     *
     * @param application SpringApplicationBuilderオブジェクト
     * @return 設定されたSpringApplicationBuilderオブジェクト
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(TbWebsiteApplication.class);
    }

}
