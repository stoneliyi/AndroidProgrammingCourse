package com.yahoo.tipcaculator;

import java.math.BigDecimal;

import android.os.Bundle;
import android.os.storage.OnObbStateChangeListener;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class TipActivity extends Activity {
	private EditText etAmount;
	private TextView tvTipAmount;
	private TextView tvTotalAmount;
	private TextView tvSBProgress;
	private SeekBar sbTipPct;
	private double tipAmount;
	private double totalAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tip);
        
        etAmount = (EditText)findViewById(R.id.etAmount);
        tvTipAmount = (TextView)findViewById(R.id.textViewTipAmount);
        tvTotalAmount = (TextView)findViewById(R.id.textViewTotalAmount);
        tvSBProgress = (TextView)findViewById(R.id.textViewSBProgress);
        sbTipPct = (SeekBar)findViewById(R.id.sbTipPct);
        sbTipPct.setMax(100);
        sbTipPct.setProgress(0);
        setupSBListen();
    }

    private void setupSBListen() {
    	sbTipPct.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {			
				int sbProgress = seekBar.getProgress();
				//Toast.makeText(getBaseContext(), "current progress: " + sbProgress, Toast.LENGTH_LONG).show();
				if (sbProgress < 0 || sbProgress > 100) {
					return;
				}
				String inputPrice = etAmount.getText().toString();
		    	if ("".equals(inputPrice)) {
		    		return;
		    	}
		    	double tipPct = (sbProgress * 1.00) / 100;
				tipAmount = Double.parseDouble(inputPrice) * tipPct;
	    		totalAmount = Double.parseDouble(inputPrice) + tipAmount;
	    		tvTipAmount.setText(String.valueOf(round(tipAmount, 2)));
	    		tvTotalAmount.setText(String.valueOf(round(totalAmount, 2)));
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				tvSBProgress.setText(progress + "%");
			}
		});
		
	}

	public void calTipAmount(View button) {
    	String inputPrice = etAmount.getText().toString();
    	if ("".equals(inputPrice)) {
    		return;
    	}
    	try {
    	   	double tipPct = getTipPct(button);
    		tipAmount = Double.parseDouble(inputPrice) * tipPct;
    		totalAmount = Double.parseDouble(inputPrice) + tipAmount;
    		tvTipAmount.setText(String.valueOf(round(tipAmount, 2)));
    		tvTotalAmount.setText(String.valueOf(round(totalAmount, 2)));
    		
    	} catch (NumberFormatException e) {
    		Toast.makeText(getBaseContext(), inputPrice + " is not a number. please re-enter", Toast.LENGTH_LONG).show();
    	} catch (Exception ex) {
    		ex.printStackTrace();
    	}   		
	}

	private double getTipPct(View button) throws Exception {
		double tipPct;
		if (button.getId() == R.id.buttonTip10) {
    		tipPct = 0.10;
    	} else if (button.getId() == R.id.buttonTip15) {
    		tipPct = 0.15;
    	} else if (button.getId() == R.id.buttonTip20) {
    		tipPct = 0.20;
    	} else {
    		throw new Exception("unknown button clicked");
    	}
		return tipPct;
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.tip, menu);
        return true;
    }
    
    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, BigDecimal.ROUND_HALF_UP);
        return bd.doubleValue();
    }
    
}
