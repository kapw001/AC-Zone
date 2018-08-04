package com.digitfellas.ac.dashboard.salesman;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.digitfellas.ac.R;
import com.digitfellas.ac.base.BaseAdapter;
import com.digitfellas.ac.base.VH;
import com.digitfellas.ac.data.model.response.TaskAdd;
import com.digitfellas.ac.utility.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TaskPerformedAdapter extends BaseAdapter<TaskAdd.Data.Perform, TaskPerformedAdapter.TaskPerformedVH> {


    protected TaskPerformedAdapter(List<TaskAdd.Data.Perform> list) {
        super(list);
    }

    @NonNull
    @Override
    public TaskPerformedVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_task_performed, parent, false);

        return new TaskPerformedVH(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final TaskPerformedVH holder, final int position) {

        final TaskAdd.Data.Perform anouncement = getValue(position);


        holder.task.setText(anouncement.getPerform());

        if (anouncement.isSelected()) {
//            holder.task.setChecked(true);
            holder.taskDescription.setVisibility(View.VISIBLE);
        } else {
//            holder.task.setChecked(false);
            holder.taskDescription.setVisibility(View.GONE);
        }


        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

//                holder.task.setChecked(isChecked);
                anouncement.setSelected(isChecked);
//                notifyItemChanged(position);
//                notifyDataSetChanged();

                if (anouncement.isSelected()) {
//            holder.task.setChecked(true);
                    holder.taskDescription.setVisibility(View.VISIBLE);
                } else {
//            holder.task.setChecked(false);
                    holder.taskDescription.setVisibility(View.GONE);
                }
            }
        });


        holder.taskDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() > 0) {

                    anouncement.setDescription(s.toString());
//                    notifyItemChanged(position);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return getCount();
    }

    public class TaskPerformedVH extends VH {

        @BindView(R.id.task)
        CheckBox task;
        @BindView(R.id.task_description)
        AppCompatEditText taskDescription;


        public TaskPerformedVH(View itemView) {
            super(itemView);
        }
    }


}
