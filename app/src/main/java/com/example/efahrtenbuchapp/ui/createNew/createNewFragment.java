package com.example.efahrtenbuchapp.ui.createNew;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.efahrtenbuchapp.R;

import java.util.concurrent.atomic.AtomicReference;

public class createNewFragment extends Fragment {

    private CreateNewViewModel createNewViewModel;
    EditText StartTimeText;
    EditText StartDateText;
    EditText StartStreetText;
    EditText StartNumberText;
    EditText StartPLZText;
    EditText StartOrtText;
    Button GetAddressButton;
    Button StartNavButton;

    EditText EndTimeText;
    EditText EndDateText;
    EditText EndStreetText;
    EditText EndNumberText;
    EditText EndPLZText;
    EditText EndOrtText;
    Button GetEndAddressButton;

    final AtomicReference<Double> Lat = new AtomicReference<>();
    final AtomicReference<Double> Lon = new AtomicReference<>();

    LocationManager locationManager;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        createNewViewModel =
                ViewModelProviders.of(this).get(CreateNewViewModel.class);
        View root = inflater.inflate(R.layout.fragment_createnew, container, false);
        Calendar rightNow = Calendar.getInstance();


        StartTimeText = (EditText) root.findViewById(R.id.etStartTime);
        StartDateText = (EditText) root.findViewById(R.id.etStartDate);
        GetAddressButton = (Button) root.findViewById(R.id.btGetStartAdress);
        StartStreetText = (EditText) root.findViewById(R.id.etStartStreet);
        StartNumberText = (EditText) root.findViewById(R.id.etStartNr);
        StartPLZText = (EditText) root.findViewById(R.id.etStartPLZ);
        StartOrtText = (EditText) root.findViewById(R.id.etStartOrt);

        EndTimeText = (EditText) root.findViewById(R.id.etEndTime);
        EndDateText = (EditText) root.findViewById(R.id.etEndDate);
        GetEndAddressButton = (Button) root.findViewById(R.id.btGetEndAdress);
        EndStreetText = (EditText) root.findViewById(R.id.etEndStreet);
        EndNumberText = (EditText) root.findViewById(R.id.etEndNr);
        EndPLZText = (EditText) root.findViewById(R.id.etEndPLZ);
        EndOrtText = (EditText) root.findViewById(R.id.etEndOrt);

        StartNavButton = (Button) root.findViewById(R.id.btStartNav);


        StartTimeText.setText(rightNow.get(Calendar.HOUR_OF_DAY) + ":" + rightNow.get(Calendar.MINUTE));
        StartTimeText.setInputType(InputType.TYPE_NULL);
        StartDateText.setText(rightNow.get(Calendar.DAY_OF_MONTH) + "." + rightNow.get(Calendar.MONTH) + "." + rightNow.get(Calendar.YEAR));
        StartDateText.setInputType(InputType.TYPE_NULL);
        EndTimeText.setText(rightNow.get(Calendar.HOUR_OF_DAY) + ":" + rightNow.get(Calendar.MINUTE));
        EndTimeText.setInputType(InputType.TYPE_NULL);
        EndDateText.setText(rightNow.get(Calendar.DAY_OF_MONTH) + "." + rightNow.get(Calendar.MONTH) + "." + rightNow.get(Calendar.YEAR));
        EndDateText.setInputType(InputType.TYPE_NULL);

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        final LocationListener locationListener = new LocationListener() {

            // Wird Aufgerufen, wenn eine neue Position durch den LocationProvider bestimmt wurde
            public void onLocationChanged(Location location) {
                Lat.set(location.getLatitude());
                Lon.set(location.getLongitude());
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);


        final TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                StartTimeText.setText(hourOfDay + ":" + minutes);
            }
        }, rightNow.get(Calendar.HOUR_OF_DAY), rightNow.get(Calendar.MINUTE), true);

        final DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        StartDateText.setText(day + "." + month + "." + year);
                    }
                }, rightNow.get(Calendar.YEAR), rightNow.get(Calendar.MONTH), rightNow.get(Calendar.DAY_OF_MONTH));

       StartTimeText.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               timePickerDialog.show();
           }
       });
       StartDateText.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               datePickerDialog.show();
           }
       });

        final TimePickerDialog timePickerDialogEnd = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                EndTimeText.setText(hourOfDay + ":" + minutes);
            }
        }, rightNow.get(Calendar.HOUR_OF_DAY), rightNow.get(Calendar.MINUTE), true);

        final DatePickerDialog datePickerDialogEnd = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        EndDateText.setText(day + "." + month + "." + year);
                    }
                }, rightNow.get(Calendar.YEAR), rightNow.get(Calendar.MONTH), rightNow.get(Calendar.DAY_OF_MONTH));

        EndTimeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialogEnd.show();
            }
        });
        EndDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialogEnd.show();
            }
        });

       GetAddressButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {


               Thread thread = new Thread(new Runnable() {

                   @Override
                   public void run() {
                       try  {
                           if (Lat.get() == null && Lon.get() == null){
                               Toast ToastGPSSignal = Toast.makeText(getActivity(), "Lade GPS Signal ...", Toast.LENGTH_SHORT);
                               ToastGPSSignal.show();

                           }
                           else {
                               locationManager.requestLocationUpdates(locationManager.getBestProvider(new Criteria(),true),0, 0, locationListener);
                               Coordinates NewAddress = new Coordinates(Lat.get(), Lon.get());
                               StartStreetText.setText(NewAddress.getStreet());
                               StartNumberText.setText(String.valueOf(NewAddress.getNumber()));
                               StartPLZText.setText(NewAddress.getCode());
                               StartOrtText.setText(NewAddress.getCity());
                               System.out.println(Lat.get());
                               System.out.println(Lon.get());
                               Toast ToastGPSErfolgreich = Toast.makeText(getActivity(), "Position wurde erfolgreich ermittelt", Toast.LENGTH_SHORT);
                               ToastGPSErfolgreich.show();
                           }
                       } catch (Exception e) {
                           e.printStackTrace();
                       }
                   }
               });
                   thread.start();
           }

       });

        GetEndAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Thread thread = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try  {
                            if (Lat.get() == null && Lon.get() == null){
                               Toast.makeText(getActivity(), "Lade GPS Signal ...", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                locationManager.requestLocationUpdates(locationManager.getBestProvider(new Criteria(),true),0, 0, locationListener);
                                Coordinates NewAddress = new Coordinates(Lat.get(), Lon.get());
                                EndStreetText.setText(NewAddress.getStreet());
                                EndNumberText.setText(String.valueOf(NewAddress.getNumber()));
                                EndPLZText.setText(NewAddress.getCode());
                                EndOrtText.setText(NewAddress.getCity());
                                System.out.println(Lat.get());
                                System.out.println(Lon.get());
                                Toast.makeText(getActivity(), "Position wurde erfolgreich ermittelt", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();
            }

        });

        StartNavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String TargetAddress = EndStreetText.getText() + " " + EndNumberText.getText() + " " + EndPLZText.getText() + " " + EndOrtText.getText();
                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + TargetAddress);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
                try
                {
                    startActivity(mapIntent);
                }
                catch(ActivityNotFoundException ex)
                {

                }


            }
        });

       return root;
    }

}