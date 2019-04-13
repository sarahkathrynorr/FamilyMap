package com.example.familymapclient;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

import src.Model.Person;
import src.RequestResponse.PersonResponse;
import src.RequestResponse.RequestLogin;
import src.RequestResponse.ResponseLogin;
import src.RequestResponse.UserRequest;
import src.RequestResponse.UserResponse;
import src.ServerProxy;

public class LoginFragment extends Fragment {

    private Button signInButton;
    private Button registerButton;

    private EditText userNameTextEdit;
    private EditText passwordTextEdit;
    private EditText serverPortListener;
    private EditText serverHostListener;
    private EditText firstNameListener;
    private EditText lastNameListener;
    private EditText emailListener;
    private RadioButton genderButton;

    private View v;
    
    private Person user;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_login, container, false);

        signInButton = (Button) v.findViewById(R.id.SignInButton);
        registerButton = (Button) v.findViewById(R.id.RegisterButton);

        signInButton.setEnabled(false);
        registerButton.setEnabled(false);

        //USERNAME LISTENER
        userNameTextEdit = (EditText) v.findViewById(R.id.UserNameText);
        userNameTextEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!((userNameTextEdit.getText().toString()).equals("")
                        || ((passwordTextEdit.getText().toString()).equals("")
                        || ((serverHostListener).getText().toString()).equals("")
                        || ((serverPortListener).getText().toString()).equals("")))) {
                    signInButton.setEnabled(true);
                    signInButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LoginTask loginTask = new LoginTask();
                            loginTask.execute();
                        }
                    });

                    if (!((( emailListener).getText().toString()).equals("")
                            || ((firstNameListener).getText().toString()).equals("")
                            || ((lastNameListener).getText().toString()).equals(""))) {
                        registerButton.setEnabled(true);
                        registerButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                 RegisterTask registerTask = new  RegisterTask();
                                registerTask.execute();
                            }
                        });
                    } else registerButton.setEnabled(false);
                } else {
                    signInButton.setEnabled(false);
                    registerButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        //PASSWORD LISTENER
        passwordTextEdit = (EditText)v.findViewById(R.id.PasswordText);
        passwordTextEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!(((userNameTextEdit).getText().toString()).equals("")
                        || ((passwordTextEdit).getText().toString()).equals("")
                        || ((serverHostListener).getText().toString()).equals("")
                        || ((serverPortListener).getText().toString()).equals(""))) {
                    signInButton.setEnabled(true);
                    signInButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LoginTask loginTask = new  LoginTask();
                            loginTask.execute();
                        }
                    });

                    if (!((( emailListener).getText().toString()).equals("")
                            || (( firstNameListener).getText().toString()).equals("")
                            || (( lastNameListener).getText().toString()).equals(""))) {
                        registerButton.setEnabled(true);
                        registerButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                RegisterTask registerTask = new  RegisterTask();
                                registerTask.execute();
                            }
                        });
                    } else registerButton.setEnabled(false);
                } else {
                    signInButton.setEnabled(false);
                    registerButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        //PORT LISTENER
        serverPortListener = v.findViewById(R.id.ServerPortText);
        serverPortListener.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!(((userNameTextEdit).getText().toString()).equals("")
                        || ((passwordTextEdit).getText().toString()).equals("")
                        || ((serverHostListener).getText().toString()).equals("")
                        || ((serverPortListener).getText().toString()).equals(""))) {
                    signInButton.setEnabled(true);
                    signInButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LoginTask loginTask = new  LoginTask();
                            loginTask.execute();
                        }
                    });

                    if (!((( emailListener).getText().toString()).equals("")
                            || (( firstNameListener).getText().toString()).equals("")
                            || (( lastNameListener).getText().toString()).equals(""))) {
                        registerButton.setEnabled(true);
                        registerButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                RegisterTask registerTask = new  RegisterTask();
                                registerTask.execute();
                            }
                        });
                    } else registerButton.setEnabled(false);
                } else {
                    signInButton.setEnabled(false);
                    registerButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        //HOST LISTENER
        serverHostListener = v.findViewById(R.id.ServerHostText);
        serverHostListener.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!(((userNameTextEdit).getText().toString()).equals("")
                        || ((passwordTextEdit).getText().toString()).equals("")
                        || ((serverHostListener).getText().toString()).equals("")
                        || ((serverPortListener).getText().toString()).equals(""))) {
                    signInButton.setEnabled(true);
                    signInButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LoginTask loginTask = new  LoginTask();
                            loginTask.execute();
                        }
                    });

                    if (!((( emailListener).getText().toString()).equals("")
                            || (( firstNameListener).getText().toString()).equals("")
                            || (( lastNameListener).getText().toString()).equals(""))) {
                        registerButton.setEnabled(true);
                        registerButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                RegisterTask registerTask = new  RegisterTask();
                                registerTask.execute();
                            }
                        });
                    } else registerButton.setEnabled(false);
                } else {
                    signInButton.setEnabled(false);
                    registerButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        //FIRST NAME LISTENER
        firstNameListener =  v.findViewById(R.id.FirstNameText);
        firstNameListener.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!(((userNameTextEdit).getText().toString()).equals("")
                        || ((passwordTextEdit).getText().toString()).equals("")
                        || ((serverHostListener).getText().toString()).equals("")
                        || ((serverPortListener).getText().toString()).equals(""))) {
                    signInButton.setEnabled(true);
                    signInButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LoginTask loginTask = new  LoginTask();
                            loginTask.execute();
                        }
                    });

                    if (!((( emailListener).getText().toString()).equals("")
                            || (( firstNameListener).getText().toString()).equals("")
                            || (( lastNameListener).getText().toString()).equals(""))) {
                        registerButton.setEnabled(true);
                        registerButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                RegisterTask registerTask = new  RegisterTask();
                                registerTask.execute();
                            }
                        });
                    } else registerButton.setEnabled(false);
                } else {
                    signInButton.setEnabled(false);
                    registerButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        //LAST NAME LISTENER
        lastNameListener =  v.findViewById(R.id.LastNameText);
        lastNameListener.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!(((userNameTextEdit).getText().toString()).equals("")
                        || ((passwordTextEdit).getText().toString()).equals("")
                        || ((serverHostListener).getText().toString()).equals("")
                        || ((serverPortListener).getText().toString()).equals(""))) {
                    signInButton.setEnabled(true);
                    signInButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LoginTask loginTask = new  LoginTask();
                            loginTask.execute();
                        }
                    });

                    if (!((( emailListener).getText().toString()).equals("")
                            || (( firstNameListener).getText().toString()).equals("")
                            || (( lastNameListener).getText().toString()).equals(""))) {
                        registerButton.setEnabled(true);
                        registerButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                RegisterTask registerTask = new RegisterTask();
                                registerTask.execute();
                            }
                        });
                    } else registerButton.setEnabled(false);
                } else {
                    signInButton.setEnabled(false);
                    registerButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        //EMAIL LISTENER
        emailListener =  v.findViewById(R.id.EmailText);
        emailListener.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            //TODO change to textEdit variables **** instead of findViewById
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!(((userNameTextEdit.getText().toString()).equals("")
                        || ((passwordTextEdit).getText().toString()).equals("")
                        || ((serverHostListener).getText().toString()).equals("")
                        || ((serverPortListener).getText().toString()).equals("")))) {
                    signInButton.setEnabled(true);
                    signInButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LoginTask loginTask = new LoginTask();
                            loginTask.execute();
                        }
                    });

                    if (!((( emailListener).getText().toString()).equals("")
                            || (( firstNameListener).getText().toString()).equals("")
                            || (( lastNameListener).getText().toString()).equals(""))) {
                        registerButton.setEnabled(true);
                        registerButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                RegisterTask registerTask = new  RegisterTask();
                                registerTask.execute();
                            }
                        });
                    } else registerButton.setEnabled(false);
                } else {
                    signInButton.setEnabled(false);
                    registerButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        return v;
    }

    private class RegisterTask extends AsyncTask<Void, UserResponse, UserResponse> {
        ServerProxy serverProxy = new ServerProxy();

        @Override
        protected UserResponse doInBackground(Void...params){
            //gender
            RadioGroup radioGroup = (RadioGroup) v.findViewById(R.id.Gender);

            // get selected radio button from radioGroup
            int selectedId = radioGroup.getCheckedRadioButtonId();

            // find the radiobutton by returned id
            RadioButton radioButton = (RadioButton) v.findViewById(selectedId);

            String chosenGender = (String) radioButton.getText().subSequence(0, 1);

            serverProxy.setServerHost(serverHostListener.getText().toString());
            serverProxy.setServerPort(serverPortListener.getText().toString());

            return serverProxy.register(new UserRequest(userNameTextEdit.getText().toString(), passwordTextEdit.getText().toString(),
                    emailListener.getText().toString(), firstNameListener.getText().toString(), lastNameListener.getText().toString(),
                    chosenGender));
        }

        @Override
        protected void onPreExecute() {
            System.out.println("onPreExecute for RegisterTask");
        }

        @Override
        protected void onPostExecute(UserResponse user) {
            if (user.getMessage() == null) {
                EditText firstNameText = v.findViewById(R.id.FirstNameText);
                EditText lastNameText = v.findViewById(R.id.LastNameText);

                Toast.makeText( getActivity(),
                        firstNameText.getText() + " " + lastNameText.getText() + " registered",
                        Toast.LENGTH_SHORT).show();

                Model.instance().setAuthToken(user.getAuthToken());
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);

            }
            else {
                Toast.makeText( getActivity(),
                        user.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class LoginTask extends AsyncTask<Void, ResponseLogin, ResponseLogin> {
        ServerProxy serverProxy = new ServerProxy();

        @Override
        protected ResponseLogin doInBackground(Void...params) {
            EditText serverHostText = v.findViewById(R.id.ServerHostText);
            EditText serverPortText = v.findViewById(R.id.ServerPortText);
            EditText userNameText = v.findViewById(R.id.UserNameText);
            EditText passwordText = v.findViewById(R.id.PasswordText);

            serverProxy.setServerHost(serverHostText.getText().toString());
            serverProxy.setServerPort(serverPortText.getText().toString());

            ResponseLogin responseLogin = serverProxy.login(new RequestLogin(userNameText.getText().toString(), passwordText.getText().toString()));
            return responseLogin;
        }

        @Override
        protected void onPostExecute(ResponseLogin responseLogin) {
            if (responseLogin.getMessage() == null) {
                Model.instance().setServerHost(serverHostListener.getText().toString());
                Model.instance().setServerPort(serverPortListener.getText().toString());
                Model.instance().setShowAllMarkers(true);
                SuccessLogin successLogin = new  SuccessLogin();
                try {
                    Model.instance().setUser(user);
                    Model.instance().setAuthToken(responseLogin.getAuthToken());
                    user = successLogin.execute(responseLogin).get().getPerson();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(getActivity(),
                        responseLogin.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class SuccessLogin extends AsyncTask<ResponseLogin, PersonResponse, PersonResponse> {
        ServerProxy serverProxy = new ServerProxy();

        @Override
        protected PersonResponse doInBackground(ResponseLogin... responseLogin) {
            serverProxy.setServerHost(serverHostListener.getText().toString());
            serverProxy.setServerPort(serverPortListener.getText().toString());

            user = serverProxy.getPerson(responseLogin[0].getPersonId(), responseLogin[0].getAuthToken()).getPerson();
            Model.instance().setUser(user);
            return serverProxy.getPerson(responseLogin[0].getPersonId(), responseLogin[0].getAuthToken());
        }

        @Override
        protected void onPostExecute(PersonResponse personResponse) {
            user = personResponse.getPerson();
            if (personResponse.getMessage() == null && personResponse != null) {
                Toast.makeText(getActivity(),
                        personResponse.getPerson().getFirstName() + " " + personResponse.getPerson().getLastName() + " logged in",
                        Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(getActivity(),
                        PersonResponse.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}
