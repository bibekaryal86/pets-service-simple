package pets.service.app.util;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Util {
    // provided at runtime
    public static final String SERVER_PORT = "PORT";
    public static final String TIME_ZONE = "TZ";
    public static final String PROFILE = "SPRING_PROFILES_ACTIVE";
    public static final String BASIC_AUTH_USR = "BASIC_AUTH_USR";
    public static final String BASIC_AUTH_PWD = "BASIC_AUTH_PWD";
    public static final String BASIC_AUTH_USR_PETSDATABASE = "BASIC_AUTH_USR_PETSDATABASE";
    public static final String BASIC_AUTH_PWD_PETSDATABASE = "BASIC_AUTH_PWD_PETSDATABASE";

    // server context-path
    public static final String CONTEXT_PATH = "/pets-service";     // NOSONAR

    // others
    public static final int SERVER_MAX_THREADS = 100;
    public static final int SERVER_MIN_THREADS = 20;
    public static final int SERVER_IDLE_TIMEOUT = 120;

    public static final String ACCOUNT_TYPE_ID_CASH = "5ede4cbb0525eb78290332e4";
    public static final String ACCOUNT_TYPE_ID_CHECKING = "5ede4cc80525eb78290332e5";
    public static final String ACCOUNT_TYPE_ID_CREDIT_CARD = "5ede4cf30525eb78290332e7";
    public static final String ACCOUNT_TYPE_ID_LOANS_MORTGAGES = "5ede4d080525eb78290332e8";
    public static final String ACCOUNT_TYPE_ID_OTHER_DEPOSITS = "5ede4d170525eb78290332e9";
    public static final String ACCOUNT_TYPE_ID_OTHER_LOANS = "5ede4d1d0525eb78290332ea";
    public static final String ACCOUNT_TYPE_ID_SAVINGS = "5ede4cde0525eb78290332e6";
    public static final String ACCOUNT_TYPE_ID_INVESTMENT = "5fa83f9d465347404cc6aa21";
    public static final String TRANSACTION_TYPE_ID_EXPENSE = "5ede664746fa58038df1b422";
    public static final String TRANSACTION_TYPE_ID_INCOME = "5ede663e46fa58038df1b421";
    public static final String TRANSACTION_TYPE_ID_TRANSFER = "5ede664e46fa58038df1b423";
    public static final String CATEGORY_ID_REFUNDS = "5ede618546fa58038df1b3e8";
    protected static final List<String> ACCOUNT_TYPES_DEPOSIT_ACCOUNTS = List.of(ACCOUNT_TYPE_ID_CASH,
            ACCOUNT_TYPE_ID_CHECKING, ACCOUNT_TYPE_ID_OTHER_DEPOSITS, ACCOUNT_TYPE_ID_SAVINGS, ACCOUNT_TYPE_ID_INVESTMENT);
    protected static final List<String> ACCOUNT_TYPES_LOAN_ACCOUNTS = List.of(ACCOUNT_TYPE_ID_CREDIT_CARD,
            ACCOUNT_TYPE_ID_LOANS_MORTGAGES, ACCOUNT_TYPE_ID_OTHER_LOANS);
    private static final String MERCHANT_ID_TRANSFER = "5f9f861c083c2023ef009a9a";
    private static final String MERCHANT_ID_FAMILY = "5fa0f0e97ed1a3304dee7caa";
    private static final String MERCHANT_ID_CASH_RECON = "5fa419dfb5e94e065bd9dad8";
    protected static final List<String> SYSTEM_DEPENDENT_MERCHANTS = List.of(MERCHANT_ID_TRANSFER,
            MERCHANT_ID_FAMILY, MERCHANT_ID_CASH_RECON);
    private static final String CATEGORY_ID_AUTO_PAYMENT = "5fa8d3a4465347404cc6aa22";
    private static final String CATEGORY_ID_MORTGAGE_PAYMENT = "5fa8d3de465347404cc6aa23";
    private static final String CATEGORY_ID_OTHER_LOAN_PAYMENT = "5fa8d3ec465347404cc6aa24";

    public static final List<String> CATEGORY_ID_LOAN_PAYMENTS = List.of(CATEGORY_ID_AUTO_PAYMENT,
            CATEGORY_ID_MORTGAGE_PAYMENT, CATEGORY_ID_OTHER_LOAN_PAYMENT);

    public static String getSystemEnvProperty(String keyName) {
        return (System.getProperty(keyName) != null) ? System.getProperty(keyName) : System.getenv(keyName);
    }

    public static LocalDateTime getLocalDateTimeNow() {
        return LocalDateTime.now(ZoneId.of(getSystemEnvProperty(TIME_ZONE)));
    }

    public static boolean hasText(String string) {
        return (string != null && !string.trim().isEmpty());
    }

    public static Gson getGson() {
        return new GsonBuilder()
                .setExclusionStrategies(new ExclusionStrategy() {
                    public boolean shouldSkipField(FieldAttributes f) {
                        return (f == null);
                    }

                    public boolean shouldSkipClass(Class<?> clazz) {
                        return false;
                    }
                }).create();
    }

    public static boolean isAuthenticatedRequest(HttpServletRequest request) {
        String username = getSystemEnvProperty(BASIC_AUTH_USR);
        String password = getSystemEnvProperty(BASIC_AUTH_PWD);
        String authorization = Base64.getEncoder().encodeToString(String.format("%s:%s", username, password).getBytes());
        String headerAuth = request.getHeader("Authorization");
        return hasText(headerAuth) && headerAuth.equals(String.format("Basic %s", authorization));
    }

    public static Map<String, String> getPetsDatabaseAuthHeaders() {
        String username = getSystemEnvProperty(BASIC_AUTH_USR_PETSDATABASE);
        String password = getSystemEnvProperty(BASIC_AUTH_PWD_PETSDATABASE);
        String authorization = Base64.getEncoder().encodeToString(String.format("%s:%s", username, password).getBytes());
        Map<String, String> headersMap = new HashMap<>();   // do not use Map.of, may have to add to headers
        headersMap.put("Authorization", String.format("Basic %s", authorization));
        return headersMap;
    }

    public static String getRequestPathParameter(HttpServletRequest request, int length, int position) {
        String[] requestUriArray = request.getRequestURI().split("/");
        if (requestUriArray.length == length && hasText(requestUriArray[position])) {
            return requestUriArray[position];
        }
        return null;
    }

    public static Object getRequestBody(HttpServletRequest request, Class<?> clazz) {
        try {
            return getGson().fromJson(request.getReader(), clazz);
        } catch (Exception ex) {
            return null;
        }
    }


}
