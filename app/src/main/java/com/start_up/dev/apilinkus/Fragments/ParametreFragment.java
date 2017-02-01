package com.start_up.dev.apilinkus.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.start_up.dev.apilinkus.API.APIGetSubscriptionList_Observer;
import com.start_up.dev.apilinkus.API.APILinkUS;
import com.start_up.dev.apilinkus.HomeActivity;
import com.start_up.dev.apilinkus.Model.Authentification;
import com.start_up.dev.apilinkus.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Vignesh on 1/17/2017.
 */

public class ParametreFragment extends Fragment implements CompoundButton.OnCheckedChangeListener, View.OnClickListener,APIGetSubscriptionList_Observer {

    private View parametreView;
    private TextView user_fullname_tv,user_username_tv,remaining_friends_tv,remaining_description_tv;
    private ImageView edit_fullname, edit_username;
    private OnChangeUserInformationListener mCallback;
    private String username,userfullname;
    private SwitchCompat facebook_switchbutton, twitter_switchbutton,gmail_switchbutton, notifications_switchbutton;
    private SwitchCompat sub_package_friend_one_switchbutton,sub_package_friend_two_switchbutton,sub_package_description_one_switchbutton,sub_package_description_two_switchbutton;
    private final String TAG = ParametreFragment.class.getSimpleName();
    private static boolean cancel_action = false;
    private LinearLayout report_pb_layout,privacy_policy_layout;
    private String free_friends, free_description;
    private boolean sub_des_one = false, sub_des_two = false, sub_frd_one = false, sub_frd_two = false;
    private APILinkUS api;

    public interface OnChangeUserInformationListener{
        void onChangeUserInformation(String key, String[] value);
        void onUpdateSubscription(String type, int length, String unit);
        void onDeleteSubscription(String type, int length, String unit);
        void onPressReportaProblemPage();
        void onPressPrivacyPolicyPage();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        parametreView = inflater.inflate(R.layout.settings_fragment_layout,container,false);
        user_fullname_tv = (TextView) parametreView.findViewById(R.id.user_fullname_tv);
        user_username_tv = (TextView) parametreView.findViewById(R.id.user_username_tv);
        edit_fullname = (ImageView) parametreView.findViewById(R.id.edit_fullname);
        edit_username = (ImageView) parametreView.findViewById(R.id.edit_username);
        facebook_switchbutton = (SwitchCompat) parametreView.findViewById(R.id.facebook_switchbutton);
        twitter_switchbutton = (SwitchCompat) parametreView.findViewById(R.id.twitter_switchbutton);
        gmail_switchbutton = (SwitchCompat) parametreView.findViewById(R.id.gmail_switchbutton);
        notifications_switchbutton = (SwitchCompat) parametreView.findViewById(R.id.notifications_switchbutton);
        sub_package_friend_one_switchbutton = (SwitchCompat) parametreView.findViewById(R.id.sub_package_friend_one_switchbutton);
        sub_package_friend_two_switchbutton = (SwitchCompat) parametreView.findViewById(R.id.sub_package_friend_two_switchbutton);
        sub_package_description_one_switchbutton = (SwitchCompat) parametreView.findViewById(R.id.sub_package_description_one_switchbutton);
        sub_package_description_two_switchbutton = (SwitchCompat) parametreView.findViewById(R.id.sub_package_description_two_switchbutton);
        report_pb_layout = (LinearLayout) parametreView.findViewById(R.id.report_pb_layout);
        privacy_policy_layout = (LinearLayout) parametreView.findViewById(R.id.privacy_policy_layout);
        remaining_friends_tv = (TextView) parametreView.findViewById(R.id.remaining_friends_tv);
        remaining_description_tv = (TextView) parametreView.findViewById(R.id.remaining_description_tv);
        api = new APILinkUS();

        return parametreView;
    }


    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        user_fullname_tv.setText(Authentification.getLastName()+ " "+Authentification.getFirstName());
        user_username_tv.setText(Authentification.getUserName());

