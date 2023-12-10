package com.example.astrotasker;// HomeFragment.java
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
//import com.example.astrotasker.Task;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class HomeFragment extends Fragment {

    private EditText editTextTask;
    private Spinner spinnerCategories;
    private Button btnAddTask;
    private RadioGroup radioGroupTasks;
    private RadioGroup radioGroupCompletedTasks;
    private TextView textViewToDo;
    String email;

    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference tasksReference;
    private DatabaseReference completedTasksReference;
    public String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        editTextTask = view.findViewById(R.id.editTextTask);
        spinnerCategories = view.findViewById(R.id.spinnerCategories);
        btnAddTask = view.findViewById(R.id.btnAddTask);
        radioGroupTasks = view.findViewById(R.id.radioGroupTasks);
        radioGroupCompletedTasks = view.findViewById(R.id.radioGroupCompletedTasks);
        textViewToDo = view.findViewById(R.id.textViewToDo);

        // Set up the spinner with categories
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(), R.array.categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategories.setAdapter(adapter);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();


        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        email = currentUser.getEmail();

        if (currentUser != null) {
            userId = currentUser.getUid();

            // Initialize DatabaseReference variables
            DatabaseReference userReference = database.getReference("Users").child(userId);
            tasksReference = userReference.child("Tasks");
            completedTasksReference = userReference.child("CompletedTasks");
        } else {
            // Handle the case when the user is not authenticated
            // You may want to redirect to the login screen or take appropriate action
        }



        btnAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTask();
            }
        });

        return view;
    }

    private void addTask() {
        String taskText = editTextTask.getText().toString().trim();
        String category = spinnerCategories.getSelectedItem().toString();

        if (!taskText.isEmpty()) {
            int xp = getXPForCategory(category);

            // Create and configure RadioButton
            RadioButton radioButton = new RadioButton(requireContext());
            radioButton.setText(String.format("%s (%d XP)", taskText, xp));
            radioButton.setOnClickListener(view -> completeTask(radioButton, xp));

            // Add RadioButton to RadioGroup
            radioGroupTasks.addView(radioButton);
            editTextTask.setText("");

            // Store task data under user's "Tasks" subcollection
            Task task = new Task(taskText, category, xp);
            tasksReference.push().setValue(task);
        } else {
            Toast.makeText(requireContext(), "Task cannot be empty", Toast.LENGTH_SHORT).show();
        }
    }

    private void completeTask(RadioButton radioButton, int xp) {
        radioGroupTasks.removeView(radioButton);
        String category = spinnerCategories.getSelectedItem().toString();
        RadioButton completedTaskRadioButton = new RadioButton(requireContext());
        completedTaskRadioButton.setText(String.format("%s", radioButton.getText(), xp));
        radioGroupCompletedTasks.addView(completedTaskRadioButton);

        if (radioGroupCompletedTasks.getChildCount() > 5) {
            radioGroupCompletedTasks.removeViewAt(0);
        }

        // Store completed task data under user's "CompletedTasks" subcollection
        Task completedTask = new Task(radioButton.getText().toString(), category, xp);
        completedTasksReference.push().setValue(completedTask);
    }


    private int getXPForCategory(String category) {
        // Define XP values for each category
        switch (category) {
            case "Work":
                return 5;
            case "Home":
                return 10;
            case "Learn":
                return 5;
            case "Personal":
                return 20;
            default:
                return 0;
        }
    }
}
