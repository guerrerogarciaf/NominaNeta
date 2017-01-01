package com.fgaplicaciones.nominaneta;

import java.text.DecimalFormat;
import java.util.List;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

//import com.appflood.AppFlood;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class Resultado extends ActionBarActivity {
	private AdView adView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		
	//Recibir Intent
		Intent intent = getIntent();
		double RETRIB = intent.getDoubleExtra(Principal.MSG_RETRIB, 0.0);
		double NOMINA = intent.getDoubleExtra(Principal.MSG_NOMINA, 0.0);
		double EXTRA = intent.getDoubleExtra(Principal.MSG_EXTRA, 0.0);
		double TIPO = intent.getDoubleExtra(Principal.MSG_TIPO, 0);
		boolean HAYEXTRA = intent.getBooleanExtra(Principal.MSG_HAYEXTRA, true);
	//Establecer vista	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_resultado);
	//Navegación atrás	
		getActionBar().setDisplayHomeAsUpEnabled(true);
	//Interfaz	
		if (!HAYEXTRA){
			GridLayout vista = (GridLayout)findViewById(R.id.resultado_grid);
			TextView extra_textview = (TextView) findViewById(R.id.extra_textview);
			TextView extra_titulo = (TextView) findViewById(R.id.extra_titulo);
			vista.removeView(extra_titulo);
			vista.removeView(extra_textview);
		}
		
		DecimalFormat formato1 = new DecimalFormat("#,###,###.##");
		DecimalFormat formato2 = new DecimalFormat("#,###,##0.00");
		
		
	   	final TextView nomina_textview = (TextView) findViewById(R.id.nomina_textview);   
	   	nomina_textview.setText(formato2.format(NOMINA) + " €", TextView.BufferType.EDITABLE);
	   	
	   	final TextView sueldo_textview = (TextView) findViewById(R.id.sueldo_textview);   
	   	sueldo_textview.setText(formato1.format(RETRIB) + " €", TextView.BufferType.EDITABLE);
	   	
	    if (HAYEXTRA){ 	
	    	final TextView extra_textview = (TextView) findViewById(R.id.extra_textview);
	    	extra_textview.setText(formato2.format(EXTRA) + " €", TextView.BufferType.EDITABLE);
	    }
	    
	    
	   	final TextView tipo_textview = (TextView) findViewById(R.id.tipo_textview);   
	   	tipo_textview.setText(formato2.format(TIPO) + " %", TextView.BufferType.EDITABLE);
	
	//AdMob 
	   	adView = new AdView(this);
	    adView.setAdUnitId("ca-app-pub-2186608671196230/1626088908");
	    adView.setAdSize(AdSize.SMART_BANNER);
	    LinearLayout vista = (LinearLayout)findViewById(R.id.resultado_linear);
	    vista.addView(adView);		  
	    if (Principal.prueba){   	  
	    	AdRequest adRequest = new AdRequest.Builder()
	    	.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)       // Emulator
	    	.addTestDevice(Principal.testDevice) // 
	    	.build();
	    	adView.loadAd(adRequest); 
	    }
	    	else{
	    		AdRequest adRequest = new AdRequest.Builder().build();  
	    		adView.loadAd(adRequest); 
	    }
	
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.resultado, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		
		int itemId = item.getItemId();
		if (itemId == android.R.id.home) {
			NavUtils.navigateUpFromSameTask(this);
			return true;
		} else if (itemId == R.id.compartir_opcion_resultado) {
			IntentCompartir();
			return true;
		} else if (itemId == R.id.puntuar_opcion_resultado) {
			IntentPuntuar();
			return true;
		}
		return super.onOptionsItemSelected(item);		
		
}

	@Override
	 public void onPause() {
	    adView.pause();
	    super.onPause();
	  }

	@Override
	public void onResume() {
	    super.onResume();
	    adView.resume();
	  }

	@Override
	public void onDestroy() {
		adView.destroy();
	    super.onDestroy();
	  }	
	private void IntentCompartir(){
	      Intent enviarIntent = new Intent();
	      enviarIntent.setAction(Intent.ACTION_SEND);
	      enviarIntent.putExtra(Intent.EXTRA_TEXT, getResources().getText(R.string.compartir_app));
	      enviarIntent.setType("text/plain");
	      Intent.createChooser(enviarIntent, getResources().getText(R.string.recomendar));
	      startActivity(Intent.createChooser(enviarIntent, getResources().getText(R.string.enviar_a)));
	}
	
	      private void IntentPuntuar(){
	    	  Intent intent = new Intent();
	    	  intent.setAction(Intent.ACTION_VIEW);
	    	  intent.setData(Uri.parse("market://search?q=foo"));

	    	  PackageManager pm = getPackageManager();
	    	  List<ResolveInfo> list = pm.queryIntentActivities(intent, 0);
	    	  if (!list.isEmpty()){
	    		  Intent i = new Intent(Intent.ACTION_VIEW);
	    		//Para desactivar el backstack del Market y que se vuelve a la aplicación  
	    		  intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET | Intent.FLAG_ACTIVITY_MULTIPLE_TASK); 
	    		  i.setData(Uri.parse("market://details?id=" + getPackageName()));
	    		  startActivity(i);
	    	  }
	      }
}

