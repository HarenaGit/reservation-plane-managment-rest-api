package mg.ny.adminui.view_logics.public_component_view.horizentalList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import mg.ny.adminui.R;
import mg.ny.adminui.data_model.StaticHorizentalListModel;
import mg.ny.adminui.view_logics.public_component_view.horizentalList.HorizentalListInterface.LoadMore;
import mg.ny.adminui.view_logics.public_component_view.interfaces.HorizentalListCallBack;


class LoadingViewHolder extends RecyclerView.ViewHolder{

    public ProgressBar progressBar;
    public LoadingViewHolder(@NonNull View itemView) {
        super(itemView);
        progressBar = itemView.findViewById(R.id.smallLoader);
    }
}

public class StaticHorizentalListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0,VIEW_TYPE_LOADING = 1;
    LoadMore loadMore;
    boolean isLoading;
    Activity activity;
    private ArrayList<StaticHorizentalListModel> items;
    int visibleThreshold = 5;
    int lastVisibleItem, totalItemCount;

    private HorizentalListCallBack<RecyclerView.ViewHolder, Integer, Boolean, Integer> onClickCallback;
    int row_index = -1;
    Boolean isFirstClicked = true;

    public StaticHorizentalListAdapter(RecyclerView recyclerView,Activity activity, ArrayList<StaticHorizentalListModel> items,HorizentalListCallBack<RecyclerView.ViewHolder, Integer, Boolean, Integer> onClickCallback) {
        this.activity = activity;
        this.items = items;
        this.onClickCallback = onClickCallback;

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if(!isLoading && totalItemCount <= lastVisibleItem+visibleThreshold){
                    if(loadMore != null)
                        loadMore.onLoadMore();
                    isLoading = true;
                }
            }
        });
    }

    public void setItem(ArrayList<StaticHorizentalListModel> i){
        this.items = i;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position) == null ? VIEW_TYPE_LOADING:VIEW_TYPE_ITEM;
    }

    public void setLoadMore(LoadMore loadMore){
        this.loadMore = loadMore;
    }


    @NonNull
    @Override

    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       if(viewType == VIEW_TYPE_ITEM){
           View view = LayoutInflater.from(activity).inflate(R.layout.horizental_list_item, parent, false);
           return new ItemViewHolder(view);
       }
       else if (viewType == VIEW_TYPE_LOADING){
           View view = LayoutInflater.from(activity).inflate(R.layout.loader, parent, false);
           return new LoadingViewHolder(view);
       }
       return null;
}

    @Override

    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof ItemViewHolder){
            StaticHorizentalListModel currentItem = items.get(position);
            ItemViewHolder viewHolder = (ItemViewHolder) holder;
            viewHolder.text.setText(currentItem.getText());

            viewHolder.horizentalLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    row_index = position;
                    notifyDataSetChanged();
                    onClickCallback.apply(viewHolder, position, isFirstClicked);
                    isFirstClicked = false;
                }
            });
            if(row_index == position) {
                viewHolder.text.setBackgroundResource(R.drawable.horizental_selected_list_bg);
                viewHolder.text.setTextColor(viewHolder.text.getResources().getColor(R.color.white));
            }
            else{
                viewHolder.text.setBackgroundResource(R.drawable.horizental_list_bg);
                viewHolder.text.setTextColor(viewHolder.text.getResources().getColor(R.color.dark_grey));
            }
        }
        else if(holder instanceof LoadingViewHolder){
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setLoaded(){
        isLoading = false;
    }
    public void setIsFirstClicked(Boolean t){
        isFirstClicked = t;
    }
    public void setRow_index(int index){
        row_index = index;
    }
    public int getRow_index(){
        return row_index;
    }

}
