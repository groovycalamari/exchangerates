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

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.convert.format.Format;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Introspected
public class HistoricalForeignExchangeReferenceRates {

    @JsonProperty("start_at")
    private LocalDate startAt;

    @JsonProperty("end_at")
    private LocalDate endAt;

    private Currency base;

    private Map<String, Map<Currency, BigDecimal>> rates = new HashMap<>();

    public HistoricalForeignExchangeReferenceRates() {
    }

    @Format("yyyy-MM-dd")
    public LocalDate getStartAt() {
        return startAt;
    }

    public void setStartAt(@Format("yyyy-MM-dd") LocalDate startAt) {
        this.startAt = startAt;
    }

    @Format("yyyy-MM-dd")
    public LocalDate getEndAt() {
        return endAt;
    }

    public void setEndAt(@Format("yyyy-MM-dd") LocalDate endAt) {
        this.endAt = endAt;
    }

    public Currency getBase() {
        return base;
    }

    public void setBase(Currency base) {
        this.base = base;
    }

    public Map<String, Map<Currency, BigDecimal>> getRates() {
        return rates;
    }

    public void setRates(Map<String, Map<Currency, BigDecimal>> rates) {
        this.rates = rates;
    }
}
