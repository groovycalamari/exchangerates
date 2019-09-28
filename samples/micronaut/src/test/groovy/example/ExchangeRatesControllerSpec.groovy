package example

import io.exchangeratesapi.Currency
import io.exchangeratesapi.ForeignExchangeReferenceRates
import io.micronaut.context.ApplicationContext
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.BlockingHttpClient
import io.micronaut.http.client.HttpClient
import io.micronaut.runtime.server.EmbeddedServer
import spock.lang.AutoCleanup
import spock.lang.PendingFeature
import spock.lang.Shared
import spock.lang.Specification

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

class ExchangeRatesControllerSpec extends Specification {

    @Shared
    @AutoCleanup
    EmbeddedServer embeddedServer = ApplicationContext.run(EmbeddedServer)

    @Shared
    HttpClient httpClient = embeddedServer.applicationContext.createBean(HttpClient, embeddedServer.URL)

    @Shared
    BlockingHttpClient client = httpClient.toBlocking()

    void "Get the latest foreign exchange reference rates with no base and symbols"() {
        when:
        ForeignExchangeReferenceRates exchangeReferenceRates = client.retrieve(HttpRequest.GET("/"), ForeignExchangeReferenceRates)

        then:
        exchangeReferenceRates.base == Currency.EUR
        exchangeReferenceRates.date == latestWorkingday()
        exchangeReferenceRates.rates.containsKey(Currency.USD)
        exchangeReferenceRates.rates.get(Currency.USD) > 0.0
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
        return ld
    }
}