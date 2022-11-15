package yvesbenabou.github.example.usingfragmentsforresponsivelayout;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
    implements ListFragment.OnItemClickListener {

  // Track whether to display a two-pane or single-pane UI
  // A single-pane display refers to phone screens, and two-pane to larger tablet screens
  private boolean mTwoPane;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    mTwoPane = getResources().getBoolean(R.bool.twoPane);
    // Determine if you're creating a two-pane or single-pane display
    if (mTwoPane)  {// findViewById(R.id.tab_layout_root_view) != null) {
      // This LinearLayout will only initially exist in the two-pane tablet case

      ListFragment listFragment = new ListFragment();
      listFragment.setDatalist(DataListUtils.getDatalist());

      DetailFragment detailFragment = new DetailFragment();
      detailFragment.setIndex(0);
      detailFragment.setDatalist(DataListUtils.getDatalist());

      getSupportFragmentManager().beginTransaction()
          .add(R.id.firstpane_placeholder, listFragment)
          .add(R.id.secondpane_placeholder, detailFragment)
          .commit();
    }
    else {
      // We're in single-pane mode and displaying fragments on a phone in separate activities
      ListFragment listFragment = new ListFragment();
      listFragment.setDatalist(DataListUtils.getDatalist());
      getSupportFragmentManager().beginTransaction()
          .add(R.id.firstpane_placeholder, listFragment)
          .commit();
    }
  }

  @Override // for nested class ListFragment.onItemClickListener
  public void onItemSelected(int position) {
    // Create a Toast that displays the position that was clicked
    Toast.makeText(this, "Position clicked = " + position, Toast.LENGTH_SHORT).show();

    // Handle the two-pane case and replace existing fragments right when a new image is selected from the master list
    if (mTwoPane) {
      // Create two-pane interaction

      // Replace the current fragment with a new one
      DetailFragment detailFragment = new DetailFragment();
      detailFragment.setIndex(position);
      detailFragment.setDatalist(DataListUtils.getDatalist());

      getSupportFragmentManager().beginTransaction()
          .replace(R.id.secondpane_placeholder, detailFragment)
          .commit();
    }
    else {

      // Handle the single-pane phone case by passing information in a Bundle attached to an Intent

      Bundle b = new Bundle();
      b.putInt("index", position);

      // Attach the Bundle to an intent
      final Intent intent = new Intent(this, DetailActivity.class);
      intent.putExtras(b);
      startActivity(intent);
    }
  }
}
