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

import io.micronaut.core.convert.format.Format;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.QueryValue;
import io.reactivex.Single;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.List;

/**
 * @see <a href="https://exchangeratesapi.io">Foreign exchange rates API</a>.
 */
public interface ExchangeratesApi {

    /**
     *
     * @param base Currency rates are quoted against
     * @param symbols Currencies for which you would like to obtain rates
     * @return Latest foreign exchange reference rates.
     */
    @Nonnull
    @NotNull
    Single<ForeignExchangeReferenceRates> latest(@Nullable @QueryValue Currency base,
                                                 @Nullable @QueryValue List<Currency> symbols);

    /**
     *
     * @return Latest Foreign exchange reference rates.
     */
    default
    @Nonnull
    @NotNull
    Single<ForeignExchangeReferenceRates> latest() {
        return latest(null, null);
    }

    /**
     *
     * @param date
     * @param base Currency rates are quoted against
     * @param symbols Currencies for which you would like to obtain rates
     * @return Foreign exchange reference rates.
     */
    @Nonnull
    @NotNull
    Single<ForeignExchangeReferenceRates> historicalRate(@Nonnull @NotNull @PastOrPresent @PathVariable @Format("yyyy-MM-dd") LocalDate date,
                                                 @Nullable @QueryValue Currency base,
                                                 @Nullable @QueryValue List<Currency> symbols);

    /**
     *
     * @param date
     * @return Foreign exchange reference rates.
     */
    @Nonnull
    @NotNull
    default Single<ForeignExchangeReferenceRates> historicalRate(@Nonnull @NotNull @PastOrPresent @PathVariable @Format("yyyy-MM-dd") LocalDate date) {
        return historicalRate(date, null, null);
    }

    /**
     *
     * @param startAt
     * @param endAt
     * @param base Currency rates are quoted against
     * @param symbols Currencies for which you would like to obtain rates
     * @return Historical Foreign exchange reference rates.
     */
    @Nonnull
    @NotNull
    Single<HistoricalForeignExchangeReferenceRates> history(@Nonnull @NotNull @PastOrPresent @Format("yyyy-MM-dd") @QueryValue(value = "start_at") LocalDate startAt,
                                                            @Nonnull @NotNull @PastOrPresent @Format("yyyy-MM-dd") @QueryValue(value = "end_at") LocalDate endAt,
                                                            @Nullable @QueryValue Currency base,
                                                            @Nullable @QueryValue List<Currency> symbols);

    /**
     *
     * @param startAt
     * @param endAt
     * @return Historical Foreign exchange reference rates.
     */
    @Nonnull
    @NotNull
    default Single<HistoricalForeignExchangeReferenceRates> history(@Nonnull @NotNull @PastOrPresent @Format("yyyy-MM-dd") @QueryValue(value = "start_at") LocalDate startAt,
                                                            @Nonnull @NotNull @PastOrPresent @Format("yyyy-MM-dd") @QueryValue(value = "end_at") LocalDate endAt) {
        return history(startAt, endAt, null, null);
    }
}