        if (savedInstanceState != null) {
            // Restore last state
            Log.d(TAG,"Restore last state " + savedInstanceState.getString("Full name"));

            remaining_description_tv.setText(savedInstanceState.getString("nb_desc_remaining"));
            remaining_friends_tv.setText(savedInstanceState.getString("nb_friends_remaining"));
            sub_frd_one = savedInstanceState.getBoolean("sub_frd_one");
            sub_des_one =  savedInstanceState.getBoolean("sub_des_one");
            sub_frd_two = savedInstanceState.getBoolean("sub_frd_two");
            sub_des_two = savedInstanceState.getBoolean("sub_des_two");

            if(!sub_frd_one && !sub_frd_two){sub_package_friend_one_switchbutton.setChecked(false); sub_package_friend_two_switchbutton.setChecked(false);}
            else if(sub_frd_one){sub_package_friend_one_switchbutton.setChecked(true); sub_package_friend_two_switchbutton.setEnabled(false);}
            else{sub_package_friend_two_switchbutton.setChecked(true); sub_package_friend_one_switchbutton.setEnabled(false);}
            if(!sub_des_one && !sub_des_two){sub_package_description_one_switchbutton.setChecked(false); sub_package_description_two_switchbutton.setChecked(false);}
            else if(sub_des_one){sub_package_description_one_switchbutton.setChecked(true); sub_package_description_two_switchbutton.setEnabled(false);}
            else{sub_package_description_two_switchbutton.setChecked(true); sub_package_description_one_switchbutton.setEnabled(false);}


        } else {
            api.getSubscriptionList(this);
        }

        userfullname = String.valueOf(user_fullname_tv.getText());
        username = String.valueOf(user_username_tv.getText());
        free_description = String.valueOf(remaining_description_tv.getText());
        free_friends = String.valueOf(remaining_friends_tv.getText());


        edit_fullname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                final EditText edittext = new EditText(getContext());
                alert.setMessage("Enter your lastname followed by a space and your firstname");
                alert.setTitle("Edit Fullname");


                alert.setView(edittext);
                alert.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String fullname_edited =  edittext.getText().toString();
                        String firstName = fullname_edited.split(" ")[1];
                        String lastName = fullname_edited.split(" ")[0];

