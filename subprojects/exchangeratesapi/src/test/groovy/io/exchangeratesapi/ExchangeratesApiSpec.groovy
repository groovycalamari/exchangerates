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

package io.exchangeratesapi

import spock.lang.Shared

import javax.validation.ConstraintViolationException
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters

class ExchangeratesApiSpec extends ApplicationContextSpecification {

    @Shared
    ExchangeratesApi exchangerateApi = applicationContext.getBean(ExchangeratesApi)

    void "Get the latest foreign exchange reference rates with no base and symbols"() {
        when:
        ForeignExchangeReferenceRates exchangeReferenceRates = exchangerateApi.latest().blockingGet()

        then:
        exchangeReferenceRates.base == Currency.EUR
        exchangeReferenceRates.date == latestWorkingday()
        exchangeReferenceRates.rates.containsKey(Currency.USD)
        exchangeReferenceRates.rates.get(Currency.USD) > 0.0
    }

    void "Get the latest foreign exchange reference rates"() {
        when:
        ForeignExchangeReferenceRates exchangeReferenceRates = exchangerateApi.latest(null, null).blockingGet()

        then:
        exchangeReferenceRates.base == Currency.EUR
        exchangeReferenceRates.date == latestWorkingday()
        exchangeReferenceRates.rates.containsKey(Currency.USD)
        exchangeReferenceRates.rates.get(Currency.USD) > 0.0

        when:
        exchangeReferenceRates = exchangerateApi.latest(Currency.USD, [Currency.EUR]).blockingGet()

        then:
        exchangeReferenceRates.base == Currency.USD
        exchangeReferenceRates.date == latestWorkingday()
        exchangeReferenceRates.rates.keySet().size() == 1
        exchangeReferenceRates.rates.containsKey(Currency.EUR)
        exchangeReferenceRates.rates.get(Currency.EUR) > 0.0
    }

    void "To get historical rates, date is required"() {
        when:
        exchangerateApi.historicalRate(null, null, null)

        then:
        thrown(ConstraintViolationException)
    }

    void "To get history range, startAt and endAt is required"() {
        given:
        LocalDate now = LocalDate.now()

        when:
        exchangerateApi.history(now, null, null, null)

        then:
        thrown(ConstraintViolationException)

        when:
        exchangerateApi.history(null, now, null, null)

        then:
        thrown(ConstraintViolationException)
    }

    void "To get history range, startAt and endAt must be past or present dates"() {
        given:
        LocalDate now = LocalDate.now()

        LocalDate futureDate = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.FRIDAY))

        when:
        exchangerateApi.history(futureDate, now, null, null)

        then:
        thrown(ConstraintViolationException)

        when:
        exchangerateApi.history(now, futureDate, null, null)

        then:
        thrown(ConstraintViolationException)
    }

    void "Get historical rates allows only past or present dates"() {
        when:
        LocalDate futureDate = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.FRIDAY))
        exchangerateApi.historicalRate(futureDate, null, null)

        then:
        thrown(ConstraintViolationException)
    }

    void "Get historical rates for any day since 1999"() {
        when:
        LocalDate ld = latestWorkingday()
        ForeignExchangeReferenceRates exchangeReferenceRates = exchangerateApi.historicalRate(ld, null, null).blockingGet()

        then:
        exchangeReferenceRates.base == Currency.EUR
        exchangeReferenceRates.date == latestWorkingday()
        exchangeReferenceRates.rates.containsKey(Currency.USD)
        exchangeReferenceRates.rates.get(Currency.USD) > 0.0

        when:
        exchangeReferenceRates = exchangerateApi.historicalRate(ld, Currency.USD, [Currency.EUR]).blockingGet()

        then:
        exchangeReferenceRates.base == Currency.USD
        exchangeReferenceRates.date == latestWorkingday()
        exchangeReferenceRates.rates.keySet().size() == 1
        exchangeReferenceRates.rates.containsKey(Currency.EUR)
        exchangeReferenceRates.rates.get(Currency.EUR) > 0.0
    }

    void "Get history between start and end date"() {
        when:
        LocalDate ld = latestWorkingday()
        HistoricalForeignExchangeReferenceRates exchangeReferenceRates = exchangerateApi.history(ld, ld, Currency.USD, [Currency.EUR]).blockingGet()

        then:
        exchangeReferenceRates.base == Currency.USD
        exchangeReferenceRates.startAt == ld
        exchangeReferenceRates.endAt == ld
        exchangeReferenceRates.rates.keySet().size() == 1
        exchangeReferenceRates.rates.keySet().first() == ld.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        exchangeReferenceRates.rates.get(exchangeReferenceRates.rates.keySet().first()).containsKey(Currency.EUR)
        exchangeReferenceRates.rates.get(exchangeReferenceRates.rates.keySet().first()).get(Currency.EUR) > 0.0
    }

    private LocalDate latestWorkingday() {
        LocalDate ld = LocalDate.now()
        if (ld.dayOfWeek == DayOfWeek.SATURDAY || ld.dayOfWeek == DayOfWeek.SUNDAY || ld.dayOfWeek == DayOfWeek.MONDAY) {
            return ld.with(TemporalAdjusters.previous(DayOfWeek.FRIDAY))
        }
        if (ld.dayOfWeek == DayOfWeek.TUESDAY) {
            return ld.with(TemporalAdjusters.previous(DayOfWeek.MONDAY))
        }
        if (ld.dayOfWeek == DayOfWeek.WEDNESDAY) {
            return ld.with(TemporalAdjusters.previous(DayOfWeek.TUESDAY))
        }
        if (ld.dayOfWeek == DayOfWeek.THURSDAY) {
            return ld.with(TemporalAdjusters.previous(DayOfWeek.WEDNESDAY))
        }
        if (ld.dayOfWeek == DayOfWeek.FRIDAY) {
            return ld.with(TemporalAdjusters.previous(DayOfWeek.THURSDAY))
        }
        return ld;
    }
}
