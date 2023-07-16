package com.itjing.utilstudy.utils;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.benmanes.caffeine.cache.RemovalCause;
import com.github.benmanes.caffeine.cache.RemovalListener;
import com.itjing.utilstudy.pojo.User;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.Random;
import java.util.concurrent.TimeUnit;


/**
 * @Author: zhanling.li
 * @Version: 1.0
 * @BelongsProject: ComponentStudy
 * @BelongsPackage: com.itjing.utilstudy.utils
 * @CreateTime: 2023-07-16  22:24
 * @Description: TODO
 */
public class CacheUtils {
    /**
     * caffeine是基于java8开发的本地缓存库，spring内部的缓存使用的就是caffeine，caffeine提供了三种缓存驱逐策略[星星眼]，分别是基于容量、时间、引用
     */
    public static LoadingCache<Long, User> loadingCache = Caffeine.newBuilder()
            // 初始的缓存空间大小
            .initialCapacity(5)
            // 缓存的最大条数
            .maximumSize(10)
            .expireAfterWrite(4, TimeUnit.SECONDS)
            .expireAfterAccess(10, TimeUnit.SECONDS)
            .refreshAfterWrite(6, TimeUnit.SECONDS)
            .recordStats()
            //设置缓存的移除通知
            .removalListener(new RemovalListener<Long, User>() {
                @Override
                public void onRemoval(@Nullable Long key, @Nullable User user, @NonNull RemovalCause removalCause) {
                    System.out.printf("Key： %s ，值：%s was removed!原因 (%s) \n", key, user, removalCause);
                }
            })
            .build(id -> {
                System.out.println("缓存未命中，从数据库加载，用户id：" + id);
                return User.builder().id(id).userName("Lily").age(new Random().nextInt(20)).build();
            });
}
