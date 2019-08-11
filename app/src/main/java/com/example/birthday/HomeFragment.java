package com.example.birthday;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class HomeFragment extends Fragment {
    RecyclerView recyclerView;
    ContactAdapter  contactAdapter;
    FloatingActionButton addBtn, addConBtn;
    BirthDatabase  birthDatabase;
    Context context;
    ArrayList<ContactModel> arrayList= new ArrayList<>();
    SearchView searchView;

    OnClickDrawerOpen onClickDrawerOpen;


    public interface drawerLayoutChange{
        void sendDrawerInfo(Boolean b);
    }

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        init(v);
        arrayList = birthDatabase.getAllContact();
        Collections.sort(arrayList, new Comparator<ContactModel>() {
            @Override
            public int compare(ContactModel o1, ContactModel o2) {
                int x = o1.getName().compareTo(o2.getName());
                return x;
            }
        });
        loadRecyclerContent(arrayList);

        onClickDrawerOpen = (OnClickDrawerOpen) getActivity();

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.content_area,new AddFragment()).commit();

                Toast.makeText(context,"Add Fragment", Toast.LENGTH_SHORT).show();
            }
        });

        return v;
    }

    private void init(View v) {
        recyclerView = v.findViewById(R.id.birthday_rv);
        addBtn = v.findViewById(R.id.add_btn);

        birthDatabase = new BirthDatabase(context);

    }


    private void loadRecyclerContent(ArrayList<ContactModel> array) {
        contactAdapter = new ContactAdapter(context,array);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(contactAdapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search,menu);

        MenuItem item = menu.findItem(R.id.search_bar);
        if(item != null){
            searchView = (SearchView) item.getActionView();
        }

        if(searchView != null){
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    filter(newText,arrayList);
                    return true;
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.search_bar:
                Toast.makeText(context,"SearchBar: "+item.getItemId(),Toast.LENGTH_SHORT).show();
                return true;
            case  R.id.clear:

                Toast.makeText(context,"Clear"+item.getItemId(),Toast.LENGTH_SHORT).show();
                return true;

            case  R.id.exit:
                getActivity().finish();

                Toast.makeText(context,"Exit ",Toast.LENGTH_SHORT).show();
                return true;

            default:
                onClickDrawerOpen.openNavDrawer(true);
                Toast.makeText(context,"Default: "+item.getItemId(),Toast.LENGTH_SHORT).show();
                return true;
        }
    }

    private void filter(String str, ArrayList<ContactModel> allContact) {
        ArrayList<ContactModel> contacts = new ArrayList<>();
        for(ContactModel contactModel: allContact){
            if(contactModel.getName().toLowerCase().contains(str.toLowerCase())  || contactModel.getContact().toLowerCase().contains(str.toLowerCase())){
                contacts.add(contactModel);
            }
        }
        contactAdapter.filterList(contacts);
    }

    public interface OnClickDrawerOpen{
        void openNavDrawer(boolean x);
    }
}
