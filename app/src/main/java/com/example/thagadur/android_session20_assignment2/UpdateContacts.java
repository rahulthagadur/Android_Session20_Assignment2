package com.example.thagadur.android_session20_assignment2;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.PhoneLookup;
import android.provider.ContactsContract.RawContacts;
import android.util.Log;


import java.util.ArrayList;

/**
 * Created by Thagadur on 11/4/2017.
 */

public class UpdateContacts {


    public static String TAG = "ContactOperations";

    /**
     * Inserting the Contacts into the contact Telephone Directory
     *
     * @param ctx--------------Context
     * @param nameSurname------SurName
     * @param telephone                -------Insert into the telephone Deirectory
     */
    public static void Insert2Contacts(Context ctx, String nameSurname,
                                       String telephone) {
        if (!isTheNumberExistsinContacts(ctx, telephone)) {
            ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
            int rawContactInsertIndex = ops.size();

            ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null).build());
            ops.add(ContentProviderOperation
                    .newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(
                            ContactsContract.Data.RAW_CONTACT_ID,
                            rawContactInsertIndex)
                    .withValue(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE)
                    .withValue(Phone.NUMBER, telephone).build());
            ops.add(ContentProviderOperation
                    .newInsert(Data.CONTENT_URI)
                    .withValueBackReference(Data.RAW_CONTACT_ID,
                            rawContactInsertIndex)
                    .withValue(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE)
                    .withValue(StructuredName.DISPLAY_NAME, nameSurname)
                    .build());
            try {
                ContentProviderResult[] res = ctx.getContentResolver()
                        .applyBatch(ContactsContract.AUTHORITY, ops);
            } catch (Exception e) {

                Log.d(TAG, e.getMessage());
            }
        }
    }

    /**
     * To check Whether the Given Number Exits or not
     *
     * @param ctx
     * @param phoneNumber----Checking whether the Contact Phone number is present or not
     * @return
     */
    public static boolean isTheNumberExistsinContacts(Context ctx,
                                                      String phoneNumber) {
        Cursor cur = null;
        ContentResolver cr = null;

        try {
            cr = ctx.getContentResolver();

        } catch (Exception ex) {
            Log.d(TAG, ex.getMessage());
        }

        try {
            cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null,
                    null, null);
        } catch (Exception ex) {
            Log.i(TAG, ex.getMessage());
        }

        try {
            if (cur.getCount() > 0) {
                while (cur.moveToNext()) {
                    String id = cur.getString(cur
                            .getColumnIndex(ContactsContract.Contacts._ID));
                    String name = cur
                            .getString(cur
                                    .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    // Log.i("Names", name);
                    if (Integer
                            .parseInt(cur.getString(cur
                                    .getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                        // Query phone here. Covered next
                        Cursor phones = ctx
                                .getContentResolver()
                                .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                        null,
                                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                                + " = " + id, null, null);
                        while (phones.moveToNext()) {
                            String phoneNumberX = phones
                                    .getString(phones
                                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            // Log.i("Number", phoneNumber);

                            phoneNumberX = phoneNumberX.replace(" ", "");
                            phoneNumberX = phoneNumberX.replace("(", "");
                            phoneNumberX = phoneNumberX.replace(")", "");
                            if (phoneNumberX.contains(phoneNumber)) {
                                phones.close();
                                return true;

                            }

                        }
                        phones.close();
                    }

                }
            }
        } catch (Exception ex) {
            Log.i(TAG, ex.getMessage());

        }

        return false;
    }
//    Update the Contact details Based on contact phone number and Contact Name

    /**
     * @param ctx                   Context
     * @param contactName           --Contact Name to be Updated
     * @param newNumber-----Contact Phone number to be Updated
     * @throws RemoteException
     * @throws OperationApplicationException
     */
    public static void updateContact(Context ctx, String contactName, String newNumber) throws RemoteException, OperationApplicationException {

        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        String selectName = ContactsContract.Contacts.DISPLAY_NAME + "=?";
        String[] name = new String[]{contactName};
        ops.add(ContentProviderOperation.newUpdate(Data.CONTENT_URI)
                .withSelection(selectName, name)
                .withValue(Phone.NUMBER, newNumber)
                .build());
        ctx.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
    }

}
