package com.example.ajaym.ksoapexample;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {
    EditText textBox;
    Button button;
    TextView text;
    public static final MediaType SOAP_MEDIA_TYPE = MediaType.parse("text/xml");

    String soap_string = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
            "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
            "  <soap:Body>\n" +
            "    <CelsiusToFahrenheit xmlns=\"https://www.w3schools.com/xml/\">\n" +
            "      <Celsius>30</Celsius>\n" +
            "    </CelsiusToFahrenheit>\n" +
            "  </soap:Body>\n" +
            "</soap:Envelope>";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xmlReqeust();
            }
        });
    }

    //you should do this process on Async Task -- to make it work on background
    public void xmlReqeust() {


        final OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(SOAP_MEDIA_TYPE, soap_string);

        final Request request = new Request.Builder()
                .url("https://www.w3schools.com/xml/tempconvert.asmx?WSDL")
                .post(body)
                .addHeader("Content-Type", "text/xml")
                .addHeader("SOAPAction", "https://www.w3schools.com/xml/CelsiusToFahrenheit")
                .addHeader("Host", "https://www.w3schools.com")
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                String mMessage = e.getMessage().toString();
                Log.w("failure Response", mMessage);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String mMessage = response.body().string();

                //code = response.code();  //you gettig the resposne on the mMessage --parse it as you want simple
                getResponse(mMessage, response);
            }
        });

    }

    Document parse;

    public void getResponse(String response, Response mainRes){

        DocumentBuilder newDocumentBuilder =
                null;
        try {
            newDocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        try {
            parse = newDocumentBuilder.parse(new
                    ByteArrayInputStream(response.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

        String dataString = parse.getElementsByTagName
                ("CelsiusToFahrenheitResult").item(0).getTextContent();
    }

}