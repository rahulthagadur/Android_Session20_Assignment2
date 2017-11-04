package com.example.thagadur.android_session20_assignment2;

import android.app.Activity;
import android.content.OperationApplicationException;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //contacts added using for updating the contacts
        UpdateContacts.Insert2Contacts(getApplicationContext(),
                "RAVINDRA BABU", "9876543211");
//        Check whether the Contact Exits or Not
        if (UpdateContacts.isTheNumberExistsinContacts(
                getApplicationContext(), "9876543211")) {
            Log.i(UpdateContacts.TAG, "Exists");
            try {
//                Update the Contacts via functions
                UpdateContacts.updateContact(getApplicationContext(), "RAVINDRA BABU", "1234567899");
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (OperationApplicationException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "CONTACT DOES NOT EXIST", Toast.LENGTH_SHORT).show();
        }

        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

}
