package example

import grails.testing.mixin.integration.Integration
import io.exchangeratesapi.ForeignExchangeReferenceRates
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.BlockingHttpClient
import io.micronaut.http.client.HttpClient
import spock.lang.Specification
import io.exchangeratesapi.Currency
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

@Integration
class ExchangeRatesControllerSpec  extends Specification {
    void "Get the latest foreign exchange reference rates with no base and symbols"() {
        given:
        BlockingHttpClient client = HttpClient.create(new URL("http://localhost:$serverPort".toString())).toBlocking()

        when:
        ForeignExchangeReferenceRates exchangeReferenceRates = client.retrieve(HttpRequest.GET("/"), ForeignExchangeReferenceRates)

        then:
        exchangeReferenceRates.base == Currency.EUR
        exchangeReferenceRates.rates.containsKey(Currency.USD)
        exchangeReferenceRates.rates.get(Currency.USD) > 0.0
        exchangeReferenceRates.date == latestWorkingday()
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
        ld
    }
}
