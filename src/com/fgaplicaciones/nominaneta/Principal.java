package com.fgaplicaciones.nominaneta;

import java.util.List;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.ads.*;



public class Principal extends ActionBarActivity {
//**PASAR INICIALIZACIONES CON VALORES INTERFAZ AL METODO DEL BOTÓN**
	private ShareActionProvider mShareActionProvider;
	
	public final static String MSG_NOMINA = "com.fgaplicaciones.nominaneta.NOMINA";
	public final static String MSG_EXTRA = "com.fgaplicaciones.nominaneta.EXTRA";	
	public final static String MSG_TIPO = "com.fgaplicaciones.nominaneta.TIPO";
	public final static String MSG_HAYEXTRA = "com.fgaplicaciones.nominaneta.HAYEXTRA";
	public final static String MSG_RETRIB = "com.fgaplicaciones.nominaneta.RETRIB";
	//Sueldo bruto
	double RETRIB;
	//Nómina
	double NOMINA;
	//Extra
	double EXTRA;	
	//Tipo de retención IRPF truncado
	Double Tipo = 0.00;
   //14 PAGAS
	boolean HAYEXTRA;
   //Salario mínimo
	int salarioMinimo = 9080;
   //Retribuciones totales
   //Año actual
	int ANYOACTUAL = 2016;
	//Variables AdMob
	private AdView adView;
	private InterstitialAd interstitial;
    public static boolean prueba = false;
    public static String testDevice = "D4EFE24489A377D535C5C6C5D8DC553C";
   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        
        CrearSpinners();
       
        //Validar año nacimiento
        /* final EditText anyo_edittext = (EditText)findViewById(R.id.anyo_edittext);
         
         anyo_edittext.setOnFocusChangeListener(new OnFocusChangeListener() {
         	public void onFocusChange(View v, boolean hasFocus) {
         		//v	The view whose state has changed.
             	//hasFocus	The new focus state of 
         		final String anyo = anyo_edittext.getText().toString(); 
         		final String anyo_min = v.getResources().getString(R.string.anyo_min);
         		final String anyo_max = v.getResources().getString(R.string.anyo_max);
         		if (!hasFocus && (anyo.compareTo (anyo_min) < 0 || anyo.compareTo(anyo_max) > 0)){ 
         			anyo_edittext.setError( v.getResources().getString(R.string.error_anyo_inicial));
         		}
         	}//Repetir validación al pulsar botón
         });*/
         //Añadir símbolo € al sueldo
        final EditText sueldo_edittext = (EditText)findViewById(R.id.sueldo_edittext);
        //Listener Sueldo
        sueldo_edittext.setOnFocusChangeListener(new OnFocusChangeListener() {
        public void onFocusChange(View v, boolean hasFocus) {
        	if (!hasFocus){
        		final String sueldo = sueldo_edittext.getText().toString(); 
        		if (!hasFocus && !sueldo.contains("€") && !sueldo.isEmpty()){	
        			sueldo_edittext.setText(sueldo + "€", TextView.BufferType.EDITABLE);
        		}	
        		ErrorSueldo();
        	}
        }
        });
       //Listener Descendientes 
        final EditText hijos2_edittext = (EditText)findViewById(R.id.hijos2_edittext);
        hijos2_edittext.setOnFocusChangeListener(new OnFocusChangeListener() {
        	public void onFocusChange(View v, boolean hasFocus) {
        		if (!hasFocus)
        		ErrorDescendientes();
        	}
        });
      //Listener Año
        final EditText anyo_edittext = (EditText)findViewById(R.id.anyo_edittext);
        anyo_edittext.setOnFocusChangeListener(new OnFocusChangeListener() {
        	public void onFocusChange(View v, boolean hasFocus) {
        		if (!hasFocus)
        			ErrorAnyo();
        	}
        });
   
