package com.example.efahrtenbuchapp.ui.createNew;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.efahrtenbuchapp.R;
import com.example.efahrtenbuchapp.eFahrtenbuch.Adresse;
import com.example.efahrtenbuchapp.eFahrtenbuch.Auto;
import com.example.efahrtenbuchapp.http.json.JSONConverter;
import com.example.efahrtenbuchapp.gps.LocationChangedListener;
import com.example.efahrtenbuchapp.http.HttpRequester;
import com.example.efahrtenbuchapp.http.UrlBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class createNewFragment extends Fragment {

    final static String FEHLERTEXT = "Fehler beim Speichern der Start-Adresse";

    @SuppressLint("MissingPermission")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        LocationManager locationManager;
        CreateNewViewModel createNewViewModel = ViewModelProviders.of(this).get(CreateNewViewModel.class);

        final AtomicReference<Double> Lat = new AtomicReference();
        final AtomicReference<Double> Lon = new AtomicReference();

        View root = inflater.inflate(R.layout.fragment_createnew, container, false);

        //Start Adresse - Textfelder
        EditText StartTimeText = root.findViewById(R.id.etStartTime);
        EditText StartDateText = root.findViewById(R.id.etStartDate);
        EditText StartStreetText = root.findViewById(R.id.etStartStreet);
        EditText StartNumberText = root.findViewById(R.id.etStartNr);
        EditText StartPLZText = root.findViewById(R.id.etStartPLZ);
        EditText StartOrtText = root.findViewById(R.id.etStartOrt);

        //Ziel Adresse - Textfelder
        EditText EndTimeText = root.findViewById(R.id.etEndTime);
        EditText EndDateText = root.findViewById(R.id.etEndDate);
        EditText EndStreetText = root.findViewById(R.id.etEndStreet);
        EditText EndNumberText = root.findViewById(R.id.etEndNr);
        EditText EndPLZText = root.findViewById(R.id.etEndPLZ);
        EditText EndOrtText = root.findViewById(R.id.etEndOrt);

        //Buttons
        Button EndAddressButton = root.findViewById(R.id.btGetEndAdress);
        Button AddressButton = root.findViewById(R.id.btGetStartAdress);
        Button StartNavButton = root.findViewById(R.id.btStartNav);
        Button sendButton = root.findViewById(R.id.btSubmit);
        Calendar rightNow = Calendar.getInstance();

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

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        LocationChangedListener locationListener =  location -> setKoordinaten(Lat, Lon, location);

        //TODO: PERMISSIONS CHECK
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

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

       AddressButton.setOnClickListener(click -> createGpsThread(locationManager, Lat, Lon, StartStreetText, StartNumberText, StartPLZText, StartOrtText, locationListener, root.getContext()).start());
       EndAddressButton.setOnClickListener(click -> createGpsThread(locationManager, Lat, Lon, EndStreetText, EndNumberText, EndPLZText, EndOrtText, locationListener, requireContext()).start());
       StartNavButton.setOnClickListener(click -> openGoogleMapsNavigation(EndStreetText, EndNumberText, EndPLZText, EndOrtText));

       sendButton.setOnClickListener(click -> {
           String startDate = StartDateText.getText().toString();
           String endDate = EndDateText.getText().toString();
           Adresse startAdresse = new Adresse(StartStreetText.getText().toString(), StartNumberText.getText().toString(), StartOrtText.getText().toString(), Integer.parseInt(StartPLZText.getText().toString()), "");
           Adresse endAdresse = new Adresse(EndStreetText.getText().toString(), EndNumberText.getText().toString(), EndOrtText.getText().toString(), Integer.parseInt(EndPLZText.getText().toString()), "");

           AtomicReference<Integer> startAdresseID = new AtomicReference();
           AtomicReference<Integer> endAdresseID = new AtomicReference();
           startAdresse.speichere(getActivity(), responseStartAdresse -> {
                endAdresse.speichere(getActivity(), responseEndAdresse -> {
                    try{
                        startAdresseID.set(Integer.parseInt(responseStartAdresse));
                        endAdresseID.set(Integer.parseInt(responseEndAdresse));
                        if(startAdresseID.get().intValue() >= 0 && endAdresseID.get().intValue() >= 0){
                            showLongToast("ERFOLGREICH DIIIIIGGA");
                                       /* Fahrt fahrt = new Fahrt(-1, Date.valueOf(startDate), Date.valueOf(endDate), java.util.Date.parse(startDate + StartTimeText.getText().toString()),
                   java.util.Date.parse(startDate + EndTimeText.getText().toString()), );
                    /*public Fahrt(int id, Date fahrtBeginnDatum, Date fahrtEndeDatum, Date fahrtBeginnZeit, Date fahrtEndeZeit, int adresseStartId,
			int adresseZielId, String reisezweck, String reiseroute, String beuchtePersonenFirmenBehoerden, double kmFahrtBeginn, double kmFahrtEnde,
			double kmGeschaeftlich, double kmPrivat, double kmWohnArbeit, double kraftstoffLiter, double kraftstoffBetrag,
			double literPro100km, double sonstigesBetrag, String sonstigesBezeichnung, String fahrerName, boolean edited, String kennzeichen) {*/
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
       });

       Spinner spinner = root.findViewById(R.id.spinner);
        int userid =  -1;//UserManager.getInstance().getUser().getId();
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
     * Gibt das volle Datum im Format DD.MM.YYYY aus
     * @param cal
     * @return das Datum als String
     */
    private String getFullDateAsString(Calendar cal){
        return getDay(cal) + "." + getMonth(cal) + "." + getYear(cal);
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
        Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show();
    }
}