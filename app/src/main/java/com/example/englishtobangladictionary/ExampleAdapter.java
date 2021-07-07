package com.example.englishtobangladictionary;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder> implements Filterable {

    private ArrayList<ExampleItem>mExamplelist;
    private  ArrayList<ExampleItem>mExamplelistfull;

    public  static  class ExampleViewHolder extends RecyclerView.ViewHolder{

        public TextView english_word,bangla_worrd;

        public ExampleViewHolder(@NonNull View itemView) {
            super(itemView);

            english_word=itemView.findViewById(R.id.english_text_view);
            bangla_worrd=itemView.findViewById(R.id.bangla_text_view);
        }
    }

    public ExampleAdapter(ArrayList<ExampleItem>example_list){

        mExamplelist=example_list;
        mExamplelistfull=new ArrayList<>(example_list);


    }
    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout,parent,false);
        ExampleViewHolder evh=new ExampleViewHolder(v);
        return  evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {

        ExampleItem current_item=mExamplelist.get(position);

        holder.english_word.setText(current_item.getEnglish_word());
        holder.bangla_worrd.setText(current_item.getBangla_word());

    }

    @Override
    public int getItemCount() {
        return mExamplelist.size();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private  Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<ExampleItem> filteredList = new ArrayList<>();

            if(constraint == null ||constraint.length()==0)
            {
                filteredList.addAll(mExamplelistfull);
            }
            else
            {
                String filter_pattern = constraint.toString().toLowerCase().trim();

                for (ExampleItem item: mExamplelistfull)
                {
                    if(item.getEnglish_word().toLowerCase().startsWith(filter_pattern))
                    {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values= filteredList;
            return  results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            mExamplelist.clear();
            mExamplelist.addAll((List)results.values);
            notifyDataSetChanged();

        }
    };
}
