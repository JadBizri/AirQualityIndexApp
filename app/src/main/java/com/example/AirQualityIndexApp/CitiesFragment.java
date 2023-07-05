package com.example.AirQualityIndexApp;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.AirQualityIndexApp.databinding.FragmentCitiesBinding;

import java.util.ArrayList;


public class CitiesFragment extends Fragment {

    FragmentCitiesBinding binding;
    ArrayList<DataServices.City> cities = DataServices.cities;
    ArrayAdapter<DataServices.City> adapter;

    public CitiesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCitiesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.toolbar.setTitle("Cities");

        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, cities);
        binding.listView.setAdapter(adapter);

        //on click of any item, take that item and send it to next fragment (item is the city in this case)
        binding.listView.setOnItemClickListener((parent, view1, position, id) -> {
            DataServices.City city = cities.get(position);
            mListener.sendSelectedCity(city);
        });
    }

    CitiesListener mListener;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (CitiesListener) context;
    }

    interface CitiesListener{
        void sendSelectedCity(DataServices.City city);
    }
}