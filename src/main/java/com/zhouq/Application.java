package com.zhouq;

import com.zhouq.entity.DB.Species;
import com.zhouq.service.ISpeciesService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.oas.annotations.EnableOpenApi;

import java.math.BigDecimal;
import java.util.Arrays;

@SpringBootApplication
@EnableOpenApi
@EnableWebMvc
@MapperScan("com.zhouq.mapper")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner seedSpeciesData(ISpeciesService speciesService) {
        return args -> {
            if (speciesService.count() > 0) {
                return;
            }
            speciesService.saveBatch(Arrays.asList(
                buildSpecies("中华白海豚", "Sousa chinensis", "脊索动物门", "哺乳纲", "鲸偶蹄目", "海豚科", "Sousa", "chinensis", "体色灰白至粉白，吻部细长", "近岸活动，常小群出现", "珠江口、雷州湾、北部湾沿海", "国家一级", "易危", "110.420500", "20.045300"),
                buildSpecies("鹿角珊瑚", "Acropora formosa", "刺胞动物门", "珊瑚纲", "石珊瑚目", "鹿角珊瑚科", "Acropora", "formosa", "分枝明显，外形似鹿角", "依赖透明海水和稳定光照", "海南三亚近岸珊瑚礁区", "无危", "近危", "109.512300", "18.238600"),
                buildSpecies("鳗草", "Zostera marina", "被子植物门", "单子叶植物纲", "泽泻目", "鳗草科", "Zostera", "marina", "叶片带状，浅海成片生长", "适应浅海弱浪环境", "厦门湾、胶州湾海草床区域", "无危", "无危", "118.150200", "24.432100"),
                buildSpecies("弹涂鱼", "Periophthalmus cantonensis", "脊索动物门", "辐鳍鱼纲", "鲈形目", "虾虎鱼科", "Periophthalmus", "cantonensis", "可短时离水活动，胸鳍发达", "常见于潮滩泥面觅食", "雷州湾红树林潮滩区域", "无危", "无危", "110.348900", "20.029800")
            ));
        };
    }

    private Species buildSpecies(String chineseName, String latinName, String phylum, String klass,
                                 String order, String family, String genus, String speciesName,
                                 String features, String habits, String distribution,
                                 String protectLevel, String endangeredStatus,
                                 String longitude, String latitude) {
        Species species = new Species();
        species.setChineseName(chineseName);
        species.setLatinName(latinName);
        species.setPhylum(phylum);
        species.setKlass(klass);
        species.setOrder(order);
        species.setFamily(family);
        species.setGenus(genus);
        species.setSpecies(speciesName);
        species.setFeatures(features);
        species.setHabits(habits);
        species.setDistribution(distribution);
        species.setProtectLevel(protectLevel);
        species.setEndangeredStatus(endangeredStatus);
        species.setLongitude(new BigDecimal(longitude));
        species.setLatitude(new BigDecimal(latitude));
        return species;
    }
}
