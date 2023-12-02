package com.laan.sportsda.config;

import com.laan.sportsda.client.CountryClient;
import com.laan.sportsda.util.PropertyUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.Locale;

@Configuration
@RequiredArgsConstructor
public class BeanConfig {

    private final PropertyUtil propertyUtil;

    @Bean
    public LocaleResolver localeResolver() {
        AcceptHeaderLocaleResolver localeResolver = new AcceptHeaderLocaleResolver();
        localeResolver.setDefaultLocale(Locale.US);
        return localeResolver;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public RestClient countryRestClient(RestClient.Builder restClientBuilder) {
        return restClientBuilder
                .requestFactory(new JdkClientHttpRequestFactory())
                .baseUrl(propertyUtil.getCountryApiBaseUrl())
                .build();
    }

    @Bean
    public CountryClient countryClient(RestClient countryRestClient) {
        return HttpServiceProxyFactory
                .builder()
                .exchangeAdapter(RestClientAdapter.create(countryRestClient))
                .build()
                .createClient(CountryClient.class);
    }

}
