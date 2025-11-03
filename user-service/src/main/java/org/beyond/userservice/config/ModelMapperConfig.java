package org.beyond.userservice.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

  /* ModelMapper : 런타임 시 객체 간의 매핑을 자동화 해주는 라이브러리 */
  @Bean
  public ModelMapper modelMapper() {
    ModelMapper modelMapper = new ModelMapper();
    modelMapper.getConfiguration()
               /* 필드명이 온전히 같을 경우만 매핑 */
               .setMatchingStrategy(MatchingStrategies.STRICT)
               /* Entity와 Setter 메소드 미사용으로 필드 접근 허용 */
               .setFieldAccessLevel(
                   org.modelmapper.config.Configuration.AccessLevel.PRIVATE
               )
               .setFieldMatchingEnabled(true);
    return modelMapper;
  }
}
