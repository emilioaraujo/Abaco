package com.blueboxmicrosystems.abaco;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.blueboxmicrosystems.abaco.controller.entiry.TagController;
import com.blueboxmicrosystems.abaco.dialog.TagAddDialog;
import com.blueboxmicrosystems.abaco.model.ListViewAdapter;
import com.blueboxmicrosystems.abaco.model.entity.Tag;
import com.google.android.gms.plus.PlusOneButton;

import java.util.ArrayList;


/**
 * A fragment with a Google +1 button.
 * Activities that contain this fragment must implement the
 * {@link TagList.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TagList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TagList extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // The request code must be 0 or greater.
    private static final int PLUS_ONE_REQUEST_CODE = 0;
    // The URL to +1.  Must be a valid URL.
    private final String PLUS_ONE_URL = "http://developer.android.com";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private PlusOneButton mPlusOneButton;
    ListView list;
    View view;
    private TagList.OnFragmentInteractionListener mListener;
    private Tag currentRecord;
    AlertDialog tagDialog;
    TagAddDialog createUpdateDialog;
    private ArrayList<String> stringArrayList;
 private final TagController tagController = new TagController(MainActivity.abacoDataBase);
    public TagList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountList.
     */
    // TODO: Rename and change types and number of parameters
    public static TagList newInstance(String param1, String param2) {
        TagList fragment = new TagList();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getActivity().setTitle("Tag List");
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tag_list, container, false);

        //Find the +1 button
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fabAddTag);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentRecord = null;
                showCreateUpdateDialog(currentRecord);
            }
        });

        list = (ListView) view.findViewById(R.id.lvTagList);
        return view;
    }

    // Method to configure the tag list
    public void configureTagList() {
        if (MainActivity.abacoDataBase != null) {
            //Declaring arrays list to store the data
            final ArrayList<Tag> tags;
            try {
                tags = tagController.findAll();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }
            if (tags!=null) {
                ListViewAdapter adapter = new ListViewAdapter(getActivity(), this, R.layout.item_listview, tags);
                list.setAdapter(adapter);
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        currentRecord = tags.get(position);
                        showRecordDetail(tags.get(position));
                    }
                });
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        configureTagList();
        // Refresh the state of the +1 button each time the activity receives focus.
        //mPlusOneButton.initialize(PLUS_ONE_URL, PLUS_ONE_REQUEST_CODE);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }


    public void showRecordDetail(Tag selectedTag) {
        // TODO: Pending to implementation
        Toast.makeText(getContext(), "Record Detail not Implemented", Toast.LENGTH_SHORT).show();
    }

    public void deleteRecord(final Tag selectedTag) {
        new AlertDialog.Builder(getActivity())
                .setTitle("Confirm")
                .setMessage("Do you really want delete: " + selectedTag.getName() + " ?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        try {
                            tagController.delete(selectedTag);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        configureTagList();
                        Toast.makeText(getContext(), "Record Deleted!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }

    public void showCreateUpdateDialog(Tag tag) {
        Bundle args = new Bundle();
        if (tag != null) {
            args.putInt("id", tag.getId());
        }else{
            args.putInt("id", 0);
        }
        createUpdateDialog = new TagAddDialog();
        createUpdateDialog.setArguments(args);
        createUpdateDialog.show(getActivity().getSupportFragmentManager(), "Tag");
        createUpdateDialog.setActionListener(new TagAddDialog.ActionListener() {
            @Override
            public void onSave(Integer id) {
                configureTagList();
            }

            @Override
            public void onCancel() {
                //Nothing to do there
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
