package com.salesforceiq.augmenteddriver.web;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.name.Named;
import com.salesforceiq.augmenteddriver.modules.PropertiesModule;
import com.salesforceiq.augmenteddriver.util.AugmentedFunctions;
import com.salesforceiq.augmenteddriver.util.WebDriverUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.TimeoutException;

import java.util.List;
import java.util.stream.Collectors;

public class AugmentedWebFunctions implements AugmentedFunctions<AugmentedWebElement> {

    private final SearchContext searchContext;
    private final int waitTimeInSeconds;
    private final AugmentedWebElementFactory augmentedWebElementFactory;

    @Inject
    public AugmentedWebFunctions(@Assisted SearchContext searchContext,
                                 @Named(PropertiesModule.WAIT_IN_SECONDS) String waitTimeInSeconds,
                                 AugmentedWebElementFactory augmentedWebElementFactory) {
        this.searchContext = Preconditions.checkNotNull(searchContext);
        this.waitTimeInSeconds= Integer.valueOf(Preconditions.checkNotNull(waitTimeInSeconds));
        this.augmentedWebElementFactory = Preconditions.checkNotNull(augmentedWebElementFactory);
    }

    @Override
    public boolean isElementPresent(By by) {
        Preconditions.checkNotNull(by);
        return isElementPresentAfter(by, waitTimeInSeconds);
    }

