package edu.socialnetwork.config;

import java.time.Duration;

import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;

import io.github.jhipster.config.jcache.BeanClassLoaderAwareJCacheRegionFactory;
import io.github.jhipster.config.JHipsterProperties;

import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        BeanClassLoaderAwareJCacheRegionFactory.setBeanClassLoader(this.getClass().getClassLoader());
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build());
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            cm.createCache(edu.socialnetwork.repository.UserRepository.USERS_BY_LOGIN_CACHE, jcacheConfiguration);
            cm.createCache(edu.socialnetwork.repository.UserRepository.USERS_BY_EMAIL_CACHE, jcacheConfiguration);
            cm.createCache(edu.socialnetwork.domain.User.class.getName(), jcacheConfiguration);
            cm.createCache(edu.socialnetwork.domain.Authority.class.getName(), jcacheConfiguration);
            cm.createCache(edu.socialnetwork.domain.User.class.getName() + ".authorities", jcacheConfiguration);
            cm.createCache(edu.socialnetwork.domain.Profile.class.getName(), jcacheConfiguration);
            cm.createCache(edu.socialnetwork.domain.Profile.class.getName() + ".sentInvitations", jcacheConfiguration);
            cm.createCache(edu.socialnetwork.domain.Profile.class.getName() + ".receivedInvitations", jcacheConfiguration);
            cm.createCache(edu.socialnetwork.domain.Profile.class.getName() + ".sentBlocks", jcacheConfiguration);
            cm.createCache(edu.socialnetwork.domain.Profile.class.getName() + ".receivedBlocks", jcacheConfiguration);
            cm.createCache(edu.socialnetwork.domain.Profile.class.getName() + ".sentMessages", jcacheConfiguration);
            cm.createCache(edu.socialnetwork.domain.Profile.class.getName() + ".adminChatrooms", jcacheConfiguration);
            cm.createCache(edu.socialnetwork.domain.Profile.class.getName() + ".joinedChatrooms", jcacheConfiguration);
            cm.createCache(edu.socialnetwork.domain.Gender.class.getName(), jcacheConfiguration);
            cm.createCache(edu.socialnetwork.domain.Gender.class.getName() + ".users", jcacheConfiguration);
            cm.createCache(edu.socialnetwork.domain.Ethnicity.class.getName(), jcacheConfiguration);
            cm.createCache(edu.socialnetwork.domain.Ethnicity.class.getName() + ".users", jcacheConfiguration);
            cm.createCache(edu.socialnetwork.domain.Relationship.class.getName(), jcacheConfiguration);
            cm.createCache(edu.socialnetwork.domain.Relationship.class.getName() + ".users", jcacheConfiguration);
            cm.createCache(edu.socialnetwork.domain.Location.class.getName(), jcacheConfiguration);
            cm.createCache(edu.socialnetwork.domain.Invitation.class.getName(), jcacheConfiguration);
            cm.createCache(edu.socialnetwork.domain.Block.class.getName(), jcacheConfiguration);
            cm.createCache(edu.socialnetwork.domain.Chatroom.class.getName(), jcacheConfiguration);
            cm.createCache(edu.socialnetwork.domain.Chatroom.class.getName() + ".participants", jcacheConfiguration);
            cm.createCache(edu.socialnetwork.domain.Chatroom.class.getName() + ".messages", jcacheConfiguration);
            cm.createCache(edu.socialnetwork.domain.Message.class.getName(), jcacheConfiguration);
            cm.createCache(edu.socialnetwork.domain.Profile.class.getName() + ".sentDirectMessages", jcacheConfiguration);
            cm.createCache(edu.socialnetwork.domain.Profile.class.getName() + ".receivedDirectMessages", jcacheConfiguration);
            cm.createCache(edu.socialnetwork.domain.DirectMessage.class.getName(), jcacheConfiguration);
            // jhipster-needle-ehcache-add-entry
        };
    }
}
