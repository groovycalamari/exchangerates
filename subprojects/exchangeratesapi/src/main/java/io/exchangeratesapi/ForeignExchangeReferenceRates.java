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

import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.convert.format.Format;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Introspected
public class ForeignExchangeReferenceRates {
    private Currency base;

    private LocalDate date;

    private Map<Currency, BigDecimal> rates = new HashMap<>();

    public ForeignExchangeReferenceRates() {
    }

    public Currency getBase() {
        return base;
    }

    public void setBase(Currency base) {
        this.base = base;
    }

    @Format("yyyy-MM-dd")
    public LocalDate getDate() {
        return date;
    }

    public void setDate(@Format("yyyy-MM-dd") LocalDate date) {
        this.date = date;
    }

    public Map<Currency, BigDecimal> getRates() {
        return rates;
    }

    public void setRates(Map<Currency, BigDecimal> rates) {
        this.rates = rates;
    }
}