    @Override
    public boolean isElementPresentAfter(By by, int waitSeconds) {
        Preconditions.checkNotNull(by);
        try {
            findElementPresentAfter(by, waitSeconds);
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    @Override
    public boolean isElementPresentImmediate(By by) {
        Preconditions.checkNotNull(by);
        return isElementPresentAfter(by, 0);
    }

    @Override
    public boolean isElementVisible(By by) {
        Preconditions.checkNotNull(by);
        return isElementVisibleAfter(by, waitTimeInSeconds);
    }

    @Override
    public boolean isElementVisibleAfter(By by, int waitSeconds) {
        Preconditions.checkNotNull(by);
        try {
            findElementVisibleAfter(by, waitSeconds);
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    @Override
    public boolean isElementVisibleImmediate(By by) {
        Preconditions.checkNotNull(by);
        return isElementVisibleAfter(by, 0);
    }

    @Override
    public boolean isElementClickable(By by) {
        Preconditions.checkNotNull(by);
        return isElementClickableAfter(by, waitTimeInSeconds);
    }

    @Override
    public boolean isElementClickableAfter(By by, int waitSeconds) {
        Preconditions.checkNotNull(by);
        try {
            findElementClickableAfter(by, waitSeconds);
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    @Override
    public boolean isElementClickableImmediate(By by) {
        Preconditions.checkNotNull(by);
        return isElementClickableAfter(by, 0);
    }

    @Override
    public AugmentedWebElement findElementPresent(By by) {
        Preconditions.checkNotNull(by);
        return findElementPresentAfter(by, waitTimeInSeconds);
    }

    @Override
    public AugmentedWebElement findElementPresentAfter(By by, int waitSeconds) {
        Preconditions.checkNotNull(by);
        return augmentedWebElementFactory.create(WebDriverUtil.findElementPresentAfter(searchContext, by, waitSeconds));
    }

    @Override
    public AugmentedWebElement findElementVisible(By by) {
        Preconditions.checkNotNull(by);
        return findElementVisibleAfter(by, waitTimeInSeconds);
    }

    @Override
    public AugmentedWebElement findElementVisibleAfter(By by, int waitSeconds) {
        Preconditions.checkNotNull(by);
        return augmentedWebElementFactory.create(WebDriverUtil.findElementVisibleAfter(searchContext, by, waitSeconds));
    }

    @Override
    public AugmentedWebElement findElementClickable(By by) {
        Preconditions.checkNotNull(by);
        return findElementClickableAfter(by, waitTimeInSeconds);
    }

    @Override
    public AugmentedWebElement findElementClickableAfter(By by, int waitSeconds) {
        Preconditions.checkNotNull(by);
        return augmentedWebElementFactory.create(WebDriverUtil.findElementClickableAfter(searchContext, by, waitSeconds));
    }

    @Override
    public AugmentedWebElement findElementNotMoving(By by) {
        Preconditions.checkNotNull(by);
        return findElementNotMovingAfter(by, waitTimeInSeconds);
    }

    @Override
    public AugmentedWebElement findElementNotMovingAfter(By by, int waitSeconds) {
        Preconditions.checkNotNull(by);
        return augmentedWebElementFactory.create(WebDriverUtil.findElementNotMovingAfter(searchContext, by, waitSeconds));
    }

    @Override
    public AugmentedWebElement findElementContain(By by, String text) {
        return findElementContainAfter(by, text, waitTimeInSeconds);
    }

    @Override
    public AugmentedWebElement findElementContainAfter(By by, String text, int waitInSeconds) {
        Preconditions.checkNotNull(by);
        Preconditions.checkArgument(!Strings.isNullOrEmpty(text));
        return augmentedWebElementFactory.create(WebDriverUtil.findElementContainAfter(searchContext, by, text, waitInSeconds));
    }

    @Override
    public List<AugmentedWebElement> findElementsVisible(By by) {
        Preconditions.checkNotNull(by);
        return findElementsVisibleAfter(by, waitTimeInSeconds);
    }

    @Override
    public List<AugmentedWebElement> findElementsVisibleAfter(By by, int waitInSeconds) {
        Preconditions.checkNotNull(by);
        return WebDriverUtil.findElementsVisibleAfter(searchContext, by, waitInSeconds)
                .stream()
                .map(webElement -> augmentedWebElementFactory.create(webElement))
                .collect(Collectors.toList());
    }

    @Override
    public List<AugmentedWebElement> findElementsPresent(By by) {
        Preconditions.checkNotNull(by);
        return findElementsPresentAfter(by, waitTimeInSeconds);
    }

    @Override
    public List<AugmentedWebElement> findElementsPresentAfter(By by, int waitInSeconds) {
        Preconditions.checkNotNull(by);
        return WebDriverUtil.findElementsPresentAfter(searchContext, by, waitInSeconds)
                .stream()
                .map(webElement -> augmentedWebElementFactory.create(webElement))
                .collect(Collectors.toList());
    }

    @Override
    public List<AugmentedWebElement> findElementsClickable(By by) {
        Preconditions.checkNotNull(by);
        return findElementsClickableAfter(by, waitTimeInSeconds);
    }

    @Override
    public List<AugmentedWebElement> findElementsClickableAfter(By by, int waitInSeconds) {
        Preconditions.checkNotNull(by);
        return WebDriverUtil.findElementsClickableAfter(searchContext, by, waitInSeconds)
                .stream()
                .map(webElement -> augmentedWebElementFactory.create(webElement))
                .collect(Collectors.toList());
    }

    @Override
    public void waitElementToNotBePresent(By by) {
        waitElementToNotBePresentAfter(by, waitTimeInSeconds);
    }

    @Override
    public void waitElementToNotBePresentAfter(By by, int waitInSeconds) {
        Preconditions.checkNotNull(by);
        WebDriverUtil.waitElementToNotBePresent(searchContext, by, waitInSeconds);
    }

    @Override
    public void waitElementToNotBeVisible(By by) {
        waitElementToNotBeVisibleAfter(by, waitTimeInSeconds);
    }

    @Override
    public void waitElementToNotBeVisibleAfter(By by, int waitInSeconds) {
        Preconditions.checkNotNull(by);
        WebDriverUtil.waitElementToNotBeVisible(searchContext, by, waitInSeconds);
    }

    @Override
    public AugmentedWebElement clickAndPresent(By click, By wait) {
        return clickAndPresentAfter(click, wait, waitTimeInSeconds);
    }

    @Override
    public AugmentedWebElement clickAndPresentAfter(By click, By wait, int waitInSeconds) {
        Preconditions.checkNotNull(click);
        Preconditions.checkNotNull(wait);
        findElementClickableAfter(click, waitInSeconds).click();
        return findElementPresentAfter(wait, waitInSeconds);
    }
}
