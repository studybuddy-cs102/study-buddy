package com.cs102.studybuddy.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.Toast;

import com.cs102.studybuddy.core.Match;
import com.cs102.studybuddy.core.User;
import com.cs102.studybuddy.databinding.MatchListLayoutBinding;
import com.cs102.studybuddy.listeners.MatchClickListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class MatchListView extends ScrollView {
    private final TableLayout matchTable;
    private final HashMap<String, Match> matchList;

    private MatchClickListener matchClickListener;

    public MatchListView(Context context, AttributeSet attrs) {
        super(context, attrs);

        matchList = new HashMap<>();

        matchTable = new TableLayout(context);
        matchTable.setStretchAllColumns(true);
        matchTable.setLayoutParams(
            new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT
            )
        );
        addView(matchTable);
    }

    public TableLayout getMatchTable() { return matchTable; }

    public void setMatchClickListener (MatchClickListener matchClickListener) {
        this.matchClickListener = matchClickListener;
    }

    public void createMatchView(Match match) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        MatchListLayoutBinding binding = MatchListLayoutBinding.inflate(inflater);
        binding.setMatch(match);
        binding.setCallback(matchClickListener);

        View courseView = binding.getRoot();
        matchTable.addView(courseView);
    }

    // Add a course to the list
    public void addMatch(Match match) {
        if (matchList.containsKey(match.getDocID())) return;
        matchList.put(match.getDocID(), match);
    }

    public void removeMatch(Match m) {
        matchList.remove(m.getDocID());
        matchTable.removeAllViews();
        for (Match match : matchList.values()) {
            createMatchView(match);
        }
    }

    //     Create course list from user's courses
    public void populateMatchList(User u) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("matches")
            .get()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        for (Object match : document.getData().values()) {
                            HashMap<String, Boolean> members = new HashMap<>();
                            members.put(u.getUsername(),true);
                            String member = members.toString();
                            member = members.toString().substring(1,member.length()-1);
                            if (match.toString().contains(member)) {
                                addMatch(document.toObject(Match.class));
                                createMatchView(document.toObject(Match.class));
                            }

                        }
                    }
                } else {
                    Toast.makeText(
                        getContext(),
                        "Failed to fetch match list",
                        Toast.LENGTH_LONG
                    ).show();
                }
            });
    }
}
