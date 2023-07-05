package com.example.AirQualityIndexApp;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.AirQualityIndexApp.databinding.FragmentAqiBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AqiFragment extends Fragment {

    private static final String CITY_KEY = "city"; //key for serializable

    // Instantiating an OkHttpClient object used for efficiency
    private final OkHttpClient client = new OkHttpClient();

    //to hold the current city
    private DataServices.City city;

    public AqiFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param city The city that user has selected.
     * @return A new instance of AqiFragment.
     */
    public static AqiFragment newInstance(DataServices.City city) {
        AqiFragment fragment = new AqiFragment();
        Bundle args = new Bundle();
        args.putSerializable(CITY_KEY, city);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                //get current city
                city = getArguments().getSerializable(CITY_KEY, DataServices.City.class);
            }
        }
    }

    FragmentAqiBinding binding;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAqiBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //set title
        binding.toolbar5.setTitle("Current AQI");
        //set city title to actual city
        binding.textViewCityName.setText(city.toString());

        //call the getWeather() method that gets the real-time current weather from the API
        getWeather();
    }

    /*
     getWeather() Method that retrieves weather data from the OpenWeatherMap API and updates the
     UI with the air quality index and its components for the specified city.
     */
    void getWeather(){
        Log.d("TAG", "getWeather: ");
        //API Key from openweathermap.org
        String API_KEY = "da04bee83a89b8e4e0eb38e04c490308";
        Request request = new Request.Builder()
                .url("https://api.openweathermap.org/data/2.5/air_pollution?lat="+city.getLat()+"&lon="+city.getLon()+"&appid="+ API_KEY)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("TAG", e.toString());
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){
                    assert response.body() != null;
                    String body = response.body().string();
                    Log.d("demo", "onResponse: " + body);

                    try {
                        JSONObject rootJson = new JSONObject(body);
                        JSONArray listJsonArray = rootJson.getJSONArray("list");

                        JSONObject mainAQIJsonObject = listJsonArray.getJSONObject(0);
                        String aqi = mainAQIJsonObject.getJSONObject("main").getString("aqi");
                        JSONObject components = mainAQIJsonObject.getJSONObject("components");
                        String co = components.getString("co");
                        String no = components.getString("no");
                        String no2 = components.getString("no2");
                        String o3 = components.getString("o3");
                        String so2 = components.getString("so2");

                        requireActivity().runOnUiThread(new Runnable() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void run() {
                                int val = Integer.parseInt(aqi);
                                String aqii = "";
                                if (val == 1)
                                    aqii = "Good";
                                else if(val == 2)
                                    aqii = "Fair";
                                else if(val == 3)
                                    aqii = "Moderate";
                                else if(val == 4)
                                    aqii = "Poor";
                                else aqii = "Very Poor";
                                binding.textViewTemp.setText(aqii);
                                binding.textViewTempMax.setText(co + " μg/m^3");
                                binding.textViewTempMin.setText(no + " μg/m^3");
                                binding.textViewDesc.setText(no2 + " μg/m^3");
                                binding.textViewWindSpeed.setText(o3 + " μg/m^3");
                                binding.textViewWindDegree.setText(so2 + " μg/m^3");
                            }
                        });
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }
}