package com.peng.PengAirline.config;

import org.hibernate.collection.spi.PersistentCollection;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper(){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                // ✅ 启用字段级别映射
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
                .setMatchingStrategy(MatchingStrategies.STANDARD)
                // ✅ 忽略懒加载集合（防止 PersistentBag 报错）
                .setPropertyCondition(context ->
                        !(context.getSource() instanceof PersistentCollection))
                // ✅ 忽略歧义字段
                .setAmbiguityIgnored(true)
                // ✅ 跳过 null 值（防止覆盖已有字段）
                .setSkipNullEnabled(true);
        
        return modelMapper;
    }

}
