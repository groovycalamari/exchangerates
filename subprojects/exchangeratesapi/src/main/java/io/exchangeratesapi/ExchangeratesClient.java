/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2019-2020 Sergio del Amo.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.exchangeratesapi;

import io.micronaut.core.convert.format.Format;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.retry.annotation.Retryable;
import io.micronaut.validation.Validated;
import io.reactivex.Single;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.List;

@Validated
@Client(
        value = "${" + ExchangerateConfigurationProperties.PREFIX + ".url:`" + ExchangerateConfigurationProperties.HOST_LIVE + "`}",
        configuration = ExchangerateConfigurationProperties.class
)
@Retryable(
        attempts = "${" + ExchangerateConfigurationProperties.PREFIX + ".retry.attempts:0}",
        delay = "${" + ExchangerateConfigurationProperties.PREFIX + ".retry.delay:5s}")
public interface ExchangeratesClient extends ExchangeratesApi {

    @Override
    @Nonnull
    @NotNull
    @Get("/latest{?base,symbols}")
    Single<ForeignExchangeReferenceRates> latest(@Nullable @QueryValue Currency base,
                                                 @Nullable @QueryValue List<Currency> symbols);
    @Override
    @Nonnull
    @NotNull
    @Get("/{date}{?base,symbols}")
    Single<ForeignExchangeReferenceRates> historicalRate(@Nonnull @NotNull @PastOrPresent @PathVariable @Format("yyyy-MM-dd") LocalDate date,
                                                         @Nullable @QueryValue Currency base,
                                                         @Nullable @QueryValue List<Currency> symbols);
    @Override
    @Nonnull
    @NotNull
    @Get("/history{?start_at,end_at,base,symbols}")
    Single<HistoricalForeignExchangeReferenceRates> history(@Nonnull @NotNull @PastOrPresent @Format("yyyy-MM-dd") @QueryValue(value = "start_at") LocalDate startAt,
                                                            @Nonnull @NotNull @PastOrPresent @Format("yyyy-MM-dd") @QueryValue(value = "end_at") LocalDate endAt,
                                                            @Nullable @QueryValue Currency base,
                                                            @Nullable @QueryValue List<Currency> symbols);
}
