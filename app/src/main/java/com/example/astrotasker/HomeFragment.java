package com.example.astrotasker;// HomeFragment.java
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
//import com.example.astrotasker.Task;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


// ... (Your imports)

public class HomeFragment extends Fragment {

    private EditText editTextTask;
    private Spinner spinnerCategories;
    private Button btnAddTask;
    private RadioGroup radioGroupTasks;
    private RadioGroup radioGroupCompletedTasks;
    private TextView textViewToDo;
    private TextView homeUsernameTV;
    private TextView homeCurrentLevelTV;
    private TextView homeNextLevelTV;
    private ImageView homeProfilePhotoIV;
    private ProgressBar homeLevelProgressBar;
    private String uid;
    private String username;
    private int imageNum;
    private int imageID;
    private int level;
    private int xp;

    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference tasksReference;
    private DatabaseReference completedTasksReference;
    private DatabaseReference levelReference;
    private DatabaseReference xpReference;
    private DatabaseReference reference;
    private DatabaseReference userReference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        uid = currentUser.getUid();
        reference = database.getReference();
        userReference = reference.child("Users").child(uid);
        tasksReference = userReference.child("Tasks");
        completedTasksReference = userReference.child("CompletedTasks");
        levelReference = userReference.child("level");
        xpReference = userReference.child("xp");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        editTextTask = view.findViewById(R.id.editTextTask);
        spinnerCategories = view.findViewById(R.id.spinnerCategories);
        btnAddTask = view.findViewById(R.id.btnAddTask);
        radioGroupTasks = view.findViewById(R.id.radioGroupTasks);
        //radioGroupTasks.s
        radioGroupCompletedTasks = view.findViewById(R.id.radioGroupCompletedTasks);
        textViewToDo = view.findViewById(R.id.textViewToDo);
        homeUsernameTV = view.findViewById(R.id.homeUsernameTV);
        homeCurrentLevelTV = view.findViewById(R.id.homeCurrentLevelTV);
        homeNextLevelTV = view.findViewById(R.id.homeNextLevelTV);
        homeProfilePhotoIV = view.findViewById(R.id.homeProfilePhotoIV);
        homeLevelProgressBar = view.findViewById(R.id.homeLevelProgressBar);

        // Load user data and tasks
        getUserInfo();

        // Set up the spinner with categories
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireActivity(),
                R.array.categories,
                R.layout.spinner_dropdown_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategories.setAdapter(adapter);

        btnAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTask();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Load user data and tasks again when the fragment resumes
        getUserInfo();
    }

    private void getUserInfo() {
        reference.child("Users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User user = dataSnapshot.getValue(User.class);
                    // Update UI with user data
                    username = user.getUsername();
                    imageNum = user.getProfilePhoto();
                    imageID = requireContext().getResources().getIdentifier("alien_" + imageNum, "drawable", requireContext().getPackageName());
                    level = user.getLevel();
                    xp = user.getXp();
                    setFields();

                    // Load tasks and completed tasks
                    loadTasks();
                } else {
                    // User data does not exist
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
                Log.i("CANCELLED", "CANCELLED");
            }
        });
    }

    private void setFields() {
        homeUsernameTV.setText(username);
        homeProfilePhotoIV.setImageResource(imageID);
        editTextTask.setTextColor(Color.parseColor("#4CAF50"));
        double requiredXP = 10; // XP required for the first level
        int level = 1;
        double tempXP = xp;

        while (tempXP >= requiredXP) {
            tempXP -= requiredXP;
            requiredXP *= 2;
            level++;
        }

        homeCurrentLevelTV.setText("Level "+level);
        String nextLevelText = "Level "+(level+1);
        homeNextLevelTV.setText(nextLevelText);

        homeLevelProgressBar.setProgress((int)(100*(tempXP/requiredXP)));

        levelReference.setValue(level);
    }

    private void addTask() {
        String taskText = editTextTask.getText().toString().trim();
        String category = spinnerCategories.getSelectedItem().toString();

        if (!taskText.isEmpty()) {
            int xp = getXPForCategory(category);

            // Create and configure RadioButton
            RadioButton radioButton = new RadioButton(requireContext());
            radioButton.setText(String.format("%s (%d XP)", taskText, xp));
            radioButton.setTextColor(Color.WHITE);
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

    private void completeTask(RadioButton radioButton, int taskXP) {
        radioGroupTasks.removeView(radioButton);
        String category = spinnerCategories.getSelectedItem().toString();
        String taskText = radioButton.getText().toString().split(" \\(")[0];
        removeTaskFromTasks(taskText);
        RadioButton completedTaskRadioButton = new RadioButton(requireContext());
        completedTaskRadioButton.setText(String.format("%s", radioButton.getText(), taskXP));
        completedTaskRadioButton.setTextColor(Color.WHITE);
        radioGroupCompletedTasks.addView(completedTaskRadioButton);

        if (radioGroupCompletedTasks.getChildCount() > 5) {
            radioGroupCompletedTasks.removeViewAt(0);
        }

        // Store completed task data under user's "CompletedTasks" subcollection
        Task completedTask = new Task(radioButton.getText().toString(), category, taskXP);
        completedTasksReference.push().setValue(completedTask);

        xp += taskXP;
        xpReference.setValue(xp);

        double requiredXP = 10; // XP required for the first level
        int level = 1;
        double tempXP = xp;

        while (tempXP >= requiredXP) {
            tempXP -= requiredXP;
            requiredXP *= 2;
            level++;
        }

        homeCurrentLevelTV.setText("Level "+level);
        String nextLevelText = "Level "+(level+1);
        homeNextLevelTV.setText(nextLevelText);

        homeLevelProgressBar.setProgress((int)(100*(tempXP/requiredXP)));

        levelReference.setValue(level);
    }
    private void removeTaskFromTasks(final String taskText) {
        // Query to find the task with the given text in the "Tasks" subcollection
        Query query = tasksReference.orderByChild("taskText").equalTo(taskText);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot taskSnapshot : dataSnapshot.getChildren()) {
                    taskSnapshot.getRef().removeValue(); // Remove the task
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
                Log.i("CANCELLED", "CANCELLED");
            }
        });
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
    private void loadTasks() {
        loadIncompleteTasks();
        loadCompletedTasks();
    }

    private void loadIncompleteTasks() {
        // Load TO DO tasks from the "Tasks" subcollection and update UI
        tasksReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                radioGroupTasks.removeAllViews(); // Clear existing tasks
                for (DataSnapshot taskSnapshot : dataSnapshot.getChildren()) {
                    Task task = taskSnapshot.getValue(Task.class);
                    if (task != null) {
                        // Create and configure RadioButton
                        RadioButton radioButton = new RadioButton(requireContext());
                        radioButton.setText(String.format("%s (%d XP)", task.getTaskText(), task.getXp()));
                        radioButton.setTextColor(Color.WHITE);
                        radioButton.setOnClickListener(view -> completeTask(radioButton, task.getXp()));

                        // Add RadioButton to RadioGroup
                        radioGroupTasks.addView(radioButton);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
                Log.i("CANCELLED", "CANCELLED");
            }
        });
    }

    private void loadCompletedTasks() {
        // Load completed tasks from the "CompletedTasks" subcollection and update UI
        completedTasksReference.orderByChild("timestamp").limitToLast(5).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                radioGroupCompletedTasks.removeAllViews(); // Clear existing completed tasks
                List<Task> recentCompletedTasks = new ArrayList<>();

                for (DataSnapshot taskSnapshot : dataSnapshot.getChildren()) {
                    Task completedTask = taskSnapshot.getValue(Task.class);
                    if (completedTask != null) {
                        recentCompletedTasks.add(completedTask);
                    }
                }

                // Display the most recent 5 completed tasks in reverse order
                for (int i = recentCompletedTasks.size() - 1; i >= 0; i--) {
                    Task completedTask = recentCompletedTasks.get(i);
                    // Create and configure RadioButton for completed task
                    RadioButton completedTaskRadioButton = new RadioButton(requireContext());
                    completedTaskRadioButton.setText(String.format("%s", completedTask.getTaskText(), completedTask.getXp()));
                    completedTaskRadioButton.setTextColor(Color.WHITE);

                    // Add RadioButton to RadioGroup
                    radioGroupCompletedTasks.addView(completedTaskRadioButton);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
                Log.i("CANCELLED", "CANCELLED");
            }
        });
    }


}
