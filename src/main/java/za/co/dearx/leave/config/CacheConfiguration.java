package za.co.dearx.leave.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration =
            Eh107Configuration.fromEhcacheCacheConfiguration(
                CacheConfigurationBuilder
                    .newCacheConfigurationBuilder(Object.class, Object.class, ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                    .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                    .build()
            );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, za.co.dearx.leave.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, za.co.dearx.leave.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, za.co.dearx.leave.domain.User.class.getName());
            createCache(cm, za.co.dearx.leave.domain.Authority.class.getName());
            createCache(cm, za.co.dearx.leave.domain.User.class.getName() + ".authorities");
            createCache(cm, za.co.dearx.leave.domain.PersistentToken.class.getName());
            createCache(cm, za.co.dearx.leave.domain.User.class.getName() + ".persistentTokens");
            createCache(cm, za.co.dearx.leave.domain.Role.class.getName());
            createCache(cm, za.co.dearx.leave.domain.Role.class.getName() + ".users");
            createCache(cm, za.co.dearx.leave.domain.Team.class.getName());
            createCache(cm, za.co.dearx.leave.domain.Team.class.getName() + ".members");
            createCache(cm, za.co.dearx.leave.domain.Staff.class.getName());
            createCache(cm, za.co.dearx.leave.domain.Staff.class.getName() + ".teams");
            createCache(cm, za.co.dearx.leave.domain.LeaveStatus.class.getName());
            createCache(cm, za.co.dearx.leave.domain.LeaveType.class.getName());
            createCache(cm, za.co.dearx.leave.domain.LeaveEntitlement.class.getName());
            createCache(cm, za.co.dearx.leave.domain.LeaveApplication.class.getName());
            createCache(cm, za.co.dearx.leave.domain.Comment.class.getName());
            createCache(cm, za.co.dearx.leave.domain.Decision.class.getName());
            createCache(cm, za.co.dearx.leave.domain.PublicHoliday.class.getName());
            createCache(cm, za.co.dearx.leave.domain.LeaveEntitlement.class.getName() + ".deductions");
            createCache(cm, za.co.dearx.leave.domain.LeaveApplication.class.getName() + ".deductions");
            createCache(cm, za.co.dearx.leave.domain.LeaveDeduction.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
