package mk.ukim.finki.mpip.listviewshowcase.app.itemListeners;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ActionMenuView;
import mk.ukim.finki.mpip.listviewshowcase.app.CustomAdapter;
import mk.ukim.finki.mpip.listviewshowcase.app.R;
import mk.ukim.finki.mpip.listviewshowcase.app.services.DownloadPersonsService;

/**
 * Created by ristes on 11/25/15.
 */
public class SearchItemListener {

  Context context;

  SearchView searchView;

  CustomAdapter customAdapter;

  public SearchItemListener(Context context, CustomAdapter customAdapter) {
    this.context = context;
    this.customAdapter = customAdapter;
  }

  public void initializeMenu(Menu menu) {
    MenuItem item = menu.findItem(R.id.action_load);

    item.setOnMenuItemClickListener(new ReloadClickListener());


    final SharedPreferences preferences = context.getSharedPreferences("ReloadPreferences",
      Activity.MODE_PRIVATE);

    MenuItem activateDeactivateReload = menu.findItem(R.id.activate_deactivate_reload);
    MenuItem reload10 = menu.findItem(R.id.reload_10);
    MenuItem reload30 = menu.findItem(R.id.reload_30);

    activateDeactivateReload.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
      @Override
      public boolean onMenuItemClick(MenuItem item) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("activeReload",
          !preferences.getBoolean("activeReload", true));

        editor.commit();
        return false;
      }
    });

    reload10.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
      @Override
      public boolean onMenuItemClick(MenuItem item) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("reloadInterval", 10);
        editor.commit();
        return false;
      }
    });


    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
      MenuItem searchItem = menu.findItem(R.id.action_search);
      searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
      searchView.setOnCloseListener(new SearchView.OnCloseListener() {
        @Override
        public boolean onClose() {
          customAdapter.search(null);
          return false;
        }
      });
      searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String s) {
          customAdapter.search(s);
          return false;
        }

        @Override
        public boolean onQueryTextChange(String s) {
          customAdapter.search(s);
          return true;
        }
      });
    }
  }


  public class ReloadClickListener
    implements ActionMenuView.OnMenuItemClickListener, MenuItem.OnMenuItemClickListener {

    @Override
    public boolean onMenuItemClick(MenuItem item) {
      context.startService(new Intent(context, DownloadPersonsService.class));
      return false;
    }
  }
}