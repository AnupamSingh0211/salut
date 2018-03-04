package hala.com.salut

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import hala.com.salut.models.ChatMessage


/**
 * @author Anupam Singh
 * @version 1.0
 * @since 2018-03-02
 */
class MainActivity : AppCompatActivity() {
    private val SIGN_IN_REQUEST_CODE = 99;
    private val MY_PERMISSIONS_READ_CONTACTS = 121
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        makeFirebaseSignIn()
    }

    private fun getCOntactsPermission() {

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                            android.Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                getContacts()
            } else {
                checkContactsPermission()
            }
        } else {

            getContacts()

        }
    }

    private fun checkContactsPermission() {

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            android.Manifest.permission.READ_CONTACTS)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                AlertDialog.Builder(this)
                        .setTitle("Contacts Permission Needed")
                        .setMessage("This app needs the Contacts permission, please accept to use profile upload functionality")
                        .setPositiveButton("OK") { dialogInterface, i ->
                            //Prompt the user once explanation has been shown
                            ActivityCompat.requestPermissions(this@MainActivity,
                                    arrayOf(Manifest.permission.READ_CONTACTS),
                                    MY_PERMISSIONS_READ_CONTACTS)
                        }
                        .create()
                        .show()


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.READ_CONTACTS),
                        MY_PERMISSIONS_READ_CONTACTS)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSIONS_READ_CONTACTS -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                                    Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {

                        getContacts()
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show()
                    finish()

                }
                return
            }
        }// other 'case' lines to check for other
        // permissions this app might request
    }

    data class PhoneNumbers(val name: String, val number: String)

    private fun makeFirebaseSignIn() {
        if (FirebaseAuth.getInstance().currentUser == null) {
            // Start sign in/sign up activity
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .build(),
                    SIGN_IN_REQUEST_CODE
            )
        } else {
            // User is already signed in. Therefore, display
            // a welcome Toast
            Toast.makeText(this,
                    "Welcome " + FirebaseAuth.getInstance()
                            .currentUser!!
                            .displayName!!,
                    Toast.LENGTH_LONG)
                    .show()

            getCOntactsPermission()
            // Load chat room contents
           // displayChatMessages()
        }
    }


    private fun getContacts() {

        val phoneNumberList: ArrayList<PhoneNumbers> = ArrayList();

        val phones = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null)
        while (phones!!.moveToNext()) {
            val name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
            val phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
            phoneNumberList.add(PhoneNumbers(name, phoneNumber))
        }
        phones.close()

        if (phoneNumberList.size > 0) {

            FirebaseDatabase.getInstance().reference
                    .child("users")
                    .child(FirebaseAuth.getInstance()
                            .currentUser!!.uid)
                    .child("contacts")
                    .setValue(phoneNumberList)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int,
                                  data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SIGN_IN_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(this,
                        "Successfully signed in. Welcome!",
                        Toast.LENGTH_LONG)
                        .show()

                getCOntactsPermission()

            } else {
                Toast.makeText(this,
                        "We couldn't sign you in. Please try again later.",
                        Toast.LENGTH_LONG)
                        .show()

                // Close the app
                finish()
            }
        }

    }


}