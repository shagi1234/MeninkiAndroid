package tm.store.meninki.adapter;

import static tm.store.meninki.utils.Const.mainFragmentManager;
import static tm.store.meninki.utils.FragmentHelper.addFragment;
import static tm.store.meninki.utils.StaticMethods.setBackgroundDrawable;
import static tm.store.meninki.utils.StaticMethods.setImageOrText;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.google.gson.JsonObject;
import com.r0adkll.slidr.model.SlidrInterface;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import tm.store.meninki.R;
import tm.store.meninki.api.data.DtoComment;
import tm.store.meninki.shared.Account;

public class AdapterComment extends RecyclerSwipeAdapter<AdapterComment.ViewHolder> {
    private Context context;
    private ArrayList<DtoComment> comments;
    private FragmentActivity activity;
    private SlidrInterface slidrInterface;
    private TextView commentCount;
    private RecyclerView rv;
    private int type;
    private Account accountPreferences;
    private boolean onSwipeOpen = false;

    public AdapterComment(Context context, ArrayList<DtoComment> data, FragmentActivity activity, TextView commentCount, RecyclerView rv) {
        this.context = context;
        this.comments = data;
        this.activity = activity;
        this.commentCount = commentCount;
        this.rv = rv;
        accountPreferences = Account.newInstance(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comment_post, parent, false);
        return new AdapterComment.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (comments != null) {
            holder.name.setText(comments.get(holder.getAdapterPosition()).getUserName());
            holder.comment.setText(comments.get(holder.getAdapterPosition()).getText());

            setBackgroundDrawable(context, holder.deleteCommentBtn, R.color.accent, 0, 0, true, 0);
            setBackgroundDrawable(context, holder.complainCommentBtn, R.color.contrast, 0, 0, true, 0);

            setImageOrText(
                    context,
                    comments.get(holder.getAdapterPosition()).getUserAvatar(),
                    comments.get(holder.getAdapterPosition()).getUserName(),
                    holder.avatar,
                    holder.avatarText,
                    true,
                    activity);

            SimpleDateFormat outputDf = new SimpleDateFormat("kk:mm' | 'dd.MM.yyyy");
            SimpleDateFormat inputDf = new SimpleDateFormat("yyyy-MM-dd'T'KK:mm:ss");
            inputDf.setTimeZone(TimeZone.getTimeZone("GMT"));

            String createdAt = comments.get(holder.getAdapterPosition()).getCreateDate();
            try {
                Date date = inputDf.parse(createdAt);
                if (date != null) holder.createdAt.setText(outputDf.format(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }


            if (comments.get(holder.getAdapterPosition()).getUserId().equals(accountPreferences.getPrefUserUUID())) {
                holder.deleteCommentBtn.setVisibility(View.VISIBLE);
                holder.complainCommentBtn.setVisibility(View.GONE);
            } else {
                holder.deleteCommentBtn.setVisibility(View.GONE);
                holder.complainCommentBtn.setVisibility(View.VISIBLE);
            }


            mItemManger.bindView(holder.itemView, holder.getAdapterPosition());

            holder.swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
                @Override
                public void onStartOpen(SwipeLayout layout) {
                    slidrInterface.lock();
                    mItemManger.closeAllExcept(layout);

                }

                @Override
                public void onOpen(SwipeLayout layout) {
                    onSwipeOpen = true;
                    slidrInterface.lock();

                }

                @Override
                public void onStartClose(SwipeLayout layout) {
                    onSwipeOpen = false;
                    slidrInterface.lock();

                }

                @Override
                public void onClose(SwipeLayout layout) {
                    slidrInterface.unlock();

                }

                @Override
                public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
                    slidrInterface.unlock();

                }

                @Override
                public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {

                }
            });

//            holder.deleteCommentBtn.setOnClickListener(v -> {
//                ViewDialog dialog = new ViewDialog();
//                dialog.showDialog(context);
//                if (context != null) {
//                    dialog.title.setText(context.getString(R.string.do_you_really_delete_comment));
//                }
//                dialog.yesBtn.setOnClickListener(v1 -> {
//                    deletePostComment(holder.getAdapterPosition(), holder);
//                    dialog.dialog.dismiss();
//                });
//                holder.swipeLayout.close();
//                mItemManger.closeAllItems();
//            });

//            holder.complainCommentBtn.setOnClickListener(v -> {
//                if (type == FragmentPostComments.TYPE_COMMENT_FEED) {
//                    addFragment(mainFragmentManager, R.id.fragment_container_main, FragmentComplain.newInstance(comments.get(holder.getAdapterPosition()).getUuid(), complainPostComment));
//                }
//                holder.swipeLayout.close();
//                mItemManger.closeAllItems();
//            });

        }
    }

    /*  private void deletePostComment(int position, ViewHolder holder) {
          holder.deleteCommentBtn.setEnabled(false);
          ApiFeedAndStory request = (ApiFeedAndStory) ApiClient.createRequest(ApiFeedAndStory.class);
          JsonObject jsonObject = new JsonObject();
          jsonObject.addProperty("bookId", comments.get(position).getUuid());
          jsonObject.addProperty("postId", feedPosts.getUUID());
          Call<Response> callUserInformation = request.deletePostComment(accountPreferences.getToken(), jsonObject);

          callUserInformation.enqueue(new RetrofitCallback<Response>() {
              @Override
              public void onResponse(Response response) {
                  if (response.isStatus()) {
                      feedPosts.setMyCommentCount(feedPosts.getMyCommentCount() - 1);
                      feedPosts.setCountComment(comments.size());
                      removeAt(position);

                      if (commentCount == null || activity == null) return;
                      commentCount.setText(comments.size() + " " + activity.getResources().getString(R.string.commentary));


                  } else {
                      holder.deleteCommentBtn.setEnabled(true);

                  }
              }

              @Override
              public void onFailure(Throwable t) {
                  holder.deleteCommentBtn.setEnabled(true);

              }
          });
      }
  */
    private void removeAt(int position) {
        try {
            comments.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, comments.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if (comments != null) {
            return comments.size();
        } else return 0;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe_layout;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView avatar;
        private TextView avatarText;
        private TextView name;
        private TextView comment;
        private TextView createdAt;
        private SwipeLayout swipeLayout;
        private FrameLayout deleteCommentBtn;
        private FrameLayout complainCommentBtn;
        private LinearLayout rightLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.avatar);
            avatarText = itemView.findViewById(R.id.avatar_txt);
            name = itemView.findViewById(R.id.name);
            comment = itemView.findViewById(R.id.comment);
            createdAt = itemView.findViewById(R.id.created_at);
            swipeLayout = itemView.findViewById(R.id.swipe_layout);
            deleteCommentBtn = itemView.findViewById(R.id.delete_swipe_img);
            complainCommentBtn = itemView.findViewById(R.id.complain_btn);
            rightLayout = itemView.findViewById(R.id.right_layout);
        }
    }
}
