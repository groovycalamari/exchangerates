package example


import groovy.transform.CompileStatic
import io.exchangeratesapi.ExchangeratesApi
import org.springframework.beans.factory.annotation.Autowired

@CompileStatic
class ExchangeRatesController {

    @Autowired
    ExchangeratesApi exchangerateApi

    def index() {
        [foreignExchangeReferenceRates: exchangerateApi.latest().blockingGet()]
    }
}