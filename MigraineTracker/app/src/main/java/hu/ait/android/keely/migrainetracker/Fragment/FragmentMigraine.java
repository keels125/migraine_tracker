package hu.ait.android.keely.migrainetracker.Fragment;

import android.app.Activity;
import android.support.v4.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


import java.util.List;

import hu.ait.android.keely.migrainetracker.Data.CreateMigraineActivity;
import hu.ait.android.keely.migrainetracker.Data.Migraine;
import hu.ait.android.keely.migrainetracker.Adapter.MigraineAdapter;
import hu.ait.android.keely.migrainetracker.R;

/**
 *  Listfragment that handles creating, updating, and deleting migraine entries
 */
public class FragmentMigraine extends ListFragment {

    public static final int REQUEST_NEW_MIGRAINE_CODE = 100;
    public static final int REQUEST_EDIT_MIGRAINE_CODE = 101;
    public static final int CONTEXT_ACTION_DELETE = 10;
    public static final int CONTEXT_ACTION_EDIT = 11;

    private MigraineAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_migraine, container, false);

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        List<Migraine> migrainesList = Migraine.listAll(Migraine.class);
        adapter = new MigraineAdapter(activity, migrainesList);
        setListAdapter(adapter);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        registerForContextMenu(getListView());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (resultCode) {
            case Activity.RESULT_OK:
                if (requestCode == REQUEST_NEW_MIGRAINE_CODE) { //Add new Migraine to list
                    Migraine migraine = (Migraine) data.getSerializableExtra(
                            CreateMigraineActivity.KEY_MIGRAINE);
                    migraine.save();

                    adapter.addMigraine(migraine);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(getActivity(), "Migraine added to the list!", Toast.LENGTH_LONG).show();

                } else if (requestCode == REQUEST_EDIT_MIGRAINE_CODE) { //Edit existing Migraine in list
                    int index = data.getIntExtra(CreateMigraineActivity.KEY_EDIT_ID, -1);
                    if (index != -1) {
                        Migraine migraine = (Migraine) data.getSerializableExtra(
                                CreateMigraineActivity.KEY_MIGRAINE);
                        migraine.setId(adapter.getItem(index).getId());
                        migraine.save();

                        adapter.updateMigraine(index, (Migraine) data.getSerializableExtra(
                                CreateMigraineActivity.KEY_MIGRAINE));
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getActivity(), "Migraine updated", Toast.LENGTH_LONG).show();

                    }

                }
                break;

            case Activity.RESULT_CANCELED: //Cancel
                Toast.makeText(getActivity(), "Canceled", Toast.LENGTH_LONG).show();
                break;
        }
    }

    private void showCreateMigraineActivity() {
        Intent i = new Intent();
        i.setClass(getActivity(), CreateMigraineActivity.class);
        startActivityForResult(i, REQUEST_NEW_MIGRAINE_CODE);


    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.migraines_list, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_new_migraine) {
            showCreateMigraineActivity();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        menu.setHeaderTitle("Options");
        menu.add(0, CONTEXT_ACTION_DELETE, 0, "Delete");
        menu.add(0, CONTEXT_ACTION_EDIT, 0, "Edit");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == CONTEXT_ACTION_DELETE) { //delete item in list
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

            Migraine migraine = adapter.getItem(info.position);
            migraine.delete();
            adapter.removeItem(info.position);
            adapter.notifyDataSetChanged();


        }
        if (item.getItemId() == CONTEXT_ACTION_EDIT) { //edit item in list
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            Migraine selectedMigraine = adapter.getItem(info.position);
            Intent i = new Intent();
            i.setClass(getActivity(), CreateMigraineActivity.class);
            i.putExtra(CreateMigraineActivity.KEY_EDIT_MIGRAINE, selectedMigraine);
            i.putExtra(CreateMigraineActivity.KEY_EDIT_ID, info.position);
            startActivityForResult(i, REQUEST_EDIT_MIGRAINE_CODE);

        } else {
            return false;
        }
        return true;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

    }
}
