package org.project36.qualopt.config;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.jsr107.Eh107Configuration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.jhipster.config.JHipsterProperties;

@Configuration
@EnableCaching
@AutoConfigureAfter(value = { MetricsConfiguration.class })
@AutoConfigureBefore(value = { WebConfigurer.class, DatabaseConfiguration.class })
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(
                    Duration.of(
                        (long) ehcache.getTimeToLiveSeconds(), 
                        ChronoUnit.SECONDS)))
                .build());
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            cm.createCache(org.project36.qualopt.domain.User.class.getName(), jcacheConfiguration);
            cm.createCache(org.project36.qualopt.domain.Authority.class.getName(), jcacheConfiguration);
            cm.createCache(org.project36.qualopt.domain.User.class.getName() + ".authorities", jcacheConfiguration);
            cm.createCache(org.project36.qualopt.domain.PersistentToken.class.getName(), jcacheConfiguration);
            cm.createCache(org.project36.qualopt.domain.User.class.getName() + ".persistentTokens",
                    jcacheConfiguration);
            cm.createCache(org.project36.qualopt.domain.SocialUserConnection.class.getName(), jcacheConfiguration);
            cm.createCache(org.project36.qualopt.domain.Study.class.getName(), jcacheConfiguration);
            cm.createCache(org.project36.qualopt.domain.Study.class.getName() + ".participants", jcacheConfiguration);
            cm.createCache(org.project36.qualopt.domain.Participant.class.getName(), jcacheConfiguration);
            cm.createCache(org.project36.qualopt.domain.Participant.class.getName() + ".studies", jcacheConfiguration);
            cm.createCache(org.project36.qualopt.domain.Email.class.getName(), jcacheConfiguration);
            cm.createCache(org.project36.qualopt.domain.EmailTemplate.class.getName(), jcacheConfiguration);
            cm.createCache(org.project36.qualopt.domain.Document.class.getName(), jcacheConfiguration);
            // jhipster-needle-ehcache-add-entry
        };
    }
}