     //AdMob -Banner
	    adView = new AdView(this);
	    adView.setAdUnitId("ca-app-pub-2186608671196230/1626088908");
	    adView.setAdSize(AdSize.SMART_BANNER);
	    LinearLayout vista = (LinearLayout)findViewById(R.id.principal_linear);
	    vista.addView(adView);
		if (prueba){
			AdRequest adRequestB = new AdRequest.Builder()
			.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)       // Emulator
			.addTestDevice(testDevice) // 
			.build();
			adView.loadAd(adRequestB); 
		}
		else{ 
			AdRequest adRequestB = new AdRequest.Builder().build();  
			adView.loadAd(adRequestB); 
		}

	//Admob Intersticial  	
	     interstitial = new InterstitialAd(this);
	     interstitial.setAdUnitId("ca-app-pub-2186608671196230/8216590907");
	     
		 if (prueba){
			 AdRequest adRequestI = new AdRequest.Builder()
		    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)       // Emulator
		    .addTestDevice(testDevice) // 
		    .build();
			interstitial.loadAd(adRequestI);
		 }
		 else{
			 AdRequest adRequestI = new AdRequest.Builder().build();
			 interstitial.loadAd(adRequestI);
		 }	 
   }
 
   @Override
   protected void onRestart() {
       super.onRestart();
       if (interstitial.isLoaded()) 
    	      interstitial.show();
    
   }
       private void CrearSpinners () {
    //Estado Civil	
    	Spinner estado_spinner = (Spinner) findViewById(R.id.estado_spinner);
    	// Create an ArrayAdapter using the string array and a default spinner layout
    	ArrayAdapter<CharSequence> estado_adapter = ArrayAdapter.createFromResource(this,
    	R.array.estado_lista, R.layout.texto_spinner);
    	// Specify the layout to use when the list of choices appears
    	estado_adapter.setDropDownViewResource(R.layout.dropdown_item_personalizado);
    	// Apply the adapter to the spinner
    	estado_spinner.setAdapter(estado_adapter);        
  
    //Deducción por vivienda habitual	
    	Spinner deduccion_spinner = (Spinner) findViewById(R.id.deduccion_spinner);   
    	ArrayAdapter<CharSequence> deduccion_adapter = ArrayAdapter.createFromResource(this,
    	R.array.deduccion_lista, R.layout.texto_spinner);     
    	deduccion_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	deduccion_spinner.setAdapter(deduccion_adapter);
    	
   //Categoría        
    	Spinner categoria_spinner = (Spinner) findViewById(R.id.categoria_spinner);      
    	ArrayAdapter<CharSequence> categoria_adapter = ArrayAdapter.createFromResource(this,
    	R.array.categoria_lista, R.layout.texto_spinner);	     
    	categoria_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	categoria_spinner.setAdapter(categoria_adapter);
    	
   //Tipo de contrato        
    	Spinner contrato_spinner = (Spinner) findViewById(R.id.contrato_spinner);      
    	ArrayAdapter<CharSequence> contrato_adapter = ArrayAdapter.createFromResource(this,
    	R.array.contrato_lista, R.layout.texto_spinner);	     
    	contrato_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	contrato_spinner.setAdapter(contrato_adapter);
    	
   //Nº pagas	        
    	Spinner pagas_spinner = (Spinner) findViewById(R.id.pagas_spinner);      
    	ArrayAdapter<CharSequence> pagas_adapter = ArrayAdapter.createFromResource(this,
    	R.array.pagas_lista, R.layout.texto_spinner);	     
    	pagas_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	pagas_spinner.setAdapter(pagas_adapter);
    }
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
       // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.principal, menu);
        /*    
     //COMPARTIR   
        MenuItem item = menu.findItem(R.id.compartir_opcion);
     //Crear Intent

        Intent enviarIntent = new Intent();
        enviarIntent.setAction(Intent.ACTION_SEND);
        enviarIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
        enviarIntent.setType("text/plain");
     
        
     // Locate MenuItem with ShareActionProvider
        Además hay que definir la clase en el XML
      /*    app:actionProviderClass=
                "android.support.v7.widget.ShareActionProvider"
        

        /* Fetch and store ShareActionProvider
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        IntentCompartir(enviarIntent);
        */
        
        
        return super.onCreateOptionsMenu(menu);		
    }

 // Call to update the share intent
    private void IntentCompartir(Intent shareIntent) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    
        
    @Override
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
   // as you specify a parent activity in AndroidManifest.xml.

   public boolean onOptionsItemSelected(MenuItem item) {
   // Handle presses on the action bar items
    //También se puede declarar cada método en el xml android:onclick
    		
    	int itemId = item.getItemId();
		if (itemId == R.id.refrescar_opcion) {
			PulsarBoton();
			return true;
		} else if (itemId == R.id.compartir_opcion) {
			IntentCompartir();
			return true;
		} else if (itemId == R.id.puntuar_opcion) {
			IntentPuntuar();
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
    	}
    	
    private boolean CalculoOK() {
    	//Validaciones: Marcamos como erróneo el campo y sacamos Toast
    	if (ErrorAnyo() || ErrorDescendientes() || ErrorSueldo()){
    		Toast.makeText(this,  this.getResources().getString(R.string.error_general), Toast.LENGTH_SHORT).show();
    		return false;
    	}	
    	//Retribuciones
    	String cadena =  ((EditText)findViewById(R.id.sueldo_edittext)).getText().toString();
    	if (cadena.isEmpty())
    		cadena = "0.00";
    	int pos = cadena.indexOf("€");
    	if (pos > -1)
    		cadena = cadena.substring(0, pos);
    	RETRIB = Float.valueOf(cadena);
    	//Base Seguridad Social
    	double BASESS;
    	//Base sin la prorrata de la paga extra, que se utilizará para la retención de IRPF
    	double BASEIR; 
    	//Tipo Contingencias Comunes   	
    	//Bases mínimas
    	double BASEMIN[] = new double[] {1051.50, 872.10, 758.70, 753.00, 753.00, 753.00, 753.00};
    	//Bases máximas
    	double BASEMAX[] = new double[] {3597.00, 3597.00, 3597.00, 3597.00, 3597.00, 3597.00, 3597.00};
    	//Tipos SS 
    	double TIPOCC = 4.70;
    	double TIPODE = 1.55;
    	double TIPODE_TEMP = 1.60;
    	double TIPOFP = 0.10;
    	//Tipo de Contrato
    	final int INDEFINIDO = 0;
    	final int TEMPORAL = 1; 
    	Spinner spinnerc = (Spinner) findViewById(R.id.contrato_spinner);
   	 	int CONTRATO = spinnerc.getSelectedItemPosition();
   	 	if (CONTRATO == TEMPORAL)
   	 		TIPODE = TIPODE_TEMP;	
    	//Número de pagas
    	final int PAGAS14 = 0;
    	final int PAGAS12 = 1;
    	final int PAGAS15 = 2;
    	final int PAGAS16 = 3;
   	 	Spinner spinner1 = (Spinner) findViewById(R.id.pagas_spinner);
   	 	int NUMPAGAS = spinner1.getSelectedItemPosition();
    	//Grupo de cotización
    	Spinner spinner2 = (Spinner) findViewById(R.id.categoria_spinner);
   	 	int GRUCOT = spinner2.getSelectedItemPosition();
    	
   	 	double RETSS;
    	//Cálculos    	
    	
    	EXTRA = 0.00;
    	NOMINA = 0.00;
    	
    	if (NUMPAGAS == PAGAS14) {
	    	HAYEXTRA = true;
    		BASEIR = RETRIB / 14;
        	BASESS = BASEIR + (BASEIR * 2) / 12;
        	
	    	if (BASESS < BASEMIN[GRUCOT])
	    		BASESS  = BASEMIN[GRUCOT];
	    	
	    	if (BASESS > BASEMAX[GRUCOT])
	    		BASESS  = BASEMAX[GRUCOT];
	    	
	    	RETSS = BASESS * (TIPOCC / 100 + TIPODE / 100 + TIPOFP /100);
	    	Tipo = TipoIRPF(RETSS);
	   	 	NOMINA = BASEIR - (BASEIR * Tipo / 100.00) - RETSS;  	 	
	   	 	EXTRA = BASEIR - (BASEIR * Tipo / 100.00);
	   	 	
    	}
    	
    	else if (NUMPAGAS == PAGAS15) {
	    	HAYEXTRA = true;
    		BASEIR = RETRIB / 15;
        	BASESS = BASEIR + (BASEIR * 3) / 12;
        	
	    	if (BASESS < BASEMIN[GRUCOT])
	    		BASESS  = BASEMIN[GRUCOT];
	    	
	    	if (BASESS > BASEMAX[GRUCOT])
	    		BASESS  = BASEMAX[GRUCOT];
	    	
	    	RETSS = BASESS * (TIPOCC / 100 + TIPODE / 100 + TIPOFP / 100);
	    	Tipo = TipoIRPF(RETSS);
	   	 	NOMINA = BASEIR - (BASEIR * Tipo / 100.00) - RETSS;  	 	
	   	 	EXTRA = BASEIR - (BASEIR * Tipo / 100.00);
	   	 	
    	}
    	
    	
    	else if (NUMPAGAS == PAGAS16) {
	    	HAYEXTRA = true;
    		BASEIR = RETRIB / 16;
        	BASESS = BASEIR + (BASEIR * 4) / 12;
        	
	    	if (BASESS < BASEMIN[GRUCOT])
	    		BASESS  = BASEMIN[GRUCOT];
	    	
	    	if (BASESS > BASEMAX[GRUCOT])
	    		BASESS  = BASEMAX[GRUCOT];
	    	
	    	RETSS = BASESS * (TIPOCC / 100 + TIPODE / 100 + TIPOFP / 100);
	    	Tipo = TipoIRPF(RETSS);
	   	 	NOMINA = BASEIR - (BASEIR * Tipo / 100.00) - RETSS;  	 	
	   	 	EXTRA = BASEIR - (BASEIR * Tipo / 100.00);
	   	 	
    	}
    	
    	else if (NUMPAGAS == PAGAS12) {
    		HAYEXTRA = false;
    		BASEIR = RETRIB / 12;	
    		BASESS = BASEIR;
    		if (BASESS < BASEMIN[GRUCOT])
        		BASESS  = BASEMIN[GRUCOT];
    		    	
 		    if (BASESS > BASEMAX[GRUCOT])
    		    BASESS  = BASEMAX[GRUCOT];		
 		   RETSS = BASESS * (TIPOCC / 100 + TIPODE / 100 + TIPOFP /100);
 		   Tipo = TipoIRPF(RETSS);
	   	   NOMINA = BASEIR - (BASEIR * Tipo / 100.00) - RETSS;	 	
    	}
    	
    	
    	
   	NOMINA = Math.round(NOMINA * 100.00) / 100.00;
   	EXTRA = Math.round(EXTRA * 100.00) / 100.00;
   	
    
   
    return true;
    }   		
    
    double TipoIRPF (double RetSS) {
    	//Retribuciones
    	String cadena =  ((EditText)findViewById(R.id.sueldo_edittext)).getText().toString();
    	if (cadena.isEmpty())
    		cadena = "0.00";
    	int pos = cadena.indexOf("€");
    	if (pos > -1)
    		cadena = cadena.substring(0, pos);
    	double RETRIB = Float.valueOf(cadena);
    	//Gastos deducibles
    	double GASTOS;
    	double OTROSGASTOS;
    	double GASTOSGEN;
    	//Cotizaciones SS
    	double COTIZACIONES;	
    	//Rendimiento neto
    	double RNT ;
    	//Reducción por ser desempleado
    	double DESEM;
    	//Reducción por tener más de dos hijos
    	double HIJOS;
    	//Rendimiento neto reducido	
    	double RNTREDU;
    	
    	//Reducción por rendimientos del trabajo
    	double RED20;
    	//Reducción por prolongación de la actividad laboral
    	double PROLONLAB;
    	//Mínimo personal y familiar
    	double MINCON;	
    	//Mínimo si edad > 65
    	double PER65;
    	//Mínimo si edad > 75
    	double PER75;
    	//Número de descendientes < 25 años
    	
    	cadena = ((EditText)findViewById(R.id.hijos1_edittext)).getText().toString();
    	if (cadena.isEmpty())
    		cadena = "0";
    	int NUMD25 =  Integer.valueOf(cadena);
    	//Mínimo por descendientes < 25 años
    	double MINDESG;
    	//Número total de descendientes
    	int NUMDES;
    	//Número de descendientes < 3 años
    	cadena = ((EditText)findViewById(R.id.hijos2_edittext)).getText().toString();
    	if (cadena.isEmpty())
    		cadena = "0";
    	int NUMDES3 = Integer.valueOf(cadena);
    	//Mínimo por descendientes < 3 años
    	double MINDES3;
    	//Mínimo total por descendientes
    	double MINDES;
    	//Mínimo personal y familiar
    	double MINPERFA;    	
    	//Número de ascendientes entre 65 y 74 años
    	cadena = ((EditText)findViewById(R.id.asc1_edittext)).getText().toString();
    	if (cadena.isEmpty())
    		cadena = "0";
    	int NUMAS65A = Integer.valueOf(cadena);
    	//Número de ascendientes de 75 años o mayores
    	cadena = ((EditText)findViewById(R.id.asc2_edittext)).getText().toString();
    	if (cadena.isEmpty())
    		cadena = "0";
    	int NUMAS75A = Integer.valueOf(cadena); 
    	
    	 //Mínimo por ascendientes entre 65 y 74 años
    	 double AS65;
    	 //Mínimo por ascendientes de 75 años o mayores
    	 double AS75;
    	//Mínimo por ascendientes
    	 double MINAS;
    	 //Suma de reducciones
    	 double REDU;
    	 //Base de retención
    	 double BASE;
    	 //Rendimientos exentos S/N
    	 boolean EXENTOS;
    	 //Situación familiar 

    	 final int SITUACION1 = 0;
    	 final int SITUACION2 = 1;
    	 final int SITUACION3 = 2;
    	
    	 Spinner spinner1 = (Spinner) findViewById(R.id.estado_spinner);
    	 int SITUFAM = spinner1.getSelectedItemPosition();
    	 
    	 boolean CASADO = (SITUFAM == 1 || SITUFAM == 2); 
    	 
    	 
    	 if (SITUFAM == 0)
    		 if (NUMD25 > 0)
    			 SITUFAM = 0;
    		 else
    			 SITUFAM = 2;
    			 
    	 //Cuota de retención
    	 double CUOTA;
    	 //Tipo de retención IRPF con decimales
    	 double TIPOD;
    	 //Deduccion art. 80 bis LIRPF
    	 double DEDUCCION;
    	 //Derecho a deducción por vivienda habitual
    	 final int NO = 0;
    	 final int SI = 1;
    	 Spinner spinner2 = (Spinner) findViewById(R.id.deduccion_spinner);
    	 int PRESVIV = spinner2.getSelectedItemPosition();
    	 //Límite del 43%
    	 double LIMITE;
    	//Tipo previo de retención IRPF
    	 double TIPOPREVIO;
    	//Tipo previo de retención IRPF redondeado al entero más próximo
    	 int TIPOPREVIOR;    	 
    	 //Importe previo de la retención
    	 double IMPORTEPREVIO;
    	 //Reducción hipoteca vivienda habitual con decimales
    	 double MINOPAGOD;
    	//Reducción hipoteca vivienda habitual truncada
    	 int MINOPAGO;
    	//Importe intermedio para el cálculo del tipo de retención
    	 double DIFERENCIAPOSITIVA;
    	//Año de nacimiento
    	 cadena = ((EditText)findViewById(R.id.anyo_edittext)).getText().toString();
      	if (cadena.isEmpty())
      		cadena = "0";
      	int ANYO =  Integer.valueOf(cadena); 
    	 //Auxiliares
    	 double CUOTA1;
    	 double CUOTA2;

    	 //Nuevos gastos deducibles 2015
    	 GASTOSGEN = 2000.00;
    	 OTROSGASTOS = GASTOSGEN;
    	 
    	 //Cotizaciones SS
    	 COTIZACIONES = RetSS * 12;
    	 
    	 if (RETRIB - COTIZACIONES < 0)
    		 OTROSGASTOS = 0;
    	
    	 if (OTROSGASTOS > RETRIB - COTIZACIONES)
    		OTROSGASTOS = RETRIB - COTIZACIONES;
    	
    	 //Total Gastos deducibles
    	 GASTOS = COTIZACIONES + OTROSGASTOS;
    	 
    	 //Rendimiento neto del trabajo
    	 RNT = RETRIB - COTIZACIONES;
    	 if (RNT < 0) 
    	 	RNT = 0;
    	 

    	 //Reducción por obtención de rendimientos del trabajo
    	 if (RNT <= 11250.00)
    		 RED20 = 3700.00;
    	 else if (RNT <= 14450.00)
    		 RED20 = 3700.00 - (1.15625 * (RNT - 11250.00));
    	 else
    		 RED20 = 0.00;
    	 
    	 RED20 = Math.round(RED20 * 100.00) / 100.00;
    	 
    	/*Reducción por prologación de la actividad laboral
    	 Desaparece en 2015
    	 if (2015 - ANYO > 64)   		
    		 PROLONLAB = RED20;
    	 else
    		 PROLONLAB = 0.00;
    	*/		   			 
    	 RNTREDU = RNT - OTROSGASTOS - RED20;
    	
    	 //Reducción por mas de dos descendientes
    	 NUMDES = NUMD25;
    	 HIJOS = 0.00;
    	 if (NUMDES > 2)
    		 HIJOS = 600.00;
    	 
    	 
    	 //Mínimo personal y familiar
    	 
    	 	//Mínimo del contribuyente
    	 
    	 MINCON = 5550.00;
    	 if (ANYOACTUAL - ANYO > 64)
    		 PER65 = 1150.00;
    	 else 
    		 PER65 = 0.00;
    	 
    	 if (ANYOACTUAL - ANYO > 74)
    		 PER75 = 1400.00;
    	 else 
    		 PER75 = 0.00;
    	 
    	 MINCON = MINCON + PER65 + PER75; 
     	
     	//Mínimo por descendientes < 25 años
    	 
    	 	//Mínimo por descendientes < 25 años
    	 MINDESG = 0.00;	
    	 if (NUMD25 > 0){
    		 for (byte i=1; i<=NUMD25; i++){
    	 			if (i == 1)
    	 				MINDESG = MINDESG + 2400.00;
    	   	   else if (i == 2)
    	 				MINDESG = MINDESG + 2700.00;
    	   	   else if (i == 3)
    	 				MINDESG = MINDESG + 4000.00;
    	   	   else if (i >= 4)
	 					MINDESG = MINDESG + 4500.00;
    		 }
    		 if (CASADO)
    		 	MINDESG = MINDESG * 0.5;
    	 	}
    	MINDESG  = Math.round(MINDESG * 100.00) / 100.00; 
    	 //Mínimo por descendientes menores de 3 años
    	 	//Se deja con suma para poder añadir cálculos por entero/mitad individuales
    	 MINDES3 = 0.00;
    	 for (byte i=1; i<= NUMDES3; i++){
    	    	MINDES3 = MINDES3 + 2800.00; 
    	    }
    	 if (CASADO)
 		 	MINDES3 = MINDES3 * 0.5;
    	 
    	 MINDES3 = Math.round(MINDES3 * 100.00) / 100.00;
    	 MINDES = MINDESG + MINDES3;
    	
    	 
    	 	//Mínimo por ascendientes entre 65 y 74 años
    	 AS65 = 0.00;
    	 for (byte i=1; i<= NUMAS65A + NUMAS75A; i++){
 	    	AS65 = AS65 + 1150.00; 
 	    }
    	 AS65 = Math.round(AS65 *100.00) / 100.00;
    	 	//Minimo por ascendientes de 75 años o mayores
    	 AS75 = 0.00;
    	 for (byte i=1; i<= NUMAS75A; i++){
 	    	AS75 = AS75 + 1400.00; 
 	    }
    	AS75 = Math.round(AS75 * 100.00) / 100.00;
    	MINAS = AS65 +AS75;		
    	//Mínimo personal y familiar
    	MINPERFA = MINCON + MINDES + MINAS;
    	 //BASE PARA CALCULAR EL TIPO DE RETENCION
    	 
    	 //Suma de reducciones
    	 REDU = HIJOS;
    	 //Cálculo de la base
    	 if (RNTREDU > REDU)
    		 BASE = RNTREDU - REDU;
    	 else
    		 BASE = 0.00;
    	
    	//Cuota de retención
    	 
    	 //Rendimientos exentos de retención
    	 EXENTOS = false;
    	 if (RETRIB <= 17138.00) {
    		 if (SITUFAM == SITUACION1){
    			 if (NUMDES == 1 && RETRIB <= 14266.00)
    				 EXENTOS = true;
    			 else if (NUMDES > 1 && RETRIB <= 15803.00)
    				 EXENTOS = true;
    		 }
    		 if (SITUFAM == SITUACION2){
    			 if (NUMDES == 0 && RETRIB <= 13696.00)
    				 EXENTOS = true;
    			 else if (NUMDES == 1 && RETRIB <= 14985.00)
    				 EXENTOS = true;
    			 else if (NUMDES > 1 && RETRIB <= 17138.00)
    				 EXENTOS = true;
    		 }
    		 if (SITUFAM == SITUACION3){
    			 if (NUMDES == 0 && RETRIB <= 12000.00)
    				 EXENTOS = true;
    			 else if (NUMDES == 1 && RETRIB <= 12607.00)
    				 EXENTOS = true;
    			 else if (NUMDES > 1 && RETRIB <= 13275.00)
    				 EXENTOS = true;
    		 }
    	 }
    	//Rendimientos sujetos a retención
    	 

    	CUOTA = 0.00;
        TIPOD = 0.00;
    	 
        if (!EXENTOS) {	 
        //Cálculo cuota
        CUOTA1 = CalcularCuota(BASE);
    	CUOTA2 = CalcularCuota(MINPERFA);
    	if (CUOTA1 > CUOTA2)
    		CUOTA = CUOTA1 - CUOTA2;
    	
    	// Deducción art. 80 bis LIRPF
    	/*Desaparece en 2014
    		 DEDUCCION = 0.00;
    		 if (BASE < 8000.00)
    			 DEDUCCION = 400.00;
    		 else if (BASE <= 12000.00 && BASE > 8000.00) 
    			 DEDUCCION = 400 - 0.1 * (BASE - 8000.00); 
    	*/
    	//Límite del 43%
    		 LIMITE = 0.00;
    		 if (RETRIB <= 22000.00) {
    			 if (SITUFAM == SITUACION1){
    				 if (NUMDES == 1)
    					 LIMITE = (RETRIB - 14266.00) * 0.43; 
    				 else if (NUMDES > 1)
    					 LIMITE = (RETRIB - 15803.00) * 0.43; 
    			 }
    			 if (SITUFAM == SITUACION2){
    				 if (NUMDES == 0)
    					 LIMITE = (RETRIB - 13696.00) * 0.43; 
    				 else if (NUMDES == 1)
    					 LIMITE = (RETRIB - 14985.00) * 0.43; 
    				 else if (NUMDES > 1)
    					 LIMITE = (RETRIB - 17138.00) * 0.43; 
    			 }
    			 if (SITUFAM == SITUACION3){
    				 if (NUMDES == 0)
    					 LIMITE = (RETRIB - 12000.00) * 0.43; 
    				 else if (NUMDES == 1)
    					 LIMITE = (RETRIB - 12607.00) * 0.43; 
    				 else if (NUMDES > 1)
    					 LIMITE = (RETRIB - 13275.00) * 0.43; 
    			 }	 
    		
    		 if (CUOTA > LIMITE)
    			 CUOTA = LIMITE;
    		 }	
    //Tipo previo de retención y tipo de retención
   /* 		 
    		 TIPOPREVIO = (CUOTA / RETRIB) * 100;
    		 TIPOPREVIOR = (int) Math.round(TIPOPREVIO);
 
    //Importe previo de la retención
    		 IMPORTEPREVIO = (RETRIB * TIPOPREVIOR) /100;
     */  
    //Reducción por hipoteca vivienda habitual
    		 if (RETRIB < 33007.20 && PRESVIV == SI)
    			 MINOPAGOD = 2.00 / 100 * RETRIB; 
    		 else
    			 MINOPAGOD = 0;
   
    		 MINOPAGOD = ((double)(int)(MINOPAGOD * 100)) / 100.00;
    	
    		 DIFERENCIAPOSITIVA = CUOTA - MINOPAGOD; 
    			
    		 if (DIFERENCIAPOSITIVA < 0) 
    			 DIFERENCIAPOSITIVA = 0;
    
    //Tipo de retención aplicable
    		 TIPOD = (DIFERENCIAPOSITIVA / RETRIB) * 100; 
    //Truncar al segundo decimal		 
             TIPOD = ((double)(int)(TIPOD * 100)) / 100.00;
        	}	   	
        
        return TIPOD;

    }
    	
 
    private double CalcularCuota(double Base){
   	 
    //Tabla de  restos de las bases de retención
    	double RESTOS[] = new double[] {12450.00, 7750.00, 15000.00, 24800.00, 0.00};
   	 //Tabla de tipos de retención
   	 	double TIPOS[] = new double[] {19.00, 24.00, 30.00, 37.00, 45.00};
   	//Cuota de retención
   	 	double CUOTA = 0.00;
		
   	 	boolean PARAR = false;
   	 	byte i = 0;
		PARAR = false;
		while (!PARAR){
			 if (Base <= RESTOS[i] || i == 4){
				 PARAR = true;
				 CUOTA = CUOTA + Base * TIPOS[i] / 100.00;
			 }
			else{
				CUOTA = CUOTA + RESTOS[i] * TIPOS[i] / 100.00;
				Base = Base - RESTOS[i];
			 }
			 i++;
		 }	 
		 return CUOTA;
    }
   private boolean ErrorSueldo() {
   
    	final EditText sueldo_edittext = (EditText)findViewById(R.id.sueldo_edittext);
    	String sueldo = sueldo_edittext.getText().toString();
    	int sueldo_int = 0;
    	
    	//Si solo contiene, símbolo euro, quitarlo
    	if (sueldo.indexOf("€") == 0){
    		sueldo = "0€";
    		sueldo_edittext.setText(sueldo);
    	}
    	if (!sueldo.isEmpty()){
    		if (sueldo.indexOf("€") > 0)
    			sueldo = sueldo.substring(0, sueldo.indexOf("€"));
    		sueldo_int = Integer.parseInt(sueldo); 
    		}
    	if  (sueldo_int <= salarioMinimo){ 
    		sueldo_edittext.setError( this.getResources().getString(R.string.error_sueldo));
    		return true;
    	}	
    	return false;	    		
    	}
    
    
    private boolean ErrorAnyo() {
    	final EditText anyo_edittext = (EditText)findViewById(R.id.anyo_edittext);
    	final String anyo = anyo_edittext.getText().toString(); 
    	final String anyo_min = this.getResources().getString(R.string.anyo_min);
    	final String anyo_max = this.getResources().getString(R.string.anyo_max);
    	
    	if  (anyo.compareTo (anyo_min) < 0 || anyo.compareTo(anyo_max) > 0){ 
    		    	anyo_edittext.setError( this.getResources().getString(R.string.error_anyo_inicial));
    		    return true;
    		 	}
    	else
    		    return false;
    	
    }
    private boolean ErrorDescendientes() {
    	
    	final EditText hijos1_edittext = (EditText)findViewById(R.id.hijos1_edittext);
    	final String hijos1 = hijos1_edittext.getText().toString();    	
    	final EditText hijos2_edittext = (EditText)findViewById(R.id.hijos2_edittext);
    	final String hijos2 = hijos2_edittext.getText().toString();
    	
    	if  (!hijos2.isEmpty() && hijos1.compareTo(hijos2) < 0 ){ 
    		hijos2_edittext.setError( this.getResources().getString(R.string.error_hijos));
    		return true;
    	}
    	else
    		hijos2_edittext.setError(null);
    		return false;
    }
    
    
 /*  private void Opciones() {
	   Toast.makeText(this, "Opciones", Toast.LENGTH_SHORT).show();
    	    } 	*/

   public void BotonCalcular(View v){
	   PulsarBoton();
   }
    
  private void PulsarBoton(){
	  if (CalculoOK()){
		   Intent resultadoIntent = new Intent(this, Resultado.class);
		   resultadoIntent.putExtra(MSG_NOMINA, NOMINA);
		   resultadoIntent.putExtra(MSG_EXTRA, EXTRA);
		   resultadoIntent.putExtra(MSG_TIPO, Tipo);
		   resultadoIntent.putExtra(MSG_HAYEXTRA, HAYEXTRA);
		   resultadoIntent.putExtra(MSG_RETRIB, RETRIB);
		   startActivity(resultadoIntent);
	  }
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