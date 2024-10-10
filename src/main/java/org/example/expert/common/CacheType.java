package org.example.expert.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CacheType {
    USER_PROFILE("user", 12, 10000);

    private final String cacheName;
    private final int expired;
    private final int maxSizes;
}
