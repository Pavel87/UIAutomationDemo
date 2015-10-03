package com.pacmac.uiautomationdemo;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.BatteryManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends ActionBarActivity {

    private EditText inputDigits;
    private TextView sum;
    private TextView subtract;
    private TextView multiply;
    private TextView inputUrl;
    private TextView time;
    private TextView batteryStatus;
    private Button submitMathResult;
    private Button openURL;
    private Button watch;
    private Button demoActBtn;
    private ImageButton editUrl;
    private Switch mSwitch;

    private Thread t;
    private long l;
    private boolean isReceiving;
    private ClipboardManager clipBoard;
    private BroadcastReceiver broadcast;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss");
    private boolean isTimerStarted = false;
    private int REQUEST_CODE = 56;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputDigits = (EditText) findViewById(R.id.inputDigits);
        sum = (TextView) findViewById(R.id.outputDigitResult1);
        subtract = (TextView) findViewById(R.id.outputDigitResult2);
        multiply = (TextView) findViewById(R.id.outputDigitResult3);
        inputUrl = (TextView) findViewById(R.id.inputUrl);
        time = (TextView) findViewById(R.id.time);
        batteryStatus = (TextView) findViewById(R.id.batStat);
        submitMathResult = (Button) findViewById(R.id.inputDigitButton);
        openURL = (Button) findViewById(R.id.openURL);
        watch = (Button) findViewById(R.id.watch);
        demoActBtn = (Button) findViewById(R.id.demoTwo);
        editUrl = (ImageButton) findViewById(R.id.editUrl);
        mSwitch = (Switch) findViewById(R.id.switch1);

        l = 0;
        isReceiving = false;

        inputDigits.clearFocus();

        broadcast = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

               if( intent.getAction() == Intent.ACTION_BATTERY_CHANGED) {

                 int level= intent.getIntExtra(BatteryManager.EXTRA_LEVEL,0);
                   int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE,0);
                   batteryStatus.setText( (level+"%"));
               }
            }
        };

        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    batteryStatus.setVisibility(View.VISIBLE);
                    isReceiving = true;
                    registerReceiver(broadcast, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
                }
                else
                {
                    batteryStatus.setText("Unknown");
                    batteryStatus.setVisibility(View.INVISIBLE);
                    unregisterReceiver(broadcast);
                    isReceiving = false;
                }
            }
        });

        clipBoard = (ClipboardManager)
                getSystemService(Context.CLIPBOARD_SERVICE);

        watch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isTimerStarted) {
                    isTimerStarted = true;
                    time.setText("00:00");
                    startTimerThread();
                    watch.setText("STOP");
                    if (t.getState() == Thread.State.NEW)
                        t.start();
                } else {
                    isTimerStarted = false;
                    watch.setText("START");
                    t.interrupt();
                    l = 0;
                }
            }
        });

        submitMathResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String temp = inputDigits.getText().toString();
                int a, b;
                a = Integer.parseInt(temp.substring(0, 1));
                b = Integer.parseInt(temp.substring(1));

                sum.setText("Sum: " + (a + b));
                subtract.setText("Subtract: " + (a - b));
                multiply.setText("Multiplication: " + (a * b));
            }
        });

        editUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeURL();
            }
        });

        openURL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Uri data = Uri.parse("http://" + inputUrl.getText().toString());
                Intent openWeb = new Intent(Intent.ACTION_VIEW);
                openWeb.setData(data);
                startActivity(openWeb);

            }
        });

        inputUrl.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                ClipData clip = ClipData.newPlainText("url:", inputUrl.getText());
                clipBoard.setPrimaryClip(clip);
                Toast.makeText(getApplicationContext(), "URL put in clipboard", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        time.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                ClipData clip = ClipData.newPlainText("time:", inputUrl.getText());
                clipBoard.setPrimaryClip(clip);
                Toast.makeText(getApplicationContext(), "Time put in clipboard", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        batteryStatus.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                ClipData clip = ClipData.newPlainText("batteryStatus", batteryStatus.getText());
                clipBoard.setPrimaryClip(clip);
                Toast.makeText(getApplicationContext(), "Bat Status put in clipboard", Toast.LENGTH_SHORT).show();

                return false;
            }
        });

        demoActBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent(getApplicationContext(),DemoActivity.class);
                Bundle extras = new Bundle();
                extras.putString("url", inputUrl.getText().toString());
                extras.putInt("constant", 5);
                intent.putExtras(extras);
                if(intent.resolveActivity(getPackageManager())!= null){
                    startActivityForResult(intent, REQUEST_CODE);
                }

            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       // super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            int x = data.getIntExtra("constant",0);
            Toast.makeText(getApplicationContext(), "Activity Returned: " +x, Toast.LENGTH_SHORT).show();
            }

    }

    @Override
    protected void onPause() {
        super.onPause();
       // unregisterReceiver(broadcast);

        mSwitch.setChecked(false);
        batteryStatus.setText("Unknown");
        batteryStatus.setVisibility(View.INVISIBLE);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_exit) {
            return true;
        }

        if (id == R.id.action_about) {
            launchAboutDialog();
            return true;
        }
        if (id == R.id.action_clear) {
            clearInput();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void launchAboutDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false).setMessage(getString(R.string.dialog_message)).setTitle(getString(R.string.about_title)).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void clearInput() {
        inputDigits.setText("");
        inputUrl.setText("www.zebra.com");
        batteryStatus.setText("Unknown");
        time.setText("00:00");
        sum.setText("Sum: ");
        subtract.setText("Subtract: ");
        multiply.setText("Multiplication: ");
        l=0;
    }

    public void changeURL() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        LayoutInflater inflater = this.getLayoutInflater();
        View v = inflater.inflate(R.layout.enter_url, null);
        final TextView urlUpd = (TextView) v.findViewById(R.id.urlUpdate);
        builder.setView(v).setCancelable(false).setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (urlUpd.getText().toString().length() > 0)
                    inputUrl.setText(urlUpd.getText());
                else
                    Toast.makeText(getApplicationContext(), "URL was empty. Try again.", Toast.LENGTH_SHORT).show();
            }
        }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });


        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void startTimerThread() {
        t = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {

                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                time.setText(dateFormat.format(new Date(l)));
                            }
                        });
                        l += 1000;
                    }
                } catch (InterruptedException e) {
                }
            }
        };
    }
}
