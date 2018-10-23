package com.example.android.mytranslation;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by android on 1/9/18.
 */

public class CustomRecyclerAdapter extends RecyclerView.Adapter<CustomRecyclerAdapter.CustomViewHolder> {
    private Context context;

    ArrayList<TranslationFav> transListView;
    SqlLiteHelper sqlLiteHelper;


    public CustomRecyclerAdapter(Context context, ArrayList<TranslationFav> mTranslationView) {
        this.context=context;
        this.transListView=mTranslationView;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.row_adapter, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder,final int position) {
        final int pos = position;
        holder.save_input_text.setText(transListView.get(position).getLanguageInputText());
        holder.source_lang_row.setText(transListView.get(position).getLanguageInputName());
        holder.save_output_text.setText(transListView.get(position).getTanslationOutputText());
        holder.dest_lang_row.setText(transListView.get(position).getLanguageOutputName());
        holder.delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFromList(v,pos, transListView.get(position).getTanslationOutputText());


            }
        });
    }



    @Override
    public int getItemCount() {

        return transListView.size();
    }

    private void deleteFromList(final View v, final int pos, final String outputResult) {
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

        //builder.setTitle("Dlete ");
        builder.setMessage("Delete Item ?")
                .setCancelable(false)
                .setPositiveButton("CONFIRM",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                sqlLiteHelper = new SqlLiteHelper(v.getContext());
                                //TranslationFav p =transListView.get(pos);
                                //String textTranslated = p.getTanslationOutputText();
                                sqlLiteHelper.dataDelete(outputResult);
                                transListView.remove(pos);






                                //sqlLiteHelper.dataDelete(ar);
                                notifyItemRemoved(pos);
                                notifyDataSetChanged();


                            }
                        })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {


                    }
                });

        builder.show();

    }


    public class CustomViewHolder extends RecyclerView.ViewHolder {
        private TextView save_input_text;
        private TextView source_lang_row;
        private TextView save_output_text;
        private TextView dest_lang_row;
        private ImageView delete_button;
        public CustomViewHolder(View itemView) {
            super(itemView);
            save_input_text=(TextView) itemView.findViewById(R.id.save_input_text);
            source_lang_row = (TextView) itemView.findViewById(R.id.source_lang_row);
            save_output_text = (TextView) itemView.findViewById(R.id.save_output_text);
            dest_lang_row = (TextView) itemView.findViewById(R.id.dest_lang_row);
            delete_button = (ImageView) itemView.findViewById(R.id.delete_button);

        }
    }
}
