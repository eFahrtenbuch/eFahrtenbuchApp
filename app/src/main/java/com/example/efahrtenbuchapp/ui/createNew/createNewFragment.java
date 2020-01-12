package com.example.efahrtenbuchapp.ui.createNew;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.util.Calendar;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.Request;
import com.example.efahrtenbuchapp.MainActivity2;
import com.example.efahrtenbuchapp.R;
import com.example.efahrtenbuchapp.eFahrtenbuch.Adresse;
import com.example.efahrtenbuchapp.eFahrtenbuch.Auto;
import com.example.efahrtenbuchapp.eFahrtenbuch.Fahrt;
import com.example.efahrtenbuchapp.eFahrtenbuch.UserManager;
import com.example.efahrtenbuchapp.http.json.JSONConverter;
import com.example.efahrtenbuchapp.gps.LocationChangedListener;
import com.example.efahrtenbuchapp.http.HttpRequester;
import com.example.efahrtenbuchapp.http.UrlBuilder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class createNewFragment extends Fragment {

    final static String FEHLERTEXT = "Fehler beim Speichern der Adresse!";

    private final AtomicReference<Double> Lat = new AtomicReference();
    private final AtomicReference<Double> Lon = new AtomicReference();
    private final LocationChangedListener locationListener =  location -> setKoordinaten(Lat, Lon, location);
    private LocationManager locationManager;

    /*Textfelder*/
    //Start-Adresse
    private EditText StartStreetText;
    private EditText StartNumberText;
    private EditText StartPLZText;
    private EditText StartOrtText;
    //End-Adresse
    private EditText EndStreetText;
    private EditText EndNumberText;
    private EditText EndPLZText;
    private EditText EndOrtText;
    /*Buttons*/
    private Button EndAddressButton;
    private Button AddressButton;
    /*Der Rest*/
    private EditText reiseRoute;
    private EditText reiseZweck;
    private EditText besuchtePersonenFirmenBehoerden;
    private EditText kmFahrtBeginn;
    private EditText kmFahrtEnde;
    private EditText kmGeschaeftlich;
    private EditText kmPrivat;
    private EditText kmWohnArbeit;
    private EditText kraftstoffLiter;
    private EditText kraftstoffBetrag;
    private EditText literPro100km;
    private     EditText sonstigesBetrag;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        CreateNewViewModel createNewViewModel = ViewModelProviders.of(this).get(CreateNewViewModel.class);


        View root = inflater.inflate(R.layout.fragment_createnew, container, false);

        //Start Adresse - Textfelder
        EditText StartTimeText = root.findViewById(R.id.etStartTime);
        EditText StartDateText = root.findViewById(R.id.etStartDate);
        StartStreetText = root.findViewById(R.id.etStartStreet);
        StartNumberText = root.findViewById(R.id.etStartNr);
        StartPLZText = root.findViewById(R.id.etStartPLZ);
        StartOrtText = root.findViewById(R.id.etStartOrt);

        //Ziel Adresse - Textfelder
        EditText EndTimeText = root.findViewById(R.id.etEndTime);
        EditText EndDateText = root.findViewById(R.id.etEndDate);
        EndStreetText = root.findViewById(R.id.etEndStreet);
        EndNumberText = root.findViewById(R.id.etEndNr);
        EndPLZText = root.findViewById(R.id.etEndPLZ);
        EndOrtText = root.findViewById(R.id.etEndOrt);

        //Buttons
        AddressButton = root.findViewById(R.id.btGetStartAdress);
        EndAddressButton = root.findViewById(R.id.btGetEndAdress);
        Button StartNavButton = root.findViewById(R.id.btStartNav);
        Button sendButton = root.findViewById(R.id.btSubmit);
        Calendar rightNow = Calendar.getInstance();

        //Spinner
        Spinner spinner = root.findViewById(R.id.spinner);

        //Start-/Endzeit setzen
        StartTimeText.setText(formatTimeAsString(rightNow));
        EndTimeText.setText(formatTimeAsString(rightNow));
        StartDateText.setText(formatDateAsString(rightNow));
        EndDateText.setText(formatDateAsString(rightNow));

        //Damit keine Tastatur angezeigt wird
        StartTimeText.setInputType(InputType.TYPE_NULL);
        StartDateText.setInputType(InputType.TYPE_NULL);
        EndTimeText.setInputType(InputType.TYPE_NULL);
        EndDateText.setInputType(InputType.TYPE_NULL);

        //Rest
        kmFahrtBeginn = root.findViewById(R.id.etStartKm);
        kmFahrtEnde = root.findViewById(R.id.etEndKm);
        kmGeschaeftlich = root.findViewById(R.id.etGeschaeftlichGef);
        kmPrivat = root.findViewById(R.id.etPrivatGef);
        kmWohnArbeit = root.findViewById(R.id.etArbeitsweg);
        kraftstoffLiter = root.findViewById(R.id.etLiterbetrag);
        kraftstoffBetrag = root.findViewById(R.id.etSprittkosten);
        literPro100km = root.findViewById(R.id.etVerbrauch);
        sonstigesBetrag = root.findViewById(R.id.etExtrasKosten);
        //Inputtype für Nummern
        kmFahrtBeginn.setInputType(InputType.TYPE_CLASS_NUMBER);
        kmFahrtEnde.setInputType(InputType.TYPE_CLASS_NUMBER);
        kmGeschaeftlich.setInputType(InputType.TYPE_CLASS_NUMBER);
        kmPrivat.setInputType(InputType.TYPE_CLASS_NUMBER);
        kmWohnArbeit.setInputType(InputType.TYPE_CLASS_NUMBER);
        kraftstoffLiter.setInputType(InputType.TYPE_CLASS_NUMBER);
        kraftstoffBetrag.setInputType(InputType.TYPE_CLASS_NUMBER);
        literPro100km.setInputType(InputType.TYPE_CLASS_NUMBER);
        sonstigesBetrag.setInputType(InputType.TYPE_CLASS_NUMBER);

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        //TODO: PERMISSIONS CHECK
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //Berechtigung vorhanden
            setAdresseErfassenListener();
        }else{
            //Berechtigung nicht vorhanden
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }


        //Erstellen der Time- und Datepicker Dialoge
        final TimePickerDialog timePickerDialogEnd = createTimepickerDialog(rightNow, EndTimeText);
        final TimePickerDialog timePickerDialog = createTimepickerDialog(rightNow, StartTimeText);
        final DatePickerDialog datePickerDialog = createDatePickerDialog(rightNow, StartDateText);
        final DatePickerDialog datePickerDialogEnd = createDatePickerDialog(rightNow, EndDateText);

        //Setzen der OnClickListener für die Time- und Datepicker
        StartTimeText.setOnClickListener(click -> timePickerDialog.show());
        StartDateText.setOnClickListener(click -> datePickerDialog.show());
        EndTimeText.setOnClickListener(click -> timePickerDialogEnd.show());
        EndDateText.setOnClickListener(click -> datePickerDialogEnd.show());

       StartNavButton.setOnClickListener(click -> openGoogleMapsNavigation(EndStreetText, EndNumberText, EndPLZText, EndOrtText));
       sendButton.setOnClickListener(click -> speichereFahrt(root, StartTimeText, StartDateText, EndTimeText, EndDateText, spinner));


        int userid = UserManager.getInstance().getUser().getId();
        String url = new UrlBuilder().path("getUserAutos").param("userid", Integer.toString(userid)).build();
        HttpRequester.simpleJsonArrayRequest(root.getContext(), url, jsonArrayResponse -> {
            List<Auto> autos = JSONConverter.mapToObjectList(Auto.class, jsonArrayResponse);
            createSpinnerAdapter(spinner, autos.stream().map(auto -> auto.getKennzeichen()).collect(Collectors.toList()));
        }, error -> {
            createSpinnerAdapter(spinner, Arrays.asList(new String[]{"Fehler beim Laden!"}));
            Toast.makeText(getActivity(), "Fehler beim Laden der Autos!", Toast.LENGTH_LONG).show();
        });
        return root;
    }

    private void speichereFahrt(View root, EditText startTimeText, EditText startDateText, EditText endTimeText, EditText endDateText, Spinner spinner) {
        String startDate = startDateText.getText().toString();
        String endDate = endDateText.getText().toString();
        Adresse startAdresse = new Adresse(string(StartStreetText), string(StartNumberText), string(StartOrtText), Integer.parseInt(string(StartPLZText)), "");
        Adresse endAdresse = new Adresse(string(EndStreetText), string(EndNumberText), string(EndOrtText), Integer.parseInt(string(EndPLZText)), "");

        AtomicReference<Integer> startAdresseID = new AtomicReference();
        AtomicReference<Integer> endAdresseID = new AtomicReference();
        startAdresse.speichere(getActivity(), responseStartAdresse -> {
            startAdresseID.set(Integer.valueOf(Integer.parseInt(responseStartAdresse)));
             endAdresse.speichere(getActivity(), responseEndAdresse -> {
                 try{
                     startAdresseID.set(Integer.valueOf(Integer.parseInt(responseStartAdresse)));
                     endAdresseID.set(Integer.valueOf(Integer.parseInt(responseEndAdresse)));
                     if(startAdresseID.get().intValue() >= 0 && endAdresseID.get().intValue() >= 0){
                         Fahrt fahrt = createFahrtFromGui(root, startTimeText, endTimeText, spinner, startDate, endDate, startAdresseID, endAdresseID);

                         HttpRequester.simpleJsonRequest(getActivity(), Request.Method.POST, new UrlBuilder().path("insertFahrt").build(), fahrt.toJSONObject(),
                                 listener -> showLongToast("Fahrt wurde erfolgreich gespeichert"),
                                 errorListener -> showLongToast("Fehler beim Speichern der Fahrt!"));
                     }
                     else{
                         showLongToast(FEHLERTEXT + "(1)");
                     }
                 }
                 catch (NumberFormatException e){
                     showLongToast(FEHLERTEXT + "(2)");
                 }
             }, error -> showLongToast(FEHLERTEXT + "(3)"));
        }, error -> showLongToast(FEHLERTEXT + "(4)"));
    }

    private Fahrt createFahrtFromGui(View root, EditText startTimeText, EditText endTimeText, Spinner spinner, String startDate, String endDate, AtomicReference<Integer> startAdresseID, AtomicReference<Integer> endAdresseID) {
        EditText reiseRoute = root.findViewById(R.id.etRoute);
        EditText reiseZweck = root.findViewById(R.id.etZweck);
        EditText besuchtePersonenFirmenBehoerden = root.findViewById(R.id.etZiel);
        EditText kmFahrtBeginn = root.findViewById(R.id.etStartKm);
        EditText kmFahrtEnde = root.findViewById(R.id.etEndKm);
        EditText kmGeschaeftlich = root.findViewById(R.id.etGeschaeftlichGef);
        EditText kmPrivat = root.findViewById(R.id.etPrivatGef);
        EditText kmWohnArbeit = root.findViewById(R.id.etArbeitsweg);
        EditText kraftstoffLiter = root.findViewById(R.id.etLiterbetrag);
        EditText kraftstoffBetrag = root.findViewById(R.id.etSprittkosten);
        EditText literPro100km = root.findViewById(R.id.etVerbrauch);
        EditText sonstigesBetrag = root.findViewById(R.id.etExtrasKosten);

        Adresse startAdresse = new Adresse(startAdresseID.get().intValue(), string(StartStreetText), string(StartNumberText), string(StartOrtText), Integer.parseInt(string(StartPLZText)), "");
        Adresse endAdresse = new Adresse(startAdresseID.get().intValue(), string(EndStreetText), string(EndNumberText), string(EndOrtText), Integer.parseInt(string(EndPLZText)), "");

        Fahrt fahrt = new Fahrt(-1, parseDate(startDate), parseDate(endDate), parseTimeAndDate(startDate,  string(startTimeText)),
                parseTimeAndDate(endDate, string(endTimeText)), startAdresseID.get().intValue(), endAdresseID.get().intValue(),
                string(reiseZweck), string(reiseRoute), string(besuchtePersonenFirmenBehoerden), asDouble(kmFahrtBeginn), asDouble(kmFahrtEnde),
                asDouble(kmGeschaeftlich), asDouble(kmPrivat), asDouble(kmWohnArbeit), asDouble(kraftstoffLiter), asDouble(kraftstoffBetrag),
                asDouble(literPro100km), asDouble(sonstigesBetrag), "", UserManager.getInstance().getUser().getBenutzername(),
                false, spinner.getSelectedItem().toString());
        fahrt.setStartAdresse(startAdresse);
        fahrt.setZielAdresse(endAdresse);
        return fahrt;
    }

    /**
     * Setzt die OnClickListener auf die Adresse-Erfassen-Buttons
     * Die Warnung wird hier Suppressed da die Permissions Abfrage an anderer Stelle stattfindet
     */
    @SuppressLint("MissingPermission")
    private void setAdresseErfassenListener() {
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        AddressButton.setOnClickListener(click -> createGpsThread(locationManager, Lat, Lon, StartStreetText, StartNumberText, StartPLZText, StartOrtText, locationListener, requireContext()).start());
        EndAddressButton.setOnClickListener(click -> createGpsThread(locationManager, Lat, Lon, EndStreetText, EndNumberText, EndPLZText, EndOrtText, locationListener, requireContext()).start());
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] ergebnis) {
        if (requestCode == 1) {
            if (ergebnis.length > 0 && ergebnis[0] == PackageManager.PERMISSION_GRANTED) {
                setAdresseErfassenListener();
            } else {
                createNoGpsPermissionBenachrichtigung();
            }
        }
        showLongToast("requestCode " + requestCode);
    }

    private void openGoogleMapsNavigation(EditText endStreetText, EditText endNumberText, EditText endPLZText, EditText endOrtText) {
        String zielAdresse = endStreetText.getText() + " " + endNumberText.getText() + " " + endPLZText.getText() + " " + endOrtText.getText();
        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + zielAdresse);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    private void setKoordinaten(AtomicReference<Double> lat, AtomicReference<Double> lon, Location location) {
        lat.set(location.getLatitude());
        lon.set(location.getLongitude());
    }

    private Thread createGpsThread(LocationManager locationManager, AtomicReference<Double> lat, AtomicReference<Double> lon, EditText endStreetText,
                                   EditText endNumberText, EditText endPLZText, EditText endOrtText, LocationListener locationListener, Context context) {
        return new Thread(() -> {
            try {
                if (lat.get() == null && lon.get() == null) {
                    getActivity().runOnUiThread(() -> Toast.makeText(context, "Lade GPS Signal ...", Toast.LENGTH_SHORT).show());
                } else {
                    holeUndSetzeAdresse(locationManager, lat, lon, endStreetText, endNumberText, endPLZText, endOrtText, locationListener);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void holeUndSetzeAdresse(LocationManager locationManager, AtomicReference<Double> lat, AtomicReference<Double> lon, EditText endStreetText, EditText endNumberText, EditText endPLZText, EditText endOrtText, LocationListener locationListener) {
        //TODO: Permissionabfrage
        locationManager.requestLocationUpdates(locationManager.getBestProvider(new Criteria(), true), 0, 0, locationListener);
        Coordinates NewAddress = new Coordinates(lat.get(), lon.get());
        endStreetText.setText(NewAddress.getStreet());
        endNumberText.setText(String.valueOf(NewAddress.getNumber()));
        endPLZText.setText(NewAddress.getCode());
        endOrtText.setText(NewAddress.getCity());
        getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), "Position wurde erfolgreich ermittelt", Toast.LENGTH_SHORT).show());
    }

    private DatePickerDialog createDatePickerDialog(Calendar rightNow, TextView textView) {
        DatePickerDialog.OnDateSetListener listener = (datePicker, year, month, day) -> textView.setText(formatDateAsString(day, month, year));
        return new DatePickerDialog(getActivity(), listener, getYear(rightNow), getMonth(rightNow), getDay(rightNow));
    }

    private final void createSpinnerAdapter(Spinner spinner, List<String> elemente) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, elemente);
        spinner.setAdapter(adapter);
    }

    private final TimePickerDialog createTimepickerDialog(Calendar rightNow, TextView textView) {
        TimePickerDialog.OnTimeSetListener listener = (timePicker, hourOfDay, minutes) -> textView.setText(formatTimeAsString(hourOfDay, minutes));
        return new TimePickerDialog(getActivity(), listener, rightNow.get(Calendar.HOUR_OF_DAY), rightNow.get(Calendar.MINUTE), true);
    }

    /**
     * Gibt die Zeit zurück im Format HH:MM
     * @param cal
     * @return die Zeit als String
     */
    private String formatTimeAsString(Calendar cal){
        return String.format("%02d:%02d", cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE));
    }

    private String formatTimeAsString(int hour, int minute){
        return String.format("%02d:%02d", hour, minute);
    }

    private String formatDateAsString(Calendar cal){
        return String.format("%02d.%02d.%04d", getDay(cal), getMonth(cal),  getYear(cal));
    }

    private String formatDateAsString(int day, int month, int year){
        return String.format("%02d.%02d.%04d", day, month + 1, year);
    }

    private int getDay(Calendar calendar){
        return calendar.get(Calendar.DAY_OF_MONTH);
    }
    private int getMonth(Calendar calendar){
        return calendar.get(Calendar.MONTH) + 1;
    }
    private int getYear(Calendar calendar){
        return calendar.get(Calendar.YEAR);
    }

    private void showLongToast(String text){
        getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show());
    }

    private void createNoGpsPermissionBenachrichtigung(){
        PendingIntent pi = PendingIntent.getActivity(getActivity(), 0, new Intent(getActivity(), MainActivity2.class), PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getActivity(), MainActivity2.BENACHRICHTIGUNG_CHANNEL_ID)
                .setSmallIcon(R.drawable.error)
                .setContentTitle("eFahrtenbuch")
                .setContentText(getString(R.string.notificationNoLocationPermission))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(getString(R.string.notificationNoLocationPermission)))
                .setAutoCancel(true)
                .setContentIntent(pi);
        ((NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE)).notify(0, mBuilder.build());
    }
    private static String string(EditText textfeld){
        return textfeld.getText().toString();
    }
    private static double asDouble(EditText textfeld){
       return Double.parseDouble(string(textfeld));
    }
    private static java.util.Date parseDate(String s){
        try {
            return new SimpleDateFormat("dd.MM.yyyy").parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    private static java.util.Date parseTimeAndDate(String date, String time){
        LocalTime localTime = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"));
        int hour = localTime.get(ChronoField.CLOCK_HOUR_OF_DAY);
        int minute = localTime.get(ChronoField.MINUTE_OF_HOUR);
        java.util.Date Date = parseDate(date);
        Date.setHours(hour);
        Date.setMinutes(minute);
        return Date;
    }
}