package com.salesforceiq.augmenteddriver.testcases;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.salesforceiq.augmenteddriver.asserts.AugmentedAssertInterface;
import com.salesforceiq.augmenteddriver.guice.GuiceTestRunner;
import com.salesforceiq.augmenteddriver.integrations.IntegrationFactory;
import com.salesforceiq.augmenteddriver.modules.PropertiesModule;
import com.salesforceiq.augmenteddriver.runners.TestRunnerRetryingRule;
import com.salesforceiq.augmenteddriver.util.Util;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.function.Supplier;

/**
 * Base Test Case for all tests.
 *
 * <p>
 *     No one should extend from this, AugmentedWebTestCase or the like should be used.
 * </p>
 */
@RunWith(GuiceTestRunner.class)
public abstract class AugmentedBaseTestCase implements AugmentedAssertInterface {

    @Rule
    public TestName testName = new TestName();

    @Rule
    @Inject
    public TestRunnerRetryingRule testRunnerRetryingRule;

    @Inject
    @Named(PropertiesModule.WAIT_IN_SECONDS)
    private String waitTimeInSeconds;

    @Inject
    @Named(PropertiesModule.UNIQUE_ID)
    private String uniqueId;

    /**
     * @return the wait time in seconds defined in the com.salesforceiq.augmenteddriver.properties (or 30 by default)
     */
    protected int waitTimeInSeconds() {
        return Integer.valueOf(Preconditions.checkNotNull(waitTimeInSeconds));
    }

    /**
     * @return Unique 10 digit Id for the run (tests will share it in the suite, or if a same test is running
     *         repeated times.
     */
    protected String getUniqueId() {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(uniqueId));

        return uniqueId;
    }

    /**
     * @return Unique name of the test, including the unique id, the class and the test name
     */
    protected String getFullTestName() {
        return String.format("%s:%s:%s", getUniqueId(), Util.shortenClass(this.getClass()), testName.getMethodName());
    }

    @Inject
    private IntegrationFactory integrations;

    /**
     * Hack, but there is no way to get the session Id in other way.
     */
    private String sessionId;

    protected void setSessionId(String sessionId) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(sessionId));
        this.sessionId = sessionId;
    }

    /**
     * @return returns the Selenium Session Id if the test has been initialized, other wise returns an empty optional.
     */
    public Future<Optional<String>> sessionId() {
        return CompletableFuture.supplyAsync(new Supplier<Optional<String>>() {
            @Override
            public Optional<String> get() {
                return (Strings.isNullOrEmpty(sessionId)) ? Optional.empty() : Optional.of(sessionId);
            }
        });
    }

    /**
     * Rule for executing code after the test finished, whether it failed or not.
     *
     * <p>
     *     IMPORTANT. It's implemented here since many of the data is generated at runtime.
     * </p>
     */
    @Rule
    public TestWatcher testWatcher = new TestWatcher() {
        @Override
        protected void failed(Throwable e, Description description) {
            if (integrations.sauceLabs().isEnabled() && !Strings.isNullOrEmpty(sessionId)) {
                integrations.sauceLabs().testPassed(false, sessionId);
            }
            if (integrations.slack().isEnabled() && !Strings.isNullOrEmpty(sessionId)) {
                integrations.slack().failed(description, e, sessionId);
            }
        }

        @Override
        protected void succeeded(Description description) {
            if (integrations.sauceLabs().isEnabled() && !Strings.isNullOrEmpty(sessionId)) {
                integrations.sauceLabs().testPassed(true, sessionId);
            }
            if (integrations.slack().isEnabled() && !Strings.isNullOrEmpty(sessionId)) {
                integrations.slack().passed(description, sessionId);
            }
        }
    };
}