                        if( lastName != null && firstName != null){
                            String first_letter = String.valueOf(firstName.charAt(0)).toUpperCase();
                            firstName = firstName.toLowerCase().replaceFirst(String.valueOf(firstName.charAt(0)),first_letter);
                            user_fullname_tv.setText(lastName.toUpperCase() + " " + firstName);
                            String[] tab = new String[2]; tab[0] = lastName.toUpperCase(); tab[1] = firstName;
                            userfullname = tab[0] + " " + tab[1];
                            mCallback.onChangeUserInformation("Fullname",tab);
                        }else{
                            Toast.makeText(getContext(),"Merci de respecter la syntaxe suivante : nom prenom",Toast.LENGTH_LONG).show();
                        }

                    }
                });

                alert.setNegativeButton("Non", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // what ever you want to do with No option.
                    }
                });

                alert.show();
            }
        });

        edit_username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                final EditText edittext = new EditText(getContext());
                alert.setMessage("Enter your new username");
                alert.setTitle("Edit Username");

                alert.setView(edittext);
                alert.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        EmailValidator validator = new EmailValidator();
                        String username_edited =  edittext.getText().toString();
                        if(validator.validate(username_edited)){
                            user_username_tv.setText(username_edited);
                            String[] tab = new String[1]; tab[0] = username_edited;
                            username = tab[0];
                            mCallback.onChangeUserInformation("Username",tab);
                        }else{
                            Toast.makeText(getContext(),"Merci de respecter la syntaxe suivante : xxxx@xxxx.com",Toast.LENGTH_LONG).show();
                        }

                    }
                });

                alert.setNegativeButton("Non", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // what ever you want to do with No option.
                    }
                });

                alert.show();
            }
        });


        report_pb_layout.setOnClickListener(this);
        privacy_policy_layout.setOnClickListener(this);
        sub_package_friend_one_switchbutton.setOnCheckedChangeListener(this);
        sub_package_friend_two_switchbutton.setOnCheckedChangeListener(this);
        sub_package_description_one_switchbutton.setOnCheckedChangeListener(this);
        sub_package_description_two_switchbutton.setOnCheckedChangeListener(this);
        checkStatusofSocialNetworks();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.report_pb_layout:
                mCallback.onPressReportaProblemPage();
                break;
            case R.id.privacy_policy_layout:
                mCallback.onPressPrivacyPolicyPage();
                break;
        }
    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle("Offres d'abonnement");

        switch (compoundButton.getId()) {

            case R.id.sub_package_friend_one_switchbutton:
                if(!isChecked){
                    if(ParametreFragment.cancel_action == false){
                        Log.d(TAG,"is not checked");
                        alert.setMessage("Souhaitez-vous désabonner de ce service ?");
                        alert.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                sub_package_friend_two_switchbutton.setEnabled(true);
                                mCallback.onDeleteSubscription("FRIEND",1,"YEAR");

                            }
                        });

                        alert.setNegativeButton("Non", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ParametreFragment.cancel_action = true;
                                sub_package_friend_one_switchbutton.setChecked(true);
                                sub_package_friend_two_switchbutton.setEnabled(false);
                            }
                        });
                        alert.show();
                    }else{
                        ParametreFragment.cancel_action = false;
                    }

                }else{
                    if(ParametreFragment.cancel_action == false){
                        Log.d(TAG,"is checked");
                        alert.setMessage("Souhaitez-vous abonner à ce service ?");
                        alert.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                sub_package_friend_two_switchbutton.setEnabled(false);
                                mCallback.onUpdateSubscription("FRIEND", 1, "YEAR");
                            }
                        });

                        alert.setNegativeButton("Non", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ParametreFragment.cancel_action = true;
                                sub_package_friend_one_switchbutton.setChecked(false);
                                sub_package_friend_two_switchbutton.setEnabled(true);
                            }
                        });
                        alert.show();
                    }else{
                        ParametreFragment.cancel_action = false;
                    }
                }
                break;
            /////////////////////////////////////////////
            case R.id.sub_package_friend_two_switchbutton:
                if(!isChecked){
                    if(ParametreFragment.cancel_action == false){
                        alert.setMessage("Souhaitez-vous désabonner de ce service ?");
                        alert.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                sub_package_friend_one_switchbutton.setEnabled(true);
                                mCallback.onDeleteSubscription("FRIEND",6,"MONTH");
                            }
                        });

                        alert.setNegativeButton("Non", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ParametreFragment.cancel_action = true;
                                sub_package_friend_two_switchbutton.setChecked(true);
                                sub_package_friend_one_switchbutton.setEnabled(false);
                            }
                        });
                        alert.show();
                    }else{ ParametreFragment.cancel_action = false; }

                }else{
                    if(ParametreFragment.cancel_action == false){
                        alert.setMessage("Souhaitez-vous abonner à ce service ?");
                        alert.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                sub_package_friend_one_switchbutton.setEnabled(false);
                                mCallback.onDeleteSubscription("FRIEND",6,"MONTH");
                            }
                        });

                        alert.setNegativeButton("Non", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ParametreFragment.cancel_action = true;
                                sub_package_friend_two_switchbutton.setChecked(false);
                                sub_package_friend_one_switchbutton.setEnabled(true);
                            }
                        });
                        alert.show();
                    }else{ ParametreFragment.cancel_action = false; }
                }
                break;
            //////////////////////////////////////////////////
            case R.id.sub_package_description_one_switchbutton:
                if(!isChecked){
                    if(ParametreFragment.cancel_action == false){
                        Log.d(TAG,"is not checked");
                        alert.setMessage("Souhaitez-vous désabonner de ce service ?");
                        alert.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                sub_package_description_two_switchbutton.setEnabled(true);
                                mCallback.onDeleteSubscription("DESCRIPTION",1,"YEAR");

                            }
                        });

                        alert.setNegativeButton("Non", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ParametreFragment.cancel_action = true;
                                sub_package_description_one_switchbutton.setChecked(true);
                                sub_package_description_two_switchbutton.setEnabled(false);
                            }
                        });
                        alert.show();
                    }else{
                        ParametreFragment.cancel_action = false;
                    }

                }else{
                    if(ParametreFragment.cancel_action == false){
                        Log.d(TAG,"is checked");
                        alert.setMessage("Souhaitez-vous abonner à ce service ?");
                        alert.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                sub_package_description_two_switchbutton.setEnabled(false);
                                mCallback.onUpdateSubscription("DESCRIPTION", 1, "YEAR");
                            }
                        });

                        alert.setNegativeButton("Non", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ParametreFragment.cancel_action = true;
                                sub_package_description_one_switchbutton.setChecked(false);
                                sub_package_description_two_switchbutton.setEnabled(true);
                            }
                        });
                        alert.show();
                    }else{
                        ParametreFragment.cancel_action = false;
                    }
                }

                break;
            //////////////////////////////////////////////////
            case R.id.sub_package_description_two_switchbutton:
                if(!isChecked){
                    if(ParametreFragment.cancel_action == false){
                        alert.setMessage("Souhaitez-vous désabonner de ce service ?");
                        alert.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                sub_package_description_one_switchbutton.setEnabled(true);
                                mCallback.onDeleteSubscription("DESCRIPTION",6,"MONTH");
                            }
                        });

                        alert.setNegativeButton("Non", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ParametreFragment.cancel_action = true;
                                sub_package_description_two_switchbutton.setChecked(true);
                                sub_package_description_one_switchbutton.setEnabled(false);
                            }
                        });
                        alert.show();
                    }else{ ParametreFragment.cancel_action = false; }

                }else{
                    if(ParametreFragment.cancel_action == false){
                        alert.setMessage("Souhaitez-vous abonner à ce service ?");
                        alert.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                sub_package_description_one_switchbutton.setEnabled(false);
                                mCallback.onDeleteSubscription("DESCRIPTION",6,"MONTH");
                            }
                        });

                        alert.setNegativeButton("Non", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ParametreFragment.cancel_action = true;
                                sub_package_description_two_switchbutton.setChecked(false);
                                sub_package_description_one_switchbutton.setEnabled(true);
                            }
                        });
                        alert.show();
                    }else{ ParametreFragment.cancel_action = false; }
                }
                break;
        }
    }

    public void checkStatusofSocialNetworks(){

        facebook_switchbutton.setChecked(false);
        twitter_switchbutton.setChecked(false);
        gmail_switchbutton.setChecked(false);

        switch(Authentification.getMode_auth()){
            case "facebook":
                facebook_switchbutton.setChecked(true);
                break;
            case "twitter":
                twitter_switchbutton.setChecked(true);
                break;
            case "gmail":
                gmail_switchbutton.setChecked(true);
                break;
            default:
                facebook_switchbutton.setEnabled(false);
                twitter_switchbutton.setEnabled(false);
                gmail_switchbutton.setEnabled(false);
                break;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("Username", username);
        outState.putString("Full name", userfullname);
        outState.putString("nb_friends_remaining",free_friends);
        outState.putString("nb_desc_remaining",free_description);
        outState.putBoolean("sub_frd_one",sub_frd_one);
        outState.putBoolean("sub_frd_two",sub_frd_two);
        outState.putBoolean("sub_des_one",sub_des_one);
        outState.putBoolean("sub_des_two",sub_des_two);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (ParametreFragment.OnChangeUserInformationListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    /*Intéressant récupérer ici https://www.mkyong.com/regular-expressions/how-to-validate-email-address-with-regular-expression/*/
    public class EmailValidator {

        private Pattern pattern;
        private Matcher matcher;

        private static final String EMAIL_PATTERN =
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        public EmailValidator() {
            pattern = Pattern.compile(EMAIL_PATTERN);
        }

        /**
         * Validate hex with regular expression
         *
         * @param hex
         *            hex for validation
         * @return true valid hex, false invalid hex
         */
        public boolean validate(final String hex) {

            matcher = pattern.matcher(hex);
            return matcher.matches();

        }
    }

    @Override
    public void subscriptionList_GetResponse(JSONArray responseArray) {
        String datefin = "";
        try {

            JSONObject friendsObject = responseArray.optJSONObject(0);
            free_friends = friendsObject.getString("free");
            datefin = friendsObject.getString("dateFin");
            if(datefin!=null){
                // format de la date est mauvaise
            }

            JSONObject descriptionObject = responseArray.optJSONObject(1);
            free_description = descriptionObject.getString("free");
            datefin = descriptionObject.getString("dateFin");
            if(datefin!=null){
                // format de la date est mauvaise
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void subscriptionList_NotifyWhenGetFinish(Integer result) {
        if(result==1){
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    remaining_friends_tv.setText(free_friends);
                    remaining_description_tv.setText(free_description);
                    if(sub_des_one){sub_package_description_one_switchbutton.setChecked(true); sub_package_description_two_switchbutton.setEnabled(true);}
                    if(sub_des_two){sub_package_description_two_switchbutton.setChecked(true); sub_package_description_one_switchbutton.setEnabled(true);}
                    if(sub_frd_one){sub_package_friend_one_switchbutton.setChecked(true); sub_package_friend_two_switchbutton.setEnabled(true);}
                    if(sub_frd_two){sub_package_friend_two_switchbutton.setChecked(true); sub_package_friend_one_switchbutton.setEnabled(true);}
                }
            });
        }else{

        }
    }
}