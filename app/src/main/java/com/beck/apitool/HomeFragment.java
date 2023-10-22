package com.beck.apitool;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.beck.apitool.databinding.HomeFragmentBinding;

public class HomeFragment extends Fragment {
    private HomeFragmentBinding binding;
    private MainViewModel viewModel;
    private EditText urlInput;
    private Observer<Boolean> statusObserver;
    private Button sendButton;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState
    ) {
        binding = HomeFragmentBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(this);
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        binding.setViewModel(viewModel);

        sendButton = binding.sendButton;
        urlInput = binding.urlInput;

        statusObserver = status -> {
            sendButton.setEnabled(!status);
            urlInput.setEnabled(!status);
        };

        Spinner spinner = binding.methodSpinner;
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this.requireContext(),
                R.array.method_array,
                R.layout.method_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel.isLoading().observe(getViewLifecycleOwner(), statusObserver);

        sendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                viewModel.httpRequest("get");
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

