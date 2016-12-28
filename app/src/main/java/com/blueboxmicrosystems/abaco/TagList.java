package com.blueboxmicrosystems.abaco;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.blueboxmicrosystems.abaco.dialog.TagAddDialog;
import com.blueboxmicrosystems.abaco.model.ListAccountModel;
import com.blueboxmicrosystems.abaco.model.ListCategoryModel;
import com.blueboxmicrosystems.abaco.model.ListTagModel;
import com.blueboxmicrosystems.abaco.model.ListViewAdapter;
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
    private Integer currentRecordId;
    AlertDialog tagDialog;
    TagAddDialog createUpdateDialog;
    private ArrayList<String> stringArrayList;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tag_list, container, false);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fabAddTag);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("", "Boton add tag presionado");
                currentRecordId = 0;
                showCreateUpdateDialog(currentRecordId);
            }
        });

        //Find the +1 button
        // mPlusOneButton = (PlusOneButton) view.findViewById(R.id.plus_one_button);
        list = (ListView) view.findViewById(R.id.lvTagList);

        //setData();
        //ArrayAdapter<String> adapter = new ListViewAdapter(getActivity(), R.layout.item_listview, stringArrayList);
        //list.setAdapter(adapter);


        return view;
    }

    public void configureTagList() {

        if (MainActivity.abacoDataBase != null) {

            Cursor c = MainActivity.abacoDataBase.rawQuery("select id,name,description,color from main.tag order by id desc", null);

            final ArrayList<Integer> tagId = new ArrayList<Integer>();
            final ArrayList<Integer> tagImageId = new ArrayList<Integer>();
            final ArrayList<String> tagName = new ArrayList<String>();
            final ArrayList<String> tagDescription = new ArrayList<String>();
            final ArrayList<Integer> tagColorId = new ArrayList<Integer>();

            if (c.moveToFirst()) {
                do {
                    Integer id = c.getInt(0);
                    String name = c.getString(1);
                    String description = c.getString(2);
                    Integer colorId = c.getInt(3);

                    tagId.add(id);
                    tagName.add(name);
                    tagDescription.add(description);
                    tagColorId.add(colorId);
                    tagImageId.add(R.drawable.ic_menu_camera);
                } while (c.moveToNext());

                //ListTagModel adapter = new ListTagModel(getActivity(), tagImageId, tagName, tagDescription);
                ListViewAdapter adapter = new ListViewAdapter(getActivity(), R.layout.item_listview, tagName,tagDescription,tagColorId);
                list.setAdapter(adapter);
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        currentRecordId = tagId.get(position);
                        showCreateUpdateDialog(currentRecordId);
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

    public void showCreateUpdateDialog(Integer id) {
        Bundle args = new Bundle();
        args.putInt("id", id);

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
                //nada por hacer aqui
            }
        });
    }

    private void setData() {
        stringArrayList = new ArrayList<>();
        stringArrayList.add("Quỳnh Trang");
        stringArrayList.add("Hoàng Biên");
        stringArrayList.add("Đức Tuấn");
        stringArrayList.add("Đặng Thành");
        stringArrayList.add("Xuân Lưu");
        stringArrayList.add("Phạm Thanh");
        stringArrayList.add("Kim Kiên");
        stringArrayList.add("Ngô Trang");
        stringArrayList.add("Thanh Ngân");
        stringArrayList.add("Nguyễn Dương");
        stringArrayList.add("Quốc Cường");
        stringArrayList.add("Trần Hà");
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
