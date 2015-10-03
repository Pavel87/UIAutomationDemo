package com.pacmac.uiautomationdemo;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import com.robotium.solo.By;
import com.robotium.solo.Solo;


/**
 * Created by pacmac on 8/5/2015.
 */


public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private Solo solo;


    public MainActivityTest() {
        super(MainActivity.class);
    }


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        solo = new Solo(getInstrumentation(), getActivity());

    }

    public void testAritmeticOperationsEnterText_Test() throws Exception {
        solo.waitForActivity("MainActivity", 2000);
        Activity activity = solo.getCurrentActivity();

        solo.enterText(0, "49");
        solo.clickOnButton("SUBMIT");

        //check the results
        TextView txt = (TextView) solo.getView(activity.getResources().getIdentifier("outputDigitResult1", "id", activity.getPackageName()));
        String actual = txt.getText().toString();
        assertEquals("Sum: 13", actual);

        txt = (TextView) solo.getView(activity.getResources().getIdentifier("outputDigitResult2", "id", activity.getPackageName()));
        actual = txt.getText().toString();
        assertEquals("Subtract: -5", actual);

        txt = (TextView) solo.getView(activity.getResources().getIdentifier("outputDigitResult3", "id", activity.getPackageName()));
        actual = txt.getText().toString();
        assertEquals("Multiplication: 36", actual);


    }

   public void testAritmeticOperationsType_Test() throws Exception {
        solo.waitForActivity("MainActivity", 2000);
        Activity activity = solo.getCurrentActivity();
        EditText editText1 = (EditText) solo.getView(activity.getResources().getIdentifier("inputDigits", "id", activity.getPackageName()));

        for(int i = 0; i< 5; i++) {
           // solo.clickOnEditText(0);
            solo.typeText(editText1, "14");
            solo.clickOnView(solo.getView(activity.getResources().getIdentifier("inputDigitButton", "id", activity.getPackageName())));

            //check the results
            TextView txt = (TextView) solo.getView(activity.getResources().getIdentifier("outputDigitResult1", "id", activity.getPackageName()));
            String actual = txt.getText().toString();
            assertEquals("Sum: 5", actual);

            txt = (TextView) solo.getView(activity.getResources().getIdentifier("outputDigitResult2", "id", activity.getPackageName()));
            actual = txt.getText().toString();
            assertEquals("Subtract: -3", actual);

            txt = (TextView) solo.getView(activity.getResources().getIdentifier("outputDigitResult3", "id", activity.getPackageName()));
            actual = txt.getText().toString();
            assertEquals("Multiplication: 4", actual);


            //clear the results
            solo.clickOnView(getActivity().findViewById(R.id.action_clear));
            //check that field has been reseted
            Thread.sleep(1000);
            actual = txt.getText().toString();
            assertEquals("Multiplication: ", actual);
        }
    }

    public void testEnteringUrl_Test() throws Exception {

        solo.waitForActivity("MainActivity", 2000);
        Activity activity = solo.getCurrentActivity();
        ImageButton imageButton = (ImageButton) solo.getView(activity.getResources().getIdentifier("editUrl", "id", activity.getPackageName()));
        solo.clickOnView(imageButton);

        solo.typeText(0, "www.google.com");
        solo.clickOnButton("UPDATE");
        solo.clickOnText("www.google.com");
        boolean actual = solo.searchText("URL put in clip");
        assertEquals("Toast message doesn't work", true, actual);

        solo.clickOnButton("DEMO 2");
        solo.waitForActivity("DemoActivity", 1000);

        // accessing webview
        final By inputSearch = By.name("q");
        final By buttonSearch = By.id("tsbb");

        solo.waitForWebElement(inputSearch);
        solo.typeTextInWebElement(inputSearch, "czech republic");
        //checking if "bmw" has been typed and then proceed with search
        assertTrue("czech republic has not been typed has not been typed", solo.getWebElement(inputSearch, 0).getText().contains("czech republic"));
        solo.scrollUp();
        solo.clickOnWebElement(buttonSearch);
        actual = solo.waitForText("www.czech.cz");
        assertEquals("Couldn't find results", true, actual);

        Thread.sleep(2000);
        //go back to Main Activity
        solo.goBack();

        //check if DemoActivity sent a correct result code.
        actual = solo.waitForText("Activity Returned: 5");
        assertTrue("Activity didn't return a proper result code!", actual);

        //restore default url by pressing "clear" btn
        solo.clickOnView(getActivity().findViewById(R.id.action_clear));
        Thread.sleep(1000);
        //check that field has been reseted
        actual = solo.searchText("www.zebra.com");
        assertTrue("URL was not restored",actual);


    }

    public void testButtonsGeneral_Gen(){

        Activity activity = solo.getCurrentActivity();

        for(int i=0; i<3; i++) {
            solo.clickOnView(activity.findViewById(R.id.action_about));
            boolean actual = solo.waitForText(activity.getResources().getString(R.string.about_title));
            assertTrue("No or wrong dialog", actual);
            solo.clickOnButton(0);
        }

        Button btn = (Button) solo.getView(activity.getResources().getIdentifier("watch","id", activity.getPackageName()));
        solo.clickOnView(btn);
        Switch switcher = (Switch) solo.getView(activity.getResources().getIdentifier("switch1", "id", activity.getPackageName()));
        solo.clickOnView(switcher);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        solo.clickOnView(btn);
        boolean actual = solo.waitForText("00:05");

        assertTrue("Timer issue", actual);

        solo.clickOnView(activity.findViewById(R.id.action_clear));
        actual = solo.searchText("00:00");
        assertTrue("Timer wasn't reseted", actual);
    }




    public void testGeneral() {
        Activity activity = solo.getCurrentActivity();

        solo.enterText(0, "27");
        solo.clickOnButton(0);
        solo.clearEditText(0);
        solo.enterText(0, "82");
        solo.clickOnButton(0);
        Button btn = (Button) solo.getView(activity.getResources().getIdentifier("watch","id", activity.getPackageName()));
        solo.clickOnView(btn);
        Switch switcher = (Switch) solo.getView(activity.getResources().getIdentifier("switch1", "id", activity.getPackageName()));
        solo.clickOnView(switcher);

        ImageButton imageButton = (ImageButton) solo.getView(activity.getResources().getIdentifier("editUrl", "id", activity.getPackageName()));
        solo.clickOnView(imageButton);

        solo.typeText(0, "www.cnn.com");
        solo.clickOnButton("UPDATE");
        solo.clickOnText("www.cnn.com");
        boolean actual = solo.searchText("URL put in clip");
        assertEquals("Toast message doesn't work", true, actual);

        solo.clearEditText(0);
        solo.enterText(0, "19");
        solo.clickOnButton(0);
        solo.clickOnView(btn);

        solo.clickOnView(activity.findViewById(R.id.action_about));
        actual = solo.waitForText(activity.getResources().getString(R.string.about_title));
        assertTrue("No or wrong dialog", actual);
        solo.clickOnButton(0);

        //restore default url by pressing "clear" btn
        solo.clickOnView(getActivity().findViewById(R.id.action_clear));
        try {
            Thread.sleep(1000);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        //check that field has been reseted
        actual = solo.searchText("www.zebra.com");
        assertTrue("Activity was not restored to default",actual);

    }

    @Override
    protected void tearDown() throws Exception {

        solo.finishOpenedActivities();
        super.tearDown();
    }

}
