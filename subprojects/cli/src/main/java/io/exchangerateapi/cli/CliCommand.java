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
package io.exchangerateapi.cli;

import io.exchangeratesapi.Currency;
import io.exchangeratesapi.ExchangeratesApi;
import io.exchangeratesapi.HistoricalForeignExchangeReferenceRates;
import io.micronaut.configuration.picocli.PicocliRunner;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Command(name = "exchangeratesapi", description = "Obtains Foreign exchange rates from https://exchangeratesapi.io",
        mixinStandardHelpOptions = true)
public class CliCommand implements Runnable {
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Option(names = {"--start"}, required = true, description = "Start at. Default Value: ${DEFAULT-VALUE}.", defaultValue = "2018-12-31", converter = LocalDateConverter.class)
    private LocalDate startAt = LocalDate.of(LocalDate.now().getYear(), 1, 1);

    @Option(names = {"--end"}, required = true, description = "End At. Default Value: ${DEFAULT-VALUE}.", defaultValue = "2019-01-31", converter = LocalDateConverter.class)
    private LocalDate endAt = LocalDate.now();

    @Option(names = {"--base"}, required = true, description = "Base currency. Default Value: ${DEFAULT-VALUE}. Possible values: ${COMPLETION-CANDIDATES}", defaultValue = "USD", completionCandidates = CurrencyCandidates.class, converter = CurrencyConverter.class)
    private Currency base = Currency.USD;

    @Option(names = {"--symbols"}, required = true, description = "Currency symbols. Default Value: ${DEFAULT-VALUE}. Possible values: ${COMPLETION-CANDIDATES}", defaultValue = "EUR", completionCandidates = CurrencyCandidates.class)
    private List<Currency> symbols = Collections.singletonList(Currency.EUR);

    public static void main(String[] args) throws Exception {
        PicocliRunner.run(CliCommand.class, args);
    }

    @Inject
    private ExchangeratesApi exchangeratesApi;

    public void run() {
        HistoricalForeignExchangeReferenceRates rates = exchangeratesApi.history(startAt, endAt, base, symbols).blockingGet();
        List<ExchangeRateRow> result = new ArrayList<>();
        for (String date : rates.getRates().keySet()) {
            for(Currency currency : rates.getRates().get(date).keySet()) {
                result.add(new ExchangeRateRow(base,
                        currency,
                        LocalDate.parse(date, FORMATTER),
                        rates.getRates().get(date).get(currency)));
            }
        }
        result.sort(Comparator.comparing(ExchangeRateRow::getDate));

        for (ExchangeRateRow row : result) {
            System.out.println(String.join(",",row.toList()));
        }
    }

    static class ExchangeRateRow {
        private Currency base;
        private Currency symbol;
        private LocalDate date;
        private BigDecimal rate;

        public ExchangeRateRow(Currency base, Currency symbol, LocalDate date, BigDecimal rate) {
            this.base = base;
            this.symbol = symbol;
            this.date = date;
            this.rate = rate;
        }

        public Currency getBase() {
            return base;
        }

        public Currency getSymbol() {
            return symbol;
        }

        public LocalDate getDate() {
            return date;
        }

        public BigDecimal getRate() {
            return rate;
        }

        public List<String> toList() {
            List<String> l = new ArrayList<>();
            l.add(date.format(CliCommand.FORMATTER));
            l.add(symbol.toString());
            l.add(base.toString());
            l.add(rate.toString());
            return l;
        }
    }
}
