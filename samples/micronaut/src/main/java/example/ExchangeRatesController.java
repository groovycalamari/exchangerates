package example;

import io.exchangeratesapi.ExchangeratesApi;
import io.exchangeratesapi.ForeignExchangeReferenceRates;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.reactivex.Single;

import javax.inject.Inject;

@Controller
public class ExchangeRatesController {

    @Inject
    private ExchangeratesApi exchangeratesApi;

    @Get
    public Single<ForeignExchangeReferenceRates> index() {
        return exchangeratesApi.latest();
    }
}
