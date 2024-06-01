/*
 * Copyright 2021 Projeto e-cordel (http://ecordel.com.br)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package br.com.itsmemario.ecordel.security.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class JwtTokenService {

    public static final String SECRET_KEY = "secretKey";
    private final JwtTokenRepository repository;

    @Cacheable(cacheNames = {SECRET_KEY})
    public JwtToken findTop() {
        return repository.findFirstByOrderByIdDesc();
    }

    @CacheEvict(allEntries = true, cacheNames = { SECRET_KEY })
    @Scheduled(fixedDelay = 30, timeUnit = TimeUnit.MINUTES)
    public void cacheEvict() {
        // spring will call this method to perform cache eviction
    }

}
