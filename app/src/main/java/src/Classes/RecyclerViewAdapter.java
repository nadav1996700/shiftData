package src.Classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.src.R;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

import src.Utils.My_images;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private ArrayList<dataItem> data;
    private LayoutInflater mInflater;
    private Context context;
    My_images images = My_images.getInstance();

    // data is passed into the constructor
    public RecyclerViewAdapter(Context context, ArrayList<dataItem> data) {
        this.mInflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
    }

    // inflates the row layout from xml when needed
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.custom_rawitem, parent, false);
        return new ViewHolder(view);
    }

    // binds the data in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        dataItem dataItem = data.get(position);
        // set full name
        String name = dataItem.getFirst_name() + " " + dataItem.getLast_name();
        holder.TV_name.setText(name);
        // set image
        String path = "gs://shiftdata-a19a0.appspot.com/workers_images/" + dataItem.getId();
        ShapeableImageView imageView = holder.imageView;
        images.downloadImageUrl(path, imageView);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return data.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView TV_name;
        ShapeableImageView imageView;
        int place_holder;

        ViewHolder(View itemView) {
            super(itemView);
            TV_name = itemView.findViewById(R.id.raw_LBL_details);
            imageView = itemView.findViewById(R.id.raw_IMG_photo);
            place_holder = R.id.raw_IMG_photo;
        }
    }
}
