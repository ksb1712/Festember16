package com.festember.festember16;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;

/**
 * Created by vishnu on 8/9/16.
 */
public class ContactsFragment extends Fragment {

    public static final String ID = "ID";
    public static final String[] PERMISSIONS = {
            Manifest.permission.CALL_PHONE
    };
    public static final int PERMISSION_REQUEST_CODE = 1000;

    private boolean isPermissionGranted = true;


    TextView contact1;

    TextView contact2;

    public static ContactsFragment getInstance(int id){

        ContactsFragment fragment = new ContactsFragment();
        Bundle args = new Bundle();
        args.putInt(ID, id);
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.contacts_layout, container, false);
        contact1 = (TextView)view.findViewById(R.id.contact1);
        contact2 = (TextView)view.findViewById(R.id.contact2);

        ButterKnife.inject(this, view);

        if(ActivityCompat.checkSelfPermission(getActivity(), PERMISSIONS[0]) !=
                PackageManager.PERMISSION_GRANTED)
        {
            isPermissionGranted = false;

            ActivityCompat.requestPermissions(
                    getActivity(),
                    PERMISSIONS,
                    PERMISSION_REQUEST_CODE
            );
        }


        Bundle bundle = getArguments();

        if(bundle!=null){
            //Todo: get the contact numbers and assign them to the respective textViews
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        contact1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callNumber(contact1.getText().toString());
            }
        });

        contact2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callNumber(contact2.getText().toString());
            }
        });
    }

    private void callNumber(String number) {
        if(isPermissionGranted){
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + number));
            startActivity(intent);
        }

        else{
            Toast.makeText(getActivity(), "Enable permissions to make calls", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){

            case PERMISSION_REQUEST_CODE:

                if(grantResults.length>0){
                    if(grantResults[0]== PackageManager.PERMISSION_GRANTED)
                        isPermissionGranted = true;
                }

                break;
        }
    }
}
