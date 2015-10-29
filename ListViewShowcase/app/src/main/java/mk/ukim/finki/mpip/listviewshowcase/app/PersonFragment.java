package mk.ukim.finki.mpip.listviewshowcase.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by ristes on 10/28/15.
 */
public class PersonFragment extends Fragment {


  private EditText name;
  private EditText lastName;
  private Button addButton;
  private Button removeButton;

  private Person editEntity;
  private Integer position;

  private CustomAdapter adapter;
  private Button clearButton;


  public static PersonFragment create(CustomAdapter adapter, Person editEntity, Integer position) {
    PersonFragment fragment = new PersonFragment();
    fragment.adapter = adapter;
    fragment.editEntity = editEntity;
    fragment.position = position;
    return fragment;
  }


  @Override
  public void onInflate(Activity activity, AttributeSet attrs, Bundle savedInstanceState) {
    super.onInflate(activity, attrs, savedInstanceState);
    Toast.makeText(activity, "onInflate", Toast.LENGTH_SHORT).show();
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    Toast.makeText(activity, "onAttach", Toast.LENGTH_SHORT).show();
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Toast.makeText(getActivity(), "onCreate", Toast.LENGTH_SHORT).show();
  }

  @Override
  public View onCreateView(LayoutInflater inflater,
                           ViewGroup container,
                           Bundle savedInstanceState) {
    Toast.makeText(getActivity(), "onCreateView", Toast.LENGTH_SHORT).show();
    return inflater.inflate(R.layout.fragment_edit_person, null);
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    Toast.makeText(getActivity(), "onViewCreated", Toast.LENGTH_SHORT).show();
    doInject(view);
  }

  private void doInject(View view) {
    name = (EditText) view.findViewById(R.id.name);
    lastName = (EditText) view.findViewById(R.id.last_name);
    addButton = (Button) view.findViewById(R.id.add_person);
    removeButton = (Button) view.findViewById(R.id.delete_person);
    clearButton = (Button) view.findViewById(R.id.clear_person);

    clearButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        editEntity = null;
        name.setText("");
        lastName.setText("");
        removeButton.setVisibility(View.GONE);
      }
    });
    if (editEntity != null) {
      name.setText(editEntity.name);
      lastName.setText(editEntity.lastName);
      removeButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
              }
            }).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                adapter.delete(position);
              }
            })
            .setTitle(R.string.delete_prompt)
            .setMessage(R.string.delete_message);

          builder.create().show();

        }
      });
    } else {
      removeButton.setVisibility(View.GONE);
    }

    addButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        Person person = editEntity;
        if (editEntity == null) {
          person = new Person();
        }
        person.name = name.getText().toString();
        person.lastName = lastName.getText().toString();
        if (editEntity != null) {
          adapter.notifyDataSetChanged();
        } else {
          adapter.add(person);
        }
      }
    });
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    Toast.makeText(getActivity(), "onActivityCreated", Toast.LENGTH_SHORT).show();
  }

  @Override
  public void onStart() {
    super.onStart();
    Toast.makeText(getActivity(), "onStart", Toast.LENGTH_SHORT).show();
  }

  @Override
  public void onResume() {
    super.onResume();
    Toast.makeText(getActivity(), "onResume", Toast.LENGTH_SHORT).show();
  }

  @Override
  public void onPause() {
    super.onPause();
    Toast.makeText(getActivity(), "onPause", Toast.LENGTH_SHORT).show();
  }

  @Override
  public void onStop() {
    super.onStop();
    Toast.makeText(getActivity(), "onStop", Toast.LENGTH_SHORT).show();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    Toast.makeText(getActivity(), "onDestroyView", Toast.LENGTH_SHORT).show();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    Toast.makeText(getActivity(), "onDestroy", Toast.LENGTH_SHORT).show();
  }

  @Override
  public void onDetach() {
    super.onDetach();
    Toast.makeText(getActivity(), "onDetach", Toast.LENGTH_SHORT).show();
  }
}
