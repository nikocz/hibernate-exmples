package pl.nc.hibernate.examples.transaction;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
class TxThisVsSelfTestConfig {

  @Bean
  public TxThisVsSelfTestHelper thisVsSelfHelper(@Lazy final TxThisVsSelfTestHelper thisVsSelfHelper) {
    return new TxThisVsSelfTestHelper(thisVsSelfHelper);
  }
}
