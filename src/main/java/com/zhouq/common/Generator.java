package com.zhouq.common;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.fill.Column;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 代码生成器
 * </p>
 */
@Component
public class Generator {


    private static final String URL = "jdbc:mysql://127.0.0.1:3306/marine_biodiv?serverTimezone=Asia/Shanghai&rewriteBatchedStatements=true";

    private static final String USERNAME = "root";

    private static final String PASSWORD = "123456";
    
    private static final String OUTPUT_DIR = System.getProperty("user.dir") + "\\src\\main\\java";


    public static void main(String[] args) {
        FastAutoGenerator.create(URL,USERNAME,PASSWORD)
                .globalConfig(builder -> {
                    builder.author("sweng-vision")
                            .fileOverride() // 开启文件覆盖，替换旧系统代码
                            .enableSwagger()
                            .outputDir(OUTPUT_DIR);
                })
                .packageConfig(builder -> {
                    builder.parent("com.zhouq")
                            .entity("entity.DB")
                            .mapper("mapper")
                            .controller("controller")
                            .service("service")
                            .serviceImpl("service.impl")
                            .xml("mapper.xml");
                })
                .strategyConfig(builder -> {
                    // 指定需要生成的海洋系统新表
                    builder.addInclude("role", "user", "species", "ecosystem", "observation", "observation_species", "sys_log", "permission", "role_permission")
                            .entityBuilder()
                            .enableLombok()
                            .enableTableFieldAnnotation()
                            .idType(IdType.AUTO) // 新表均采用自增ID
                            .addTableFills(new Column("create_time", FieldFill.INSERT))
                            .addTableFills(new Column("update_time",FieldFill.INSERT_UPDATE))
                            .controllerBuilder()
                            .enableRestStyle();
                }).execute();

    }
}
