package com.example.mehranm4;

import android.content.Context;
import android.content.Intent;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.BySelector;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiScrollable;
import androidx.test.uiautomator.UiSelector;
import androidx.test.uiautomator.Until;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

@RunWith(AndroidJUnit4.class)
public class UiTest {
    private static final String PACKAGE = "com.example.mehranm4";
    private static final int TIMEOUT = 500;
    private static final String STRING_TO_BE_TYPED = "UiAutomator";
    private UiDevice device;

    @Before
    public void startMainActivityFromHomeScreen() {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        Context context = ApplicationProvider.getApplicationContext();
        final Intent intent = context.getPackageManager().getLaunchIntentForPackage(PACKAGE);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);

        device.wait(Until.hasObject(By.pkg(PACKAGE).depth(0)), 5000);
    }

    @Test
    public void test() throws UiObjectNotFoundException {
        UiObject uiObject = device.findObject(new UiSelector().text("Allow"));
        if (uiObject.exists())
            uiObject.clickAndWaitForNewWindow(TIMEOUT);

        long balance = Long.parseLong(device.findObject(new UiSelector().textContains("Balance:")).getText().replace("Balance: ", "").replace(",", "").trim());
        long costs = Long.parseLong(device.findObject(new UiSelector().resourceId(PACKAGE + ":id/costs")).getText().trim().replace(",", ""));
        long incomes = Long.parseLong(device.findObject(new UiSelector().resourceId(PACKAGE + ":id/incomes")).getText().trim().replace(",", ""));
        long addIncome = 9000000;
        long addCost = 100000;
        device.findObject(new UiSelector().resourceId(PACKAGE + ":id/add")).clickAndWaitForNewWindow(TIMEOUT);
        device.findObject(new UiSelector().text("Incomes")).click();
        device.findObject(new UiSelector().text("Enter cost")).setText(String.valueOf(addIncome));
        device.findObject(new UiSelector().text("Enter description")).setText("Test");
        device.findObject(new UiSelector().text("Select category")).clickAndWaitForNewWindow(TIMEOUT);
        UiObject selectFirstCategory = device.findObject(new UiSelector().textContains("Salary"));
        selectFirstCategory.clickAndWaitForNewWindow(TIMEOUT);
        device.findObject(By.text("ADD")).click();

        device.findObject(new UiSelector().resourceId(PACKAGE + ":id/add")).clickAndWaitForNewWindow(TIMEOUT);
        device.findObject(new UiSelector().text("Costs")).click();
        device.findObject(new UiSelector().text("Select category")).clickAndWaitForNewWindow(TIMEOUT);

        UiScrollable categoryList = new UiScrollable(new UiSelector().resourceId(PACKAGE + ":id/rec"));

        categoryList.scrollToEnd(100);

        device.findObject(By.text("Add Category")).clickAndWait(Until.newWindow(), TIMEOUT);
        String categoryName = "Test Category";
        device.findObject(By.text("Enter category name")).setText(categoryName);
        device.findObject(By.text("CREATE")).clickAndWait(Until.newWindow(), TIMEOUT);
        device.findObject(new UiSelector().text(categoryName)).clickAndWaitForNewWindow(TIMEOUT);
        device.findObject(By.text("CLOSE")).clickAndWait(Until.newWindow(), TIMEOUT);


        device.findObject(By.text("Budgets")).clickAndWait(Until.newWindow(), TIMEOUT);
        device.findObject(new UiSelector().resourceId(PACKAGE + ":id/add")).clickAndWaitForNewWindow(TIMEOUT);
        device.findObject(new UiSelector().text("Enter budget")).setText("700000");
        device.findObject(new UiSelector().text("Select category")).clickAndWaitForNewWindow(TIMEOUT);
        device.findObject(new UiSelector().text(categoryName)).clickAndWaitForNewWindow(TIMEOUT);
        device.findObject(By.text("ADD")).clickAndWait(Until.newWindow(), TIMEOUT);


        device.findObject(By.text("Home")).clickAndWait(Until.newWindow(), TIMEOUT);
        device.findObject(new UiSelector().resourceId(PACKAGE + ":id/add")).clickAndWaitForNewWindow(TIMEOUT);
        device.findObject(new UiSelector().text("Costs")).click();
        device.findObject(new UiSelector().text("Enter cost")).setText(String.valueOf(addCost));
        device.findObject(new UiSelector().text("Enter description")).setText("Test 2");
        device.findObject(new UiSelector().text("Select category")).clickAndWaitForNewWindow(TIMEOUT);
        device.findObject(new UiSelector().text(categoryName)).clickAndWaitForNewWindow(TIMEOUT);
        device.findObject(By.text("ADD")).clickAndWait(Until.newWindow(), TIMEOUT);

        long newBalance = Long.parseLong(device.findObject(new UiSelector().textContains("Balance:")).getText().replace("Balance: ", "").replace(",", "").trim());
        long newCosts = Long.parseLong(device.findObject(new UiSelector().resourceId(PACKAGE + ":id/costs")).getText().trim().replace(",", ""));
        long newIncomes = Long.parseLong(device.findObject(new UiSelector().resourceId(PACKAGE + ":id/incomes")).getText().trim().replace(",", ""));


        Assert.assertEquals(newBalance, balance + addIncome - addCost);
        Assert.assertEquals(newCosts, costs + addCost);
        Assert.assertEquals(newIncomes, incomes + addIncome);


    }

    @After
    public void clear() throws IOException {
        InstrumentationRegistry.getInstrumentation().getUiAutomation().executeShellCommand("pm clear " + PACKAGE).close();
    }

}
