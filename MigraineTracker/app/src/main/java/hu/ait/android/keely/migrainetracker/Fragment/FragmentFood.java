package hu.ait.android.keely.migrainetracker.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
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

import hu.ait.android.keely.migrainetracker.Adapter.FoodAdapter;

import hu.ait.android.keely.migrainetracker.Data.CreateFoodActivity;

import hu.ait.android.keely.migrainetracker.Data.Food;

import hu.ait.android.keely.migrainetracker.R;

/**
 * Listfragment that handles creating, updating, and deleting food entries
 */
public class FragmentFood extends ListFragment {

    public static final int REQUEST_NEW_FOOD_CODE = 100;
    public static final int REQUEST_EDIT_FOOD_CODE = 101;
    public static final int CONTEXT_ACTION_DELETE = 10;
    public static final int CONTEXT_ACTION_EDIT = 11;

    private ListView listView;
    private FoodAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_food, container, false);

        return rootView;

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        List<Food> foodsList = Food.listAll(Food.class);
        adapter = new FoodAdapter(activity, foodsList);
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
                if (requestCode == REQUEST_NEW_FOOD_CODE) { //Adding food to list
                    Food food = (Food) data.getSerializableExtra(
                            getString(R.string.KEY_FOOD));
                    food.save();

                    adapter.addFood(food);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(getActivity(), "Food added to the list!", Toast.LENGTH_LONG).show();

                } else if (requestCode == REQUEST_EDIT_FOOD_CODE) { //Updating food already in list
                    int index = data.getIntExtra(getString(R.string.KEY_EDIT_ID), -1);
                    if (index != -1) {
                        Food food = (Food) data.getSerializableExtra(
                                getString(R.string.KEY_FOOD));
                        food.setId(adapter.getItem(index).getId());
                        food.save();

                        adapter.updateFood(index, (Food) data.getSerializableExtra(
                                getString(R.string.KEY_FOOD)));
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getActivity(), "Food updated", Toast.LENGTH_LONG).show();

                    }

                }
                break;

            case Activity.RESULT_CANCELED: //Cancelled
                Toast.makeText(getActivity(), "Canceled", Toast.LENGTH_LONG).show();
                break;
        }
    }

    private void showCreateFoodActivity() {
        Intent i = new Intent();
        i.setClass(getActivity(), CreateFoodActivity.class);
        startActivityForResult(i, REQUEST_NEW_FOOD_CODE);

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.foods_list, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_new_food) {
            showCreateFoodActivity();
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
        if (item.getItemId() == CONTEXT_ACTION_DELETE) { //Delete food in the list
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

            Food food = adapter.getItem(info.position);
            food.delete();
            adapter.removeItem(info.position);
            adapter.notifyDataSetChanged();

        }
        if (item.getItemId() == CONTEXT_ACTION_EDIT) { //Edit food in the list

            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            Food selectedFood = adapter.getItem(info.position);
            Intent i = new Intent();
            i.setClass(getActivity(), CreateFoodActivity.class);
            i.putExtra(getString(R.string.KEY_EDIT_FOOD), selectedFood);
            i.putExtra(getString(R.string.KEY_EDIT_ID), info.position);
            startActivityForResult(i, REQUEST_EDIT_FOOD_CODE);

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
