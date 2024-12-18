package hu.nje.mozifxml.service.oanda;

import com.oanda.v20.Context;
import com.oanda.v20.ContextBuilder;
import com.oanda.v20.ExecuteException;
import com.oanda.v20.RequestException;
import com.oanda.v20.account.AccountID;
import com.oanda.v20.account.AccountSummary;
import com.oanda.v20.instrument.CandlestickGranularity;
import com.oanda.v20.instrument.InstrumentCandlesRequest;
import com.oanda.v20.instrument.InstrumentCandlesResponse;
import com.oanda.v20.order.MarketOrderRequest;
import com.oanda.v20.order.OrderCreateRequest;
import com.oanda.v20.order.OrderCreateResponse;
import com.oanda.v20.pricing.ClientPrice;
import com.oanda.v20.pricing.PricingGetResponse;
import com.oanda.v20.primitives.DateTime;
import com.oanda.v20.primitives.InstrumentName;
import com.oanda.v20.trade.Trade;
import com.oanda.v20.trade.TradeCloseRequest;
import com.oanda.v20.trade.TradeSpecifier;
import hu.nje.mozifxml.util.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import static hu.nje.mozifxml.util.Constant.EMPTY;
import static hu.nje.mozifxml.util.Helper.format;


public class OandaService {

    public static final String DIRECTION_VETEL = "Vétel";
    public static final String DIRECTION_ELADAS = "Eladás";

    private static final Logger logger = LoggerFactory.getLogger(OandaService.class);
    private static final String OANDA_URL = "oanda.url";
    private static final String OANDA_TOKEN = "oanda.token";
    private static final String OANDA_ACCOUNT_ID = "oanda.account_id";
    public final List<String> defaultCurrencyCodes = List.of("EUR", "USD", "JPY", "GBP", "CHF", "HUF");

    private final Context context;
    private final AccountID accountID;

    public OandaService() {
        final Properties properties = Helper.loadConfigProperties();

        this.context = new ContextBuilder(properties.getProperty(OANDA_URL))
                .setToken(properties.getProperty(OANDA_TOKEN))
                .setApplication(EMPTY)
                .build();

        this.accountID = new AccountID(properties.getProperty(OANDA_ACCOUNT_ID));
    }

    public List<Item> accountInformation() {
        try {
            final List<Item> items = new ArrayList<>();
            final AccountSummary summary = this.context.account.summary(accountID).getAccount();

            Field[] declaredFields = summary.getClass().getDeclaredFields();
            for (Field declaredField : declaredFields) {
                declaredField.setAccessible(true);
                items.add(new Item(declaredField.getName(), declaredField.get(summary)));
            }

            System.out.println("summary = " + summary);
            return items;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public List<ClientPrice> actualPrices(String... instruments) {
        return this.actualPrices(Arrays.asList(instruments));
    }

    public List<ClientPrice> actualPrices(final Collection<String> instruments) {
        try {
            PricingGetResponse pricingGetResponse = this.context.pricing.get(accountID, instruments);
            return pricingGetResponse.getPrices();
        } catch (RequestException | ExecuteException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void historicalPrices(final String instrument,
                                 final DateTime startDate,
                                 final DateTime endDate) {
        try {
            final var request = new InstrumentCandlesRequest(new InstrumentName(instrument));
            request.setGranularity(CandlestickGranularity.H1);
            request.setFrom(startDate);
            request.setTo(endDate);

            InstrumentCandlesResponse resp = this.context.instrument.candles(request);

            resp.getCandles()
                    .forEach(candle -> System.out.println(
                                    format.apply(candle.getTime()) + "\t" + candle.getMid().getC()
                                            + "\t" + candle.getVolume()
                            )
                    );

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }


    public boolean open(final String instrument, final double units, final Direction direction) {
        System.out.println("Place a Market Order");
        try {
            final var marketorderrequest = new MarketOrderRequest();
            marketorderrequest.setInstrument(new InstrumentName(instrument));
            marketorderrequest.setUnits(direction.units(units));

            final var request = new OrderCreateRequest(this.accountID);
            request.setOrder(marketorderrequest);
            final OrderCreateResponse response = this.context.order.create(request);

            logger.info("tradeId: {}", response.getOrderFillTransaction().getId());
            return true;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return false;
    }

    public boolean close(final String tradeId) {
        return this.close(new TradeSpecifier(tradeId));
    }

    public boolean close(final TradeSpecifier tradeSpecifier) {
        try {
            this.context.trade.close(new TradeCloseRequest(this.accountID, tradeSpecifier));
            return true;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return false;
    }

    public List<Trade> listOpenPositions() {
        try {
            final List<Trade> trades = this.context.trade.listOpen(this.accountID).getTrades();
            /*
            //for testing
            trades.forEach(trade -> {
                System.out.println(trade.toString());
                System.out.println();
                System.out.println(
                        trade.getId() + "\t" + trade.getInstrument() + "\t" + trade.getOpenTime()
                                + "\t" + trade.getCurrentUnits() + "\t" + trade.getPrice() + "\t" + trade.getUnrealizedPL());
            });
            */
            return trades;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
