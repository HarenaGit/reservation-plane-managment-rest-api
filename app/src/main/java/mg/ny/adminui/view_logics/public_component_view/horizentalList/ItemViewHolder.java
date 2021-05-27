package mg.ny.adminui.view_logics.public_component_view.horizentalList;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import mg.ny.adminui.R;

public class ItemViewHolder extends  RecyclerView.ViewHolder{
    public TextView text;
    public RelativeLayout horizentalLayout;
    public ItemViewHolder(@NonNull View itemView) {
        super(itemView);
        text = itemView.findViewById(R.id.horizentalTextItem);
        horizentalLayout = itemView.findViewById(R.id.horizentalLayout);
    }
}
