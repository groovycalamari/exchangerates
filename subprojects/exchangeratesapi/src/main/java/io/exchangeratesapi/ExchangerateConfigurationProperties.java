/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2019 Sergio del Amo.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.exchangeratesapi;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.http.client.HttpClientConfiguration;
import io.micronaut.runtime.ApplicationConfiguration;

import javax.annotation.Nonnull;
import javax.validation.constraints.NotBlank;
import java.time.Duration;

/**
 * {@link ConfigurationProperties} for {@link ExchangeratesClient}.
 */
@ConfigurationProperties(ExchangerateConfigurationProperties.PREFIX)
public class ExchangerateConfigurationProperties extends HttpClientConfiguration {
    public static final String PREFIX = "exchangerate";
    public static final String HOST_LIVE = "https://api.exchangeratesapi.io";

    private final ExchangerateConnectionPoolConfiguration connectionPoolConfiguration;

    @Nonnull
    @NotBlank
    private String url = HOST_LIVE;

    public ExchangerateConfigurationProperties(final ApplicationConfiguration applicationConfiguration,
                                               final ExchangerateConnectionPoolConfiguration connectionPoolConfiguration) {
        super(applicationConfiguration);
        this.connectionPoolConfiguration = connectionPoolConfiguration;
    }

    @NotBlank
    @Nonnull
    public String getUrl() {
        return this.url;
    }

    public void setUrl(@Nonnull @NotBlank String url) {
        this.url = url;
    }

    @Override
    public ConnectionPoolConfiguration getConnectionPoolConfiguration() {
        return this.connectionPoolConfiguration;
    }

    /**
     * {@link ConnectionPoolConfiguration} for {@link ExchangeratesClient}.
     */
    @ConfigurationProperties(ConnectionPoolConfiguration.PREFIX)
    public static class ExchangerateConnectionPoolConfiguration extends ConnectionPoolConfiguration {
    }

    /**
     * Extra {@link ConfigurationProperties} to set the values for the {@link io.micronaut.retry.annotation.Retryable} annotation on {@link ExchangeRatesClient}.
     */
    @ConfigurationProperties(ExchangerateRetryConfiguration.PREFIX)
    public static class ExchangerateRetryConfiguration {

        public static final String PREFIX = "retry";

        private static final Duration DEFAULT_DELAY = Duration.ofSeconds(5);
        private static final int DEFAULT_ATTEMPTS = 0;

        /**
         * @return The delay between retry attempts
         */
        private Duration delay = DEFAULT_DELAY;

        /**
         * @return The maximum number of retry attempts
         */
        private int attempts = DEFAULT_ATTEMPTS;

        public Duration getDelay() {
            return delay;
        }

        public void setDelay(Duration delay) {
            this.delay = delay;
        }

        public int getAttempts() {
            return attempts;
        }

        public void setAttempts(int attempts) {
            this.attempts = attempts;
        }
    }
}
