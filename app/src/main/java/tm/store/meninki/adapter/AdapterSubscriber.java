package tm.store.meninki.adapter;

import static tm.store.meninki.api.Network.BASE_URL;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import tm.store.meninki.api.data.UserProfile;
import tm.store.meninki.databinding.ItemSubscriberBinding;

public class AdapterSubscriber extends RecyclerView.Adapter<AdapterSubscriber.SubscriberHolder> {
    private ArrayList<UserProfile> users = new ArrayList<>();
    private Context context;

    public AdapterSubscriber(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterSubscriber.SubscriberHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemSubscriberBinding itemSubscriberBinding = ItemSubscriberBinding.inflate(layoutInflater, parent, false);
        return new SubscriberHolder(itemSubscriberBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterSubscriber.SubscriberHolder holder, int position) {
        holder.bind();
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class SubscriberHolder extends RecyclerView.ViewHolder {
        ItemSubscriberBinding b;

        public SubscriberHolder(@NonNull ItemSubscriberBinding itemView) {
            super(itemView.getRoot());
            this.b = itemView;
        }

        public void bind() {
            Glide.with(context)
                    .load(BASE_URL + users.get(getAdapterPosition()).getImgPath())
                    .into(b.friendImg);
            b.name.setText(users.get(getAdapterPosition()).getName());
        }
    }

    public void setUsers(ArrayList<UserProfile> users) {
        this.users = users;
        notifyDataSetChanged();
    }
}
